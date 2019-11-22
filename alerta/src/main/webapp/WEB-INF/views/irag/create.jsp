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
<jsp:include page="../fragments/bodyHeader.jsp"/>
<!-- #NAVIGATION -->
<jsp:include page="../fragments/bodyNavigation.jsp"/>

<!-- MAIN PANEL -->
<div id="main" data-role="main">
<!-- RIBBON -->
<div id="ribbon">
			<span class="ribbon-button-alignment">
				<span id="refresh" class="btn btn-ribbon" data-action="resetWidgets(fff)" data-placement="bottom"
                      data-original-title="<i class='text-warning fa fa-warning'></i> <spring:message code="msg.reset" />"
                      data-html="true">
					<i class="fa fa-refresh"></i>
				</span>
			</span>
    <!-- breadcrumb -->
    <ol class="breadcrumb">
        <li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home"/></a> <i
                class="fa fa-angle-right"></i> <a
                href="<spring:url value="/irag/create" htmlEscape="true "/>"><spring:message code="menu.irageti"/></a>
        </li>
    </ol>
    <!-- end breadcrumb -->
    <jsp:include page="../fragments/layoutOptions.jsp"/>
</div>
<!-- END RIBBON -->
<!-- MAIN CONTENT -->
<div id="content">
<!-- row -->
<div class="row">
    <!-- col -->
    <div class="col col-xs-12 col-sm-7 col-md-7 col-lg-5">
        <h2 class="page-title txt-color-blueDark">
            <!-- PAGE HEADER -->
            <i class="fa-fw fa fa-stethoscope"></i>
            <spring:message code="lbl.form.irag"/>
						<span> <i class="fa fa-angle-right"></i>
							<spring:message code="lbl.register"/>
						</span>
        </h2>
    </div>
    <!-- end col -->
    <!-- right side of the page with the sparkline graphs -->
    <!-- col -->
    <div class="col col-xs-12 col-sm-5 col-md-5 col-lg-7">
        <!-- sparks -->
        <ul id="sparks">
            <li class="sparks-info">
                <h5><spring:message code="sp.day"/> <span class="txt-color-greenDark"><i
                        class="fa fa-arrow-circle-down"></i>17</span></h5>

                <div class="sparkline txt-color-blue hidden-mobile hidden-md hidden-sm">
                    0,1,3,4,11,12,11,13,10,11,15,14,20,17
                </div>
            </li>
            <li class="sparks-info">
                <h5><spring:message code="sp.week"/> <span class="txt-color-red"><i class="fa fa-arrow-circle-up"></i>&nbsp;57</span>
                </h5>

                <div class="sparkline txt-color-purple hidden-mobile hidden-md hidden-sm">
                    23,32,11,23,33,45,44,54,45,48,57
                </div>
            </li>
            <li class="sparks-info">
                <h5><spring:message code="sp.month"/> <span class="txt-color-red"><i class="fa fa-arrow-circle-up"></i>&nbsp;783</span>
                </h5>

                <div class="sparkline txt-color-purple hidden-mobile hidden-md hidden-sm">
                    235,323,114,231,333,451,444,541,451,483,783
                </div>
            </li>
        </ul>
        <!-- end sparks -->
    </div>

    <!-- end col -->
</div>
<div id="dWarning" hidden="hidden" class="alert alert-warning fade in">
    <button class="close" data-dismiss="alert">
        ×
    </button>

        <!-- PAGE HEADER -->
        <i class="fa-fw fa fa-warning"></i>
    <strong> <spring:message code="lbl.warning"/></strong>
        <spring:message code="lbl.unauthorized.user.warning"/>
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

    <h2><spring:message code="lbl.irag"/></h2>

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
            <span class="badge">4</span> <spring:message code="lbl.step4.short"/> <span class="chevron"></span>
        </li>
        <li id="liStep5" data-target="#step5">
            <span class="badge">5</span> <spring:message code="lbl.step5.short"/> <span class="chevron"></span>
        </li>
        <li data-target="#step6">
            <span class="badge">6</span> <spring:message code="lbl.step6.short"/> <span class="chevron"></span>
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
<form:form modelAttribute="irag" id="wizard-1" class="smart-form">
<h2 class="row-seperator-header"><i class="fa fa-male "></i> <spring:message
        code="lbl.patient"/> ${irag.idNotificacion.persona.primerNombre} ${irag.idNotificacion.persona.segundoNombre} ${irag.idNotificacion.persona.primerApellido} ${irag.idNotificacion.persona.segundoApellido}
</h2>

<div class="step-pane active" id="step1">
<h3 class="">
    <spring:message code="lbl.general.data"/>
</h3>

<fieldset>
<div hidden="hidden">

    <label class="input"> <i
            class="icon-prepend fa fa-male fa-fw"></i> <input value="${irag.idNotificacion.persona.personaId}"
                                                              type="text" id="personaId" name="personaId"
            />
    </label>

    <label class="input"> <i
            class="icon-prepend fa fa-male fa-fw"></i> <input type="text" id="idNotificacion" name="idNotificacion"
                                                              value="${irag.idNotificacion.idNotificacion}"
            />
    </label>
