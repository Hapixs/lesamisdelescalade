package fr.alexandresarouille.lesamisdelescalade.services;

import fr.alexandresarouille.lesamisdelescalade.dao.ReservationRepository;
import fr.alexandresarouille.lesamisdelescalade.entities.Reservation;
import fr.alexandresarouille.lesamisdelescalade.entities.Topo;
import fr.alexandresarouille.lesamisdelescalade.entities.User;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Classe java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 15/01/2021
 **/
@Service
@Transactional
public class ReservationService implements IReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public Optional<Reservation> findById(Integer id) {
        return reservationRepository.findById(id);
    }

    @Override
    public Reservation findByIdIfExist(Integer id) throws EntityNotExistException {

        Optional<Reservation> target = findById(id);

        if(!target.isPresent())
            throw new EntityNotExistException("Résérvation introuvable.");

        return target.get();
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.saveAndFlush(reservation);
    }

    @Override
    public Reservation editReservation(Reservation reservation, Integer id) throws EntityNotExistException {

        Reservation target = findByIdIfExist(id);

        target.setTopo(reservation.getTopo() == null ? target.getTopo() : reservation.getTopo());

        target.setUser(reservation.getUser() == null ? target.getUser() : reservation.getUser());

        reservationRepository.saveAndFlush(target);

        return target;
    }

    @Override
    public void deleteReservation(Integer id) throws EntityNotExistException {
        Reservation target = findByIdIfExist(id);

        reservationRepository.delete(target);
    }

    @Override
    public Page<Reservation> listAllByTopo(PageRequest pageRequest, Topo topo) {
        return reservationRepository.findAllByTopo(pageRequest, topo);
    }

    @Override
    public Optional<Reservation> getConfirmedReservationForTopo(Topo topo) {
        return reservationRepository.findByTopoAndAccepted(topo, true);
    }

    @Override
    public Optional<Reservation> getReservationByTopoAndUser(Topo topo, User user) {
        return reservationRepository.findByTopoAndUser(topo, user);
    }


}
