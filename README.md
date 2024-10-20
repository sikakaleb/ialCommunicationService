
---

# CommService

CommService est un microservice de communication centralisé conçu pour permettre des échanges fluides entre divers acteurs dans un environnement de santé connecté. Il assure l'envoi de messages asynchrones via des canaux variés (email, SMS, notifications push) et est capable de gérer des conversations entre médecins, infirmières, patients et leurs proches.

## **Fonctionnalités principales**

- **Gestion des Conversations** :
    - Création de conversations entre utilisateurs (médecins, infirmières, patients, proches).
    - Récupération des conversations actives et archivées d'un utilisateur.
    - Fermeture, archivage et restauration des conversations.

- **Gestion des Messages** :
    - Envoi de messages asynchrones entre utilisateurs.
    - Récupération des messages dans une conversation.
    - Statut des messages (envoyé, reçu, lu).
    - Suppression, archivage et restauration des messages.

- **Intégration avec Kafka** :
    - Utilisation d'Apache Kafka pour la gestion des événements d'envoi de messages.
    - Support des événements asynchrones pour garantir l'envoi des messages à destination.

## **Technologies Utilisées**

- **Spring Boot** : Framework principal pour le développement du service.
- **MongoDB** : Utilisé comme base de données pour stocker les conversations et les messages.
- **Apache Kafka** : Utilisé pour gérer l'envoi asynchrone des messages entre les utilisateurs.
- **Java 17** : Version Java utilisée pour ce projet.

## **Pré-requis**

Avant de lancer le projet, assurez-vous d'avoir installé les outils suivants :

- Java 17 ou supérieur
- Apache Kafka
- MongoDB
- Maven 3.x

## **Installation et Lancement**

### **Étape 1 : Cloner le repository**

```bash
git clone https://github.com/sikakaleb/ialCommunicationService
cd ialCommunicationService
```

### **Étape 2 : Configurer MongoDB et Kafka**

Assurez-vous que vos instances MongoDB et Kafka fonctionnent. Mettez à jour les fichiers de configuration `application.properties` ou les fichiers spécifiques à l'environnement (`application-dev.properties`, `application-prod.properties`) avec vos paramètres.

```properties
# Extrait de application.properties
spring.data.mongodb.uri=mongodb://localhost:27017/commServiceDB

# Configuration Kafka
kafka.bootstrap-servers=localhost:9092
```

### **Étape 3 : Construire et exécuter le projet**

Construisez le projet avec Maven et exécutez-le :

```bash
mvn clean install
mvn spring-boot:run
```

### **Étape 4 : Tester les API**

Une fois le projet démarré, vous pouvez interagir avec les différentes API en utilisant un outil comme **Postman** ou **cURL**.

#### **Exemples de routes API :**

- **Conversations :**
    - `GET /api/conversations/user/{userId}` : Récupérer les conversations d'un utilisateur.
    - `POST /api/conversations/start` : Démarrer une nouvelle conversation.

- **Messages :**
    - `POST /api/messages/send` : Envoyer un message.
    - `GET /api/messages/conversation/{conversationId}/messages` : Récupérer les messages d'une conversation.

## **Tests**

Des tests unitaires ont été ajoutés pour garantir la qualité des services implémentés. Pour exécuter les tests, utilisez la commande suivante :

```bash
mvn test
```

Les tests unitaires actuels couvrent les services principaux tels que la gestion des conversations, des messages et l'intégration basique avec Kafka. Des tests d'intégration seront ajoutés dans les prochaines itérations.

## **Améliorations Futures**

- Implémentation d'une gestion plus avancée des accusés de réception (ACK) pour Kafka.
- Amélioration de la gestion des erreurs Kafka (réessai automatique, traitement des échecs).
- Intégration avec des services externes pour l'envoi de SMS (Twilio) et de notifications push (Firebase).

## **Contributions**

Les contributions à ce projet sont les bienvenues. Si vous souhaitez contribuer, veuillez ouvrir une **issue** ou soumettre une **pull request**.


---

