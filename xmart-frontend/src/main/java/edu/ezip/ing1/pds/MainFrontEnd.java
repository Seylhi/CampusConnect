package edu.ezip.ing1.pds;

import de.vandermeer.asciitable.AsciiTable;
import edu.ezip.ing1.pds.business.dto.Utilisateur;
import edu.ezip.ing1.pds.business.dto.Utilisateurs;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.UtilisateurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class MainFrontEnd {

    private final static String LoggingLabel = "FrontEnd";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);
    private final static String networkConfigFile = "network.yaml";
    //private static final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

    public static void main(String[] args) throws IOException, InterruptedException {
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);
        logger.debug("Load Network config file : {}", networkConfig.toString());

        final UtilisateurService utilisateurService = new UtilisateurService(networkConfig);
        //utilisateurService.insertUtilisateurs();
        Utilisateurs utilisateurs = utilisateurService.selectUtilisateurs();
        final AsciiTable asciiTable = new AsciiTable();
        /*for (final Utilisateur utilisateur : utilisateurs.getUtilisateurs()) {
            asciiTable.addRule();
            asciiTable.addRow(utilisateur.getPrenom(), utilisateur.getNom(), utilisateur.getEmail());
        }
        asciiTable.addRule();
        logger.debug("\n{}\n", asciiTable.render());*/

        if (utilisateurs != null && !utilisateurs.getUtilisateurs().isEmpty()) {
            logger.info("Connexion à la bdd :");
            for (final Utilisateur utilisateur : utilisateurs.getUtilisateurs()) {
                System.out.println("Utilisateur : " + utilisateur.getPrenom() + " " + utilisateur.getNom() + " | Email : " + utilisateur.getEmail());
            }
        } else {
            logger.warn("Aucun utilisateur trouvé.");
        }
        SwingUtilities.invokeLater(MenuPrincipalApp::new);
    }
}