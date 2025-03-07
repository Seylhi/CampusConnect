package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.Capteur;
import edu.ezip.ing1.pds.business.dto.Capteurs;
import edu.ezip.ing1.pds.services.CapteurService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CapteurUI {

    private CapteurService capteurService;

    public CapteurUI(Capteurs capteurs, CapteurService capteurService) {
        this.capteurService = capteurService;
        SwingUtilities.invokeLater(() -> createAndShowGUI(capteurs));
    }

    private void createAndShowGUI(Capteurs capteurs) {
        JTextField idField = new JTextField(5);
        JButton rechercheButton = new JButton("Rechercher");
        JLabel statutLabel = new JLabel("Statut : ");
        JLabel presenceLabel = new JLabel("Présence : ");
        JLabel problemeLabel = new JLabel("Problème détecté : ");

        // Création d'une liste déroulante (JComboBox) pour afficher les IDs existants
        JComboBox<String> idComboBox = new JComboBox<>();

        // Remplir la JComboBox avec les IDs des capteurs existants
        for (Capteur capteur : capteurs.getCapteurs()) {
            idComboBox.addItem(capteur.getId());
        }

        // Formulaire pour la recherche par ID avec la liste déroulante
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("ID Capteur:"));
        searchPanel.add(idComboBox); // Remplacer le JTextField par le JComboBox
        searchPanel.add(rechercheButton);

        // Panel pour afficher les informations du capteur
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.add(statutLabel);
        resultPanel.add(presenceLabel);
        resultPanel.add(problemeLabel);

        // Recherche du capteur par ID lorsque l'on appuie sur le bouton "Rechercher"
        rechercheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = (String) idComboBox.getSelectedItem(); // Récupérer l'ID sélectionné dans le JComboBox
                if (id != null && !id.isEmpty()) {
                    // Recherche du capteur par ID
                    Capteur capteur = null;
                    for (Capteur c : capteurs.getCapteurs()) {
                        if (c.getId().equals(id)) {
                            capteur = c;
                            break;
                        }
                    }

                    if (capteur != null) {
                        // Affichage des informations du capteur trouvé
                        statutLabel.setText("Statut : " + (capteur.getStatut() ? "Actif" : "Inactif"));
                        presenceLabel.setText("Présence : " + (capteur.getPresence() ? "Oui" : "Non"));
                        problemeLabel.setText("Problème détecté : " + (capteur.getDetectionProbleme() ? "Oui" : "Non"));
                    } else {
                        JOptionPane.showMessageDialog(null, "Capteur non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un ID de capteur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // JFrame pour l'interface
        JFrame frame = new JFrame("Gestion des Capteurs");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(resultPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
