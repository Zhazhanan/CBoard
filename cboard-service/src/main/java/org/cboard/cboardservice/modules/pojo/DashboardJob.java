package org.cboard.cboardservice.modules.pojo;

import lombok.Data;

import java.util.Date;

/**
 * Created by yfyuan on 2017/2/16.
 */
@Data
public class DashboardJob {

    private Long id;
    private String name;
    private String cronExp;
    private Date startDate;
    private Date endDate;
    private String jobType;
    private String config;
    private String userId;
    private String userName;
    private Date lastExecTime;
    private Long jobStatus;
    private String execLog;
    private String permission;
}
