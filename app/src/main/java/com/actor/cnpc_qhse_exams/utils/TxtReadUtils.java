package com.actor.cnpc_qhse_exams.utils;

import android.text.TextUtils;

import com.actor.cnpc_qhse_exams.bean.SubjectDriver;
import com.actor.database.greendao.GreenDaoUtils;
import com.actor.myandroidframework.utils.AssetsUtils;
import com.actor.myandroidframework.utils.LogUtils;
import com.actor.myandroidframework.utils.TextUtils2;
import com.greendao.gen.SubjectDriverDao;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * description: 读取文本, 将各种题目类型读取成SubjectDriver
 * company    :
 *
 * @author : ldf
 * date       : 2026/3/19 on 16
 * @version 1.0
 */
public class TxtReadUtils {

    /**
     * 读取文本, 将各种题目类型读取成SubjectDriver, 并存如数据库
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
     * 128、采用液压刹车系统的小轿车是利用液体的(           )原理。  //4783行
     * 可压缩 （B）可传递 （C）不可压缩 （D）不可传递                //没有"（A）", 需补上
     *
     * 129、下列不是刹车系统必须具备的特性是（          ）           //4786行: 这1道题的选项没有ABCD, 要补上...
     * 能够产生足够的减速度。                                        //（A）
     * 有一定的稳定度。                                           //（B）
     * 刹车系统的关联性。                                          //（C）
     * 刹车系统的动态稳定性。                                        //（D）
     * 答文：C
     *
     *
     * //-- 查询选项中有"答文"或"答案"的item:
     * 14.【考核点：层级+专业；题型：单选题；难度：中；类型：专业】 //5139行
     * 汽车驶经学校、影剧院、百货商店和其他一些行人较多的场所附近时，应以（ A    ）通过。
     * （A）低速行驶；
     * （B）经济车速；
     * （C）中等车速；
     * （D）正常车速；
     * 答文：                                                  //这一行改成: "答文：A", 否则会合并到D选项
     * 解析：
     *
     * 27.【考核点：层级+专业；题型：单选题；难度：中；类型：专业】 //5258行
     * 高原山区行车应适当调整发动机的点火提前角，与平原地区相比可（ C ）。
     * （A）推迟 2º-3º；
     * （B）推迟 1º-2º；
     * （C）提前 2º-3º；
     * （D）提前 4º-5º；
     * 答文：                                                   //这一行改成: "答文：C", 否则会合并到D选项
     * 解析：
     *
     *
     * 24.【考核点：层级+专业；题型：单选题；难度：中；类型：专业】 //5608行
     * （A）类火灾是指（ ）火灾。                                //这一行是标题, 将"A" 改成 "Ą", 否则读取不到标题
     * （A）固体物质；
     * （B）液体；
     * （C）气体；
     * （D）金属；
     * 答文：A
     * 解析：
     *
     *
     * //有图片的题目:
     * 68.【考核点：层级+专业；题型：单选题；难度：中；类型：专业】
     * 汽车行驶过程中，电子仪表盘上出现（    ）黄色信号并伴有一种警告声，表示制动灯有故
     * 障。
     * （A）   ；
     * （B）   ；
     * （C）    ；
     * （D）    ；
     * 答文：A
     * 解析：
     * 69.【考核点：层级+专业；题型：单选题；难度：中；类型：专业】
     * 在汽车行驶过程中，电子仪表盘上出现（    ）黄色信号，表示燃油箱液面太低。
     * （A）    ；
     * （B）    ；
     * （C）        ；
     * （D）        ；
     * 答文：B
     * 解析：
     * 3.【考核点：层级+专业；题型：多选题；难度：易；类型：基础】
     * 如图所示，驾驶机动车要采取的正确做法是（       ）。
     *                                                        //这儿有张图片
     * （A）减速；
     * （B）鸣喇叭；
     * （C）靠右侧行驶；
     * （D）靠左侧行驶；
     * 答文：ABC
     * 解析：
     * 9.【考核点：层级+专业；题型：多选题；难度：易；类型：基础】
     * 如图所示，此车在行驶中，驾驶人要采取的措施是（       ）。
     *                                                        //这儿有张图片
     * （A）控制车速，避免紧急制动；
     * （B）尽量循前车的车辙走；
     * （C）避免急转方向；
     * （D）紧跟前车；
     * 答文：ABC
     * 解析：
     */
    public static void readTxt2SubjectDrivers() {
        String content = AssetsUtils.readAssets2String("交通安全基层站队QHSE标准化建设—驾驶员应知应会题库20260316_assets.txt", Charset.defaultCharset().name());
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
        LogUtils.errorFormat("subjects51.size() = %d", subjects51.size());  //151 (跳过了第84题, 所以数据库中最大编号是152)
        LogUtils.errorFormat("subjects52.size() = %d", subjects52.size());  //0
        LogUtils.errorFormat("subjects53.size() = %d", subjects53.size());  //20
        LogUtils.errorFormat("subjects61.size() = %d", subjects61.size());  //41
        LogUtils.errorFormat("subjects62.size() = %d", subjects62.size());  //0
        LogUtils.errorFormat("subjects63.size() = %d", subjects63.size());  //0
        LogUtils.errorFormat("subjects71.size() = %d", subjects71.size());  //28
        LogUtils.errorFormat("subjects72.size() = %d", subjects72.size());  //2
        LogUtils.errorFormat("subjects73.size() = %d", subjects73.size());  //11

        //存储
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects11);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects12);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects13);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects21);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects22);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects23);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects31);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects32);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects33);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects41);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects42);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects43);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects51);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects52);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects53);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects61);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects62);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects63);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects71);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects72);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects73);



        /**
         * 查询字段不应该为空但实际为空的item
         * SELECT * FROM SUBJECT_DRIVER WHERE CHAPTER_TYPE = 0 OR SUBJECT_TYPE = 0 OR SUBJECT IS NULL OR TRIM(SUBJECT) = '' OR ANSWER IS NULL OR TRIM(ANSWER) = '';
         */
        String sql = TextUtils2.getStringFormat("WHERE %s = 0 OR %s = 0 OR %s IS NULL OR TRIM(%s) = '' OR %s IS NULL OR TRIM(%s) = ''",
                SubjectDriverDao.Properties.ChapterType.columnName,
                SubjectDriverDao.Properties.SubjectType.columnName,
                SubjectDriverDao.Properties.Subject.columnName,
                SubjectDriverDao.Properties.Subject.columnName,
                SubjectDriverDao.Properties.Answer.columnName,
                SubjectDriverDao.Properties.Answer.columnName
        );
        List<SubjectDriver> emptyIssueList = GreenDaoUtils.queryRawCreate(SubjectSelectUtils.DAO, sql).list();
        for (SubjectDriver listOption : emptyIssueList) {
            LogUtils.errorFormat("有字段不该为空, 但为空: id=%d, subject=%s", listOption.getId(), listOption.getSubject());
        }



        /**
         * 查询选项中有"答文"或"答案"的item
         * SELECT * FROM SUBJECT_DRIVER WHERE OPTIONS LIKE "%答文%" OR OPTIONS LIKE "%答案%";
         */
        List<SubjectDriver> listOptions = GreenDaoUtils.queryList(SubjectSelectUtils.DAO,
                SubjectDriverDao.Properties.Options.like("%答文%"),
                SubjectDriverDao.Properties.Options.like("%答案%")
        );
        for (SubjectDriver listOption : listOptions) {
            LogUtils.errorFormat("读取有误: 选项中有\"答文\"or\"答案\": id=%d, subject=%s", listOption.getId(), listOption.getSubject());
        }



        /**
         * 查询有图片的题目
         * SELECT * FROM SUBJECT_DRIVER WHERE SUBJECT LIKE "%电子仪表盘上出现%" OR SUBJECT LIKE "%如图所示%";
         * 手动添加图片
         */
        List<SubjectDriver> listSubject0 = GreenDaoUtils.queryList(SubjectSelectUtils.DAO,
                SubjectDriverDao.Properties.Subject.like("%汽车行驶过程中，电子仪表盘上出现%")
        );
        //第68题, ABCD选项图片
        if (!listSubject0.isEmpty()) {
            SubjectDriver subjectDriver = listSubject0.get(0);
            if (subjectDriver.getSubject().startsWith("68")) {
                String optionImages = TextUtils2.getStringFormat("%s\n%s\n%s\n%s", "68A.jpg", "68B.jpg", "68C.jpg", "68D.jpg");
                subjectDriver.setOptionImages(optionImages);
                GreenDaoUtils.update(SubjectSelectUtils.DAO, subjectDriver);
            } else {
                LogUtils.error("手动添加图片, 第68题, ABCD选项图片 未找到!");
            }
        }
        //第69题, ABCD选项图片
        if (listSubject0.size() > 1) {
            SubjectDriver subjectDriver = listSubject0.get(1);
            if (subjectDriver.getSubject().startsWith("69")) {
                String optionImages = TextUtils2.getStringFormat("%s\n%s\n%s\n%s", "69A.jpg", "69B.jpg", "69C.jpg", "69D.jpg");
                subjectDriver.setOptionImages(optionImages);
                GreenDaoUtils.update(SubjectSelectUtils.DAO, subjectDriver);
            } else {
                LogUtils.error("手动添加图片, 第69题, ABCD选项图片 未找到!");
            }
        }


        //标题图片
        List<SubjectDriver> listSubject1 = GreenDaoUtils.queryList(SubjectSelectUtils.DAO,
                SubjectDriverDao.Properties.Subject.like("%如图所示%")
        );
        //第3题, 标题图片
        if (!listSubject1.isEmpty()) {
            SubjectDriver subjectDriver = listSubject1.get(0);
            if (subjectDriver.getSubject().startsWith("3")) {
                subjectDriver.setSubjectImage("3subject.jpg");
                GreenDaoUtils.update(SubjectSelectUtils.DAO, subjectDriver);
            } else {
                LogUtils.error("手动添加图片, 第3题, 标题图片 未找到!");
            }
        }
        //第9题, 标题图片
        if (listSubject1.size() > 1) {
            SubjectDriver subjectDriver = listSubject1.get(1);
            if (subjectDriver.getSubject().startsWith("9")) {
                subjectDriver.setSubjectImage("9subject.jpg");
                GreenDaoUtils.update(SubjectSelectUtils.DAO, subjectDriver);
            } else {
                LogUtils.error("手动添加图片, 第9题, 标题图片 未找到!");
            }
        }
    }

    /**
     * 读取单选题目
     */
    public static void readSingleSelectSubjects() {
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
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects11);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects21);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects31);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects41);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects51);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects61);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects71);
    }

    /**
     * 读取多选题目
     */
    public static void readMultiSelectSubjects() {
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
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects12);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects22);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects32);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects42);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects72);
    }

    /**
     * 读取判断题
     */
    public static void readJudgeSubjects() {
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
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects13);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects23);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects33);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects43);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects53);
        GreenDaoUtils.insertInTx(SubjectSelectUtils.DAO, subjects73);
    }
}
