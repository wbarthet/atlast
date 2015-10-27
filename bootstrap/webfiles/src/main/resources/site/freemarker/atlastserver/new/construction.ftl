<#include "../../include/imports.ftl">
<#-- @ftlvariable name="worldMarket" type="org.atlast.beans.Market" -->
<#-- @ftlvariable name="construction" type="org.atlast.beans.Construction" -->
<#-- @ftlvariable name="player" type="org.atlast.beans.Player" -->
<#-- @ftlvariable name="recipes" type="java.util.List<org.atlast.beans.descriptors.RecipeDescriptor>" -->

<div class="section-headline">
  <div class="row">
    <div class="small-12 medium-12 large-12 large-centered columns">
      <h1>
        <img class="land-detail-icon" src="<@hst.link path="/binaries/content/gallery/atlastserver/icons/council/construction.png"/>"/>Construction

        <img class="land-detail-icon" src="<@hst.link path="/binaries/content/gallery/atlastserver/icons/council/construction.png"/>"/>

      </h1>
    </div>
  </div>
</div>

<div class="section-bg-color2">

  <div class="row">
    <h3>Construction</h3>

    <form method="post" action="<@hst.actionURL/>">
      <p>
        <select class="select-long" name="recipe">
        <#if construction.recipeDescriptor??>
          <option value="none" selected>Do nothing</option>
        <#else>

        </#if>

        <#list recipes as recipe>
          <#if construction.recipeDescriptor.uuid == recipe.uuid>
            <option value="${recipe.uuid}" selected>${recipe.name}</option>
          <#else >
            <option value="${recipe.uuid}">${recipe.name}</option>
          </#if>
        </#list>
        </select>

        Wages: <input name="wages" class="input-number" type="number" step="0.01" value="${construction.wages}">

        <input type="hidden" name="uuid" value="${construction.uuid}">
        <input type="hidden" name="action" value="save">
        <input class="button round button-small" type="submit" value="save">
      </p>

    <#if construction.pops?has_content>
    <#assign popcount = construction.pops?size/>
    <#else>
      <#assign popcount = 0/>
    </#if>
      <p>
        Skill level ${construction.getSkillLevel(construction.recipeDescriptor.skill)}%
      </p>
      <p>
        <#assign pnl=0.00/>
        <#if construction.recipeDescriptor.inputs?has_content>
          Input:
          <#list construction.recipeDescriptor.inputs as input>
            <#assign inputCount = input.quantity + input.quantity * popcount / construction.recipeDescriptor.labour/>
          ${inputCount} x ${input.resourceDescriptor.name}
            <img class="resource-icon" src="<@hst.link hippobean=input.resourceDescriptor.icon.smallicon/>"/>
            <#assign pnl=pnl-inputCount * worldMarket.getStore(input.resourceDescriptor.category?lower_case).getResourceLevel(input.resourceDescriptor.name?lower_case)?double/>
          </#list>
          |</#if>
        <#assign pnl = pnl - (construction.wages * popcount)/>

        Labour: ${construction.recipeDescriptor.labour} x
        <img style="width: 1.2em;" class="resource-icon" src="<@hst.link path="/binaries/content/gallery/atlastserver/img/upper.png"/>"/> |
        <#if construction.recipeDescriptor.outputs?has_content>
          Output:
          <#list construction.recipeDescriptor.outputs as output>
          ${output.resourceDescriptor.name} x ${construction.outputs[output.resourceDescriptor.name]}
            <img class="resource-icon" src="<@hst.link hippobean=output.resourceDescriptor.icon.smallicon/>"/>
          </#list>
        </#if>
        |
      ${pnl?string.currency}
      </p>


    </form>

  </div>
</div>

<div class="section-bg-color2">
  <div class="row first-row">
    <div class="small-12 medium-6 large-6 columns">
      <h3>Employees</h3>
    <#if construction.pops?has_content>
        <#list construction.pops as pop>
          <div class="row">
            <div class="feature-box">
              <form action="<@hst.actionURL/>" method="post">
                <input type="hidden" value="${pop.uuid}" name="uuid">
                <input type="hidden" value="fire" name="action">
                <img align="left" style="margin-right: 1em; background-color: ${pop.identity.colour};" class="pop-icon" src="<@hst.link path="binaries/content/gallery/atlastserver/img/"+pop.popClass+"2.png"/>">
              ${pop.name} | Merchant: ${pop.getSkill('merchant')?floor}% | ${player.identity.name} ${pop.getIdentityLevel(player.identity.getUuid())?floor}%
                <input align="right" class="button round button-small" type="submit" value=">">
              </form>
            </div>
          </div>
        </#list>
    </#if>
    </div>

    <div class="small-12 medium-6 large-6 columns">
      <h3>Employment Pool <a href="?sort=skill">(Skill&downarrow;)</a>
        <a href="?sort=alignment">(Alignment&downarrow;)</a></h3>
    <#list pool as pop>
      <div class="row">
        <div class="feature-box">
          <form action="<@hst.actionURL/>" method="post">
            <input type="hidden" value="${pop.uuid}" name="uuid">
            <input type="hidden" value="hire" name="action">
            <input align="left" style="margin-left: 1em;" class="button round button-small" type="submit" value="<">
          ${pop.name} | Merchant: ${pop.getSkill('merchant')?floor}% | ${player.identity.name} ${pop.getIdentityLevel(player.identity.getUuid())?floor}%
            <img align="right" style="background-color: ${pop.identity.colour};" class="pop-icon" src="<@hst.link path="binaries/content/gallery/atlastserver/img/"+pop.popClass+"2.png"/>">
          </form>
        </div>
      </div>
    </#list>

    </div>
  </div>
</div>

<div class="section-bg-color2">
  <div class="row-centered">
    <h4><a href="../council">Back to council</a></h4>
  </div>
</div>
