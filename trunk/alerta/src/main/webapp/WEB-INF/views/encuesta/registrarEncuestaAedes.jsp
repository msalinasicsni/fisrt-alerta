<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <jsp:include page="../fragments/headTag.jsp" />
    <style>
        .midatepicker {
            z-index: 1051 !important;
        }
        .modal .modal-dialog {
            width: 60%;
        }
        .select2-hidden-accessible {
            display: none !important;
            visibility: hidden !important;
        }
        /* columns right and center aligned datatables */
        .aw-right {
            padding-left: 0;
            padding-right: 10px;
            text-align: right;
        }
        .aw-center {
            padding-left: 0;
            padding-right: 10px;
            text-align: center;
        }
        .dataTable{
            width: 100% !important;
        }
    </style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="">
<c:url var="unidadesURL" value="/api/v1/unidadesPrimarias"/>
<c:url var="municipiosURL" value="/api/v1/municipiosbysilais"/>
<c:url var="comunidadesURL" value="/api/v1/comunidad"/>
<c:url var="distritosURL" value="/api/v1/distritosMng"/>
<c:url var="areasURL" value="/api/v1/areasMng"/>
<c:url value="/encuesta/guardarAedes" var="encuesta" />
<c:url var="recargarDetalleEncuestasURL" value="/encuesta/obtenerEncuestasAedesMae"/>
<c:url var="existeLocalidadURL" value="/encuesta/comunidadExisteEncuestaAedes"/>
<c:url var="existeMaestroURL" value="/encuesta/existeMaestroEncuestaAedes"/>
<c:url var="semanaEpidemiologicaURL" value="/api/v1/semanaEpidemiologica"/>
<c:url var="editarEncuestaURL" value="/encuesta/edit"/>

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
        <li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.ento" /></a> <i class="fa fa-angle-right"></i> <a href="<spring:url value="#" htmlEscape="true "/>"><spring:message code="lbl.breadcrumb.ento.addaedes" /></a></li>
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
    <div class="col-xs-12 col-sm-7 col-md-7 col-lg-5">
        <h1 class="page-title txt-color-blueDark">
            <!-- PAGE HEADER -->
            <i class="fa-fw fa fa-home"></i>
            <spring:message code="lbl.ento.add" />
						<span> <i class="fa fa-angle-right"></i>
							<spring:message code="lbl.ento.sub.addades" />
						</span>
        </h1>
    </div>
    <!-- end col -->
    <!-- right side of the page with the sparkline graphs -->
    <!-- col -->
    <div class="col-xs-12 col-sm-5 col-md-5 col-lg-7">
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
    <span class="widget-icon"> <i class="fa fa-list"></i> </span>
    <h2><spring:message code="lbl.widgettitle.ento.add.mae" /> </h2>
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
<div class="widget-body">
<!-- this is what the user will see -->
<form class="smart-form" id="frmPrincipal" novalidate="novalidate">
<!--<header>
    Maestro de Encuesta
