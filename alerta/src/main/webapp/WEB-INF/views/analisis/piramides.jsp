<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<!-- BEGIN HEAD -->
<head>
	<jsp:include page="../fragments/headTag.jsp" />
	<style>
		/* columns right and center aligned datatables */
        .aw-right {
            padding-left: 0;
            padding-right: 10px;
            text-align: right;
        }
        td.highlight {
        	font-weight: bold;
        	color: red;
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
				<spring:message code="analysis" /> <i class="fa fa-angle-right"></i> <a href="<spring:url value="/analisis/piramides/" htmlEscape="true "/>"><spring:message code="pyram" /></a></li>
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
						<i class="fa-fw fa fa-bar-chart-o"></i> 
							<spring:message code="analysis" />
						<span> <i class="fa fa-angle-right"></i>  
							<spring:message code="pyram" />
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
					<article class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
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
									<form id="parameters_form" class ="smart-form">
										<fieldset>
										<!-- START ROW -->
                                      	<div class="row">
                                      		<section>
                                      		<div class="input-group">
												<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
												<select data-placeholder="<spring:message code="act.select" /> <spring:message code="level" />" name="codArea" id="codArea" class="select2">
                                                    <option value=""></option>
                                                    <c:forEach items="${areas}" var="area">
                                                        <c:if test="${area.codigo ne 'AREAREP|UNI' and area.codigo ne 'AREAREP|ZE'}">
                                                            <option value="${area.codigo}">${area.valor}</option>
                                                        </c:if>
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
                                        <div class="row">
                                            <section class="col col-6">
                                            	<label class="text-left txt-color-blue font-sm">
                                            		<spring:message code="year1" />
                                            	</label>
	                                        	<div class="input-group">
													<span class="input-group-addon"></span>
													<select name="anioI" id="anioI" class="select2">
                                                        <option value=""></option>
                                                        <c:forEach items="${anios}" var="anio">
															<option value="${anio.valor}">${anio.valor}</option>
														</c:forEach>
													</select>
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
					<!-- NEW WIDGET START -->
					<article class="col-xs-12 col-sm-12 col-md-8 col-lg-8">
						<!-- Widget ID (each widget will need unique ID)-->
						<div class="jarviswidget" id="wid-id-1">
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
								<h2><spring:message code="pyram" /></h2>				
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
									<div id="lineChart-title" align="center"></div>
									<div id="pyramid" style="width:100%;height:400px;"></div>
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
	<!-- Flot Chart Plugin: Flot Engine, Flot Pyramid -->
	<spring:url value="/resources/js/plugin/flot/jquery.flot.js" var="jqueryFlot" />
	<script src="${jqueryFlot}"></script>
	<spring:url value="/resources/js/plugin/flot/jquery.flot.pyramid.js" var="jqueryFlotPyr" />
	<script src="${jqueryFlotPyr}"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<spring:url value="/resources/scripts/analisis/piramides.js" var="piramides" />
	<script src="${piramides}"></script>
	<spring:url value="/resources/scripts/utilidades/seleccionRegionSIVE.js" var="seleccionRegionSIVE" />
	<script src="${seleccionRegionSIVE}"></script>
	<!-- END PAGE LEVEL SCRIPTS -->
	<spring:url value="/analisis/piramidesdata/" var="sActionUrl"/>
	<c:set var="blockMess"><spring:message code="blockUI.message" /></c:set>
	<spring:url var="municipiosURL" value="/api/v1/municipiosbysilais"/>
	<spring:url var="unidadesUrl"   value="/api/v1/unidadesPorSilaisyMuni"  />
	<script type="text/javascript">
		$(document).ready(function() {
			pageSetUp();
			var parametros = {sActionUrl: "${sActionUrl}",
					blockMess:"${blockMess}",
					municipiosUrl:"${municipiosURL}",
					unidadesUrl: "${unidadesUrl}",
					dataTablesTTSWF: "${dataTablesTTSWF}"};
			ViewReport.init(parametros);
			SeleccionRegionSIVE.init(parametros);
	    	$("li.analisis").addClass("open");
	    	$("li.piramides").addClass("active");
	    	if("top"!=localStorage.getItem("sm-setmenu")){
	    		$("li.piramides").parents("ul").slideDown(200);
	    	}
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>