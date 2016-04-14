<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<!-- BEGIN HEAD -->
<head>
	<jsp:include page="../fragments/headTag.jsp" />
    <spring:url value="/resources/img/plus.png" var="plus"/>
    <spring:url value="/resources/img/minus.png" var="minus"/>
    <style>
        td.details-control {
        background: url("${plus}") no-repeat center center;
        cursor: pointer;
        }
        tr.shown td.details-control {
        background: url("${minus}") no-repeat center center;
        }
        ul.pagination.pagination-sm{
            width: 50%!important;
        }
        .dataTables_paginate.paging_simple_numbers{
            width: 75%!important;
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
				<li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a> <i class="fa fa-angle-right"></i> <a href="<spring:url value="/paciente/create" htmlEscape="true "/>"><spring:message code="menu.paciente" /></a></li>
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
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
					<h1 class="page-title txt-color-blueDark">
						<!-- PAGE HEADER -->
						<i class="fa-fw fa fa-fire"></i> 
							<spring:message code="lbl.header.noti.patient" />
					</h1>
				</div>
				<!-- end col -->
			</div>
			<!-- end row -->
			<!-- widget grid -->
			<section id="widget-grid" class="">
				<!-- row -->
				<div class="row">
					<!-- a blank row to get started -->
				</div>
				<!-- end row -->
				<!-- row -->
				<div class="row">
					<!-- NEW WIDGET START -->
					<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<!-- Widget ID (each widget will need unique ID)-->
						<div class="jarviswidget jarviswidget-color-darken" id="wid-id-0" data-widget-editbutton="false" data-widget-deletebutton="false">
							<header>
								<span class="widget-icon"> <i class="fa fa-fire"></i> </span>
								<h2><spring:message code="lbl.header.noti.patient" /> </h2>
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
											<li data-target="#step1" class="active">
												<span class="badge badge-info">1</span><spring:message code="patient.step1" /><span class="chevron"></span>
											</li>
											<li data-target="#step2">
												<span class="badge">2</span><spring:message code="patient.step2" /><span class="chevron"></span>
											</li>
											<li data-target="#step3">
												<span class="badge">3</span><spring:message code="patient.step3" /><span class="chevron"></span>
											</li>
										</ul>
										<div class="actions">
											<button type="button" class="btn btn-sm btn-primary btn-prev">
												<i class="fa fa-arrow-left"></i><spring:message code="lbl.previous" />
											</button>
											<button type="button" class="btn btn-sm btn-success btn-next" data-last="<spring:message code="lbl.finalize" />">
												<spring:message code="lbl.next" /><i class="fa fa-arrow-right"></i>
											</button>
										</div>
									</div>
									<div class="step-content">
										<form id="sind_feb_form" class ="smart-form">
                                            <input value="${notificacion.idNotificacion}" type="hidden" id="idNotificacion" name="idNotificacion" />
                                            <input value="${notificacion.persona.personaId}" type="hidden" id="personaId" name="personaId" />
											<!-- wizard form starts here -->
											<div class="step-pane active" id="step1">
												<h3><spring:message code="patient.step1.long" /></h3>
												<fieldset>
                                                <!-- START ROW -->
                                                <div class="row">
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md hidden-xs">
                                                            <spring:message code="person.name1" />
                                                        </label>
                                                        <div>
                                                            <label class="input">
                                                                <i class="icon-prepend fa fa-user"></i> <i class="icon-append fa fa-sort-alpha-asc"></i>
                                                                <input class="form-control" type="text" name="primerNombre" id="primerNombre"
                                                                       value="${notificacion.persona.primerNombre}" readonly
                                                                       placeholder=" <spring:message code="person.name1" />">
                                                            </label>
                                                        </div>
                                                    </section>
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md hidden-xs">
                                                            <spring:message code="person.name2" />
                                                        </label>
                                                        <div>
                                                            <label class="input">
                                                                <i class="icon-prepend fa fa-user"></i> <i class="icon-append fa fa-sort-alpha-asc"></i>
                                                                <input class="form-control" type="text" name="segundoNombre" id="segundoNombre"
                                                                       value="${notificacion.persona.segundoNombre}" readonly
                                                                       placeholder=" <spring:message code="person.name2" />">
                                                            </label>
                                                        </div>
                                                    </section>
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md hidden-xs">
                                                            <spring:message code="person.lastname1" />
                                                        </label>
                                                        <div>
                                                            <label class="input">
                                                                <i class="icon-prepend fa fa-user"></i> <i class="icon-append fa fa-sort-alpha-asc"></i>
                                                                <input class="form-control" type="text" name="primerApellido" id="primerApellido"
                                                                       value="${notificacion.persona.primerApellido}" readonly
                                                                       placeholder=" <spring:message code="person.lastname1" />">
                                                            </label>
                                                        </div>
                                                    </section>
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md hidden-xs">
                                                            <spring:message code="person.lastname2" />
                                                        </label>
                                                        <div>
                                                            <label class="input">
                                                                <i class="icon-prepend fa fa-user"></i> <i class="icon-append fa fa-sort-alpha-asc"></i>
                                                                <input class="form-control" type="text" name="segundoApellido" id="segundoApellido"
                                                                       value="${notificacion.persona.segundoApellido}" readonly
                                                                       placeholder=" <spring:message code="person.name2" />">
                                                            </label>
                                                        </div>
                                                    </section>
                                                </div>
                                                <!-- END ROW -->
                                                <!-- START ROW -->
                                                <div class="row">
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md hidden-xs">
                                                            <spring:message code="person.fecnac" />
                                                        </label>
                                                        <div>
                                                            <label class="input">
                                                                <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar"></i>
                                                                <input class="form-control" readonly
                                                                       type="text" name="fechaNacimiento" id="fechaNacimiento" value="<fmt:formatDate value="${notificacion.persona.fechaNacimiento}" pattern="dd/MM/yyyy" />"
                                                                       placeholder=" <spring:message code="person.fecnac" />">
                                                            </label>
                                                        </div>
                                                    </section>
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md hidden-xs">
                                                            <spring:message code="lbl.age" />
                                                        </label>
                                                        <div>
                                                            <label class="input">
                                                                <i class="icon-prepend fa fa-user"></i> <i class="icon-append fa fa-sort-numeric-asc"></i>
                                                                <input class="form-control" type="text" name="edad" id="edad" readonly
                                                                       placeholder=" <spring:message code="lbl.age" />">
                                                            </label>
                                                        </div>
                                                    </section>
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md hidden-xs">
                                                            <spring:message code="person.sexo" />
                                                        </label>
                                                        <div>
                                                            <label class="input">
                                                                <c:choose>
                                                                    <c:when test="${notificacion.persona.sexo.codigo eq 'SEXO|M'}">
                                                                        <i class="icon-prepend fa fa-male"></i> <i class="icon-append fa fa-sort-alpha-asc"></i>
                                                                    </c:when>
                                                                    <c:when test="${notificacion.persona.sexo.codigo eq 'SEXO|F'}">
                                                                        <i class="icon-prepend fa fa-female"></i> <i class="icon-append fa fa-sort-alpha-asc"></i>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <i class="icon-prepend fa fa-question"></i> <i class="icon-append fa fa-sort-alpha-asc"></i>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <input class="form-control" type="text" name="sexo" id="sexo"
                                                                       value="${notificacion.persona.sexo}" readonly
                                                                       placeholder=" <spring:message code="person.sexo" />">
                                                            </label>
                                                        </div>
                                                    </section>
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md hidden-xs">
                                                            <spring:message code="person.ocupacion" />
                                                        </label>
                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i class="fa fa-wrench"></i></span>
                                                            <select disabled data-placeholder="<spring:message code="act.select" /> <spring:message code="person.ocupacion" />" name="ocupacion" id="ocupacion" class="select2">
                                                                <option value=""></option>
                                                                <c:forEach items="${ocupaciones}" var="ocupacion">
                                                                    <c:choose>
                                                                        <c:when test="${ocupacion.codigo eq notificacion.persona.ocupacion.codigo}">
                                                                            <option selected value="${ocupacion.codigo}">${ocupacion.nombre}</option>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <option value="${ocupacion.codigo}">${ocupacion.nombre}</option>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </section>
                                                </div>
                                                <!-- END ROW -->
                                                <!-- START ROW -->
                                                <div class="row">
                                                    <section class="col col-9">
                                                        <label class="text-left txt-color-blue font-md hidden-xs">
                                                            <spring:message code="person.direccion" />
                                                        </label>
                                                        <div>
                                                            <label class="input">
                                                                <i class="icon-prepend fa fa-map-marker"></i> <i class="icon-append fa fa-sort-alpha-asc"></i>
                                                                <input disabled class="form-control" type="text" name="direccionResidencia" id="direccionResidencia"
                                                                       value="${notificacion.persona.direccionResidencia}"
                                                                       placeholder=" <spring:message code="person.direccion" />">
                                                                <b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="person.direccion" /></b>
                                                            </label>
                                                        </div>
                                                    </section>
                                                </div>
                                                <!-- END ROW -->
                                                <!-- START ROW -->
                                                <div class="row">
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md hidden-xs">
                                                            <spring:message code="lbl.person.depart.resi" />
                                                        </label>
                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i class="fa fa-location-arrow fa-fw"></i></span>
                                                            <select disabled data-placeholder="<spring:message code="act.select" /> <spring:message code="lbl.person.depart.resi" />" name="departamento" id="departamento" class="select2">
                                                                <option value=""></option>
                                                                <c:forEach items="${departamentos}" var="departamento">
                                                                    <c:choose>
                                                                        <c:when test="${departamento.codigoNacional eq notificacion.persona.municipioResidencia.dependencia.codigoNacional}">
                                                                            <option selected value="${departamento.codigoNacional}">${departamento.nombre}</option>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <option value="${departamento.codigoNacional}">${departamento.nombre}</option>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </section>
                                                    <section class="col col-4">
                                                        <label class="text-left txt-color-blue font-md hidden-xs">
                                                            <spring:message code="person.mun.res" />
                                                        </label>
                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i class="fa fa-location-arrow fa-fw"></i></span>
                                                            <select disabled data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.muni" />" name="municipioResidencia" id="municipioResidencia" class="select2">
                                                                <option value=""></option>
                                                                <c:forEach items="${municipiosResi}" var="municipioResi">
                                                                    <c:choose>
                                                                        <c:when test="${municipioResi.codigoNacional eq notificacion.persona.municipioResidencia.codigoNacional}">
                                                                            <option selected value="${municipioResi.codigoNacional}">${municipioResi.nombre}</option>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <option value="${municipioResi.codigoNacional}">${municipioResi.nombre}</option>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </section>
                                                    <section class="col col-5">
                                                        <label class="text-left txt-color-blue font-md hidden-xs">
                                                            <spring:message code="person.com.res" />
                                                        </label>
                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i class="fa fa-location-arrow fa-fw"></i></span>
                                                            <select disabled data-placeholder="<spring:message code="act.select" /> <spring:message code="person.com.res" />" name="comunidadResidencia" id="comunidadResidencia" class="select2">
                                                                <option value=""></option>
                                                                <c:forEach items="${comunidades}" var="comunid">
                                                                    <c:choose>
                                                                        <c:when test="${comunid.codigo eq notificacion.persona.comunidadResidencia.codigo}">
                                                                            <option selected value="${comunid.codigo}">${comunid.nombre}-${comunid.sector.unidad.nombre}</option>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <option value="${comunid.codigo}">${comunid.nombre}-${comunid.sector.unidad.nombre}</option>
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
                                                <input id="data_value" type="hidden" value="<spring:message code="lbl.data.value"/>"/>
                                                <input id="data_date" type="hidden" value="<spring:message code="lbl.register.date"/>"/>
                                                <input id="data_response" type="hidden" value="<spring:message code="lbl.data.name"/>"/>
												<h3><spring:message code="patient.step2.long" /></h3>
                                                <h4><spring:message code="lbl.step2.short" />: ${notificacion.persona.primerNombre} ${notificacion.persona.segundoNombre} ${notificacion.persona.primerApellido} ${notificacion.persona.segundoApellido}</h4>
                                                <br/><br/>
                                                <div class="widget-body no-padding">
                                                    <div class="row">
                                                        <section class="col col-sm-12 col-md-12 col-lg-12">
                                                            <table class="table table-striped table-hover table-bordered"  width="100%" id="lista_datos">
                                                                <thead>
                                                                <tr>
                                                                    <th><spring:message code="lbl.request.type"/></th>
                                                                    <th><spring:message code="lbl.desc.request"/></th>
                                                                    <th><spring:message code="lbl.send.request.date"/></th>
                                                                    <th><spring:message code="lbl.result.date.approve"/></th>
                                                                    <th><spring:message code="lbl.unique.code.mx"/></th>
                                                                    <th><spring:message code="lbl.result.mx.type"/></th>
                                                                    <th><spring:message code="lbl.data.request"/></th>
                                                                </tr>
                                                                </thead>
                                                            </table>
                                                        </section>
                                                    </div>
                                                </div>
                                            </div>
											<div class="step-pane" id="step3">
                                                <input id="text_value" type="hidden" value="<spring:message code="lbl.result.value"/>"/>
                                                <input id="text_date" type="hidden" value="<spring:message code="lbl.result.date"/>"/>
                                                <input id="text_response" type="hidden" value="<spring:message code="lbl.result.response"/>"/>
												<h3><spring:message code="patient.step3.long" /></h3>
												<h4><spring:message code="lbl.step2.short" />: ${notificacion.persona.primerNombre} ${notificacion.persona.segundoNombre} ${notificacion.persona.primerApellido} ${notificacion.persona.segundoApellido}</h4>
                                                <br/><br/>
                                                <div class="widget-body no-padding">
                                                    <div class="row">
                                                        <section class="col col-sm-12 col-md-12 col-lg-12">
                                                            <table class="table table-striped table-hover table-bordered"  width="100%" id="lista_resultados">
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
                                                        </section>
                                                    </div>
                                                </div>
											</div>
										</form>
									</div>
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
	<!-- JQUERY BOOTSTRAP WIZARD -->
	<spring:url value="/resources/js/plugin/bootstrap-wizard/jquery.bootstrap.wizard.min.js" var="jqueryBootstrap" />
	<script src="${jqueryBootstrap}"></script>
	<!-- JQUERY FUELUX WIZARD -->
	<spring:url value="/resources/js/plugin/fuelux/wizard/wizard.min.js" var="jQueryFueWiz" />
	<script src="${jQueryFueWiz}"></script>
    <!--DATA TABLE -->
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
	<spring:url value="/resources/js/plugin/jquery-validate/jquery.validate.min.js" var="jqueryValidate" />
	<script src="${jqueryValidate}"></script>
	<spring:url value="/resources/js/plugin/jquery-validate/messages_{language}.js" var="jQValidationLoc">
	<spring:param name="language" value="${pageContext.request.locale.language}" /></spring:url>				
	<script src="${jQValidationLoc}"></script>
	<!-- jQuery Select2 Input -->
	<spring:url value="/resources/js/plugin/select2/select2.min.js" var="selectPlugin"/>
	<script src="${selectPlugin}"></script>
	<!-- jQuery Select2 Locale -->
	<spring:url value="/resources/js/plugin/select2/select2_locale_{language}.js" var="selectPluginLocale">
	<spring:param name="language" value="${pageContext.request.locale.language}" /></spring:url>
	<script src="${selectPluginLocale}"></script>
	<!-- JQUERY BLOCK UI -->
	<spring:url value="/resources/js/plugin/jquery-blockui/jquery.blockUI.js" var="jqueryBlockUi" />
	<script src="${jqueryBlockUi}"></script>
	<!-- bootstrap datepicker -->
	<spring:url value="/resources/js/plugin/bootstrap-datepicker/bootstrap-datepicker.js" var="datepickerPlugin" />
	<script src="${datepickerPlugin}"></script>
	<spring:url value="/resources/js/plugin/jquery-inputmask/jquery.inputmask.bundle.min.js" var="jqueryInputMask" />
	<script src="${jqueryInputMask}"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<spring:url value="/resources/scripts/utilidades/seleccionUnidad.js" var="selecUnidad" />
	<script src="${selecUnidad}"></script>
	<spring:url value="/resources/scripts/paciente/detail.js" var="detailNotiPatient" />
	<script src="${detailNotiPatient}"></script>
	<spring:url value="/resources/scripts/utilidades/handleDatePickers.js" var="handleDatePickers" />
	<script src="${handleDatePickers}"></script>
	<spring:url value="/resources/scripts/utilidades/handleInputMask.js" var="handleInputMask" />
	<script src="${handleInputMask}"></script>
	<!-- script calcular edad -->
	<spring:url value="/resources/scripts/utilidades/calcularEdad.js" var="calculateAge" />
	<script src="${calculateAge}"></script>
	<!-- END PAGE LEVEL SCRIPTS -->
	<!-- PARAMETROS URL -->
	<spring:url var="municipiosURL" value="/api/v1/municipiosbysilais"/>
	<spring:url var="unidadesUrl"   value="/api/v1/unidadesPrimHosp"  />
	<spring:url var="municipioByDepaUrl" value="/api/v1/municipio" />
	<spring:url var="comunidadUrl" value="/api/v1/comunidad"/>
                 <spring:url var="sPacienteSearchUrl" value="/paciente/search/"/>
    <spring:url var="sPacienteResultsUrl" value="/api/v1/searchApproveResultsNoti"/>
    <spring:url var="sDatosSolicitudUrl" value="/paciente/getDatosSolicitudDetalleByNotifi"/>
	<!-- PARAMETROS LENGUAJE -->
	<c:set var="blockMess"><spring:message code="blockUI.message" /></c:set>
	<script type="text/javascript">
		$(document).ready(function() {
			pageSetUp();
			
			var parametros = {
                    municipiosUrl:"${municipiosURL}",
                    unidadesUrl: "${unidadesUrl}",municipioByDepaUrl: "${municipioByDepaUrl}",comunidadUrl: "${comunidadUrl}",
                    sPacienteSearchUrl: "${sPacienteSearchUrl}",
                    blockMess:"${blockMess}",
                    sPacienteResultsUrl : "${sPacienteResultsUrl}",
                sDatosSolicitudUrl : "${sDatosSolicitudUrl}"
 			 };
			PatientDetail.init(parametros);
			SeleccionUnidad.init(parametros);
			handleDatePickers("${pageContext.request.locale.language}");
			handleInputMasks();
	    	$("li.notificacion").addClass("open");
	    	$("li.paciente").addClass("active");
	    	if("top"!=localStorage.getItem("sm-setmenu")){
	    		$("li.paciente").parents("ul").slideDown(200);
	    	}
	    	$('#fechaNacimiento').change();
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>