package models;

import play.db.jpa.Model;
import javax.persistence.Entity;

@Entity
public class Admin extends Model {

    public String username;
    public String password;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
