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
				<li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a> <i class="fa fa-angle-right"></i> <a href="<spring:url value="/irag/create" htmlEscape="true "/>"><spring:message code="menu.irageti" /></a></li>
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
						<i class="fa-fw fa fa-stethoscope"></i>
							<spring:message code="sindfeb.prevev" />
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
						<div class="jarviswidget jarviswidget-color-darken" id="wid-id-0">
							<header>
								<span class="widget-icon"> <i class="fa fa-reorder"></i> </span>
								<h2><spring:message code="sindfeb.selectev" /> </h2>
							</header>
							<!-- widget div-->
							<div>
								<!-- widget edit box -->
								<div class="jarviswidget-editbox">
									<!-- This area used as dropdown edit box -->
									<input class="form-control" type="text">	
								</div>

                                <input type="hidden" id="inYes"  value="<spring:message code="lbl.yes"/>"/>
                                <input type="hidden" id="inNo"  value="<spring:message code="lbl.no"/>"/>
                                <input type="hidden" id="titleC"  value="<spring:message code="msg.sending.confirm.title"/>"/>
                                <input type="hidden" id="contentC"  value="<spring:message code="msg.confirmation.content"/>"/>
                                <input type="hidden" id="titleCancel"  value="<spring:message code="msg.sampling.cancel"/>"/>
                                <input id="smallBox_content" type="hidden" value="<spring:message code="smallBox.content.4s"/>"/>
                                <input id="msgIncompleta" type="hidden" value="<spring:message code="msg.noti.incompleta"/>"/>
                                <input id="msgNoOverride" type="hidden" value="<spring:message code="msg.noti.in.laboratory"/>"/>
								<!-- end widget edit box -->
								<!-- widget content -->
								<div class="widget-body no-padding">
                                    <input id="idPerson" hidden="hidden" value="${idPerson}" type="text" name="idPerson"/>
                                    <input id="incompleta" hidden="hidden" value="${incompleta}" type="text" name="incompleta"/>

                                    <table id="noti_results" class="table table-striped table-bordered table-hover" width="100%">
                                        <thead>
                                        <tr>
                                            <th data-class="expand"><i class="fa fa-fw fa-key text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message code="lbl.consultation.date"/></th>
                                            <th data-hide="phone"><i class="fa fa-fw fa-times txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="lbl.active"/></th>
                                            <th data-hide="phone"><i class="fa fa-fw fa-times txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="lbl.complete"/></th>
                                            <th data-hide="phone, tablet"><i class="fa fa-fw fa-folder-o txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="lbl.file.number"/></th>
                                            <th data-hide="phone"><i class="fa fa-fw fa-stethoscope txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="sindfeb.unidad"/></th>
                                            <th data-hide="phone"><i class="fa fa-fw fa-user txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.name1"/></th>
                                            <th data-hide="phone"><i class="fa fa-fw fa-user txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.lastname1"/></th>
                                            <th data-hide="phone,tablet"><i class="fa fa-fw fa-user txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.lastname2"/></th>
                                            <th><spring:message code="act.edit"/></th>
                                            <th><spring:message code="act.export.pdf"/></th>
                                            <th><spring:message code="menu.taking.sample"/></th>
                                            <th><spring:message code="act.override"/></th>


                                        </tr>
                                        </thead>
                                    </table>

                                    <spring:url value="/irag/new/{personaId}" var="newUrl">
                                        <spring:param name="personaId" value="${idPerson}"/>
                                    </spring:url>

                                   <%-- <table id="noti_results" class="table table-striped table-bordered table-hover" data-width="100%">
										<thead>			                
											<tr>

												<th data-class="expand"><i class="fa fa-fw fa-calendar txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="lbl.register.date"/></th>
												<th data-hide="phone"><i class="fa fa-fw fa-user txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.name1"/></th>
												<th data-hide="phone"><i class="fa fa-fw fa-user txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.name2"/></th>
												<th data-hide="phone"><i class="fa fa-fw fa-user txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.lastname1"/></th>
                                                <th data-hide="phone"><i class="fa fa-fw fa-user txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.lastname2"/></th>
                                                <th data-hide="phone"><i class="fa fa-fw fa-user txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="lbl.canceled"/></th>

                                                <th> <spring:message code="act.edit" /> </th>
                                                <th><spring:message code="act.override" /> </th>
											</tr>
										</thead>
										<tbody>
										<c:forEach items="${records}" var="record">
                                            <tr>

                                                <td><c:out value="${record.fechaRegistro}"/></td>
                                                <td><c:out value="${record.idNotificacion.persona.primerNombre}"/></td>
                                                <td><c:out value="${record.idNotificacion.persona.segundoNombre}"/></td>
                                                <td><c:out value="${record.idNotificacion.persona.primerApellido}"/></td>
                                                <td><c:out value="${record.idNotificacion.persona.segundoApellido}"/></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${record.idNotificacion.pasivo==true}">
                                                            <spring:message code="lbl.yes"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <spring:message code="lbl.no"/>
                                                        </c:otherwise>
                                                    </c:choose>

                                                </td>
                                                <spring:url value="/irag/edit/{idIrag}" var="editUrl">
                                                    <spring:param name="idIrag" value="${record.idNotificacion.idNotificacion}"/>
                                                </spring:url>
                                                <spring:url value="/irag/override/{idIrag}" var="overrideUrl">
                                                    <spring:param name="idIrag" value="${record.idNotificacion.idNotificacion}"/>
                                                </spring:url>

                                                <td>
                                                    <c:choose>
                                                        <c:when test="${record.idNotificacion.pasivo==true}">
                                                            <button class="btn btn-xs" disabled>
                                                                <i class="fa fa-edit fa-fw"></i>
                                                            </button>

                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="${fn:escapeXml(editUrl)}"
                                                               class="btn btn-default btn-xs"><i class="fa fa-edit"></i></a>

                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${record.idNotificacion.pasivo==true}">
                                                            <button class="btn btn-xs btn-danger override" disabled>
                                                                <i class="fa fa-times fa-fw"></i>
                                                            </button>

                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:choose>
                                                                <c:when test="${not empty iragAutorizadas}">
                                                                    <c:set var="encontrada" value="0"/>
                                                                    <c:forEach items="${iragAutorizadas}" var="ira">
                                                                            <c:if test="${record.idNotificacion.idNotificacion==ira.idNotificacion.idNotificacion}">
                                                                                <c:set var="encontrada" value="1"/>
                                                                                <a data-toggle="modal"
                                                                                   data-id="${fn:escapeXml(overrideUrl)}"
                                                                                   class="btn btn-xs btn-danger override"><i
                                                                                        class="fa fa-times fa-fw"></i></a>
                                                                            </c:if>
                                                                    </c:forEach>
                                                                    <c:if test="${encontrada < 1}">
                                                                        <button class="btn btn-xs btn-danger override"
                                                                                disabled>
                                                                            <i class="fa fa-times fa-fw"></i>
                                                                        </button>
                                                                    </c:if>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <button class="btn btn-xs btn-danger override"
                                                                            disabled>
                                                                        <i class="fa fa-times fa-fw"></i>
                                                                    </button>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                            </tr>
                                        </c:forEach>
										</tbody>
									</table>--%>
								</div>


								<!-- end widget content -->
							</div>

							<!-- end widget div -->

                            <div style="border: none" class="row">
                                <!--<a href="${fn:escapeXml(newUrl)}" class="btn btn-default btn-large btn-primary pull-right"><i class="fa fa-plus"></i> <spring:message code="lbl.add.notification"/>   </a>
                                -->
                                <button type="button" id="agregarNoti" class="btn btn-default btn-large btn-primary pull-right">
                                    <i class="fa fa-plus"></i> <spring:message code="lbl.add.notification" />
                                </button>
                            </div>

						</div>
						<!-- end widget -->

                        <div class="modal fade" id="d_confirmacion"  role="dialog" tabindex="-1" data-aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header alert-warning">
                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                            &times;
                                        </button>
                                        <h4 class="modal-title fa fa-warning"> <spring:message code="msg.sending.confirm.title" /></h4>
                                    </div>

                                    <div class="modal-body">
                                        <form method="{method}">
                                            <input type=hidden id="overrideUrl"/>
                                            <div id="cuerpo">
                                                <label id="questionOverride"><spring:message code="lbl.question.override" /></label>

                                            </div>
                                        </form>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="act.cancel" /></button>
                                        <button id="btnOverride" type="button" class="btn btn-info" ><spring:message code="act.accept" /></button>
                                    </div>

                                </div>

                                <!-- /.modal-content -->
                            </div>
                            <!-- /.modal-dialog -->
                        </div>

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
    <!-- JQUERY BLOCK UI -->
    <spring:url value="/resources/js/plugin/jquery-blockui/jquery.blockUI.js" var="jqueryBlockUi"/>
    <script src="${jqueryBlockUi}"></script>
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
    <spring:url value="/resources/scripts/irag/results.js" var="iragResultsJS" />
    <script src="${iragResultsJS}"></script>
    <spring:url value="/resources/scripts/utilidades/generarReporte.js" var="generarReporte" />
    <script src="${generarReporte}"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<!-- END PAGE LEVEL SCRIPTS -->
    <c:url var="getResults" value="/irag/getResults"/>
    <c:url var="editUrl" value="/irag/edit/"/>
    <c:set var="blockMess"><spring:message code="blockUI.message" /></c:set>
    <c:url var="overrideUrl" value="/irag/override/"/>
    <c:url var="createMxUrl" value="/tomaMx/create/"/>
    <c:url var="pdfUrl" value="/irag/getPDF"/>
    <spring:url value="/tomaMx/tomaMxByIdNoti" var="tomaMxUrl"/>
	<script type="text/javascript">
		$(document).ready(function() {
			pageSetUp();

            var parametros = {getResultsUrl : "${getResults}",
                editUrl : "${editUrl}",
                overrideUrl: "${overrideUrl}",
                createMxUrl: "${createMxUrl}",
                pdfUrl:"${pdfUrl}",
                blockMess: "${blockMess}",
                iragAutorizadas:"${iragAutorizadas}",
                tomaMxUrl: "${tomaMxUrl}",
                newUrl: "${newUrl}"


            };

            IragResults.init(parametros);


	    	$("li.notificacion").addClass("open");
	    	$("li.irageti").addClass("active");
	    	if("top"!=localStorage.getItem("sm-setmenu")){
	    		$("li.irageti").parents("ul").slideDown(200);
	    	}
		});



	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>