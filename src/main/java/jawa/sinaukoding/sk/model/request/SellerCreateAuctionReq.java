package jawa.sinaukoding.sk.model.request;

public record SellerCreateAuctionReq(String name,  //
                                     String description, //
                                     Integer minimumPrice, //
                                     String startedAt, //
                                     String endedAt //
) {
}