package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.Reservation;
import edu.ezip.ing1.pds.business.dto.Reservations;
import edu.ezip.ing1.pds.services.ReservationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class ReservationUI {

    private final ReservationService reservationService;

    public ReservationUI(Reservations reservations, ReservationService reservationService) {
        this.reservationService = reservationService;
        SwingUtilities.invokeLater(() -> createAndShowGUI(reservations));
    }

    private void createAndShowGUI(Reservations reservations) {
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{
                "ID Réservation", "Nom", "Date", "Heure Début", "Heure Fin", "Type", "Description"
        }, 0);

        if (reservations != null && reservations.getReservations() != null) {
            for (Reservation reservation : reservations.getReservations()) {
                tableModel.addRow(new Object[]{
                        reservation.getId(), reservation.getName(), reservation.getDate(),
                        reservation.getHeuredeb(), reservation.getHeurefin(), reservation.getType(),
                        reservation.getDescription()
                });
            }
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JPanel formPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        JTextField[] fields = new JTextField[7];
        String[] labels = {"ID Réservation:", "Nom:", "Date:", "Heure Début:", "Heure Fin:", "Type:", "Description:"};

        for (int i = 0; i < labels.length; i++) {
            formPanel.add(new JLabel(labels[i]));
            fields[i] = new JTextField(10);
            formPanel.add(fields[i]);
        }

        JButton ajouterButton = new JButton("Ajouter");
        JButton rafraichirButton = new JButton("Rafraîchir");

        ajouterButton.addActionListener(e -> {
            try {
                String id = fields[0].getText().trim();
                String name = fields[1].getText().trim();
                String date = fields[2].getText().trim();
                String heureDeb = fields[3].getText().trim();
                String heureFin = fields[4].getText().trim();
                String type = fields[5].getText().trim();
                String description = fields[6].getText().trim();

                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Reservation reservation = new Reservation(id, name, java.sql.Date.valueOf(date),
                        java.sql.Time.valueOf(heureDeb), java.sql.Time.valueOf(heureFin), type, description);
                reservationService.insertReservation(reservation);
                tableModel.addRow(new Object[]{id, name, date, heureDeb, heureFin, type, description});

                for (JTextField field : fields) field.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de la réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Données invalides.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        rafraichirButton.addActionListener(e -> table.repaint());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(ajouterButton);
        buttonPanel.add(rafraichirButton);

        JFrame frame = new JFrame("Gestion des Réservations");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.add(formPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
