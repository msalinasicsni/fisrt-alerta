<%--
  Created by IntelliJ IDEA.
  User: souyen-ics
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <jsp:include page="../fragments/headTag.jsp"/>
    <spring:url value="/resources/img/plus.png" var="plus"/>
    <spring:url value="/resources/img/minus.png" var="minus"/>
    <style>
        .styleButton {
            float: right;
            height: 31px;
            margin: 10px 0px 0px 5px;
            padding: 0px 22px;
            font: 300 15px/29px "Open Sans", Helvetica, Arial, sans-serif;
            cursor: pointer;
        }
        .pagination-sm {
            width: 170px !important;
        }
        a.disabled {
            pointer-events: none;
            cursor: default;
        }
        .datepicker {
            z-index: 1065 !important;
        }
        td.details-control {
            background: url("${plus}") no-repeat center center;
            cursor: pointer;
        }
        tr.shown td.details-control {
            background: url("${minus}") no-repeat center center;
        }
    </style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="">
<!-- #HEADER -->
<jsp:include page="../fragments/bodyHeader.jsp" />
<!-- #NAVIGATION -->
<jsp:include page="../fragments/bodyNavigation.jsp" />
<!-- MAIN PANEL -->
<div id="main" data-role="main">
<!-- RIBBON -->
<div id="ribbon">
			<span class="ribbon-button-alignment">
				<span id="refresh" class="btn btn-ribbon" data-action="resetWidgets" data-placement="bottom" data-original-title="<i class='text-warning fa fa-warning'></i> <spring:message code="msg.reset" />" data-html="true">
					<i class="fa fa-refresh"></i>
				</span>
			</span>
    <!-- breadcrumb -->
    <ol class="breadcrumb">
        <li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a> <i class="fa fa-angle-right"></i> <a href="<spring:url value="/rotavirus/create" htmlEscape="true "/>"><spring:message code="menu.rotavirus" /></a></li>
    </ol>
    <!-- end breadcrumb -->
    <jsp:include page="../fragments/layoutOptions.jsp" />
</div>
<!-- END RIBBON -->
<!-- MAIN CONTENT -->
<div id="content">
<!-- row -->
<div class="row">
    <!-- col -->
    <div class="col col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <h2 class="page-title txt-color-blueDark">
            <!-- PAGE HEADER -->
            <i class="fa-fw fa fa-stethoscope"></i>
            <spring:message code="lbl.form.rotavirus"/>
						<span> <i class="fa fa-angle-right"></i>
							<spring:message code="lbl.register"/>
						</span>
        </h2>
    </div>
    <!-- end col -->
</div>
<!-- end row -->
<!--
    The ID "widget-grid" will start to initialize all widgets below
    You do not need to use widgets if you dont want to. Simply remove
    the <section></section> and you can use wells or panels instead
    -->
<!-- widget grid -->
<section id="widget-grid" class="">
<!-- row -->
<div class="row">
<!-- NEW WIDGET START -->
<article class="col col-xs-12 col-sm-12 col-md-12 col-lg-12">
<!-- Widget ID (each widget will need unique ID)-->
<div class="jarviswidget jarviswidget-color-darken" id="wid-id-0">
<!-- widget options:
    usage: <div class="jarviswidget" id="wid-id-0" data-widget-editbutton="false">
    data-widget-colorbutton="false"
    data-widget-editbutton="false"
    data-widget-togglebutton="false"
    data-widget-deletebutton="false"
    data-widget-fullscreenbutton="false"
    data-widget-custombutton="false"
    data-widget-collapsed="true"
    data-widget-sortable="false"
-->
<header>
    <span class="widget-icon"> <i class="fa fa-stethoscope"></i> </span>
    <h2><spring:message code="lbl.rotavirus"/></h2>
</header>
<!-- widget div-->
<div>
<!-- widget edit box -->
<div class="jarviswidget-editbox">
    <!-- This area used as dropdown edit box -->
    <input class="form-control" type="text">
</div>
<!-- end widget edit box -->
<!-- widget content -->
<div class="widget-body fuelux">
<div id="wizard" class="wizard">
    <ul class="steps">
        <li class="active" data-target="#step1">
            <span class="badge badge-info">1</span><spring:message code="lbl.step1.short"/><span class="chevron"></span>
        </li>
        <li data-target="#step2">
            <span class="badge">2</span> <spring:message code="lbl.step2.short"/> <span class="chevron"></span>
        </li>
        <li data-target="#step3">
            <span class="badge">3 </span> <spring:message code="lbl.step3.short"/> <span class="chevron"></span>
        </li>
        <li data-target="#step4">
            <span class="badge">4</span> <spring:message code="lbl.step4.short.rota"/> <span class="chevron"></span>
        </li>
        <li id="liStep5" data-target="#step5">
            <span class="badge">5</span> <spring:message code="lbl.step5.short.rota"/> <span class="chevron"></span>
        </li>
        <li data-target="#step6">
            <span class="badge">6</span> <spring:message code="lbl.step6.short.rota"/> <span class="chevron"></span>
        </li>
        <li data-target="#step7">
            <span class="badge">7</span> <spring:message code="lbl.step7.short.rota"/> <span class="chevron"></span>
        </li>
        <li data-target="#step8">
            <span class="badge">8</span> <spring:message code="lbl.step8.short.rota"/> <span class="chevron"></span>
        </li>
    </ul>
    <div class="actions">
        <button type="button" class="btn btn-sm btn-primary btn-prev">
            <i class="fa fa-arrow-left"></i><spring:message code="lbl.previous"/>
        </button>
        <button type="button" class="btn btn-sm btn-success btn-next"
                data-last="<spring:message code="lbl.finalize" />">
            <spring:message code="lbl.next"/><i class="fa fa-arrow-right"></i>
        </button>
    </div>
</div>
<div class="step-content">
<form id="wizard-1" class="smart-form">
<h2 class="row-seperator-header"><i class="fa fa-male "></i> <spring:message
        code="lbl.patient"/> ${fichaRotavirus.daNotificacion.persona.primerNombre} ${fichaRotavirus.daNotificacion.persona.primerApellido}
</h2>

