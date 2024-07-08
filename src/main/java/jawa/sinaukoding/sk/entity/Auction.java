package jawa.sinaukoding.sk.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

public record Auction(Long id, //
                      String code, //
                      String name, //
                      String description, //
                      Integer offer, //
                      Integer highestBid, //
                      Long highestBidderId,
                      String highestBidderName, //
                      Status status, //
                      OffsetDateTime startedAt, //
                      OffsetDateTime endedAt, //
                      Long createdBy, //
                      Long updatedBy, //
                      Long deletedBy, //
                      OffsetDateTime createdAt, //
                      OffsetDateTime updatedAt, //
                      OffsetDateTime deletedAt //
) {

    public static final String TABLE_NAME = "sk_auction";

    public PreparedStatement insert(final Connection connection) {
        try {
            String query = "INSERT INTO " + TABLE_NAME + " (code, name, description, offer, started_at, ended_at, highest_bid, highest_bidder_id, hignest_bidder_name, status, created_by, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, this.code());
            ps.setString(2, this.name());
            ps.setString(3, this.description());
            ps.setInt(4, this.offer());
            ps.setTimestamp(5, Timestamp.valueOf(this.startedAt().toLocalDateTime()));
            ps.setTimestamp(6, Timestamp.valueOf(this.endedAt().toLocalDateTime()));
            ps.setInt(7, this.highestBid());
            ps.setLong(8, this.highestBidderId());
            ps.setString(9, this.highestBidderName());
            ps.setString(10, this.status().toString());
            ps.setLong(11, this.createdBy());
            ps.setTimestamp(12, Timestamp.valueOf(this.createdAt().toLocalDateTime()));
            return ps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Auction(String code, String name, String description, Integer offer, OffsetDateTime startedAt, OffsetDateTime endedAt, Long createdBy) {
        this(null, code, name, description, offer, 0, 0L, "", Status.WAITING_FOR_APPROVAL, startedAt, endedAt, createdBy, null, null, OffsetDateTime.now(), null, null);
    }

    public Auction updateStatus(Status status, Long updatedBy) {
        return new Auction(id(), code(), name(), description(), offer(), highestBid(), highestBidderId(), highestBidderName(), status, startedAt(), endedAt(), createdBy(), updatedBy, deletedBy(), createdAt(), OffsetDateTime.now(), deletedAt());
    }

    public enum Status {
        WAITING_FOR_APPROVAL, APPROVED, REJECTED, CLOSED
    }
}
