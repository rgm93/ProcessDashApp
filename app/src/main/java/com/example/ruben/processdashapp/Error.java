package com.example.ruben.processdashapp;

public class Error
{
    private String tipo;
    private String descripcion;

    private String fecha;
    private String errorTime;

    public Error() {}
    public Error(String tipo, String descripcion, String fecha, String errorTime) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.errorTime = errorTime;
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getErrorTime() { return errorTime; }
    public void setErrorTime(String errorTime) { this.errorTime = errorTime; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
