package com.example.ruben.processdashapp;

import java.util.ArrayList;

/**
 * Created by Ruben on 10/12/17.
 */

public class Project {
    private String name = "";
    private String timeEstimated = "";
    private String realTime = "";
    private String planTime = "";
    private String designTime = "";
    private String codingTime = "";
    private String compileTime = "";
    private String testTime = "";
    private String postMortemTime = "";
    private ArrayList<Iteration> iterations = new ArrayList<>();

    public Project(String nameProject, String timeEstimated, ArrayList<Iteration> iterations) {
        this.name = nameProject;
        this.timeEstimated = timeEstimated;
        this.iterations = iterations;
    }

    public Project() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeEstimated() {
        return timeEstimated;
    }

    public void setTimeEstimated(String timeEstimated) {
        this.timeEstimated = timeEstimated;
    }

    public String getRealTime() {
        return realTime;
    }

    public void setRealTime(String realTime) {
        this.realTime = realTime;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public String getDesignTime() {
        return designTime;
    }

    public void setDesignTime(String designTime) {
        this.designTime = designTime;
    }

    public String getCodingTime() {
        return codingTime;
    }

    public void setCodingTime(String codingTime) {
        this.codingTime = codingTime;
    }

    public String getCompileTime() {
        return compileTime;
    }

    public void setCompileTime(String compileTime) {
        this.compileTime = compileTime;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

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

    public ArrayList<Iteration> getIterations() {
        return iterations;
    }

    public void setIterations(ArrayList<Iteration> iterations) {
        this.iterations = iterations;
    }
}
