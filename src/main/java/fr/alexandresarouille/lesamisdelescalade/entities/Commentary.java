package fr.alexandresarouille.lesamisdelescalade.entities;

import javax.persistence.*;

/**
 * Classe java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 *
 * Entitée de springboot
 * Commentaire d'un utilisateur sur un site d'escalade
 **/
@Entity(name = "commentary")
public class Commentary {

    public Commentary() {}

    public Commentary(User user,
                      String content,
                      ClimbingSite climbingSite) {

        this.user = user;
        this.content = content;
        this.climbingSite = climbingSite;

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
    @ManyToOne
    private User user; // UTILISATEUR A L ORGINE DU COMMENTAIRE

    private String content; // CONTENUE DU COMMENTAIRE

    @ManyToOne
    private ClimbingSite climbingSite; // SITE D ESCALADE

    //GETTERS & SETTERS

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ClimbingSite getClimbingSite() {
        return climbingSite;
    }

    public void setClimbingSite(ClimbingSite climbingSite) {
        this.climbingSite = climbingSite;
    }
}
