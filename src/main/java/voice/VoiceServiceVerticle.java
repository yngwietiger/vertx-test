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
import io.vertx.ext.web.handler.sockjs.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;


public class VoiceServiceVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(VoiceServiceVerticle2.class);

    @Override
    public void start() {
        Router router = Router.router(vertx);

        router.route("/eventbus/*").handler(eventBusHandler());
        router.mountSubRouter("/api", voiceApiRouter());
        router.route().failureHandler(errorHandler());

        router.route().handler(StaticHandler.create("voice"));

//        router.route().handler(staticHandler());

//        vertx.createHttpServer().requestHandler(router::accept).listen(8080);

//        String keyFile = "/Users/cbridges/Dev/vertx-test/keystore/keystore.jks";
        String keyFile = "/Users/cbridges/Dev/vertx-test/cert/certificate.pem";
//        String keyFile = "/Users/cbridges/Dev/vertx-test/keystore/private-key.pem";

        HttpServerOptions options = new HttpServerOptions();
//                .setUseAlpn(true)
//                .setSsl(true);
//        Buffer key = vertx.fileSystem().readFileSync("/mykey.pem");
//        Buffer cert = vertx.fileSystem().readFileSync("/mycert.pem");

//        String keyPath = "/Users/cbridges/Dev/vertx-test/cert2/file.pem";
//        String certPath = "/Users/cbridges/Dev/vertx-test/cert2/certificate.pem";
//
        String keyPath = config().getString("key.file");
        String certPath = config().getString("cert.file");

        logger.info("Key Path: " + keyPath);
        logger.info("Cert Path: " + certPath);

        options.setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath(keyPath).setCertPath(certPath));
        options.setSsl(true);

//                .setKeyStoreOptions(
//                        new JksOptions()
//                                .setPath(keyFile)
//                                .setPassword("password"));

        HttpServer server = vertx.createHttpServer(options);
        server.requestHandler(router::accept);
//        server.listen(8080);
        int port = config().getInteger("http.port");
//        int port = 8080;
//
        logger.info("HTTP Port: " + port);

        server.listen(port);


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
