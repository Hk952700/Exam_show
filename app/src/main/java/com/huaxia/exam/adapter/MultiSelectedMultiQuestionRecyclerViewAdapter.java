package com.huaxia.exam.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.huaxia.exam.R;
import com.huaxia.exam.adapter.viewholder.MultiSelectedMultiQuestionViewHolder;
import com.huaxia.exam.bean.AnswerNineSelectedFiveOptionBean;

import java.util.ArrayList;

/**
 * 2019年4月15日 10:34:43
 * jiao hao kang
 * 十二选七(十二宫格)/九选五(九宫格) 答案的adapter
 */
public class MultiSelectedMultiQuestionRecyclerViewAdapter extends RecyclerView.Adapter<MultiSelectedMultiQuestionViewHolder> {
    private Context mContext;
    private ArrayList<AnswerNineSelectedFiveOptionBean> mList = new ArrayList<>();
    private int mCount;

    public MultiSelectedMultiQuestionRecyclerViewAdapter(Context mContext, int count) {
        this.mContext = mContext;
        this.mCount = count;
    }


    public void setmList(ArrayList<AnswerNineSelectedFiveOptionBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }


    @Override
    public MultiSelectedMultiQuestionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = View.inflate(mContext, R.layout.answer_nine_selected_five_item_layout1, null);
        return new MultiSelectedMultiQuestionViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiSelectedMultiQuestionViewHolder answerNineSelectedFiveQuestion, int i) {
        if (mList.size() >= i + 1) {
            answerNineSelectedFiveQuestion.mAnswer_nine_selected_five_item1_textview.setText(mList.get(i).getValue());
        } else {
            answerNineSelectedFiveQuestion.mAnswer_nine_selected_five_item1_textview.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return mCount;
    }
}
