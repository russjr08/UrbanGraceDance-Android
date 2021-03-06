package com.urbangracedance.app.android.urbangracedance.api.models;

import java.util.List;

/**
 * @author russjr08
 */
public class Student extends DanceModel {

    public String first_name, last_name;
    public int birth_year;
    public List<DanceClass> classes;

    public String getFullName() {
        return first_name + " " + last_name;
    }

}
