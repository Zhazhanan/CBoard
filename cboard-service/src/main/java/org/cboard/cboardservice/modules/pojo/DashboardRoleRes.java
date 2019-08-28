package org.cboard.cboardservice.modules.pojo;

import lombok.Data;

/**
 * Created by yfyuan on 2016/12/7.
 */
@Data
public class DashboardRoleRes {
    private Long roleResId;
    private String roleId;
    private Long resId;
    private String resType;
    private String permission;
}
