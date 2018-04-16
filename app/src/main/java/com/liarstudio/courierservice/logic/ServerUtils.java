package com.liarstudio.courierservice.logic;

import com.liarstudio.courierservice.entities.user.User;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServerUtils {

    //URL, по которому расположен сервер
    public static String BASE_SERVER_URL = "http://192.168.0.106:8080";

    //URL, по которому расположен сервер
    public static String BASE_LOCAL_URL = "http://192.168.0.106:8080";



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
