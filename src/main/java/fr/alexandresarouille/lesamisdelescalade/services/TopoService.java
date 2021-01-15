package fr.alexandresarouille.lesamisdelescalade.services;

import fr.alexandresarouille.lesamisdelescalade.dao.TopoRepository;
import fr.alexandresarouille.lesamisdelescalade.entities.Topo;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TopoService implements ITopoService {

    @Autowired
    private TopoRepository topoRepository;

    @Override
    public Optional<Topo> findById(Integer id) {
        return topoRepository.findById(id);
    }

    @Override
    public Topo findByIdIfExist(Integer id) throws EntityNotExistException {

        Optional<Topo> target = findById(id);

        if(!target.isPresent())
            throw new EntityNotExistException("Topo introuvable.");

        return target.get();
    }

    @Override
    public Topo createTopo(Topo topo) {
        return topoRepository.saveAndFlush(topo);
    }

    @Override
    public Topo editTopo(Topo topo, Integer id) throws EntityNotExistException {

        Topo target = findByIdIfExist(id);

        target.setUser(topo.getUser() == null ? target.getUser() : topo.getUser());

        target.setDisplayName(topo.getDisplayName() == null || topo.getDisplayName().isEmpty() ? target.getDisplayName() : topo.getDisplayName());
        target.setDescription(topo.getDescription() == null || topo.getDescription().isEmpty() ? target.getDescription() : topo.getDescription());

        target.setRegion(topo.getRegion()  == null ? target.getRegion() : topo.getRegion());

        target.setReleaseDate(topo.getReleaseDate() == null ? target.getReleaseDate() : target.getReleaseDate());

        target.setAvailable(topo.getAvailable() == null ? target.getAvailable() : topo.getAvailable());

        target = topoRepository.saveAndFlush(target);

        return target;
    }

    @Override
    public void deleteTopo(Integer id) throws EntityNotExistException {
        Topo target = findByIdIfExist(id);

        topoRepository.delete(target);
    }
}
