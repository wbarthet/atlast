<#include "../include/imports.ftl">

<#-- @ftlvariable name="messages" type="java.util.List<java.lang.String>" -->

<div id="page">

  <!-- Main -->
  <div id="main" class="container">
    <div class="row">
      <div class="12u">
        <section>
          <header>
            <h2>Create an account</h2>
          </header>
        </section>
      </div>
      <p></p>
      <p></p>
    </div>
  </div>
  <!-- Main -->

  <div id="marketing" class="container">

    <div class="row">

      <div class="9u">
      <#if messages??>
          <#list messages as message>
            <p class="error">${message}</p>
          </#list>
      </#if>
<p></p>
        <form action="<@hst.actionURL/>" method="post">
          <p>User Name: <input type="text" name="userName" value=""></p>
          <p>Password: <input type="password" name="password" value=""></p>
          <p>Confirm: <input type="password" name="password2" value=""></p>
          <p>I am not a robot<input type="checkbox" name="amrobot" value="true"></p>
          <p>Actually, I am a robot<input type="checkbox" name="amnotrobot" value="true"></p>
          <p><input class="button" type="submit" value="Signup"></p>
        </form>

      </div>

    </div>

  </div>
</div>

