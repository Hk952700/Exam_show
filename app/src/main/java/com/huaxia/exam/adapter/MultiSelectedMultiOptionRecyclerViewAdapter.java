package com.huaxia.exam.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.huaxia.exam.R;
import com.huaxia.exam.adapter.viewholder.MultiSelectedMultiOptionViewHolder;
import com.huaxia.exam.bean.AnswerNineSelectedFiveOptionBean;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 2019年4月15日 10:34:43
 * jiao hao kang
 * 十二选七(十二宫格)/九选五(九宫格) 选项的adapter
 */

public class MultiSelectedMultiOptionRecyclerViewAdapter extends RecyclerView.Adapter<MultiSelectedMultiOptionViewHolder> {

    private Context mContext;
    private ArrayList<AnswerNineSelectedFiveOptionBean> mList = new ArrayList<>();
    private int mType = 0;

    public MultiSelectedMultiOptionRecyclerViewAdapter(Context mContext, int type) {
        this.mContext = mContext;
        this.mType = type;
    }

    public void setmList(ArrayList<AnswerNineSelectedFiveOptionBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }


    private int selectedItemCount = 0;

    public int getSelectedItemCount() {
        return selectedItemCount;
    }

    public void setSelectedItemCount(int selectedItemCount) {
        this.selectedItemCount = selectedItemCount;
    }

    @NonNull
    @Override
    public MultiSelectedMultiOptionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = View.inflate(mContext, R.layout.answer_nine_selected_five_item_layout2, null);
        return new MultiSelectedMultiOptionViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiSelectedMultiOptionViewHolder answerNineSelectedFiveOptionViewHolder, final int i) {
        answerNineSelectedFiveOptionViewHolder.itemView.setOnClickListener(new View.OnClickListener() {//选项的图片的点击事件
            @Override
            public void onClick(View v) {
                //判断选中的数量不能超过  预设值
                if (selectedItemCount < mType) {
                    //未选中状态 进行回调传值
                    if (!mList.get(i).isChecked()) {
                        mList.get(i).setChecked(true);
                        notifyItemChanged(i);
                        onOptionItemselected.optionItemselected(mList.get(i));
                    }

                }

            }
        });
        //设置选项的显示背景图片的透明度
        answerNineSelectedFiveOptionViewHolder.mAnswer_nine_selected_five_item2_textview.setText(mList.get(i).getValue());
        if (mList.get(i).isChecked()) {
            answerNineSelectedFiveOptionViewHolder.mAnswer_nine_selected_five_item2_imageview.setAlpha(127);
        } else {
            answerNineSelectedFiveOptionViewHolder.mAnswer_nine_selected_five_item2_imageview.setAlpha(255);
        }


    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    private onOptionItemselected onOptionItemselected;

    public void setOnOptionItemselected(onOptionItemselected onOptionItemselected) {
        this.onOptionItemselected = onOptionItemselected;
    }

    //接口回调 当选项被选中的时候进行回调到主类
    public interface onOptionItemselected {
        /**
         * @param selectedItem 选中的对象
         */
        void optionItemselected(AnswerNineSelectedFiveOptionBean selectedItem);
    }


}
