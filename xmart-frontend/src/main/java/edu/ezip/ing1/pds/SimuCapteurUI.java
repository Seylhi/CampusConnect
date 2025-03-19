/*package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.Capteur;
import edu.ezip.ing1.pds.business.dto.Capteurs;
import edu.ezip.ing1.pds.services.CapteurService;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SimuCapteurUI {

    private CapteurService capteurService;

    // Initialisation du service Capteur avec NetworkConfig
    public SimuCapteurUI(NetworkConfig networkConfig) {
        this.capteurService = new CapteurService(networkConfig);
    }

    // Méthode pour afficher le formulaire de création de capteur
    public void createAndShowSimuUI(Capteurs capteurs) {
        // Composants du formulaire pour ajouter un capteur
        JTextField idField = new JTextField(5);
        JCheckBox statutCheckBox = new JCheckBox("Actif");
        JCheckBox presenceCheckBox = new JCheckBox("Présent");
        JCheckBox problemeCheckBox = new JCheckBox("Problème détecté");
        JButton ajouterButton = new JButton("Ajouter");

        // Création d'un JPanel pour l'ajout de capteur
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10));
        formPanel.add(new JLabel("ID Capteur:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Statut :"));
        formPanel.add(statutCheckBox);
        formPanel.add(new JLabel("Présence :"));
        formPanel.add(presenceCheckBox);
        formPanel.add(new JLabel("Problème détecté :"));
        formPanel.add(problemeCheckBox);
        formPanel.add(ajouterButton);

        // Action du bouton "Ajouter"
        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText().trim();
                boolean statut = statutCheckBox.isSelected();
                boolean presence = presenceCheckBox.isSelected();
                boolean probleme = problemeCheckBox.isSelected();

                if (!id.isEmpty()) {
                    try {
                        // Création du nouveau capteur
                        Capteur capteur = new Capteur(id, statut, presence, probleme);
                        // Ajout du capteur via le service avec NetworkConfig
                        capteurService.insertCapteur(capteur);
                        JOptionPane.showMessageDialog(null, "Capteur ajouté avec succès !");
                        // Réinitialisation du formulaire après ajout
                        idField.setText("");
                        statutCheckBox.setSelected(false);
                        presenceCheckBox.setSelected(false);
                        problemeCheckBox.setSelected(false);
                    } catch (IOException | InterruptedException ex) {
                        JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du capteur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Création de la fenêtre principale
        JFrame frame = new JFrame("Simulation des capteurs");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.add(formPanel, BorderLayout.NORTH); // Ajout uniquement du formulaire
        frame.setVisible(true);
    }

    // Méthode pour afficher la liste des capteurs
    public void afficherListeCapteurs() {
        Capteurs capteurs;
        try {
            // Utilisation de NetworkConfig pour récupérer les capteurs
            capteurs = capteurService.selectCapteurs();
        } catch (InterruptedException | IOException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des capteurs : " + e.getMessage());
            return;
        }

        // Créer la fenêtre Swing pour afficher la liste des capteurs
        JFrame frame = new JFrame("Liste des Capteurs");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        // Créer les colonnes du tableau
        String[] columnNames = {"ID", "Statut", "Présence", "Problème détecté"};

        // Créer un modèle de tableau pour y insérer les données
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Si des capteurs sont trouvés, on les ajoute au modèle du tableau
        if (capteurs != null && capteurs.getCapteurs() != null && !capteurs.getCapteurs().isEmpty()) {
            for (final Capteur capteur : capteurs.getCapteurs()) {
                Object[] row = {
                        capteur.getId(),
                        capteur.getStatut(),
                        capteur.getPresence(),
                        capteur.getDetectionProbleme() // "Problème détecté"
                };
                tableModel.addRow(row);
            }
        } else {
            // Ajouter une ligne pour indiquer qu'aucun capteur n'a été trouvé
            tableModel.addRow(new Object[]{"", "", "", "Aucun capteur trouvé"});
        }

        // Créer le tableau avec le modèle de données
        JTable table = new JTable(tableModel);

        // Ajouter le tableau dans un JScrollPane pour pouvoir faire défiler
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        // Afficher la fenêtre
        frame.setVisible(true);
    }

    // Méthode pour rechercher un capteur par ID et afficher ses attributs
    public void rechercherCapteurParId(String id) {
        // Utilisation de NetworkConfig pour rechercher un capteur par ID
        Capteur capteur = capteurService.getCapteur(id);
        if (capteur != null) {
            // Afficher les informations du capteur
            JOptionPane.showMessageDialog(null, "Capteur trouvé :\n"
                    + "ID: " + capteur.getId() + "\n"
                    + "Statut: " + capteur.getStatut() + "\n"
                    + "Présence: " + capteur.getPresence() + "\n"
                    + "Problème détecté: " + capteur.getDetectionProbleme());
        } else {
            JOptionPane.showMessageDialog(null, "Capteur non trouvé avec l'ID : " + id, "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}*/
