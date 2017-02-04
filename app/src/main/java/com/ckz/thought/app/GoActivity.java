package com.ckz.thought.app;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ckz.thought.R;
import com.ckz.thought.utils.MusicUtils;
import com.ckz.thought.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kaiser on 2015/10/26.
 * 版权@陈科肇
 * 菜单一
 * 算法基础，对于算术公式的默算能力提升
 *  大体功能介绍：
 *          这是一个锻炼默算能力的功能，大体上，按钮对应数字，按钮的颜色和数字颜色一致，则表示，该按
 *          钮代表该数字。采用的主要是线性布局...
 *  1.创建公式-开始时、答对时、答错时、超时时等场景时，创建公式并显示；
 *  2.可以查看结果-按下？图标即可看结果，弹起则隐藏掉结果；
 *  3.按钮是随机布局的，这便于打散按钮的布局；
 *  4.每点下一个按钮，就记录下按钮颜色对应数字颜色的值为一个输入结果；
 *  5.数字图片是一张图片，其中通过改变其图片的像素点颜色，来达到可随机分配每个数字有不同的颜色；
 *  6.在操作的过程，记录下一些操作结果；
 *  7.在答对、答错、超时等情况下，都会对布局进行重新洗牌（公式创建、操作面板按钮随机布置、数字图片中的数字颜色随机分配）；
 *  8.每个操作，有或没有地都有对应着一个音效；
 *  9.设置了第一次按下按钮时，记录时间，规定不能超过设置的时间；
 *  10.
 *  0这个数字有点特别，在公式的结果为0时，整体的数字图片就表示一个按钮，这个按钮的值就是0。
 *  如果公式的结果不为0时，第一次按下按钮，记录第一个值时，这个整体的数字图片不表示为一个按钮，
 *  即不为0，但在第二次按下这个数字图片按钮时，这个数字图片按钮就表示为0；
 *  11.其中有对大图片(Bitmap)进行压缩处理，使其占内存变小，以便提高程序性能；
 */
public class GoActivity extends AppCompatActivity{

    private Resources res;
    //位图处理工具类
    private BitmapUtils bitmapUtils;
    //音效播放服务
    private MusicUtils musicUtils;
    private final String TAG = GoActivity.class.getSimpleName();
    private int[][] btns;//九宫格按钮资源ID集合
    private int[][] drawId;//按钮资源ID
    private int[][] drawId_1;
    private int[][] btnColors;//按钮颜色集合
    private int temp = 0;//记录九宫格按钮按下的位置
    private ImageView btn;
    private ImageView imageView;
    private TextView tvFormula;//显示公式的文本框
    private Bitmap bitmap;//九宫格数字位图
    //随机公式创建
    private String[] operations=new String[]{"+","-","×"};
    private int numberOne = 0;
    private int numberTwo = 0;
    private int result = 0;
    private int numMax = 20;
    private List<Integer> btnClickResult;//保存输入的结果值
    //private static int resultLength=5;//保存记录结果的最大长度
    private TextView recordInput;//记录输入的结果
    private TextView tvShowHelp;//显示结果帮助
    private int score =0;//分数记录
    private int count = 0;//操作次数
    private TextView app_go_score;//获取分数记录控件
    private TextView app_go_count;//获取操作次数控件
    private int setTimeOut = 5;//设置超时时间，单位秒(S)
    private int timeOut = 0;
    private TextView app_go_timeOut;//获取超时显示控件
    //提醒任务
    private Timer timer;
    private final int TIMEOUT =1;

    //数字0按钮
    private ImageView zeroBtn;

    private Bitmap[] bitmaps;//记录下bitmap，便于回收

