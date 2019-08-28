package org.cboard.cboardservice.modules.dto;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.Date;

/**
 * Created by zyong on 2017/9/28.
 */
@Data
public class CBoardActionLog {

    private User user;
    private String requestUrl;
    private Date actionTime = new Date();

    public CBoardActionLog() {
    }

    public CBoardActionLog(User user, String requestUrl) {
        this.user = user;
        this.requestUrl = requestUrl;
    }


    @Override
    public String toString() {
        return JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss.SSS");
    }
}
