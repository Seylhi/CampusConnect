package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@JsonRootName(value = "capteur")
public class Capteur {
    private String id;
    private boolean statut;
    private boolean presence;
    private boolean detectionProbleme;


    public Capteur() {
    }
    public final Capteur build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet,"id_capteur", "statut", "presence", "detection_probleme");
        return this;
    }
    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, id , String.valueOf(statut) , String.valueOf(presence) , String.valueOf(detectionProbleme));
    }
    public Capteur( String id, boolean statut, boolean presence, boolean detectionProbleme) {
        this.id = id;
        this.statut = statut;
        this.presence = presence;
        this.detectionProbleme = detectionProbleme;
    }

    public String getId() {
        return id;
    }
    public boolean getStatut() {return statut;}
    public boolean getPresence() {return presence;}
    public boolean getDetectionProbleme() {return detectionProbleme;}



    @JsonProperty("capteur_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("capteur_statut")
    public void setStatut(boolean statut) {
        this.statut = statut;
    }

    @JsonProperty("capteur_presence")
    public void setPresence(boolean presence) {
        this.presence = presence;
    }

    @JsonProperty("capteur_detectionProbleme")
    public void setDetectionProbleme(boolean detectionProbleme) {
        this.detectionProbleme = detectionProbleme;
    }

    private void setFieldsFromResulset(final ResultSet resultSet, final String ... fieldNames )
            throws NoSuchFieldException, SQLException, IllegalAccessException {
        for(final String fieldName : fieldNames ) {
            final Field field = this.getClass().getDeclaredField(fieldName);
            field.set(this, resultSet.getObject(fieldName));
        }
    }
    private final PreparedStatement buildPreparedStatement(PreparedStatement preparedStatement, final String ... fieldNames )
            throws NoSuchFieldException, SQLException, IllegalAccessException {
        int ix = 0;
        for(final String fieldName : fieldNames ) {
            preparedStatement.setString(++ix, fieldName);
        }
        return preparedStatement;
    }

    @Override
    public String toString() {
        return "Capteur{" +
                ", id='" + id + '\'' +
                '}';
    }
}
