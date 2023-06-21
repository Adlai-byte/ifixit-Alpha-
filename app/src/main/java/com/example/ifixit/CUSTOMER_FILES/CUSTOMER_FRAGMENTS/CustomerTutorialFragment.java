package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.codebyashish.autoimageslider.AutoImageSlider;
import com.codebyashish.autoimageslider.Enums.ImageActionTypes;
import com.codebyashish.autoimageslider.Enums.ImageScaleType;
import com.codebyashish.autoimageslider.ExceptionsClass;
import com.codebyashish.autoimageslider.Interfaces.ItemsListener;
import com.codebyashish.autoimageslider.Models.ImageSlidesModel;
import com.example.ifixit.R;

import java.util.ArrayList;

public class CustomerTutorialFragment extends Fragment implements ItemsListener {

    private ItemsListener listener;
    ViewPager2 viewPager2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.customer_tutorial_fragment,container,false);

        listener = this;

        ArrayList<ImageSlidesModel> autoImageList = new ArrayList<>();
        AutoImageSlider autoImageSlider = rootView.findViewById(R.id.autoImageSlider);

        // add some imagees or titles (text) inside the imagesArrayList

        try {
            autoImageList.add(new ImageSlidesModel(R.drawable.a1, "Step 1 "));
        } catch (ExceptionsClass e) {
            throw new RuntimeException(e);
        }
        try {
            autoImageList.add(new ImageSlidesModel(R.drawable.a2, "Step 2"));
        } catch (ExceptionsClass e) {
            throw new RuntimeException(e);
        }

        try {
            autoImageList.add(new ImageSlidesModel(R.drawable.a3, "Step 3 "));
        } catch (ExceptionsClass e) {
            throw new RuntimeException(e);
        }

        try {
            autoImageList.add(new ImageSlidesModel(R.drawable.a4, "Step 4 "));
        } catch (ExceptionsClass e) {
            throw new RuntimeException(e);
        }

        try {
            autoImageList.add(new ImageSlidesModel(R.drawable.a5, "Step 5 "));
        } catch (ExceptionsClass e) {
            throw new RuntimeException(e);
        }

        try {
            autoImageList.add(new ImageSlidesModel(R.drawable.a6, "Step 6 "));
        } catch (ExceptionsClass e) {
            throw new RuntimeException(e);
        }

        try {
            autoImageList.add(new ImageSlidesModel(R.drawable.a7, "Step 7 "));
        } catch (ExceptionsClass e) {
            throw new RuntimeException(e);
        }
        try {
            autoImageList.add(new ImageSlidesModel(R.drawable.a8, "Step a8 "));
        } catch (ExceptionsClass e) {
            throw new RuntimeException(e);
        }
        try {
            autoImageList.add(new ImageSlidesModel(R.drawable.a9, "Step 9 "));
        } catch (ExceptionsClass e) {
            throw new RuntimeException(e);
        }
        try {
            autoImageList.add(new ImageSlidesModel(R.drawable.a10, "Step 10 "));
        } catch (ExceptionsClass e) {
            throw new RuntimeException(e);
        }
        try {
            autoImageList.add(new ImageSlidesModel(R.drawable.a11, "Step 11 "));
        } catch (ExceptionsClass e) {
            throw new RuntimeException(e);
        }
        try {
            autoImageList.add(new ImageSlidesModel(R.drawable.a12, "Step 12 "));
        } catch (ExceptionsClass e) {
            throw new RuntimeException(e);
        }





        // set the added images inside the AutoImageSlider
        autoImageSlider.setImageList(autoImageList, ImageScaleType.FIT);

        // set any default animation or custom animation (setSlideAnimation(ImageAnimationTypes.ZOOM_IN))
        autoImageSlider.setDefaultAnimation();

        // handle click event on item click
        autoImageSlider.onItemClickListener(listener);


        return rootView;
    }

    @Override
    public void onItemChanged(int position) {

    }

    @Override
    public void onTouched(ImageActionTypes actionTypes, int position) {

    }

    @Override
    public void onItemClicked(int position) {

    }
}
