<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <jsp:include page="../fragments/headTag.jsp"/>
    <style>
        textarea {
            resize: none;
        }
    </style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="">
<!-- #HEADER -->
<jsp:include page="../fragments/bodyHeader.jsp"/>
<!-- #NAVIGATION -->
<jsp:include page="../fragments/bodyNavigation.jsp"/>
<!-- MAIN PANEL -->
<div id="main" data-role="main">
<!-- RIBBON -->
<div id="ribbon">
			<span class="ribbon-button-alignment"> 
				<span id="refresh" class="btn btn-ribbon" data-action="resetWidgets" data-placement="bottom"
                      data-original-title="<i class='text-warning fa fa-warning'></i> <spring:message code="msg.reset" />"
                      data-html="true">
					<i class="fa fa-refresh"></i>
				</span> 
			</span>
    <!-- breadcrumb -->
    <ol class="breadcrumb">
        <li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home"/></a> <i
                class="fa fa-angle-right"></i>

            <a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.daily.reporting"/></a> <i
                    class="fa fa-angle-right"></i>

            <a
                href="<spring:url value="/notificacionDiaria/initSearch" htmlEscape="true "/>"><spring:message
                code="menu.search.notiD"/></a></li>
    </ol>
    <!-- end breadcrumb -->
    <jsp:include page="../fragments/layoutOptions.jsp"/>
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
            <i class="fa-fw fa fa-file-text"></i>
            <spring:message code="menu.daily.reporting"/>
						<span> <i class="fa fa-angle-right"></i>  
							<spring:message code="menu.search.notiD"/>
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
                <h5><spring:message code="sp.day"/> <span class="txt-color-greenDark"><i
                        class="fa fa-arrow-circle-down"></i>17</span></h5>

                <div class="sparkline txt-color-blue hidden-mobile hidden-md hidden-sm">
                    0,1,3,4,11,12,11,13,10,11,15,14,20,17
                </div>
            </li>
            <li class="sparks-info">
                <h5><spring:message code="sp.week"/> <span class="txt-color-red"><i class="fa fa-arrow-circle-up"></i>&nbsp;57</span>
                </h5>

                <div class="sparkline txt-color-purple hidden-mobile hidden-md hidden-sm">
                    23,32,11,23,33,45,44,54,45,48,57
                </div>
            </li>
            <li class="sparks-info">
                <h5><spring:message code="sp.month"/> <span class="txt-color-red"><i class="fa fa-arrow-circle-up"></i>&nbsp;783</span>
                </h5>

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
    <span class="widget-icon"> <i class="fa fa-edit"></i> </span>

    <%--<h2><spring:message code="lbl.person"/></h2>--%>
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
<input type="hidden" id="disappear" value="<spring:message code="lbl.messagebox.disappear"/>"/>
<input type="hidden" id="msjSuccessful" value="<spring:message code="lbl.messagebox.successful.saved"/>"/>
<input type="hidden" id="msjduplicate" value="<spring:message code="lbl.msg.duplicate.record"/>"/>
<input type="hidden" id="close" value="<spring:message code="lbl.state.close"/>"/>
<input type="hidden" id="open" value="<spring:message code="lbl.state.open"/>"/>
<input id="msg_no_results_found" type="hidden" value="<spring:message code="msg.ento.no.results.found"/>"/>




    <form id="search-form" class="smart-form" autocomplete="off">
<fieldset>
    <h3 style="text-align: center"><spring:message code="lbl.nic.rep"/></h3>

    <h3 style="text-align: center"><spring:message code="lbl.minsa"/></h3>

    <h3 style="text-align: center"><spring:message code="lbl.daily,reporting.title"/></h3>