</div>
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
                        <c:when test="${entidad.codigo eq irag.idNotificacion.codSilaisAtencion.codigo}">
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
                        <c:when test="${muni.codigoNacional eq irag.idNotificacion.codUnidadAtencion.municipio.codigoNacional}">
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

    <section class="col col-xs-12 col-sm-12 col-md-8 col-lg-5">
        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
        <spring:message var="hu" code="lbl.health.unit"/>
        <label class="text-left txt-color-blue font-md">
                ${hu}
        </label>


        <div class="input-group">
            <spring:message code="msg.select.hu" var="selectUni"/>
            <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
            <select data-placeholder="${selectUni}" name="codUnidadAtencion" id="codUnidadAtencion" class="select2">
                <option value=""></option>
                <c:forEach items="${uni}" var="unid">
                    <c:choose>
                        <c:when test="${irag.idNotificacion.codUnidadAtencion.codigo eq unid.codigo}">
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
    <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
        <spring:message var="cDate" code="lbl.consultation.date"/>
        <label class="text-left txt-color-blue font-md">
                ${cDate}
        </label>

        <div class="input">
            <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
            <input class="form-control date-picker" data-date-end-date="+0d"
                        type="text" name="fechaConsulta" id="fechaConsulta" value="<fmt:formatDate value="${irag.fechaConsulta}" pattern="dd/MM/yyyy" />"
                        placeholder="${cDate}"/>


            <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                    code="lbl.consultation.date"/></b>

        </div>

    </section>

    <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-4">

            <spring:message var="fCDate" code="lbl.first.consultation.date"/>
            <label class="text-left txt-color-blue font-md">
                    ${fCDate}
            </label>

            <div class="input" style="width: 73%">
                <spring:message var="consDate" code="lbl.first.cons.date"/>

                <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                <input class="form-control date-picker"
                            type="text" name="fechaPrimeraConsulta" id="fechaPrimeraConsulta" value="<fmt:formatDate value="${irag.fechaPrimeraConsulta}" pattern="dd/MM/yyyy" />"
                            placeholder="${consDate}"/>
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i>
                    <spring:message code="lbl.first.consultation.date"/></b>
            </div>


    </section>

    <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
        <spring:message var="fileNumber" code="lbl.file.number"/>
        <label class="text-left txt-color-blue font-md">
                ${fileNumber}
        </label>

        <div class="input">

            <i class="icon-prepend fa fa-sort-alpha-asc fa-fw"></i> <i
                class="icon-append fa fa-sort-numeric-asc fa-fw"></i>

            <form:input name="codExpediente" id="codExpediente" path="codExpediente" class="form-control" type="text"
                        placeholder="${fileNumber} "/>
            <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                    code="lbl.file.number"/></b>
        </div>
    </section>

</div>

<div class="row">
    <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="lbl.classification"/>
        </label>


        <div class="input-group">
            <spring:message var="selectClassification" code="msg.select.classification"/>
            <span class="input-group-addon"><i class="fa fa-list fa-fw"></i></span>
            <form:select id="codClasificacion" name="codClasificacion" path="codClasificacion"
                         data-placeholder="${selectClassification}" cssClass="select2">
                <option value=""></option>
                <form:options items="${catClasif}" itemValue="codigo" itemLabel="valor"/>
            </form:select>

        </div>

    </section>


    <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
        <label class="text-left txt-color-blue font-md hidden-xs">
            <spring:message code="sindfeb.urgent"/>
        </label>

        <div class="input-group">
            <span class="input-group-addon"> <i class="fa fa-exclamation-triangle fa-fw"></i></span>
            <select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.urgent" />"
                    name="urgente" id="urgente" class="select2">
                <option value=""></option>
                <c:forEach items="${catResp}" var="cresp">
                    <c:choose>
                        <c:when test="${cresp.codigo eq  irag.idNotificacion.urgente.codigo}">
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
                                                                    value="${irag.idNotificacion.persona.primerNombre}"/>
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
                                                                    value="${irag.idNotificacion.persona.segundoNombre}"/>
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
                                                                    value="${irag.idNotificacion.persona.primerApellido}"/>
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
                                                                    value="${irag.idNotificacion.persona.segundoApellido}"/>
        </label>
    </section>


</div>

<div class="row">

    <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="person.sexo"/>
        </label>

        <div class="input-group">
            <c:choose>
                <c:when test="${irag.idNotificacion.persona.sexo.codigo eq 'SEXO|M'}">
                    <span class="input-group-addon"><i class="fa fa-male fa-fw"></i></span>
                </c:when>
                <c:when test="${irag.idNotificacion.persona.sexo.codigo eq 'SEXO|F'}">
                    <span class="input-group-addon"><i class="fa fa-female fa-fw"></i></span>
                </c:when>
                <c:otherwise>
                    <span class="input-group-addon"><i class="fa fa-question fa-fw"></i></span>
                </c:otherwise>
            </c:choose>
            <label class="input">
                <input class="form-control" type="text" name="sexo" id="sexo"
                       value="${irag.idNotificacion.persona.sexo}" readonly cssStyle="background-color: #f0fff0"
                       placeholder=" <spring:message code="person.sexo" />">
            </label>
            <span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
        </div>


    </section>


    <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">

        <label class="text-left txt-color-blue font-md">
            <spring:message code="person.fecnac"/>

        </label>

        <div class="input-group">
            <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
            <label class="input">
                <input style="background-color: #f0fff0" class="form-control" readonly
                       type="text" name="fechaNacimiento" id="fechaNacimiento"
                       value="<fmt:formatDate value="${irag.idNotificacion.persona.fechaNacimiento}" pattern="dd/MM/yyyy" />"
                       placeholder=" <spring:message code="person.fecnac" />">
            </label>
            <span class="input-group-addon"><i class="fa fa-calendar fa-fw"></i></span>
        </div>


    </section>

    <section class="col col-xs-12 col-sm-12 col-md-8 col-lg-4">
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

    <section class="col col-xs-12 col-sm-8 col-md-4 col-lg-6">
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
                        <c:when test="${depa.divisionpoliticaId eq departamentoProce.divisionpoliticaId}">
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


    <section class="col col-xs-12 col-sm-12 col-md-4 col-lg-6">
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
                        <c:when test="${muniResi.codigoNacional eq irag.idNotificacion.persona.municipioResidencia.codigoNacional or muniResi.codigoNacional eq irag.idNotificacion.municipioResidencia.codigoNacional}">
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
    <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-12">
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
                        <c:when test="${comu.codigo eq irag.idNotificacion.comunidadResidencia.codigo or comu.codigo eq irag.idNotificacion.persona.comunidadResidencia.codigo}">
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
</div>

<div class="row">
    <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-6">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="person.direccion"/>
        </label>

        <div class="input">

            <i class="icon-prepend fa fa-map-marker fa-fw"></i> <i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
            <c:choose>
                <c:when test="${not empty irag.idNotificacion.direccionResidencia}">
                    <input class="form-control" type="text" name="direccionResidencia" id="direccionResidencia"
                           value="${irag.idNotificacion.direccionResidencia}"
                           placeholder=" <spring:message code="person.direccion" />">
                </c:when>
                <c:otherwise>
                    <input class="form-control" type="text" name="direccionResidencia" id="direccionResidencia"
                           value="${irag.idNotificacion.persona.direccionResidencia}"
                           placeholder=" <spring:message code="person.direccion" />">
                </c:otherwise>
            </c:choose>
            <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                    code="person.direccion"/></b>
        </div>

    </section>

    <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="person.telefono"/>
        </label>

        <div>
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i
                    class="icon-append fa fa-sort-numeric-asc"></i>
                <input type="text" name="telefonoResidencia" value="${irag.idNotificacion.persona.telefonoResidencia}"
                       id="telefonoResidencia" class="telefono">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                        code="person.telefono"/> </b>
            </label>
        </div>

    </section>


