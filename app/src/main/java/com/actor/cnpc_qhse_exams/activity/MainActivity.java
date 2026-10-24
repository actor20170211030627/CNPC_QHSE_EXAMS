package com.actor.cnpc_qhse_exams.activity;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.actor.cnpc_qhse_exams.adapter.MainAdapter;
import com.actor.cnpc_qhse_exams.apis.GetQbQuestionList20261016Api;
import com.actor.cnpc_qhse_exams.apis.LoginApi;
import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.cnpc_qhse_exams.databinding.ActivityMainBinding;
import com.actor.cnpc_qhse_exams.dialog.UpdateDialog;
import com.actor.cnpc_qhse_exams.global.Global;
import com.actor.database.greendao.GreenDaoUtils;
import com.actor.myandroidframework.recyclerview.BaseItemDecoration;
import com.actor.myandroidframework.utils.LogUtils;
import com.actor.myandroidframework.utils.MMKVUtils;
import com.actor.myandroidframework.utils.toaster.ToasterUtils;
import com.actor.pgyer.PgyerApi20Utils;
import com.actor.pgyer.bean.AppCheckBean;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.greendao.gen.SubjectDriverDao;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.tencent.mmkv.MMKV;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 交通安全基层站队QHSE标准化建设—驾驶员应知应会题库
 * @author    : ldf
 * date       : 2025/4/21 on 16:09
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //学习全部
        viewBinding.stvStudyAll.setOnClickListener(v -> {
            StudyActivity.start(this, v, -1, true, new ArrayList<>(0));
        });

        SubjectDriverDao dao = GreenDaoUtils.getDaoSession().getSubjectDriverDao();
        List<SubjectDriver> subjectDrivers = groupBy(dao, SubjectDriverDao.Properties.Version);
        viewBinding.recyclerView.addItemDecoration(new BaseItemDecoration(0f, SizeUtils.dp2px(10f)));
        viewBinding.recyclerView.setAdapter(new MainAdapter(this, subjectDrivers));

        if (!MMKVUtils.getBoolean(Global.IS_CHECK_UPDATE)) {
            PgyerApi20Utils.init(Global.API_KEY, Global.APP_KEY);
            PgyerApi20Utils.appCheck(this, AppUtils.getAppVersionName(), null, new OnHttpListener<AppCheckBean.AppCheckResponse>() {
                @Override
                public void onHttpSuccess(@NonNull AppCheckBean.AppCheckResponse result) {
                    MMKVUtils.putBoolean(Global.IS_CHECK_UPDATE, true, MMKV.ExpireInDay);
                    if (result.isSuccess()) {
                        if (result.data == null) return;
                        if (result.data.buildHaveNewVersion || result.data.buildVersionNo > AppUtils.getAppVersionCode()) {
                            new UpdateDialog(mActivity, result.data).show();
                        }
                    } else {
                        ToasterUtils.warning(result.message);
                    }
                }
                @Override
                public void onHttpFail(@NonNull Throwable throwable) {
                }
            });
        }

        /**
         * 20261016
         */
        getToken20261016();
        //单选题
//        get20261016(1, 1, 2026101601L);
        //多选题
//        get20261016(1, 2, 2026101601L);
        //判断题
        get20261016(1, 3, 2026101601L);



