package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ifixit.R;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder>{

    private List<SlideItem> slideItemList;
    private ViewPager2 viewPager2;

    public SlideAdapter(List<SlideItem> slideItemList, ViewPager2 viewPager2) {
        this.slideItemList = slideItemList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SlideViewHolder(LayoutInflater.from(parent.getContext()).inflate(com.codebyashish.autoimageslider.R.layout.layout_slider_image,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
            holder.setImage(slideItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return slideItemList.size();
    }

    class SlideViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

         SlideViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.autoImageSlider);
        }


    void setImage(SlideItem slideItem) {
        imageView.setImageResource(slideItem.getImage());
    }
  }

}
