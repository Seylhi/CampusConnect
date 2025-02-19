package edu.ezip.ing1.pds.business.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import edu.ezip.ing1.pds.business.dto.Student;
//import edu.ezip.ing1.pds.business.dto.Students;
import edu.ezip.ing1.pds.business.dto.Utilisateur;
import edu.ezip.ing1.pds.business.dto.Utilisateurs;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.commons.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class XMartCityService {

    private final static String LoggingLabel = "B u s i n e s s - S e r v e r";
    private final Logger logger = LoggerFactory.getLogger(LoggingLabel);

    private enum Queries {
        //SELECT_ALL_STUDENTS("SELECT t.name, t.firstname, t.groupname, t.id FROM students t"),
        //INSERT_STUDENT("INSERT into students (name, firstname, groupname) values (?, ?, ?)");
        SELECT_ALL_UTILISATEURS("SELECT t.nom_utilisateur, t.nom, t.prenom, t.email, t.id_utilisateur FROM utilisateurs t"),
        INSERT_UTILISATEUR("INSERT INTO utilisateurs (nom_utilisateur, nom, prenom, email, password) VALUES (?, ?, ?, ?, ?)");
        private final String query;

        private Queries(final String query) {
            this.query = query;
        }
    }

    public static XMartCityService inst = null;
    public static final XMartCityService getInstance()  {
        if(inst == null) {
            inst = new XMartCityService();
        }
        return inst;
    }

    private XMartCityService() {

    }

    public final Response dispatch(final Request request, final Connection connection)
            throws InvocationTargetException, IllegalAccessException, SQLException, IOException {
        Response response = null;

        final Queries queryEnum = Enum.valueOf(Queries.class, request.getRequestOrder());
        switch(queryEnum) {

            case SELECT_ALL_UTILISATEURS:
                response = selectAllUtilisateurs(request, connection);
                break;
            case INSERT_UTILISATEUR:
                response = insertUtilisateur(request, connection);
                break;
            default:
                break;
        }

        return response;
    }

/*  private Response InsertStudent(final Request request, final Connection connection) throws SQLException, IOException {
//
//        final ObjectMapper objectMapper = new ObjectMapper();
//        final Student student = objectMapper.readValue(request.getRequestBody(), Student.class);
//
//        final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_STUDENT.query);
//        stmt.setString(1, student.getName());
//        stmt.setString(2, student.getFirstname());
//        stmt.setString(3, student.getGroup());
//        stmt.executeUpdate();
//
//        final Statement stmt2 = connection.createStatement();
//        final ResultSet res = stmt2.executeQuery("SELECT LAST_INSERT_ID()");
//        res.next();
//
//        student.setId(res.getInt(1));
//
//        return new Response(request.getRequestId(), objectMapper.writeValueAsString(student));
//    }*/

    private Response insertUtilisateur(final Request request, final Connection connection) throws SQLException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Utilisateur utilisateur = objectMapper.readValue(request.getRequestBody(), Utilisateur.class);

        final PreparedStatement stmt = connection.prepareStatement(Queries.INSERT_UTILISATEUR.query);
        stmt.setString(1, utilisateur.getNomUtilisateur());
        stmt.setString(2, utilisateur.getNom());
        stmt.setString(3, utilisateur.getPrenom());
        stmt.setString(4, utilisateur.getEmail());
        stmt.setString(5, utilisateur.getPassword());
        stmt.executeUpdate();

        final Statement stmt2 = connection.createStatement();
        final ResultSet res = stmt2.executeQuery("SELECT LAST_INSERT_ID()");
        res.next();

        utilisateur.setIdUtilisateur(res.getString(1));

        return new Response(request.getRequestId(), objectMapper.writeValueAsString(utilisateur));
    }


    /*private Response SelectAllUsers(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
       final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_STUDENTS.query);
        Utilisateur students = new Students();
        while (res.next()) {
            Student student = new Student();
            student.setName(res.getString(1));
            student.setFirstname(res.getString(2));
            student.setGroup(res.getString(3));
            student.setId(res.getInt(4));
            students.add(student);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(students));
    }*/

    private Response selectAllUtilisateurs(final Request request, final Connection connection) throws SQLException, JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Statement stmt = connection.createStatement();
        final ResultSet res = stmt.executeQuery(Queries.SELECT_ALL_UTILISATEURS.query);
        Utilisateurs utilisateurs = new Utilisateurs();
        while (res.next()) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNomUtilisateur(res.getString(1));
            utilisateur.setNom(res.getString(2));
            utilisateur.setPrenom(res.getString(3));
            utilisateur.setEmail(res.getString(4));
            utilisateur.setIdUtilisateur(res.getString(5));
            utilisateurs.add(utilisateur);
        }
        return new Response(request.getRequestId(), objectMapper.writeValueAsString(utilisateurs));
    }

}