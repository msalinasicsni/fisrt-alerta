<%--
  Created by IntelliJ IDEA.
  User: JMPS
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<!-- BEGIN HEAD -->
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
<c:url var="consultaFichasVihURL" value="busquedaFichas"/>
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
            <li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a> <i class="fa fa-angle-right"></i> <a href="<spring:url value="#" htmlEscape="true"/>"><spring:message code="lbl.breadcrumb.vih.create" /></a></li>
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
                <h2 class="page-title txt-color-blueDark">
                    <!-- PAGE HEADER -->
                    <i class="fa-fw fa fa-home"></i>
                    <spring:message code="lbl.vih.begin" />
						<span> <i class="fa fa-angle-right"></i>
							<spring:message code="lbl.register" />
						</span>
                </h2>
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
                            <span class="widget-icon"> <i class="fa fa-comments"></i> </span>
                            <h2><spring:message code="lbl.widgettitle.vih.create" /></h2>

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
                            <div class="widget-body fuelux">
                            <form:form modelAttribute="formVI" id="fuelux-wizard" class ="smart-form">
                                <div class="wizard">
                                    <ul class="steps">
                                        <li data-target="#step1" class="active">
                                            <span class="badge badge-info">1</span><spring:message code="lbl.general.data" /><span class="chevron"></span>
                                        </li>
                                        <li data-target="#step2">
                                            <span class="badge">2</span><spring:message code="lbl.patient" /><span class="chevron"></span>
                                        </li>
                                        <li data-target="#step3">
                                            <span class="badge">3</span><spring:message code="lbl.history" /><span class="chevron"></span>
                                        </li>
                                        <li data-target="#step4">
                                            <span class="badge">4</span><spring:message code="lbl.epidemioloy" /><span class="chevron"></span>
                                        </li>
                                        <li data-target="#step5">
                                            <span class="badge">5</span><spring:message code="lbl.laboratory" /><span class="chevron"></span>
                                        </li>
                                        <li data-target="#step6">
                                            <span class="badge">5</span><spring:message code="lbl.clinicalstatus.data" /><span class="chevron"></span>
                                        </li>
                                    </ul>
                                    <div class="actions">
                                        <button type="button" class="btn btn-sm btn-primary btn-prev">
                                            <i class="fa fa-arrow-left"></i><spring:message code="lbl.previous" />
                                        </button>
                                        <button type="button" class="btn btn-sm btn-success btn-next" data-last="<spring:message code="lbl.finalize" />">
                                            <spring:message code="lbl.next" /><i class="fa fa-arrow-right"></i>
                                        </button>
                                    </div>
                                </div>
                                <div class="step-content">

                                        <div class="step-pane active" id="step1">
                                            <h3><spring:message code="lbl.general.data" /></h3>
                                            <!-- wizard form starts here -->
                                            <fieldset>
                                                <div class="row">
                                                    <section class="col col-6">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.silais" />

                                                        </label>

                                                        <div class="input-group">
																<span class="input-group-addon"> <i
                                                                        class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                            <form:select
                                                                    path="entidadesAdtva" id="codSilaisAtencion" name="codSilaisAtencion"
                                                                    class="select2">
                                                                <option value="">Seleccione...</option>
                                                                <c:forEach items="${entidades}" var="entidad">
                                                                    <option value="${entidad.codigo}">${entidad.nombre}</option>
                                                                </c:forEach>
                                                            </form:select>
                                                        </div>
                                                    </section>

                                                    <section class="col col-6">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.health.unit" />
                                                        </label>

                                                        <div class="input-group">
																<span class="input-group-addon"> <i
                                                                        class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                            <form:select id="codUnidadAtencion" name="codUnidadAtencion"
                                                                         path="unidadSalud"
                                                                         class="select2">
                                                                <option value="">Seleccione...</option>

                                                            </form:select>
                                                        </div>
                                                    </section>

                                                </div>

                                                <div class="row">
                                                    <section class="col col-3">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.vih.date" />
                                                        </label>

                                                        <div class="input-group">
                                                            <form:input type="text" id="fecha" name="fecha" path="fecha"

                                                                        class="form-control datepicker"
                                                                        data-dateformat="dd/mm/yy"/>
                    <span class="input-group-addon"> <i
                            class="fa fa-calendar fa-fw"></i></span>

                                                        </div>
                                                    </section>

                                                    <section class="col col-6">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.vih.user" />
                                                        </label>
                                                        <label class="input"> <i
                                                                class="icon-prepend fa fa-pencil fa-f"></i> <form:input
                                                                type="text" path="codigo_usuario_vih" name="codUsuario" id="codUsuario" onKeyUp="this.value=this.value.toUpperCase();"
                                                                />
                                                        </label>
                                                    </section>

                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.vih.insurancemembershipcategory" />
                                                        </label>

                                                        <form:select id="codCategoria"
                                                                     path="id_categoria_afiliacion" name="codCategoria"
                                                                     class="select2">
                                                            <option value="">Seleccione...</option>
                                                            <c:forEach items="${tipoas}" var="tipoas">
                                                                <option value="${tipoas.codigo}">${tipoas.valor}</option>
                                                            </c:forEach>
                                                        </form:select>

                                                    </section>

                                                </div>
                                            </fieldset>
                                        </div>
                                        <div class="step-pane" id="step2">
                                            <h3><spring:message code="lbl.patient.data" /></h3>

                                            <fieldset>

                                                <div class="row">
                                                    <section class="col col-6">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.patient.nationality" />

                                                        </label>

                                                        <div class="input-group">
																<span class="input-group-addon"> <i
                                                                        class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                            <form:select
                                                                    path="" id="codPaisNacionalidad" name="codPaisNacionalidad"
                                                                    class="select2">
                                                                <option value="">Seleccione...</option>
                                                                <c:forEach items="${paises}" var="paises">
                                                                    <option value="${paises.codigoAlfatres}">${paises.nombre}</option>
                                                                </c:forEach>
                                                            </form:select>
                                                        </div>
                                                    </section>

                                                    <section class="col col-6">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.patient.country" />
                                                        </label>

                                                        <div class="input-group">
																<span class="input-group-addon"> <i
                                                                        class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                            <form:select id="codPaisRes" name="codPaisRes"
                                                                         path=""
                                                                         class="select2">
                                                                <option value="">Seleccione...</option>
                                                                <c:forEach items="${paises}" var="paises">
                                                                    <option value="${paises.codigoAlfatres}">${paises.nombre}</option>
                                                                </c:forEach>
                                                            </form:select>
                                                        </div>
                                                    </section>

                                                </div>
                                                <div class="row">
                                                    <section class="col col-6">

                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.patient.city" />

                                                        </label>

                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i
                                                                    class="fa fa-location-arrow fa-fw"></i>
																</span>
														    <form:select
                                                                    path="" id="codDepartamento" name="codDepartamento"
                                                                    class="select2">
                                                                <option value="">Seleccione...</option>
                                                                <c:forEach items="${departamentos}" var="dep">
                                                                    <option value="${dep.codigoNacional}">${dep.nombre}</option>
                                                                </c:forEach>
                                                            </form:select>
                                                        </div>
                                                    </section>

                                                    <section class="col col-6">

                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.town" />
                                                        </label>

                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i
                                                                    class="fa fa-location-arrow fa-fw"></i>
																</span>
															<form:select id="codMunicipio" name="codMunicipio"
                                                                         path=""
                                                                         class="select2">
                                                                <option value="">Seleccione...</option>

                                                            </form:select>
                                                        </div>
                                                    </section>
                                                </div>
                                                <div class="row">
                                                    <section class="col col-8">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.address" />
                                                        </label>

                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i
                                                                    class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                            <label class="textarea textarea-expandable">
                                                                <textarea maxlength="200" id="direccion" name="direccion" rows="2" class="custom-scroll"></textarea>
                                                            </label>
                                                        </div>
                                                    </section>
                                                    <section class="col col-4">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.provenance" />
                                                        </label>
                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i
                                                                    class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                            <form:select id="codProcedencia" name="codProcedencia" class="select2" path="">
                                                                <option value="">Seleccione...</option>
                                                                <c:forEach items="${catProcedencia}" var="proc">
                                                                    <form:option value="${proc.codigo}">${proc.valor}</form:option>
                                                                </c:forEach>
                                                            </form:select>
                                                        </div>
                                                    </section>
                                                </div>
                                                <div class="row">
                                                    <section class="col col-3">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.birthdate" />
                                                        </label>

                                                        <div class="input-group">
                                                            <form:input type="text" id="fechaNac" name="fechaNac" path=""

                                                                        class="form-control datepicker"
                                                                        data-dateformat="dd/mm/yy"/>
                                                                <span class="input-group-addon"> <i
                                                                        class="fa fa-calendar fa-fw"></i></span>

                                                        </div>
                                                    </section>
                                                    <section class="col col-3">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.age" />
                                                        </label>

                                                        <label class="input"> <i
                                                                class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i>
                                                            <form:input type="text" path="" name="edad" id="edad" />
                                                        </label>
                                                    </section>
                                                    <section class="col col-3">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.age.type" />
                                                        </label>
                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i
                                                                    class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                            <form:select id="tipoEdad" name="tipoEdad" class="select2" path="">
                                                                <option value="">Seleccione...</option>
                                                                <c:forEach items="${tipoedad}" var="tipoed">
                                                                    <form:option value="${tipoed.codigo}">${tipoed.valor}</form:option>
                                                                </c:forEach>
                                                            </form:select>
                                                        </div>
                                                    </section>
                                                    <section class="col col-3">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="person.sexo" />
                                                        </label>

                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i
                                                                    class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                            <form:select id="sexo" name="sexo" class="select2" path="">
                                                                <option value="">Seleccione...</option>
                                                                <c:forEach items="${sexo}" var="listasexo">
                                                                    <form:option value="${listasexo.codigo}">${listasexo.valor}</form:option>
                                                                </c:forEach>
                                                            </form:select>

                                                        </div>
                                                    </section>
                                                </div>
                                                <div class="row">
                                                <section class="col col-3">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="person.etnia" />
                                                        </label>
                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i
                                                                    class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                            <form:select id="etnia" name="etnia" class="select2" path="">
                                                                <option value="">Seleccione...</option>
                                                                <c:forEach items="${etnia}" var="listaetnia">
                                                                    <form:option value="${listaetnia.codigo}">${listaetnia.valor}</form:option>
                                                                </c:forEach>
                                                            </form:select>

                                                        </div>

                                                    </section>
                                                    <section class="col col-6">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="person.estadocivil" />
                                                        </label>
                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i
                                                                    class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                            <form:select id="estadocivil" name="estadocivil" class="select2" path="">
                                                                <option value="">Seleccione...</option>
                                                                <c:forEach items="${estadocivil}" var="estado">
                                                                    <form:option value="${estado.codigo}">${estado.valor}</form:option>
                                                                </c:forEach>
                                                            </form:select>
                                                        </div>
                                                    </section>
                                                    <section class="col col-3">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="person.escolaridad" />
                                                        </label>

                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i
                                                                    class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                            <form:select id="escolaridad" name="escolaridad" class="select2" path="">
                                                                <option value="">Seleccione...</option>
                                                                <c:forEach items="${escolaridad}" var="listaesc">
                                                                    <form:option value="${listaesc.codigo}">${listaesc.valor}</form:option>
                                                                </c:forEach>
                                                            </form:select>

                                                        </div>
                                                    </section>
                                                </div>
                                                <div class="row">
                                                    <section class="col col-3">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="person.ocupacion" />
                                                        </label>
                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i
                                                                    class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                            <form:select id="ocupacion" name="ocupacion" class="select2" path="">
                                                                <option value="">Seleccione...</option>
                                                                <c:forEach items="${ocupaciones}" var="listaocup">
                                                                    <form:option value="${listaocup.codigo}">${listaocup.nombre}</form:option>
                                                                </c:forEach>
                                                            </form:select>

                                                        </div>
                                                    </section>
                                                    <section class="col col-3">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md"><spring:message code="person.trabaja" /></label>
                                                        <div class="inline-group" style="padding-bottom: 8px">
                                                            <label class="radio"> <input type="radio" value="1" name="trabaja"> <i></i> <spring:message code="lbl.yes" />  </label>
                                                            <label class="radio"> <input type="radio" value="0" name="trabaja"> <i></i> <spring:message code="lbl.no" /> </label>
                                                            <label class="radio"> <input type="radio" value="" name="trabaja"> <i></i> <spring:message code="lbl.noaplica" /> </label>
                                                        </div>
                                                    </section>
                                                    <section class="col col-3">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i>
                                                        <label class="text-left txt-color-blue font-md"><spring:message code="person.estudia" /></label>
                                                        <div class="inline-group" style="padding-bottom: 8px">
                                                            <label class="radio"> <input type="radio" value="1" name="estudia"> <i></i> <spring:message code="lbl.yes" />  </label>
                                                            <label class="radio"> <input type="radio" value="0" name="estudia"> <i></i> <spring:message code="lbl.no" /> </label>
                                                            <label class="radio"> <input type="radio" value="" name="estudia"> <i></i> <spring:message code="lbl.noaplica" /> </label>
                                                        </div>
                                                    </section>
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.vih.canthijosmenores" />
                                                        </label>
                                                        <label class="input"> <i
                                                                class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i>
                                                            <form:input type="text" size="5" path="" name="canthijos" id="canthijos" />
                                                        </label>
                                                    </section>
                                                </div>
                                            </fieldset>
                                        </div> <%--FIN PRIMERA PESTAÑA--%>
                                        <div class="step-pane" id="step3">
                                            <h3><spring:message code="lbl.sexualhistory.data" /></h3>
                                            <fieldset>
                                                <div class="row">
                                                    <section class="col col-4">
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.vih.edad1sexo" />
                                                        </label>
                                                        <label class="input"> <i
                                                                class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i>
                                                            <form:input type="text" path="" name="edadsexo" id="edadsexo" />
                                                        </label>
                                                    </section>
                                                    <section class="col col-4">
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.vih.parejasultanio" />
                                                        </label>
                                                        <label class="input"> <i
                                                                class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i>
                                                            <form:input type="text" path="" name="parejasultanio" id="parejasultanio" />
                                                        </label>
                                                    </section>
                                                    <section class="col col-4">
                                                        <label class="text-left txt-color-blue font-md"><spring:message code="lbl.vih.usocondonultanio" /></label>
                                                        <div class="inline-group" style="padding-bottom: 8px">
                                                            <label class="radio"> <input type="radio" value="1" name="usocondonultanio"> <i></i> <spring:message code="lbl.siempre" />  </label>
                                                            <label class="radio"> <input type="radio" value="0" name="usocondonultanio"> <i></i> <spring:message code="lbl.aveces" /> </label>
                                                            <label class="radio"> <input type="radio" value="" name="usocondonultanio"> <i></i> <spring:message code="lbl.nunca" /> </label>
                                                        </div>
                                                    </section>
                                                </div>
                                                <div class="row">
                                                    <section class="col col-4">
                                                        <label class="text-left txt-color-blue font-md"><spring:message code="lbl.vih.embarazada" /></label>
                                                        <div class="inline-group" style="padding-bottom: 8px">
                                                            <label class="radio"> <input disabled="disabled" type="radio" value="1" name="embarazada"> <i></i> <spring:message code="lbl.yes" />  </label>
                                                            <label class="radio"> <input disabled="disabled" type="radio" value="0" name="embarazada"> <i></i> <spring:message code="lbl.no" /> </label>
                                                        </div>
                                                    </section>
                                                    <section class="col col-4">
                                                        <label class="text-left txt-color-blue font-md"><spring:message code="lbl.vih.relacextranjero" /></label>
                                                        <div class="inline-group" style="padding-bottom: 8px">
                                                            <label class="radio"> <input type="radio" value="1" name="relacextranj"> <i></i> <spring:message code="lbl.yes" />  </label>
                                                            <label class="radio"> <input type="radio" value="0" name="relacextranj"> <i></i> <spring:message code="lbl.no" /> </label>
                                                        </div>
                                                    </section>
                                                    <section class="col col-4">
                                                        <label class="text-left txt-color-blue font-md"><spring:message code="lbl.vih.usocondon" /></label>
                                                        <div class="inline-group" style="padding-bottom: 8px">
                                                            <label class="radio"> <input disabled="disabled" type="radio" value="1" name="usocondoextranj"> <i></i> <spring:message code="lbl.yes" />  </label>
                                                            <label class="radio"> <input disabled="disabled" type="radio" value="0" name="usocondoextranj"> <i></i> <spring:message code="lbl.no" /> </label>
                                                        </div>
                                                    </section>
                                                </div>
                                                <div class="row" hidden="hidden" id="datosemb">
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.fum" />
                                                        </label>
                                                        <div class="input-group">
                                                            <form:input type="text" id="fum" name="fum" path=""
                                                                        class="form-control datepicker"
                                                                        data-dateformat="dd/mm/yy"/>
                                                                <span class="input-group-addon"> <i
                                                                        class="fa fa-calendar fa-fw"></i></span>
                                                        </div>
                                                    </section>
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.weeks.pregnancy" />
                                                        </label>
                                                        <label class="input"> <i
                                                                class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i>
                                                            <form:input type="text" path="" name="semanasemb" id="semanasemb" />
                                                        </label>
                                                    </section>
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.metodoestimacionsem" />
                                                        </label>
                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i
                                                                    class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                            <form:select id="metodosemgest" name="metodosemgest" class="select2" path="">
                                                                <option value="">Seleccione...</option>
                                                                <c:forEach items="${metodosemgest}" var="metodsem">
                                                                    <form:option value="${metodsem.codigo}">${metodsem.valor}</form:option>
                                                                </c:forEach>
                                                            </form:select>
                                                        </div>
                                                    </section>
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.fpp" />
                                                        </label>
                                                        <div class="input-group">
                                                            <form:input type="text" id="fpp" name="fpp" path=""
                                                                        class="form-control datepicker"
                                                                        data-dateformat="dd/mm/yy"/>
                                                                <span class="input-group-addon"> <i
                                                                        class="fa fa-calendar fa-fw"></i></span>
                                                        </div>
                                                    </section>
                                                </div>
                                                <div class="row" hidden="hidden" id="datosemb2">
                                                    <section class="col col-4">
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.periodopruebavihemb" />
                                                        </label>
                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i
                                                                    class="fa fa-location-arrow fa-fw"></i>
																</span>
                                                            <form:select id="periodopruebavihemb" name="periodopruebavihemb" class="select2" path="">
                                                                <option value="">Seleccione...</option>
                                                                <c:forEach items="${periodopruebavihemb}" var="periodopruebavihemb">
                                                                    <form:option value="${periodopruebavihemb.codigo}">${periodopruebavihemb.valor}</form:option>
                                                                </c:forEach>
                                                            </form:select>
                                                        </div>
                                                    </section>
                                                    <section class="col col-4">
                                                        <label class="text-left txt-color-blue font-md"><spring:message code="lbl.controlprenatal" /></label>
                                                        <div class="inline-group" style="padding-bottom: 8px">
                                                            <label class="radio"> <input type="radio" value="1" name="controlprenatal"> <i></i> <spring:message code="lbl.yes" />  </label>
                                                            <label class="radio"> <input type="radio" value="0" name="controlprenatal"> <i></i> <spring:message code="lbl.no" /> </label>
                                                        </div>
                                                    </section>
                                                </div>
                                                <h3><spring:message code="lbl.antecedentesobstetricos" /></h3>
                                                <div class="row">
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.gesta" />
                                                        </label>
                                                        <label class="input"> <i
                                                                class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i>
                                                            <form:input type="text" disabled="true" path="" name="gesta" id="gesta" />
                                                        </label>
                                                    </section>
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.para" />
                                                        </label>
                                                        <label class="input"> <i
                                                                class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i>
                                                            <form:input type="text" disabled="true" path="" name="para" id="para" />
                                                        </label>
                                                    </section>
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.cesarea" />
                                                        </label>
                                                        <label class="input"> <i
                                                                class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i>
                                                            <form:input type="text" disabled="true" path="" name="cesarea" id="cesarea" />
                                                        </label>
                                                    </section>
                                                    <section class="col col-3">
                                                        <label class="text-left txt-color-blue font-md">
                                                            <spring:message code="lbl.aborto" />
                                                        </label>
                                                        <label class="input"> <i
                                                                class="icon-prepend fa fa-sort-numeric-asc fa-fw"></i>
                                                            <form:input type="text" disabled="true" path="" name="aborto" id="aborto" />
                                                        </label>
                                                    </section>
                                                </div>
                                            </fieldset>
                                        </div>
                                        <div class="step-pane" id="step4">
                                            <h3><spring:message code="lbl.epidemiology.data" /></h3>
                                        </div>
                                        <div class="step-pane" id="step5">
                                            <h3><spring:message code="lbl.laboratory.data" /></h3>
                                        </div>
                                        <div class="step-pane" id="step6">
                                            <h3><spring:message code="lbl.clinicalstatus.data" /></h3>
                                        </div>

                                </div>
                            </form:form>
                            </div>
                            <!-- end widget content -->
                        </div>
                        <!-- end widget div -->
                    </div>
                    <!-- end widget -->
                </article>
                <!-- WIDGET END -->
            </div>

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
<!-- Bootstrap Wizard -->
<spring:url value="/resources/js/plugin/bootstrap-wizard/jquery.bootstrap.wizard.min.js" var="jqueryBootstrap" />
<script src="${jqueryBootstrap}"></script>
<!-- JQUERY BOOTSTRAP WIZARD -->
<spring:url value="/resources/js/plugin/bootstrap-wizard/jquery.bootstrap.wizard.min.js" var="jqueryBootstrap" />
<script src="${jqueryBootstrap}"></script>
<!-- JQUERY FUELUX WIZARD -->
<spring:url value="/resources/js/plugin/fuelux/wizard/wizard.min.js" var="jQueryFueWiz" />
<script src="${jQueryFueWiz}"></script>

