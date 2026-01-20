import play.jobs.Job;
import play.jobs.OnApplicationStart;
import models.Artista;
import models.Sala;
import models.Concierto;
import models.Admin;
import controllers.Application;

@OnApplicationStart
public class Bootstrap extends Job {

    @Override
    public void doJob() {

        boolean sinArtistas = Artista.count() == 0;
        boolean sinSalas = Sala.count() == 0;
        boolean sinConciertos = Concierto.count() == 0;

        if (sinArtistas && sinSalas && sinConciertos) {
            InitialData.crearConciertosEjemplo();
        } else {
            if (Admin.count() == 0) {
                new Admin("admin", "admin123").save();
            }
        }
    }
}


