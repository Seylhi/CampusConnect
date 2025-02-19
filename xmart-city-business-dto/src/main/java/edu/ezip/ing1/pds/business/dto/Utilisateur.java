package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@JsonRootName(value = "utilisateur")
public class Utilisateur {
    private String idUtilisateur;
    private String nomUtilisateur;
    private String email;
    private String password;
    private Date dateCreation;
    private String nom;
    private String prenom;
    private int age;
    private Date dateDeNaissance;
    private String sexe;

    public Utilisateur() {}

    public final Utilisateur build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResultSet(resultSet, "idUtilisateur", "nomUtilisateur", "email", "password", "dateCreation", "nom", "prenom", "age", "dateDeNaissance", "sexe");
        return this;
    }

    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, idUtilisateur, nomUtilisateur, email, password, dateCreation.toString(), nom, prenom, String.valueOf(age), dateDeNaissance.toString(), sexe);
    }

    public Utilisateur(String idUtilisateur, String nomUtilisateur, String email, String password, Date dateCreation,
                       String nom, String prenom, int age, Date dateDeNaissance, String sexe) {
        this.idUtilisateur = idUtilisateur;
        this.nomUtilisateur = nomUtilisateur;
        this.email = email;
        this.password = password;
        this.dateCreation = dateCreation;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.dateDeNaissance = dateDeNaissance;
        this.sexe = sexe;
    }

    public String getIdUtilisateur() { return idUtilisateur; }
    @JsonProperty("user_id")
    public void setIdUtilisateur(String idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public String getNomUtilisateur() { return nomUtilisateur; }
    @JsonProperty("username")
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }

    public String getEmail() { return email; }
    @JsonProperty("user_email")
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    @JsonProperty("user_password")
    public void setPassword(String password) { this.password = password; }

    public Date getDateCreation() { return dateCreation; }
    @JsonProperty("creation_date")
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }

    public String getNom() { return nom; }
    @JsonProperty("first_name")
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    @JsonProperty("last_name")
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public int getAge() { return age; }
    @JsonProperty("user_age")
    public void setAge(int age) { this.age = age; }

    public Date getDateDeNaissance() { return dateDeNaissance; }
    @JsonProperty("birth_date")
    public void setDateDeNaissance(Date dateDeNaissance) { this.dateDeNaissance = dateDeNaissance; }

    public String getSexe() { return sexe; }
    @JsonProperty("gender")
    public void setSexe(String sexe) { this.sexe = sexe; }

    private void setFieldsFromResultSet(final ResultSet resultSet, final String ... fieldNames )
            throws NoSuchFieldException, SQLException, IllegalAccessException {
        for(final String fieldName : fieldNames ) {
            final Field field = this.getClass().getDeclaredField(fieldName);
            field.set(this, resultSet.getObject(fieldName));
        }
    }

    private final PreparedStatement buildPreparedStatement(PreparedStatement preparedStatement, final String ... fieldValues )
            throws SQLException {
        int ix = 0;
        for(final String fieldValue : fieldValues ) {
            preparedStatement.setString(++ix, fieldValue);
        }
        return preparedStatement;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "idUtilisateur='" + idUtilisateur + '\'' +
                ", nomUtilisateur='" + nomUtilisateur + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dateCreation=" + dateCreation +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", age=" + age +
                ", dateDeNaissance=" + dateDeNaissance +
                ", sexe='" + sexe + '\'' +
                '}';
    }
}
