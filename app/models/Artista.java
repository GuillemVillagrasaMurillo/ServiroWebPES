package models;

import play.db.jpa.Model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Artista extends Model {
    public String nombre_artistico;
    public String nombre;
    public String password;
    public int edad;

    public String webToken;


    @OneToMany(mappedBy = "artista")
    public List<Concierto> conciertos = new ArrayList<Concierto>();

    @ManyToMany
    public List<Sala> salas = new ArrayList<Sala>();

    public Artista(String nombre_artistico, String nombre, String password, int edad)
    {
        this.nombre_artistico = nombre_artistico;
        this.nombre = nombre;
        this.password = password;
        this.edad = edad;
    }
}
