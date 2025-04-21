package com.actor.cnpc_qhse_exams.utils;

import android.text.TextUtils;

import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.myandroidframework.utils.TextUtils2;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 单选/多选 / 选择题
 * company    :
 *
 * @author : ldf
 * date       : 2025/4/21 on 17
 * @version 1.0
 */
public class SubjectReadUtils {

    /**
     * 单选题
     * 1.“道路”，是指（   ）、城市道路和虽在单位管辖范围但允许社会机动车通行的地方，包括广场、公共停车场等用于公众通行的场所。
     * （A）马路；
     * （B）铁路；
     * （C）公路；
     * （D）公安机关交通管理部门；       //可能没有D选项
     * 答案：C
     * 解析：《驾驶员初级理论知识试题》 //大部分没有解析
     */
    public static List<SubjectDriver> read2SelectList(List<String> list, int chapterType, int subjectType) {
        String subject = null;
        StringBuilder sb = new StringBuilder();
        List<String> options = new ArrayList<>(7);
        List<SubjectDriver> subjects = new ArrayList<>(200);

        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);

            if (judgeEmpty(item)) continue;

            item = item.trim();

            //标题                    答案那儿subject = null, 所以这儿要判断是否是解析
            if (subject == null && !item.startsWith("解析：")) {
                judgeSubject(item, i);
                subject = item;
                continue;
            }
            //选项
            if (item.startsWith("（A）") || item.startsWith("（B）")
                    || item.startsWith("（C）") || item.startsWith("（D）")
                    || item.startsWith("（E）") || item.startsWith("（F）")
                    || item.startsWith("（G）")
                    || item.startsWith("C") //"C机动车和非机动车       （D）汽车和拖拉机"
                    || item.startsWith("A") //"A、前轮死锁  B、后轮死锁  C、甩尾失控  D、立刻停下"
                    || item.startsWith("D") //"D  挤靠“加塞”车辆，逼其离开"
            ) {
                options.add(item);
                continue;
            }
            //答案
            if (item.startsWith("答案：")) {
                if (subject == null) {
                    throw new IllegalStateException(TextUtils2.getStringFormat("没有读取到标题: %d 行, 字符串: %s", i, item));
                }
                for (int i1 = 0; i1 < options.size(); i1++) {
                    sb.append(options.get(i1));
                    if (i1 < options.size() - 1) sb.append("\n");
                }
                SubjectDriver sd = new SubjectDriver(chapterType, subjectType, subject, sb.toString(), item, null);
                subjects.add(sd);

                subject = null;
                sb.setLength(0);
                options.clear();
                continue;
            }
            //解析
            if (item.startsWith("解析：")) {
                if (subjects.isEmpty()) {
                    throw new IllegalStateException(TextUtils2.getStringFormat("为什么先把解析读取出来了? : %d 行, 字符串: %s", i, item));
                } else {
                    subjects.get(subjects.size() - 1).setAnalysis(item);
                    continue;
                }
            }

            throw new IllegalArgumentException(TextUtils2.getStringFormat("%d 行, 未知类型字符串: %s", i, item));
        }
        return subjects;
    }

    /**
     * 选择题
     * 3.实习驾驶员可以试车。
     * 答案：错误
     * 解析：实习驾驶员不可以试车。   //可能没有解析
     */
    public static List<SubjectDriver> read2Judges(List<String> list, int chapterType, int subjectType) {
        String subject = null;
//        StringBuilder sb = new StringBuilder();
//        List<String> options = new ArrayList<>(4);
        List<SubjectDriver> subjects = new ArrayList<>(60);

        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);

            if (judgeEmpty(item)) continue;

            item = item.trim();

            //标题                    答案那儿subject = null, 所以这儿要判断是否是解析
            if (subject == null && !item.startsWith("解析：")) {
                judgeSubject(item, i);
                subject = item;
                continue;
            }
            //选项
//            if (item.startsWith("（A）") || item.startsWith("（B）")
//                    || item.startsWith("（C）") || item.startsWith("（D）")
//                    || item.startsWith("（E）") || item.startsWith("（F）")
//                    || item.startsWith("（G）")
//                    || item.startsWith("C") //"C机动车和非机动车       （D）汽车和拖拉机"
//                    || item.startsWith("A") //"A、前轮死锁  B、后轮死锁  C、甩尾失控  D、立刻停下"
//                    || item.startsWith("D") //"D  挤靠“加塞”车辆，逼其离开"
//            ) {
//                options.add(item);
//                continue;
//            }
            //答案
            if (item.startsWith("答案：")) {
                if (subject == null) {
                    throw new IllegalStateException(TextUtils2.getStringFormat("没有读取到标题: %d 行, 字符串: %s", i, item));
                }
//                for (int i1 = 0; i1 < options.size(); i1++) {
//                    sb.append(options.get(i1));
//                    if (i1 < options.size() - 1) sb.append("\n");
//                }

//                SubjectDriver sd = new SubjectDriver(type, subject, sb.toString(), item, null);
                SubjectDriver sd = new SubjectDriver(chapterType, subjectType, subject, null, item, null);
                subjects.add(sd);

                subject = null;
//                sb.setLength(0);
//                options.clear();
                continue;
            }
            //解析
            if (item.startsWith("解析：")) {
                if (subjects.isEmpty()) {
                    throw new IllegalStateException(TextUtils2.getStringFormat("为什么先把解析读取出来了? : %d 行, 字符串: %s", i, item));
                } else {
                    subjects.get(subjects.size() - 1).setAnalysis(item);
                    continue;
                }
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
     * 判断标题, if不是标题就报错
     */
    public static void judgeSubject(String item, int lineNum) {
        //25、雨天行车，遇撑雨伞和穿雨衣的行人在公路上行走时，按照防御性驾驶技术要求，要引人注意、留有余地，应当（    ），谨慎通行。
        //134、轮胎胎纹低于0.5MM时应（     ）
        String[] split = item.substring(0, 5).split("\\.");
        if (split.length < 2) {
            split = item.split("、");
            if (split.length < 2) {
                throw new IllegalArgumentException(TextUtils2.getStringFormat("%d 行, 不包含'.' or '、': %s", lineNum, item));
            }
        }
        if (!TextUtils.isDigitsOnly(split[0])) {
            throw new IllegalArgumentException(TextUtils2.getStringFormat("%d 行, 不是以数字开头: %s", lineNum, item));
        }
    }
}
