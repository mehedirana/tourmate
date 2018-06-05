package com.mehedicr.tourmate.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mehedicr.tourmate.Model.Moment;
import com.mehedicr.tourmate.R;


import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryItemHolder> {
    private Context context;
    private List<Moment> momentList = new ArrayList<>();

    public GalleryAdapter(Context context, List<Moment>momentList){
        this.context = context;
        this.momentList = momentList;
    }
    @NonNull
    @Override
    public GalleryItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_list_item,parent,false);
        return new GalleryItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryItemHolder holder, int position) {
        Moment moment = momentList.get(position);
        Bitmap bitmap = BitmapFactory.decodeFile(moment.getPhotoPath());
        holder.galleryImageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return momentList.size();
    }

    public class GalleryItemHolder extends RecyclerView.ViewHolder{
        private ImageView galleryImageView;

        public GalleryItemHolder(View itemView) {
            super(itemView);
            galleryImageView = itemView.findViewById(R.id.galleryImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