//        TxtReadUtils.readTxt2SubjectDrivers(2025041501);
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


    private void getToken20261016() {
        if (true) return;
        EasyHttp.get(this)
                .api(LoginApi.class)
                .request(new OnHttpListener<LoginApi.LoginBean>() {
                    @Override
                    public void onHttpSuccess(@NonNull LoginApi.LoginBean result) {
                        if (result.code == 0) {
                            LoginApi.LoginBean.DataBean data = result.data;
                            if (data == null) return;
                            String token = data.token;
                        } else {
                            ToasterUtils.warning(result.message);
                        }
                    }
                    @Override
                    public void onHttpFail(@NonNull Throwable throwable) {
                        throwable.printStackTrace();
                        ToasterUtils.error(throwable.getMessage());
                    }
                });
    }

    //网络请求
    private List<SubjectDriver> subjects = new ArrayList<>(200);
    private StringBuilder SB = new StringBuilder();
    private String[] answers = {"", "A", "B", "C", "D", "E", "F", "G", "H"};
    private void get20261016(@IntRange(from = 1) int page, int questionType, long version) {
        if (true) return;
        if (page == 1) subjects.clear();
        EasyHttp.get(this)
                .api(new GetQbQuestionList20261016Api(page, questionType))
                .request(new OnHttpListener<GetQbQuestionList20261016Api.GetQbQuestionListBean>() {
                    @Override
                    public void onHttpSuccess(@NonNull GetQbQuestionList20261016Api.GetQbQuestionListBean result) {
                        if (result.isSuccess()) {
                            GetQbQuestionList20261016Api.GetQbQuestionListBean.DataBean data = result.data;
                            if (data == null) return;
                            List<GetQbQuestionList20261016Api.GetQbQuestionListBean.DataBean.QuestionListBean> question_list = data.question_list;
                            if (question_list == null || question_list.isEmpty()) {
                                LogUtils.errorFormat("question_list = %s", question_list);
                                return;
                            }
                            for (GetQbQuestionList20261016Api.GetQbQuestionListBean.DataBean.QuestionListBean question : question_list) {
                                if (question.url_list != null && !question.url_list.isEmpty() || question.analysis_url_list != null && !question.analysis_url_list.isEmpty()) {
                                    LogUtils.errorFormat("题目有图片!!!: url_list = %s, analysis_url_list = %s", question.url_list, question.analysis_url_list);
                                }
                                String title = question.describe;
                                List<GetQbQuestionList20261016Api.GetQbQuestionListBean.DataBean.QuestionListBean.OptionDescribeBean> optionBeans = question.option_describe;
                                if (TextUtils.isEmpty(title)) {
                                    LogUtils.errorFormat("标题有问题!!!: title = %s", title);
                                    return;
                                }

                                String options = null;
                                //选择题 没有选项
                                if (questionType == 1 || questionType == 2) {
                                    if (optionBeans == null || optionBeans.isEmpty()) {
                                        LogUtils.errorFormat("选项有问题!!!: title = %s, options = %s", title, optionBeans);
                                        return;
                                    }
                                    SB.setLength(0);
                                    for (int i = 0; i < optionBeans.size(); i++) {
                                        GetQbQuestionList20261016Api.GetQbQuestionListBean.DataBean.QuestionListBean.OptionDescribeBean optionBean = optionBeans.get(i);
                                        if (optionBean.url_list != null && !optionBean.url_list.isEmpty()) {
                                            LogUtils.errorFormat("选项有图片!!!: title = %s, option = %s, url_list = %s", title, optionBean, optionBean.url_list);
                                            return;
                                        }
                                        SB.append(answers[i + 1]).append(".").append(optionBean.describe);
                                        if (i != optionBeans.size() - 1) SB.append("\n");
                                    }
                                    options = SB.toString();
                                }

                                String answerS = null;
                                if (questionType == 1) {        //单选题
                                    int answer = question.answer;
                                    if (answer <= 0 || answer > optionBeans.size()) {
                                        LogUtils.errorFormat("答案有问题!!!: title = %s, options = %s, answer = %d", title, optionBeans, answer);
                                        return;
                                    }
                                    answerS = answers[answer];
                                } else if (questionType == 2) { //多选题
                                    List<Integer> answer_list = question.answer_list;
                                    if (answer_list == null || answer_list.isEmpty()) {
                                        LogUtils.errorFormat("答案有问题!!!: title = %s, options = %s, answer_list = %s", title, optionBeans, answer_list);
                                        return;
                                    }
                                    SB.setLength(0);
                                    for (Integer integer : answer_list) {
                                        SB.append(answers[integer]);
                                    }
                                    answerS = SB.toString();
                                } else if (questionType == 3) { //判断题
                                    int answer = question.answer;
                                    if (answer != 0 && answer != 1) {
                                        LogUtils.errorFormat("答案有问题!!!: title = %s, options = %s, answer = %d", title, optionBeans, answer);
                                        return;
                                    }
                                    answerS = answer == 0 ? "错误" : "正确";
                                }
                                subjects.add(new SubjectDriver(version, -1, questionType, null, title, options, answerS, null));
                            }
                            if (data.offset + question_list.size() >= data.total_question_count) {
                                ToasterUtils.success("全部数据请求完成!");
                                SubjectDriverDao dao = GreenDaoUtils.getDaoSession().getSubjectDriverDao();
                                GreenDaoUtils.insertInTx(dao, subjects);
                                return;
                            } else {
                                get20261016(page + 1, questionType, version);
                            }
                        } else {
                            ToasterUtils.warning(result.message);
                        }
                    }
                    @Override
                    public void onHttpFail(@NonNull Throwable throwable) {
                        throwable.printStackTrace();
                        ToasterUtils.error(throwable.getMessage());
                    }
                });
    }
}