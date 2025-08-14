package com.aluracursos.literalura.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class AutorResponse {

    @JsonAlias("name")
    private String nombre;

    @JsonAlias("birth_year")
    private Integer fechaNacimiento;

    @JsonAlias("death_year")
    private Integer fechaFallecimiento;

    public String getNombre() { return nombre; }
    public Integer getFechaNacimiento() { return fechaNacimiento; }
    public Integer getFechaFallecimiento() { return fechaFallecimiento; }
}
