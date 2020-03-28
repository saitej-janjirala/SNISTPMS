package com.example.snistpms;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

public class showppts extends Fragment{
    RecyclerView mrecyclerview;
    showpptsadapter madapater;
    RecyclerView.LayoutManager mlayoutmanager;
    String x;
    ArrayList<showpptsdata> showpptsdataobject;
    List<RepoContentList> mlist;
    String filename,contenturl;
    static  final int PERMISSION_REQUEST_CODE=2;
    long downloadID=-1;
    ProgressBar progressBar;
    //ImageView mimageView;
    Networkconnection networkconnection;
    public showppts(String val){
        x=val;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.showppts,container,false);
        mrecyclerview=view.findViewById(R.id.recyclerviewtoshowppts);
        mlayoutmanager=new LinearLayoutManager(getContext());
        progressBar=view.findViewById(R.id.progressbartoshowppts);
        showpptsdataobject=new ArrayList<>();
        networkconnection=new Networkconnection(getContext());
        getActivity().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        progressBar.setVisibility(View.VISIBLE);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        final Githubclient client = retrofit.create(Githubclient.class);
        Call<List<RepoContentList>> contentcall = client.UserRepositoriesContents("naruto-pdfs",x);
        contentcall.enqueue(new Callback<List<RepoContentList>>() {
            @Override
            public void onResponse(Call<List<RepoContentList>> call, Response<List<RepoContentList>> response) {
                final showpptsadapter madapter;
                mlist=response.body();
                Log.i("size",mlist.size()+"");
                mrecyclerview.setHasFixedSize(true);
                for(int i=0;i<mlist.size();i++){
                    if((mlist.get(i).getContentname().equals("README.md"))||(mlist.get(i).getContentname().equals(".gitattributes"))){
                        mlist.remove(i);
                        i-=1;
                    }
                    else {
                        Log.i("ppts",mlist.get(i).getContentname());
                        showpptsdataobject.add(new showpptsdata(mlist.get(i).getContentname(),mlist.get(i).getContenturl()));
                    }
                }
                madapter=new showpptsadapter(showpptsdataobject,x,getContext());
                mrecyclerview.setLayoutManager(mlayoutmanager);
                mrecyclerview.setAdapter(madapter);
                progressBar.setVisibility(View.GONE);
                try{
                    madapter.setonpptselectedlistener(new showpptsadapter.setonpptselectedlistener() {
                        public void ondownload(int positon) {
                            contenturl=showpptsdataobject.get(positon).getMurl();
                            filename=showpptsdataobject.get(positon).getMdocname();
                            File file=new File("/storage/emulated/0/SNIST-PMS/"+x+"/"+filename);
                            if(file.exists()){
                                Toast.makeText(getContext(),"Already downloaded",Toast.LENGTH_SHORT).show();
                            }
                            else {

                                //downloadFile();

                                if(checkPermission()){
                                    //mimageView.setImageResource(R.drawable.loading);
                                    if(networkconnection.isnetworkavailable()){
                                        Downloaddocs mysync=new Downloaddocs(x,filename,contenturl);
                                        Toast.makeText(getContext(),"Downloading....",Toast.LENGTH_SHORT).show();
                                        mysync.execute(contenturl);
                                    }
                                    else{
                                        Toast.makeText(getContext(),"Check Your Internet Connection",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    requestPermission();
                                }
                            }
                        }
                        @Override
                        public void onitemclick(int position,String type) {
                            if(networkconnection.isnetworkavailable()){
                                Intent intent=new Intent(getContext(),pdfview.class);
                                intent.putExtra("path","/"+x+"/"+mlist.get(position).getContentname());
                                intent.putExtra("url",showpptsdataobject.get(position).getMurl());
                                //intent.putExtra("htmlurl",showpptsdataobject.get(position).getMhtmlurl());
                                intent.putExtra("type",type);
                                startActivity(intent);
                                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/viewer?url="+mlist.get(position).getContenturl()));
                                //startActivity(browserIntent);
                                Toast.makeText(getContext(),showpptsdataobject.get(position).getMdocname(),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getContext(),"Check Your Internet Connection",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<List<RepoContentList>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Go Back And Try Again",Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }

    public class Downloaddocs extends AsyncTask<String,Integer,String>{
        String xfilename;
        String xdocname;
        String xurl;
        public Downloaddocs(String filename,String docname,String url) {
            super();
            xfilename=filename;
            xdocname=docname;
            xurl=url;
        }
        @Override
        protected String doInBackground(String... strings) {
            DownloadManager downloadmanager = (DownloadManager) Objects.requireNonNull(getActivity()).getBaseContext().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(xurl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(filename);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            Log.i("directory",Environment.getRootDirectory()+"");
            request.setDestinationInExternalPublicDir("/SNIST-PMS/"+x,xdocname);
            request.setAllowedOverMetered(true);
            // Set if download is allowed on Mobile network
            request.setAllowedOverRoaming(true);
            assert downloadmanager != null;
            downloadID = downloadmanager.enqueue(request);
            return null;
        }


    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(),"Click again to start download",Toast.LENGTH_SHORT).show();

                } else {
                   Toast.makeText(getContext(),"Permissions denied",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //DownloadManager.ERROR_INSUFFICIENT_SPACE;
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {

                Toast.makeText(getContext(), "Download Completed", Toast.LENGTH_SHORT).show();
                //mimageView.setImageResource(R.drawable.check);
            }
            else {
                //mimageView.setImageResource(R.drawable.ic_file_download_black_24dp);
                Toast.makeText(getContext(),"DOWNLOAD FAILED",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(onDownloadComplete);
    }
}