</div>

</fieldset>
<fieldset>
    <div class="row">
        <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-6">
            <spring:message var="mName" code="lbl.mother.father"/>
            <label class="text-left txt-color-blue font-md">
                    ${mName}
            </label>
            <label class="input"> <i
                    class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                <form:input placeholder="${mName}"
                            type="text" path="nombreMadreTutor" name="nombreMadreTutor"
                        />
                <b class="tooltip tooltip-top-right"> <i
                        class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.mother"/>
                </b>


            </label>
        </section>

        <section class="col col-xs-12 col-sm-12 col-md-8 col-lg-4">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.provenance"/>
            </label>

            <div class="input-group">
                <spring:message code="msg.select.proc" var="selectProc"/>
                <span class="input-group-addon"> <i class="fa fa-location-arrow fa-fw"></i></span>
                <form:select placeholder="${selectProc}" name="codProcedencia" cssClass="select2" path="codProcedencia">
                    <option value=""></option>
                    <form:options items="${catProcedencia}" itemValue="codigo" itemLabel="valor"/>

                </form:select>
            </div>
        </section>
    </div>


    <div class="row">

        <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-6">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <spring:message var="diagnosis" code="lbl.diagnosis"/>
            <label class="text-left txt-color-blue font-md">
                    ${diagnosis}
            </label>


            <div class="input-group">
                <spring:message code="msg.select.diagnosis" var="sDx"/>
                <span class="input-group-addon"> <i class="fa fa-list"></i></span>
                <select data-placeholder="${sDx}" id="diagnostico" name="diagnostico" class="select2">
                    <option value=""></option>
                    <c:forEach items="${catCie10Irag}" var="enf">
                        <c:choose>
                            <c:when test="${fn:contains(irag.diagnostico.codigoCie10, enf.codigoCie10)}">
                                <option selected value="${enf.codigoCie10}">${enf.codigoCie10} - ${enf.nombreCie10}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${enf.codigoCie10}">${enf.codigoCie10} - ${enf.nombreCie10}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>

        </section>

        <section class="col col-xs-12 col-sm-12 col-md-8 col-lg-4">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.capture"/>
            </label>

            <div class="input-group">
                <spring:message code="msg.select.capt" var="selectProc"/>
                <span class="input-group-addon"> <i class="fa fa-hospital-o fa-fw"></i></span>
                <form:select placeholder="${selectProc}" name="codCaptacion" class="select2" path="codCaptacion">
                    <option value=""></option>
                    <form:options items="${catCaptac}" itemValue="codigo" itemLabel="valor"/>
                </form:select>
            </div>
        </section>
    </div>
</fieldset>

<fieldset>
    <legend class="text-left txt-color-blue font-md">
        <spring:message code="lbl.vaccination.history"/>
    </legend>
    <div class="row">
        <section class="col col-xs-12 col-sm-12 col-md-8 col-lg-4">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.presents.vaccination.card"/>
            </label>

            <div class="inline-group">
                <label class="radio"> <form:radiobutton path="tarjetaVacuna" value="1" name="tarjetaVacuna"/>
                    <i></i><spring:message code="lbl.yes"/></label>
                <label class="radio"> <form:radiobutton path="tarjetaVacuna" value="0" name="tarjetaVacuna"/>
                    <i></i><spring:message code="lbl.no"/></label>

            </div>
        </section>


        <section class="col col-xs-10 col-sm-8 col-md-5 col-lg-2">

            <c:choose>
                <c:when test="${!autorizado}">
                    <button disabled type="button" id="btnAddVaccine" class="btn btn-primary styleButton"
                            data-toggle="modal"
                            data-target="#myModal">
                        <i class="fa fa-plus icon-white"></i>
                        <spring:message code="act.add.vaccine"/>
                    </button>
                </c:when>

                <c:otherwise>
                    <button type="button" id="btnAddVaccine" class="btn btn-primary styleButton" data-toggle="modal"
                            data-target="#myModal">
                        <i class="fa fa-plus icon-white"></i>
                        <spring:message code="act.add.vaccine"/>
                    </button>
                </c:otherwise>

            </c:choose>

        </section>

    </div>


    <div id="dVaccinesTable">

        <!-- widget edit box -->
        <div class="jarviswidget-editbox">
            <!-- This area used as dropdown edit box -->

        </div>
        <!-- end widget edit box -->

        <!-- widget content -->
        <div class="widget-body no-padding">


            <table id="lista_vacunas"
                   class="table table-striped table-bordered table-hover" data-width="100%">
                <thead>
                <tr>
                    <th data-class="expand"><i
                            class="fa fa-fw fa-eyedropper text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message
                            code="lbl.vaccine"/></th>
                    <th data-hide="phone"><i class="fa fa-fw fa-list-alt text-muted hidden-md hidden-sm hidden-xs"></i>
                        <spring:message code="lbl.which"/></th>
                    <th data-hide="phone"><i
                            class="fa fa-fw fa-sort-numeric-asc text-muted hidden-md hidden-sm hidden-xs"></i>
                        <spring:message code="lbl.dose.number"/></th>
                    <th data-hide="phone"><i class="fa fa-fw fa-calendar text-muted hidden-md hidden-sm hidden-xs"></i>
                        <spring:message code="lbl.dose.last.date"/></th>
                    <th></th>

                </tr>
                </thead>
            </table>
        </div>

    </div>

</fieldset>


