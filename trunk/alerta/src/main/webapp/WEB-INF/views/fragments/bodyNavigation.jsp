<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Left panel : Navigation area -->
<!-- Note: This width of the aside area can be adjusted through LESS variables -->
<%@ page import="ni.gob.minsa.alerta.service.SeguridadService" %>
<%
    SeguridadService seguridadService = new SeguridadService();
    boolean seguridadHabilitada = seguridadService.seguridadHabilitada();
%>
<aside id="left-panel">
    <!-- User info -->
    <div class="login-info">
	<span> <!-- User image size is adjusted inside CSS, it should stay as it -->
		<spring:url value="/resources/img/user.png" var="user" />
		<a href="javascript:void(0);" id="show-shortcut" data-action="toggleShortcut">
            <img src="${user}" alt="<spring:message code="lbl.user" />" class="online" />
			<span>
            <%if (seguridadHabilitada) {%>
				<%=seguridadService.obtenerNombreUsuario(request)%>
            <% } else { %>
                <spring:message code="lbl.user" />
            <% } %>
			</span>
            <i class="fa fa-angle-down"></i>
        </a>

	</span>
    </div>
    <!-- end user info -->
    <!-- NAVIGATION : This navigation is also responsive
    To make this navigation dynamic please make sure to link the node
    (the reference to the nav > ul) after page load. Or the navigation
    will not initialize.
    -->
    <nav>
        <!-- NOTE: Notice the gaps after each icon usage <i></i>..
        Please note that these links work a bit different than
        traditional href="" links. See documentation for details.
        -->

        <ul>
            <%if (seguridadHabilitada) {%>
            <%=seguridadService.obtenerMenu(request)%>
            <% } else { %>
            <li class="home">
                <a href="<spring:url value="/" htmlEscape="true "/>" title="<spring:message code="menu.home" />"><i class="fa fa-lg fa-fw fa-home"></i> <span class="menu-item-parent"><spring:message code="menu.home" /></span></a>
            </li>
            <li class="mantenimiento">
                <a href="#" title="<spring:message code="menu.maint" />"><i class="fa fa-lg fa-fw fa-cogs"></i> <span class="menu-item-parent"><spring:message code="menu.maint" /></span></a>
                <ul>
                    <li class="personas">
                        <a href="<spring:url value="/personas/search" htmlEscape="true "/>" title="<spring:message code="menu.persons" />"><i class="fa fa-lg fa-fw fa-group"></i> <spring:message code="menu.persons" /></a>
                    </li>
                </ul>
            </li>
            <li class="notificacion">
                <a href="#" title="<spring:message code="menu.notif" />"><i class="fa fa-lg fa-fw fa-rss"></i> <span class="menu-item-parent"><spring:message code="menu.notif" /></span></a>
                <ul>
                    <li class="sindfeb">
                        <a href="<spring:url value="/febriles/create" htmlEscape="true "/>" title="<spring:message code="menu.sindfeb" />"><i class="fa fa-lg fa-fw fa-fire"></i> <spring:message code="menu.sindfeb" /></a>
                    </li>
                    <li class="irageti">
                        <a href="<spring:url value="/irag/create" htmlEscape="true "/>" title="<spring:message code="menu.irageti" />"><i class="fa fa-lg fa-fw fa-stethoscope"></i> <spring:message code="menu.irageti" /></a>
                    </li>
                    <li class="vih">
                        <a href="<spring:url value="/vih/search" htmlEscape="true "/>" title="<spring:message code="menu.vih" />"><i class="fa fa-lg fa-fw fa-flask"></i> <spring:message code="menu.vih" /></a>
                    </li>
                </ul>
            </li>
            <li class="entomologia">
                <a href="#" title="<spring:message code="menu.ento" />"><i class="fa fa-lg fa-fw fa-bug"></i> <span class="menu-item-parent"><spring:message code="menu.ento" /></span></a>
                <ul>
                    <li class="entoadd">
                        <a href="#" title="<spring:message code="menu.ento.add" />"><i class="fa fa-lg fa-fw fa-pencil"></i> <spring:message code="menu.ento.add" /></a>
                        <ul>
                            <li class="entoaddaedes">
                                <a href="<spring:url value="/encuesta/create/aedes" htmlEscape="true "/>" title="<spring:message code="menu.ento.add.aedes" />"><i class="fa fa-lg fa-fw fa-bug"></i> <spring:message code="menu.ento.add.aedes" /></a>
                            </li>
                            <li class="entoaddlarvae">
                                <a href="<spring:url value="/encuesta/create/larvae" htmlEscape="true "/>" title="<spring:message code="menu.ento.add.larvae" />"><i class="fa fa-lg fa-fw fa-vine"></i> <spring:message code="menu.ento.add.larvae" /></a>
                            </li>
                            <li class="entoadddep">
                                <a href="<spring:url value="/encuesta/create/dep" htmlEscape="true "/>" title="<spring:message code="menu.ento.add.dep" />"><i class="fa fa-lg fa-fw fa-archive"></i> <spring:message code="menu.ento.add.dep" /></a>
                            </li>
                        </ul>
                    </li>
                    <li class="entosearch">
                        <a href="<spring:url value="/encuesta/search" htmlEscape="true "/>" title="<spring:message code="menu.ento.search" />"><i class="fa fa-lg fa-fw fa-search"></i> <spring:message code="menu.ento.search" /></a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="<spring:url value="/logout" htmlEscape="true "/>"> <i class="fa fa-lg fa-fw fa-sign-out"></i> <span class="menu-item-parent"><spring:message code="menu.logout" /></span></a>
            </li>
            <% } %>
        </ul>

    </nav>

<span class="minifyme" data-action="minifyMenu">
	<i class="fa fa-arrow-circle-left hit"></i>
</span>
</aside>
<!-- END NAVIGATION -->