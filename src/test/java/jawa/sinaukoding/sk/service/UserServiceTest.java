package jawa.sinaukoding.sk.service;

import jawa.sinaukoding.sk.entity.User;
import jawa.sinaukoding.sk.model.Authentication;
import jawa.sinaukoding.sk.model.request.*;
import jawa.sinaukoding.sk.model.Response;
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
class UserServiceTest {

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
    void registerSeller() {
        final RegisterSellerReq req = new RegisterSellerReq("Charlie", "charlie", "alice");
        Mockito.when(userRepository.saveSeller(ArgumentMatchers.any())).thenReturn(2L);
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response = userService.registerSeller(authentication, req);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("0500", response.code());
        Assertions.assertEquals("Sukses", response.message());
        Assertions.assertEquals(2L, response.data());
    }

    @Test
    void registerSellerBadRequest() {
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response1 = userService.registerSeller(authentication, null);
        Assertions.assertNotNull(response1);
        Assertions.assertEquals("0301", response1.code());
        Assertions.assertEquals("bad request", response1.message());
        Assertions.assertNull(response1.data());
    }

    @Test
    void registerSellerFailed() {
        final RegisterSellerReq req = new RegisterSellerReq("Charlie", "charlie", "alice");
        Mockito.when(userRepository.saveSeller(ArgumentMatchers.any())).thenReturn(0L);
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response = userService.registerSeller(authentication, req);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("0501", response.code());
        Assertions.assertEquals("Gagal mendaftarkan seller", response.message());
        Assertions.assertNull(response.data());
    }

    @Test
    void registerBuyer() {
        final RegisterBuyerReq req = new RegisterBuyerReq("Charlie", "charlie", "alice");
        Mockito.when(userRepository.saveBuyer(ArgumentMatchers.any())).thenReturn(2L);
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response = userService.registerBuyer(authentication, req);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("0600", response.code());
        Assertions.assertEquals("Sukses", response.message());
        Assertions.assertEquals(2L, response.data());
    }

    @Test
    void registerBuyerBadRequest() {
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response1 = userService.registerBuyer(authentication, null);
        Assertions.assertNotNull(response1);
        Assertions.assertEquals("0301", response1.code());
        Assertions.assertEquals("bad request", response1.message());
        Assertions.assertNull(response1.data());
    }

    @Test
    void registerBuyerFailed() {
        final RegisterBuyerReq req = new RegisterBuyerReq("Charlie", "charlie", "alice");
        Mockito.when(userRepository.saveSeller(ArgumentMatchers.any())).thenReturn(0L);
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response = userService.registerBuyer(authentication, req);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("0601", response.code());
        Assertions.assertEquals("Gagal mendaftarkan buyer", response.message());
        Assertions.assertNull(response.data());
    }

    @Test
    void loginBadRequest() {
        final Response<Object> response = userService.login(null);
        Assertions.assertEquals(Response.badRequest().code(), response.code());
        Assertions.assertEquals(Response.badRequest().message(), response.message());
    }

    @Test
    void loginUserNotFound() {
        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        final Response<Object> response = userService.login(new LoginReq("charlie@sksk.com", "12345678"));
        Assertions.assertEquals("0801", response.code());
        Assertions.assertEquals("Email atau password salah", response.message());
    }

