package com.actor.cnpc_qhse_exams.utils;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.database.greendao.GreenDaoUtils;
import com.greendao.gen.SubjectDriverDao;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 描述
 * company    :
 *
 * @author : ldf
 * date       : 2025/4/21 on 21
 * @version 1.0
 */
public class SubjectSelectUtils {

    private static final SubjectDriverDao     DAO             = GreenDaoUtils.getDaoSession().getSubjectDriverDao();
    private static final List<WhereCondition> whereConditions = new ArrayList<>(3);

    /**
     * @param subject 搜索内容
     * @param chapter 章节
     * @param subType 题型
     */
    public static List<SubjectDriver> select(@Nullable String subject, int chapter, int subType) {
        if (TextUtils.isEmpty(subject) && chapter == 0 && subType == 0) {
            return GreenDaoUtils.queryAll(DAO);
        } else {
            whereConditions.clear();
            if (!TextUtils.isEmpty(subject)) {
                whereConditions.add(SubjectDriverDao.Properties.Subject.like("%" + subject + "%"));
            }
            //if选择了章节
            if (chapter > 0) {
                whereConditions.add(SubjectDriverDao.Properties.ChapterType.eq(chapter));
            }
            if (subType > 0) {
                whereConditions.add(SubjectDriverDao.Properties.SubjectType.eq(subType));
            }
            WhereCondition cond = whereConditions.remove(0);
            WhereCondition[] condMore = new WhereCondition[whereConditions.size()];
            for (int i = 0; i < whereConditions.size(); i++) {
                condMore[i] = whereConditions.get(i);
            }
            return  GreenDaoUtils.queryList(DAO, cond, condMore);
        }
    }
}
