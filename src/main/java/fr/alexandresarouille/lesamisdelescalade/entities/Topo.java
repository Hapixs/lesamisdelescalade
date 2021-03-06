package fr.alexandresarouille.lesamisdelescalade.entities;

import fr.alexandresarouille.lesamisdelescalade.entities.enums.Region;

import javax.persistence.*;
import java.util.Date;

/**
 * Classe java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 *
 * Entitée springboot
 * Topo d'escalade
 **/
@Entity(name = "topo")
public class Topo {

    public Topo() {}

    public Topo(User user,
                String displayName,
                String description,
                Region region,
                Date releaseDate,
                Boolean available) {

        this.user = user;
        this.displayName = displayName;
        this.description = description;
        this.region = region;
        this.releaseDate = releaseDate;
        this.available = available;

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


    @ManyToOne
    private User user; // UTILISATEUR DETENANT LE TOPO

    private String displayName; // NOM DU TOPO
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description; // DESCRIPTION DU TOPO

    private Region region; // Region du topo

    private Date releaseDate; // DATE DE MISE EN LIGNE DU TOPO

    private Boolean available; // DISPONIBILITE DU TOPO

    @OneToOne
    private Reservation currentReservation;
    //GETTERS & SETTERS

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Reservation getCurrentReservation() {
        return currentReservation;
    }

    public void setCurrentReservation(Reservation currentReservation) {
        this.currentReservation = currentReservation;
    }
}
