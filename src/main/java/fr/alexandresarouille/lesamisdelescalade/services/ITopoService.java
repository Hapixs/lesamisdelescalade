package fr.alexandresarouille.lesamisdelescalade.services;

import fr.alexandresarouille.lesamisdelescalade.entities.Topo;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;

import java.util.Optional;

/**
 * Interface java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 **/
public interface ITopoService {

    public Optional<Topo> findById(Integer id);

    public Topo findByIdIfExist(Integer id) throws EntityNotExistException;



    public Topo createTopo(Topo topo);

    public Topo editTopo(Topo topo, Integer id) throws EntityNotExistException;

    public void deleteTopo(Integer id) throws EntityNotExistException;
}
