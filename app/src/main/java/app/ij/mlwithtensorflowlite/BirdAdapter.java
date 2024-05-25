package app.ij.mlwithtensorflowlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

import app.ij.mlwithtensorflowlite.data.Bird;
import app.ij.mlwithtensorflowlite.data.BirdImageComparator;
import app.ij.mlwithtensorflowlite.data.BirdStorage;

public class BirdAdapter extends RecyclerView.Adapter<BirdAdapter.ViewHolder> {
    List<Bird> birds;
    Context context;
    Boolean inLike;
    public BirdAdapter(List<Bird> birds,Context context,Boolean inLike){
        this.context = context;
        this.birds = birds;
        this.inLike = inLike;
        Collections.sort(birds,new BirdImageComparator());
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bird_item,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Bird bird = birds.get(position);
        List<Bird> birds1 = BirdStorage.getAllBirds(context);
        holder.like.setTag(R.drawable.heart_outlined);
        holder.like.setImageResource(R.drawable.heart_outlined);
        for (Bird b: birds) {
            for (Bird bi: birds1) {
                if(b.getId() == bi.getId()){
                    holder.like.setTag(R.drawable.heart_filled);
                    holder.like.setImageResource(R.drawable.heart_filled);
                }else {
                    Log.e("hi",bird.getId()+" "+b.getId());
                    holder.like.setTag(R.drawable.heart_outlined);
                    holder.like.setImageResource(R.drawable.heart_outlined);
                }
            }
        }
        holder.birdname.setText("Name: "+bird.getName());
        holder.sciname.setText("Sci-Name: "+bird.getSciName());
        holder.region.setText("Region: "+bird.getRegion().get(0));
        holder.family.setText("Family: "+bird.getFamily());
        if(!bird.getImages().isEmpty()) {
            String imageUrl = bird.getImages().get(0);
            Log.e("im", imageUrl + " " + position);
            Picasso.get().load(imageUrl).fit().centerCrop().into(holder.image);
        }
        else {
            Picasso.get().load(R.drawable.question).into(holder.image);
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("hi",bird.getName());
                if ((int) holder.like.getTag() == R.drawable.heart_outlined) {
                    holder.like.setImageResource(R.drawable.heart_filled);
                    holder.like.setTag(R.drawable.heart_filled); // Update tag
                    BirdStorage.saveBird(context, bird);
                } else {
                    if(inLike){
                        if(position != RecyclerView.NO_POSITION){
                            birds.remove(position);
                            notifyItemRemoved(position);
                        }
                    }
                    holder.like.setImageResource(R.drawable.heart_outlined);
                    holder.like.setTag(R.drawable.heart_outlined); // Update tag
                    BirdStorage.removeBird(context, bird.getId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return birds.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView birdname;
        TextView sciname;
        TextView region;
        TextView family;
        ImageView image;
        ImageView like;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            birdname = itemView.findViewById(R.id.birdName);
            sciname = itemView.findViewById(R.id.sciName);
            region = itemView.findViewById(R.id.region);
            family = itemView.findViewById(R.id.family);
            image = itemView.findViewById(R.id.birdImage);
            like = itemView.findViewById(R.id.like);
        }
    }
}
