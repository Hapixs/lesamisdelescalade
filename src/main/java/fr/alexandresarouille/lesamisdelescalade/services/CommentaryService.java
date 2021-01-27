package fr.alexandresarouille.lesamisdelescalade.services;

import fr.alexandresarouille.lesamisdelescalade.dao.CommentaryRepostitory;
import fr.alexandresarouille.lesamisdelescalade.entities.ClimbingSite;
import fr.alexandresarouille.lesamisdelescalade.entities.Commentary;
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
public class CommentaryService implements ICommentaryService {


    @Autowired
    private CommentaryRepostitory CommentaryRepository;

    @Override
    public Optional<Commentary> findById(Integer id) {
        return CommentaryRepository.findById(id);
    }

    @Override
    public Commentary findByIdIfExist(Integer id) throws EntityNotExistException {

        Optional<Commentary> target = findById(id);

        if(!target.isPresent())
            throw new EntityNotExistException("Commentaire introuvable.");

        return target.get();
    }

    @Override
    public Commentary createCommentary(Commentary commentary) {

       commentary = CommentaryRepository.saveAndFlush(commentary);

        return commentary;
    }

    @Override
    public Commentary editCommentary(Commentary commentary, Integer id) throws EntityNotExistException {

        Commentary target = findByIdIfExist(id);

        target.setUser(commentary.getUser() == null ? target.getUser() : commentary.getUser());

        target.setContent(commentary.getContent() == null || commentary.getContent().isEmpty() ? target.getContent() : commentary.getContent());

        target.setClimbingSite(commentary.getClimbingSite() == null ? target.getClimbingSite() : commentary.getClimbingSite());

        CommentaryRepository.saveAndFlush(target);

        return target;
    }

    @Override
    public void deleteCommentary(Integer id) throws EntityNotExistException {
        Commentary target = findByIdIfExist(id);

        CommentaryRepository.delete(target);
    }

    @Override
    public Page<Commentary> findByClimbingSite(PageRequest pageRequest, ClimbingSite site) {
        return CommentaryRepository.findAllByClimbingSite(pageRequest, site);
    }
}
