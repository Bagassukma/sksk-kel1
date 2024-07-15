package jawa.sinaukoding.sk.controller;

import jawa.sinaukoding.sk.model.Authentication;
import jawa.sinaukoding.sk.model.Response;
import jawa.sinaukoding.sk.model.request.BuyerCreateBiddingReq;
import jawa.sinaukoding.sk.model.request.SellerCreateAuctionReq;
import jawa.sinaukoding.sk.model.request.UpdateStatusReq;
import jawa.sinaukoding.sk.service.AuctionService;
import jawa.sinaukoding.sk.util.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/secured/auction")
public class AuctionController  {

    private AuctionService auctionService = null;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    // seller bisa createAuction
    @PostMapping("create-auction")
    public Response<Object> auctionCreate(@RequestBody SellerCreateAuctionReq req) {
        Authentication auth = SecurityContextHolder.getAuthentication();
        return auctionService.auctionCreate(auth, req);
    }

    @PutMapping("/close")
    public Response<Object> closeAuction(@RequestBody UpdateStatusReq req) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return auctionService.closeAuctionStatus(authentication, req);
    }

    @GetMapping("/list")
    public Response<Object> listUser(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "2") int size,
                                     @RequestParam(value = "name", required = false) String name) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return auctionService.listAuction(authentication, page, size, name);
    }

    @GetMapping("/list/{id}")
    public Response<Object> getAuctionById(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return auctionService.getAuctionById(authentication, id);
    }

    @PutMapping("/status")
    public Response<Object> approveAuction(@RequestBody UpdateStatusReq req) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return auctionService.updateAuctionStatus(authentication, req);
    }

    @PostMapping("/list/{id}")
    public Response<Object> createBidding(@PathVariable("id") Long id, @RequestBody BuyerCreateBiddingReq req) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return auctionService.createBidding(authentication, req, id);
    }
}