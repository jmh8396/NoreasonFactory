package com.example.gwpark.shit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gwpark.shit.Data.Place;
import com.example.gwpark.shit.MainActivity;
import com.example.gwpark.shit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gwpark on 16. 8. 11..
 */
public class InfoFragment extends Fragment {
    @BindView(R.id.restaurant_name) TextView name;
    @BindView(R.id.restaurant_location) TextView location;
    @BindView(R.id.restaurant_time) TextView time;
    @BindView(R.id.restaurant_url) TextView url;
    private String contents;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void update(int tag) {
        Place p = MainActivity.places.get(tag-1);
        this.name.setText(p.name);
        this.location.setText(p.location);
        this.time.setText("시간");
        this.url.setText(p.tel);
    }
}
