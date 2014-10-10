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
        /* columns right aligned datatables */
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
    </style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="">
<c:url var="unidadesURL" value="/api/v1/unidades"/>
<c:url var="municipiosURL" value="/api/v1/municipio"/>
<c:url var="comunidadesURL" value="/api/v1/comunidad"/>
<c:url var="distritosURL" value="/api/v1/distritosMng"/>
<c:url var="areasURL" value="/api/v1/areasMng"/>
<c:url value="/encuesta/guardarLarvariaAedes" var="encuesta" />
<c:url var="recargarDetalleEncuestasURL" value="/encuesta/obtenerEncuestasLarvariasMae"/>
<c:url var="existeLocalidadURL" value="/encuesta/comunidadExisteEncuestaLarva"/>
<c:url var="existeMaestroURL" value="/encuesta/existeMaestroEncuestaLarva"/>
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
				<li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.ento" /></a> <i class="fa fa-angle-right"></i> <a href="<spring:url value="#" htmlEscape="true "/>"><spring:message code="lbl.breadcrumb.ento.addlarva" /></a></li>
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
					<h1 class="page-title txt-color-blueDark">
						<!-- PAGE HEADER -->
						<i class="fa-fw fa fa-home"></i> 
							<spring:message code="lbl.ento.add" />
						<span> <i class="fa fa-angle-right"></i>  
							<spring:message code="lbl.ento.sub.addlarvaria" />
						</span>
                    </h1>
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
                                            <section class="col col-4">
                                                <!--<label class="input">
                                                    <input type="text" placeholder="2/12">
                                                </label>-->
                                                <label class="text-left txt-color-blue font-md">
                                                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>SILAIS </label>
                                                <div class="input-group">
                                <span class="input-group-addon">
                                     <i class="fa fa-location-arrow fa-fw"></i>
                                </span>
                                                    <select path="codSilais" id="codSilais" name="codSilais"
                                                            class="select2">
                                                        <option value="">Seleccione...</option>
                                                        <c:forEach items="${entidades}" var="entidad">
                                                            <option value="${entidad.codigo}">${entidad.nombre}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </section>
                                            <section class="col col-2">
                                                <label class="text-left txt-color-blue font-md">
                                                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Departamentos </label>
                                                <div class="input-group">
							<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
                                                    <select name="codigoDepartamento" id="codigoDepartamento" class="select2">
                                                        <option value="">Seleccione...</option>
                                                        <c:forEach items="${departamentos}" var="departamentos">
                                                            <option value="${departamentos.codigoNacional}">${departamentos.nombre}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </section>
                                            <section class="col col-2">
                                                <label class="text-left txt-color-blue font-md">
                                                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Municipio </label>
                                                <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
                                                    <select  class="select2" name="codigoMunicipio" id="codigoMunicipio" path="codigoMunicipio">
                                                        <option value="">Seleccione...</option>
                                                    </select>
                                                </div>
                                            </section>
                                            <section class="col col-2">
                                                <label class="text-left txt-color-blue font-md">
                                                    <!--<i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>-->Distrio
                                                </label>
                                                <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
                                                    <select class="select2" id="codigoDistrito" name="codigoDistrito" path="codigoDistrito">
                                                        <option value="">Seleccione...</option>

                                                    </select>
                                                </div>
                                            </section>
                                            <section class="col col-2">
                                                <label class="text-left txt-color-blue font-md">
                                                    <!--<i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>-->Area
                                                </label>
                                                <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
                                                    <select class="select2" id="codigoArea" name="codigoArea"  path="codigoArea">
                                                        <option value="">Seleccione...</option>
                                                    </select>
                                                </div>
                                            </section>
                                        </div>
                                        <!-- primera fila -->
                                        <div class="row">
                                            <section class="col col-4">
                                                <label class="text-left txt-color-blue font-md">
                                                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Unidad de Salud
                                                </label>
                                                <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
                                                    <select class="select2" id="codUnidadSalud" name="codUnidadSalud">
                                                        <option value="">Seleccione...</option>
                                                    </select>
                                                </div>
                                            </section>
                                            <section class="col col-2">
                                                <label class="text-left txt-color-blue font-md">
                                                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Fecha Inicio
                                                </label>
                                                <div class="input-group">
                                                    <input type="text" name="fecInicioEncuesta" id="fecInicioEncuesta"
                                                           placeholder="Fecha de Inicio de Encuesta"
                                                           class="form-control datepicker"
                                                           data-dateformat="dd/mm/yy"/> <!-- class="form-control datepicker"-->
                            <span class="input-group-addon"> <i    class="fa fa-calendar fa-fw"></i>
                            </span>
                                                </div>
                                            </section>
                                            <section class="col col-2">
                                                <label class="text-left txt-color-blue font-md">
                                                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Fecha Final
                                                </label>
                                                <div class="input-group">
                                                    <input type="text" name="fecFinEncuesta" id="fecFinEncuesta"
                                                           placeholder="Fecha de Fin de Encuesta"
                                                           class="form-control datepicker"
                                                           data-dateformat="dd/mm/yy"/>
                            <span class="input-group-addon"> <i class="fa fa-calendar fa-fw"></i>
                            </span>
                                                </div>
                                            </section>
                                            <section class="col col-2">
                                                <label class="text-left txt-color-blue font-md">
                                                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Ordinal
                                                </label>
                                                <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
                                                    <select class="select2" id="codOrdinal" name="codOrdinal"  path="codOrdinal">
                                                        <option value="">Seleccione...</option>
                                                        <c:forEach items="${ordinales}" var="ordinales">
                                                            <option value="${ordinales.codigo}">${ordinales.valor}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </section>
                                            <section class="col col-2">
                                                <label class="text-left txt-color-blue font-md">
                                                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Procedencia
                                                </label>
                                                <div class="input-group">
	    					<span class="input-group-addon">
                                <i class="fa fa-location-arrow fa-fw"></i>
		    				</span>
                                                    <select class="select2" id="codProcedencia" name="codProcedencia" path="codProcedencia">
                                                        <option value="">Seleccione...</option>
                                                        <c:forEach items="${procedencias}" var="procedencias">
                                                            <option value="${procedencias.codigo}">${procedencias.valor}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </section>
                                        </div>
                                        <div class="row">
                                            <section class="col col-5">
                                                <section class="col col-2">
                                                    <label class="text-left txt-color-blue font-md">
                                                        Mes
                                                    </label>
                                                    <label class="input">
                                                        <input type="number" id="mesEpi" name="mesEpi" path="mesEpi" disabled="true" placeholder="Mes" class="input-sm">
                                                    </label>
                                                </section>
                                                <section class="col col-2">
                                                    <label class="text-left txt-color-blue font-md">
                                                        </i>Año
                                                    </label>
                                                    <label class="input">
                                                        <input type="number" id="anioEpi" name="anioEpi" path="anioEpi" disabled="true" placeholder="Año" class="input-sm">
                                                    </label>
                                                </section>
                                                <section class="col col-5">
                                                    <label class="text-left txt-color-blue font-md">
                                                        Semana Epidemiológica
                                                    </label>
                                                    <div class="col-6">
                                                        <label class="input" >
                                                            <input type="number" id="semanaEpi" name="semanaEpi" path="semanaEpi" disabled="true" placeholder="Semana Epi" class="input-sm">
                                                        </label>
                                                    </div>
                                                </section>
                                            </section>
                                        </div>
                                    </fieldset>
                                    <fieldset>
                                        <div class="row">
                                            <div class="col-xs-10 col-sm-10 col-md-10 col-lg-10">
                                                <a class="btn btn-primary btn-lg pull-right header-btn hidden-mobile" id="btnNuevoRegistro"><i class="fa fa-circle-arrow-up fa-lg"></i>Nuevo Registro</a>
                                            </div>
                                            <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                                <input id="idMaestroAgregado" type="hidden"/>
                                                <!-- Button trigger modal -->
                                                <a data-toggle="modal" href="#" class="btn btn-success btn-lg pull-right header-btn hidden-mobile" id="openModal"><i class="fa fa-circle-arrow-up fa-lg"></i>Agregar Localidad</a>
                                            </div>
                                        </div>
                                    </fieldset>
                                    <!--<fieldset>
                                    </fieldset>
                                    <footer>
                                    </footer>-->
                                    </form>
    							</div>
								<!-- end widget content -->
							</div>
							<!-- end widget div -->
						</div>
						<!-- end widget -->
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
                                    <div class="widget-body-toolbar">
                                    </div>
                                    <table id="dtDetalleIndices" class="table table-striped table-bordered table-hover" data-width="100%">
                                        <thead>
                                        <tr><th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">No</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Localidad&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Pila</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Llanta</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Barril</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Florero</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Bebed</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Art Esp</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">O Dep</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Cister</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Inodo</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Barro</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Planta</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Arbol</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Pozo</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Total</p></th></tr></thead>
                                        <tfoot>
                                        <tr><th style="text-align: right; background-color: #86b4dd"></th><th class="font-sm" style="text-align: center; background-color: #86b4dd">TOTALES</th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalPila" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalLlanta"  style="font-weight: bold"><strong>0</strong></label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalBarril" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalFloreros" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalBeberedos" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalArtEspec" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalODep" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalCister" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalInodo" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalBarro" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalPlanta" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalArbol" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalPozo" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalTotalI" style="font-weight: bold">0</label></th></tr>
                                        <tr><th style="text-align: right; background-color: #86b4dd"></th><th class="font-sm" style="text-align: center; background-color: #86b4dd">INDICES</th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indicePila" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceLlanta"  style="font-weight: bold"><strong>0</strong></label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceBarril" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceFloreros" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceBeberedos" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceArtEspec" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceODep" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceCister" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceInodo" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceBarro" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indicePlanta" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceArbol" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indicePozo" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"></th></tr>
                                        </tfoot>
                                    </table>
                                    <div class="widget-body-toolbar">
                                    </div>
                                    <table id="dtDetalleDistribucion" class="table table-striped table-bordered table-hover">
                                        <thead>
                                        <tr><th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">No</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Localidad&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Aedes Aegypti</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Aedes Albopic</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Culex Quinque</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Culex Nigrip</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Culex Coronat</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Culex Erratico</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Culex Tarsalis</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Culex Fatigans</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Anoph Albim</p></th>
                                            <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Total</p></th></tr></thead>
                                        <tfoot><tr><th style="text-align: right; background-color: #86b4dd"></th><th class="font-sm" style="text-align: center; background-color: #86b4dd">TOTALES</th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalAegypti" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalAlbopic"  style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalQuinque" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalNigrip" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalCoronat" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalErratico" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalTarsalis" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalFatigans" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalAnophAlbim" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalTotalD" style="font-weight: bold">0</label></th></tr>
                                        <tr><th style="text-align: right; background-color: #86b4dd"></th><th class="font-sm" style="text-align: center; background-color: #86b4dd">INDICES</th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceAegypti" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceAlbopic"  style="font-weight: bold"><strong>0</strong></label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceQuinque" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceNigrip" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceCoronat" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceErratico" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceTarsalis" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceFatigans" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceAnophAlbim" style="font-weight: bold">0</label></th>
                                            <th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"></th></tr></tfoot>
                                    </table>
                                </div>
                            </div>
                            <!-- end widget div -->
                        </div>
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
            <!-- Modal -->
            <div class="modal fade" id="myModal" aria-hidden="true" data-backdrop="static"> <!--tabindex="-1" role="dialog" -->
            <div class="modal-dialog">
            <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">
                    Agregar Detalle de Encuesta
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
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Localidades
                    </label>
                    <div class="input-group">
	    					        <span class="input-group-addon">
                                        <i class="fa fa-location-arrow fa-fw"></i>
		    				        </span>
                        <select class="select2" id="codigoLocalidad" name="codigoLocalidad" path="codigoLocalidad">
                            <option value="">Seleccione...</option>
                        </select>
                    </div>
                </section>
            </div>
            <!-- FIN LOCALIDAD -->
            <header>Indices de Recipientes Infestados</header>
            <!-- VIVIENDAS -->
            <div class="row">
                <section class="col col-sm-6 col-md-4 col-lg-2">
                    <label class=" txt-color-blue font-md"><!--col col-4-->
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Pila
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtPila" id="txtPila">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-2">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Llanta
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtLlanta" id="txtLlanta">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-2">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Barril
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtBarril" id="txtBarril">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-2">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Florero
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtFlorero" id="txtFlorero">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-2">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Bebedero
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtBebedero" id="txtBebedero">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-2">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Art_Esp
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtArt_Esp" id="txtArt_Esp">
                        </label>
                    </div>
                </section>
            </div>
            <!--FIN VIVIENDAS -->
            <!-- MANZANAS -->
            <div class="row">
                <section class="col col-sm-6 col-md-4 col-lg-2">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>O_Dep
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtO_Dep" id="txtO_Dep">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-2">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Cister
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtCister" id="txtCister">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-2">
                    <label class=" txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Inodoro
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtInodoro" id="txtInodoro">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-2">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Barro
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtBarro" id="txtBarro">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-2">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Planta
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtPlanta" id="txtPlanta">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-2">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Árbol
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtArbol" id="txtArbol">
                        </label>
                    </div>
                </section>
            </div>
            <!--FIN MANZANAS -->
            <!-- FECHAS-->
            <div class="row">
                <section class="col col-sm-6 col-md-4 col-lg-2">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Pozo
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtPozo" id="txtPozo">
                        </label>
                    </div>
                </section>
            </div>
            <!-- FIN FECHAS-->
            <header>Distribución Especies Reportadas</header>
            <!-- VIVIENDAS -->
            <div class="row">
                <section class="col col-sm-6 col-md-4 col-lg-4">
                    <label class=" txt-color-blue font-md"><!--col col-4-->
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Aedes Aegypti
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtAedesAegyti" id="txtAedesAegyti">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-4">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Aedes Albopic
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtAedesAlbopic" id="txtAedesAlbopic">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-4">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Culex Quinque
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtCulexQuinque" id="txtCulexQuinque">
                        </label>
                    </div>
                </section>
            </div>
            <!--FIN VIVIENDAS -->
            <!-- MANZANAS -->
            <div class="row">
                <section class="col col-sm-6 col-md-4 col-lg-4">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Culex Nigrip
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtCulexNigrip" id="txtCulexNigrip">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-4">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Culex Coronat
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtCulexCoronat" id="txtCulexCoronat">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-4">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Culex Erratico
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtCulexErratico" id="txtCulexErratico">
                        </label>
                    </div>
                </section>
            </div>
            <!--FIN MANZANAS -->
            <!-- DEPOSITOS Y PUPAS -->
            <div class="row">
                <section class="col col-sm-6 col-md-4 col-lg-4">
                    <label class="txt-color-blue font-md">
                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Culex Tarsalis
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtCulexTarsalis" id="txtCulexTarsalis">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-4">
                    <label class="txt-color-blue font-md"><i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Culex Fatigans
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtCulexFatigans" id="txtCulexFatigans">
                        </label>
                    </div>
                </section>
                <section class="col col-sm-6 col-md-4 col-lg-4">
                    <label class=" txt-color-blue font-md"><i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Anoph Albim
                    </label>
                    <div class="">
                        <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                            <input type="number" name="txtAnophAlbim" id="txtAnophAlbim">
                        </label>
                    </div>
                </section>
            </div>
            <!-- FIN DEPOSITO Y PUPAS-->
            </fieldset>
            <footer>
                <button type="submit" class="btn btn-primary" id="btnGuardarDetalle">
                    Guardar
                </button>
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    Finalizar
                </button>

            </footer>

            </form>
            </div>
            </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
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
<spring:url value="/resources/scripts/encuestas/survey-addaedes.js" var="surveyAddAedes" />
<script src="${surveyAddAedes}"></script>
    <!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->

    <!-- END PAGE LEVEL SCRIPTS -->
	<script>
	    $(function () {
	    	$("li.entoaddlarvae").addClass("active");
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
        dFechaHoy: "${fechaHoy}"
    };
    AddLarvariaSurvey.init(parametros);
});
</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>