package fr.alexandresarouille.lesamisdelescalade.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Classe java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 *
 * Entitée de springboot
 * Réservation d'un topo par un utilisateur
 **/
@Entity(name = "reservation")
public class Reservation {

    public Reservation() {}

    public Reservation(User user,
                       Topo topo) {

        this.user = user;
        this.topo = topo;

    }

    /**
     * Id de l'entitée
     */
    private Integer id;

    /**
     * Setter d'id de l'entitée
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter d'id de l'entitée
     */
    @Id
    public Integer getId() {
        return id;
    }





    // VARIABLES
    @OneToOne
    private User user; // UTILISATEUR A L ORIGINE DE LA RESERVATION

    @OneToOne
    private Topo topo; // TOPO A RESERVER

    //GETTERS & SETTERS

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Topo getTopo() {
        return topo;
    }

    public void setTopo(Topo topo) {
        this.topo = topo;
    }
}
