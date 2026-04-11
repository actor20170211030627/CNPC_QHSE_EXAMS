package com.actor.cnpc_qhse_exams.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.actor.cnpc_qhse_exams.R;
import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.myandroidframework.utils.TextUtils2;
import com.actor.myandroidframework.utils.glide.GlideUtils;
import com.actor.myandroidframework.widget.BaseItemDecoration;
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
public class ExamAdapter extends BaseQuickAdapter<SubjectDriver, BaseViewHolder> {

    private final OnCommitClickListener commitClickListener;

    public ExamAdapter(OnCommitClickListener listener) {
        super(R.layout.item_exam_adapter);
        this.commitClickListener = listener;
        //提交
        addChildClickViewIds(R.id.stv_commit);
        setOnItemChildClickListener((adapter, view, position) -> {
            RecyclerView recyclerView = (RecyclerView) getViewByPosition(position, R.id.recycler_view);
            ExamChildAdapter adapter1 = (ExamChildAdapter) recyclerView.getAdapter();
            boolean answerRight = ((ExamChildAdapter) adapter1).isAnswerRight();
            SubjectDriver item = getItem(position);
            item.isShowAnswer = true;
            notifyItemChanged(position, item.isShowAnswer);
            //if答错, 抖动动画
            if (!answerRight) {
                //抖动动画
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                getViewByPosition(position, R.id.tv_answer).startAnimation(animation);
            }
            if (commitClickListener != null) commitClickListener.OnCommitClick(position, item.getId(), answerRight);
        });
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, SubjectDriver item) {
        int position = holder.getAbsoluteAdapterPosition();
        boolean isTestPointEmpty = TextUtils.isEmpty(item.getTestPoint());
        boolean isAnalysisEmpty = TextUtils.isEmpty(item.getAnalysis());
        RecyclerView recyclerView = holder.setText(R.id.tv_subject, item.getSubject())
//                .setGone(R.id.tv_options, TextUtils.isEmpty(item.getOptions()))
                .setText(R.id.tv_options, item.getOptions())
                .setVisible(R.id.tv_answer, item.isShowAnswer)
                .setText(R.id.tv_answer, "答案：" + item.getAnswer())
                .setVisible(R.id.tv_analysis, !isAnalysisEmpty && item.isShowAnswer)
                .setText(R.id.tv_analysis, "解析：" + item.getAnalysis())
                .setVisible(R.id.tv_test_point, !isTestPointEmpty)
                .setText(R.id.tv_test_point, "考核点：" + item.getTestPoint())
                .setText(R.id.tv_word_pos_total, TextUtils2.getStringFormat("进度 %d/%d", position + 1, getDefItemCount()))
                .getView(R.id.recycler_view)
        ;
        ExamChildAdapter adapter = (ExamChildAdapter) recyclerView.getAdapter();
        if (adapter == null) {
            adapter = new ExamChildAdapter();
            recyclerView.setAdapter(adapter);
        }
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(new BaseItemDecoration(20, 0));
        }
        adapter.setList2(item.getSubjectType(), item.getOptions(), item.getAnswer());

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
    protected void convert(@NonNull BaseViewHolder holder, SubjectDriver item, @NonNull List<?> payloads) {
        super.convert(holder, item, payloads);
        if (!payloads.isEmpty()) {
            Object isShowAnswer = payloads.get(0);
            if (isShowAnswer instanceof Boolean) {
                if ((Boolean) isShowAnswer) {
                    holder.setVisible(R.id.tv_answer, true);
                    boolean isAnalysisEmpty = TextUtils.isEmpty(item.getAnalysis());
                    holder.setVisible(R.id.tv_analysis, !isAnalysisEmpty);
                } else {
                    holder.setVisible(R.id.tv_answer, false);
                    holder.setVisible(R.id.tv_analysis, false);
                }
            }
        }
    }

    public interface OnCommitClickListener {
        public void OnCommitClick(int position, Long id, boolean isAnswerRight);
    }
}