<div class="step-pane active" id="step1">
    <h3 class="">
        <spring:message code="lbl.general.data"/>
    </h3>

    <fieldset>
        <input value="${fichaRotavirus.daNotificacion.persona.personaId}"
               type="hidden" name="personaId" id="personaId" />
        <input type="hidden" id="idNotificacion" name="idNotificacion"
               value="${fichaRotavirus.daNotificacion.idNotificacion}" />

        <div class="row">


            <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.silais"/>

                </label>

                <div class="input-group">
                    <spring:message code="msg.select.silais" var="selectSilais"/>
                    <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                    <select data-placeholder="${selectSilais}" name="codSilaisAtencion" id="codSilaisAtencion" class="select2">
                        <option value=""></option>
                        <c:forEach items="${entidades}" var="entidad">
                            <c:choose>
                                <c:when test="${entidad.codigo eq fichaRotavirus.daNotificacion.codSilaisAtencion.codigo}">
                                    <option selected value="${entidad.codigo}">${entidad.nombre}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${entidad.codigo}">${entidad.nombre}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </section>
            <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-4">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="sindfeb.muni"/>
                </label>
                <div class="input-group">
                    <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                    <select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.muni" />"
                            name="codMunicipio" id="codMunicipio" class="select2">
                        <option value=""></option>
                        <c:forEach items="${munic}" var="muni">
                            <c:choose>
                                <c:when test="${muni.codigoNacional eq fichaRotavirus.daNotificacion.codUnidadAtencion.municipio.codigoNacional}">
                                    <option selected value="${muni.codigoNacional}">${muni.nombre}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${muni.codigoNacional}">${muni.nombre}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>


            </section>

            <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-5">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.health.unit"/>
                </label>
                <div class="input-group">
                    <spring:message code="msg.select.hu" var="selectUni"/>
                    <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                    <select data-placeholder="${selectUni}" name="codUnidadAtencion" id="codUnidadAtencion" class="select2">
                        <option value=""></option>
                        <c:forEach items="${uni}" var="unid">
                            <c:choose>
                                <c:when test="${fn:contains(fichaRotavirus.daNotificacion.codUnidadAtencion.codigo, unid.codigo)}">
                                    <option selected value="${unid.codigo}">${unid.nombre}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${unid.codigo}">${unid.nombre}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </section>

        </div>

        <div class="row">
            <section class="col col-xs-12 col-sm-6 col-md-6 col-lg-3">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.file.number"/>
                </label>
                <div class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i
                        class="icon-append fa fa-sort-alpha-asc fa-fw"></i>

                    <input name="codExpediente" id="codExpediente" class="form-control" type="text"
                           placeholder="<spring:message code="lbl.file.number"/> " value="${fichaRotavirus.numExpediente}" />
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                            code="lbl.file.number"/></b>
                </div>
            </section>
            <!--<section class="col col-xs-12 col-sm-6 col-md-6 col-lg-3">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.code"/>
                </label>
                <div class="input">
                    <i class="icon-prepend fa fa-sort-alpha-asc fa-fw"></i> <i
                        class="icon-append fa fa-sort-numeric-asc fa-fw"></i>

                    <input name="codigo" id="codigo" class="form-control" type="text" value="${fichaRotavirus.codigo}"
                           placeholder="<spring:message code="lbl.code"/>"/>
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                            code="lbl.code"/></b>
                </div>
            </section>-->
            <section class="col col-xs-12 col-sm-6 col-md-6 col-lg-3">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.sala"/>
                </label>
                <div class="input-group">
                    <spring:message code="msg.select.sala" var="selectUni"/>
                    <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                    <select data-placeholder="${selectUni}" name="codSala" id="codSala" class="select2">
                        <option value=""></option>
                        <c:forEach items="${salas}" var="cresp">
                            <c:choose>
                                <c:when test="${cresp.codigo eq  fichaRotavirus.sala.codigo}">
                                    <option selected value="${cresp.codigo}">${cresp.valor}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${cresp.codigo}">${cresp.valor}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </section>
            <section class="col col-xs-12 col-sm-6 col-md-6 col-lg-3">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="sindfeb.hosp.date"/>
                </label>

                <div class="input">
                    <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                    <input class="form-control date-picker" data-date-end-date="0d"
                           type="text" name="fechaIngreso" id="fechaIngreso" pattern="dd/MM/yyyy"
                           value="<fmt:formatDate value="${fichaRotavirus.fechaIngreso}" pattern="dd/MM/yyyy" />"
                           placeholder="<spring:message code="sindfeb.hosp.date"/>"/>
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                            code="sindfeb.hosp.date"/></b>
                </div>
            </section>
            <section class="col col-xs-12 col-sm-6 col-md-6 col-lg-3">
                <label class="text-left txt-color-blue font-md hidden-xs">
                    <spring:message code="sindfeb.urgent"/>
                </label>
                <div class="input-group">
                    <span class="input-group-addon"> <i class="fa fa-exclamation-triangle fa-fw"></i></span>
                    <select name="urgente" id="urgente" data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.urgent" />"
                            class="select2">
                        <option value=""></option>
                        <c:forEach items="${catResp}" var="cresp">
                            <c:choose>
                                <c:when test="${cresp.codigo eq  fichaRotavirus.daNotificacion.urgente.codigo}">
                                    <option selected value="${cresp.codigo}">${cresp.valor}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${cresp.codigo}">${cresp.valor}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </section>
        </div>
    </fieldset>
</div>
<div class="step-pane" id="step2">
<h3>
    <spring:message code="lbl.patient.data"/>
</h3>
<fieldset>
<div class="row">
    <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="person.name1"/>
        </label>
        <label class="input"> <i
                class="icon-prepend fa fa-pencil fa-fw"></i> <input readonly="true"
                                                                    cssStyle="background-color: #f0fff0"
                                                                    type="text" name="primerNombre"
                                                                    value="${fichaRotavirus.daNotificacion.persona.primerNombre}"/>
        </label>
    </section>
    <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="person.name2"/>
        </label>
        <label class="input"> <i
                class="icon-prepend fa fa-pencil fa-fw"></i> <input readonly="true"
                                                                    cssStyle="background-color: #f0fff0"
                                                                    type="text" name="segundoNombre"
                                                                    value="${fichaRotavirus.daNotificacion.persona.segundoNombre}"/>
        </label>
    </section>
    <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="person.lastname1"/>
        </label>
        <label class="input"> <i
                class="icon-prepend fa fa-pencil fa-fw"></i> <input readonly="true"
                                                                    cssStyle="background-color: #f0fff0"
                                                                    type="text" name="primerApellido"
                                                                    value="${fichaRotavirus.daNotificacion.persona.primerApellido}"/>
        </label>
    </section>
    <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="person.lastname2"/>
        </label>
        <label class="input"> <i
                class="icon-prepend fa fa-pencil fa-fw"></i> <input readonly="true"
                                                                    cssStyle="background-color: #f0fff0"
                                                                    type="text" name="segundoApellido"
                                                                    value="${fichaRotavirus.daNotificacion.persona.segundoApellido}"/>
        </label>
    </section>
</div>

<div class="row">
    <section class="col col-xs-12 col-sm-6 col-md-6 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="person.sexo"/>
        </label>
        <div class="input-group">
            <c:choose>
                <c:when test="${fichaRotavirus.daNotificacion.persona.sexo.codigo eq 'SEXO|M'}">
                    <span class="input-group-addon"><i class="fa fa-male fa-fw"></i></span>
                </c:when>
                <c:when test="${fichaRotavirus.daNotificacion.persona.sexo.codigo eq 'SEXO|F'}">
                    <span class="input-group-addon"><i class="fa fa-female fa-fw"></i></span>
                </c:when>
                <c:otherwise>
                    <span class="input-group-addon"><i class="fa fa-question fa-fw"></i></span>
                </c:otherwise>
            </c:choose>
            <label class="input">
                <input class="form-control" type="text" name="sexo" id="sexo"
                       value="${fichaRotavirus.daNotificacion.persona.sexo}" readonly cssStyle="background-color: #f0fff0"
                       placeholder=" <spring:message code="person.sexo" />">
            </label>
            <span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
        </div>
    </section>
    <section class="col col-xs-12 col-sm-6 col-md-6 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="person.fecnac"/>
        </label>
        <div class="input-group">
            <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
            <label class="input">
                <input style="background-color: #f0fff0" class="form-control" readonly
                       type="text" name="fechaNacimiento" id="fechaNacimiento"
                       value="<fmt:formatDate value="${fichaRotavirus.daNotificacion.persona.fechaNacimiento}" pattern="dd/MM/yyyy" />"
                       placeholder=" <spring:message code="person.fecnac" />">
            </label>
            <span class="input-group-addon"><i class="fa fa-calendar fa-fw"></i></span>
        </div>
    </section>
    <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-4">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="lbl.age"/>
        </label>
        <label class="input"> <i
                class="icon-prepend fa fa-credit-card fa-fw"></i> <input readonly=""
                                                                         style="background-color: #f0fff0"
                                                                         type="text" name="edad"
                                                                         id="edad">
        </label>
    </section>
</div>

