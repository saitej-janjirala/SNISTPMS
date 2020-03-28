package com.example.snistpms;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;
import static java.security.AccessController.getContext;

public class Addsubjectsadapter extends RecyclerView.Adapter<Addsubjectsadapter.Addsubjectsviewholder> implements Filterable
{
    public ArrayList<showandaddsubjectsdata> mlist;
    public ArrayList<showandaddsubjectsdata> mlistfull;
    public setonsubjectclicklistener mlistener;
    SQLiteDatabase msubjectsdb;
    Cursor cursor;
    public  interface setonsubjectclicklistener
    {
        void onitemclick(int position);
        void onaddclick(int position);
    }
    public void setonsubjectclicklistener(setonsubjectclicklistener listener)
    {
        mlistener=listener;
    }

    public static class Addsubjectsviewholder extends RecyclerView.ViewHolder{
        ImageView mimageview;
        TextView mtextview;
        public Addsubjectsviewholder(@NonNull View itemView,final setonsubjectclicklistener listener) {
            super(itemView);
            mtextview=itemView.findViewById(R.id.subjectname);
            mimageview=itemView.findViewById(R.id.addbutton);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onitemclick(position);
                        }
                    }
                }
            });
            mimageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onaddclick(position);
                        }
                    }
                }
            });
        }
    }
    public Addsubjectsadapter(ArrayList<showandaddsubjectsdata> list,SQLiteDatabase sqLiteDatabase){
        this.mlist=list;
        this.mlistfull=list;
        msubjectsdb=sqLiteDatabase;
        cursor=msubjectsdb.rawQuery("select name from subjects",null);
    }
    @NonNull
    public Addsubjectsviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.exampleitemforsubjects,parent,false);
        Addsubjectsviewholder evh=new Addsubjectsviewholder(v,mlistener);
        return  evh;
    }
    public void onBindViewHolder(@NonNull Addsubjectsviewholder holder, int position) {
        holder.mtextview.setText    (mlist.get(position).getReponame());
    }

    public int getItemCount() {
        return mlist.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }
    private Filter mFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<showandaddsubjectsdata> filteredlist=new ArrayList<>();
            if (constraint==null||constraint.length()==0){
                filteredlist.addAll(mlistfull);
            }else {
                String filterpattern=constraint.toString().toLowerCase().trim();
                for(showandaddsubjectsdata list:mlistfull){
                    if(list.getReponame().toLowerCase().contains(filterpattern)){
                        filteredlist.add(list);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredlist;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mlist.clear();
            mlist.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
