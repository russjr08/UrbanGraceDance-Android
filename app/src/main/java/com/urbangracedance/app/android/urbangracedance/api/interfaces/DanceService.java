package com.urbangracedance.app.android.urbangracedance.api.interfaces;

import com.urbangracedance.app.android.urbangracedance.api.models.User;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * @author russjr08
 */
public interface DanceService {

    @GET("/me")
    void getSelf(Callback<User> callback);


}
