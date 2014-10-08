<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<meta charset="ISO-8859-1">
<title><spring:message code="title" /> | <spring:message code="heading" /></title>
<meta name="description" content="">
<meta name="author" content="">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<!-- BEGIN GLOBAL MANDATORY STYLES -->
<spring:url value="/resources/css/bootstrap.min.css" var="bootstrapCss" />
<link href="${bootstrapCss}" rel="stylesheet" type="text/css"/>
<spring:url value="/resources/css/font-awesome.min.css" var="fontawesomeCss" />
<link href="${fontawesomeCss}" rel="stylesheet" type="text/css"/>
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN THEME STYLES -->
<spring:url value="/resources/css/smartadmin-production.min.css" var="smartadminProdCss" />
<link href="${smartadminProdCss}" rel="stylesheet" type="text/css"/>
<spring:url value="/resources/css/smartadmin-skins.min.css" var="smartadminSkinCss" />
<link href="${smartadminSkinCss}" rel="stylesheet" type="text/css"/>
<spring:url value="/resources/css/layout.min.css" var="layoutCss" />
<link href="${layoutCss}" rel="stylesheet" type="text/css"/>
<!-- END THEME STYLES -->
<!-- FAVICONS -->
<spring:url value="/resources/img/favicon/alerta.ico" var="favicon" />
<link rel="shortcut icon" href="${favicon}" type="image/x-icon"/>
<!-- END FAVICONS -->
<!-- GOOGLE FONT -->
<spring:url value="/resources/css/googlefonts.css" var="googleFontsCss" />
<link href="${googleFontsCss}" rel="stylesheet" type="text/css"/>
<!-- END GOOGLE FONT -->