</header>-->
<div class="row" id="msjMaestro">
</div>
<fieldset>
    <div class="row">
        <section class="col col-sm-12 col-md-6 col-lg-3">
            <!--<label class="input">
                <input type="text" placeholder="2/12">
            </label>-->
            <label class="text-left txt-color-blue font-md">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.silais" /> </label>
            <div class="input-group">
                                                    <span class="input-group-addon">
                                                         <i class="fa fa-location-arrow fa-fw"></i>
                                                    </span>
                <select path="codSilais" id="codSilais" name="codSilais" class="select2">
                    <option value=""><spring:message code="lbl.select" />...</option>
                    <c:forEach items="${entidades}" var="entidad">
                        <option value="${entidad.codigo}">${entidad.nombre}</option>
                    </c:forEach>
                </select>
            </div>
        </section>
        <section class="col col-sm-12 col-md-6 col-lg-3">
            <label class="text-left txt-color-blue font-md">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.health.unit" />
            </label>
            <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
                <select class="select2" id="codUnidadSalud" name="codUnidadSalud">
                    <option value=""><spring:message code="lbl.select" />...</option>
                </select>
            </div>
        </section>
        <!--<section class="col col-2">
            <label class="text-left txt-color-blue font-md">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.department" /> </label>
            <div class="input-group">
						<span class="input-group-addon">
			                           <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
                <select name="codigoDepartamento" id="codigoDepartamento" class="select2">
                    <option value=""><spring:message code="lbl.select" />...</option>
                    <c:forEach items="${departamentos}" var="departamentos">
                        <option value="${departamentos.codigoNacional}">${departamentos.nombre}</option>
                    </c:forEach>
                </select>
            </div>
        </section>-->
        <section class="col col-sm-6 col-md-4 col-lg-2">
            <label class="text-left txt-color-blue font-md">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="muni" /> </label>
            <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
                <select  class="select2" name="codigoMunicipio" id="codigoMunicipio" path="codigoMunicipio">
                    <option value=""><spring:message code="lbl.select" />...</option>
                </select>
            </div>
        </section>
        <section class="col col-sm-6 col-md-4 col-lg-2">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.district" />
            </label>
            <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
                <select class="select2" id="codigoDistrito" name="codigoDistrito" path="codigoDistrito">
                    <option value=""><spring:message code="lbl.select" />...</option>

                </select>
            </div>
        </section>
        <section class="col col-sm-6 col-md-4 col-lg-2">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.area" />
            </label>
            <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
                <select class="select2" id="codigoArea" name="codigoArea"  path="codigoArea">
                    <option value=""><spring:message code="lbl.select" />...</option>
                </select>
            </div>
        </section>
    </div>
    <!-- primera fila -->
    <div class="row">
        <section class="col col-sm-6 col-md-4 col-lg-2">
            <label class="text-left txt-color-blue font-md">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.start.date" />
            </label>
            <div class="input-group">
                <input type="text" name="fecInicioEncuesta" id="fecInicioEncuesta"
                       placeholder="<spring:message code="lbl.date.format"/>"
                       class="form-control datepicker"
                       data-dateformat="dd/mm/yy"/>
                                                <span class="input-group-addon"> <i    class="fa fa-calendar fa-fw"></i>
                                                </span>
            </div>
        </section>
        <section class="col col-sm-6 col-md-4 col-lg-2">
            <label class="text-left txt-color-blue font-md">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.end.date" />
            </label>
            <div class="input-group">
                <input type="text" name="fecFinEncuesta" id="fecFinEncuesta"
                       placeholder="<spring:message code="lbl.date.format"/>"
                       class="form-control datepicker"
                       data-dateformat="dd/mm/yy"/>
                            <span class="input-group-addon"> <i class="fa fa-calendar fa-fw"></i>
                            </span>
            </div>
        </section>
        <section class="col col-sm-12 col-md-4 col-lg-4">
            <!--<div class="col col-12">-->
            <section class="col col-3">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.month" />
                </label>
                <label class="input">
                    <input type="number" id="mesEpi" name="mesEpi" path="mesEpi" disabled="true" placeholder="<spring:message code="lbl.month"/>" class="input-sm">
                </label>
            </section>
            <section class="col col-3">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.year" />
                </label>
                <label class="input">
                    <input type="number" id="anioEpi" name="anioEpi" path="anioEpi" disabled="true" placeholder="<spring:message code="lbl.year"/>" class="input-sm">
                </label>
            </section>
            <section class="col col-6">
                <label class="text-left txt-color-blue font-md">
                    <spring:message code="lbl.ew" />
                </label>
                <label class="input" >
                    <input type="number" id="semanaEpi" name="semanaEpi" path="semanaEpi" disabled="true" placeholder="<spring:message code="lbl.ew"/>" class="input-sm">
                </label>
            </section>
            <!--</div>-->
        </section>
        <section class="col col-sm-6 col-md-4 col-lg-2">
            <label class="text-left txt-color-blue font-md">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.ordinal" />
            </label>
            <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
                <select class="select2" id="codOrdinal" name="codOrdinal"  path="codOrdinal">
                    <option value=""><spring:message code="lbl.select" />...</option>
                    <c:forEach items="${ordinales}" var="ordinales">
                        <option value="${ordinales.codigo}">${ordinales.valor}</option>
                    </c:forEach>
                </select>
            </div>
        </section>
        <section class="col col-sm-6 col-md-4 col-lg-2">
            <label class="text-left txt-color-blue font-md">
                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.provenance" />
            </label>
            <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
                <select class="select2" id="codProcedencia" name="codProcedencia" path="codProcedencia">
                    <option value=""><spring:message code="lbl.select" />...</option>
                    <c:forEach items="${procedencias}" var="procedencias">
                        <option value="${procedencias.codigo}">${procedencias.valor}</option>
                    </c:forEach>
                </select>
            </div>
        </section>
    </div>
