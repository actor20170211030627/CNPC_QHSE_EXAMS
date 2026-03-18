package com.actor.cnpc_qhse_exams.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;

import com.actor.cnpc_qhse_exams.adapter.SearchAdapter;
import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.cnpc_qhse_exams.databinding.ActivityMainBinding;
import com.actor.cnpc_qhse_exams.dialog.MainSettingDialog;
import com.actor.cnpc_qhse_exams.utils.SubjectReadUtils;
import com.actor.cnpc_qhse_exams.utils.SubjectSelectUtils;
import com.actor.database.greendao.GreenDaoUtils;
import com.actor.myandroidframework.utils.AssetsUtils;
import com.actor.myandroidframework.utils.LogUtils;
import com.actor.myandroidframework.widget.BaseItemDecoration;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.greendao.gen.SubjectDriverDao;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * description: 交通安全基层站队QHSE标准化建设—驾驶员应知应会题库
 * @author    : ldf
 * date       : 2025/4/21 on 16:09
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> {


    private final SubjectDriverDao DAO      = GreenDaoUtils.getDaoSession().getSubjectDriverDao();
    private final SearchAdapter    mAdapter = new SearchAdapter();

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


    /**
     * 读取文本, 将各种题目类型读取成List
     *
     * 1.用豆包将PDF 转换成Txt, 会读取错误, 不知道咋回事, 例:
     *   48.【考核点：层级+专业；题型：单选题；难度：易；类型：基础】
     *   乘车人负有（ ）的职责和义务。。
     *   （A）行车安全监督；
     *   （B）完成用车申请；
     *   （C）周六、周日；                                //会读成: (C) 协助驾驶；
     *   答文：C
     *   解析：
     *
     * 2.Ctrl + A全选PDF内容, 然后Ctrl + C复制文本到Txt    //文字格式有误, 有些不应该同一行的文字在同一行
     *
     * 3.在网站转换: https://convertio.co/zh/pdf-txt/     //靠谱的多
     *
     *
     *
     * 修改错题格式(...):
     *  128、采用液压刹车系统的小轿车是利用液体的(           )原理。
     *  可压缩 （B）可传递 （C）不可压缩 （D）不可传递                  //没有"（A）", 需补上
     *
     * 24.【考核点：层级+专业；题型：单选题；难度：中；类型：专业】
     * （A）类火灾是指（ ）火灾。                               //这一行改成"（Ą）类火灾是指（ ）火灾。", 前面+空格, 否则读取不到标题
     * （A）固体物质；
     * （B）液体；
     * （C）气体；
     * （D）金属；
     * 答文：A
     * 解析：
     */
    private void readTxt2Lists() {
        String content = AssetsUtils.readAssets2String("交通安全基层站队QHSE标准化建设—驾驶员应知应会题库20260316.txt", Charset.defaultCharset().name());
        if (TextUtils.isEmpty(content)) return;
        //换页键, 换页符, 光标移到下一页
        content = content.replaceAll("\f", "");
        //分割成每一行
        String[] splits = content.split(System.lineSeparator());
        List<String> lines = new ArrayList<>(splits.length);
        for (String s : splits) {
            String split = s.trim();
            if (!split.isEmpty()) lines.add(split);
        }

        //遍历 & 判断题目类型
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
         * 53: 第五章 防御性驾驶        二、判断题
         * 61: 第六章 风险控制          一、单选题
         * 62: 无
         * 63: 无
         * 71: 第七章 应急处置          一、单选题
         * 72: 第七章 应急处置          二、多选题
         * 73: 第七章 应急处置          三、判断题
         */
        int chapterType = 0, subjectType = 0;
        List<String> list11 = new ArrayList<>(), list12 = new ArrayList<>(), list13 = new ArrayList<>();
        List<String> list21 = new ArrayList<>(), list22 = new ArrayList<>(), list23 = new ArrayList<>();
        List<String> list31 = new ArrayList<>(), list32 = new ArrayList<>(), list33 = new ArrayList<>();
        List<String> list41 = new ArrayList<>(), list42 = new ArrayList<>(), list43 = new ArrayList<>();
        List<String> list51 = new ArrayList<>(), list52 = new ArrayList<>(), list53 = new ArrayList<>();
        List<String> list61 = new ArrayList<>(), list62 = new ArrayList<>(), list63 = new ArrayList<>();
        List<String> list71 = new ArrayList<>(), list72 = new ArrayList<>(), list73 = new ArrayList<>();
        for (String line : lines) {
            //章节类型
            if (line.equals("第一章 交通安全法律法规")) { chapterType = 1; continue; }
            if (line.equals("第二章 规章制度")) {         chapterType = 2; continue; }
            if (line.equals("第三章 汽车基础知识")) {     chapterType = 3; continue; }
            if (line.equals("第四章 安全行车注意事项")) { chapterType = 4; continue; }
            if (line.equals("第五章 防御性驾驶")) {       chapterType = 5; continue; }
            if (line.equals("第六章 风险控制")) {         chapterType = 6; continue; }
            if (line.equals("第七章 应急处置")) {         chapterType = 7; continue; }
            //题目类型
            if (line.equals("一、单选题")) {                             subjectType = 1; continue; }
            if (line.equals("二、多选题")) {                             subjectType = 2; continue; }
            if (line.equals("二、判断题") || line.equals("三、判断题")) { subjectType = 3; continue; }

            //读取题目
            if (chapterType == 1 && subjectType == 1) { list11.add(line); continue; }
            if (chapterType == 1 && subjectType == 2) { list12.add(line); continue; }
            if (chapterType == 1 && subjectType == 3) { list13.add(line); continue; }
            if (chapterType == 2 && subjectType == 1) { list21.add(line); continue; }
            if (chapterType == 2 && subjectType == 2) { list22.add(line); continue; }
            if (chapterType == 2 && subjectType == 3) { list23.add(line); continue; }
            if (chapterType == 3 && subjectType == 1) { list31.add(line); continue; }
            if (chapterType == 3 && subjectType == 2) { list32.add(line); continue; }
            if (chapterType == 3 && subjectType == 3) { list33.add(line); continue; }
            if (chapterType == 4 && subjectType == 1) { list41.add(line); continue; }
            if (chapterType == 4 && subjectType == 2) { list42.add(line); continue; }
            if (chapterType == 4 && subjectType == 3) { list43.add(line); continue; }
            if (chapterType == 5 && subjectType == 1) { list51.add(line); continue; }
            if (chapterType == 5 && subjectType == 2) { list52.add(line); continue; }
            if (chapterType == 5 && subjectType == 3) { list53.add(line); continue; }
            if (chapterType == 6 && subjectType == 1) { list61.add(line); continue; }
            if (chapterType == 6 && subjectType == 2) { list62.add(line); continue; }
            if (chapterType == 6 && subjectType == 3) { list63.add(line); continue; }
            if (chapterType == 7 && subjectType == 1) { list71.add(line); continue; }
            if (chapterType == 7 && subjectType == 2) { list72.add(line); continue; }
            if (chapterType == 7 && subjectType == 3) { list73.add(line); continue; }
        }

        //读取成题目
        List<SubjectDriver> subjects11 = SubjectReadUtils.read2SelectList(list11, 1, 1);
        List<SubjectDriver> subjects12 = SubjectReadUtils.read2SelectList(list12, 1, 2);
        List<SubjectDriver> subjects13 = SubjectReadUtils.read2Judges(list13, 1, 3);
        List<SubjectDriver> subjects21 = SubjectReadUtils.read2SelectList(list21, 2, 1);
        List<SubjectDriver> subjects22 = SubjectReadUtils.read2SelectList(list22, 2, 2);
        List<SubjectDriver> subjects23 = SubjectReadUtils.read2Judges(list23, 2, 3);
        List<SubjectDriver> subjects31 = SubjectReadUtils.read2SelectList(list31, 3, 1);
        List<SubjectDriver> subjects32 = SubjectReadUtils.read2SelectList(list32, 3, 2);
        List<SubjectDriver> subjects33 = SubjectReadUtils.read2Judges(list33, 3, 3);
        List<SubjectDriver> subjects41 = SubjectReadUtils.read2SelectList(list41, 4, 1);
        List<SubjectDriver> subjects42 = SubjectReadUtils.read2SelectList(list42, 4, 2);
        List<SubjectDriver> subjects43 = SubjectReadUtils.read2Judges(list43, 4, 3);
        List<SubjectDriver> subjects51 = SubjectReadUtils.read2SelectList(list51, 5, 1);
        List<SubjectDriver> subjects52 = SubjectReadUtils.read2SelectList(list52, 5, 2);
        List<SubjectDriver> subjects53 = SubjectReadUtils.read2Judges(list53, 5, 3);
        List<SubjectDriver> subjects61 = SubjectReadUtils.read2SelectList(list61, 6, 1);
        List<SubjectDriver> subjects62 = SubjectReadUtils.read2SelectList(list62, 6, 2);
        List<SubjectDriver> subjects63 = SubjectReadUtils.read2Judges(list63, 6, 3);
        List<SubjectDriver> subjects71 = SubjectReadUtils.read2SelectList(list71, 7, 1);
        List<SubjectDriver> subjects72 = SubjectReadUtils.read2SelectList(list72, 7, 2);
        List<SubjectDriver> subjects73 = SubjectReadUtils.read2Judges(list73, 7, 3);

        LogUtils.errorFormat("subjects11.size() = %d", subjects11.size());  //101
        LogUtils.errorFormat("subjects12.size() = %d", subjects12.size());  //19
        LogUtils.errorFormat("subjects13.size() = %d", subjects13.size());  //20
        LogUtils.errorFormat("subjects21.size() = %d", subjects21.size());  //50
        LogUtils.errorFormat("subjects22.size() = %d", subjects22.size());  //4
        LogUtils.errorFormat("subjects23.size() = %d", subjects23.size());  //21
        LogUtils.errorFormat("subjects31.size() = %d", subjects31.size());  //96
        LogUtils.errorFormat("subjects32.size() = %d", subjects32.size());  //11
        LogUtils.errorFormat("subjects33.size() = %d", subjects33.size());  //50
        LogUtils.errorFormat("subjects41.size() = %d", subjects41.size());  //79
        LogUtils.errorFormat("subjects42.size() = %d", subjects42.size());  //24
        LogUtils.errorFormat("subjects43.size() = %d", subjects43.size());  //10
        LogUtils.errorFormat("subjects51.size() = %d", subjects51.size());  //150
        LogUtils.errorFormat("subjects52.size() = %d", subjects52.size());  //0
        LogUtils.errorFormat("subjects53.size() = %d", subjects53.size());  //20
        LogUtils.errorFormat("subjects61.size() = %d", subjects61.size());  //41
        LogUtils.errorFormat("subjects62.size() = %d", subjects62.size());  //0
        LogUtils.errorFormat("subjects63.size() = %d", subjects63.size());  //0
        LogUtils.errorFormat("subjects71.size() = %d", subjects71.size());  //28
        LogUtils.errorFormat("subjects72.size() = %d", subjects72.size());  //2
        LogUtils.errorFormat("subjects73.size() = %d", subjects73.size());  //11

        //存储
        GreenDaoUtils.insertInTx(DAO, subjects11);
        GreenDaoUtils.insertInTx(DAO, subjects12);
        GreenDaoUtils.insertInTx(DAO, subjects13);
        GreenDaoUtils.insertInTx(DAO, subjects21);
        GreenDaoUtils.insertInTx(DAO, subjects22);
        GreenDaoUtils.insertInTx(DAO, subjects23);
        GreenDaoUtils.insertInTx(DAO, subjects31);
        GreenDaoUtils.insertInTx(DAO, subjects32);
        GreenDaoUtils.insertInTx(DAO, subjects33);
        GreenDaoUtils.insertInTx(DAO, subjects41);
        GreenDaoUtils.insertInTx(DAO, subjects42);
        GreenDaoUtils.insertInTx(DAO, subjects43);
        GreenDaoUtils.insertInTx(DAO, subjects51);
        GreenDaoUtils.insertInTx(DAO, subjects52);
        GreenDaoUtils.insertInTx(DAO, subjects53);
        GreenDaoUtils.insertInTx(DAO, subjects61);
        GreenDaoUtils.insertInTx(DAO, subjects62);
        GreenDaoUtils.insertInTx(DAO, subjects63);
        GreenDaoUtils.insertInTx(DAO, subjects71);
        GreenDaoUtils.insertInTx(DAO, subjects72);
        GreenDaoUtils.insertInTx(DAO, subjects73);
    }

    /**
     * 读取单选题目
     */
    private void readSingleSelectSubjects() {
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
     * 读取判断题
     */
    private void readJudgeSubjects() {
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