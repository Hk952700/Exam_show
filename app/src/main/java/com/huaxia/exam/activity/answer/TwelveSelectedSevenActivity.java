package com.huaxia.exam.activity.answer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huaxia.exam.R;
import com.huaxia.exam.adapter.MultiSelectedMultiOptionRecyclerViewAdapter;
import com.huaxia.exam.adapter.MultiSelectedMultiQuestionRecyclerViewAdapter;
import com.huaxia.exam.base.BaseActivity;
import com.huaxia.exam.bean.AnswerNineSelectedFiveOptionBean;
import com.huaxia.exam.bean.AnswerResultDataBean;
import com.huaxia.exam.utils.AnswerConstants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class TwelveSelectedSevenActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<AnswerNineSelectedFiveOptionBean> bufferArrayList = new ArrayList<>();
    private ArrayList<AnswerNineSelectedFiveOptionBean> optionArrayList = new ArrayList<>();
    private TextView mCountDownText;
    private RecyclerView mQuestionRecyclerview;
    private RecyclerView mOptionRecyclerview;
    //private ImageView mQuestionRemoveImageview;


    //private int count = 0;//标记已选中选型的个数
    private MultiSelectedMultiOptionRecyclerViewAdapter mOptionRecyclerViewAdapter;
    private MultiSelectedMultiQuestionRecyclerViewAdapter mQuestionRecyclerViewAdapter;
    private Button mConfirmButton;
    private String mOptions1;
    private String mAnswer;
    private TextView mTime;
    private TextView mRightAnswer;
    private TextView mSelectedAnswer;
    private LinearLayout mOver;
    private AnswerResultDataBean answer;
    //private Button mToResult;
    private long date;
    private long submitTime;
    private TextView mTitleContext;
    private ImageView mAnswerOverImage;
    private RelativeLayout mWebsocket_status;
    private SimpleDraweeView mTitle2Back;
    private boolean mIsLastQuestion = false;
    private AnimatorSet animatorSet;
    private ImageView end_img;

    @Override
    public int setContentView() {
        return R.layout.activity_twelve_selected_seven;
    }

    @Override
    public Context setContext() {
        return TwelveSelectedSevenActivity.this;
    }


    @Override
    public void init() {
        //右上角WebSocket状态方框
        mWebsocket_status = (RelativeLayout) findViewById(R.id.twelve_selected_seven_websocket_status);
        end_img = (ImageView) findViewById(R.id.end_img);
        mTitle2Back = (SimpleDraweeView) findViewById(R.id.twelve_selected_seven_title2_back);
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(R.drawable.twelve_title_back))
                .build();
        DraweeController draweeController =
                Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                        .build();
        mTitle2Back.setController(draweeController);
        //初始化控件
        mCountDownText = (TextView) findViewById(R.id.twelve_selected_seven_count_down);//倒计时(textview)
        mQuestionRecyclerview = (RecyclerView) findViewById(R.id.twelve_selected_seven_question_recyclerview);//选手选中的答案的recyclerview
        //mQuestionRemoveImageview = (ImageView) findViewById(R.id.twelve_selected_seven_question_remove_imageview);//选中的答案的删除按钮(imageview)
        mOptionRecyclerview = (RecyclerView) findViewById(R.id.twelve_selected_seven_option_recyclerview);//选项的recyclerview


        //mQuestionRemoveImageview.setOnClickListener(this);

        mConfirmButton = (Button) findViewById(R.id.twelve_selected_seven_confirm_button);//确认图片
        ImageView background_l = (ImageView) findViewById(R.id.twelve_selected_seven_background_l);
        ImageView background_r = (ImageView) findViewById(R.id.twelve_selected_seven_background_r);
        RelativeLayout background_ll = (RelativeLayout) findViewById(R.id.twelve_selected_seven_background_ll);

        mOver = (LinearLayout) findViewById(R.id.twelve_selected_seven_over);
        mOver.setVisibility(View.GONE);
        //mTime = (TextView) findViewById(R.id.twelve_selected_seven_time);
        mRightAnswer = (TextView) findViewById(R.id.twelve_selected_seven_right_answer);
        // mSelectedAnswer = (TextView) findViewById(R.id.twelve_selected_seven_selected_answer);


        // mToResult = (Button) findViewById(R.id.twelve_selected_seven_to_result_button);

        // mToResult.setOnClickListener(this);
        // mToResult.setVisibility(View.INVISIBLE);

        //  mAnswerOverImage = (ImageView) findViewById(R.id.twelve_selected_seven_over_image);

        mTitleContext = (TextView) findViewById(R.id.twelve_selected_seven_title_context);
        OpenAnimation(background_l, background_r, background_ll);
        initData();

    }


    private void initData() {
        Intent intent01 = getIntent();
        answer = (AnswerResultDataBean) intent01.getParcelableExtra("answer");

        if (answer != null) {

            mTitleContext.setText("第" + answer.getTp_senum() + "题");

            mOptions1 = answer.getTp_subject();
            mAnswer = answer.getTp_answer();

            mOptions1 = answer.getTp_subject();
            Log.i("jtest", "initData: mOptions为:" + mOptions1);
            Log.i("jtest", "initData: mAnswer为:" + mAnswer);
        }

        if (!TextUtils.isEmpty(mOptions1)) {
            char[] chars = mOptions1.toCharArray();

            for (int i = 0; i < chars.length; i++) {
                AnswerNineSelectedFiveOptionBean answerNineSelectedFiveOptionBean = new AnswerNineSelectedFiveOptionBean();
                answerNineSelectedFiveOptionBean.setValue(String.valueOf(chars[i]));
                answerNineSelectedFiveOptionBean.setIndex(i);
                optionArrayList.add(answerNineSelectedFiveOptionBean);
            }


        }
        initRecyclerView(optionArrayList, bufferArrayList);
    }


    private void initRecyclerView(final ArrayList<AnswerNineSelectedFiveOptionBean> optionArrayList, final ArrayList<AnswerNineSelectedFiveOptionBean> bufferArrayList) {

        mOptionRecyclerViewAdapter = new MultiSelectedMultiOptionRecyclerViewAdapter(TwelveSelectedSevenActivity.this, 7);
        mOptionRecyclerview.setLayoutManager(new GridLayoutManager(TwelveSelectedSevenActivity.this, 4));

        mQuestionRecyclerview.setLayoutManager(new LinearLayoutManager(TwelveSelectedSevenActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mQuestionRecyclerViewAdapter = new MultiSelectedMultiQuestionRecyclerViewAdapter(TwelveSelectedSevenActivity.this, 7);
        mQuestionRecyclerview.setAdapter(mQuestionRecyclerViewAdapter);

        mOptionRecyclerview.setAdapter(mOptionRecyclerViewAdapter);
        mOptionRecyclerViewAdapter.setmList(optionArrayList);

        //选中回调
        mOptionRecyclerViewAdapter.setOnOptionItemselected(new MultiSelectedMultiOptionRecyclerViewAdapter.onOptionItemselected() {

            @Override
            public void optionItemselected(AnswerNineSelectedFiveOptionBean selectedItem) {
                bufferArrayList.add(selectedItem);
                mQuestionRecyclerViewAdapter.setmList(bufferArrayList);
                mOptionRecyclerViewAdapter.setSelectedItemCount(mOptionRecyclerViewAdapter.getSelectedItemCount() + 1);
            }
        });

    }

    //开场动画结束开启倒计时  开启点击确认的点击事件
    @Override
    public void animationEnd() {
        super.animationEnd();
        startCountDown(mCountDownText, 30);
        mConfirmButton.setOnClickListener(this);
    }

    @Override
    public void onCountDownFinish(long date, long submitTime) {
        super.onCountDownFinish(date, submitTime);
        this.submitTime = submitTime;
        this.date = date;

        if (answer != null) {

            mRightAnswer.setText("正确答案:" + answer.getTp_answer());
            mOver.setVisibility(View.VISIBLE);
            if (answer.getTp_senum() == AnswerConstants.ANSWER_QUESTION_SUM) {
                mIsLastQuestion = true;
            } else {
                mIsLastQuestion = false;
            }
        }

        //是最后一道题进行弹窗倒计时切换布局
        if (mIsLastQuestion) {
            //Log.i("jiao123", "onCountDownFinish: ");
            if (handler != null) {
                handler.sendEmptyMessageDelayed(0, 1000);
            }

        } else {
            if (handler != null) {
                handler.sendEmptyMessageDelayed(1, 1000);
            }
        }

       /* UploadGradeDataBean uploadGradeDataBean = new UploadGradeDataBean();
        if (bufferArrayList.size() != 0) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < bufferArrayList.size(); i++) {
                stringBuffer.append(bufferArrayList.get(i).getValue());
            }
            String s = stringBuffer.toString();
            Log.i("jtest", "onCountDownFinish: 十二宫格学生答案為:" + s);
            Log.i("jtest", "onCountDownFinish: 正確答案答案為:" + mAnswer);
            if (s.equals(mAnswer)) {
                uploadGradeDataBean.setTrRight("0");//对错
                mAnswerOverImage.setImageResource(R.drawable.answer_yes);
                uploadGradeDataBean.setTrMark(answer.getTp_score() + "");//分数
            } else {
                uploadGradeDataBean.setTrRight("1");//对错
                mAnswerOverImage.setImageResource(R.drawable.answer_no);
                uploadGradeDataBean.setTrMark("0");//分数
            }
            Log.i("jtest", "onCountDownFinish: 时间:" + date);

            mRightAnswer.setText("正确答案:" + mAnswer);
            mTime.setText("本题用时:" + stampToDate02(date));
            mSelectedAnswer.setText("我的答案:" + s);
            uploadGradeDataBean.setTrAnswer(s);//学生答案
        } else {
            mAnswerOverImage.setImageResource(R.drawable.answer_no);
            uploadGradeDataBean.setTrAnswer("未作答");//学生答案
            mSelectedAnswer.setText("我的答案:未作答");
            uploadGradeDataBean.setTrMark("0");//分数
            uploadGradeDataBean.setTrRight("1");//对错
        }


        if (answer != null) {
            uploadGradeDataBean.setTrQuestion(answer.getTp_subject());
            mRightAnswer.setText("正确答案:" + mAnswer);
            mTime.setText("本题用时:" + stampToDate02(date));
            uploadGradeDataBean.setTrClass(answer.getTp_class() + "");//班级
            uploadGradeDataBean.setTrTime(date + "");//耗时
            uploadGradeDataBean.setTrPapernum(answer.getTp_senum() + "");//题号
            uploadGradeDataBean.setTrRightAnswer(mAnswer);
            uploadGradeDataBean.setTrType(answer.getTp_type() + "");
            UploadGrade(uploadGradeDataBean);
            if (answer.getTp_senum() == AnswerConstants.ANSWER_QUESTION_SUM) {
                mToResult.setVisibility(View.VISIBLE);
            }
        }*/


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.twelve_selected_seven_question_remove_imageview://点击删除按钮(imageview)
                //Log.i("jtest", "onClick: getSelectedItemCount=" + mOptionRecyclerViewAdapter.getSelectedItemCount() + "bufferArrayList.size()=0" + bufferArrayList.size() + "optionArrayList.size()=" + optionArrayList.size());
                if (mOptionRecyclerViewAdapter.getSelectedItemCount() > 0 && bufferArrayList.size() > 0) {
                    for (int i = 0; i < optionArrayList.size(); i++) {
                        if (bufferArrayList.get(bufferArrayList.size() - 1).getIndex() == optionArrayList.get(i).getIndex()) {
                            optionArrayList.get(i).setChecked(false);
                            mOptionRecyclerViewAdapter.setmList(optionArrayList);//下面选项的
                            mOptionRecyclerViewAdapter.setSelectedItemCount(mOptionRecyclerViewAdapter.getSelectedItemCount() - 1);
                            //循环遍历 移除  刷新两个适配器
                            bufferArrayList.remove(bufferArrayList.size() - 1);
                            mQuestionRecyclerViewAdapter.setmList(bufferArrayList);//上面填空的
                            break;
                        }
                    }


                }

                break;*/
            /*case R.id.twelve_selected_seven_confirm_button: //点击确认按钮(textview)
                if (bufferArrayList.size() != 0) {
                    if (bufferArrayList.size() == ANSWER_TWELVE_SELECTED_SEVEN_COUNT) {
                        confirm();
                    } else {
                        Toast.makeText(this, "请填写完整!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请选择答案!", Toast.LENGTH_SHORT).show();
                }
                break;*/

            /*case R.id.twelve_selected_seven_to_result_button:
                Log.i("jtest", "十二选七:onClick: " + "点击提交");

                //toResult(date, System.currentTimeMillis() - submitTime);
                getAnswerRecord();
                break;*/
            default:
                break;
        }
    }

    @Override
    public void websocketStatusChange(int color) {
        Log.i("jiao", "12nwebsocketStatusChange: 前=" + color);
        if (mWebsocket_status != null) {
            mWebsocket_status.setBackgroundResource(color);
            Log.i("jiao", "12nwebsocketStatusChange: 后=" + color);
        }
    }

    private TwelveSelectedSevenActivity.MyHandler handler = new TwelveSelectedSevenActivity.MyHandler(TwelveSelectedSevenActivity.this);

    //弹窗动画切换弹窗布局倒计时3秒
    private int mAnimationCountDown = 3;


    /**
     * Handler 内部类(防内存泄露)
     */
    private class MyHandler extends Handler {
        // 弱引用 ，防止内存泄露
        private WeakReference<TwelveSelectedSevenActivity> weakReference;


        public MyHandler(TwelveSelectedSevenActivity handlerMemoryActivity) {
            weakReference = new WeakReference<TwelveSelectedSevenActivity>(handlerMemoryActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    if (mAnimationCountDown > 0) {
                        mAnimationCountDown--;
                        if (handler != null) {
                            handler.sendEmptyMessageDelayed(0, 1000);
                        }

                    } else {
                        //开始透明渐变切换弹窗布局
                        //TODO

                        dialogSwitchoverAnimation(mOver, end_img);
                    }
                    break;
                case 1:
                    if (mAnimationCountDown > 0) {
                        mAnimationCountDown--;
                        if (handler != null) {
                            handler.sendEmptyMessageDelayed(0, 1000);
                        }

                    } else {
                        //开始透明渐变切换弹窗布局
                        //TODO

                        dialogSwitchoverAnimation(mOver, end_img);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    //弹窗渐变透明动画
    private void dialogSwitchoverAnimation(final LinearLayout layout0, ImageView layout1) {

        if (layout0 != null && layout1 != null) {

            ObjectAnimator anim0 = ObjectAnimator.ofFloat(layout0, "alpha", 1f, 0f);
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(layout1, "alpha", 0f, 1f);

            animatorSet = new AnimatorSet();
            animatorSet.playTogether(anim0, anim1);
            animatorSet.setDuration(1 * 1000);
            animatorSet.start();
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //layout0.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();


        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

        if (animatorSet != null) {
            animatorSet.cancel();
            animatorSet = null;
        }


    }
}
