package org.cboard.cboardservice.modules.pojo;

import lombok.Data;

/**
 * Created by yfyuan on 2017/5/5.
 */
@Data
public class DashboardBoardParam {
    private Long id;
    private String userId;
    private Long boardId;
    private String config;

}