</fieldset>
<fieldset>
    <div class="row">
        <div class="col-xs-10 col-sm-10 col-md-10 col-lg-10">
            <a class="btn btn-primary btn-lg pull-right header-btn hidden-mobile" id="btnNuevoRegistro"><i class="fa fa-circle-arrow-up fa-lg"></i><spring:message code="act.ento.new.survey" /></a>
        </div>
        <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
            <c:set var="valmsg1"><spring:message code="msg.ento.header.already.exist"/></c:set>
            <input id="msg_header_exist" type="hidden" value="${valmsg1}"/>
            <c:set var="valmsg2"><spring:message code="msg.ento.location.added.successfully"/></c:set>
            <input id="msg_location_added" type="hidden" value="${valmsg2}"/>
            <c:set var="valmsg3"><spring:message code="msg.ento.location.alreadey.exist"/></c:set>
            <input id="msg_location_exist" type="hidden" value="${valmsg3}"/>
            <c:set var="valselect"><spring:message code="lbl.select"/></c:set>
            <input id="text_opt_select" type="hidden" value="${valselect}"/>

            <input id="idMaestroAgregado" type="hidden"/>
            <!-- Button trigger modal -->
            <a data-toggle="modal" href="#" class="btn btn-success btn-lg pull-right header-btn hidden-mobile" id="openModal"><i class="fa fa-circle-arrow-up fa-lg"></i><spring:message code="act.ento.add.locality" /></a>
        </div>
    </div>
</fieldset>
</form>
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
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div class="jarviswidget jarviswidget-color-darken" id="wid-id-2" data-widget-editbutton="false" data-widget-deletebutton="false">
            <header>
                <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                <h2><spring:message code="lbl.widgettitle.ento.add.det" /> </h2>
            </header>
            <!-- widget div-->
            <div>
                <!-- widget edit box -->
                <div class="jarviswidget-editbox">
                    <!-- This area used as dropdown edit box -->
                </div>
                <!-- end widget edit box -->
                <!-- widget content -->
                <div class="widget-body no-padding">
                    <table id="dtDetalle" class="table table-striped table-bordered table-hover" data-width="100%">
                        <thead>
                        <tr>
                            <th></th><th></th>
                            <th colspan="3" style="text-align: center" class="font-md "><spring:message code="lbl.ento.homes" /></th>
                            <th colspan="3" style="text-align: center" class="font-md "><spring:message code="lbl.ento.block" /></th>
                            <th colspan="3" style="text-align: center" class="font-md "><spring:message code="lbl.ento.tank" /></th>
                            <th colspan="3" style="text-align: center" class="font-md "><spring:message code="lbl.ento.index" /></th>
                            <th colspan="2" style="text-align: center" class="font-md "><spring:message code="lbl.ento.dates" /></th>
                            <th colspan="1" style="text-align: center" class="font-md "><spring:message code="lbl.ento.no" /></th>
                            <th colspan="1" style="text-align: center" class="font-md "><spring:message code="lbl.ento.no" /></th>
                            <th colspan="1" style="text-align: center" class="font-md "><spring:message code="lbl.ento.no" /></th>
                        </tr>
                        <tr>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.no" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.locality" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.insp" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.posit" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.index" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.insp" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.posit" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.index" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.insp" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.posit" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.index" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.bretes" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.pupae" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.ipupae" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.abat" /></p></th>
                            <!--<th><p class="text-center font-sm "><spring:message code="lbl.ento.report" /></p></th>-->
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.vent" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.abat" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.elimi" /></p></th>
                            <th><p class="text-center font-sm "><spring:message code="lbl.ento.neutr" /></p></th>
                        </tr>
                        </thead>
                        <tfoot>
                        <tr>
                            <th style="text-align: right; background-color: #86b4dd"></th>
                            <th colspan="1" style="text-align: center; background-color: #86b4dd"><spring:message code="lbl.ento.totals" /></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalViviendasInspec" style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalViviendasPosit"  style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalViviendasIndice" style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalManzanasInspec" style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalManzanasPosit" style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalManzanasIndice" style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalDepositosInspec" style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalDepositosPosit" style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalDepositosIndice" style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalIndiceBrete" style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalPupas" style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalIndiceIPupa" style="font-weight: bold">0</label></th>
                            <th colspan="5" rowspan="1" style="text-align: center; background-color: #86b4dd"></th>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
            <!-- end widget div -->
        </div>

    </article>
</div>
<!-- end row -->
</section>
<!-- Modal -->
<div class="modal fade" id="myModal" aria-hidden="true" data-backdrop="static"> <!--tabindex="-1" role="dialog" -->
<div class="modal-dialog">
<div class="modal-content">
<div class="modal-header">
    <!--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
        &times;
                    </button>-->
    <h4 class="modal-title">
        <spring:message code="lbl.widgettitle.ento.add.det" />
    </h4>
