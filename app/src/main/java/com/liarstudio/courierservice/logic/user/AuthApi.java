package com.liarstudio.courierservice.logic.user;

import com.liarstudio.courierservice.entitiy.user.User;

import java.util.List;
import io.reactivex.Observable;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface AuthApi {

    /**
     * Авторизация пользователя
     *
     * @param email электронный адрес
     * @param password зашифрованный пароль
     *
     * @return Observable с пользователем
     */
    @GET("/auth")
    Observable<User> login(@Query("email") String email, @Query("password") String password);


    /**
     * Регистрация пользователя
     * @param email электронный адрес
     * @param name имя пользователя
     * @param password зашифрованный пароль
     *
     * @return Observable с созданным пользователем
     */
    @FormUrlEncoded
    @POST("/auth")
    Observable<User> register(
            @Field("email") String email,
            @Field("name") String name,
            @Field("password") String password);


    /**
     * Получение списка пользователей
     *
     * @param role роль пользователя
     *
     * @return  Call со списком пользователей
     */
    @GET("/users")
    Call<List<User>> loadUsers(@Query("role") int role);


    /**
     * Получение пользователя по id
     *
     * @param id идентификатор пользователя
     *
     * @return Call с пользователем
     */
    @GET("/users/{id}")
    Call<User> loadUser(@Path("id") int id);
}
