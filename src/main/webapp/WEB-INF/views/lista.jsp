<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="${not empty cookie.idiomaPreferido ? cookie.idiomaPreferido.value : 'es'}"/>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="${not empty cookie.idiomaPreferido ? cookie.idiomaPreferido.value : 'es'}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${initParam['app.nombre']} - Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <!-- Barra de Navegación Superior -->
    <header class="navbar">
        <div class="navbar-inner">
            <a href="${pageContext.request.contextPath}/app?comando=listar" class="brand">
                <span class="brand-icon">✓</span>
                <span>${initParam['app.nombre']}</span>
            </a>

            <div class="nav-user-panel">
                <!-- Selector de Idioma -->
                <div class="lang-selector">
                    <a href="${pageContext.request.contextPath}/app?comando=idioma&lang=es" 
                       class="lang-btn ${(empty cookie.idiomaPreferido or cookie.idiomaPreferido.value == 'es') ? 'active' : ''}">ES</a>
                    <a href="${pageContext.request.contextPath}/app?comando=idioma&lang=en" 
                       class="lang-btn ${cookie.idiomaPreferido.value == 'en' ? 'active' : ''}">EN</a>
                </div>

                <!-- Saludo e Información de Sesión del Usuario -->
                <div class="user-info">
                    <span><fmt:message key="lista.bienvenida"/>, <strong><c:out value="${sessionScope.usuarioActual.nombreCompleto}"/></strong></span>
                    <span class="badge badge-rol badge-${sessionScope.usuarioActual.rol.toLowerCase()}">
                        <c:out value="${sessionScope.usuarioActual.rol}"/>
                    </span>
                </div>

                <!-- Botón de Cerrar Sesión -->
                <a href="${pageContext.request.contextPath}/app?comando=logout" class="btn btn-sm btn-outline-danger">
                    <fmt:message key="lista.salir"/>
                </a>
            </div>
        </div>
    </header>

    <!-- Contenido Principal -->
    <main class="app-container">
        <div class="card">
            <div class="card-header">
                <div>
                    <h1 class="card-title">Listado de Tareas</h1>
                    <p style="color: var(--text-muted); font-size: 0.875rem;">Administra las tareas asignadas y su estado de ejecución</p>
                </div>
                <!-- Enlace para Crear Nueva Tarea -->
                <a href="${pageContext.request.contextPath}/app?comando=formulario" class="btn btn-primary">
                    <fmt:message key="lista.nueva"/>
                </a>
            </div>

            <!-- Alerta de Error (ej. Restricción de Autorización por Rol) -->
            <c:if test="${not empty error}">
                <div class="alert-error" id="alertaErrorRol">
                    <strong>Aviso de Seguridad: </strong><c:out value="${error}"/>
                </div>
            </c:if>

            <!-- Tabla de Tareas -->
            <div class="table-responsive">
                <table class="task-table">
                    <thead>
                        <tr>
                            <th><fmt:message key="lista.titulo"/></th>
                            <th><fmt:message key="lista.categoria"/></th>
                            <th><fmt:message key="lista.prioridad"/></th>
                            <th><fmt:message key="lista.fecha"/></th>
                            <th><fmt:message key="lista.estado"/></th>
                            <th style="text-align: right;"><fmt:message key="lista.acciones"/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty tareas}">
                                <c:forEach var="t" items="${tareas}">
                                    <tr class="${t.completada ? 'task-completed' : ''}" id="tarea-${t.id}">
                                        <td>
                                            <div class="task-title"><c:out value="${t.titulo}"/></div>
                                        </td>
                                        <td>
                                            <span class="badge badge-cat"><c:out value="${t.categoria}"/></span>
                                        </td>
                                        <td>
                                            <span class="badge badge-prio badge-prio-${t.prioridad.toLowerCase()}">
                                                <c:out value="${t.prioridad}"/>
                                            </span>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${t.fechaLimite}" pattern="yyyy-MM-dd"/>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${t.completada}">
                                                    <span class="badge badge-success">Completada</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-warning">Pendiente</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <div class="actions-cell" style="justify-content: flex-end;">
                                                <!-- Botón Completar (solo si está pendiente) -->
                                                <c:if test="${not t.completada}">
                                                    <a href="${pageContext.request.contextPath}/app?comando=completar&id=${t.id}" 
                                                       class="btn btn-xs btn-success"
                                                       title="Marcar como completada">
                                                        <fmt:message key="lista.completar"/>
                                                    </a>
                                                </c:if>

                                                <!-- Botón Eliminar -->
                                                <a href="${pageContext.request.contextPath}/app?comando=eliminar&id=${t.id}" 
                                                   class="btn btn-xs btn-danger"
                                                   onclick="return confirm('¿Está seguro de que desea eliminar esta tarea?');"
                                                   title="Eliminar tarea">
                                                    <fmt:message key="lista.eliminar"/>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6" style="text-align: center; padding: 32px; color: var(--text-muted);">
                                        No hay tareas registradas en este momento.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</body>
</html>
