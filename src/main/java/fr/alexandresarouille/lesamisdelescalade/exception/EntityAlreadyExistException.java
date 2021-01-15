package fr.alexandresarouille.lesamisdelescalade.exception;

/**
 * Classe java du projet: Les-amis-de-lescalade
 *
 * @author Alexandre Sarouille
 * @since 14/01/2021
 **/
public class EntityAlreadyExistException extends Exception {
    public EntityAlreadyExistException(String message){
        super(message);
    }
}
