package com.actor.cnpc_qhse_exams.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.actor.cnpc_qhse_exams.R;
import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.myandroidframework.utils.glide.GlideUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.Collection;
import java.util.List;

/**
 * description: 描述
 * company    :
 *
 * @author : ldf
 * date       : 2025/4/21 on 18
 * @version 1.0
 */
public class StudyAdapter extends BaseQuickAdapter<SubjectDriver, BaseViewHolder> implements LoadMoreModule {

    private boolean isShowTestPoint = false, isShowAnswer = false, isShowAnalysis = false;
    private final String click2ShowAnswer = "点击显示答案";

    public StudyAdapter() {
        super(R.layout.item_study_adapter);
        setOnItemClickListener((adapter, view, position) -> {
            SubjectDriver item = getItem(position);
            item.isShowAnswer = !item.isShowAnswer;
            notifyItemChanged(position, item.isShowAnswer);
        });
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, SubjectDriver item) {
        boolean isTestPointGone = !isShowTestPoint || TextUtils.isEmpty(item.getTestPoint());
        boolean isAnalysisGone = !isShowAnalysis || TextUtils.isEmpty(item.getAnalysis());
        holder.setText(R.id.tv_subject, item.getSubject())
                .setGone(R.id.tv_options, TextUtils.isEmpty(item.getOptions()))
                .setText(R.id.tv_options, item.getOptions())
                .setText(R.id.tv_answer, item.isShowAnswer ? "答案：" + item.getAnswer() : click2ShowAnswer)
                .setGone(R.id.tv_analysis, isAnalysisGone)
                .setText(R.id.tv_analysis, "解析：" + item.getAnalysis())
                .setGone(R.id.tv_test_point, isTestPointGone)
                .setText(R.id.tv_test_point, "考核点：" + item.getTestPoint())
        ;
        //if标题有图片
        if (!TextUtils.isEmpty(item.getSubjectImage())) {
            ImageView ivSubjectImage = holder.getView(R.id.iv_subject_image);
            ivSubjectImage.setVisibility(View.VISIBLE);
            GlideUtils.loadAsset(ivSubjectImage, item.getSubjectImage());
        } else {
            holder.setGone(R.id.iv_subject_image, true);
        }

        //if选项有图片
        boolean hasOptionImages = !TextUtils.isEmpty(item.getOptionImages());
        ImageView ivOptionA = holder.getView(R.id.iv_option_a);
        ImageView ivOptionB = holder.getView(R.id.iv_option_b);
        ImageView ivOptionC = holder.getView(R.id.iv_option_c);
        ImageView ivOptionD = holder.getView(R.id.iv_option_d);
        if (hasOptionImages) {
            String[] optionImages = item.getOptionImages().split("\n");
            if (optionImages.length > 0) {
                ivOptionA.setVisibility(View.VISIBLE);
                GlideUtils.loadAsset(ivOptionA, optionImages[0]);
            }
            if (optionImages.length > 1) {
                ivOptionB.setVisibility(View.VISIBLE);
                GlideUtils.loadAsset(ivOptionB, optionImages[1]);
            }
            if (optionImages.length > 2) {
                ivOptionC.setVisibility(View.VISIBLE);
                GlideUtils.loadAsset(ivOptionC, optionImages[2]);
            }
            if (optionImages.length > 3) {
                ivOptionD.setVisibility(View.VISIBLE);
                GlideUtils.loadAsset(ivOptionD, optionImages[3]);
            }
        } else {
            ivOptionA.setVisibility(View.INVISIBLE);
            ivOptionB.setVisibility(View.INVISIBLE);
            ivOptionC.setVisibility(View.INVISIBLE);
            ivOptionD.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, SubjectDriver item, List<?> payloads) {
        super.convert(holder, item, payloads);
        if (!payloads.isEmpty()) {
            Object isShowAnswer = payloads.get(0);
            if (isShowAnswer instanceof Boolean) {
                if ((Boolean) isShowAnswer) {
                    holder.setText(R.id.tv_answer, "答案：" + item.getAnswer());
                } else {
                    holder.setText(R.id.tv_answer, click2ShowAnswer);
                }
            }
        }
    }

    public void setIsShowSettings(boolean isShowTestPoint, boolean isShowAnswer, boolean isShowAnalysis) {
        if (this.isShowTestPoint == isShowTestPoint &&
                this.isShowAnswer == isShowAnswer &&
                this.isShowAnalysis == isShowAnalysis
        ) return;
        this.isShowTestPoint = isShowTestPoint;
        //if答案的显示状态重新设置了, 才修改item的答案显示状态
        if (this.isShowAnswer != isShowAnswer) {
            this.isShowAnswer = isShowAnswer;
            for (SubjectDriver datum : getData()) {
                datum.isShowAnswer = isShowAnswer;
            }
        }
        this.isShowAnalysis = isShowAnalysis;
        notifyDataSetChanged();
    }

    @Override
    public void setList(Collection<? extends SubjectDriver> list) {
        if (list != null && !list.isEmpty() && list.stream().findFirst().get().isShowAnswer != isShowAnswer) {
            //检查并重设 isShowAnswer
            for (SubjectDriver subjectDriver : list) subjectDriver.isShowAnswer = isShowAnswer;
        }
        super.setList(list);
    }

    public boolean isShowTestPoint() {
        return isShowTestPoint;
    }

    public boolean isShowAnswer() {
        return isShowAnswer;
    }

    public boolean isShowAnalysis() {
        return isShowAnalysis;
    }

    @NonNull
    @Override
    public BaseLoadMoreModule addLoadMoreModule(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter) {
        return new BaseLoadMoreModule(baseQuickAdapter);
    }
}