<div class="row">
    <section class="col col-xs-12 col-sm-12 col-md-6 col-lg-6">
        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
        <label class="text-left txt-color-blue font-md">
            <spring:message code="person.depa.resi"/>
        </label>

        <div class="input-group">
            <span class="input-group-addon"> <i class="fa fa-location-arrow fa-fw"></i></span>
            <select data-placeholder="<spring:message code="act.select" /> <spring:message code="person.depa.resi" />"
                    id="departamento" name="departamento"
                    class="select2">
                <option value=""></option>
                <c:forEach items="${departamentos}" var="depa">
                    <c:choose>
                        <c:when test="${depa.divisionpoliticaId eq fichaRotavirus.daNotificacion.persona.municipioResidencia.dependencia.divisionpoliticaId}">
                            <option selected value="${depa.codigoNacional}">${depa.nombre}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${depa.codigoNacional}">${depa.nombre}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>
        </div>
    </section>
    <section class="col col-xs-12 col-sm-12 col-md-6 col-lg-6">
        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
        <label class="text-left txt-color-blue font-md">
            <spring:message code="person.mun.res"/>
        </label>

        <div class="input-group">
            <span class="input-group-addon"> <i class="fa fa-location-arrow fa-fw"></i></span>
            <select data-placeholder="<spring:message code="act.select" /> <spring:message code="person.mun.res" />"
                    id="municipioResidencia" name="municipioResidencia"
                    class="select2">
                <option value=""></option>
                <c:forEach items="${municipiosResi}" var="muniResi">
                    <c:choose>
                        <c:when test="${muniResi.codigoNacional eq fichaRotavirus.daNotificacion.persona.municipioResidencia.codigoNacional}">
                            <option selected value="${muniResi.codigoNacional}">${muniResi.nombre}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${muniResi.codigoNacional}">${muniResi.nombre}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>
        </div>
    </section>
</div>

<div class="row">
    <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-6">
        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
        <label class="text-left txt-color-blue font-md">
            <spring:message code="person.com.res"/>
        </label>
        <div class="input-group">
            <span class="input-group-addon"> <i class="fa fa-location-arrow fa-fw"></i></span>
            <select data-placeholder="<spring:message code="act.select" /> <spring:message code="person.com.res" />"
                    id="comunidadResidencia" name="comunidadResidencia" class="select2">
                <option value=""></option>
                <c:forEach items="${comunidades}" var="comu">
                    <c:choose>
                        <c:when test="${comu.codigo eq fichaRotavirus.daNotificacion.persona.comunidadResidencia.codigo}">
                            <option selected value="${comu.codigo}">${comu.nombre}-${comu.sector.unidad.nombre}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${comu.codigo}">${comu.nombre}-${comu.sector.unidad.nombre}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>
        </div>
    </section>
    <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-6">
        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
        <label class="text-left txt-color-blue font-md">
            <spring:message code="person.direccion"/>
        </label>

        <div class="input">

            <i class="icon-prepend fa fa-map-marker fa-fw"></i> <i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
            <input class="form-control" type="text" name="direccionResidencia" id="direccionResidencia"
                   value="${fichaRotavirus.daNotificacion.persona.direccionResidencia}"
                   placeholder=" <spring:message code="person.direccion" />">
            <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                    code="person.direccion"/></b>
        </div>
    </section>
</div>
<div class="row">
    <section class="col col-xs-12 col-sm-12 col-md-8 col-lg-6">
        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
        <label class="text-left txt-color-blue font-md">
            <spring:message code="lbl.mother.father"/>
        </label>
        <label class="input"> <i
                class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
            <input type="text" id="nombreMadreTutor" name="nombreMadreTutor" value="${fichaRotavirus.nombreTutorAcompana}"
                   placeholder="<spring:message code="lbl.mother.father"/>"  />
            <b class="tooltip tooltip-top-right"> <i
                    class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.mother"/>
            </b>
        </label>
    </section>
    <section class="col col-xs-12 col-sm-6 col-md-4 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="person.telefono"/>
        </label>
        <div>
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i
                    class="icon-append fa fa-sort-numeric-asc"></i>
                <input type="text" name="telefonoTutor" value="${fichaRotavirus.telefonoTutor}"
                       id="telefonoTutor" class="telefono" placeholder="<spring:message code="person.telefono"/>">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                        code="msg.enter.tutor.telefono"/> </b>
            </label>
        </div>
    </section>
</div>
<div class="row">
    <section class="col col-xs-12 col-sm-6 col-md-4 col-lg-3">
        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
        <label class="text-left txt-color-blue font-md">
            <spring:message code="lbl.child.care"/>
        </label>
        <div class="inline-group">
            <c:choose>
                <c:when test="${not empty fichaRotavirus.enGuarderia}">
                    <c:choose>
                        <c:when test="${fichaRotavirus.enGuarderia}">
                            <label class="radio">
                                <input type="radio" name="rbtnCare" id="rbtnCareY" value="true" checked="checked">
                                <i></i><spring:message code="lbl.yes"/></label>
                            <label class="radio">
                                <input type="radio" name="rbtnCare" value="false" id="rbtnCareN">
                                <i></i><spring:message code="lbl.no"/></label>
                        </c:when>
                        <c:otherwise>
                            <label class="radio">
                                <input type="radio" name="rbtnCare" value="true" id="rbtnCareY">
                                <i></i><spring:message code="lbl.yes"/></label>
                            <label class="radio">
                                <input type="radio" name="rbtnCare" value="false" checked="checked" id="rbtnCareN">
                                <i></i><spring:message code="lbl.no"/></label>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <label class="radio">
                        <input type="radio" name="rbtnCare" value="true" id="rbtnCareY">
                        <i></i><spring:message code="lbl.yes"/></label>
                    <label class="radio">
                        <input type="radio" name="rbtnCare" value="false" id="rbtnCareN">
                        <i></i><spring:message code="lbl.no"/></label>
                </c:otherwise>
            </c:choose>
        </div>
        <div hidden="hidden" id="dErrorCare" class="errorDiv txt-color-red"><spring:message code="lbl.required.field"/></div>
    </section>
    <section id="dCareName" class="col col-xs-12 col-sm-12 col-md-8 col-lg-6">
        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
        <label class="text-left txt-color-blue font-md">
            <spring:message code="lbl.child.care.name"/>
        </label>
        <label class="input">
            <i class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
            <input name="nombreGuarderia" id="nombreGuarderia" placeholder="<spring:message code="lbl.child.care.name"/>"
                   type="text" value="${fichaRotavirus.nombreGuarderia}" />
            <b class="tooltip tooltip-top-right"> <i
                    class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.care.name"/>
            </b>
        </label>
    </section>
</div>
</fieldset>

</div>
<div class="step-pane" id="step3">
<h3>
    <spring:message code="lbl.clinical.data"/>
