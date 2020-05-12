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
public class ContactChannels extends Fragment {

    public ContactChannels() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_channels, container, false);
    }
}
