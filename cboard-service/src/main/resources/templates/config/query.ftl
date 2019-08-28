<#list params as param>
<div class="form-group">
    <label class="col-sm-2 control-label">${param.label}</label>
    <div class="col-sm-10">
        <#if param.type == "input">
            <input ng-model="curWidget.query.$param.name" class="form-control" placeholder="$param.placeholder"/>
        <#elseif param.type == "textarea">
            <div ui-ace="queryAceOpt" ng-model="curWidget.query.${param.name}" class="form-control"></div>
        <#elseif param.type == "textarea2">
            <textarea ng-model="curWidget.query.${param.name}" class="form-control input-sm" placeholder="${param.placeholder}"></textarea>
        <#elseif param.type == "number">
            <input ng-model="curWidget.query.${param.name}" class="form-control" type="number" placeholder="${param.placeholder}"/>
        <#elseif param.type == "checkbox">
            <input ng-model="curWidget.query.${param.name}" type="checkbox"/>
        <#elseif param.type == "select">
            <select class="form-control" ng-model="curWidget.query.${param.name}">
                <#list param.options as option>
                    <option>${option}</option>
                </#list>
            </select>
        </#if>
    </div>
</div>
</#list>

