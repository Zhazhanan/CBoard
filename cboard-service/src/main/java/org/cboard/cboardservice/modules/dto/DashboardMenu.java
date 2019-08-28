package org.cboard.cboardservice.modules.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by yfyuan on 2016/12/21.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardMenu implements Serializable {

    private static final long serialVersionUID = 6613825652238712519L;
    private long menuId;
    private long parentId;
    private String menuName;
    private String menuCode;
}
