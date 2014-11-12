<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<!-- BEGIN HEAD -->
<!-- PAGINA DE BUSQUEDA FICHAS VIH -->

<head>
    <jsp:include page="../fragments/headTag.jsp" />
    <style>
        .select2-hidden-accessible {
            display: none !important;
            visibility: hidden !important;
        }
    </style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="">
<c:url var="unidadesURL" value="/api/v1/unidades"/>
<c:url var="consultaFichasVihURL" value="/vih/busquedaFichas"/>
<c:url var="editarFichasVihURL" value="/vih/editFicha"/>
<c:url var="createVIH" value="/vih/create"/>
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
        <li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a> <i class="fa fa-angle-right"></i> <a href="<spring:url value="#" htmlEscape="true "/>"><spring:message code="lbl.breadcrumb.vih.search" /></a></li>
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
            <spring:message code="lbl.vih.begin" />
						<span> <i class="fa fa-angle-right"></i>  
							<spring:message code="lbl.vih.sub.search" />
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
    <!-- row -->
    <div class="row">
        <!-- NEW WIDGET START -->
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <!-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-id-0">
                <header>
                    <span class="widget-icon"> <i class="fa fa-comments"></i> </span>
                    <h2><spring:message code="lbl.widgettitle.vih.search" /> </h2>
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
                                    <section class="col col-3">
                                        <label class="text-left txt-color-blue font-md">
                                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.silais" /></label>
                                        <div class="input-group">
                                                    <span class="input-group-addon">
                                                         <i class="fa fa-location-arrow fa-fw"></i>
                                                    </span>
                                            <select path="codSilais" id="codSilais" name="codSilais"
                                                    class="select2">
                                                <option value=""><spring:message code="act.select" /></option>
                                                <c:forEach items="${entidades}" var="entidad">
                                                    <option value="${entidad.entidadAdtvaId}">${entidad.nombre}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </section>
                                    <section class="col col-5">
                                        <label class="text-left txt-color-blue font-md">
                                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.health.unit" />
                                        </label>
                                        <div class="input-group">
                                                    <span class="input-group-addon">
                                                        <i class="fa fa-location-arrow fa-fw"></i>
                                                    </span>
                                            <select class="select2" id="codUnidadSalud" name="codUnidadSalud">
                                                <option value=""><spring:message code="act.select" /></option>
                                            </select>
                                        </div>
                                    </section>
                                    <section class="col col-3">
                                        <label class="text-left txt-color-blue font-md">
                                            <spring:message code="lbl.vih.user" />
                                        </label>
                                        <label class="input">
                                            <input type="text" id="codUsuario" name="codUsuario" path="codUsuario" placeholder="<spring:message code="lbl.vih.user" />" class="input-sm">
                                        </label>
                                    </section>
                                </div>
                                <div class="row">
                                    <div class="col-xs-10 col-sm-10 col-md-10 col-lg-10">
                                        <a href="${createVIH}" <a class="btn btn-primary btn-lg pull-right header-btn hidden-mobile" id="btnNuevoRegistro"><i class="fa fa-circle-arrow-up fa-lg"></i><spring:message code="act.add" /></a></a>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
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
<section id="widget-grid1" class="">
    <!-- row -->
    <div class="row">
        <!-- NEW WIDGET START -->
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget jarviswidget-color-darken" id="wid-id-2" data-widget-editbutton="false" data-widget-deletebutton="false">
                <header>
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2>Resultados Busqueda</h2>
                </header>
                <!-- widget div-->
                <div>
                    <!-- widget edit box -->
                    <div class="jarviswidget-editbox">
                        <!-- This area used as dropdown edit box -->
                    </div>
                    <!-- end widget edit box -->
                    <!-- widget content -->
                    <div class="widget-body no-padding">
                        <table id="dtBusqueda" class="table table-striped table-bordered table-hover" data-width="100%">
                            <thead>
                            <tr><th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">SILAIS</p></th>
                                <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Unidad de Salud&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p></th>
                                <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Codigo Usuario</p></th>
                                <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Fecha</p></th>
                                <th style="background-color: #3276b1"><p class="text-center font-sm txt-color-white">Editar</p></th>
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
<script src="${jQValidationLoc}"/></script>
<!-- Selecte2Input -->
<spring:url value="/resources/js/plugin/select2/select2.min.js" var="selectPlugin"/>
<script src="${selectPlugin}"></script>
<!-- END PAGE LEVEL PLUGINS -->

<!-- BEGIN PAGE LEVEL SCRIPTS -->
<spring:url value="/resources/scripts/vih/vih-search.js" var="vihSearch" />
<script src="${vihSearch}"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>
    $(function () {
        $("li.vih").addClass("active");
    });
</script>
<script type="text/javascript">
    $(document).ready(function(){
        pageSetUp();
        var parametros = {sSurveyUrl: "${consultaFichasVihURL}", sSurveyEditUrl : "${editarFichasVihURL}", sUnidadesUrl: "${unidadesURL}"};
        SearchVih.init(parametros);
    });
    function Nuevo(){
        alert("Nuevo");

    }
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>