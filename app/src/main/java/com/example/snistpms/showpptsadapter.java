package com.example.snistpms;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class showpptsadapter extends RecyclerView.Adapter<showpptsadapter.showpptsviewholder>{
    private String msubjectname;
    ArrayList<ImageView> imageViews;
    Context mcontext;
    private setonpptselectedlistener mlistener;
    ArrayList<showpptsdata> mshowpptsdataobject;
    public interface setonpptselectedlistener{
        public void ondownload(int positon);
        public void onitemclick(int position,String type);
    }
    public void setonpptselectedlistener(setonpptselectedlistener listener){
        mlistener=listener;
    }
    public static class showpptsviewholder extends RecyclerView.ViewHolder{
        ImageView mimagefiletype,mdownloadimage;
        TextView mnameoffile,mprogresstext;
        ImageView sendimageview;

        public showpptsviewholder(@NonNull View itemView, final setonpptselectedlistener listener,final Context context) {
            super(itemView);
            mimagefiletype=itemView.findViewById(R.id.filetype);
            mdownloadimage=itemView.findViewById(R.id.download);
            mnameoffile=itemView.findViewById(R.id.pptstext);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            PopupMenu popup=new PopupMenu(context,view, Gravity.NO_GRAVITY);
                            MenuInflater inflater = popup.getMenuInflater();
                            inflater.inflate(R.menu.viewer, popup.getMenu());
                            popup.setOnMenuItemClickListener(new MyMenuItemClickListener(listener,position));
                            popup.show();
                            //listener.onitemclick(position);
                        }
                    }
                }
            });
            mdownloadimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            sendimageview=mdownloadimage;
                            listener.ondownload(position);
                        }
                    }
                }
            });

        }
        private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
            setonpptselectedlistener xlistener;
            int xposition;
            public MyMenuItemClickListener(setonpptselectedlistener listener,int position) {
                xlistener=listener;
                xposition=position;
            }
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.officeview:
                        xlistener.onitemclick(xposition,"microsoft");
                        return true;
                    case R.id.googledocs:
                        xlistener.onitemclick(xposition,"google");
                        return true;
                    default:
                }
                return false;
            }

        }
    }
    public showpptsadapter(ArrayList<showpptsdata> showpptsdataobject, String subjectname, Context context){
        mshowpptsdataobject=new ArrayList<>();
        this.mshowpptsdataobject=showpptsdataobject;
        this.msubjectname=subjectname;
        imageViews=new ArrayList<>();
        mcontext=context;
    }
    @NonNull
    @Override
    public showpptsviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.exampleitemforshowppts,parent,false);
        showpptsviewholder vh=new showpptsviewholder(view,mlistener,mcontext);

        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull showpptsviewholder holder, int position) {
        holder.mnameoffile.setText(mshowpptsdataobject.get(position).getMdocname());
        File file=new File("/storage/emulated/0/SNIST-PMS/"+msubjectname+"/"+mshowpptsdataobject.get(position).getMdocname());
        if(file.exists()){
            holder.mdownloadimage.setImageResource(R.drawable.check);
        }
        else{
            holder.mdownloadimage.setImageResource(R.drawable.ic_file_download_black_24dp);
        }

    }

    @Override
    public int getItemCount() {
        return mshowpptsdataobject.size();
    }
}
