package com.ismet.durt.utils;

/**
 * Created by ismet on 09/02/16.
 */
public class CredentialUtils {

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
