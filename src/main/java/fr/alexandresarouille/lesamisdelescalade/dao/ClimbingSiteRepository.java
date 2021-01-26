package fr.alexandresarouille.lesamisdelescalade.dao;

import fr.alexandresarouille.lesamisdelescalade.entities.ClimbingSite;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Difficulty;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Interface java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 **/
@Repository
public interface ClimbingSiteRepository extends JpaRepository<ClimbingSite, Integer> {

    @Query("SELECT cs FROM climbingsite cs WHERE " +
            "(:region IS NULL OR cs.region=:region )" +
            "AND (:difficulty IS NULL OR cs.difficulty=:difficulty)" +
            "AND (:sectors IS NULL OR cs.sectorAmount=:sectors)")
    Page<ClimbingSite> findAllFromFilter(Pageable pageable,
                                              @Param("region")Region region,
                                              @Param("difficulty")Difficulty difficulty,
                                              @Param("sectors")Integer sectors);
}
