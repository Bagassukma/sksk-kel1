package jawa.sinaukoding.sk.controller;

import jawa.sinaukoding.sk.model.Authentication;
import jawa.sinaukoding.sk.model.Response;
import jawa.sinaukoding.sk.model.request.SellerCreateAuctionReq;
import jawa.sinaukoding.sk.model.request.UpdateStatusReq;
import jawa.sinaukoding.sk.service.AuctionService;
import jawa.sinaukoding.sk.util.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/secured/auction")
public class AuctionController  {

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    // seller bisa createAuction
    @PostMapping("create-auction")
    public Response<Object> createAuction(@RequestBody SellerCreateAuctionReq req) {
        Authentication auth = SecurityContextHolder.getAuthentication();
        return auctionService.auctionCreate(auth, req);
    }

    // admin, bisa approve
    @PostMapping("approve-auction")
    public Response<Object> approveAuction(@RequestParam (value = "id") Long id) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return auctionService.ApproveAuction(authentication, id);
    }

    // admin, bisa reject
    @PostMapping("reject")
    public Response<Object> rejectAuction( @RequestParam (value = "id") Long id) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return auctionService.rejectAuction(authentication,id);
    }

    @GetMapping("/list")
    public Response<Object> listUser(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "2") int size) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return auctionService.listAuction(authentication, page, size);
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
}