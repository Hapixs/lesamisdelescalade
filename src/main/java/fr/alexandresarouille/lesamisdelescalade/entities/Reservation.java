package fr.alexandresarouille.lesamisdelescalade.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

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
}
