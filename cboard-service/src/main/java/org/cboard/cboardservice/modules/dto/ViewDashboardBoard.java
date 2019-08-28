package org.cboard.cboardservice.modules.dto;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import lombok.Data;
import org.cboard.cboardservice.modules.pojo.DashboardBoard;
import org.cboard.cboardservice.modules.services.role.RolePermission;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by yfyuan on 2016/8/23.
 */
@Data
public class ViewDashboardBoard implements Serializable {

    private static final long serialVersionUID = 4296692540427149767L;
    private Long id;
    private String userId;
    private Long categoryId;
    private String name;
    private String userName;
    private String loginName;
    private String createTime;
    private String updateTime;
    private Map<String, Object> layout;
    private String categoryName;
    private boolean edit;
    private boolean delete;

    public static final Function TO = new Function<DashboardBoard, ViewDashboardBoard>() {
        @Nullable
        @Override
        public ViewDashboardBoard apply(@Nullable DashboardBoard input) {
            return new ViewDashboardBoard(input);
        }
    };

    public ViewDashboardBoard(DashboardBoard board) {
        this.id = board.getId();
        this.userId = board.getUserId();
        this.categoryId = board.getCategoryId();
        this.name = board.getName();
        this.userName = board.getUserName();
        this.loginName = board.getLoginName();
        this.createTime = board.getCreateTime().toString();
        this.updateTime = board.getUpdateTime().toString();
        this.layout = JSONObject.parseObject(board.getLayout());
        this.categoryName = board.getCategoryName();
        this.edit = RolePermission.isEdit(board.getPermission());
        this.delete = RolePermission.isDelete(board.getPermission());
    }

}
