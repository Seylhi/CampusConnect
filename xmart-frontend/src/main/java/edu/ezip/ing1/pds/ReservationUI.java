package edu.ezip.ing1.pds;

import edu.ezip.ing1.pds.business.dto.Reservation;
import edu.ezip.ing1.pds.business.dto.Reservations;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.services.ReservationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Time;
import java.util.Date;

public class ReservationUI {

    private ReservationService reservationService;

    public ReservationUI(NetworkConfig networkConfig) {
        this.reservationService = new ReservationService(networkConfig);
    }

    public void afficherReservations(Reservations reservations) {
        // Créer la fenêtre principale avec deux boutons
        JFrame frame = new JFrame("Gestion des Réservations");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Créer un panel avec un layout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));

        // Boutons pour afficher la liste des réservations et créer une nouvelle réservation
        JButton afficherButton = new JButton("Liste des Réservations");
        JButton creerButton = new JButton("Créer Nouvelle Réservation");

        // ActionListener pour afficher la liste des réservations
        afficherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Appeler la méthode pour afficher la liste des réservations
                afficherListeReservations();
            }
        });

        // ActionListener pour créer une nouvelle réservation
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ouvrir un formulaire pour créer une nouvelle réservation
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

    // Méthode pour afficher la liste des réservations
    private void afficherListeReservations() {
        Reservations reservations;
        try {
            reservations = reservationService.selectReservations();
        } catch (InterruptedException | IOException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des réservations : " + e.getMessage());
            return;
        }

        JFrame frame = new JFrame("Liste des Réservations");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Nom", "Date", "Heure de début", "Heure de fin", "Type", "Description"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        // Ajouter les données des réservations
        if (reservations != null && reservations.getReservations() != null && !reservations.getReservations().isEmpty()) {
            for (Reservation reservation : reservations.getReservations()) {
                Object[] row = {
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getDate(),
                        reservation.getHeuredeb(),
                        reservation.getHeurefin(),
                        reservation.getType(),
                        reservation.getDescription()
                };
                tableModel.addRow(row);
            }
        } else {
            tableModel.addRow(new Object[]{"", "Aucune réservation trouvée", "", "", "", "", ""});
        }

        // Ajout du bouton "Modifier"
        JButton modifierButton = new JButton("Modifier Réservation");
        modifierButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner une réservation.");
                return;
            }

            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String nom = (String) tableModel.getValueAt(selectedRow, 1);
            Date date = (Date) tableModel.getValueAt(selectedRow, 2);
            Time heureDebut = (Time) tableModel.getValueAt(selectedRow, 3);
            Time heureFin = (Time) tableModel.getValueAt(selectedRow, 4);
            String type = (String) tableModel.getValueAt(selectedRow, 5);
            String description = (String) tableModel.getValueAt(selectedRow, 6);

            Reservation reservation = new Reservation();
            reservation.setId(id);
            reservation.setName(nom);
            reservation.setDate(date);
            reservation.setHeuredeb(Time.valueOf(String.valueOf(heureDebut)));
            reservation.setHeurefin(Time.valueOf(String.valueOf(heureFin)));
            reservation.setType(type);
            reservation.setDescription(description);

            afficherFormulaireModification(reservation, tableModel, selectedRow);
        });

        // Ajout du bouton "Supprimer"
        JButton supprimerButton = new JButton("Supprimer Réservation");
        supprimerButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner une réservation à supprimer.");
                return;
            }

            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String nom = (String) tableModel.getValueAt(selectedRow, 1);

            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Voulez-vous vraiment supprimer la réservation " + nom + " ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Reservation reservation = new Reservation();
                    reservation.setId(id);

                    reservationService.deleteReservation(reservation);
                    JOptionPane.showMessageDialog(frame, "Réservation supprimée avec succès !");

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

    // Méthode pour afficher un formulaire de création de réservation
    private void afficherFormulaireCreation() {
        JFrame frame = new JFrame("Créer une Réservation");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        // Panel pour contenir les champs du formulaire
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));

        // Champs pour saisir les informations de la réservation
        panel.add(new JLabel("Nom :"));
        JTextField nomField = new JTextField();
        panel.add(nomField);

        panel.add(new JLabel("Date (dd-MM-yyyy) :"));
        JTextField dateField = new JTextField();
        panel.add(dateField);

        panel.add(new JLabel("Heure de début (HH:mm:ss) :"));
        JTextField heureDebutField = new JTextField();
        panel.add(heureDebutField);

        panel.add(new JLabel("Heure de fin (HH:mm:ss) :"));
        JTextField heureFinField = new JTextField();
        panel.add(heureFinField);

        panel.add(new JLabel("Type :"));
        JTextField typeField = new JTextField();
        panel.add(typeField);

        panel.add(new JLabel("Description :"));
        JTextField descriptionField = new JTextField();
        panel.add(descriptionField);

        // Bouton pour valider la création
        JButton createButton = new JButton("Créer");
        panel.add(createButton);

        // ActionListener pour valider la création
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nom = nomField.getText();
                    String dateStr = dateField.getText();
                    String heureDebutStr = heureDebutField.getText();
                    String heureFinStr = heureFinField.getText();
                    String type = typeField.getText();
                    String description = descriptionField.getText();

                    // Conversion des champs
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = dateFormat.parse(dateStr);

                    Time heureDebut = Time.valueOf(heureDebutStr + ":00");
                    Time heureFin = Time.valueOf(heureFinStr + ":00");

                    // Créer un objet Reservation
                    Reservation reservation = new Reservation();
                    reservation.setName(nom);
                    reservation.setDate(date);
                    reservation.setHeuredeb(heureDebut);
                    reservation.setHeurefin(heureFin);
                    reservation.setType(type);
                    reservation.setDescription(description);

                    // Appeler la méthode du service pour ajouter la réservation à la base de données
                    reservationService.insertReservation(reservation);
                    JOptionPane.showMessageDialog(frame, "Réservation créée avec succès !");
                } catch (ParseException parseEx) {
                    JOptionPane.showMessageDialog(frame, "Erreur de format pour la date : " + parseEx.getMessage());
                } catch (IllegalArgumentException iaEx) {
                    JOptionPane.showMessageDialog(frame, "Erreur de format pour l'heure : " + iaEx.getMessage());
                } catch (IOException | InterruptedException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de la création de la réservation : " + ex.getMessage());
                }

                frame.dispose();  // Fermer le formulaire après création
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private void afficherFormulaireModification(Reservation reservation, DefaultTableModel tableModel, int selectedRow) {
        JFrame frame = new JFrame("Modifier une Réservation");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));

        panel.add(new JLabel("ID Réservation :"));
        JTextField idField = new JTextField(String.valueOf(reservation.getId()));
        idField.setEditable(false);
        panel.add(idField);

        panel.add(new JLabel("Nom :"));
        JTextField nomField = new JTextField(reservation.getName());
        panel.add(nomField);

        panel.add(new JLabel("Date (yyyy-MM-dd) :"));
        JTextField dateField = new JTextField(reservation.getDate().toString());
        panel.add(dateField);

        panel.add(new JLabel("Heure de début (HH:mm:ss) :"));
        JTextField heureDebutField = new JTextField(reservation.getHeuredeb().toString());
        panel.add(heureDebutField);

        panel.add(new JLabel("Heure de fin (HH:mm:ss) :"));
        JTextField heureFinField = new JTextField(reservation.getHeurefin().toString());
        panel.add(heureFinField);

        panel.add(new JLabel("Type :"));
        JTextField typeField = new JTextField(reservation.getType());
        panel.add(typeField);

        panel.add(new JLabel("Description :"));
        JTextField descriptionField = new JTextField(reservation.getDescription());
        panel.add(descriptionField);

        JButton updateButton = new JButton("Mettre à jour");
        panel.add(updateButton);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    reservation.setName(nomField.getText());
                    reservation.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dateField.getText()));
                    reservation.setHeuredeb(Time.valueOf(heureDebutField.getText() + ":00"));
                    reservation.setHeurefin(Time.valueOf(heureFinField.getText() + ":00"));
                    reservation.setType(typeField.getText());
                    reservation.setDescription(descriptionField.getText());

                    reservationService.updateReservation(reservation);
                    tableModel.setValueAt(reservation.getName(), selectedRow, 1);
                    tableModel.setValueAt(reservation.getDate(), selectedRow, 2);
                    tableModel.setValueAt(reservation.getHeuredeb(), selectedRow, 3);
                    tableModel.setValueAt(reservation.getHeurefin(), selectedRow, 4);
                    tableModel.setValueAt(reservation.getType(), selectedRow, 5);
                    tableModel.setValueAt(reservation.getDescription(), selectedRow, 6);
                    JOptionPane.showMessageDialog(frame, "Réservation mise à jour avec succès !");
                    frame.dispose();
                } catch (ParseException parseEx) {
                    JOptionPane.showMessageDialog(frame, "Erreur de format pour la date : " + parseEx.getMessage());
                } catch (IllegalArgumentException iaEx) {
                    JOptionPane.showMessageDialog(frame, "Erreur de format pour l'heure : " + iaEx.getMessage());
                } catch (IOException | InterruptedException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de la mise à jour : " + ex.getMessage());
                }
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}