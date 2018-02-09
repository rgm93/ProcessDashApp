package com.example.ruben.processdashapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProcessCounter extends Fragment
{
    private ArrayList<Project> projects;
    private ArrayAdapter<String> listProcesos;
    private ArrayList<String> procesosList = new ArrayList<>();
    private ArrayList<String> procesosListOut = new ArrayList<>();
    private ArrayList<Boolean> creadoFase = new ArrayList<>();
    private Chronometer cronometro, cronometro2, cronometro3, cronometro4, cronometro5, cronometro6;
    private Button start, stop, finish, error;
    private String[] procesos = {};
    private Spinner spinner;
    private boolean pulsado, cronometrando;
    private int pos, posReal, fasePos = 0;
    private String faseNm;
    private ArrayList<Long> tiempoPausa = new ArrayList<>();
    private String[] currentTime = {"","","","","", ""};
    private String[] valorCronos = {"","","","","", ""};
    private ArrayList<Error>[] listaErrores = new ArrayList[6];

    /* Dialog */

    private EditText descripcion;
    private Chronometer cronometroDialog;
    private Spinner tipo;
    private EditText fecha;
    private ImageButton inicioArreglo, pausaArreglo;
    private long tiempoPausaDialog = 0L;

    public ProcessCounter() {}

    @SuppressLint("ValidFragment")
    public ProcessCounter(ArrayList<Project> projects, int pos)
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_process_counter, container, false);
        procesosList = projects.get(projects.size()-1).getIterations().get(pos).getProcesos();
        procesos = procesosList.toArray(new String[0]);

        spinner = view.findViewById(R.id.spinner);

        listProcesos = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, procesos);
        listProcesos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(listProcesos);
        spinner.setSelection(fasePos);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                faseNm = spinner.getSelectedItem().toString();

                if(!cronometrando)
                {
                    if (pulsado) {
                        pulsado = false;
                    } else {
                        switch(faseNm)
                        {
                            case "Planning":
                                cronometro.setVisibility(View.VISIBLE);
                                cronometro2.setVisibility(View.INVISIBLE);
                                cronometro3.setVisibility(View.INVISIBLE);
                                cronometro4.setVisibility(View.INVISIBLE);
                                cronometro5.setVisibility(View.INVISIBLE);
                                cronometro6.setVisibility(View.INVISIBLE);
                                fasePos = 0;
                                break;
                            case "Design":
                                cronometro2.setVisibility(View.VISIBLE);
                                cronometro.setVisibility(View.INVISIBLE);
                                cronometro3.setVisibility(View.INVISIBLE);
                                cronometro4.setVisibility(View.INVISIBLE);
                                cronometro5.setVisibility(View.INVISIBLE);
                                cronometro6.setVisibility(View.INVISIBLE);
                                fasePos = 1;
                                break;
                            case "Code":
                                cronometro3.setVisibility(View.VISIBLE);
                                cronometro.setVisibility(View.INVISIBLE);
                                cronometro2.setVisibility(View.INVISIBLE);
                                cronometro4.setVisibility(View.INVISIBLE);
                                cronometro5.setVisibility(View.INVISIBLE);
                                cronometro6.setVisibility(View.INVISIBLE);
                                fasePos = 2;
                                break;
                            case "Compile":
                                cronometro4.setVisibility(View.VISIBLE);
                                cronometro.setVisibility(View.INVISIBLE);
                                cronometro2.setVisibility(View.INVISIBLE);
                                cronometro3.setVisibility(View.INVISIBLE);
                                cronometro5.setVisibility(View.INVISIBLE);
                                cronometro6.setVisibility(View.INVISIBLE);
                                fasePos = 3;
                                break;
                            case "Test":
                                cronometro5.setVisibility(View.VISIBLE);
                                cronometro.setVisibility(View.INVISIBLE);
                                cronometro2.setVisibility(View.INVISIBLE);
                                cronometro3.setVisibility(View.INVISIBLE);
                                cronometro4.setVisibility(View.INVISIBLE);
                                cronometro6.setVisibility(View.INVISIBLE);
                                fasePos = 4;
                                break;
                            case "PostMortem":
                                cronometro6.setVisibility(View.VISIBLE);
                                cronometro.setVisibility(View.INVISIBLE);
                                cronometro2.setVisibility(View.INVISIBLE);
                                cronometro3.setVisibility(View.INVISIBLE);
                                cronometro4.setVisibility(View.INVISIBLE);
                                cronometro5.setVisibility(View.INVISIBLE);
                                fasePos = 5;
                                break;
                        }
                        cronometrando = false;
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "El cronómetro sigue en funcionamiento.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cronometro = view.findViewById(R.id.cronometro); cronometro.setFormat("%s");
        cronometro2 = view.findViewById(R.id.cronometro2); cronometro2.setFormat("%s");
        cronometro3 = view.findViewById(R.id.cronometro3); cronometro3.setFormat("%s");
        cronometro4 = view.findViewById(R.id.cronometro4); cronometro4.setFormat("%s");
        cronometro5 = view.findViewById(R.id.cronometro5); cronometro5.setFormat("%s");
        cronometro6 = view.findViewById(R.id.cronometro6); cronometro6.setFormat("%s");

        cronometro.setVisibility(View.INVISIBLE);
        cronometro2.setVisibility(View.INVISIBLE);
        cronometro3.setVisibility(View.INVISIBLE);
        cronometro4.setVisibility(View.INVISIBLE);
        cronometro5.setVisibility(View.INVISIBLE);
        cronometro6.setVisibility(View.INVISIBLE);
        cronometrando = false;

        for (int i = 0; i < procesosList.size(); i++)
        {
            creadoFase.add(false);
        }

        for (int j = 0; j < procesosList.size(); j++)
        {
            tiempoPausa.add(0L);
        }

        for (int k = 0; k < listaErrores.length; k++)
        {
            listaErrores[k] = new ArrayList<>();
        }

        start = view.findViewById(R.id.buttonStart);
        stop = view.findViewById(R.id.buttonStop);
        finish = view.findViewById(R.id.buttonFinish);
        error = view.findViewById(R.id.buttonError);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fasePos = spinner.getSelectedItemPosition();
                faseNm = spinner.getSelectedItem().toString();
                switch(faseNm) {
                    case "Planning":
                        if (!creadoFase.get(posReal)) {
                            cronometro.setBase(SystemClock.elapsedRealtime() + tiempoPausa.get(fasePos));
                        }
                        cronometro.setBase(SystemClock.elapsedRealtime() + tiempoPausa.get(fasePos));
                        cronometro.start();
                        cronometrando = true;
                        break;
                    case "Design":
                        if (!creadoFase.get(fasePos)) {
                            cronometro2.setBase(SystemClock.elapsedRealtime() + tiempoPausa.get(fasePos));
                        }
                        cronometro2.setBase(SystemClock.elapsedRealtime() + tiempoPausa.get(fasePos));
                        cronometro2.start();
                        cronometrando = true;
                        break;
                    case "Code":
                        if (!creadoFase.get(fasePos)) {
                            cronometro3.setBase(SystemClock.elapsedRealtime() + tiempoPausa.get(fasePos));
                        }
                        cronometro3.setBase(SystemClock.elapsedRealtime() + tiempoPausa.get(fasePos));
                        cronometro3.start();
                        cronometrando = true;
                        break;
                    case "Compile":
                        if (!creadoFase.get(fasePos)) {
                            cronometro4.setBase(SystemClock.elapsedRealtime() + tiempoPausa.get(fasePos));
                        }
                        cronometro4.setBase(SystemClock.elapsedRealtime() + tiempoPausa.get(fasePos));
                        cronometro4.start();
                        cronometrando = true;
                        break;
                    case "Test":
                        if (!creadoFase.get(fasePos)) {
                            cronometro5.setBase(SystemClock.elapsedRealtime() + tiempoPausa.get(fasePos));
                        }
                        cronometro5.setBase(SystemClock.elapsedRealtime() + tiempoPausa.get(fasePos));
                        cronometro5.start();
                        cronometrando = true;
                        break;
                    case "PostMortem":
                        if (!creadoFase.get(fasePos)) {
                            cronometro6.setBase(SystemClock.elapsedRealtime() + tiempoPausa.get(fasePos));
                        }
                        cronometro6.setBase(SystemClock.elapsedRealtime() + tiempoPausa.get(fasePos));
                        cronometro6.start();
                        cronometrando = true;
                        break;
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tiempo = "";
                fasePos = spinner.getSelectedItemPosition();
                faseNm = spinner.getSelectedItem().toString();
                switch(faseNm)
                {
                    case "Planning":
                        tiempoPausa.set(fasePos, cronometro.getBase() - SystemClock.elapsedRealtime());
                        cronometro.stop();

                        if(!creadoFase.get(fasePos))
                        {
                            creadoFase.set(fasePos, true);
                        }

                        tiempo = (String) cronometro.getText();
                        currentTime[0] = tiempo;
                        break;
                    case "Design":
                        tiempoPausa.set(fasePos, cronometro2.getBase() - SystemClock.elapsedRealtime());
                        cronometro2.stop();

                        if(!creadoFase.get(fasePos))
                        {
                            creadoFase.set(fasePos, true);
                        }

                        tiempo = (String) cronometro2.getText();
                        currentTime[1] = tiempo;
                        break;
                    case "Code":
                        tiempoPausa.set(fasePos, cronometro3.getBase() - SystemClock.elapsedRealtime());
                        cronometro3.stop();

                        if(!creadoFase.get(fasePos))
                        {
                            creadoFase.set(fasePos, true);
                        }

                        tiempo = (String) cronometro3.getText();
                        currentTime[2] = tiempo;
                        break;
                    case "Compile":
                        tiempoPausa.set(fasePos, cronometro4.getBase() - SystemClock.elapsedRealtime());
                        cronometro4.stop();

                        if(!creadoFase.get(fasePos))
                        {
                            creadoFase.set(fasePos, true);
                        }

                        tiempo = (String) cronometro4.getText();
                        currentTime[3] = tiempo;
                        break;
                    case "Test":
                        tiempoPausa.set(fasePos, cronometro5.getBase() - SystemClock.elapsedRealtime());
                        cronometro5.stop();

                        if(!creadoFase.get(fasePos))
                        {
                            creadoFase.set(fasePos, true);
                        }

                        tiempo = (String) cronometro5.getText();
                        currentTime[4] = tiempo;
                        break;
                    case "PostMortem":
                        tiempoPausa.set(fasePos, cronometro6.getBase() - SystemClock.elapsedRealtime());
                        cronometro6.stop();

                        if(!creadoFase.get(fasePos))
                        {
                            creadoFase.set(fasePos, true);
                        }

                        tiempo = (String) cronometro6.getText();
                        currentTime[5] = tiempo;
                        break;
                }
                cronometrando = false;
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cronometrando)
                {
                    Toast.makeText(getContext(), "El cronómetro sigue en funcionamiento.", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    fasePos = spinner.getSelectedItemPosition();
                    faseNm = spinner.getSelectedItem().toString();
                    switch(faseNm)
                    {
                        case "Planning":
                            cronometro.setBase(SystemClock.elapsedRealtime());
                            cronometro.setVisibility(View.INVISIBLE);
                            valorCronos[0] = currentTime[0];
                            currentTime[0] = "";
                            break;
                        case "Design":
                            cronometro2.setBase(SystemClock.elapsedRealtime());
                            cronometro2.setVisibility(View.INVISIBLE);
                            valorCronos[1] = currentTime[1];
                            currentTime[1] = "";
                            break;
                        case "Code":
                            cronometro3.setBase(SystemClock.elapsedRealtime());
                            cronometro3.setVisibility(View.INVISIBLE);
                            valorCronos[2] = currentTime[2];
                            currentTime[2] = "";
                            break;
                        case "Compile":
                            cronometro4.setBase(SystemClock.elapsedRealtime());
                            cronometro4.setVisibility(View.INVISIBLE);
                            valorCronos[3] = currentTime[3];
                            currentTime[3] = "";
                            break;
                        case "Test":
                            cronometro5.setBase(SystemClock.elapsedRealtime());
                            cronometro5.setVisibility(View.INVISIBLE);
                            valorCronos[4] = currentTime[4];
                            currentTime[4] = "";
                            break;
                        case "PostMortem":
                            cronometro6.setBase(SystemClock.elapsedRealtime());
                            cronometro6.setVisibility(View.INVISIBLE);
                            valorCronos[5] = currentTime[5];
                            currentTime[5] = "";
                            break;
                    }

                    tiempoPausa.set(fasePos, 0L);
                    procesosListOut.add(spinner.getSelectedItem().toString());
                    procesosList.remove(fasePos);
                    creadoFase.remove(fasePos);
                    tiempoPausa.remove(fasePos);
                    procesos = procesosList.toArray(new String[0]);
                    listProcesos = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, procesos);
                    listProcesos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(listProcesos);

                    if (procesosList.isEmpty()) {
                        projects.get(projects.size()-1).getIterations().get(pos).setProcesosOut(procesosListOut);
                        projects.get(projects.size()-1).getIterations().get(pos).setValorCronos(valorCronos);
                        projects.get(projects.size()-1).getIterations().get(pos).setListaErrores(listaErrores);
                        listIterations li = new listIterations(projects, pos);
                        Fragment fragment = li;
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frag, fragment).commit();
                    }
                }
            }
        });

        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*faseNm = spinner.getSelectedItem().toString();
                Errors err = new Errors(projects, faseNm, coleccionErrores, fasePos, tiempoPausa.get(fasePos));
                Fragment fragment = err;
                FragmentManager fm = getFragmentManager();
                //fm.beginTransaction().replace(R.id.content_frag, fragment).commit();
                err.show(fm, "fragment_errors");*/

                AlertDialog.Builder ventanaEmergente = new AlertDialog.Builder(getActivity());
                final View vistaEmergente = (LayoutInflater.from(getActivity())).inflate(R.layout.fragment_errors, null);
                tipo = vistaEmergente.findViewById(R.id.spinnerDialog);
                descripcion = vistaEmergente.findViewById(R.id.descriptionContent);
                descripcion.setImeOptions(EditorInfo.IME_ACTION_DONE);
                fecha = vistaEmergente.findViewById(R.id.dateContent);
                fecha.setImeOptions(EditorInfo.IME_ACTION_DONE);
                cronometroDialog = vistaEmergente.findViewById(R.id.cronometroErr);
                cronometroDialog.setFormat("%s");

                inicioArreglo = vistaEmergente.findViewById(R.id.buttonStartDialog);
                pausaArreglo = vistaEmergente.findViewById(R.id.buttonStopDialog);

                inicioArreglo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    cronometroDialog.setBase(SystemClock.elapsedRealtime() + tiempoPausaDialog);
                    cronometroDialog.start();
                    cronometrando = true;
                    }
                });

                pausaArreglo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    tiempoPausaDialog = cronometroDialog.getBase() - SystemClock.elapsedRealtime();
                    cronometroDialog.stop();
                    cronometrando = false;
                    }
                });

                ventanaEmergente.setMessage(" <Error en la fase '" + faseNm + "'> ")
                    .setView(vistaEmergente)
                    .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!tipo.getSelectedItem().toString().equals("")) {
                                if (!descripcion.getText().toString().equals("")) {
                                    if (!fecha.getText().toString().equals("")) {
                                        if (!cronometroDialog.getText().equals("00:00") && !cronometrando) {
                                            int pos = -1;
                                            switch (faseNm) {
                                                case "Planning":
                                                    pos = 0;
                                                    break;
                                                case "Design":
                                                    pos = 1;
                                                    break;
                                                case "Code":
                                                    pos = 2;
                                                    break;
                                                case "Compile":
                                                    pos = 3;
                                                    break;
                                                case "Test":
                                                    pos = 4;
                                                    break;
                                                case "PostMortem":
                                                    pos = 5;
                                                    break;
                                            }
                                            Error err = new Error(tipo.getSelectedItem().toString(), descripcion.getText().toString(), fecha.getText().toString(), cronometroDialog.getText().toString());
                                            listaErrores[pos].add(err);
                                            cronometroDialog.setBase(SystemClock.elapsedRealtime());
                                            tiempoPausaDialog = 0L;
                                            Toast.makeText(getActivity(), "¡Campos guardados!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "El cronómetro debe tener algo de tiempo", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "No hay una fecha detallada del error", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "No hay una descripción del error comentado", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "No hay un tipo de error seleccionado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .setCancelable(false);
                AlertDialog dialog = ventanaEmergente.create();
                dialog.show();
            }
        });

        return view;
    }
}
