package org.cboard.cboardservice.modules.pojo;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by yfyuan on 2016/8/18.
 */
@Data
public class DashboardDatasource {

    private Long id;
    private String userId;
    private String name;
    private String type;
    private String config;
    private String permission;
    private String userName;
    private String loginName;
    private Timestamp createTime;
    private Timestamp updateTime;
}
