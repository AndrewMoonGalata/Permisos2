package com.example.permisos2;

public class Employee {

    private String nombre;

    private String apellidos;
    private String nlista;
    private String empresa;
    private String departamento;
    private String puesto;
    private String turno;
    private String jefe;

    public Employee(String nombre, String apellidos, String nlista, String empresa, String departamento, String puesto, String turno, String jefe) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nlista = nlista;
        this.empresa = empresa;
        this.departamento = departamento;
        this.puesto = puesto;
        this.turno = turno;
        this.jefe = jefe;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() { return apellidos; }

    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getNlista() {
        return nlista;
    }

    public void setNlista(String nlista) {
        this.nlista = nlista;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getJefe() {
        return jefe;
    }

    public void setJefe(String jefe) {
        this.jefe = jefe;
    }
}
