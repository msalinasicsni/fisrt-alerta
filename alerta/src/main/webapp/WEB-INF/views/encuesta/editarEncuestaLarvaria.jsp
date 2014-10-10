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
<c:url value="/encuesta/guardarLarvariaAedes" var="encuesta" />
<c:url var="recargarDetalleEncuestasURL" value="/encuesta/obtenerEncuestasLarvariasMae"/>
<c:url var="existeLocalidadURL" value="/encuesta/comunidadExisteEncuestaLarva"/>
<c:url var="existeMaestroURL" value="/encuesta/existeMaestroEncuestaLarva"/>
<c:url var="semanaEpidemiologicaURL" value="/api/v1/semanaEpidemiologica"/>
<c:url var="editarEncuestaURL" value="/encuesta/edit"/>
<c:url var="editarLarvariaURL" value="/encuesta/actualizarLarvaria"/>
<c:url var="recuperarDetalleLarva" value="/encuesta/recuperarDetalleLarvaria"/>

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
                                            <!--<div class="col-xs-10 col-sm-10 col-md-10 col-lg-10">
                                                <a class="btn btn-primary btn-lg pull-right header-btn hidden-mobile" id="btnNuevoRegistro"><i class="fa fa-circle-arrow-up fa-lg"></i>Nuevo Registro</a>
                                                </div>-->
                                            <div class="col-xs-2 col-sm-2 col-md-2 col-lg-12">
                                                <input id="idMaestroEditado" type="hidden"/>
                                                <!-- Button trigger modal -->
                                                <a data-toggle="modal" href="#" class="btn btn-success btn-lg pull-right header-btn hidden-mobile" id="openModal"><i class="fa fa-circle-arrow-up fa-lg"></i>Agregar Localidad</a>
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
                                <h2>Detalle de Encuesta</h2>
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
                                    <form class="smart-form" novalidate="novalidate">
                                        <header>Indices de Recipientes Infestados</header>
                                    </form>
                                    <div class="widget-body-toolbar">
                                    </div>
                                    <table id="dtDetalleIndices" class="table table-striped table-bordered table-hover">
                                    </table>
                                    <form class="smart-form" novalidate="novalidate">
                                        <header>Distribución Especies Reportadas</header>
                                    </form>

                                    <div class="widget-body-toolbar">
                                    </div>
                                    <table id="dtDetalleDistribucion" class="table table-striped table-bordered table-hover">
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
	    	$("li.entoaddlarvae").addClass("active");
	    });
	</script>
