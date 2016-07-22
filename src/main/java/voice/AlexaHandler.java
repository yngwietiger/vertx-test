package voice;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.http.HttpServerResponse;
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

        SpeechletRequest speechletRequest = requestEnvelope.getRequest();

        System.out.println("Session ID: " + session.getSessionId());
        System.out.println("***** Writing message to session 1 !!!!");

        SpeechletResponseEnvelope responseEnvelope = new SpeechletResponseEnvelope();
        SpeechletResponse speechletResponse = new SpeechletResponse();
        responseEnvelope.setResponse(speechletResponse);
        responseEnvelope.setVersion("1.0");

        PlainTextOutputSpeech plainTextOutputSpeech = new PlainTextOutputSpeech();
        plainTextOutputSpeech.setText("Got it, thanks!");

        speechletResponse.setOutputSpeech(plainTextOutputSpeech);
        speechletResponse.setShouldEndSession(true);

        String responseString = "";
        try {
            responseString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseEnvelope);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        VoiceCommand voiceCommand = new VoiceCommand("session.1", "UNKNOWN");

        if (speechletRequest instanceof IntentRequest) {

            IntentRequest intentRequest = (IntentRequest)speechletRequest;
            String name = intentRequest.getIntent().getName();
            voiceCommand.setCommand(name);
        }

        String voiceCommandString = "";
        try {
            voiceCommandString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(voiceCommand);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("VOICE COMMAND: " + voiceCommandString);

        context.vertx().eventBus().publish("session.1", voiceCommandString);

        HttpServerResponse response = context.response();
        response.setStatusCode(200);
        response.headers()
                .add("Content-Length", "" + responseString.length())
                .add("Content-Type", "application/json");

        response.write(responseString);
        response.end();
    }

}
