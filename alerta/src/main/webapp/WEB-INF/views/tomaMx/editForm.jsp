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
        .modal .modal-dialog {
            width: 60%;
        }
        .cancelar {
            padding-left: 0;
            padding-right: 10px;
            text-align: center;
            width: 5%;
        }
        .well {
            margin: 10px 5px 5px 5px;
            padding: 10px 22px;
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
                                <input id="smallBox_content" type="hidden" value="<spring:message code="smallBox.content.4s"/>"/>
                                <input id="msg_request_added" type="hidden" value="<spring:message code="msg.add.request"/>"/>
                                <input id="msg_request_cancel" type="hidden" value="<spring:message code="msg.cancel.request"/>"/>
                                <input id="msgSinUnidadSalud" type="hidden" value="<spring:message code="msg.notification.without.health.unit"/>"/>
                                <input id="lbl_yes" type="hidden" value="<spring:message code="lbl.yes"/>"/>
                                <input id="lbl_no" type="hidden" value="<spring:message code="lbl.no"/>"/>
                                <input id="msg_request_cannotbe_edited" type="hidden" value="<spring:message code="msg.sample.already.sent"/>"/>
                                <input id="msjErrorSaving" type="hidden" value="<spring:message code="lbl.messagebox.error.saving"/>"/>
                                <input id="msjSuccessful" type="hidden" value="<spring:message code="lbl.messagebox.successful.saved"/>"/>
                                <!-- widget content -->
								<div class="widget-body fuelux">
                                    <form id="noti" class="smart-form" >
                                        <input id="esEstudio" type="hidden" value="${esEstudio}"/>
                                    <fieldset >
                                        <legend class="text-left txt-color-blue font-md"> <spring:message code="lbl.notification.data"/>
                                                ${muestra.idNotificacion.codTipoNotificacion.valor}
                                        </legend>
                                        <div class="row">

                                            <section class="col col-md-4">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="sindfeb.silais" />
                                                    <input value="${muestra.idNotificacion.codTipoNotificacion.codigo}" hidden="hidden" type="text" id="tipoNoti" name="tipoNoti"/>
                                                    <input value="${muestra.idTomaMx}" hidden="hidden" type="text" id="idTomaMx" name="idTomaMx"/>
                                                    <input value="${muestra.idNotificacion.codUnidadAtencion.unidadId}" type="hidden" id="unidadId" name="unidadId"/>
                                                    <input id="idTipoMx" type="hidden" value="${muestra.codTipoMx.idTipoMx}"/>
                                                </label>


                                                <div class="input-group">
                                                    <span class="input-group-addon"><i class="fa fa-location-arrow"></i></span>
                                                    <label class="input">
                                                        <input style="background-color: #f0fff0" disabled class="form-control" value="${muestra.idNotificacion.codSilaisAtencion.nombre}" type="text"   />
                                                    </label>
                                                    <span class="input-group-addon"><i class="fa fa-chevron-down fa-fw"></i></span>
                                                </div>
                                            </section>

                                            <section class="col col-md-8">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.health.unit" />
                                                </label>

                                                <div class="input-group">
                                                    <span class="input-group-addon"><i class="fa fa-location-arrow"></i></span>
                                                    <label class="input">
                                                        <input style="background-color: #f0fff0" disabled class="form-control" type="text" value="${muestra.idNotificacion.codUnidadAtencion.nombre}"   />
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
                                                <div class="">
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil"></i><i class="icon-append fa fa-sort-alpha-asc"></i>
                                                        <input style="background-color: #f0fff0" disabled class="form-control" type="text" value="${muestra.idNotificacion.persona.primerNombre}" />
                                                    </label>
                                                </div>
                                            </section>
                                            <section class="col col-md-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="person.name2" />
                                                </label>

                                                <div class="">
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil"></i><i class="icon-append fa fa-sort-alpha-asc"></i>
                                                        <input style="background-color: #f0fff0" disabled class="form-control" type="text" value="${muestra.idNotificacion.persona.segundoNombre}"/>
                                                    </label>
                                                </div>
                                            </section>
                                            <section class="col col-md-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="person.lastname1" />
                                                </label>
                                                <div class="">
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil"></i><i class="icon-append fa fa-sort-alpha-asc"></i>
                                                        <input style="background-color: #f0fff0" disabled class="form-control" type="text" value="${muestra.idNotificacion.persona.primerApellido}"  />
                                                    </label>
                                                </div>
                                            </section>
                                            <section class="col col-md-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="person.lastname2" />
                                                </label>
                                                <div class="">
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil"></i><i class="icon-append fa fa-sort-alpha-asc"></i>
                                                        <input style="background-color: #f0fff0" disabled class="form-control" type="text" value="${muestra.idNotificacion.persona.segundoApellido}" />
                                                    </label>
                                                </div>
                                            </section>
                                        </div>
                                        <div class="row">
                                            <section class="col col-md-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="person.sexo" />
                                                </label>
                                                <div class="">
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil"></i><i class="icon-append fa fa-sort-alpha-asc"></i>
                                                        <input style="background-color: #f0fff0" disabled class="form-control" type="text" value="${muestra.idNotificacion.persona.sexo.valor}"  >
                                                    </label>
                                                </div>
                                            </section>
                                            <section class="col col-md-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="person.fecnac" />
                                                </label>
                                                <div class="">
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-calendar fa-fw"></i>
                                                        <input style="background-color: #f0fff0" class="form-control" disabled type="text"
                                                               value="<fmt:formatDate value="${muestra.idNotificacion.persona.fechaNacimiento}" pattern="dd/MM/yyyy" />"/>
                                                    </label>
                                                </div>
                                            </section>
                                            <section class="col col-md-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.register.date" />
                                                </label>
                                                <div class="">
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-calendar fa-fw"></i>
                                                        <input style="background-color: #f0fff0" class="form-control" disabled  type="text"
                                                               value="<fmt:formatDate value="${muestra.idNotificacion.fechaRegistro}" pattern="dd/MM/yyyy" />" />
                                                    </label>
                                                </div>
                                            </section>
                                            <section class="col col-3">
                                                <label class="text-left txt-color-blue font-md hidden-xs">
                                                    <spring:message code="sindfeb.urgent" />
                                                </label>
                                                <div class="input-group">
                                                    <span class="input-group-addon"><i class="fa fa-exclamation-triangle"></i></span>
                                                    <label class="input">
                                                        <input style="background-color: #f0fff0" disabled class="form-control" value="${muestra.idNotificacion.urgente}" type="text"/>
                                                    </label>
                                                    <span class="input-group-addon"><i class="fa fa-chevron-down fa-fw"></i></span>
                                                </div>
                                            </section>
                                        </div>
                                        <div class="row">
                                            <section class="col col-md-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.symptoms.start.date" />
                                                </label>

                                                <div class="input-group">
                                                    <span class="input-group-addon"><i class="fa fa-pencil"></i></span>
                                                    <label class="input">
                                                        <input style="background-color: #f0fff0" class="form-control" readonly id="fechaInicioSintomas" name="fechaInicioSintomas"
                                                               type="text" value="<fmt:formatDate value="${muestra.idNotificacion.fechaInicioSintomas}" pattern="dd/MM/yyyy" />"
                                                               placeholder=" <spring:message code="lbl.symptoms.start.date" />">
                                                    </label>
                                                    <span class="input-group-addon"><i class="fa fa-calendar fa-fw"></i></span>
                                                </div>
                                            </section>
                                        </div>
                                     </fieldset>
                                    </form>
                                    <form  id="registroMx" class="smart-form">
                                    <fieldset>
                                        <legend class="text-left txt-color-blue font-md"> <spring:message
                                                code="lbl.taking.sample.data"/></legend>
                                        <div class="row">
                                            <section class="col col-sm-4 col-md-3 col-lg-3">
                                                <label class="text-left txt-color-blue font-md"><spring:message code="lbl.changeUs"/></label>

                                            </section>
                                            <section class="col col-sm-4 col-md-3 col-lg-2">
                                                <c:choose>
                                                    <c:when test="${not empty muestra.codSilaisAtencion}">
                                                        <label class="checkbox">
                                                            <input type="checkbox" checked name="ckChangeUS" id="ckChangeUS">
                                                            <i></i>
                                                        </label>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <label class="checkbox">
                                                            <input type="checkbox" name="ckChangeUS" id="ckChangeUS">
                                                            <i></i>
                                                        </label>
                                                    </c:otherwise>
                                                </c:choose>

                                            </section>
                                        </div>

                                        <div id="dSilais" hidden="hidden"  class="row">
                                            <section class="col col-sm-12 col-md-6 col-lg-3">
                                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                <label class="text-left txt-color-blue font-md hidden-xs">
                                                    <spring:message code="sindfeb.silais" />
                                                </label>

                                                <div class="input-group">
                                                    <span class="input-group-addon"> <i class="fa fa-list fa-fw"></i></span>
                                                    <select data-placeholder="<spring:message code="act.select" /> <spring:message code="lbl.silais" />" name="codSilaisAtencion" id="codSilaisAtencion" class="select2">
                                                        <option value=""></option>
                                                        <c:forEach items="${entidades}" var="entidad">
                                                            <c:choose>
                                                                <c:when test="${entidad.codigo eq muestra.codSilaisAtencion.codigo}">
                                                                    <option selected value="${entidad.codigo}">${entidad.nombre}</option>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <option value="${entidad.codigo}">${entidad.nombre}</option>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </section>
                                                    <section class="col col-4">
                                                        <label class="text-left txt-color-blue font-md hidden-xs">
                                                            <spring:message code="sindfeb.muni" />
                                                        </label>
                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                                                            <select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.muni" />" name="codMunicipio" id="codMunicipio" class="select2">
                                                                <option value=""></option>
                                                                <c:forEach items="${municipios}" var="muni">
                                                                    <c:choose>
                                                                        <c:when test="${muni.codigoNacional eq muestra.codUnidadAtencion.municipio.codigoNacional}">
                                                                            <option selected value="${muni.codigoNacional}">${muni.nombre}</option>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <option value="${muni.codigoNacional}">${muni.nombre}</option>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </section>
                                                    <section class="col col-4">
                                                        <label class="text-left txt-color-blue font-md hidden-xs">
                                                            <spring:message code="sindfeb.unidad" />
                                                        </label>
                                                        <div class="input-group">
                                                            <span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
                                                            <select data-placeholder="<spring:message code="act.select" /> <spring:message code="lbl.health.unit" />" name="codUnidadAtencion" id="codUnidadAtencion" class="select2">
                                                                <option value=""></option>
                                                                <c:forEach items="${unidades}" var="us">
                                                                    <c:choose>
                                                                        <c:when test="${us.codigo eq muestra.codUnidadAtencion.codigo}">
                                                                            <option selected value="${us.codigo}">${us.nombre}</option>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <option value="${us.codigo}">${us.nombre}</option>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </section>

                                         </div>
                                        <div class="row">
                                            <section class="col col-3">
                                                <label  class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.state" />
                                                </label>
                                                <input id="codEstadoMx" value="${muestra.estadoMx.codigo}" type='hidden'/>
                                                <div class="">
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                                                        <input name="estadoMx" readonly value="${muestra.estadoMx.valor}" type='text'  class="form-control"/>
                                                    </label>
                                                </div>
                                            </section>
                                            <section class="col col-3">
                                                <label  class="text-left txt-color-blue font-md">
                                                    <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.sampling.date" />
                                                </label>
                                                <div class="">
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-calendar fa-fw"></i>
                                                        <input name="fechaHTomaMx" id="fechaHTomaMx" value="<fmt:formatDate value="${muestra.fechaHTomaMx}" pattern="dd/MM/yyyy" />" type='text'
                                                               class="form-control date-picker" data-date-end-date="+0d"
                                                               placeholder="<spring:message code="lbl.sampling.date" />"/>
                                                    </label>
                                                </div>
                                            </section>
                                            <section class="col col-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.sampling.time" />
                                                </label>
                                                <div class=''>
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-clock-o fa-fw"></i>
                                                        <input id="horaTomaMx" name="horaTomaMx" value="${muestra.horaTomaMx}" type='text'
                                                               class="form-control"
                                                               placeholder="<spring:message code="lbl.sampling.time" />"/>
                                                    </label>
                                                </div>
                                            </section>
                                            <section class="col col-3">
                                                <label class="text-left txt-color-blue font-md">
                                                   <spring:message code="lbl.sample.type"/>
                                                </label>
                                                <div class="">
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                                                        <input id="codTipoMx" name="codTipoMx" disabled value="${muestra.codTipoMx.nombre}" type='text'
                                                               class="form-control"
                                                               placeholder="<spring:message code="lbl.sampling.time" />"/>
                                                    </label>
                                                </div>
                                            </section>
                                        </div>
                                        <div class="row">
                                            <section class="col col-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="lbl.number.tubes" />
                                                </label>
                                                <div class="">
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                                                        <input name="canTubos" id="canTubos" value="${muestra.canTubos}" class="form-control entero" type="text"
                                                               placeholder=" <spring:message code="lbl.number.tubes" />"/>
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
                                                        <i class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-sort-numeric-asc fa-fw"></i>
                                                        <input value="${muestra.volumen}" id="volumen" name="volumen" class="decimal"  type="text"
                                                               placeholder="<spring:message code="lbl.volume" />" />
                                                        <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.volume"/></b>
                                                    </label>
                                                </div>
                                            </section>
                                            <section class="col col-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="cooling.time" />
                                                </label>
                                                <div class=''>
                                                    <label class="input">
                                                        <i class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-calendar fa-fw"></i>
                                                        <input id="horaRefrigeracion" name="horaRefrigeracion" value="${muestra.horaRefrigeracion}" type='text'
                                                               class="form-control"
                                                               placeholder="<spring:message code="cooling.time" />"/>
                                                        <b class="tooltip tooltip-bottom-right"> <i class="fa fa-warning txt-color-pink"></i> <spring:message code="msg.enter.cooling.time"/></b>
                                                    </label>
                                                </div>
                                            </section>
                                            <section class="col col-sm-12 col-md-3 col-lg-3">
                                                <label class="text-left txt-color-blue font-md">
                                                    <spring:message code="sample.separation"/>
                                                </label>
                                                <input id="mxSeparadaVal" name="mxSeparadaVal" value="${muestra.mxSeparada}" type='hidden'/>
                                                <div class="inline-group">
                                                    <c:choose>
                                                        <c:when test="${not empty muestra.mxSeparada and muestra.mxSeparada == true}">
                                                            <label class="radio">
                                                                <input type="radio" checked name="mxSeparada" value="1">
                                                                <i></i><spring:message code="lbl.yes"/></label>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <label class="radio">
                                                                <input type="radio" name="mxSeparada" value="1">
                                                                <i></i><spring:message code="lbl.yes"/></label>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${not empty muestra.mxSeparada and muestra.mxSeparada == false}">
                                                            <label class="radio">
                                                                <input type="radio" checked name="mxSeparada" value="0">
                                                                <i></i><spring:message code="lbl.no"/></label>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <label class="radio">
                                                                <input type="radio" name="mxSeparada" value="0">
                                                                <i></i><spring:message code="lbl.no"/></label>
                                                        </c:otherwise>
                                                    </c:choose>

                                                </div>
                                                <div hidden="hidden" id="dErrorCantTubos" class="errorDiv txt-color-red"><spring:message code="lbl.required.field"/></div>
                                            </section>
                                        </div>
                                        <div>
                                            <header>
                                                <c:choose>
                                                    <c:when test="${muestra.estadoMx.codigo eq 'ESTDMX|PEND' }">
                                                        <label class="text-left txt-color-blue" style="font-weight: bold">
                                                            <spring:message code="lbl.requests" />
                                                        </label>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <label class="text-left txt-color-blue" style="font-weight: bold">
                                                            <spring:message code="lbl.requests" /> <i class="fa fa-angle-right"></i>
                                                        </label>
                                                        <label class="text-left txt-color-red"><spring:message code="msg.sample.already.sent" /></label>
                                                    </c:otherwise>
                                                </c:choose>
                                            </header>
                                            <br/>
                                            <br/>
                                            <div class="widget-body no-padding">
                                                <table id="solicitudes_list" class="table table-striped table-bordered table-hover" width="100%">
                                                    <thead>
                                                    <tr>
                                                        <th data-class="expand"><i class="fa fa-fw fa-list text-muted hidden-md hidden-sm hidden-xs"></i><spring:message code="lbl.send.request.type"/></th>
                                                        <th data-hide="phone"><i class="fa fa-fw fa-list text-muted hidden-md hidden-sm hidden-xs"></i><spring:message code="lbl.desc.request"/></th>
                                                        <th data-hide="phone"><i class="fa fa-fw fa-calendar text-muted hidden-md hidden-sm hidden-xs"></i><spring:message code="lbl.send.request.date"/></th>
                                                        <th data-hide="phone"><spring:message code="lbl.state"/></th>
                                                        <th data-hide="phone"><spring:message code="act.cancel"/></th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>

                                                    </tbody>
                                                </table>
                                            </div>
                                            <div class="row">
                                                <section class="col col-sm-12 col-md-12 col-lg-12">
                                                    <button type="button" id="btnAddDx" class="btn btn-primary styleButton" data-toggle="modal"
                                                            data-target="modalSolicitudes">
                                                        <i class="fa fa-plus icon-white"></i>
                                                        <spring:message code="act.add"/> <spring:message code="lbl.request"/>
                                                    </button>
                                                </section>
                                            </div>

                                        </div>
                                        <footer style="background-color:white;">

                                            <button type="button" id="submit" class="btn btn-success fc-header-center">
                                                <spring:message code="act.save"  />
                                                <button type="button" id="back" class="btn btn-danger fc-header-center">
                                                    <spring:message code="act.cancel"  />
                                                </button>
                                            </button>
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
			</section>
			<!-- end widget grid -->
            <div class="modal fade" id="modalSolicitudes" aria-hidden="true" data-backdrop="static"> <!--tabindex="-1" role="dialog" -->
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <div class="alert alert-info">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                    &times;
                                </button>
                                <h4 class="modal-title">
                                    <spring:message code="act.add"/> <spring:message code="lbl.request"/>
                                </h4>
                            </div>
                        </div>
                        <div class="modal-body"> <!--  no-padding -->
                            <form id="addDx-form" class="smart-form" novalidate="novalidate">
                                <fieldset>
                                    <div class="row">
                                        <c:choose>
                                            <c:when test="${esEstudio}">
                                                <section class="col col-sm-12 col-md-12 col-lg-12">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.study" />
                                                    </label>
                                                    <div class="input-group">
                                <span class="input-group-addon">
                                    <i class="fa fa-location-arrow fa-fw"></i>
                                </span>
                                                        <select  class="select2" id="codEstudioNuevo" name="codEstudioNuevo" >
                                                            <option value=""><spring:message code="lbl.select" />...</option>
                                                        </select>
                                                    </div>
                                                </section>
                                            </c:when>
                                            <c:otherwise>
                                                <section class="col col-sm-12 col-md-12 col-lg-12">
                                                    <label class="text-left txt-color-blue font-md">
                                                        <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.routine" />
                                                    </label>
                                                    <div class="input-group">
                                <span class="input-group-addon">
                                    <i class="fa fa-location-arrow fa-fw"></i>
                                </span>
                                                        <select  class="select2" id="codDXNuevo" name="codDXNuevo" >
                                                            <option value=""><spring:message code="lbl.select" />...</option>
                                                        </select>
                                                    </div>
                                                </section>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </fieldset>

                                <footer>
                                    <button type="submit" class="btn btn-success" id="btnAgregarDx">
                                        <i class="fa fa-save"></i> <spring:message code="act.save" />
                                    </button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">
                                        <i class="fa fa-times"></i> <spring:message code="act.end" />
                                    </button>

                                </footer>

                            </form>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->


            <!-- Modal -->
            <div class="modal fade" id="modalOverrideSoli" aria-hidden="true" data-backdrop="static">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <div class="alert alert-info">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                    &times;
                                </button>
                                <h4 class="modal-title">
                                    <i class="fa-fw fa fa-times"></i>
                                    <spring:message code="lbl.override" /> <spring:message code="lbl.request" />
                                </h4>
                            </div>
                        </div>
                        <div class="modal-body"> <!--  no-padding -->
                            <form id="override-sol-form" class="smart-form" novalidate="novalidate">
                                <input id="idSolicitud" type="hidden" value=""/>
                                <fieldset>
                                    <div class="row">
                                        <section class="col col-sm-12 col-md-12 col-lg-12">
                                            <label class="text-left txt-color-blue font-md">
                                                <i class="fa fa-fw fa-asterisk txt-color-red font-sm"></i><spring:message code="lbl.annulment.cause" /> </label>
                                            <div class="">
                                                <label class="textarea">
                                                    <i class="icon-prepend fa fa-pencil fa-fw"></i><i class="icon-append fa fa-sort-alpha-asc fa-fw"></i>
                                                    <textarea class="form-control" rows="3" name="causaAnulacion" id="causaAnulacion"
                                                              placeholder="<spring:message code="lbl.annulment.cause" />"></textarea>
                                                    <b class="tooltip tooltip-bottom-right"> <i
                                                            class="fa fa-warning txt-color-pink"></i> <spring:message code="tooltip.annulment.cause"/>
                                                    </b>
                                                </label>
                                            </div>
                                        </section>
                                    </div>
                                </fieldset>
                                <footer>
                                    <button type="submit" class="btn btn-success">
                                        <i class="fa fa-save"></i> <spring:message code="act.accept" />
                                    </button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">
                                        <i class="fa fa-times"></i> <spring:message code="act.cancel" />
                                    </button>
                                </footer>
                            </form>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
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
	<spring:url value="/resources/scripts/muestras/edit-form.js" var="enterFormTomaMx" />
	<script src="${enterFormTomaMx}"></script>
	<spring:url value="/resources/scripts/utilidades/handleDatePickers.js" var="handleDatePickers" />
	<script src="${handleDatePickers}"></script>
    <!-- bootstrap datetimepicker -->
    <!-- bootstrap datetimepicker -->
    <spring:url value="/resources/js/plugin/bootstrap-datetimepicker-4/moment-with-locales.js" var="moment" />
    <script src="${moment}"></script>
    <spring:url value="/resources/js/plugin/bootstrap-datetimepicker-4/bootstrap-datetimepicker.js" var="datetimepicker" />
    <script src="${datetimepicker}"></script>
    <!-- JQUERY INPUT MASK -->
    <spring:url value="/resources/js/plugin/jquery-inputmask/jquery.inputmask.bundle.min.js" var="jqueryInputMask" />
    <script src="${jqueryInputMask}"></script>
    <spring:url value="/resources/scripts/utilidades/handleInputMask.js" var="handleInputMask" />
    <script src="${handleInputMask}"></script>

	<!-- END PAGE LEVEL SCRIPTS -->
	<!-- PARAMETROS LENGUAJE -->
	<c:set var="blockMess"><spring:message code="blockUI.message" /></c:set>
    <spring:url value="/tomaMx/dxByMx" var="dxUrl"/>
    <spring:url value="/tomaMx/editToma" var="saveTomaUrl"/>
    <spring:url value="/tomaMx/search" var="searchUrl"/>
    <spring:url var="municipiosURL" value="/api/v1/municipiosbysilais"/>
    <spring:url var="unidadesUrl"   value="/api/v1/unidadesPrimHosp"  />
    <c:url var="sGetSolicitudesUrl" value="/tomaMx/getSolicitudes"/>
    <c:url var="sAnularSolicitudUrl" value="/tomaMx/anularSolicitud"/>
    <spring:url value="/tomaMx/getStudiesBySampleAndNoti" var="sEstudiosURL"/>
    <c:url var="sAgregarSolicitudUrl" value="/tomaMx/agregarSolicitud"/>
    <script type="text/javascript">
        $(document).ready(function() {
            pageSetUp();
            var parametros = {blockMess: "${blockMess}",
                             dxUrl: "${dxUrl}",
                              saveTomaUrl: "${saveTomaUrl}",
                              searchUrl: "${searchUrl}",
                              municipiosUrl:"${municipiosURL}",
                              unidadesUrl: "${unidadesUrl}",
                sGetSolicitudesUrl : "${sGetSolicitudesUrl}",
                sAnularSolicitudUrl : "${sAnularSolicitudUrl}",
                sEstudiosURL : "${sEstudiosURL}",
                sAgregarSolicitudUrl : "${sAgregarSolicitudUrl}"
            };
            EditFormTomaMx.init(parametros);
            SeleccionUnidad.init(parametros);
            $("#ckChangeUS").change();
            handleInputMasks();
            handleDatePickers("${pageContext.request.locale.language}");
            $("li.samples").addClass("open");
            $("li.searchMx").addClass("active");
            if("top"!=localStorage.getItem("sm-setmenu")){
                $("li.searchMx").parents("ul").slideDown(200);
            }
        });
    </script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>