package fr.alexandresarouille.lesamisdelescalade.services;


import fr.alexandresarouille.lesamisdelescalade.entities.ClimbingSite;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;

import java.util.Optional;

/**
 * Interface java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 **/
public interface IClimbingSiteService {

    public Optional<ClimbingSite> findById(Integer id);

    public ClimbingSite findByIdIfExist(Integer id) throws EntityNotExistException;



    public ClimbingSite createClimbingSite(ClimbingSite site);

    public ClimbingSite editClimbingSite(ClimbingSite site, Integer id) throws EntityNotExistException;

    public void deleteClimbingSite(Integer id) throws EntityNotExistException;
}
