package jawa.sinaukoding.sk.model.response;

import java.util.List;

public record ListAuctionWithBiddingDto(Long id,
                                       String code,
                                       String name,
                                       String description,
                                       Integer minimumPrice,
                                       String startedAt,
                                       String endedAt,
                                       String status,
                                       List<BiddingDto> bidding) {
}