<fieldset>

    <legend class="text-left txt-color-blue font-md">
        <spring:message code="lbl.preexisting.conditions"/>
    </legend>

    <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-6">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="lbl.preexisting.conditions"/>
        </label>

        <div class="input-group">
            <spring:message code="msg.select.preexisting.condition" var="selectPreCon"/>

            <span class="input-group-addon"> <i class="fa fa-stethoscope"></i></span>
            <select data-placeholder="${selectCM}" name="condiciones" id="condiciones" multiple style="width:100%"
                    class="select2">

                <c:forEach items="${catCondPre}" var="cond">
                    <c:choose>
                        <c:when test="${fn:contains(irag.condiciones,cond.codigo)}">
                            <option selected value="${cond.codigo}">${cond.valor}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${cond.codigo}">${cond.valor}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>
        </div>
    </section>

    <section hidden="hidden" id="sOtraCond" class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="lbl.other.preexisting.condition"/>
        </label>

        <label class="input"> <i
                class="icon-prepend fa fa-sort-alpha-asc fa-fw"></i> <form:input
                type="text" name="otraCondicion" id="otraCondicion"
                path="otraCondicion"
                />
        </label>
    </section>

    <section hidden="hidden" id="sWeeksP" class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
        <spring:message var="weeksP" code="lbl.weeks.pregnancy"/>
        <label class="text-left txt-color-blue font-md">
                ${weeksP}
        </label>

        <div class="input">
            <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
            <form:input placeholder="${weeksP}" type="text" path="semanasEmbarazo" name="semanasEmbarazo"/>
            <b class="tooltip tooltip-bottom-right"> <i
                    class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.weeks.pregnancy"/>
            </b>
        </div>
    </section>
</fieldset>

</div>
<div class="step-pane" id="step3">
<h3>
    <spring:message code="lbl.clinical.data"/>
</h3>
<fieldset>
    <div class="row">
        <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-4">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <spring:message var="symptomsDate" code="lbl.symptoms.start.date"/>
            <label class="text-left txt-color-blue font-md">
                    ${symptomsDate}
            </label>

            <div class="input">
                <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                <form:input type="text" name="fechaInicioSintomas"
                            path="idNotificacion.fechaInicioSintomas"
                            class="form-control date-picker"
                            data-dateformat="dd/mm/yy" placeholder="${symptomsDate}"/>
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                        code="msg.enter.sym.date"/></b>
            </div>
        </section>

       <%-- <section class="col col-xs-10 col-sm-8 col-md-5 col-lg-2">
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
        <section class="col col-xs-10 col-sm-8 col-md-5 col-lg-2">
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

        <section class="col col-xs-10 col-sm-8 col-md-5 col-lg-2">
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
        </section>--%>
    </div>
    <div class="row">
        <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-6">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.clinical.manifestations"/>
            </label>

            <div class="input-group">
                <spring:message code="msg.select.clinical.manifestations" var="selectCM"/>

                <span class="input-group-addon"> <i class="fa fa-stethoscope"></i></span>
                <select data-placeholder="${selectCM}" name="manifestaciones" id="manifestaciones" multiple
                        style="width:100%" class="select2">

                    <c:forEach items="${catManCli}" var="mani">
                        <c:choose>
                            <c:when test="${fn:contains(irag.manifestaciones, mani.codigo)}">
                                <option selected value="${mani.codigo}">${mani.valor}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${mani.codigo}">${mani.valor}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
        </section>

        <section hidden="hidden" id="sOtraMani" class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.other.clinical.manifestation"/>
            </label>

            <label class="input"> <i
                    class="icon-prepend fa fa-sort-alpha-asc fa-fw"></i> <form:input
                    type="text" name="otraManifestacion" id="otraManifestacion"
                    path="otraManifestacion"
                    />
            </label>
        </section>


    </div>


</fieldset>
<fieldset>

    <div class="row">
        <section class="col col-xs-12 col-sm-8 col-md-8 col-lg-4">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.use.antibiotics"/>
            </label>

            <div class="input-group">
                <spring:message code="msg.select.use.antibiotics" var="selectUse"/>
                <span class="input-group-addon"> <i class="fa fa-list-ul fa-fw"></i></span>
                <form:select name="codAntbUlSem" placeholder="${selectUse}" cssClass="select2" path="codAntbUlSem">
                    <option value=""></option>
                    <form:options items="${catResp}" itemValue="codigo" itemLabel="valor"/>

                </form:select>

            </div>


        </section>
    </div>

    <div id="dAntib" hidden="hidden">
    <div class="row">
        <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
            <spring:message var="antAmount" code="lbl.antibiotics.amount"/>
            <label class="text-left txt-color-blue font-md">
                    ${antAmount}
            </label>

            <div class="input">
                <i class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <form:input placeholder="${antAmount}" type="text" path="cantidadAntib" name="cantidadAntib"/>
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                        code="msg.enter.antibiotics.amount"/>
                </b>

            </div>

        </section>

        <section class="col col-xs-12 col-sm-12 col-md-10 col-lg-5">
            <spring:message var="antName" code="lbl.antibiotics.names"/>
            <label class="text-left txt-color-blue font-md">
                    ${antName}
            </label>


            <div class="input">
                <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                <form:input type="text" placeholder="${antName}" size="6" name="nombreAntibiotico"
                            path="nombreAntibiotico"/>
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                        code="msg.enter.antibiotics.name"/>
                </b>

            </div>


        </section>
    </div>

    <div class="row">
        <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
            <spring:message var="doseFDate" code="lbl.dose.first.date"/>
            <label class="text-left txt-color-blue font-md">
                    ${doseFDate}
            </label>

            <div class="input">
                <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                <form:input placeholder="${doseFDate}" type="text" name="fechaPrimDosisAntib"
                            path="fechaPrimDosisAntib"
                            class="form-control date-picker"
                            data-dateformat="dd/mm/yy"/>
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                        code="msg.enter.first.date.antib"/></b>


            </div>

        </section>

        <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
            <spring:message var="doseLDate" code="lbl.dose.last.date"/>
            <label class="text-left txt-color-blue font-md">
                    ${doseLDate}
            </label>

            <div class="input">
                <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                <form:input placeholder="${doseLDate}" type="text" name="fechaUltDosisAntib"
                            path="fechaUltDosisAntib"
                            class="form-control date-picker"
                            data-dateformat="dd/mm/yy"/>
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                        code="msg.enter.last.date.antib"/></b>
            </div>

        </section>

        <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
            <spring:message var="doseNumber" code="lbl.dose.number"/>
            <label class="text-left txt-color-blue font-md">
                    ${doseNumber}
            </label>


            <div class="input">
                <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <form:input class="entero" placeholder="${doseNumber}" type="text" path="noDosisAntib"
                            name="noDosisAntib"/>
                <b class="tooltip tooltip-bottom-right"> <i
                        class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.number.dose"/>
                </b>
            </div>


        </section>

        <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.route"/>
            </label>

            <div class="input-group">
                <spring:message code="msg.select.route" var="selectRoute"/>
                <span class="input-group-addon"> <i class="fa fa-list-ul fa-fw"></i></span>
                <form:select name="codViaAntb" placeholder="${selectRoute}" cssClass="select2" path="codViaAntb">
                    <option value=""></option>
                    <form:options items="${catVia}" itemValue="codigo" itemLabel="valor"/>

                </form:select>

            </div>


        </section>
    </div>
    </div>

