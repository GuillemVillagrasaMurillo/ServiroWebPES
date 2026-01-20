package models;

import play.db.jpa.Model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Sala extends Model {
    public String nombre;
    public String direccion;
    public int capacidad;

    @OneToMany(mappedBy = "sala")
    public List<Concierto> conciertos = new ArrayList<Concierto>();

    @ManyToMany(mappedBy = "salas")
    public List<Artista> artistas = new ArrayList<Artista>();

    public Sala(String nombre, String direccion, int capacidad) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidad = capacidad;
    }
}
