package com.example.ruben.processdashapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class listProjects extends Fragment {


    private ListView listView;

    private ArrayList<Project> projects = new ArrayList<>();
    private ArrayAdapter<Project> listViewAdapter;
    private FloatingActionButton addProject;
    private boolean nuevo = true, click = true;

    public listProjects() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public listProjects(ArrayList<Project> projects) {

        this.projects = projects;
        nuevo = false;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_projects, container, false);
        listView = (ListView) view.findViewById(R.id.listViewProjects);

        ((MainActivity) getActivity()).setActionBarTitle("ProcessDashApp");
        if(!nuevo) {
            guardarDatos();
            nuevo = true;
        } else {
            try {
                leerDatos();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }

        listViewAdapter = new ArrayAdapter<Project>(getActivity(), android.R.layout.simple_list_item_1, projects){

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
                if (click) {
                    ProjectSelected ps = new ProjectSelected(projects, position);
                    Fragment fragment = ps;
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frag, fragment).commit();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                click = false;
                final int p = position;
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                projects.remove(p);
                                listView.setAdapter(listViewAdapter);
                                Toast.makeText(getContext(), "¡Has borrado un proyecto!", Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                        click = true;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("¿Deseas eliminar este proyecto?").setPositiveButton("Si", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                return false;
            }
        });

        addProject = view.findViewById(R.id.newProjectFloat);
        addProject.setVisibility(View.VISIBLE);
        addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new newProject(projects);
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

    public void guardarDatos()
    {

        XmlSerializer fichero = Xml.newSerializer();
        StringWriter escritura = new StringWriter();

        try {
            fichero.setOutput(escritura);
            fichero.startDocument("UTF-8", true);

            fichero.startTag("", "Proyectos");
            for(Project p: projects)
            {
                fichero.startTag("", "Proyecto");
                fichero.startTag("", "NameP");
                fichero.text(String.valueOf(p.getName()));
                fichero.endTag("", "NameP");

                fichero.startTag("", "TimeEstimatedP");
                fichero.text(String.valueOf(p.getTimeEstimated()));
                fichero.endTag("", "TimeEstimatedP");

                fichero.startTag("", "RealTimeP");
                fichero.text(String.valueOf(p.getRealTime()));
                fichero.endTag("", "RealTimeP");

                fichero.startTag("", "PlanTimeP");
                fichero.text(String.valueOf(p.getPlanTime()));
                fichero.endTag("", "PlanTimeP");

                fichero.startTag("", "DesignTimeP");
                fichero.text(String.valueOf(p.getDesignTime()));
                fichero.endTag("", "DesignTimeP");

                fichero.startTag("", "CodingTimeP");
                fichero.text(String.valueOf(p.getCodingTime()));
                fichero.endTag("", "CodingTimeP");

                fichero.startTag("", "CompileTimeP");
                fichero.text(String.valueOf(p.getCompileTime()));
                fichero.endTag("", "CompileTimeP");

                fichero.startTag("", "TestTimeP");
                fichero.text(String.valueOf(p.getTestTime()));
                fichero.endTag("", "TestTimeP");

                fichero.startTag("", "PostMortemTimeP");
                fichero.text(String.valueOf(p.getPostMortemTime()));
                fichero.endTag("", "PostMortemTimeP");


                fichero.startTag("", "Iterations");
                for (Iteration i: p.getIterations())
                {
                    fichero.startTag("", "Iteration");
                    fichero.startTag("", "Name");
                    fichero.text(String.valueOf(i.getName()));
                    fichero.endTag("", "Name");

                    fichero.startTag("", "PlanTime");
                    fichero.text(String.valueOf(i.getPlanTime()));
                    fichero.endTag("", "PlanTime");

                    fichero.startTag("", "DesignTime");
                    fichero.text(String.valueOf(i.getDesignTime()));
                    fichero.endTag("", "DesignTime");

                    fichero.startTag("", "CodingTime");
                    fichero.text(String.valueOf(i.getCodingTime()));
                    fichero.endTag("", "CodingTime");

                    fichero.startTag("", "CompileTime");
                    fichero.text(String.valueOf(i.getCompileTime()));
                    fichero.endTag("", "CompileTime");

                    fichero.startTag("", "TestTime");
                    fichero.text(String.valueOf(i.getTestTime()));
                    fichero.endTag("", "TestTime");

                    fichero.startTag("", "PostMortemTime");
                    fichero.text(String.valueOf(i.getPostMortemTime()));
                    fichero.endTag("", "PostMortemTime");

                    fichero.startTag("", "ListaProcesos");
                    for (String pr: i.getProcesos())
                    {
                        fichero.startTag("", "Proceso");
                        fichero.text(String.valueOf(pr));
                        fichero.endTag("", "Proceso");
                    }
                    fichero.endTag("", "ListaProcesos");

                    fichero.startTag("", "ListaProcesosOut");
                    for (String prOut: i.getProcesosOut())
                    {
                        fichero.startTag("", "ProcesoOut");
                        fichero.text(String.valueOf(prOut));
                        fichero.endTag("", "ProcesoOut");
                    }
                    fichero.endTag("", "ListaProcesosOut");

                    fichero.startTag("", "ValoresCronos");
                    for (String cronos: i.getValorCronos())
                    {
                        fichero.startTag("", "ValorCrono");
                        fichero.text(String.valueOf(cronos));
                        fichero.endTag("", "ValorCrono");
                    }
                    fichero.endTag("", "ValoresCronos");

                    fichero.startTag("", "ListaErrores");
                    for (int err = 0; err < i.getListaErrores().length; err++)
                    {
                        if(!i.getListaErrores()[err].isEmpty()) {
                            fichero.startTag("", "Fase");
                            fichero.text(String.valueOf(err));
                            fichero.endTag("", "Fase");
                            fichero.startTag("", "Error_Fase_" + err);
                            for (int err2 = 0; err2 < i.getListaErrores()[err].size(); err2++) {
                                if (i.getListaErrores()[err].get(err2).getTipo() != null) {
                                    fichero.startTag("", "Error_" + err2);
                                    fichero.startTag("", "Tipo");
                                    fichero.text(i.getListaErrores()[err].get(err2).getTipo());
                                    fichero.endTag("", "Tipo");

                                    fichero.startTag("", "Fecha");
                                    fichero.text(i.getListaErrores()[err].get(err2).getFecha());
                                    fichero.endTag("", "Fecha");

                                    fichero.startTag("", "ErrorTime");
                                    fichero.text(i.getListaErrores()[err].get(err2).getErrorTime());
                                    fichero.endTag("", "ErrorTime");

                                    fichero.startTag("", "Descripcion");
                                    fichero.text(i.getListaErrores()[err].get(err2).getDescripcion());
                                    fichero.endTag("", "Descripcion");
                                    fichero.endTag("", "Error_" + err2);
                                }
                            }
                            fichero.endTag("", "Error_Fase_" + err);
                        }
                    }
                    fichero.endTag("", "ListaErrores");
                    fichero.endTag("", "Iteration");
                }
                fichero.endTag("", "Iterations");
                fichero.endTag("", "Proyecto");

            }
            fichero.endTag("", "Proyectos");

            fichero.endDocument();

            String resultado = escritura.toString();
            FileOutputStream fos = getContext().openFileOutput("Datos.xml", Context.MODE_PRIVATE);
            fos.write(resultado.getBytes(), 0, resultado.length());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "¡Error al guardar el archivo!", Toast.LENGTH_SHORT).show();
        }
    }

    public void leerDatos() throws XmlPullParserException {
        XmlPullParserFactory fabrica = XmlPullParserFactory.newInstance();
        fabrica.setNamespaceAware(true);
        FileInputStream fis = null;
        try {
            StringBuilder sb = new StringBuilder();
            fabrica.setNamespaceAware(true);
            XmlPullParser parser = fabrica.newPullParser();
            fis = getContext().openFileInput("Datos.xml");
            parser.setInput(fis, null);

            int tipoEvento = parser.getEventType();

            Project p = new Project();
            Iteration i = new Iteration();
            ArrayList<Iteration> listIterations = new ArrayList<>();
            ArrayList<String> procesos = new ArrayList<>();
            ArrayList<String> procesosOut = new ArrayList<>();
            String[] valorCronos = {"","","","","", ""};
            Error error = new Error();
            ArrayList<Error>[] listaErrores = new ArrayList[6];
            int posFase = -1;

            for (int k = 0; k < listaErrores.length; k++)
            {
                listaErrores[k] = new ArrayList<>();
            }

            int contadorCronos = 0, contadorErrores = 0;

            while (tipoEvento != XmlPullParser.END_DOCUMENT) {
                tipoEvento = parser.next();
                String tag = parser.getName();
                switch (tipoEvento) {
                    case XmlPullParser.START_TAG:
                        if (tag.equalsIgnoreCase("NameP")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                p.setName(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("TimeEstimatedP")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                p.setTimeEstimated(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("RealTimeP")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                p.setRealTime(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("PlanTimeP")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                p.setPlanTime(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("DesignTimeP")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                p.setDesignTime(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("CodingTimeP")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                p.setCodingTime(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("CompileTimeP")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                p.setCompileTime(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("TestTimeP")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                p.setTestTime(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("PostMortemTimeP")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                p.setPostMortemTime(parser.getText());
                            }
                        }

                        /* ITERATIONS */

                        if (tag.equalsIgnoreCase("Name")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                i.setName(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("PlanTime")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                i.setPlanTime(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("DesignTime")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                i.setDesignTime(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("CodingTime")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                i.setCodingTime(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("CompileTime")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                i.setCompileTime(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("TestTime")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                i.setTestTime(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("PostMortemTime")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                i.setPostMortemTime(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("ProcesoOut")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                procesosOut.add(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("ValorCrono")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                valorCronos[contadorCronos++] = parser.getText();
                            }
                        }


                        if (tag.equalsIgnoreCase("Fase")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                posFase = Integer.parseInt(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("Tipo")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                error.setTipo(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("Fecha")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                error.setFecha(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("ErrorTime")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                error.setErrorTime(parser.getText());
                            }
                        }

                        if (tag.equalsIgnoreCase("Descripcion")) {
                            tipoEvento = parser.next();
                            if (tipoEvento == XmlPullParser.TEXT) {
                                error.setDescripcion(parser.getText());
                                listaErrores[posFase].add(error);
                                error = new Error();
                            }
                        }

                        break;

                    case XmlPullParser.END_TAG:

                        if (tag.equalsIgnoreCase("ListaProcesos")) {
                            i.setProcesos(procesos);
                            procesos = new ArrayList<>();
                        }

                        if (tag.equalsIgnoreCase("ListaProcesosOut")) {
                            i.setProcesosOut(procesosOut);
                            procesosOut = new ArrayList<>();
                        }

                        if (tag.equalsIgnoreCase("ValoresCronos")) {
                            i.setValorCronos(valorCronos);
                        }

                        if(tag.equalsIgnoreCase("ListaErrores")) {
                            i.setListaErrores(listaErrores);
                            listaErrores = new ArrayList[6];
                            for (int k = 0; k < listaErrores.length; k++)
                            {
                                listaErrores[k] = new ArrayList<>();
                            }
                        }

                        if (tag.equalsIgnoreCase("Iteration")) {
                            listIterations.add(i);
                            i = new Iteration();
                        }

                        if (tag.equalsIgnoreCase("Iterations")) {
                            p.setIterations(listIterations);
                            listIterations = new ArrayList<>();
                        }

                        if (tag.equalsIgnoreCase("Proyecto")) {
                            projects.add(p);
                            p = new Project();
                        }
                        break;

                    default:
                        break;
                }
            }
    } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            if (fis != null)
            {
                try
                {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