</fieldset>

<fieldset>

    <div class="row">
        <section class="col col-xs-12 col-sm-8 col-md-8 col-lg-4">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.use.antiviral"/>
            </label>


            <div class="input-group">
                <spring:message code="msg.select.use.antivirales" var="selectUseAntiv"/>
                <span class="input-group-addon"> <i class="fa fa-list-ul fa-fw"></i></span>
                <form:select placeholder="${selectUseAntiv}" name="usoAntivirales" cssClass="select2"
                             path="usoAntivirales">
                    <option value=""></option>
                    <form:options items="${catResp}" itemValue="codigo" itemLabel="valor"/>
                </form:select>

            </div>

        </section>
    </div>

    <div id="dAntiv" hidden="hidden">

    <div class="row">

        <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
            <spring:message var="antivName" code="lbl.antiviral.name"/>
            <label class="text-left txt-color-blue font-md">
                    ${antivName}
            </label>


            <div class="input">
                <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                <form:input placeholder="${antivName}" type="text" name="nombreAntiviral" path="nombreAntiviral"/>
                <b class="tooltip tooltip-bottom-right"> <i
                        class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.antiviral.name"/>
                </b>
            </div>


        </section>

        <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.dose.first.date"/>
            </label>

            <div class="input">
                <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                <form:input placeholder="${doseFDate}" type="text" name="fechaPrimDosisAntiviral"
                            path="fechaPrimDosisAntiviral"
                            class="form-control date-picker"
                            data-dateformat="dd/mm/yy"/>
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                        code="msg.enter.first.date.antiv"/></b>


            </div>

        </section>

        <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.dose.last.date"/>
            </label>


            <div class="input">
                <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                <form:input placeholder="${doseLDate}" type="text" name="fechaUltDosisAntiviral"
                            path="fechaUltDosisAntiviral"
                            class="form-control date-picker"
                            data-dateformat="dd/mm/yy"/>
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message
                        code="msg.enter.last.date.antiv"/></b>


            </div>

        </section>

        <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">

            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.dose.number"/>
            </label>

            <div class="input">
                <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                <form:input class="entero" type="text" placeholder="${doseNumber}" path="noDosisAntiviral"
                            name="noDosisAntiviral"/>
                <b class="tooltip tooltip-bottom-right"> <i
                        class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.number.dose"/> </b>
            </div>


        </section>
    </div>
    </div>

</fieldset>

<fieldset>

    <div class="row">
        <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-6">
            <spring:message var="radRes" code="lbl.radiology.results"/>
            <label class="text-left txt-color-blue font-md">
                    ${radRes}
            </label>


            <div class="input-group">
                <span class="input-group-addon"> <i class="fa fa-file-text-o"></i></span>
                <select data-placeholder="${radRes}" name="codResRadiologia" id="codResRadiologia" multiple
                        style="width:100%" class="select2">

                    <c:forEach items="${catResRad}" var="result">
                        <c:choose>
                            <c:when test="${fn:contains(irag.codResRadiologia, result.codigo)}">
                                <option selected value="${result.codigo}">${result.valor}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${result.codigo}">${result.valor}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>

        </section>

        <section id="sOtroResRadiologia" hidden="hidden" class="col col-xs-12 col-sm-12 col-md-12 col-lg-6">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.description.other"/>
            </label>
            <br>

            <label class="input"> <i
                    class="icon-prepend fa fa-pencil fa-fw"></i> <form:input
                    type="text" name="otroResultadoRadiologia"
                    path="otroResultadoRadiologia"
                    placeholder="Descripcion Otro Resultado"/>
            </label>

        </section>


    </div>

</fieldset>

</div>

<div class="step-pane" id="step4">
    <h3>
        <spring:message code="lbl.laboratory.data"/>
    </h3>

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

<div class="step-pane" id="step5">
    <h3>
        <spring:message code="lbl.patient.evolution"/>
    </h3>
    <fieldset>
        <div class="row">
            <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-6">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.uci"/>
                </label>
                <br>
            </section>
            <section class="col col-xs-12 col-sm-10 col-md-5 col-lg-2">
                <div class="inline-group" style="padding-bottom: 8px">
                    <label class="radio"> <form:radiobutton id="uciS" path="uci" value="1"
                                                            name="uci"/>
                        <i></i><spring:message code="lbl.yes"/></label>
                    <label class="radio"> <form:radiobutton id="uciN" path="uci" value="0"
                                                            name="uci"/>
                        <i></i><spring:message code="lbl.no"/></label>
                </div>
            </section>
        </div>

        <div hidden="hidden" id="dUci">

        <div class="row">
            <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
                <spring:message var="numberDays" code="lbl.numbers.days"/>
                <label class="text-left txt-color-blue font-md">
                        ${numberDays}
                </label>

                <div class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i
                        class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                    <form:input class="entero" id="noDiasHospitalizado" placeholder="${numberDays}" type="text" path="noDiasHospitalizado"
                                name="noDiasHospitalizado"/>
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i>
                        <spring:message code="msg.enter.number.days"/>
                    </b>
                </div>

            </section>

            <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.assisted.ventilation"/>
                </label>

                <div class="inline-group" style="padding-bottom: 8px">
                    <label class="radio"> <form:radiobutton id="ventS" path="ventilacionAsistida" value="1"
                                                            name="ventilacionAsistida"/> <i></i><spring:message
                            code="lbl.yes"/></label>
                    <label class="radio"> <form:radiobutton id="ventN" path="ventilacionAsistida" value="0"
                                                            name="ventilacionAsistida"/> <i></i><spring:message
                            code="lbl.no"/></label>

                </div>

            </section>
        </div>

        </div>
        <div class="row">
            <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-6">
                <spring:message var="diagnosis1" code="lbl.egress.diagnosis1"/>
                <label class="text-left txt-color-blue font-md">
                        ${diagnosis1}
                </label>
                <div class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                    <form:input placeholder="${sDx}" type="text" name="diagnostico1Egreso" id="diagnostico1Egreso" path="diagnostico1Egreso"/>
                </div>

            </section>
            <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-6">
                <spring:message var="diagnosis2" code="lbl.egress.diagnosis2"/>
                <label class="text-left txt-color-blue font-md">
                        ${diagnosis2}
                </label>
                <div class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                    <form:input placeholder="${sDx}" type="text" name="diagnostico2Egreso" id="diagnostico2Egreso" path="diagnostico2Egreso"/>
                </div>


            </section>
            </div>
            <div class="row">
            <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
                <spring:message var="egressDate" code="lbl.egress.date"/>
                <label class="text-left txt-color-blue font-md">
                        ${egressDate}
                </label>

                <div class="input">
                    <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                    <form:input placeholder="${egressDate}" type="text" name="fechaEgreso"
                                path="fechaEgreso"
                                class="form-control date-picker"
                                data-dateformat="dd/mm/yy"/>
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i>
                        <spring:message code="msg.enter.egress.date"/></b>

                </div>

            </section>

            <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-3">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.egress.condition"/>
                </label>


                <div class="input-group">
                    <spring:message code="msg.select.egress.condition" var="selectCondition"/>
                    <span class="input-group-addon"> <i class="fa fa-list-ul fa-fw"></i></span>
                    <form:select placeholder="${selectCondition}" name="codCondEgreso" cssClass="select2"
                                 path="codCondEgreso">
                        <option value=""></option>
                        <form:options items="${catConEgreso}" itemValue="codigo" itemLabel="valor"/>
                    </form:select>
                </div>

            </section>
        </div>

    </fieldset>

