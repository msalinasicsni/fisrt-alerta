<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<!-- BEGIN HEAD -->
<head>
	<jsp:include page="fragments/headTag.jsp" />
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
							
							<header>
								<span class="widget-icon"> <i class="glyphicon glyphicon-stats txt-color-darken"></i> </span>
								<h2>Situación del Dengue </h2>

								<ul class="nav nav-tabs pull-right in" id="myTab">
									<li class="active">
										<a data-toggle="tab" href="#s1"><i class="fa fa-clock-o"></i> <span class="hidden-mobile hidden-tablet">Análisis</span></a>
									</li>

									<li>
										<a data-toggle="tab" href="#s2"><i class="fa fa-clock-o"></i> <span class="hidden-mobile hidden-tablet">Por Mes</span></a>
									</li>

									<li>
										<a data-toggle="tab" href="#s3"><i class="fa fa-map-marker"></i> <span class="hidden-mobile hidden-tablet">Por semana</span></a>
									</li>
								</ul>

							</header>
							<!-- widget div-->
							<div class="no-padding">
								<div class="widget-body">
									<!-- content -->
									<div id="myTabContent" class="tab-content">
										<div class="tab-pane fade active in padding-10 no-padding-bottom" id="s1">
											<div class="widget-body-toolbar bg-color-white smart-form" id="rev-toggles">

												<div class="inline-group">

													<label for="gra-0" class="checkbox">
														<input type="checkbox" name="gra-0" id="gra-0" checked="checked">
														<i></i> Casos </label>
													<label for="gra-1" class="checkbox">
														<input type="checkbox" name="gra-1" id="gra-1" checked="checked">
														<i></i> Positividad </label>
													<label for="gra-2" class="checkbox">
														<input type="checkbox" name="gra-2" id="gra-2" checked="checked">
														<i></i> Presencia vectores </label>
												</div>
											</div>
											<div class="padding-10">
												<div id="flotcontainer" class="chart-large has-legend-unique"></div>
											</div>
										</div>
										<!-- end s1 tab pane -->
										<div class="tab-pane fade" id="s2">
											<div class="padding-10">
												<div id="statsChart" class="chart-large has-legend-unique"></div>
											</div>
										</div>
										<!-- end s2 tab pane -->
										<div class="tab-pane fade" id="s3">
											<div class="row no-space">
												<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
													<div id="updating-chart" class="chart-large txt-color-blue"></div>
												</div>
											</div>
										</div>
										<!-- end s3 tab pane -->	
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
					<article class="col-sm-12 col-md-12 col-lg-6">
						<!-- new widget -->
						<div class="jarviswidget" id="wid-id-1" data-widget-editbutton="true">
							<header>
								<span class="widget-icon"> <i class="fa fa-map-marker"></i> </span>
								<h2>Situación del Dengue</h2>
							</header>
							<!-- widget div-->
							<div>
								<div class="widget-body no-padding">
									<!-- content goes here -->
									<div id="vector-map" class="vector-map"></div>
									<div id="heat-fill">
										<span class="fill-a">0</span>
										<span class="fill-b">5000</span>
									</div>
									<table class="table table-striped table-hover table-condensed">
										<thead>
											<tr>
												<th>SILAIS</th>
												<th>Tasa x 1000 hab</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td><a href="javascript:void(0);">Nueva Segovia</a></td>
												<td>4977</td>
											</tr>
											<tr>
												<td><a href="javascript:void(0);">Chinandega</a></td>
												<td>4873</td>
											</tr>
											<tr>
												<td><a href="javascript:void(0);">RAAN</a></td>
												<td>3671</td>
											</tr>
											<tr>
												<td><a href="javascript:void(0);">Matagalpa</a></td>
												<td>2476</td>
											</tr>
											<tr>
												<td><a href="javascript:void(0);">Jinotega</a></td>
												<td>1476</td>
											</tr>
											<tr>
												<td><a href="javascript:void(0);">Managua</a></td>
												<td>146</td>
											</tr>
											<tr>
												<td><a href="javascript:void(0);">RAAS</a></td>
												<td>134</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</article>
				</div>
				<!-- end row -->
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
	<!-- Vector Maps Plugin: Vectormap engine, Vectormap language -->
	<spring:url value="/resources/js/plugin/vectormap/jquery-jvectormap-1.2.2.min.js" var="jqueryVectorMap" />
	<script src="${jqueryVectorMap}"></script>
	<spring:url value="/resources/js/plugin/vectormap/nic-map-sil.js" var="jqueryVectorMapSilais" />
	<script src="${jqueryVectorMapSilais}"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<!-- END PAGE LEVEL SCRIPTS -->
	<script>
	    $(function () {
	    	$("li.home").addClass("active");
	    });
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
			pageSetUp();
			
			/*
			 * PAGE RELATED SCRIPTS
			 */

			/*
			* VECTOR MAP
			*/

					data_array = {
						"11" : 4977,
						"21" : 4873,
						"71" : 3671,
						"62" : 2476,
						"61" : 1476,
						"31" : 146,
						"81" : 134,
						"22" : 100,
						"51" : 96,
						"52" : 52,
						"12" : 45,
						"13" : 23,
						"43" : 12,
						"91" : 11,
						"41" : 5,
						"42" : 2,
						"44" : 0
					};

					$('#vector-map').vectorMap({
						map : 'nicaragua_mill_en',
						backgroundColor : '#fff',
						regionStyle : {
							initial : {
								fill : '#c4c4c4'
							},
							hover : {
								"fill-opacity" : 1
							}
						},
						series : {
							regions : [{
								values : data_array,
								scale : ['#F3F6F8', '#4d7686'],
								normalizeFunction : 'polynomial'
							}]
						},
						onRegionLabelShow : function(e, el, code) {
							if ( typeof data_array[code] == 'undefined') {
								e.preventDefault();
							} else {
								var countrylbl = data_array[code];
								el.html(el.html() + ': ' + countrylbl + ' x 1000 hab');
							}
						}
					});
			
					/*
					* RUN PAGE GRAPHS
					*/

					/* TAB 1: UPDATING CHART */
					
					var data = [], totalPoints = 52, $UpdatingChartColors = $("#updating-chart").css('color');

					function getRandomData() {
						if (data.length > 0)
							data = data.slice(1);

						// do a random walk
						while (data.length < totalPoints) {
							var prev = data.length > 0 ? data[data.length - 1] : 50;
							var y = prev + Math.random() * 10 - 5;
							if (y < 0)
								y = 0;
							if (y > 100)
								y = 100;
							data.push(y);
						}

						// zip the generated y values with the x values
						var res = [];
						for (var i = 0; i < data.length; ++i)
							res.push([i, data[i]]);
						return res;
					}


					// setup plot
					var options = {
						yaxis : {
							min : 0,
							max : 100
						},
						xaxis : {
							min : 0,
							max : 53
						},
						colors : [$UpdatingChartColors],
						series : {
							lines : {
								lineWidth : 1,
								fill : true,
								fillColor : {
									colors : [{
										opacity : 0.4
									}, {
										opacity : 0
									}]
								},
								steps : false

							}
						}
					};

					var plot = $.plot($("#updating-chart"), [getRandomData()], options);


					/*end updating chart*/

					/* TAB 2: Monthly report  */

					$(function() {
						// jQuery Flot Chart
						var sospechosos = [[1, 27], [2, 34], [3, 51], [4, 48], [5, 55], [6, 65], [7, 61], [8, 70], [9, 65], [10, 75], [11, 57], [12, 59], [13, 62]];
						var confirmados = [[1, 25], [2, 31], [3, 45], [4, 37], [5, 38], [6, 40], [7, 47], [8, 55], [9, 43], [10, 50], [11, 47], [12, 39], [13, 47]];
						var data = [{
							label : "Sospechoso",
							data : sospechosos,
							lines : {
								show : true,
								lineWidth : 1,
								fill : true,
								fillColor : {
									colors : [{
										opacity : 0.1
									}, {
										opacity : 0.13
									}]
								}
							},
							points : {
								show : true
							}
						}, {
							label : "Confirmado",
							data : confirmados,
							lines : {
								show : true,
								lineWidth : 1,
								fill : true,
								fillColor : {
									colors : [{
										opacity : 0.1
									}, {
										opacity : 0.13
									}]
								}
							},
							points : {
								show : true
							}
						}];

						var options = {
							grid : {
								hoverable : true
							},
							colors : ["#568A89", "#3276B1"],
							tooltip : true,
							tooltipOpts : {
								//content : "Value <b>$x</b> Value <span>$y</span>",
								defaultTheme : false
							},
							xaxis : {
								ticks : [[1, "ENE"], [2, "FEB"], [3, "MAR"], [4, "ABR"], [5, "MAY"], [6, "JUN"], [7, "JUL"], [8, "AGO"], [9, "SEP"], [10, "OCT"], [11, "NOV"], [12, "DIC"], [13, "ENE+1"]]
							},
							yaxes : {

							}
						};

						var plot3 = $.plot($("#statsChart"), data, options);
					});

					// END TAB 2

					// TAB THREE GRAPH //
					/* TAB 3: Lugar  */

					$(function() {

						var 
						trgt = [[1354586000000, 153], [1364587000000, 658], [1374588000000, 198], [1384589000000, 663], [1394590000000, 801], [1404591000000, 1080], [1414592000000, 353], [1424593000000, 749], [1434594000000, 523], [1444595000000, 258], [1454596000000, 688], [1464597000000, 364]], 
						prft = [[1354586000000, 5.3], [1364587000000, 6.5], [1374588000000, 9.8], [1384589000000, 8.3], [1394590000000, 9.80], [1404591000000, 80.8], [1414592000000, 72.0], [1424593000000, 67.4], [1434594000000, 23], [1444595000000, 79], [1454596000000, 88], [1464597000000, 36]], 
						sgnups = [[1354586000000, 6.47], [1364587000000, 43.5], [1374588000000, 78.4], [1384589000000, 34.6], [1394590000000, 48.7], [1404591000000, 46.3], [1414592000000, 47.9], [1424593000000, 23.6], [1434594000000, 84.3], [1444595000000, 65.7], [1454596000000, 24.1], [1464597000000, 34.1]], toggles = $("#rev-toggles"), target = $("#flotcontainer");

						var data = [{
							label : "Casos",
							data : trgt,
							bars : {
								show : true,
								align : "center",
								barWidth : 30 * 30 * 60 * 1000 * 80
							}
						}, {
							label : "% Positividad",
							data : prft,
							color : '#3276B1',
							yaxis : 2,
							lines : {
								show : true,
								lineWidth : 3
							},
							points : {
								show : true
							}
						}, {
							label : "% Infestacion",
							data : sgnups,
							color : '#71843F',
							yaxis : 2,
							lines : {
								show : true,
								lineWidth : 1
							},
							points : {
								show : true
							}
						}]

						var options = {
							grid : {
								hoverable : true
							},
							tooltip : true,
							tooltipOpts : {
								//content: '%x - %y',
								//dateFormat: '%b %y',
								defaultTheme : false
							},
							xaxis : {
								mode : "time"
							},
							yaxes : [{
								tickFormatter : function(val, axis) {
									return "" + val;
								},
								max : 1200
							}, {
								position : "right"
							} ]

						};

						plot2 = null;

						function plotNow() {
							var d = [];
							toggles.find(':checkbox').each(function() {
								if ($(this).is(':checked')) {
									d.push(data[$(this).attr("name").substr(4, 1)]);
								}
							});
							if (d.length > 0) {
								if (plot2) {
									plot2.setData(d);
									plot2.draw();
								} else {
									plot2 = $.plot(target, d, options);
								}
							}

						};

						toggles.find(':checkbox').on('change', function() {
							plotNow();
						});
						plotNow()

					});

		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>