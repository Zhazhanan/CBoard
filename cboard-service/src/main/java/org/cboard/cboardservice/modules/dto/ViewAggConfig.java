package org.cboard.cboardservice.modules.dto;

import lombok.Data;
import org.cboard.cboardservice.dataprovider.config.AggConfig;
import org.cboard.cboardservice.dataprovider.config.ConfigComponent;
import org.cboard.cboardservice.dataprovider.config.DimensionConfig;
import org.cboard.cboardservice.dataprovider.config.ValueConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yfyuan on 2017/4/27.
 */
@Data
public class ViewAggConfig implements Serializable {

    private static final long serialVersionUID = 6426669983848853273L;
    private List<DimensionConfig> rows;
    private List<DimensionConfig> columns;
    private List<DimensionConfig> filters;
    private List<ValueConfig> values;

    public static AggConfig getAggConfig(ViewAggConfig viewAggConfig) {
        if (viewAggConfig != null) {
            AggConfig aggConfig = new AggConfig();
            aggConfig.setRows(viewAggConfig.getRows());
            aggConfig.setColumns(viewAggConfig.getColumns());
            if (viewAggConfig.getFilters() != null) {
                aggConfig.setFilters(viewAggConfig.getFilters().stream().map(e -> (ConfigComponent) e).collect(Collectors.toList()));
            } else {
                aggConfig.setFilters(new ArrayList<>());
            }
            aggConfig.setValues(viewAggConfig.getValues());
            return aggConfig;
        }
        return null;
    }
}
