package com.huaxia.exam.activity.answer;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.util.ArrayList;

/**
 * 2019年4月12日 10:45:48
 * jiao hao kang
 * 九选五 填空题(九宫格)
 */
public class NineSelectedFiveActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<AnswerNineSelectedFiveOptionBean> bufferArrayList = new ArrayList<>();
    private ArrayList<AnswerNineSelectedFiveOptionBean> optionArrayList = new ArrayList<>();
    private TextView mCountDownText;
    private RecyclerView mQuestionRecyclerview;
    //private ImageView mQuestionRemoveImageview;
    private RecyclerView mOptionRecyclerview;
    private MultiSelectedMultiOptionRecyclerViewAdapter mOptionRecyclerViewAdapter;
    private MultiSelectedMultiQuestionRecyclerViewAdapter mQuestionRecyclerViewAdapter;
    private TextView mConfirmText;
    // private int count;
    private Button mConfirmButton;
    private String mOptions01;
    private String mAnswer;
    private LinearLayout mOver;
    private TextView mTime;
    private TextView mRightAnswer;
    private TextView mSelectedAnswer;
    private AnswerResultDataBean answer;
    // private Button mToResult;
    private long date;
    private long submitTime;

    private TextView mTitleContext;
    private ImageView mOverImage;
    private RelativeLayout mWebsocket_status;
    private SimpleDraweeView mTitle2Back;
    private boolean mIsLastQuestion = false;
    private AnimatorSet animatorSet;

    @Override
    public int setContentView() {
        return R.layout.activity_nine_selected_five;
    }

    @Override
    public Context setContext() {
        return NineSelectedFiveActivity.this;
    }

    @Override
    public void init() {
        //右上角WebSocket状态方框
        mWebsocket_status = (RelativeLayout) findViewById(R.id.nine_selected_five_websocket_status);

        mTitle2Back = (SimpleDraweeView) findViewById(R.id.nine_selected_five_title2_back);
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(R.drawable.nine_title_back))
                .build();
        DraweeController draweeController =
                Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                        .build();
        mTitle2Back.setController(draweeController);
        //倒计时textview
        mCountDownText = (TextView) findViewById(R.id.nine_selected_five_count_down);
        //答案的recyclerview
        mQuestionRecyclerview = (RecyclerView) findViewById(R.id.nine_selected_five_question_recyclerview);
        //移除选中的选项
        //mQuestionRemoveImageview = (ImageView) findViewById(R.id.nine_selected_five_question_remove_imageview);
        //显示选项的recyclerview
        mOptionRecyclerview = (RecyclerView) findViewById(R.id.nine_selected_five_option_recyclerview);
        mConfirmButton = (Button) findViewById(R.id.nine_selected_five_confirm_button);//确认图片


        ImageView background_l = (ImageView) findViewById(R.id.nine_selected_five_background_l);
        ImageView background_r = (ImageView) findViewById(R.id.nine_selected_five_background_r);
        RelativeLayout background_ll = (RelativeLayout) findViewById(R.id.nine_selected_five_background_ll);

        mOver = (LinearLayout) findViewById(R.id.nine_selected_five_over);
        //mTime = (TextView) findViewById(R.id.nine_selected_five_time);
        mRightAnswer = (TextView) findViewById(R.id.nine_selected_five_right_answer);
        //mSelectedAnswer = (TextView) findViewById(R.id.nine_selected_five_selected_answer);
        mOver.setVisibility(View.GONE);

        mConfirmButton.setOnClickListener(this);
        //mQuestionRemoveImageview.setOnClickListener(this);

        /*mToResult = (Button) findViewById(R.id.nine_selected_five_to_result_button);

        mToResult.setOnClickListener(this);*/
        /* mToResult.setVisibility(View.INVISIBLE);*/

        mTitleContext = (TextView) findViewById(R.id.nine_selected_five_title_context);


        //mOverImage = (ImageView) findViewById(R.id.nine_selected_five_over_image);

        OpenAnimation(background_l, background_r, background_ll);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        answer = (AnswerResultDataBean) intent.getParcelableExtra("answer");

        if (answer != null) {
            mTitleContext.setText("第" + answer.getTp_senum() + "题");


            mOptions01 = answer.getTp_subject();
            mAnswer = answer.getTp_answer();
            if (!TextUtils.isEmpty(mOptions01)) {
                char[] chars = mOptions01.toCharArray();

                for (int i = 0; i < chars.length; i++) {
                    AnswerNineSelectedFiveOptionBean answerNineSelectedFiveOptionBean = new AnswerNineSelectedFiveOptionBean();
                    answerNineSelectedFiveOptionBean.setValue(String.valueOf(chars[i]));
                    answerNineSelectedFiveOptionBean.setIndex(i);
                    optionArrayList.add(answerNineSelectedFiveOptionBean);
                }


            }
        }
        nineSelectedFive(bufferArrayList, optionArrayList);

    }


    @Override
    public void onCountDownFinish(long date, long submitTime) {
        super.onCountDownFinish(date, submitTime);
        this.date = date;
        this.submitTime = submitTime;

        if (answer != null) {

            mRightAnswer.setText("正确答案:" + answer.getTp_answer());
            mOver.setVisibility(View.VISIBLE);
        }

     /*   UploadGradeDataBean uploadGradeDataBean = new UploadGradeDataBean();
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
                mOverImage.setImageResource(R.drawable.answer_yes);
                uploadGradeDataBean.setTrMark(answer.getTp_score() + "");//分数
            } else {
                uploadGradeDataBean.setTrRight("1");//对错
                mOverImage.setImageResource(R.drawable.answer_no);
                uploadGradeDataBean.setTrMark("0");//分数
            }
            Log.i("jtest", "onCountDownFinish: 时间:" + date);

            mRightAnswer.setText("正确答案:" + mAnswer);
            mTime.setText("本题用时:" + stampToDate02(date));
            mSelectedAnswer.setText("我的答案:" + s);
            uploadGradeDataBean.setTrAnswer(s);//学生答案

        } else {
            mOverImage.setImageResource(R.drawable.answer_no);
            uploadGradeDataBean.setTrAnswer("未作答");//学生答案
            mSelectedAnswer.setText("我的答案:未作答");
            uploadGradeDataBean.setTrMark("0");//分数
            uploadGradeDataBean.setTrRight("1");//对错
        }
        mOver.setVisibility(View.VISIBLE);
        mRightAnswer.setText("正确答案:" + mAnswer);
        mTime.setText("本题用时:" + stampToDate02(date));
        if (answer != null) {
            uploadGradeDataBean.setTrClass(answer.getTp_class() + "");//班级
            uploadGradeDataBean.setTrTime(date + "");//耗时
            uploadGradeDataBean.setTrQuestion(answer.getTp_subject());
            uploadGradeDataBean.setTrPapernum(answer.getTp_senum() + "");//题号
            uploadGradeDataBean.setTrRightAnswer(answer.getTp_answer());
            uploadGradeDataBean.setTrType(answer.getTp_type() + "");

            UploadGrade(uploadGradeDataBean);
            if (answer.getTp_senum() == AnswerConstants.ANSWER_QUESTION_SUM) {
                mToResult.setVisibility(View.VISIBLE);
            }
        }*/

    }


    /**
     * 2019年4月1日 10:17:54
     * jiao hao kang
     * 九选五(九宫格)
     */
    private void nineSelectedFive(final ArrayList<AnswerNineSelectedFiveOptionBean> bufferArrayList, final ArrayList<AnswerNineSelectedFiveOptionBean> optionArrayList) {
        //int mNineSelectedFiveQuestionType = AnswerConstants.ANSWER_QUESTION_TYPE_NINE_SELECTED_FIVE;


        //设置两个recyclertview的布局管理器  初始化dapter 配置dapter setList
        mOptionRecyclerview.setLayoutManager(new GridLayoutManager(NineSelectedFiveActivity.this, 3));
        mQuestionRecyclerview.setLayoutManager(new LinearLayoutManager(NineSelectedFiveActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mOptionRecyclerViewAdapter = new MultiSelectedMultiOptionRecyclerViewAdapter(NineSelectedFiveActivity.this, 5);
        mQuestionRecyclerViewAdapter = new MultiSelectedMultiQuestionRecyclerViewAdapter(NineSelectedFiveActivity.this, 5);
        mOptionRecyclerview.setAdapter(mOptionRecyclerViewAdapter);
        mQuestionRecyclerview.setAdapter(mQuestionRecyclerViewAdapter);
        mOptionRecyclerViewAdapter.setmList(optionArrayList);//下面选项的
        mQuestionRecyclerViewAdapter.setmList(bufferArrayList);//上面填空的
        //上面填空的背景

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


    @Override
    public void animationEnd() {
        super.animationEnd();
        startCountDown(mCountDownText, 30);
        mCountDownText.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.nine_selected_five_question_remove_imageview:
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


                }*/
               /* if (mOptionRecyclerViewAdapter.getSelectedItemCount() > 0) {
                    //循环遍历 移除  刷新两个适配器
                    for (int i = 0; i < optionArrayList.size(); i++) {
                        if (bufferArrayList.get(bufferArrayList.size() - 1).getIndex() == optionArrayList.get(i).getIndex()) {
                            bufferArrayList.remove(bufferArrayList.size() - 1);
                            optionArrayList.get(i).setChecked(false);
                            mQuestionRecyclerViewAdapter.setmList(bufferArrayList);//上面填空的
                            mOptionRecyclerViewAdapter.setmList(optionArrayList);//下面选项的
                            mOptionRecyclerViewAdapter.setSelectedItemCount(mOptionRecyclerViewAdapter.getSelectedItemCount() - 1);
                            break;
                        }

                    }

                }*/

            /*break;*/

            /*case R.id.nine_selected_five_confirm_button:
                if (bufferArrayList.size() != 0) {
                    if (bufferArrayList.size() == ANSWER_NINE_SELECTED_FIVE_COUNT) {
                        confirm();
                    } else {
                        Toast.makeText(this, "请填写完整!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请选择答案!", Toast.LENGTH_SHORT).show();
                }
                break;*/

           /* case R.id.nine_selected_five_to_result_button:
                //toResult(date, System.currentTimeMillis() - submitTime);
                getAnswerRecord();
                break;*/
            default:
                break;
        }
    }

    @Override
    public void websocketStatusChange(int color) {
        Log.i("jiao", "九nwebsocketStatusChange: 前=" + color);
        if (mWebsocket_status != null) {

            mWebsocket_status.setBackgroundResource(color);
            Log.i("jiao", "九websocketStatusChange: 前=" + color);

        }
    }
}
