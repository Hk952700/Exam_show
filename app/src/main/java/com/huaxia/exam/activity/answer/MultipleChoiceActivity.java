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
import com.huaxia.exam.adapter.MultipleChoiceRecyclerViewAdapter;
import com.huaxia.exam.base.BaseActivity;
import com.huaxia.exam.bean.AnswerResultDataBean;
import com.huaxia.exam.bean.SingleChoiceItemBean;

import java.util.ArrayList;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * 多选Activity
 */
public class MultipleChoiceActivity extends BaseActivity implements View.OnClickListener {

    private TextView mCountDown;
    private TextView mQuestion;
    private RecyclerView mOptionRecyclerview;
    private TextView mConfirmText;
    private Button mConfirmButton;


    private LinearLayout mOver;
    private TextView mTime;
    private TextView mRightAnswer;
    //private TextView mSelectedAnswer;
    private ArrayList<SingleChoiceItemBean> optionsArray = new ArrayList<>();
    private AnswerResultDataBean data;
    private Button mToResult;
    private long date;
    private long submitTime;
    private TextView mTitleContext;
    private RelativeLayout background_ll;
    private ImageView background_r;
    private ImageView background_l;
    private ImageView mOverImage;
    private RelativeLayout mWebsocket_status;
    private SimpleDraweeView mTitle2Back;
    private boolean mIsLastQuestion = false;
    private AnimatorSet animatorSet;
    private String[] split;

    @Override
    public int setContentView() {
        return R.layout.activity_multiple_choice;
    }

    @Override
    public Context setContext() {
        return MultipleChoiceActivity.this;
    }

    @Override
    public void init() {
        //右上角WebSocket状态方框
        mWebsocket_status = (RelativeLayout) findViewById(R.id.multiple_choice_websocket_status);


        mTitle2Back = (SimpleDraweeView) findViewById(R.id.multiple_choice_title2_back);
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(R.drawable.multiple_title_back))
                .build();
        DraweeController draweeController =
                Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                        .build();
        mTitle2Back.setController(draweeController);


        mCountDown = (TextView) findViewById(R.id.multiple_choice_count_down);//倒计时显示textview
        mQuestion = (TextView) findViewById(R.id.multiple_choice_question);//问题展示
        mOptionRecyclerview = (RecyclerView) findViewById(R.id.multiple_choice_option_recyclerview);//选项展示
        mConfirmButton = (Button) findViewById(R.id.multiple_choice_confirm_button);//确认图片
        mTitleContext = (TextView) findViewById(R.id.multiple_choice_title_context);

        background_ll = (RelativeLayout) findViewById(R.id.multiple_choice_background_ll);
        background_r = (ImageView) findViewById(R.id.multiple_choice_background_r);
        background_l = (ImageView) findViewById(R.id.multiple_choice_background_l);

        mOver = (LinearLayout) findViewById(R.id.multiple_choice_over);
        mOver.setVisibility(View.GONE);
        /*mTime = (TextView) findViewById(R.id.multiple_choice_time);*/
        mRightAnswer = (TextView) findViewById(R.id.multiple_choice_right_answer);
      /*  mSelectedAnswer = (TextView) findViewById(R.id.multiple_choice_selected_answer);

        mToResult = (Button) findViewById(R.id.multiple_choice_to_result_button);*/
        //mToResult.setOnClickListener(this);
        //mToResult.setVisibility(View.INVISIBLE);

        /* mOverImage = (ImageView) findViewById(R.id.multiple_choice_over_image);*/

