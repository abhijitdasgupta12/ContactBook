package com.example.contactbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable
{
    ArrayList<ModelClass> data_holder;
    ArrayList<ModelClass> backups;

    public MyAdapter(ArrayList<ModelClass> data_holder)
    {
        this.data_holder = data_holder;
        backups=new ArrayList<>(data_holder);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.dname.setText("Name: "+data_holder.get(position).getName());
        holder.dpcontact.setText("Phone 1: "+data_holder.get(position).getPcontact());
        holder.dscontact.setText("Phone 2: "+data_holder.get(position).getScontact());
        holder.demail.setText("Email: "+data_holder.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return data_holder.size();
    }

    //Search operation start
    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter=new Filter()
    {
    @Override
    protected FilterResults performFiltering(CharSequence keyword)
    {
        ArrayList<ModelClass> filtered_data= new ArrayList<>();

        if (keyword.toString().isEmpty()) //If there is nothing typed in the search bar
        {
            filtered_data.addAll(backups); //All information inside ArrayList "data" will be added inside of "filtered_data"
        }
        else
        {
            for (ModelClass object : backups) //The objects inside the array list "backup" will be put inside the object of ModelClass "object"
            {
                //taking the "header" object from arraylist "backup" & comparing elements with contents of header
                if(object.getName().toLowerCase().contains(keyword.toString().toLowerCase()))
                {
                    filtered_data.add(object);
                }
            }
        }

        //We will have to return the class object since the type of this function is a Class
        FilterResults filterResults=new FilterResults();
        filterResults.values=filtered_data; //storing the value

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results)
    {
        data_holder.clear(); //clearing the information before putting filtered results

        data_holder.addAll((ArrayList<ModelClass>)results.values);

        notifyDataSetChanged(); //The control will be passed to publishResults once the filtering is done by performFiltering
    }
};//search operation end


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView dname, dpcontact, dscontact, demail;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            dname=itemView.findViewById(R.id.displayname);
            dpcontact=itemView.findViewById(R.id.displaypcontact);
            dscontact=itemView.findViewById(R.id.displayscontact);
            demail=itemView.findViewById(R.id.displayemail);
        }
    }
}
