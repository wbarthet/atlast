<#include "../../include/imports.ftl">
<#-- @ftlvariable name="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu" -->
<#-- @ftlvariable name="secureMenu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu" -->
<header>
  <div class="row">
    <div class="small-12 medium-12 large-12 columns">
      <div>
        <img class="logo" src="<@hst.link path="/binaries/content/gallery/atlastserver/img/tophead.png/tophead.png"/>"/>
      </div>
      <h2><a href="">ATALASTAR</a></h2>
      <!--
       <div class="logo">
         <a href="index.html"><img src="img/logo.png" alt="Willamette" /></a>
       </div>
       -->
    </div>
  </div>
</header>


<#if menu??>
<nav>
  <div class="row">
    <div class="small-12 medium-12 large-12 columns">
      <ul>

          <#if secureMenu??>
              <#list secureMenu.siteMenuItems as item>
                  <#if  item.selected || item.expanded>
                    <li class="current"><a href="<@hst.link path=item.getParameter("path")?replace("_player_", player)/>">${item.name?html}</a></li>
                  <#else>
                    <li><a href="<@hst.link path=item.getParameter("path")?replace("_player_", player)/>">${item.name?html}</a></li>
                  </#if>
              </#list>
          <#else>
              <#list menu.siteMenuItems as item>
                  <#if  item.selected || item.expanded>
                    <li class="current"><a href="<@hst.link link=item.hstLink/>">${item.name?html}</a></li>
                  <#else>
                    <li><a href="<@hst.link link=item.hstLink/>">${item.name?html}</a></li>
                  </#if>
              </#list>
          </#if>

      </ul>
    </div>
  </div>
</nav>


    <@hst.cmseditmenu menu=menu/>
<#-- @ftlvariable id="editMode" type="java.lang.Boolean"-->
<#elseif editMode>
<img src="<@hst.link path="/images/essentials/catalog-component-icons/menu.png" />"> Click to edit Menu
</#if>