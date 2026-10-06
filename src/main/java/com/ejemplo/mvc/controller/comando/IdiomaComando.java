package com.ejemplo.mvc.controller.comando;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Comando para cambiar la preferencia de idioma del usuario.
 * Almacena el código de idioma ("es" o "en") en una Cookie persistente por 30 días.
 */
public class IdiomaComando implements Comando {

    private static final int COOKIE_MAX_AGE_SECONDS = 30 * 24 * 60 * 60; // 30 días

    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lang = req.getParameter("lang");

        if (lang != null && ("es".equalsIgnoreCase(lang) || "en".equalsIgnoreCase(lang))) {
            Cookie idiomaCookie = new Cookie("idiomaPreferido", lang.toLowerCase());
            idiomaCookie.setMaxAge(COOKIE_MAX_AGE_SECONDS);
            idiomaCookie.setPath("/");
            resp.addCookie(idiomaCookie);
        }

        String referer = req.getHeader("Referer");
        if (referer != null && !referer.trim().isEmpty()) {
            resp.sendRedirect(referer);
        } else {
            resp.sendRedirect(req.getContextPath() + "/app");
        }
        return null;
    }
}
