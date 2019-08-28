package org.cboard.cboardservice.modules.dto;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import lombok.Data;
import org.cboard.cboardservice.modules.pojo.DashboardWidget;
import org.cboard.cboardservice.modules.services.role.RolePermission;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by yfyuan on 2016/8/19.
 */
@Data
public class ViewDashboardWidget implements Serializable {

    private static final long serialVersionUID = -3168389249078888650L;
    private Long id;
    private String userId;
    private String name;
    private String categoryName;
    private String userName;
    private String loginName;
    private String createTime;
    private String updateTime;
    private Map<String, Object> data;
    private boolean edit;
    private boolean delete;

    public static final Function TO = new Function<DashboardWidget, ViewDashboardWidget>() {
        @Nullable
        @Override
        public ViewDashboardWidget apply(@Nullable DashboardWidget input) {
            return new ViewDashboardWidget(input);
        }
    };

    public ViewDashboardWidget(DashboardWidget widget) {
        this.id = widget.getId();
        this.userId = widget.getUserId();
        this.name = widget.getName();
        this.categoryName = widget.getCategoryName();
        this.userName = widget.getUserName();
        this.loginName = widget.getLoginName();
        this.createTime = widget.getCreateTime().toString();
        this.updateTime = widget.getUpdateTime().toString();
        this.data = JSONObject.parseObject(widget.getData());
        this.edit = RolePermission.isEdit(widget.getPermission());
        this.delete = RolePermission.isDelete(widget.getPermission());
    }
}
