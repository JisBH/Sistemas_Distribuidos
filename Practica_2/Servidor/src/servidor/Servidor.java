/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package servidor;

import java.util.Scanner;
import java.rmi.Naming;
import servicio.comun.GestorBibliotecaIntf;

/**
 *
 * @author Usuario
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {

            GestorBibliotecaIntf biblioStub = new GestorBiblioteca();
            int Puerto = 0;
            Scanner Teclado = new Scanner(System.in);
            System.out.print("Introduce el nº de puerto para comunicarse: ");
            Puerto = Teclado.nextInt();

            Naming.rebind("rmi://localhost:" + Puerto + "/Calculadora", biblioStub);

            System.out.println("Servidor Calculadora esperando peticiones ... ");
            //Naming.unbind("rmi://localhost:"+Puerto+"/Calculadora");

        } catch (Exception e) {
            System.out.println("Error en servidor Calculadora:" + e);
        }
    }

}


