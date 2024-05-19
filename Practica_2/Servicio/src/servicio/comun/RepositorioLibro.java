/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package servicio.comun;

/**
 *
 * @author Usuario
 */
public interface RepositorioLibro {
    
    public void aniadirLibro(TLibro libro);
    
    public void eliminarLibro(TLibro libro);
    
    public TLibro getLibroPorIsbn(String isbn);
    
    public TLibro getLibro(int pos);
    
}
