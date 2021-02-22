package fr.alexandresarouille.lesamisdelescalade;

import fr.alexandresarouille.lesamisdelescalade.entities.*;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Difficulty;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Region;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Role;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityAlreadyExistException;
import fr.alexandresarouille.lesamisdelescalade.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


@SpringBootApplication
public class LesamisdelescaladeApplication extends SpringBootServletInitializer implements CommandLineRunner {

    @Autowired
    private IUserService userService;
    @Autowired
    private IClimbingSiteService climbingSiteService;
    @Autowired
    private ICommentaryService commentaryService;
    @Autowired
    private ITopoService topoService;
    @Autowired
    private IReservationService reservationService;

    public static void main(String[] args) {
        SpringApplication.run(LesamisdelescaladeApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // VARIABLE à MODIFIER EN FONCTION DU NOMBRE DE JEUX DE DONNEE GENERER ALEATOIREMENT VOULUT
        // A METTRE SUR 0 POUR N AVOIR AUCUN JEUX DE DONNE GENERER
        boolean adminAccount = true; // username = admin ; password = admin
        int numberSiteExampleToCreate = 500;
        int numberUserToCreate = 100;
        int numberCommentaryToCreate = 750;
        int numberToposToCreate = 75;
        int numberReservationsToCreate = 200;
        // ---------------------------------------------------------------------------------

        System.out.println("Génération du jeux de données....");
        ArrayList<User> users = new ArrayList<>();
        if(adminAccount){
            User user = new User("admin", "admin", "admin", Role.ADMIN);
            userService.createUserAccount(user);
            users.add(user);
        }

        ArrayList<ClimbingSite> sites = new ArrayList<>();
        for(int i = 0; i < numberSiteExampleToCreate; i++) {
            Difficulty d = Difficulty.values()[new Random().nextInt(Difficulty.values().length)];
            Region r = Region.values()[new Random().nextInt(Region.values().length)];
            ClimbingSite c = new ClimbingSite(
                    "Site test number"+i,
                    "A Simple test description for the site number "+i,
                    r,
                    new Random().nextInt(),
                    new Random().nextInt(),
                    new Random().nextInt(),
                    new Random().nextBoolean(),
                    d
            );
            sites.add(climbingSiteService.createClimbingSite(c));
        }
        for(int i = 0; i < numberUserToCreate; i++) {
            User u = new User(
                    "User_"+i,
                    "user"+i+"@mail.com",
                    "theultimatepassword",
                    Role.values()[new Random().nextInt(Role.values().length)]
            );
            users.add(userService.createUserAccount(u));
        }

        for(int i = 0; i < numberCommentaryToCreate; i++) {
            User u = users.get(new Random().nextInt(users.size()));
            ClimbingSite s = sites.get(new Random().nextInt(sites.size()));

            Commentary c = new Commentary(u, "Test commentary number "+i, s);

            commentaryService.createCommentary(c);
        }

        ArrayList<Topo> topos = new ArrayList<>();
        for(int i = 0; i < numberToposToCreate; i++) {
            User u = users.get(new Random().nextInt(users.size()));

            Topo t = new Topo(
                    u,
                    "Test topo number"+i,
                    "Simple description for topo number "+i,
                    Region.values()[new Random().nextInt(Region.values().length)],
                    new Date(),
                    new Random().nextBoolean()
            );

            topos.add(topoService.createTopo(t));
        }

        for(int i = 0; i < numberReservationsToCreate; i++) {
            User u = users.get(new Random().nextInt(users.size()));
            Topo t = topos.get(new Random().nextInt(topos.size()));

            Reservation r = new Reservation(u, t);

            r = reservationService.createReservation(r);

            if(!t.getAvailable()) {
                r.setAccepted(true);
                r = reservationService.editReservation(r, r.getId());
                t.setCurrentReservation(r);
                topoService.editTopo(t, t.getId());
            }
        }
        System.out.println("Jeux de donnée créer dans la base de donnée.");
    }
}
