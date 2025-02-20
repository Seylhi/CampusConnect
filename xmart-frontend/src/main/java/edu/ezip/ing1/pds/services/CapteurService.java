package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Capteur;
import edu.ezip.ing1.pds.business.dto.Capteurs;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.ConfigLoader;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.InsertCapteursClientRequest;
import edu.ezip.ing1.pds.requests.SelectAllCapteursClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class CapteurService {
    private final static String LoggingLabel = "FrontEnd - CapteurService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);
    private final static String capteursToBeInserted = "capteurs-to-be-inserted.yaml";

    final String insertRequestOrder = "INSERT_CAPTEUR";
    final String selectRequestOrder = "SELECT_ALL_CAPTEURS";

    private final NetworkConfig networkConfig;

    public CapteurService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertCapteurs() throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final Capteurs cps = ConfigLoader.loadConfig(Capteurs.class, capteursToBeInserted);

        int birthdate = 0;
        for(final Capteur cp : cps.getCapteurs()) {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cp);
            logger.trace("Capteur with its JSON face : {}", jsonifiedGuy);
            final String requestId = UUID.randomUUID().toString();
            final Request request = new Request();
            request.setRequestId(requestId);
            request.setRequestOrder(insertRequestOrder);
            request.setRequestContent(jsonifiedGuy);
            objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

            final InsertCapteursClientRequest clientRequest = new InsertCapteursClientRequest(
                    networkConfig,
                    birthdate++, request, cp, requestBytes);
            clientRequests.push(clientRequest);
        }

        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest = clientRequests.pop();
            clientRequest.join();
            final Capteur cp = (Capteur)clientRequest.getInfo();
            logger.debug("Thread {} complete : {} {} {} --> {}",
                    clientRequest.getThreadName(),
                    cp.getId(),
                    clientRequest.getResult());
        }
    }

    public Capteurs selectCapteurs() throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectRequestOrder);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);
        final SelectAllCapteursClientRequest clientRequest = new SelectAllCapteursClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            return (Capteurs) joinedClientRequest.getResult();
        }
        else {
            logger.error("No Capteurs found");
            return null;
        }
    }

}
