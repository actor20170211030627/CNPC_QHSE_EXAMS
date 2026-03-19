package com.actor.cnpc_qhse_exams.utils;

import androidx.annotation.Nullable;

import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.myandroidframework.utils.LogUtils;
import com.actor.myandroidframework.utils.TextUtils2;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: 单选题/多选题 / 判断题
 * company    :
 *
 * @author : ldf
 * date       : 2025/4/21 on 17
 * @version 1.0
 */
public class SubjectReadUtils {

    /**
     * 单选题/多选题
     *
     * 1.“道路”，是指（   ）、城市道路和虽在单位管辖范围但允许社会机动车通行的地方，包括广场、公共停车场等用于公众通行的场所。
     * （A）马路；
     * （B）铁路；
     * （C）公路；
     * （D）公安机关交通管理部门；       //可能没有D选项
     * 答案：C
     * 解析：《驾驶员初级理论知识试题》 //大部分没有解析
     *
     *
     * 1.【考核点：层级+专业；题型：单选题；难度：中；类型：专业】
     * 当驾驶操作效果与驾驶员的意志产生偏差时，感觉器官又将这些误差的新信息输送到大脑，
     * 这种机能称为（   ）。
     * （A）反射；
     * （B）幻觉；
     * （C）本能；
     * （D）反馈；
     * 答文：D
     * 解析：
     */
    public static List<SubjectDriver> read2SelectList(List<String> list, int chapterType, int subjectType) {
        //考核点                  标题号             标题             标题里的答案
        String testPoint = null, subjectNum = null, subject = null, answerInSubject = null;
        //是否已经解析选项
        boolean isParseOption = false;
        StringBuilder sb = new StringBuilder();
        //选项: A~G
        List<String> options = new ArrayList<>(7);
        List<SubjectDriver> subjects = new ArrayList<>(200);

        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);

            //考核点
            if (testPoint == null && isTestPoint(item)) {
                testPoint = getTestPoint(item, list.get(i + 1));
                subjectNum = getSubjectNum(item);
                continue;
            }
            //是否是选项
            boolean isOption = isOption(item);
            //是否是答案
            String answer = getAnswer(item);
            /**
             * 是否是选项的下一行 (1个选项有可能是2行)
             * 要先判断是否是答案, 因为答案的上面也是选项
             * 标题的上面也有可能是选项, 例:
             *     145、高速公路上停车检修时应在（ D ）放置警示牌。
             *     （A）车前 100 米 （B）车后 100 米 （C）车前 150 米   （D）车后 150 米
             *     146、我们有两个词来形容防御性驾驶，它们是（ C ），空间给车辆，能见度给驾驶员。  //← 这1行
             */
            boolean isOptionNext = answer == null && !isSubject(item) && isOption(list.get(i - 1));
            //是否是解析
            boolean isParse = isParse(item);
            //标题(有可能是多行)                                  答案那儿subject = null, 所以这儿要判断是否是解析
            if (!isParseOption && !isOption && !isOptionNext && !isParse) {
                answerInSubject = getAnswerInSubject(item);
                if (subject == null) {
                    if (subjectNum == null) {
                        subject = item;
                    } else subject = subjectNum + item;
                } else {
                    subject = subject + item;
                }
                continue;
            }
            //选项
            if (isOption) {
                options.add(item);
                isParseOption = true;
                /**
                 * 选项的下1行有可能是:
                 *     本选项的第2行  //这1行选项没显示完
                 *     下一个选项
                 *     答案
                 *     下一题的标题   //这1题的答案在标题里, 所以这1题没有答案
                 */
                if (i >= list.size() - 1) continue;
                //判断下一行是否是答案, if下一行是答案, continue
                String answerNext = getAnswer(list.get(i + 1));
                if (answerNext != null) continue;
                //判断下一行是否是选项, if下一行是选项, continue
                boolean isNextOption = isOption(list.get(i + 1));
                if (isNextOption) continue;
                //判断下一行是否是标题, if下一行是标题, 需要直接走"答案"判断的代码
                boolean isSubject = isSubject(list.get(i + 1));
                if (!isSubject) continue;
                // if题目里有答案(这道题可能没有明确答案, 答案在标题里), 就需要判断下一行是否是选项,
//                if (answerInSubject == null) continue;
            } else if (isOptionNext) {
                if (options.isEmpty()) {
                    LogUtils.errorFormat("options为空, item = %s", item);
                }
                options.set(options.size() - 1, options.get(options.size() - 1) + item);
                isParseOption = true;
                continue;
            }
            //答案
            if (answer == null) answer = answerInSubject;
            if (answer != null) {
                if (subject == null || options.isEmpty()) {
                    throw new IllegalStateException(TextUtils2.getStringFormat("没有读取到标题 or 选项: 字符串: %s, options.size() = %d", item, options.size()));
                }
                for (int i1 = 0; i1 < options.size(); i1++) {
                    sb.append(options.get(i1));
                    if (i1 < options.size() - 1) sb.append("\n");
                }
                SubjectDriver sd = new SubjectDriver(chapterType, subjectType, testPoint, subject, sb.toString(), answer, null);
                subjects.add(sd);

                testPoint = subjectNum = subject = answerInSubject = null;
                isParseOption = false;
                sb.setLength(0);
                options.clear();
                continue;
            }
            //解析(不一定有解析)
            if (isParse) {
                subjects.get(subjects.size() - 1).setAnalysis(item);
                continue;
            }

