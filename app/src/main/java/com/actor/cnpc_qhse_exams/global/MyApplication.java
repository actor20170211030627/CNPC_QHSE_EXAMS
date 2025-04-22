package com.actor.cnpc_qhse_exams.global;

import com.actor.database.greendao.GreenDaoUtils;
import com.actor.myandroidframework.application.ActorApplication;
import com.actor.myandroidframework.utils.AssetsUtils;
import com.actor.myandroidframework.utils.ConfigUtils;
import com.actor.myandroidframework.utils.toaster.ToasterUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

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

    @Override
    protected void initAllKits() {
//        super.initAllKits();

        //配置工具类
        Utils.init(this);

//        ConfigUtils.initAllKits(this);
        //配置轮子哥的Log日志
        LogUtils.getConfig()
                .setLogSwitch(ConfigUtils.IS_APP_DEBUG)//是否能输出日志到 控制台/文件
                .setBorderSwitch(true)//是否打印边框
                .setConsoleSwitch(true)//是否能输出到 控制台
                .setLogHeadSwitch(true)//是否打印头(哪个文件哪一行, 点击能跳转相应文件)
                .setSingleTagSwitch(false)//日志是否在控制台开始位置输出, 默认true
                .setLog2FileSwitch(false);//是否能输出到 文件, 默认false

        //配置轮子哥吐司
        ToasterUtils.init(this);

        //初始化MMKV
//        String rootDir = MMKV.initialize(ConfigUtils.APPLICATION);

        //初始化崩溃处理
//        ConfigUtils.initDefaultUncaughtExceptionHandler(ConfigUtils.IS_APP_DEBUG, new CrashUtils.OnCrashListener() {
//            @Override
//            public void onCrash(CrashUtils.CrashInfo crashInfo) {
//                onUncaughtException(crashInfo.getThrowable());
//            }
//        });
    }
}
