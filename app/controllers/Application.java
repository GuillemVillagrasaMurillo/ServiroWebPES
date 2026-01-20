package controllers;

import play.*;
import play.mvc.*;
import java.util.*;
import models.*;

public class Application extends Controller {

// ----------------------------------------------------------------------------------
//                                  Usuario
// ----------------------------------------------------------------------------------
    public static void index()
    {
        render("Application/Inicio.html");
    }

    public static void doLogIn(String n2, String p) {

        Artista usuario = Artista.find("byNombre", n2).first();

        if (n2 == null || n2.trim().isEmpty()) {
            flash.put("errorUser", "El nom és obligatori");
            login();
            return;
        }

        if (p == null || p.trim().isEmpty()) {
            flash.put("errorPass", "La contrasenya és obligatòria");
            login();
            return;
        }

        if (usuario == null) {
            flash.put("errorUser", "Usuario no encontrado");
            login();
            return;
        }

        if (!usuario.password.equals(p)) {
            flash.put("errorPass", "Contraseña incorrecta");
            login();
            return;
        }

        session.put("username", usuario.nombre);
        redirect("/");
    }

    public static void login()
    {
        render("Application/Login.html");
    }

    public static void doRegister(String nombre_artistico, String nombre, String password, Integer edad) {

        Artista existente = Artista.find("byNombre_artistico", nombre_artistico).first();

        if (nombre_artistico == null || nombre_artistico.trim().isEmpty()) {
            flash.put("errorNombreArtistico", "El nom artístic és obligatori");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            flash.put("errorNombre", "El nom és obligatori");
        }

        if (password == null || password.trim().isEmpty()) {
            flash.put("errorPassword", "La contrasenya és obligatòria");
        }

        if (edad == null) {
            flash.put("errorEdad", "L'edat és obligatòria");
        }

        if (flash.get("errorNombreArtistico") != null ||
                flash.get("errorNombre") != null ||
                flash.get("errorPassword") != null ||
                flash.get("errorEdad") != null) {

            registre();
            return;
        }

        if (existente != null) {
            flash.put("errorNombreArtistico", "Este nombre artístico ya está en uso");
            registre();
            return;
        }

        new Artista(nombre_artistico, nombre, password, edad).save();

        redirect("/login");
    }

    public static void registre()
    {
        render("Application/Registre.html");
    }

    public static void logout() {
        session.clear();
        index();
    }

    public static void artistas() {
        List<Artista> artistas = Artista.findAll();
        render(artistas);
    }

    public static void conciertos() {
        List<Concierto> conciertos = Concierto.find("order by fecha asc").fetch();
        render(conciertos);
    }

    public static void salas() {
        List<Sala> salas = Sala.findAll();
        render(salas);
    }

    public static void fromToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            redirect("/login");
        }

        Artista artista = Artista.find("byWebToken", token).first();
        if (artista == null) {
            redirect("/login");
        }

        session.put("username", artista.nombre);

        redirect("/");
    }



// ----------------------------------------------------------------------------------
//                                      Administrador
// ----------------------------------------------------------------------------------
    //Para iniciar sesión como administrador ---> http://localhost:9000/admin/login
    //Usuario admin: admin
    //Contraseña: admin123
    //Con administrador se puede acceder a las 3 tablas (artístas, salas, conciertos) y hacer las modificaciones deseadas (crear, editar, eliminar) a parte de poder ver la tabla completa

    public static void adminLogin() {
        render("Application/AdminLogin.html");
    }

    public static void doAdminLogin(String u, String p) {

        flash.put("adminUserValue", u);

        Admin admin = Admin.find("byUsername", u).first();

        if (admin == null) {
            flash.put("errorAdminUser", "Administrador no encontrado");
            adminLogin();
            return;
        }

        if (!admin.password.equals(p)) {
            flash.put("errorAdminPass", "Contraseña incorrecta");
            adminLogin();
            return;
        }

        session.put("isAdmin", "true");
        session.put("adminUser", admin.username);
        redirect("/admin");
    }

    public static void adminLogout() {
        session.remove("isAdmin");
        session.remove("adminUser");
        redirect("/");
    }

    public static void adminPanel() {
        if (session.get("isAdmin") == null) {
            redirect("/admin/login");
        }
        redirect("/");
    }

    private static void requireAdmin() {
        if (session.get("isAdmin") == null) {
            redirect("/admin/login");
        }
    }

