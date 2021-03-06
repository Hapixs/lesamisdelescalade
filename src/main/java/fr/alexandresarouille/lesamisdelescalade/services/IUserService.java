package fr.alexandresarouille.lesamisdelescalade.services;

import fr.alexandresarouille.lesamisdelescalade.entities.User;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityAlreadyExistException;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

/**
 * Interface java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 **/
public interface IUserService extends UserDetailsService {

    public Optional<User> findById(Integer id);

    public User findByIdIfExist(Integer id) throws EntityNotExistException;

    public Optional<User> findByUsernameOrEmail(String s);

    public User findByUsernameOrEmailIfExist(String s) throws EntityNotExistException;

    public User createUser(User user);

    public User editUser(User user, Integer id) throws EntityNotExistException;

    public void deleteUser(Integer id) throws EntityNotExistException;

    public User createUserAccount(User user) throws EntityAlreadyExistException;

}