<!-- JQUERY VALIDATE -->
<spring:url value="/resources/js/plugin/jquery-validate/jquery.validate.min.js" var="validate" />
<script src="${validate}"></script>
<spring:url value="/resources/js/plugin/jquery-validate/messages_{language}.js" var="jQValidationLoc">
<spring:param name="language" value="${pageContext.request.locale.language}" /></spring:url>
<script src="${jQValidationLoc}"/></script>

<%-- jQuery Validate datepicker --%>
<spring:url value="/resources/js/plugin/jquery-validate-datepicker/jquery.ui.datepicker.validation.min.js" var="jQueryValidateDatepicker"/>
<script src="${jQueryValidateDatepicker}" ></script>
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

<!-- END PAGE LEVEL SCRIPTS -->

<!-- BEGIN PAGE LEVEL PLUGINS -->
<spring:url value="/resources/scripts/vih/vih-create.js" var="vihCreate" />
<script src="${vihCreate}"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>
    $(function () {
        $("li.notificacion").addClass("open");
        $("li.vih").addClass("active");
    });
</script>
<spring:url value="/irag/saveIrag" var="sAddIragUrl"/>
<spring:url value="/api/v1/unidades" var="unidadesURL" />
<spring:url value="/api/v1/municipio" var="municipiosURL" />
<script type="text/javascript">

    $(document).ready(function() {
        pageSetUp();

        var parametros = {sAddIragUrl: "${sAddIragUrl}",
                          sUnidadesUrl: "${unidadesURL}",
                          sMunicipiosUrl: "${municipiosURL}",
                          dToday: "${today}"
        };
        console.log( "${today}");
        CreateVIH.init(parametros);
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>