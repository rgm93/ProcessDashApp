package com.example.ruben.processdashapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SuppressLint("ValidFragment")
public class listIterations extends Fragment {


    private ListView listView;
    private ArrayList<String> procesosIn = new ArrayList<>();
    private ArrayList<Iteration> iterations = new ArrayList<>();
    private ArrayAdapter<Iteration> listViewAdapter;
    private static int contadorIteraciones = 0;
    private int pos; private boolean remove;
    private ArrayList<Project> projects = new ArrayList<>();

    public listIterations(){}

    public listIterations(ArrayList<Project> projects)
    {
        this.projects = projects;
    }

    public listIterations(ArrayList<Project> projects, int pos)
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

        View view = inflater.inflate(R.layout.fragment_list_iterations, container, false);
        listView = view.findViewById(R.id.listViewIterations);

        if(!projects.get(projects.size() - 1).getIterations().isEmpty()) {
            if (!projects.get(projects.size() - 1).getIterations().get(pos).getProcesosOut().isEmpty()) {
                for (int i = 0; i < projects.get(projects.size() - 1).getIterations().get(pos).getProcesosOut().size(); i++) {
                    if (projects.get(projects.size() - 1).getIterations().get(pos).getProcesosOut().get(i).equals("Planning")) {
                        projects.get(projects.size() - 1).getIterations().get(pos).setPlanTime(projects.get(projects.size() - 1).getIterations().get(pos).getValorCronos()[i]);
                    } else if (projects.get(projects.size() - 1).getIterations().get(pos).getProcesosOut().get(i).equals("Design")) {
                        projects.get(projects.size() - 1).getIterations().get(pos).setDesignTime(projects.get(projects.size() - 1).getIterations().get(pos).getValorCronos()[i]);
                    } else if (projects.get(projects.size() - 1).getIterations().get(pos).getProcesosOut().get(i).equals("Code")) {
                        projects.get(projects.size() - 1).getIterations().get(pos).setCodingTime(projects.get(projects.size() - 1).getIterations().get(pos).getValorCronos()[i]);
                    } else if (projects.get(projects.size() - 1).getIterations().get(pos).getProcesosOut().get(i).equals("Compile")) {
                        projects.get(projects.size() - 1).getIterations().get(pos).setCompileTime(projects.get(projects.size() - 1).getIterations().get(pos).getValorCronos()[i]);
                    } else if (projects.get(projects.size() - 1).getIterations().get(pos).getProcesosOut().get(i).equals("Test")) {
                        projects.get(projects.size() - 1).getIterations().get(pos).setTestTime(projects.get(projects.size() - 1).getIterations().get(pos).getValorCronos()[i]);
                    } else if (projects.get(projects.size() - 1).getIterations().get(pos).getProcesosOut().get(i).equals("PostMortem")) {
                        projects.get(projects.size() - 1).getIterations().get(pos).setPostMortemTime(projects.get(projects.size() - 1).getIterations().get(pos).getValorCronos()[i]);
                    }
                }
                projects.get(projects.size() - 1).getIterations().get(pos).setCreado(true);
            }
        }

        listViewAdapter = new ArrayAdapter<Iteration>(getActivity(), android.R.layout.simple_list_item_1,  projects.get(projects.size() - 1).getIterations()){

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);

