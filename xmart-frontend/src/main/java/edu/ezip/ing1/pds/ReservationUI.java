package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.Reservation;
import edu.ezip.ing1.pds.business.dto.Reservations;
import edu.ezip.ing1.pds.services.ReservationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ReservationUI {

    private ReservationService reservationService;

    public ReservationUI(Reservations reservations, ReservationService reservationService) {
        this.reservationService = reservationService;
        SwingUtilities.invokeLater(() -> createAndShowGUI(reservations));
    }
    // informations que l'on va sortir dans notre table
    private void createAndShowGUI(Reservations reservations) {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID Réservation");
        tableModel.addColumn("Nom");
        tableModel.addColumn("Date");
        tableModel.addColumn("Heure Début");
        tableModel.addColumn("Heure Fin");
        tableModel.addColumn("Type");
        tableModel.addColumn("Description");

        // vérifier que les informations de "réservation", ne sont pas nulles pour ajouter les informations
        if (reservations != null && reservations.getReservations() != null && !reservations.getReservations().isEmpty()) {
            for (Reservation reservation : reservations.getReservations()) {
                tableModel.addRow(new Object[]{
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getDate(),
                        reservation.getHeuredeb(),
                        reservation.getHeurefin(),
                        reservation.getType(),
                        reservation.getDescription()
                });
            }
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JTextField idField = new JTextField(5);
        JTextField nameField = new JTextField(10);
        JTextField dateField = new JTextField(10);
        JTextField heureDebField = new JTextField(10);
        JTextField heureFinField = new JTextField(10);
        JTextField typeField = new JTextField(10);
        JTextField descriptionField = new JTextField(15);
        JButton ajouterButton = new JButton("Ajouter");
        JButton rafraichirButton = new JButton("Rafraîchir la liste");

        // potentiellement pour ajouter des réservations à l'avenir
        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("ID Réservation:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Nom:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Date:"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Heure Début:"));
        formPanel.add(heureDebField);
        formPanel.add(new JLabel("Heure Fin:"));
        formPanel.add(heureFinField);
        formPanel.add(new JLabel("Type:"));
        formPanel.add(typeField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);
        formPanel.add(ajouterButton);
        //formPanel.add(rafraichirButton);

        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                String date = dateField.getText().trim();
                String heureDeb = heureDebField.getText().trim();
                String heureFin = heureFinField.getText().trim();
                String type = typeField.getText().trim();
                String description = descriptionField.getText().trim();

                if (!id.isEmpty()) {
                    try {
                        Reservation reservation = new Reservation(id, name, java.sql.Date.valueOf(date),
                                java.sql.Time.valueOf(heureDeb), java.sql.Time.valueOf(heureFin), type, description);
                        reservationService.insertReservation(reservation);
                        tableModel.addRow(new Object[]{id, name, date, heureDeb, heureFin, type, description});
                        idField.setText("");
                        nameField.setText("");
                        dateField.setText("");
                        heureDebField.setText("");
                        heureFinField.setText("");
                        typeField.setText("");
                        descriptionField.setText("");
                    } catch (IOException | InterruptedException ex) {
                        JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de la réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //rafraichirButton.addActionListener(e -> table.repaint());

        JFrame frame = new JFrame("Gestion des Réservations");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.add(formPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
