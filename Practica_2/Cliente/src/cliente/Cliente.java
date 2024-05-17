/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cliente;

import java.util.Scanner;
import java.rmi.Naming;
import servicio.comun.GestorBibliotecaIntf;
import servicio.comun.TLibro;

/**
 *
 * @author Usuario
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TLibro tLibro = new TLibro();

        try {
            int Puerto = 0;
            String Host;
            Scanner Teclado = new Scanner(System.in);
            System.out.print("Introduce el nº de puerto para comunicarse: ");
            Puerto = Teclado.nextInt();
            System.out.print("Introduce el nombre del host: ");
            Host = Teclado.next();

            // Obtiene el stub del rmiregistry 
            GestorBibliotecaIntf biblioStub = (GestorBibliotecaIntf) Naming.lookup("rmi://" + Host + ":" + Puerto + "/GestorBiblioteca");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }
}
