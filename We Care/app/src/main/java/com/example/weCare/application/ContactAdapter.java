package com.example.weCare.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends BaseAdapter implements Filterable {

    Context context;
    List<Contact> contactList, filterContactList;
    int contactListSize;

    public ContactAdapter(Context context, List<Contact> contactList) {
        super();

        this.context = context;
        this.contactList = contactList;
        this.contactListSize = contactList.size();
    }

    public class ContactHolder{
        TextView name, number;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults filterResults = new FilterResults();
                final ArrayList<Contact> results = new ArrayList<Contact>();
                if (filterContactList==null){
                    filterContactList = contactList;
                }
                if (constraint!=null){
                    if (filterContactList!=null && filterContactList.size()>0){
                        for (final Contact f: filterContactList){
                            if (f.getName().toLowerCase().contains(constraint.toString().toLowerCase()))
                                results.add(f);
                        }
                    }
                }
                else{
                    if (filterContactList!=null && filterContactList.size()>0){
                        for (final Contact f: filterContactList){
                            results.add(f);
                        }
                    }
                }
                filterResults.values = results;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactList = (ArrayList<Contact>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        final ContactHolder holder;

        if (convertView==null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.contact_listview, parent, false);
            holder = new ContactHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tvName);
            holder.number = (TextView) convertView.findViewById(R.id.tvPhone);
            convertView.setTag(holder);
        }
        else{
            holder = (ContactHolder) convertView.getTag();
        }

        holder.name.setText(contactList.get(position).getName());
        holder.number.setText(contactList.get(position).getNumber());

        return convertView;
    }
}
