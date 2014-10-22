<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
				<li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a> <i class="fa fa-angle-right"></i> <a href="<spring:url value="/febriles/create" htmlEscape="true "/>"><spring:message code="menu.sindfeb" /></a></li>
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
						<i class="fa-fw fa fa-fire"></i> 
							<spring:message code="sindfeb.header" />
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
								<h2><spring:message code="sindfeb.header" /> </h2>				
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
									<div class="wizard">
										<ul class="steps">
											<li data-target="#step1" class="active">
												<span class="badge badge-info">1</span><spring:message code="sindfeb.step1" /><span class="chevron"></span>
											</li>
											<li data-target="#step2">
												<span class="badge">2</span><spring:message code="sindfeb.step2" /><span class="chevron"></span>
											</li>
											<li data-target="#step3">
												<span class="badge">3</span><spring:message code="sindfeb.step3" /><span class="chevron"></span>
											</li>
											<li data-target="#step4">
												<span class="badge">4</span><spring:message code="sindfeb.step4" /><span class="chevron"></span>
											</li>
											<li data-target="#step5">
												<span class="badge">5</span><spring:message code="sindfeb.step5" /><span class="chevron"></span>
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
										<form id="fuelux-wizard" class ="smart-form">
											<div class="step-pane active" id="step1">
												<h3><spring:message code="sindfeb.step1.long" /></h3>
												<!-- wizard form starts here -->
												<fieldset>
												<div class="row">
		                                            <section class="col col-6">
		                                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
		                                                <label class="text-left txt-color-blue font-md">
		                                                    <spring:message code="lbl.silais" />
		
		                                                </label>
		
		                                                <div class="input-group">
																		<span class="input-group-addon"> <i
		                                                                        class="fa fa-location-arrow fa-fw"></i>
																		</span>
		                                                    <select
		                                                            id="codSilaisAtencion" name="codSilaisAtencion"
		                                                            class="select2">
		                                                        <option value="">Seleccione...</option>
		                                                        <c:forEach items="${entidades}" var="entidad">
		                                                            <option value="${entidad.codigo}">${entidad.nombre}</option>
		                                                        </c:forEach>
		                                                    </select>
		                                                </div>
		                                            </section>
		
		                                         <section class="col col-6">
		                                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
		                                                <label class="text-left txt-color-blue font-md">
		                                                    <spring:message code="lbl.health.unit" />
		                                                </label>
		
		                                                <div class="input-group">
																		<span class="input-group-addon"> <i
		                                                                        class="fa fa-location-arrow fa-fw"></i>
																		</span>
		                                                    <select id="codUnidadAtencion" name="codUnidadAtencion"
		                                                                 
		                                                                 class="select2">
		                                                        <option value="">Seleccione...</option>
		
		                                                    </select>
		                                                </div>
		                                            </section>
		
		                                        </div>
												</fieldset>
											</div>
											<div class="step-pane" id="step2">
												<h3><spring:message code="sindfeb.step2.long" /></h3>		
											</div>
											<div class="step-pane" id="step3">
												<h3><spring:message code="sindfeb.step3.long" /></h3>
											</div>
											<div class="step-pane" id="step4">
												<h3><spring:message code="sindfeb.step4.long" /></h3>
											</div>
											<div class="step-pane" id="step5">
												<h3><spring:message code="sindfeb.step5.long" /></h3>
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
	<!-- JQUERY VALIDATE -->
	<spring:url value="/resources/js/plugin/jquery-validate/jquery.validate.min.js" var="jqueryValidate" />
	<script src="${jqueryValidate}"></script>
	<spring:url value="/resources/js/plugin/jquery-validate/messages_{language}.js" var="jQValidationLoc">
	<spring:param name="language" value="${pageContext.request.locale.language}" /></spring:url>				
	<script src="${jQValidationLoc}"/></script>
	<!-- jQuery Selecte2 Input -->
	<spring:url value="/resources/js/plugin/select2/select2.min.js" var="selectPlugin"/>
	<script src="${selectPlugin}"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<spring:url value="/resources/scripts/sindfeb/enterForm.js" var="enterFormSindFeb" />
	<script src="${enterFormSindFeb}"></script>
	<!-- END PAGE LEVEL SCRIPTS -->
	<script type="text/javascript">
		$(document).ready(function() {
			pageSetUp();
	    	$("li.notificacion").addClass("open");
	    	$("li.sindfeb").addClass("active");
	    	if("top"!=localStorage.getItem("sm-setmenu")){
	    		$("li.sindfeb").parents("ul").slideDown(200);
	    	}
	    	// fuelux wizard
	    	var wizard = $('.wizard').wizard();
	    	wizard.on('finished', function (e, data) {
	    		//$("#fuelux-wizard").submit();
	    		//console.log("submitted!");
		    	$.smallBox({
			    title: "Congratulations! Your form was submitted",
			    content: "<i class='fa fa-clock-o'></i> <i>1 seconds ago...</i>",
			    color: "#5F895F",
			    iconSmall: "fa fa-check bounce animated",
			    timeout: 4000
			    });
	    	});
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>