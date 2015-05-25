package com.urbangracedance.app.android.urbangracedance.api.models;

import java.util.List;

/**
 * @author russjr08
 */
public class User extends DanceModel {

    public String first_name, last_name, email_address;

    public boolean isAdmin, notification;

    public List<Student> students;

}
