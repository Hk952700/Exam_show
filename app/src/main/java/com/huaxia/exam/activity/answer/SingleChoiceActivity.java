package com.huaxia.exam.activity.answer;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
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
import com.huaxia.exam.adapter.SingleChoiceRecyclerViewAdapter;
import com.huaxia.exam.base.BaseActivity;
import com.huaxia.exam.bean.AnswerResultDataBean;
import com.huaxia.exam.bean.SingleChoiceItemBean;

import java.util.ArrayList;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * 2019年4月12日 10:22:14
 * jiao  hao kang
 * 单选题 activity
 */
public class SingleChoiceActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView mOptionRecyclerview;
    private TextView mQuestion;
    private TextView mCountDownText;
    private Button mConfirmButton;
    private SingleChoiceItemBean mUserAnswer;
    private RelativeLayout background_ll;
    private ImageView background_r;
    private ImageView background_l;
    private AnswerResultDataBean data;
    private TextView mTime;
    private TextView mRightAnswer;
    //private TextView mSelectedAnswer;
    private LinearLayout mOverLyout;
    //private Button mToResult;
    private long date;
    private long submitTime;
    private TextView mTitleContext;
    private ImageView mOverImage;
    private RelativeLayout mWebsocket_status;
    private SimpleDraweeView mTitle2Back;
    private boolean mIsLastQuestion = false;
    private AnimatorSet animatorSet;
    private String[] split;

    @Override
    public int setContentView() {
        return R.layout.activity_single_choice;
    }

    @Override
    public Context setContext() {
        return SingleChoiceActivity.this;
    }


    @Override
    public void init() {
        //右上角WebSocket状态方框
        mWebsocket_status = (RelativeLayout) findViewById(R.id.single_choice_websocket_status);

        mTitle2Back = (SimpleDraweeView) findViewById(R.id.single_choice_title2_back);
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(R.drawable.single_title_back))
                .build();
        DraweeController draweeController =
                Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                        .build();
        mTitle2Back.setController(draweeController);
        mCountDownText = (TextView) findViewById(R.id.single_choice_count_down);
        mQuestion = (TextView) findViewById(R.id.single_choice_question);
        mOptionRecyclerview = (RecyclerView) findViewById(R.id.single_choice_option_recyclerview);

        mConfirmButton = (Button) findViewById(R.id.single_choice_confirm_button);//确认图片


        background_ll = (RelativeLayout) findViewById(R.id.single_choice_background_ll);
        background_r = (ImageView) findViewById(R.id.single_choice_background_r);
        background_l = (ImageView) findViewById(R.id.single_choice_background_l);

        mOverLyout = (LinearLayout) findViewById(R.id.single_choice_over);//考完本道题布局
        mOverLyout.setVisibility(View.GONE);
        //mTime = (TextView) findViewById(R.id.single_choice_time);//选手本题用时
        mRightAnswer = (TextView) findViewById(R.id.single_choice_right_answer);//本题正确答案
        //mSelectedAnswer = (TextView) findViewById(R.id.single_choice_selected_answer);//选手选择的答案

        // mToResult = (Button) findViewById(R.id.single_choice_to_result_button);
        // mToResult.setOnClickListener(this);
        //mToResult.setVisibility(View.INVISIBLE);

        mTitleContext = (TextView) findViewById(R.id.single_choice_title_context);

        //mOverImage = (ImageView) findViewById(R.id.single_choice_over_image);

        initDataAndRecycler();
        OpenAnimation(background_l, background_r, background_ll);


    }


    private void initDataAndRecycler() {
        Intent intent = getIntent();
        data = (AnswerResultDataBean) intent.getParcelableExtra("answer");

        Log.i("jtest", "initDataAndRecycler: " + data);
        if (data != null) {

            mTitleContext.setText("第" + data.getTp_senum() + "题");
            if (data.getTp_subject().length() < 13) {
                mQuestion.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            } else {
                mQuestion.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            }

            mQuestion.setText(data.getTp_subject().trim());
            ArrayList<SingleChoiceItemBean> singleChoiceItemBeans = new ArrayList<>();
            split = data.getTp_options().split("/");
            for (int i = 0; i < split.length; i++) {
                char[] chars = split[i].toCharArray();
                StringBuffer stringBuffer = new StringBuffer();
                for (int j = 0; j < chars.length; j++) {
                    if (j > 0) {
                        stringBuffer.append(chars[j]);
                    }
                }

                singleChoiceItemBeans.add(new SingleChoiceItemBean(String.valueOf(chars[0]), stringBuffer.toString(), false, false));
            }
            singleChoice(singleChoiceItemBeans);
        }

    }


    @Override
    public void onCountDownFinish(long date, long submitTime) {
        super.onCountDownFinish(date, submitTime);
        this.date = date;
        this.submitTime = submitTime;

        if (data != null) {
            String tp_answer = data.getTp_answer();

            StringBuffer stringBuffer = new StringBuffer();

            for (int i = 0; i < split.length; i++) {
                char[] chars = split[i].toCharArray();
                if (String.valueOf(chars[0]).trim().equals(tp_answer.trim())) {

                    stringBuffer.append(String.valueOf(chars[0]) + ":" + split[i].substring(1, split[i].length()).trim());
                }
            }

            mRightAnswer.setText("正确答案:" + stringBuffer);
            mOverLyout.setVisibility(View.VISIBLE);
        }

       /* UploadGradeDataBean uploadGradeDataBean = new UploadGradeDataBean();

        if (mUserAnswer != null) {
            mSelectedAnswer.setText("我的答案:" + mUserAnswer.getOption());
            if (data.getTp_answer().equals(mUserAnswer.getOption())) {
                uploadGradeDataBean.setTrRight("0");//对错
                mOverImage.setImageResource(R.drawable.answer_yes);
                Log.i("jtest", "onCountDownFinish:分数=" + data.getTp_score());
                uploadGradeDataBean.setTrMark(data.getTp_score() + "");//分数
            } else {
                mOverImage.setImageResource(R.drawable.answer_no);
                uploadGradeDataBean.setTrRight("1");//对错
                uploadGradeDataBean.setTrMark("0");//分数
            }

            uploadGradeDataBean.setTrAnswer(mUserAnswer.getOption());//学生答案


        } else {
            mOverImage.setImageResource(R.drawable.answer_no);
            mSelectedAnswer.setText("我的答案:未作答");
            uploadGradeDataBean.setTrAnswer("未作答");//学生答案
            uploadGradeDataBean.setTrMark("0");//分数
            uploadGradeDataBean.setTrRight("1");//对错
        }
        if (data != null) {
            uploadGradeDataBean.setTrClass(data.getTp_class() + "");//班级
            uploadGradeDataBean.setTrTime(date + "");//耗时
            uploadGradeDataBean.setTrQuestion(data.getTp_subject());
            uploadGradeDataBean.setTrPapernum(data.getTp_senum() + "");//题号
            mRightAnswer.setText("正确答案:" + data.getTp_answer());
            mTime.setText("本题用时:" + stampToDate02(date));
            uploadGradeDataBean.setTrRightAnswer(data.getTp_answer());
            uploadGradeDataBean.setTrType(data.getTp_type() + "");
            UploadGrade(uploadGradeDataBean);
            if (data.getTp_senum() == AnswerConstants.ANSWER_QUESTION_SUM) {
                mToResult.setVisibility(View.VISIBLE);
            }
        }
        mConfirmButton.setVisibility(View.GONE);
        mOverLyout.setVisibility(View.VISIBLE);*/

    }


    /**
     * 2019年4月1日 11:41:27
     * jiao hao kang
     * 单选题
     */
    private void singleChoice(ArrayList<SingleChoiceItemBean> singleDatas) {

        mOptionRecyclerview.setLayoutManager(new LinearLayoutManager(this, VERTICAL, false));
        SingleChoiceRecyclerViewAdapter answerSingleChoiceRecyclerViewAdapter = new SingleChoiceRecyclerViewAdapter(this);
        mOptionRecyclerview.setAdapter(answerSingleChoiceRecyclerViewAdapter);
        answerSingleChoiceRecyclerViewAdapter.setmList(singleDatas);
        answerSingleChoiceRecyclerViewAdapter.setOnItemClickListener(new SingleChoiceRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClick(SingleChoiceItemBean item) {
                mUserAnswer = item;
            }
        });


    }

    //开场动画结束开启倒计时  开启点击确认的点击事件
    @Override
    public void animationEnd() {
        super.animationEnd();
        startCountDown(mCountDownText, 20);
        mConfirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
          /*  case R.id.single_choice_confirm_button:
                if (mUserAnswer != null && !TextUtils.isEmpty(mUserAnswer.getOption())) {
                    confirm();
                } else {
                    Toast.makeText(this, "请选择答案!", Toast.LENGTH_SHORT).show();
                }


                break;*/

           /* case R.id.single_choice_to_result_button:
                //toResult(date, System.currentTimeMillis() - submitTime);
                getAnswerRecord();
                break;*/
            default:
                break;
        }
    }

    @Override
    public void websocketStatusChange(int color) {
        Log.i("jiao", "單nwebsocketStatusChange: 前=" + color);
        if (mWebsocket_status != null) {
            mWebsocket_status.setBackgroundResource(color);
            Log.i("jiao", "單nwebsocketStatusChange: 后=" + color);
        }
    }
}
