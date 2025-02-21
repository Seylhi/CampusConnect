package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.Utilisateur;
import edu.ezip.ing1.pds.business.dto.Utilisateurs;
import edu.ezip.ing1.pds.services.UtilisateurService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UtilisateurUI {


    public void afficherUtilisateurs(Utilisateurs utilisateurs) {
        // Créer la fenêtre Swing
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
}
