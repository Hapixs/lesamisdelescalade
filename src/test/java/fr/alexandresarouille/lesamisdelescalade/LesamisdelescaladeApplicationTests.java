package fr.alexandresarouille.lesamisdelescalade;

import fr.alexandresarouille.lesamisdelescalade.entities.*;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Difficulty;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Region;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Role;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;
import fr.alexandresarouille.lesamisdelescalade.services.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest()
class LesamisdelescaladeApplicationTests {


    @Autowired
    private IClimbingSiteService climbingSiteService;
    @Autowired
    private ICommentaryService commentaryService;
    @Autowired
    private IReservationService reservationService;
    @Autowired
    private ITopoService topoService;
    @Autowired
    private IUserService userService;

    private ClimbingSite testSite = new ClimbingSite(
            "TEST SITE",
            "TEST SITE",
            Region.HAUTS_DE_FRANCE,
            194,
            14098,
            192,
            false,
            Difficulty.MEDIUM
    );
    private User testUser = new User(
            "TEST",
            "TEST",
            "TEST",
            Role.USER
    );
    private Commentary testCommentary = new Commentary(
            testUser,
            "TEST",
            testSite
    );
    private Topo testTopo = new Topo(
            testUser,
            "TEST",
            "TEST",
            Region.HAUTS_DE_FRANCE,
            new Date(),
            true
    );
    private Reservation testReservation = new Reservation(
            testUser,
            testTopo
    );

    @Test
    public void testSiteCRUD() {
        // CREATE
        testSite = climbingSiteService.createClimbingSite(testSite);

        try {
            //UPDATE
            ClimbingSite changes = new ClimbingSite();
            changes.setDifficulty(Difficulty.HIGH);
            changes.setOfficial(true);

            climbingSiteService.editClimbingSite(changes, testSite.getId());

            //DELETE
            climbingSiteService.deleteClimbingSite(testSite.getId());
        } catch (EntityNotExistException e) {
            System.out.println(e.getMessage());
        }
    }


    @Test
    public void testUserCRUD() {
        //CREATE
        testUser = userService.createUser(testUser);

        try {
            //EDITION
            User changes = new User();
            changes.setRole(Role.ADMIN);
            changes.setUsername("TEST CHANGE");

            userService.editUser(changes, testUser.getId());


            //DELETE
            userService.deleteUser(testUser.getId());
        } catch (EntityNotExistException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testCommentaryCRUD() {
        //CREATE
        testUser = userService.createUser(testUser);
        testSite = climbingSiteService.createClimbingSite(testSite);

        testCommentary = commentaryService.createCommentary(testCommentary);

        try {
            //UPDATE
            Commentary changes = new Commentary();
            changes.setContent("TEST CHANGE");
            commentaryService.editCommentary(changes, testCommentary.getId());

            //DELETE
            commentaryService.deleteCommentary(testCommentary.getId());

            userService.deleteUser(testUser.getId());
            climbingSiteService.deleteClimbingSite(testSite.getId());
        } catch (EntityNotExistException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testTopoCRUD() {
        //CREATE
        testUser = userService.createUser(testUser);

        testTopo = topoService.createTopo(testTopo);

        try {
            //UPDATE
            Topo changes = new Topo();
            changes.setRegion(Region.CORSE);
            changes.setDescription("TEST CHANGE");
            testTopo = topoService.editTopo(changes, testTopo.getId());

            //DELETE
            topoService.deleteTopo(testTopo.getId());

            userService.deleteUser(testUser.getId());
        } catch (EntityNotExistException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testReservationCRUD() {
        //CREATE
        testUser = userService.createUser(testUser);
        testTopo = topoService.createTopo(testTopo);

        testReservation = reservationService.createReservation(testReservation);

        try{
            //UPDATE
            User changesUser = new User("TEST", "TEST", "TEST", Role.USER);
            changesUser = userService.createUser(changesUser);

            Reservation changes = new Reservation();
            changes.setUser(changesUser);
            reservationService.editReservation(changes, testReservation.getId());

            //DELETE
            reservationService.deleteReservation(testReservation.getId());

            userService.deleteUser(changesUser.getId());

            topoService.deleteTopo(testTopo.getId());
            userService.deleteUser(testUser.getId());
        }catch (EntityNotExistException e){
            System.out.println(e.getMessage());
        }
    }
}
