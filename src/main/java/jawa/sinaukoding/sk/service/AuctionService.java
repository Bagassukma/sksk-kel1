package jawa.sinaukoding.sk.service;

import jawa.sinaukoding.sk.entity.Auction;
import jawa.sinaukoding.sk.entity.User;
import jawa.sinaukoding.sk.model.Authentication;
import jawa.sinaukoding.sk.model.request.SellerCreateAuctionReq;
import jawa.sinaukoding.sk.model.Response;
import jawa.sinaukoding.sk.model.request.UpdateStatusReq;
import jawa.sinaukoding.sk.model.response.AuctionDto;
import jawa.sinaukoding.sk.repository.AuctionRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public final class AuctionService extends AbstractService {

    private final AuctionRepository auctionRepository;

    public AuctionService(final Environment env, final AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    private static String generateCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public Response<Object> auctionCreate(final Authentication authentication, final SellerCreateAuctionReq req) {
        return precondition(authentication, User.Role.SELLER).orElseGet(() -> {
            if (req == null) {
                return Response.badRequest();
            }

            OffsetDateTime startedAt = OffsetDateTime.parse(req.startedAt());
            OffsetDateTime endedAt = OffsetDateTime.parse(req.endedAt());

            Auction newAuction = new Auction(
                    generateCode(),
                    req.name(),
                    req.description(),
                    req.minimumPrice(),
                    startedAt,
                    endedAt,
                    authentication.id()
            );

            final Long saved = auctionRepository.saveAuction(newAuction);

            if (saved == 0L) {
                return Response.create("05", "01", "Gagal membuat lelang.", null);
            } else {
                return Response.create("05", "00", "Sukses membuat lelang.", saved);
            }
        });
    }

    public Response<Object> rejectAuction(final Authentication authentication, Long id) {
        return precondition(authentication, User.Role.ADMIN).orElseGet(() -> {
            Optional<Auction> auctionOptional = auctionRepository.findById(id);
            Auction auction = auctionOptional.get();
            if (auction.status().equals(auction.status().WAITING_FOR_APPROVAL)){
                if (isInvalid(auction)) {
                    Auction updatedAuction = new Auction(
                            auction.id(),
                            auction.code(),
                            auction.name(),
                            auction.description(),
                            auction.offer(),
                            auction.highestBid(),
                            auction.highestBidderId(),
                            auction.highestBidderName(),
                            Auction.Status.REJECTED,
                            auction.startedAt(),
                            auction.endedAt(),
                            auction.createdBy(),
                            auction.updatedBy(),
                            auction.deletedBy(),
                            auction.createdAt(),
                            auction.updatedAt(),
                            auction.deletedAt()
                    );
                    Long x = auctionRepository.RejectedAuction(id);
                    return Response.create("01", "01", "Auction rejected successfully", x);
                } else {
                    return Response.create("01", "02", "cannot rejected", null);
                }
            }
            return Response.badRequest();

        });

    }

    private boolean isInvalid(Auction auction) {
        return auction.id() == null ||
                isNullOrEmpty(auction.code()) ||
                isNullOrEmpty(auction.name()) ||
                isNullOrEmpty(auction.description()) ||
                auction.offer() == null ||
                auction.highestBid() == null ||
                auction.highestBidderId() == null ||
                isNullOrEmpty(auction.highestBidderName()) ||
                auction.status() == null ||
                auction.startedAt() == null ||
                auction.endedAt() == null ||
                auction.createdBy() == null ||
                auction.updatedBy() == null ||
                auction.deletedBy() == null ||
                auction.createdAt() == null ||
                auction.updatedAt() == null ||
                auction.deletedAt() == null;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public Response<Object> listAuction(final Authentication authentication, final int page, final int size) {
        return precondition(authentication, User.Role.ADMIN, User.Role.SELLER, User.Role.BUYER).orElseGet(() -> {
            if (page <= 0 || size <= 0) {
                return Response.badRequest();
            }

            final List<Auction> auctions = auctionRepository.listAuction(page, size);
            final List<AuctionDto> dto = auctions.stream()
                    .map(auction -> new AuctionDto(
                            auction.id(),
                            auction.code(),
                            auction.name(),
                            auction.description(),
                            auction.offer(),
                            auction.startedAt().toString(),
                            auction.endedAt().toString(),
                            auction.status().toString())).toList();

            return Response.create("09", "00", "Sukses", dto);
        });
    }

    public Response<Object> ApproveAuction(final Authentication authentication, Long id) {
        return precondition(authentication, User.Role.ADMIN).orElseGet(() -> {
            Optional<Auction> auctionOptional = auctionRepository.findById(id);
            Auction auction = auctionOptional.get();
            if (auction.status().equals(auction.status().WAITING_FOR_APPROVAL)){
                if (ifPresent(auction)) {
                    Auction updatedAuction = new Auction(
                            auction.id(),
                            auction.code(),
                            auction.name(),
                            auction.description(),
                            auction.offer(),
                            auction.highestBid(),
                            auction.highestBidderId(),
                            auction.highestBidderName(),
                            Auction.Status.APPROVED,
                            auction.startedAt(),
                            auction.endedAt(),
                            auction.createdBy(),
                            auction.updatedBy(),
                            auction.deletedBy(),
                            auction.createdAt(),
                            auction.updatedAt(),
                            auction.deletedAt()
                    );
                    Long x = auctionRepository.ApproveAuction(id);
                    return Response.create("01", "01", "Auction Approved successfully", x);
                } else {
                    return Response.create("01", "02", "cannot Approved", null);
                }
            }
            return Response.badRequest();

        });

    }

    private boolean ifPresent(Auction auction) {
        return auction.id() != null ||
                isNotNullOrEmpty(auction.code()) ||
                isNotNullOrEmpty(auction.name()) ||
                isNotNullOrEmpty(auction.description()) ||
                auction.offer() != null ||
                auction.highestBid() != null ||
                auction.highestBidderId() != null ||
                isNotNullOrEmpty(auction.highestBidderName()) ||
                auction.status() != null ||
                auction.startedAt() != null ||
                auction.endedAt() != null ||
                auction.createdBy() != null ||
                auction.updatedBy() != null ||
                auction.deletedBy() != null ||
                auction.createdAt() != null ||
                auction.updatedAt() != null ||
                auction.deletedAt() != null;
    }

    private boolean isNotNullOrEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public Response<Object> getAuctionById(final Authentication authentication, final Long id) {
        return precondition(authentication, User.Role.ADMIN, User.Role.SELLER, User.Role.BUYER).orElseGet(() -> {
            Optional<Auction> auctions = auctionRepository.findById(id);

            if (auctions.isEmpty()) {
                return Response.create("09", "01", "Auction not found", null);
            }

            Auction auction = auctions.get();
            AuctionDto auctionDto = new AuctionDto(
                    auction.id(),
                    auction.code(),
                    auction.name(),
                    auction.description(),
                    auction.offer(),
                    auction.startedAt().toString(),
                    auction.endedAt().toString(),
                    auction.status().toString()
            );

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("auction", auctionDto);

            return Response.create("09", "00", "Sukses", responseData);
        });
    }

    public Response<Object> updateAuctionStatus(Authentication authentication, UpdateStatusReq req) {
        return precondition(authentication, User.Role.ADMIN).orElseGet(() -> {
            Auction.Status newStatus = req.status();

            Optional<Auction> auctions = auctionRepository.findById(req.id());
            if (auctions.isEmpty()) {
                return Response.create("07", "02", "Auction tidak ditemukan", null);
            }

            Auction auction = auctions.get();

            if (auction.status() == Auction.Status.APPROVED || auction.status() == Auction.Status.REJECTED) {
                return Response.create("07", "03", "Status auction sudah DIUBAH dan tidak bisa diubah lagi", null);
            }

            Auction updatedAuction = new Auction(
                    auction.id(),
                    auction.code(),
                    auction.name(),
                    auction.description(),
                    auction.offer(),
                    auction.highestBid(),
                    auction.highestBidderId(),
                    auction.highestBidderName(),
                    newStatus,
                    auction.startedAt(),
                    auction.endedAt(),
                    auction.createdBy(),
                    auction.updatedBy(),
                    auction.deletedBy(),
                    auction.createdAt(),
                    OffsetDateTime.now(),
                    auction.deletedAt()
            );

            long updated = auctionRepository.updateAuctionStatus(updatedAuction);

            if (updated == 1L) {
                return Response.create("07", "00", "Sukses", updated);
            } else {
                return Response.create("07", "01", "Gagal update Status", null);
            }
        });
    }
}