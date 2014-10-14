<%--
  Created by IntelliJ IDEA.
  User: souyen-ics
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <jsp:include page="../fragments/headTag.jsp" />
    <style>
        .select2-hidden-accessible {
            display: none !important;
            visibility: hidden !important;
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
				<span id="refresh" class="btn btn-ribbon" data-action="resetWidgets(fff)" data-placement="bottom" data-original-title="<i class='text-warning fa fa-warning'></i> <spring:message code="msg.reset" />" data-html="true">
					<i class="fa fa-refresh"></i>
				</span>
			</span>
        <!-- breadcrumb -->
        <ol class="breadcrumb">
            <li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a> <i class="fa fa-angle-right"></i> <a href="<spring:url value="#" htmlEscape="true "/>"><spring:message code="lbl.breadcrumb" /></a></li>
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
            <div class="col-xs-12 col-sm-7 col-md-7 col-lg-4">
                <h2 class="page-title txt-color-blueDark">
                    <!-- PAGE HEADER -->
                    <i class="fa-fw fa fa-home"></i>
                    <spring:message code="lbl.form.irag" />
						<span> <i class="fa fa-angle-right"></i>
							<spring:message code="lbl.register" />
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
                        <h5> <spring:message code="sp.day" /> <span class="txt-color-greenDark"><i class="fa fa-arrow-circle-down"></i>17</span></h5>
                        <div class="sparkline txt-color-blue hidden-mobile hidden-md hidden-sm">
                            0,1,3,4,11,12,11,13,10,11,15,14,20,17
                        </div>
                    </li>
                    <li class="sparks-info">
                        <h5> <spring:message code="sp.week" /> <span class="txt-color-red"><i class="fa fa-arrow-circle-up"></i>&nbsp;57</span></h5>
                        <div class="sparkline txt-color-purple hidden-mobile hidden-md hidden-sm">
                            23,32,11,23,33,45,44,54,45,48,57
                        </div>
                    </li>
                    <li class="sparks-info">
                        <h5> <spring:message code="sp.month" /> <span class="txt-color-red"><i class="fa fa-arrow-circle-up"></i>&nbsp;783</span></h5>
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
                    <div class="jarviswidget" id="wid-id-0">
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
                            <span class="widget-icon"> <i class="fa fa-comments"></i> </span>
                            <h2><spring:message code="lbl.irag" /> </h2>

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

                                <!-- this is what the user will see -->
                                <form:form modelAttribute="formVI" id="wizard-1" class="smart-form">
                                <div id="bootstrap-wizard-1" class="col-lg-12">

                                <div class="wizard">
                                    <ul class="steps form-wizard">
                                        <li class="active" data-target="#step1"><a href="#tab1" data-toggle="tab"> <span
                                                class="badge badge-info">1</span><spring:message code="lbl.general.data" /><span class="chevron"></span></a></li>

                                        <li data-target="#step2"><a href="#tab2" data-toggle="tab"><span class="badge">2</span> <spring:message code="lbl.patient.data" /> <span
                                                class="chevron"></span></a>
                                        </li>
                                        <li data-target="#step3"><a href="#tab3" data-toggle="tab"><span class="badge">3</span> <spring:message code="lbl.clinical.data" /> <span
                                                class="chevron"></span></a>
                                        </li>
                                        <li data-target="#step4"><a href="#tab4" data-toggle="tab"><span class="badge">4</span> <spring:message code="lbl.radiology.results" />   <span class="chevron"></span></a>
                                        </li>
                                        <li data-target="#step5"><a href="#tab5" data-toggle="tab"><span class="badge">5</span> <spring:message code="lbl.laboratory.data" /> <span
                                                class="chevron"></span></a>
                                        </li>
                                        <li data-target="#step6"><a href="#tab6" data-toggle="tab"><span class="badge">6</span> <spring:message code="lbl.patient.evolution" /> <span
                                                class="chevron"></span></a>
                                        </li>
                                        <li data-target="#step7"><a href="#tab7" data-toggle="tab"><span class="badge">7</span> <spring:message code="lbl.final.case.classification" /> <span
                                                class="chevron"></span></a>
                                        </li>

                                    </ul>

                                    <div class="clearfix"></div>
                                </div>
                                <div class="tab-content">
                                <div class="tab-pane active" id="tab1">
                                    <br>
                                    <h3>
                            <span>
                                <spring:message code="lbl.general.data" />
                            </span>

                                    </h3>
                                  <fieldset>
                                       <div class="row">
                                            <section class="col col-6">
                                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.silais" />

                                                </label>

                                                <div class="input-group">
																<span class="input-group-addon"> <i
                                                                        class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                    <form:select
                                                            path="codSilaisAtencion" id="codSilaisAtencion" name="codSilaisAtencion"
                                                            class="select2">
                                                        <option value="">Seleccione...</option>
                                                        <c:forEach items="${entidades}" var="entidad">
                                                            <option value="${entidad.codigo}">${entidad.nombre}</option>
                                                        </c:forEach>
                                                    </form:select>
                                                </div>
                                            </section>

                                         <section class="col col-6">
                                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.health.unit" />
                                                </label>

                                                <div class="input-group">
																<span class="input-group-addon"> <i
                                                                        class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                    <form:select id="codUnidadAtencion" name="codUnidadAtencion"
                                                                 path="codUnidadAtencion"
                                                                 class="select2">
                                                        <option value="">Seleccione...</option>

                                                    </form:select>
                                                </div>
                                            </section>

                                        </div>

                                        <div class="row">
                                            <section class="col col-3">
                                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.consultation.date" />
                                                </label>

                                                <div class="input-group">
                                                    <form:input type="text" id="fechaConsulta" name="fechaConsulta" path="fechaConsulta"

                                                                class="form-control datepicker"
                                                                data-dateformat="dd/mm/yy"/>
                    <span class="input-group-addon"> <i
                            class="fa fa-calendar fa-fw"></i></span>

                                                </div>
                                            </section>

                                            <section class="col col-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.first.consultation.date" />
                                                </label>

                                                <div class="input-group">
                                                    <form:input id="fechaPrimeraConsulta" path="fechaPrimeraConsulta" type="text" name="fechaPrimeraConsulta"

                                                                class="form-control datepicker"
                                                                data-dateformat="dd/mm/yy"/>
                    <span class="input-group-addon"> <i
                            class="fa fa-calendar fa-fw"></i></span>

                                                </div>
                                            </section>

                                            <section class="col col-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.file.number" />
                                                </label>
                                                <label class="input"> <i
                                                        class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i> <form:input
                                                        type="text" path="codExpediente" name="codExpediente" id="noExp"
                                                       />
                                                </label>
                                            </section>

                                            <section class="col col-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.classification" />
                                                </label>

                                                <form:select id="codClasificacion"
                                                             path="codClasificacion" name="codClasificacion"
                                                             class="select2">
                                                    <option value="">Seleccione...</option>
                                                    <c:forEach items="${catClasif}" var="clas">
                                                        <option value="${clas.codigo}">${clas.valor}</option>
                                                    </c:forEach>
                                                </form:select>

                                            </section>

                                        </div>

                                    </fieldset>
                                </div>
                                <div class="tab-pane" id="tab2">
                                <br>
                                <h3>
                    <span>
                        <spring:message code="lbl.patient.data" />
                    </span>
                                </h3>

                             <%--   <fieldset>
                                    <div class="row">
                                        <section class="col col-6">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.names" />
                                            </label>
                                            <label class="input"> <i
                                                    class="icon-prepend fa fa-pencil fa-fw"></i> <form:input readonly="true" cssStyle="background-color: #f0fff0"
                                                                                                             type="text" name="nombreCompleto"
                                                                                                             path="nombreCompleto"/>
                                            </label>
                                        </section>

                                        <section class="col col-2">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.gender" />
                                            </label>

                                            <div>
                                                <form:select cssStyle="background-color: #f0fff0" disabled="true" name="codigoSexo" class="select2" path="codigoSexo"
                                                             id="codigoSexo">
                                                    <option value="">Seleccione...</option>
                                                    <c:forEach items="${catGenero}" var="genero">
                                                        <form:option value="${genero.codigo}">${genero.valor}</form:option>
                                                    </c:forEach>
                                                </form:select>
                                            </div>
                                        </section>


                                        <section class="col col-2">

                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.birthdate" />

                                            </label>

                                            <div class="input-group">
                                                <form:input readonly="true" cssStyle="background-color: #f0fff0" type="text" name="fechaNacimiento" id="fechaNacimiento"
                                                            path="fechaNacimiento"
                                                            class="form-control datepicker"
                                                            data-dateformat="dd/mm/yy"/> <span
                                                    class="input-group-addon"> <i
                                                    class="fa fa-calendar fa-fw"></i>
                                                                     </span>
                                            </div>
                                        </section>

                                        <section class="col col-2">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.age" />
                                            </label>
                                            <label class="input"> <i
                                                    class="icon-prepend fa fa-credit-card fa-fw"></i> <input readonly="" style="background-color: #f0fff0"
                                                                                                             type="text" name="edadAnios" id="edadAnios">
                                            </label>
                                        </section>
                                    </div>

                                    <div class="row">

                                        <section class="col col-6">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.provenance.city" />
                                            </label>

                                            <div class="input">
                                                <i class="icon-prepend fa fa-location-arrow fa-fw"></i>
                                                <form:input readonly="true" cssStyle="background-color: #f0fff0" type="text" path="departamentoProcedencia" name="departamentoProcedencia"
                                                            class="form-control" text="Managua"/>
                                            </div>
                                        </section>

                                        <section class="col col-6">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.town" />
                                            </label>

                                            <div class="input">
                                                <i class="icon-prepend fa fa-location-arrow fa-fw"></i>
                                                <form:input readonly="true" cssStyle="background-color: #f0fff0" type="text" path="codMunicipioProcedencia" name="municipioProcedencia"
                                                            class="form-control"/>
                                            </div>
                                        </section>
                                    </div>

                                    <div class="row">
                                        <section class="col col-6">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.address" />
                                            </label>

                                            <div class="input">
                                                <i class="icon-prepend fa fa-location-arrow fa-fw"></i>
                                                <form:input readonly="true" cssStyle="background-color: #f0fff0" type="text" path="direccionResidencia" name="direccionResidencia"
                                                            class="form-control"/>
                                            </div>
                                        </section>

                                        <section class="col col-3">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.phone" />
                                            </label>

                                            <div class="input">
                                                <i class="icon-prepend fa fa-location-arrow fa-fw"></i>
                                                <form:input readonly="true" cssStyle="background-color: #f0fff0" type="text" path="telefono" name="telefono"
                                                            class="form-control"/>
                                            </div>
                                        </section>


                                    </div>
                                </fieldset>--%>
                               <fieldset>
                                    <div class="row">
                                        <section class="col col-6">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.mother.father" />
                                            </label>
                                            <label class="input"> <i
                                                    class="icon-prepend fa fa-pencil fa-fw"></i> <form:input
                                                    type="text" path="nombreMadreTutor" name="nombreMadreTutor"
                                                   />
                                                <b class="tooltip tooltip-bottom-right"> <i
                                                        class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.mother" />
                                                </b>
                                            </label>
                                        </section>

                                        <section class="col col-3">
                                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.provenance" />
                                            </label>

                                            <div>
                                                <form:select name="codProcedencia" class="select2" path="codProcedencia">
                                                    <option value="">Seleccione...</option>
                                                    <c:forEach items="${catProcedencia}" var="proc">
                                                        <form:option value="${proc.codigo}">${proc.valor}</form:option>
                                                    </c:forEach>
                                                </form:select>
                                            </div>
                                        </section>
                                    </div>



                                    <div class="row">
                                        <section class="col col-6">
                                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.diagnosis" />
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
                                                <spring:message code="lbl.capture" />
                                            </label>

                                            <div>

                                                <form:select name="codCaptacion" class="select2" path="codCaptacion">
                                                    <option value="">Seleccione...</option>
                                                    <c:forEach items="${catCaptac}" var="capta">
                                                        <form:option value="${capta.codigo}">${capta.valor}</form:option>
                                                    </c:forEach>
                                                </form:select>
                                            </div>
                                        </section>
                                    </div>
                                </fieldset>

                                <fieldset>
                                    <legend class="text-left txt-color-blue font-md">
                                        <spring:message code="lbl.vaccination.history" />
                                    </legend>
                                    <div class="row">
                                        <section class="col col-4">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.presents.vaccination.card" />
                                            </label>
                                            <br>
                                        </section>
                                        <section class="col col-2">

                                            <div class="inline-group" style="padding-bottom: 8px">
                                                <label class="radio"> <input type="radio" value="1" name="tarjetaVacuna"> <i></i> <spring:message code="lbl.yes" />  </label>
                                                <label class="radio"> <input type="radio" value="0" name="tarjetaVacuna"> <i></i> <spring:message code="lbl.no" /> </label>

                                            </div>


                                        </section>

                                        <section id="sAddVaccine" hidden="hidden" class="col col-2">
                                            <button type="button" id="btnAddVaccine" class="btn btn-primary btn-" data-toggle="modal" data-target="#myModal">
                                                <spring:message code="act.add.vaccine" />
                                            </button>
                                        </section>
                                    </div>


                                    <div id="dVaccinesTable" hidden="hidden">

                                        <!-- widget edit box -->
                                        <div class="jarviswidget-editbox">
                                            <!-- This area used as dropdown edit box -->

                                        </div>
                                        <!-- end widget edit box -->

                                        <!-- widget content -->
                                        <div class="widget-body no-padding">


                                            <table  id="lista_vacunas"
                                                    class="table table-striped table-bordered smart-form">
                                                <tbody>

                                                </tbody>

                                            </table>
                                        </div>

                                    </div>

                                </fieldset>


                                <fieldset>

                                    <legend class="text-left txt-color-blue font-md">
                                        <spring:message code="lbl.preexisting.conditions"/>

                                    </legend>

                                    <diV class="row">
                                        <section class="col col-2">
                                            <button type="button" class="btn btn-primary btn-" data-toggle="modal" data-target="#modalCondPre">
                                                <spring:message code="act.add.preexisting.conditions" />
                                            </button>
                                        </section>
                                    </diV>


                                    <div >

                                        <!-- widget edit box -->
                                        <div class="jarviswidget-editbox">
                                            <!-- This area used as dropdown edit box -->

                                        </div>
                                        <!-- end widget edit box -->

                                        <!-- widget content -->
                                        <div class="widget-body no-padding">


                                            <table  id="lista_condiciones"
                                                    class="table table-striped table-bordered smart-form">
                                                <tbody>

                                                </tbody>

                                            </table>
                                        </div>

                                    </div>
                                </fieldset>

                                </div>
                                <div class="tab-pane" id="tab3">
                                <br>
                                <h3>
                        <span>
                            <spring:message code="lbl.clinical.data" />
                        </span>
                                </h3>
                                       <fieldset>
                                            <div class="row">
                                                <section class="col col-3">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <spring:message code="lbl.symptoms.start.date" />
                                                    </label>

                                                    <div class="input-group">
                                                        <form:input type="text" name="fechaInicioSintomas"
                                                                    path="fechaInicioSintomas"
                                                                    class="form-control datepicker"
                                                                    data-dateformat="dd/mm/yy"/>
                            <span class="input-group-addon"> <i
                                    class="fa fa-calendar fa-fw"></i></span>

                                                    </div>
                                                </section>
                                            </div>
                                        </fieldset>
                                        <fieldset>
                                            <div class="row">
                                                <section class="col col-2">
                                                    <button type="button" class="btn btn-primary btn-" data-toggle="modal" data-target="#modalCM">
                                                        <spring:message code="act.add.clinical.manifestation" />
                                                    </button>
                                                </section>
                                            </div>


                                            <div >

                                                <!-- widget edit box -->
                                                <div class="jarviswidget-editbox">
                                                    <!-- This area used as dropdown edit box -->

                                                </div>
                                                <!-- end widget edit box -->

                                                <!-- widget content -->
                                                <div class="widget-body no-padding">


                                                    <table  id="lista_CM"
                                                            class="table table-striped table-bordered smart-form">
                                                        <tbody>

                                                        </tbody>

                                                    </table>
                                                </div>

                                            </div>

                                        </fieldset>
                                        <fieldset>

                                            <div class="row">
                                                <section class="col col-5">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <spring:message code="lbl.use.antibiotics" />
                                                    </label>
                                                    <br>


                                                    <div>

                                                        <form:select name="codAntbUlSem" class="select2" path="codAntbUlSem">
                                                            <option value="">Seleccione...</option>
                                                            <c:forEach items="${catResp}" var="resp">
                                                                <form:option value="${resp.codigo}">${resp.valor}</form:option>
                                                            </c:forEach>
                                                        </form:select>
                                                    </div>


                                                </section>
                                            </div>

                                            <div class="row">
                                                <section class="col col-4">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <spring:message code="lbl.antibiotics.amount" />
                                                    </label>
                                                    <br>

                                                    <label class="input"> <i
                                                            class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i> <form:input
                                                            type="text" path="cantidadAntib" name="cantidadAntib"/> <b
                                                            class="tooltip tooltip-bottom-right"> <i
                                                            class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.antibiotics.amount" />
                                                    </b>
                                                    </label>

                                                </section>

                                                <section class="col col-5">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <spring:message code="lbl.antibiotics.names" />
                                                    </label>
                                                    <br>

                                                    <label class="input"> <i
                                                            class="icon-prepend fa fa-pencil fa-fw"></i> <form:input
                                                            type="text" name="nombreAntibiotico"
                                                            path="nombreAntibiotico"
                                                           />
                                                    </label>

                                                </section>
                                            </div>

                                            <div class="row">
                                                <section class="col col-3">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <spring:message code="lbl.dose.first.date" />
                                                    </label>
                                                    <br>

                                                    <div class="input-group">
                                                        <form:input type="text" name="fechaPrimDosisAntib"
                                                                    path="fechaPrimDosisAntib"
                                                                    class="form-control datepicker"
                                                                    data-dateformat="dd/mm/yy"/>
                            <span class="input-group-addon"> <i
                                    class="fa fa-calendar fa-fw"></i></span>

                                                    </div>

                                                </section>

                                                <section class="col col-3">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <spring:message code="lbl.dose.last.date" />
                                                    </label>
                                                    <br>

                                                    <div class="input-group">
                                                        <form:input type="text" name="fechaUltDosisAntib"
                                                                    path="fechaUltDosisAntib"
                                                                    class="form-control datepicker"
                                                                    data-dateformat="dd/mm/yy"/>
                            <span class="input-group-addon"> <i
                                    class="fa fa-calendar fa-fw"></i></span>

                                                    </div>

                                                </section>

                                                <section class="col col-2">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <spring:message code="lbl.dose.number" />
                                                    </label>
                                                    <br>

                                                    <label class="input"> <i
                                                            class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i> <form:input
                                                            type="text" path="noDosisAntib" name="noDosisAntib"/> <b
                                                            class="tooltip tooltip-bottom-right"> <i
                                                            class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.number.dose" />
                                                    </b>
                                                    </label>

                                                </section>

                                                <section class="col col-3">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <spring:message code="lbl.route" />
                                                    </label>
                                                    <br>


                                                    <div>
                                                        <form:select name="codViaAntb" class="select2" path="codViaAntb">
                                                            <option value="">Seleccione...</option>
                                                            <c:forEach items="${catVia}" var="via">
                                                                <form:option value="${via.codigo}">${via.valor}</form:option>
                                                            </c:forEach>
                                                        </form:select>
                                                    </div>


                                                </section>
                                            </div>



                                        </fieldset>

                                        <fieldset>

                                            <div class="row">
                                                <section class="col col-5">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <spring:message code="lbl.use.antiviral" />
                                                    </label>
                                                    <br>

                                                    <div class="inline-group" style="padding-bottom: 8px">
                                                        <label class="radio"> <input type="radio" value="1" name="usoAntivirales"> <i></i>Si </label>
                                                        <label class="radio"> <input type="radio" value="0" name="usoAntivirales"> <i></i>No </label>

                                                    </div>
                                                </section>
                                            </div>

                                            <div class="row">

                                                <section class="col col-3">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <spring:message code="lbl.antiviral.name" />
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
                                                        <spring:message code="lbl.dose.first.date" />
                                                    </label>
                                                    <br>

                                                    <div class="input-group">
                                                        <form:input type="text" name="fechaPrimDosisAntiviral"
                                                                     path="fechaPrimDosisAntiviral"
                                                                    class="form-control datepicker"
                                                                    data-dateformat="dd/mm/yy"/>
                            <span class="input-group-addon"> <i
                                    class="fa fa-calendar fa-fw"></i></span>

                                                    </div>

                                                </section>

                                                <section class="col col-3">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <spring:message code="lbl.dose.last.date" />
                                                    </label>
                                                    <br>

                                                    <div class="input-group">
                                                        <form:input type="text" name="fechaUltDosisAntiviral"
                                                                    path="fechaUltDosisAntiviral"
                                                                    class="form-control datepicker"
                                                                    data-dateformat="dd/mm/yy"/>
                            <span class="input-group-addon"> <i
                                    class="fa fa-calendar fa-fw"></i></span>

                                                    </div>

                                                </section>

                                                <section class="col col-3">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <spring:message code="lbl.dose.number" />
                                                    </label>
                                                    <br>

                                                    <label class="input"> <i
                                                            class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i> <form:input
                                                            type="text" path="noDosisAntiviral" name="noDosisAntiviral"/> <b
                                                            class="tooltip tooltip-bottom-right"> <i
                                                            class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.number.dose" />                                </b>
                                                    </label>

                                                </section>
                                            </div>

                                        </fieldset>

                                </div>
                                <div class="tab-pane" id="tab4">
                                    <br>
                                    <h3>
                            <span>
                                <spring:message code="lbl.radiology.results" />
                            </span>

                                    </h3>
                                    <br>

                                   <fieldset>
                                        <div class="row">
                                            <section class="col col-6">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.radiology.results" />
                                                </label>
                                                <br>


                                                <div>
                                                    <form:select name="codResRadiologia" class="select2" path="codResRadiologia">
                                                        <option value="">Seleccione...</option>
                                                        <c:forEach items="${catResRad}" var="resRad">
                                                            <form:option value="${resRad.codigo}">${resRad.valor}</form:option>
                                                        </c:forEach>
                                                    </form:select>
                                                </div>

                                            </section>

                                            <section class="col col-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.description.other" />
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

                                <div class="tab-pane" id="tab5">
                                    <br>
                                    <h3>
                            <span>

                                <spring:message code="lbl.laboratory.data" />
                            </span>
                                    </h3>

                                    <fieldset>
                                        <div class="table-responsive">
                                            <table class="table table-striped table-hover table-bordered" id="lista_resultados">
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="lbl.test" /></th>
                                                    <th><spring:message code="lbl.result" /></th
                                                    <th><spring:message code="lbl.laboratory" /></th>
                                                    <th><spring:message code="lbl.result.date" /></th>

                                                </tr>
                                                </thead>

                                            </table>
                                        </div>
                                    </fieldset>

                                </div>

                                <div class="tab-pane" id="tab6">
                                    <br>
                                    <h3>
                            <span>
                                <spring:message code="lbl.patient.evolution" />
                            </span>

                                    </h3>
                                    <fieldset>
                                        <div class="row">
                                            <section class="col col-6">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.uci" />
                                                </label>
                                                <br>
                                            </section>
                                            <section class="col col-2">
                                                <div class="inline-group" style="padding-bottom: 8px">
                                                    <label class="radio"> <input type="radio" value="1" name="uci"> <i></i>  <spring:message code="lbl.yes" /> </label>
                                                    <label class="radio"> <input type="radio" value="0" name="uci"> <i></i> <spring:message code="lbl.no" /> </label>

                                                </div>
                                            </section>
                                        </div>

                                        <div class="row">
                                            <section class="col col-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.numbers.days" />
                                                </label>
                                                <br>

                                                <label class="input"> <i
                                                        class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i> <form:input
                                                        type="text" path="noDiasHospitalizado" name="noDiasHospitalizado"/> <b
                                                        class="tooltip tooltip-bottom-right"> <i
                                                        class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.number.days" />
                                                </b>
                                                </label>

                                            </section>

                                            <section class="col col-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.assisted.ventilation" />
                                                </label>

                                                <div class="inline-group" style="padding-bottom: 8px">
                                                    <label class="radio"> <input type="radio" value="1" name="ventilacionAsistida"> <i></i>Si </label>
                                                    <label class="radio"> <input type="radio" value="0" name="ventilacionAsistida"> <i></i>No </label>

                                                </div>

                                            </section>

                                            <section class="col col-6">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.egress.diagnosis1" />
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
                                                    <spring:message code="lbl.egress.diagnosis2" />
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
                                                    <spring:message code="lbl.egress.date" />
                                                </label>
                                                <br>

                                                <div class="input-group">
                                                    <form:input type="text" name="fechaEgreso"
                                                                path="fechaEgreso"
                                                                class="form-control datepicker"
                                                                data-dateformat="dd/mm/yy"/>
                    <span class="input-group-addon"> <i
                            class="fa fa-calendar fa-fw"></i></span>

                                                </div>

                                            </section>

                                            <section class="col col-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.egress.condition" />
                                                </label>
                                                <br>


                                                <div>
                                                    <form:select name="codCondEgreso" class="select2" path="codCondEgreso">
                                                        <option value="">Seleccione...</option>
                                                        <c:forEach items="${catConEgreso}" var="egre">
                                                            <form:option value="${egre.codigo}">${egre.valor}</form:option>
                                                        </c:forEach>
                                                    </form:select>
                                                </div>

                                            </section>
                                        </div>


                                    </fieldset>

                                </div>

                                <div class="tab-pane" id="tab7">
                                    <br>
                                    <h3>
                            <span>
                                <spring:message code="lbl.final.case.classification" />
                            </span>
                                    </h3>

                                   <fieldset>
                                        <div class="row">
                                            <section class="col col-5">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.final.case.classification" />
                                                </label>
                                                <br>


                                                <div >
                                                    <form:select name="codClasFCaso" class="select2" path="codClasFCaso">
                                                        <option value="">Seleccione...</option>
                                                        <c:forEach items="${catClaFinal}" var="claF">
                                                            <form:option value="${claF.codigo}">${claF.valor}</form:option>
                                                        </c:forEach>
                                                    </form:select>
                                                </div>

                                            </section>


                                        </div>
                                        <div class="row">
                                            <section class="col col-6">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.bacterial.etiologic.agent" />
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
                                                    <spring:message code="lbl.serotyping" />
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

                                        <div class="row">
                                            <section class="col col-6">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.viral.etiologic.agent" />
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

                                        <div class="row">
                                            <section class="col col-6">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.etiologic.agents" />
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

                                <ul class="pager wizard">
                                    <li class="previous"><a href="javascript:;"> <spring:message code="lbl.previous" /> </a></li>
                                    <li class="next"><a href="javascript:;"><spring:message code="lbl.next" /> </a></li>
                                    <li class="next finish" style="display:none;"><a href="javascript:;"><spring:message code="lbl.finalize" /> </a></li>
                                </ul>
                                </div>

                                </div>
                                </form:form>

                            </div>
                            <!-- end widget content -->
                        </div>
                        <!-- end widget div -->
                    </div>
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
</div>



<!-- END MAIN PANEL -->
<!-- BEGIN FOOTER -->
<jsp:include page="../fragments/footer.jsp" />
<!-- END FOOTER -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<jsp:include page="../fragments/corePlugins.jsp" />
<!-- BEGIN PAGE LEVEL PLUGINS -->
<!-- Bootstrap Wizard -->
<spring:url value="/resources/js/plugin/bootstrap-wizard/jquery.bootstrap.wizard.min.js" var="jqueryBootstrap" />
<script src="${jqueryBootstrap}"></script>

<!-- JQUERY VALIDATE -->
<spring:url value="/resources/js/plugin/jquery-validate/jquery.validate.min.js" var="validate" />
<script src="${validate}"></script>
<spring:url value="/resources/js/plugin/jquery-validate/messages_{language}.js" var="jQValidationLoc">
<spring:param name="language" value="${pageContext.request.locale.language}" /></spring:url>
<script src="${jQValidationLoc}"/></script>

<!-- jQuery Validate datepicker -->
<spring:url value="/resources/js/plugin/jquery-validate-datepicker/jquery.ui.datepicker.validation.min.js" var="jQueryValidateDatepicker"/>
<script src="${jQueryValidateDatepicker}" ></script>
<!-- Data table -->
<spring:url value="/resources/js/plugin/datatables/jquery.dataTables.min.js" var="dataTables" />
<script src="${dataTables}"></script>
<spring:url value="/resources/js/plugin/datatables/dataTables.colVis.min.js" var="dataTablesColVis" />
<script src="${dataTablesColVis}"></script>
<spring:url value="/resources/js/plugin/datatables/dataTables.tableTools.min.js" var="dataTablesTableTools" />
<script src="${dataTablesTableTools}"></script>
<spring:url value="/resources/js/plugin/datatables/dataTables.bootstrap.min.js" var="dataTablesBootstrap" />
<script src="${dataTablesBootstrap}"></script>
<spring:url value="/resources/js/plugin/datatable-responsive/datatables.responsive.min.js" var="dataTablesResponsive" />
<script src="${dataTablesResponsive}"></script>

<!-- jQuery Selecte2 Input -->
<spring:url value="/resources/js/plugin/select2/select2.min.js" var="selectPlugin"/>
<script src="${selectPlugin}"></script>

<!-- END PAGE LEVEL SCRIPTS -->

<!-- BEGIN PAGE LEVEL PLUGINS -->
<spring:url value="/resources/scripts/irag/irag-create.js" var="iragCreate" />
<script src="${iragCreate}"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>
    $(function () {
        $("li.irageti").addClass("active");
    });
</script>
<spring:url value="/irag/saveIrag" var="sAddIragUrl"/>
<spring:url value="/api/v1/unidades" var="unidadesURL" />
<script type="text/javascript">

    $(document).ready(function() {
        pageSetUp();

        var parametros = {sAddIragUrl: "${sAddIragUrl}",
                          sUnidadesUrl: "${unidadesURL}",
                          dToday: "${today}"
        };
        console.log( "${today}");
        CreateIrag.init(parametros);
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>