<#include "../../include/imports.ftl">

<div class="section-headline">
  <div class="row">
    <div class="small-12 medium-12 large-12 large-centered columns">
      <h1>Login</h1>
    </div>
  </div>
</div>

<@hst.link var='loginLink' path='/login/proxy'/>
<@hst.link path="root" var="dashURL"/>

<div class="section-bg-color2">
  <div class="row-centered">
    <form class="form-horizontal" action="${loginLink}" method="post">
      <input id="destination-input" type="hidden" name="destination" value=""     />
      <p>Username: <input class="select-long" id="username-field" type="text" name="username"
      <#if Session['org.hippoecm.hst.security.servlet.username']?exists>
                          value="${Session['org.hippoecm.hst.security.servlet.username']}"
      </#if>></p>
      <p>Password: <input class="select-long" id="password-field" type="password" name="password"></p>
      <p><input class="button round" id="login-button" type="submit" value="ENTER"></p>
    </form>

  </div>
</div>

<div class="section-bg-color2">
  <div class="row-centered">
    <h4><a href="<@hst.link path="signup"/>">Signup</a></h4>
  </div>
</div>


<script type="application/javascript">
  var submitButton = document.getElementById('login-button');
  var userNameField = document.getElementById('username-field');
  var passwordField = document.getElementById('password-field');
  var destinationField = document.getElementById('destination-input');

   userNameField.onblur = function(){
     destinationField.setAttribute("value", "${dashURL}"+userNameField.value);
  }

  submitButton.onclick = function() {
    destinationField.setAttribute("value", "${dashURL}"+userNameField.value);
  }

</script>
<!-- Header -->