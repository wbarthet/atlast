<#include "../../include/imports.ftl">
<#-- @ftlvariable name="land" type="org.atlast.beans.Land" -->

<div class="section-headline">
  <div class="row">
    <div class="small-12 medium-12 large-12 large-centered columns">
      <h1>
        <img class="land-detail-icon" src="<@hst.link hippobean=land.landDescriptor.icon.mediumicon/>"/>${land.name}
      <#if land.development>
        <img class="land-detail-icon" src="<@hst.link hippobean=land.developmentDescriptor.icon.mediumicon/>"/>
      <#else>
        <img class="land-detail-icon" src="<@hst.link hippobean=land.landDescriptor.icon.mediumicon/>"/>
      </#if>
      </h1>
      <h5>${land.landDescriptor.name}
      <#if land.development>
        , ${land.developmentDescriptor.name}
      </#if>
      </h5>
    </div>
  </div>
</div>

<div class="section-bg-color2">
  <div class="row">
    <h3>Development</h3>
  <#if land.development>

    <form method="post" action="<@hst.actionURL/>">
      <p>
      ${land.developmentDescriptor.name} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="hidden" name="uuid" value="${land.uuid}">
        <input type="hidden" name="action" value="destroy">
        <input class="button round button-small" type="submit" value="destroy">
      </p>
    </form>

    <form method="post" action="<@hst.actionURL/>">
      <p>
        <select class="select-long" name="recipe">
            <#if land.recipeDescriptor??>
              <option value="none" selected>Do nothing</option>
            <#else>
              <option value="none">Do nothing</option>
            </#if>

            <#list land.developmentDescriptor.allowedRecipes as recipe>
                <#if land.recipeDescriptor?? && land.recipeDescriptor.uuid == recipe.uuid>
                  <option value="${recipe.uuid}" selected>${recipe.name}</option>
                <#else >
                  <option value="${recipe.uuid}">${recipe.name}</option>
                </#if>
            </#list>
        </select>

        Wages: <input name="wages" class="input-number" type="number" step="0.01" value="${land.wages}">

        <input type="hidden" name="uuid" value="${land.uuid}">
        <input type="hidden" name="action" value="save">
        <input class="button round button-small" type="submit" value="save">
      </p>
      <p>
        Skill level 25% | Tech level: 43% | Land exhaustion: 67%
      </p>
      <p>
          <#if land.recipeDescriptor.inputs?has_content>
            Input: ${land.pops?size} x
              <#list land.recipeDescriptor.inputs as input>
              ${input.resourceDescriptor.name}
                <img class="resource-icon" src="<@hst.link hippobean=input.resourceDescriptor.icon.smallicon/>"/>
              </#list>
          </#if>
        | Labour: ${land.recipeDescriptor.labour} x
        <img style="width: 1.2em;" class="resource-icon" src="img/other/lower.png"/> |
          <#if land.recipeDescriptor.inputs?has_content>
            Output: ${land.pops?size} x
              <#list land.recipeDescriptor.outputs as output>
              ${output.resourceDescriptor.name}
                <img class="resource-icon" src="<@hst.link hippobean=output.resourceDescriptor.icon.smallicon/>"/>
              </#list>
          </#if>
      </p>
    </form>
  <#else>
    <form method="post" action="<@hst.actionURL/>">
      <p>
        <select name="development" class="select-long">
            <#list land.landDescriptor.allowedDevelopments as development>
              <option value="${development.uuid}">${development.name}</option>
            </#list>
        </select>
        <input type="hidden" name="uuid" value="${land.uuid}">
        <input type="hidden" name="action" value="build">
        <input class="button round button-small" type="submit" value="build">
      </p>
    </form>
  </#if>

  </div>
</div>

<#if land.development>
<div class="section-bg-color2">
  <div class="row first-row">
    <div class="small-12 medium-6 large-6 columns">
      <h3>Employees</h3>

    <#list land.pops as pop>
      <div class="row">
        <div class="feature-box">
          <form action="<@hst.actionURL/>" method="post">
            <input type="hidden" value="${pop.uuid}" name="uuid">
            <input type="hidden" value="fire" name="action">
            <img align="left" style="margin-right: 1em;" class="pop-icon" src="<@hst.link path="binaries/content/gallery/atlastserver/img/lower.png"/>">
            ${pop.name} | Lumberjack: 45%
            <input align="right" class="button round button-small" type="submit" value=">">
          </form>
        </div>
      </div>
    </#list>
    </div>

    <div class="small-12 medium-6 large-6 columns">
      <h3>Employment Pool</h3>

      <#list pool as pop>
        <div class="row">
          <div class="feature-box">
            <form action="<@hst.actionURL/>" method="post">
              <input type="hidden" value="${pop.uuid}" name="uuid">
              <input type="hidden" value="hire" name="action">
              <input align="left" style="margin-left: 1em;" class="button round button-small" type="submit" value="<">
              ${pop.name} | Lumberjack: 0%
              <img align="right" style="background-color: #66afe9;" class="pop-icon" src="<@hst.link path="binaries/content/gallery/atlastserver/img/lower.png"/>">
            </form>
          </div>
        </div>
      </#list>

    </div>
  </div>
</div>
</#if>

<div class="section-bg-color2">
  <div class="row-centered">
    <h4><a>Back to lands overview</a></h4>
  </div>
</div>
