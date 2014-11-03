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
<html>
<!-- BEGIN HEAD -->
<head>
    <jsp:include page="../fragments/headTag.jsp"/>
    <style>

        .styleButton {

            float: right;
            height: 31px;
            margin: 10px 0px 0px 5px;
            padding: 0px 22px;
            font: 300 15px/29px "Open Sans", Helvetica, Arial, sans-serif;
            cursor: pointer;
        }

        .pagination-sm{
            width: 170px !important;
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
    <div class="col-xs-12 col-sm-7 col-md-7 col-lg-4">
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
    <div class="col-xs-12 col-sm-5 col-md-5 col-lg-8">
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
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
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
        <li data-target="#step5">
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
<form:form modelAttribute="formVI" id="wizard-1" class="smart-form">
<h2 class="row-seperator-header"><i class="fa fa-male "></i> <spring:message code="lbl.patient" /> ${formVI.persona.primerNombre} ${formVI.persona.primerApellido}  </h2>
<div class="step-pane active" id="step1">
        <h3 class="">
            <spring:message code="lbl.general.data"/>
        </h3>

    <fieldset>
        <div hidden="hidden">

            <label hidden="hidden" class="input"> <i
                    class="icon-prepend fa fa-male fa-fw"></i> <form:input type="text" name="persona.personaId"
                                                                           path="persona.personaId"/>
            </label>

            <label hidden="hidden" class="input"> <i
                    class="icon-prepend fa fa-male fa-fw"></i> <form:input type="text" id="idIrag" name="idIrag"
                                                                           path="idIrag"/>
            </label>
        </div>
        <div class="row">


            <section class="col col-3">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.silais"/>

                </label>

                <form:select cssClass="select2" id="codSilaisAtencion" name="codSilaisAtencion"
                             path="codSilaisAtencion">
                    <option value=""><spring:message code="lbl.select"/></option>
                    <form:options items="${entidades}" itemValue="codigo" itemLabel="nombre"/>
                </form:select>


            </section>

            <section class="col col-4">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="sindfeb.muni"/>

                </label>

                <div class="input-group">
																<span class="input-group-addon"> <i
                                                                        class="fa fa-location-arrow fa-fw"></i>
																</span>
                    <select id="codMunicipio" name="codMunicipio"
                            class="select2">
                        <option value=""><spring:message code="lbl.select"/></option>
                        <c:forEach items="${munic}" var="muni">
                            <option value="${muni.codigoNacional}">${muni.nombre}</option>
                        </c:forEach>
                    </select>
                </div>
            </section>

            <section class="col col-5">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.health.unit"/>
                </label>

                <form:select cssClass="select2" id="codUnidadAtencion" name="codUnidadAtencion"
                             path="codUnidadAtencion">
                    <option value=""><spring:message code="lbl.select"/></option>
                    <form:options id="optUni" items="${uni}" itemValue="codigo" itemLabel="nombre"/>
                </form:select>
            </section>

        </div>

        <div class="row">
            <section class="col col-3">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.consultation.date"/>
                </label>

                <div class="input-group">

                    <form:input type="text" id="fechaConsulta" name="fechaConsulta" path="fechaConsulta"

                                class="form-control date-picker" data-date-end-date="+0d"
                            />

                    <span class="input-group-addon"> <i
                            class="fa fa-calendar fa-fw"></i></span>

                </div>
            </section>

            <section class="col col-3">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.first.consultation.date"/>
                </label>

                <div class="input-group">
                    <form:input id="fechaPrimeraConsulta" path="fechaPrimeraConsulta" type="text"
                                name="fechaPrimeraConsulta"

                                class="form-control date-picker"
                            />
                    <span class="input-group-addon"> <i
                            class="fa fa-calendar fa-fw"></i></span>

                </div>
            </section>

            <section class="col col-3">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.file.number"/>
                </label>
                <label class="input"> <i
                        class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i> <form:input
                        type="text" path="codExpediente" name="codExpediente" id="noExp"
                        />
                </label>
            </section>

            <section class="col col-3">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.classification"/>
                </label>

                <form:select cssClass="select2" id="codClasificacion" name="codClasificacion" path="codClasificacion">
                    <option value=""><spring:message code="lbl.select"/></option>
                    <form:options items="${catClasif}" itemValue="codigo" itemLabel="valor"/>
                </form:select>

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

        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="person.name1"/>
            </label>
            <label class="input"> <i
                    class="icon-prepend fa fa-pencil fa-fw"></i> <form:input readonly="true"
                                                                             cssStyle="background-color: #f0fff0"
                                                                             type="text" name="primerNombre"
                                                                             path="persona.primerNombre"/>
            </label>
        </section>

        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="person.name2"/>
            </label>
            <label class="input"> <i
                    class="icon-prepend fa fa-pencil fa-fw"></i> <form:input readonly="true"
                                                                             cssStyle="background-color: #f0fff0"
                                                                             type="text" name="segundoNombre"
                                                                             path="persona.segundoNombre"/>
            </label>
        </section>

        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="person.lastname1"/>
            </label>
            <label class="input"> <i
                    class="icon-prepend fa fa-pencil fa-fw"></i> <form:input readonly="true"
                                                                             cssStyle="background-color: #f0fff0"
                                                                             type="text" name="primerApellido"
                                                                             path="persona.primerApellido"/>
            </label>
        </section>

        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="person.lastname2"/>
            </label>
            <label class="input"> <i
                    class="icon-prepend fa fa-pencil fa-fw"></i> <form:input readonly="true"
                                                                             cssStyle="background-color: #f0fff0"
                                                                             type="text" name="segundoApellido"
                                                                             path="persona.segundoApellido"/>
            </label>
        </section>


    </div>

    <div class="row">

        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="person.sexo"/>
            </label>

            <label class="input"> <i
                    class="icon-prepend fa fa-male fa-fw"></i> <form:input readonly="true"
                                                                           cssStyle="background-color: #f0fff0"
                                                                           type="text" name="sexo"
                                                                           path="persona.sexo"/>
            </label>


        </section>


        <section class="col col-3">

            <label class="text-left txt-color-blue font-md">
                <spring:message code="person.fecnac"/>

            </label>

            <label class="input"> <i
                    class="icon-prepend fa fa-calendar fa-fw"></i> <form:input readonly="true"
                                                                               cssStyle="background-color: #f0fff0"
                                                                               type="text" name="fechaNacimiento"
                                                                               path="persona.fechaNacimiento"/>
            </label>


        </section>

        <section class="col col-2">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.age"/>
            </label>
            <label class="input"> <i
                    class="icon-prepend fa fa-credit-card fa-fw"></i> <input readonly=""
                                                                             style="background-color: #f0fff0"
                                                                             type="text" name="edadAnios"
                                                                             id="edadAnios">
            </label>
        </section>
    </div>

    <div class="row">

        <section class="col col-4">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.person.depart.resi"/>
            </label>

            <select
                    id="departamento" name="departamento"
                    class="select2">
                <option value=""><spring:message code="lbl.select"/></option>
                <c:forEach items="${departamentos}" var="depa">
                    <option value="${depa.codigoNacional}">${depa.nombre}</option>
                </c:forEach>
            </select>
        </section>


        <section class="col col-4">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="person.mun.res"/>
            </label>


            <div class="input-group">
																<span class="input-group-addon"> <i
                                                                        class="fa fa-location-arrow fa-fw"></i>
																</span>
                <form:select id="municipioResidencia" name="municipioResidencia"
                             path="persona.municipioResidencia.codigoNacional"
                             cssClass="select2">
                    <option value=""><spring:message code="lbl.select"/></option>
                    <form:options items="${municipiosResi}" itemValue="codigoNacional" itemLabel="nombre"/>

                </form:select>
            </div>
        </section>


        <section class="col col-4">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="person.com.res"/>
            </label>

            <div class="input-group">
																<span class="input-group-addon"> <i
                                                                        class="fa fa-location-arrow fa-fw"></i>
																</span>
                <form:select id="comunidadResidencia" name="comunidadResidencia"
                             path="persona.comunidadResidencia"
                             cssClass="select2">

                    <option value=""><spring:message code="lbl.select"/></option>
                    <form:options items="${comunidades}" itemValue="codigo" itemLabel="nombre"/>

                </form:select>
            </div>
        </section>


    </div>

    <div class="row">
        <section class="col col-6">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="person.direccion"/>
            </label>

            <form:textarea path="persona.direccionResidencia" name="direccionResidencia" class="form-control" rows="4"
                           placeholder="Textarea"/>

        </section>

        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="person.telefono"/>
            </label>

            <div class="input">
                <i class="icon-prepend fa fa-location-arrow fa-fw"></i>
                <form:input type="text" path="persona.telefonoResidencia" name="telefonoResidencia"
                            class="form-control"/>
            </div>


        </section>


    </div>

</fieldset>
<fieldset>
    <div class="row">
        <section class="col col-6">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.mother.father"/>
            </label>
            <label class="input"> <i
                    class="icon-prepend fa fa-pencil fa-fw"></i> <form:input
                    type="text" path="nombreMadreTutor" name="nombreMadreTutor"
                    />
                <b class="tooltip tooltip-bottom-right"> <i
                        class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.mother"/>
                </b>
            </label>
        </section>

        <section class="col col-3">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.provenance"/>
            </label>

            <div>
                <form:select name="codProcedencia" cssClass="select2" path="codProcedencia">
                    <option value=""><spring:message code="lbl.select"/></option>
                    <form:options items="${catProcedencia}" itemValue="codigo" itemLabel="valor"/>

                </form:select>


            </div>
        </section>
    </div>


    <div class="row">

        <section class="col col-6">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.diagnosis"/>
            </label>

            <div class="input">
                <i class="icon-prepend fa fa-location-arrow fa-fw"></i>
                <form:input type="text" path="diagnostico" name="diagnostico"
                            class="form-control"/>
            </div>
        </section>

        <section class="col col-3">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.capture"/>
            </label>

            <div>

                <form:select name="codCaptacion" class="select2" path="codCaptacion">
                    <option value=""><spring:message code="lbl.select"/></option>
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
        <section class="col col-4">
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


        <section class="col col-2">

            <button type="button" id="btnAddVaccine" class="btn btn-primary styleButton" data-toggle="modal"
                    data-target="#myModal">
                <spring:message code="act.add.vaccine"/>
            </button>

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
                    <th data-class="expand"><i
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


    <diV class="row">
        <section class="col md-3">

            <button type="button" class="btn btn-primary styleButton" data-toggle="modal" data-target="#modalCondPre">
                <spring:message code="act.add.preexisting.conditions"/>
            </button>

        </section>
    </diV>
    <br>

    <div>

        <!-- widget edit box -->
        <div class="jarviswidget-editbox">
            <!-- This area used as dropdown edit box -->

        </div>
        <!-- end widget edit box -->

        <!-- widget content -->
        <div class="widget-body no-padding col-md-6">


            <table id="lista_condiciones"
                   class="table table-striped table-bordered table-hover">
                <thead>
                <tr>
                    <th><i class="fa fa-fw fa-stethoscope text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message
                            code="lbl.preexisting.condition"/></th>
                    <th></th>
                </tr>
                </thead>
            </table>
        </div>

    </div>
</fieldset>

</div>
<div class="step-pane" id="step3">
<h3>
<spring:message code="lbl.clinical.data"/>
</h3>
<fieldset>
    <div class="row">
        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.symptoms.start.date"/>
            </label>

            <div class="input-group">
                <form:input type="text" name="fechaInicioSintomas"
                            path="fechaInicioSintomas"
                            class="form-control date-picker"
                            data-dateformat="dd/mm/yy"/>
                                                    <span class="input-group-addon"> <i
                                                            class="fa fa-calendar fa-fw"></i></span>

            </div>
        </section>
    </div>
</fieldset>
<fieldset>
    <div class="row">
        <section class="col md-3">
            <button type="button" class="btn btn-primary styleButton" data-toggle="modal" data-target="#modalCM">
                <spring:message code="act.add.clinical.manifestation"/>
            </button>
        </section>
    </div>
    <br>

    <div>

        <!-- widget edit box -->
        <div class="jarviswidget-editbox">
            <!-- This area used as dropdown edit box -->

        </div>
        <!-- end widget edit box -->

        <!-- widget content -->
        <div class="widget-body no-padding col-md-6">


            <table id="lista_manifestaciones"
                   class="table table-striped table-bordered table-hover">
                <thead>
                <tr>
                    <th><i class="fa fa-fw fa-stethoscope text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message
                            code="lbl.clinical.manifestation"/></th>
                    <th></th>
                </tr>
                </thead>
            </table>
        </div>

    </div>

</fieldset>
<fieldset>

    <div class="row">
        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.use.antibiotics"/>
            </label>
            <br>


            <div>

                <form:select name="codAntbUlSem" cssClass="select2" path="codAntbUlSem">
                    <option value=""><spring:message code="lbl.select"/></option>
                    <form:options items="${catResp}" itemValue="codigo" itemLabel="valor"/>

                </form:select>

            </div>


        </section>
    </div>

    <div class="row">
        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.antibiotics.amount"/>
            </label>


            <label style="width: 100px" class="input"> <i
                    class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i> <form:input
                    type="text" path="cantidadAntib" name="cantidadAntib"/> <b
                    class="tooltip tooltip-bottom-right"> <i
                    class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.antibiotics.amount"/>
            </b>
            </label>


        </section>

        <section class="col col-5">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.antibiotics.names"/>
            </label>
            <br>

            <label class="input"> <i
                    class="icon-prepend fa fa-pencil fa-fw"></i> <form:input
                    type="text" size="6" name="nombreAntibiotico"
                    path="nombreAntibiotico"
                    />
            </label>

        </section>
    </div>

    <div class="row">
        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.dose.first.date"/>
            </label>
            <br>

            <div class="input-group">
                <form:input type="text" name="fechaPrimDosisAntib"
                            path="fechaPrimDosisAntib"
                            class="form-control date-picker"
                            data-dateformat="dd/mm/yy"/>
                            <span class="input-group-addon"> <i
                                    class="fa fa-calendar fa-fw"></i></span>

            </div>

        </section>

        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.dose.last.date"/>
            </label>
            <br>

            <div class="input-group">
                <form:input type="text" name="fechaUltDosisAntib"
                            path="fechaUltDosisAntib"
                            class="form-control date-picker"
                            data-dateformat="dd/mm/yy"/>
                            <span class="input-group-addon"> <i
                                    class="fa fa-calendar fa-fw"></i></span>

            </div>

        </section>

        <section class="col col-2">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.dose.number"/>
            </label>
            <br>

            <label class="input"> <i
                    class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i> <form:input
                    type="text" path="noDosisAntib" name="noDosisAntib"/> <b
                    class="tooltip tooltip-bottom-right"> <i
                    class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.number.dose"/>
            </b>
            </label>

        </section>

        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.route"/>
            </label>
            <br>


            <div>
                <form:select name="codViaAntb" cssClass="select2" path="codViaAntb">
                    <option value=""><spring:message code="lbl.select"/></option>
                    <form:options items="${catVia}" itemValue="codigo" itemLabel="valor"/>

                </form:select>

            </div>


        </section>
    </div>


</fieldset>

<fieldset>

    <div class="row">
        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.use.antiviral"/>
            </label>
            <br>

            <div>
                <form:select name="usoAntivirales" cssClass="select2" path="usoAntivirales">
                    <option value=""><spring:message code="lbl.select"/></option>
                    <form:options items="${catResp}" itemValue="codigo" itemLabel="valor"/>

                </form:select>

            </div>

        </section>
    </div>

    <div class="row">

        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.antiviral.name"/>
            </label>
            <br>

            <label class="input"> <i
                    class="icon-prepend fa fa-pencil fa-fw"></i> <form:input
                    type="text" name="nombreAntiviral"
                    path="nombreAntiviral"
                    />
            </label>

        </section>

        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.dose.first.date"/>
            </label>
            <br>

            <div class="input-group">
                <form:input type="text" name="fechaPrimDosisAntiviral"
                            path="fechaPrimDosisAntiviral"
                            class="form-control date-picker"
                            data-dateformat="dd/mm/yy"/>
                            <span class="input-group-addon"> <i
                                    class="fa fa-calendar fa-fw"></i></span>

            </div>

        </section>

        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.dose.last.date"/>
            </label>
            <br>

            <div class="input-group">
                <form:input type="text" name="fechaUltDosisAntiviral"
                            path="fechaUltDosisAntiviral"
                            class="form-control date-picker"
                            data-dateformat="dd/mm/yy"/>
                            <span class="input-group-addon"> <i
                                    class="fa fa-calendar fa-fw"></i></span>

            </div>

        </section>

        <section class="col col-2">

            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.dose.number"/>
            </label>
            <br>

            <label class="input"> <i
                    class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i> <form:input
                    type="text" path="noDosisAntiviral" name="noDosisAntiviral"/> <b
                    class="tooltip tooltip-bottom-right"> <i
                    class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.number.dose"/> </b>
            </label>

        </section>
    </div>

</fieldset>

<fieldset>
    <h3>
                                <span>
                                <spring:message code="lbl.radiology.results"/>
                                </span>
    </h3>

    <br>

    <div class="row">
        <section class="col col-4">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.radiology.results"/>
            </label>
            <br>


            <div>

                <form:select name="codResRadiologia" cssClass="select2" path="codResRadiologia">
                    <option value=""><spring:message code="lbl.select"/></option>
                    <form:options items="${catResRad}" itemValue="codigo" itemLabel="valor"/>
                </form:select>
            </div>

        </section>

        <section id="sOtroResRadiologia" hidden="hidden" class="col col-3">
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
                    <th><spring:message code="lbl.test"/></th>
                    <th><spring:message code="lbl.result"/></th>
                    <th><spring:message code="lbl.laboratory"/></th>
                    <th><spring:message code="lbl.result.date"/></th>

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
            <section class="col col-6">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.uci"/>
                </label>
                <br>
            </section>
            <section class="col col-2">
                <div class="inline-group" style="padding-bottom: 8px">
                    <label class="radio"> <form:radiobutton path="uci" value="1"
                                                            name="uci"/>
                        <i></i><spring:message code="lbl.yes"/></label>
                    <label class="radio"> <form:radiobutton path="uci" value="0"
                                                            name="uci"/>
                        <i></i><spring:message code="lbl.no"/></label>
                </div>
            </section>
        </div>

        <div class="row">
            <section class="col col-3">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.numbers.days"/>
                </label>
                <br>

                <label class="input"> <i
                        class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i> <form:input
                        type="text" path="noDiasHospitalizado" name="noDiasHospitalizado"/> <b
                        class="tooltip tooltip-bottom-right"> <i
                        class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.number.days"/>
                </b>
                </label>

            </section>

            <section class="col col-3">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.assisted.ventilation"/>
                </label>

                <div class="inline-group" style="padding-bottom: 8px">
                    <label class="radio"> <form:radiobutton path="ventilacionAsistida" value="1"
                                                            name="ventilacionAsistida"/> <i></i><spring:message
                            code="lbl.yes"/></label>
                    <label class="radio"> <form:radiobutton path="ventilacionAsistida" value="0"
                                                            name="ventilacionAsistida"/> <i></i><spring:message
                            code="lbl.no"/></label>

                </div>

            </section>

            <section class="col col-6">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.egress.diagnosis1"/>
                </label>
                <br>

                <label class="input"> <i
                        class="icon-prepend fa fa-pencil fa-fw"></i> <form:input
                        type="text" name="diagnostico1Egreso"
                        path="diagnostico1Egreso"
                        />
                </label>

            </section>
        </div>

        <div class="row">

            <section class="col col-6">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.egress.diagnosis2"/>
                </label>
                <br>

                <label class="input"> <i
                        class="icon-prepend fa fa-pencil fa-fw"></i> <form:input
                        type="text" name="diagnostico2Egreso"
                        path="diagnostico2Egreso"
                        />
                </label>

            </section>

            <section class="col col-3">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.egress.date"/>
                </label>
                <br>

                <div class="input-group">
                    <form:input type="text" name="fechaEgreso"
                                path="fechaEgreso"
                                class="form-control date-picker"
                                data-dateformat="dd/mm/yy"/>
                    <span class="input-group-addon"> <i
                            class="fa fa-calendar fa-fw"></i></span>

                </div>

            </section>

            <section class="col col-3">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.egress.condition"/>
                </label>
                <br>


                <div>
                    <form:select name="codCondEgreso" cssClass="select2" path="codCondEgreso">
                        <option value=""><spring:message code="lbl.select"/></option>
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
        <div class="row">
            <section class="col col-5">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.final.case.classification"/>
                </label>
                <br>


                <div>
                    <form:select name="codClasFCaso" cssClass="select2" path="codClasFCaso">
                        <option value=""><spring:message code="lbl.select"/></option>
                        <form:options items="${catClaFinal}" itemValue="codigo" itemLabel="valor"/>
                    </form:select>
                </div>
            </section>


        </div>
        <div class="row" hidden="hidden" id="dConfNB">
            <section class="col col-6">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.bacterial.etiologic.agent"/>
                </label>
                <br>

                <label class="input"> <i
                        class="icon-prepend fa fa-pencil fa-fw"></i> <form:input
                        type="text" name="agenteBacteriano"
                        path="agenteBacteriano"
                        />
                </label>

            </section>

            <section class="col col-3">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.serotyping"/>
                </label>
                <br>

                <label class="input"> <i
                        class="icon-prepend fa fa-pencil fa-fw"></i> <form:input
                        type="text" name="serotipificacion"
                        path="serotipificacion"
                        />
                </label>

            </section>
        </div>

        <div class="row" hidden="hidden" id="dViralEti">
            <section class="col col-6">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.viral.etiologic.agent"/>
                </label>
                <br>

                <label class="input"> <i
                        class="icon-prepend fa fa-pencil fa-fw"></i> <form:input
                        type="text" name="agenteViral"
                        path="agenteViral"
                        />
                </label>

            </section>
        </div>

        <div hidden="hidden" id="dEtiAgents" class="row">
            <section class="col col-6">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.etiologic.agents"/>
                </label>
                <br>

                <label class="input"> <i
                        class="icon-prepend fa fa-pencil fa-fw"></i> <form:input
                        type="text" name="agenteEtiologico"
                        path="agenteEtiologico"
                        />
                </label>

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

                    <div class="row">
                        <section class="col col-sm-12 col-md-6 col-lg-5">

                            <input hidden="hidden" id="inVacIdIrag" type="text" name="idIrag"/>

                            <label class="text-left txt-color-blue font-md">
                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                <spring:message code="lbl.vaccine"/>
                            </label>

                            <div>
                                <form:select id="codVacuna" name="codVacuna" class="select2" path="codVacuna">
                                    <option value=""><spring:message code="lbl.select"/></option>
                                    <c:forEach items="${catVacunas}" var="vac">
                                        <form:option value="${vac.codigo}">${vac.valor}</form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </section>


                        <section class="col col-sm-12 col-md-6 col-lg-5">

                            <label class="text-left txt-color-blue font-md">
                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                <spring:message code="lbl.which"/>
                            </label>

                            <div id="dVacHib">

                                <select id="tVacHib" hidden="hidden" class="select2">
                                    <option value=""><spring:message code="lbl.select"/></option>
                                    <c:forEach items="${catTVacHib}" var="hib">
                                        <option value="${hib.codigo}">${hib.valor}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div id="dVacMenin" hidden="hidden">
                                <select id="tVacMenin" class="select2">
                                    <option value=""><spring:message code="lbl.select"/></option>
                                    <c:forEach items="${catTVacMenin}" var="menin">
                                        <option value="${menin.codigo}">${menin.valor}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div id="dVacNeumo" hidden="hidden">
                                <select id="tVacNeumo" class="select2">
                                    <option value=""><spring:message code="lbl.select"/></option>
                                    <c:forEach items="${catTVacNeumo}" var="neumo">
                                        <option value="${neumo.codigo}">${neumo.valor}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div id="dVacFlu" hidden="hidden">
                                <select id="tVacFlu" class="select2">
                                    <option value=""><spring:message code="lbl.select"/></option>
                                    <c:forEach items="${catTVacFlu}" var="flu">
                                        <option value="${flu.codigo}">${flu.valor}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </section>
                    </div>

                    <div class="row">

                        <section class="col col-sm-12 col-md-6 col-lg-5">
                            <label class="text-left txt-color-blue font-md">
                                <spring:message code="lbl.dose.number"/>
                            </label>

                            <label style="width: 100px" class="input"> <i
                                    class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i> <form:input
                                    type="text" name="dosis" id="dosis"
                                    path="dosis"
                                    />
                            </label>


                        </section>

                        <section class="col col-sm-12 col-md-6 col-lg-5">
                            <label class="text-left txt-color-blue font-md">
                                <spring:message code="lbl.dose.last.date"/>
                            </label>

                            <div class="input-group">
                                <form:input id="fechaUltimaDosis" cssStyle="z-index: 1065" type="text"
                                            name="fechaUltimaDosis"
                                            path="fechaUltimaDosis"
                                            class="form-control date-picker"
                                        />
                    <span class="input-group-addon"> <i
                            class="fa fa-calendar fa-fw"></i></span>

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

<!-- Modal -->
<div class="modal fade" id="modalCondPre" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="background-color:#3276b1 ">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title txt-color-white"><spring:message code="lbl.new.preexisting.condition"/></h4>
            </div>

            <div class="modal-body">
                <form:form id="fCondPre" modelAttribute="fCondPre" class="smart-form">

                    <div class="row">
                        <input hidden="hidden" id="inCondIdIrag" type="text" name="idIrag"/>

                        <div class="col col-sm-12 col-md-6 col-lg-6">
                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                            <label class="text-left txt-color-blue font-md">
                                <spring:message code="lbl.preexisting.condition"/>
                            </label>

                            <div>
                                <form:select id="codCondicion" name="codCondicion" class="select2" path="codCondicion">
                                    <option value=""><spring:message code="lbl.select"/></option>
                                    <c:forEach items="${catCondPre}" var="cond">
                                        <form:option value="${cond.codigo}">${cond.valor}</form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>


                        <div id="dSemEmb" hidden="hidden" class="col col-sm-12 col-md-6 col-lg-6">
                            <label class="text-left txt-color-blue font-md">
                                <spring:message code="lbl.weeks.pregnancy"/>
                            </label>


                            <label style="width: 100px" class="input"> <i
                                    class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i> <form:input
                                    type="text" name="semanasEmbarazo" id="semanasEmbarazo"
                                    path="semanasEmbarazo"
                                    />
                            </label>


                        </div>

                        <div id="dOtraCondicion" hidden="hidden" class="col col-sm-12 col-md-6 col-lg-6">
                            <label class="text-left txt-color-blue font-md">
                                <spring:message code="lbl.other.preexisting.condition"/>
                            </label>

                            <label class="input"> <i
                                    class="icon-prepend fa fa-sort-alpha-asc fa-fw"></i> <form:input
                                    type="text" name="otraCondicion" id="otraCondicion"
                                    path="otraCondicion"
                                    />
                            </label>


                        </div>

                    </div>
                    <br>

                </form:form>
            </div>

            <div class="modal-footer">
                <button id="btnCancelAddCP" type="button" class="btn btn-default" data-dismiss="modal">
                    <spring:message code="act.cancel"/>
                </button>
                <button id="btnSaveCondPre" type="submit" class="btn btn-primary">
                    <spring:message code="act.save"/>
                </button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<!-- Modal Manifestaciones clinicas -->
<div class="modal fade" id="modalCM" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="background-color:#3276b1 ">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title txt-color-white"><spring:message code="lbl.new.clinical.manifestation"/></h4>
            </div>

            <div class="modal-body">
                <form:form id="fCM" modelAttribute="fCM" class="smart-form">

                    <div class="row">

                        <input hidden="hidden" id="inManifIdIrag" type="text" name="idIrag"/>

                        <div class="col col-sm-12 col-md-6 col-lg-6">
                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                            <label class="text-left txt-color-blue font-md">
                                <spring:message code="lbl.clinical.manifestation"/>
                            </label>

                            <div>
                                <form:select id="codManifestacion" name="codManifestacion" class="select2"
                                             path="codManifestacion">
                                    <option value=""><spring:message code="lbl.select"/></option>
                                    <c:forEach items="${catManCli}" var="mani">
                                        <form:option value="${mani.codigo}">${mani.valor}</form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>


                        <div id="dMani" hidden="hidden" class="col col-sm-12 col-md-6 col-lg-6">
                            <label class="text-left txt-color-blue font-md">
                                <spring:message code="lbl.other.clinical.manifestation"/>
                            </label>


                            <label class="input"> <i
                                    class="icon-prepend fa fa-sort-alpha-asc fa-fw"></i> <form:input
                                    type="text" name="otraManifestacion" id="otraManifestacion"
                                    path="otraManifestacion"
                                    />
                            </label>

                        </div>

                    </div>


                </form:form>
            </div>

            <div class="modal-footer">
                <button id="btnCancelCM" type="button" class="btn btn-default" data-dismiss="modal">
                    <spring:message code="act.cancel"/>
                </button>
                <button id="btnSaveCM" type="submit" class="btn btn-primary">
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
<input type="hidden" id="error"  value="${error}" >
<input type="hidden" id="disappear"  value="<spring:message code="lbl.messagebox.disappear"/>"/>
<input type="hidden" id="msjError"  value="<spring:message code="lbl.messagebox.error.completing"/>"/>
<input type="hidden" id="msjErrorSaving"  value="<spring:message code="lbl.messagebox.error.saving"/>"/>
<input type="hidden" id="msjSuccessful"  value="<spring:message code="lbl.messagebox.successful.saved"/>"/>

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
<spring:url value="/resources/js/plugin/bootstrap-datepicker/bootstrap-datepicker.js" var="datepickerPlugin" />
<script src = "${datepickerPlugin}" ></script>

<!-- JQUERY BLOCK UI -->
<spring:url value="/resources/js/plugin/jquery-blockui/jquery.blockUI.js" var="jqueryBlockUi"/>
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

<spring:url var="municipiosURL" value="/api/v1/municipiosbysilais"/>
<spring:url value="/irag/saveIrag" var="sAddIragUrl"/>
<spring:url value="/api/v1/unidadesPrimHosp" var="unidadesUrl"/>
<spring:url value="/irag/newVaccine" var="sAddVaccineUrl"/>
<spring:url value="/irag/vaccines" var="sLoadVaccinesUrl"/>
<spring:url value="/irag/conditions" var="conditionsUrl"/>
<spring:url value="/irag/newPreCondition" var="addConditionUrl"/>
<spring:url value="/irag/manifestations" var="loadManifestationsUrl"/>
<spring:url value="/irag/newCM" var="addManifestationUrl"/>
<spring:url value="/api/v1/municipio" var="municipioByDepaUrl"/>
<spring:url value="/api/v1/comunidad" var="comunidadUrl"/>
<spring:url value="/irag/updatePerson" var="updatePersonUrl"/>
<spring:url value="/irag/completeIrag" var="completeIragUrl"/>
<spring:url value="/irag/search" var="searchIragUrl"/>
<spring:url value="/irag/overrideVaccine" var="overrideVaccineUrl"/>
<spring:url value="/irag/overrideCondition" var="overrideConditionUrl"/>
<spring:url value="/irag/overrideManifestation" var="overrideManifestationUrl"/>

<script type="text/javascript">

    $(document).ready(function () {
        pageSetUp();


        var parametros = {sAddIragUrl: "${sAddIragUrl}",
            unidadesUrl: "${unidadesUrl}",
            dToday: "${today}",
            sAddVaccineUrl: "${sAddVaccineUrl}",
            vaccines: "${sLoadVaccinesUrl}",
            conditions: "${conditionsUrl}",
            addCondition: "${addConditionUrl}",
            manifestations: "${loadManifestationsUrl}",
            addManifestation: "${addManifestationUrl}",
            municipiosUrl: "${municipiosURL}",
            municipioByDepaUrl: "${municipioByDepaUrl}",
            comunidadUrl: "${comunidadUrl}",
            updatePersonUrl: "${updatePersonUrl}",
            completeIragUrl: "${completeIragUrl}",
            searchIragUrl: "${searchIragUrl}",
            overrideVaccineUrl: "${overrideVaccineUrl}",
            overrideConditionUrl: "${overrideConditionUrl}",
            overrideManifestationUrl: "${overrideManifestationUrl}"


        };
        <%--console.log( "${today}");--%>
        CreateIrag.init(parametros);
        handleDatePickers("${pageContext.request.locale.language}");
        //asignar departamento segun municipio
        $("#departamento").val("${departamentoProce.codigoNacional}").change();

        if ($('#codUnidadAtencion').val() != "") {
            $("#codMunicipio").val("${municipio.codigoNacional}").change();
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