</h3>
<fieldset>
    <div class="row">
        <section class="col col-xs-8 col-sm-6 col-md-6 col-lg-5">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.diarrea.start.date"/>
            </label>
            <div class="input">
                <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                <input type="text" name="fechaInicioDiarrea" data-date-end-date="0d"
                       id="fechaInicioDiarrea"
                       class="form-control date-picker" value="<fmt:formatDate value="${fichaRotavirus.fechaInicioDiarrea}" pattern="dd/MM/yyyy" />"
                       data-dateformat="dd/mm/yy" placeholder="<spring:message code="lbl.diarrea.start.date"/>"/>
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                        code="msg.enter.diarrea.start.date"/></b>
            </div>
        </section>
        <section class="col col-xs-8 col-sm-6 col-md-6 col-lg-5">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.no.evacuaciones.24.hrs"/>
            </label>
            <div>
                <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i
                        class="icon-append fa fa-sort-numeric-asc"></i>
                    <input type="text" name="noevacuaciones" value="${fichaRotavirus.noEvacuaciones24Hrs}"
                           id="noevacuaciones" placeholder="<spring:message code="lbl.no.evacuaciones.24.hrs"/>">
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                            code="msg.enter.diarrea.days"/> </b>
                </label>
            </div>
        </section>
    </div>
    <!--<div class="row">
        <section class="col col-xs-3 col-sm-3 col-md-3 col-lg-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.month"/>
            </label>
            <div class="">
                <label class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i
                        class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                    <input type="text" id="mesEpi" name="mesEpi" readonly
                           placeholder="<spring:message code="lbl.month"/>" class="form-control">
                </label>
            </div>
        </section>
        <section class="col col-xs-3 col-sm-3 col-md-3 col-lg-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.year"/>
            </label>
            <div class="">
                <label class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i
                        class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                    <input type="text" id="anioEpi" name="anioEpi" readonly
                           placeholder="<spring:message code="lbl.year"/>" class="form-control">
                </label>

            </div>
        </section>
        <section class="col col-xs-6 col-sm-3 col-md-3 col-lg-4">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.ew"/>
            </label>
            <div class="">
                <label class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i
                        class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                    <input type="text" id="semanaEpi" name="semanaEpi" readonly
                           placeholder="<spring:message code="lbl.ew"/>" class="form-control">
                </label>
            </div>
        </section>
    </div>-->
</fieldset>
<fieldset>
    <div class="row">
        <section class="col col-xs-6 col-sm-6 col-md-6 col-lg-3">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.fiebre"/>
            </label>
            <div class="input-group">
                <span class="input-group-addon"> <i class="fa fa-list fa-fw"></i></span>
                <select name="fiebre" id="fiebre" data-placeholder="<spring:message code="msg.enter.fiebre" />"
                        class="select2">
                    <option value=""></option>
                    <c:forEach items="${catResp}" var="cresp">
                        <c:choose>
                            <c:when test="${cresp.codigo eq  fichaRotavirus.fiebre.codigo}">
                                <option selected value="${cresp.codigo}">${cresp.valor}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${cresp.codigo}">${cresp.valor}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
        </section>
        <section class="col col-xs-6 col-sm-6 col-md-6 col-lg-3">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.vomito"/>
            </label>
            <div class="input-group">
                <span class="input-group-addon"> <i class="fa fa-list fa-fw"></i></span>
                <select name="vomito" id="vomito" data-placeholder="<spring:message code="msg.enter.vomito" />"
                        class="select2">
                    <option value=""></option>
                    <c:forEach items="${catResp}" var="cresp">
                        <c:choose>
                            <c:when test="${cresp.codigo eq  fichaRotavirus.vomito.codigo}">
                                <option selected value="${cresp.codigo}">${cresp.valor}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${cresp.codigo}">${cresp.valor}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
        </section>
        <section id="dNoVomito" class="col col-xs-6 col-sm-6 col-md-6 col-lg-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.no.vomito.24.hrs"/>
            </label>
            <div>
                <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i
                        class="icon-append fa fa-sort-numeric-asc"></i>
                    <input type="text" class="entero" name="novomito" value="${fichaRotavirus.noVomito24Hrs}"
                           id="novomito" placeholder="<spring:message code="lbl.no.vomito.24.hrs"/>">
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                            code="msg.enter.vomito.days"/> </b>
                </label>
            </div>
        </section>
        <section id="dFechaVomito" class="col col-xs-6 col-sm-6 col-md-6 col-lg-3">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <spring:message var="symptomsDate" code="lbl.vomito.start.date"/>
            <label class="text-left txt-color-blue font-md">
                ${symptomsDate}
            </label>
            <div class="input">
                <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                <input type="text" name="fechaInicioVomito"  id="fechaInicioVomito" data-date-end-date="0d"
                       class="form-control date-picker" value="<fmt:formatDate value="${fichaRotavirus.fechaInicioVomito}" pattern="dd/MM/yyyy" />"
                       data-dateformat="dd/mm/yy" placeholder="${symptomsDate}" />
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                        code="msg.enter.vomito.start.date"/></b>
            </div>
        </section>
    </div>
    <div class="row">
        <section class="col col-xs-6 col-sm-6 col-md-6 col-lg-5">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.heces"/>
            </label>
            <div class="input-group">
                <span class="input-group-addon"> <i class="fa fa-list fa-fw"></i></span>
                <select name="heces" id="heces" class="select2" data-placeholder="<spring:message code="msg.enter.heces" />">
                    <option value=""></option>
                    <c:forEach items="${caracteristaHeceses}" var="cresp">
                        <c:choose>
                            <c:when test="${cresp.codigo eq  fichaRotavirus.caracteristaHeces.codigo}">
                                <option selected value="${cresp.codigo}">${cresp.valor}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${cresp.codigo}">${cresp.valor}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
        </section>
        <section id="dOtraCaracHeces" class="col col-xs-12 col-sm-12 col-md-8 col-lg-6">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.otra.heces"/>
            </label>
            <label class="input">
                <i class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                <input name="otraCaracHeces" id="otraCaracHeces" placeholder="<spring:message code="lbl.otra.heces"/>"
                       type="text" value="${fichaRotavirus.otraCaracteristicaHeces}" />
                <b class="tooltip tooltip-top-right"> <i
                        class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.otra.heces"/>
                </b>
            </label>
        </section>
    </div>
    <div class="row">
        <section class="col col-xs-6 col-sm-6 col-md-6 col-lg-5">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.deshidratacion"/>
            </label>
            <div class="input-group">
                <span class="input-group-addon"> <i class="fa fa-list fa-fw"></i></span>
                <select name="deshidratacion" id="deshidratacion" class="select2"
                        data-placeholder="<spring:message code="msg.enter.deshidratacion" />">
                    <option value=""></option>
                    <c:forEach items="${gradoDeshidratacions}" var="cresp">
                        <c:choose>
                            <c:when test="${cresp.codigo eq  fichaRotavirus.gradoDeshidratacion.codigo}">
                                <option selected value="${cresp.codigo}">${cresp.valor}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${cresp.codigo}">${cresp.valor}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
        </section>
        <section class="col col-xs-6 col-sm-6 col-md-6 col-lg-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.days.hosp"/>
            </label>
            <div>
                <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i
                        class="icon-append fa fa-sort-numeric-asc"></i>
                    <input type="text" class="entero" name="diasHosp" value="${fichaRotavirus.diasHospitalizacion}"
                           id="diasHosp" placeholder="<spring:message code="lbl.days.hosp"/>">
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                            code="msg.enter.number.days"/> </b>
                </label>
            </div>
        </section>
        <section class="col col-xs-6 col-sm-6 col-md-6 col-lg-4">
            <spring:message var="symptomsDate" code="lbl.alta.date"/>
            <label class="text-left txt-color-blue font-md">
                ${symptomsDate}
            </label>
            <div class="input">
                <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                <input type="text" name="fechaAlta" data-date-end-date="0d"
                       id="fechaAlta"
                       class="form-control date-picker" value="<fmt:formatDate value="${fichaRotavirus.fechaAlta}" pattern="dd/MM/yyyy" />"
                       data-dateformat="dd/mm/yy" placeholder="${symptomsDate}" />
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                        code="msg.enter.alta.date"/></b>
            </div>
        </section>
    </div>
