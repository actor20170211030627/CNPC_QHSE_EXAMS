package com.actor.cnpc_qhse_exams.utils;

import android.text.TextUtils;

import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.cnpc_qhse_exams.bean.SubjectDriverJson;
import com.actor.myandroidframework.utils.AssetsUtils;
import com.actor.myandroidframework.utils.LogUtils;
import com.actor.myandroidframework.utils.TextUtils2;
import com.blankj.utilcode.util.GsonUtils;

import java.util.ArrayList;
import java.util.List;

public class SubjectJsonUtils_2026071201 {

    /**
     * 版本：20260712
     * 将Excel转换成json，https://www.minifier.org/excel-to-json
     */
    private static final long version = 2026071201;

    /**
     * 读取单选题，593道题
     */
    public static List<SubjectDriver> readJsonSingle() {
        String json = AssetsUtils.readAssets2String("excel-to-json_single.json", "utf-8");
        List<SubjectDriverJson> subjectDrivers = GsonUtils.fromJson(json, GsonUtils.getListType(SubjectDriverJson.class));
        StringBuilder sb = new StringBuilder(250);
        //标题，选项，答案
        String subject, options, answer;
        List<SubjectDriver> drivers = new ArrayList<>(595);
        for (SubjectDriverJson subjectDriver : subjectDrivers) {
            /**
             * 汽车在泥泞道路上行驶时，应该注意()。
             * A 可以中途换档，制动、转向和停车;
             * B 不可急转方向盘或转动角度过大;
             * C 如果车轮空转打滑，应挖去泥浆铺上砂石草木等;
             * 解析:B
             */
            String contents = subjectDriver.content;
            String[] splits = contents.split("\r\n");
            //初始化变量
            sb.setLength(0);
            subject = splits[0];
            answer = null;
            for (int i = 1; i < splits.length; i++) {
                if (i < splits.length - 1) {
                    sb.append(splits[i]);
                    if (i < splits.length - 2) sb.append("\n");
                } else answer = splits[i];
            }
            options = sb.toString();
            judgeOptionsAndThrow(contents, options);
            answer = answer.split("解析:")[1].trim();
            String format = TextUtils2.getStringFormat("subject = %s, options = %s, answer = %s", subject, options, answer);
            if (TextUtils.isEmpty(subject) || TextUtils.isEmpty(options) || TextUtils.isEmpty(answer)) {
                throw new RuntimeException(format);
            } else LogUtils.error(format);

            drivers.add(new SubjectDriver(version, -1, 1, null, subject, options, answer, null));
        }
        return drivers;
    }

    /**
     * 读取多选题，80道题
     */
    public static List<SubjectDriver> readJsonMulti() {
        String json = AssetsUtils.readAssets2String("excel-to-json_multi.json", "utf-8");
        List<SubjectDriverJson> subjectDrivers = GsonUtils.fromJson(json, GsonUtils.getListType(SubjectDriverJson.class));
        StringBuilder sb = new StringBuilder(250);
        //标题，选项，答案
        String subject, options, answer;
        List<SubjectDriver> drivers = new ArrayList<>(85);
        for (SubjectDriverJson subjectDriver : subjectDrivers) {
            /**
             * 根据《西南油气田分公司道路交通安全管理办法》，车辆按照运行风险大小分为几类?包括?
             * A 一类车辆:危险货物运输车辆等
             * B 二类车辆:普通货运车辆等
             * C 三类车辆:一类、二类以外的其他车辆
             * D 四类车辆:私家车
             * 解析:ABC
             */
            String contents = subjectDriver.content;
            String[] splits = contents.split("\r\n");
            //初始化变量
            sb.setLength(0);
            subject = splits[0];
            answer = null;
            for (int i = 1; i < splits.length; i++) {
                if (i < splits.length - 1) {
                    sb.append(splits[i]);
                    if (i < splits.length - 2) sb.append("\n");
                } else answer = splits[i];
            }
            options = sb.toString();
            judgeOptionsAndThrow(contents, options);
            answer = answer.split("解析:")[1].trim();
            String format = TextUtils2.getStringFormat("subject = %s, options = %s, answer = %s", subject, options, answer);
            if (TextUtils.isEmpty(subject) || TextUtils.isEmpty(options) || TextUtils.isEmpty(answer)) {
                throw new RuntimeException(format);
            } else LogUtils.error(format);

            drivers.add(new SubjectDriver(version, -1, 2, null, subject, options, answer, null));
        }
        return drivers;
    }

    /**
     * 读取判断题，182道题
     */
    public static List<SubjectDriver> readJsonJudge() {
        String json = AssetsUtils.readAssets2String("excel-to-json_judge.json", "utf-8");
        List<SubjectDriverJson> subjectDrivers = GsonUtils.fromJson(json, GsonUtils.getListType(SubjectDriverJson.class));
        //标题，答案
        String subject, answer;
        List<SubjectDriver> drivers = new ArrayList<>(185);
        for (SubjectDriverJson subjectDriver : subjectDrivers) {
            /**
             * 根据《重庆气矿汽车服务大队驾驶员操作手册》，驾驶员需每月定期如实填报运行里程数，并按时完成应知应会知识答题。
             * 正确
             * 错误
             * 解析:正确
             */
            String contents = subjectDriver.content;
            String[] splits = contents.split("\r\n");
            //初始化变量
            subject = splits[0];
            //判断第2,3行一定是无用行，否则就可能是标题或答案，就 throw
            if (!TextUtils.equals(splits[1].trim(), "正确") && !TextUtils.equals(splits[2].trim(), "错误")) {
                throw new RuntimeException(contents);
            }
            answer = splits[splits.length - 1].split("解析:")[1].trim();
            String format = TextUtils2.getStringFormat("subject = %s, answer = %s", subject, answer);
            //校验标题，答案
            if (TextUtils.isEmpty(subject) || TextUtils.isEmpty(answer)) {
                throw new RuntimeException(format);
            } else if (!TextUtils.equals(answer, "正确") && !TextUtils.equals(answer, "错误")) {
                throw new RuntimeException(format);
            }else LogUtils.error(format);

            drivers.add(new SubjectDriver(version, -1, 3, null, subject, null, answer, null));
        }
        return drivers;
    }

    /**
     * 判断选项是否是A开头，if不是选项就 throw，防止读取到标题
     */
    private static void judgeOptionsAndThrow(String contents, String options) {
        if (!options.startsWith("A")) {
            throw new RuntimeException(contents);
        }
    }
}