                return view;
            }
        };

        listView.setAdapter(listViewAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(!remove)
                {
                    if ( projects.get(projects.size() - 1).getIterations().get(position).getCreado()) {
                        Toast.makeText(getContext(), "Iteracion ya realizada", Toast.LENGTH_SHORT).show();
                    } else {
                        projects.get(projects.size()-1).setIterations(projects.get(projects.size() - 1).getIterations());
                        ProcessChoose pc = new ProcessChoose(projects, position);
                        Fragment fragment = pc;
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frag, fragment).commit();
                    }
                }
                else if (remove)
                {
                    final int p = position;
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    projects.get(projects.size() - 1).getIterations().remove(p);
                                    for (int i = 0; i <  projects.get(projects.size() - 1).getIterations().size(); i++)
                                    {
                                        projects.get(projects.size() - 1).getIterations().get(i).setName("Iteration " + (contadorIteraciones-1));
                                    }

                                    listView.setAdapter(listViewAdapter);
                                    contadorIteraciones--;
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("¿Deseas eliminar esta iteración?").setPositiveButton("Si", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                    remove = false;

                }
            }
        });

        Button botonIteracion = view.findViewById(R.id.buttonAddIteracion);
        botonIteracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iteration it = new Iteration();
                contadorIteraciones++;
                it.setName("Iteration " + contadorIteraciones);
                projects.get(projects.size() - 1).getIterations().add(it);
                listView.setAdapter(listViewAdapter);
            }
        });

        Button botonRemoveIteration = view.findViewById(R.id.buttonRemoveIteracion);
        botonRemoveIteration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove = true;
                Toast.makeText(getContext(), "Elige una iteración a eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        Button botonFinalizarProyecto = view.findViewById(R.id.buttonEndIteracion);
        botonFinalizarProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcularMedicion();
                listProjects pc = new listProjects(projects);
                Fragment fragment = pc;
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frag, fragment).commit();
            }
        });


        return view;
    }

    public void refreshList()
    {
        listViewAdapter.notifyDataSetChanged();
    }

    public void calcularMedicion()
    {
        int minR = 0, secR = 0, minP = 0, secP = 0, minD = 0, secD = 0,
        minC = 0, secC = 0, minCm = 0, secCm = 0, minT = 0, secT = 0, minPM = 0, secPM = 0;

        String realTimeString = "", planTimeString = "", designTimeString = "",
        codingTimeString = "", compileTimeString = "", testTimeString = "", postMortemString = "";

        String T[];
        for (int i = 0; i <  projects.get(projects.size() - 1).getIterations().size(); i++)
        {
            if(!projects.get(projects.size()-1).getIterations().get(i).getValorCronos()[0].equals(""))
            {
                T = projects.get(projects.size()-1).getIterations().get(i).getValorCronos()[0].split(":");
                minP = minP + Integer.parseInt(T[0]); secP = secP + Integer.parseInt(T[1]);
            }

            if(!projects.get(projects.size()-1).getIterations().get(i).getValorCronos()[1].equals(""))
            {
                T = projects.get(projects.size()-1).getIterations().get(i).getValorCronos()[1].split(":");
                minD = minD + Integer.parseInt(T[0]); secD = secD + Integer.parseInt(T[1]);
            }

            if(!projects.get(projects.size()-1).getIterations().get(i).getValorCronos()[2].equals(""))
            {
                T = projects.get(projects.size()-1).getIterations().get(i).getValorCronos()[2].split(":");
                minC = minC + Integer.parseInt(T[0]); secC = secC + Integer.parseInt(T[1]);
            }

            if(!projects.get(projects.size()-1).getIterations().get(i).getValorCronos()[3].equals(""))
            {
                T = projects.get(projects.size()-1).getIterations().get(i).getValorCronos()[3].split(":");
                minCm = minCm + Integer.parseInt(T[0]); secCm = secCm + Integer.parseInt(T[1]);
            }

            if(!projects.get(projects.size()-1).getIterations().get(i).getValorCronos()[4].equals(""))
            {
                T = projects.get(projects.size()-1).getIterations().get(i).getValorCronos()[4].split(":");
                minT = minT + Integer.parseInt(T[0]); secT = secT + Integer.parseInt(T[1]);
            }

            if(!projects.get(projects.size()-1).getIterations().get(i).getValorCronos()[5].equals(""))
            {
                T = projects.get(projects.size()-1).getIterations().get(i).getValorCronos()[5].split(":");
                minPM = minPM + Integer.parseInt(T[0]); secPM = secPM + Integer.parseInt(T[1]);
            }
        }

        String minF = "", secF = "";
        minF = formatearMin(minP); secF = formatearSec(secP);
        planTimeString = minF + minP + ":" + secF + secP;
        minF = formatearMin(minD); secF = formatearSec(secD);
        designTimeString = minF + minD + ":" + secF + secD;
        minF = formatearMin(minC); secF = formatearSec(secC);
        codingTimeString = minF + minC + ":" + secF + secC;
        minF = formatearMin(minCm); secF = formatearSec(secCm);
        compileTimeString = minF + minCm + ":" + secF + secCm;
        minF = formatearMin(minT); secF = formatearSec(secT);
        testTimeString = minF + minT + ":" + secF + secT;
        minF = formatearMin(minPM); secF = formatearSec(secPM);
        postMortemString = minF + minPM + ":" + secF + secPM;

        minR = minP + minD + minC + minCm + minT + minPM;
        secR = secP + secD + secC + secCm + secT + secPM;
        minF = formatearMin(minR); secF = formatearSec(secR);
        realTimeString = minF + minR + ":" + secF + secR;

        projects.get(projects.size()-1).setPlanTime(String.valueOf(planTimeString));
        projects.get(projects.size()-1).setDesignTime(String.valueOf(designTimeString));
        projects.get(projects.size()-1).setCodingTime(String.valueOf(codingTimeString));
        projects.get(projects.size()-1).setCompileTime(String.valueOf(compileTimeString));
        projects.get(projects.size()-1).setTestTime(String.valueOf(testTimeString));
        projects.get(projects.size()-1).setRealTime(String.valueOf(realTimeString));
        projects.get(projects.size()-1).setPostMortemTime(String.valueOf(postMortemString));
    }

    public String formatearMin(int min)
    {
        String minF = "";

        if(min < 10)
        {
            minF = "0";
        }

        return minF;
    }

    public String formatearSec(int sec)
    {
        String secF = "";

        if(sec < 10)
        {
            secF = "0";
        }

        return secF;
    }
}
