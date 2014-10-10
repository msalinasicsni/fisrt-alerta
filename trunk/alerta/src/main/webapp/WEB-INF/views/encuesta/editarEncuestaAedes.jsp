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
<c:url var="recargarDetalleEncuestasURL" value="/encuesta/obtenerEncuestasAedesMae"/>
<c:url var="existeLocalidadURL" value="/encuesta/comunidadExisteEncuestaAedes"/>
<c:url var="existeMaestroURL" value="/encuesta/existeMaestroEncuestaAedes"/>
<c:url var="semanaEpidemiologicaURL" value="/api/v1/semanaEpidemiologica"/>
<c:url var="editarEncuestaURL" value="/encuesta/edit"/>
<c:url var="editarAedesURL" value="/encuesta/actualizarAedes"/>
<c:url var="recuperarDetalleAedes" value="/encuesta/recuperarDetalleAedes"/>

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
				<div class="col-xs-12 col-sm-7 col-md-7 col-lg-4">
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
                                                    <select path="codSilais" id="codSilais" name="codSilais" class="select2">
                                                        <!--<option value="">Seleccione...</option>-->
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
                                                        <!--<option value="">Seleccione...</option>-->
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
                                                    <select name="codigoMunicipio" id="codigoMunicipio" path="codigoMunicipio" class="select2">
                                                        <!--<option value="">Seleccione...</option>-->
                                                        <c:forEach items="${municipios}" var="municipios">
                                                            <option value="${municipios.codigoNacional}">${municipios.nombre}</option>
                                                        </c:forEach>

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
                                                    <select  id="codigoDistrito" name="codigoDistrito" path="codigoDistrito" class="select2">
                                                        <option value="">Seleccione...</option>
                                                        <c:forEach items="${distritos}" var="distritos">
                                                            <option value="${distritos.codigo}">${distritos.valor}</option>
                                                        </c:forEach>

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
                                                    <select  id="codigoArea" name="codigoArea"  path="codigoArea" class="select2">
                                                        <option value="">Seleccione...</option>
                                                        <c:forEach items="${areas}" var="areas">
                                                            <option value="${areas.codigo}">${areas.valor}</option>
                                                        </c:forEach>
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
                                                    <select  id="codUnidadSalud" name="codUnidadSalud" class="select2">
                                                        <!--<option value="">Seleccione...</option>-->
                                                        <c:forEach items="${unidadesSalud}" var="unidadesSalud">
                                                            <option value="${unidadesSalud.codigo}">${unidadesSalud.nombre}</option>
                                                        </c:forEach>
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
                                                    <select id="codOrdinal" name="codOrdinal"  path="codOrdinal" class="select2">
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
                                                    <select id="codProcedencia" name="codProcedencia" path="codProcedencia" class="select2">
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
                                                <!--<div class="col col-12">-->
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
                                                <!--</div>-->
                                            </section>

                                        </div>
                                    </fieldset>
                                    <fieldset>
                                        <div class="row">
                                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                                <label id="idMaestroEditado"></label>
                                                <!-- Button trigger modal -->
                                                <a data-toggle="modal" href="#" class="btn btn-success btn-lg pull-right header-btn hidden-mobile editDetalle" id="openModal"><i class="fa fa-circle-arrow-up fa-lg"></i>Agregar Localidad</a>
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
                                    <table id="dtDetalle" class="table table-striped table-bordered table-hover">
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
            <!-- Modal -->
            <div class="modal fade" id="myModal" aria-hidden="true" data-backdrop="static"> <!--tabindex="-1" role="dialog" -->
            <div class="modal-dialog">
            <div class="modal-content">
            <div class="modal-header">
                <!--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                    </button>-->
                <h4 class="modal-title">
                    Agregar Detalle de Encuesta
                </h4>
            </div>
            <div class="modal-body"> <!--  no-padding -->
            <form id="frmDetalleEncuesta" class="smart-form" novalidate="novalidate">
            <fieldset>
                <div>
                    <input type="hidden" id="idDetalleEditar"/>
                    <input id="codLocalidadEdicion" type="hidden"/>
                </div>
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
                                <c:forEach items="${localidades}" var="localidades">
                                    <option value="${localidades.codigo}">${localidades.nombre}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </section>
                </div>
                <!-- FIN LOCALIDAD -->
                <!-- VIVIENDAS -->
                <div class="row">
                    <section class="col col-sm-12 col-md-6 col-lg-4">
                        <label class=" txt-color-blue font-md"><!--col col-4-->
                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Viviendas Inspec.
                        </label>
                        <div class="">
                            <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                                <input type="number" name="viviendasInspec" id="viviendasInspec">
                            </label>
                        </div>
                    </section>
                    <section class="col col-sm-12 col-md-6 col-lg-4">
                        <label class="txt-color-blue font-md">
                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Viviendas Posit.
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
                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Manzanas Inspec.
                        </label>
                        <div class="">
                            <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                                <input type="number" name="manzanasInspec" id="manzanasInspec">
                            </label>
                        </div>
                    </section>
                    <section class="col col-sm-12 col-md-6 col-lg-4">
                        <label class="txt-color-blue font-md">
                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Manzanas Posit.
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
                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Depósitos Inspec.
                        </label>
                        <div class="">
                            <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                                <input type="number" name="depositosInspec" id="depositosInspec">
                            </label>
                        </div>
                    </section>
                    <section class="col col-sm-12 col-md-6 col-lg-4">
                        <label class="txt-color-blue font-md">
                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Depósitos Posit.
                        </label>
                        <div class="">
                            <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                                <input type="number" name="depositosPositovos" id="depositosPositovos">
                            </label>
                        </div>
                    </section>
                    <section class="col col-sm-12 col-md-6 col-lg-4">
                        <label class="txt-color-blue font-md">
                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>Pupas Positivas
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
                        <label class="txt-color-blue font-md">No Abati
                        </label>
                        <div class="">
                            <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                                <input type="number" name="noAbati" id="noAbati">
                            </label>
                        </div>
                    </section>
                    <section class="col col-sm-12 col-md-6 col-lg-4">
                        <label class=" txt-color-blue font-md">No Elimin
                        </label>
                        <div class="">
                            <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                                <input type="number" name="noElimni" id="noElimni">
                            </label>
                        </div>
                    </section>
                    <section class="col col-sm-12 col-md-6 col-lg-4">
                        <label class="txt-color-blue font-md">No Neutr
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
                            Fecha Abat
                        </label>
                        <div class="input-group">
                            <input path="fecAbat" type="text"
                                   name="fecAbat" id="fecAbat"
                                   placeholder="dd/mm/aaaa"
                                   class="form-control datepicker midatepicker"
                                   data-dateformat="dd/mm/yy"/>
                                        <span class="input-group-addon"> <i class="fa fa-calendar fa-fw"></i>
                                        </span>
                        </div>
                    </section>
                    <section class="col col-sm-12 col-md-6 col-lg-4">
                        <label class="txt-color-blue font-md">
                            Fecha Report
                        </label>
                        <div class="input-group">
                            <input path="fecReport" type="text"
                                   name="fecReport" id="fecReport"
                                   placeholder="dd/mm/aaaa"
                                   class="form-control datepicker midatepicker"
                                   data-dateformat="dd/mm/yy"/>
                                        <span class="input-group-addon"> <i class="fa fa-calendar fa-fw"></i>
                                        </span>
                        </div>
                    </section>
                    <section class="col col-sm-12 col-md-6 col-lg-4">
                        <label class="txt-color-blue font-md">
                            Fecha V. Ent
                        </label>
                        <div class="input-group">
                            <input path="fecVent" type="text"
                                   name="fecVent" id="fecVent"
                                   placeholder="dd/mm/aaaa"
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
                <button type="button" class="btn btn-primary" id="btnGuardarDetalle">
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
    <!-- JQUERY VALIDATE -->
    <spring:url value="/resources/js/plugin/jquery-validate/jquery.validate.min.js" var="validate" />
    <script src="${validate}"></script>

    <!-- jQuery Validate datepicker -->
    <spring:url value="/resources/js/plugin/jquery-validate-datepicker/jquery.ui.datepicker.validation.min.js" var="jQueryValidateDatepicker"/>
    <script src="${jQueryValidateDatepicker}"></script>

    <!-- jQuery Selecte2 Input -->
    <spring:url value="/resources/js/plugin/select2/select2.min.js" var="selectPlugin"/>
    <script src="${selectPlugin}"></script>

    <!-- Data table -->
    <spring:url value="/resources/js/plugin/datatables/jquery.dataTables-cust.min.js" var="dataTablesMin"/>
    <script src="${dataTablesMin}"></script>
    <spring:url value="/resources/js/plugin/datatables/ColReorder.min.js" var="colReorderMin"/>
    <script src="${colReorderMin}"></script>
    <spring:url value="/resources/js/plugin/datatables/ColVis.min.js" var="ColVisMin"/>
    <script src="${ColVisMin}"></script>
    <spring:url value="/resources/js/plugin/datatables/FixedColumns.min.js" var="TableTools"/>
    <script src="${TableTools}"></script>
    <spring:url value="/resources/js/plugin/datatables/DT_bootstrap.js" var="DTB"/>
    <script src="${DTB}"></script>
    <spring:url value="/resources/js/plugin/datatables/ZeroClipboard.js" var="ZC"/>
    <script src="${ZC}"></script>

    <!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->

    <!-- END PAGE LEVEL SCRIPTS -->
	<script>
	    $(function () {
	    	$("li.entoaddaedes").addClass("active");
	    });
	</script>
