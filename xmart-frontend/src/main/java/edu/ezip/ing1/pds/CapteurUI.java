package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.Capteur;
import edu.ezip.ing1.pds.business.dto.Capteurs;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.CapteurService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CapteurUI {

    private CapteurService capteurService;

    public CapteurUI(NetworkConfig networkConfig) {
        this.capteurService = new CapteurService(networkConfig);
    }

    public void afficherCapteurs(Capteurs capteurs) {
        // Créer la fenêtre principale avec les boutons nécessaires
        JFrame frame = new JFrame("Gestion des Capteurs");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Créer un panel avec un layout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));

        // Boutons pour afficher la liste des capteurs et ajouter un nouveau capteur
        JButton afficherButton = new JButton("Liste des Capteurs");
        JButton creerButton = new JButton("Créer Nouveau Capteur");

        // ActionListener pour afficher la liste des capteurs
        afficherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Appeler la méthode pour afficher la liste des capteurs
                afficherListeCapteurs();
            }
        });

        // ActionListener pour créer un nouveau capteur
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ouvrir un formulaire pour créer un nouveau capteur
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

    private void afficherListeCapteurs() {
        Capteurs capteurs;
        try {
            capteurs = capteurService.selectCapteurs();
        } catch (InterruptedException | IOException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des capteurs : " + e.getMessage());
            return;
        }

        JFrame frame = new JFrame("Liste des Capteurs");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Statut", "Présence", "Problème détecté"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        // Ajouter les données des capteurs
        if (capteurs != null && capteurs.getCapteurs() != null && !capteurs.getCapteurs().isEmpty()) {
            for (Capteur capteur : capteurs.getCapteurs()) {
                Object[] row = {
                        capteur.getId(),
                        capteur.getStatut() ? "Actif" : "Inactif",
                        capteur.getPresence() ? "Oui" : "Non",
                        capteur.getDetectionProbleme() ? "Oui" : "Non"
                };
                tableModel.addRow(row);
            }
        } else {
            tableModel.addRow(new Object[]{"", "Aucun capteur trouvé", "", ""});
        }

        // Ajout du bouton "Modifier"
        JButton modifierButton = new JButton("Modifier Capteur");
        modifierButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un capteur.");
                return;
            }

            String id = (String) tableModel.getValueAt(selectedRow, 0);
            boolean statut = "Actif".equals(tableModel.getValueAt(selectedRow, 1));
            boolean presence = "Oui".equals(tableModel.getValueAt(selectedRow, 2));
            boolean detectionProbleme = "Oui".equals(tableModel.getValueAt(selectedRow, 3));

            Capteur capteur = new Capteur();
            capteur.setId(id);
            capteur.setStatut(statut);
            capteur.setPresence(presence);
            capteur.setDetectionProbleme(detectionProbleme);

            afficherFormulaireModification(capteur, tableModel, selectedRow);
        });

        //Ajout du bouton "Supprimer"
        JButton supprimerButton = new JButton("Supprimer Capteur");
        supprimerButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un capteur à supprimer.");
                return;
            }

            String id = (String) tableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Voulez-vous vraiment supprimer le capteur avec l'ID " + id + " ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Capteur capteur = new Capteur();
                    capteur.setId(id);

                    //capteurService.deleteCapteur(capteur);
                    JOptionPane.showMessageDialog(frame, "Capteur supprimé avec succès !");

                    // Supprimer la ligne du tableau
                    tableModel.removeRow(selectedRow);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression : " + ex.getMessage());
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(modifierButton);
        panel.add(supprimerButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void afficherFormulaireCreation() {
        JFrame frame = new JFrame("Créer un Capteur");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        // Panel pour contenir les champs du formulaire
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        // Champs pour saisir les informations du capteur
        panel.add(new JLabel("ID Capteur :"));
        JTextField idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Statut (Actif/ Inactif) :"));
        JTextField statutField = new JTextField();
        panel.add(statutField);

        panel.add(new JLabel("Présence (Oui/ Non) :"));
        JTextField presenceField = new JTextField();
        panel.add(presenceField);

        panel.add(new JLabel("Problème détecté (Oui/ Non) :"));
        JTextField detectionField = new JTextField();
        panel.add(detectionField);

        // Bouton pour valider la création
        JButton createButton = new JButton("Créer");
        panel.add(createButton);

        // ActionListener pour valider la création
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupérer les données des champs
                String id = idField.getText();
                boolean statut = "Actif".equals(statutField.getText());
                boolean presence = "Oui".equals(presenceField.getText());
                boolean detectionProbleme = "Oui".equals(detectionField.getText());

                // Créer un objet Capteur
                Capteur capteur = new Capteur();
                capteur.setId(id);
                capteur.setStatut(statut);
                capteur.setPresence(presence);
                capteur.setDetectionProbleme(detectionProbleme);

                // Appeler la méthode du service pour ajouter le capteur
                try {
                    capteurService.insertCapteur(capteur);
                    JOptionPane.showMessageDialog(frame, "Capteur créé avec succès !");
                } catch (InterruptedException | IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de la création du capteur : " + ex.getMessage());
                }

                frame.dispose();  // Fermer le formulaire après création
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private void afficherFormulaireModification(Capteur capteur, DefaultTableModel tableModel, int selectedRow) {
        JFrame frame = new JFrame("Modifier un Capteur");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("ID Capteur :"));
        JTextField idField = new JTextField(capteur.getId());
        idField.setEditable(false);
        panel.add(idField);

        panel.add(new JLabel("Statut (Actif/ Inactif) :"));
        JTextField statutField = new JTextField(capteur.getStatut() ? "Actif" : "Inactif");
        panel.add(statutField);

        panel.add(new JLabel("Présence (Oui/ Non) :"));
        JTextField presenceField = new JTextField(capteur.getPresence() ? "Oui" : "Non");
        panel.add(presenceField);

        panel.add(new JLabel("Problème détecté (Oui/ Non) :"));
        JTextField detectionField = new JTextField(capteur.getDetectionProbleme() ? "Oui" : "Non");
        panel.add(detectionField);

        JButton updateButton = new JButton("Modifier");
        panel.add(updateButton);

        updateButton.addActionListener(e -> {
            try {
                String statut = statutField.getText();
                String presence = presenceField.getText();
                String detection = detectionField.getText();

                // Mise à jour du capteur
                capteur.setStatut("Actif".equals(statut));
                capteur.setPresence("Oui".equals(presence));
                capteur.setDetectionProbleme("Oui".equals(detection));

                //capteurService.updateCapteur(capteur);
                JOptionPane.showMessageDialog(frame, "Capteur modifié avec succès !");

                // Mettre à jour la ligne dans le tableau
                tableModel.setValueAt(statut, selectedRow, 1);
                tableModel.setValueAt(presence, selectedRow, 2);
                tableModel.setValueAt(detection, selectedRow, 3);

                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de la modification : " + ex.getMessage());
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
