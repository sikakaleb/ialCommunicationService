package com.service.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class ServiceApplication {

	// Initialisation du logger
	private static final Logger logger = LoggerFactory.getLogger(ServiceApplication.class);

	public static void main(String[] args) {
		// Charger les variables d'environnement depuis le fichier .env
		Dotenv dotenv = Dotenv.load();

		// Récupérer le profil actif de l'environnement
		String activeProfile = dotenv.get("SPRING_PROFILES_ACTIVE", "dev");

		// Définir le profil actif
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, activeProfile);

		// Log pour indiquer quel environnement est chargé
		logger.info("Le profil actif est : {}", activeProfile);

		// Lancement de l'application Spring Boot
		SpringApplication.run(ServiceApplication.class, args);
	}

}
