package com.actor.cnpc_qhse_exams.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;
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
     * 读取 单选题/多选题
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
     *
     * 12. 【考核点：层级+专业；题型：单选题；难度：易；类型：基础】
     * 13. 雾天对安全行车的主要影响是能见度低，视线不清，按照防御性驾驶技术要求，要留有
     * 余地，适当拉开与前车的跟车距离，要引人注意，及时开启（    ）。
     *
     * 145、高速公路上停车检修时应在（ D ）放置警示牌。
     * （A）车前 100 米 （B）车后 100 米 （C）车前 150 米   （D）车后 150 米
     *
     * @param list 选择题列表
     * @param chapterType 章节
     * @param subjectType 题型
     * @return
     */
    @NonNull
    public static List<SubjectDriver> read2SelectList(List<String> list, int chapterType, int subjectType) {
        List<Integer> numberStartPos = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            //if是考核点
            if (isTestPoint(item)) {
                numberStartPos.add(i);
                continue;
            }
            //if本行是数字开头           && (第1行 || 上1行不是考核点)
            if (isStartWithNumber(item) && (numberStartPos.isEmpty() || !isTestPoint(list.get(i - 1)))) {
                numberStartPos.add(i);
            }
        }
        LogUtils.errorFormat("%d%d 选择题共找到 %d 个", chapterType, subjectType, numberStartPos.size());
        List<SubjectDriver> subjects = new ArrayList<>(numberStartPos.size());
        if (numberStartPos.isEmpty()) return subjects;

        //本题开始位置     下一题开始位置
        int posStart = 0, posNext = 0;
        //从每一道题的开始位置 遍历每一道题
        for (int i = 0; i < numberStartPos.size() - 1; i++) {
            posStart = numberStartPos.get(i);
            posNext = numberStartPos.get(i + 1);
            subjects.add(readListRange2Select(list, chapterType, subjectType, posStart, posNext));
        }
        //最后1题
        posStart = numberStartPos.get(numberStartPos.size() - 1);
        posNext = list.size();
        subjects.add(readListRange2Select(list, chapterType, subjectType, posStart, posNext));
        return subjects;
    }

    /**
     * 从list中读取数据解析成 SubjectDriver
     * @param list
     * @param posStart 本题开始位置
     * @param posNext 下一题开始位置
     * @return
     */
    public static SubjectDriver readListRange2Select(List<String> list, int chapterType, int subjectType, int posStart, int posNext) {
        //考核点                  标题号             标题             选项            答案
        String testPoint = null, subjectNum = null, subject = null, options = null, answer = null,
                //标题里的答案             解析
                answerInSubject = null, parse = null;
        StringBuilder sb = new StringBuilder(200);
        String item = list.get(posStart);
        /**
         * 第1项: 有可能是 考核点/标题
         *        if是考核点 (不一定有) (最多1行)
         */
        if (isTestPoint(item)) {
            testPoint = getTestPoint(item, list.get(posStart + 1));
            subjectNum = getSubjectNum(item);
            item = list.get(++posStart);
        } else {
            //第1行是标题
            sb.append(item);
        }

        /**
         * 第2项: 标题 (一定有) (有可能是多行)
         */
        if (sb.length() == 0) sb.append(subjectNum).append(item);
        item = list.get(++posStart);
        boolean isOption = isOption(item);
        while (!isOption) {
            sb.append(item);
            item = list.get(++posStart);
            isOption = isOption(item);
        }
        subject = sb.toString();
        //获取标题中的答案 (不一定有)
        answerInSubject = getAnswerInSubject(subject);
        sb.setLength(0);

        /**
         * 第3项: 选项ABCDEFG (一定有) (一定是多个选项) (每个选项有可能是多行) (也可能多个选项共一行)
         */
        //添加第1个选项
        sb.append(item);
        String answer1 = null;
        boolean isParse = false;
        if (posStart < posNext - 1) {
            item = list.get(++posStart);
            answer1 = getAnswer(item);
            isParse = isParse(item);
            //if既不是答案, 也不是解析, 就一定还是选项 (答案不一定有) (解析不一定有)
            while (answer1 == null && !isParse) {
                sb.append(item);
                if (posStart >= posNext - 1) break;
                item = list.get(++posStart);
                answer1 = getAnswer(item);
                isParse = isParse(item);
            }
        }
        options = sb2Options(sb);
        sb.setLength(0);

        /**
         * 第4项: 答案 (不一定有) (最多1行)
         */
        if (answer1 != null) answer = answer1;
        if (answer == null) answer = answerInSubject;
        if (answer == null) {
            throw new IllegalStateException(TextUtils2.getStringFormat("这题未读取到答案: %s", subject));
        }

        /**
         * 第5项: 解析 (不一定有) (有可能是多行)
         */
        if (isParse) sb.append(getParse(item));
        while (posStart < posNext - 1) {
            item = list.get(++posStart);
            sb.append(getParse(item));
        }
        if (sb.length() > 0) parse = sb.toString();
        sb.setLength(0);

        return new SubjectDriver(chapterType, subjectType, testPoint, subject, options, answer, parse);
    }


    /**
     * 读取 判断题
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
    public static List<SubjectDriver> read2JudgeList(List<String> list, int chapterType, int subjectType) {
        List<Integer> numberStartPos = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            //if是考核点
            if (isTestPoint(item)) {
                numberStartPos.add(i);
                continue;
            }
            //if本行是数字开头           && (第1行 || 上1行不是考核点)
            if (isStartWithNumber(item) && (numberStartPos.isEmpty() || !isTestPoint(list.get(i - 1)))) {
                numberStartPos.add(i);
            }
        }
        LogUtils.errorFormat("%d%d 判断题共找到 %d 个", chapterType, subjectType, numberStartPos.size());
        List<SubjectDriver> subjects = new ArrayList<>(numberStartPos.size());
        if (numberStartPos.isEmpty()) return subjects;

        //本题开始位置     下一题开始位置
        int posStart = 0, posNext = 0;
        //从每一道题的开始位置 遍历每一道题
        for (int i = 0; i < numberStartPos.size() - 1; i++) {
            posStart = numberStartPos.get(i);
            posNext = numberStartPos.get(i + 1);
            subjects.add(readListRange2Judge(list, chapterType, subjectType, posStart, posNext));
        }
        //最后1题
        posStart = numberStartPos.get(numberStartPos.size() - 1);
        posNext = list.size();
        subjects.add(readListRange2Judge(list, chapterType, subjectType, posStart, posNext));
        return subjects;
    }

    /**
     * 从list中读取数据解析成 SubjectDriver
     * @param list
     * @param posStart 本题开始位置
     * @param posNext 下一题开始位置
     * @return
     */
    public static SubjectDriver readListRange2Judge(List<String> list, int chapterType, int subjectType, int posStart, int posNext) {
        //考核点                  标题号             标题            答案            解析
        String testPoint = null, subjectNum = null, subject = null, answer = null, parse = null;
        StringBuilder sb = new StringBuilder(200);
        String item = list.get(posStart);
        /**
         * 第1项: 有可能是 考核点/标题
         *        if是考核点 (不一定有) (最多1行)
         */
        if (isTestPoint(item)) {
            testPoint = getTestPoint(item, list.get(posStart + 1));
            subjectNum = getSubjectNum(item);
            item = list.get(++posStart);
        } else {
            //第1行是标题
            sb.append(item);
        }

        /**
         * 第2项: 标题 (一定有) (有可能是多行)
         */
        if (sb.length() == 0) sb.append(subjectNum).append(item);
        item = list.get(++posStart);
        String answer1 = getAnswer(item);
        while (answer1 == null) {
            sb.append(item);
            item = list.get(++posStart);
            answer1 = getAnswer(item);
        }
        subject = sb.toString();
        sb.setLength(0);

        /**
         * 第3项: 答案 (一定有) (只有1行)
         */
        answer = answer1;

        /**
         * 第4项: 解析 (不一定有) (有可能是多行)
         */
        while (posStart < posNext - 1) {
            item = list.get(++posStart);
            sb.append(getParse(item));
        }
        if (sb.length() > 0) parse = sb.toString();
        sb.setLength(0);

        return new SubjectDriver(chapterType, subjectType, testPoint, subject, null, answer, parse);
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
     * ^        行开头
     * \\d+     匹配数字（1 位、2 位、3 位都可以：1、10、141）
     * [.、]    匹配 . 或者 、 两种符号
     * \s*      匹配0~ 多个空格（兼容有无空格）
     * 【考核点：以"【考核点："开头
     * ([^】]+) 分组 1：捕获所有非 】 的字符 = group(1)
     * 】       以】结尾
     * (.*)     分组 2：捕获后面所有内容（包括空） = group(2)
     */
    private static final Pattern REGEX_TEST_POINT = Pattern.compile("^\\d+[.、]\\s*【考核点：([^】]+)】(.*)$");
    /**
     * 获取考核点
     * @param item 1.【考核点：层级+专业；题型：单选题；难度：中；类型：专业】
     *             20. 【考核点：层级+专业；题型：单选题；难度：易；类型：基础】
     * @param nextItem 标题, 用于没找到考核点时抛异常
     * @return 层级+专业；题型：单选题；难度：中；类型：专业
     */
    public static String getTestPoint(String item, String nextItem) {
        Matcher matcher = REGEX_TEST_POINT.matcher(item);
        if (matcher.find()) {
            String group2 = matcher.group(2);
            if (!TextUtils.isEmpty(group2)) {
                throw new IllegalStateException(TextUtils2.getStringFormat("【考核点】后面有内容, 是标题??: %s, 下一行: %s", item, nextItem));
            }
            return matcher.group(1);
        }
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
     * 是否是数字开头: 考核点/标题
     * @return true, false
     */
    public static boolean isStartWithNumber(String item) {
        Matcher matcher = REGEX_SUBJECT_NUM.matcher(item);
//        return matcher.matches(); //完美匹配
        return matcher.find();
    }

    /**
     * [（(]?     匹配可选的左括号
     * [A-ZＡ-Ｚ] 匹配半角 / 全角 ABCD
     * [）)]?     匹配可选的右括号
     * .*         表示后面可以跟任意内容
     */
    private static final Pattern PATTERN_IS_OPTION = Pattern.compile("^[（(]?[A-ZＡ-Ｚ][）)]?.*");
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
        return PATTERN_IS_OPTION.matcher(item).matches();
    }

    /**
     * [（(]?     匹配可选的左括号
     * [A-ZＡ-Ｚ] 匹配半角 / 全角 ABCD
     * [）)]?     匹配可选的右括号
     * [、\\s]?   匹配可选的顿号或空格
     * .*?       非贪婪匹配所有内容（不截断、不丢字）
     * (?=...)   前瞻定位，自动分割下一个选项
     */
    private static final Pattern PATTERN_OPTIONS = Pattern.compile("([（(]?[A-ZＡ-Ｚ][）)]?[、\\s]?.*?)(?=\\s*[（(]?[A-ZＡ-Ｚ]|$)");
    /**
     * StringBuilder 转换成String
     * @param sb
     * @return
     */
    public static String sb2Options(StringBuilder sb) {
        String options = sb.toString();
        Matcher matcher = PATTERN_OPTIONS.matcher(options);
        sb.setLength(0);
        boolean isMatch = false;
        while (matcher.find()) {
            isMatch = true;
            String option = matcher.group(1);
            sb.append(option).append("\n");
        }
        if (isMatch) {
            //去掉最后1个\n
            sb.setLength(sb.length() - 1);
            return sb.toString();
        }
        throw new IllegalStateException(TextUtils2.getStringFormat("选项没有匹配上: %s", options));
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
     * 解析    固定开头
     * [:：]?  冒号可选（有或没有都匹配）
     * \s*     吞掉空格
     * (.*)    group (1) = 解析后面所有内容
     */
    private static final Pattern PATTERN_PARSE = Pattern.compile("解析[:：]?\\s*(.*)");
    /**
     * 获取解析 (不一定有解析)(也有可能是多行)
     * @param item "解析：" or "解析"
     * @return 是否是解析
     */
    public static String getParse(String item) {
        Matcher matcher = PATTERN_PARSE.matcher(item);
        if (matcher.find()) return matcher.group(1);
        //没匹配到, 是解析的第2行
        return item;
    }


    /**
     * (?:答文|答案)             匹配 答文 或 答案,  ?: 不存起来的分组（非捕获组, 只要分组功能，不占用 group(1)编号, 不要保存结果，不占分组位置！）
     * [:：]                     匹配 半角冒号 : 或 全角冒号 ：
     * \s*                      吞掉冒号后的空格
     * [（(]?                    重复零次或一次
     * ([A-ZＡ-Ｚ\s]+|正确|错误)  捕获字母|正确|错误 + 允许中间空格 = group(1)
     * [）)]?                    重复零次或一次
     * \s*$                     吞掉末尾空格
     */
    private static final Pattern REGEX_ANSWER = Pattern.compile("(?:答文|答案)[:：]\\s*[（(]?([A-ZＡ-Ｚ\\s]+|正确|错误)[）)]?\\s*$");
    private static final char    charDistance = 'Ａ' - 'A';
    /**
     * 返回答案
     * @param item 答文：C
     *             答案：ABD
     *             答文:C
     *             答文：            //"高原山区行车应适当调整发动机的点火提前角，与平原地区相比可（ C ）。" 这道题就是这个b样子, 本方法会返回null
     *             单选 12 答文：B   //需要切割
     *             单选 23 答文：B
     *             单选选 27 答文：D
     *             答文：正确
     *             答文：错误
     *             答文：（D）       //答案在括号里...
     * @return ABCD or 正确/错误 or null
     */
    @Nullable
    public static String getAnswer(String item) {
        Matcher matcher = REGEX_ANSWER.matcher(item);
        // 🔥 去掉答案中间所有空格
        if (matcher.find()) {
            String group1 = matcher.group(1);
            if (TextUtils.isEmpty(group1)) {
                throw new IllegalStateException(TextUtils2.getStringFormat("答案没有匹配到值: %s", item));
            }
            group1 = group1.replaceAll("\\s+", "");
            for (int i = 0; i < group1.length(); i++) {
                char c = group1.charAt(i);
                if (c >= 'Ａ' && c <= 'Ｚ') group1 = group1.replace(c, (char) (c - charDistance));
            }
            return group1;
        }
        return null;
    }

    /**
     *  正则：[（(]                     匹配左括号（全 / 半角）
     *        \s*                      匹配0~ 多个空格（兼容有无空格）
     *        ([A-ZＡ-Ｚ\s]+?)         捕获所有字母（全 / 半角）、中间空格
     *        \s*                      匹配字母后0~ 多个空格
     *        [）)]                    匹配右括号（全 / 半角）
     */
    private static final Pattern PATTERN_ANSWER = Pattern.compile("[（(]\\s*([A-ZＡ-Ｚ\\s]+?)\\s*[）)]");
    /**
     * 获取标题里的答案, 预防只有这个标题和选项, 没有答案的情况
     * @param item 146、我们有两个词来形容防御性驾驶，它们是（ C ），空间给车辆，能见度给驾驶员。
     *             A 空间与目视距离       B 能见度与目测范围          C 空间与能见度
     *
     *             148、驾驶人在行车中经过积水路面时，按照防御性驾驶技术要求，应注意观察，留有余地，
     *             ( A ) 。
     *             A 特别注意减速慢行      B 迅速加速通过
     *             C 保持正常车速通过      D 低档加速通过
     * @return ABCD
     */
    @Nullable
    public static String getAnswerInSubject(String item) {
        Matcher matcher = PATTERN_ANSWER.matcher(item);
        if (matcher.find()) {
            String group1 = matcher.group(1).replaceAll("\\s+", "");
            for (int i = 0; i < group1.length(); i++) {
                char c = group1.charAt(i);
                if (c >= 'Ａ' && c <= 'Ｚ') group1 = group1.replace(c, (char) (c - charDistance));
            }
            return group1;
        }
        return null;
    }
}
