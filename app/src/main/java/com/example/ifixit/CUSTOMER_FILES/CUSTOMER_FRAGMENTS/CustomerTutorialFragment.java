package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.customer_tutorial_fragment,container,false);

        listener = this;

        ArrayList<ImageSlidesModel> autoImageList = new ArrayList<>();
        AutoImageSlider autoImageSlider = rootView.findViewById(R.id.autoImageSlider);

        // add some imagees or titles (text) inside the imagesArrayList
        try {
            autoImageList.add(new ImageSlidesModel("https://picsum.photos/id/237/200/300", "First image"));
        } catch (ExceptionsClass e) {
            throw new RuntimeException(e);
        }
        try {
            autoImageList.add(new ImageSlidesModel("https://picsum.photos/id/238/200/300", ""));
        } catch (ExceptionsClass e) {
            throw new RuntimeException(e);
        }
        try {
            autoImageList.add(new ImageSlidesModel("https://picsum.photos/id/239/200/300", "Third image"));
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
