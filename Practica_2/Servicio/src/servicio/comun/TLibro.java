/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio.comun;

import java.util.Objects;

/**
 *
 * @author Usuario
 */
public class TLibro {
    
    public String titulo;
    public String autor;
    public String pais;
    public String idioma;
    public String isbn;
    public String anio;
    public int disponibles;
    public int prestados;
    public int reservados;

    public TLibro() {
    }

    public TLibro(String titulo, String autor, String pais, String idioma, String isbn, String anio, int disponibles, int prestados, int reservados) {
        this.titulo = titulo;
        this.autor = autor;
        this.pais = pais;
        this.idioma = idioma;
        this.isbn = isbn;
        this.anio = anio;
        this.disponibles = disponibles;
        this.prestados = prestados;
        this.reservados = reservados;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.titulo);
        hash = 67 * hash + Objects.hashCode(this.autor);
        hash = 67 * hash + Objects.hashCode(this.pais);
        hash = 67 * hash + Objects.hashCode(this.idioma);
        hash = 67 * hash + Objects.hashCode(this.isbn);
        hash = 67 * hash + Objects.hashCode(this.anio);
        hash = 67 * hash + this.disponibles;
        hash = 67 * hash + this.prestados;
        hash = 67 * hash + this.reservados;
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
        final TLibro other = (TLibro) obj;
        if (this.disponibles != other.disponibles) {
            return false;
        }
        if (this.prestados != other.prestados) {
            return false;
        }
        if (this.reservados != other.reservados) {
            return false;
        }
        if (!Objects.equals(this.titulo, other.titulo)) {
            return false;
        }
        if (!Objects.equals(this.autor, other.autor)) {
            return false;
        }
        if (!Objects.equals(this.pais, other.pais)) {
            return false;
        }
        if (!Objects.equals(this.idioma, other.idioma)) {
            return false;
        }
        if (!Objects.equals(this.isbn, other.isbn)) {
            return false;
        }
        return Objects.equals(this.anio, other.anio);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TLibro{");
        sb.append("titulo=").append(titulo);
        sb.append(", autor=").append(autor);
        sb.append(", pais=").append(pais);
        sb.append(", idioma=").append(idioma);
        sb.append(", isbn=").append(isbn);
        sb.append(", anio=").append(anio);
        sb.append(", disponibles=").append(disponibles);
        sb.append(", prestados=").append(prestados);
        sb.append(", reservados=").append(reservados);
        sb.append('}');
        return sb.toString();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }

    public int getPrestados() {
        return prestados;
    }

    public void setPrestados(int prestados) {
        this.prestados = prestados;
    }

    public int getReservados() {
        return reservados;
    }

    public void setReservados(int reservados) {
        this.reservados = reservados;
    }
    
}
