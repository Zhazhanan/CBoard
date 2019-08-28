package org.cboard.cboardservice.modules.dto;

import lombok.Data;
import org.cboard.cboardservice.modules.pojo.DashboardRoleRes;
import org.cboard.cboardservice.modules.services.role.RolePermission;

import java.io.Serializable;

/**
 * Created by yfyuan on 2017/3/14.
 */
@Data
public class ViewDashboardRoleRes implements Serializable {

    private static final long serialVersionUID = -3126025679614897840L;
    private Long roleResId;
    private String roleId;
    private Long resId;
    private String resType;
    private boolean edit;
    private boolean delete;


    public ViewDashboardRoleRes(DashboardRoleRes dashboardRoleRes) {
        this.roleResId = dashboardRoleRes.getRoleResId();
        this.roleId = dashboardRoleRes.getRoleId();
        this.resId = dashboardRoleRes.getResId();
        this.resType = dashboardRoleRes.getResType();
        this.edit = RolePermission.isEdit(dashboardRoleRes.getPermission());
        this.delete = RolePermission.isDelete(dashboardRoleRes.getPermission());
    }

}
