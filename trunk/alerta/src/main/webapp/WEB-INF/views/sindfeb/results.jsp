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
									<table id="fichas_result" class="table table-striped table-bordered table-hover" width="100%">
										<thead>			                
											<tr>
												<th data-class="expand"><i class="fa fa-fw fa-key text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message code="sindfeb.numFicha"/></th>
												<th data-hide="phone"><i class="fa fa-fw fa-calendar txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="sindfeb.date"/></th>
												<th data-hide="phone"><i class="fa fa-fw fa-calendar txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="lbl.ento.elimi"/></th>
												<th data-hide="phone, tablet"><i class="fa fa-fw fa-folder-o txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="sindfeb.exp"/></th>
												<th data-hide="phone"><i class="fa fa-fw fa-stethoscope txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="sindfeb.unidad"/></th>
												<th data-hide="phone"><i class="fa fa-fw fa-user txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.name1"/></th>
												<th data-hide="phone"><i class="fa fa-fw fa-user txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.lastname1"/></th>
												<th data-hide="phone,tablet"><i class="fa fa-fw fa-user txt-color-blue hidden-md hidden-sm hidden-xs"></i> <spring:message code="person.lastname2"/></th>
												<th></th>
												<th></th>
											</tr>
										</thead>
										<tbody>
										<c:forEach items="${fichas}" var="ficha">
											<tr>
												<td><c:out value="${ficha.numFicha}" /></td>
												<td><c:out value="${ficha.fechaFicha}" /></td>
												<td><c:out value="${ficha.idNotificacion.pasivo}" /></td>
												<td><c:out value="${ficha.codExpediente}" /></td>
												<td><c:out value="${ficha.idNotificacion.codUnidadAtencion.nombre}" /></td>
												<td><c:out value="${ficha.idNotificacion.persona.primerNombre}" /></td>
												<td><c:out value="${ficha.idNotificacion.persona.primerApellido}" /></td>
												<td><c:out value="${ficha.idNotificacion.persona.segundoApellido}" /></td>
												<spring:url value="/febriles/edit/{idNotificacion}" var="editUrl">
													<spring:param name="idNotificacion" value="${ficha.idNotificacion.idNotificacion}" />
												</spring:url>
												<spring:url value="/febriles/delete/{idNotificacion}" var="deleteUrl">
													<spring:param name="idNotificacion" value="${ficha.idNotificacion.idNotificacion}" />
												</spring:url>
												<spring:url value="/febriles/new/{idPersona}" var="newUrl">
													<spring:param name="idPersona" value="${ficha.idNotificacion.persona.personaId}" />
												</spring:url>
												<td><c:if test="${ficha.idNotificacion.pasivo==false}"><a href="${fn:escapeXml(editUrl)}" class="btn btn-default btn-xs"><i class="fa fa-edit"></i></a></c:if></td>
												<td><c:if test="${ficha.idNotificacion.pasivo==false}"><a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default btn-xs"><i class="fa fa-times"></i></a></c:if></td>
											</tr>
										</c:forEach>
										</tbody>
									</table>
								</div>
								<!-- end widget content -->
							</div>
							<!-- end widget div -->
							<div style="border: none" class="row">
								<a href="${fn:escapeXml(newUrl)}"
									class="btn btn-default btn-large btn-primary pull-right"><i
									class="fa fa-plus"></i> <spring:message
										code="lbl.add.notification" /> </a>
							</div>
							<a data-toggle="modal" href="#" class="btn btn-success btn-lg pull-right header-btn hidden-mobile" id="openModal"><i class="fa fa-circle-arrow-up fa-lg"></i><spring:message code="act.ento.add.locality" /></a>
						</div>
						<!-- end widget -->
					</article>
					<!-- WIDGET END -->
				</div>
				<!-- end row -->
			</section>
			<!-- Modal -->
<div class="modal fade" id="myModal" aria-hidden="true" data-backdrop="static"> <!--tabindex="-1" role="dialog" -->
<div class="modal-dialog">
<div class="modal-content">
<div class="modal-header">
    <!--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
        &times;
                    </button>-->
    <h4 class="modal-title">
        <spring:message code="lbl.widgettitle.ento.add.det" />
    </h4>
