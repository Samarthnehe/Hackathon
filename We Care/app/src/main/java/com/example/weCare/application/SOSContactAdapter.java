package com.example.weCare.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SOSContactAdapter extends BaseAdapter {
    Context context;
    List<Contact> contactList;
    int contactListSize;

    public SOSContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.contactListSize = contactList.size();
    }

    public class SOSContactHolder{
        TextView name, number;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SOSContactHolder holder;

        if (convertView==null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.contact_listview, parent, false);
            holder = new SOSContactHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tvName);
            holder.number = (TextView) convertView.findViewById(R.id.tvPhone);
            convertView.setTag(holder);
        }
        else{
            holder = (SOSContactHolder) convertView.getTag();
        }

        holder.name.setText(contactList.get(position).getName());
        holder.number.setText(contactList.get(position).getNumber());

        return convertView;
    }
}
