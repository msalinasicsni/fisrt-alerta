<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<!-- BEGIN HEAD -->
<head>
    <jsp:include page="../fragments/headTag.jsp" />
    <spring:url value="/resources/js/plugin/chartjs/chartjs.css" var="chartjsCss" />
    <link href="${chartjsCss}" rel="stylesheet" type="text/css"/>
    <style>
        /* columns right and center aligned datatables */
        .aw-right {
            padding-left: 0;
            padding-right: 10px;
            text-align: right;
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
        <li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a> <i class="fa fa-angle-right"></i>
            <spring:message code="analysis" /> <i class="fa fa-angle-right"></i>
            <a href="<spring:url value="/boletin/init/" htmlEscape="true "/>"><spring:message code="bol" /></a></li>
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
            <i class="fa-fw fa fa-bar-chart-o"></i>
            <spring:message code="bol" />

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
    <article class="col-xs-12 col-sm-12 col-md-4 col-lg-6">
        <!-- Widget ID (each widget will need unique ID)-->
        <div class="jarviswidget" id="wid-id-0">
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
                <span class="widget-icon"> <i class="fa fa-wrench"></i> </span>
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
                <div class="widget-body">
                    <input id="nicRepublic" type="hidden" value="<spring:message code="lbl.nicaragua.republic"/>"/>
                    <input id="dep" type="hidden" value="<spring:message code="lbl.dep"/>"/>
                    <input id="munic" type="hidden" value="<spring:message code="muni"/>"/>
                    <input hidden="hidden" id="sem" value="<spring:message code="week"/>" />
                    <input id="to" type="hidden" value="<spring:message code="lbl.to1"/>"/>
                    <input id="lblAnios" type="hidden" value="<spring:message code="lbl.years"/>"/>
                    <input id="silaisT" type="hidden" value="<spring:message code="lbl.silais"/>"/>

                    <form id="parameters_form" class ="smart-form">
                        <fieldset>
                            <!-- START ROW -->
                            <div class="row">
                                <section class="col col-sm-12 col-md-12 col-lg-12">
                                    <div class="input-group">
                                        <span class="input-group-addon"> <i class="fa fa-stethoscope"></i></span>
                                        <select data-placeholder="<spring:message code="act.select" /> <spring:message code="pato" />" multiple name="codPato" id="codPato" class="select2">
                                            <option value=""></option>
                                            <c:forEach items="${patologias}" var="patologia">
                                                <option  value="${patologia.codigo}">${patologia.codigo} - ${patologia.nombre}</option>
                                            </c:forEach>
                                            <c:forEach items="${grupos}" var="grupo">
                                                <option value="GRP-${grupo.idGrupo}"> GRP - ${grupo.nombre}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </section>
                            </div>
                            <!-- END ROW -->

                            <!-- START ROW -->
                            <div class="row">
                                <section class="col col-sm-12 col-md-12 col-lg-12">
                                    <div class="input-group">
                                        <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                                        <select  name="codArea" id="codArea" data-placeholder="<spring:message code="act.select" /> <spring:message code="level" />" class="select2">
                                            <option value=""></option>
                                            <c:forEach items="${areas}" var="area">
                                                <option value="${area.codigo}">${area.valor}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </section>
                            </div>
                            <!-- END ROW -->

                            <!-- START ROW -->
                            <div class="row">
                                <section class="col col-sm-12 col-md-12 col-lg-12" id="silais" hidden="hidden">
                                    <div class="input-group">
                                        <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                                        <select data-placeholder="<spring:message code="act.select" /> <spring:message code="silais" />" name="codSilaisAtencion" id="codSilaisAtencion" class="select2">
                                            <option value=""></option>
                                            <c:forEach items="${entidades}" var="entidad">
                                                <option value="${entidad.entidadAdtvaId}">${entidad.nombre}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </section>
                            </div>
                            <!-- END ROW -->
                            <!-- START ROW -->
                            <div class="row">
                                <section class="col col-sm-12 col-md-12 col-lg-12" id="departamento" hidden="hidden">
                                    <div class="input-group">
                                        <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                                        <select data-placeholder="<spring:message code="msg.select.depa" />" name="codDepartamento" id="codDepartamento" class="select2">
                                            <option value=""></option>
                                            <c:forEach items="${departamentos}" var="departamento">
                                                <option value="${departamento.divisionpoliticaId}">${departamento.nombre}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </section>
                            </div>
                            <!-- END ROW -->

                            <!-- START ROW -->
                            <div class="row">
                               <%-- <section class="col col-sm-12 col-md-6 col-lg-6">

                                    <div class="input-group">
                                        <span class="input-group-addon"></span>
                                        <select name="semI" id="semI" class="select2" data-placeholder=" <spring:message code="week1" />" >
                                            <option value=""></option>
                                            <c:forEach items="${semanas}" var="semana">
                                                <option value="${semana.valor}">${semana.valor}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </section>--%>
                                <section class="col col-sm-12 col-md-6 col-lg-6">

                                    <div class="input-group">
                                        <span class="input-group-addon"></span>
                                        <select name="semF" id="semF" class="select2" data-placeholder=" <spring:message code="week2" />" >
                                            <option value=""></option>
                                            <c:forEach items="${semanas}" var="semana">
                                                <option value="${semana.valor}">${semana.valor}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </section>
                            </div>
                            <!-- END ROW -->

                            <!-- START ROW -->
                            <div class="row">
                                <section class="col col-sm-12 col-md-6 col-lg-5">

                                    <div class="input-group">
                                        <span class="input-group-addon"></span>
                                        <select  name="anio" id="anio" class="select2" data-placeholder=" <spring:message code="year" />">
                                            <option value=""></option>
                                            <c:forEach items="${anios}" var="anio">
                                                <option value="${anio.valor}">${anio.valor}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </section>

                            </div>
                            <!-- END ROW -->
                            <footer>
                                <button type="submit" id="view-report" class="btn btn-info"><i class="fa fa-refresh"></i> <spring:message code="update" /></button>
                            </footer>
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
        <!-- NEW WIDGET START -->
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <!-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-id-summ">
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
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2><spring:message code="lbl.country.summary"/> </h2>
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
                        <table id="summaryTable" class="table table-striped table-bordered table-hover" width="100%">
                            <thead>

                            <tr role="row">
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th id="firstY" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th id="secY" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>

                            </tr>

                            <tr>

                                <th><spring:message code="lbl.pathologies"/></th>
                                <th><spring:message code="lbl.rates.x"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.unlike.cases"/></th>
                                <th><spring:message code="lbl.relative.percentage.rate"/></th>


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
        <!-- NEW WIDGET START -->
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <!-- Widget ID (each widget will need unique ID)-->
            <div hidden="hidden" class="jarviswidget" id="dTable1">
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
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2><spring:message code="pato"/> </h2>
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
                        <table id="table1" class="table table-striped table-bordered table-hover" width="100%">
                            <thead>
                            <tr>
                                <th style="text-align: center" id="pat1" rowspan="1" colspan="10"></th>
                            </tr>

                            <tr role="row">
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th id="firstYear1" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th id="secYear1" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>

                            </tr>

                            <tr>

                                <th id="entidad1" ></th>
                                <th><spring:message code="lbl.last.week"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.unlike.cases"/></th>
                                <th><spring:message code="lbl.relative.percentage.rate"/></th>


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

        <!-- NEW WIDGET START -->
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <!-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" hidden="hidden" id="dTable2">
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
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                     <h2><spring:message code="pato"/> </h2>
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
                        <table id="table2" class="table table-striped table-bordered table-hover" width="100%">
                            <thead>
                            <tr>
                                <th style="text-align: center" id="pat2" rowspan="1" colspan="10"></th>
                            </tr>

                            <tr role="row">
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th id="firstYear2" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th id="secYear2" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>

                            </tr>

                            <tr>

                                <th id="entidad2" ></th>
                                <th><spring:message code="lbl.last.week"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.unlike.cases"/></th>
                                <th><spring:message code="lbl.relative.percentage.rate"/></th>

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

        <!-- NEW WIDGET START -->
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <!-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" hidden="hidden" id="dTable3">
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
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2><spring:message code="pato"/> </h2>
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
                        <table id="table3" class="table table-striped table-bordered table-hover" width="100%">
                            <thead>
                            <tr>
                                <th style="text-align: center" id="pat3" rowspan="1" colspan="10"></th>
                            </tr>

                            <tr role="row">
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th id="firstYear3" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th id="secYear3" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>

                            </tr>

                            <tr>

                                <th id="entidad3" ></th>
                                <th><spring:message code="lbl.last.week"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.unlike.cases"/></th>
                                <th><spring:message code="lbl.relative.percentage.rate"/></th>


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

        <!-- NEW WIDGET START -->
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <!-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" hidden="hidden" id="dTable4">
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
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2><spring:message code="pato"/> </h2>
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
                        <table id="table4" class="table table-striped table-bordered table-hover" width="100%">
                            <thead>
                            <tr>
                                <th style="text-align: center" id="pat4" rowspan="1" colspan="10"></th>
                            </tr>

                            <tr role="row">
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th id="firstYear4" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th id="secYear4" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>

                            </tr>

                            <tr>

                                <th id="entidad4" ></th>
                                <th><spring:message code="lbl.last.week"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.unlike.cases"/></th>
                                <th><spring:message code="lbl.relative.percentage.rate"/></th>


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

        <!-- NEW WIDGET START -->
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <!-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="dTable5" hidden="hidden">
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
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2><spring:message code="pato"/> </h2>
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
                        <table id="table5" class="table table-striped table-bordered table-hover" width="100%">
                            <thead>
                            <tr>
                                <th style="text-align: center" id="pat5" rowspan="1" colspan="10"></th>
                            </tr>

                            <tr role="row">
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th id="firstYear5" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th id="secYear5" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>

                            </tr>

                            <tr>

                                <th id="entidad5" ></th>
                                <th><spring:message code="lbl.last.week"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.unlike.cases"/></th>
                                <th><spring:message code="lbl.relative.percentage.rate"/></th>


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

        <!-- NEW WIDGET START -->
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <!-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" hidden="hidden" id="dTable6">
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
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2><spring:message code="pato"/> </h2>
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
                        <table id="table6" class="table table-striped table-bordered table-hover" width="100%">
                            <thead>
                            <tr>
                                <th style="text-align: center" id="pat6" rowspan="1" colspan="10"></th>
                            </tr>

                            <tr role="row">
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th id="firstYear6" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th id="secYear6" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>

                            </tr>

                            <tr>

                                <th id="entidad6" ></th>
                                <th><spring:message code="lbl.last.week"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.unlike.cases"/></th>
                                <th><spring:message code="lbl.relative.percentage.rate"/></th>


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

        <!-- NEW WIDGET START -->
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <!-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="dTable7" hidden="hidden">
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
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2><spring:message code="pato"/> </h2>
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
                        <table id="table7" class="table table-striped table-bordered table-hover" width="100%">
                            <thead>
                            <tr>
                                <th style="text-align: center" id="pat7" rowspan="1" colspan="10"></th>
                            </tr>

                            <tr role="row">
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th id="firstYear7" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th id="secYear7" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>

                            </tr>

                            <tr>

                                <th id="entidad7" ></th>
                                <th><spring:message code="lbl.last.week"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.unlike.cases"/></th>
                                <th><spring:message code="lbl.relative.percentage.rate"/></th>


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

        <!-- NEW WIDGET START -->
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <!-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" hidden="hidden" id="dTable8">
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
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2><spring:message code="pato"/> </h2>
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
                        <table id="table8" class="table table-striped table-bordered table-hover" width="100%">
                            <thead>
                            <tr>
                                <th style="text-align: center" id="pat8" rowspan="1" colspan="10"></th>
                            </tr>

                            <tr role="row">
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th id="firstYear8" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th id="secYear8" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>

                            </tr>

                            <tr>

                                <th id="entidad8" ></th>
                                <th><spring:message code="lbl.last.week"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.unlike.cases"/></th>
                                <th><spring:message code="lbl.relative.percentage.rate"/></th>


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

        <!-- NEW WIDGET START -->
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <!-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" hidden="hidden" id="dTable9">
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
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2><spring:message code="pato"/> </h2>
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
                        <table id="table9" class="table table-striped table-bordered table-hover" width="100%">
                            <thead>
                            <tr>
                                <th style="text-align: center" id="pat9" rowspan="1" colspan="10"></th>
                            </tr>

                            <tr role="row">
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th id="firstYear9" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th id="secYear9" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>

                            </tr>

                            <tr>

                                <th id="entidad9" ></th>
                                <th><spring:message code="lbl.last.week"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.unlike.cases"/></th>
                                <th><spring:message code="lbl.relative.percentage.rate"/></th>


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

        <!-- NEW WIDGET START -->
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <!-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" hidden="hidden" id="dTable10">
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
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2><spring:message code="pato"/> </h2>
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
                        <table id="table10" class="table table-striped table-bordered table-hover" width="100%">
                            <thead>
                            <tr>
                                <th style="text-align: center" id="pat10" rowspan="1" colspan="10"></th>
                            </tr>

                            <tr role="row">
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th id="firstYear10" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th id="secYear10" colspan="3" style="text-align: center" rowspan="1"></th>
                                <th rowspan="1" colspan="1"></th>
                                <th rowspan="1" colspan="1"></th>

                            </tr>

                            <tr>

                                <th id="entidad10" ></th>
                                <th><spring:message code="lbl.last.week"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.cases"/></th>
                                <th><spring:message code="lbl.acum"/></th>
                                <th><spring:message code="lbl.rates"/></th>
                                <th><spring:message code="lbl.unlike.cases"/></th>
                                <th><spring:message code="lbl.relative.percentage.rate"/></th>


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
</div>
<!-- end row -->
<!-- row -->

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
<spring:url value="/resources/js/plugin/datatables/swf/copy_csv_xls_pdf.swf" var="dataTablesTTSWF" />
<!-- JQUERY VALIDATE -->
<spring:url value="/resources/js/plugin/jquery-validate/jquery.validate.min.js" var="jqueryValidate" />
<script src="${jqueryValidate}"></script>
<spring:url value="/resources/js/plugin/jquery-validate/messages_{language}.js" var="jQValidationLoc">
<spring:param name="language" value="${pageContext.request.locale.language}" /></spring:url>
<script src="${jQValidationLoc}"></script>
<!-- jQuery Select2 Input -->
<spring:url value="/resources/js/plugin/select2/select2.min.js" var="selectPlugin"/>
<script src="${selectPlugin}"></script>
<!-- jQuery Select2 Locale -->
<spring:url value="/resources/js/plugin/select2/select2_locale_{language}.js" var="selectPluginLocale">
    <spring:param name="language" value="${pageContext.request.locale.language}" /></spring:url>
<script src="${selectPluginLocale}"></script>
<!-- jQuery Chart JS-->
<spring:url value="/resources/js/plugin/chartjs/chart.min.js" var="chartJs"/>
<script src="${chartJs}"></script>
<spring:url value="/resources/js/plugin/chartjs/legend.js" var="legendChartJs"/>
<script src="${legendChartJs}"></script>
<!-- JQUERY BLOCK UI -->
<spring:url value="/resources/js/plugin/jquery-blockui/jquery.blockUI.js" var="jqueryBlockUi" />
<script src="${jqueryBlockUi}"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<spring:url value="/resources/scripts/analisis/boletin.js" var="boletinJS" />
<script src="${boletinJS}"></script>
<spring:url value="/resources/scripts/utilidades/seleccionRegionSIVE.js" var="seleccionRegionSIVE" />
<script src="${seleccionRegionSIVE}"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<spring:url value="/boletin/dataBulletin" var="sActionUrl"/>
<c:set var="blockMess"><spring:message code="blockUI.message" /></c:set>
<spring:url var="municipiosURL" value="/api/v1/municipiosbysilais"/>
<spring:url var="unidadesUrl"   value="/api/v1/unidadesPorSilaisyMuni"  />
<script type="text/javascript">
    $(document).ready(function () {
        pageSetUp();
        var parametros = {sActionUrl: "${sActionUrl}",
            blockMess: "${blockMess}",
            municipiosUrl: "${municipiosURL}",
            unidadesUrl: "${unidadesUrl}",
            dataTablesTTSWF: "${dataTablesTTSWF}"};
        Boletin.init(parametros);
        SeleccionRegionSIVE.init(parametros);
        $("li.analisis").addClass("open");
        $("li.boletin").addClass("active");
        if ("top" != localStorage.getItem("sm-setmenu")) {
            $("li.boletin").parents("ul").slideDown(200);
        }
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>