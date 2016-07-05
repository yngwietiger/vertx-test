package voice;

import auction.Auction;
import auction.AuctionNotFoundException;
import auction.AuctionRepository;

public class VoiceValidator {

    private final VoiceRepository repository;

    public VoiceValidator(VoiceRepository repository) {
        this.repository = repository;
    }

//    public boolean validate(VoiceCommand command) {
//
//        Auction auctionDatabase = repository.getById(auction.getId())
//            .orElseThrow(() -> new AuctionNotFoundException(auction.getId()));
//
//        return auctionDatabase.getPrice().compareTo(auction.getPrice()) == -1;
//    }
}
