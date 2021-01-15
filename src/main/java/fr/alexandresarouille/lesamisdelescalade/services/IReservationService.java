package fr.alexandresarouille.lesamisdelescalade.services;

import fr.alexandresarouille.lesamisdelescalade.entities.Reservation;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;

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
}
