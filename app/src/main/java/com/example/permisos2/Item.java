package com.example.permisos2;

public class Item {

    String descripcion;
    String nombreEmpleado;
    String apellidosEmpleado;
    String Nlista;
    String Fpermiso;

    String fechaRegistro;

    String observaciones;

    public Item(String descripcion, String nombreEmpleado, String apellidosEmpleado, String Nlista, String Fpermiso, String fechaRegistro, String observaciones) {
        this.descripcion = descripcion;
        this.nombreEmpleado = nombreEmpleado;
        this.apellidosEmpleado = apellidosEmpleado;
        this.Nlista = Nlista;
        this.Fpermiso = Fpermiso;
        this.fechaRegistro = fechaRegistro;
        this.observaciones = observaciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getApellidosEmpleado() {
        return apellidosEmpleado;
    }

    public void setApellidosEmpleado(String apellidosEmpleado) {
        this.apellidosEmpleado = apellidosEmpleado;
    }

    public String getNlista() {
        return Nlista;
    }

    public void setNlista(String Nlista) {
        this.Nlista = Nlista;
    }

    public String getFpermiso() {
        return Fpermiso;
    }

    public void setFpermiso(String Fpermiso) {
        this.Fpermiso = Fpermiso;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
