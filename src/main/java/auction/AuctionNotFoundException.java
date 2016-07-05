package auction;

public class AuctionNotFoundException extends RuntimeException {
    public AuctionNotFoundException(String auctionId) {
        super("VoiceCommand not found: " + auctionId);
    }
}
