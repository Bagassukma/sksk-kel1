package jawa.sinaukoding.sk.service;

import jawa.sinaukoding.sk.entity.Auction;
import jawa.sinaukoding.sk.entity.User;
import jawa.sinaukoding.sk.model.Authentication;
import jawa.sinaukoding.sk.model.request.*;
import jawa.sinaukoding.sk.model.Response;
import jawa.sinaukoding.sk.repository.AuctionRepository;
import jawa.sinaukoding.sk.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class AuctionServiceTest {

    private static final User ADMIN = new User(1L, //
            "ADMIN", //
            "ADMIN@EXAMPLE.com", //
            "PASSWORD", //
            User.Role.ADMIN, //
            0L, //
            null, //
            null, //
            OffsetDateTime.now(), //
            null, //
            null); //
    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @MockBean
    private AuctionRepository auctionRepository;

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void findAdmin() {
        Mockito.when(userRepository.findById(ArgumentMatchers.eq(1L))).thenReturn(Optional.of(ADMIN));
        Mockito.when(userRepository.findById(ArgumentMatchers.eq(2L))).thenReturn(Optional.of(new User( //
                2L, //
                "Charlie", //
                "charlie", //
                "alice", //
                User.Role.SELLER, //
                ADMIN.id(), //
                null, //
                null, //
                OffsetDateTime.now(), //
                null, //
                null //
        )));
    }

    @Test
    void createAuction() {
        final SellerCreateAuctionReq req = new SellerCreateAuctionReq(
                "Barang Lelang",
                "Ini Barang di Lelang.",
                1000,
                "2024-07-08T08:00:00Z",
                "2024-07-10T18:00:00Z"
        );

        Mockito.when(auctionRepository.saveAuction(ArgumentMatchers.any())).thenReturn(2L);

        final User seller = userRepository.findById(2L).orElseThrow();
        final Authentication authentication = new Authentication(seller.id(), seller.role(), true);
        final Response<Object> response = auctionService.auctionCreate(authentication, req);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("0500", response.code());
        Assertions.assertEquals("Sukses membuat lelang.", response.message());
        Assertions.assertEquals(2L, response.data());
    }

    @Test
    void createAuctionBadRequest() {
        final User seller = userRepository.findById(2L).orElseThrow();
        final Authentication authentication = new Authentication(seller.id(), seller.role(), true);
        final Response<Object> response = auctionService.auctionCreate(authentication, null);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("0301", response.code());
        Assertions.assertEquals("bad request", response.message());
        Assertions.assertNull(response.data());
    }

    @Test
    void createAuctionFailed() {
        final SellerCreateAuctionReq req = new SellerCreateAuctionReq(
                "Barang Lelang",
                "Ini Barang di Lelang.",
                1000,
                "2024-07-08T08:00:00Z",
                "2024-07-10T18:00:00Z"
        );

        Mockito.when(auctionRepository.saveAuction(ArgumentMatchers.any())).thenReturn(0L);

        final User seller = userRepository.findById(2L).orElseThrow();
        final Authentication authentication = new Authentication(seller.id(), seller.role(), true);
        final Response<Object> response = auctionService.auctionCreate(authentication, req);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("0501", response.code());
        Assertions.assertEquals("Gagal membuat lelang.", response.message());
        Assertions.assertNull(response.data());
    }

    // List All Auction

    // List Auction By ID

    // Update Status By ID

    @Test
    void closeAuctionStatus() {
        final UpdateStatusReq req = new UpdateStatusReq(1L, Auction.Status.CLOSED);
        final Auction auction = new Auction(
                1L, "code", "Barang Lelang", "Ini Barang di Lelang.", 1000, null,
                1L, "name", Auction.Status.APPROVED, OffsetDateTime.now(),
                OffsetDateTime.now(), null, null, null,
                OffsetDateTime.now(), OffsetDateTime.now(), null
        );

        final List<Auction> auctions = List.of(auction);

        Mockito.when(auctionRepository.findById(1L)).thenReturn(auctions);
        Mockito.when(auctionRepository.updateAuctionStatus(ArgumentMatchers.any(Auction.class))).thenReturn(1L);

        final User seller = userRepository.findById(2L).orElseThrow();
        final Authentication authentication = new Authentication(seller.id(), seller.role(), true);
        final Response<Object> response = auctionService.rejectAuction(authentication, req.id());

        Assertions.assertNotNull(response);
        Assertions.assertEquals("0700", response.code());
        Assertions.assertEquals("Sukses", response.message());
        Assertions.assertEquals(1L, response.data());
    }
}