</div>

<div class="step-pane" id="step6">
    <h3>
        <spring:message code="lbl.final.case.classification"/>
    </h3>

    <fieldset>
        <input id="complete_t" type="hidden" value="<spring:message code="msg.sending.confirm.title"/>"/>
        <input id="complete_c" type="hidden" value="<spring:message code="msg.complete.noti"/>"/>
        <input id="opc_yes" type="hidden" value="<spring:message code="lbl.send.confirm.msg.opc.yes"/>"/>
        <input id="opc_no" type="hidden" value="<spring:message code="lbl.send.confirm.msg.opc.no"/>"/>
        <input type="text" hidden="hidden" name="completa" id="completa" value="${irag.idNotificacion.completa}" />
        <div class="row">
            <section class="col col-xs-12 col-sm-12 col-md-10 col-lg-5">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.final.case.classification"/>
                </label>


                <div class="input-group">
                    <spring:message code="msg.select.classification" var="selectCF"/>

                    <span class="input-group-addon"> <i class="fa fa-stethoscope"></i></span>
                    <select data-placeholder="${selectCF}" name="codClasFCaso" id="codClasFCaso" multiple
                            style="width:100%" class="select2">

                        <c:forEach items="${catClaFinal}" var="clasF">
                            <c:choose>
                                <c:when test="${fn:contains(irag.codClasFCaso, clasF.codigo)}">
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

        </div>

        <div id="dNB" hidden="hidden" class="row">
            <section class="col col-xs-12 col-sm-10 col-md-8 col-lg-4">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.final.case.NB"/>
                </label>


                <div class="input-group">
                    <spring:message code="msg.select.final.case.nb" var="selectFCNB"/>

                    <span class="input-group-addon"> <i class="fa fa-stethoscope"></i></span>
                    <select data-placeholder="${selectFCNB}" id="codClasFDetalleNB" name="codClasFDetalleNB"
                            class="select2">
                        <option value=""></option>
                        <c:forEach items="${catNB}" var="clasNB">
                            <c:choose>
                                <c:when test="${fn:contains(irag.codClasFDetalleNB.codigo, clasNB.codigo)}">
                                    <option selected value="${clasNB.codigo}">${clasNB.valor}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${clasNB.codigo}">${clasNB.valor}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </section>
            <div id="dNBDet" hidden="hidden">
                <section class="col col-xs-12 col-sm-10 col-md-8 col-lg-4">
                    <spring:message var="bacterialAgent" code="lbl.bacterial.etiologic.agent"/>
                    <label class="text-left txt-color-blue font-md">
                            ${bacterialAgent}
                    </label>

                    <div class="input">
                        <i class="icon-prepend fa fa-pencil fa-fw"></i> <i
                            class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                        <form:input placeholder="${bacterialAgent}" type="text" name="agenteBacteriano"
                                    id="agenteBacteriano"
                                    path="agenteBacteriano"/>
                        <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i>
                            <spring:message code="msg.enter.bacterial.agent"/>
                        </b>
                    </div>


                </section>

                <section class="col col-xs-12 col-sm-10 col-md-8 col-lg-4">
                    <spring:message var="serot" code="lbl.serotyping"/>
                    <label class="text-left txt-color-blue font-md">
                            ${serot}
                    </label>


                    <div class="input">
                        <i class="icon-prepend fa fa-pencil fa-fw"></i> <i
                            class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                        <form:input placeholder="${serot}" type="text" name="serotipificacion"
                                    path="serotipificacion" id="serotipificacion"/>
                        <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i>
                            <spring:message code="msg.enter.serotyping"/>
                        </b>
                    </div>
                </section>
            </div>

        </div>

        <div id="dNV" hidden="hidden" class="row">
            <section class="col col-4">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.final.case.NV"/>
                </label>


                <div class="input-group">
                    <spring:message code="msg.select.final.case.nv" var="selectFCNV"/>
                    <span class="input-group-addon"> <i class="fa fa-stethoscope"></i></span>
                    <select data-placeholder="${selectFCNV}" id="codClasFDetalleNV" name="codClasFDetalleNV"
                            class="select2">
                        <option value=""></option>
                        <c:forEach items="${catNV}" var="clasNV">
                            <c:choose>
                                <c:when test="${fn:contains(irag.codClasFDetalleNV.codigo, clasNV.codigo)}">
                                    <option selected value="${clasNV.codigo}">${clasNV.valor}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${clasNV.codigo}">${clasNV.valor}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </section>

            <section id="sNV" hidden="hidden" class="col col-xs-12 col-sm-10 col-md-8 col-lg-4">
                <spring:message var="viralAgent" code="lbl.viral.etiologic.agent"/>
                <label class="text-left txt-color-blue font-md">
                        ${viralAgent}
                </label>

                <div class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i
                        class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                    <form:input placeholder="${viralAgent}" type="text" name="agenteViral"
                                path="agenteViral" id="agenteViral"/>
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i>
                        <spring:message code="msg.enter.viral.agent"/>
                    </b>
                </div>

            </section>

        </div>


        <div hidden="hidden" id="dEtiAgents" class="row">
            <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-6">
                <spring:message var="etioAgents" code="lbl.etiologic.agents"/>
                <label class="text-left txt-color-blue font-md">
                        ${etioAgents}
                </label>

                <div class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i
                        class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                    <form:input placeholder="${etioAgents}" type="text" name="agenteEtiologico"
                                path="agenteEtiologico"/>
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i>
                        <spring:message code="msg.enter.etiologic.agents"/>
                    </b>
                </div>

            </section>
        </div>


    </fieldset>

