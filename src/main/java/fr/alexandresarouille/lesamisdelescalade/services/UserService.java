package fr.alexandresarouille.lesamisdelescalade.services;

import fr.alexandresarouille.lesamisdelescalade.dao.UserRepository;
import fr.alexandresarouille.lesamisdelescalade.entities.User;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Role;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityAlreadyExistException;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
    public Optional<User> findByUsernameOrEmail(String s) {
        return userRepository.findByEmailOrUsername(s);
    }

    @Override
    public User findByUsernameOrEmailIfExist(String s) throws EntityNotExistException {
        Optional<User> target = findByUsernameOrEmail(s);

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
        target.setPassword(bCryptPasswordEncoder.encode(user.getPassword() == null || user.getPassword().isEmpty() ? target.getPassword() : user.getPassword()));

        target.setRole(user.getRole() == null ? target.getRole() : user.getRole());

        userRepository.saveAndFlush(target);

        return target;
    }

    @Override
    public void deleteUser(Integer id) throws EntityNotExistException {
        User target = findByIdIfExist(id);

        userRepository.delete(target);
    }

    @Override
    public User createUserAccount(User user) throws EntityAlreadyExistException {
        Optional<User> target = userRepository.findByEmailOrUsername(user.getEmail());

        if(target.isPresent())
            throw new EntityAlreadyExistException("Cette email est déjà utilisée!");

        target = userRepository.findByEmailOrUsername(user.getUsername());

        if(target.isPresent())
            throw new EntityAlreadyExistException("Ce nom d'utilisateur est déjà pris");

        if(user.getRole()==null)
            user.setRole(Role.USER);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        createUser(user);

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> target = userRepository.findByEmailOrUsername(s);

        if(!target.isPresent())
            throw new UsernameNotFoundException("Utilisateur introuvable");

        User user = target.get();
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(user.getRole().toString()));


        UserDetails details = new org.springframework.security.core.userdetails.User(
            user.getUsername(), user.getPassword(), authorityList
        );

        return details;
    }
}
