<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
							<spring:message code="sindfeb.header" />
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
								<span class="widget-icon"> <i class="fa fa-fire"></i> </span>
								<h2><spring:message code="sindfeb.header" /> </h2>				
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
									<div class="wizard">
										<ul class="steps">
											<li data-target="#step1" class="active">
												<span class="badge badge-info">1</span><spring:message code="sindfeb.step1" /><span class="chevron"></span>
											</li>
											<li data-target="#step2">
												<span class="badge">2</span><spring:message code="sindfeb.step2" /><span class="chevron"></span>
											</li>
											<li data-target="#step3">
												<span class="badge">3</span><spring:message code="sindfeb.step3" /><span class="chevron"></span>
											</li>
											<li data-target="#step4">
												<span class="badge">4</span><spring:message code="sindfeb.step4" /><span class="chevron"></span>
											</li>
											<li data-target="#step5">
												<span class="badge">5</span><spring:message code="sindfeb.step5" /><span class="chevron"></span>
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
										<form id="fuelux-wizard" id="wizard1" class ="smart-form">
											<!-- wizard form starts here -->
											<div class="step-pane active" id="step1">
												<h3><spring:message code="sindfeb.step1.long" /></h3>
												<h4><spring:message code="lbl.step2.short" />: ${daSindFeb.idNotificacion.persona.primerNombre} ${daSindFeb.idNotificacion.persona.primerApellido}</h4>
												<fieldset>
													<!-- START ROW -->
                                      				<div class="row">
                                            		<section class="col col-4">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.silais" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.silais" />" name="codSilaisAtencion" id="codSilaisAtencion" class="select2">
																<option value=""></option>
																<c:forEach items="${entidades}" var="entidad">
																	<c:choose> 
																		<c:when test="${entidad.codigo eq daSindFeb.idNotificacion.codSilaisAtencion.codigo}">
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
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.muni" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.muni" />" name="codMunicipio" id="codMunicipio" class="select2">
	                                                    		<option value=""></option>
																<c:forEach items="${munic}" var="muni">
																	<c:choose> 
																		<c:when test="${muni.codigoNacional eq daSindFeb.idNotificacion.codUnidadAtencion.municipio.codigoNacional}">
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
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.unidad" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.unidad" />" name="codUnidadAtencion" id="codUnidadAtencion" class="select2">
																<option value=""></option>
																<c:forEach items="${uni}" var="us">
																	<c:choose> 
																		<c:when test="${us.codigo eq daSindFeb.idNotificacion.codUnidadAtencion.codigo}">
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
                                            		<!-- END ROW -->
                                            		<!-- START ROW -->
                                            		<div class="row">
                                            		<section class="col col-4">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.exp" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-folder"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="codExpediente" id="codExpediente" value="${daSindFeb.codExpediente}"
																	placeholder=" <spring:message code="act.enter" /> <spring:message code="sindfeb.exp" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="act.enter" /> <spring:message code="sindfeb.exp" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-4">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.numFicha" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-tag"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="numFicha" id="numFicha" value="${daSindFeb.numFicha}"
																	placeholder=" <spring:message code="act.enter" /> <spring:message code="sindfeb.numFicha" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="act.enter" /> <spring:message code="sindfeb.numFicha" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-numeric-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-4">
                                            			<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.date" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-pencil"></i></span>
															<label class="input">
																<input class="form-control date-picker" data-date-end-date="+0d" data-date-start-date="-30d" 
																	type="text" name="fechaFicha" id="fechaFicha" value="<fmt:formatDate value="${daSindFeb.fechaFicha}" pattern="dd/MM/yyyy" />"
																	placeholder=" <spring:message code="act.enter" /> <spring:message code="sindfeb.date" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="act.enter" /> <spring:message code="sindfeb.date" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-calendar fa-fw"></i></span>
														</div>
                                            		</section>
                                            		</div>
                                            		<!-- END ROW -->
                                            	</fieldset>
											</div>
											<div class="step-pane" id="step2">
												<h3><spring:message code="sindfeb.step2.long" /></h3>
												<fieldset>
													<!-- START ROW -->
	                                      			<div class="row">
                                      				<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="person.name1" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="primerNombre" id="primerNombre" 
																	value="${daSindFeb.idNotificacion.persona.primerNombre}" readonly
																	placeholder=" <spring:message code="person.name1" />">
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="person.name2" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="segundoNombre" id="segundoNombre" 
																	value="${daSindFeb.idNotificacion.persona.segundoNombre}" readonly
																	placeholder=" <spring:message code="person.name2" />">
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="person.lastname1" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="primerApellido" id="primerApellido" 
																	value="${daSindFeb.idNotificacion.persona.primerApellido}" readonly
																	placeholder=" <spring:message code="person.lastname1" />">
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="person.lastname2" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="segundoApellido" id="segundoApellido" 
																	value="${daSindFeb.idNotificacion.persona.segundoApellido}" readonly
																	placeholder=" <spring:message code="person.name2" />">
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
	                                      			</div>	
	                                      			<!-- END ROW -->
	                                            	<!-- START ROW -->
	                                            	<div class="row">
                                            		<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="person.fecnac" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-pencil"></i></span>
															<label class="input">
																<input class="form-control" readonly 
																	type="text" name="fechaNacimiento" id="fechaNacimiento" value="<fmt:formatDate value="${daSindFeb.idNotificacion.persona.fechaNacimiento}" pattern="dd/MM/yyyy" />"
																	placeholder=" <spring:message code="person.fecnac" />">
															</label>
															<span class="input-group-addon"><i class="fa fa-calendar fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="lbl.age" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="edad" id="edad" readonly
																placeholder=" <spring:message code="lbl.age" />">
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-numeric-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="person.sexo" />
                                            			</label>
                                            			<div class="input-group">
                                            				<c:choose>
															<c:when test="${daSindFeb.idNotificacion.persona.sexo.codigo eq 'SEXO|M'}">
																<span class="input-group-addon"><i class="fa fa-male fa-fw"></i></span>
															</c:when>
															<c:when test="${daSindFeb.idNotificacion.persona.sexo.codigo eq 'SEXO|F'}">
																<span class="input-group-addon"><i class="fa fa-female fa-fw"></i></span>
															</c:when>
															<c:otherwise>
																<span class="input-group-addon"><i class="fa fa-question fa-fw"></i></span>
															</c:otherwise>
															</c:choose>
															<label class="input">
																<input class="form-control" type="text" name="sexo" id="sexo" 
																	value="${daSindFeb.idNotificacion.persona.sexo}" readonly
																	placeholder=" <spring:message code="person.sexo" />">
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="person.ocupacion" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-wrench"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="person.ocupacion" />" name="ocupacion" id="ocupacion" class="select2">
																<option value=""></option>
																<c:forEach items="${ocupaciones}" var="ocupacion">
																	<c:choose> 
																		<c:when test="${ocupacion.codigo eq daSindFeb.idNotificacion.persona.ocupacion.codigo}">
																			<option selected value="${ocupacion.codigo}">${ocupacion.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${ocupacion.codigo}">${ocupacion.nombre}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
	                                            	</div>
	                                            	<!-- END ROW -->
	                                            	<!-- START ROW -->
	                                      			<div class="row">
                                      				<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="lbl.mother.father" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-male"></i><i class="fa fa-female"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="nombPadre" id="nombPadre" 
																	value="${daSindFeb.nombPadre}" 
																	placeholder=" <spring:message code="lbl.mother.father" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="msg.enter.mother" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-9">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="person.direccion" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-map-marker fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="direccionResidencia" id="direccionResidencia" 
																	value="${daSindFeb.idNotificacion.persona.direccionResidencia}" 
																	placeholder=" <spring:message code="person.direccion" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="person.direccion" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
	                                      			</div>	
	                                      			<!-- END ROW -->
	                                            	<!-- START ROW -->
                                      				<div class="row">
                                            		<section class="col col-3">
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="lbl.person.depart.resi" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-location-arrow fa-fw"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="lbl.person.depart.resi" />" name="departamento" id="departamento" class="select2">
																<option value=""></option>
																<c:forEach items="${departamentos}" var="departamento">
																	<c:choose> 
																		<c:when test="${departamento.codigoNacional eq daSindFeb.idNotificacion.persona.municipioResidencia.dependencia.codigoNacional}">
																			<option selected value="${departamento.codigoNacional}">${departamento.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${departamento.codigoNacional}">${departamento.nombre}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="person.mun.res" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-location-arrow fa-fw"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.muni" />" name="municipioResidencia" id="municipioResidencia" class="select2">
																<option value=""></option>
																<c:forEach items="${municipiosResi}" var="municipioResi">
																	<c:choose> 
																		<c:when test="${municipioResi.codigoNacional eq daSindFeb.idNotificacion.persona.municipioResidencia.codigoNacional}">
																			<option selected value="${municipioResi.codigoNacional}">${municipioResi.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${municipioResi.codigoNacional}">${municipioResi.nombre}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="person.com.res" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-location-arrow fa-fw"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="person.com.res" />" name="comunidadResidencia" id="comunidadResidencia" class="select2">
																<option value=""></option>
																<c:forEach items="${comunidades}" var="comunid">
																	<c:choose> 
																		<c:when test="${comunid.codigo eq daSindFeb.idNotificacion.persona.comunidadResidencia.codigo}">
																			<option selected value="${comunid.codigo}">${comunid.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${comunid.codigo}">${comunid.nombre}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="lbl.provenance" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-location-arrow fa-fw"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="lbl.provenance" />" name="codProcedencia" id="codProcedencia" class="select2">
																<option value=""></option>
																<c:forEach items="${catProcedencia}" var="catProc">
																	<c:choose> 
																		<c:when test="${catProc.codigo eq daSindFeb.codProcedencia.codigo}">
																			<option selected value="${catProc.codigo}">${catProc.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${catProc.codigo}">${catProc.valor}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		</div>
                                            		<!-- END ROW -->
                                            		<!-- START ROW -->
                                      				<div class="row">
                                            		<section class="col col-3">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.travel" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-car fa-fw"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.travel" />" name="viaje" id="viaje" class="select2">
																<option value=""></option>
																<c:forEach items="${catResp}" var="cresp">
																	<c:choose> 
																		<c:when test="${cresp.codigo eq daSindFeb.viaje.codigo}">
																			<option selected value="${cresp.codigo}">${cresp.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${cresp.codigo}">${cresp.valor}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.travel.where" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-globe fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="dondeViaje" id="dondeViaje" 
																	value="${daSindFeb.dondeViaje}" 
																	placeholder=" <spring:message code="sindfeb.travel.where" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.travel.where" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.pregnancy" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-child fa-fw"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.pregnancy" />" name="embarazo" id="embarazo" class="select2">
																<option value=""></option>
																<c:forEach items="${catResp}" var="cresp">
																	<c:choose> 
																		<c:when test="${cresp.codigo eq daSindFeb.embarazo.codigo}">
																			<option selected value="${cresp.codigo}">${cresp.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${cresp.codigo}">${cresp.valor}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.pregnancy.time" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-child fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="mesesEmbarazo" id="mesesEmbarazo" 
																	value="${daSindFeb.mesesEmbarazo}" 
																	placeholder=" <spring:message code="sindfeb.pregnancy.time" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.pregnancy.time" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-calendar fa-fw"></i></span>
														</div>
                                            		</section>
                                            		</div>
                                            		<!-- END ROW -->
                                            		<!-- START ROW -->
                                      				<div class="row">
                                      				<section class="col col-3">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.cronic" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-wheelchair"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.cronic" />" name="enfCronica" id="enfCronica" multiple style="width:100%" class="select2">
																<option value=""></option>
																<c:forEach items="${enfCronicas}" var="enfCronica">
																	<c:choose> 
																		<c:when test="${fn:contains(daSindFeb.enfCronica, enfCronica.codigo)}">
																			<option selected value="${enfCronica.codigo}">${enfCronica.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${enfCronica.codigo}">${enfCronica.valor}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.cronic.other" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="otraCronica" id="otraCronica" 
																	value="${daSindFeb.otraCronica}" 
																	placeholder=" <spring:message code="sindfeb.cronic.other" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.cronic.other" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.acute" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-user-md fa-fw"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.acute" />" name="enfAgudaAdicional" id="enfAgudaAdicional" multiple style="width:100%" class="select2">
																<option value=""></option>
																<c:forEach items="${enfAgudas}" var="enfAguda">
																	<c:choose> 
																		<c:when test="${fn:contains(daSindFeb.enfAgudaAdicional, enfAguda.codigo)}">
																			<option selected value="${enfAguda.codigo}">${enfAguda.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${enfAguda.codigo}">${enfAguda.valor}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.acute.other" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="otraAgudaAdicional" id="otraAgudaAdicional" 
																	value="${daSindFeb.otraAgudaAdicional}" 
																	placeholder=" <spring:message code="sindfeb.acute.other" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.acute.other" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                      				</div>
                                      				<!-- END ROW -->
	                                            </fieldset>
											</div>
											<div class="step-pane" id="step3">
												<h3><spring:message code="sindfeb.step3.long" /></h3>
												<h4><spring:message code="lbl.step2.short" />: ${daSindFeb.idNotificacion.persona.primerNombre} ${daSindFeb.idNotificacion.persona.primerApellido}</h4>
												<fieldset>
													<!-- START ROW -->
                                      				<div class="row">
                                      				<section class="col col-3">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.water" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.water" />" name="fuenteAgua" id="fuenteAgua" multiple style="width:100%" class="select2">
																<option value=""></option>
																<c:forEach items="${fuentesAgua}" var="fuenteAgua">
																	<c:choose> 
																		<c:when test="${fn:contains(daSindFeb.fuenteAgua, fuenteAgua.codigo)}">
																			<option selected value="${fuenteAgua.codigo}">${fuenteAgua.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${fuenteAgua.codigo}">${fuenteAgua.valor}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.water.other" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="otraFuenteAgua" id="otraFuenteAgua" 
																	value="${daSindFeb.otraFuenteAgua}" 
																	placeholder=" <spring:message code="sindfeb.water.other" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.water.other" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.animals" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.animals" />" name="animales" id="animales" multiple style="width:100%" class="select2">
																<option value=""></option>
																<c:forEach items="${animales}" var="animal">
																	<c:choose> 
																		<c:when test="${fn:contains(daSindFeb.animales, animal.codigo)}">
																			<option selected value="${animal.codigo}">${animal.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${animal.codigo}">${animal.valor}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.animals.other" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="otrosAnimales" id="otrosAnimales" 
																	value="${daSindFeb.otrosAnimales}" 
																	placeholder=" <spring:message code="sindfeb.animals.other" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.animals.other" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                      				</div>
                                      				<!-- END ROW -->
												</fieldset>
											</div>
											<div class="step-pane" id="step4">
												<h3><spring:message code="sindfeb.step4.long" /></h3>
												<h4><spring:message code="lbl.step2.short" />: ${daSindFeb.idNotificacion.persona.primerNombre} ${daSindFeb.idNotificacion.persona.primerApellido}</h4>
												<fieldset>
													<!-- START ROW -->
                                      				<div class="row">
                                      				<section class="col col-4">
                                            			<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.dos" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-pencil"></i></span>
															<label class="input">
																<input class="form-control date-picker" data-date-end-date="+0d" data-date-start-date="-30d" 
																	type="text" name="fechaInicioSintomas" id="fechaInicioSintomas" value="<fmt:formatDate value="${daSindFeb.fechaInicioSintomas}" pattern="dd/MM/yyyy" />"
																	placeholder=" <spring:message code="act.enter" /> <spring:message code="sindfeb.dos" />">
																	<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.dos" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-calendar fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-4">
                                            			<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.dst" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-pencil"></i></span>
															<label class="input">
																<input class="form-control date-picker" data-date-end-date="+0d" data-date-start-date="-30d" 
																	type="text" name="fechaTomaMuestra" id="fechaTomaMuestra" value="<fmt:formatDate value="${daSindFeb.fechaTomaMuestra}" pattern="dd/MM/yyyy" />"
																	placeholder=" <spring:message code="act.enter" /> <spring:message code="sindfeb.dst" />">
																	<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.dst" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-calendar fa-fw"></i></span>
														</div>
                                            		</section>
                                      				</div>
                                      				<!-- END ROW -->
                                      				<!-- START ROW -->
                                      				<div class="row">
                                      				<section class="col col-4">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.temp" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="temperatura" id="temperatura" 
																	value="${daSindFeb.temperatura}" 
																	placeholder=" <spring:message code="sindfeb.temp" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.temp" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-numeric-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-4">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.pas" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="pas" id="pas" 
																	value="${daSindFeb.pas}" 
																	placeholder=" <spring:message code="sindfeb.pas" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.pas" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-numeric-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-4">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.pad" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="pad" id="pad" 
																	value="${daSindFeb.pad}" 
																	placeholder=" <spring:message code="sindfeb.pad" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.pad" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-numeric-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                      				</div>
                                      				<!-- END ROW -->
                                      				<!-- START ROW -->
                                      				<div class="row">
                                      				<section class="col col-4">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.ssa" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.ssa" />" name="ssDSA" id="ssDSA" multiple style="width:100%" class="select2">
																<option value=""></option>
																<c:forEach items="${sintDssa}" var="sint">
																	<c:choose> 
																		<c:when test="${fn:contains(daSindFeb.ssDSA, sint.codigo)}">
																			<option selected value="${sint.codigo}">${sint.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${sint.codigo}">${sint.valor}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		<section class="col col-4">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.sca" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.sca" />" name="ssDCA" id="ssDCA" multiple style="width:100%" class="select2">
																<option value=""></option>
																<c:forEach items="${sintDcsa}" var="sint">
																	<c:choose> 
																		<c:when test="${fn:contains(daSindFeb.ssDCA, sint.codigo)}">
																			<option selected value="${sint.codigo}">${sint.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${sint.codigo}">${sint.valor}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		<section class="col col-4">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.dg" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.dg" />" name="ssDS" id="ssDS" multiple style="width:100%" class="select2">
																<option value=""></option>
																<c:forEach items="${sintDgra}" var="sint">
																	<c:choose> 
																		<c:when test="${fn:contains(daSindFeb.ssDS, sint.codigo)}">
																			<option selected value="${sint.codigo}">${sint.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${sint.codigo}">${sint.valor}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                      				</div>
                                      				<!-- END ROW -->
                                      				<!-- START ROW -->
                                      				<div class="row">
                                      				<section class="col col-4">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.lept" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.lept" />" name="ssLepto" id="ssLepto" multiple style="width:100%" class="select2">
																<option value=""></option>
																<c:forEach items="${sintLept}" var="sint">
																	<c:choose> 
																		<c:when test="${fn:contains(daSindFeb.ssLepto, sint.codigo)}">
																			<option selected value="${sint.codigo}">${sint.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${sint.codigo}">${sint.valor}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		<section class="col col-4">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.hanta" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.hanta" />" name="ssHV" id="ssHV" multiple style="width:100%" class="select2">
																<option value=""></option>
																<c:forEach items="${sintHant}" var="sint">
																	<c:choose> 
																		<c:when test="${fn:contains(daSindFeb.ssHV, sint.codigo)}">
																			<option selected value="${sint.codigo}">${sint.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${sint.codigo}">${sint.valor}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		<section class="col col-4">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.chik" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-location-arrow"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.chik" />" name="ssCK" id="ssCK" multiple style="width:100%" class="select2">
																<option value=""></option>
																<c:forEach items="${sintChik}" var="sint">
																	<c:choose> 
																		<c:when test="${fn:contains(daSindFeb.ssCK, sint.codigo)}">
																			<option selected value="${sint.codigo}">${sint.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${sint.codigo}">${sint.valor}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                      				</div>
                                      				<!-- END ROW -->
                                      				<!-- START ROW -->
                                      				<div class="row">
                                      				<section class="col col-3">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.hosp" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-hospital-o fa-fw"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.hosp" />" name="hosp" id="hosp" class="select2">
																<option value=""></option>
																<c:forEach items="${catResp}" var="cresp">
																	<c:choose> 
																		<c:when test="${cresp.codigo eq daSindFeb.hosp.codigo}">
																			<option selected value="${cresp.codigo}">${cresp.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${cresp.codigo}">${cresp.valor}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.hosp.date" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-pencil"></i></span>
															<label class="input">
																<input class="form-control date-picker" data-date-end-date="+0d" data-date-start-date="-30d" 
																	type="text" name="fechaIngreso" id="fechaIngreso" value="<fmt:formatDate value="${daSindFeb.fechaIngreso}" pattern="dd/MM/yyyy" />"
																	placeholder=" <spring:message code="act.enter" /> <spring:message code="sindfeb.hosp.date" />">
																	<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.hosp.date" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-calendar fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                                		<i class="fa fa-fw fa-asterisk txt-color-red font-sm hidden-xs"></i>
                                                		<label class="text-left txt-color-blue font-md hidden-xs">
                                                    		<spring:message code="sindfeb.dead" />
                                                    	</label>
                                                    	<div class="input-group">
                                                    		<span class="input-group-addon"> <i class="fa fa-plus-square fa-fw"></i></span>
	                                                    	<select data-placeholder="<spring:message code="act.select" /> <spring:message code="sindfeb.dead" />" name="fallecido" id="fallecido" class="select2">
																<option value=""></option>
																<c:forEach items="${catResp}" var="cresp">
																	<c:choose> 
																		<c:when test="${cresp.codigo eq daSindFeb.fallecido.codigo}">
																			<option selected value="${cresp.codigo}">${cresp.nombre}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${cresp.codigo}">${cresp.valor}</option>
																		</c:otherwise>
																	</c:choose> 
																</c:forEach>
															</select>
														</div>
                                            		</section>
                                            		<section class="col col-3">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.dead.date" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-pencil"></i></span>
															<label class="input">
																<input class="form-control date-picker" data-date-end-date="+0d" data-date-start-date="-30d" 
																	type="text" name="fechaFallecido" id="fechaFallecido" value="<fmt:formatDate value="${daSindFeb.fechaFallecido}" pattern="dd/MM/yyyy" />"
																	placeholder=" <spring:message code="act.enter" /> <spring:message code="sindfeb.dead.date" />">
																	<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.dead.date" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-calendar fa-fw"></i></span>
														</div>
                                            		</section>
                                      				</div>
                                      				<!-- END ROW -->
                                      				<!-- START ROW -->
                                      				<div class="row">
                                      				<section class="col col-4">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.pre.dx" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-user-md fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="dxPresuntivo" id="dxPresuntivo" 
																	value="${daSindFeb.dxPresuntivo}" 
																	placeholder=" <spring:message code="sindfeb.pre.dx" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.pre.dx" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-4">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.final.dx" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-user-md fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="dxFinal" id="dxFinal" 
																	value="${daSindFeb.dxFinal}" 
																	placeholder=" <spring:message code="sindfeb.final.dx" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.final.dx" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                            		<section class="col col-4">
                                            			<label class="text-left txt-color-blue font-md hidden-xs">
                                            				<spring:message code="sindfeb.llenoficha" />
                                            			</label>
                                            			<div class="input-group">
															<span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
															<label class="input">
																<input class="form-control" type="text" name="nombreLlenoFicha" id="nombreLlenoFicha" 
																	value="${daSindFeb.nombreLlenoFicha}" 
																	placeholder=" <spring:message code="sindfeb.llenoficha" />">
																<b class="tooltip tooltip-top-left"> <i class="fa fa-info"></i> <spring:message code="sindfeb.llenoficha" /></b>
															</label>
															<span class="input-group-addon"><i class="fa fa-sort-alpha-asc fa-fw"></i></span>
														</div>
                                            		</section>
                                      				</div>
                                      				<!-- END ROW -->
												</fieldset>
											</div>
											<div class="step-pane" id="step5">
												<h3><spring:message code="sindfeb.step5.long" /></h3>
												<h4><spring:message code="lbl.step2.short" />: ${daSindFeb.idNotificacion.persona.primerNombre} ${daSindFeb.idNotificacion.persona.primerApellido}</h4>
												<fieldset>
											        <div class="table-responsive">
											            <table class="table table-striped table-hover table-bordered" id="lista_resultados">
											                <thead>
											                <tr>
											                    <th><spring:message code="lbl.test"/></th>
											                    <th><spring:message code="lbl.result"/></th>
											                    <th><spring:message code="lbl.laboratory"/></th>
											                    <th><spring:message code="lbl.result.date"/></th>
											                </tr>
											                </thead>
											            </table>
											        </div>
											    </fieldset>
											</div>
										</form>
									</div>
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
	<script src="${jQValidationLoc}"/></script>
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
	<spring:url value="/resources/scripts/sindfeb/enterForm.js" var="enterFormSindFeb" />
	<script src="${enterFormSindFeb}"></script>
	<spring:url value="/resources/scripts/utilidades/handleDatePickers.js" var="handleDatePickers" />
	<script src="${handleDatePickers}"></script>
	<!-- script calcular edad -->
	<spring:url value="/resources/scripts/utilidades/calcularEdad.js" var="calculateAge" />
	<script src="${calculateAge}"></script>
	<!-- END PAGE LEVEL SCRIPTS -->
	<!-- PARAMETROS URL -->
	<spring:url var="municipiosURL" value="/api/v1/municipiosbysilais"/>
	<spring:url var="unidadesUrl"   value="/api/v1/unidadesPrimHosp"  />
	<spring:url var="municipioByDepaUrl" value="/api/v1/municipio" />
	<spring:url var="comunidadUrl" value="/api/v1/comunidad"/>
	<!-- PARAMETROS LENGUAJE -->
	<c:set var="blockMess"><spring:message code="blockUI.message" /></c:set>
	<script type="text/javascript">
		$(document).ready(function() {
			pageSetUp();
			
			var parametros = {
                    municipiosUrl:"${municipiosURL}",
                    unidadesUrl: "${unidadesUrl}",municipioByDepaUrl: "${municipioByDepaUrl}",comunidadUrl: "${comunidadUrl}",
                    blockMess:"${blockMess}"
 			 };
			EnterFormSindFeb.init(parametros);
			SeleccionUnidad.init(parametros);
			handleDatePickers("${pageContext.request.locale.language}");
	    	$("li.notificacion").addClass("open");
	    	$("li.sindfeb").addClass("active");
	    	if("top"!=localStorage.getItem("sm-setmenu")){
	    		$("li.sindfeb").parents("ul").slideDown(200);
	    	}
	    	$('#fechaNacimiento').change();
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>