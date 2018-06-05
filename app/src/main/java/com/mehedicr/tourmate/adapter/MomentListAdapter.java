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
import android.widget.TextView;

import com.mehedicr.tourmate.Model.Moment;
import com.mehedicr.tourmate.R;


import java.util.ArrayList;
import java.util.List;

public class MomentListAdapter extends RecyclerView.Adapter<MomentListAdapter.MomentHolder> {

    Context context;
    List<Moment>momentList = new ArrayList<>();

    public MomentListAdapter(Context context, List<Moment> momentList){
        this.context = context;
        this.momentList = momentList;
    }


    @NonNull
    @Override
    public MomentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.moment_list_item,parent,false);
        return new MomentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MomentHolder holder, int position) {
        Moment moment = momentList.get(position);
        Bitmap bitmap = BitmapFactory.decodeFile(moment.getPhotoPath());
        holder.photoImageView.setImageBitmap(bitmap);
        holder.photoNameTextView.setText(moment.getFileName());
        holder.photoNameWithFormatTextView.setText(moment.getFormatName());
        holder.dateTextView.setText(moment.getDate());
    }


    @Override
    public int getItemCount() {
        return momentList.size();
    }

    public class MomentHolder extends RecyclerView.ViewHolder{
        private ImageView photoImageView;
        private TextView photoNameTextView;
        private TextView photoNameWithFormatTextView;
        private TextView dateTextView;
        private ImageView editImageButton;

        public MomentHolder(View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
            photoNameTextView = itemView.findViewById(R.id.fileNameTextView);
            photoNameWithFormatTextView = itemView.findViewById(R.id.fileNameWithFormat);
            dateTextView = itemView.findViewById(R.id.captureDateTextView);
            editImageButton = itemView.findViewById(R.id.editImageButton);

            editImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }
}
