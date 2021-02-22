package fr.alexandresarouille.lesamisdelescalade.dao;

import fr.alexandresarouille.lesamisdelescalade.entities.Topo;
import fr.alexandresarouille.lesamisdelescalade.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Interface java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 **/
@Repository
public interface TopoRepository extends JpaRepository<Topo, Integer> {

    Page<Topo> findAllByUser(Pageable pageable, User user);

    Page<Topo> findAll(Pageable pageable);

    @Query("SELECT t FROM topo t WHERE (:user IS NULL OR t.user <> :user)")
    Page<Topo> findAllWithNotUser(Pageable pageable, @Param("user")User user);

    @Query("SELECT t FROM topo t WHERE t.user=:user AND t.available=:available")
    Page<Topo> findAllByUserAndAvailable(Pageable pageable, @Param("user") User user, @Param("available") boolean available);
}