</div>
<div class="modal-body"> <!--  no-padding -->
<form id="frmDetalleEncuesta" class="smart-form" novalidate="novalidate">
    <fieldset>
        <!-- NOTIFICACIÓN -->
        <div id="mensaje">
        </div>
        <!-- NOTIFICACIÓN -->
        <!-- LOCALIDAD -->
        <div class="row">
            <section class="col col-sm-12 col-md-12 col-lg-12">
                <label class="text-left txt-color-blue font-md">
                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.locality" />
                </label>
                <div class="input-group">
	    					        <span class="input-group-addon">
                                        <i class="fa fa-location-arrow fa-fw"></i>
		    				        </span>
                    <select class="select2" id="codigoLocalidad" name="codigoLocalidad" path="codigoLocalidad">
                        <option value=""><spring:message code="lbl.select" />...</option>
                    </select>
                </div>
            </section>
        </div>
        <!-- FIN LOCALIDAD -->
        <!-- VIVIENDAS -->
        <div class="row">
            <section class="col col-sm-12 col-md-6 col-lg-4">
                <label class=" txt-color-blue font-md"><!--col col-4-->
                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.homes" /> <spring:message code="lbl.ento.insp" />
                </label>
                <div class="">
                    <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                        <input type="number" name="viviendasInspec" id="viviendasInspec">
                    </label>
                </div>
            </section>
            <section class="col col-sm-12 col-md-6 col-lg-4">
                <label class="txt-color-blue font-md">
                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.homes" /> <spring:message code="lbl.ento.posit" />
                </label>
                <div class="">
                    <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                        <input type="number" name="viviendasPositivas" id="viviendasPositivas">
                    </label>
                </div>
            </section>
        </div>
        <!--FIN VIVIENDAS -->
        <!-- MANZANAS -->
        <div class="row">
            <section class="col col-sm-12 col-md-6 col-lg-4">
                <label class="txt-color-blue font-md">
                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.block" /> <spring:message code="lbl.ento.insp" />
                </label>
                <div class="">
                    <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                        <input type="number" name="manzanasInspec" id="manzanasInspec">
                    </label>
                </div>
            </section>
            <section class="col col-sm-12 col-md-6 col-lg-4">
                <label class="txt-color-blue font-md">
                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.block" /> <spring:message code="lbl.ento.posit" />
                </label>
                <div class="">
                    <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                        <input type="number" name="manzanasPositivas" id="manzanasPositivas">
                    </label>
                </div>
            </section>
        </div>
        <!--FIN MANZANAS -->
        <!-- DEPOSITOS Y PUPAS -->
        <div class="row">
            <section class="col col-sm-12 col-md-6 col-lg-4">
                <label class="txt-color-blue font-md">
                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.tank" /> <spring:message code="lbl.ento.insp" />
                </label>
                <div class="">
                    <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                        <input type="number" name="depositosInspec" id="depositosInspec">
                    </label>
                </div>
            </section>
            <section class="col col-sm-12 col-md-6 col-lg-4">
                <label class="txt-color-blue font-md">
                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.tank" /> <spring:message code="lbl.ento.posit" />
                </label>
                <div class="">
                    <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                        <input type="number" name="depositosPositovos" id="depositosPositovos">
                    </label>
                </div>
            </section>
            <section class="col col-sm-12 col-md-6 col-lg-4">
                <label class="txt-color-blue font-md">
                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.modal.pupae.posit" />
                </label>
                <div class="">
                    <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                        <input type="number" name="pupasPositivas" id="pupasPositivas">
                    </label>
                </div>
            </section>
        </div>
        <!-- FIN DEPOSITO Y PUPAS-->
        <!-- NO ABATI, NO ELIMINI Y NO NEUTR -->
        <div class="row">
            <section class="col col-sm-12 col-md-6 col-lg-4">
                <label class="txt-color-blue font-md">
                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.no" /> </i><spring:message code="lbl.ento.abat" />
                </label>
                <div class="">
                    <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                        <input type="number" name="noAbati" id="noAbati">
                    </label>
                </div>
            </section>
            <section class="col col-sm-12 col-md-6 col-lg-4">
                <label class=" txt-color-blue font-md">
                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.no" /> <spring:message code="lbl.ento.elimi" />
                </label>
                <div class="">
                    <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                        <input type="number" name="noElimni" id="noElimni">
                    </label>
                </div>
            </section>
            <section class="col col-sm-12 col-md-6 col-lg-4">
                <label class="txt-color-blue font-md">
                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.no" /> </i><spring:message code="lbl.ento.neutr" />
                </label>
                <div class="">
                    <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                        <input type="number" name="noNeutr" id="noNeutr">
                    </label>
                </div>
            </section>
        </div>
        <!-- FIN NO ABATI, NO ELIMINI Y NO NEUTR -->
        <!-- FECHAS-->
        <div class="row">
            <section class="col col-sm-12 col-md-6 col-lg-4">
                <label class="txt-color-blue font-md">
                    <spring:message code="lbl.ento.date" /> <spring:message code="lbl.ento.abat" />
                </label>
                <div class="input-group">
                    <input path="fecAbat" type="text"
                           name="fecAbat" id="fecAbat"
                           placeholder="<spring:message code="lbl.date.format"/>"
                           class="form-control datepicker midatepicker"
                           data-dateformat="dd/mm/yy"/>
                                        <span class="input-group-addon"> <i class="fa fa-calendar fa-fw"></i>
                                        </span>
                </div>
            </section>
            <!--<section class="col col-sm-12 col-md-6 col-lg-4">
                    <label class="txt-color-blue font-md">
                        <spring:message code="lbl.ento.date" /> <spring:message code="lbl.ento.report" />
                    </label>
                    <div class="input-group">
                        <input path="fecReport" type="text"
                               name="fecReport" id="fecReport"
                               placeholder="<spring:message code="lbl.date.format"/>"
                               class="form-control datepicker midatepicker"
                               data-dateformat="dd/mm/yy"/>
                                        <span class="input-group-addon"> <i class="fa fa-calendar fa-fw"></i>
                                        </span>
                    </div>
                </section>-->
            <section class="col col-sm-12 col-md-6 col-lg-4">
                <label class="txt-color-blue font-md">
                    <spring:message code="lbl.ento.date" /> <spring:message code="lbl.ento.vent" />
                </label>
                <div class="input-group">
                    <input path="fecVent" type="text"
                           name="fecVent" id="fecVent"
                           placeholder="<spring:message code="lbl.date.format"/>"
                           class="form-control datepicker midatepicker"
                           data-dateformat="dd/mm/yy"/>
                                        <span class="input-group-addon"> <i class="fa fa-calendar fa-fw"></i>
                                        </span>
                </div>
            </section>
        </div>
        <!-- FIN FECHAS-->
    </fieldset>

    <footer>
        <!--<button type="button" class="btn btn-primary" id="btnGuardarDetalle">
            Guardar
        </button>-->
        <button type="submit" class="btn btn-primary" id="btnGuardarDetalle">
            <spring:message code="act.save" />
        </button>
        <button type="button" class="btn btn-default" data-dismiss="modal">
            <spring:message code="act.end" />
        </button>

    </footer>

