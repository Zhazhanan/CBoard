package org.cboard.cboardservice.modules.pojo;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by yfyuan on 2016/8/18.
 */
@Data
public class DashboardBoard {

    private Long id;
    private String userId;
    private Long categoryId;
    private String name;
    private String layout;
    private String categoryName;
    private String userName;
    private String loginName;
    private String permission;
    private Timestamp createTime;
    private Timestamp updateTime;
}
