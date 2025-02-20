package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Utilisateur;
import edu.ezip.ing1.pds.business.dto.Utilisateurs;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.InsertUtilisateursClientRequest;
import edu.ezip.ing1.pds.requests.SelectAllUtilisateursClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class UtilisateurService {
    private final static String LoggingLabel = "FrontEnd - UtilisateurService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);
    private final static String utilisateursToBeInserted = "Utilisateurs-to-be-inserted.yaml";

    final String insertRequestOrder = "INSERT_UTILISATEUR";
    final String selectRequestOrder = "SELECT_ALL_UTILISATEURS";

    private final NetworkConfig networkConfig;

    public UtilisateurService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertUtilisateurs() throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final Utilisateurs users = ConfigLoader.loadConfig(Utilisateurs.class, utilisateursToBeInserted);

        int birthdate = 0;
        for(final Utilisateur user : users.getUtilisateurs()) {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonifiedUser = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
            logger.trace("Utilisateur with its JSON face : {}", jsonifiedUser);
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();
            request.setRequestId(requestId);
            request.setRequestOrder(insertRequestOrder);
            request.setRequestContent(jsonifiedUser);
            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

            final InsertUtilisateursClientRequest clientRequest = new InsertUtilisateursClientRequest(
                    networkConfig, birthdate++, request, user, requestBytes);
            clientRequests.push(clientRequest);
        }

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest = clientRequests.pop();
            clientRequest.join();
            final Utilisateur user = (Utilisateur)clientRequest.getInfo();
            logger.debug("Thread {} complete : {} {} {} --> {}",
                    clientRequest.getThreadName(),
                    user.getPrenom(), user.getNom(), user.getEmail(),
                    clientRequest.getResult());
        }
    }

    public Utilisateurs selectUtilisateurs() throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectRequestOrder);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);

        final SelectAllUtilisateursClientRequest clientRequest = new SelectAllUtilisateursClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();


            if (joinedClientRequest.getResult() != null) {
                Utilisateurs utilisateurs = (Utilisateurs) joinedClientRequest.getResult();


                for (Utilisateur user : utilisateurs.getUtilisateurs()) {

                }
                return utilisateurs;
            } else {
                logger.warn("Aucun utilisateur récupéré.");
                return null;
            }
        } else {
            logger.error("Erreur requete");
            return null;
        }
    }

}
