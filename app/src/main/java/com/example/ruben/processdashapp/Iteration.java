package com.example.ruben.processdashapp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Iteration {
    private String name;
    private String planTime;
    private String designTime;
    private String codingTime;
    private String compileTime;
    private String testTime;
    private String postMortemTime;
    private ArrayList<String> procesos = new ArrayList<>();
    private ArrayList<String> procesosOut = new ArrayList<>();
    private String[] valorCronos = {};
    private ArrayList<Error>[] listaErrores = new ArrayList[6];

    private boolean creado = false;

    public Iteration() {}

    public Iteration(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanTime() { return planTime; }

    public void setPlanTime(String planTime) { this.planTime = planTime; }

    public String getDesignTime() { return designTime; }

    public void setDesignTime(String designTime) { this.designTime = designTime; }

    public String getCodingTime() { return codingTime; }

    public void setCodingTime(String codingTime) { this.codingTime = codingTime; }

    public String getCompileTime() { return compileTime; }

    public void setCompileTime(String compileTime) { this.compileTime = compileTime; }

    public String getTestTime() { return testTime; }

    public void setTestTime(String testTime) { this.testTime = testTime; }

    public String getPostMortemTime() {
        return postMortemTime;
    }

    public void setPostMortemTime(String postMortemTime) {
        this.postMortemTime = postMortemTime;
    }

    public String toString()
    {
        return this.name;
    }

    public boolean getCreado() { return creado; }

    public void setCreado(boolean creado) { this.creado = creado; }

    public ArrayList<String> getProcesos() {
        return procesos;
    }

    public void setProcesos(ArrayList<String> procesos) {
        this.procesos = procesos;
    }

    public ArrayList<String> getProcesosOut() {
        return procesosOut;
    }

    public void setProcesosOut(ArrayList<String> procesosOut) {
        this.procesosOut = procesosOut;
    }

    public String[] getValorCronos() {
        return valorCronos;
    }

    public void setValorCronos(String[] valorCronos) {
        this.valorCronos = valorCronos;
    }

    public ArrayList<Error>[] getListaErrores() {
        return listaErrores;
    }

    public void setListaErrores(ArrayList<Error>[] listaErrores) {
        this.listaErrores = listaErrores;
    }
}
