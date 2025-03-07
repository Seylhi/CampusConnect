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

        // Formulaire pour la recherche par ID
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("ID Capteur:"));
        searchPanel.add(idField);
        searchPanel.add(rechercheButton);

        // Panel pour afficher les informations du capteur
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.add(statutLabel);
        resultPanel.add(presenceLabel);
        resultPanel.add(problemeLabel);

        // Recherche du capteur par ID
        rechercheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText().trim();
                if (!id.isEmpty()) {
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
                    JOptionPane.showMessageDialog(null, "Veuillez entrer un ID de capteur.", "Erreur", JOptionPane.ERROR_MESSAGE);
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