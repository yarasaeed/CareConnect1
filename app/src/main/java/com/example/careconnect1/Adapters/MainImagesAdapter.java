package com.example.careconnect1.Adapters;

import static com.example.careconnect1.Utilities.Config.MAIN_IMAGES_DIR;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.example.careconnect1.R;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;

public class MainImagesAdapter extends PagerAdapter {
    private final Activity context;
    private final ArrayList<String> arrayList;
    private  View custom = null;

    public MainImagesAdapter(Activity context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @SuppressLint("SetTextI18n")
    @Override
    public  Object instantiateItem(@NonNull ViewGroup container, int position) {
        try {
            custom = View.inflate(context, R.layout.layout_main_flipper, null);
            ShapeableImageView imageView = custom.findViewById(R.id.imageView);
            try
            {
                Glide.with(context).load(MAIN_IMAGES_DIR  +
                        arrayList.get(position))
                        .into(imageView);

            }catch (Exception | Error ignored){}
             container.addView(custom);
            return custom;
        }catch (Exception | Error ignored){}
        return custom;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }
}