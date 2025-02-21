package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.Utilisateur;
import edu.ezip.ing1.pds.business.dto.Utilisateurs;
import edu.ezip.ing1.pds.business.dto.Capteurs;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.UtilisateurService;
import edu.ezip.ing1.pds.services.CapteurService;
import edu.ezip.ing1.pds.services.CapteurService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;

public class MainFrontEnd {

    private final static String networkConfigFile = "network.yaml";

    public static void main(String[] args) throws IOException, InterruptedException {

        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, networkConfigFile);

        // Créer le service des capteurs
        final CapteurService capteurService = new CapteurService(networkConfig);
        Capteurs capteurs = capteurService.selectCapteurs();
        final UtilisateurService utilisateurService = new UtilisateurService(networkConfig);
        Utilisateurs utilisateurs = utilisateurService.selectUtilisateurs();

        // Création de la fenêtre principale
        JFrame frame = new JFrame("Menu Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        // Création d'un panel avec un layout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));

        // Bouton pour ouvrir l'interface des capteurs
        JButton capteurButton = new JButton("Gérer les Capteurs");
        JButton utilisateurButton = new JButton("Utilisateurs");

        // ActionListener pour ouvrir la fenêtre CapteurUI
        capteurButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CapteurUI(capteurs, capteurService);
            }
        });
        utilisateurButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UtilisateurUI(utilisateurs, utilisateurService);
            }
        });

        // Ajouter le bouton au panel
        panel.add(capteurButton);

        // Ajouter le panel à la fenêtre principale
        frame.add(panel);

        // Afficher la fenêtre principale
        frame.setVisible(true);
    }
}