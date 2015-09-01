<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
				<li><a href="<spring:url value="/" htmlEscape="true "/>"><spring:message code="menu.home" /></a> <i class="fa fa-angle-right"></i> <a href="<spring:url value="/tomaMx/create" htmlEscape="true "/>"><spring:message code="menu.taking.sample" /></a></li>
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
						<i class="fa-fw fa fa-eyedropper"></i>
							<spring:message code="lbl.sampling.register" />
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
						<div class="jarviswidget jarviswidget-color-darken" id="wid-id-0" data-widget-editbutton="false" data-widget-deletebutton="false">
							<header>
								<span class="widget-icon"> <i class="fa fa-eyedropper"></i> </span>
								<h2><spring:message code="lbl.sampling.register" /> </h2>
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

                                <form:form modelAttribute="noti" id="noti" class="smart-form" >
                                <fieldset >
                                    <legend class="text-left txt-color-blue font-md"> <spring:message code="lbl.notification.data"/>
                                        ${noti.codTipoNotificacion.valor}
                                   </legend>
                                    <div class="row">
                                        <section class="col col-md-4">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="sindfeb.silais" />
                                                <input value="${noti.codTipoNotificacion.codigo}" hidden="hidden" type="text" id="tipoNoti" name="tipoNoti"/>
                                            </label>
                                            <div class="input-group">
                                                <span class="input-group-addon"><i class="fa fa-location-arrow"></i></span>
                                                <label class="input">
                                                    <input style="background-color: #f0fff0" disabled class="form-control" value="${noti.codSilaisAtencion.nombre}" type="text"   />
                                                </label>
                                                <span class="input-group-addon"><i class="fa fa-chevron-down fa-fw"></i></span>
                                            </div>
                                        </section>
                                        <section class="col col-md-8">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.health.unit" />
                                                <input value="${noti.codUnidadAtencion.unidadId}" type="hidden" id="unidadId" name="unidadId"/>
                                            </label>
                                            <div class="input-group">
                                                <span class="input-group-addon"><i class="fa fa-location-arrow"></i></span>
                                                <label class="input">
                                                    <input style="background-color: #f0fff0" disabled class="form-control" type="text" value="${noti.codUnidadAtencion.nombre}"   />
                                                </label>
                                                <span class="input-group-addon"><i class="fa fa-chevron-down fa-fw"></i></span>
                                            </div>
                                        </section>
                                    </div>
                                    <div class="row">
                                        <section class="col col-md-3">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="person.name1" />
                                            </label>
                                            <div class="input-group">
                                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                                <label class="input">
                                                    <form:input style="background-color: #f0fff0" disabled="true" class="form-control" type="text" path="persona.primerNombre"   />
                                                </label>
                                                <span class="input-group-addon"><i class="fa fa-sort-alpha-asc"></i></span>
                                            </div>
                                        </section>
                                        <section class="col col-md-3">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="person.name2" />
                                            </label>
                                            <div class="input-group">
                                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                                <label class="input">
                                                    <form:input style="background-color: #f0fff0" disabled="true" class="form-control" type="text" path="persona.segundoNombre"   />
                                                </label>
                                                <span class="input-group-addon"><i class="fa fa-sort-alpha-asc"></i></span>
                                            </div>
                                        </section>
                                        <section class="col col-md-3">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="person.lastname1" />
                                            </label>
                                            <div class="input-group">
                                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                                <label class="input">
                                                    <form:input style="background-color: #f0fff0" disabled="true" class="form-control" type="text" path="persona.primerApellido"   />
                                                </label>
                                                <span class="input-group-addon"><i class="fa fa-sort-alpha-asc"></i></span>
                                            </div>
                                        </section>
                                        <section class="col col-md-3">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="person.lastname2" />
                                            </label>
                                            <div class="input-group">
                                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                                <label class="input">
                                                    <form:input style="background-color: #f0fff0" disabled="true" class="form-control" type="text" path="persona.segundoApellido"   />
                                                </label>
                                                <span class="input-group-addon"><i class="fa fa-sort-alpha-asc"></i></span>
                                            </div>
                                        </section>
                                    </div>
                                    <div class="row">
                                        <section class="col col-md-3">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="person.sexo" />
                                            </label>
                                            <div class="input-group">
                                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                                <label class="input">
                                                    <form:input style="background-color: #f0fff0" disabled="true" class="form-control" type="text" path="persona.sexo.valor"   />
                                                </label>
                                                <span class="input-group-addon"><i class="fa fa-chevron-down fa-fw"></i></span>
                                            </div>
                                        </section>
                                        <section class="col col-md-3">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="person.fecnac" />
                                            </label>
                                            <div class="input-group">
                                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                                <label class="input">
                                                    <input style="background-color: #f0fff0" class="form-control" readonly
                                                           type="text" value="<fmt:formatDate value="${noti.persona.fechaNacimiento}" pattern="dd/MM/yyyy" />"
                                                           placeholder=" <spring:message code="person.fecnac" />">
                                                </label>
                                                <span class="input-group-addon"><i class="fa fa-calendar fa-fw"></i></span>
                                            </div>
                                        </section>
                                        <section class="col col-md-3">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.register.date" />
                                            </label>
                                            <div class="input-group">
                                                <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                                <label class="input">
                                                    <input style="background-color: #f0fff0" class="form-control" readonly
                                                           type="text" value="<fmt:formatDate value="${noti.fechaRegistro}" pattern="dd/MM/yyyy" />">
                                                </label>
                                                <span class="input-group-addon"><i class="fa fa-calendar fa-fw"></i></span>
                                            </div>
                                        </section>
                                    </div>
                                </fieldset>
                                </form:form>
                                <form  id="registroMx" class="smart-form" autocomplete="off">
                                <fieldset>
                                    <legend class="text-left txt-color-blue font-md"> <spring:message
                                            code="lbl.taking.sample.data"/></legend>
                                    <div class="row">
                                        <input value="${noti.idNotificacion}"  type="hidden" id="idNotificacion" name="idNotificacion"/>
                                        <section class="col col-3">
                                            <label class="text-left txt-color-blue font-md">
                                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.unique.code.mx" />
                                            </label>
                                            <div class="">
                                                <label class="input">
                                                    <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                                                    <input name="codigoUnicoMx" id="codigoUnicoMx" class="form-control" type="text"
                                                           placeholder="<spring:message code="lbl.unique.code.mx" />"/>
                                                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.unique.code.mx"/></b>
                                                </label>
                                            </div>
                                        </section>
                                        <section class="col col-3">
                                            <label  class="text-left txt-color-blue font-md">
                                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.sampling.datetime" />
                                            </label>
                                            <div class="">
                                                <label class="input">
                                                    <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-calendar fa-fw"></i>
                                                    <input type="text" name="fechaHTomaMx" id="fechaHTomaMx"
                                                           placeholder="<spring:message code="lbl.sampling.datetime"/>"
                                                           class="form-control datetimepicker" data-date-end-date="+0d" value=""/>
                                                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.sampling.datetime"/></b>
                                                </label>
                                            </div>
                                        </section>
                                        <section class="col col-6">
                                            <label class="text-left txt-color-blue font-md">
                                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.sample.type"/>
                                            </label>
                                            <div class="input-group">
                                                    <span class="input-group-addon">
                                                        <i class="fa fa-list fa-fw"></i>
                                                    </span>
                                                <label for="codTipoMx"></label>
                                                <select  id="codTipoMx" name="codTipoMx" class="select2">
                                                    <option value=""><spring:message code="lbl.select" />...</option>
                                                    <c:forEach items="${catTipoMx}" var="catTipoMx">
                                                        <option value="${catTipoMx.tipoMx.idTipoMx}">${catTipoMx.tipoMx.nombre}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </section>
                                    </div>
                                    <div class="row">
                                        <section class="col col-6">
                                            <label class="text-left txt-color-blue font-md">
                                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.study.sample.type"/>
                                            </label>
                                            <div class="input-group">
                                                <spring:message code="msj.select.study" var="selectStudy" />
                                                <span class="input-group-addon"> <i class="fa fa-file-text-o"></i></span>
                                                <label for="idEstudio"></label>
                                                <select name="idEstudio" id="idEstudio" class="select2">
                                                </select>
                                            </div>
                                        </section>
                                        <div class="col col-3" id="divCategoriaMx">
                                            <label class="text-left txt-color-blue font-md">
                                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.sample.category"/>
                                            </label>
                                            <div class="input-group">
                                                    <span class="input-group-addon">
                                                        <i class="fa fa-list fa-fw"></i>
                                                    </span>
                                                <label for="codCategoriaMx"></label>
                                                <select  id="codCategoriaMx" name="codCategoriaMx" class="select2">
                                                    <option value=""><spring:message code="lbl.select" />...</option>
                                                    <c:forEach items="${categMxList}" var="catTipoMx">
                                                        <option value="${catTipoMx.codigo}">${catTipoMx.valor}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <section class="col col-3">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.number.tubes" />
                                            </label>
                                            <div class="">
                                                <label class="input">
                                                    <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                                                    <input name="canTubos" id="canTubos" class="form-control entero" type="text"
                                                           placeholder="<spring:message code="lbl.number.tubes" />"/>
                                                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.number.tubes"/></b>
                                                </label>
                                            </div>
                                        </section>
                                        <section class="col col-3">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="lbl.volume" />
                                            </label>
                                            <div class="">
                                                <label class="input">
                                                    <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                                                    <input id="volumen" name="volumen" class="decimal"  type="text"
                                                           placeholder="<spring:message code="lbl.volume" />" />
                                                    <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.volume"/></b>
                                                </label>
                                            </div>
                                        </section>
                                        <section class="col col-3">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="cooling.time" />
                                            </label>
                                            <div class="">
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil"></i> <i class="icon-append glyphicon glyphicon-calendar"></i>
                                                        <input id="horaRefrigeracion" name="horaRefrigeracion" type='text'
                                                               class="form-control"
                                                               placeholder="<spring:message code="cooling.time" />"/>
                                                        <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.cooling.time"/></b>

                                                    </label>
                                            </div>
                                        </section>
                                        <section class="col col-sm-12 col-md-6 col-lg-3">
                                            <label class="text-left txt-color-blue font-md">
                                                <spring:message code="sample.separation"/>
                                            </label>
                                            <div class="inline-group">
                                                <label class="radio">
                                                    <input type="radio" name="mxSeparada" value="1">
                                                    <i></i><spring:message code="lbl.yes"/></label>
                                                <label class="radio">
                                                    <input type="radio" name="mxSeparada" value="0">
                                                    <i></i><spring:message code="lbl.no"/></label>
                                            </div>
                                            <div hidden="hidden" id="dErrorCantTubos" class="errorDiv txt-color-red"><spring:message code="lbl.required.field"/></div>
                                        </section>
                                    </div>
                                    <footer style="background-color:white;">
                                      <button type="button" id="submit" class="btn btn-primary fc-header-center">
                                            <spring:message code="act.save"  />
                                        </button>
                                    </footer>
                                </fieldset>
                                    <input type="hidden" id="disappear"  value="<spring:message code="lbl.messagebox.disappear"/>"/>
                                    <input type="hidden" id="msjError"  value="<spring:message code="lbl.messagebox.error.completing"/>"/>
                                    <input type="hidden" id="msjErrorSaving"  value="<spring:message code="lbl.messagebox.error.saving"/>"/>
                                    <input type="hidden" id="msjSuccessful"  value="<spring:message code="lbl.messagebox.successful.saved"/>"/>
                                    <input id="text_opt_select" type="hidden" value="<spring:message code="lbl.select"/>"/>
                                    <input type="hidden" id="msjInvalidCodMxChd"  value="<spring:message code="msg.invalid.code.mx.study.chd"/>"/>
                                    <input type="hidden" id="msjInvalidCodMxCnd"  value="<spring:message code="msg.invalid.code.mx.study.cnd"/>"/>
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
	<!-- JQUERY BOOTSTRAP WIZARD -->
	<spring:url value="/resources/js/plugin/bootstrap-wizard/jquery.bootstrap.wizard.min.js" var="jqueryBootstrap" />
	<script src="${jqueryBootstrap}"></script>
	<!-- JQUERY FUELUX WIZARD -->
	<spring:url value="/resources/js/plugin/fuelux/wizard/wizard.min.js" var="jQueryFueWiz" />
	<script src="${jQueryFueWiz}"></script>
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
	<!-- JQUERY BLOCK UI -->
	<spring:url value="/resources/js/plugin/jquery-blockui/jquery.blockUI.js" var="jqueryBlockUi" />
	<script src="${jqueryBlockUi}"></script>
	<!-- bootstrap datepicker -->
	<spring:url value="/resources/js/plugin/bootstrap-datepicker/bootstrap-datepicker.js" var="datepickerPlugin" />
	<script src="${datepickerPlugin}"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<spring:url value="/resources/scripts/utilidades/seleccionUnidad.js" var="selecUnidad" />
	<script src="${selecUnidad}"></script>
	<spring:url value="/resources/scripts/muestras/enter-form.js" var="enterFormTomaMx" />
	<script src="${enterFormTomaMx}"></script>
	<spring:url value="/resources/scripts/utilidades/handleDatePickers.js" var="handleDatePickers" />
	<script src="${handleDatePickers}"></script>
    <!-- bootstrap datetimepicker -->
    <spring:url value="/resources/js/plugin/bootstrap-datetimepicker/moment.js" var="moment" />
    <script src="${moment}"></script>
    <spring:url value="/resources/js/plugin/bootstrap-datetimepicker/bootstrap-datetimepicker.js" var="datetimepicker" />
    <script src="${datetimepicker}"></script>
    <spring:url value="/resources/js/plugin/bootstrap-datetimepicker/locale/{languagedt}.js" var="datetimePickerLoc">
        <spring:param name="languagedt" value="${pageContext.request.locale.language}" /></spring:url>
    <script src="${datetimePickerLoc}"></script>
    <!-- JQUERY INPUT MASK -->
    <spring:url value="/resources/js/plugin/jquery-inputmask/jquery.inputmask.bundle.min.js" var="jqueryInputMask" />
    <script src="${jqueryInputMask}"></script>
    <spring:url value="/resources/scripts/utilidades/handleInputMask.js" var="handleInputMask" />
    <script src="${handleInputMask}"></script>

	<!-- END PAGE LEVEL SCRIPTS -->
	<!-- PARAMETROS LENGUAJE -->
	<c:set var="blockMess"><spring:message code="blockUI.message" /></c:set>
    <spring:url value="/tomaMx/getStudiesBySampleAndNoti" var="sStudiesUrl"/>
    <spring:url value="/tomaMx/saveTomaMxStudy" var="saveTomaMxStudy"/>
    <spring:url value="/tomaMx/searchStudy" var="searchUrl"/>
    <script type="text/javascript">
        $(document).ready(function() {
            pageSetUp();
            var parametros = {blockMess: "${blockMess}",
                sStudiesUrl: "${sStudiesUrl}",
                saveTomaMxStudy: "${saveTomaMxStudy}",
                              searchUrl: "${searchUrl}"

            };
            EnterFormTomaMxStudies.init(parametros);
            handleInputMasks();
            $("li.samples").addClass("open");
            $("li.tomaMxEstudio").addClass("active");
            if("top"!=localStorage.getItem("sm-setmenu")){
                $("li.tomaMxEstudio").parents("ul").slideDown(200);
            }
            $("#divCategoriaMx").hide();
        });
    </script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>