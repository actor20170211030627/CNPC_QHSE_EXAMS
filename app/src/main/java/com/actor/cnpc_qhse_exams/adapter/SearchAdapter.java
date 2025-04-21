package com.actor.cnpc_qhse_exams.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.actor.cnpc_qhse_exams.R;
import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

/**
 * description: 描述
 * company    :
 *
 * @author : ldf
 * date       : 2025/4/21 on 18
 * @version 1.0
 */
public class SearchAdapter extends BaseQuickAdapter<SubjectDriver, BaseViewHolder> {

    private boolean isShowAnalysis = false;

    public SearchAdapter() {
        super(R.layout.item_search);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, SubjectDriver item) {
        String analysis = item.getAnalysis();
        boolean isAnalysisGone = !isShowAnalysis || TextUtils.isEmpty(analysis);
        holder.setText(R.id.tv_subject, item.getSubject())
                .setGone(R.id.tv_options, TextUtils.isEmpty(item.getOptions()))
                .setText(R.id.tv_options, item.getOptions())
                .setText(R.id.tv_answer, item.getAnswer())
                .setGone(R.id.tv_analysis, isAnalysisGone)
                .setText(R.id.tv_analysis, analysis);
    }

    public void setIsShowAnalysis(boolean isShowAnalysis) {
        if (this.isShowAnalysis != isShowAnalysis) {
            this.isShowAnalysis = isShowAnalysis;
            notifyDataSetChanged();
        }
    }

    public boolean isShowAnalysis() {
        return isShowAnalysis;
    }
}
