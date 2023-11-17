package com.example.mychat;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class PreferencesFragment extends Fragment {
    private static final String MSG = "PreferencesFragment";
    private boolean     logedIn = false;
    private int userID = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        //CreateImage(rootView);

        return inflater.inflate(R.layout.fragment_preferences, container, false);
    }

    private void CreateImage(View view){
        ImageView imageView = (ImageView) view.findViewById(R.id.image) ;
        String filename = "android_001.png";
        InputStream inputStream = null;
        try{
//            getActivity().getApplication()
            inputStream = requireActivity().getApplicationContext().getAssets().open(filename);
            if (inputStream != null) {
                try {
                    Drawable drawable = Drawable.createFromStream(inputStream, null);
                    imageView.setImageDrawable(drawable);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                } catch ( Exception e1){
                    Log.e(MSG, "CreateImage E1: "+e1.getMessage());
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                if(inputStream!=null)
                    inputStream.close();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
