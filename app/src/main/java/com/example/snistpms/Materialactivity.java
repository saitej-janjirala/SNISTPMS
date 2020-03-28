package com.example.snistpms;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.ArrayList;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Materialactivity extends Fragment
{
    Button addsubjects;
    RecyclerView mrecyclerview;
    SQLiteDatabase subjectsdb,deletedsubjectsdb;
    Cursor cursor;
    RecyclerView.LayoutManager mlayoutmanager;
    ArrayList<String> marraylist;
    Showsubjectsadpater madapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.materials, container, false);
        addsubjects=v.findViewById(R.id.addsubjects);
        mrecyclerview=v.findViewById(R.id.recyclerviewtoshowsubjects);
        marraylist=new ArrayList<>();
        subjectsdb= Objects.requireNonNull(getContext()).openOrCreateDatabase("SNIST_PMS1", Context.MODE_PRIVATE,null);
        subjectsdb.execSQL("create table if not exists subjects(name varchar(100) unique)");
        cursor=subjectsdb.rawQuery("select name from subjects",null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                int a=cursor.getColumnIndex("name");
                marraylist.add(cursor.getString(a));
            }
        }
        mrecyclerview.setHasFixedSize(true);
        madapter=new Showsubjectsadpater(marraylist);
        mlayoutmanager=new LinearLayoutManager(getContext());
        mrecyclerview.setLayoutManager(mlayoutmanager);
        mrecyclerview.setVerticalScrollBarEnabled(true);
        mrecyclerview.setAdapter(madapter);
        madapter.setshowsubjectslistener(new Showsubjectsadpater.setshowsubjectslistener() {
            @Override
            public void onitemclick(int position) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction .replace(R.id.fragment_container,new showppts(marraylist.get(position)));
                fragmentTransaction.addToBackStack("materialtoppts").commit();
            }
            @Override
            public void ondeleteclick(int position) {
                    subjectsdb.execSQL("delete from subjects where name= '"+marraylist.get(position)+"'" );
                    marraylist.remove(position);
                    madapter.notifyItemRemoved(position);
            }
        });
        addsubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,new showandaddsubjects());
                fragmentTransaction.addToBackStack(null).commit();
            }
        });
        return v;
    }
}
