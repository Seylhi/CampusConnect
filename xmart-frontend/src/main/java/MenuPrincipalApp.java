import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MenuPrincipalApp {

        private static String nomFichier = "accounts.log";
        private static ArrayList<AppareilConnecte> appareils = new ArrayList<>();

        public static void main(String[] args) {
            // Ajout des appareils connectés
            appareils.add(new Radiateur("Radiateur Salon"));
            appareils.add(new SecheServiettes("Sèche-Serviettes Salle de Bain"));
            appareils.add(new Radiateur("Radiateur Chambre Parentale"));
            appareils.add(new Radiateur("Radiateur Chambre Enfant"));
            appareils.add(new SecheServiettes("Sèche-Serviettes WC"));
            appareils.add(new Radiateur("Radiateur Bureau"));

            SwingUtilities.invokeLater(MenuPrincipalApp::creerInterface);
        }

        private static void creerInterface() {
            JFrame frame = new JFrame("Gestion des Appareils Connectés");
            frame.setSize(300, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            // Création du panel principal
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Ajout des composants
            JLabel labelUsername = new JLabel("Nom d'utilisateur :");
            JTextField texteUsername = new JTextField();

            JLabel labelPassword = new JLabel("Mot de Passe :");
            JPasswordField textePassword = new JPasswordField();

            panel.add(labelUsername);
            panel.add(texteUsername);
            panel.add(labelPassword);
            panel.add(textePassword);

            JButton boutonConnexion = new JButton("Connexion");
            JButton boutonInscription = new JButton("Créer un compte");

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(boutonConnexion);
            buttonPanel.add(boutonInscription);


            // Gestionnaires d'événements

            boutonConnexion.addActionListener(e -> {
                String username = texteUsername.getText();
                char[] password = textePassword.getPassword();

                try (BufferedReader fileBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(nomFichier)))) {
                    String line;
                    boolean authReussi = false;

                    while ((line = fileBufferedReader.readLine()) != null) {
                        String[] parts = line.split(", ");
                        if (parts.length == 2) {
                            String fileUsername = parts[0].split(": ")[1];
                            String filePassword = parts[1].split(": ")[1];

                            if (fileUsername.equals(username) && filePassword.equals(new String(password))) {
                                authReussi = true;
                                break;
                            }
                        }
                    }

                    if (authReussi) {
                        JOptionPane.showMessageDialog(frame, "Connexion réussie !");
                        frame.dispose();
                        menuPrincipalIHM();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Nom d'utilisateur ou mot de passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de la connexion.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });


            boutonInscription.addActionListener(e -> creerCompteIHM());

            // Ajout des panels à la fenêtre
            frame.add(panel, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);

        }



        // Création de compte

        private static void creerCompteIHM() {
            JFrame frame = new JFrame("Créer un compte");
            frame.setSize(300, 200);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(4, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel lblUsername = new JLabel("Nom d'utilisateur :");
            JTextField txtUsername = new JTextField();

            JLabel lblPassword = new JLabel("Mot de passe :");
            JPasswordField txtPassword = new JPasswordField();

            JLabel lblConfirmPassword = new JLabel("Confirmer le mot de passe");
            JPasswordField txtConfirmPassword = new JPasswordField();

            panel.add(lblUsername);
            panel.add(txtUsername);
            panel.add(lblPassword);
            panel.add(txtPassword);
            panel.add(lblConfirmPassword);
            panel.add(txtConfirmPassword);


            JButton boutonCreer = new JButton("Créer");
            JButton boutonRetour = new JButton("Retour");

            JPanel boutonPanel = new JPanel();
            boutonPanel.add(boutonCreer);
            boutonPanel.add(boutonRetour);

            boutonCreer.addActionListener(e -> {
                String username = txtUsername.getText();
                char[] password = txtPassword.getPassword();
                char [] confirmPassword = txtConfirmPassword.getPassword();

                if (!Arrays.equals(password, confirmPassword)) {
                    JOptionPane.showMessageDialog(frame,"Les mots de passe ne correspondent pas",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nomFichier, true)))) {
                    bufferedWriter.write("Username: " + username + ", Password: " + new String(password));
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    JOptionPane.showMessageDialog(frame, "Compte créé avec succès !");
                    frame.dispose();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de la création du compte.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });


            boutonRetour.addActionListener(e -> frame.dispose());

            // Ajout des panels à la fenêtre
            frame.add(panel, BorderLayout.CENTER);
            frame.add(boutonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        }




        private static void menuPrincipalIHM() {
            JFrame frame = new JFrame("Menu Principal");
            frame.setSize(400, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(7, 1, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel lblBienvenue = new JLabel("Bienvenue dans le menu principal !");
            lblBienvenue.setHorizontalAlignment(SwingConstants.CENTER); // Centrer horizontalement
            lblBienvenue.setVerticalAlignment(SwingConstants.TOP);      // Placer en haut
            panel.add(lblBienvenue);

            JButton btnReglerTemperature = new JButton("Régler température");
            btnReglerTemperature.addActionListener(e -> reglerTemperatureIHM());
            JButton btnAjouterPlanification = new JButton("Ajouter planification");
            btnAjouterPlanification.addActionListener(e -> ajouterPlanificationIHM());
            JButton btnAfficherPlanification = new JButton("Afficher planification");
            btnAfficherPlanification.addActionListener(e -> afficherPlanificationIHM());
            JButton btnConsommation = new JButton("Consommation");
            btnConsommation.addActionListener(e -> afficherConsommationIHM());
            JButton btnDetecterProbleme = new JButton("Détecter problème");
            btnDetecterProbleme.addActionListener(e -> detecterProblemeIHM());
            JButton btnQuitter = new JButton("Quitter");
            btnQuitter.addActionListener(e -> frame.dispose());

            panel.add(btnReglerTemperature);
            panel.add(btnAjouterPlanification);
            panel.add(btnAfficherPlanification);
            panel.add(btnConsommation);
            panel.add(btnDetecterProbleme);
            panel.add(btnQuitter);

            frame.add(panel);
            frame.setVisible(true);
        }

        private static void reglerTemperatureIHM() {
            JFrame frame = new JFrame("Régler Température");
            frame.setSize(300, 200);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Ajout des composants
            JLabel lblNomAppareil = new JLabel("Nom de l'appareil :");
            JTextField txtNomAppareil = new JTextField();
            JLabel lblTemperature = new JLabel("Température :");
            JTextField txtTemperature = new JTextField();

            panel.add(lblNomAppareil);
            panel.add(txtNomAppareil);
            panel.add(lblTemperature);
            panel.add(txtTemperature);

            JButton btnRegler = new JButton("Régler");
            JButton btnRetour = new JButton("Retour");

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(btnRegler);
            buttonPanel.add(btnRetour);

            btnRegler.addActionListener(e -> {
                String nom = txtNomAppareil.getText();
                try {
                    double temperature = Double.parseDouble(txtTemperature.getText());
                    AppareilConnecte appareil = chercherAppareilParNom(nom);
                    if (appareil != null) {
                        appareil.setTemperature(temperature);
                        JOptionPane.showMessageDialog(frame, "Température réglée avec succès pour l'appareil " + appareil.getNom() + " à " + temperature + "°C !");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Appareil introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Température invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnRetour.addActionListener(e -> frame.dispose());

            frame.add(panel, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);

        }

        private static AppareilConnecte chercherAppareilParNom(String nom) {
            for (AppareilConnecte appareil : appareils) {
                if (appareil.getNom().equalsIgnoreCase(nom)) {
                    return appareil;
                }
            }
            return null;
        }


        private static void afficherPlanificationIHM() {
            // Fenêtre pour afficher la planification
            JFrame frame = new JFrame("Planification des Appareils");
            frame.setSize(500, 300);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(7, 1, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Affichage de la planification pour chaque appareil
            for (AppareilConnecte appareil : appareils) {
                JLabel labelAppareil = new JLabel(appareil.getNom());
                labelAppareil.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(labelAppareil);

                HashMap<String, Double> planification = appareil.getPlanification();
                if (planification.isEmpty()) {
                    JLabel labelNoPlanification = new JLabel("Aucune planification définie.");
                    labelNoPlanification.setAlignmentX(Component.CENTER_ALIGNMENT);
                    panel.add(labelNoPlanification);
                } else {
                    // Affichage de chaque heure et température dans la planification
                    planification.forEach((heure, temp) -> {
                        JLabel labelPlanification = new JLabel("Heure: " + heure + " - Température: " + temp + "°C");
                        labelPlanification.setAlignmentX(Component.CENTER_ALIGNMENT);
                        panel.add(labelPlanification);
                    });
                }
            }

            // Bouton pour fermer la fenêtre
            JButton btnRetour = new JButton("Retour");

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(btnRetour);

            btnRetour.addActionListener(e -> frame.dispose());

            frame.add(panel, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        }


        private static void afficherConsommationIHM() {
            // Fenêtre pour afficher la consommation
            JFrame frame = new JFrame("Consommation des Appareils");
            frame.setSize(500, 300);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(7, 1, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


            // Affichage de la consommation de chaque appareil
            for (AppareilConnecte appareil : appareils) {
                String consomm = appareil.getNom() + " : " + appareil.getConsommation() + " kWh";
                JLabel label = new JLabel(consomm);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(label);
            }

            // Bouton pour fermer la fenêtre
            JButton btnRetour = new JButton("Retour");

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(btnRetour);

            btnRetour.addActionListener(e -> frame.dispose());

            frame.add(panel, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        }


        private static void detecterProblemeIHM() {
            // Fenêtre pour afficher l'état du problème
            JFrame frame = new JFrame("Détection de Problème");
            frame.setSize(500, 250);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(4, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Demande à l'utilisateur de choisir un appareil
            JLabel lblNomAppareil = new JLabel("Nom de l'appareil :");
            JTextField txtNomAppareil = new JTextField();
            panel.add(lblNomAppareil);
            panel.add(txtNomAppareil);

            // Création des boutons pour détecter un problème
            JButton boutonProbleme = new JButton("Détecter Problème");
            JButton boutonRetour = new JButton("Retour");

            JPanel boutonPanel = new JPanel();
            boutonPanel.add(boutonProbleme);
            boutonPanel.add(boutonRetour);


            boutonProbleme.addActionListener(e -> {
                String nomAppareil = txtNomAppareil.getText();
                AppareilConnecte appareil = chercherAppareilParNom(nomAppareil);

                if (appareil != null) {
                    // Simule un problème aléatoire (true ou false)
                    boolean probleme = Math.random() > 0.5; // Valeur aléatoire pour démonstration

                    appareil.detecterProbleme(probleme);

                    String message = probleme ? appareil.getNom() + " a un problème détecté." : appareil.getNom() + " fonctionne normalement.";
                    JOptionPane.showMessageDialog(frame, message);
                } else {
                    JOptionPane.showMessageDialog(frame, "Appareil non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Bouton pour fermer la fenêtre

            boutonRetour.addActionListener(e -> frame.dispose());

            frame.add(panel, BorderLayout.CENTER);
            frame.add(boutonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
            frame.setVisible(true);
        }

        private static void ajouterPlanificationIHM() {
            // Fenêtre pour ajouter une planification
            JFrame frame = new JFrame("Ajouter Planification");
            frame.setSize(500, 250);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(4, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Ajout des composants
            JLabel lblNomAppareil = new JLabel("Nom de l'appareil :");
            JTextField txtNomAppareil = new JTextField();
            JLabel lblHeure = new JLabel("Heure (format HH:mm) :");
            JTextField txtHeure = new JTextField();
            JLabel lblTemperature = new JLabel("Température :");
            JTextField txtTemperature = new JTextField();

            panel.add(lblNomAppareil);
            panel.add(txtNomAppareil);
            panel.add(lblHeure);
            panel.add(txtHeure);
            panel.add(lblTemperature);
            panel.add(txtTemperature);

            JButton btnAjouter = new JButton("Ajouter");
            JButton btnRetour = new JButton("Retour");

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(btnAjouter);
            buttonPanel.add(btnRetour);


            btnAjouter.addActionListener(e -> {
                String nom = txtNomAppareil.getText();
                String heure = txtHeure.getText();
                try {
                    double temperature = Double.parseDouble(txtTemperature.getText());
                    AppareilConnecte appareil = chercherAppareilParNom(nom);
                    if (appareil != null) {
                        appareil.ajouterPlanification(heure, temperature);
                        JOptionPane.showMessageDialog(frame, "Planification ajoutée pour l'appareil " + appareil.getNom() + " à " + heure + " avec une température de " + temperature + "°C !");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Appareil introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Température invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnRetour.addActionListener(e -> frame.dispose());

            frame.add(panel, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        }

    }

    abstract class AppareilConnecte {
        private String nom;
        private double temperature;
        private HashMap<String, Double> planification = new HashMap<>();

        public AppareilConnecte(String nom) {
            this.nom = nom;
        }

        public String getNom() {
            return nom;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public void ajouterPlanification(String heure, double temp) {
            planification.put(heure, temp);
        }

        public HashMap<String, Double> getPlanification() {
            return planification;
        }

        public double getConsommation() {
            return temperature * 1.5;
        }

        public void detecterProbleme(boolean probleme) {

        }
    }

    class Radiateur extends AppareilConnecte {
        public Radiateur(String nom) {
            super(nom);
        }
    }

    class SecheServiettes extends AppareilConnecte {
        public SecheServiettes(String nom) {
            super(nom);
        }
    }

