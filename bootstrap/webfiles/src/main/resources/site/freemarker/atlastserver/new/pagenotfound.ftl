<#include "../../include/imports.ftl">

<@hst.setBundle basename="essentials.pagenotfound"/>

<div class="section-headline">
  <div class="row">
    <div class="small-12 medium-12 large-12 large-centered columns">
      <h1><@fmt.message key="pagenotfound.title" var="title"/>${title?html}</h1>
      <h2><@fmt.message key="pagenotfound.text"/> Go <a style="text-decoration: underline;" href="<@hst.link path="/"/>">home</a>.</h2>
    </div>
  </div>
</div>


