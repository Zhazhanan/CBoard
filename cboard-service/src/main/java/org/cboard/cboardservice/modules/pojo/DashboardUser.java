package org.cboard.cboardservice.modules.pojo;

import lombok.Data;

/**
 * Created by yfyuan on 2016/12/2.
 */
@Data
public class DashboardUser {
    private String userId;
    private String loginName;
    private String userName;
    private String userPassword;
    private String userStatus;
}
