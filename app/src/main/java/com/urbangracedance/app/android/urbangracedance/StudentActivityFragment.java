package com.urbangracedance.app.android.urbangracedance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.urbangracedance.app.android.urbangracedance.adapters.StudentAdapter;
import com.urbangracedance.app.android.urbangracedance.util.DividerItemDecoration;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A placeholder fragment containing a simple view.
 */
public class StudentActivityFragment extends Fragment {

    @InjectView(R.id.studentsList) RecyclerView studentsList;

    public StudentActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.students_fragment, container, false);

        getActivity().setTitle("Your Students");

        ButterKnife.inject(this, v);

        studentsList.addItemDecoration(new DividerItemDecoration(getActivity().getBaseContext(), DividerItemDecoration.VERTICAL_LIST));
        studentsList.setLayoutManager(new LinearLayoutManager(this.getActivity().getBaseContext()));
        studentsList.setAdapter(new StudentAdapter());

        return v;
    }
}
