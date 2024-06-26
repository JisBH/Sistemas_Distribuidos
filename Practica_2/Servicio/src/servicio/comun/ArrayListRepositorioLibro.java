/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio.comun;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class ArrayListRepositorioLibro implements RepositorioLibro, Serializable
{

    private List<TLibro> libros = new ArrayList<>();

    @Override
    public void aniadirLibro(TLibro libro)
    {
        this.libros.add(libro);
    }

    @Override
    public void eliminarLibro(TLibro libro)
    {
        this.libros.remove(libro);
    }

    @Override
    public TLibro getLibroPorIsbn(String isbn)
    {

        for (TLibro libro : libros)
        {
            if (libro.isbn.equals(isbn))
            {
                return libro;
            }
        }
        return null;
    }

    @Override
    public TLibro getLibro(int pos)
    {
        return libros.get(pos);
    }

    @Override
    public int numLibros()
    {
        return libros.size();
    }

    @Override
    public List<TLibro> getTodosLibros()
    {
        return libros;
    }

}
