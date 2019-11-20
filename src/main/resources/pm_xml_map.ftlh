<#macro opt xpath><#if xpath?has_content>${xpath}</#if></#macro>

<#macro optnode node xpath><#if xpath?has_content>
<${node}>${xpath}</${node}>
</#if></#macro>

<#macro trimzeros str>${str?replace('^0+(?!$)', '', 'r')}</#macro>

<#macro date_yyyyMMdd_to_ddMMyyyy date>${date?date('yyyy-MM-dd')?string('dd.MM.yyyy')}</#macro>
<#macro date_dd_MMM_yyyy_to_yyyyMMdd date>${date?date('dd-MMM-yyyy')?string('yyyyMMdd')}</#macro>