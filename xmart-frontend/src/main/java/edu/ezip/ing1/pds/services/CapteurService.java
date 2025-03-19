package edu.ezip.ing1.pds.services;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import edu.ezip.ing1.pds.requests.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Capteur;
import edu.ezip.ing1.pds.business.dto.Capteurs;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

public class CapteurService {

    private final static String LoggingLabel = "FrontEnd - CapteurService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_CAPTEUR";
    final String selectRequestOrder = "SELECT_ALL_CAPTEURS";
    final String deleteRequestOrder = "DELETE_CAPTEUR";
    final String updateRequestOrder = "UPDATE_CAPTEUR";

    private final NetworkConfig networkConfig;

    public CapteurService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    /**
     * Insère un capteur dans la base de données.
     */
    public void insertCapteur(Capteur capteur) throws InterruptedException, IOException {
        processCapteur(capteur, insertRequestOrder);
    }

    /**
     *Modifier un capteur et le changer de la base de donnes.
     */
    public void updateCapteur(Capteur capteur) throws InterruptedException, IOException {
        processCapteur(capteur, updateRequestOrder);
    }

    /**
     *SUPPRIMER un capteur.
     */
    public void deleteCapteur(Capteur capteur) throws InterruptedException, IOException {
        processCapteur(capteur, deleteRequestOrder);
    }


    /**
     * Fonction générique pour traiter un capteur en fonction d'une requête spécifique.
     */
    private void processCapteur(Capteur capteur, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> capteurRequests = new ArrayDeque<>();

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedCapteur = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(capteur);
        logger.trace("Capteur en JSON : {}", jsonifiedCapteur);

        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedCapteur);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        ClientRequest capteurRequest;

        if ("INSERT_CAPTEUR".equals(requestOrder)) {
            capteurRequest = new InsertCapteursClientRequest(networkConfig, 0, request, capteur, requestBytes);
        } else if ("UPDATE_CAPTEUR".equals(requestOrder)) {
            capteurRequest = new UpdateCapteursClientRequest(networkConfig, 0, request, capteur, requestBytes);
        } else if ("DELETE_CAPTEUR".equals(requestOrder)) {
            capteurRequest = new DeleteCapteursClientRequest(networkConfig, 0, request, capteur, requestBytes);
        } else {
            throw new IllegalArgumentException("Requête non supportée : " + requestOrder);
        }


        capteurRequests.push(capteurRequest);

        while (!capteurRequests.isEmpty()) {
            final ClientRequest processedRequest = capteurRequests.pop();
            processedRequest.join();
            final Capteur processedCapteur = (Capteur) processedRequest.getInfo();
            logger.debug("Thread {} terminé : {} {} {}  --> {}",
                    processedRequest.getThreadName(),
                    processedCapteur.getId(), processedCapteur.getStatut(), processedCapteur.getPresence(), processedCapteur.getDetectionProbleme() ,
                    processedRequest.getResult());
        }
    }

    /**
     * Récupère la liste des capteurs de la base de données.
     */
    public Capteurs selectCapteurs() throws InterruptedException, IOException {
        final Deque<ClientRequest> capteurRequests = new ArrayDeque<>();
        final ObjectMapper objectMapper = new ObjectMapper();

        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectRequestOrder);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        logger.debug("Envoi de la requête SELECT_ALL_CAPTEURS avec ID {}", requestId);

        final SelectAllCapteursClientRequest capteurRequest = new SelectAllCapteursClientRequest(
                networkConfig, 0, request, null, requestBytes);
        capteurRequests.push(capteurRequest);

        logger.debug("Taille de capteurRequests : {}", capteurRequests.size());

        if (!capteurRequests.isEmpty()) {
            final ClientRequest joinedCapteurRequest = capteurRequests.pop();
            joinedCapteurRequest.join();
            logger.debug(" Thread {} terminé.", joinedCapteurRequest.getThreadName());

            Object result = joinedCapteurRequest.getResult();
            logger.debug(" Résultat de getResult() : {}", result);

            if (result instanceof Capteurs) {
                Capteurs capteurs = (Capteurs) result;
                logger.info(" {} capteurs récupérés.", capteurs.getCapteurs().size());
                return capteurs;
            } else {
                logger.warn(" Aucun capteur trouvé.");
                return null;
            }
        } else {
            logger.error("Aucun capteur récupéré.");
            return null;
        }
    }

    public Capteur getCapteur(String id) {
        return null;
    }
}

