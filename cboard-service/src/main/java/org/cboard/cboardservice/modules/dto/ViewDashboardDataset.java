package org.cboard.cboardservice.modules.dto;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import lombok.Data;
import org.cboard.cboardservice.modules.pojo.DashboardDataset;
import org.cboard.cboardservice.modules.services.role.RolePermission;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by yfyuan on 2016/10/11.
 */
@Data
public class ViewDashboardDataset implements Serializable {
    private static final long serialVersionUID = 8714636768396034069L;
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


    public static final Function TO = new Function<DashboardDataset, ViewDashboardDataset>() {
        @Nullable
        @Override
        public ViewDashboardDataset apply(@Nullable DashboardDataset input) {
            return new ViewDashboardDataset(input);
        }
    };

    public ViewDashboardDataset(DashboardDataset dataset) {
        this.id = dataset.getId();
        this.userId = dataset.getUserId();
        this.name = dataset.getName();
        this.userName = dataset.getUserName();
        this.categoryName = dataset.getCategoryName();
        this.loginName = dataset.getLoginName();
        this.createTime = dataset.getCreateTime().toString();
        this.updateTime = dataset.getUpdateTime().toString();
        this.data = JSONObject.parseObject(dataset.getData());
        this.edit = RolePermission.isEdit(dataset.getPermission());
        this.delete = RolePermission.isDelete(dataset.getPermission());
    }
}
