package jawa.sinaukoding.sk.controller;

import jawa.sinaukoding.sk.model.Authentication;
import jawa.sinaukoding.sk.model.Response;
import jawa.sinaukoding.sk.model.request.*;
import jawa.sinaukoding.sk.service.UserService;
import jawa.sinaukoding.sk.util.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/secured/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public Response<Object> listUser(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "2") int size) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return userService.listUsers(authentication, page, size);
    }

    @PostMapping("/register-seller")
    public Response<Object> registerSeller(@RequestBody RegisterSellerReq req) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return userService.registerSeller(authentication, req);
    }

    @PostMapping("/register-buyer")
    public Response<Object> registerBuyer(@RequestBody RegisterBuyerReq req) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return userService.registerBuyer(authentication, req);
    }

    @PutMapping("/reset-password")
    public Response<Object> resetPassword(@RequestBody ResetPasswordReq req) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return userService.resetPassword(authentication, req);
    }

    @PostMapping("/update-profile")
    public Response<Object> updateProfile(@RequestBody UpdateProfileReq req) {
        Authentication auth = SecurityContextHolder.getAuthentication();
        return userService.updateProfile(auth, req, auth.id());
    }

    @DeleteMapping("/delete-user")
    public Response<Object> deleteUser(@RequestBody DeleteUserReq req) {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return userService.deleteUser(authentication, req);
    }
    @GetMapping("/current")
    public Response<Object> currentUser() {
        Authentication authentication = SecurityContextHolder.getAuthentication();
        return userService.currentUser(authentication);
    }
}
