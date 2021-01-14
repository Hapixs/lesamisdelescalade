package fr.alexandresarouille.lesamisdelescalade.dao;

import fr.alexandresarouille.lesamisdelescalade.entities.ClimbingSite;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 **/
public interface ClimbingSiteRepository extends JpaRepository<ClimbingSite, Integer> { }
