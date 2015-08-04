<#include "../../include/imports.ftl">

<#-- @ftlvariable name="hstRequestContext" type="org.hippoecm.hst.core.request.HstRequestContext" -->
<#-- @ftlvariable name="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu" -->

<@hst.defineObjects/>

<!-- Header -->
<div id="header">
  <div class="container">

    <!-- Logo -->
    <div id="logo">
      <h1><a href="#">ATLAST</a></h1>
    </div>

  <@hst.link var='loginLink' path='/login/proxy'/>

    <!-- Nav -->

  <@hst.link path="secure" var="secureURL"/>

    <nav id="nav">
      <ul>
      <#--TODO: fx active class-->
      <#list menu.siteMenuItems as item>

        <#assign path = item.getParameter("path")?replace("_player_", player)/>

          <#if hstRequestContext.resolvedSiteMapItem.pathInfo?contains(path)>
            <li class="active"><a href="<@hst.link path=path/>">${item.name?html}</a></li>
          <#else>
            <li><a href="<@hst.link path=path/>">${item.name?html}</a>
            </li>
          </#if>
      </#list>
      </ul>
    </nav>

  </div>
</div>

<!-- Header -->