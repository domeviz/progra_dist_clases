package com.distribuida.servicios;

import com.distribuida.db.Persona;
import java.util.List;

public interface IServicioPersona {
    Persona buscarPersona(Integer id);
    List<Persona> buscarPersonas();
}
