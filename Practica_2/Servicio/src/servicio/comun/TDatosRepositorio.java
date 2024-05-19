/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio.comun;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Usuario
 */
public class TDatosRepositorio implements Serializable{
    
    private int numeroLibros;
    private String nombreRepositorio;
    private String direccionRepositorio;
    private RepositorioLibro repositorioLibro;

    public TDatosRepositorio() {
    }

    public TDatosRepositorio(int numeroLibros, String nombreRepositorio, String direccionRepositorio, RepositorioLibro repositorioLibro) {
        this.numeroLibros = numeroLibros;
        this.nombreRepositorio = nombreRepositorio;
        this.direccionRepositorio = direccionRepositorio;
        this.repositorioLibro = repositorioLibro;
    }

    public int getNumeroLibros() {
        return numeroLibros;
    }

    public void setNumeroLibros(int numeroLibros) {
        this.numeroLibros = numeroLibros;
    }

    public String getNombreRepositorio() {
        return nombreRepositorio;
    }

    public void setNombreRepositorio(String nombreRepositorio) {
        this.nombreRepositorio = nombreRepositorio;
    }

    public String getDireccionRepositorio() {
        return direccionRepositorio;
    }

    public void setDireccionRepositorio(String direccionRepositorio) {
        this.direccionRepositorio = direccionRepositorio;
    }

    public RepositorioLibro getRepositorioLibro() {
        return repositorioLibro;
    }

    public void setRepositorioLibro(RepositorioLibro repositorioLibro) {
        this.repositorioLibro = repositorioLibro;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.numeroLibros;
        hash = 89 * hash + Objects.hashCode(this.nombreRepositorio);
        hash = 89 * hash + Objects.hashCode(this.direccionRepositorio);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TDatosRepositorio other = (TDatosRepositorio) obj;
        if (this.numeroLibros != other.numeroLibros) {
            return false;
        }
        if (!Objects.equals(this.nombreRepositorio, other.nombreRepositorio)) {
            return false;
        }
        return Objects.equals(this.direccionRepositorio, other.direccionRepositorio);
    }
    
    
}
