<#include "../../include/imports.ftl">
<#-- @ftlvariable name="stores" type="java.util.List<org.atlast.beans.Store>" -->

<@hst.link var='loginLink' path='/login/proxy'/>

<div class="login-form">
  <form class="form-horizontal" action="${loginLink}" method="post">
  <#if RequestParameters.login?exists && RequestParameters.login == "failed">
    <p class="error">Invalid username or password</p>
  </#if>
    <input type="hidden" name="destination"
    <#if Session['org.hippoecm.hst.security.servlet.destination']?exists>
           value="${Session['org.hippoecm.hst.security.servlet.destination']}"
    </#if>
      />
    <div class="control-group">
      <label class="control-label" for="username-field">Username</label>
      <div class="controls">
        <input id="username-field" type="text" name="username"
        <#if Session['org.hippoecm.hst.security.servlet.username']?exists>
               value="${Session['org.hippoecm.hst.security.servlet.username']}"
        </#if>
          />
      </div>
    </div>
    <div class="control-group">
      <label class="control-label" for="password-field">Password</label>
      <div class="controls">
        <input id="password-field" type="password" name="password"/>
      </div>
    </div>
    <div class="control-group">
      <div class="controls">
        <input id="login-button" type="submit" value="Login">
      </div>
    </div>
  </form>
</div>
