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
<c:url var="unidadesURL" value="/api/v1/unidadesPrimariasSilais"/>
<c:url var="consultaEncuestasURL" value="/encuesta/busquedaEncuesta"/>
<c:url var="editarEncuestasURL" value="/encuesta/edit"/>
<!-- #HEADER -->
<jsp:include page="../fragments/bodyHeader.jsp" />
<!-- #NAVIGATION -->
<jsp:include page="../fragments/bodyNavigation.jsp" />
<!-- MAIN PANEL -->
<div id="main" data-role="main">
<!-- RIBBON -->
<div id="ribbon">
			<span class="ribbon-button-alignment">
				<span id="refresh" class="btn btn-ribbon" data-action="resetWidgets(fff)" data-placement="bottom" data-original-title="<i class='text-warning fa fa-warning'></i> <spring:message code="msg.reset" />" data-html="true">
					<i class="fa fa-refresh"></i>
				</span>
			</span>
    <!-- breadcrumb -->
    <ol class="breadcrumb">
        <li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a> <i class="fa fa-angle-right"></i> <a href="<spring:url value="/encuesta/search" htmlEscape="true "/>"><spring:message code="lbl.breadcrumb.ento.search" /></a></li>
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
    <div class="col-xs-12 col-sm-7 col-md-7 col-lg-5">
        <h1 class="page-title txt-color-blueDark">
            <!-- PAGE HEADER -->
            <i class="fa-fw fa fa-bug"></i>
            <spring:message code="lbl.ento.search" />
						<span> <i class="fa fa-angle-right"></i>
							<spring:message code="lbl.ento.sub.search" />
						</span>
        </h1>
    </div>
    <!-- end col -->
    <!-- right side of the page with the sparkline graphs -->
    <!-- col -->
    <div class="col-xs-12 col-sm-5 col-md-5 col-lg-7">
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
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <!-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget jarviswidget-color-darken" id="wid-id-0">
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
                    <span class="widget-icon"> <i class="fa fa-search"></i> </span>
                    <h2><spring:message code="lbl.widgettitle.ento.search" /></h2>
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
                        <form class="smart-form" id="frmPrincipal" novalidate="novalidate">
                            <fieldset>
                                <div class="row">
                                    <div id="mensaje" class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                    </div>
                                    <input id="msg_no_results_found" type="hidden" value="<spring:message code="msg.ento.no.results.found"/>"/>
                                    <input id="text_opt_select" type="hidden" value="<spring:message code="lbl.select"/>"/>
                                    <input id="smallBox_content" type="hidden" value="<spring:message code="smallBox.content.4s"/>"/>
                                    <input id="blockUI_message" type="hidden" value="<spring:message code="blockUI.message"/>"/>
                                </div>
                                <div class="row">
                                    <section class="col col-xs-12 col-sm-6 col-md-2 col-lg-2">
                                        <label class="text-left txt-color-blue font-md">
                                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.ento.survey.model" />
                                        </label>
                                        <div class="input-group">
                                                    <span class="input-group-addon">
                                                        <i class="fa fa-location-arrow fa-fw"></i>
                                                    </span>
                                            <select class="select2" id="codModeloEncu" name="codModeloEncu">
                                                <option value=""><spring:message code="lbl.select" />...</option>
                                                <c:forEach items="${modelos}" var="modelos">
                                                    <option value="${modelos.codigo}">${modelos.valor}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </section>
                                    <section class="col col-xs-12 col-sm-6 col-md-3 col-lg-3">
                                        <label class="text-left txt-color-blue font-md">
                                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.silais" /> </label>
                                        <div class="input-group">
                                                    <span class="input-group-addon">
                                                         <i class="fa fa-location-arrow fa-fw"></i>
                                                    </span>
                                            <select id="codSilais" name="codSilais"
                                                    class="select2">
                                                <option value=""><spring:message code="lbl.select" />...</option>
                                                <c:forEach items="${entidades}" var="entidad">
                                                    <option value="${entidad.entidadAdtvaId}">${entidad.nombre}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </section>
                                    <section class="col col-xs-12 col-sm-12 col-md-4 col-lg-4">
                                        <label class="text-left txt-color-blue font-md">
                                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.health.unit" />
                                        </label>
                                        <div class="input-group">
                                                    <span class="input-group-addon">
                                                        <i class="fa fa-location-arrow fa-fw"></i>
                                                    </span>
                                            <select class="select2" id="codUnidadSalud" name="codUnidadSalud">
                                                <option value=""><spring:message code="lbl.select" />...</option>
                                            </select>
                                        </div>
                                    </section>
                                    <section class="col col-xs-6 col-sm-6 col-md-3 col-lg-3">
                                        <section class="col col-xs-12 col-sm-6 col-md-6 col-lg-6">
                                            <label class="text-left txt-color-blue font-md">
                                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.month" />
                                            </label>
                                            <div class="">
                                                <label class="input">
                                                    <i class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                                                    <input type="text" id="mesEpi" name="mesEpi" placeholder="<spring:message code="lbl.month"/>" class="form-control">
                                                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.month.survey"/></b>
                                                </label>
                                            </div>
                                        </section>
                                        <section class="col col-xs-12 col-sm-6 col-md-6 col-lg-6">
                                            <label class="text-left txt-color-blue font-md">
                                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.year" />
                                            </label>
                                            <div class="">
                                                <label class="input">
                                                    <i class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                                                    <input type="text" id="anioEpi" name="anioEpi" placeholder="<spring:message code="lbl.year"/>" class="input-sm">
                                                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.ento.year.survey"/></b>
                                                </label>
                                            </div>
                                        </section>
                                    </section>
                                </div>
                            </fieldset>
                            <fieldset>
                                <div class="row">
                                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                        <!--<a href="#" id="buscarEncuesta" data-toggle="modal" class="btn btn-primary btn-lg pull-right header-btn hidden-mobile">
                                            <i class="fa fa-circle-arrow-up fa-lg"></i>
                                            Consultar
                                        </a>-->
                                        <button id="buscarEncuesta" type="submit"
                                                class="btn btn-primary btn-lg pull-right header-btn hidden-mobile">
                                            <spring:message code="act.search" />
                                        </button>
                                    </div>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                    <!-- end widget content -->
                </div>
                <!-- end widget div -->
            </div>
            <!-- end widget -->
            <div class="jarviswidget jarviswidget-color-darken" id="wid-id-2" data-widget-editbutton="false" data-widget-deletebutton="false">
                <header>
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2><spring:message code="lbl.widgettitle.ento.search.res" /></h2>
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
                        <table id="dtBusqueda" class="table table-striped table-bordered table-hover" data-width="100%">
                            <thead>
                            <tr><th><p class="text-center font-sm "><spring:message code="lbl.silais" /></p></th>
                                <th><p class="text-center font-sm "><spring:message code="lbl.health.unit" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p></th>
                                <th><p class="text-center font-sm "><spring:message code="lbl.month" /></p></th>
                                <th><p class="text-center font-sm "><spring:message code="lbl.year" /></p></th>
                                <th><p class="text-center font-sm "><spring:message code="lbl.department" /></p></th>
                                <th><p class="text-center font-sm "><spring:message code="muni" /></p></th>
                                <th><p class="text-center font-sm "><spring:message code="lbl.district" /></p></th>
                                <th><p class="text-center font-sm "><spring:message code="lbl.area" /></p></th>
                                <th><p class="text-center font-sm "><spring:message code="lbl.ento.ordinal" /></p></th>
                                <th><p class="text-center font-sm "><spring:message code="lbl.provenance" /></p></th>
                                <th><p class="text-center font-sm "><spring:message code="lbl.ento.start.date" /></p></th>
                                <th><p class="text-center font-sm "><spring:message code="lbl.ento.end.date" /></p></th>
                                <th><p class="text-center font-sm "><spring:message code="lbl.ento.model" /></p></th>
                                <th><p class="text-center font-sm "><spring:message code="act.edit" /></p></th>
                            </tr></thead>
                        </table>
                    </div>
                </div>
                <!-- end widget div -->
            </div>
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
</div>
<!-- END MAIN PANEL -->
<!-- BEGIN FOOTER -->
<jsp:include page="../fragments/footer.jsp" />
<!-- END FOOTER -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<jsp:include page="../fragments/corePlugins.jsp" />
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

