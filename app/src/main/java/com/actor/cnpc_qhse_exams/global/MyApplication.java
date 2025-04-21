package com.actor.cnpc_qhse_exams.global;

import com.actor.database.greendao.GreenDaoUtils;
import com.actor.myandroidframework.application.ActorApplication;
import com.actor.myandroidframework.utils.AssetsUtils;
import com.actor.myandroidframework.utils.ConfigUtils;

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
        AssetsUtils.copyFile2InternalDbsDir(false, "cnpc_qhse.db3");
        GreenDaoUtils.init(this, ConfigUtils.IS_APP_DEBUG, "cnpc_qhse.db3", null);
    }
}
