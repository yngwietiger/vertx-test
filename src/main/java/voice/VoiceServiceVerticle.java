package voice;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;


public class VoiceServiceVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(VoiceServiceVerticle.class);

    @Override
    public void start() {
        Router router = Router.router(vertx);

        router.route("/eventbus/*").handler(eventBusHandler());
        router.mountSubRouter("/api", voiceApiRouter());
        router.route().failureHandler(errorHandler());

        router.route().handler(StaticHandler.create("voice"));

//        router.route().handler(staticHandler());

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    private SockJSHandler eventBusHandler() {
        BridgeOptions options = new BridgeOptions()
            .addOutboundPermitted(new PermittedOptions().setAddressRegex("session\\.[0-9]+"));
        return SockJSHandler.create(vertx).bridge(options, event -> {
            if (event.type() == BridgeEventType.SOCKET_CREATED) {
                logger.info("A socket was created");
            }
            event.complete(true);
        });
    }

    private Router voiceApiRouter() {

        VoiceRepository repository = new VoiceRepository(vertx.sharedData());
        VoiceValidator validator = new VoiceValidator(repository);
        VoiceHandler handler = new VoiceHandler(repository, validator);

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.route().consumes("application/json");
        router.route().produces("application/json");

//        router.route("/auctions/:id").handler(handler::initAuctionInSharedData);
//        router.get("/auctions/:id").handler(handler::handleGetAuction);
        router.post("/voice/:sessionId").handler(handler::handleVoiceCommand);

        return router;
    }


    private ErrorHandler errorHandler() {
        return ErrorHandler.create(true);
    }

    private StaticHandler staticHandler() {
        return StaticHandler.create()
            .setCachingEnabled(false);
    }
}
