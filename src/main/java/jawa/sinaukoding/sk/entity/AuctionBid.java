package jawa.sinaukoding.sk.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

public record AuctionBid(Long id, //
                         Long auctionId, //
                         Integer bid, //
                         Long bidder, //
                         OffsetDateTime createdAt //
) {
    public static final String TABLE_NAME = "sk_auction_bit";

    public PreparedStatement insert(final Connection connection) {
        try {
            String query = "INSERT INTO " + TABLE_NAME + " (auction_id, bid, bidder, created_at) " + "VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setLong(1, this.auctionId());
            ps.setInt(2, this.bid());
            ps.setLong(3, this.bidder());
            ps.setTimestamp(4, Timestamp.valueOf(this.createdAt().toLocalDateTime()));
            return ps;
        } catch (Exception e) {
            return null;
        }
    }
}