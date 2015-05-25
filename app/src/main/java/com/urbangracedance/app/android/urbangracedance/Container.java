package com.urbangracedance.app.android.urbangracedance;

import com.urbangracedance.app.android.urbangracedance.api.Requests;
import com.urbangracedance.app.android.urbangracedance.api.models.User;

/**
 * @author russjr08
 */
public class Container {

    private static Container instance;

    public Requests requester;
    public User user;

    public Container() {

        if(instance == null) {
            instance = this;
        }

    }

    public static Container getInstance() {

        if(instance == null) {
            new Container();
        }

        return instance;
    }

}