</fieldset>
</div>
<div class="step-pane" id="step4">
    <h3>
        <spring:message code="lbl.tratamiento"/>
    </h3>
    <fieldset>
        <div class="row">
            <section class="col col-xs-12 col-sm-8 col-md-8 col-lg-6">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.use.antibiotics.prev"/>
                </label>
                <div class="input-group">
                    <spring:message code="msg.select.use.antibiotics.prev" var="selectUse"/>
                    <span class="input-group-addon"> <i class="fa fa-list-ul fa-fw"></i></span>
                    <select name="antibioticoPrevio" data-placeholder="${selectUse}" class="select2" id="antibioticoPrevio">
                        <option value=""></option>
                        <c:forEach items="${catResp}" var="cresp">
                            <c:choose>
                                <c:when test="${cresp.codigo eq  fichaRotavirus.usoAntibioticoPrevio.codigo}">
                                    <option selected value="${cresp.codigo}">${cresp.valor}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${cresp.codigo}">${cresp.valor}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </section>
        </div>
        <div class="row">
            <section class="col col-xs-12 col-sm-12 col-md-10 col-lg-6">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <spring:message var="antName" code="lbl.plain"/>
                <label class="text-left txt-color-blue font-md">
                    ${antName}
                </label>
            </section>
            <section class="col col-xs-12 col-sm-12 col-md-10 col-lg-6">
                <div class="inline-group">
                            <c:choose>
                                <c:when test="${not empty fichaRotavirus.planB and fichaRotavirus.planB}">
                                    <label class="radio">
                                        <input type="radio" name="planinline" checked="checked" value="planB">
                                        <i></i><spring:message code="lbl.plain.b"/></label>
                                    <label class="radio">
                                        <input type="radio" name="planinline" value="planC">
                                        <i></i><spring:message code="lbl.plain.c"/></label>
                                </c:when>
                                <c:when test="${not empty fichaRotavirus.planC and fichaRotavirus.planC}">
                                    <label class="radio">
                                        <input type="radio" name="planinline" value="planB">
                                        <i></i><spring:message code="lbl.plain.b"/></label>
                                    <label class="radio">
                                        <input type="radio" name="planinline" checked="checked" value="planC">
                                        <i></i><spring:message code="lbl.plain.c"/></label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio">
                                        <input type="radio" name="planinline" value="planB">
                                        <i></i><spring:message code="lbl.plain.b"/></label>
                                    <label class="radio">
                                        <input type="radio" name="planinline" value="planC">
                                        <i></i><spring:message code="lbl.plain.c"/></label>
                                </c:otherwise>
                            </c:choose>
                </div>
                <div hidden="hidden" id="dErrorPlain" class="errorDiv txt-color-red"><spring:message code="lbl.required.field"/></div>
            </section>
        </div>
        <div class="row">
            <section class="col col-xs-12 col-sm-5 col-md-5 col-lg-5">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.use.antibiotics.hosp"/>
                </label>
                <div class="input-group">
                    <spring:message code="msg.select.use.antibiotics" var="selectUse"/>
                    <span class="input-group-addon"> <i class="fa fa-list-ul fa-fw"></i></span>
                    <select data-placeholder="${selectUse}" class="select2" id="antHosp" name="antHosp">
                        <option value=""></option>
                        <c:forEach items="${catResp}" var="cresp">
                            <c:choose>
                                <c:when test="${cresp.codigo eq  fichaRotavirus.antibioticoHospital.codigo}">
                                    <option selected value="${cresp.codigo}">${cresp.valor}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${cresp.codigo}">${cresp.valor}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </section>
            <section id="dAntName" class="col col-xs-12 col-sm-7 col-md-7 col-lg-7">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <spring:message var="antName" code="lbl.antibiotics.names"/>
                <label class="text-left txt-color-blue font-md">
                    ${antName}
                </label>
                <div class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                    <input type="text" placeholder="${antName}" name="nombreAntibiotico"
                           id="nombreAntibiotico" value="${fichaRotavirus.cualAntibiotico}"/>
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                            code="msg.enter.antibiotics.name"/>
                    </b>
                </div>
            </section>
        </div>
    </fieldset>
    <fieldset>
        <div class="row">
            <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-6">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.uci"/>
                </label>
                <br>
            </section>
            <section class="col col-xs-12 col-sm-10 col-md-5 col-lg-2">
                <div class="inline-group">
                    <c:choose>
                        <c:when test="${not empty fichaRotavirus.UCI}">
                            <c:choose>
                                <c:when test="${fichaRotavirus.UCI}">
                                    <label class="radio">
                                        <input type="radio" name="uciinline" value="true" checked id="rbtnUciY">
                                        <i></i><spring:message code="lbl.yes"/></label>
                                    <label class="radio">
                                        <input type="radio" name="uciinline" value="false" id="rbtnUciN">
                                        <i></i><spring:message code="lbl.no"/></label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio">
                                        <input type="radio" name="uciinline" value="true" id="rbtnUciY">
                                        <i></i><spring:message code="lbl.yes"/></label>
                                    <label class="radio">
                                        <input type="radio" name="uciinline" value="false" checked id="rbtnUciN">
                                        <i></i><spring:message code="lbl.no"/></label>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <label class="radio">
                                <input type="radio" name="uciinline" value="true" id="rbtnUciY">
                                <i></i><spring:message code="lbl.yes"/></label>
                            <label class="radio">
                                <input type="radio" name="uciinline" value="false" id="rbtnUciN">
                                <i></i><spring:message code="lbl.no"/></label>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div hidden="hidden" id="dErrorUCI" class="errorDiv txt-color-red"><spring:message code="lbl.required.field"/></div>
            </section>
        </div>

        <div class="row">
            <section id="dNoDaysUci" class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <spring:message var="numberDays" code="lbl.numbers.days"/>
                <label class="text-left txt-color-blue font-md">
                    ${numberDays}
                </label>
                <div class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i
                        class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                    <input class="entero" placeholder="${numberDays}" type="text" id="noDiasUCI"
                           name="noDiasUCI" value="${fichaRotavirus.diasUCI}"/>
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i>
                        <spring:message code="msg.enter.uci.number.days"/>
                    </b>
                </div>
            </section>
            <section id="dUciDiarrea" class="col col-xs-12 col-sm-8 col-md-6 col-lg-4">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.uci.diarrea"/>
                </label>
                <div class="inline-group">
                    <c:choose>
                        <c:when test="${not empty fichaRotavirus.altaUCIDiarrea}">
                            <c:choose>
                                <c:when test="${fichaRotavirus.altaUCIDiarrea}">
                                    <label class="radio">
                                        <input type="radio" name="uciDiarrea" value="true" checked id="rbtnUciDY">
                                        <i></i><spring:message code="lbl.yes"/></label>
                                    <label class="radio">
                                        <input type="radio" name="uciDiarrea" value="false" id="rbtnUciDN">
                                        <i></i><spring:message code="lbl.no"/></label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio">
                                        <input type="radio" name="uciDiarrea" value="true" id="rbtnUciDY">
                                        <i></i><spring:message code="lbl.yes"/></label>
                                    <label class="radio">
                                        <input type="radio" name="uciDiarrea" value="false" checked id="rbtnUciDN">
                                        <i></i><spring:message code="lbl.no"/></label>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <label class="radio">
                                <input type="radio" name="uciDiarrea" value="true" id="rbtnUciDY">
                                <i></i><spring:message code="lbl.yes"/></label>
                            <label class="radio">
                                <input type="radio" name="uciDiarrea" value="false" id="rbtnUciDN">
                                <i></i><spring:message code="lbl.no"/></label>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div hidden="hidden" id="dErrorAltaUCI" class="errorDiv txt-color-red"><spring:message code="lbl.required.field"/></div>
            </section>
            <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
                <spring:message var="egressDate" code="lbl.diarrea.end.date"/>
                <label class="text-left txt-color-blue font-md">
                    ${egressDate}
                </label>
                <div class="input">
                    <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                    <input placeholder="${egressDate}" type="text" name="fechaFinDiarrea"
                           id="fechaFinDiarrea" data-date-end-date="0d"
                           class="form-control date-picker" value="<fmt:formatDate value="${fichaRotavirus.fechaTerminoDiarrea}" pattern="dd/MM/yyyy" />"
                           data-dateformat="dd/mm/yy"  />
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i>
                        <spring:message code="msg.enter.diarrea.end.date"/></b>
                </div>
            </section>
            <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-2">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <spring:message var="ignorado" code="lbl.ignorado"/>
                <label class="text-left txt-color-blue font-md">
                    ${ignorado}
                </label>
                <div class="row">
                    <div class="col col-4">
                        <label class="checkbox">
                            <c:choose>
                                <c:when test="${fichaRotavirus.ignoradoFechaTD}">
                                    <input type="checkbox" name="checkboxignorado" id="checkboxignorado" checked>
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" name="checkboxignorado" id="checkboxignorado" >
                                </c:otherwise>
                            </c:choose>
                            <i></i>
                        </label>
                    </div>
                </div>
            </section>
        </div>
    </fieldset>
