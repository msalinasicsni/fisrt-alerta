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
        .override {
            padding-left: 0;
            padding-right: 10px;
            text-align: center;
            width: 5%;
        }
        .edit {
            padding-left: 0;
            padding-right: 10px;
            text-align: center;
            width: 5%;
        }
        .groupHeader{
            width: 40%;
        }
        .patoHeader{
            width: 50%;
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
        <li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a> <i class="fa fa-angle-right"></i> <a href="<spring:url value="/administracion/patogroup/init" htmlEscape="true "/>"><spring:message code="lbl.pathologies.grouped" /></a></li>
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
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <h1 class="page-title txt-color-blueDark" id="lblHeader">
                <!-- PAGE HEADER -->
                <i class="fa-fw fa fa-link"></i>
                <spring:message code="lbl.pathologies.grouped" />
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
                            <input id="succGroup" type="hidden" value="<spring:message code="msg.group.saved"/>"/>
                            <input id="succPatho" type="hidden" value="<spring:message code="msg.patho.group.added"/>"/>
                            <input id="succDeletePatho" type="hidden" value="<spring:message code="msg.patho.group.deleted"/>"/>
                            <input id="succDeleteGroup" type="hidden" value="<spring:message code="msg.group.deleted"/>"/>
                            <input id="msg_conf" type="hidden" value="<spring:message code="msg.sending.confirm.title"/>"/>
                            <input id="msg_yes" type="hidden" value="<spring:message code="lbl.send.confirm.msg.opc.yes"/>"/>
                            <input id="msg_no" type="hidden" value="<spring:message code="lbl.send.confirm.msg.opc.no"/>"/>
                            <input id="msg_overridePatho_confirm_c" type="hidden" value="<spring:message code="msg.override.pathoGroup.confirm.content"/>"/>
                            <input id="msg_override_confirm_c" type="hidden" value="<spring:message code="msg.override.group.confirm.content"/>"/>
                            <input id="msg_override_cancel" type="hidden" value="<spring:message code="msg.override.cancel"/>"/>
                            <input id="text_opt_select" type="hidden" value="<spring:message code="lbl.select"/>"/>

                            <table id="records" class="table table-striped table-bordered table-hover" width="100%">
                                <thead>
                                <tr>
                                    <th data-class="expand" width="15%"><spring:message code="lbl.group.name"/></th>
                                    <th><spring:message code="lbl.pathologies"/></th>
                                    <th><spring:message code="act.edit"/></th>
                                    <th><spring:message code="act.override"/></th>
                                </tr>
                                </thead>
                            </table>

                        </div>


                        <!-- end widget content -->
                    </div>
                    <!-- end widget div -->
                    <!-- end widget div -->
                    <div style="border: none" class="row">
                        <section class="col col-sm-12 col-md-12 col-lg-12">
                            <div style="border: none" id="dNew" class="pull-right">
                                <button type="button" id="btnNew" class="btn btn-primary"><i class="fa fa-plus"></i>
                                    <spring:message code="lbl.add.group"/></button>
                            </div>

                        </section>
                    </div>
                </div>

                <div class="jarviswidget jarviswidget-color-darken" id="div2" hidden="hidden">
                    <header>
                        <span class="widget-icon"> <i class="fa fa-reorder"></i> </span>
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
                            <form id="group-form" class="smart-form" autocomplete="off">
                                <fieldset>
                                    <div class="row">
                                        <section class="col col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                            <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.group.name"/>
                                            </label>
                                            <div class="input">
                                                <i class="icon-prepend fa fa-pencil fa-fw"></i> <i
                                                    class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                                                <input name="nombreGrupo" id="nombreGrupo" class="form-control" type="text"
                                                       placeholder="<spring:message code="lbl.name"/> <spring:message code="lbl.group.name"/> " value="" />
                                            </div>
                                        </section>
                                    </div>
                                </fieldset>
                                <footer>
                                    <button type="button" id="btnSave" class="btn btn-success"><i class="fa fa-save"></i> <spring:message code="act.save" /></button>
                                </footer>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="jarviswidget jarviswidget-color-darken" id="div3" hidden="hidden">
                    <header>
                        <span class="widget-icon"> <i class="fa fa-reorder"></i> </span>
                        <h2><spring:message code="lbl.associated.patho" /> </h2>
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
                            <table id="recordsPato" class="table table-striped table-bordered table-hover" width="100%">
                                <thead>
                                <tr>
                                    <th data-class="expand" width="15%"><spring:message code="lbl.code"/></th>
                                    <th><spring:message code="pato"/></th>
                                    <th><spring:message code="act.override"/></th>
                                </tr>
                                </thead>
                            </table>

                        </div>
                        <!-- end widget content -->
                    </div>
                    <!-- end widget div -->
                    <div style="border: none" class="row">
                        <section class="col col-sm-12 col-md-6 col-lg-6">
                            <div style="border: none" id="dBack" class="pull-left">
                                <button type="button" id="btnBack" class="btn btn-primary"><i class="fa fa-arrow-left"></i>
                                    <spring:message code="lbl.back"/></button>
                            </div>
                        </section>

                        <section class="col col-sm-12 col-md-6 col-lg-6">
                            <div style="border: none" id="dAdd" class="pull-right">
                                <button type="button" id="btnAdd" class="btn btn-primary"><i class="fa fa-link"></i>
                                    <spring:message code="lbl.associate.patho"/></button>
                            </div>

                        </section>
                    </div>
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
                <!-- Modal agrupar patología -->
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
                                        <spring:message code="lbl.associate.patho"/>
                                    </h4>
                                </div>
                            </div>

                            <div class="modal-body">
                                <form id="patho-form" class="smart-form" autocomplete="off">
                                    <input id="idGrupo" hidden="hidden" type="text" name="idGrupo"/>
                                    <section class="col col-sm-12 col-md-6 col-lg-12">
                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                        <label class="text-left txt-color-blue font-md hidden-xs">
                                            <spring:message code="pato" />
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                                            <select data-placeholder="<spring:message code="act.select" /> <spring:message code="pato" />" name="idPatologia" id="idPatologia" class="select2">
                                                <option value=""></option>
                                            </select>
                                        </div>
                                    </section>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-danger" data-dismiss="modal">
                                    <i class="fa fa-times"></i> <spring:message code="act.end" />
                                </button>
                                <button type="button" id="btnSavePatho" class="btn btn-success"><i class="fa fa-save"></i> <spring:message code="act.save" /></button>

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
<spring:url value="/resources/scripts/administracion/patoGroup.js" var="groupJS" />
<script src="${groupJS}"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<c:set var="blockMess"><spring:message code="blockUI.message" /></c:set>
<c:set var="lblHeader"><spring:message code="lbl.pathologies.grouped"/></c:set>
<c:url var="groupsUrl" value="/administracion/patogroup/getGroups"/>
<c:url var="pathosUrl" value="/administracion/patogroup/getPathoGroup"/>
<c:url var="pathosAvailableUrl" value="/administracion/patogroup/getPathoAvailableGroup"/>
<c:url var="saveGroupUrl" value="/administracion/patogroup/addOrUpdateGroup"/>
<c:url var="savePathoGroupUrl" value="/administracion/patogroup/addPatoGroup"/>
<c:url var="deletePathoGroupUrl" value="/administracion/patogroup/deletePatoGroup"/>
<c:url var="deleteGroupUrl" value="/administracion/patogroup/deleteGroup"/>

<script type="text/javascript">
    $(document).ready(function() {
        pageSetUp();
        var parametros = {blockMess: "${blockMess}",
            groupsUrl : "${groupsUrl}",
            pathosUrl:"${pathosUrl}",
            lblHeader: "${lblHeader}",
            pathosAvailableUrl: "${pathosAvailableUrl}",
            saveGroupUrl: "${saveGroupUrl}",
            savePathoGroupUrl: "${savePathoGroupUrl}",
            deleteGroupUrl: "${deleteGroupUrl}",
            deletePathoGroupUrl: "${deletePathoGroupUrl}"
        };
        PatoGroup.init(parametros);
        $("li.mantenimiento").addClass("open");
        $("li.patoGroup").addClass("active");
        if("top"!=localStorage.getItem("sm-setmenu")){
            $("li.patoGroup").parents("ul").slideDown(200);
        }
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>