<script type="text/javascript">
$(document).ready(function(){
    pageSetUp();

    $("#mensaje").hide();
    //mostrar modal detalle encuesta
    $("#openModal").click(function(){
        limpiarTextos();
        mostrarModalDetalle();
    });

    function ocultarModalDetalle(){
        $('#myModal').modal('hide');
    }
    function mostrarModalDetalle(){
        var $valid = $("#frmPrincipal").valid();
        if (!$valid) {
            $formPrincipal.focusInvalid();
            return false;
        }else {
            $("#myModal").modal({
                show: true
            });
        }
    }

    $("#codSilais").val("${maestro.entidadesAdtva.codigo}").change();
    $("#codUnidadSalud > option[value="+ ${maestro.unidadSalud.codigo} +"]").prop("selected",true).change();
    $("#codigoDepartamento > option[value="+ ${maestro.departamento.codigoNacional} +"]").attr("selected",true).change();
    $("#codigoMunicipio > option[value="+ ${maestro.municipio.codigoNacional} +"]").attr("selected",true).change();
    $("#codOrdinal > option[value='"+ "${maestro.ordinalEncuesta.codigo}"+"']").attr("selected",true).change();
    $("#codProcedencia > option[value='"+ "${maestro.procedencia.codigo}" +"']").attr("selected",true).change();
    $("#codigoArea").val("${maestro.codArea}").change();
    $("#codigoDistrito").val("${maestro.codDistrito}").change();
    $("#fecInicioEncuesta").val("${fechaInicioEncuesta}");
    $("#fecFinEncuesta").val("${fechaFinEncuesta}");
    $("#mesEpi").val("${maestro.mesEpi}");
    $("#anioEpi").val("${maestro.anioEpi}");
    $("#semanaEpi").val("${maestro.semanaEpi}");
    cargarDetalle("${maestro.encuestaId}");
    <!-- al seleccionar departamento -->
    $('#codigoDepartamento').change(function(){
        $.getJSON('${municipiosURL}', {
            departamentoId: $(this).val(),
            ajax: 'true'
        }, function(data){
            var html = null;
            var len = data.length;
            //html += '<option value="">Seleccione...</option>';
            for(var i = 0; i < len; i++){
                html += '<option value="' + data[i].codigoNacional + '">'
                        + data[i].nombre
                        + '</option>';
                html += '</option>';
            }
            $('#codigoMunicipio').html(html);
        })
    });
    <!-- al seleccionar municipio -->
    $('#codigoMunicipio').change(function(){
        $.getJSON('${comunidadesURL}', {
            municipioId: $(this).val(),
            ajax: 'true'
        }, function(data){
            var html = null;
            var len = data.length;
            html += '<option value="">Seleccione...</option>';
            for(var i = 0; i < len; i++){
                html += '<option value="' + data[i].codigo + '">'
                        + data[i].nombre
                        + '</option>';
                //html += '</option>';
            }
            $('#codigoLocalidad').html(html);
        });
        $.getJSON('${distritosURL}', {
            codMunicipio: $(this).val(),
            ajax: 'true'
        }, function(data){
            var html = null;
            var len = data.length;
            html += '<option value="">Seleccione...</option>';
            for(var i = 0; i < len; i++){
                html += '<option value="' + data[i].codigo + '">'
                        + data[i].valor
                        + '</option>';
                html += '</option>';
            }
            $('#codigoDistrito').html(html);
        });
        $.getJSON('${areasURL}', {
            codMunicipio: $(this).val(),
            ajax: 'true'
        }, function(data){
            var html = null;
            var len = data.length;
            html += '<option value="">Seleccione...</option>';
            for(var i = 0; i < len; i++){
                html += '<option value="' + data[i].codigo + '">'
                        + data[i].valor
                        + '</option>';
                html += '</option>';
            }
            $('#codigoArea').html(html);
        })
    });
    <!-- al seleccionar SILAIS -->
    $('#codSilais').change(function(){
        $.getJSON('${unidadesURL}', {
            silaisId: $(this).val(),
            ajax: 'true'
        }, function(data){
            var html = null;
            var len = data.length;
            html += '<option value="">Seleccione...</option>';
            for(var i = 0; i < len; i++){
                html += '<option value="' + data[i].codigo + '">'
                        + data[i].nombre
                        + '</option>';
                html += '</option>';
            }
            $('#codUnidadSalud').html(html);
        })
    });
    <!-- al seleccionar localidad -->
    $('#codigoLocalidad').change(function() {
        if ($(this).val() != $("#codLocalidadEdicion").val() && ($(this).val().length > 0 )){ //&& !$("#codLocalidadEdicion").val().length > 0
            var encuestaObj = {};
            encuestaObj['idLocalidad'] = $(this).val();
            encuestaObj['idMaestroEncuesta'] = "${maestro.encuestaId}";
            //encuestaObj['maestroEncuesta'] = maestro;
            $.getJSON('${existeLocalidadURL}', {
                datosEncuesta: JSON.stringify(encuestaObj),
                ajax: 'true'
            }, function (data) {
                var html = null;
                var len = data.length;
                if (len > 0) {
                    html = '<div class="alert alert-block alert-warning"> ' +
                            '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                            '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                            '<p> ' +
                            ' Localidad ya fue agregada con anterioridad al maestro de la encuesta informada ' +
                            '</p> ' +
                            '</div>';
                    mostrarMensaje(html);
                    setTimeout(function(){$("#codigoLocalidad").val('').change();}, 2000);
                } else {
                    ocultarMensaje();
                }
            })
        }else {
            setTimeout(ocultarMensaje(), 2000);
        }
    });

    function mostrarMensaje(msgHtml){
        $("#mensaje").html(msgHtml);
        $("#mensaje").show("slow");
        $("#mensaje").focus();
    }
    function ocultarMensaje(){
        $("#mensaje").hide("slow");
    }
    //guardar detalle desde Pop UP
    $("#btnGuardarDetalle").click(function() {
        var $valid = $("#frmDetalleEncuesta").valid();
        if (!$valid) {
            $formDetalleEncuesta.focusInvalid();
            return false;
        }else {
            var maestro = {
                encuestaId: "${maestro.encuestaId}", // se pasa el id del maestro que se esta editando,
                codSilais: $('#codSilais option:selected').val(),
                codDepartamento: $('#codigoDepartamento option:selected').val(),
                codMunicipio: $('#codigoMunicipio option:selected').val(),
                codDistrito: $('#codigoDistrito option:selected').val(),
                codArea: $('#codigoArea option:selected').val(),
                codUnidadSalud: $('#codUnidadSalud option:selected').val(),
                codProcedencia: $('#codProcedencia option:selected').val(),
                feInicioEncuesta: $('#fecInicioEncuesta').val(),
                feFinEncuesta: $('#fecFinEncuesta').val(),
                codOrdinalEncu: $('#codOrdinal option:selected').val(),
                codModeloEncu: 1,
                semanaEpi: $('#semanaEpi').val(),
                mesEpi: $('#mesEpi').val(),
                anioEpi: $('#anioEpi').val(),
                usuarioRegistroId: 1
            };
            var detalle = {
                detaEncuestaId: $("#idDetalleEditar").val(),
                codLocalidad: $('#codigoLocalidad option:selected').val(),
                viviendaInspeccionada: $('#viviendasInspec').val(),
                viviendaPositiva: $('#viviendasPositivas').val(),
                manzanaInspeccionada: $('#manzanasInspec').val(),
                manzanaPositiva: $('#manzanasPositivas').val(),
                depositoInspeccionado: $('#depositosInspec').val(),
                depositoPositivo: $('#depositosPositovos').val(),
                pupaPositiva: $('#pupasPositivas').val(),
                noAbatizado: $('#noAbati').val(),
                noEliminado: $('#noElimni').val(),
                noNeutralizado: $('#noNeutr').val(),
                feAbatizado: $('#fecAbat').val(),
                feRepot: $('#fecReport').val(),
                feVEnt: $('#fecVent').val(),
                usuarioRegistroId: 1
            };
            var esEdicion = ($("#idDetalleEditar").val()!=null && $("#idDetalleEditar").val().trim().length > 0);
            var encuestaObj = {};
            encuestaObj['idMaestro'] = "${maestro.encuestaId}";
            encuestaObj['mensaje'] = '';
            encuestaObj['maestro'] = maestro;
            encuestaObj['detalle'] = detalle;
            var html = null;
            $.ajax(
                    {
                        url: "${editarAedesURL}",
                        type: 'POST',
                        dataType: 'json',
                        data: JSON.stringify(encuestaObj),
                        contentType: 'application/json',
                        mimeType: 'application/json',
                        success: function (data) {
                            if (data.mensaje.length > 0){
                                html = '<div class="alert alert-block alert-warning"> ' +
                                        '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                                        '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                                        '<p> '+data.mensaje+
                                        '</p> ' +
                                        '</div>';
                            }else {
                                cargarDetalle(data.idMaestro);
                                limpiarTextos();
                                html = '<div class="alert alert-block alert-success"> ' +
                                        '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                                        '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                                        '<p> ';
                                if (esEdicion) {
                                    html = html + ' Localidad actualizada con éxito ';
                                    setTimeout(function () {
                                        ocultarModalDetalle()
                                    }, 5000);
                                } else {
                                    html = html + ' Localidad agregada con éxito ';
                                }
                                html = html + '</p> ' +
                                        '</div>';
                            }
                            mostrarMensaje(html);

                        },
                        error: function (data, status, er) {
                            alert("error: " + data + " status: " + status + " er:" + er);
                        }
                    }
            )
        }
    });

    function cargarDetalle(idMaestro){
        $.ajax({
            type: "GET",
            url: "${recargarDetalleEncuestasURL}",
            data: {idMaestroEncuesta: idMaestro},
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (response) {
                $('#dtDetalle').empty();
                $('#dtDetalle').append('<thead><tr><th style="background-color: #3276b1"></th><th style="background-color: #3276b1"></th>' +
                        '<th colspan="3" style="text-align: center; background-color: #3276b1;" class="font-md txt-color-white">Viviendas</th>' +
                        '<th colspan="3" style="text-align: center; background-color: #3276b1" class="font-md txt-color-white">Manzanas</th>' +
                        '<th colspan="3" style="text-align: center; background-color: #3276b1" class="font-md txt-color-white">Depósitos</th>' +
                        '<th colspan="3" style="text-align: center; background-color: #3276b1" class="font-md txt-color-white">Índice</th>' +
                        '<th colspan="3" style="text-align: center; background-color: #3276b1" class="font-md txt-color-white">Fechas</th>' +
                        '<th colspan="1" style="text-align: center; background-color: #3276b1" class="font-md txt-color-white">No</th>' +
                        '<th colspan="1" style="text-align: center; background-color: #3276b1" class="font-md txt-color-white">No</th>' +
                        '<th colspan="1" style="text-align: center; background-color: #3276b1" class="font-md txt-color-white">No</th>' +
                        '<th style="background-color: #3276b1"></th></tr>'+
                        '<tr><th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">No</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Localidad</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Insp</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Posit</p></th>'+
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Índice</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Insp</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Posit</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Índice</p></th>'+
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Insp</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Posit</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Índice</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Bretes</p></th>'+
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Pupas</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Ipupas</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Abat</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Report</p></th>'+
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">V. Ent</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Abati</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Elimi</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Neutr</p></th>'+
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white"></p></th></tr></thead><tbody>');
                var nTotalViviendasInspec    = 0;
                var nTotalViviendasPosit     = 0;
                var nTotalManzanasInspec     = 0;
                var nTotalManzanasPosit      = 0;
                var nTotalDepositosInspec    = 0;
                var nTotalDepositosPosit     = 0;
                var nTotalPupas              = 0;

                for (var i = 0; i < response.length; i++) {
                    nTotalViviendasInspec = nTotalViviendasInspec + response[i][0].viviendasInspec;
                    nTotalViviendasPosit     = nTotalViviendasPosit + response[i][0].viviendasPosit;
                    nTotalManzanasInspec     = nTotalManzanasInspec + response[i][0].manzanasInspec;
                    nTotalManzanasPosit      = nTotalManzanasPosit + response[i][0].manzanasPosit;
                    nTotalDepositosInspec    = nTotalDepositosInspec + response[i][0].depositosInspec;
                    nTotalDepositosPosit     = nTotalDepositosPosit + response[i][0].depositosPosit;
                    nTotalPupas              = nTotalPupas + response[i][0].pupasPosit;
                    $('#dtDetalle').append('<tr>' +
                            '<td class="font-sm">' + (i+1) + '</td>' +
                            '<td class="font-sm">' + response[i][0].localidad + '</td>'+
                            '<td class="font-sm" style="text-align: right">' + response[i][0].viviendasInspec + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].viviendasPosit + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].indiceViviendas + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].manzanasInspec + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].manzanasPosit + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].indiceManzanas + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].depositosInspec + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].depositosPosit + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].indiceDepositos + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].indiceBrete + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].pupasPosit + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].indicePupas + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].fechaAbat + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].fechaReport + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].fechaVEnt + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].noAbati + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].noElimin + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].noNeutr + '</td>' +
                            '<td class="font-sm" style="text-align: center">' +
                            '<a data-toggle="modal" class="btn btn-default btn-xs editDetalle" data-id='+response[i][0].detaEncuestaId+'><i class="fa fa-pencil fa-fw"></i></a>'+
                            '</td>'+
                            '</tr>');
                }

                var nTotalViviendasIndice    = parseFloat((nTotalViviendasPosit / nTotalViviendasInspec)*100).toFixed(1);
                var nTotalManzanasIndice     = parseFloat((nTotalManzanasPosit / nTotalManzanasInspec)*100).toFixed(1);
                var nTotalDepositosIndice    = parseFloat((nTotalDepositosPosit/nTotalDepositosInspec)*100).toFixed(1);
                var nTotalIndiceBrete        = parseFloat((nTotalPupas/nTotalViviendasInspec)*100).toFixed(1);
                var nTotalIndiceIPupa		 = parseFloat((nTotalDepositosPosit/nTotalViviendasInspec)*100).toFixed(1);

                $('#dtDetalle').append('</tbody><tfoot><th style="text-align: right; background-color: #86b4dd"></th><th style="text-align: center; background-color: #86b4dd">TOTALES</th>' +
                    //'<th>'+nTotalViviendasInspec+'</th><th>'+nTotalViviendasPosit+'</th><th>'+nTotalViviendasIndice+'</th>'+
                    //'<th>'+nTotalManzanasInspec+'</th><th>'+nTotalManzanasPosit+'</th><th>'+nTotalManzanasIndice+'</th>'+
                    //'<th>'+nTotalDepositosInspec+'</th><th>'+nTotalDepositosPosit+'</th><th>'+nTotalDepositosIndice+'</th>'+
                    //'<th>'+nTotalIndiceBrete+'</th><th>'+nTotalPupas+'</th><th>'+nTotalIndiceIPupa+'</th></tfoot>');
                        '<th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalViviendasInspec" style="font-weight: bold">'+nTotalViviendasInspec+'</label></th> '+
                        '<th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalViviendasPosit"  style="font-weight: bold"><strong>'+nTotalViviendasPosit+'</strong></label></th> '+
                        '<th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalViviendasIndice" style="font-weight: bold">'+nTotalViviendasIndice+'</label></th> '+
                        '<th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalManzanasInspec" style="font-weight: bold">'+nTotalManzanasInspec+'</label></th> '+
                        '<th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalManzanasPosit" style="font-weight: bold">'+nTotalManzanasPosit+'</label></th> '+
                        '<th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalManzanasIndice" style="font-weight: bold">'+nTotalManzanasIndice+'</label></th> '+
                        '<th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalDepositosInspec" style="font-weight: bold">'+nTotalDepositosInspec+'</label></th> '+
                        '<th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalDepositosPosit" style="font-weight: bold">'+nTotalDepositosPosit+'</label></th> '+
                        '<th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalDepositosIndice" style="font-weight: bold">'+nTotalDepositosIndice+'</label></th> '+
                        '<th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalIndiceBrete" style="font-weight: bold">'+nTotalIndiceBrete+'</label></th> '+
                        '<th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalPupas" style="font-weight: bold">'+nTotalPupas+'</label></th> '+
                        '<th colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalIndiceIPupa" style="font-weight: bold">'+nTotalIndiceIPupa+'</label></th> '+
                        '<th colspan="7" rowspan="1" style="text-align: center; background-color: #86b4dd"></th></tfoot>');

                $('#dtDetalle').dataTable({
                    "sPaginationType": "bootstrap",
                    "bFilter": true,
                    "bPaginate": true,
                    //"bRetrieve":true,
                    "bDestroy": true,
                    "sPaging": true,
                    "sDom":  "<fl<t>ip>"
                });
                $("#totalViviendasInspec").text(nTotalViviendasInspec);
                $("#totalViviendasPosit").text(nTotalViviendasPosit);
                $("#totalViviendasIndice").text(nTotalViviendasIndice);
                $("#totalManzanasInspec").text(nTotalManzanasInspec);
                $("#totalManzanasPosit").text(nTotalManzanasPosit);
                $("#totalManzanasIndice").text(nTotalManzanasIndice);
                $("#totalDepositosInspec").text(nTotalDepositosInspec);
                $("#totalDepositosPosit").text(nTotalDepositosPosit);
                $("#totalDepositosIndice").text(nTotalDepositosIndice);
                $("#totalPupas").text(nTotalPupas);
                $("#totalIndiceBrete").text(nTotalIndiceBrete);
                $("#totalIndiceIPupa").text(nTotalIndiceIPupa);

                $(".editDetalle").click(function(){
                    limpiarTextos();
                    $('#idDetalleEditar').val($(this).data('id'));
                    cargarInfoModalDetalle($(this).data('id'));
                    mostrarModalDetalle();
                });
                $(".pagination").click(function() {
                    $(".editDetalle").click(function () {
                        limpiarTextos();
                        $('#idDetalleEditar').val($(this).data('id'));
                        cargarInfoModalDetalle($(this).data('id'));
                        mostrarModalDetalle();
                    });
                });

            },
            error: function (result) {
                alert("Error " + result.status + '' + result.statusText);
            }
        });
    }

    function cargarInfoModalDetalle(idDetalleEditar){
        $.ajax({
            type: "GET",
            url: "${recuperarDetalleAedes}",
            data: {idDetalleEncu: idDetalleEditar},
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (response) {
                $("#codLocalidadEdicion").val(response.codLocalidad);
                $("#viviendasInspec").val(response.viviendasInspec);
                $("#viviendasPositivas").val(response.viviendasPosit);
                $("#manzanasInspec").val(response.manzanasInspec);
                $("#manzanasPositivas").val(response.manzanasPosit);
                $("#depositosInspec").val(response.depositosInspec);
                $("#depositosPositovos").val(response.depositosPosit);
                $("#pupasPositivas").val(response.pupasPosit);
                $("#fecAbat").val(response.fechaAbat);
                $("#fecReport").val(response.fechaRepot);
                $("#fecVent").val(response.fechaVEnt);
                $("#noAbati").val(response.noAbati);
                $("#noElimni").val(response.noElimin);
                $("#noNeutr").val(response.noNeutr);
                $("#codigoLocalidad").val($("#codLocalidadEdicion").val()).change();
            },
            error: function (result) {

            }
        });
    }
    function limpiarTextos(){
        $("#idDetalleEditar").val('');
        $('#codigoLocalidad option:first').prop("selected", true).change();
        $("#codLocalidadEdicion").val('');
        $("#viviendasInspec").val('');
        $("#viviendasPositivas").val('');
        $("#manzanasInspec").val('');
        $("#manzanasPositivas").val('');
        $("#depositosInspec").val('');
        $("#depositosPositovos").val('');
        $("#pupasPositivas").val('');
        $("#noAbati").val('');
        $("#noElimni").val('');
        $("#noNeutr").val('');
        $("#fecReport").val('');
        $("#fecVent").val('');
        $("#fecAbat").val('');

    }

    function actualizarTotales(){
        var viviendasInspec     = $("#viviendasInspec").val();
        var viviendasPositivas  = $("#viviendasPositivas").val();
        var viviendasIndice     = ((viviendasPositivas / viviendasInspec) * 100).toFixed(1);
        var manzanasInspec      = $("#manzanasInspec").val();
        var manzanasPositivas   = $("#manzanasPositivas").val();
        var manzanasIndice      = ((manzanasPositivas / manzanasInspec) * 100).toFixed(1);
        var depositosInspec     = $("#depositosInspec").val();
        var depositosPositovos  = $("#depositosPositovos").val();
        var depositosIndice     = ((depositosPositovos / depositosInspec) * 100).toFixed(1);
        var pupasPositivas      = $("#pupasPositivas").val();
        var pupasIndices        = ((pupasPositivas / viviendasInspec) * 100).toFixed(1);
        var breteauIndices      = ((depositosPositovos / viviendasInspec) * 100).toFixed(1);
        var totalViviendasInspec    = $("#totalViviendasInspec").text();
        var totalViviendasPosit     = $("#totalViviendasPosit").text();
        var totalViviendasIndice    = $("#totalViviendasIndice").text();
        var totalManzanasInspec     = $("#totalManzanasInspec").text();
        var totalManzanasPosit      = $("#totalManzanasPosit").text();
        var totalManzanasIndice     = $("#totalManzanasIndice").text();
        var totalDepositosInspec    = $("#totalDepositosInspec").text();
        var totalDepositosPosit     = $("#totalDepositosPosit").text();
        var totalDepositosIndice    = $("#totalDepositosIndice").text();
        var totalPupas              = $("#totalPupas").text();
        var totalIndiceBrete        = $("#totalIndiceBrete").text();
        var totalIndiceIPupa		= $("#totalIndiceIPupa").text();

        var nTotalViviendasInspec    = parseInt(totalViviendasInspec) + parseInt(viviendasInspec);
        var nTotalViviendasPosit     = parseInt(totalViviendasPosit) + parseInt(viviendasPositivas);
        var nTotalViviendasIndice    = parseFloat((nTotalViviendasPosit / nTotalViviendasInspec)*100).toFixed(1);
        var nTotalManzanasInspec     = parseInt(totalManzanasInspec) + parseInt(manzanasInspec);
        var nTotalManzanasPosit      =parseInt(totalManzanasPosit) + parseInt(manzanasPositivas);
        var nTotalManzanasIndice     = parseFloat((nTotalManzanasPosit / nTotalManzanasInspec)*100).toFixed(1);
        var nTotalDepositosInspec    = parseInt(totalDepositosInspec) + parseInt(depositosInspec);
        var nTotalDepositosPosit     = parseInt(totalDepositosPosit) + parseInt(depositosPositovos);
        var nTotalDepositosIndice    = parseFloat((nTotalDepositosPosit/nTotalDepositosInspec)*100).toFixed(1);
        var nTotalPupas              = parseInt(totalPupas) + parseInt(pupasPositivas);
        var nTotalIndiceBrete        = parseFloat((nTotalPupas/nTotalViviendasInspec)*100).toFixed(1);
        var nTotalIndiceIPupa		 = parseFloat((nTotalDepositosPosit/nTotalViviendasInspec)*100).toFixed(1);

        $("#totalViviendasInspec").text(nTotalViviendasInspec);
        $("#totalViviendasPosit").text(nTotalViviendasPosit);
        $("#totalViviendasIndice").text(nTotalViviendasIndice);
        $("#totalManzanasInspec").text(nTotalManzanasInspec);
        $("#totalManzanasPosit").text(nTotalManzanasPosit);
        $("#totalManzanasIndice").text(nTotalManzanasIndice);
        $("#totalDepositosInspec").text(nTotalDepositosInspec);
        $("#totalDepositosPosit").text(nTotalDepositosPosit);
        $("#totalDepositosIndice").text(nTotalDepositosIndice);
        $("#totalPupas").text(nTotalPupas);
        $("#totalIndiceBrete").text(nTotalIndiceBrete);
        $("#totalIndiceIPupa").text(nTotalIndiceIPupa);
    };

    var $formPrincipal = $("#frmPrincipal").validate({
        rules: {
            codSilais: {
                required: true
            },
            codigoDepartamento: {
                required: true
            },
            codigoMunicipio: {
                required: true
            },
            codUnidadSalud: {
                required: true
            },
            fecInicioEncuesta: {
                required: true,
                dpDate: true,
                dpCompareDate: ['before', '#fecFinEncuesta', 'notAfter', "${fechaHoy}" ]
            },
            fecFinEncuesta: {
                required: true,
                dpDate: true,
                dpCompareDate: {after: '#fecInicioEncuesta', 'notAfter': "${fechaHoy}"}

            },
            codOrdinal: {
                required: true
            },
            codProcedencia: {
                required: true
            },
            semanaEpi: {
                digits: true,
                maxlength: 2
            },
            mesEpi: {
                digits: true,
                maxlength: 2
            },
            anioEpi: {
                digits: true,
                maxlength: 4
            }
        },
        messages: {
            codSilais: {
                required: 'Debe seleccionar un SILAIS'
            },
            codigoDepartamento: {
                required: 'Debe seleccionar un departamento'
            },
            codigoMunicipio: {
                required: 'Debe seleccionar un municipio'
            },
            codUnidadSalud: {
                required: 'Debe seleccionar unidad de salud'
            },
            fecInicioEncuesta: {
                required: 'Debe seleccionar una fecha de inicio de encuesta'
            },
            fecFinEncuesta: {
                required: 'Debe seleccionar una fecha de fin de encuesta'
            },
            codOrdinal: {
                required: 'Debe seleccionar el ordinal de la encuesta'
            },
            codProcedencia: {
                required: 'Debe seleccionar la procedencia de la encuesta'
            },
            semanaEpi: {
                digits: 'Debe ingresar únicamente valores positivos',
                maxlength: 'Sólo se permiten dos dígitos'
            },
            mesEpi: {
                digits: 'Debe ingresar únicamente valores positivos',
                maxlength: 'Sólo se permiten dos dígitos'
            },
            anioEpi: {
                digits: 'Debe ingresar únicamente valores positivos',
                maxlength: 'Sólo se permiten cuatro dígitos'
            }
        },
        // Do not change code below
        errorPlacement: function (error, element) {
            error.insertAfter(element.parent());

        }
    });

    var $formDetalleEncuesta = $("#frmDetalleEncuesta").validate({
        rules: {
            codigoLocalidad:{
                required: true
            },
            viviendasInspec:{
                required: true,
                digits: true
            },
            viviendasPositivas:{
                required: true,
                digits: true
            },
            manzanasInspec:{
                required: true,
                digits: true
            },
            manzanasPositivas:{
                required: true,
                digits: true
            },
            depositosInspec:{
                required: true,
                digits: true
            },
            depositosPositovos:{
                required: true,
                digits: true
            },
            pupasPositivas:{
                required: true,
                digits: true
            },
            noAbati:{
                digits: true
            },
            noElimni:{
                digits: true
            },
            noNeutr: {
                digits: true
            },
            fecAbat: {
                dpDate: true
            },
            fecReport: {
                dpDate: true
            },
            fecVent: {
                dpDate: true
            }
        },
        messages: {
            codigoLocalidad:{
                required: 'Debe seleccionar una localidad'
            },
            viviendasInspec:{
                required: 'Debe ingresar cantidad de viviendas inspeccionadas',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            viviendasPositivas:{
                required: 'Debe ingresar cantidad de viviendas positivas',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            manzanasInspec:{
                required: 'Debe ingresar cantidad de manzanas inspeccionadas',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            manzanasPositivas:{
                required: 'Debe ingresar cantidad de manzanas positivas',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            depositosInspec:{
                required: 'Debe ingresar cantidad de depósitos inspeccionados',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            depositosPositovos:{
                required: 'Debe ingresar cantidad de depósitos positivos',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            pupasPositivas:{
                required: 'Debe ingresar cantidad de pupas positivas',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            noAbati:{
                digits: 'Debe ingresar únicamente valores positivos'
            },
            noElimni:{
                digits: 'Debe ingresar únicamente valores positivos'
            },
            noNeutr: {
                digits: 'Debe ingresar únicamente valores positivos'
            }
        },
        // Do not change code below
        errorPlacement: function (error, element) {
            error.insertAfter(element.parent());

        }
    });

    $('#fecInicioEncuesta').change(function () {
        var fecha = $('#fecInicioEncuesta').val();
        var arr = fecha.split('/');
        $('#mesEpi').val(arr[1]);
        $('#anioEpi').val(arr[2]);
        $.ajax({
            type: "GET",
            url: "${semanaEpidemiologicaURL}",
            data: {fechaValidar: fecha},
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (response) {
                $('#semanaEpi').val(response.noSemana);
            },
            error: function (result) {
                $('#semanaEpi').val("");
            }
        });
    });

});
</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>