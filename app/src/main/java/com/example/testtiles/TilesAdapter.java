package com.example.testtiles;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TilesAdapter extends RecyclerView.Adapter<TilesAdapter.TilesHolder>{
    private Context context;
    private List<DataModel> tiles;
    private OnItemClickListener clickListener;

    public TilesAdapter(Context context, List<DataModel> data){
        this.context = context;
        this.tiles = data;
    }

    @NonNull
    @Override
    public TilesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        return new TilesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TilesHolder holder, int position) {
        context = holder.itemView.getContext();

        DataModel current = tiles.get(position);
        Glide.with(context)
                .load(current.getImgUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

        holder.title.setText(current.getTitle());
    }

    @Override
    public int getItemCount() {
        return tiles.size();
    }

    public class TilesHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView imageView;

        public TilesHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            imageView = (ImageView) itemView.findViewById(R.id.title_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position =getAdapterPosition();
                    if(clickListener!=null && position!=RecyclerView.NO_POSITION){
                        clickListener.onItemClick(tiles.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DataModel dataModel);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }
}
