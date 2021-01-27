package fr.alexandresarouille.lesamisdelescalade.services;

import fr.alexandresarouille.lesamisdelescalade.entities.ClimbingSite;
import fr.alexandresarouille.lesamisdelescalade.entities.Commentary;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

/**
 * Interface java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 **/
public interface ICommentaryService {

    public Optional<Commentary> findById(Integer id);

    public Commentary findByIdIfExist(Integer id) throws EntityNotExistException;



    public Commentary createCommentary(Commentary commentary);

    public Commentary editCommentary(Commentary commentary, Integer id) throws EntityNotExistException;

    public void deleteCommentary(Integer id) throws EntityNotExistException;

    public Page<Commentary> findByClimbingSite(PageRequest pageRequest, ClimbingSite site);
}
