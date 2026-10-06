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
    <title><fmt:message key="login.titulo"/> - ${initParam['app.nombre']}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <div class="auth-wrapper">
        <div class="auth-card">
            <!-- Selector de Idioma en la esquina superior -->
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                <div class="brand">
                    <span class="brand-icon">✓</span>
                    <span>Gestor MVC</span>
                </div>
                <div class="lang-selector">
                    <a href="${pageContext.request.contextPath}/app?comando=idioma&lang=es" 
                       class="lang-btn ${(empty cookie.idiomaPreferido or cookie.idiomaPreferido.value == 'es') ? 'active' : ''}">ES</a>
                    <a href="${pageContext.request.contextPath}/app?comando=idioma&lang=en" 
                       class="lang-btn ${cookie.idiomaPreferido.value == 'en' ? 'active' : ''}">EN</a>
                </div>
            </div>

            <div class="auth-header">
                <h1 class="auth-title"><fmt:message key="login.titulo"/></h1>
                <p class="auth-subtitle">Ingresa tus credenciales para acceder a tus tareas</p>
            </div>

            <!-- Mensaje de error si la autenticación falla -->
            <c:if test="${not empty errorLogin}">
                <div class="alert-error" id="errorLoginAlert">
                    <strong>Error: </strong><c:out value="${errorLogin}"/>
                </div>
            </c:if>

            <!-- Formulario de Login -->
            <form action="${pageContext.request.contextPath}/app" method="post">
                <input type="hidden" name="comando" value="login"/>

                <div class="form-group">
                    <label for="username" class="form-label"><fmt:message key="login.usuario"/></label>
                    <input type="text" 
                           id="username" 
                           name="username" 
                           class="input-field" 
                           value="<c:out value='${username}'/>" 
                           placeholder="admin / maria" 
                           required 
                           autofocus/>
                </div>

                <div class="form-group">
                    <label for="clave" class="form-label"><fmt:message key="login.clave"/></label>
                    <input type="password" 
                           id="clave" 
                           name="clave" 
                           class="input-field" 
                           placeholder="••••••••" 
                           required/>
                </div>

                <div style="margin-top: 28px;">
                    <button type="submit" class="btn btn-primary" style="width: 100%; padding: 12px;">
                        <fmt:message key="login.entrar"/>
                    </button>
                </div>
            </form>

            <!-- Credenciales de prueba -->
            <div class="credentials-box">
                <strong>Credenciales de demostración:</strong><br>
                • Administrador: <code>admin</code> / <code>Admin123!</code> (Rol ADMIN)<br>
                • Usuario regular: <code>maria</code> / <code>Maria2026!</code> (Rol USER)
            </div>
        </div>
    </div>
</body>
</html>