</div>
<div class="modal-body"> <!--  no-padding -->
<form id="frmDetalleEncuesta" class="smart-form" novalidate="novalidate">
<fieldset>
<!-- NOTIFICACI�N -->
<div id="mensaje">
</div>
<!-- NOTIFICACI�N -->
<!-- SECTOR Y LOCALIDAD -->
<div class="row">
    <section class="col col-sm-12 col-md-5 col-lg-5">
        <label class="text-left txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.sector" />
        </label>
        <div class="input-group">
    		        <span class="input-group-addon">
                        <i class="fa fa-location-arrow fa-fw"></i>
			        </span>
            <select  class="select2" id="codigoSector" name="codigoSector" >
                <option value=""><spring:message code="lbl.select" />...</option>
            </select>
        </div>
    </section>
    <section class="col col-sm-12 col-md-7 col-lg-7">
        <label class="text-left txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.locality" />
        </label>
        <div class="input-group">
	    					        <span class="input-group-addon">
                                        <i class="fa fa-location-arrow fa-fw"></i>
		    				        </span>
            <select class="select2" id="codigoLocalidad" name="codigoLocalidad" >
                <option value=""><spring:message code="lbl.select" />...</option>
            </select>
        </div>
    </section>
</div>
<!-- FIN LOCALIDAD -->
<!-- MANZANAS -->
<div class="row">
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.block" /> <spring:message code="lbl.ento.insp" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-desc"></i>
                <input type="text" name="manzanasInspec" id="manzanasInspec" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> <spring:message code="lbl.ento.block"/> <spring:message code="tooltip.ento.inspecf"/></b>
            </label>
        </div>
    </section>
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.block" /> <spring:message code="lbl.ento.posit" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-desc"></i>
                <input type="text" name="manzanasPositivas" id="manzanasPositivas" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> <spring:message code="lbl.ento.block"/> <spring:message code="tooltip.ento.positf"/></b>
            </label>
        </div>
        <!--<div class="">
            <label class="input"> <i class="icon-append fa fa-sort-numeric-desc"></i>
                <input type="text" name="manzanasPositivas" id="manzanasPositivas">
            </label>
        </div> -->
    </section>
</div>
<!--FIN MANZANAS -->
<!-- VIVIENDAS -->
<div class="row">
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class=" txt-color-blue font-md"><!--col col-4-->
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.homes" /> <spring:message code="lbl.ento.insp" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="viviendasInspec" id="viviendasInspec" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> <spring:message code="lbl.ento.homes"/> <spring:message code="tooltip.ento.inspecf"/></b>
            </label>
        </div>
    </section>
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.homes" /> <spring:message code="lbl.ento.posit" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="viviendasPositivas" id="viviendasPositivas" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> <spring:message code="lbl.ento.homes"/> <spring:message code="tooltip.ento.positf"/></b>
            </label>
        </div>
    </section>
</div>
<!--FIN VIVIENDAS -->
<!-- DEPOSITOS Y PUPAS -->
<div class="row">
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.tank" /> <spring:message code="lbl.ento.insp" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="depositosInspec" id="depositosInspec" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> <spring:message code="lbl.ento.tank"/> <spring:message code="tooltip.ento.inspecm"/></b>
            </label>
        </div>
    </section>
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.tank" /> <spring:message code="lbl.ento.posit" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="depositosPositovos" id="depositosPositovos" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> <spring:message code="lbl.ento.tank"/> <spring:message code="tooltip.ento.positm"/></b>
            </label>
        </div>
    </section>
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.modal.pupae.posit" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="pupasPositivas" id="pupasPositivas" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> <spring:message code="lbl.ento.pupae"/> <spring:message code="tooltip.ento.positf"/></b>
            </label>
        </div>
    </section>
