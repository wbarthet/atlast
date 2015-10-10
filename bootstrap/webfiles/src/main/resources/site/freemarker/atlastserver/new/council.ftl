<#include "../../include/imports.ftl">
<#-- @ftlvariable name="player" type="org.atlast.beans.Player" -->

<#-- @ftlvariable name="worldMarket" type="org.atlast.beans.Market" -->

<div class="section-headline">
  <div class="row">
    <div class="small-12 medium-12 large-12 large-centered columns">
      <h1>Council</h1>
      <h5>In the court of the Aqurids</h5>
    </div>
  </div>
</div>

<div class="section-features section-bg-color1">
  <div class="row">

    <div class="small-12 medium-4 large-6 columns">
    <a href="<@hst.link hippobean=player.library/>">
      <div class="feature-box">
        <a href="<@hst.link hippobean=player.library/>"><h4><img class="land-icon forest" src="<@hst.link path="binaries/content/gallery/atlastserver/icons/council/library.png"/>"/> Library</h4></a>
        <p>Focus: ${player.library.focus}</p>
        <p>
        <#list player.library.pops as pop>
          <img class="pop-icon" style="background-color: ${pop.identity.colour};" src="<@hst.link path="binaries/content/gallery/atlastserver/img/lower2.png"/>"/>
        </#list>
        </p>
      </div>
    </a>
    </div>


  </div>
</div>
