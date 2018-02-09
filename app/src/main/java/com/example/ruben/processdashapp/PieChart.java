package com.example.ruben.processdashapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class PieChart extends Fragment {

    private ArrayList<Project> projects = new ArrayList<>();
    private int position;
    private Button volver;
    private final int[] colores = {
        Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
        Color.rgb(106, 150, 31), Color.rgb(179, 100, 53), Color.BLUE
    };

    @SuppressLint("ValidFragment")
    public PieChart(ArrayList<Project> projects, int position) {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        List<PieEntry> porcentajes = new ArrayList<>();
        float minR = 0, secR = 0, minP = 0, secP = 0, minD = 0, secD = 0,
                minC = 0, secC = 0, minCm = 0, secCm = 0, minT = 0, secT = 0, minPM = 0, secPM = 0;

        String T[];
        T = projects.get(position).getPlanTime().split(":");
        minP = minP + Integer.parseInt(T[0]); secP = secP + Integer.parseInt(T[1]);


        T = projects.get(position).getDesignTime().split(":");
        minD = minD + Integer.parseInt(T[0]); secD = secD + Integer.parseInt(T[1]);


        T = projects.get(position).getCodingTime().split(":");
        minC = minC + Integer.parseInt(T[0]); secC = secC + Integer.parseInt(T[1]);


        T = projects.get(position).getCompileTime().split(":");
        minCm = minCm + Integer.parseInt(T[0]); secCm = secCm + Integer.parseInt(T[1]);


        T = projects.get(position).getTestTime().split(":");
        minT = minT + Integer.parseInt(T[0]); secT = secT + Integer.parseInt(T[1]);


        T = projects.get(position).getPostMortemTime().split(":");
        minPM = minPM + Integer.parseInt(T[0]); secPM = secPM + Integer.parseInt(T[1]);

        minR = minP + minD + minC + minCm + minT + minPM;
        secR = secP + secD + secC + secCm + secT + secPM;

        minR = minP + minD + minC + minCm + minT + minPM;
        secR = secP + secD + secC + secCm + secT + secPM;

        float porcentajeP, porcentajeD, porcentajeC, porcentajeCm, porcentajeT, porcentajePM;
        porcentajeP = ((minP + secP) / (minR + secR)) * 100;
        porcentajeD = ((minD + secD) / (minR + secR)) * 100;
        porcentajeC = ((minC + secC) / (minR + secR)) * 100;
        porcentajeCm = ((minCm + secCm) / (minR + secR)) * 100;
        porcentajeT = ((minT + secT) / (minR + secR)) * 100;
        porcentajePM = ((minPM + secPM) / (minR + secR)) * 100;


        porcentajes.add(new PieEntry(porcentajeP, "Plan"));
        porcentajes.add(new PieEntry(porcentajeD, "Design"));
        porcentajes.add(new PieEntry(porcentajeC, "Code"));
        porcentajes.add(new PieEntry(porcentajeCm, "Compile"));
        porcentajes.add(new PieEntry(porcentajeT, "Test"));
        porcentajes.add(new PieEntry(porcentajePM, "PostMortem"));

        PieDataSet dataSet = new PieDataSet(porcentajes, "");
        dataSet.setColors(colores);
        dataSet.setSelectionShift(5f);
        dataSet.setSliceSpace(1f);
        dataSet.setValueFormatter(new MyValueFormatter());

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);

        com.github.mikephil.charting.charts.PieChart pie = view.findViewById(R.id.pie);
        pie.setUsePercentValues(true);
        pie.getDescription().setEnabled(false);
        pie.setExtraOffsets(5, 10, 5, 5);
        pie.setDragDecelerationFrictionCoef(0.95f);
        pie.setDrawHoleEnabled(false);
        pie.setHoleColor(Color.parseColor("#2cc98d"));
        pie.setTransparentCircleRadius(61f);
        pie.setData(data);
        pie.setDescription(null);
        pie.animateY(1000);
        pie.invalidate();

        volver = view.findViewById(R.id.buttonVolver);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectSelected ps = new ProjectSelected(projects, position);
                Fragment fragmentProject = ps;
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frag, fragmentProject).commit();
            }
        });


        return view;
    }
}



