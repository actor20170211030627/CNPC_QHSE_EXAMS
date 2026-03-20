package com.actor.cnpc_qhse_exams.activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.inputmethod.EditorInfo;

import com.actor.cnpc_qhse_exams.adapter.SearchAdapter;
import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.cnpc_qhse_exams.databinding.ActivityMainBinding;
import com.actor.cnpc_qhse_exams.dialog.MainSettingDialog;
import com.actor.cnpc_qhse_exams.utils.SubjectSelectUtils;
import com.actor.myandroidframework.utils.LogUtils;
import com.actor.myandroidframework.widget.BaseItemDecoration;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;

import java.util.List;

/**
 * description: 交通安全基层站队QHSE标准化建设—驾驶员应知应会题库
 * @author    : ldf
 * date       : 2025/4/21 on 16:09
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private final SearchAdapter    mAdapter = new SearchAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTitle("中石油题库 - 驾驶员");
//        TxtReadUtils.readTxt2SubjectDrivers();
//        /**
//         * 写入版本信息
//         */
//        DBVersionDao DAO = GreenDaoUtils.getDaoSession().getDBVersionDao();
//        List<Integer> versionCodes = GreenDaoUtils.queryAll(DAO).stream().map(DBVersion::getVersionCode).collect(Collectors.toList());
//        if (!versionCodes.contains(20250421)) {
//            GreenDaoUtils.insert(DAO, new DBVersion("1.0", 20250421, "第1版, 浮窗输入&滑动有问题"));
//        }
//        if (!versionCodes.contains(20250422)) {
//            GreenDaoUtils.insert(DAO, new DBVersion("1.1", 20250422, "第2版, remove EasyWindow, 开启代码混淆, 打包排除txt, 优化app到2.16MB"));
//        }
//        if (!versionCodes.contains(20260319)) {
//            GreenDaoUtils.insert(DAO, new DBVersion("1.2", 20260319, "第3版, 改为读取整个txt文, 重新填装题目, 新增考核点字段, 新增是否显示答案功能, 修复有些题目有图片没显示的问题, 更换logo"));
//        }


//        final Pattern PATTERN_OPTIONS = Pattern.compile("(?:答文|答案)[:：]\\s*[（(]?([A-ZＡ-Ｚ\\s]+|正确|错误)[）)]?\\s*$");
//        String[] lines = {
//                "答文：C",
//                "答案：ABD",
//                "答文:C",
//                "答文：",
//                "单选 12 答文：B",
//                "单选 23 答文：B",
//                "单选选 27 答文：D",
//                "答文：正确",
//                "答文：错误",
//                "答文：（D）",
//                "答文：（A D）",
//                "答案：(A D)"
//        };
//        for (String s : lines) {
//            Matcher matcher = PATTERN_OPTIONS.matcher(s);
//            while (matcher.find()) {
//                String option = matcher.group(1); // 取出带空格的内容
//                LogUtils.errorFormat("option = '%s'", option);
////                String answer = rawAnswer.replaceAll("\\s+", ""); // 🔥 去掉所有空格
////                LogUtils.errorFormat("s = %s, rawAnswer = '%s', answer = '%s'", s, rawAnswer, answer);
//            }
//            LogUtils.error("\n");
//        }

//        TxtReadUtils.readSingleSelectSubjects();
//        TxtReadUtils.readMultiSelectSubjects();
//        TxtReadUtils.readJudgeSubjects();
//        if (true) return;

        viewBinding.ivSetting.setOnClickListener(v -> {
            new MainSettingDialog(this, mAdapter.isShowTestPoint(), mAdapter.isShowAnswer(), mAdapter.isShowAnalysis(), (isShowTestPoint, isShowAnswer, isShowAnalysis, isShow2Screen) -> {
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
            Editable editable = viewBinding.setSearch.getText();
            String subject = null;
            if (editable != null) subject = editable.toString().trim();
            //章节
            int chapter = viewBinding.bsChapters.getSelectedItemPosition();
            int subType = viewBinding.bsTypes.getSelectedItemPosition();

            List<SubjectDriver> subjectDrivers = SubjectSelectUtils.select(subject, chapter, subType);
            mAdapter.setList(subjectDrivers);
        });

        viewBinding.recyclerView.addItemDecoration(new BaseItemDecoration(0f, SizeUtils.dp2px(5f)));
        viewBinding.recyclerView.setAdapter(mAdapter);
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