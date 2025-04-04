package edu.ezip.ing1.pds.services;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Utilisateur;
import edu.ezip.ing1.pds.business.dto.Utilisateurs;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.InsertUtilisateursClientRequest;
import edu.ezip.ing1.pds.requests.SelectAllUtilisateursClientRequest;
import edu.ezip.ing1.pds.requests.UpdateUtilisateursClientRequest;
import edu.ezip.ing1.pds.requests.DeleteUtilisateursClientRequest;

public class UtilisateurService {

    private final static String LoggingLabel = "FrontEnd - UtilisateurService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_UTILISATEUR";
    final String selectRequestOrder = "SELECT_ALL_UTILISATEURS";
    final String updateRequestOrder = "UPDATE_UTILISATEUR";
    final String deleteRequestOrder = "DELETE_UTILISATEUR";

    private final NetworkConfig networkConfig;

    public UtilisateurService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    /**
     * Insère un utilisateur dans la base de données.
     */
    public void insertUtilisateur(Utilisateur utilisateur) throws InterruptedException, IOException {
        processUtilisateur(utilisateur, insertRequestOrder);
    }

    /**
     *Modifier un utilisateur et le changer de la base de donnes.
     */
    public void updateUtilisateur(Utilisateur utilisateur) throws InterruptedException, IOException {
        processUtilisateur(utilisateur, updateRequestOrder);
    }

    /**
     *SUPPRIMER un utilisateur.
     */
    public void deleteUtilisateur(Utilisateur utilisateur) throws InterruptedException, IOException {
        processUtilisateur(utilisateur, deleteRequestOrder);
    }


    /**
     * Fonction générique pour traiter un utilisateur en fonction d'une requête spécifique.
     */
    private void processUtilisateur(Utilisateur utilisateur, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> utilisateurRequests = new ArrayDeque<>();

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedUtilisateur = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(utilisateur);
        logger.trace("Utilisateur en JSON : {}", jsonifiedUtilisateur);

        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedUtilisateur);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        ClientRequest utilisateurRequest;

        if ("INSERT_UTILISATEUR".equals(requestOrder)) {
            utilisateurRequest = new InsertUtilisateursClientRequest(networkConfig, 0, request, utilisateur, requestBytes);
        } else if ("UPDATE_UTILISATEUR".equals(requestOrder)) {
            utilisateurRequest = new UpdateUtilisateursClientRequest(networkConfig, 0, request, utilisateur, requestBytes);
        } else if ("DELETE_UTILISATEUR".equals(requestOrder)) {
            utilisateurRequest = new DeleteUtilisateursClientRequest(networkConfig, 0, request, utilisateur, requestBytes);
        } else {
            throw new IllegalArgumentException("Requête non supportée : " + requestOrder);
        }


        utilisateurRequests.push(utilisateurRequest);

        while (!utilisateurRequests.isEmpty()) {
            final ClientRequest processedRequest = utilisateurRequests.pop();
            processedRequest.join();
            final Utilisateur processedUtilisateur = (Utilisateur) processedRequest.getInfo();
            logger.debug("Thread {} terminé : {} {} {}  --> {}",
                    processedRequest.getThreadName(),
                    processedUtilisateur.getNom(), processedUtilisateur.getPrenom(), processedUtilisateur.getEmail(),
                    processedRequest.getResult());
        }
    }


    /**
     * Récupère la liste des utilisateurs de la base de données.
     */
    public Utilisateurs selectUtilisateurs() throws InterruptedException, IOException {
        final Deque<ClientRequest> utilisateurRequests = new ArrayDeque<>();
        final ObjectMapper objectMapper = new ObjectMapper();

        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectRequestOrder);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        logger.debug("Envoi de la requête SELECT_ALL_UTILISATEURS avec ID {}", requestId);

        final SelectAllUtilisateursClientRequest utilisateurRequest = new SelectAllUtilisateursClientRequest(
                networkConfig, 0, request, null, requestBytes);
        utilisateurRequests.push(utilisateurRequest);

        logger.debug("Taille de utilisateurRequests : {}", utilisateurRequests.size());

        if (!utilisateurRequests.isEmpty()) {
            final ClientRequest joinedUtilisateurRequest = utilisateurRequests.pop();
            joinedUtilisateurRequest.join();
            logger.debug(" Thread {} terminé.", joinedUtilisateurRequest.getThreadName());

            Object result = joinedUtilisateurRequest.getResult();
            logger.debug(" Résultat de getResult() : {}", result);

            if (result instanceof Utilisateurs) {
                Utilisateurs utilisateurs = (Utilisateurs) result;
                logger.info(" {} utilisateurs récupérés.", utilisateurs.getUtilisateurs().size());
                return utilisateurs;
            } else {
                logger.warn(" Aucun utilisateur trouvé.");
                return null;
            }
        } else {
            logger.error("Aucun utilisateur récupéré.");
            return null;
        }
    }

}

