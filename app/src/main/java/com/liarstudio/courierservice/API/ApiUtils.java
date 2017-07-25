package com.liarstudio.courierservice.API;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ApiUtils {

    //URL, по которому расположен сервер
    public static String BASE_URL = "http://217.25.225.54:8080";

    //Текущий пользователь в системе
    public static User CURRENT_USER = null;

    public static boolean IS_ADMIN = false;


    /* Функция MD5-шифрования пароля */
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
