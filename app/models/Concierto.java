package models;

import play.db.jpa.Model;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Concierto extends Model {
    public String fecha;
    public String estado;

    @ManyToOne
    public Artista artista;

    @ManyToOne
    public Sala sala;

    public Concierto(String fecha, String estado) {
        this.fecha = fecha;
        this.estado = estado;
    }
}
