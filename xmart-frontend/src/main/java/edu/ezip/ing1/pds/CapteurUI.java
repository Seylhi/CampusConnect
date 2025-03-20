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

        // Appliquer un Layout pour positionner les boutons
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacement entre les composants

        // Boutons pour afficher la liste des capteurs et ajouter un nouveau capteur
        JButton afficherButton = new JButton("Liste des Capteurs");
        JButton creerButton = new JButton("Créer Nouveau Capteur");
        afficherButton.setPreferredSize(new Dimension(200, 50));
        creerButton.setPreferredSize(new Dimension(200, 50));

        // Positionnement des boutons avec GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(afficherButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(creerButton, gbc);

        // ActionListener pour afficher la liste des capteurs
        afficherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherListeCapteurs();
            }
        });

        // ActionListener pour créer un nouveau capteur
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherFormulaireCreation();
            }
        });
        frame.add(panel);
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
        modifierButton.setPreferredSize(new Dimension(160, 40));

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

        // Ajout du bouton "Supprimer"
        JButton supprimerButton = new JButton("Supprimer Capteur");
        supprimerButton.setPreferredSize(new Dimension(160, 40));

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

                    capteurService.deleteCapteur(capteur);
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
        frame.setSize(450, 350);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Panel principal avec un GridLayout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10)); // 5 lignes et 2 colonnes, avec des espaces entre les composants

        // Ajout des labels et des champs de texte pour saisir les informations du capteur
        JLabel idLabel = new JLabel("ID Capteur :");
        JTextField idField = new JTextField(10);
        JLabel statutLabel = new JLabel("Statut (Actif/Inactif) :");
        String[] statutOptions = {"Actif", "Inactif"};
        JComboBox<String> statutComboBox = new JComboBox<>(statutOptions);
        statutComboBox.setPreferredSize(new Dimension(20, 20));
        JLabel presenceLabel = new JLabel("Présence (Oui/Non) :");
        String[] presenceOptions = {"Oui", "Non"};
        JComboBox<String> presenceComboBox = new JComboBox<>(presenceOptions);
        presenceComboBox.setPreferredSize(new Dimension(20, 20));
        JLabel detectionLabel = new JLabel("Problème détecté (Oui/Non) :");
        String[] detectionOptions = {"Oui", "Non"};
        JComboBox<String> detectionComboBox = new JComboBox<>(detectionOptions);
        detectionComboBox.setPreferredSize(new Dimension(20, 20));

        // Ajouter les composants dans le panel avec GridLayout
        panel.add(idLabel);
        panel.add(idField);
        panel.add(statutLabel);
        panel.add(statutComboBox);
        panel.add(presenceLabel);
        panel.add(presenceComboBox);
        panel.add(detectionLabel);
        panel.add(detectionComboBox);

        // Créer un bouton "Créer" avec une taille raisonnable
        JButton createButton = new JButton("Créer");
        createButton.setPreferredSize(new Dimension(20, 20));

        // Ajouter un écouteur d'événements au bouton
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String statut = (String) statutComboBox.getSelectedItem();
                String presence = (String) presenceComboBox.getSelectedItem();
                String detection = (String) detectionComboBox.getSelectedItem();

                // Vérifier si un capteur avec cet ID existe déjà
                try {
                    Capteurs capteurs = capteurService.selectCapteurs();
                    boolean idExists = capteurs.getCapteurs().stream().anyMatch(capteur -> capteur.getId().equals(id));

                    if (idExists) {
                        JOptionPane.showMessageDialog(frame, "Un capteur avec cet ID existe déjà !", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Créer un objet Capteur
                    Capteur capteur = new Capteur();
                    capteur.setId(id);
                    capteur.setStatut("Actif".equals(statut));
                    capteur.setPresence("Oui".equals(presence));
                    capteur.setDetectionProbleme("Oui".equals(detection));

                    // Appeler la méthode du service pour ajouter le capteur
                    capteurService.insertCapteur(capteur);
                    JOptionPane.showMessageDialog(frame, "Capteur créé avec succès !");
                } catch (InterruptedException | IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de la création du capteur : " + ex.getMessage());
                }

                frame.dispose(); // Fermer le formulaire après création
            }
        });

        // Ajouter le bouton à la fin du formulaire
        panel.add(createButton);

        // Ajouter le panel au JFrame
        frame.add(panel, BorderLayout.CENTER);
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
        updateButton.setPreferredSize(new Dimension(120, 40));
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

                capteurService.updateCapteur(capteur);
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