package com.gnorizon.solutions.pandemic_aid.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gnorizon.solutions.pandemic_aid.R;

public class SeeDoctor extends Fragment {
    private Button call_doctor;
    private View doc_view;


    public SeeDoctor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        doc_view = inflater.inflate(R.layout.fragment_see_doctor, container, false);

        call_doctor = doc_view.findViewById(R.id.call_doctor);

        call_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "+23480097000010";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        return doc_view;
    }


}