            throw new IllegalArgumentException(TextUtils2.getStringFormat("%d 行, 未知类型字符串: %s", i, item));
        }
        return subjects;
    }

    /**
     * 判断题
     *
     * 3.实习驾驶员可以试车。
     * 答案：错误
     * 解析：实习驾驶员不可以试车。   //可能没有解析
     *
     * 1.【考核点：层级+专业；题型：判断题；难度：易；类型：基础】
     * 患心肌梗塞、严重高血压、传染病等疾病的人、怀孕 8 个月以上的孕妇及不到 2 周的新生儿
     * 均不宜乘坐飞机。
     * 答文：正确
     * 解析：
     */
    public static List<SubjectDriver> read2Judges(List<String> list, int chapterType, int subjectType) {
        //考核点                  标题号             标题
        String testPoint = null, subjectNum = null, subject = null;
        List<SubjectDriver> subjects = new ArrayList<>(60);

        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            //考核点
            if (testPoint == null && isTestPoint(item)) {
                testPoint = getTestPoint(item, list.get(i + 1));
                subjectNum = getSubjectNum(item);
                continue;
            }
            //是否是答案
            String answer = getAnswer(item);
            //是否是解析
            boolean isParse = isParse(item);
            //标题(有可能是多行)                 答案那儿subject = null, 所以这儿要判断是否是解析
            if (answer == null && !isParse) {
                if (subject == null) {
                    if (subjectNum == null) {
                        subject = item;
                    } else subject = subjectNum + item;
                } else {
                    LogUtils.errorFormat("请确认这是标题的一部分(而不是答案): %s", item);
                    subject = subject + item;
                }
                continue;
            }
            //答案
            if (answer != null) {
                if (subject == null) {
                    throw new IllegalStateException(TextUtils2.getStringFormat("没有读取到标题: %d 行, 字符串: %s", i, item));
                }
                SubjectDriver sd = new SubjectDriver(chapterType, subjectType, testPoint, subject, null, answer, null);
                subjects.add(sd);

                testPoint = subjectNum = subject = null;
                continue;
            }
            //解析(有可能是多行)
            if (isParse) {
                subjects.get(subjects.size() - 1).setAnalysis(item);
                if (i >= list.size() - 1) continue;
                //if下一行是标题, 继续下一行
                if (isSubject(list.get(i + 1))) continue;
                //否则下一行就还是解析的第2行
                subjects.get(subjects.size() - 1).setAnalysis(item + list.get(i + 1));
                i ++;
                continue;
            }

            throw new IllegalArgumentException(TextUtils2.getStringFormat("%d 行, 未知类型字符串: %s", i, item));
        }
        return subjects;
    }

    /**
     * 判断是否为空
     */
    public static boolean judgeEmpty(String item) {
        if (item == null)  return true;
        return item.trim().isEmpty();
    }

    /**
     * 是否是考核点
     * @param item 1.【考核点：层级+专业；题型：单选题；难度：中；类型：专业】
     * @return 是否是考核点
     */
    public static boolean isTestPoint(String item) {
        return item.contains("【考核点");
    }

    /**
     * ^     行开头
     * \\d+  匹配数字（1 位、2 位、3 位都可以：1、10、141）
     * [.、] 匹配 . 或者 、 两种符号
     * 【    以【开头
     * .+    捕获【后面所有内容
     * 】    以】开头
     */
    private static final Pattern REGEX_TEST_POINT = Pattern.compile("^\\d+[.、](【.+】)");
    /**
     * 获取考核点
     * @param item 1.【考核点：层级+专业；题型：单选题；难度：中；类型：专业】
     * @param nextItem 标题, 用于没找到考核点时抛异常
     * @return 【考核点：层级+专业；题型：单选题；难度：中；类型：专业】
     */
    public static String getTestPoint(String item, String nextItem) {
        Matcher matcher = REGEX_TEST_POINT.matcher(item);
        //只返回【】和里面的内容
        if (matcher.find()) return matcher.group(1);
        throw new IllegalStateException(TextUtils2.getStringFormat("【考核点】读取有问题: %s, 下一行: %s", item, nextItem));
    }

    /**
     * ^     行开头
     * \\d+  匹配数字（1 位、2 位、3 位都可以：1、10、141）
     * [.、] 匹配 . 或者 、 两种符号
     */
    private static final Pattern REGEX_SUBJECT_NUM = Pattern.compile("^\\d+[.、]");
    /**
     * 获取标题号, if不是标题就报错
     * @return 1. or 1、 or null
     */
    @Nullable
    public static String getSubjectNum(String item) {
        Matcher matcher = REGEX_SUBJECT_NUM.matcher(item);
        if (matcher.find()) return matcher.group();
        return null;
    }

    /**
     * 是否是标题
     * @return true, false
     */
    public static boolean isSubject(String item) {
        Matcher matcher = REGEX_SUBJECT_NUM.matcher(item);
//        return matcher.matches(); //完美匹配
        return matcher.find();
    }

    /**
     *  正则：匹配（A/B/C/D/E/F/G）     开头 or
     *       匹配  A/B/C/D/E/F/G       开头 or
     *       匹配  Ａ/Ｂ/Ｃ/Ｄ/Ｅ/Ｆ/Ｇ  开头 or
     *       匹配（Ａ/Ｂ/Ｃ/Ｄ/Ｅ/Ｆ/Ｇ）开头
     *       .* 表示后面可以跟任意内容
     */
    private static final Pattern PATTERN_OPTION = Pattern.compile("^（[ABCDEFG]）.*|^[ABCDEFGＡＢＣＤＥＦＧ].*|^（[ＡＢＣＤＥＦＧ]）.*");
    /**
     * 是否是选项
     * @param item （A）马路；
     *             A、前轮死锁  B、后轮死锁  C、甩尾失控  D、立刻停下
     *             C 机动车和非机动车          （D）汽车和拖拉机
     *             D 挤靠“加塞”车辆，逼其离开
     *             Ａ、前轮死锁       Ｂ、后轮死锁     Ｃ、甩尾失控      Ｄ、立刻停下
     *             Ｃ、气化、刹车距离减少          Ｄ、杂质增大、刹车距离减少
     * @return 是否是选项
     */
    public static boolean isOption(String item) {
        return PATTERN_OPTION.matcher(item).matches();
    }

    /**
     * 是否是解析
     * @param item "解析：" or "解析"
     * @return 是否是解析
     */
    public static boolean isParse(String item) {
        return item.startsWith("解析");
    }


    /**
     * (?:答文|答案) 匹配 答文 或 答案,  ?: 不存起来的分组（非捕获组, 只要分组功能，不占用 group(1)编号, 不要保存结果，不占分组位置！）
     * [:：]         匹配 半角冒号 : 或 全角冒号 ：
     * (.+)          捕获冒号后面所有答案内容（ABC/ABD/BCD 都可以）= group(1)
     */
    private static final Pattern REGEX_ANSWER = Pattern.compile("(?:答文|答案)[:：](.+)");
