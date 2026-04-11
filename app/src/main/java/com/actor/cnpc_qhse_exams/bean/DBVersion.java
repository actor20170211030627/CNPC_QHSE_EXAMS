package com.actor.cnpc_qhse_exams.bean;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * description: 数据库版本
 * company    :
 *
 * @author : ldf
 * date       : 2026/3/20 on 12
 * @version 1.0
 */
@Keep
@Entity(nameInDb = "DB_Version",  //这个实体类会在数据库中生成对应的表, 默认是大写并用_分开
        createInDb = true,            //如果数据库里没有这张表, 就在数据库里创建
        generateGettersSetters = true,//如果写成false, Dao里面的方法会报错...
        generateConstructors = false) //是否生成全参构造方法
public class DBVersion {

    @Id(autoincrement = true)
    private Long   id;      //表id(从0开始, 自增长. 如果自增长=true, 必须是Long)

    /**
     * 版本名称
     */
    @NonNull
    @NotNull
    @Unique                 //该字段唯一
    private String versionName;

    /**
     * 版本号
     */
    @Unique                 //该字段唯一
    private int versionCode;

    /**
     * 更新说明
     */
    @NonNull
    @NotNull
    private String updateDoc;

    public DBVersion() {
    }

    public DBVersion(@NonNull String versionName, int versionCode, @NonNull String updateDoc) {
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.updateDoc = updateDoc;
    }

    public Long getId() {
    return this.id;
}

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getUpdateDoc() {
        return this.updateDoc;
    }

    public void setUpdateDoc(String updateDoc) {
        this.updateDoc = updateDoc;
    }
}
