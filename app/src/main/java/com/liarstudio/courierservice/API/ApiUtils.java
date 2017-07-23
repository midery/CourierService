package com.liarstudio.courierservice.API;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by M1DERY on 19.07.2017.
 */

public class ApiUtils {


    public static final String BASE_URL = "http://10.0.2.2:8080";
    public static User CURRENT_USER = null;
    public static boolean TOGGLE_OFFLINE = false;
    public static boolean IS_ADMIN = false;


    /** Функция MD5-шифрования пароля **/
    public static String encryptPassword(String nonEncryptedPassword) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(StandardCharsets.UTF_8.encode(nonEncryptedPassword));
            return String.format("%032x", new BigInteger(1, md5.digest()));
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            return null;
        }
    }


}
