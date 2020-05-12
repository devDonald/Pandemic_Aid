package com.gnorizon.solutions.pandemic_aid.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gnorizon.solutions.pandemic_aid.R;
import com.gnorizon.solutions.pandemic_aid.activities.ReportACase;
import com.gnorizon.solutions.pandemic_aid.activities.TakeTest;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View home_view = inflater.inflate(R.layout.fragment_home, container, false);
        Button take_test = home_view.findViewById(R.id.take_test);
        Button report_case = home_view.findViewById(R.id.report_case);

        take_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contact = new Intent(getContext(), TakeTest.class);
                startActivity(contact);
            }
        });

        report_case.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent report = new Intent(getContext(), ReportACase.class);
                startActivity(report);
            }
        });
        return home_view;
    }
}
