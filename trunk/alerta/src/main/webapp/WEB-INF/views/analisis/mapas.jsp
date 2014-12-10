<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<!-- BEGIN HEAD -->
<head>
	<jsp:include page="../fragments/headTag.jsp" />
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
				<li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a> <i class="fa fa-angle-right"></i> <a href="<spring:url value="#" htmlEscape="true "/>"><spring:message code="lbl.breadcrumb" /></a></li>
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
							<spring:message code="lbl.pageheader" />
						<span> <i class="fa fa-angle-right"></i>  
							<spring:message code="lbl.pagesubtitle" />
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
			<!-- START ROW -->
				<div class="row">
					<!-- NEW COL START -->
					<article class="col-sm-12 col-md-12 col-lg-12">
						<!-- Widget ID (each widget will need unique ID)-->
						<div class="jarviswidget" id="wid-id-1"
							data-widget-editbutton="false"
							data-widget-fullscreenbutton="false"
							data-widget-custombutton="false">
							<header>
								<span class="widget-icon"> <i class="fa fa-edit"></i>
								</span>
								<h2>
									<spring:message code="mapopt" />
								</h2>
							</header>

							<!-- widget div-->
							<div>
								<!-- widget content -->
								<div class="widget-body no-padding">

									<form id="checkout-form" class="smart-form"
										novalidate="novalidate">

										<fieldset>
											<div class="row">
												<label class="label col col-2"><spring:message code="detail" /></label>
												<section class="col col-2">
													<label class="select"> <select name="detail">
															<option value="1">
																<spring:message code="level1" />
															</option>
															<option value="2">
																<spring:message code="level2" />
															</option>
															<option value="3">
																<spring:message code="level3" />
															</option>
															<option value="4">
																<spring:message code="level4" />
															</option>
													</select> <i></i>
													</label>
												</section>
												<label class="label col col-2"><spring:message code="subcomp" /></label>
												<section class="col col-2">
													<label class="select"> <select name="subcomp">
															<option value="0">
																<spring:message code="none" />
															</option>
															<option value="1">
																<spring:message code="level1" />
															</option>
															<option value="2">
																<spring:message code="level2" />
															</option>
															<option value="3">
																<spring:message code="level3" />
															</option>
													</select> <i></i>
													</label>
												</section>
												<label class="label col col-2"><spring:message code="totcomp" /></label>
												<section class="col col-2">
													<label class="select"> <select name="totcomp">
															<option value="0">
																<spring:message code="none" />
															</option>
															<option value="1">
																<spring:message code="level0" />
															</option>
													</select> <i></i>
													</label>
												</section>
											</div>
										</fieldset>
										<fieldset>
											<div class="row">
												<label class="label col col-1"><spring:message code="rep" /></label>
												<section class="col col-2">
													<label class="select"> <select name="rep">
															<option value="1">
																<spring:message code="numandrate" />
															</option>
															<option value="2">
																<spring:message code="cases" />
															</option>
													</select> <i></i>
													</label>
												</section>
												<label class="label col col-1"><spring:message code="sex" /></label>
												<section class="col col-2">
													<label class="select"> <select name="sex">
															<option value="1">
																<spring:message code="both" />
															</option>
															<option value="2">
																<spring:message code="male" />
															</option>
															<option value="3">
																<spring:message code="female" />
															</option>
													</select> <i></i>
													</label>
												</section>
												<label class="label col col-1"><spring:message code="interval" /></label>
												<section class="col col-2">
													<label class="select"> <select name="interval">
															<option value="1">
																<spring:message code="polynomial" />
															</option>
															<option value="2">
																<spring:message code="lineal" />
															</option>
													</select> <i></i>
													</label>
												</section>
												<label class="label col col-1"><spring:message code="design" /></label>
												<section class="col col-2">
													<label class="select"> <select name="design">
															<option value="1">
																<spring:message code="design-1" />
															</option>
															<option value="2">
																<spring:message code="design-2" />
															</option>
															<option value="3">
																<spring:message code="design-3" />
															</option>
													</select> <i></i>
													</label>
												</section>
											</div>
										</fieldset>
										<fieldset>
											<div class="row">
												<label class="label col col-1"><spring:message code="pato" /></label>
												<section class="col col-2">
													<label class="select"> <select name="pato">
															<option value="1">Enfermedad Diarreica Aguda</option>
															<option value="2">Infecciones Respiratorias Agudas</option>
													</select> <i></i>
													</label>
												</section>	
												<label class="label col col-1"><spring:message code="period" /></label>
												<section class="col col-2">
													<label class="select"> <select name="period">
															<option value="1">Hasta la semana</option>
															<option value="2">Entre la semana</option>
													</select> <i></i>
													</label>
												</section>
												<section class="col col-1">
													<label class="input">
														<input type="text" name="sem1" placeholder="<spring:message code="week" />" data-mask="99">
													</label>
												</section>
												<section class="col col-1">
													<label class="input">
														<input type="text" name="year" placeholder="Año" data-mask="2099">
													</label>
												</section>	
											</div>
										</fieldset>
										<fieldset>
											<section>
											<select id="cdeptos2" name="depto2" class="select">
												<option value="0"><spring:message code="level2" /></option>
													<c:forEach items="${departamentos}" var="departamento">
														<option value="${departamento.divisionpoliticaId}">${departamento.nombre}</option>
													</c:forEach>
												</select>
											</section>
										</fieldset>
										<footer>
											<button type="submit" class="btn btn-primary">
												<spring:message code="update" />
											</button>
										</footer>
									</form>

								</div>
								<!-- end widget content -->

							</div>
							<!-- end widget div -->

						</div>
						<!-- end widget -->
					</article>
					<!-- END COL -->
				</div>
				<!-- END ROW -->
				<!-- row -->
				<div class="row">
					<article class="col-sm-12 col-md-12 col-lg-6">
						<!-- new widget -->
						<div class="jarviswidget" id="wid-id-2"
							data-widget-editbutton="true">
							<header>
								<span class="widget-icon"> <i class="fa fa-map-marker"></i>
								</span>
								<h2>
									<spring:message code="incrate" />
								</h2>
							</header>
							<!-- widget div-->
							<div>
								<div class="widget-body no-padding">
									<!-- content goes here -->
									<div id="vector-map" class="vector-map"></div>
									<div id="heat-fill">
										<span class="fill-a">0</span> <span class="fill-b">5000</span>
									</div>
									<table class="table table-striped table-hover table-condensed">
										<thead>
											<tr>
												<th>Departamento</th>
												<th>Casos</th>
												<th>Tasa x 1000 hab</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td><a href="javascript:void(0);">Nueva Segovia</a></td>
												<td>23443</td>
												<td>4977</td>
											</tr>
											<tr>
												<td><a href="javascript:void(0);">Chinandega</a></td>
												<td>18987</td>
												<td>4873</td>
											</tr>
											<tr>
												<td><a href="javascript:void(0);">RAAN</a></td>
												<td>15896</td>
												<td>3671</td>
											</tr>
											<tr>
												<td><a href="javascript:void(0);">Matagalpa</a></td>
												<td>12050</td>
												<td>2476</td>
											</tr>
											<tr>
												<td><a href="javascript:void(0);">Jinotega</a></td>
												<td>8975</td>
												<td>1476</td>
											</tr>
											<tr>
												<td><a href="javascript:void(0);">Managua</a></td>
												<td>10255</td>
												<td>146</td>
											</tr>
											<tr>
												<td><a href="javascript:void(0);">RAAS</a></td>
												<td>5421</td>
												<td>134</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</article>

					<article class="col-sm-12 col-md-12 col-lg-6">
						<!-- new widget -->
						<div class="jarviswidget" id="wid-id-3"
							data-widget-editbutton="true">
							<header>
								<span class="widget-icon"> <i class="fa fa-map-marker"></i>
								</span>
								<h2>
									<spring:message code="cases" />
								</h2>
							</header>
							<!-- widget div-->
							<div>
								<div class="widget-body no-padding">
									<!-- content goes here -->
									<div id="bar-chart" class="chart"></div>
								</div>
							</div>
						</div>
						<!-- new widget -->
						<div class="jarviswidget" id="wid-id-4"
							data-widget-editbutton="true">
							<header>
								<span class="widget-icon"> <i class="fa fa-map-marker"></i>
								</span>
								<h2>
									<spring:message code="cases" />
								</h2>
							</header>
							<!-- widget div-->
							<div>
								<div class="widget-body no-padding">
									<!-- content goes here -->
									<div id="pie-chart" class="chart"></div>
								</div>
							</div>
						</div>
					</article>
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
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<!-- END PAGE LEVEL SCRIPTS -->
	<script type="text/javascript">
		$(document).ready(function() {
			pageSetUp();
			$("li.analisis").addClass("open");
	    	$("li.mapas").addClass("active");
	    	if("top"!=localStorage.getItem("sm-setmenu")){
	    		$("li.mapas").parents("ul").slideDown(200);
	    	}
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>