package com.urbangracedance.app.android.urbangracedance;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.urbangracedance.app.android.urbangracedance.adapters.StudentAdapter;
import com.urbangracedance.app.android.urbangracedance.api.models.Student;
import com.urbangracedance.app.android.urbangracedance.api.models.User;
import com.urbangracedance.app.android.urbangracedance.util.DividerItemDecoration;
import com.urbangracedance.app.android.urbangracedance.util.EmptyRecyclerView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class StudentActivityFragment extends Fragment implements View.OnClickListener {

    @InjectView(R.id.studentsList) EmptyRecyclerView studentsList;
    @InjectView(R.id.btn_student_create) FloatingActionButton student_create_fab;
    @InjectView(R.id.studentEmptyView) LinearLayout student_empty_view;

    private StudentAdapter adapter;

    public StudentActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.students_fragment, container, false);

        getActivity().setTitle("Your Students");

        ButterKnife.inject(this, v);

        adapter = new StudentAdapter(this);

        studentsList.setEmptyView(student_empty_view);
        studentsList.addItemDecoration(new DividerItemDecoration(getActivity().getBaseContext(), DividerItemDecoration.VERTICAL_LIST));
        studentsList.setLayoutManager(new LinearLayoutManager(this.getActivity().getBaseContext()));
        studentsList.setAdapter(adapter);

        student_create_fab.setOnClickListener(this);

        return v;
    }

    public void notifyDataWasRefreshed() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_student_create) {
            MaterialDialog dialog = new MaterialDialog.Builder(this.getActivity())
                    .title(R.string.student_create_title)
                    .customView(R.layout.student_create_dialog, true)
                    .positiveText(R.string.student_create_positive)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);

                            Student student = new Student();
                            student.first_name = ((EditText) dialog.findViewById(R.id.student_create_name)).getText().toString();
                            student.last_name = ((EditText) dialog.findViewById(R.id.student_create_lastname)).getText().toString();
                            student.birth_year = Integer.valueOf(((EditText) dialog.findViewById(R.id.student_create_year)).getText().toString());

                            Container.getInstance().requester.createStudent(student, new Callback<User>() {
                                @Override
                                public void success(User user, Response response) {
                                    Container.getInstance().user = user;

                                    adapter.notifyDataSetChanged();

                                    SnackbarManager.show(
                                            Snackbar.with(StudentActivityFragment.this.getActivity())
                                                    .text("Student registered successfully!"));
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    SnackbarManager.show(
                                            Snackbar.with(StudentActivityFragment.this.getActivity())
                                                    .textColor(Color.RED)
                                                    .text("Something went wrong...!"));
                                }
                            });
                        }
                    })
                    .show();


        }
    }
}