<script type="text/javascript">
$(document).ready(function(){
    pageSetUp();
    /*
     $("li.home").removeClass("home").addClass("active");
     $("#myModal").on("shown.bs.modal", function(){
     $("#noAbati").focus();
     });
     */
    $("#mensaje").hide();

    //mostrar modal detalle encuesta
    $("#openModal").click(function(){
        limpiarCamposDetalle();
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
    cargarDetalles("${maestro.encuestaId}");

    <!-- al seleccionar departamento -->
    $('#codigoDepartamento').change(function(){
        if ($(this).val().length > 0) {
            if ($(this).val().length > 0) {
                $.getJSON('${municipiosURL}', {
                    departamentoId: $(this).val(),
                    ajax: 'true'
                }, function (data) {
                    var html = null;
                    var len = data.length;
                    html += '<option value="">Seleccione...</option>';
                    for (var i = 0; i < len; i++) {
                        html += '<option value="' + data[i].codigoNacional + '">'
                                + data[i].nombre
                                + '</option>';
                        html += '</option>';
                    }
                    $('#codigoMunicipio').html(html);
                })
            }
        }
    });
    <!-- al seleccionar municipio -->
    $('#codigoMunicipio').change(function(){
        if ($(this).val().length > 0) {
            $.getJSON('${comunidadesURL}', {
                municipioId: $(this).val(),
                ajax: 'true'
            }, function (data) {
                var html = null;
                var len = data.length;
                html += '<option value="">Seleccione...</option>';
                for (var i = 0; i < len; i++) {
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
            }, function (data) {
                var html = null;
                var len = data.length;
                html += '<option value="">Seleccione...</option>';
                for (var i = 0; i < len; i++) {
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
            }, function (data) {
                var html = null;
                var len = data.length;
                html += '<option value="">Seleccione...</option>';
                for (var i = 0; i < len; i++) {
                    html += '<option value="' + data[i].codigo + '">'
                            + data[i].valor
                            + '</option>';
                    html += '</option>';
                }
                $('#codigoArea').html(html);
            })
        }
    });

    <!-- al seleccionar SILAIS -->
    $('#codSilais').change(function(){
        if ($(this).val().length > 0) {
            $.getJSON('${unidadesURL}', {
                silaisId: $(this).val(),
                ajax: 'true'
            }, function (data) {
                var html = null;
                var len = data.length;
                html += '<option value="">Seleccione...</option>';
                for (var i = 0; i < len; i++) {
                    html += '<option value="' + data[i].codigo + '">'
                            + data[i].nombre
                            + '</option>';
                    html += '</option>';
                }
                $('#codUnidadSalud').html(html);
            })
        }
    });

    <!-- al seleccionar localidad -->
    $('#codigoLocalidad').change(function() {
        if ($(this).val() != $("#codLocalidadEdicion").val() && ($(this).val().length > 0 )){ //&& !$("#codLocalidadEdicion").val().length > 0
            /*var maestro = {
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
             };*/
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
        }else{
            setTimeout(ocultarMensaje(), 2000);
        }
    });

    function mostrarMensaje(msgHtml){
        $("#mensaje").html(msgHtml);
        $("#mensaje").show("slow");
        $("#mensaje").focus();
    }
    function ocultarMensaje() {
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
                pilaInfestado: $('#txtPila').val(),
                llantaInfestado: $('#txtLlanta').val(),
                barrilInfestado: $('#txtBarril').val(),
                floreroInfestado: $('#txtFlorero').val(),
                bebederoInfestado: $('#txtBebedero').val(),
                artEspecialInfes: $('#txtArt_Esp').val(),
                otrosDepositosInfes: $('#txtO_Dep').val(),
                cisterInfestado: $('#txtCister').val(),
                inodoroInfestado: $('#txtInodoro').val(),
                barroInfestado: $('#txtBarro').val(),
                plantaInfestado: $('#txtPlanta').val(),
                arbolInfestado: $('#txtArbol').val(),
                pozoInfestado: $('#txtPozo').val(),
                especieAegypti: $('#txtAedesAegyti').val(),
                especieAlbopic: $('#txtAedesAlbopic').val(),
                especieCulexQuinque: $('#txtCulexQuinque').val(),
                especieCulexNigrip: $('#txtCulexNigrip').val(),
                especieCulexCoronat: $('#txtCulexCoronat').val(),
                especieCulexErratico: $('#txtCulexErratico').val(),
                especieCulexTarsalis: $('#txtCulexTarsalis').val(),
                especieCulexFatigans: $('#txtCulexFatigans').val(),
                especieCulexAlbim: $('#txtAnophAlbim').val(),
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
                        url: "${editarLarvariaURL}",
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
                                cargarDetalles(data.idMaestro);
                                limpiarCamposDetalle();
                                html = '<div class="alert alert-block alert-success"> ' +
                                        '<a class="close" data-dismiss="alert" href="#">×</a> ' +
                                        '<h4 class="alert-heading"><i class="fa fa-check-square-o"></i> Aviso!</h4>' +
                                        '<p> ';
                                if (esEdicion) {
                                    html = html + ' Localidad actualizada con éxito '
                                    setTimeout(function () {
                                        ocultarModalDetalle()
                                    }, 5000);
                                } else {
                                    html = html + ' Localidad agregada con éxito '
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

    function cargarDetalles(idMaestro){
        $.ajax({
            type: "GET",
            url: "${recargarDetalleEncuestasURL}",
            data: {idMaestroEncuesta: idMaestro},
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (response) {
                //var patientTable = $('#dtDetalle');
                $('#dtDetalleIndices').empty();
                $('#dtDetalleDistribucion').empty();

                $('#dtDetalleIndices').append('<thead>' +
                        '<tr><th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">No</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Localidad&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Pila</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Llanta</p></th>'+
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Barril</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Florero</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Bebed</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Art Esp</p></th>'+
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">O Dep</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Cister</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Inodo</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Barro</p></th>'+
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Planta</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Arbol</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Pozo</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Total</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Ed</p></th></tr></thead><tbody>');

                $('#dtDetalleDistribucion').append('<thead>' +
                        '<tr><th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">No</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Localidad&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Aedes Aegypti</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Aedes Albopic</p></th>'+
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Culex Quinque</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Culex Nigrip</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Culex Coronat</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Culex Erratico</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Culex Tarsalis</p></th>'+
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Culex Fatigans</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Anoph Albim</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Total</p></th>' +
                        '<th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Ed</p></th></tr></thead><tbody>');


                var nIndicePilas    = 0, nIndiceLlanta   = 0, nIndiceBarril   = 0, nIndiceFlorero  = 0,nIndiceBebedero = 0, nIndiceArtEspec = 0, nIndiceODep     = 0, nIndiceCister   = 0, nIndiceInodo    = 0;
                var nIndicePlanta   = 0, nIndiceBarro    = 0, nIndiceArbol    = 0,nIndicePozo     = 0, nIndiceAegypti  = 0, nIndiceAlbopic  = 0, nIndiceQuinque  = 0, nIndiceNigrip   = 0, nIndiceCoronat  = 0;
                var nIndiceErratico = 0, nIndiceTarsalis = 0, nIndiceFatigans = 0, nIndiceAlbim    = 0, nTotalPilas    = 0, nTotalLlanta   = 0, nTotalBarril   = 0, nTotalFlorero  = 0, nTotalBebedero = 0;
                var nTotalArtEspec = 0, nTotalODep     = 0, nTotalCister   = 0, nTotalInodo    = 0, nTotalPlanta   = 0, nTotalBarro    = 0, nTotalArbol    = 0, nTotalPozo     = 0, nTotalAegypti  = 0;
                var nTotalAlbopic  = 0, nTotalQuinque  = 0, nTotalNigrip   = 0, nTotalCoronat  = 0, nTotalErratico = 0, nTotalTarsalis = 0, nTotalFatigans = 0, nTotalAlbim    = 0, nTotalTotalIndice = 0, nTotalTotalDist=0;

                for (var i = 0; i < response.length; i++) {

                    var nTotalIndice = response[i][0].pilaInfestado + response[i][0].llantaInfestado + response[i][0].barrilInfestado + response[i][0].floreroInfestado + response[i][0].bebederoInfestado + response[i][0].artEspecialInfes +
                            response[i][0].otrosDepositosInfes + response[i][0].cisterInfestado + response[i][0].inodoroInfestado + response[i][0].barroInfestado  +response[i][0].plantaInfestado + response[i][0].arbolInfestado +response[i][0].pozoInfestado;
                    nTotalTotalIndice = nTotalTotalIndice + nTotalIndice;
                    nTotalPilas    =    nTotalPilas    + response[i][0].pilaInfestado;
                    nTotalLlanta   =    nTotalLlanta   + response[i][0].llantaInfestado ;
                    nTotalBarril   =    nTotalBarril   + response[i][0].barrilInfestado ;
                    nTotalFlorero  =    nTotalFlorero  + response[i][0].floreroInfestado ;
                    nTotalBebedero =    nTotalBebedero + response[i][0].bebederoInfestado ;
                    nTotalArtEspec =    nTotalArtEspec + response[i][0].artEspecialInfes;
                    nTotalODep     =    nTotalODep     + response[i][0].otrosDepositosInfes;
                    nTotalCister   =    nTotalCister   + response[i][0].cisterInfestado ;
                    nTotalInodo    =    nTotalInodo    + response[i][0].inodoroInfestado;
                    nTotalBarro    =    nTotalBarro    + response[i][0].barroInfestado ;
                    nTotalPlanta   =    nTotalPlanta   + response[i][0].plantaInfestado;
                    nTotalArbol    =    nTotalArbol    + response[i][0].arbolInfestado;
                    nTotalPozo     =    nTotalPozo     + response[i][0].pozoInfestado;
                    nTotalAegypti  =    nTotalAegypti  + response[i][0].especieAegypti;
                    nTotalAlbopic  =    nTotalAlbopic  + response[i][0].especieAlbopic;
                    nTotalQuinque  =    nTotalQuinque  + response[i][0].especieCulexQuinque;
                    nTotalNigrip   =    nTotalNigrip   + response[i][0].especieCulexNigrip  ;
                    nTotalCoronat  =    nTotalCoronat  + response[i][0].especieCulexCoronat ;
                    nTotalErratico =    nTotalErratico + response[i][0].especieCulexErratico;
                    nTotalTarsalis =    nTotalTarsalis + response[i][0].especieCulexTarsalis;
                    nTotalFatigans =    nTotalFatigans + response[i][0].especieCulexFatigans;
                    nTotalAlbim    =    nTotalAlbim    + response[i][0].especieCulexAlbim;

                    $('#dtDetalleIndices').append('<tr>' +
                            '<td class="font-sm">' + (i+1) + '</td>' +
                            '<td class="font-sm">' + response[i][0].localidad + '</td>'+
                            '<td class="font-sm" style="text-align: right">' + response[i][0].pilaInfestado + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].llantaInfestado + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].barrilInfestado + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].floreroInfestado + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].bebederoInfestado + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].artEspecialInfes + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].otrosDepositosInfes + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].cisterInfestado + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].inodoroInfestado + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].barroInfestado + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].plantaInfestado + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].arbolInfestado + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].pozoInfestado + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + nTotalIndice + '</td>' +
                            '<td class="font-sm" style="text-align: center">' +
                            '<a data-toggle="modal" class="btn btn-default btn-xs editDetalle" data-id='+response[i][0].detaEncuestaId+'><i class="fa fa-pencil fa-fw"></i></a>'+
                            '</td>'+
                            '</tr>');

                    var nTotalDistribucion = response[i][0].especieAegypti  +
                            response[i][0].especieAlbopic +
                            response[i][0].especieCulexQuinque +
                            response[i][0].especieCulexNigrip  +
                            response[i][0].especieCulexCoronat +
                            response[i][0].especieCulexErratico+
                            response[i][0].especieCulexTarsalis+
                            response[i][0].especieCulexFatigans+
                            response[i][0].especieCulexAlbim;
                    nTotalTotalDist = nTotalTotalDist + nTotalDistribucion;
                    $('#dtDetalleDistribucion').append('<tr>' +
                            '<td class="font-sm">' + (i+1) + '</td>' +
                            '<td class="font-sm">' + response[i][0].localidad + '</td>'+
                            '<td class="font-sm" style="text-align: right">' + response[i][0].especieAegypti + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].especieAlbopic + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].especieCulexQuinque + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].especieCulexNigrip + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].especieCulexCoronat + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].especieCulexErratico + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].especieCulexTarsalis + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].especieCulexFatigans + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + response[i][0].especieCulexAlbim + '</td>' +
                            '<td class="font-sm" style="text-align: right">' + nTotalDistribucion + '</td>' +
                            '<td class="font-sm" style="text-align: center">' +
                            '<a data-toggle="modal" class="btn btn-default btn-xs editDetalle" data-id='+response[i][0].detaEncuestaId+'><i class="fa fa-pencil fa-fw"></i></a>'+
                            '</td>'+
                            '</tr>');
                }

                nIndicePilas = parseFloat((nTotalPilas / nTotalTotalIndice)*100).toFixed(1);
                nIndiceLlanta = parseFloat((nTotalLlanta / nTotalTotalIndice)*100).toFixed(1);
                nIndiceBarril = parseFloat((nTotalBarril / nTotalTotalIndice)*100).toFixed(1);
                nIndiceFlorero = parseFloat((nTotalFlorero / nTotalTotalIndice)*100).toFixed(1);
                nIndiceBebedero = parseFloat((nTotalBebedero / nTotalTotalIndice)*100).toFixed(1);
                nIndiceArtEspec = parseFloat((nTotalArtEspec / nTotalTotalIndice)*100).toFixed(1);
                nIndiceODep = parseFloat((nTotalODep / nTotalTotalIndice)*100).toFixed(1);
                nIndiceCister = parseFloat((nTotalCister / nTotalTotalIndice)*100).toFixed(1);
                nIndiceInodo = parseFloat((nTotalInodo / nTotalTotalIndice)*100).toFixed(1);
                nIndicePlanta = parseFloat((nTotalPlanta / nTotalTotalIndice)*100).toFixed(1);
                nIndiceBarro = parseFloat((nTotalBarro / nTotalTotalIndice)*100).toFixed(1);
                nIndiceArbol = parseFloat((nTotalArbol / nTotalTotalIndice)*100).toFixed(1);
                nIndicePozo = parseFloat((nTotalPozo / nTotalTotalIndice)*100).toFixed(1);
                nIndiceAegypti = parseFloat((nTotalAegypti / nTotalTotalDist)*100).toFixed(1);
                nIndiceAlbopic = parseFloat((nTotalAlbopic / nTotalTotalDist)*100).toFixed(1);
                nIndiceQuinque = parseFloat((nTotalQuinque / nTotalTotalDist)*100).toFixed(1);
                nIndiceNigrip = parseFloat((nTotalNigrip / nTotalTotalDist)*100).toFixed(1);
                nIndiceCoronat = parseFloat((nTotalCoronat / nTotalTotalDist)*100).toFixed(1);
                nIndiceErratico = parseFloat((nTotalErratico / nTotalTotalDist)*100).toFixed(1);
                nIndiceTarsalis = parseFloat((nTotalTarsalis / nTotalTotalDist)*100).toFixed(1);
                nIndiceFatigans = parseFloat((nTotalFatigans / nTotalTotalDist)*100).toFixed(1);
                nIndiceAlbim = parseFloat((nTotalAlbim / nTotalTotalDist)*100).toFixed(1);

                $('#dtDetalleIndices').append('</tbody><tfoot><tr><th style="text-align: right; background-color: #86b4dd"></th><th class="font-sm" style="text-align: center; background-color: #86b4dd">TOTALES</th>' +
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalPila" style="font-weight: bold">'+nTotalPilas+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalLlanta"  style="font-weight: bold"><strong>'+nTotalLlanta+'</strong></label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalBarril" style="font-weight: bold">'+nTotalBarril+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalFloreros" style="font-weight: bold">'+nTotalFlorero+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalBeberedos" style="font-weight: bold">'+nTotalBebedero+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalArtEspec" style="font-weight: bold">'+nTotalArtEspec+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalODep" style="font-weight: bold">'+nTotalODep+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalCister" style="font-weight: bold">'+nTotalCister+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalInodo" style="font-weight: bold">'+nTotalInodo+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalBarro" style="font-weight: bold">'+nTotalBarro+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalPlanta" style="font-weight: bold">'+nTotalPlanta+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalArbol" style="font-weight: bold">'+nTotalArbol+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalPozo" style="font-weight: bold">'+nTotalPozo+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalTotalI" style="font-weight: bold">'+nTotalTotalIndice+'</label></th>' +
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"></th></tr>' +
                        '<tr><th style="text-align: right; background-color: #86b4dd"></th><th class="font-sm" style="text-align: center; background-color: #86b4dd">INDICES</th>' +
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indicePila" style="font-weight: bold">'+nIndicePilas+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceLlanta"  style="font-weight: bold"><strong>'+nIndiceLlanta+'</strong></label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceBarril" style="font-weight: bold">'+nIndiceBarril+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceFloreros" style="font-weight: bold">'+nIndiceFlorero+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceBeberedos" style="font-weight: bold">'+nIndiceBebedero+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceArtEspec" style="font-weight: bold">'+nIndiceArtEspec+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceODep" style="font-weight: bold">'+nIndiceODep+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceCister" style="font-weight: bold">'+nIndiceCister+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceInodo" style="font-weight: bold">'+nIndiceInodo+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceBarro" style="font-weight: bold">'+nIndiceBarro+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indicePlanta" style="font-weight: bold">'+nIndicePlanta+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceArbol" style="font-weight: bold">'+nIndiceArbol+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indicePozo" style="font-weight: bold">'+nIndicePozo+'</label></th> '+
                        '<th class="font-sm" colspan="2" rowspan="1" style="text-align: right; background-color: #86b4dd"></th></tr></tfoot>');

                $('#dtDetalleDistribucion').append('</tbody><tfoot><tr><th style="text-align: right; background-color: #86b4dd"></th><th class="font-sm" style="text-align: center; background-color: #86b4dd">TOTALES</th>' +
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalAegypti" style="font-weight: bold">'+nTotalAegypti+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalAlbopic"  style="font-weight: bold">'+nTotalAlbopic+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalQuinque" style="font-weight: bold">'+nTotalQuinque+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalNigrip" style="font-weight: bold">'+nTotalNigrip+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalCoronat" style="font-weight: bold">'+nTotalCoronat+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalErratico" style="font-weight: bold">'+nTotalErratico+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalTarsalis" style="font-weight: bold">'+nTotalTarsalis+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalFatigans" style="font-weight: bold">'+nTotalFatigans+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalAnophAlbim" style="font-weight: bold">'+nTotalAlbim+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="totalTotalD" style="font-weight: bold">'+nTotalTotalDist+'</label></th>' +
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"></th></tr>' +
                        '<tr><th style="text-align: right; background-color: #86b4dd"></th><th class="font-sm" style="text-align: center; background-color: #86b4dd">INDICES</th>' +
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceAegypti" style="font-weight: bold">'+nIndiceAegypti+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceAlbopic"  style="font-weight: bold"><strong>'+nIndiceAlbopic+'</strong></label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceQuinque" style="font-weight: bold">'+nIndiceQuinque+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceNigrip" style="font-weight: bold">'+nIndiceNigrip+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceCoronat" style="font-weight: bold">'+nIndiceCoronat+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceErratico" style="font-weight: bold">'+nIndiceErratico+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceTarsalis" style="font-weight: bold">'+nIndiceTarsalis+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceFatigans" style="font-weight: bold">'+nIndiceFatigans+'</label></th> '+
                        '<th class="font-sm" colspan="1" rowspan="1" style="text-align: right; background-color: #86b4dd"><label id="indiceAnophAlbim" style="font-weight: bold">'+nIndiceAlbim+'</label></th> '+
                        '<th class="font-sm" colspan="2" rowspan="1" style="text-align: right; background-color: #86b4dd"></th></tr></tfoot>');

                $('#dtDetalleIndices').dataTable({
                    "sPaginationType": "bootstrap",
                    "bFilter": false,
                    "bPaginate": true,
                    //"bRetrieve":true,
                    "bDestroy": true,
                    "sPaging": true
                });

                $('#dtDetalleDistribucion').dataTable({
                    "sPaginationType": "bootstrap",
                    "bFilter": false,
                    "bPaginate": true,
                    //"bRetrieve":true,
                    "bDestroy": true,
                    "sPaging": true
                });

                //Esto permite que mediante la clase editDetalle se pueda cargar el pop con la información para editarla
                $(".editDetalle").click(function(){
                    limpiarCamposDetalle();
                    $('#idDetalleEditar').val($(this).data('id'));
                    cargarInfoModalDetalle($(this).data('id'));
                    mostrarModalDetalle();
                });
                $(".pagination").click(function() {
                    $(".editDetalle").click(function () {
                        limpiarCamposDetalle();
                        $('#idDetalleEditar').val($(this).data('id'));
                        cargarInfoModalDetalle($(this).data('id'));
                        mostrarModalDetalle();
                    });
                });

                $("#indicePila").text(nIndicePilas);
                $("#indiceLlanta").text(nIndiceLlanta);
                $("#indiceBarril").text(nIndiceBarril);
                $("#indiceFloreros").text(nIndiceFlorero);
                $("#indiceBeberedos").text(nIndiceBebedero);
                $("#indiceArtEspec").text(nIndiceArtEspec);
                $("#indiceODep").text(nIndiceODep);
                $("#indiceCister").text(nIndiceCister);
                $("#indiceInodo").text(nIndiceInodo);
                $("#indiceBarro").text(nIndiceBarro);
                $("#indicePlanta").text(nIndicePlanta);
                $("#indiceArbol").text(nIndiceArbol);
                $("#indicePozo").text(nIndicePozo);

                $("#indiceAegypti").text(nIndiceAegypti  );
                $("#indiceAlbopic").text(nIndiceAlbopic  );
                $("#indiceQuinque").text(nIndiceQuinque  );
                $("#indiceNigrip").text( nIndiceNigrip  );
                $("#indiceCoronat").text(nIndiceCoronat  );
                $("#indiceErratico").text(nIndiceErratico );
                $("#indiceTarsalis").text(nIndiceTarsalis );
                $("#indiceFatigans").text(nIndiceFatigans );
                $("#indiceAnophAlbim").text(nIndiceAlbim    );

                $("#totalPila").text(nTotalPilas);
                $("#totalLlanta").text(nTotalLlanta);
                $("#totalBarril").text(nTotalBarril);
                $("#totalFloreros").text(nTotalFlorero);
                $("#totalBeberedos").text(nTotalBebedero);
                $("#totalArtEspec").text(nTotalArtEspec);
                $("#totalODep").text(nTotalODep);
                $("#totalCister").text(nTotalCister);
                $("#totalInodo").text(nTotalInodo);
                $("#totalBarro").text(nTotalBarro);
                $("#totalPlanta").text(nTotalPlanta);
                $("#totalArbol").text(nTotalArbol);
                $("#totalPozo").text(nTotalPozo);
                $("#totalTotalI").text(nTotalTotalIndice);

                $("#totalAegypti").text(nTotalAegypti  );
                $("#totalAlbopic").text(nTotalAlbopic  );
                $("#totalQuinque").text(nTotalQuinque  );
                $("#totalNigrip").text( nTotalNigrip  );
                $("#totalCoronat").text(nTotalCoronat  );
                $("#totalErratico").text(nTotalErratico );
                $("#totalTarsalis").text(nTotalTarsalis );
                $("#totalFatigans").text(nTotalFatigans );
                $("#totalAnophAlbim").text(nTotalAlbim    );
                $("#totalTotalD").text(nTotalTotalDist);

            },
            error: function (result) {
                alert("Error " + result.status + '' + result.statusText);
            }
        });
    }

    function cargarInfoModalDetalle(idDetalleEditar){
        $.ajax({
            type: "GET",
            url: "${recuperarDetalleLarva}",
            data: {idDetalleEncu: idDetalleEditar},
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (response) {
                $("#codLocalidadEdicion").val(response.codLocalidad);
                $("#txtPila").val(response.pilaInfestado);
                $("#txtLlanta").val(response.llantaInfestado);
                $("#txtBarril").val(response.barrilInfestado);
                $("#txtFlorero").val(response.floreroInfestado);
                $("#txtBebedero").val(response.bebederoInfestado);
                $("#txtArt_Esp").val(response.artEspecialInfes);
                $("#txtO_Dep").val(response.otrosDepositosInfes);
                $("#txtCister").val(response.cisterInfestado);
                $("#txtInodoro").val(response.inodoroInfestado);
                $("#txtBarro").val(response.barroInfestado);
                $("#txtPlanta").val(response.plantaInfestado);
                $("#txtArbol").val(response.arbolInfestado);
                $("#txtPozo").val(response.pozoInfestado);
                $("#txtAedesAegyti").val(response.especieAegypti);
                $("#txtAedesAlbopic").val(response.especieAlbopic);
                $("#txtCulexQuinque").val(response.especieCulexQuinque);
                $("#txtCulexNigrip").val(response.especieCulexNigrip);
                $("#txtCulexCoronat").val(response.especieCulexCoronat);
                $("#txtCulexErratico").val(response.especieCulexErratico);
                $("#txtCulexTarsalis").val(response.especieCulexTarsalis);
                $("#txtCulexFatigans").val(response.especieCulexFatigans);
                $("#txtAnophAlbim").val(response.especieCulexAlbim);
                $("#codigoLocalidad").val($("#codLocalidadEdicion").val()).change();

            },
            error: function (result) {

            }
        });
    }

    function limpiarCamposDetalle(){
        $("#idDetalleEditar").val('');
        $('#codigoLocalidad option:first').prop("selected", true).change();
        $("#txtPila").val('');
        $("#txtLlanta").val('');
        $("#txtBarril").val('');
        $("#txtFlorero").val('');
        $("#txtBebedero").val('');
        $("#txtArt_Esp").val('');
        $("#txtO_Dep").val('');
        $("#txtCister").val('');
        $("#txtInodoro").val('');
        $("#txtBarro").val('');
        $("#txtPlanta").val('');
        $("#txtArbol").val('');
        $("#txtPozo").val('');
        $("#txtAedesAegyti").val('');
        $("#txtAedesAlbopic").val('');
        $("#txtCulexQuinque").val('');
        $("#txtCulexNigrip").val('');
        $("#txtCulexCoronat").val('');
        $("#txtCulexErratico").val('');
        $("#txtCulexTarsalis").val('');
        $("#txtCulexFatigans").val('');
        $("#txtAnophAlbim").val('');
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
            txtPila:{
                required: true,
                digits: true
            },
            txtLlanta:{
                required: true,
                digits: true
            },
            txtBarril:{
                required: true,
                digits: true
            },
            txtFlorero:{
                required: true,
                digits: true
            },
            txtBebedero:{
                required: true,
                digits: true
            },
            txtArt_Esp:{
                required: true,
                digits: true
            },
            txtO_Dep:{
                required: true,
                digits: true
            },
            txtCister:{
                required: true,
                digits: true
            },
            txtInodoro:{
                required: true,
                digits: true
            },
            txtBarro: {
                required: true,
                digits: true
            },
            txtPlanta: {
                required: true,
                digits: true
            },
            txtArbol: {
                required: true,
                digits: true
            },
            txtPozo: {
                required: true,
                digits: true
            },
            txtAedesAegyti: {
                required: true,
                digits: true
            },
            txtAedesAlbopic: {
                required: true,
                digits: true
            },
            txtCulexQuinque: {
                required: true,
                digits: true
            },
            txtCulexNigrip: {
                required: true,
                digits: true
            },
            txtCulexCoronat: {
                required: true,
                digits: true
            },
            txtCulexErratico: {
                required: true,
                digits: true
            },
            txtCulexTarsalis: {
                required: true,
                digits: true
            },
            txtCulexFatigans: {
                required: true,
                digits: true
            },
            txtAnophAlbim: {
                required: true,
                digits: true
            }
        },
        messages: {
            codigoLocalidad:{
                required: 'Debe seleccionar una localidad'
            },
            txtPila:{
                required: 'Debe ingresar cantidad pila',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtLlanta:{
                required: 'Debe ingresar cantidad llantas',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtBarril:{
                required: 'Debe ingresar cantidad barril',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtFlorero:{
                required: 'Debe ingresar cantidad floreros',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtBebedero:{
                required: 'Debe ingresar cantidad bebederos',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtArt_Esp:{
                required: 'Debe ingresar cantidad art_esp',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtO_Dep:{
                required: 'Debe ingresar cantidad O_Dep',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtCister:{
                required: 'Debe ingresar cantidad cister',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtInodoro:{
                required: 'Debe ingresar cantidad inodoro',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtBarro: {
                required: 'Debe ingresar cantidad barro',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtPlanta:{
                required: 'Debe ingresar cantidad planta',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtArbol:{
                required: 'Debe ingresar cantidad árbol',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtPozo:{
                required: 'Debe ingresar cantidad pozo',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtAedesAegyti: {
                required: 'Debe ingresar cantidad aedes aegypti',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtAedesAlbopic: {
                required: 'Debe ingresar cantidad aedes albopic',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtCulexQuinque: {
                required: 'Debe ingresar cantidad culex quinque',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtCulexNigrip: {
                required: 'Debe ingresar cantidad culex nigrip',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtCulexCoronat: {
                required: 'Debe ingresar cantidad culex  coronat',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtCulexErratico: {
                required: 'Debe ingresar cantidad culex erratico',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtCulexTarsalis: {
                required: 'Debe ingresar cantidad culex tarsalis',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtCulexFatigans: {
                required: 'Debe ingresar cantidad culex fatigans',
                digits: 'Debe ingresar únicamente valores positivos'
            },
            txtAnophAlbim: {
                required: 'Debe ingresar cantidad Anoph albim',
                digits: 'Debe ingresar únicamente valores positivos'
            }
        },
        // Do not change code below
        errorPlacement: function (error, element) {
            error.insertAfter(element.parent());

        }
    });

    //dvFecInicio
    //fecInicioEncuesta
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

    function limpiarCamposMaestro(){
        $('#codSilais').val('').change();
        $('#codUnidadSalud').val('').change();
        $('#codigoDepartamento').val('').change();
        $('#codigoMunicipio').val('').change();
        $('#codigoDistrito').val('').change();
        $('#codigoArea').val('').change();
        $('#codProcedencia').val('').change();
        $('#codOrdinal').val('').change();
        $('#mesEpi').val('');
        $("#anioEpi").val('');
        $("#semanaEpi").val('');
        $("#fecInicioEncuesta").val('');
        $("#fecFinEncuesta").val('');
    }

    function limpiarTablasDetalle(){
        if ($('#dtDetalleIndices >tbody >tr').length > 0) {
            $("#dtDetalleIndices").empty();
            $('#dtDetalleIndices').dataTable({
                "sPaginationType": "none",
                "bFilter": false,
                "bPaginate": false,
                "bInfo": false,
                "bLengthChang": false,
                "bDestroy": true,
                "sPaging": false
            });
        }
        if ($('#dtDetalleDistribucion >tbody >tr').length > 0) {
            $("#dtDetalleDistribucion").empty();
            $('#dtDetalleDistribucion').dataTable({
                "sPaginationType": "none",
                "bFilter": false,
                "bPaginate": false,
                "bInfo": false,
                "bLengthChang":false,
                "bDestroy": true,
                "sPaging": false
            });
        }
        $("#indicePila").text('0');
        $("#indiceLlanta").text('0');
        $("#indiceBarril").text('0');
        $("#indiceFloreros").text('0');
        $("#indiceBeberedos").text('0');
        $("#indiceArtEspec").text('0');
        $("#indiceODep").text('0');
        $("#indiceCister").text('0');
        $("#indiceInodo").text('0');
        $("#indiceBarro").text('0');
        $("#indicePlanta").text('0');
        $("#indiceArbol").text('0');
        $("#indicePozo").text('0');

        $("#indiceAegypti").text('0');
        $("#indiceAlbopic").text('0');
        $("#indiceQuinque").text('0');
        $("#indiceNigrip").text('0');
        $("#indiceCoronat").text('0');
        $("#indiceErratico").text('0');
        $("#indiceTarsalis").text('0');
        $("#indiceFatigans").text('0');
        $("#indiceAnophAlbim").text('0'    );

        $("#totalPila").text('0');
        $("#totalLlanta").text('0');
        $("#totalBarril").text('0');
        $("#totalFloreros").text('0');
        $("#totalBeberedos").text('0');
        $("#totalArtEspec").text('0');
        $("#totalODep").text('0');
        $("#totalCister").text('0');
        $("#totalInodo").text('0');
        $("#totalBarro").text('0');
        $("#totalPlanta").text('0');
        $("#totalArbol").text('0');
        $("#totalPozo").text('0');
        $("#totalTotalI").text('0');

        $("#totalAegypti").text('0' );
        $("#totalAlbopic").text('0');
        $("#totalQuinque").text('0');
        $("#totalNigrip").text('0');
        $("#totalCoronat").text('0');
        $("#totalErratico").text('0');
        $("#totalTarsalis").text('0');
        $("#totalFatigans").text('0');
        $("#totalAnophAlbim").text('0');
        $("#totalTotalD").text('0');

    }

});
</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>