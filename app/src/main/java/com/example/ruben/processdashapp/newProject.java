package com.example.ruben.processdashapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class newProject extends Fragment {

    private Button b;
    private EditText nameP, timeE;
    private ArrayList<Project> projects;

    public newProject() {}

    @SuppressLint("ValidFragment")
    public newProject(ArrayList<Project> projects) {
        this.projects = projects;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_new_project, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        nameP = view.findViewById(R.id.inputNameProject);
        nameP.setImeOptions(EditorInfo.IME_ACTION_DONE);
        timeE = view.findViewById(R.id.inputTimeEstimated);
        timeE.setImeOptions(EditorInfo.IME_ACTION_DONE);

        b = view.findViewById(R.id.buttonNewProcess);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!nameP.getText().toString().trim().equals("") && !timeE.getText().toString().trim().equals(""))
                {
                    Project p = new Project();
                    p.setName(nameP.getText().toString());
                    p.setTimeEstimated(timeE.getText().toString());
                    projects.add(p);
                    listIterations li = new listIterations(projects);
                    Fragment fragment = li;
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frag, fragment).commit();
                }

                else
                {
                    Toast.makeText(getContext(), "Los campos no están rellenados.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
}
