package jawa.sinaukoding.sk.model.request;

public class ResetPasswordReq {
    private final String name;
    private final String newPassword;

    public ResetPasswordReq(String name, String newPassword) {
        this.name = name;
        this.newPassword = newPassword;
    }

    public String getName() {
        return name;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
