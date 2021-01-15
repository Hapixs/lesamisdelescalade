package fr.alexandresarouille.lesamisdelescalade.dao;

import fr.alexandresarouille.lesamisdelescalade.entities.Topo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 **/
@Repository
public interface TopoRepository extends JpaRepository<Topo, Integer> {
}
