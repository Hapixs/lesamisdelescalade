package fr.alexandresarouille.lesamisdelescalade.services;

import fr.alexandresarouille.lesamisdelescalade.entities.Reservation;
import fr.alexandresarouille.lesamisdelescalade.entities.Topo;
import fr.alexandresarouille.lesamisdelescalade.entities.User;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interface java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 **/
public interface IReservationService {

    public Optional<Reservation> findById(Integer id);

    public Reservation findByIdIfExist(Integer id) throws EntityNotExistException;



    public Reservation createReservation(Reservation reservation);

    public Reservation editReservation(Reservation reservation, Integer id) throws EntityNotExistException;

    public void deleteReservation(Integer id) throws EntityNotExistException;

    public Page<Reservation> listAllByTopo(PageRequest pageRequest, Topo topo);

    public Optional<Reservation> getConfirmedReservationForTopo(Topo topo);

    public Optional<Reservation> getReservationByTopoAndUser(Topo topo, User user);

    public Page<Reservation> listAllByUser(Pageable pageable, User user);
}
