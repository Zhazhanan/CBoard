package org.cboard.cboardservice.modules.pojo;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by yfyuan on 2016/8/22.
 */
@Data
public class DashboardWidget {

    private Long id;
    private String userId;
    private String name;
    private String categoryName;
    private String userName;
    private String loginName;
    private String data;
    private String permission;
    private Timestamp createTime;
    private Timestamp updateTime;
}
