<#include "../../include/imports.ftl">

<#-- @ftlvariable name="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu" -->
<#-- @ftlvariable name="secureMenu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu" -->

<!-- Header -->
<div id="header">
  <div class="container">

    <!-- Logo -->
    <div id="logo">
      <h1><a href="#">ATLAST</a></h1>
    </div>

  <@hst.link var='loginLink' path='/login/proxy'/>

    <!-- Nav -->

  <@hst.link path="secure" var="secureURL"/>

    <nav id="nav">
      <ul>
      <#list menu.siteMenuItems as item>
          <#if  item.selected || item.expanded>
            <li class="active"><a href="<@hst.link link=item.hstLink/>">${item.name?html}</a></li>
          <#else>
            <li><a href="<@hst.link link=item.hstLink/>">${item.name?html}</a></li>
          </#if>
      </#list>



        <form action="${loginLink}" method="post">
          <input id="destination-input" type="hidden" name="destination" value=""/>
          <li>
            <input id="username-field" type="text" name="username"
            <#if Session['org.hippoecm.hst.security.servlet.username']?exists>
                   value="${Session['org.hippoecm.hst.security.servlet.username']}"
            </#if>
              />
          </li>
          <li>
            <input id="password-field" type="password" name="password"/>
          </li>
          <li>
            <input id="login-button" type="submit" value="Login">
          </li>
        </form>

      </ul>
    </nav>

  </div>
</div>


<script type="application/javascript">
  var submitButton = document.getElementById('login-button');
  var userNameField = document.getElementById('username-field');
  var passwordField = document.getElementById('password-field');

  submitButton.onmousedown = function(){

    document.getElementById('destination-input').setAttribute("value", "${secureURL}/"+userNameField.value);
  }
  userNameField.blur = function(){

    document.getElementById('destination-input').setAttribute("value", "${secureURL}/"+userNameField.value);
  }
  passwordField.blur = function(){

    document.getElementById('destination-input').setAttribute("value", "${secureURL}/"+userNameField.value);
  }
</script>
<!-- Header -->