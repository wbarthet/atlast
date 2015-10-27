<#include "../../include/imports.ftl">
<#-- @ftlvariable name="worldMarket" type="org.atlast.beans.Market" -->
<#-- @ftlvariable name="library" type="org.atlast.beans.Library" -->
<#-- @ftlvariable name="player" type="org.atlast.beans.Player" -->

<div class="section-headline">
  <div class="row">
    <div class="small-12 medium-12 large-12 large-centered columns">
      <h1>
        <img class="land-detail-icon" src="<@hst.link path="/binaries/content/gallery/atlastserver/icons/council/library.png"/>"/>Library

        <img class="land-detail-icon" src="<@hst.link path="/binaries/content/gallery/atlastserver/icons/council/library.png"/>"/>

      </h1>
    </div>
  </div>
</div>

<div class="section-bg-color2">

<#list library.technology?chunk(3) as techrow>
  <div class="row">
      <#list techrow as tech>

        <div class="small-12 medium-4 large-4 columns">
          <div class="feature-box">
            <h4>
              <img class="land-icon forest" src="<@hst.link path="/binaries/content/gallery/atlastserver/icons/council/scroll.png"/>"/>
                <#if tech.innovation>
                  Innovation
                <#else>
                ${tech.recipe.name}
                </#if>
            </h4>

            <p>level: ${tech.level}</p>
            <p>progress: ${tech.progress?round}%</p>
          </div>
        </div>
      </#list>
  </div>
</#list>

  <div class="row">
    <h3>Research</h3>

    <form method="post" action="<@hst.actionURL/>">
      <p>
        <select class="select-long" name="focus">
        <#if "Innovation" == library.focus>
          <option value="ascending" selected>Innovation</option>
          <option value="descending">Improvement</option>
        <#else >
          <option value="descending" selected>Improvement</option>
          <option value="ascending">Innovation</option>
        </#if>
        </select>

        Wages: <input name="wages" class="input-number" type="number" step="0.01" value="${library.wages}">

        <input type="hidden" name="uuid" value="${library.uuid}">
        <input type="hidden" name="action" value="save">
        <input class="button round button-small" type="submit" value="save">
      </p>

      <p>
      <#assign pnl=0.00/>

      <#assign pnl = pnl - (library.wages * library.pops?size)/>

        Output:
        technology x ${library.output}

        <img class="resource-icon" src="<@hst.link path="binaries/content/gallery/atlastserver/icons/council/scroll.png"/>"/>
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
    <#if library.pops?has_content>
        <#list library.pops as pop>
          <div class="row">
            <div class="feature-box">
              <form action="<@hst.actionURL/>" method="post">
                <input type="hidden" value="${pop.uuid}" name="uuid">
                <input type="hidden" value="fire" name="action">
                <img align="left" style="margin-right: 1em; background-color: ${pop.identity.colour};" class="pop-icon" src="<@hst.link path="binaries/content/gallery/atlastserver/img/"+pop.popClass+"2.png"/>">
              ${pop.name} | Scientist: ${pop.getSkill('scientist')?floor}% | ${player.identity.name} ${pop.getIdentityLevel(player.identity.getUuid())?floor}%
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
          ${pop.name} | Scientist: ${pop.getSkill('scientist')?floor}% | ${player.identity.name} ${pop.getIdentityLevel(player.identity.getUuid())?floor}%
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
