package fr.alexandresarouille.lesamisdelescalade.entities;

import fr.alexandresarouille.lesamisdelescalade.entities.enums.Difficulty;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Region;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Classe java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 *
 * Entitée SpringBoot
 * Site d'escalade.
 **/
@Entity(name = "climbingsite")
public class ClimbingSite {

    public ClimbingSite() {}

    public ClimbingSite(String displayName,
                        String description,
                        Region region,
                        Integer sectorAmount,
                        Integer length,
                        Integer wayAmount,
                        Boolean official,
                        Difficulty difficulty) {

        this.displayName = displayName;
        this.description = description;
        this.region = region;
        this.sectorAmount = sectorAmount;
        this.length = length;
        this.wayAmount = wayAmount;
        this.official = official;
        this.difficulty = difficulty;

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
    private String displayName; // NOM DU SITE
    private String description; // DESCRIPTION DU SITE

    private Region region;      // REGION DU SITE

    private Integer sectorAmount; // NOMBRE DE SECTEURS
    private Integer length;     // LONGEURS DU SITE
    private Integer wayAmount;  // NOMBRE DE VOIES

    private Boolean official;   // OFFICIEL LES AMIS DE L ESCALADE

    private Difficulty difficulty; // DIFFICULTE DU SITE




    // GETTERS & SETTERS

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

    public Integer getSectorAmount() {
        return sectorAmount;
    }

    public void setSectorAmount(Integer sectorAmount) {
        this.sectorAmount = sectorAmount;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWayAmount() {
        return wayAmount;
    }

    public void setWayAmount(Integer wayAmount) {
        this.wayAmount = wayAmount;
    }

    public Boolean getOfficial() {
        return official;
    }

    public void setOfficial(Boolean official) {
        this.official = official;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