</form>
</div>
</div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
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

<!-- JQUERY VALIDATE -->
<spring:url value="/resources/js/plugin/jquery-validate/jquery.validate.min.js" var="validate" />
<script src="${validate}"></script>
<spring:url value="/resources/js/plugin/jquery-validate/messages_{language}.js" var="jQValidationLoc">
    <spring:param name="language" value="${pageContext.request.locale.language}" /></spring:url>
<script src="${jQValidationLoc}"/></script>

<!-- jQuery Validate datepicker -->
<spring:url value="/resources/js/plugin/jquery-validate-datepicker/jquery.ui.datepicker.validation.min.js" var="jQueryValidateDatepicker"/>
<script src="${jQueryValidateDatepicker}"></script>

<!-- jQuery Selecte2 Input -->
<spring:url value="/resources/js/plugin/select2/select2.min.js" var="selectPlugin"/>
<script src="${selectPlugin}"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<spring:url value="/resources/scripts/encuestas/survey-add.js" var="surveyAddAedes" />
<script src="${surveyAddAedes}"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>
    $(function () {
        $("li.entoaddaedes").addClass("active");
    });
</script>
<script type="text/javascript">
    $(document).ready(function(){
        pageSetUp();
        var parametros = {sAddSurvey: "${encuesta}",
            sSurveyDetailsUrl : "${recargarDetalleEncuestasURL}",
            sSurveyHeaderUrl : "${existeMaestroURL}",
            sMunicipiosUrl : "${municipiosURL}",
            sComunidadesUrl : "${comunidadesURL}",
            sDistritosUrl : "${distritosURL}",
            sAreasUrl : "${areasURL}",
            sUnidadesUrl: "${unidadesURL}",
            sValidarLocalidadUrl : "${existeLocalidadURL}",
            sSemanaEpiUrl : "${semanaEpidemiologicaURL}",
            dFechaHoy: "${fechaHoy}",
            sEditSurveyUrl : "${editarEncuestaURL}"
        };
        AddAedesSurvey.init(parametros);
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>