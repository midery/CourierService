package com.liarstudio.courierservice.logic;

import com.liarstudio.courierservice.entitiy.user.User;

public class ServerUtils {

    //URL, по которому расположен сервер
    public static String BASE_SERVER_URL = "http://207.154.210.82:8080";

    //URL, по которому расположен сервер
    public static String BASE_LOCAL_URL = "http://192.168.0.106:8080";



    //Текущий пользователь в системе
    public static User CURRENT_USER = null;

    public static boolean IS_ADMIN = false;
}
