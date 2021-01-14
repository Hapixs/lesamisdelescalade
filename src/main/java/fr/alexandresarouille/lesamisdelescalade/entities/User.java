package fr.alexandresarouille.lesamisdelescalade.entities;

import fr.alexandresarouille.lesamisdelescalade.entities.enums.Role;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Classe java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 *
 * Entitée de springboot
 * Utilisateur connecter sur le site à l'aide d'identifiants
 **/
@Entity(name = "user")
public class User {

    public User() {}

    public User(String username,
                String email,
                String password,
                Role role) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;

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
    private String username; // NOM D'UTILISATEUR
    private String email; // EMAIl
    private String password; // MOT DE PASSE

    private Role role; // ROLE

    // GETTERS & SETTERS

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