// ----------------------------------------------------------------------------------
//                                      Tabla Salas - Administrador
// ----------------------------------------------------------------------------------
    //Página Tabla Salas
    public static void adminSalas() {
        requireAdmin();

        List<Sala> salas = Sala.find("order by nombre asc").fetch();

        Sala salaEdit = null;
        boolean showSalaModal = false;
        String nombreSalaError = null;

        render("Application/AdminSala.html", salas, salaEdit, showSalaModal, nombreSalaError);
    }

    // Guardar
    public static void adminGuardarSala(Long id, String nombre, String direccion, int capacidad) {
        requireAdmin();

        List<Sala> salas = Sala.find("order by nombre asc").fetch();

        Sala salaEdit = new Sala(
                nombre != null ? nombre.trim() : "",
                direccion,
                capacidad
        );
        salaEdit.id = id;

        boolean showSalaModal = true;
        String nombreSalaError = null;

        if (salaEdit.nombre.isEmpty()) {
            nombreSalaError = "El nombre no puede estar vacío";
            render("Application/AdminSala.html", salas, salaEdit, showSalaModal, nombreSalaError);
        }
        if (capacidad < 1) {
            flash.error("La capacidad debe ser mayor que 0");
            render("Application/AdminSala.html", salas, salaEdit, showSalaModal, nombreSalaError);
        }

        Sala dup = Sala.find("byNombre", salaEdit.nombre).first();
        if (dup != null) {
            if (id == null || !dup.id.equals(id)) {
                nombreSalaError = "Este nombre de sala ya está en uso";
                render("Application/AdminSala.html", salas, salaEdit, showSalaModal, nombreSalaError);
            }
        }

        if (id == null) {
            new Sala(salaEdit.nombre, salaEdit.direccion, salaEdit.capacidad).save();
            flash.success("Sala creada correctamente");
            redirect("/admin/salas");
        }

        Sala s = Sala.findById(id);
        if (s == null) {
            flash.error("Sala no encontrada para actualizar");
            redirect("/admin/salas");
        }

        s.nombre = salaEdit.nombre;
        s.direccion = salaEdit.direccion;
        s.capacidad = salaEdit.capacidad;
        s.save();

        flash.success("Sala actualizada correctamente");
        redirect("/admin/salas");
    }

    // Eliminar
    public static void adminEliminarSala(Long id) {
        requireAdmin();

        Sala s = Sala.findById(id);
        if (s == null) {
            flash.error("Sala no encontrada para eliminar");
            redirect("/admin/salas");
        }

        if (s.conciertos != null && !s.conciertos.isEmpty()) {
            flash.error("No se puede eliminar: la sala tiene conciertos asociados");
            redirect("/admin/salas");
        }

        s.delete();
        flash.success("Sala eliminada correctamente");
        redirect("/admin/salas");
    }

// ----------------------------------------------------------------------------------
//                                      Tabla Artistas - Administrador
// ----------------------------------------------------------------------------------
    //Página Tabla Artistas
    public static void adminArtistas(Long editId) {
        requireAdmin();

        List<Artista> artistas = Artista.find("order by nombre_artistico asc").fetch();
        Artista artistaEdit = null;

        if (editId != null) {
            artistaEdit = Artista.findById(editId);
        }

        boolean showEditModal = false;
        String nombreArtisticoError = null;

        render("Application/AdminArtistas.html", artistas, artistaEdit, showEditModal, nombreArtisticoError);
    }

    // Guardar
    public static void adminGuardarArtista(Long id, String nombre_artistico, String nombre, String password, int edad) {
        requireAdmin();

        List<Artista> artistas = Artista.find("order by nombre_artistico asc").fetch();

        Artista artistaEdit = new Artista(
                nombre_artistico != null ? nombre_artistico.trim() : "",
                nombre != null ? nombre.trim() : "",
                password != null ? password : "",
                edad
        );
        artistaEdit.id = id;

        boolean showEditModal = (id != null);
        String nombreArtisticoError = null;

        // Validaciones básicas
        if (artistaEdit.nombre_artistico.isEmpty()) {
            flash.error("El nombre artístico no puede estar vacío");
            showEditModal = true;
            render("Application/AdminArtistas.html", artistas, artistaEdit, showEditModal, nombreArtisticoError);
        }
        if (artistaEdit.nombre.isEmpty()) {
            flash.error("El nombre no puede estar vacío");
            showEditModal = true;
            render("Application/AdminArtistas.html", artistas, artistaEdit, showEditModal, nombreArtisticoError);
        }
        if (artistaEdit.password.trim().isEmpty()) {
            flash.error("La contraseña no puede estar vacía");
            showEditModal = true;
            render("Application/AdminArtistas.html", artistas, artistaEdit, showEditModal, nombreArtisticoError);
        }
        if (edad < 0) {
            flash.error("La edad no puede ser negativa");
            showEditModal = true;
            render("Application/AdminArtistas.html", artistas, artistaEdit, showEditModal, nombreArtisticoError);
        }

        Artista dup = Artista.find("byNombre_artistico", artistaEdit.nombre_artistico).first();
        if (dup != null) {
            if (id == null || !dup.id.equals(id)) {
                nombreArtisticoError = "Este nombre artístico ya está en uso";
                showEditModal = true;
                render("Application/AdminArtistas.html", artistas, artistaEdit, showEditModal, nombreArtisticoError);
            }
        }

        // CREATE
        if (id == null) {
            new Artista(artistaEdit.nombre_artistico, artistaEdit.nombre, artistaEdit.password, edad).save();
            flash.success("Artista creado correctamente");
            redirect("/admin/artistas");
        }

        // UPDATE
        Artista a = Artista.findById(id);
        if (a == null) {
            flash.error("Artista no encontrado para actualizar");
            redirect("/admin/artistas");
        }

        a.nombre_artistico = artistaEdit.nombre_artistico;
        a.nombre = artistaEdit.nombre;
        a.password = artistaEdit.password;
        a.edad = edad;
        a.save();

        flash.success("Artista actualizado correctamente");
        redirect("/admin/artistas");
    }

    // Eliminar
    public static void adminEliminarArtista(Long id) {
        requireAdmin();

        Artista a = Artista.findById(id);
        if (a == null) {
            flash.error("Artista no encontrado para eliminar");
            redirect("/admin/artistas");
        }

        if (a.conciertos != null && !a.conciertos.isEmpty()) {
            flash.error("No se puede eliminar: el artista tiene conciertos asociados");
            redirect("/admin/artistas");
        }

        a.delete();
        flash.success("Artista eliminado correctamente");
        redirect("/admin/artistas");
    }

