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


    public Capteur() {
    }
    public final Capteur build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResulset(resultSet,"id");
        return this;
    }
    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, id);
    }
    public Capteur( String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }



    @JsonProperty("capteur_id")
    public void setId(String id) {
        this.id = id;
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
        return "Student{" +
                ", id='" + id + '\'' +
                '}';
    }
}
