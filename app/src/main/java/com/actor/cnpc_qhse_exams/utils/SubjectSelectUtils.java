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

    public static final SubjectDriverDao DAO = GreenDaoUtils.getDaoSession().getSubjectDriverDao();
    private static final List<WhereCondition> CONDS = new ArrayList<>(4);


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
        if (!TextUtils.isEmpty(subject)) {
            CONDS.add(SubjectDriverDao.Properties.Subject.like("%" + subject + "%"));
        }
        //if章节
        if (chapter == 0) {
            /**
             * @see SubjectDriver#chapterType
             */
            CONDS.add(SubjectDriverDao.Properties.ChapterType.between(1, 7));
        } else {
            CONDS.add(SubjectDriverDao.Properties.ChapterType.eq(chapter));
        }
        //if选择了类型
        if (subType > 0) {
            CONDS.add(SubjectDriverDao.Properties.SubjectType.eq(subType));
        }
        List<SubjectDriver> subjectDrivers = GreenDaoUtils.queryPage(DAO, page, size, CONDS.toArray(new WhereCondition[CONDS.size()]));
        CONDS.clear();
        return subjectDrivers;
    }
}
