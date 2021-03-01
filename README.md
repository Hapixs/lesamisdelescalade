# Les Amis De L'escalade
Site communautaire regroupant les différent site d'escalade de France et des topos sur chaqu'une des régions.
***
##Déploiement de l'application
###Modification de la base de données visée

Pour modifier la base de donnée que spring-data utilisera pour stocker les donnée il suffis de modifier le fichier application.properties à la racine de l'application et de modifier les paramètres de spring-data tel que:

- spring.datasource.url
- spring.datasource.username
- spring.datasource.password
- spring.datasource.driver-class-name

Il faudra aussi modifier toujours dans le même fichier .properties les paramètre hibernate tel que:

- spring.jpa.hibernate.ddl-auto
- spring.jpa.properties.hibernate.dialect

Il faudra au préalable créer la base de donnée SQL désirer manuellement. <br/>
Ensuite Hibernate créera automatique les tables si elle n'existes pas.
###Déploiement de l'application avec Tomcat ###
1. Afin de récupere l'archive war de l'application il faut utiliser la command 'mvn package' afin que maven génére l'application war à l'aide du plugin donner par spring boot.
2. Une foie le war générer il suffit de le mettre dans les fichier de Tomcat puis de démarer le serveur web.
   (Aucune configuration spécifique de tomcat n'est requise.)