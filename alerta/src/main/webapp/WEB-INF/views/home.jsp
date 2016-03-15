<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<!-- BEGIN HEAD -->
<head>
	<jsp:include page="fragments/headTag.jsp" />
    <spring:url value="/resources/js/plugin/chartjs/chartjs.css" var="chartjsCss" />
    <link href="${chartjsCss}" rel="stylesheet" type="text/css"/>
    <spring:url value="/resources/js/plugin/vectormap/jquery-jvectormap-2.0.4.css" var="vMapCss" />
    <link href="${vMapCss}" rel="stylesheet" type="text/css"/>
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
        .jvectormap-legend-icons {
            background: white;
            border: black 1px solid;
        }
        .jvectormap-legend-icons {
            color: black;
        }
    </style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="">
	<!-- #HEADER -->
	<jsp:include page="fragments/bodyHeader.jsp" />
	<!-- #NAVIGATION -->
	<jsp:include page="fragments/bodyNavigation.jsp" />
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
				<li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a></li>
			</ol>
			<!-- end breadcrumb -->
			<jsp:include page="fragments/layoutOptions.jsp" />
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
							<spring:message code="menu.home" />
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
					<article class="col-sm-12">
						<!-- new widget -->
						<div class="jarviswidget" id="wid-id-0" data-widget-togglebutton="false" 
							data-widget-editbutton="false" data-widget-fullscreenbutton="false" data-widget-colorbutton="false" data-widget-deletebutton="false">
                            <input id="semanaI" hidden="hidden" value="${semanaI}" type="text" name="semanaI"/>
                            <input id="semanaF" hidden="hidden" value="${semanaF}" type="text" name="semanaF"/>
                            <input id="anioI" hidden="hidden" value="${anioI}" type="text" name="anioI"/>
                            <input id="anioF" hidden="hidden" value="${anioF}" type="text" name="anioF"/>
                            <input id="nivelUsuario" hidden="hidden" value="${nivel}" type="text" name="nivelUsuario"/>
                            <input id="semana" type="hidden" value="<spring:message code="week"/>"/>
                            <input id="semanaHasta" type="hidden" value="<spring:message code="hweek"/>"/>
                            <input id="nivel" type="hidden" value="<spring:message code="lbl.level"/>"/>
                            <input id="dengueConfirmado" type="hidden" value="<spring:message code="lbl.dengue.confirmed"/> "/>
                            <input id="dengueSospechoso" type="hidden" value="<spring:message code="lbl.dengue.suspect"/> "/>
                            <input id="dengueConfirmadoMAY" type="hidden" value="<spring:message code="lbl.dengue.confirmed.upper"/>"/>
                            <input id="dengueSospechosoMAY" type="hidden" value="<spring:message code="lbl.dengue.suspect.upper"/>"/>
                            <input id="nivelNac" type="hidden" value="<spring:message code="lbl.national"/>"/>
                            <input id="nivelUS" type="hidden" value="<spring:message code="lbl.us.assigned"/>"/>
                            <input id="nivelSilais" type="hidden" value="<spring:message code="lbl.SILAIS.assigned"/>"/>
                            <input id="week" type="hidden" value="<spring:message code="year2o"/>"/>
							<header>
								<span class="widget-icon"> <i class="glyphicon glyphicon-stats txt-color-darken"></i> </span>
								<h2><spring:message code="lbl.dengue.confirmed"/></h2>

								<ul class="nav nav-tabs pull-right in" id="myTab">
									<li class="active">
										<a data-toggle="tab" href="#s1"><i class="fa fa-clock-o"></i> <span class="hidden-mobile hidden-tablet">Casos</span></a>
									</li>

									<li>
										<a data-toggle="tab" href="#s2"><i class="fa fa-clock-o"></i> <span class="hidden-mobile hidden-tablet">Tasas</span></a>
									</li>
								</ul>

							</header>
							<!-- widget div-->
							<div class="no-padding">
								<div class="widget-body">
									<!-- content -->
									<div id="myTabContent" class="tab-content">
										<div class="tab-pane fade active in padding-10 no-padding-bottom" id="s1">
											<div class="widget-body">
                                                <div id="lineChart1-title" align="center"></div>
                                                <div id="lineLegend1"></div>
												<canvas id="lineChart1" height="120"></canvas>
											</div>
										</div>
										<!-- end s1 tab pane -->
										<div class="tab-pane fade padding-10 no-padding-bottom" id="s2">
											<div class="widget-body">
                                                <div id="lineChart2-title" align="center"></div>
                                                <div id="lineLegend2"></div>
                                                <canvas id="lineChart2" height="120"></canvas>
											</div>
										</div>
										<!-- end s2 tab pane -->
									</div>
								</div>
							</div>
							<!-- end widget div -->
						</div>
						<!-- end widget -->
					</article>
				</div>
				<!-- end row -->
                <!-- row -->
                <div class="row">
                    <article class="col-sm-12">
                        <!-- new widget -->
                        <div class="jarviswidget" id="wid-id-1" data-widget-togglebutton="false"
                             data-widget-editbutton="false" data-widget-fullscreenbutton="false" data-widget-colorbutton="false" data-widget-deletebutton="false">

                            <header>
                                <span class="widget-icon"> <i class="glyphicon glyphicon-stats txt-color-darken"></i> </span>
                                <h2><spring:message code="lbl.dengue.suspect"/></h2>

                                <ul class="nav nav-tabs pull-right in" id="myTab2">
                                    <li class="active">
                                        <a data-toggle="tab" href="#ss1"><i class="fa fa-clock-o"></i> <span class="hidden-mobile hidden-tablet">Casos</span></a>
                                    </li>

                                    <li>
                                        <a data-toggle="tab" href="#ss2"><i class="fa fa-clock-o"></i> <span class="hidden-mobile hidden-tablet">Tasas</span></a>
                                    </li>
                                </ul>

                            </header>
                            <!-- widget div-->
                            <div class="no-padding">
                                <div class="widget-body">
                                    <!-- content -->
                                    <div id="myTabContent2" class="tab-content">
                                        <div class="tab-pane fade in active padding-10 no-padding-bottom" id="ss1">
                                            <div class="widget-body">
                                                <div id="lineChart3-title" align="center"></div>
                                                <div id="lineLegend3"></div>
                                                <canvas id="lineChart3" height="120"></canvas>
                                            </div>
                                        </div>
                                        <!-- end s1 tab pane -->
                                        <div class="tab-pane fade padding-10 no-padding-bottom" id="ss2">
                                            <div class="widget-body">
                                                <div id="lineChart4-title" align="center"></div>
                                                <div id="lineLegend4"></div>
                                                <canvas id="lineChart4" height="120"></canvas>
                                            </div>
                                        </div>
                                        <!-- end s2 tab pane -->
                                    </div>
                                </div>
                            </div>
                            <!-- end widget div -->
                        </div>
                        <!-- end widget -->
                    </article>
                </div>
                <!-- end row -->

                <div class="row" id="divMapas">
                    <!-- NEW WIDGET START -->
                    <article class="col-xs-12 col-sm-12 col-md-8 col-lg-8">
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
                                <h2><spring:message code="lbl.dengue.confirmed" /></h2>
                            </header>
                            <!-- widget div-->
                            <div>
                                <!-- widget edit box -->
                                <div class="jarviswidget-editbox">
                                    <!-- This area used as dropdown edit box -->
                                    <input class="form-control" type="text">
                                </div>
                                <!-- end widget edit box -->
                                <div class="widget-body-toolbar bg-color-white smart-form" id="rev-toggles2">
                                    <form id="parameters_form2" class ="smart-form">
                                        <div class="inline-group">
                                            <section class="col col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <label class="radio">
                                                    <input type="radio" name="nivelPais2" id="rbNPSILAIS" value="true" checked="checked">
                                                    <i></i><spring:message code="lbl.silais"/></label>
                                            </section>
                                            <section class="col col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                                <label class="radio">
                                                    <input type="radio" name="nivelPais2" value="false" id="rbNPMunicipio">
                                                    <i></i><spring:message code="lbl.municps"/></label>
                                            </section>
                                        </div>
                                    </form>
                                </div>
                                <!-- widget content -->
                                <div class="widget-body">
                                    <!-- this is what the user will see -->
                                    <div id="vector-map2" class="vector-map" style="width:100%; height:500px;"></div>
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
                        <div class="jarviswidget" id="wid-id-2">
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
                                <h2><spring:message code="lbl.dengue.suspect" /></h2>
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
                                <div class="widget-body-toolbar bg-color-white smart-form" id="rev-toggles">
                                    <form id="parameters_form" class ="smart-form">
                                    <div class="inline-group">

                                        <section class="col col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                            <label class="radio">
                                                <input type="radio" name="nivelPais" id="rbNPSILAIS" value="true" checked="checked">
                                                <i></i><spring:message code="lbl.silais"/></label>
                                        </section>
                                        <section class="col col-xs-6 col-sm-6 col-md-6 col-lg-6">
                                            <label class="radio">
                                                <input type="radio" name="nivelPais" value="false" id="rbNPMunicipio">
                                                <i></i><spring:message code="lbl.municps"/></label>
                                        </section>
                                    </div>
                                        </form>
                                </div>
                                <div class="widget-body">
                                    <!-- this is what the user will see -->
                                    <div id="vector-map" class="vector-map" style="width:100%; height:500px;"></div>
                                </div>
                                <!-- end widget content -->
                            </div>
                            <!-- end widget div -->
                        </div>
                        <!-- end widget -->
                    </article>
                    <!-- WIDGET END -->
                </div>
			</section>
			<!-- end widget grid -->
		</div>
		<!-- END MAIN CONTENT -->
	</div>
	<!-- END MAIN PANEL -->
	<!-- BEGIN FOOTER -->
	<jsp:include page="fragments/footer.jsp" />
	<!-- END FOOTER -->
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<jsp:include page="fragments/corePlugins.jsp" />
	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<!-- Flot Chart Plugin: Flot Engine, Flot Resizer, Flot Tooltip -->
	<spring:url value="/resources/js/plugin/flot/jquery.flot.cust.min.js" var="jqueryFlot" />
	<script src="${jqueryFlot}"></script>
	<spring:url value="/resources/js/plugin/flot/jquery.flot.resize.min.js" var="jqueryFlotResize" />
	<script src="${jqueryFlotResize}"></script>
	<spring:url value="/resources/js/plugin/flot/jquery.flot.time.min.js" var="jqueryFlotTime" />
	<script src="${jqueryFlotTime}"></script>
	<spring:url value="/resources/js/plugin/flot/jquery.flot.tooltip.min.js" var="jqueryFlotToolTip" />
	<script src="${jqueryFlotToolTip}"></script>
    <!-- jQuery Chart JS-->
    <spring:url value="/resources/js/plugin/chartjs/chart.min.js" var="chartJs"/>
    <script src="${chartJs}"></script>
    <spring:url value="/resources/js/plugin/chartjs/legend.js" var="legendChartJs"/>
    <script src="${legendChartJs}"></script>
    <!-- JQUERY BLOCK UI -->
    <spring:url value="/resources/js/plugin/jquery-blockui/jquery.blockUI.js" var="jqueryBlockUi" />
    <script src="${jqueryBlockUi}"></script>

    <!-- Vector Maps Plugin: Vectormap engine, Vectormap language -->
    <spring:url value="/resources/js/plugin/vectormap/jquery-jvectormap-2.0.4.min.js" var="jqueryVectorMap" />
	<script src="${jqueryVectorMap}"></script>
	<spring:url value="/resources/js/plugin/vectormap/nic-map-sil.js" var="jqueryVectorMapSilais" />
	<script src="${jqueryVectorMapSilais}"></script>
    <spring:url value="/resources/js/plugin/vectormap/nic-map-mun.js" var="jqueryVectorMapMunicipios" />
    <script src="${jqueryVectorMapMunicipios}"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
    <spring:url value="/resources/scripts/inicio/inicio.js" var="inicio" />
    <script src="${inicio}"></script>
    <c:url var="sActionUrl" value="/inicio/casostasasdata"/>
    <c:url var="sMapasUrl" value="/inicio/mapasdata"/>
    <c:set var="blockMess"><spring:message code="blockUI.message" /></c:set>
	<!-- END PAGE LEVEL SCRIPTS -->
	<script type="text/javascript">
		$(document).ready(function() {
			pageSetUp();

            var parametros = {sActionUrl: "${sActionUrl}",
                sMapasUrl: "${sMapasUrl}",
                blockMess:"${blockMess}"};
            PaginaInicio.init(parametros);
            $("li.home").addClass("active");
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>