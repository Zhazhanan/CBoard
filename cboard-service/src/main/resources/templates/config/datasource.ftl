<#list params as param>
<div class="form-group">
    <label>${param.label}</label>
    <#if param.type == "input">
        <input ng-model="curDatasource.config.${param.name}" class="form-control input-sm" placeholder="${param.placeholder}"/>
    <#elseif param.type == "textarea">
        <textarea ng-model="curDatasource.config.${param.name}" class="form-control input-sm" placeholder="${param.placeholder}"/>
    <#elseif param.type == "password">
        <input ng-model="curDatasource.config.${param.name}" class="form-control input-sm" type="password" placeholder="${param.placeholder}"/>
    <#elseif param.type == "checkbox">
        <input ng-model="curDatasource.config.${param.name}" type="checkbox"/>
    <#elseif param.type == "select">
        <select class="form-control" ng-model="curDatasource.config.${param.name}">
            <#list param.options as option>
                <option>${option}</option>
            </#list>
        </select>
    </#if>
</div>
</#list>
