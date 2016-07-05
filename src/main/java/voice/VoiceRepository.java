package voice;

import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;

import java.math.BigDecimal;
import java.util.Optional;

public class VoiceRepository {

    private SharedData sharedData;

    public VoiceRepository(SharedData sharedData) {
        this.sharedData = sharedData;
    }

//    public Optional<VoiceCommand> getById(String auctionId) {
//        LocalMap<String, String> auctionSharedData = this.sharedData.getLocalMap(auctionId);
//        return Optional.of(auctionSharedData)
//            .filter(m -> !m.isEmpty())
//            .map(this::convertToAuction);
//    }

//    public void save(VoiceCommand voiceCommand) {
//        LocalMap<String, String> auctionSharedData = this.sharedData.getLocalMap(voiceCommand.getId());
//
//        auctionSharedData.put("id", voiceCommand.getId());
//        auctionSharedData.put("price", voiceCommand.getPrice().toString());
//    }
//
//    private VoiceCommand convertToAuction(LocalMap<String, String> auction) {
//        return new VoiceCommand(
//            auction.get("id"),
//            new BigDecimal(auction.get("price"))
//        );
//    }
}