    //消息处理机制
    private final Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TIMEOUT:
                    //游戏超时
                    musicUtils.gameOverMusic(GoActivity.this);
                    Toast.makeText(GoActivity.this, "NoNoNo ＠︿＠ 超时 "+setTimeOut+" 秒", Toast.LENGTH_SHORT).show();
                    timeOut++;
                    count++;
                    score--;
                    app_go_count.setText("次数：" + count);
                    app_go_score.setText("分数："+score);
                    app_go_timeOut.setText("超时："+timeOut+" 次");
                    //创建运算公式
                    String formula = createFormula();
                    tvFormula.setText(formula);
                    //清空记录
                    recordInput.setText("");
                    //洗牌所有
                    shuffleAll();
                    //清空记录结果
                    btnClickResult=null;//设置为null，让垃圾回收机制回收
                    break;
                default:
                    break;
            }
        }
    };

    public  GoActivity(){
        super();
    }


    /**
     * 提醒任务
     */
    class RemindTask extends TimerTask{
        @Override
        public void run() {
            clearTimeout(); //Terminate the timer thread
            Message message = new Message();
            message.what = TIMEOUT;
            myHandler.sendMessage(message);
        }

    }

    /**
     * 设置计时
     * @param seconds
     */
    public void setTimeout(int seconds) {
        if(timer==null){
            timer = new Timer();
            timer.schedule(new RemindTask(), seconds*1000);
        }

    }

    /**
     * 取消计时器任务
     */
    public void clearTimeout(){
        if(timer!=null){
            timer.cancel();
            timer.purge();
            timer=null;
        }
    }


    /**
     * 监听按钮的单击事件
     */
    private class MyBtnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tvFormula :
                    //游戏通关音效
                    musicUtils.gameNextMusic(GoActivity.this);
                    //创建运算公式
                    String formula = createFormula();
                    tvFormula.setText(formula);
                    //单击按钮的结果
                    btnClickResult=null;//设置为null，让垃圾回收机制回收
                    //清空记录
                    recordInput.setText("");
                    Toast.makeText(GoActivity.this, "清空了记录的结果！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 按钮的监听按下和弹起事件
     */
    private class MyBtnTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //按下
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                switch (v.getId()){
                    case R.id.textNumber://数字0按钮事件处理
                        //保存单击的数字，不为空，0生效
                        if(btnClickResult!=null){
                            //按钮音效
                            musicUtils.gameBtnMusic(GoActivity.this);
                            //时间器-开始计时
                            setTimeout(setTimeOut);
                            btnClickResult.add(0);//记录0
                            //判断答案与结果是否一致
                            resultEquals();
                        }else if(result==0){
                            btnClickResult = new ArrayList<Integer>();
                            btnClickResult.add(0);//记录0
                            //判断答案与结果是否一致
                            resultEquals();
                        }
                        break;
                    case R.id.tvFormula ://显示公式文本框事件
                        tvFormula.setBackground(null);
                        break;
                    case R.id.tvShowHelp ://显示结果帮助事件
                        //?音效
                        musicUtils.gameQuestionMusic(GoActivity.this);
                        String tempResult = String.valueOf(result);
                        if(tempResult.length()>2){
                            tempResult = "...";
                        }
                        tvShowHelp.setText(tempResult);
                        tvShowHelp.setBackground(null);
                        Toast.makeText(GoActivity.this, "◎＿◎ 你想干嘛", Toast.LENGTH_SHORT).show();
                        break;
                    default://九宫格按钮事件

                        //时间器-开始计时
                        setTimeout(setTimeOut);

                        //按钮音效
                        musicUtils.gameBtnMusic(GoActivity.this);
                        int id = v.getId();
                        int length = btns.length;
                        for(int i=0;i<length;i++){
                            btn = (ImageView) findViewById(btns[i][1]);
                            if(btn.getId()==id){
                                //对应事件
                                temp=i;
                                //btn.setBackground(getResources().getDrawable(drawId_1[i][1]));
                                btn.setImageBitmap(bitmapUtils.compressBitmapFromResource(res,drawId_1[i][1],btn));
                                //找到对应的数字
                                int num = drawId[i][0];
                                int flag = 0;
                                int btnsLength = btnColors.length;
                                for(int j=0;j<btnsLength;j++){
                                    if(num==btnColors[j][0]){
                                        //记录下对应的数字
                                        flag = j;
                                        break;
                                    }
                                }
                                //Toast.makeText(GoActivity.this, "当前按钮代表数字为："+(flag+1), Toast.LENGTH_SHORT).show();
                                //保存单击的数字
                                if(btnClickResult==null){
                                    btnClickResult = new ArrayList<Integer>();
                                }
                                btnClickResult.add(flag+1);

                                //判断答案与结果是否一致
                                resultEquals();

                                break;
                            }
                        }
                        break;
                }
            }
            //弹起
            if(event.getAction() == MotionEvent.ACTION_UP){
                switch (v.getId()) {
                    case R.id.tvFormula://显示公式文本框事件
                        tvFormula.setBackground(getResources().getDrawable(R.drawable.tv_dialog));
                        tvShowHelp.setText("");
                        tvShowHelp.setBackground(getResources().getDrawable(R.drawable.question));
                        break;
                    case R.id.tvShowHelp://显示结果帮助事件
                        tvShowHelp.setText("");
                        tvShowHelp.setBackground(getResources().getDrawable(R.drawable.question));
                        break;
                    default://九宫格按钮事件
                        //btn.setBackground(getResources().getDrawable(drawId[temp][1]));
                        btn.setImageBitmap(bitmapUtils.compressBitmapFromResource(res,drawId[temp][1],btn));
                        break;
                }
            }
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go);

        res = getResources();
        //activity创建背景音效
        //gameBackMusic();
        //初始化音效工具类
        musicUtils = new MusicUtils();
        //初始化工具类
        bitmapUtils=new BitmapUtils();

        //数字0事件
        zeroBtn = (ImageView) findViewById(R.id.textNumber);
        //初始化资源
        app_go_score = (TextView) findViewById(R.id.app_go_score);
        app_go_count = (TextView) findViewById(R.id.app_go_count);
        app_go_timeOut = (TextView) findViewById(R.id.app_go_timeOut);
        //显示记录数据
        app_go_count.setText("次数："+count);
        app_go_score.setText("分数："+score);
        app_go_timeOut.setText("超时："+timeOut+" 次");
        tvShowHelp = (TextView) findViewById(R.id.tvShowHelp);
        recordInput = (TextView) findViewById(R.id.recordInput);
        imageView = (ImageView) findViewById(R.id.textNumber);
        tvFormula  = (TextView) findViewById(R.id.tvFormula);
        //获取按钮对应的图片资源ID
        drawId =new int[][]{
                {1,R.drawable.btn_gray},
                {2,R.drawable.btn_red},
                {3,R.drawable.btn_green},
                {4,R.drawable.btn_khaki},
                {5,R.drawable.btn_violet},
                {6,R.drawable.btn_blue_green},
                {7,R.drawable.btn_white},
                {8,R.drawable.btn_yellow},
                {9,R.drawable.btn_watchet}
        };
        drawId_1 =new int[][]{
                {1,R.drawable.btn_gray_1},
                {2,R.drawable.btn_red_1},
                {3,R.drawable.btn_green_1},
                {4,R.drawable.btn_khaki_1},
                {5,R.drawable.btn_violet_1},
                {6,R.drawable.btn_blue_green_1},
                {7,R.drawable.btn_white_1},
                {8,R.drawable.btn_yellow_1},
                {9,R.drawable.btn_watchet_1}
        };
        //获取按钮对应的资源ID
        btns = new int[][]{
                {1,R.id.btnI_1},
                {2,R.id.btnI_2},
                {3,R.id.btnI_3},
                {4,R.id.btnI_4},
                {5,R.id.btnI_5},
                {6,R.id.btnI_6},
                {7,R.id.btnI_7},
                {8,R.id.btnI_8},
                {9,R.id.btnI_9}
        };

        //app_go_number
        bitmap = bitmapUtils.getMutableBitmap(getResources(),R.drawable.app_go_number);
        //九宫格按钮颜色数组
        btnColors = new int[][]{
                {1,getResources().getColor(R.color.btn_go_one)},
                {2,getResources().getColor(R.color.btn_go_two)},
                {3,getResources().getColor(R.color.btn_go_three)},
                {4,getResources().getColor(R.color.btn_go_four)},
                {5,getResources().getColor(R.color.btn_go_five)},
                {6,getResources().getColor(R.color.btn_go_six)},
                {7,getResources().getColor(R.color.btn_go_seven)},
                {8,getResources().getColor(R.color.btn_go_eight)},
                {9,getResources().getColor(R.color.btn_go_nine)}/*,
                {0,getResources().getColor(R.color.btn_go_zero)}*/
        };

        //注册公式文本框事件
        tvFormula.setOnClickListener(new MyBtnClickListener());
        tvFormula.setOnTouchListener(new MyBtnTouchListener());
        //帮助help事件注册，XML添加android:clickable="true"
        tvShowHelp.setOnTouchListener(new MyBtnTouchListener());

        //注册0的处理事件
        zeroBtn.setOnTouchListener(new MyBtnTouchListener());

        //注册按钮监听事件
        int length = btns.length;
        for(int i=0;i<length;i++){
            findViewById(btns[i][1]).setOnTouchListener(new MyBtnTouchListener());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        shuffleAll();
        //创建运算公式
        String formula = createFormula();
        tvFormula.setText(formula);
    }

    /**
     * 洗牌所有，数字颜色及按钮
     */
    private void shuffleAll(){
        //打乱颜色及显示
        shuffleNum(btnColors, bitmap, imageView);
        //打乱按钮显示顺序
        shuffleBtn(btns, drawId);
    }

    /**
     * 打乱按钮显示顺序
     * @param btns  九宫格按钮资源ID集合
     * @param drawId 按钮图片资源ID集合
     */
    private void shuffleBtn(int[][] btns,int[][] drawId){

        //随机排序按钮图片资源List
        List<int[]> list = Arrays.asList(drawId);
        Collections.shuffle(list);
        //同步drawId_1
        int size = list.size();
        int length_ = drawId_1.length;
        for(int i=0;i<size;i++){
            int temp = list.get(i)[0];
            for(int j=i;j<length_;j++){
                if(temp==drawId_1[j][0]){
                    int[] temp2 = drawId_1[i];
                    drawId_1[i]=drawId_1[j];
                    drawId_1[j]=temp2;
                    break;
                }
            }
        }

        int length = btns.length;
        bitmaps = new Bitmap[length];
        for(int i=0;i<length;i++){
            ImageView btn = (ImageView) findViewById(btns[i][1]);
            //btn.setBackground(getResources().getDrawable(list.get(i)[1]));
            bitmaps[i]=bitmapUtils.compressBitmapFromResource(res,list.get(i)[1],btn);
            btn.setImageBitmap(bitmaps[i]);
        }
    }

    /**
     * 打乱数字颜色显示顺序
     * @param btnColors 定义好的按钮颜色数组
     * @param bitmap 要处理的图片对象
     * @param imageView 要显示处理后的图片的ImageView
     */
    private void shuffleNum(int[][] btnColors,Bitmap bitmap,ImageView imageView){

        //打乱数字颜色显示顺序
        List<int[]> list = Arrays.asList(btnColors);
        Collections.shuffle(list);

        //显示颜色
        int bitmapW = bitmap.getWidth();
        int bitmapH = bitmap.getHeight();
        int xW = bitmapW/5;
        int yH = bitmapH/2;
        for(int i=0;i<9;i++){
            int n = 0;
            if(i>4){
                n=1;
            }
            int j = i;
            if(i>=5){
                j=i-5;
            }
            bitmap=bitmapUtils.setBitmapPixel(
                    bitmap,
                    xW*j,
                    yH*n,
                    xW*(j+1),
                    yH*(n+1),
                    btnColors[i][1]
            );
        }
        imageView.setImageBitmap(bitmap);
    }

    /**
     * 创建基本的运算公式，并保存运算的结果
     * @return
     */
    private String createFormula(){
        //打乱操作符排序，并取出第一个操作符
        List<String> list = Arrays.asList(operations);
        Collections.shuffle(list);
        String operation =list.get(0);
        numberOne = randomInteger(numMax);
        numberTwo = randomInteger(numMax);
        if(numberOne<numberTwo && "-".equals(operation)){
            int temp =numberOne;
            numberOne = numberTwo;
            numberTwo = temp;
        }
        String str = numberOne+operation+numberTwo;
        switch (operation){
            case "+" :
                result = numberOne+numberTwo;
                break;
            case "-" :
                result = numberOne-numberTwo;
                break;
            case "×":
                result = numberOne*numberTwo;
                break;
        }
        return str;
    }

    /**
     * 返回一个指定最大数内的一个随机整数
     * @param max
     * @return
     */
    private int randomInteger(int max){
        return new Random().nextInt(max);
    }

    /**
     * 判断答案与结果是否一致
     */
    private void resultEquals(){
        int result_l = btnClickResult.size();
        String resultStr = "";
        for(int r=0;r<result_l;r++){
            resultStr+=btnClickResult.get(r);
            recordInput.setText("操作记录："+resultStr);
        }
        boolean rFlag = false;//是否洗牌标记
        String message ="";
        if(result==Integer.parseInt(resultStr)){
            //通关音效
            musicUtils.gameNextMusic(GoActivity.this);
            //答案与结果一致
            rFlag = true;
            message = "Very Good，←︿←";
            score++;//加一分
            count++;//记录操作次数
        }else if(Integer.parseInt(resultStr)>result){
            //game over背景音乐
            musicUtils.gameOverMusic(GoActivity.this);
            //答案不一致，并记录值超出正确结果长度
            rFlag = true;
            message = "呵呵 ↓◎＿◎↓ 大于正确结果";
            score--;//减一分
            count++;//记录操作次数
        }
        if(rFlag){
            //取消计时器
            clearTimeout();
            Toast.makeText(GoActivity.this, message, Toast.LENGTH_SHORT).show();
            //洗牌
            shuffleAll();
            //创建公式
            String formula = createFormula();
            tvFormula.setText(formula);
            btnClickResult=null;//设置为null，让垃圾回收机制回收
            //清空记录
            recordInput.setText("");
            //清空帮助
            tvShowHelp.setText("");
            tvShowHelp.setBackground(getResources().getDrawable(R.drawable.question));
            //显示记录数据
            app_go_count.setText("次数："+count);
            app_go_score.setText("分数："+score);
            app_go_timeOut.setText("超时("+setTimeOut+"秒)："+timeOut+" 次");
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearTimeout();
        //回收bitmap
        bitmapUtils.recycleBitmaps(bitmaps);
    }


}
