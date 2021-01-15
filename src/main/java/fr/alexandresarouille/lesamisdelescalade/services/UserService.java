package fr.alexandresarouille.lesamisdelescalade.services;

import fr.alexandresarouille.lesamisdelescalade.dao.UserRepository;
import fr.alexandresarouille.lesamisdelescalade.entities.User;
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
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByIdIfExist(Integer id) throws EntityNotExistException {

        Optional<User> target = findById(id);

        if(!target.isPresent())
            throw new EntityNotExistException("Utilisateur introuvable.");

        return target.get();
    }

    @Override
    public User createUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User editUser(User user, Integer id) throws EntityNotExistException {

        User target = findByIdIfExist(id);

        target.setUsername(user.getUsername() == null || user.getUsername().isEmpty() ? target.getUsername() : user.getUsername());
        target.setEmail(user.getEmail() == null || user.getEmail().isEmpty() ? target.getEmail() : user.getEmail());
        target.setPassword(user.getPassword() == null || user.getPassword().isEmpty() ? target.getPassword() : user.getPassword());

        target.setRole(user.getRole() == null ? target.getRole() : user.getRole());

        userRepository.saveAndFlush(target);

        return target;
    }

    @Override
    public void deleteUser(Integer id) throws EntityNotExistException {
        User target = findByIdIfExist(id);

        userRepository.delete(target);
    }
}
