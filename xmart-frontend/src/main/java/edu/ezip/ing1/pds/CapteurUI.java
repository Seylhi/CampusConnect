package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.Capteur;
import edu.ezip.ing1.pds.business.dto.Capteurs;
import edu.ezip.ing1.pds.services.CapteurService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CapteurUI {

    private CapteurService capteurService;

    public CapteurUI(Capteurs capteurs, CapteurService capteurService) {
        this.capteurService = capteurService;
        SwingUtilities.invokeLater(() -> createAndShowGUI(capteurs));
    }

    private void createAndShowGUI(Capteurs capteurs) {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID Capteur");
        tableModel.addColumn("Statut");
        tableModel.addColumn("Présence");
        tableModel.addColumn("Problème Détecté");

        if (capteurs != null && capteurs.getCapteurs() != null && !capteurs.getCapteurs().isEmpty()) {
            for (Capteur capteur : capteurs.getCapteurs()) {
                tableModel.addRow(new Object[]{capteur.getId(), capteur.getStatut() ? "Actif" : "Inactif", capteur.getPresence() ? "Oui" : "Non", capteur.getDetectionProbleme() ? "Oui" : "Non"});
            }
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JTextField idField = new JTextField(5);
        JCheckBox statutCheckBox = new JCheckBox("Actif");
        JCheckBox presenceCheckBox = new JCheckBox("Présent");
        JCheckBox problemeCheckBox = new JCheckBox("Problème détecté");
        JButton ajouterButton = new JButton("Ajouter");
        JButton rafraichirButton = new JButton("Rafraîchir la liste");

        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("ID Capteur:"));
        formPanel.add(idField);
        formPanel.add(statutCheckBox);
        formPanel.add(presenceCheckBox);
        formPanel.add(problemeCheckBox);
        formPanel.add(ajouterButton);
        formPanel.add(rafraichirButton);

        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText().trim();
                boolean statut = statutCheckBox.isSelected();
                boolean presence = presenceCheckBox.isSelected();
                boolean probleme = problemeCheckBox.isSelected();

                if (!id.isEmpty()) {
                    try {
                        Capteur capteur = new Capteur(id, statut, presence, probleme);
                        capteurService.insertCapteur(capteur);
                        tableModel.addRow(new Object[]{id, statut ? "Actif" : "Inactif", presence ? "Oui" : "Non", probleme ? "Oui" : "Non"});
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

        rafraichirButton.addActionListener(e -> table.repaint());

        JFrame frame = new JFrame("Gestion des Capteurs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.add(formPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
