<!doctype html>
<#include "../include/imports.ftl">

<@hst.defineObjects/>

<html lang="en">
  <head>

    <title>ATLAST</title>

    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">

    <link href='http://fonts.googleapis.com/css?family=Roboto+Condensed:700italic,400,300,700' rel='stylesheet' type='text/css'>
    <!--[if lte IE 8]>
      <script src="<@hst.webfile path="/js/html5shiv.js"/>  </script><![endif]-->
    <script src="<@hst.webfile path="/js/jquery.min.js"/>"></script>
    <script src="<@hst.webfile path="/js/skel.min.js"/>"></script>
    <script src="<@hst.webfile path="/js/skel-panels.min.js"/>"></script>
    <script src="<@hst.webfile path="/js/init.js"/>"></script>


      <link rel="stylesheet" href="<@hst.webfile path="/css/skel-noscript.css"/>"/>
      <link rel="stylesheet" href="<@hst.webfile path="/css/style.css"/>"/>
      <link rel="stylesheet" media="(max-width: 480px)" href="<@hst.webfile path="/css/style-mobile.css"/>"/>
      <link rel="stylesheet" media="(min-width: 481px)" href="<@hst.webfile path="/css/style-desktop.css"/>"/>

    <!--[if lte IE 8]>
      <link rel="stylesheet" href="<@hst.webfile path="/css/ie/v8.css"/>   <![endif]-->
    <!--[if lte IE 9]>
      <link rel="stylesheet" href="<@hst.webfile path="/css/ie/v9.css"/>   <![endif]-->

  <#if hstRequest.requestContext.cmsRequest>
    <link rel="stylesheet" href="<@hst.webfile  path="/css/cms-request.css"/>" type="text/css"/>
  </#if>
  <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts" xhtml=true/>
  </head>
  <body>

  <@hst.include ref="body"/>

  <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
  </body>
</html>