</fieldset>
<fieldset>
    <div class="row">

        <section class="col col-xs-12 col-sm-12 col-md-6 col-lg-6">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <label class="text-left txt-color-blue font-md">
                <spring:message code="lbl.noti.silais"/>

            </label>

            <div class="input-group">
                <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                <select data-placeholder="<spring:message code="act.select" /> <spring:message code="silais" />"
                        name="codSilaisAtencion" id="codSilaisAtencion" class="select2 sel">
                    <option value=""></option>
                    <c:forEach items="${entidades}" var="entidad">
                        <option value="${entidad.codigo}">${entidad.nombre}</option>
                    </c:forEach>
                </select>
            </div>
        </section>

        <section class="col col-xs-12 col-sm-12 col-md-6 col-lg-6">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
            <label class="text-left txt-color-blue font-md hidden-xs">
                <spring:message code="sindfeb.muni"/>
            </label>

            <div class="input-group">
                <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                <select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.muni" />"
                        name="codMunicipio" id="codMunicipio" class="select2 sel">
                    <option value=""></option>
                </select>
            </div>
        </section>

    </div>

    <div class="row">
        <section class="col col-xs-12 col-sm-12 col-md-6 col-lg-6">
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
            <label class="text-left txt-color-blue font-md hidden-xs">
                <spring:message code="sindfeb.unidad"/>
            </label>

            <div class="input-group">
                <span class="input-group-addon"> <i class="fa fa-list fa-fw"></i></span>

                <spring:message var="selectUS" code="msg.select.hu"/>

                <select name="codUnidadAtencion" id="codUnidadAtencion" data-placeholder="${selectUS}" class="select2 sel">
                    <option value=""></option>

                </select>

            </div>

        </section>

        <section class="col col-xs-6 col-sm-6 col-md-3 col-lg-3">
            <spring:message var="notidate" code="lbl.dnoti.date"/>
            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
            <label class="text-left txt-color-blue font-md">
                ${notidate}
            </label>

            <div class="form-group">
                <div class='input-group'>
                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                    <label class="input">
                        <input name="notidate" id="notidate" type='text' data-date-end-date="+0d"
                               class="form-control date-picker"
                               placeholder="${notidate}"/> </label>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>  </span>
                </div>
            </div>
        </section>
    </div>
</fieldset>

<footer>

    <div >
        <button id="btnSearch" class="btn btn-primary"><i class="fa fa-search"></i> <spring:message
                code="act.search"/></button>
    </div>


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
</div>
<!-- end row -->
<!-- row -->
<div class="row">
    <!-- a blank row to get started -->
    <div class="col-sm-12">
        <!-- Widget ID (each widget will need unique ID)-->
        <div class="jarviswidget jarviswidget-color-darken" id="wid-id-1">
            <header>
                <span class="widget-icon"> <i class="fa fa-reorder"></i> </span>

                <h2><spring:message code="sindfeb.prevev"/></h2>
            </header>
            <!-- widget div-->
            <div>
                <!-- widget edit box -->
                <div class="jarviswidget-editbox">
                    <!-- This area used as dropdown edit box -->
                    <input class="form-control" type="text">

                </div>
                <!-- end widget edit box -->
                <div class="widget-body no-padding">
                    <table id="dtNotiD" class="table table-striped table-bordered table-hover" width="100%">
                        <thead>
                        <tr>
                            <th data-class="expand"><i class="fa fa-fw fa-calendar-o text-muted hidden-md hidden-sm hidden-xs"></i>
                                <spring:message code="lbl.dnoti.date"/></th>
                            <th data-hide="phone"><i class="fa fa-fw fa fa-map-marker text-muted hidden-md hidden-sm hidden-xs"></i>
                                <spring:message code="lbl.silais"/></th>
                            <th data-hide="phone,tablet"><i
                                    class="fa fa-fw fa-map-marker text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message
                                    code="muni"/></th>
                            <th><i class="fa fa-fw fa-map-marker text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message
                                    code="lbl.health.unit"/></th>
                            <th><i class="fa fa-fw fa-unlock text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message
                                    code="lbl.state"/></th>
                            <th><i class="fa fa-fw fa fa-list-alt text-muted hidden-md hidden-sm hidden-xs"></i> <spring:message
                                    code="view"/></th>

                        </tr>
                        </thead>
                    </table>
                </div>
                <!-- end widget content -->
            </div>
            <!-- end widget div -->
        </div>
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
<jsp:include page="../fragments/footer.jsp"/>
<!-- END FOOTER -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<jsp:include page="../fragments/corePlugins.jsp"/>
<!-- BEGIN PAGE LEVEL PLUGINS -->
<!-- Data table -->
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

