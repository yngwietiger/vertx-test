package voice;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
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
        System.out.println("***** Writing message to session 1 !!!!");

        SpeechletResponseEnvelope responseEnvelope = new SpeechletResponseEnvelope();
        SpeechletResponse speechletResponse = new SpeechletResponse();
        responseEnvelope.setResponse(speechletResponse);

        PlainTextOutputSpeech plainTextOutputSpeech = new PlainTextOutputSpeech();
        plainTextOutputSpeech.setText("Got it, thanks!");

        speechletResponse.setOutputSpeech(plainTextOutputSpeech);
        speechletResponse.setShouldEndSession(true);

        String responseString = "";
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseEnvelope);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        context.vertx().eventBus().publish("session.1", context.getBodyAsString());
        context.response()
                .setStatusCode(200)
                .write(responseString)
                .end();
    }

}
