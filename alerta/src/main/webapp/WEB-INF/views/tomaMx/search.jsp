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
				<li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a> <i class="fa fa-angle-right"></i> <a href="<spring:url value="/tomaMx/create" htmlEscape="true "/>"><spring:message code="menu.taking.sample" /></a></li>
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
						<i class="fa-fw fa fa-eyedropper"></i>
							<spring:message code="menu.taking.sample" />
						<span> <i class="fa fa-angle-right"></i>  
							<spring:message code="lbl.search.notices" />
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
			<!-- widget grid -->
			<section id="widget-grid" class="">
				<!-- row -->
				<div class="row">
					<!-- NEW WIDGET START -->
					<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<!-- Widget ID (each widget will need unique ID)-->
						<div class="jarviswidget jarviswidget-color-darken" id="wid-id-0">
							<header>
								<span class="widget-icon"> <i class="fa fa-search"></i> </span>
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
								<div class="widget-body no-padding">
                                    <form id="search-notices" class="smart-form" autocomplete="off">
                                        <fieldset>
                                            <div class="row">
                                                <section class="col col-sm-12 col-md-12 col-lg-12">
                                                    <label class="input"> <i class="icon-append fa fa-check"></i>
                                                        <input type="text" id="filtro" name="filtro" placeholder="<spring:message code="person.search.parameters"/>">
                                                        <b class="tooltip tooltip-bottom-right"><spring:message code="person.search.parameters"/></b> </label>
                                                </section>
                                            </div>
                                            <div class="row">
                                                <section class="col col-sm-6 col-md-4 col-lg-3">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <spring:message code="lbl.noti.start.date" />
                                                    </label>
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar"></i>
                                                        <input type="text" name="fechaInicio" id="fechaInicio"
                                                               placeholder="<spring:message code="lbl.date.format"/>"
                                                               class="form-control from_date" data-date-end-date="+0d"/>
                                                    </label>
                                                </section>
                                                <section class="col col-sm-6 col-md-4 col-lg-3">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <spring:message code="lbl.noti.end.date" />
                                                    </label>
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar"></i>
                                                        <input type="text" name="fechaFin" id="fechaFin"
                                                               placeholder="<spring:message code="lbl.date.format"/>"
                                                               class="form-control to_date" data-date-end-date="+0d"/>
                                                    </label>
                                                </section>
                                            </div>
                                            <c:choose>
                                                <c:when test="${not empty esEstudio}">
                                                    <input type="hidden" id="esEstudio" value="true">
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="hidden" id="esEstudio" value="false">
                                                </c:otherwise>
                                            </c:choose>
                                        </fieldset>
										<footer>
											<button type="submit" id="search-person" class="btn btn-info"><i class="fa fa-search"></i> <spring:message code="act.search" /></button>
										</footer>
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
					<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<!-- Widget ID (each widget will need unique ID)-->
						<div class="jarviswidget jarviswidget-color-darken" id="wid-id-1">
							<header>
								<span class="widget-icon"> <i class="fa fa-reorder"></i> </span>
								<h2><spring:message code="lbl.results" /> </h2>				
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
								<div class="widget-body no-padding">
									<table id="notices_result" class="table table-striped table-bordered table-hover" data-width="100%">
										<thead>			                
											<tr>
												<th data-class="expand"><i class="fa fa-fw fa-user text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.name1"/></th>
												<th data-hide="phone"><i class="fa fa-fw fa-user text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.name2"/></th>
												<th data-hide="phone"><i class="fa fa-fw fa-user text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.lastname1"/></th>
												<th data-hide="phone"><i class="fa fa-fw fa-user text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.lastname2"/></th>
												<th data-hide="phone"><i class="fa fa-fw fa-calendar txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.fecnac"/></th>
												<th data-hide="phone"><i class="fa fa-fw fa-map-marker text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.mun.res"/></th>
                                                <th data-hide="phone"><i class="fa fa-fw fa-file-text-o text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message code="lbl.notification.type"/></th>
                                                <th data-hide="phone"><i class="fa fa-fw fa-calendar text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message code="lbl.notification.date"/></th>
                                                <th></th>
											</tr>
										</thead>
									</table>
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
        <input id="msg_no_results_found" type="hidden" value="<spring:message code="msg.ento.no.results.found"/>"/>
        <input type="hidden" id="inYes"  value="<spring:message code="lbl.yes"/>"/>
        <input type="hidden" id="inNo"  value="<spring:message code="lbl.no"/>"/>
        <input type="hidden" id="titleC"  value="<spring:message code="msg.sending.confirm.title"/>"/>
        <input type="hidden" id="contentC"  value="<spring:message code="msg.confirmation.content"/>"/>
        <input type="hidden" id="titleCancel"  value="<spring:message code="msg.sampling.cancel"/>"/>
        <input id="smallBox_content" type="hidden" value="<spring:message code="smallBox.content.4s"/>"/>
        <input type="hidden" id="iragNotice"  value="<spring:message code="menu.irageti"/>"/>
        <input type="hidden" id="febrilNotice"  value="<spring:message code="menu.sindfeb"/>"/>
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
    <!-- bootstrap datepicker -->
    <spring:url value="/resources/js/plugin/bootstrap-datepicker/bootstrap-datepicker.js" var="datepickerPlugin" />
    <script src="${datepickerPlugin}"></script>
    <spring:url value="/resources/js/plugin/bootstrap-datepicker/locales/bootstrap-datepicker.{languagedt}.js" var="datePickerLoc">
        <spring:param name="languagedt" value="${pageContext.request.locale.language}" /></spring:url>
    <script src="${datePickerLoc}"></script>
    <!-- JQUERY VALIDATE -->
	<spring:url value="/resources/js/plugin/jquery-validate/jquery.validate.min.js" var="jqueryValidate" />
	<script src="${jqueryValidate}"></script>
	<spring:url value="/resources/js/plugin/jquery-validate/messages_{language}.js" var="jQValidationLoc">
	<spring:param name="language" value="${pageContext.request.locale.language}" /></spring:url>				
	<script src="${jQValidationLoc}"></script>
	<!-- JQUERY BLOCK UI -->
	<spring:url value="/resources/js/plugin/jquery-blockui/jquery.blockUI.js" var="jqueryBlockUi" />
	<script src="${jqueryBlockUi}"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<spring:url value="/resources/scripts/muestras/search-notices.js" var="noticesSearch" />
	<script src="${noticesSearch}"></script>
    <spring:url value="/resources/scripts/utilidades/handleDatePickers.js" var="handleDatePickers" />
    <script src="${handleDatePickers}"></script>
    <c:set var="blockMess"><spring:message code="blockUI.message" /></c:set>
	<!-- END PAGE LEVEL SCRIPTS -->
	<spring:url value="/tomaMx/notices" var="noticesUrl"/>
    <spring:url value="/tomaMx/create" var="actionUrl"/>
    <spring:url value="/tomaMx/createStudy" var="createStudyUrl"/>
    <spring:url value="/tomaMx/tomaMxByIdNoti" var="tomaMxUrl"/>
	<script type="text/javascript">
		$(document).ready(function() {
			pageSetUp();
			var parametros = {noticesUrl: "${noticesUrl}",
                              blockMess: "${blockMess}",
                              actionUrl: "${actionUrl}",
                              tomaMxUrl: "${tomaMxUrl}",
                              createStudyUrl : "${createStudyUrl}"
            };
			SearchNotices.init(parametros);
            handleDatePickers("${pageContext.request.locale.language}");
	    	$("li.samples").addClass("open");
            if ($("#esEstudio").val()=='true'){
                $("li.tomaMxEstu").addClass("active");
                if ("top" != localStorage.getItem("sm-setmenu")) {
                    $("li.tomaMxEstu").parents("ul").slideDown(200);
                }
            }else {
                $("li.tomaMx").addClass("active");
                if ("top" != localStorage.getItem("sm-setmenu")) {
                    $("li.tomaMx").parents("ul").slideDown(200);
                }
            }
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>