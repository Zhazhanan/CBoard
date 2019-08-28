package org.cboard.cboardservice.modules.pojo;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by yfyuan on 2016/10/11.
 */
@Data
public class DashboardDataset {

    private Long id;
    private String userId;
    private String name;
    private String categoryName;
    private String data;
    private String permission;
    private String userName;
    private String loginName;
    private Timestamp createTime;
    private Timestamp updateTime;
}
