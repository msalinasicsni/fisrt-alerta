<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<!-- BEGIN HEAD -->
<head>
	<jsp:include page="../fragments/headTag.jsp" />
	<spring:url value="/resources/js/plugin/chartjs/chartjs.css" var="chartjsCss" />
	<link href="${chartjsCss}" rel="stylesheet" type="text/css"/>
	<style>
		/* columns right and center aligned datatables */
        .aw-right {
            padding-left: 0;
            padding-right: 10px;
            text-align: right;
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
				<li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a> <i class="fa fa-angle-right"></i> 
				<spring:message code="lbl.reports" /><i class="fa fa-angle-right"></i> <a href="<spring:url value="/reportes/porDia" htmlEscape="true "/>"><spring:message code="menu.report.per.day" /></a></li>
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
						<i class="fa-fw fa fa-line-chart"></i>
							<spring:message code="lbl.reports" />
						<span> <i class="fa fa-angle-right"></i>  
							<spring:message code="menu.report.per.day" />
						</span>
					</h1>
				</div>
				<!-- end col -->
				<!-- right side of the page with the sparkline graphs -->
				<!-- col -->
				<div class="col-xs-12 col-sm-5 col-md-5 col-lg-8">
					<!-- sparks -->
					<ul id="sparks">
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
					<article class="col-xs-12 col-sm-12 col-md-5 col-lg-5">
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
								<span class="widget-icon"> <i class="fa fa-wrench"></i> </span>
								<h2><spring:message code="lbl.parameters" /> </h2>				
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
                                    <input id="areaL" type="hidden" value="<spring:message code="lbl.health.area1"/>"/>

                                    <form id="parameters_form" class ="smart-form">
										<fieldset>
                                            <!-- START ROW -->
                                            <div class="row">
                                                <section>
                                                    <div class="input-group">
                                                        <span class="input-group-addon"><i class="fa fa-stethoscope"></i></span>
                                                        <select id="tipoNotificacion" name="tipoNotificacion" data-placeholder="<spring:message code="act.select" /> <spring:message code="lbl.notification.type" />"
                                                                class="select2">
                                                            <option value=""></option>
                                                            <c:forEach items="${tiposNotificacion}" var="tiposNotificacion">
                                                                <option value="${tiposNotificacion.codigo}">${tiposNotificacion.valor}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </section>
                                            </div>
                                            <!-- END ROW -->
										<!-- START ROW -->
                                      	<div class="row">
                                      		<section>
                                      		<div class="input-group">
												<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
												<select name="codArea" id="codArea" data-placeholder="<spring:message code="act.select" /> <spring:message code="level" />" class="select2">
													<option value=""></option>
													<c:forEach items="${areas}" var="area">
														<option value="${area.codigo}">${area.valor}</option>
													</c:forEach>
												</select>
											</div>
											</section>
                                   		</div>
                                   		<!-- END ROW -->
                                   		<!-- START ROW -->
                                        <div class="row">
                                   			<section id="silais" hidden="hidden">
                                      		<div class="input-group">
												<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
												<select data-placeholder="<spring:message code="act.select" /> <spring:message code="silais" />" name="codSilaisAtencion" id="codSilaisAtencion" class="select2">
													<option value=""></option>
													<c:forEach items="${entidades}" var="entidad">
														<option value="${entidad.entidadAdtvaId}">${entidad.nombre}</option>
													</c:forEach>
												</select>
											</div>
											</section>
                                   		</div>
                                   		<!-- END ROW -->
                                   		<!-- START ROW -->
                                        <div class="row">
                                   			<section id="departamento" hidden="hidden">
                                      		<div class="input-group">
												<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
												<select data-placeholder="<spring:message code="msg.select.depa" />" name="codDepartamento" id="codDepartamento" class="select2">
													<option value=""></option>
													<c:forEach items="${departamentos}" var="departamento">
														<option value="${departamento.divisionpoliticaId}">${departamento.nombre}</option>
													</c:forEach>
												</select>
											</div>
											</section>
                                   		</div>
                                   		<!-- END ROW -->
                                   		<!-- START ROW -->
                                        <div class="row">
                                   			<section id="municipio" hidden="hidden">
                                      		<div class="input-group">
												<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
												<select data-placeholder="<spring:message code="act.select" /> <spring:message code="muni" />" name="codMunicipio" id="codMunicipio" class="select2">
													<option value=""></option>
												</select>
											</div>
											</section>
                                   		</div>
                                   		<!-- END ROW -->
                                            <!-- START ROW -->
                                            <div class="row">
                                                <section id="zona" hidden="hidden">
                                                    <div class="input-group">
                                                        <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                                                        <select data-placeholder="<spring:message code="act.select" /> <spring:message code="lbl.special.area" />"
                                                                name="codZona" id="codZona" class="select2">
                                                            <option value=""></option>
                                                            <c:forEach items="${zonas}" var="zona">
                                                                <option value="${zona.codigo}">${zona.valor}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </section>
                                            </div>
                                   		<!-- START ROW -->
                                        <div class="row">
                                   			<section id="unidad" hidden="hidden">
                                      		<div class="input-group">
												<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
												<select data-placeholder="<spring:message code="act.select" /> <spring:message code="lbl.health.unit" />" name="codUnidadAtencion" id="codUnidadAtencion" class="select2">
													<option value=""></option>
												</select>
											</div>
											</section>
                                   		</div>
                                   		<!-- END ROW -->

                                            <!-- START ROW -->
                                            <div id="dSubUnits" hidden="hidden" class="row">
                                                <section class="col col-sm-6 col-md-6 col-lg-5">
                                                    <label class="text-left txt-color-blue font-sm"><spring:message code="lbl.include.subunits"/></label>

                                                </section>

                                                <section class="col col-sm-4 col-md-3 col-lg-2">
                                                    <label class="checkbox">
                                                        <input type="checkbox" checked name="ckUS" id="ckUS">
                                                        <i></i>
                                                    </label>
                                                </section>
                                            </div>
                                            <!-- END ROW -->
                                   		<!-- START ROW -->
                                        <div class="row">
                                            <section class="col col-6">
                                            	<label class="text-left txt-color-blue font-sm">
                                            		<spring:message code="lbl.ento.start.date" />
                                            	</label>
                                                <div class="">
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar"></i>
                                                        <input type="text" name="fechaInicial" id="fechaInicial"
                                                               placeholder="<spring:message code="lbl.ento.start.date"/>"
                                                               class="form-control from_date" data-date-end-date="+0d"/>
                                                    </label>
                                                </div>
                                        	</section>
                                            <section class="col col-6">
	                                            <label class="text-left txt-color-blue font-sm">
                                            		<spring:message code="lbl.ento.end.date" />
                                            	</label>
                                                <div class="">
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar"></i>
                                                        <input type="text" name="fechaFinal" id="fechaFinal"
                                                               placeholder="<spring:message code="lbl.ento.end.date"/>"
                                                               class="form-control to_date" data-date-end-date="+0d"/>
                                                    </label>
                                                </div>
                                       		</section>
                                            </div>
                                            <!-- END ROW -->
                                   		<footer>
											<button type="submit" id="view-report" class="btn btn-info"><i class="fa fa-refresh"></i> <spring:message code="update" /></button>
										</footer>
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
					<!-- NEW WIDGET START -->
					<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<!-- Widget ID (each widget will need unique ID)-->
						<div class="jarviswidget" id="wid-id-3">
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
								<h2><spring:message code="lbl.widgettitle.weekly.report.graph1"/></h2>
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
                                    <div id="noroll" style="width:100%; height:600px;"></div>
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
	<spring:url value="/resources/js/plugin/datatables/swf/copy_csv_xls_pdf.swf" var="dataTablesTTSWF" />
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

    <!-- bootstrap datepicker -->
    <spring:url value="/resources/js/plugin/bootstrap-datepicker/bootstrap-datepicker.js" var="datepickerPlugin" />
    <script src="${datepickerPlugin}"></script>
    <spring:url value="/resources/js/plugin/bootstrap-datepicker/locales/bootstrap-datepicker.{languagedt}.js" var="datePickerLoc">
        <spring:param name="languagedt" value="${pageContext.request.locale.language}" /></spring:url>
    <script src="${datePickerLoc}"></script>

    <!-- JQUERY BLOCK UI -->
	<spring:url value="/resources/js/plugin/jquery-blockui/jquery.blockUI.js" var="jqueryBlockUi" />
	<script src="${jqueryBlockUi}"></script>

    <!-- DYGRAPH -->
    <spring:url value="/resources/js/plugin/dygraphs/dygraph-combined.min.js" var="dyGgraph" />
    <script src="${dyGgraph}"></script>

	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<spring:url value="/resources/scripts/reportes/porDia.js" var="diaJs" />
	<script src="${diaJs}"></script>
    <spring:url value="/resources/scripts/utilidades/seleccionUnidadReporte.js" var="seleccionUnidadReporte" />
    <script src="${seleccionUnidadReporte}"></script>
    <spring:url value="/resources/scripts/utilidades/handleDatePickers.js" var="handleDatePickers" />
    <script src="${handleDatePickers}"></script>
	<!-- END PAGE LEVEL SCRIPTS -->
	<spring:url value="/reportes/getDataPorDia/" var="sActionUrl"/>
	<c:set var="blockMess"><spring:message code="blockUI.message" /></c:set>
    <c:set var="noData"><spring:message code="lbl.no.data" /></c:set>
    <c:set var="msgNoData"><spring:message code="msg.no.data.found" /></c:set>
    <c:set var="nicaragua"><spring:message code="lbl.nicaragua.republic" /></c:set>
    <c:set var="departamento"><spring:message code="lbl.dep" /></c:set>
    <c:set var="municipio"><spring:message code="lbl.munic" /></c:set>
    <c:set var="unidad"><spring:message code="sindfeb.unidad" /></c:set>
    <c:set var="desde"><spring:message code="lbl.from" /></c:set>
    <c:set var="hasta"><spring:message code="lbl.to" /></c:set>
    <c:set var="casos"><spring:message code="lbl.cases" /></c:set>
    <c:set var="zona"><spring:message code="lbl.special.area" />:</c:set>
	<spring:url var="municipiosURL" value="/api/v1/municipiosbysilais"/>
	<spring:url var="unidadesUrl"   value="/api/v1/uniRepPorSilaisyMuni"  />
	<script type="text/javascript">
		$(document).ready(function() {
			pageSetUp();
			var parametros = {sActionUrl: "${sActionUrl}",
					blockMess:"${blockMess}",
					municipiosUrl:"${municipiosURL}",
					unidadesUrl: "${unidadesUrl}",
					dataTablesTTSWF: "${dataTablesTTSWF}",
                noData: "${noData}",
                msgNoData: "${msgNoData}",
                nicaragua : "${nicaragua}",
                departamento : "${departamento}",
                municipio : "${municipio}",
                unidad : "${unidad}",
                desde : "${desde}",
                hasta : "${hasta}",
                casos : "${casos}",
                zona : "${zona}"
            };
			ViewReportDay.init(parametros);
            SeleccionUnidadReporte.init(parametros);
            handleDatePickers("${pageContext.request.locale.language}");
            $("li.reportes").addClass("open");
	    	$("li.day").addClass("active");
	    	if("top"!=localStorage.getItem("sm-setmenu")){
	    		$("li.day").parents("ul").slideDown(200);
	    	}
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>