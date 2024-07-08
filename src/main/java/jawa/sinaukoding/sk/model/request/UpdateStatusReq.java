package jawa.sinaukoding.sk.model.request;

import jawa.sinaukoding.sk.entity.Auction;

public record UpdateStatusReq(Long id, Auction.Status status) {
}
