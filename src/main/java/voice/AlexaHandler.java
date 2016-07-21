package voice;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.Session;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;

public class AlexaHandler {

    private final VoiceRepository repository;
    private final VoiceValidator validator;

    public AlexaHandler(VoiceRepository repository, VoiceValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public void handleVoiceCommand(RoutingContext context) {

        System.out.println("***** RECEIVED ALEXA REQUEST!!!!!");

//        String sessionId = context.request().getParam("sessionId");

//        System.out.println("Received command -> sessionId: " + sessionId);
        System.out.println("***** Context.getBodyAsString(): " + context.getBodyAsString());

        String body = context.getBodyAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SpeechletRequestEnvelope requestEnvelope = null;
        try {
            requestEnvelope = objectMapper.readValue(body, SpeechletRequestEnvelope.class);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Session session = requestEnvelope.getSession();

        System.out.println("Session ID: " + session.getSessionId());

        context.vertx().eventBus().publish("session." + session.getSessionId(), context.getBodyAsString());
        context.response()
                .setStatusCode(200)
                .end();
    }

}
