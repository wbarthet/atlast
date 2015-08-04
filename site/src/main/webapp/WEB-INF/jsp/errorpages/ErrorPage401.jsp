
<!doctype html>
<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>




<html lang="en">
  <head>
    <meta charset="utf-8"/>
    <hst:link path="/" var="loginLink"/>
    <meta http-equiv="refresh" content='1;${loginLink}'/>
    <title>401 error</title>
  </head>
  <body>
    <h1>Unauthorised!!!</h1>
    <p>You are not allowed to see this page! Redirecting you to login page...</p>
  </body>
</html>