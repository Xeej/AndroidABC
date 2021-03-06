package com.example.examplereg.ui.home;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.example.examplereg.R;
import com.google.android.gms.maps.GoogleMap;


import org.osmdroid.views.MapView;




public class HomeFragment extends Fragment  {


    private HomeViewModel homeViewModel;
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View mView = inflater.inflate(R.layout.fragment_home, container, false);

        Button mapp;
        mapp = (Button) mView.findViewById(R.id.mapp);
        View.OnClickListener click = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent MapsActivity1 = new Intent(HomeFragment.this.getActivity(), com.example.examplereg.MapsActivity.class);

                startActivity(MapsActivity1);
            }
        };

        mapp.setOnClickListener(click);



        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return mView;
    }




}