</div>
<!-- FIN DEPOSITO Y PUPAS-->
<!-- NO ABATI, NO ELIMINI Y NO NEUTR -->
<div class="row">
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.no" /> </i><spring:message code="lbl.ento.abatizado" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="noAbati" id="noAbati" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> no <spring:message code="lbl.ento.abatizado"/></b>
            </label>
        </div>
    </section>
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class=" txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.no" /> <spring:message code="lbl.ento.eliminado" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="noElimni" id="noElimni" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> no <spring:message code="lbl.ento.eliminado"/></b>
            </label>
        </div>
    </section>
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.no" /> <spring:message code="lbl.ento.neutralizado" />
        </label>
        <div class="">
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                <input type="text" name="noNeutr" id="noNeutr" class="entero">
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.enter.quantity"/> no <spring:message code="lbl.ento.neutralizado"/></b>
            </label>
        </div>
    </section>
</div>
<!-- FIN NO ABATI, NO ELIMINI Y NO NEUTR -->
<!-- FECHAS-->
<div class="row">
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <spring:message code="lbl.ento.date" /> <spring:message code="lbl.ento.date.abatizado" />
        </label>
        <div class="">
            <!--<span class="input-group-addon"> <i class="fa fa-pencil fa-fw"></i></span>-->
            <label class="input"> <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                <input type="text"
                       name="fecAbat" id="fecAbat"
                       placeholder="<spring:message code="lbl.date.format"/>"
                       class="form-control date-picker"
                       data-dateformat="dd/mm/yy"/>
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.date.abat"/></b>
            </label>
            <!--                    <span class="input-group-addon"> <i class="fa fa-calendar fa-fw"></i>
                                </span>-->
        </div>
    </section>
    <!--<section class="col col-sm-12 col-md-6 col-lg-4">
                    <label class="txt-color-blue font-md">
                        <spring:message code="lbl.ento.date" /> <spring:message code="lbl.ento.report" />
                    </label>
                    <div class="input-group">
                        <input type="text"
                               name="fecReport" id="fecReport"
                               placeholder="<spring:message code="lbl.date.format"/>"
                               class="form-control datepicker midatepicker"
                               data-dateformat="dd/mm/yy"/>
                                        <span class="input-group-addon"> <i class="fa fa-calendar fa-fw"></i>
                                        </span>
                    </div>
                </section>-->
    <section class="col col-sm-12 col-md-6 col-lg-4">
        <label class="txt-color-blue font-md">
            <spring:message code="lbl.ento.date" /> <spring:message code="lbl.ento.modal.vent" />
        </label>
        <div class="">
            <!--<span class="input-group-addon"> <i class="fa fa-pencil fa-fw"></i></span>-->
            <label class="input">
                <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                <input type="text"
                       name="fecVent" id="fecVent"
                       placeholder="<spring:message code="lbl.date.format"/>"
                       class="form-control date-picker"
                       data-dateformat="dd/mm/yy"/>
                <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.date.vent"/></b>
                <!--<span class="input-group-addon"> <i class="fa fa-calendar fa-fw"></i>
                </span>-->
            </label>
        </div>
    </section>
</div>
<!-- FIN FECHAS-->
</fieldset>

<footer>
    <!--<button type="button" class="btn btn-primary" id="btnGuardarDetalle">
        Guardar
    </button>-->
    <button type="submit" class="btn btn-primary" id="btnGuardarDetalle">
        <spring:message code="act.save" />
    </button>
    <button type="button" class="btn btn-default" data-dismiss="modal">
        <spring:message code="act.end" />
    </button>

</footer>

</form>
</div>
</div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
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
	    	$("li.sindfeb").addClass("active");
	    	if("top"!=localStorage.getItem("sm-setmenu")){
	    		$("li.sindfeb").parents("ul").slideDown(200);
	    	}
		});
		var responsiveHelper_dt_basic = undefined;
		var breakpointDefinition = {
			tablet : 1024,
			phone : 480
		};
		var table1 = $('#fichas_result').dataTable({
			"sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+
				"t"+
				"<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
			"autoWidth" : true,
			"preDrawCallback" : function() {
				// Initialize the responsive datatables helper once.
				if (!responsiveHelper_dt_basic) {
					responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#fichas_result'), breakpointDefinition);
				}
			},
			"rowCallback" : function(nRow) {
				responsiveHelper_dt_basic.createExpandIcon(nRow);
			},
			"drawCallback" : function(oSettings) {
				responsiveHelper_dt_basic.respond();
			}
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>