        OpenAnimation(background_l, background_r, background_ll);
        initDataAndRecycler();
    }


    private void initDataAndRecycler() {
        Intent intent = getIntent();
        data = (AnswerResultDataBean) intent.getParcelableExtra("answer");
        Log.i("jtest", "initDataAndRecycler: 多选题Activity接受到intent=" + data);
        //TODO 后期完善
        if (data != null) {
            if (data.getTp_subject().length() < 13) {
                mQuestion.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            } else {
                mQuestion.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            }

            mTitleContext.setText("第" + data.getTp_senum() + "题");

            mQuestion.setText(data.getTp_subject().trim());
            //选项
            ArrayList<SingleChoiceItemBean> multipleChoiceItemBeans = new ArrayList<>();
            split = data.getTp_options().split("/");
            for (int i = 0; i < split.length; i++) {
                char[] chars = split[i].toCharArray();
                StringBuffer stringBuffer = new StringBuffer();
                for (int j = 0; j < chars.length; j++) {
                    if (j > 0) {
                        stringBuffer.append(chars[j]);
                    }
                }

                multipleChoiceItemBeans.add(new SingleChoiceItemBean(String.valueOf(chars[0]), stringBuffer.toString(), false, false));
            }

            multipleChoice(multipleChoiceItemBeans);
        }


    }

    /**
     * 2019年5月6日 18:05:45
     * jiao hao kang
     *
     * @param multipleChoiceItemBeans
     */
    private void multipleChoice(ArrayList<SingleChoiceItemBean> multipleChoiceItemBeans) {

        MultipleChoiceRecyclerViewAdapter multipleChoiceRecyclerViewAdapter = new MultipleChoiceRecyclerViewAdapter(MultipleChoiceActivity.this);
        mOptionRecyclerview.setLayoutManager(new LinearLayoutManager(this, VERTICAL, false));
        mOptionRecyclerview.setAdapter(multipleChoiceRecyclerViewAdapter);
        multipleChoiceRecyclerViewAdapter.setmList(multipleChoiceItemBeans);
        multipleChoiceRecyclerViewAdapter.setOnItemClickListener(new MultipleChoiceRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClick(ArrayList<SingleChoiceItemBean> items) {
                optionsArray = items;
            }
        });


    }


    @Override
    public void onCountDownFinish(long date, long submitTime) {
        super.onCountDownFinish(date, submitTime);
        this.date = date;
        this.submitTime = submitTime;

        if (data != null) {
            char[] chars = data.getTp_answer().trim().toCharArray();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                for (int j = 0; j < chars.length; j++) {
                    if (split[i].trim().substring(0, 1).equals(String.valueOf(chars[j]))) {
                        stringBuffer.append(String.valueOf(chars[j]) + ":" + split[i].trim().substring(1, split[i].trim().length()) + ",");
                        continue;
                    }
                }
            }

            String substring = stringBuffer.substring(0, stringBuffer.length() - 1);
            mRightAnswer.setText("正确答案:" + substring);
            mOver.setVisibility(View.VISIBLE);
        }


        /*UploadGradeDataBean uploadGradeDataBean = new UploadGradeDataBean();

        if (optionsArray != null && optionsArray.size() > 0) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < optionsArray.size(); i++) {
                stringBuffer.append(optionsArray.get(i).getOption().trim());
            }

            if (isRight(stringBuffer.toString(), data.getTp_answer())) {
                mOverImage.setImageResource(R.drawable.answer_yes);
                uploadGradeDataBean.setTrRight("0");//对错
                uploadGradeDataBean.setTrMark(data.getTp_score() + "");//分数
            } else {
                mOverImage.setImageResource(R.drawable.answer_no);
                uploadGradeDataBean.setTrRight("1");//对错
                uploadGradeDataBean.setTrMark("0");//分数
            }
            uploadGradeDataBean.setTrAnswer(stringBuffer.toString());//学生答案
            mSelectedAnswer.setText("我的答案:" + stringBuffer.toString());
        } else {
            mOverImage.setImageResource(R.drawable.answer_no);
            uploadGradeDataBean.setTrAnswer("未作答");//学生答案
            mSelectedAnswer.setText("我的答案:未作答");
            uploadGradeDataBean.setTrRight("1");//对错
            uploadGradeDataBean.setTrMark("0");//分数
        }

        uploadGradeDataBean.setTrQuestion(data.getTp_subject());
        uploadGradeDataBean.setTrClass(data.getTp_class() + "");//班级
        uploadGradeDataBean.setTrTime(date + "");//耗时
        uploadGradeDataBean.setTrPapernum(data.getTp_senum() + "");//题号
        uploadGradeDataBean.setTrRightAnswer(data.getTp_answer());
        mRightAnswer.setText("正确答案:" + data.getTp_answer());
        mTime.setText("本题用时:" + stampToDate02(date));
        mOver.setVisibility(View.VISIBLE);
        uploadGradeDataBean.setTrType(data.getTp_type() + "");
        UploadGrade(uploadGradeDataBean);
        if (data.getTp_senum() == AnswerConstants.ANSWER_QUESTION_SUM) {
            mToResult.setVisibility(View.VISIBLE);
        }

        mConfirmButton.setVisibility(View.GONE);
*/
    }


    /**
     * 2019年5月6日 18:38:42
     * jiao hao kang
     *
     * @param userAnswer 选手答案
     * @param answer     正确答案
     * @return 是否正确
     */

    private boolean isRight(String userAnswer, String answer) {

        char[] chars0 = userAnswer.toCharArray();//选手答案
        char[] chars1 = answer.trim().toCharArray();//正确答案

        if (chars0.length != chars1.length) {
            return false;
        } else {
            for (int i = 0; i < chars1.length; i++) {
                boolean flag = false;
                for (int j = 0; j < chars0.length; j++) {
                    if (String.valueOf(chars0[j]).equals(String.valueOf(chars1[i]))) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return false;
                }

            }
        }

        return true;
    }

    //开场动画结束开启倒计时  开启点击确认的点击事件
    @Override
    public void animationEnd() {
        super.animationEnd();
        startCountDown(mCountDown, 20);
        mConfirmButton.setOnClickListener(this);
    }

    @Override
    public void websocketStatusChange(int color) {
        Log.i("jiao", "多nwebsocketStatusChange: 前=" + color);
        if (mWebsocket_status != null) {
            mWebsocket_status.setBackgroundResource(color);
            Log.i("jiao", "多nwebsocketStatusChange: 后=" + color);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.multiple_choice_confirm_button:
                if (optionsArray != null && optionsArray.size() > 0) {
                    confirm();
                } else {
                    Toast.makeText(this, "请选择答案!", Toast.LENGTH_SHORT).show();
                }
                break;*/

          /*  case R.id.multiple_choice_to_result_button:
                //toResult(date, System.currentTimeMillis() - submitTime);//最后一道获取排名
                getAnswerRecord();

                break;*/
            default:
                break;
        }
    }


}
