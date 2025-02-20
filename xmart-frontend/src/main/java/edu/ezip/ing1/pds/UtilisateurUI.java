package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.Utilisateur;
import edu.ezip.ing1.pds.business.dto.Utilisateurs;
import edu.ezip.ing1.pds.services.UtilisateurService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class UtilisateurUI {

    private UtilisateurService utilisateurService; // Service pour gérer les utilisateurs

    public UtilisateurUI(Utilisateurs utilisateurs, UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService; // Injection du service
        SwingUtilities.invokeLater(() -> createAndShowGUI(utilisateurs));
    }

    private void createAndShowGUI(Utilisateurs utilisateurs) {
        // Créer le modèle de la table
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Nom d'utilisateur");
        tableModel.addColumn("Nom");
        tableModel.addColumn("Prénom");
        tableModel.addColumn("Email");

        // Ajouter les utilisateurs existants au modèle de table
        if (utilisateurs != null && utilisateurs.getUtilisateurs() != null && !utilisateurs.getUtilisateurs().isEmpty()) {
            for (Utilisateur utilisateur : utilisateurs.getUtilisateurs()) {
                tableModel.addRow(new Object[]{utilisateur.getNomUtilisateur(), utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getEmail()});
            }
        }

        // Créer une JTable pour afficher les utilisateurs
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // Créer les champs de saisie pour le formulaire
        JTextField nomUtilisateurField = new JTextField(10);
        JTextField nomField = new JTextField(10);
        JTextField prenomField = new JTextField(10);
        JTextField emailField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(10);
        JButton ajouterButton = new JButton("Ajouter");
        JButton afficherButton = new JButton("Afficher la liste");

        // Panel pour le formulaire d'ajout
        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Nom d'utilisateur:"));
        formPanel.add(nomUtilisateurField);
        formPanel.add(new JLabel("Nom:"));
        formPanel.add(nomField);
        formPanel.add(new JLabel("Prénom:"));
        formPanel.add(prenomField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Mot de passe:"));
        formPanel.add(passwordField);
        formPanel.add(ajouterButton);
        formPanel.add(afficherButton);

        // Action du bouton Ajouter
        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomUtilisateur = nomUtilisateurField.getText().trim();
                String nom = nomField.getText().trim();
                String prenom = prenomField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                // Vérification que tous les champs sont remplis
                if (!nomUtilisateur.isEmpty() && !nom.isEmpty() && !prenom.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    try {
                        // Créer un objet Utilisateur
                        Utilisateur utilisateur = new Utilisateur(nomUtilisateur, nom, prenom, email, password);

                        // Insérer l'utilisateur dans la base de données via le service
                        utilisateurService.insertUtilisateur(utilisateur);

                        // Ajouter l'utilisateur à la table (interface graphique)
                        tableModel.addRow(new Object[]{nomUtilisateur, nom, prenom, email});

                        // Réinitialiser les champs de texte
                        nomUtilisateurField.setText("");
                        nomField.setText("");
                        prenomField.setText("");
                        emailField.setText("");
                        passwordField.setText("");
                    } catch (IOException | InterruptedException ex) {
                        // Capturer les exceptions
                        JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de l'insertion de l'utilisateur.",
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Afficher une erreur si un champ est vide
                    JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action du bouton Afficher
        afficherButton.addActionListener(e -> table.repaint());

        // Créer une fenêtre JFrame pour afficher la table et le formulaire
        JFrame frame = new JFrame("Gestion des Utilisateurs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);
        frame.setLocationRelativeTo(null); // Centrer la fenêtre
        frame.setLayout(new BorderLayout());
        frame.add(formPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
