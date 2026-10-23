package com.actor.cnpc_qhse_exams.apis;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestHost;

import java.util.List;

/**
 * description: 描述
 * company    :
 *
 * @author : ldf
 * date       : 2026/10/21 on 12
 * @version 1.0
 */
public class GetQbQuestionList20261016Api implements IRequestApi, IRequestHost {

    @HttpHeader
    public String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo3Njg0MTksInQiOjE3OTI1NTYyNDMsImlzQXV0aCI6dHJ1ZSwicHJvdmlkZXIiOiJ3ZWl4aW4iLCJhdmF0YXIiOiJodHRwczovL3dvcmstMTMwMDY1MDEyOC5maWxlLm15cWNsb3VkLmNvbS91Lzc2ODQxOS8xNzgzMzM2MzM1NzI1LmpwZyIsIm5pY2tfbmFtZSI6InRlc3QxMjMiLCJvbmxpbmUiOnRydWUsIm9wZW5pZCI6Im9QdFdzNU5MOWVOejZ1M3psRHdScm5nSzloUHMifQ.YXYYHgDhTYhT00FQEu98AQk_xmqS6Wr1gRj9xJ4VH24";

    public int offset;
    public final int limit = 10;
    /**
     * 1: 单选题
     * 2: 多选题
     */
    public int question_type;
    public final int qb_id = 70976;

    /**
     * @param page 第几页, 从1开始
     * @param questionType 1: 单选题, 2: 多选题, 3: 判断题
     */
    public GetQbQuestionList20261016Api(@IntRange(from = 1) int page, int questionType) {
        this.offset = (page - 1) * 10;
        this.question_type = questionType;
    }

    @NonNull
    @Override
    public String getApi() {
        return "https://work.zhima.team/qb/v2/get_qb_question_list";
    }

    @NonNull
    @Override
    public String getHost() {
        return "";
    }

    public static class GetQbQuestionListBean {

        /**
         * code : 0
         * message :
         * data : {"question_list":[{"question_id":4391153,"describe":"根据附件3《驾驶员道路交通安全记分标准》，车辆存在安全隐患，未及时报告、检修或未采取安全措施的，一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":3,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391154,"describe":"根据附件3《驾驶员道路交通安全记分标准》，存在公车私用行为的，一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":3,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391155,"describe":"根据附件3《驾驶员道路交通安全记分标准》，私自拆除、改变车辆卫星定位监控装置，破坏卫星定位车载终端，屏蔽监控终端信号的，一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":3,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391156,"describe":"根据附件3《驾驶员道路交通安全记分标准》，驾驶员出车前未开展酒精检测的，或者伪造、虚假填写酒精检测记录的,一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":3,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391157,"describe":"根据附件3《驾驶员道路交通安全记分标准》,出现伪造或者虚假填写\u201c回场检验\u201d和\u201c一日三检\u201d的,一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":3,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391158,"describe":"根据附件3《驾驶员道路交通安全记分标准》,发生一般B级道路交通事故负次要责任的,一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":2,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391159,"describe":"根据附件3《驾驶员道路交通安全记分标准》,发生一般C级道路交通事故负同等及以上责任的,一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":2,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391160,"describe":"根据附件3《驾驶员道路交通安全记分标准》,存在超速行驶、疲劳驾驶等交通违法违规行为受到公司通报的,一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":2,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391161,"describe":"根据附件3《驾驶员道路交通安全记分标准》,驾驶时使用手机或行车不系安全带的,一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":2,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391162,"describe":"根据附件3《驾驶员道路交通安全记分标准》,私自破坏、停用行车记录仪的,一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":2,"analysis":"","analysis_url_list":[],"question_type":1}],"qb_id":70976,"qb_name":"驾驶员季度交通安全理论知识测评题库--刷题版","offset":10,"limit":10,"keyword":"","operate":0,"total_question_count":199}
         */

        public int code;
        public String   message;
        public DataBean data;

        public boolean isSuccess() {
            return code == 0;
        }

        public static class DataBean {
            /**
             * question_list : [{"question_id":4391153,"describe":"根据附件3《驾驶员道路交通安全记分标准》，车辆存在安全隐患，未及时报告、检修或未采取安全措施的，一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":3,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391154,"describe":"根据附件3《驾驶员道路交通安全记分标准》，存在公车私用行为的，一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":3,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391155,"describe":"根据附件3《驾驶员道路交通安全记分标准》，私自拆除、改变车辆卫星定位监控装置，破坏卫星定位车载终端，屏蔽监控终端信号的，一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":3,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391156,"describe":"根据附件3《驾驶员道路交通安全记分标准》，驾驶员出车前未开展酒精检测的，或者伪造、虚假填写酒精检测记录的,一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":3,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391157,"describe":"根据附件3《驾驶员道路交通安全记分标准》,出现伪造或者虚假填写\u201c回场检验\u201d和\u201c一日三检\u201d的,一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":3,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391158,"describe":"根据附件3《驾驶员道路交通安全记分标准》,发生一般B级道路交通事故负次要责任的,一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":2,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391159,"describe":"根据附件3《驾驶员道路交通安全记分标准》,发生一般C级道路交通事故负同等及以上责任的,一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":2,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391160,"describe":"根据附件3《驾驶员道路交通安全记分标准》,存在超速行驶、疲劳驾驶等交通违法违规行为受到公司通报的,一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":2,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391161,"describe":"根据附件3《驾驶员道路交通安全记分标准》,驾驶时使用手机或行车不系安全带的,一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":2,"analysis":"","analysis_url_list":[],"question_type":1},{"question_id":4391162,"describe":"根据附件3《驾驶员道路交通安全记分标准》,私自破坏、停用行车记录仪的,一次记多少分？","url_list":[],"option_describe":[{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}],"answer":2,"analysis":"","analysis_url_list":[],"question_type":1}]
             * qb_id : 70976
             * qb_name : 驾驶员季度交通安全理论知识测评题库--刷题版
             * offset : 10
             * limit : 10
             * keyword :
             * operate : 0
             * total_question_count : 199
             */

            public int qb_id;
            public String                 qb_name;
            public int                    offset;
            public int                    limit;
            public String                 keyword;
            public int                    operate;
            public int                    total_question_count;
            public List<QuestionListBean> question_list;

            public static class QuestionListBean {
                /**
                 * question_id : 4391153
                 * describe : 根据附件3《驾驶员道路交通安全记分标准》，车辆存在安全隐患，未及时报告、检修或未采取安全措施的，一次记多少分？
                 * url_list : []
                 * option_describe : [{"id":1,"describe":"1分","url_list":[]},{"id":2,"describe":"3分","url_list":[]},{"id":3,"describe":"6分","url_list":[]},{"id":4,"describe":"12分","url_list":[]}]
                 * answer : 3               //从1开始
                 * answer_list: [1, 2, 3]   //从1开始
                 * analysis :
                 * analysis_url_list : []
                 * question_type : 1
                 */

                public int question_id;
                public String                   describe;
                public int                      answer;     //单选/选择题(0❌️ 1✔️) 才有这个字段
                public List<Integer>            answer_list;//多选 才有这个字段
                public String                   analysis;
                public int                      question_type;
                public List<?>                  url_list;
                public List<OptionDescribeBean> option_describe;    //选择题没有这个选项字段
                public List<?>                  analysis_url_list;

                public static class OptionDescribeBean {
                    /**
                     * id : 1
                     * describe : 1分
                     * url_list : []
                     */

                    public int id;
                    public String  describe;
                    public List<?> url_list;
                }
            }
        }
    }
}
