package com.gnorizon.solutions.pandemic_aid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;


import com.gnorizon.solutions.pandemic_aid.R;
import com.gnorizon.solutions.pandemic_aid.helpers.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AboutUs extends AppCompatActivity {

    private static ExpandableListView expandableListView;
    private ExpandableListAdapter adapter;
    private AppCompatActivity activity;
    private Dialog popDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        popDialog = new Dialog(AboutUs.this);

        expandableListView =
                (ExpandableListView)findViewById(R.id.list_viewEp);
        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);

        setItems();
        setListener();

    }


    // Setting headers and childs to expandable listview
    void setItems() {

        // Array list for header
        ArrayList<String> header = new ArrayList<String>();

        // Array list for child items
        List<String> child1 = new ArrayList<String>();
        List<String> child2 = new ArrayList<String>();

        // Hash map for both header and child
        HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();

        // Adding headers to list

        header.add("Who we Are");
        header.add("What we do");

        child2.add("IT CONSULTING");
        child2.add("SOFTWARE DEVELOPMENT");
        child2.add("NETWORK DESIGN");
        child2.add("CREATION OF BRANDS");


        child1.add(getString(R.string.text_assignment));


        // Adding header and childs to hash map
        hashMap.put(header.get(0), child1);
        hashMap.put(header.get(1), child2);

        adapter = new ExpandableListAdapter(getApplicationContext(), header, hashMap);

        // Setting adpater over expandablelistview
        expandableListView.setAdapter(adapter);
    }

    // Setting different listeners to expandablelistview
    void setListener() {

        // This listener will show toast on group click
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView listview, View view,
                                        int group_pos, long id) {

                return false;
            }
        });

        // This listener will expand one group at one time
        // You can remove this listener for expanding all groups
        expandableListView
                .setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                    // Default position
                    int previousGroup = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousGroup)

                            // Collapse the expanded group
                            expandableListView.collapseGroup(previousGroup);
                        previousGroup = groupPosition;
                    }

                });

        // This listener will show toast on child click
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView listview, View view,
                                        int groupPos, int childPos, long id) {
                if(groupPos==1 && childPos==0){

                    showPopup(R.layout.about_consulting);
                }
                if (groupPos==1 && childPos==1){

                    showPopup(R.layout.about_software);

                }

                if(groupPos==1 && childPos==2){

                    showPopup(R.layout.about_network);


                }

                if(groupPos==1 && childPos==3){

                    showPopup(R.layout.about_brand);


                }


                return false;
            }
        });
    }


    public void showPopup(int view) {
        TextView txtclose;
        popDialog.setContentView(view);
        txtclose =popDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popDialog.dismiss();
            }
        });
        popDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popDialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}
