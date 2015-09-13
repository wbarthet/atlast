<#include "../../include/imports.ftl">

<div class="section-headline">
  <div class="row">
    <div class="small-12 medium-12 large-12 large-centered columns">
      <h1>Create an Account</h1>
    </div>
  </div>
</div>
\

<div class="section-bg-color2">
  <div class="row-centered">
    <form class="form-horizontal" action="<@hst.actionURL/>" method="post">
    <#if messages??>
      <#list messages as message>
        <p class="error">${message}</p>
      </#list>
    </#if>
      <p>User Name: <input class="select-long" type="text" name="userName" value=""></p>
      <p>Password: <input class="select-long" type="password" name="password" value=""></p>
      <p>Confirm: <input class="select-long" type="password" name="password2" value=""></p>
      <p>I am not a robot <input type="checkbox" name="amrobot" value="true"></p>
      <p>Actually, I am a robot <input type="checkbox" name="amnotrobot" value="true"></p>
      <p><input class="button round" id="login-button" type="submit" value="SIGNUP"></p>
    </form>

  </div>
</div>



