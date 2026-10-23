package com.actor.cnpc_qhse_exams.apis;

import androidx.annotation.NonNull;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestHost;

/**
 * description: 描述
 * company    :
 *
 * @author : ldf
 * date       : 2026/10/21 on 16
 * @version 1.0
 */
public class LoginApi implements IRequestApi, IRequestHost {

    public String code = "0c1t58nl2wE7Ci4DFZkl216psX3t58nV";
    public String provider = "weixin";
    public int register_source = 1012;

    @NonNull
    @Override
    public String getApi() {
        return "https://work.zhima.team/user/login/";
    }

    @NonNull
    @Override
    public String getHost() {
        return "";
    }

    public static class LoginBean {

        /**
         * code : 0
         * message :
         * data : {"userId":768419,"isAuth":true,"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo3Njg0MTksInQiOjE3OTI1NzIwNTYsImlzQXV0aCI6dHJ1ZSwicHJvdmlkZXIiOiJ3ZWl4aW4iLCJhdmF0YXIiOiJodHRwczovL3dvcmstMTMwMDY1MDEyOC5maWxlLm15cWNsb3VkLmNvbS91Lzc2ODQxOS8xNzgzMzM2MzM1NzI1LmpwZyIsIm5pY2tfbmFtZSI6InRlc3QxMjMiLCJvbmxpbmUiOnRydWUsIm9wZW5pZCI6Im9QdFdzNU5MOWVOejZ1M3psRHdScm5nSzloUHMifQ.RfNN_ahFyHXgYOuf9pNmrmnRYzuBjwvpfRjH5Cz_5B0","register_time":1783336234,"openid":"oPtWs5NL9eNz6u3zlDwRrngK9hPs"}
         */

        public int code;
        public String   message;
        public DataBean data;

        public static class DataBean {
            /**
             * userId : 768419
             * isAuth : true
             * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo3Njg0MTksInQiOjE3OTI1NzIwNTYsImlzQXV0aCI6dHJ1ZSwicHJvdmlkZXIiOiJ3ZWl4aW4iLCJhdmF0YXIiOiJodHRwczovL3dvcmstMTMwMDY1MDEyOC5maWxlLm15cWNsb3VkLmNvbS91Lzc2ODQxOS8xNzgzMzM2MzM1NzI1LmpwZyIsIm5pY2tfbmFtZSI6InRlc3QxMjMiLCJvbmxpbmUiOnRydWUsIm9wZW5pZCI6Im9QdFdzNU5MOWVOejZ1M3psRHdScm5nSzloUHMifQ.RfNN_ahFyHXgYOuf9pNmrmnRYzuBjwvpfRjH5Cz_5B0
             * register_time : 1783336234
             * openid : oPtWs5NL9eNz6u3zlDwRrngK9hPs
             */

            public int userId;
            public boolean isAuth;
            public String  token;
            public int     register_time;
            public String  openid;
        }
    }
}
