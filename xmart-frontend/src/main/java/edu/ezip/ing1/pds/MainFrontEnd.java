package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.*;
import edu.ezip.ing1.pds.client.commons.*;
import edu.ezip.ing1.pds.services.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class MainFrontEnd {

    private static final String NETWORK_CONFIG_FILE = "network.yaml";

    public static void main(String[] args) throws IOException, InterruptedException {

        // Chargement de la configuration réseau
        final NetworkConfig networkConfig = ConfigLoader.loadConfig(NetworkConfig.class, NETWORK_CONFIG_FILE);

        // Initialisation des services
        final CapteurService capteurService = new CapteurService(networkConfig);
        final UtilisateurService utilisateurService = new UtilisateurService(networkConfig);
        final ReservationService reservationService = new ReservationService(networkConfig);

        // Récupération des données
        Capteurs capteurs = capteurService.selectCapteurs();
        Utilisateurs utilisateurs = utilisateurService.selectUtilisateurs();
        Reservations reservations = reservationService.selectReservations();

        // Création et configuration de la fenêtre principale
        JFrame frame = new JFrame("CAMPUS CONNECT");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setLocationRelativeTo(null);

        // Création du panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Bienvenue dans l'application de gestion scolaire CAMPUS CONNECT !");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Création des boutons
        JButton utilisateurButton = new JButton("Utilisateurs");
        JButton reservationButton = new JButton("Réservations");
        JButton capteurButton = new JButton("Capteurs");
        //JButton capteurSimuButton = new JButton("Simulation capteurs");


        //capteurSimuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        capteurButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        utilisateurButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        reservationButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ajout des événements aux boutons
        //capteurSimuButton.addActionListener((ActionEvent e) -> new SimuCapteurUI(networkConfig).createAndShowSimuUI(capteurs));
        capteurButton.addActionListener((ActionEvent e) -> new CapteurUI(networkConfig).afficherCapteurs(capteurs));
        utilisateurButton.addActionListener((ActionEvent e) -> new UtilisateurUI(networkConfig).afficherUtilisateurs(utilisateurs));
        reservationButton.addActionListener((ActionEvent e) -> new ReservationUI(networkConfig).afficherReservations(reservations));

        // Ajout des composants au panel
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(capteurButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(utilisateurButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(reservationButton);
        panel.add(Box.createVerticalStrut(10));
        //panel.add(capteurSimuButton);

        // Ajout du panel à la fenêtre
        frame.add(panel);

        // Affichage de la fenêtre principale
        frame.setVisible(true);
    }
}