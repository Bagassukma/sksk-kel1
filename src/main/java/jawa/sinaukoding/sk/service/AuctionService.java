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
import java.util.List;
import java.util.UUID;

@Service
public final class AuctionService extends AbstractService {

    private final AuctionRepository auctionRepository;

    public AuctionService(final Environment env, final AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    private static String generateCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public Response<Object> createAuction(final Authentication authentication, final SellerCreateAuctionReq req) {
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

    public Response<Object> getAuctionById(final Authentication authentication, final Long id) {
        return precondition(authentication, User.Role.ADMIN, User.Role.SELLER, User.Role.BUYER).orElseGet(() -> {
            List<Auction> auction = auctionRepository.findById(id);

            if (auction == null) {
                return Response.create("09","01", "Auction not found", null);
            }

            final List<Auction> auctions = auctionRepository.findById(id);
            final List<AuctionDto> dto = auctions.stream()
                    .map(auctionList -> new AuctionDto(
                            auctionList.id(),
                            auctionList.code(),
                            auctionList.name(),
                            auctionList.description(),
                            auctionList.offer(),
                            auctionList.startedAt().toString(),
                            auctionList.endedAt().toString(),
                            auctionList.status().toString())).toList();

            return Response.create("09", "00", "Sukses", dto);
        });
    }

    public Response<Object> updateAuctionStatus(Authentication authentication, UpdateStatusReq req) {
        return precondition(authentication, User.Role.ADMIN).orElseGet(() -> {
            Auction.Status newStatus = req.status();
            if (newStatus != Auction.Status.APPROVED && newStatus != Auction.Status.REJECTED) {
                return Response.badRequest();
            }

            List<Auction> auctions = auctionRepository.findById(req.id());
            if (auctions.isEmpty()) {
                return Response.create("07", "02", "Auction tidak ditemukan", null);
            }

            Auction auction = auctions.get(0);

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
                return Response.create("07", "01", "Gagal reset password", null);
            }
        });
    }


}