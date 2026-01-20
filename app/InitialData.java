import models.*;

public class InitialData {

    public static void crearConciertosEjemplo() {

        Artista a1 = new Artista("Bad Bunny", "Benito Martínez", "1234", 29).save();
        Artista a2 = new Artista("Rosalía", "Rosalía Vila", "abcd", 31).save();
        Artista a3 = new Artista("Aitana", "Aitana Ocaña", "pass1", 25).save();
        Artista a4 = new Artista("Quevedo", "Pedro Quevedo", "pass2", 23).save();
        Artista a5 = new Artista("Lola Indigo", "Miriam Doblas", "pass3", 32).save();
        Artista a6 = new Artista("Rels B", "Daniel Heredia", "pass4", 30).save();
        Artista a7 = new Artista("Morad", "Morad El Khattouti", "pass5", 24).save();
        Artista a8 = new Artista("C. Tangana", "Antón Álvarez", "pass6", 33).save();
        Artista a9 = new Artista("Nathy Peluso", "Natalia Peluso", "pass7", 29).save();
        Artista a10 = new Artista("Dellafuente", "Pablo Enoc", "pass8", 31).save();


        Sala s1 = new Sala("Sala Apolo", "Barcelona", 1200).save();
        Sala s2 = new Sala("Razzmatazz", "Barcelona", 2000).save();
        Sala s3 = new Sala("La Riviera", "Madrid", 2500).save();
        Sala s4 = new Sala("WiZink Center", "Madrid", 15000).save();
        Sala s5 = new Sala("Sant Jordi Club", "Barcelona", 5000).save();
        Sala s6 = new Sala("Palau Sant Jordi", "Barcelona", 18000).save();
        Sala s7 = new Sala("Teatro Real", "Madrid", 1750).save();
        Sala s8 = new Sala("Auditorio Nacional", "Madrid", 2300).save();
        Sala s9 = new Sala("Sala Moon", "Valencia", 800).save();
        Sala s10 = new Sala("Sala Zentral", "Pamplona", 1200).save();


        Concierto c1 = new Concierto("2024-11-01", "Confirmado");
        c1.artista = a1;
        c1.sala = s1;
        c1.save();

        Concierto c2 = new Concierto("2024-11-03", "Pendiente");
        c2.artista = a2;
        c2.sala = s2;
        c2.save();

        Concierto c3 = new Concierto("2024-11-05", "Confirmado");
        c3.artista = a3;
        c3.sala = s3;
        c3.save();

        Concierto c4 = new Concierto("2024-11-07", "Cancelado");
        c4.artista = a4;
        c4.sala = s4;
        c4.save();

        Concierto c5 = new Concierto("2024-11-09", "Pendiente");
        c5.artista = a5;
        c5.sala = s5;
        c5.save();

        Concierto c6 = new Concierto("2024-11-11", "Confirmado");
        c6.artista = a6;
        c6.sala = s6;
        c6.save();

        Concierto c7 = new Concierto("2024-11-13", "Pendiente");
        c7.artista = a7;
        c7.sala = s7;
        c7.save();

        Concierto c8 = new Concierto("2024-11-15", "Confirmado");
        c8.artista = a8;
        c8.sala = s8;
        c8.save();

        Concierto c9 = new Concierto("2024-11-17", "Cancelado");
        c9.artista = a9;
        c9.sala = s9;
        c9.save();

        Concierto c10 = new Concierto("2024-11-19", "Confirmado");
        c10.artista = a10;
        c10.sala = s10;
        c10.save();

        Concierto c11 = new Concierto("2024-11-21", "Pendiente");
        c11.artista = a1;
        c11.sala = s2;
        c11.save();

        Concierto c12 = new Concierto("2024-11-23", "Confirmado");
        c12.artista = a2;
        c12.sala = s3;
        c12.save();

        Concierto c13 = new Concierto("2024-11-25", "Pendiente");
        c13.artista = a3;
        c13.sala = s4;
        c13.save();

        Concierto c14 = new Concierto("2024-11-27", "Confirmado");
        c14.artista = a4;
        c14.sala = s5;
        c14.save();

        Concierto c15 = new Concierto("2024-11-29", "Cancelado");
        c15.artista = a5;
        c15.sala = s6;
        c15.save();

        Concierto c16 = new Concierto("2024-12-01", "Confirmado");
        c16.artista = a6;
        c16.sala = s7;
        c16.save();

        Concierto c17 = new Concierto("2024-12-03", "Pendiente");
        c17.artista = a7;
        c17.sala = s8;
        c17.save();

        Concierto c18 = new Concierto("2024-12-05", "Confirmado");
        c18.artista = a8;
        c18.sala = s9;
        c18.save();

        Concierto c19 = new Concierto("2024-12-07", "Pendiente");
        c19.artista = a9;
        c19.sala = s10;
        c19.save();

        Concierto c20 = new Concierto("2024-12-09", "Confirmado");
        c20.artista = a10;
        c20.sala = s1;
        c20.save();

        if (Admin.count() == 0) {
            new Admin("admin", "admin123").save();
        }
    }
}
