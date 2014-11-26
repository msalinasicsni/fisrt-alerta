<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <jsp:include page="../fragments/headTag.jsp" />
    <style>
        .datepicker{
            z-index: 1065 !important;
        }
        .modal .modal-dialog {
            width: 60%;
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
<c:url var="comunidadesURL" value="/api/v1/comunidadesSector"/>
<c:url var="distritosURL" value="/api/v1/distritosMng"/>
<c:url var="areasURL" value="/api/v1/areasMng"/>
<c:url value="/encuesta/guardarAedes" var="encuesta" />
<c:url var="recargarDetalleEncuestasURL" value="/encuesta/obtenerEncuestasAedesMae"/>
<c:url var="existeLocalidadURL" value="/encuesta/comunidadExisteEncuestaAedes"/>
<c:url var="existeMaestroURL" value="/encuesta/existeMaestroEncuestaAedes"/>
<c:url var="semanaEpidemiologicaURL" value="/api/v1/semanaEpidemiologica"/>
<c:url var="editarEncuestaURL" value="/encuesta/edit"/>
<c:url var="sectoresURL" value="/api/v1/sectoresMunicipio"/>

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
            <i class="fa-fw fa fa-bug"></i>
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
            <select id="codSilais" name="codSilais" class="select2">
                <option value=""><spring:message code="lbl.select" />...</option>
                <c:forEach items="${entidades}" var="entidad">
                    <option value="${entidad.codigo}">${entidad.nombre}</option>
                </c:forEach>
            </select>
        </div>
    </section>
    <section class="col col-sm-6 col-md-4 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="muni" /> </label>
        <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
            <select  class="select2" name="codMunicipioEncu" id="codMunicipioEncu">
                <option value=""><spring:message code="lbl.select" />...</option>
            </select>
        </div>
    </section>
    <section class="col col-sm-12 col-md-6 col-lg-6">
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
</div>
<!-- primera fila -->
<div class="row">
    <section class="col col-sm-6 col-md-4 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="lbl.district" />
        </label>
        <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
            <select class="select2" id="codigoDistrito" name="codigoDistrito" >
                <option value=""><spring:message code="lbl.select" />...</option>

            </select>
        </div>
    </section>
    <section class="col col-sm-6 col-md-4 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <spring:message code="lbl.area" />
        </label>
        <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
            <select class="select2" id="codigoArea" name="codigoArea" >
                <option value=""><spring:message code="lbl.select" />...</option>
            </select>
        </div>
    </section>
    <section class="col col-sm-6 col-md-4 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.ordinal" />
        </label>
        <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
            <select class="select2" id="codOrdinal" name="codOrdinal" >
                <option value=""><spring:message code="lbl.select" />...</option>
                <c:forEach items="${ordinales}" var="ordinales">
                    <option value="${ordinales.codigo}">${ordinales.valor}</option>
                </c:forEach>
            </select>
        </div>
    </section>
    <section class="col col-sm-6 col-md-4 col-lg-3">
        <label class="text-left txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.provenance" />
        </label>
        <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
            <select class="select2" id="codProcedencia" name="codProcedencia" >
                <option value=""><spring:message code="lbl.select" />...</option>
                <c:forEach items="${procedencias}" var="procedencias">
                    <option value="${procedencias.codigo}">${procedencias.valor}</option>
                </c:forEach>
            </select>
        </div>
    </section>
</div>
<div class="row">
    <section class="col col-sm-6 col-md-4 col-lg-2">
        <label class="text-left txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.start.date" />
        </label>
        <div class="">
            <!--<span class="input-group-addon">
                <i class="fa fa-pencil fa-fw"></i>
            </span>-->
            <label class="input">
                <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar"></i>
                <input type="text" name="fecInicioEncuesta" id="fecInicioEncuesta"
                       placeholder="<spring:message code="lbl.date.format"/>"
                       class="form-control from_date" data-date-end-date="+0d"/>
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.startdate.survey"/></b>
                <!--                        <span class="input-group-addon"> <i    class="fa fa-calendar fa-fw"></i>
                                        </span>-->
            </label>
        </div>
    </section>
    <section class="col col-sm-6 col-md-4 col-lg-2">
        <label class="text-left txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.end.date" />
        </label>
        <div class="">
            <label class="input">
                <!--<span class="input-group-addon">
                    <i class="fa fa-pencil fa-fw"></i>
		        </span>-->
                <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar"></i>
                <input type="text" name="fecFinEncuesta" id="fecFinEncuesta"
                       placeholder="<spring:message code="lbl.date.format"/>"
                       class="form-control to_date" data-date-end-date="+0d"/>
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enddate.survey"/></b>
                <!--<span class="input-group-addon"> <i class="fa fa-calendar fa-fw"></i>
                </span>-->
            </label>
        </div>
    </section>
    <section class="col col-sm-12 col-md-6 col-lg-6">
        <!--<div class="col col-12">-->
        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.month" />
            </label>
            <div class="">
                <!--<span class="input-group-addon">
                    <i class="fa fa-pencil fa-fw"></i>
                </span>-->
                <label class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                    <input type="text" id="mesEpi" name="mesEpi" readonly placeholder="<spring:message code="lbl.month"/>" class="form-control">
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.month.survey"/></b>
                </label>
                <!--<span class="input-group-addon">
                    <i class="fa fa-sort-numeric-asc fa-fw"></i>
                </span>-->
            </div>
        </section>
        <section class="col col-3">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.year" />
            </label>
            <div class=""><!-- <div class="input-group"> -->
                <!--<span class="input-group-addon">
                    <i class="fa fa-pencil fa-fw"></i>
                </span>-->
                <label class="input">
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                    <input type="text" id="anioEpi" name="anioEpi" readonly placeholder="<spring:message code="lbl.year"/>" class="form-control">
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.year.survey"/></b>
                </label>
                <!--<span class="input-group-addon">
                    <i class="fa fa-sort-numeric-asc fa-fw"></i>
                </span>-->
            </div>
        </section>
        <section class="col col-4">
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.ew" />
            </label>
            <div class="">
                <!--<span class="input-group-addon">
                    <i class="fa fa-pencil fa-fw"></i>
		        </span>-->
                <label class="input" >
                    <i class="icon-prepend fa fa-pencil fa-fw"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                    <input type="text" id="semanaEpi" name="semanaEpi" readonly placeholder="<spring:message code="lbl.ew"/>" class="form-control">
                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.ew"/></b>
                </label>
                <!--<span class="input-group-addon">
                    <i class="fa fa-sort-numeric-asc fa-fw"></i>
		        </span>-->
            </div>
        </section>
        <!--</div>-->
    </section>

</div>
</fieldset>
<fieldset>
    <div class="row">
        <div class="col-xs-10 col-sm-10 col-md-10 col-lg-10">
            <a class="btn btn-primary btn-lg pull-left header-btn hidden-mobile" id="btnNuevoRegistro"><i class="fa fa-circle-arrow-up fa-lg"></i><spring:message code="act.ento.new.survey" /></a>
        </div>
        <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
            <input id="msg_header_exist" type="hidden" value="<spring:message code="msg.ento.header.already.exist"/>"/>
            <input id="msg_location_added" type="hidden" value="<spring:message code="msg.ento.location.added.successfully"/>"/>
            <input id="msg_location_exist" type="hidden" value="<spring:message code="msg.ento.location.alreadey.exist"/>"/>
            <input id="text_opt_select" type="hidden" value="<spring:message code="lbl.select"/>"/>
            <c:set var="msgValid_greaterOrEqualThan"><spring:message code="msg.validation.greaterOrEqualThan"/></c:set>
            <c:set var="msgValid_lessOrEqualThan"><spring:message code="msg.validation.lessOrEqualThan"/></c:set>
            <c:set var="valBlock"><spring:message code="lbl.ento.block"/></c:set>
            <c:set var="valHomes"><spring:message code="lbl.ento.homes"/></c:set>
            <c:set var="valTanks"><spring:message code="lbl.ento.tank"/></c:set>
            <c:set var="valPosit"><spring:message code="lbl.ento.posit"/></c:set>
            <c:set var="valInspec"><spring:message code="lbl.ento.insp"/></c:set>
            <c:set var="valNoAbat"><spring:message code="lbl.ento.no"/> <spring:message code="lbl.ento.abat"/></c:set>
            <c:set var="valNoElimi"><spring:message code="lbl.ento.no"/> <spring:message code="lbl.ento.elimi"/></c:set>
            <c:set var="valNoNeutr"><spring:message code="lbl.ento.no"/> <spring:message code="lbl.ento.neutr"/></c:set>
            <input id="txt_act_edit" type="hidden" value="<spring:message code="act.edit"/>"/>
            <input id="smallBox_content" type="hidden" value="<spring:message code="smallBox.content.4s"/>"/>
            <input id="blockUI_message" type="hidden" value="<spring:message code="blockUI.message"/>"/>

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
                    <table id="dtDetalle" class="table table-striped table-bordered table-hover" data-width="100%" >
                        <thead>
                        <tr>
                            <th></th><th></th>
                            <th colspan="3" style="text-align: center" class="font-md "><spring:message code="lbl.ento.block" /></th>
                            <th colspan="3" style="text-align: center" class="font-md "><spring:message code="lbl.ento.homes" /></th>
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
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalManzanasInspec" style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalManzanasPosit" style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalManzanasIndice" style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalViviendasInspec" style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalViviendasPosit"  style="font-weight: bold">0</label></th>
                            <th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalViviendasIndice" style="font-weight: bold">0</label></th>
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
<!-- SECTOR Y LOCALIDAD -->
<div class="row">
    <section class="col col-sm-12 col-md-5 col-lg-5">
        <label class="text-left txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.sector" />
        </label>
        <div class="input-group">
    		        <span class="input-group-addon">
                        <i class="fa fa-location-arrow fa-fw"></i>
			        </span>
            <select  class="select2" id="codigoSector" name="codigoSector" >
                <option value=""><spring:message code="lbl.select" />...</option>
            </select>
        </div>
    </section>
    <section class="col col-sm-12 col-md-7 col-lg-7">
        <label class="text-left txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.locality" />
        </label>
        <div class="input-group">
	    					        <span class="input-group-addon">
                                        <i class="fa fa-location-arrow fa-fw"></i>
		    				        </span>
            <select class="select2" id="codigoLocalidad" name="codigoLocalidad" >
                <option value=""><spring:message code="lbl.select" />...</option>
            </select>
        </div>
    </section>
</div>
<!-- FIN LOCALIDAD -->
<!-- MANZANAS -->
<div class="row">
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.block" /> <spring:message code="lbl.ento.insp" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-desc"></i>
                <input type="text" name="manzanasInspec" id="manzanasInspec" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> <spring:message code="lbl.ento.block"/> <spring:message code="tooltip.ento.inspecf"/></b>
            </label>
        </div>
    </section>
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.block" /> <spring:message code="lbl.ento.posit" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-desc"></i>
                <input type="text" name="manzanasPositivas" id="manzanasPositivas" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> <spring:message code="lbl.ento.block"/> <spring:message code="tooltip.ento.positf"/></b>
            </label>
        </div>
        <!--<div class="">
            <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                <input type="text" name="manzanasPositivas" id="manzanasPositivas">
            </label>
        </div> -->
    </section>
</div>
<!--FIN MANZANAS -->
<!-- VIVIENDAS -->
<div class="row">
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class=" txt-color-blue font-md"><!--col col-4-->
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.homes" /> <spring:message code="lbl.ento.insp" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="viviendasInspec" id="viviendasInspec" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> <spring:message code="lbl.ento.homes"/> <spring:message code="tooltip.ento.inspecf"/></b>
            </label>
        </div>
    </section>
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.homes" /> <spring:message code="lbl.ento.posit" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="viviendasPositivas" id="viviendasPositivas" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> <spring:message code="lbl.ento.homes"/> <spring:message code="tooltip.ento.positf"/></b>
            </label>
        </div>
    </section>
</div>
<!--FIN VIVIENDAS -->
<!-- DEPOSITOS Y PUPAS -->
<div class="row">
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.tank" /> <spring:message code="lbl.ento.insp" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="depositosInspec" id="depositosInspec" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> <spring:message code="lbl.ento.tank"/> <spring:message code="tooltip.ento.inspecm"/></b>
            </label>
        </div>
    </section>
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.tank" /> <spring:message code="lbl.ento.posit" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="depositosPositovos" id="depositosPositovos" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> <spring:message code="lbl.ento.tank"/> <spring:message code="tooltip.ento.positm"/></b>
            </label>
        </div>
    </section>
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.modal.pupae.posit" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="pupasPositivas" id="pupasPositivas" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> <spring:message code="lbl.ento.pupae"/> <spring:message code="tooltip.ento.positf"/></b>
            </label>
        </div>
    </section>
</div>
<!-- FIN DEPOSITO Y PUPAS-->
<!-- NO ABATI, NO ELIMINI Y NO NEUTR -->
<div class="row">
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.no" /> </i><spring:message code="lbl.ento.abatizado" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="noAbati" id="noAbati" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> no <spring:message code="lbl.ento.abatizado"/></b>
            </label>
        </div>
    </section>
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class=" txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.no" /> <spring:message code="lbl.ento.eliminado" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="noElimni" id="noElimni" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> no <spring:message code="lbl.ento.eliminado"/></b>
            </label>
        </div>
    </section>
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.no" /> <spring:message code="lbl.ento.neutralizado" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="noNeutr" id="noNeutr" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> no <spring:message code="lbl.ento.neutralizado"/></b>
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
        <div class="">
            <!--<span class="input-group-addon"> <i class="fa fa-pencil fa-fw"></i></span>-->
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                <input type="text"
                       name="fecAbat" id="fecAbat"
                       placeholder="<spring:message code="lbl.date.format"/>"
                       class="form-control date-picker"
                       data-dateformat="dd/mm/yy"/>
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.date.abat"/></b>
            </label>
            <!--                    <span class="input-group-addon"> <i class="fa fa-calendar fa-fw"></i>
                                </span>-->
        </div>
    </section>
    <!--<section class="col col-sm-12 col-md-6 col-lg-4">
                    <label class="txt-color-blue font-md">
                        <spring:message code="lbl.ento.date" /> <spring:message code="lbl.ento.report" />
                    </label>
                    <div class="input-group">
                        <input type="text"
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
        <div class="">
            <!--<span class="input-group-addon"> <i class="fa fa-pencil fa-fw"></i></span>-->
            <label class="input">
                <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                <input type="text"
                       name="fecVent" id="fecVent"
                       placeholder="<spring:message code="lbl.date.format"/>"
                       class="form-control date-picker"
                       data-dateformat="dd/mm/yy"/>
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.date.vent"/></b>
                <!--<span class="input-group-addon"> <i class="fa fa-calendar fa-fw"></i>
                </span>-->
            </label>
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
<script src="${jQValidationLoc}"></script>

<!-- bootstrap datepicker -->
<spring:url value="/resources/js/plugin/bootstrap-datepicker/bootstrap-datepicker.js" var="datepickerPlugin" />
<script src="${datepickerPlugin}"></script>

<!-- jQuery Selecte2 Input -->
<spring:url value="/resources/js/plugin/select2/select2.min.js" var="selectPlugin"/>
<script src="${selectPlugin}"></script>

<!-- JQUERY BLOCK UI -->
<spring:url value="/resources/js/plugin/jquery-blockui/jquery.blockUI.js" var="jqueryBlockUi" />
<script src="${jqueryBlockUi}"></script>

<!-- JQUERY INPUT MASK -->
<spring:url value="/resources/js/plugin/jquery-inputmask/jquery.inputmask.bundle.min.js" var="jqueryInputMask" />
<script src="${jqueryInputMask}"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<spring:url value="/resources/scripts/utilidades/seleccionUnidad.js" var="selecUnidad" />
<script src="${selecUnidad}"></script>
<spring:url value="/resources/scripts/encuestas/survey-add.js" var="surveyAddAedes" />
<script src="${surveyAddAedes}"></script>
<spring:url value="/resources/scripts/utilidades/handleDatePickers.js" var="handleDatePickers" />
<script src="${handleDatePickers}"></script>
<spring:url value="/resources/scripts/utilidades/handleInputMask.js" var="handleInputMask" />
<script src="${handleInputMask}"></script>
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
            sSectoresUrl : "${sectoresURL}",
            dFechaHoy: "${fechaHoy}",
            sEditSurveyUrl : "${editarEncuestaURL}",
            msg_greaterOrEqualThan: ${msgValid_greaterOrEqualThan},
            msg_lessOrEqualThan:${msgValid_lessOrEqualThan},
            sValBlock : "${valBlock}",
            sValHomes : "${valHomes}",
            sValTanks : "${valTanks}",
            sValPosit : "${valPosit}",
            sValInspec : "${valInspec}",
            sValNoAbat : "${valNoAbat}",
            sValNoElimi : "${valNoElimi}",
            sValNoNeutr : "${valNoNeutr}",
            blockMess : $("#blockUI_message").val()
        };
        AddAedesSurvey.init(parametros);
        SeleccionUnidad.init(parametros);
        handleDatePickers("${pageContext.request.locale.language}");
        handleInputMasks();
        $("li.entomologia").addClass("open");
        $("li.entoaddaedes").addClass("active");
        if("top"!=localStorage.getItem("sm-setmenu")){
            $("li.entoaddaedes").parents("ul").slideDown(200);
        }
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>