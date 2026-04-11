package com.actor.cnpc_qhse_exams.activity;

import android.content.Intent;
import android.os.Bundle;

import com.actor.cnpc_qhse_exams.databinding.ActivityMainBinding;
import com.actor.cnpc_qhse_exams.dialog.ChapterTypeSettingDialog;

/**
 * description: 交通安全基层站队QHSE标准化建设—驾驶员应知应会题库
 * @author    : ldf
 * date       : 2025/4/21 on 16:09
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding.stvStudy.setOnClickListener(v -> {
            startActivity(new Intent(this, StudyActivity.class), v);
        });
        viewBinding.stvExam.setOnClickListener(v -> {
            new ChapterTypeSettingDialog(this, (chapter, type) -> {
                ExamActivity.start(MainActivity.this, v, chapter, type);
            }).show();
        });

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


        //正则测试 的代码
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
    }
}