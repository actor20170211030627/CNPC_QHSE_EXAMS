package com.actor.cnpc_qhse_exams.utils;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.database.greendao.GreenDaoUtils;
import com.greendao.gen.SubjectDriverDao;

import org.greenrobot.greendao.query.QueryBuilder;

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

    public static final SubjectDriverDao     DAO             = GreenDaoUtils.getDaoSession().getSubjectDriverDao();


    /**
     * 查询
     * @param subject 搜索内容
     * @param chapter 章节
     * @param subType 题型
     * @return
     */
    public static List<SubjectDriver> select(@Nullable String subject, int chapter, int subType) {
        return selectPage(subject, chapter, subType, 1, Integer.MAX_VALUE);
    }

    /**
     * 分页查询
     * @param subject 搜索内容
     * @param chapter 章节
     * @param subType 题型
     * @param page 分页查询, 第几页, 从1开始
     * @param size 每页多少条数据
     */
    public static List<SubjectDriver> selectPage(@Nullable String subject, int chapter, int subType, int page, int size) {
        QueryBuilder<SubjectDriver> queryBuilder = DAO.queryBuilder()
                .offset((page - 1) * size)
                .limit(size);
        if (TextUtils.isEmpty(subject) && chapter == 0 && subType == 0) return queryBuilder.list();

        if (!TextUtils.isEmpty(subject)) {
            queryBuilder.where(SubjectDriverDao.Properties.Subject.like("%" + subject + "%"));
        }
        //if选择了章节
        if (chapter > 0) {
            queryBuilder.where(SubjectDriverDao.Properties.ChapterType.eq(chapter));
        }
        //if选择了类型
        if (subType > 0) {
            queryBuilder.where(SubjectDriverDao.Properties.SubjectType.eq(subType));
        }
        return  queryBuilder.list();
    }
}
