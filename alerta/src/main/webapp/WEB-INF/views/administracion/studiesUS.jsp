<%--
  Created by IntelliJ IDEA.
  User: souyen-ics
  Date: 09-17-15
  Time: 11:00 AM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <jsp:include page="../fragments/headTag.jsp" />
    <style>
        textarea {
            resize: none;
        }

        .styleButton {

            float: right;
            height: 31px;
            margin: 10px 0px 0px 5px;
            padding: 0px 22px;
            font: 300 15px/29px "Open Sans", Helvetica, Arial, sans-serif;
            cursor: pointer;
        }

        .alert{
            margin-bottom: 0px;
        }
    </style>
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
        <li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a> <i class="fa fa-angle-right"></i> <a href="<spring:url value="/administracion/studiesUS/init" htmlEscape="true "/>"><spring:message code="lbl.studiesbyUS" /></a></li>
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
        <div class="col-xs-12 col-sm-8 col-md-8 col-lg-5">
            <h1 class="page-title txt-color-blueDark">
                <!-- PAGE HEADER -->
                <i class="fa-fw fa fa-link"></i>
                <spring:message code="lbl.studiesbyUS" />

            </h1>
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


                <div class="jarviswidget jarviswidget-color-darken" id="div1">
                    <header>
                        <span class="widget-icon"> <i class="fa fa-reorder"></i> </span>
                        <h2><spring:message code="lbl.records" /> </h2>
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


                            <input id="disappear" type="hidden" value="<spring:message code="lbl.messagebox.disappear"/>"/>
                            <input id="succ" type="hidden" value="<spring:message code="msg.associate.us.added"/>"/>
                            <input id="msg_conf" type="hidden" value="<spring:message code="msg.sending.confirm.title"/>"/>
                            <input id="msg_yes" type="hidden" value="<spring:message code="lbl.send.confirm.msg.opc.yes"/>"/>
                            <input id="msg_no" type="hidden" value="<spring:message code="lbl.send.confirm.msg.opc.no"/>"/>
                            <input id="msg_overrideUs_confirm_c" type="hidden" value="<spring:message code="msg.overrideUs.confirm.content"/>"/>
                            <input id="msg_succOverrideUs" type="hidden" value="<spring:message code="msg.successfully.overrideUs"/>"/>
                            <input id="msg_overrideUs_cancel" type="hidden" value="<spring:message code="msg.override.us.cancel"/>"/>

                            <table id="records" class="table table-striped table-bordered table-hover" width="100%">
                                <thead>
                                <tr>
                                    <th data-class="expand" width="15%"><spring:message code="lbl.study"/></th>
                                    <th><spring:message code="lbl.area"/></th>
                                    <th><spring:message code="lbl.associated.us"/></th>

                                </tr>
                                </thead>
                            </table>

                        </div>


                        <!-- end widget content -->
                    </div>
                    <!-- end widget div -->
                </div>
                <!-- end widget -->

                <div hidden="hidden" class="jarviswidget jarviswidget-color-darken" id="div2">
                    <header>
                        <span class="widget-icon"> <i class="fa fa-reorder"></i> </span>
                        <h2><spring:message code="lbl.associated.us" /> </h2>
                        <h2 id="estudio" ></h2>
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

                            <table id="table-us" class="table table-striped table-bordered table-hover" width="100%">
                                <thead>
                                <tr>
                                    <th data-class="expand" width="35%"><spring:message code="lbl.silais"/></th>
                                    <th><spring:message code="muni"/></th>
                                    <th><spring:message code="sindfeb.unidad"/></th>
                                    <th><spring:message code="lbl.override"/></th>
                                </tr>
                                </thead>
                            </table>
                        </div>


                        <!-- end widget content -->
                    </div>

                    <div style="border: none" class="row">
                        <section class="col col-sm-12 col-md-6 col-lg-6">
                            <div style="border: none" id="dBack" class="pull-left">
                                <button type="button" id="btnBack" class="btn btn-primary"><i class="fa fa-arrow-left"></i>
                                    <spring:message code="lbl.back"/></button>
                            </div>
                        </section>

                        <section class="col col-sm-12 col-md-6 col-lg-6">
                            <div style="border: none" id="dAdd" class="pull-right">
                                <button type="button" id="btnAddUs" class="btn btn-primary"><i class="fa fa-link"></i>
                                    <spring:message code="lbl.associate.us"/></button>
                            </div>

                        </section>
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
                <!-- Modal Aliquot -->
                <div class="modal fade" id="myModal" aria-hidden="true" data-backdrop="static">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <div class="alert alert-info">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                        &times;
                                    </button>
                                    <h4 class="modal-title">
                                        <i class="fa-fw fa fa-list-ul"></i>
                                        <spring:message code="lbl.associate.us"/>
                                    </h4>
                                </div>
                            </div>

                            <div class="modal-body">
                                <form id="form" class="smart-form" autocomplete="off">
                                    <div class="row">
                                        <input id="idEst" hidden="hidden" type="text" name="idEst"/>
                                        <input id="opt_select" type="hidden" value="<spring:message code="lbl.select"/>"/>

                                        <section class="col col-xs-12 col-sm-8 col-md-6 col-lg-5">
                                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.silais"/>

                                            </label>

                                            <div class="input-group">
                                                <spring:message code="msg.select.silais" var="selectSilais"/>
                                                <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                                                <select data-placeholder="${selectSilais}" name="codSilaisAtencion" id="codSilaisAtencion" class="select2">
                                                    <option value=""></option>
                                                    <c:forEach items="${entidades}" var="entidad">
                                                    <option value="${entidad.codigo}">${entidad.nombre}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </section>

                                        <section class="col col-sm-12 col-md-6 col-lg-7">
                                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                            <label class="text-left txt-color-blue font-md hidden-xs">
                                                <spring:message code="sindfeb.muni" />
                                            </label>
                                            <div class="input-group">
                                                <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                                                <select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.muni" />" name="codMunicipio" id="codMunicipio" class="select2">
                                                    <option value=""></option>
                                                </select>
                                            </div>
                                        </section>
                                    </div>

                                    <div class="row">
                                        <section class="col col-xs-12 col-sm-12 col-md-8 col-lg-12">
                                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                            <label class="text-left txt-color-blue font-md hidden-xs">
                                                <spring:message code="lbl.health.unit" />
                                            </label>

                                            <div class="input-group">
                                                <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                                                <select data-placeholder="<spring:message code="act.select" /> <spring:message code="lbl.health.unit" />" name="codUnidadAtencion" id="codUnidadAtencion" class="select2">
                                                    <option value=""></option>
                                                </select>
                                            </div>
                                        </section>
                                    </div>

                                </form>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-danger" data-dismiss="modal">
                                    <i class="fa fa-times"></i> <spring:message code="act.end" />
                                </button>
                                <button type="submit" id="btnAddTest" class="btn btn-success"><i class="fa fa-save"></i> <spring:message code="act.save" /></button>

                            </div>
                        </div>
                        <!-- /.modal-content -->
                    </div>
                    <!-- /.modal-dialog -->
                </div>
                <!-- /.modal -->

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
<spring:url value="/resources/js/plugin/datatables/swf/copy_csv_xls_pdf.swf" var="tabletools" />
<!-- jQuery Selecte2 Input -->
<spring:url value="/resources/js/plugin/select2/select2.min.js" var="selectPlugin"/>
<script src="${selectPlugin}"></script>
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
<!-- JQUERY INPUT MASK -->
<spring:url value="/resources/js/plugin/jquery-inputmask/jquery.inputmask.bundle.min.js" var="jqueryInputMask" />
<script src="${jqueryInputMask}"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<spring:url value="/resources/scripts/administracion/studiesUS.js" var="studiesUsJS" />
<script src="${studiesUsJS}"></script>
<spring:url value="/resources/scripts/utilidades/seleccionUnidad.js" var="selecUnidad"/>
<script src="${selecUnidad}"></script>
<spring:url value="/resources/scripts/utilidades/handleDatePickers.js" var="handleDatePickers" />
<script src="${handleDatePickers}"></script>
<spring:url value="/resources/scripts/utilidades/handleInputMask.js" var="handleInputMask" />
<script src="${handleInputMask}"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<c:set var="blockMess"><spring:message code="blockUI.message" /></c:set>
<c:url var="getCatalogue" value="/administracion/studiesUS/getStudies"/>
<c:url var="getUS" value="/administracion/studiesUS/getAssociatedUS"/>
<c:url var="saveEstUsUrl" value="/administracion/studiesUS/addUpdateUs"/>
<spring:url var="municipiosURL" value="/api/v1/municipiosbysilais2"/>
<spring:url var="unidadesUrl"   value="/api/v1/unidadesPrimHosp2"  />





<script type="text/javascript">
    $(document).ready(function() {
        pageSetUp();
        var parametros = {blockMess: "${blockMess}",
            catalogueUrl : "${getCatalogue}",
            usUrl:"${getUS}",
            municipiosUrl:"${municipiosURL}",
            unidadesUrl: "${unidadesUrl}",
            saveEstUsUrl:"${saveEstUsUrl}"


        };
        StudiesUS.init(parametros);
        SeleccionUnidad.init(parametros);

        handleDatePickers("${pageContext.request.locale.language}");
        handleInputMasks();
        $("li.administracion").addClass("open");
        $("li.studiesUS").addClass("active");
        if("top"!=localStorage.getItem("sm-setmenu")){
            $("li.studiesUS").parents("ul").slideDown(200);
        }
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>
