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
								<!-- end widget edit box -->
								<!-- widget content -->
								<div class="widget-body no-padding">
									<table id="noti_results" class="table table-striped table-bordered table-hover" data-width="100%">
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
                                                <spring:url value="/irag/new/{personaId}" var="newUrl">
                                                    <spring:param name="personaId" value="${record.idNotificacion.persona.personaId}"/>
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
                                                                    <c:forEach items="${iragAutorizadas}" var="ira">
                                                                        <c:choose>
                                                                            <c:when test="${record.idNotificacion.idNotificacion==ira.idNotificacion.idNotificacion}">
                                                                                <c:set var="encontrada" value="1"/>
                                                                                <a data-toggle="modal"
                                                                                   data-id="${fn:escapeXml(overrideUrl)}"
                                                                                   class="btn btn-xs btn-danger override"><i
                                                                                        class="fa fa-times fa-fw"></i></a>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <c:set var="encontrada" value="0"/>
                                                                            </c:otherwise>
                                                                        </c:choose>
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
									</table>
								</div>


								<!-- end widget content -->
							</div>

							<!-- end widget div -->

                            <div style="border: none" class="row">
                                <a href="${fn:escapeXml(newUrl)}" class="btn btn-default btn-large btn-primary pull-right"><i class="fa fa-plus"></i> <spring:message code="lbl.add.notification"/>   </a>

                            </div>

						</div>
						<!-- end widget -->

                        <div class="modal fade" id="d_confirmacion"  role="dialog" tabindex="-1" data-aria-hidden="true">
                            <c:set var="questionOverride"><spring:message code="lbl.question.override" /></c:set>
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
                                            <div id="cuerpo"></div>
                                        </form>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="act.cancel" /></button>
                                        <button type="button" class="btn btn-info" onclick="override()"><spring:message code="act.accept" /></button>
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
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<!-- END PAGE LEVEL SCRIPTS -->
	<script type="text/javascript">
		$(document).ready(function() {
			pageSetUp();
	    	$("li.notificacion").addClass("open");
	    	$("li.irageti").addClass("active");
	    	if("top"!=localStorage.getItem("sm-setmenu")){
	    		$("li.irageti").parents("ul").slideDown(200);
	    	}
		});

        var responsiveHelper_dt_basic = undefined;
        var breakpointDefinition = {
            tablet : 1024,
            phone : 480
        };
        var table1 = $('#noti_results').dataTable({
            "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+
                    "t"+
                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
            "autoWidth" : true,
            "preDrawCallback" : function() {
                // Initialize the responsive datatables helper once.
                if (!responsiveHelper_dt_basic) {
                    responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#noti_results'), breakpointDefinition);
                }
            },
            "rowCallback" : function(nRow) {
                responsiveHelper_dt_basic.createExpandIcon(nRow);
            },
            "drawCallback" : function(oSettings) {
                responsiveHelper_dt_basic.respond();
            }
        });

        $(".override").click(function(){
            $('#overrideUrl').val($(this).data('id'));
            $('#cuerpo').html('<h4 class="modal-title">'+ "${questionOverride}"+'</h4>');
            $('#d_confirmacion').modal('show');
        });

        function override() {

            window.location.href = $('#overrideUrl').val();

        }

	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>