// ----------------------------------------------------------------------------------
//                                      Tabla Conciertos - Administrador
// ----------------------------------------------------------------------------------
    //Página Tabla Conciertos
    public static void adminConciertos() {
        requireAdmin();

        List<Concierto> conciertos = Concierto.find("order by fecha asc").fetch();
        List<Artista> artistas = Artista.find("order by nombre_artistico asc").fetch();
        List<Sala> salas = Sala.find("order by nombre asc").fetch();

        Concierto conciertoEdit = null;
        boolean showConciertoModal = false;
        String fechaConciertoError = null;

        render("Application/AdminConciertos.html",
                conciertos, artistas, salas, conciertoEdit, showConciertoModal, fechaConciertoError);
    }

    // Guardar
    public static void adminGuardarConcierto(Long id, String fecha, String estado, Long artistaId, Long salaId) {
        requireAdmin();

        List<Concierto> conciertos = Concierto.find("order by fecha asc").fetch();
        List<Artista> artistas = Artista.find("order by nombre_artistico asc").fetch();
        List<Sala> salas = Sala.find("order by nombre asc").fetch();

        boolean showConciertoModal = true;
        String fechaConciertoError = null;

        Concierto conciertoEdit = new Concierto(fecha != null ? fecha.trim() : "", estado != null ? estado.trim() : "");
        conciertoEdit.id = id;

        if (conciertoEdit.fecha.isEmpty()) {
            fechaConciertoError = "La fecha no puede estar vacía";
            render("Application/AdminConciertos.html",
                    conciertos, artistas, salas, conciertoEdit, showConciertoModal, fechaConciertoError);
        }
        if (conciertoEdit.estado.isEmpty()) {
            flash.error("El estado no puede estar vacío");
            render("Application/AdminConciertos.html",
                    conciertos, artistas, salas, conciertoEdit, showConciertoModal, fechaConciertoError);
        }
        if (artistaId == null || salaId == null) {
            flash.error("Debes seleccionar artista y sala");
            render("Application/AdminConciertos.html",
                    conciertos, artistas, salas, conciertoEdit, showConciertoModal, fechaConciertoError);
        }

        Artista artista = Artista.findById(artistaId);
        Sala sala = Sala.findById(salaId);

        if (artista == null || sala == null) {
            flash.error("Artista o sala no encontrados");
            render("Application/AdminConciertos.html",
                    conciertos, artistas, salas, conciertoEdit, showConciertoModal, fechaConciertoError);
        }

        conciertoEdit.artista = artista;
        conciertoEdit.sala = sala;

        Concierto dup = Concierto.find("fecha = ?1 and sala = ?2", conciertoEdit.fecha, sala).first();
        if (dup != null) {
            if (id == null || !dup.id.equals(id)) {
                fechaConciertoError = "Ya existe un concierto en esa sala para esa fecha";
                render("Application/AdminConciertos.html",
                        conciertos, artistas, salas, conciertoEdit, showConciertoModal, fechaConciertoError);
                return;
            }
        }

        if (id == null) {
            Concierto c = new Concierto(conciertoEdit.fecha, conciertoEdit.estado);
            c.artista = artista;
            c.sala = sala;
            c.save();
            flash.success("Concierto creado correctamente");
            redirect("/admin/conciertos");
        }

        Concierto c = Concierto.findById(id);
        if (c == null) {
            flash.error("Concierto no encontrado para actualizar");
            redirect("/admin/conciertos");
        }

        c.fecha = conciertoEdit.fecha;
        c.estado = conciertoEdit.estado;
        c.artista = artista;
        c.sala = sala;
        c.save();

        flash.success("Concierto actualizado correctamente");
        redirect("/admin/conciertos");
    }

    // Eliminar
    public static void adminEliminarConcierto(Long id) {
        requireAdmin();

        Concierto c = Concierto.findById(id);
        if (c == null) {
            flash.error("Concierto no encontrado para eliminar");
            redirect("/admin/conciertos");
        }

        c.delete();
        flash.success("Concierto eliminado correctamente");
        redirect("/admin/conciertos");
    }
}