/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio.comun;

import java.util.Comparator;

/**
 *
 * @author Usuario
 */
public class ComparadorLibro implements Comparator<TLibro>
{
     private int campoOrdenacion;

    public ComparadorLibro(int campoOrdenacion)
    {
        this.campoOrdenacion = campoOrdenacion;
    }

    @Override
    public int compare(TLibro o1, TLibro o2)
    {
        int C = 0;
        switch (this.campoOrdenacion)
        {
            case 0:
                C = o1.getIsbn().compareTo(o2.getIsbn());
                break;
            case 1:
                C = o1.getTitulo().compareTo(o2.getTitulo());
                break;
            case 2:
                C = o1.getAutor().compareTo(o2.getAutor());
                break;
            case 3:
                C = Integer.compare(o1.getAnio(), o2.getAnio());
                break;
            case 4:
                C = o1.getPais().compareTo(o2.getPais());
                break;
            case 5:
                C = o1.getIdioma().compareTo(o2.getIdioma());
                break;
            case 6:
                C = Integer.compare(o1.getDisponibles(), o2.getDisponibles());
                break;
            case 7:
                C = Integer.compare(o1.getPrestados(), o2.getPrestados());
                break;
            case 8:
                C = Integer.compare(o1.getReservados(), o2.getReservados());
                break;
        }
        return C;
    }
}
