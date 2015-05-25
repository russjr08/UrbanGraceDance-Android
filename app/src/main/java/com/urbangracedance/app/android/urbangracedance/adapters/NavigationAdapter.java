package com.urbangracedance.app.android.urbangracedance.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.urbangracedance.app.android.urbangracedance.Container;
import com.urbangracedance.app.android.urbangracedance.R;

/**
 * @author russjr08
 */
public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.NavViewHolder> {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;

    private NavItem[] items = { new NavItem("Students", R.drawable.ic_human_child) };


    @Override
    public NavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);

            return new NavViewHolder(v, viewType);

        } else if(viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_item_row, parent, false);

            return new NavViewHolder(v, viewType);
        }

        return null;

    }

    @Override
    public void onBindViewHolder(NavViewHolder holder, int position) {
        if(holder.viewType == TYPE_HEADER) {
            holder.name.setText(Container.getInstance().user.first_name + " " + Container.getInstance().user.last_name);
            holder.emailAddress.setText(Container.getInstance().user.email_address);
            if(Container.getInstance().user.isAdmin) {
                holder.adminText.setText("Administrator");
            } else {
                holder.adminText.setVisibility(View.GONE);
            }
        } else {
            holder.navText.setText(items[position - 1].title);
            holder.icon.setImageResource(items[position - 1].icon);
        }
    }

    @Override
    public int getItemCount() {
        return items.length + 1; // +1 for the header.
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    public static class NavViewHolder extends RecyclerView.ViewHolder {

        public TextView name, emailAddress, navText, adminText;
        public ImageView icon;
        public int viewType;

        public NavViewHolder(View itemView, int viewType) {
            super(itemView);

            this.viewType = viewType;

            if(viewType == TYPE_ITEM) {
                navText = (TextView) itemView.findViewById(R.id.itemText);
                icon = (ImageView) itemView.findViewById(R.id.itemIcon);
            } else {
                name = (TextView) itemView.findViewById(R.id.name);
                emailAddress = (TextView) itemView.findViewById(R.id.email);
                adminText = (TextView) itemView.findViewById(R.id.adminStatus);
            }

        }
    }

    public static class NavItem {
        public String title;
        public int icon;

        public NavItem(String title, int icon) {
            this.title = title;
            this.icon = icon;
        }
    }

}
