package org.cboard.cboardservice.modules.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by yfyuan on 2016/8/26.
 */
@Data
@NoArgsConstructor
public class DataProviderResult implements Serializable {

    private String[][] data;
    private String[] columns;
    private String msg;
    private int resultCount = 0;

    public DataProviderResult(String[][] data, String msg) {
        this.data = data;
        this.msg = msg;
    }

    public DataProviderResult(String[][] data, String msg, int resultCount) {
        this.data = data;
        this.msg = msg;
        this.resultCount = resultCount;
    }
}
