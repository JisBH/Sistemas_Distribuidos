/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cliente;

import java.util.Scanner;
import java.rmi.Naming;
import servicio.comun.GestorBibliotecaIntf;
import servicio.comun.TLibro;
import java.rmi.RemoteException;

/**
 *
 * @author Usuario
 */
public class Cliente
{

    private int MenuPrincipal()
    {
        int salida;
        do
        {
            System.out.println("\n\n GESTOR BIBLIOTECARIO 2.0 (M. PRINCIPAL)");
            System.out.println("*****************************************");
            System.out.println("\t1.- M. Administración");
            System.out.println("\t2.- Consulta de libros");
            System.out.println("\t3.- Préstamo de libros");
            System.out.println("\t4.- Devolución de libros");
            System.out.println("\t0.- Salir\n");
            System.out.println(" Elige opción: ");
            Scanner Teclado = new Scanner(System.in);
            salida = Teclado.nextInt();
            if (salida < 0 || salida > 4)
            {
                System.out.println("\n\n *** Error en la entrada de Datos.***\n");
            }
        } while (salida < 0 || salida > 4);
        return salida;
    }

    private int MenuAdministracion()
    {
        int salida;
        do
        {
            System.out.println("\n\n GESTOR BIBLIOTECARIO 2.0 (M. ADMINISTRACION)");
            System.out.println("**********************************************");
            System.out.println("\t1.- Cargar datos Biblioteca");
            System.out.println("\t2.- Guardar datos Biblioteca");
            System.out.println("\t3.- Nuevo libro");
            System.out.println("\t4.- Comprar libros");
            System.out.println("\t5.- Retirar libros");
            System.out.println("\t6.- Ordenar libros");
            System.out.println("\t7.- Buscar libros");
            System.out.println("\t8.- Listar libros");
            System.out.println("\t0.- Salir\n");
            System.out.println(" Elige opción: ");
            Scanner Teclado = new Scanner(System.in);
            salida = Teclado.nextInt();
            if (salida < 0 || salida > 4)
            {
                System.out.println("\n\n *** Error en la entrada de Datos.***\n");
            }
        } while (salida < 0 || salida > 8);
        return salida;
    }

    private int funcConexion(String contraseña, GestorBibliotecaIntf biblioStub)
    {

        int resultado;

        try
        {
            resultado = biblioStub.Conexion(contraseña);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion conexion");
            resultado = 0;
        }

        return resultado;
    }

    private boolean funcDesconexion(int ida, GestorBibliotecaIntf biblioStub)
    {

        boolean resultado;

        try
        {
            resultado = biblioStub.Desconexion(ida);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion desconexion");
            resultado = false;
        }

        return resultado;
    }

    private int funcAbrirRepositorio(int ida, GestorBibliotecaIntf biblioStub, String nombreRepositorio)
    {

        int resultado;

        try
        {
            resultado = biblioStub.AbrirRepositorio(ida, nombreRepositorio);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion abrir repositorio");
            resultado = -3;
        }

        return resultado;
    }

    private int funcGuardarRepositorio(int ida, GestorBibliotecaIntf biblioStub, int pRepos)
    {

        try
        {
            return biblioStub.GuardarRepositorio(ida, pRepos-1);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion guardar repositorio");
        }

        return -3;
    }

