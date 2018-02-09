package com.example.ruben.processdashapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ProjectSelected extends Fragment {

    private ArrayList<Project> projects = new ArrayList<>();
    private int position;
    private Button botonPie;

    private LinkedHashMap<String, GroupInfo> subjects = new LinkedHashMap<>();
    private ArrayList<GroupInfo> deptList = new ArrayList<>();

    private CustomAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;

    public ProjectSelected() {}

    @SuppressLint("ValidFragment")
    public ProjectSelected(ArrayList<Project> projects, int position) {
        this.projects = projects;
        this.position = position;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_selected, container, false);

        TextView tTimeEstimated = view.findViewById(R.id.timeEstimated);
        TextView tTimeReal = view.findViewById(R.id.timeReal);
        TextView tPlanning = view.findViewById(R.id.planningTime);
        TextView tDesign = view.findViewById(R.id.designTime);
        TextView tCode = view.findViewById(R.id.codeTime);
        TextView tCompile = view.findViewById(R.id.compileTime);
        TextView tTest = view.findViewById(R.id.testTime);
        TextView tPostMort = view.findViewById(R.id.postmortemTime);

        ((MainActivity) getActivity()).setActionBarTitle(projects.get(position).getName());
        tTimeEstimated.setText(projects.get(position).getTimeEstimated());
        tTimeReal.setText(projects.get(position).getRealTime());
        tPlanning.setText(projects.get(position).getPlanTime());
        tDesign.setText(projects.get(position).getDesignTime());
        tCode.setText(projects.get(position).getCodingTime());
        tCompile.setText(projects.get(position).getCompileTime());
        tTest.setText(projects.get(position).getTestTime());
        tPostMort.setText(projects.get(position).getPostMortemTime());

        botonPie = view.findViewById(R.id.buttonPie);
        botonPie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    PieChart pc = new PieChart(projects, position);
                    Fragment fragmentPie = pc;
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frag, fragmentPie).commit();

            }
        });

        // add data for displaying in expandable list view
        loadData();

        //get reference of the ExpandableListView
        simpleExpandableListView = (ExpandableListView) view.findViewById(R.id.listaExpansibleErrores);
        // create the adapter by passing your ArrayList data
        listAdapter = new CustomAdapter(getContext(), deptList);
        // attach the adapter to the expandable list view
        simpleExpandableListView.setAdapter(listAdapter);

        //expand all the Groups
        //expandAll();

        // setOnChildClickListener listener for child row click
        simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //get the group header
                GroupInfo headerInfo = deptList.get(groupPosition);
                //get the child info
                ChildInfo detailInfo =  headerInfo.getProductList().get(childPosition);

                AlertDialog.Builder ventanaEmergente = new AlertDialog.Builder(getActivity());
                final View vistaEmergente = (LayoutInflater.from(getActivity())).inflate(R.layout.fragment_errors_view, null);

                boolean encontrado = false;
                int i = 0, j = 0, k = 0;
                String tipoS = "", descS = "", fechaS = "", cronoS = "";
                while (i < projects.get(position).getIterations().size() && !encontrado)
                {
                    while (j < projects.get(position).getIterations().get(i).getListaErrores().length && !encontrado)
                    {
                        while (k < projects.get(position).getIterations().get(i).getListaErrores()[j].size() && !encontrado)
                        {
                            Error err = projects.get(position).getIterations().get(i).getListaErrores()[j].get(k);
                            if(detailInfo.getName().equals(err.getTipo()))
                            {
                                tipoS = err.getTipo();
                                descS = err.getDescripcion();
                                fechaS = err.getFecha();
                                cronoS = err.getErrorTime();
                                encontrado = true;
                            }
                            else
                            {
                                k++;
                            }
                        }
                        j++;
                        k = 0;
                    }
                    i++;
                    j = 0;
                }

                TextView tipo = vistaEmergente.findViewById(R.id.type2);
                TextView descripcion = vistaEmergente.findViewById(R.id.description2);
                TextView fecha = vistaEmergente.findViewById(R.id.date2);
                TextView cronometro = vistaEmergente.findViewById(R.id.errTime2);

                tipo.setText(tipoS); descripcion.setText(descS);
                fecha.setText(fechaS); cronometro.setText(cronoS);

                ventanaEmergente.setMessage(" <Error en la fase '" + headerInfo.getName() + "'> ")
                        .setView(vistaEmergente)
                        .setPositiveButton("Ok", null)
                        //.setNegativeButton("Cancelar", null)
                        .setCancelable(false);
                AlertDialog dialog = ventanaEmergente.create();
                dialog.show();
                return false;
            }
        });
        // setOnGroupClickListener listener for group heading click
        simpleExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //get the group header
                GroupInfo headerInfo = deptList.get(groupPosition);

                return false;
            }
        });

        return view;
    }



    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.collapseGroup(i);
        }
    }

    //load some initial data into out list
    private void loadData(){

        for (int i = 0; i < projects.get(position).getIterations().size(); i++)
        {
            for (int j = 0; j < projects.get(position).getIterations().get(i).getListaErrores().length; j++)
            {
                String m = "";
                switch(j)
                {
                    case 0:
                        for (int k = 0; k < projects.get(position).getIterations().get(i).getListaErrores()[j].size(); k++) {
                            addProduct("Planning", projects.get(position).getIterations().get(i).getListaErrores()[j].get(k).getTipo());
                        }
                        break;
                    case 1:
                        for (int k = 0; k < projects.get(position).getIterations().get(i).getListaErrores()[j].size(); k++) {
                            addProduct("Design", projects.get(position).getIterations().get(i).getListaErrores()[j].get(k).getTipo());
                        }
                        break;
                    case 2:
                        for (int k = 0; k < projects.get(position).getIterations().get(i).getListaErrores()[j].size(); k++) {
                            addProduct("Code", projects.get(position).getIterations().get(i).getListaErrores()[j].get(k).getTipo());
                        }
                        break;
                    case 3:
                        for (int k = 0; k < projects.get(position).getIterations().get(i).getListaErrores()[j].size(); k++) {
                            addProduct("Compile", projects.get(position).getIterations().get(i).getListaErrores()[j].get(k).getTipo());
                        }
                        break;
                    case 4:
                        for (int k = 0; k < projects.get(position).getIterations().get(i).getListaErrores()[j].size(); k++) {
                            addProduct("Test", projects.get(position).getIterations().get(i).getListaErrores()[j].get(k).getTipo());
                        }
                        break;
                    case 5:
                        for (int k = 0; k < projects.get(position).getIterations().get(i).getListaErrores()[j].size(); k++) {
                            addProduct("PostMortem", projects.get(position).getIterations().get(i).getListaErrores()[j].get(k).getTipo());
                        }
                        break;

                }
            }
        }
        System.out.println("");
    }



    //here we maintain our products in various departments
    private int addProduct(String department, String product){

        int groupPosition = 0;

        //check the hash map if the group already exists
        GroupInfo headerInfo = subjects.get(department);
        //add the group if doesn't exists
        if(headerInfo == null){
            headerInfo = new GroupInfo();
            headerInfo.setName(department);
            subjects.put(department, headerInfo);
            deptList.add(headerInfo);
        }

        //get the children for the group
        ArrayList<ChildInfo> productList = headerInfo.getProductList();
        //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        ChildInfo detailInfo = new ChildInfo();
        detailInfo.setSequence(String.valueOf(listSize));
        detailInfo.setName(product);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        //find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }
}
