<#include "../../include/imports.ftl">
<#-- @ftlvariable name="player" type="org.atlast.beans.Player" -->

<div class="section-headline">
  <div class="row">
    <div class="small-12 medium-12 large-12 large-centered columns">
      <h1>${player.name} of the ${player.identity.name} </h1>
    </div>
  </div>
</div>

<@hst.link var='loginLink' path='/login/proxy'/>

<div class="section-bg-color2">
  <div class="row-centered">

  <#assign cash=player.cash/>
    <p>Cash: ${cash?string.currency}</p>

  </div>

<#list player.inbox.messages as message>
  <div class="row">
    <div class="small-6 medium-8 large-12 columns">
      <div class="feature-box">
        <h4>${message.title}</h4>
        <p>${message.text}</p>
      </div>
    </div>
  </div>
</#list>

</div>