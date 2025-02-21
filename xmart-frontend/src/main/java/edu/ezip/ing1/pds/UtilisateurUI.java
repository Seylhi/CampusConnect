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

    private UtilisateurService utilisateurService;

    public UtilisateurUI() {
        this.utilisateurService = utilisateurService;
    }

    public void afficherUtilisateurs(Utilisateurs utilisateurs) {
        // Créer la fenêtre principale avec deux boutons
        JFrame frame = new JFrame("Gestion des Utilisateurs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Créer un panel avec un layout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));

        // Boutons pour afficher la liste des utilisateurs et créer un nouveau utilisateur
        JButton afficherButton = new JButton("Afficher Liste des Utilisateurs");
        JButton creerButton = new JButton("Créer Nouveau Utilisateur");

        // ActionListener pour afficher la liste des utilisateurs
        afficherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Appeler la méthode pour afficher la liste des utilisateurs
                afficherListeUtilisateurs(utilisateurs);
            }
        });

        // ActionListener pour créer un nouveau utilisateur
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ouvrir un formulaire pour créer un nouvel utilisateur
                afficherFormulaireCreation();
            }
        });

        // Ajouter les boutons au panel
        panel.add(afficherButton);
        panel.add(creerButton);

        // Ajouter le panel à la fenêtre principale
        frame.add(panel);

        // Afficher la fenêtre principale
        frame.setVisible(true);
    }

    // Méthode pour afficher la liste des utilisateurs
    private void afficherListeUtilisateurs(Utilisateurs utilisateurs) {
        // Créer la fenêtre Swing pour afficher la liste des utilisateurs
        JFrame frame = new JFrame("Liste des Utilisateurs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Créer les colonnes du tableau
        String[] columnNames = {"ID", "Nom d'utilisateur", "Email", "Mot de passe", "Date de création",
                "Nom", "Prénom", "Âge", "Date de naissance", "Sexe"};

        // Créer un modèle de tableau pour y insérer les données
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Si des utilisateurs sont trouvés, on les ajoute au modèle du tableau
        if (utilisateurs != null && utilisateurs.getUtilisateurs() != null && !utilisateurs.getUtilisateurs().isEmpty()) {
            for (final Utilisateur utilisateur : utilisateurs.getUtilisateurs()) {
                Object[] row = {
                        utilisateur.getIdUtilisateur(),
                        utilisateur.getNomUtilisateur(),
                        utilisateur.getEmail(),
                        utilisateur.getPassword(),
                        utilisateur.getDateCreation(),
                        utilisateur.getNom(),
                        utilisateur.getPrenom(),
                        utilisateur.getAge(),
                        utilisateur.getDateDeNaissance(),
                        utilisateur.getSexe()
                };
                tableModel.addRow(row);
            }
        } else {
            // Ajouter une ligne pour indiquer qu'aucun utilisateur n'a été trouvé
            tableModel.addRow(new Object[]{"", "", "", "", "", "", "", "", "", "Aucun utilisateur trouvé"});
        }

        // Créer le tableau avec le modèle de données
        JTable table = new JTable(tableModel);

        // Ajouter le tableau dans un JScrollPane pour pouvoir faire défiler
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        // Afficher la fenêtre
        frame.setVisible(true);
    }

    // Méthode pour afficher un formulaire de création d'utilisateur
    private void afficherFormulaireCreation() {
        JFrame frame = new JFrame("Créer un Utilisateur");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        // Panel pour contenir les champs du formulaire
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        // Champs pour saisir les informations de l'utilisateur
        panel.add(new JLabel("Nom d'utilisateur :"));
        JTextField nomUtilisateurField = new JTextField();
        panel.add(nomUtilisateurField);

        panel.add(new JLabel("Email :"));
        JTextField emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Mot de passe :"));
        JTextField passwordField = new JTextField();
        panel.add(passwordField);

        panel.add(new JLabel("Nom :"));
        JTextField nomField = new JTextField();
        panel.add(nomField);

        panel.add(new JLabel("Prénom :"));
        JTextField prenomField = new JTextField();
        panel.add(prenomField);

        // Bouton pour valider la création
        JButton createButton = new JButton("Créer");
        panel.add(createButton);

        // ActionListener pour valider la création
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupérer les données des champs
                String nomUtilisateur = nomUtilisateurField.getText();
                String email = emailField.getText();
                String password = passwordField.getText();
                String nom = nomField.getText();
                String prenom = prenomField.getText();

                // Créer un objet Utilisateur
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setNomUtilisateur(nomUtilisateur);
                utilisateur.setEmail(email);
                utilisateur.setPassword(password);
                utilisateur.setNom(nom);
                utilisateur.setPrenom(prenom);

                // Appeler la méthode du service pour ajouter l'utilisateur à la base de données
                try {
                    utilisateurService.insertUtilisateur(utilisateur);
                    JOptionPane.showMessageDialog(frame, "Utilisateur créé avec succès !");
                } catch (InterruptedException | IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de la création de l'utilisateur : " + ex.getMessage());
                }

                frame.dispose();  // Fermer le formulaire après création
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
