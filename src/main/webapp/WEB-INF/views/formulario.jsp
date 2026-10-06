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
    <title>${initParam['app.nombre']} - ${not empty tarea ? 'Editar Tarea' : 'Nueva Tarea'}</title>
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
                <div class="lang-selector">
                    <a href="${pageContext.request.contextPath}/app?comando=idioma&lang=es" 
                       class="lang-btn ${(empty cookie.idiomaPreferido or cookie.idiomaPreferido.value == 'es') ? 'active' : ''}">ES</a>
                    <a href="${pageContext.request.contextPath}/app?comando=idioma&lang=en" 
                       class="lang-btn ${cookie.idiomaPreferido.value == 'en' ? 'active' : ''}">EN</a>
                </div>

                <div class="user-info">
                    <span><fmt:message key="lista.bienvenida"/>, <strong><c:out value="${sessionScope.usuarioActual.nombreCompleto}"/></strong></span>
                    <span class="badge badge-rol badge-${sessionScope.usuarioActual.rol.toLowerCase()}">
                        <c:out value="${sessionScope.usuarioActual.rol}"/>
                    </span>
                </div>

                <a href="${pageContext.request.contextPath}/app?comando=logout" class="btn btn-sm btn-outline-danger">
                    <fmt:message key="lista.salir"/>
                </a>
            </div>
        </div>
    </header>

    <!-- Contenedor del Formulario -->
    <main class="app-container" style="max-width: 720px;">
        <div class="card">
            <div class="card-header">
                <div>
                    <h1 class="card-title">
                        <c:choose>
                            <c:when test="${not empty tarea.id or not empty id}">Editar Tarea</c:when>
                            <c:otherwise>Nueva Tarea</c:otherwise>
                        </c:choose>
                    </h1>
                    <p style="color: var(--text-muted); font-size: 0.875rem;">Completa la información requerida para registrar la tarea</p>
                </div>
                <a href="${pageContext.request.contextPath}/app?comando=listar" class="btn btn-secondary btn-sm">
                    ← Volver al Listado
                </a>
            </div>

            <!-- Bloque de Errores Generales -->
            <c:if test="${not empty errores}">
                <div class="alert-error" id="bloqueErrores">
                    <strong>Por favor corrige los siguientes errores de validación:</strong>
                    <ul>
                        <c:forEach var="entry" items="${errores}">
                            <li><c:out value="${entry.value}"/></li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>

            <!-- Variables auxiliares para repoblación de datos -->
            <c:set var="valId" value="${not empty id ? id : (not empty tarea ? tarea.id : '')}"/>
            <c:set var="valTitulo" value="${not empty titulo ? titulo : (not empty tarea ? tarea.titulo : '')}"/>
            <c:set var="valCategoria" value="${not empty categoria ? categoria : (not empty tarea ? tarea.categoria : '')}"/>
            <c:set var="valPrioridad" value="${not empty prioridad ? prioridad : (not empty tarea ? tarea.prioridad : '')}"/>
            <c:set var="valFechaLimite">
                <c:choose>
                    <c:when test="${not empty fechaLimite}"><c:out value="${fechaLimite}"/></c:when>
                    <c:when test="${not empty tarea.fechaLimite}"><fmt:formatDate value="${tarea.fechaLimite}" pattern="yyyy-MM-dd"/></c:when>
                    <c:otherwise></c:otherwise>
                </c:choose>
            </c:set>

            <!-- Formulario Principal -->
            <form action="${pageContext.request.contextPath}/app" method="post" novalidate>
                <input type="hidden" name="comando" value="guardar"/>
                <input type="hidden" name="id" value="<c:out value='${valId}'/>"/>

                <!-- Campo Título -->
                <div class="form-group">
                    <label for="titulo" class="form-label">
                        <fmt:message key="lista.titulo"/> * 
                        <span style="font-weight: 400; color: var(--text-muted); font-size: 0.8rem;">
                            (Máximo ${not empty applicationScope.maxLongitudTitulo ? applicationScope.maxLongitudTitulo : '80'} caracteres)
                        </span>
                    </label>
                    <input type="text" 
                           id="titulo" 
                           name="titulo" 
                           class="input-field ${not empty errores.titulo ? 'input-error' : ''}" 
                           value="<c:out value='${valTitulo}'/>" 
                           placeholder="Ej. Realizar pruebas unitarias del módulo MVC"
                           maxlength="${not empty applicationScope.maxLongitudTitulo ? applicationScope.maxLongitudTitulo : 80}"
                           autofocus/>
                    <c:if test="${not empty errores.titulo}">
                        <span class="campo-error" id="error-titulo"><c:out value="${errores.titulo}"/></span>
                    </c:if>
                </div>

                <!-- Campo Categoría -->
                <div class="form-group">
                    <label for="categoria" class="form-label"><fmt:message key="lista.categoria"/> *</label>
                    <input type="text" 
                           id="categoria" 
                           name="categoria" 
                           class="input-field ${not empty errores.categoria ? 'input-error' : ''}" 
                           value="<c:out value='${valCategoria}'/>" 
                           placeholder="Ej. Backend, Frontend, Infraestructura, Documentación"/>
                    <c:if test="${not empty errores.categoria}">
                        <span class="campo-error" id="error-categoria"><c:out value="${errores.categoria}"/></span>
                    </c:if>
                </div>

                <!-- Campo Prioridad -->
                <div class="form-group">
                    <label for="prioridad" class="form-label"><fmt:message key="lista.prioridad"/> *</label>
                    <select id="prioridad" 
                            name="prioridad" 
                            class="input-field ${not empty errores.prioridad ? 'input-error' : ''}">
                        <option value="" ${empty valPrioridad ? 'selected' : ''}>-- Seleccionar prioridad --</option>
                        <option value="Alta" ${valPrioridad == 'Alta' ? 'selected' : ''}>Alta</option>
                        <option value="Media" ${valPrioridad == 'Media' ? 'selected' : ''}>Media</option>
                        <option value="Baja" ${valPrioridad == 'Baja' ? 'selected' : ''}>Baja</option>
                    </select>
                    <c:if test="${not empty errores.prioridad}">
                        <span class="campo-error" id="error-prioridad"><c:out value="${errores.prioridad}"/></span>
                    </c:if>
                </div>

                <!-- Campo Fecha Límite -->
                <div class="form-group">
                    <label for="fechaLimite" class="form-label"><fmt:message key="lista.fecha"/> * (yyyy-MM-dd)</label>
                    <input type="date" 
                           id="fechaLimite" 
                           name="fechaLimite" 
                           class="input-field ${not empty errores.fechaLimite ? 'input-error' : ''}" 
                           value="<c:out value='${valFechaLimite}'/>"/>
                    <c:if test="${not empty errores.fechaLimite}">
                        <span class="campo-error" id="error-fechaLimite"><c:out value="${errores.fechaLimite}"/></span>
                    </c:if>
                </div>

                <!-- Acciones del Formulario -->
                <div style="display: flex; gap: 12px; margin-top: 32px; justify-content: flex-end;">
                    <a href="${pageContext.request.contextPath}/app?comando=listar" class="btn btn-secondary">
                        Cancelar
                    </a>
                    <button type="submit" class="btn btn-primary">
                        Guardar Tarea
                    </button>
                </div>
            </form>
        </div>
    </main>
</body>
</html>
