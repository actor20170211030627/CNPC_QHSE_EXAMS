package com.actor.cnpc_qhse_exams.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.actor.myandroidframework.activity.ViewBindingActivity;
import com.blankj.utilcode.util.BarUtils;

/**
 * description: 描述
 * company    :
 *
 * @author : ldf
 * date       : 2025/3/26 on 17
 * @version 1.0
 */
public class BaseActivity<VB extends ViewBinding> extends ViewBindingActivity<VB> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarLightMode(this, true);
    }
}
