package com.gnorizon.solutions.pandemic_aid.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnorizon.solutions.pandemic_aid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutCovid19 extends Fragment {
    private View aboutView;

    public AboutCovid19() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        aboutView = inflater.inflate(R.layout.fragment_about_covid19, container, false);

        return  aboutView;
    }
}
