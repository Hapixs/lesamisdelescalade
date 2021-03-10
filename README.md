# Les Amis De L'escalade
Site communautaire regroupant les différent site d'escalade de France et des topos sur chaqu'une des régions.
***
###Prérequis
+ java version 1.8.0
+ maven 2
+ une base de donnée MySQL
###Configuration
**Utiliser la configuration par défaut de l'application:**
* host: localhost
* port: 3306
* base de donnée: lesamisdelescalade *(Elle doit être créer manuellement)*
* utilisateur: localroot
* mot de passe: *(aucun)*

**Modifier la configuration de l'application**:
1. Modifier les different champs du fichier application.properties tel que 
   * `spring.datasource.url`
   * `spring.datasource.username`
   * `spring.datasource.password`
2. Rebuild l'application avec maven et la commande: `mvn clean package`
###Déploiement de l'application ###
Afin de déployer l'application vous allez devoir demander à maven de build l'application afin d'en générer le .war utiliser.

En ce placent a la racine du projet utiliser la commande: `mvn clean package`

**Pour windows**: 

Après avoir build l'application à l'aide de maven il vous suffit d'utiliser le fichier `lunch.bat` présent à la racine du projet. Ou executer dans un terminal la commande: `java -jar {chemin_de_l'archive}/lesamisdelescalade.war`


Vous pouvez aussi créer un fichier batch (.bat) à n'importe quel endroit contenant la commande ci-dessus pointant vers l'archive war.


**Pour Linux**:

Après avoir build l'application à l'aide de maven il vous suffit soit d'utiliser le fichier `lunch.sh`:
* Vous devrez avant d'utiliser le fichier ajouter la permission d'execution avec `chmod +x lunch.sh`


Our alors utiliser la commande `java -jar {chemin_de_l'archive}/lesamisdelescalade.war`


Vous pouvez aussi créer un fichier shell (.sh) à n'importe quel endroit contenant la commande ci-dessus pointant vers l'archive war.

###Utilisation
Une foie l'application déployé vous pour y acceder localement depuis le lien: http://localhost:8080/

Voilà l'application devrais être en place et prête à être utiliser.