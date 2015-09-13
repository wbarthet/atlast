<!doctype html>
<#include "../../include/imports.ftl">

<@hst.setBundle basename="essentials.global"/>
<html class="no-js" lang="en">
  <head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <title>ATALASTER</title>
    <meta name="description" content="Page description goes here">

    <!-- ******************************************************************************************
    Set the type and color theme here - the Pro version contains additional themes -->
    <link href="<@hst.webfile path="/css/style.css"/>" rel="stylesheet">
    <!--  ************************************************************************************* -->

    <link href="<@hst.webfile path="/css/font-awesome.min.css"/>" rel="stylesheet">
    <script src="<@hst.webfile path="/js/vendor/modernizr.js"/>"></script>
    <link rel="shortcut icon" href="<@hst.webfile path="/img/other/favicon.ico"/>" type="image/x-icon"/>
  </head>
  <body>

  <@hst.include ref="top"/>

  <@hst.include ref="body"/>

  <@hst.include ref="footer"/>

    <script src="<@hst.webfile path="/js/vendor/jquery.min.js"/>"></script>
    <script src="<@hst.webfile path="/js/foundation.min.js"/>"></script>
    <script src="<@hst.webfile path="/js/willamette.js"/>"></script>

  </body>
</html>