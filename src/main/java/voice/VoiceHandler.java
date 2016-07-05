package voice;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.math.BigDecimal;
import java.util.Optional;

public class VoiceHandler {

    private final VoiceRepository repository;
    private final VoiceValidator validator;

    public VoiceHandler(VoiceRepository repository, VoiceValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

//    public void handleGetAuction(RoutingContext context) {
//        String auctionId = context.request().getParam("id");
//        Optional<VoiceCommand> auction = this.repository.getById(auctionId);
//
//        if (auction.isPresent()) {
//            context.response()
//                .putHeader("content-type", "application/json")
//                .setStatusCode(200)
//                .end(Json.encodePrettily(auction.get()));
//        } else {
//            context.response()
//                .putHeader("content-type", "application/json")
//                .setStatusCode(404)
//                .end();
//        }
//    }

    public void handleVoiceCommand(RoutingContext context) {


        String sessionId = context.request().getParam("sessionId");

        System.out.println("Received command -> sessionId: " + sessionId);
        System.out.println("Context.getBodyAsString(): " + context.getBodyAsString());

        context.vertx().eventBus().publish("session." + sessionId, context.getBodyAsString());
        context.response()
                .setStatusCode(200)
                .end();

//        String auctionId = context.request().getParam("id");
//        VoiceCommand voiceCommandRequest = new VoiceCommand(
//            auctionId,
//            new BigDecimal(context.getBodyAsJson().getString("price"))
//        );
//
//        if (validator.validate(voiceCommandRequest)) {
//            this.repository.save(voiceCommandRequest);
//            context.vertx().eventBus().publish("auction." + auctionId, context.getBodyAsString());
//
//            context.response()
//                .setStatusCode(200)
//                .end();
//        } else {
//            context.response()
//                .setStatusCode(422)
//                .end();
//        }
    }

//    public void initAuctionInSharedData(RoutingContext context) {
//        String auctionId = context.request().getParam("id");
//
//        Optional<VoiceCommand> auction = this.repository.getById(auctionId);
//        if(!auction.isPresent()) {
//            this.repository.save(new VoiceCommand(auctionId));
//        }
//
//        context.next();
//    }
}
