<#include "../include/imports.ftl">

<#-- @ftlvariable name="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu" -->
<#-- @ftlvariable name="secureMenu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu" -->
<#-- @ftlvariable id="editMode" type="java.lang.Boolean"-->
<#if menu??>
  <#if menu.siteMenuItems??>
    <ul class="nav nav-pills">
      <#list menu.siteMenuItems as item>
        <#if  item.selected || item.expanded>
          <li class="active"><a href="<@hst.link link=item.hstLink/>">${item.name?html}</a></li>
        <#else>
          <li><a href="<@hst.link link=item.hstLink/>">${item.name?html}</a></li>
        </#if>
      </#list>
      <#if player??>
          <#if secureMenu??>
            <#list secureMenu.siteMenuItems as item>
              <#if  item.selected || item.expanded>
                <li class="active"><a href="<@hst.link path="secure/"+player+"/"+item.name/>">${item.name?html}</a></li>
              <#else>
                <li><a href="<@hst.link path="secure/"+player+"/"+item.name/>">${item.name?html}</a></li>
              </#if>
            </#list>
          </#if>
      </#if>
    </ul>
  </#if>
  <@hst.cmseditmenu menu=menu/>
<#else>
  <#if editMode>
    <h5>[Menu Component]</h5>
    <sub>Click to edit Menu</sub>
  </#if>
</#if>