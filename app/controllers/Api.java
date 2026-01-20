package controllers;

import com.google.gson.Gson;
import models.Artista;
import play.mvc.Controller;

import java.util.HashMap;
import java.util.Map;

public class Api extends Controller {

    private static final Gson gson = new Gson();

    public static void ping() {
        response.contentType = "application/json";
        renderJSON("{\"ok\":true}");
    }

    public static void login() {
        System.out.println(">>> Conexion recibida desde Android");
        String raw = params.current().get("body");
        if (raw == null || raw.trim().isEmpty()) {
            response.status = 400;
            renderJSON("{\"error\":\"Body vacío\"}");
            return;
        }

        Map body;
        try {
            Gson gson = new Gson();
            body = gson.fromJson(raw, Map.class);
        } catch (Exception e) {
            response.status = 400;
            renderJSON("{\"error\":\"JSON inválido\"}");
            return;
        }

        if (body == null) {
            response.status = 400;
            renderJSON("{\"error\":\"JSON inválido\"}");
            return;
        }

        String nombre = body.get("nombre") != null ? body.get("nombre").toString() : null;
        String password = body.get("password") != null ? body.get("password").toString() : null;

        if (nombre == null || nombre.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.status = 400;
            renderJSON("{\"error\":\"Faltan campos\"}");
            return;
        }

        Artista artista = Artista.find("byNombre", nombre).first();
        if (artista == null || artista.password == null || !artista.password.equals(password)) {
            response.status = 401;
            renderJSON("{\"error\":\"Credenciales incorrectas\"}");
            return;
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", artista.id);
        result.put("nombre_artistico", artista.nombre_artistico);
        result.put("nombre", artista.nombre);

        String token = java.util.UUID.randomUUID().toString();
        artista.webToken = token;
        artista.save();
        result.put("token", token);

        System.out.println("Log In correcto");
        renderJSON(new Gson().toJson(result));
    }

    public static void register() {
        System.out.println(">>> REGISTER API llamado desde: " + request.remoteAddress);

        String raw = params.current().get("body");
        if (raw == null || raw.trim().isEmpty()) {
            response.status = 400;
            renderJSON("{\"error\":\"Body vacío\"}");
            return;
        }

        Map body;
        Gson gson = new Gson();
        try {
            body = gson.fromJson(raw, Map.class);
        } catch (Exception e) {
            response.status = 400;
            renderJSON("{\"error\":\"JSON inválido\"}");
            return;
        }

        if (body == null) {
            response.status = 400;
            renderJSON("{\"error\":\"JSON inválido\"}");
            return;
        }

        String nombreArtistico = body.get("nombre_artistico") != null ? body.get("nombre_artistico").toString() : null;
        String nombre = body.get("nombre") != null ? body.get("nombre").toString() : null;
        String password = body.get("password") != null ? body.get("password").toString() : null;

        Integer edad = null;
        try {
            if (body.get("edad") != null) {
                Object v = body.get("edad");
                if (v instanceof Number) edad = ((Number) v).intValue();
                else edad = Integer.parseInt(v.toString());
            }
        } catch (Exception ignored) {
            edad = null;
        }

        if (nombreArtistico == null || nombreArtistico.trim().isEmpty()
                || nombre == null || nombre.trim().isEmpty()
                || password == null || password.trim().isEmpty()
                || edad == null) {
            response.status = 400;
            renderJSON("{\"error\":\"Faltan campos\"}");
            return;
        }

        Artista existente = Artista.find("byNombre", nombre).first();
        if (existente != null) {
            response.status = 409; // Conflict
            renderJSON("{\"error\":\"El usuario ya existe\"}");
            return;
        }

        Artista a = new Artista(nombreArtistico, nombre, password, edad);
        a.save();
        System.out.println("Register correcto");

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", a.id);
        result.put("nombre_artistico", a.nombre_artistico);
        result.put("nombre", a.nombre);

        response.status = 201;
        renderJSON(gson.toJson(result));
    }

}

