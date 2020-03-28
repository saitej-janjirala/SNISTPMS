package com.example.snistpms;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class showandaddsubjects extends Fragment {
    RecyclerView mrecyclerview;
    Addsubjectsadapter madapter;
    RecyclerView.LayoutManager mlayoutmanager;
    SQLiteDatabase subjectsdb;
    ArrayList<showandaddsubjectsdata> mshowandaddsubjectsdataobject;
    List<RepoList> list;
    Cursor cursor;
    ProgressBar mprogressbar;
    SwipeRefreshLayout mswiperefreshlayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.showandaddsubjects,container,false);
        mrecyclerview=view.findViewById(R.id.recyclerviewtoaddsubjects);
        mprogressbar=view.findViewById(R.id.progressbarinshowandadd);
        mprogressbar.setVisibility(View.VISIBLE);
        mlayoutmanager=new LinearLayoutManager(getContext());
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        mshowandaddsubjectsdataobject=new ArrayList<>();
        subjectsdb= Objects.requireNonNull(getContext()).openOrCreateDatabase("SNIST_PMS1", MODE_PRIVATE,null);
        subjectsdb.execSQL("create table if not exists subjects(name varchar(100) unique)");
        final Githubclient client = retrofit.create(Githubclient.class);
        Call<List<RepoList>> call = client.UserRepositories("naruto-pdfs");
        call.enqueue(new Callback<List<RepoList>>() {
            @Override
            public void onResponse(Call<List<RepoList>> call, Response<List<RepoList>> response) {
                list = response.body();
                cursor=subjectsdb.rawQuery("select name from subjects",null);
                if(cursor!=null){
                    while (cursor.moveToNext()){
                        int a=cursor.getColumnIndex("name");
                        for(int i=0;i<list.size();i++){
                            if(cursor.getString(a).equals(list.get(i).getRepoName())){
                                list.remove(i);
                            }
                        }
                    }
                }
                for(int i=0;i<list.size();i++){
                    mshowandaddsubjectsdataobject.add(new showandaddsubjectsdata(list.get(i).getRepoName()));
                }
                mrecyclerview.setHasFixedSize(true);
                madapter=new Addsubjectsadapter(mshowandaddsubjectsdataobject,subjectsdb);
                mrecyclerview.setLayoutManager(mlayoutmanager);
                mrecyclerview.setAdapter(madapter);
                mprogressbar.setVisibility(View.GONE);
                madapter.setonsubjectclicklistener(new Addsubjectsadapter.setonsubjectclicklistener() {
                    @Override
                    public void onitemclick(int position){
                      Toast.makeText(getContext(),"item clicked",Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction .replace(R.id.fragment_container,new showppts(list.get(position).getRepoName()));
                        fragmentTransaction.addToBackStack(null).commit();
                    }
                    @Override
                    public void onaddclick(int position) {
                        Toast.makeText(getContext(),list.get(position).getRepoName()+" is added",Toast.LENGTH_SHORT).show();
                        try {
                            String repname=list.get(position).getRepoName();
                            list.remove(position);
                            madapter.notifyItemRemoved(position);
                            subjectsdb.execSQL("insert into subjects values('" + repname+ "')");
                        }
                        catch (Exception e){
                            Toast.makeText(getContext(),"Already added",Toast.LENGTH_SHORT).show();
                        }
                        //FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                        //FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        //fragmentTransaction.replace(R.id.fragment_container,new Materialactivity());
                        //fragmentTransaction.addToBackStack(null);
                        //fragmentTransaction.commit();
                    }
                });
            }
            @Override
            public void onFailure(Call<List<RepoList>> call, Throwable t) {
                Toast.makeText(getContext(),t.getCause().toString(),Toast.LENGTH_SHORT).show();
                mprogressbar.setVisibility(View.GONE);
            }
        });
        return  view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                madapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

}
