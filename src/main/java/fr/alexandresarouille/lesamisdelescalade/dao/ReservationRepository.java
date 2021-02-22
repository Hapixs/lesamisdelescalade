package fr.alexandresarouille.lesamisdelescalade.dao;

import fr.alexandresarouille.lesamisdelescalade.entities.Reservation;
import fr.alexandresarouille.lesamisdelescalade.entities.Topo;
import fr.alexandresarouille.lesamisdelescalade.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 **/
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Page<Reservation> findAllByTopo(Pageable pageable, Topo topo);

    @Query("SELECT r FROM reservation r WHERE r.topo=:topo AND r.isAccepted=:accepted")
    Optional<Reservation> findByTopoAndAccepted(@Param("topo") Topo topo, @Param("accepted") Boolean accepted);

    Optional<Reservation> findByTopoAndUser(Topo topo, User user);
}
