package com.actor.cnpc_qhse_exams.global;

import com.actor.cnpc_qhse_exams.bean.DBVersion;
import com.actor.database.greendao.GreenDaoUtils;
import com.actor.myandroidframework.application.ActorApplication;
import com.actor.myandroidframework.utils.AssetsUtils;
import com.actor.myandroidframework.utils.ConfigUtils;
import com.actor.myandroidframework.utils.TextUtils2;
import com.blankj.utilcode.util.AppUtils;
import com.greendao.gen.DBVersionDao;

/**
 * description: 描述
 * company    :
 *
 * @author : ldf
 * date       : 2025/4/19 on 17
 * @version 1.0
 */
public class MyApplication extends ActorApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        String dbName = "cnpc_qhse.db3";
        AssetsUtils.copyFile2InternalDbsDir(false, dbName);
        GreenDaoUtils.init(this, ConfigUtils.IS_APP_DEBUG, dbName, null);
        DBVersionDao dao = GreenDaoUtils.getDaoSession().getDBVersionDao();
        String sql = TextUtils2.getStringFormat("WHERE %s = (SELECT MAX(%s) FROM %s)",
                DBVersionDao.Properties.VersionCode.columnName,
                DBVersionDao.Properties.VersionCode.columnName,
                DBVersionDao.TABLENAME
        );
        DBVersion dbVersion = GreenDaoUtils.queryRawCreate(dao, sql).unique();
        //每一个版本都重新copy一次数据库
        if (dbVersion != null && AppUtils.getAppVersionCode() != dbVersion.getVersionCode()) {
            AssetsUtils.copyFile2InternalDbsDir(true, dbName);
        }
    }
}
