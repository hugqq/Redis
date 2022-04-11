package ${package.Entity};

<#if entityLombokModel>
import com.ocrud.entity.BaseEntity;
</#if>
import lombok.*;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
<#if superEntityClass??>
public class ${entity} extends ${superEntityClass} {
<#elseif activeRecord>
public class ${entity} extends Model<${entity}> {
<#else>
public class ${entity} implements Serializable {
</#if>

    private static final long serialVersionUID = 1L;
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
<#if field.keyFlag>
    <#assign keyPropertyName="${field.propertyName}"/>
</#if>

<#if field.comment!?length gt 0>
    /**
    * ${field.comment}
    */
</#if>
<#if field.keyFlag>
    <#-- 主键 -->
    <#if field.keyIdentityFlag>
        @TableId(value = "${field.name}", type = IdType.AUTO)
    <#elseif idType??>
        @TableId(value = "${field.name}", type = IdType.${idType})
    <#elseif field.convert>
        @TableId("${field.name}")
    </#if>
    <#-- 普通字段 -->
    <#elseif field.fill??>
    <#-- -----   存在字段填充设置   ----->
    <#if field.convert>
        @TableField(value = "${field.name}", fill = FieldFill.${field.fill})
    <#else>
        @TableField(fill = FieldFill.${field.fill})
    </#if>
    <#elseif field.convert>
        @TableField("${field.name}")
    </#if>
<#-- 乐观锁注解 -->
    <#if (versionFieldName!"") == field.name>
        @Version
    </#if>
<#-- 逻辑删除注解 -->
    <#if (logicDeleteFieldName!"") == field.name>
        @TableLogic
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->

<#if !entityLombokModel>
    <#list table.fields as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
        public ${field.propertyType} ${getprefix}${field.capitalName}() {
        return ${field.propertyName};
        }

        <#if entityBuilderModel>
            public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
        <#else>
            public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
        </#if>
        this.${field.propertyName} = ${field.propertyName};
        <#if entityBuilderModel>
            return this;
        </#if>
        }
    </#list>
</#if>
<#if entityColumnConstant>
    <#list table.fields as field>
        public static final String ${field.name?upper_case} = "${field.name}";
    </#list>
</#if>
}
