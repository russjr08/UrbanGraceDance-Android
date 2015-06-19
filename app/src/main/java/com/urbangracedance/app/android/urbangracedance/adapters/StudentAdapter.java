package com.urbangracedance.app.android.urbangracedance.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.urbangracedance.app.android.urbangracedance.Container;
import com.urbangracedance.app.android.urbangracedance.R;
import com.urbangracedance.app.android.urbangracedance.StudentDetailFragment;
import com.urbangracedance.app.android.urbangracedance.api.models.Student;

/**
 * @author russjr08
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentListViewHolder> {

    private int lastPosition = -1;

    private Fragment fragment;

    public StudentAdapter(Fragment fragment) {
        this.fragment = fragment;
    }


    @Override
    public StudentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_row, parent, false);

        return new StudentListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StudentListViewHolder holder, final int position) {
        Student student = Container.getInstance().user.students.get(position);
        holder.nameView.setText(student.getFullName());
        holder.birthYearView.setText(String.valueOf(student.birth_year));

        if(position > lastPosition) {
            Animation anim = AnimationUtils.loadAnimation(holder.base.getContext(), R.anim.abc_slide_in_bottom);
            holder.base.startAnimation(anim);
            lastPosition = position;
        }

        holder.base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudentDetailFragment detailFragment = new StudentDetailFragment(Container.getInstance().user.students.get(position));
                FragmentTransaction transaction = fragment.getFragmentManager().beginTransaction();
                transaction.replace(R.id.mainFragmentContainer, detailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(Container.getInstance().user != null && Container.getInstance().user.students != null) {
            return Container.getInstance().user.students.size();
        }
        return 0;
    }

    public static class StudentListViewHolder extends RecyclerView.ViewHolder {

        public TextView nameView;
        public TextView birthYearView;
        public View base;

        public StudentListViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.name);
            birthYearView = (TextView) itemView.findViewById(R.id.birth_year);
            base = itemView;
        }


    }

}