</div>


</form:form>
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

<!-- Modal Vacuna -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="background-color:#3276b1 ">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title txt-color-white" id="myModalLabel"><spring:message code="lbl.new.vaccine"/></h4>
            </div>

            <div class="modal-body">
                <form:form id="fVaccine" modelAttribute="fVacuna" class="smart-form">
                    <input id="inVacIdNoti" hidden="hidden" type="text" name="idNotificacion"/>

                    <div class="row">
                        <section class="col col-sm-12 col-md-6 col-lg-6">

                            <label class="text-left txt-color-blue font-md">
                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                <spring:message code="lbl.vaccine"/>
                            </label>

                            <div class="input-group">
                                <spring:message code="msg.select.vaccine" var="selectVac"/>
                                <span class="input-group-addon"> <i class="fa fa-list-ul fa-fw"></i></span>
                            <form:select multiple="" placeholder="${selectVac}" id="codVacuna" name="codVacuna"
                                             cssClass="select2" path="codVacuna">
                                    <option value=""></option>
                                    <form:options items="${catVacunas}" itemValue="codigo" itemLabel="valor"/>
                                </form:select>


                            </div>
                        </section>


                        <section class="col col-sm-12 col-md-6 col-lg-6">

                            <label class="text-left txt-color-blue font-md">
                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                <spring:message code="lbl.which"/>
                            </label>


                            <div id="dVacHib">
                                <div class="input-group">
                                    <spring:message code="msg.select.vaccine.type" var="vacType"/>
                                    <span class="input-group-addon"> <i class="fa fa-list-ul fa-fw"></i></span>
                                   <select multiple data-placeholder="${vacType}" id="tVacHib" hidden="hidden" class="select2">
                                        <c:forEach items="${catTVacHib}" var="hib">
                                            <option value="${hib.codigo}">${hib.valor}</option>
                                        </c:forEach>
                                    </select>


                                </div>
                            </div>

                            <div id="dVacMenin" hidden="hidden">
                                <div class="input-group">
                                    <spring:message code="msg.select.vaccine.type" var="vacType"/>
                                    <span class="input-group-addon"> <i class="fa fa-list-ul fa-fw"></i></span>
                                   <select data-placeholder="${vacType}" multiple id="tVacMenin" class="select2">
                                        <c:forEach items="${catTVacMenin}" var="menin">
                                            <option value="${menin.codigo}">${menin.valor}</option>
                                        </c:forEach>
                                    </select>

                                </div>
                            </div>

                            <div id="dVacNeumo" hidden="hidden">

                                <div class="input-group">
                                    <spring:message code="msg.select.vaccine.type" var="vacType"/>
                                    <span class="input-group-addon"> <i class="fa fa-list-ul fa-fw"></i></span>
                                    <select multiple data-placeholder="${vacType}" id="tVacNeumo" class="select2">
                                        <c:forEach items="${catTVacNeumo}" var="neumo">
                                            <option value="${neumo.codigo}">${neumo.valor}</option>
                                        </c:forEach>
                                    </select>

                                </div>
                            </div>

                            <div id="dVacFlu" hidden="hidden">
                                <div class="input-group">
                                    <spring:message code="msg.select.vaccine.type" var="vacType"/>
                                    <span class="input-group-addon"> <i class="fa fa-list-ul fa-fw"></i></span>
                                 <select multiple data-placeholder="${vacType}" id="tVacFlu" class="select2">
                                        <c:forEach items="${catTVacFlu}" var="flu">
                                            <option value="${flu.codigo}">${flu.valor}</option>
                                        </c:forEach>
                                    </select>

                                </div>
                            </div>
                        </section>
                    </div>

                    <div class="row">

                        <section class="col col-sm-12 col-md-6 col-lg-6">
                            <label class="text-left txt-color-blue font-md">
                                <spring:message code="lbl.dose.number"/>
                            </label>


                            <div class="input">
                                <i class="icon-prepend fa fa-pencil fa-fw"></i> <i
                                    class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                                <form:input class="entero" placeholder="${doseNumber}" type="text" id="dosis"
                                            path="dosis" name="dosis"/>
                                <b class="tooltip tooltip-bottom-right"> <i
                                        class="fa fa-warning txt-color-pink"></i> <spring:message
                                        code="msg.enter.number.dose"/>
                                </b>
                            </div>


                        </section>

                        <section class="col col-sm-12 col-md-6 col-lg-6">
                            <label class="text-left txt-color-blue font-md">
                                <spring:message code="lbl.dose.last.date"/>
                            </label>


                            <div class="input">
                                <i class="icon-prepend fa fa-pencil"></i> <i
                                    class="icon-append fa fa-calendar fa-fw"></i>
                                <form:input path="fechaUltimaDosis" class="form-control date-picker"
                                            data-date-end-date="+0d"
                                            type="text" name="fechaUltimaDOsis" id="fechaUltimaDosis"
                                            pattern="dd/MM/yyyy"
                                            placeholder="${cDate}"/>
                                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i>
                                    <spring:message code="lbl.dose.last.date"/></b>

                            </div>


                        </section>

                    </div>


                </form:form>
            </div>

            <div class="modal-footer">
                <button id="btnCancel" type="button" class="btn btn-default" data-dismiss="modal">
                    <spring:message code="act.cancel"/>
                </button>
                <button id="btnSaveVaccine" type="submit" class="btn btn-primary">
                    <spring:message code="act.save"/>
                </button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<input id="inPregnancy" type="hidden" value="<spring:message code="lbl.weeks"/>"/>
