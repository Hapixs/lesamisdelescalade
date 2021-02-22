package fr.alexandresarouille.lesamisdelescalade.entities;

import javax.persistence.*;

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
        isAccepted = false;
    }

    /**
     * Id de l'entitée
     */
    @Id @GeneratedValue
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
    public Integer getId() {
        return id;
    }





    // VARIABLES
    @ManyToOne @JoinColumn(name = "user_id")
    private User user; // UTILISATEUR A L ORIGINE DE LA RESERVATION

    @ManyToOne @JoinColumn(name = "topo_id")
    private Topo topo; // TOPO A RESERVER

    private boolean isAccepted;
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

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