</div>
<div class="step-pane" id="step5">
    <h3>
        <spring:message code="lbl.vaccines.rota"/>
    </h3>
    <fieldset>
        <div class="row">
            <section class="col col-xs-12 col-sm-8 col-md-8 col-lg-4">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.vacunado"/>
                </label>
                <div class="input-group">
                    <spring:message code="msg.select.vacunado" var="selectVac"/>
                    <span class="input-group-addon"> <i class="fa fa-list-ul fa-fw"></i></span>
                    <select name="vacunado" class="select2" id="vacunado" data-placeholder="${selectVac}">
                        <option value=""></option>
                        <c:forEach items="${catResp}" var="cresp">
                            <c:choose>
                                <c:when test="${cresp.codigo eq  fichaRotavirus.vacunado.codigo}">
                                    <option selected value="${cresp.codigo}">${cresp.valor}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${cresp.codigo}">${cresp.valor}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </section>
            <section id="dRegistroV" class="col col-xs-12 col-sm-8 col-md-8 col-lg-4">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.registro.vacunado"/>
                </label>
                <div class="input-group">
                    <spring:message code="msg.select.registro.vac" var="selectRegV"/>
                    <span class="input-group-addon"> <i class="fa fa-list-ul fa-fw"></i></span>
                    <select name="registrovacunado" class="select2" id="registrovacunado" data-placeholder="${selectRegV}">
                        <option value=""></option>
                        <c:forEach items="${registroVacunas}" var="cresp">
                            <c:choose>
                                <c:when test="${cresp.codigo eq  fichaRotavirus.registroVacuna.codigo}">
                                    <option selected value="${cresp.codigo}">${cresp.valor}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${cresp.codigo}">${cresp.valor}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </section>
            <section id="dTipoVacuna" class="col col-xs-12 col-sm-8 col-md-8 col-lg-4">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.vaccine.type"/>
                </label>
                <div class="input-group">
                    <spring:message code="msg.select.tipo.vac" var="selectTipoV"/>
                    <span class="input-group-addon"> <i class="fa fa-list-ul fa-fw"></i></span>
                    <select name="tipoVacuna" class="select2" id="tipoVacuna" data-placeholder="${selectTipoV}">
                        <option value=""></option>
                        <c:forEach items="${tipoVacunaRotavirus}" var="cresp">
                            <c:choose>
                                <c:when test="${cresp.codigo eq  fichaRotavirus.tipoVacunaRotavirus.codigo}">
                                    <option selected value="${cresp.codigo}">${cresp.valor}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${cresp.codigo}">${cresp.valor}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </section>
        </div>
        <div class="row">
            <section class="col col-xs-12 col-sm-6 col-md-4 col-lg-4">
                <spring:message var="numberDays" code="lbl.d1"/>
                <label class="text-left txt-color-blue font-md">
                    ${numberDays}
                </label>
                <div class="row">
                    <div class="col col-4">
                        <label class="checkbox">
                            <c:choose>
                                <c:when test="${fichaRotavirus.dosi1}">
                                    <input type="checkbox" name="checkbox-d1" id="checkbox-d1" checked>
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" name="checkbox-d1" id="checkbox-d1" >
                                </c:otherwise>
                            </c:choose>
                            <i></i>
                        </label>
                    </div>
                </div>
            </section>
            <section id="dFechaD1" class="col col-xs-12 col-sm-8 col-md-6 col-lg-4">
                <spring:message var="egressDate" code="lbl.aplication.date"/>
                <label class="text-left txt-color-blue font-md">
                    ${egressDate}
                </label>
                <div class="input">
                    <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                    <input placeholder="${egressDate}" type="text" name="fechaD1"
                           id="fechaD1" data-date-end-date="0d"
                           class="form-control date-picker" value="<fmt:formatDate value="${fichaRotavirus.fechaAplicacionDosis1}" pattern="dd/MM/yyyy" />"
                           data-dateformat="dd/mm/yy"  />
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i>
                        <spring:message code="msg.enter.date.d1"/></b>
                </div>

            </section>
        </div>
        <div class="row">
            <section class="col col-xs-12 col-sm-6 col-md-4 col-lg-4">
                <spring:message var="numberDays" code="lbl.d2"/>
                <label class="text-left txt-color-blue font-md">
                    ${numberDays}
                </label>
                <div class="row">
                    <div class="col col-4">
                        <label class="checkbox">
                            <c:choose>
                                <c:when test="${fichaRotavirus.dosi2}">
                                    <input type="checkbox" name="checkbox-d2" id="checkbox-d2" checked>
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" name="checkbox-d2" id="checkbox-d2">
                                </c:otherwise>
                            </c:choose>
                            <i></i>
                        </label>
                    </div>
                </div>
            </section>
            <section id="dFechaD2" class="col col-xs-12 col-sm-8 col-md-6 col-lg-4">
                <spring:message var="egressDate" code="lbl.aplication.date"/>
                <label class="text-left txt-color-blue font-md">
                    ${egressDate}
                </label>
                <div class="input">
                    <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                    <input placeholder="${egressDate}" type="text" name="fechaD2"
                           id="fechaD2" data-date-end-date="0d"
                           class="form-control date-picker" value="<fmt:formatDate value="${fichaRotavirus.fechaAplicacionDosis2}" pattern="dd/MM/yyyy" />"
                           data-dateformat="dd/mm/yy" />
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i>
                        <spring:message code="msg.enter.date.d2"/></b>
                </div>
            </section>
        </div>
        <div class="row">
            <section class="col col-xs-12 col-sm-6 col-md-4 col-lg-4">
                <label class="text-left txt-color-blue font-md"><spring:message code="lbl.d3"/></label>
                <div class="row">
                    <div class="col col-4">
                        <label class="checkbox">
                            <c:choose>
                                <c:when test="${fichaRotavirus.dosi3}">
                                    <input type="checkbox" name="checkbox-d3" id="checkbox-d3" checked>
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" name="checkbox-d3" id="checkbox-d3">
                                </c:otherwise>
                            </c:choose>
                            <i></i>
                        </label>
                    </div>
                </div>
            </section>
            <section id="dFechaD3" class="col col-xs-12 col-sm-8 col-md-6 col-lg-4">
                <spring:message var="egressDate" code="lbl.aplication.date"/>
                <label class="text-left txt-color-blue font-md">
                    ${egressDate}
                </label>
                <div class="input">
                    <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                    <input placeholder="${egressDate}" type="text" name="fechaD3"
                           id="fechaD3" data-date-end-date="0d"
                           class="form-control date-picker" value="<fmt:formatDate value="${fichaRotavirus.fechaAplicacionDosis3}" pattern="dd/MM/yyyy" />"
                           data-dateformat="dd/mm/yy" />
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i>
                        <spring:message code="msg.enter.date.d3"/></b>
                </div>
            </section>
        </div>
    </fieldset>
