package com.actor.cnpc_qhse_exams.bean;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;

/**
 * description: 交通安全基层站队QHSE标准化建设—驾驶员应知应会题库
 *
 * @author : ldf
 * date       : 2025/4/20 on 14
 * @version 1.0
 */
@Keep
@Entity(nameInDb = "SUBJECT_DRIVER",  //这个实体类会在数据库中生成对应的表, 默认是大写并用_分开
        createInDb = true,            //如果数据库里没有这张表, 就在数据库里创建
        generateGettersSetters = true,//如果写成false, Dao里面的方法会报错...
        generateConstructors = false) //是否生成全参构造方法
public class SubjectDriver {

    @Transient//不映射到数据库
    public static final int SEX_GIRL = 0;//女
    @Transient
    public static final int SEX_BOY = 1;//男


    //下方几个是正常数据库字段
    @Id(autoincrement = true)
    private Long   id;      //表id(从0开始, 自增长. 如果自增长=true, 必须是Long)

    /**
     * 章节类型: 1~7
     * 11: 第一章 交通安全法律法规  一、单选题
     * 12: 第一章 交通安全法律法规  二、多选题
     * 13: 第一章 交通安全法律法规  三、判断题
     * 21: 第二章 规章制度         一、单选题
     * 22: 第二章 规章制度         二、多选题
     * 23: 第二章 规章制度         三、判断题
     * 31: 第三章 汽车基础知识      一、单选题
     * 32: 第三章 汽车基础知识      二、多选题
     * 33: 第三章 汽车基础知识      三、判断题
     * 41: 第四章 安全行车注意事项   一、单选题
     * 42: 第四章 安全行车注意事项   二、多选题
     * 43: 第四章 安全行车注意事项   三、判断题
     * 51: 第五章 防御性驾驶        一、单选题
     * 52: 无
     * 53: 第五章 防御性驾驶        三、判断题
     * 61: 第六章 风险控制          一、单选题
     * 71: 第七章 应急处置          一、单选题
     * 72: 第七章 应急处置          二、多选题
     * 73: 第七章 应急处置          三、判断题
     */
    private int chapterType;

    /**
     * 题目类型: 1~3
     * 一、单选题
     * 二、多选题
     * 三、判断题
     */
    private int subjectType;

    /**
     * 题目
     */
    @NonNull
    @NotNull
    private String subject;

    /**
     * 选项
     */
    @Nullable
    private String options;

    /**
     * 答案
     */
    @NonNull
    @NotNull
    private String answer;

    /**
     * 解析
     */
    @Nullable
    private String analysis;

//    @NotNull                //该字段不可以为空
//    @Unique                 //该字段唯一
//    private String idCard;  //身份证


    public SubjectDriver() {
    }

    public SubjectDriver(int chapterType, int subjectType, @NotNull @NonNull String subject,
                         @Nullable String options,
                         @NotNull @NonNull String answer,
                         @Nullable String analysis
    ) {
        this.chapterType = chapterType;
        this.subjectType = subjectType;
        this.subject = subject;
        this.options = options;
        this.answer = answer;
        this.analysis = analysis;
    }

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public String getSubject() {
    return this.subject;
}

public void setSubject(String subject) {
    this.subject = subject;
}

public String getAnswer() {
    return this.answer;
}

public void setAnswer(String answer) {
    this.answer = answer;
}

public String getAnalysis() {
    return this.analysis;
}

public void setAnalysis(String analysis) {
    this.analysis = analysis;
}

public String getOptions() {
    return this.options;
}

public void setOptions(String options) {
    this.options = options;
}

public int getChapterType() {
    return this.chapterType;
}

public void setChapterType(int chapterType) {
    this.chapterType = chapterType;
}

public int getSubjectType() {
    return this.subjectType;
}

public void setSubjectType(int subjectType) {
    this.subjectType = subjectType;
}
}