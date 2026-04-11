package com.actor.cnpc_qhse_exams.activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.inputmethod.EditorInfo;

import com.actor.cnpc_qhse_exams.adapter.StudyAdapter;
import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.cnpc_qhse_exams.databinding.ActivityStudyBinding;
import com.actor.cnpc_qhse_exams.dialog.StudySettingDialog;
import com.actor.cnpc_qhse_exams.utils.SubjectSelectUtils;
import com.actor.myandroidframework.utils.BRVUtils;
import com.actor.myandroidframework.utils.LogUtils;
import com.actor.myandroidframework.widget.BaseItemDecoration;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;

import java.util.List;

/**
 * description: 学习
 * company    :
 * @author    : ldf
 * date       : 2026/4/11 on 17:53
 */
public class StudyActivity extends BaseActivity<ActivityStudyBinding> {

    private final StudyAdapter mAdapter = new StudyAdapter();
    private final int          SIZE     = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.ivBack.setOnClickListener(v -> onBackPressed());

        viewBinding.ivSetting.setOnClickListener(v -> {
            new StudySettingDialog(this, mAdapter.isShowTestPoint(), mAdapter.isShowAnswer(),
                    mAdapter.isShowAnalysis(), (isShowTestPoint, isShowAnswer, isShowAnalysis, isShow2Screen) -> {
                mAdapter.setIsShowSettings(isShowTestPoint, isShowAnswer, isShowAnalysis);
                if (isShow2Screen) {
                    judgePermissionAndShowWindow();
                } else {
//                    EasyWindow.recycleAllWindow();
                }
            }).show();
        });

        //搜索按钮点击监听
        viewBinding.setSearch.setOnEditorActionListener((v, actionId, event) -> {
            LogUtils.errorFormat("actionId = %d, event = %s", actionId, event);
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewBinding.stvSearch.callOnClick();
                return true;
            }
            return false;
        });

        viewBinding.ivClose.setOnClickListener(v -> {
            viewBinding.setSearch.setText("");
        });

        viewBinding.stvSearch.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(v);
            getList(true);
        });
        viewBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            getList(true);
        });

        viewBinding.recyclerView.addItemDecoration(new BaseItemDecoration(0f, SizeUtils.dp2px(5f)));
        viewBinding.recyclerView.setAdapter(mAdapter);
        BRVUtils.setEmptyView(mAdapter);
        BRVUtils.setOnLoadMoreListener(mAdapter, () -> {
            getList(false);
        });

        getList(true);
    }

    /**
     * 获取数据
     * @param isRefresh 是否是下拉刷新
     */
    private void getList(boolean isRefresh) {
        Editable editable = viewBinding.setSearch.getText();
        String subject = null;
        if (editable != null) subject = editable.toString().trim();
        //章节
        int chapter = viewBinding.bsChapters.getSelectedItemPosition();
        int subType = viewBinding.bsTypes.getSelectedItemPosition();
        int page = BRVUtils.getPage(mAdapter, isRefresh, SIZE);
        List<SubjectDriver> subjectDrivers = SubjectSelectUtils.selectPage(subject, chapter, subType, page, SIZE);

        viewBinding.swipeRefreshLayout.setRefreshing(false);
        if (isRefresh) {
            mAdapter.setList(subjectDrivers);
        } else if (subjectDrivers != null) {
            mAdapter.addData(subjectDrivers);
        }
        BRVUtils.setLoadMoreStateBySize(mAdapter, subjectDrivers, SIZE);
    }

    /**
     * 判断权限 & 显示全局浮窗
     */
    private void judgePermissionAndShowWindow() {
//        if (XXPermissions.isGranted(this, Permission.SYSTEM_ALERT_WINDOW)) {
//            new EasyWindowSubjects().show();
//        } else {
//            new ConfirmDialog(this, "权限申请说明",
//                    "显示悬浮窗需要申请权限, 否则不能显示到其它应用上.",
//                    isConfirmClick -> {
//                if (!isConfirmClick) return;
//                XXPermissions.with(mActivity)
//                        .permission(Permission.SYSTEM_ALERT_WINDOW)
//                        .request(new OnPermissionCallback() {
//                            @Override
//                            public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
//                                new EasyWindowSubjects().show();
//                            }
//                            @Override
//                            public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
//                                OnPermissionCallback.super.onDenied(permissions, doNotAskAgain);
//                                ToasterUtils.warning("您拒绝了权限!");
//                            }
//                        });
//                }).show();
//        }
    }
}