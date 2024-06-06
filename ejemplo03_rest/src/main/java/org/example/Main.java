package org.example;

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
public class Main {

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


    public static void main(String[] args) {
        //Iniciar contenedor
        container = SeContainerInitializer.newInstance().initialize();

        IServicioPersona sp=container.select(IServicioPersona.class).get();

        sp.buscarPersonas().stream().map(Persona::getNombre).forEach(System.out::println);

        Gson gson=new Gson();

        //Puerto a asignar
        port(8080);

        get("/personas", Main::listarPersonas,gson::toJson);
        get("/personas/:id", Main::buscarPersona,gson::toJson);
    }
}