    @Test
    void loginWrongPassword() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.id()).thenReturn(1L);
        Mockito.when(user.role()).thenReturn(User.Role.ADMIN);
        Mockito.when(user.password()).thenReturn("234234234");

        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(user));
        final Response<Object> response = userService.login(new LoginReq("charlie@sksk.com", "12345678"));
        Assertions.assertEquals("0802", response.code());
        Assertions.assertEquals("Email atau password salah", response.message());
    }

    @Test
    void login() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.id()).thenReturn(1L);
        Mockito.when(user.role()).thenReturn(User.Role.ADMIN);
        String password = "12345678";
        Mockito.when(user.password()).thenReturn(passwordEncoder.encode(password));

        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(user));
        final Response<Object> response = userService.login(new LoginReq("charlie@sksk.com", password));
        Assertions.assertEquals("0800", response.code());
        Assertions.assertEquals("Sukses", response.message());
    }

    @Test
    void listUserBadRequest() {
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);

        Response<Object> response1 = userService.listUsers(authentication, 0, 10);
        Assertions.assertEquals(Response.badRequest().code(), response1.code());
        Assertions.assertEquals(Response.badRequest().message(), response1.message());

        Response<Object> response2 = userService.listUsers(authentication, -1, 10);
        Assertions.assertEquals(Response.badRequest().code(), response2.code());
        Assertions.assertEquals(Response.badRequest().message(), response2.message());

        Response<Object> response3 = userService.listUsers(authentication, 1, 0);
        Assertions.assertEquals(Response.badRequest().code(), response3.code());
        Assertions.assertEquals(Response.badRequest().message(), response3.message());

        Response<Object> response4 = userService.listUsers(authentication, 1, -1);
        Assertions.assertEquals(Response.badRequest().code(), response4.code());
        Assertions.assertEquals(Response.badRequest().message(), response4.message());
    }

    @Test
    void listUser() {
        final User admin = userRepository.findById(1L).orElseThrow();
        Mockito.when(userRepository.listUsers(1, 10)).thenReturn(List.of(admin));
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);

        Response<Object> response = userService.listUsers(authentication, 1, 10);
        Assertions.assertEquals("0900", response.code());
        Assertions.assertEquals("Sukses", response.message());
    }



    @Test
    void deleteUser() {
        final DeleteUserReq req = new DeleteUserReq("Budi");
        final User admin = userRepository.findById(1L).orElseThrow();
        Mockito.when(userRepository.deleteUser("Budi")).thenReturn(true);
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        Response<Object> response = userService.deleteUser(authentication, req);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("1000",  response.code());
        Assertions.assertEquals("User berhasil dihapus", response.message());
        Assertions.assertEquals(true, response.data());
    }

    @Test
    void deleteUserFailed() {
        final DeleteUserReq req = new DeleteUserReq("Budi");
        final User admin = userRepository.findById(1L).orElseThrow();
        Mockito.when(userRepository.deleteUser("Budi")).thenReturn(false);
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response = userService.deleteUser(authentication, req);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("1001", response.code());
        Assertions.assertEquals("Gagal menghapus user", response.message());
        Assertions.assertEquals(false, response.data());
    }

    @Test
    void deleteUserBadRequest() {
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response = userService.deleteUser(authentication, null);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("0301", response.code());
        Assertions.assertEquals("bad request", response.message());
        Assertions.assertNull(response.data());
    }

    @Test
    void resetPassword() {
        final ResetPasswordReq req = new ResetPasswordReq("arbi@sksk.id", "cekpass");
        User mockUser = new User(
                1L,
                "Arbi",
                "arbi@sksk.id",
                "oldpassword",
                User.Role.BUYER,
                ADMIN.id(),
                null,
                null,
                OffsetDateTime.now(),
                null,
                null
        );
        Mockito.when(userRepository.findByEmail(req.email())).thenReturn(Optional.of(mockUser));
        Mockito.when(userRepository.updatePasswordByEmail(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(2L);
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response = userService.resetPassword(authentication, req);

        log.info(String.valueOf(response));
        Assertions.assertNotNull(response);
        Assertions.assertEquals("0700", response.code());
        Assertions.assertEquals("Sukses", response.message());
        Assertions.assertEquals(2L, response.data());
    }

    @Test
    void resetPasswordFailed() {
        final ResetPasswordReq req = new ResetPasswordReq("arbi@sksk.id", "cekpass");
        User mockUser = new User(
                1L,
                "Arbi",
                "arbi@sksk.id",
                "oldpassword",
                User.Role.BUYER,
                ADMIN.id(),
                null,
                null,
                OffsetDateTime.now(),
                null,
                null
        );
        Mockito.when(userRepository.findByEmail(req.email())).thenReturn(Optional.of(mockUser));
        Mockito.when(userRepository.updatePasswordByEmail(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(0L);
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response = userService.resetPassword(authentication, req);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("0701", response.code());
        Assertions.assertEquals("Gagal reset password", response.message());
        Assertions.assertNull(response.data());
    }

    @Test
    void resetPasswordUserNotFound() {
        final ResetPasswordReq req = new ResetPasswordReq("arbi@sksk.id", "cekpass");
        Mockito.when(userRepository.findByEmail(req.email())).thenReturn(Optional.empty());
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response = userService.resetPassword(authentication, req);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("0702", response.code());
        Assertions.assertEquals("User tidak ditemukan", response.message());
        Assertions.assertNull(response.data());
    }

    @Test
    void resetPasswordEmailNull() {
        final ResetPasswordReq req = new ResetPasswordReq(null, "cekpass");
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response = userService.resetPassword(authentication, req);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("0703", response.code());
        Assertions.assertEquals("Email tidak boleh null", response.message());
        Assertions.assertNull(response.data());
    }

    @Test
    void resetPasswordEmailEmpty() {
        final ResetPasswordReq req = new ResetPasswordReq("", "cekpass");
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response = userService.resetPassword(authentication, req);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("0703", response.code());
        Assertions.assertEquals("Email tidak boleh null", response.message());
        Assertions.assertNull(response.data());
    }

    @Test
    void resetPasswordPasswordNull() {
        final ResetPasswordReq req = new ResetPasswordReq("arbi@sksk.id", null);
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response = userService.resetPassword(authentication, req);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("0704", response.code());
        Assertions.assertEquals("Password tidak boleh null", response.message());
        Assertions.assertNull(response.data());
    }

    @Test
    void resetPasswordPasswordEmpty() {
        final ResetPasswordReq req = new ResetPasswordReq("arbi@sksk.id", "");
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response = userService.resetPassword(authentication, req);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("0704", response.code());
        Assertions.assertEquals("Password tidak boleh null", response.message());
        Assertions.assertNull(response.data());
    }

    @Test
    void resetPasswordBadRequest() {
        final User admin = userRepository.findById(1L).orElseThrow();
        final Authentication authentication = new Authentication(admin.id(), admin.role(), true);
        final Response<Object> response = userService.resetPassword(authentication, null);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("0301", response.code());
        Assertions.assertEquals("bad request", response.message());
        Assertions.assertNull(response.data());
    }
}