package com.example.snistpms;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;
import static java.security.AccessController.getContext;

public class Showsubjectsadpater extends RecyclerView.Adapter<Showsubjectsadpater.Showsubjectsviewholder> {

    ArrayList<String> marraylist;
    setshowsubjectslistener mlistener;
    SQLiteDatabase subjectsdb;
    public interface setshowsubjectslistener{
        public void onitemclick(int position);
        public void ondeleteclick(int position);
    }
    public void setshowsubjectslistener(setshowsubjectslistener listener){
        mlistener=listener;
    }
    public static class Showsubjectsviewholder extends RecyclerView.ViewHolder{
        ImageView mfolderimage,mddeleteimage;
        TextView mtexttoshowsubject;
        public Showsubjectsviewholder(@NonNull View itemView, final setshowsubjectslistener listener) {
            super(itemView);
            mfolderimage=itemView.findViewById(R.id.folderimageinshow);
            mddeleteimage=itemView.findViewById(R.id.deletebutton);
            mtexttoshowsubject=itemView.findViewById(R.id.subjectnameinshow);
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
            mddeleteimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.ondeleteclick(position);
                        }
                    }
                }
            });
        }
    }
    public Showsubjectsadpater(ArrayList<String> arrayList){
        marraylist=arrayList;
    }
    @NonNull
    @Override
    public Showsubjectsviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.exampleitemtoshowsubjects,parent,false);
        Showsubjectsviewholder evh;
        evh = new Showsubjectsviewholder(v,mlistener);
        return  evh;
    }

    @Override
    public void onBindViewHolder(@NonNull Showsubjectsviewholder holder, int position) {
        holder.mtexttoshowsubject.setText(marraylist.get(position));
    }

    @Override
    public int getItemCount() {
        return marraylist.size();
    }
}
