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
public class ReviewFragment extends Fragment {
    @BindView(R.id.section_label3) TextView label;
    private String contents;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        ButterKnife.bind(this, view);
        label.setText("Review " + contents);
        return view;
    }

    public void update(int tag) {
        Place p = MainActivity.places.get(tag-1);
        label.setText("Review " + p.name);
    }
}
