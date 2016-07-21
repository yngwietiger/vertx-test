package voice;

import io.vertx.ext.web.RoutingContext;

public class VoiceHandler {

    private final VoiceRepository repository;
    private final VoiceValidator validator;

    public VoiceHandler(VoiceRepository repository, VoiceValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public void handleVoiceCommand(RoutingContext context) {


        String sessionId = context.request().getParam("sessionId");

        System.out.println("Received command -> sessionId: " + sessionId);
        System.out.println("Context.getBodyAsString(): " + context.getBodyAsString());

        context.vertx().eventBus().publish("session." + sessionId, context.getBodyAsString());
        context.response()
                .setStatusCode(200)
                .putHeader("Access-Control-Allow-Origin", "*")
                .end();
    }

}
