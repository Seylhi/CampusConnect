package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.Capteur;
import edu.ezip.ing1.pds.business.dto.Capteurs;
import edu.ezip.ing1.pds.services.CapteurService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SimuCapteurUI {
 
    private CapteurService capteurService;

    public SimuCapteurUI(Capteurs capteurs, CapteurService capteurService) {
        this.capteurService = capteurService;
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    private void createAndShowGUI() {
        // Composants du formulaire pour ajouter un capteur
        JTextField idField = new JTextField(5);
        JCheckBox statutCheckBox = new JCheckBox("Actif");
        JCheckBox presenceCheckBox = new JCheckBox("Présent");
        JCheckBox problemeCheckBox = new JCheckBox("Problème détecté");
        JButton ajouterButton = new JButton("Ajouter");

        // Création d'un JPanel pour l'ajout de capteur
        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("ID Capteur:"));
        formPanel.add(idField);
        formPanel.add(statutCheckBox);
        formPanel.add(presenceCheckBox);
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
                        // Ajout du capteur via le service
                        capteurService.insertCapteur(capteur);
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

        // Connexion à la base de données et récupération des capteurs si nécessaire (pas affichée ici)
        // Capteurs capteurs = capteurService.getCapteursFromDatabase();
        // Tu peux manipuler ces capteurs en arrière-plan si nécessaire sans les afficher
    }
}
