package com.actor.cnpc_qhse_exams.activity;

import android.os.Bundle;
import android.text.Editable;

import androidx.annotation.NonNull;

import com.actor.cnpc_qhse_exams.adapter.SearchAdapter;
import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.cnpc_qhse_exams.databinding.ActivityMainBinding;
import com.actor.cnpc_qhse_exams.dialog.ConfirmDialog;
import com.actor.cnpc_qhse_exams.dialog.MainSettingDialog;
import com.actor.cnpc_qhse_exams.global.EasyWindowSubjects;
import com.actor.cnpc_qhse_exams.utils.SubjectReadUtils;
import com.actor.cnpc_qhse_exams.utils.SubjectSelectUtils;
import com.actor.database.greendao.GreenDaoUtils;
import com.actor.myandroidframework.utils.AssetsUtils;
import com.actor.myandroidframework.utils.toaster.ToasterUtils;
import com.actor.myandroidframework.widget.BaseItemDecoration;
import com.blankj.utilcode.util.SizeUtils;
import com.greendao.gen.SubjectDriverDao;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.window.EasyWindow;

import java.nio.charset.Charset;
import java.util.List;

/**
 * description: 交通安全基层站队QHSE标准化建设—驾驶员应知应会题库
 * @author    : ldf
 * date       : 2025/4/21 on 16:09
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> {


    private static final SubjectDriverDao DAO      = GreenDaoUtils.getDaoSession().getSubjectDriverDao();
    private final        SearchAdapter    mAdapter = new SearchAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTitle("中石油题库 - 驾驶员");
//        readSingleSelectSubjects();
//        readMultiSelectSubjects();
//        readJudgeSubjects();
//
//        if (true) return;

        viewBinding.ivSetting.setOnClickListener(v -> {
            new MainSettingDialog(this, mAdapter.isShowAnalysis(), (isShowAnalysis, isShow2Screen) -> {
                mAdapter.setIsShowAnalysis(isShowAnalysis);
                if (isShow2Screen) {
                    judgePermissionAndShowWindow();
                } else {
                    EasyWindow.recycleAllWindow();
                }
            }).show();
        });

        viewBinding.stvSearch.setOnClickListener(v -> {
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

    private void judgePermissionAndShowWindow() {
        if (XXPermissions.isGranted(this, Permission.SYSTEM_ALERT_WINDOW)) {
            new EasyWindowSubjects().show();
        } else {
            new ConfirmDialog(this, "权限申请说明",
                    "显示悬浮窗需要申请权限, 否则不能显示到其它应用上.",
                    isConfirmClick -> {
                if (!isConfirmClick) return;
                XXPermissions.with(mActivity)
                        .permission(Permission.SYSTEM_ALERT_WINDOW)
                        .request(new OnPermissionCallback() {
                            @Override
                            public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                                new EasyWindowSubjects().show();
                            }
                            @Override
                            public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                                OnPermissionCallback.super.onDenied(permissions, doNotAskAgain);
                                ToasterUtils.warning("您拒绝了权限!");
                            }
                        });
                }).show();
        }
    }


    /**
     * 读取单选题目
     */
    private void readSingleSelectSubjects() {
        /**
         * 题目类型:
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
        List<String> list11 = AssetsUtils.readAssets2List("qhse_11_single_select.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects11 = SubjectReadUtils.read2SelectList(list11, 1, 1);
        List<String> list21 = AssetsUtils.readAssets2List("qhse_21_single_select.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects21 = SubjectReadUtils.read2SelectList(list21, 2, 1);
        List<String> list31 = AssetsUtils.readAssets2List("qhse_31_single_select.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects31 = SubjectReadUtils.read2SelectList(list31, 3, 1);
        List<String> list41 = AssetsUtils.readAssets2List("qhse_41_single_select.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects41 = SubjectReadUtils.read2SelectList(list41, 4, 1);
        List<String> list51 = AssetsUtils.readAssets2List("qhse_51_single_select.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects51 = SubjectReadUtils.read2SelectList(list51, 5, 1);
        List<String> list61 = AssetsUtils.readAssets2List("qhse_61_single_select.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects61 = SubjectReadUtils.read2SelectList(list61, 6, 1);
        List<String> list71 = AssetsUtils.readAssets2List("qhse_71_single_select.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects71 = SubjectReadUtils.read2SelectList(list71, 7, 1);

        //存储
        GreenDaoUtils.insertInTx(DAO, subjects11);
        GreenDaoUtils.insertInTx(DAO, subjects21);
        GreenDaoUtils.insertInTx(DAO, subjects31);
        GreenDaoUtils.insertInTx(DAO, subjects41);
        GreenDaoUtils.insertInTx(DAO, subjects51);
        GreenDaoUtils.insertInTx(DAO, subjects61);
        GreenDaoUtils.insertInTx(DAO, subjects71);
    }

    /**
     * 读取多选题目
     */
    private void readMultiSelectSubjects() {
        /**
         * 题目类型:
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
        List<String> list12 = AssetsUtils.readAssets2List("qhse_12_multi_select.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects12 = SubjectReadUtils.read2SelectList(list12, 1, 2);
        List<String> list22 = AssetsUtils.readAssets2List("qhse_22_multi_select.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects22 = SubjectReadUtils.read2SelectList(list22, 2, 2);
        List<String> list32 = AssetsUtils.readAssets2List("qhse_32_multi_select.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects32 = SubjectReadUtils.read2SelectList(list32, 3, 2);
        List<String> list42 = AssetsUtils.readAssets2List("qhse_42_multi_select.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects42 = SubjectReadUtils.read2SelectList(list42, 4, 2);
        List<String> list72 = AssetsUtils.readAssets2List("qhse_72_multi_select.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects72 = SubjectReadUtils.read2SelectList(list72, 7, 2);

        //存储
        GreenDaoUtils.insertInTx(DAO, subjects12);
        GreenDaoUtils.insertInTx(DAO, subjects22);
        GreenDaoUtils.insertInTx(DAO, subjects32);
        GreenDaoUtils.insertInTx(DAO, subjects42);
        GreenDaoUtils.insertInTx(DAO, subjects72);
    }

    /**
     * 读取选择题
     */
    private void readJudgeSubjects() {
        /**
         * 题目类型:
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
        List<String> list13 = AssetsUtils.readAssets2List("qhse_13_judge.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects13 = SubjectReadUtils.read2Judges(list13, 1, 3);
        List<String> list23 = AssetsUtils.readAssets2List("qhse_23_judge.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects23 = SubjectReadUtils.read2Judges(list23, 2, 3);
        List<String> list33 = AssetsUtils.readAssets2List("qhse_33_judge.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects33 = SubjectReadUtils.read2Judges(list33, 3, 3);
        List<String> list43 = AssetsUtils.readAssets2List("qhse_43_judge.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects43 = SubjectReadUtils.read2Judges(list43, 4, 3);
        List<String> list53 = AssetsUtils.readAssets2List("qhse_53_judge.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects53 = SubjectReadUtils.read2Judges(list53, 5, 3);
        List<String> list73 = AssetsUtils.readAssets2List("qhse_73_judge.txt", Charset.defaultCharset().name());
        List<SubjectDriver> subjects73 = SubjectReadUtils.read2Judges(list73, 7, 3);

        //存储
        GreenDaoUtils.insertInTx(DAO, subjects13);
        GreenDaoUtils.insertInTx(DAO, subjects23);
        GreenDaoUtils.insertInTx(DAO, subjects33);
        GreenDaoUtils.insertInTx(DAO, subjects43);
        GreenDaoUtils.insertInTx(DAO, subjects53);
        GreenDaoUtils.insertInTx(DAO, subjects73);
    }
}