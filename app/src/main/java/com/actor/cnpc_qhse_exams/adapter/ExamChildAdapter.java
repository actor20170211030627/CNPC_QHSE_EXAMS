package com.actor.cnpc_qhse_exams.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.actor.cnpc_qhse_exams.R;
import com.actor.myandroidframework.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 描述
 * company    :
 *
 * @author : ldf
 * date       : 2026/4/11 on 21
 * @version 1.0
 */
public class ExamChildAdapter extends BaseQuickAdapter<ExamChildAdapter.ExamChildItem, BaseViewHolder> {

    private int subjectType;
    private       String        answer;
    private final List<Boolean> answers        = new ArrayList<>(8);
    private final StringBuilder answerSelected = new StringBuilder();

    public ExamChildAdapter() {
        super(R.layout.item_exam_adapter_child);
        for (int i = 0; i < 8; i++) {
            answers.add(false);
        }
        setOnItemClickListener((adapter, view, position) -> {
            ExamChildItem item = getItem(position);
            item.isSelected = !item.isSelected;
            answers.set(position, item.isSelected);
            view.setSelected(item.isSelected);
            if (item.isSelected) {
                //if是单选题 or 选择题, 防止多选
                if (subjectType == 1 || subjectType == 3) {
                    List<ExamChildItem> data = getData();
                    for (int i = 0; i < data.size(); i++) {
                        if (i == position) continue;
                        if (data.get(i).isSelected) {
                            data.get(i).isSelected = false;
                            answers.set(i, false);
                            getViewByPosition(i, R.id.ratio_layout).setSelected(false);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ExamChildAdapter.ExamChildItem item) {
        holder.setText(R.id.stv_word, item.option).itemView.setSelected(item.isSelected);
    }

    /**
     * 设置答案
     * @param subjectType 题目类型: 1~3
     *                    一、单选题
     *                    二、多选题
     *                    三、判断题
     * @param options 选项, 选择题没有
     * @param answer A,  ABCDEFG, 正确
     */
    public void setList2(int subjectType, String options, String answer) {
        this.subjectType = subjectType;
        this.answer = answer;
        for (int i = 0; i < 8; i++) {
            answers.set(i, false);
        }
        List<ExamChildAdapter.ExamChildItem> data = getData();
        data.clear();
        switch (subjectType) {
            case 1:
            case 2:
                //获取题目数量
                int optionCount = options.split("\n").length + 1;
                for (int i = 0; i < optionCount; i++) {
                    data.add(new ExamChildAdapter.ExamChildItem(String.valueOf((char) ('A' + i))));
                }
                setList(data);
                break;
            case 3:
                data.add(new ExamChildAdapter.ExamChildItem("正确"));
                data.add(new ExamChildAdapter.ExamChildItem("错误"));
                setList(data);
                break;
            default:
                break;
        }
        RecyclerView.LayoutManager layoutManager = getRecyclerView().getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanCount(data.size());
        }
    }

    protected static class ExamChildItem {
        private       boolean isSelected = false;
        private final String  option;
        public ExamChildItem(String option) {
            this.option = option;
        }
    }

    /**
     * 答案是否正确
     * @return
     */
    public boolean isAnswerRight() {
        answerSelected.setLength(0);
        for (int i = 0; i < getDefItemCount(); i++) {
            if (answers.get(i)) answerSelected.append(getItem(i).option);
        }
        return answerSelected.toString().equals(answer);
    }
}
