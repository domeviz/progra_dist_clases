package com.distribuida;

import com.distribuida.db.Persona;
import com.distribuida.servicios.IServicioPersona;
import com.google.gson.Gson;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import spark.Request;
import spark.Response;

import java.util.List;

import static spark.Spark.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class PrincipalP {

    static SeContainer container;

    static List<Persona> listarPersonas (Request req, Response rsp) {
        rsp.type("application/json");

        var servicio=container.select(IServicioPersona.class).get();

        return servicio.buscarPersonas();
    }

    static Persona buscarPersona (Request req, Response rsp) {
        rsp.type("application/json");
        String _id=req.params(":id");

        var servicio=container.select(IServicioPersona.class).get();

        var persona=servicio.buscarPersona(Integer.valueOf(_id));

        if(persona==null){
            halt(404, "Persona no encontrada");
        }
        return persona;
    }

    static Persona insertarPersona(Request req, Response rsp) {
        rsp.type("application/json");

        String jsonPersona = req.body();
        Persona persona = new Gson().fromJson(jsonPersona, Persona.class);

        IServicioPersona servicio = container.select(IServicioPersona.class).get();
        Persona insertPersona = servicio.insertar(persona);

        if (insertPersona == null) {
            halt(400, "Error al insertar persona");
        }

        return insertPersona;
    }

    static Persona actualizarPersona(Request req, Response rsp) {
        rsp.type("application/json");
        String jsonPersona = req.body();
        Persona persona = new Gson().fromJson(jsonPersona, Persona.class);

        IServicioPersona servicio = container.select(IServicioPersona.class).get();
        Persona actualizaPersona = servicio.actualizar(persona);

        return actualizaPersona;
    }

    static Persona eliminarPersona(Request req, Response rsp) {
        rsp.type("application/json");
        String _id=req.params(":id");

        var servicio=container.select(IServicioPersona.class).get();
        Persona persona=servicio.eliminar(Integer.valueOf(_id));

        return persona;

    }

    public static void main(String[] args) {
        //Iniciar contenedor
        container = SeContainerInitializer.newInstance().initialize();

        IServicioPersona sp=container.select(IServicioPersona.class).get();

        sp.buscarPersonas().stream().map(Persona::getNombre).forEach(System.out::println);

        Gson gson=new Gson();

        //Puerto a asignar
        port(8080);

        get("/personas", PrincipalP::listarPersonas,gson::toJson);
        get("/personas/:id", PrincipalP::buscarPersona,gson::toJson);
        post("/personas", PrincipalP::insertarPersona,gson::toJson);
        put("/personas", PrincipalP::actualizarPersona,gson::toJson);
        delete("/personas/:id", PrincipalP::eliminarPersona);
    }
}