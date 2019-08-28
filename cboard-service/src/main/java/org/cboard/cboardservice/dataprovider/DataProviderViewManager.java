package org.cboard.cboardservice.dataprovider;

import com.google.common.collect.Ordering;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang3.StringUtils;
import org.cboard.cboardservice.dataprovider.annotation.DatasourceParameter;
import org.cboard.cboardservice.dataprovider.annotation.QueryParameter;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by yfyuan on 2016/8/15.
 */
public class DataProviderViewManager {

    private static Logger LOG = LoggerFactory.getLogger(DataProviderViewManager.class);

    private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_22);

    static {
        // 指定加载模板所在的路径
        CONFIGURATION.setTemplateLoader(new ClassTemplateLoader(DataProviderViewManager.class, "/templates/config/"));
        CONFIGURATION.setDefaultEncoding("UTF-8");
        CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        CONFIGURATION.setCacheStorage(NullCacheStorage.INSTANCE);
    }


    public static List<Map<String, Object>> getQueryParams(String type, String page, Map<String, String> dataSource) {
        Class clz = DataProviderManager.getDataProviderClass(type);
        Set<Field> fieldSet = ReflectionUtils.getAllFields(clz, ReflectionUtils.withAnnotation(QueryParameter.class));
        List<Field> fieldList = fieldOrdering.sortedCopy(fieldSet);
        List<Map<String, Object>> params = null;
        try {
            Object o = clz.newInstance();
            params = new ArrayList<>();
            for (Field field : fieldList) {
                field.setAccessible(true);
                QueryParameter queryParameter = field.getAnnotation(QueryParameter.class);
                Map<String, Object> param = new HashMap<>();
                param.put("label", queryParameter.label());
                param.put("type", queryParameter.type().toString());
                param.put("name", (String) field.get(o));
                param.put("placeholder", queryParameter.placeholder());
                param.put("value", queryParameter.value());
                param.put("options", queryParameter.options());
                String optionsMethod = queryParameter.optionsMethod();
                try {
                    if (StringUtils.isNotBlank(optionsMethod) && dataSource != null) {
                        for (Class supClz = clz; supClz != Object.class; supClz = supClz.getSuperclass()) {
                            if (supClz == DataProvider.class) {
                                Field f = supClz.getDeclaredField("dataSource");
                                f.setAccessible(true);
                                f.set(o, dataSource);
                            }
                        }
                        Method method = clz.getDeclaredMethod(optionsMethod);
                        method.setAccessible(true);
                        param.put("options", method.invoke(o));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                param.put("checked", queryParameter.checked());
                param.put("required", queryParameter.required());
                /*
                不同页面显示不同输入框
                 */
                String pageType = queryParameter.pageType();
                if (pageType.contains("all") || StringUtils.isBlank(page)) {
                    params.add(param);
                } else if ("test.html".equals(page) && pageType.contains("test")) {
                    params.add(param);
                } else if ("dataset.html".equals(page) && pageType.contains("dataset")) {
                    params.add(param);
                } else if ("widget.html".equals(page) && pageType.contains("widget")) {
                    params.add(param);
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
        return params;
    }

    public static String getQueryView(String type, String page, Map<String, String> dataSource) {
        List<Map<String, Object>> params = getQueryParams(type, page, dataSource);
        StringBuilderWriter result = new StringBuilderWriter();
        if (params != null && params.size() > 0) {
            Map<String, Object> map = new HashMap<String, Object>() {
                {
                    put("params", params);
                }
            };
            try {
                Template template = CONFIGURATION.getTemplate("query.ftl");
                template.process(map, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    public static List<Map<String, Object>> getDatasourceParams(String type) {
        Class clz = DataProviderManager.getDataProviderClass(type);
        Set<Field> fieldSet = ReflectionUtils.getAllFields(clz, ReflectionUtils.withAnnotation(DatasourceParameter.class));
        List<Field> fieldList = fieldOrdering.sortedCopy(fieldSet);
        List<Map<String, Object>> params = null;
        try {
            Object o = clz.newInstance();
            params = new ArrayList<>();
            for (Field field : fieldList) {
                field.setAccessible(true);
                DatasourceParameter datasourceParameter = field.getAnnotation(DatasourceParameter.class);
                Map<String, Object> param = new HashMap<>();
                param.put("label", datasourceParameter.label());
                param.put("type", datasourceParameter.type().toString());
                param.put("name", (String) field.get(o));
                param.put("placeholder", datasourceParameter.placeholder());
                param.put("value", datasourceParameter.value());
                param.put("options", datasourceParameter.options());
                param.put("checked", datasourceParameter.checked());
                param.put("required", datasourceParameter.required());
                params.add(param);
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
        return params;
    }

    public static String getDatasourceView(String type) {
        List<Map<String, Object>> params = getDatasourceParams(type);
        StringBuilderWriter result = new StringBuilderWriter();
        try {
            if (params != null && params.size() > 0) {
                Map<String, Object> map = new HashMap<String, Object>() {
                    {
                        put("params", params);
                    }
                };
                Template template = CONFIGURATION.getTemplate("datasource.ftl");
                template.process(map, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private static Ordering<Field> fieldOrdering = Ordering.from(new Comparator<Field>() {
        @Override
        public int compare(Field o1, Field o2) {
            return Integer.compare(getOrder(o1), getOrder(o2));
        }

        private int getOrder(Field field) {
            field.setAccessible(true);
            DatasourceParameter datasourceParameter = field.getAnnotation(DatasourceParameter.class);
            if (datasourceParameter != null) {
                return datasourceParameter.order();
            }
            QueryParameter queryParameter = field.getAnnotation(QueryParameter.class);
            if (queryParameter != null) {
                return queryParameter.order();
            }
            return 0;
        }
    });

}