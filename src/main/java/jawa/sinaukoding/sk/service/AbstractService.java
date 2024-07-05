package jawa.sinaukoding.sk.service;

import jawa.sinaukoding.sk.entity.User;
import jawa.sinaukoding.sk.model.Authentication;
import jawa.sinaukoding.sk.model.Response;

import java.util.Optional;

abstract class AbstractService {

    Optional<Response<Object>> precondition(final Authentication authentication, User.Role... role) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of(Response.unauthenticated());
        }
        if (authentication.id() == null || authentication.id() < 0) {
            return Optional.of(Response.unauthorized());
        }
        for (User.Role r : role) {
            if (r == authentication.role()) {
                return Optional.empty();
            }
        }
        return Optional.of(Response.unauthorized());
    }

}