</div>
<div class="step-pane" id="step6">
    <h3>
        <spring:message code="lbl.laboratory.data"/>
    </h3>
    <fieldset>
        <div class="row">
            <section class="col col-xs-6 col-sm-6 col-md-6 col-lg-4">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.muestraHeces"/>
                </label>
                <div class="input-group">
                    <span class="input-group-addon"> <i class="fa fa-list fa-fw"></i></span>
                    <select name="muestraHeces" id="muestraHeces" data-placeholder="<spring:message code="msg.select.muestraHeces" />"
                            class="select2">
                        <option value=""></option>
                        <c:forEach items="${catResp}" var="cresp">
                            <c:choose>
                                <c:when test="${cresp.codigo eq  fichaRotavirus.tomoMuestraHeces.codigo}">
                                    <option selected value="${cresp.codigo}">${cresp.valor}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${cresp.codigo}">${cresp.valor}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </section>
        </div>
    </fieldset>
    <fieldset>
        <div class="table-responsive">
            <table class="table table-striped table-hover table-bordered" id="lista_resultados">
                <thead>
                <tr>
                    <th><spring:message code="lbl.request.type"/></th>
                    <th><spring:message code="lbl.desc.request"/></th>
                    <th><spring:message code="lbl.send.request.date"/></th>
                    <th><spring:message code="lbl.result.date.approve"/></th>
                    <th><spring:message code="lbl.unique.code.mx"/></th>
                    <th><spring:message code="lbl.result.mx.type"/></th>
                    <th><spring:message code="lbl.final.result"/></th>
                </tr>
                </thead>

            </table>
        </div>
    </fieldset>

</div>
<div class="step-pane" id="step7">
    <h3>
        <spring:message code="lbl.final.case.classification"/>
    </h3>

    <fieldset>
        <div class="row">
            <section class="col col-xs-12 col-sm-12 col-md-10 col-lg-6">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.final.case.classification"/>
                </label>
                <div class="input-group">
                    <spring:message code="msg.select.classification" var="selectCF"/>
                    <span class="input-group-addon"> <i class="fa fa-stethoscope"></i></span>
                    <select name="codClasFCaso" id="codClasFCaso" class="select2" data-placeholder="${selectCF}">
                        <option value=""></option>
                        <c:forEach items="${catClasif}" var="clasF">
                            <c:choose>
                                <c:when test="${clasF.codigo eq  fichaRotavirus.clasificacionFinal.codigo}">
                                    <option selected value="${clasF.codigo}">${clasF.valor}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${clasF.codigo}">${clasF.valor}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </section>
            <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-6">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.egress.condition"/>
                </label>
                <div class="input-group">
                    <spring:message code="msg.select.egress.condition.rt" var="selectCondition"/>
                    <span class="input-group-addon"> <i class="fa fa-list-ul fa-fw"></i></span>
                    <select name="codCondEgreso" class="select2" id="codCondEgreso" data-placeholder="${selectCondition}">
                        <option value=""></option>
                        <c:forEach items="${condicionEgresos}" var="cresp">
                            <c:choose>
                                <c:when test="${cresp.codigo eq  fichaRotavirus.condicionEgreso.codigo}">
                                    <option selected value="${cresp.codigo}">${cresp.valor}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${cresp.codigo}">${cresp.valor}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </section>
        </div>
    </fieldset>

</div>
<div class="step-pane" id="step8">
    <h3>
        <spring:message code="lbl.responsable.informacion"/>
    </h3>
    <fieldset>
        <div class="row">
            <section class="col col-xs-12 col-sm-6 col-md-6 col-lg-6">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <spring:message var="antName" code="sindfeb.llenoficha"/>
                <label class="text-left txt-color-blue font-md">
                    ${antName}
                </label>
                <div class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                    <input type="text" placeholder="${antName}" name="nombreLlenoFicha"
                           id="nombreLlenoFicha" value="${fichaRotavirus.nombreLlenaFicha}"/>
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                            code="sindfeb.llenoficha"/>
                    </b>
                </div>
            </section>
            <section class="col col-xs-12 col-sm-6 col-md-6 col-lg-6">
                <spring:message var="antName" code="lbl.nombre.tomo.mx"/>
                <label class="text-left txt-color-blue font-md">
                    ${antName}
                </label>
                <div class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                    <input type="text" placeholder="${antName}" name="nombreTomoMx"
                           id="nombreTomoMx" value="${fichaRotavirus.nombreTomoMx}"/>
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                            code="lbl.nombre.tomo.mx"/>
                    </b>
                </div>
            </section>
            <section class="col col-xs-12 col-sm-6 col-md-6 col-lg-6">
                <spring:message var="antName" code="lbl.epidemiologist"/>
                <label class="text-left txt-color-blue font-md">
                    ${antName}
                </label>
                <div class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                    <input type="text" placeholder="${antName}" name="epidemiologo"
                           id="epidemiologo" value="${fichaRotavirus.epidemiologo}"/>
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                            code="lbl.epidemiologist"/>
                    </b>
                </div>
            </section>
        </div>
    </fieldset>
</div>
</form>
</div>

</div>
<!-- end widget content -->

</div>

</div>
<!-- end widget div -->

<!-- end widget -->
</article>
<!-- WIDGET END -->
</div>
<!-- end row -->
<!-- row -->
<div class="row">
    <!-- a blank row to get started -->
    <div class="col-sm-12">
        <!-- your contents here -->

    </div>
</div>
<!-- end row -->
</section>
<!-- end widget grid -->
</div>
<!-- END MAIN CONTENT -->

<input type="hidden" id="autorizado" value="${autorizado}">
<input type="hidden" id="enGuaderia" value="${fichaRotavirus.enGuarderia}">
<input type="hidden" id="enUCI" value="${fichaRotavirus.UCI}">
<input type="hidden" id="altaUCIDiarrea" value="${fichaRotavirus.altaUCIDiarrea}">
<input type="hidden" id="disappear" value="<spring:message code="lbl.messagebox.disappear"/>"/>
<input type="hidden" id="msjError" value="<spring:message code="lbl.messagebox.error.completing"/>"/>
<input type="hidden" id="msjErrorSaving" value="<spring:message code="lbl.messagebox.error.saving"/>"/>
<input type="hidden" id="msjSuccessful" value="<spring:message code="lbl.messagebox.successful.saved"/>"/>
<input type="hidden" id="msjSelect" value="<spring:message code="lbl.select"/>"/>
<input id="text_value" type="hidden" value="<spring:message code="lbl.result.value"/>"/>
<input id="text_date" type="hidden" value="<spring:message code="lbl.result.date"/>"/>
<input id="text_response" type="hidden" value="<spring:message code="lbl.result.response"/>"/>
</div>


