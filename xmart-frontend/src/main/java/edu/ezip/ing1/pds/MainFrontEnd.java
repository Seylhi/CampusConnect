package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.Utilisateur;
import edu.ezip.ing1.pds.business.dto.Utilisateurs;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.UtilisateurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MainFrontEnd {

    private final static String LoggingLabel = "FrontEnd";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);
    private final static String networkConfigFile = "network.yaml";

    public static void main(String[] args) throws IOException, InterruptedException {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        logger.info("Chargement de la configuration réseau : {}", networkConfig);

        final UtilisateurService utilisateurService = new UtilisateurService(networkConfig);
        Utilisateurs utilisateurs = utilisateurService.selectUtilisateurs();

        if (utilisateurs != null && !utilisateurs.getUtilisateurs().isEmpty()) {
            logger.info("Connexion à la base de données réussie. Liste des utilisateurs :");
            for (final Utilisateur utilisateur : utilisateurs.getUtilisateurs()) {
                System.out.println("Utilisateur : " + utilisateur.getPrenom() + " " + utilisateur.getNom() + " | Email : " + utilisateur.getEmail());
            }
        } else {
            logger.warn("Aucun utilisateur trouvé. Vérifiez la connexion à la base de données et les données disponibles.");
        }
    }
}