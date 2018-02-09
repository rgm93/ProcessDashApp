package com.example.ruben.processdashapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProcessChoose extends Fragment {

    private ArrayList<Project> projects = new ArrayList<>();
    private ArrayList<String> procesos;
    private ArrayAdapter<String> listProcesos;
    private Button botonFases;
    private CheckBox c1, c2, c3, c4, c5, c6;
    private int pos;

    public ProcessChoose() {}

    @SuppressLint("ValidFragment")
    public ProcessChoose(ArrayList<Project> projects, int pos)
    {
        this.projects = projects;
        this.pos = pos;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_process_choose, container, false);

        String name = projects.get(projects.size()-1).getIterations().get(pos).getName();
        TextView tName = view.findViewById(R.id.name);
        tName.setText(name);


        procesos = new ArrayList<>();
        listProcesos = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, procesos);
        listProcesos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        c1 = view.findViewById(R.id.checkBoxP);
        c2 = view.findViewById(R.id.checkBoxD);
        c3 = view.findViewById(R.id.checkBoxC);
        c4 = view.findViewById(R.id.checkBoxCm);
        c5 = view.findViewById(R.id.checkBoxT);
        c6 = view.findViewById(R.id.checkBoxPM);

        c1.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
        c2.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
        c3.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
        c4.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
        c5.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
        c6.setOnCheckedChangeListener(new myCheckBoxChnageClicker());

        botonFases = view.findViewById(R.id.buttonFases);
        botonFases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!procesos.isEmpty()) {
                    projects.get(projects.size()-1).getIterations().get(pos).setProcesos(procesos);
                    ProcessCounter pc = new ProcessCounter(projects, pos);
                    Fragment fragment = pc;
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frag, fragment).commit();
                } else {
                    Toast.makeText(getContext(), "No se ha elegido ninguna fase.", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }

    class myCheckBoxChnageClicker implements CheckBox.OnCheckedChangeListener
    {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
        {
            if (b) {
                if (compoundButton == c1) {
                    procesos.add("Planning");
                } else if (compoundButton == c2) {
                    procesos.add("Design");
                } else if (compoundButton == c3) {
                    procesos.add("Code");
                } else if (compoundButton == c4) {
                    procesos.add("Compile");
                } else if (compoundButton == c5) {
                    procesos.add("Test");
                } else if (compoundButton == c6) {
                    procesos.add("PostMortem");
                }
            } else {
                if((compoundButton == c1)) {
                    procesos.remove("Planning");
                } else if((compoundButton == c2)) {
                    procesos.remove("Design");
                } else if((compoundButton == c3)) {
                    procesos.remove("Code");
                } else if((compoundButton == c4)) {
                    procesos.remove("Compile");
                } else if((compoundButton == c5)) {
                    procesos.remove("Test");
                } else if((compoundButton == c6)) {
                    procesos.remove("PostMortem");
                }
            }
        }
    }
}
