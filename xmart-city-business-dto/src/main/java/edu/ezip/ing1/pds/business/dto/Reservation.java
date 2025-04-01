package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

@JsonRootName(value = "reservation")
public class Reservation {
    private int id;
    private String name;
    private Date date;
    private Time heuredeb;
    private Time heurefin;
    private String type;
    private String description;


    public Reservation() {
    }
    public final Reservation build(final ResultSet resultSet)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        setFieldsFromResultSet(resultSet, "Name_resa", "Date_resa", "Heure_deb_resa", "Heure_fin_resa" , "Type_resa", "Description_resa","Id_resa");
        return this;
    }
    public final PreparedStatement build(PreparedStatement preparedStatement)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        return buildPreparedStatement(preparedStatement, String.valueOf(id), name,date.toString(),heuredeb.toString(),heurefin.toString(),type,description);
    }
    public Reservation(int id, String name,Date date,Time heuredeb, Time heurefin,String type, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.heuredeb = heuredeb;
        this.heurefin = heurefin;
        this.type = type;
        this.description = description;
    }

    public int getId() { return id; }
    public String getName() {return name;}
    public Date getDate() { return date; }
    public Time getHeuredeb() { return heuredeb; }
    public Time getHeurefin() { return heurefin; }
    public String getType() { return type; }
    public String getDescription() { return description; }

    @JsonProperty("Name_resa")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Date_resa")
    public void setDate(Date date) {
        this.date = date;
    }

    @JsonProperty("Heure_deb_resa")
    public void setHeuredeb(Time heuredeb) {
        this.heuredeb = heuredeb;
    }

    @JsonProperty("Heure_fin_resa")
    public void setHeurefin(Time heurefin) {
        this.heurefin = heurefin;
    }

    @JsonProperty("Type_resa")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("Description_resa")
    public void setDescription(String description) {
        this.description = description;


    }

    @JsonProperty("Id_resa")
    public void setId(int id) {
        this.id = id;
    }

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
        return "Salle{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                "date=" + date +
                "heuredeb=" + heuredeb +
                "heurefin=" + heurefin +
                "type='" + type + '\'' +
                "description='" + description + '\'' +
                '}';
    }
}