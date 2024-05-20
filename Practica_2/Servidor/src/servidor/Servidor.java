/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package servidor;

import servicio.comun.GestorBibliotecaIntf;
import java.rmi.RemoteException;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.Scanner;

/**
 *
 * @author Usuario
 */
public class Servidor
{

    public static void main(String[] args) throws RemoteException
    {

        try
        {
            int Puerto = 0;
            Scanner Teclado = new Scanner(System.in);
            System.out.print("Introduce el nº de puerto para comunicarse: ");
            Puerto = Teclado.nextInt();

            Registry registry = LocateRegistry.createRegistry(Puerto);
            GestorBiblioteca obj = new GestorBiblioteca();

            GestorBibliotecaIntf stub = (GestorBibliotecaIntf) UnicastRemoteObject.exportObject(obj, Puerto);

            registry = LocateRegistry.getRegistry(Puerto);
            registry.bind("GestorBiblioteca", stub);

            System.out.println("Servidor GestorBiblioteca esperando peticiones ... ");

        } catch (Exception e)
        {
            System.out.println("Error en servidor GestorBiblioteca:" + e);
        }
    }

}