<!-- JQUERY VALIDATE -->
<spring:url value="/resources/js/plugin/jquery-validate/jquery.validate.min.js" var="validate" />
<script src="${validate}"></script>
<spring:url value="/resources/js/plugin/jquery-validate/messages_{language}.js" var="jQValidationLoc">
    <spring:param name="language" value="${pageContext.request.locale.language}" /></spring:url>
<script src="${jQValidationLoc}"></script>
<!-- Selecte2Input -->
<spring:url value="/resources/js/plugin/select2/select2.min.js" var="selectPlugin"/>
<script src="${selectPlugin}"></script>
<!-- JQUERY BLOCK UI -->
<spring:url value="/resources/js/plugin/jquery-blockui/jquery.blockUI.js" var="jqueryBlockUi" />
<script src="${jqueryBlockUi}"></script>

<!-- JQUERY INPUT MASK -->
<spring:url value="/resources/js/plugin/jquery-inputmask/jquery.inputmask.bundle.min.js" var="jqueryInputMask" />
<script src="${jqueryInputMask}"></script>
<!-- END PAGE LEVEL PLUGINS -->

<!-- BEGIN PAGE LEVEL SCRIPTS -->
<spring:url value="/resources/scripts/encuestas/survey-search.js" var="surveySearch" />
<script src="${surveySearch}"></script>
<spring:url value="/resources/scripts/utilidades/handleInputMask.js" var="handleInputMask" />
<script src="${handleInputMask}"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>
    $(function () {
        $("li.entosearch").addClass("active");
    });
</script>
<script type="text/javascript">
    $(document).ready(function(){
        pageSetUp();
        var parametros = {sSurveyUrl: "${consultaEncuestasURL}", sSurveyEditUrl : "${editarEncuestasURL}", sUnidadesUrl: "${unidadesURL}"};
        SearchSurvey.init(parametros);
        handleInputMasks();
        $("li.entomologia").addClass("open");
        $("li.entosearch").addClass("active");
        if("top"!=localStorage.getItem("sm-setmenu")){
            $("li.entosearch").parents("ul").slideDown(200);
        }
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>