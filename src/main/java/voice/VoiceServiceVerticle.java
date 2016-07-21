package voice;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;
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

        HttpServerOptions options = new HttpServerOptions();

        String keyPath = config().getString("key.file");
        String certPath = config().getString("cert.file");

        logger.info("Key Path: " + keyPath);
        logger.info("Cert Path: " + certPath);

        options.setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath(keyPath).setCertPath(certPath));
        options.setSsl(true);

        HttpServer httpsServer = vertx.createHttpServer(options);
        httpsServer.requestHandler(router::accept);

        int port = config().getInteger("http.port");
        logger.info("HTTP Port: " + port);

        httpsServer.listen(port);

        HttpServer httpServer = vertx.createHttpServer(options);
        httpsServer.requestHandler(router::accept);

//        int port = config().getInteger("http.port");
//        logger.info("HTTP Port: " + port);

        httpServer.listen(8080);
    }

    private SockJSHandler eventBusHandler() {
        BridgeOptions options = new BridgeOptions()
                .addOutboundPermitted(new PermittedOptions().setAddressRegex("session\\.[0-9]+"));
        return SockJSHandler.create(vertx).bridge(options, event -> {
            if (event.type() == BridgeEventType.SOCKET_CREATED) {
                logger.info(">>>>> A socket was created: " + event.getRawMessage());
            }
            event.complete(true);
        });
    }

    private Router voiceApiRouter() {

        VoiceRepository repository = new VoiceRepository(vertx.sharedData());
        VoiceValidator validator = new VoiceValidator(repository);
        VoiceHandler handler = new VoiceHandler(repository, validator);
        AlexaHandler alexaHandler = new AlexaHandler(repository, validator);


        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.route().consumes("application/json");
        router.route().produces("application/json");

        router.post("/voice/:sessionId").handler(handler::handleVoiceCommand);

        router.post("/alexa").handler(alexaHandler::handleVoiceCommand);

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
