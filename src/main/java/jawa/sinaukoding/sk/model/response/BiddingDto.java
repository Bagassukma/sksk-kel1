package jawa.sinaukoding.sk.model.response;

public record BiddingDto(
        Long id,
        Long auctionId,
        Integer bid,
        Long bidder,
        String created_at
) {
}