<!-- jQuery Selecte2 Input -->
<spring:url value="/resources/js/plugin/select2/select2.min.js" var="selectPlugin"/>
<script src="${selectPlugin}"></script>
<!-- bootstrap datepicker -->
<spring:url value="/resources/js/plugin/bootstrap-datepicker/bootstrap-datepicker.js" var="datepickerPlugin"/>
<script src="${datepickerPlugin}"></script>
<spring:url value="/resources/js/plugin/bootstrap-datepicker/locales/bootstrap-datepicker.{languagedt}.js"
            var="datePickerLoc">
    <spring:param name="languagedt" value="${pageContext.request.locale.language}"/></spring:url>
<script src="${datePickerLoc}"></script>
<!-- JQUERY VALIDATE -->
<spring:url value="/resources/js/plugin/jquery-validate/jquery.validate.min.js" var="jqueryValidate"/>
<script src="${jqueryValidate}"></script>
<spring:url value="/resources/js/plugin/jquery-validate/messages_{language}.js" var="jQValidationLoc">
    <spring:param name="language" value="${pageContext.request.locale.language}"/></spring:url>
<script src="${jQValidationLoc}"></script>
<!-- JQUERY BLOCK UI -->
<spring:url value="/resources/js/plugin/jquery-blockui/jquery.blockUI.js" var="jqueryBlockUi"/>
<script src="${jqueryBlockUi}"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<spring:url value="/resources/scripts/utilidades/handleDatePickers.js" var="handleDatePickers"/>
<script src="${handleDatePickers}"></script>
<spring:url value="/resources/scripts/notificacionDiaria/searchForm.js" var="notificaciondjs"/>
<script src="${notificaciondjs}"></script>
<!-- JQUERY INPUT MASK -->
<spring:url value="/resources/js/plugin/jquery-inputmask/jquery.inputmask.bundle.min.js" var="jqueryInputMask"/>
<script src="${jqueryInputMask}"></script>
<spring:url value="/resources/scripts/utilidades/handleInputMask.js" var="handleInputMask"/>
<script src="${handleInputMask}"></script>
<spring:url value="/resources/scripts/utilidades/seleccionUnidadesSIVE.js" var="seleccionRegionSIVE" />
<script src="${seleccionRegionSIVE}"></script>


<!-- END PAGE LEVEL SCRIPTS -->
<spring:url var="municipiosURL" value="/api/v1/municipiosbysilais"/>
<c:set var="blockMess"><spring:message code="blockUI.message"/></c:set>
<spring:url var="municipiosURL" value="/api/v1/municipiosbysilais"/>
<c:url var="unidadesUrl"   value="/notificacionDiaria/unidadesPorSilaisyMuni2"  />
<c:url var="semanaEpidemiologicaURL" value="/api/v1/semanaEpidemiologica"/>
<c:url var="saveURL" value="/notificacionDiaria/save"/>
<c:url var="detailsUrl" value="/notificacionDiaria/getEventNotiD"/>
<c:url var="duplicateUrl" value="/notificacionDiaria/getEventsByParams1"/>
<c:url var="notiDUrl" value="/notificacionDiaria/getNotiD"/>
<c:url var="loadNotiDFormUrl" value="/notificacionDiaria/loadEventsNoti/"/>
<script type="text/javascript">
    $(document).ready(function () {
        pageSetUp();
        var parametros = {municipiosUrl: "${municipiosURL}",
            unidadesUrl: "${unidadesUrl}",
            detailsUrl: "${detailsUrl}",
            loadNotiDFormUrl: "${loadNotiDFormUrl}",
            blockMess: "${blockMess}"
        };
        SearchNotificationD.init(parametros);
        SeleccionUnidadesSIVE.init(parametros);
        handleInputMasks();
        handleDatePickers("${pageContext.request.locale.language}");
        $("li.notificacionD").addClass("open");
        $("li.buscarNotiD").addClass("active");
        if ("top" != localStorage.getItem("sm-setmenu")) {
            $("li.buscarNotiD").parents("ul").slideDown(200);
        }

    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>