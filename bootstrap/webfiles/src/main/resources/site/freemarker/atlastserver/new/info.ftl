<#include "../../include/imports.ftl">
<#-- @ftlvariable name="document" type="org.atlast.beans.ContentDocument" -->
<#-- @ftlvariable name="menu" type="java.util.List<org.hippoecm.hst.content.beans.standard.HippoBean>" -->

<div class="section-headline">
  <div class="row">
    <div class="small-12 medium-12 large-12 large-centered columns">
      <h1>${document.title}</h1>
    </div>
  </div>
</div>

<div class="section-bg-color2">

  <div class="row first-row">
    <div class=" large-8 medium-push-4 medium-8 columns ">

      <article>
      <@hst.html hippohtml=document.content/>
      </article>

    </div>

    <div class="large-4 medium-4 medium-pull-8 columns">
      <aside class="blog-sidebar">

        <ul>
        <#list menu as item>
            <#if item.isSelf(document) || item.isSelf(document.parentBean)>
              <li><h5>${item.name?cap_first}</h5></li>
            <#else>
              <li><a href="<@hst.link hippobean = item/>">${item.name?cap_first}</a></li>
            </#if>

        </#list>


        </ul>
      <#if document.parentBean.isHippoFolderBean()>
          <#if document.parentBean.parentBean.name != "info">
            <p>
              <a href="<@hst.link hippobean = document.parentBean.parentBean/>">Back</a>
            </p>
          </#if>
      <#else>
        <p><a href="<@hst.link hippobean = document.parentBean/>">Back</a></p>
      </#if>

      </aside>
    </div>
  </div>

</div>