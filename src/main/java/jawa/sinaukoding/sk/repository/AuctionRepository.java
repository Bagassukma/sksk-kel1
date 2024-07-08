package jawa.sinaukoding.sk.repository;

import jawa.sinaukoding.sk.entity.Auction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Repository
public class AuctionRepository {

    private static final Logger log = LoggerFactory.getLogger(AuctionRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public AuctionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long saveAuction(final Auction auction) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            if (jdbcTemplate.update(con -> Objects.requireNonNull(auction.insert(con)), keyHolder) != 1) {
                return 0L;
            } else {
                return Objects.requireNonNull(keyHolder.getKey()).longValue();
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            return 0L;
        }
    }

    public List<Auction> listAuction(int page, int size) {
        final int offset = (page - 1) * size;
        final String sql = "SELECT * FROM %s LIMIT ? OFFSET ?".formatted(Auction.TABLE_NAME);
        return jdbcTemplate.query(sql, new Object[]{size, offset}, (rs, rowNum) -> new Auction(
                rs.getLong("id"),
                rs.getString("code"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("offer"),
                rs.getInt("highest_bid"),
                rs.getLong("highest_bidder_id"),
                rs.getString("hignest_bidder_name"),
                Auction.Status.valueOf(rs.getString("status")),
                rs.getTimestamp("started_at").toInstant().atOffset(ZoneOffset.UTC),
                rs.getTimestamp("ended_at").toInstant().atOffset(ZoneOffset.UTC),
                rs.getLong("created_by"),
                rs.getLong("updated_by"),
                rs.getLong("deleted_by"),
                rs.getTimestamp("created_at").toInstant().atOffset(ZoneOffset.UTC),
                rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toInstant().atOffset(ZoneOffset.UTC) : null,
                rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toInstant().atOffset(ZoneOffset.UTC) : null
        ));
    }

    public List<Auction> findById(Long id) {
        String sql = "SELECT * FROM %s WHERE id = ?".formatted(Auction.TABLE_NAME);
        return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> new Auction(
                rs.getLong("id"),
                rs.getString("code"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("offer"),
                rs.getInt("highest_bid"),
                rs.getLong("highest_bidder_id"),
                rs.getString("hignest_bidder_name"),
                Auction.Status.valueOf(rs.getString("status")),
                rs.getTimestamp("started_at").toInstant().atOffset(ZoneOffset.UTC),
                rs.getTimestamp("ended_at").toInstant().atOffset(ZoneOffset.UTC),
                rs.getLong("created_by"),
                rs.getLong("updated_by"),
                rs.getLong("deleted_by"),
                rs.getTimestamp("created_at").toInstant().atOffset(ZoneOffset.UTC),
                rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toInstant().atOffset(ZoneOffset.UTC) : null,
                rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toInstant().atOffset(ZoneOffset.UTC) : null
        ));
    }

    public long updateAuctionStatus(Auction auction) {
        String sql = "UPDATE %s SET status = ? WHERE id = ?".formatted(Auction.TABLE_NAME);
        try {
            return jdbcTemplate.update(sql, auction.status().toString(), auction.id());
        } catch (Exception e) {
            log.error("Failed to update auction status: {}", e.getMessage());
            return 0L;
        }
    }


}