<input type="hidden" id="autorizado" value="${autorizado}">
<input type="hidden" id="disappear" value="<spring:message code="lbl.messagebox.disappear"/>"/>
<input type="hidden" id="msjError" value="<spring:message code="lbl.messagebox.error.completing"/>"/>
<input type="hidden" id="msjErrorSaving" value="<spring:message code="lbl.messagebox.error.saving"/>"/>
<input type="hidden" id="msjSuccessful" value="<spring:message code="lbl.messagebox.successful.saved"/>"/>
<input type="hidden" id="msjSelect" value="<spring:message code="lbl.select"/>"/>
<input id="text_value" type="hidden" value="<spring:message code="lbl.result.value"/>"/>
<input id="text_date" type="hidden" value="<spring:message code="lbl.result.date"/>"/>
<input id="text_response" type="hidden" value="<spring:message code="lbl.result.response"/>"/>
<input id="succ" type="hidden" value="<spring:message code="lbl.added.vaccine"/>"/>
<input id="msg_yes" type="hidden" value="<spring:message code="lbl.send.confirm.msg.opc.yes"/>"/>
<input id="msg_no" type="hidden" value="<spring:message code="lbl.send.confirm.msg.opc.no"/>"/>
<input id="msg_conf" type="hidden" value="<spring:message code="msg.sending.confirm.title"/>"/>
<input id="msg_override_confirm_c" type="hidden" value="<spring:message code="msg.overrideV.confirm.content"/>"/>
<input id="msg_succOverride" type="hidden" value="<spring:message code="msg.successfully.overrideV"/>"/>
<input id="msg_override_cancel" type="hidden" value="<spring:message code="msg.override.vaccine.cancel"/>"/>


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
<spring:url value="/resources/js/plugin/jquery-blockui/jquery.blockUI.js" var="jqueryBlockUi" />
<script src="${jqueryBlockUi}"></script>

<!-- jQuery Selecte2 Input -->
<spring:url value="/resources/js/plugin/select2/select2.min.js" var="selectPlugin"/>
<script src="${selectPlugin}"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<spring:url value="/resources/scripts/irag/irag-create.js" var="iragCreate"/>
<script src="${iragCreate}"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<spring:url value="/resources/scripts/utilidades/handleDatePickers.js" var="handleDatePickers"/>
<script src="${handleDatePickers}"></script>

<spring:url value="/resources/scripts/utilidades/seleccionUnidad.js" var="selecUnidad"/>
<script src="${selecUnidad}"></script>
<!-- PARAMETROS LENGUAJE -->
<!-- script calcular edad -->
<spring:url value="/resources/scripts/utilidades/calcularEdad.js" var="calculateAge"/>
<script src="${calculateAge}"></script>
<!-- JQUERY INPUT MASK -->
<spring:url value="/resources/js/plugin/jquery-inputmask/jquery.inputmask.bundle.min.js" var="jqueryInputMask"/>
<script src="${jqueryInputMask}"></script>
<spring:url value="/resources/scripts/utilidades/handleInputMask.js" var="handleInputMask"/>
<script src="${handleInputMask}"></script>

<spring:url var="municipiosURL" value="/api/v1/municipiosbysilais"/>
<spring:url value="/irag/saveIrag" var="sAddIragUrl"/>
<spring:url value="/api/v1/unidadesPrimHosp" var="unidadesUrl"/>
<spring:url value="/irag/addVaccine" var="sAddVaccineUrl"/>
<spring:url value="/irag/getVaccines" var="sLoadVaccinesUrl"/>
<spring:url value="/api/v1/municipio" var="municipioByDepaUrl"/>
<spring:url value="/api/v1/comunidad" var="comunidadUrl"/>
<spring:url value="/irag/updatePerson" var="updatePersonUrl"/>
<spring:url value="/irag/completeIrag" var="completeIragUrl"/>
<spring:url value="/irag/search" var="searchIragUrl"/>
<spring:url value="/irag/overrideVaccine" var="overrideVaccineUrl"/>
<spring:url value="/irag/enfermedades" var="enfermedadUrl"/>
<c:url var="semanaEpidemiologicaURL" value="/api/v1/semanaEpidemiologica"/>
<spring:url var="sResultsUrl" value="/api/v1/searchApproveResultsNoti"/>
<c:set var="blockMess"><spring:message code="blockUI.message" /></c:set>


<script type="text/javascript">

    $(document).ready(function () {
        pageSetUp();


        var parametros = {sAddIragUrl: "${sAddIragUrl}",
            unidadesUrl: "${unidadesUrl}",
            sAddVaccineUrl: "${sAddVaccineUrl}",
            vaccines: "${sLoadVaccinesUrl}",
            municipiosUrl: "${municipiosURL}",
            municipioByDepaUrl: "${municipioByDepaUrl}",
            comunidadUrl: "${comunidadUrl}",
            updatePersonUrl: "${updatePersonUrl}",
            completeIragUrl: "${completeIragUrl}",
            searchIragUrl: "${searchIragUrl}",
            overrideVaccineUrl: "${overrideVaccineUrl}",
            blockMess: "${blockMess}",
            enfermedadUrl: "${enfermedadUrl}",
            sSemanaEpiUrl: "${semanaEpidemiologicaURL}",
            sResultsUrl: "${sResultsUrl}"


        };

        CreateIrag.init(parametros);
        handleInputMasks();
        SeleccionUnidad.init(parametros);
        handleDatePickers("${pageContext.request.locale.language}");
        $('#idNotificacion').val("${irag.idNotificacion.idNotificacion}").change();
        $('#fechaConsulta').change();
        $('#codClasFDetalleNB').change();
        $('#codClasFDetalleNV').change();
        $('#codResRadiologia').change();


        $('#condiciones').change();
        $('#manifestaciones').change();
        $('#codClasFCaso').change();
        $('#fechaNacimiento').change();
        $('#codAntbUlSem').change();
        $('#usoAntivirales').change();
        $('#uciS').change();

        if($('#autorizado').val() != "true"){
            $('#dWarning').fadeIn('slow');
            $('#wizard-1').find('input, textarea, button, select').attr('disabled','disabled');
        }




        $("li.notificacion").addClass("open");
        $("li.irageti").addClass("active");
        if ("top" != localStorage.getItem("sm-setmenu")) {
            $("li.irageti").parents("ul").slideDown(200);
        }
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>