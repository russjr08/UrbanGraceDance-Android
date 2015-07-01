package com.urbangracedance.app.android.urbangracedance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.urbangracedance.app.android.urbangracedance.api.models.Student;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StudentDetailFragment extends Fragment {

    private Student student;

    @InjectView(R.id.name) TextView name;
    @InjectView(R.id.age) TextView age;
    @InjectView(R.id.classList) ListView classes;

    public StudentDetailFragment(Student student) {
        this.student = student;
    }

    public StudentDetailFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_student_detail, container, false);


        ButterKnife.inject(this, v);

        if(student != null) {

            getActivity().setTitle(String.format("%s's Details", student.first_name));
            name.setText(student.getFullName());
            int calculated_age = Calendar.getInstance().get(Calendar.YEAR) - student.birth_year;
            age.setText(String.valueOf(calculated_age));

            classes.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, student.classes));
        }

        return v;
    }

}