//    private static final Pattern REGEX_ANSWER = Pattern.compile("(答文|答案)[:：](.+)");//也可, group(1)不同而已="答文|答案"
    /**
     * 返回答案
     * @param item 答文：C
     *             答案：ABD
     *             答文:C
     *             答文：            //"高原山区行车应适当调整发动机的点火提前角，与平原地区相比可（ C ）。" 这道题就是这个b样子, 本方法会返回null
     *             单选 12 答文：B   //需要切割
     *             单选 23 答文：B
     *             单选选 27 答文：D
     * @return 答案 or null
     */
    @Nullable
    public static String getAnswer(String item) {
        Matcher matcher = REGEX_ANSWER.matcher(item);
        if (matcher.find()) return matcher.group();
        return null;
    }

    /**
     *  正则：[（(]                     匹配 全角左括号 或 半角左括号
     *        \\s*                     匹配0~ 多个空格（兼容有无空格）
     *        ([ABCDEFGＡＢＣＤＥＦＧ]) 捕获答案字母（半角 + 全角 A/B/C 都支持）
     *        \\s*                     匹配字母后0~ 多个空格
     *        [）)]                    匹配 全角右括号 或 半角右括号
     */
    private static final Pattern PATTERN_ANSWER = Pattern.compile("[（(]\\s*([ABCDEFGＡＢＣＤＥＦＧ])\\s*[）)]");
    /**
     * 获取标题里的答案, 预防只有这个标题和选项, 没有答案的情况
     * @param item 146、我们有两个词来形容防御性驾驶，它们是（ C ），空间给车辆，能见度给驾驶员。
     *             A 空间与目视距离       B 能见度与目测范围          C 空间与能见度
     *
     *             148、驾驶人在行车中经过积水路面时，按照防御性驾驶技术要求，应注意观察，留有余地，
     *             ( A ) 。
     *             A 特别注意减速慢行      B 迅速加速通过
     *             C 保持正常车速通过      D 低档加速通过
     * @return "答案：A"
     */
    @Nullable
    public static String getAnswerInSubject(String item) {
        Matcher matcher = PATTERN_ANSWER.matcher(item);
//        while (matcher.find()) {
//            String group = matcher.group();
//            LogUtils.errorFormat("group = %s", group);
//        }
        if (matcher.find()) {
            // 提取括号里的字母（group 1）
            String group = matcher.group(1);
//            LogUtils.errorFormat("item = %s, group(1) = %s", item, group);
            if (group != null) return "答案:" + group;
        }
        return null;
    }
}
