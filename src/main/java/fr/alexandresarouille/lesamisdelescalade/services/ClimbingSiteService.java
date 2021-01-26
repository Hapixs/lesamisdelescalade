package fr.alexandresarouille.lesamisdelescalade.services;

import fr.alexandresarouille.lesamisdelescalade.dao.ClimbingSiteRepository;
import fr.alexandresarouille.lesamisdelescalade.entities.ClimbingSite;
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
public class ClimbingSiteService implements IClimbingSiteService {


    @Autowired
    private ClimbingSiteRepository climbingSiteRepository;

    @Override
    public Optional<ClimbingSite> findById(Integer id) {
        return climbingSiteRepository.findById(id);
    }

    @Override
    public ClimbingSite findByIdIfExist(Integer id) throws EntityNotExistException {

        Optional<ClimbingSite> target = findById(id);

        if(!target.isPresent())
            throw new EntityNotExistException("Site d'escalade introuvable.");

        return target.get();
    }

    @Override
    public ClimbingSite createClimbingSite(ClimbingSite site) {

        site = climbingSiteRepository.save(site);

        return site;
    }

    @Override
    public ClimbingSite editClimbingSite(ClimbingSite site, Integer id) throws EntityNotExistException {

        ClimbingSite target = findByIdIfExist(id);

        target.setDisplayName(site.getDisplayName() == null || site.getDisplayName().isEmpty() ? target.getDisplayName() : site.getDisplayName());
        target.setDescription(site.getDescription() == null || site.getDescription().isEmpty() ? target.getDescription() : site.getDescription());

        target.setRegion(site.getRegion() == null ? target.getRegion() : site.getRegion());

        target.setSectorAmount(site.getSectorAmount() == null ? target.getSectorAmount() : site.getSectorAmount());
        target.setLength(site.getLength() == null ? target.getLength() : site.getLength());
        target.setWayAmount(site.getWayAmount() == null ? target.getWayAmount() : site.getWayAmount());

        target.setOfficial(site.getOfficial() == null ? target.getOfficial() : site.getOfficial());

        target.setDifficulty(site.getDifficulty() == null ? target.getDifficulty() : site.getDifficulty());

        climbingSiteRepository.saveAndFlush(target);

        return target;
    }

    @Override
    public void deleteClimbingSite(Integer id) throws EntityNotExistException {
        ClimbingSite target = findByIdIfExist(id);

        climbingSiteRepository.delete(target);
    }

    @Override
    public Page<ClimbingSite> listAll(PageRequest pageRequest) {
        return climbingSiteRepository.findAll(pageRequest);
    }

    @Override
    public Page<ClimbingSite> listAllByFilter(PageRequest pageRequest, ClimbingSite climbingSiteFilter) {
        return climbingSiteRepository.findAllFromFilter(pageRequest,
                climbingSiteFilter.getRegion(),
                climbingSiteFilter.getDifficulty(),
                climbingSiteFilter.getSectorAmount());
    }
}
