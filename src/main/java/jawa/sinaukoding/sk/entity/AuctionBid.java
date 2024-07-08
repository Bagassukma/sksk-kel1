package jawa.sinaukoding.sk.entity;

import java.math.BigInteger;
import java.time.OffsetDateTime;

public record AuctionBid(Long id, //
                         Long auctionId, //
                         BigInteger bid, //
                         Long bidder, //
                         OffsetDateTime createdAt //
) {
}