<!-- END MAIN PANEL -->
<!-- BEGIN FOOTER -->
<jsp:include page="../fragments/footer.jsp"/>
<!-- END FOOTER -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<jsp:include page="../fragments/corePlugins.jsp"/>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<!-- JQUERY BOOTSTRAP WIZARD -->
<spring:url value="/resources/js/plugin/bootstrap-wizard/jquery.bootstrap.wizard.min.js" var="jqueryBootstrap"/>
<script src="${jqueryBootstrap}"></script>
<!-- JQUERY FUELUX WIZARD -->
<spring:url value="/resources/js/plugin/fuelux/wizard/wizard.min.js" var="jQueryFueWiz"/>
<script src="${jQueryFueWiz}"></script>
<!-- Data table -->
<spring:url value="/resources/js/plugin/datatables/jquery.dataTables.min.js" var="dataTables"/>
<script src="${dataTables}"></script>
<spring:url value="/resources/js/plugin/datatables/dataTables.colVis.min.js" var="dataTablesColVis"/>
<script src="${dataTablesColVis}"></script>
<spring:url value="/resources/js/plugin/datatables/dataTables.tableTools.min.js" var="dataTablesTableTools"/>
<script src="${dataTablesTableTools}"></script>
<spring:url value="/resources/js/plugin/datatables/dataTables.bootstrap.min.js" var="dataTablesBootstrap"/>
<script src="${dataTablesBootstrap}"></script>
<spring:url value="/resources/js/plugin/datatable-responsive/datatables.responsive.min.js" var="dataTablesResponsive"/>
<script src="${dataTablesResponsive}"></script>
<!-- JQUERY VALIDATE -->
<spring:url value="/resources/js/plugin/jquery-validate/jquery.validate.min.js" var="validate"/>
<script src="${validate}"></script>
<spring:url value="/resources/js/plugin/jquery-validate/messages_{language}.js" var="jQValidationLoc">
    <spring:param name="language" value="${pageContext.request.locale.language}"/></spring:url>
<script src="${jQValidationLoc}"></script>

<!-- bootstrap datepicker -->
<spring:url value="/resources/js/plugin/bootstrap-datepicker/bootstrap-datepicker.js" var="datepickerPlugin"/>
<script src="${datepickerPlugin}"></script>

<!-- JQUERY BLOCK UI -->
<spring:url value="/resources/js/plugin/jquery-blockui/jquery.blockUI.js" var="jqueryBlockUi"/>
<script src="${jqueryBlockUi}"></script>

<!-- jQuery Selecte2 Input -->
<spring:url value="/resources/js/plugin/select2/select2.min.js" var="selectPlugin"/>
<script src="${selectPlugin}"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<spring:url value="/resources/scripts/rotavirus/enterForm.js" var="enterForm"/>
<script src="${enterForm}"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<spring:url value="/resources/scripts/utilidades/handleDatePickers.js" var="handleDatePickers"/>
<script src="${handleDatePickers}"></script>
<spring:url value="/resources/scripts/utilidades/seleccionUnidad.js" var="selecUnidad"/>
<script src="${selecUnidad}"></script>
<!-- PARAMETROS LENGUAJE -->
<c:set var="blockMess"><spring:message code="blockUI.message"/></c:set>
<!-- script calcular edad -->
<spring:url value="/resources/scripts/utilidades/calcularEdad.js" var="calculateAge"/>
<script src="${calculateAge}"></script>
<!-- JQUERY INPUT MASK -->
<spring:url value="/resources/js/plugin/jquery-inputmask/jquery.inputmask.bundle.min.js" var="jqueryInputMask"/>
<script src="${jqueryInputMask}"></script>
<spring:url value="/resources/scripts/utilidades/handleInputMask.js" var="handleInputMask"/>
<script src="${handleInputMask}"></script>

<spring:url var="municipiosURL" value="/api/v1/municipiosbysilais"/>
<c:url value="/rotavirus/save" var="saveUrl"/>
<spring:url value="/api/v1/unidadesPrimHosp" var="unidadesUrl"/>
<spring:url value="/api/v1/municipio" var="municipioByDepaUrl"/>
<spring:url value="/api/v1/comunidad" var="comunidadUrl"/>
<spring:url value="/irag/updatePerson" var="updatePersonUrl"/>
<spring:url value="/irag/completeIrag" var="completeIragUrl"/>
<c:url value="/rotavirus/search/" var="searchUrl"/>
<c:url var="semanaEpidemiologicaURL" value="/api/v1/semanaEpidemiologica"/>
<c:url var="updatePersonURL" value="/rotavirus/updatePerson"/>
<spring:url var="sResultsUrl" value="/api/v1/searchApproveResultsNoti"/>

<script type="text/javascript">

    $(document).ready(function () {
        pageSetUp();
        var parametros = {saveUrl: "${saveUrl}",
            unidadesUrl: "${unidadesUrl}",
            municipiosUrl: "${municipiosURL}",
            municipioByDepaUrl: "${municipioByDepaUrl}",
            comunidadUrl: "${comunidadUrl}",
            updatePersonUrl: "${updatePersonUrl}",
            completeIragUrl: "${completeIragUrl}",
            searchUrl: "${searchUrl}",
            blockMess: "${blockMess}",
            sSemanaEpiUrl: "${semanaEpidemiologicaURL}",
            sResultsUrl: "${sResultsUrl}",
            updatePersonURL: "${updatePersonURL}"
        };

        handleInputMasks();
        SeleccionUnidad.init(parametros);
        handleDatePickers("${pageContext.request.locale.language}");
        $('#idNotificacion').val("${fichaRotavirus.daNotificacion.idNotificacion}").change();
        EnterRotavirus.init(parametros);

        $("li.notificacion").addClass("open");
        $("li.rotavirus").addClass("active");
        if ("top" != localStorage.getItem("sm-setmenu")) {
            $("li.rotavirus").parents("ul").slideDown(200);
        }

        $("#dFechaD1").hide();
        $("#dFechaD2").hide();
        $("#dFechaD3").hide();
        $('#dRegistroV').hide();
        $('#dTipoVacuna').hide();
        $('#dNoVomito').hide();
        $('#dFechaVomito').hide();
        $('#dNoDaysUci').hide();
        $('#dUciDiarrea').hide();
        $('#dAntName').hide();
        $('#dOtraCaracHeces').hide();
        $('#dCareName').hide();
        $("#fechaNacimiento").change();
        $('#checkbox-d1').change();
        $('#checkbox-d2').change();
        $("#checkbox-d3").change();
        $('#heces').val('${fichaRotavirus.caracteristaHeces.codigo}').change();
        $('#otraCaracHeces').change();
        $('#antHosp').val('${fichaRotavirus.usoAntibioticoPrevio.codigo}').change();
        $('#nombreAntibiotico').val('${fichaRotavirus.cualAntibiotico}');
        $('#vomito').val('${fichaRotavirus.vomito.codigo}').change();
        $('#novomito').val('${fichaRotavirus.noVomito24Hrs}');
        $('#fechaInicioVomito').change();
        $('#vacunado').val('${fichaRotavirus.vacunado.codigo}').change();
        $('#registrovacunado').val('${fichaRotavirus.registroVacuna.codigo}').change();
        $('#tipoVacuna').val('${fichaRotavirus.tipoVacunaRotavirus.codigo}').change();
        if ($("#enGuaderia").val()!=null){
            var enGuarderia = $("#enGuaderia").val();
            if (enGuarderia === 'true') {
                $("#rbtnCareY").attr("checked", true).change();
            } else {
                $("#rbtnCareN").attr("checked", true).change();
            }
        }
        if ($("#enUCI").val()!=null) {
            var uci = $("#enUCI").val();
            if (uci === 'true') {
                $("#rbtnUciY").prop("checked", true).change();
            } else {
                $("#rbtnUciN").prop("checked", true).change();
            }
            $('#noDiasUCI').val('${fichaRotavirus.diasUCI}').change();
            if ($("#altaUCIDiarrea").val()!=null) {
                var uciDiarrea = $("#altaUCIDiarrea").val();
                if (uciDiarrea === 'true') {
                    $("#rbtnUciDY").prop("checked", true).change();
                } else {
                    $("#rbtnUciDN").prop("checked", true).change();
                }
            }
        }
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>
