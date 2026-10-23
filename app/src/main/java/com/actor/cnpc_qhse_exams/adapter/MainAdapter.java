package com.actor.cnpc_qhse_exams.adapter;

import androidx.annotation.NonNull;

import com.actor.cnpc_qhse_exams.R;
import com.actor.cnpc_qhse_exams.activity.BaseActivity;
import com.actor.cnpc_qhse_exams.activity.ExamActivity;
import com.actor.cnpc_qhse_exams.activity.StudyActivity;
import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.cnpc_qhse_exams.dialog.ChapterTypeSettingDialog;
import com.actor.database.greendao.GreenDaoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.greendao.gen.SubjectDriverDao;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 描述
 * company    :
 *
 * @author : ldf
 * date       : 2026/10/20 on 18
 * @version 1.0
 */
public class MainAdapter extends BaseQuickAdapter<SubjectDriver, BaseViewHolder> {

    public MainAdapter(BaseActivity<?> activity, List<SubjectDriver> data) {
        super(R.layout.item_main, data);
        addChildClickViewIds(R.id.stv_study, R.id.stv_exam);
        String[] array = activity.getResources().getStringArray(R.array.chapters);
        List<String> chapters = new ArrayList<>(array.length);
        SubjectDriverDao dao = GreenDaoUtils.getDaoSession().getSubjectDriverDao();
        setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            long version = getItem(position).getVersion();
            List<SubjectDriver> subjectDrivers = GreenDaoUtils.queryRaw(dao,
                    "WHERE " + SubjectDriverDao.Properties.Version.columnName + " = " + version
                            + " GROUP BY " + SubjectDriverDao.Properties.ChapterType.columnName
            );
            /**
             * @see SubjectDriver#chapterType
             */
            chapters.clear();
            for (SubjectDriver subjectDriver : subjectDrivers) {
                int chapterType = subjectDriver.getChapterType();
                if (chapterType > 0 && chapterType < array.length) chapters.add(array[chapterType]);
            }
            if (!chapters.isEmpty()) chapters.add(0, array[0]);   //全部

            if (id == R.id.stv_study) {
                StudyActivity.start(activity, view, version, chapters);
            } else if (id == R.id.stv_exam) {
                new ChapterTypeSettingDialog(activity, version, chapters, (version1, chapter, type) -> {
                    ExamActivity.start(activity, view, version1, chapter, type);
                }).show();
            }
        });
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, SubjectDriver item) {
        holder.setText(R.id.stv_date, String.valueOf(item.getVersion()));
    }
}
