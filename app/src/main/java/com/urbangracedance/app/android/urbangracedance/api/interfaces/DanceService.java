package com.urbangracedance.app.android.urbangracedance.api.interfaces;

import com.urbangracedance.app.android.urbangracedance.api.models.Student;
import com.urbangracedance.app.android.urbangracedance.api.models.User;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * @author russjr08
 */
public interface DanceService {

    @GET("/me")
    void getSelf(Callback<User> callback);

    @POST("/student/create")
    void createStudent(@Body Student student, Callback<User> callback);


}
