<#include "../../include/imports.ftl">
<#-- @ftlvariable name="land" type="org.atlast.beans.Land" -->
<#-- @ftlvariable name="pool" type="java.util.List<org.atlast.beans.Pop>" -->
<#-- @ftlvariable name="worldMarket" type="org.atlast.beans.Market" -->
<#-- @ftlvariable name="player" type="org.atlast.beans.Player" -->

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
        <#if land.pops?has_content>

          <p>
            Skill level ${land.getSkillLevel(land.recipeDescriptor.skill)}% | Tech level: 43% | Land exhaustion: 67%
          </p>
          <p>
              <#assign pnl=0.00/>
              <#if land.recipeDescriptor.inputs?has_content>
                Input:
                  <#list land.recipeDescriptor.inputs as input>
                  <#assign inputCount = input.quantity * land.pops?size / land.recipeDescriptor.labour/>
                  ${inputCount} x ${input.resourceDescriptor.name}
                    <img class="resource-icon" src="<@hst.link hippobean=input.resourceDescriptor.icon.smallicon/>"/>
                      <#assign pnl=pnl-inputCount * worldMarket.getStore(input.resourceDescriptor.category?lower_case).getResourceLevel(input.resourceDescriptor.name?lower_case)?double/>
                  </#list>
                |</#if>
              <#assign pnl = pnl - (land.wages * land.pops?size)/>

            Labour: ${land.recipeDescriptor.labour} x
            <img style="width: 1.2em;" class="resource-icon" src="img/other/lower.png"/> |
              <#if land.recipeDescriptor.outputs?has_content>
                Output:
                  <#list land.recipeDescriptor.outputs as output>
                  ${output.resourceDescriptor.name} x ${land.outputs[output.resourceDescriptor.name]}
                  <#-- ${land.getOutputs().get(output.name)} x ${output.name}-->
                    <img class="resource-icon" src="<@hst.link hippobean=output.resourceDescriptor.icon.smallicon/>"/>
                      <#assign pnl=pnl + land.outputs[output.resourceDescriptor.name] * worldMarket.getStore(output.resourceDescriptor.category?lower_case).getResourceLevel(output.resourceDescriptor.name?lower_case) />
                  </#list>
              </#if>
            |
          ${pnl?string.currency}
          </p>
        </#if>
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
        <#if land.pops?has_content>
            <#list land.pops as pop>
              <div class="row">
                <div class="feature-box">
                  <form action="<@hst.actionURL/>" method="post">
                    <input type="hidden" value="${pop.uuid}" name="uuid">
                    <input type="hidden" value="fire" name="action">
                    <img align="left" style="margin-right: 1em; background-color: ${pop.identity.colour};" class="pop-icon" src="<@hst.link path="binaries/content/gallery/atlastserver/img/lower2.png"/>">
                  ${pop.name} | ${land.recipeDescriptor.skill?cap_first}: ${pop.getSkill(land.recipeDescriptor.skill)?floor}% | ${player.identity.name} ${pop.getIdentityLevel(player.identity.getUuid())?floor}%
                    <input align="right" class="button round button-small" type="submit" value=">">
                  </form>
                </div>
              </div>
            </#list>
        </#if>
    </div>

    <div class="small-12 medium-6 large-6 columns">
      <h3>Employment Pool <a href="?sort=skill">(Skill&downarrow;)</a> <a href="?sort=alignment">(Alignment&downarrow;)</a></h3>
        <#list pool as pop>
          <div class="row">
            <div class="feature-box">
              <form action="<@hst.actionURL/>" method="post">
                <input type="hidden" value="${pop.uuid}" name="uuid">
                <input type="hidden" value="hire" name="action">
                <input align="left" style="margin-left: 1em;" class="button round button-small" type="submit" value="<">
              ${pop.name} | ${land.recipeDescriptor.skill?cap_first}: ${pop.getSkill(land.recipeDescriptor.skill)?floor}% | ${player.identity.name} ${pop.getIdentityLevel(player.identity.getUuid())?floor}%
                <img align="right" style="background-color: ${pop.identity.colour};" class="pop-icon" src="<@hst.link path="binaries/content/gallery/atlastserver/img/lower2.png"/>">
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
    <h4><a href="../lands">Back to lands overview</a></h4>
  </div>
</div>