    private void gestionAdministracion(int ida, GestorBibliotecaIntf biblioStub)
    {
        int opcionMenuAdministracion;
        boolean desconexion = false;
        int resultado;
        Scanner teclado = new Scanner(System.in);
        String nombreFichero;
        int opcion;

        do
        {
            opcionMenuAdministracion = MenuAdministracion();
            switch (opcionMenuAdministracion)
            {

                case 1:
                    System.out.println("\n\nIntroduzca el nombre del repositorio a abrir: ");
                    nombreFichero = teclado.nextLine();
                    resultado = funcAbrirRepositorio(ida, biblioStub, nombreFichero);

                    if (resultado == -1)
                    {
                        System.out.println("\n\nError, el id de administrador es incorrecto");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();
                    } else if (resultado == -2)
                    {
                        System.out.println("\n\nError, ya existe un repositorio cargado con el mismo nombre");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();
                    } else if (resultado == 0)
                    {
                        System.out.println("\n\nNo se ha podido abrir el repositorio");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();
                    } else if (resultado == 1)
                    {
                        System.out.println("\n\n**El repositorio ha sido cargado correctamente**");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();
                    }
                    break;

                case 2:
                    System.out.println("POS \t NOMBRE \t\t\t\tDIRECCION\t\t\t\tNº LIBROS");
                    System.out.println("***********************************************");
                    System.out.println("1 = Primero  2 = Segundo... 0 = Todos");
                    System.out.println("Elige repositorio: ");
                    opcion = teclado.nextInt();
                    resultado = funcGuardarRepositorio(ida, biblioStub, opcion);
                    teclado.nextLine(); //Limpia el buffer

                    if (resultado == -1)
                    {
                        System.out.println("\n\nError, el id de administrador es incorrecto");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();
                    } else if (resultado == -2)
                    {
                        System.out.println("\n\nError, la posicion introducida es incorrecta");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();
                    } else if (resultado == 0)
                    {
                        System.out.println("\n\nError, no se ha podido guardar el repositorio");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();
                    } else if (resultado == 1)
                    {
                        System.out.println("\n\n*** Se ha guardado el/los repositorios seleccionados de la biblioteca.**");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();
                    }

                    break;

                case 3:

                    break;

                case 4:

                    break;

                case 5:

                    break;

                case 6:

                    break;

                case 7:

                    break;

                case 8:

                    break;

                case 0:

                    desconexion = funcDesconexion(ida, biblioStub);
                    if (desconexion == true)
                    {
                        System.out.println("Saliendo del menu de administracion\n");

                    } else
                    {
                        System.out.println("Error en la desconexion\n");
                    }

                    break;
            }
        } while (opcionMenuAdministracion != 0 && desconexion != true);
    }

    private void gestionPrincipal(GestorBibliotecaIntf biblioStub)
    {
        int opcionMenuPrincipal;
        String contraseña;
        Scanner teclado = new Scanner(System.in);
        int resultado;

        do
        {
            opcionMenuPrincipal = MenuPrincipal();
            switch (opcionMenuPrincipal)
            {
                case 1:

                    System.out.println("");
                    System.out.println("Por favor inserte la contraseña de administración: ");
                    contraseña = teclado.nextLine();
                    resultado = funcConexion(contraseña, biblioStub);

                    if (resultado == -1)
                    {
                        System.out.println("Error, ya hay un usuario identificado como administrador, solo se permite uno");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();

                    } else if (resultado == -2)
                    {
                        System.out.println("Error, la contraseña es incorrecta\n");
                        System.out.println("\nPresiona una tecla para continuar...\n");
                        teclado.nextLine();

                    } else if (resultado != 0 && resultado != -1 && resultado != -2)
                    {
                        System.out.println("*** Contraseña correcta, puede acceder al menú de administración ***\n");
                        System.out.println("\nPresiona una tecla para continuar...\n");
                        teclado.nextLine();
                        gestionAdministracion(resultado, biblioStub);
                    }

                    break;

                case 2:

                    break;

                case 3:

                    break;

                case 4:

                    break;

                case 0:
                    System.out.println("Saliendo del menu principal");
                    break;
            }
        } while (opcionMenuPrincipal != 0);
    }

    public static void main(String[] args)
    {
        TLibro tLibro = new TLibro();
        Cliente c = new Cliente();

        try
        {
            int Puerto = 0;
            String Host;
            Scanner Teclado = new Scanner(System.in);
            System.out.print("Introduce el nº de puerto para comunicarse: ");
            Puerto = Teclado.nextInt();
            System.out.print("Introduce el nombre del host: ");
            Host = Teclado.next();

            // Obtiene el stub del rmiregistry 
            GestorBibliotecaIntf biblioStub = (GestorBibliotecaIntf) Naming.lookup("rmi://" + Host + ":" + Puerto + "/GestorBiblioteca");
            c.gestionPrincipal(biblioStub);
        } catch (Exception e)
        {
            System.out.println("Error: " + e);
        }

    }
}
