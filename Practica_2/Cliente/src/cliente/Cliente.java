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
import java.util.ArrayList;
import java.util.List;
import servicio.comun.TDatosRepositorio;

/**
 *
 * @author Usuario
 */
public class Cliente
{

    private String Ajustar(String S, int Ancho)
    {
        byte v[] = S.getBytes();
        int c = 0;
        int len = 0;
        int uin;
        for (int i = 0; i < v.length; i++)
        {
            uin = Byte.toUnsignedInt(v[i]);
            if (uin > 128)
            {
                c++;
            }
        }

        len = c / 2;

        for (int i = 0; i < len; i++)
        {
            S = S + " ";
        }

        return S;
    }

    public void Mostrar(int Pos, boolean Cabecera, TLibro libro)
    {
        if (Cabecera)
        {
            System.out.println(String.format("%-5s%-58s%-18s%-4s%-4s%-4s", "POS", "TITULO", "ISBN", "DIS", "PRE", "RES"));
            System.out.println(String.format("     %-30s%-28s%-12s", "AUTOR", "PAIS (IDIOMA)", "AÑO"));
            for (int i = 0; i < 93; i++)
            {
                System.out.print("*");
            }
            System.out.print("\n");
        }

        String T = Ajustar(String.format("%-58s", libro.getTitulo()), 58);
        String A = Ajustar(String.format("%-30s", libro.getAutor()), 30);
        String PI = Ajustar(String.format("%-28s", libro.getPais() + " (" + libro.getIdioma() + ")"), 28);

        System.out.println(String.format("%-5d%s%-18s%-4d%-4d%-4d", Pos + 1, T, libro.getIsbn(), libro.getDisponibles(), libro.getPrestados(), libro.getReservados()));
        System.out.println(String.format("     %s%s%-12d", A, PI, libro.getAnio()));
    }

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
            if (salida < 0 || salida > 8)
            {
                System.out.println("\n\n *** Error en la entrada de Datos.***\n");
            }
        } while (salida < 0 || salida > 8);
        return salida;
    }

    private void muestraListaRepositorios(List<TDatosRepositorio> repositorios, boolean flag)
    {
        System.out.println("POS \t\t\tNOMBRE \t\t\tDIRECCION \t\tNº LIBROS");
        for (int i = 0; i < 93; i++)
        {
            System.out.print("*");
        }

        System.out.println("");

        for (int i = 0; i < repositorios.size(); i++)
        {
            TDatosRepositorio repositorio = repositorios.get(i);

            System.out.println((i + 1) + " \t\t\t" + repositorio.getNombreRepositorio()
                    + " \t\t" + repositorio.getDireccionRepositorio() + " \t\t"
                    + repositorio.getNumeroLibros());
        }

        if (flag == true)
        {
            System.out.println(0 + " \t\t\tTodos los repositorios");
        }
    }

    private void obtenerListaRepositorios(int ida, GestorBibliotecaIntf biblioStub, boolean flag)
    {

        List<TDatosRepositorio> repositoriosCargados = new ArrayList<>();

        int numRepositorios = funcNRepositorios(ida, biblioStub);

        for (int i = 0; i < numRepositorios; i++)
        {
            TDatosRepositorio datosRepositorio = funcDatosRepositorio(ida, biblioStub, i);

            if (datosRepositorio != null)
            {
                repositoriosCargados.add(datosRepositorio);

            }
        }

        muestraListaRepositorios(repositoriosCargados, flag);

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
            return biblioStub.GuardarRepositorio(ida, pRepos - 1);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion guardar repositorio");
        }

        return -3;
    }

    private int funcNRepositorios(int ida, GestorBibliotecaIntf biblioStub)
    {
        try
        {
            return biblioStub.NRepositorios(ida);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion nRepositorios");
        }

        return -3;
    }

    private TDatosRepositorio funcDatosRepositorio(int ida, GestorBibliotecaIntf biblioStub, int pRepos)
    {

        try
        {
            return biblioStub.DatosRepositorio(ida, pRepos);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion DatosRepositorio");
        }

        return null;
    }

    private int funcNuevoLibro(int ida, GestorBibliotecaIntf biblioStub, TLibro libro, int pRepos)
    {

        try
        {
            return biblioStub.NuevoLibro(ida, libro, pRepos - 1);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion nuevo libro");
        }

        return -3;
    }

    private int funcNLibros(int pRepo, GestorBibliotecaIntf biblioStub)
    {

        try
        {
            return biblioStub.NLibros(pRepo);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion NLibros");
        }

        return -3;
    }

    private TLibro funcDescargar(int ida, GestorBibliotecaIntf biblioStub, int pRepo, int pPos)
    {

        try
        {
            return biblioStub.Descargar(ida, pRepo, pPos);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion descargar");
        }

        return null;
    }

    private int funcComprar(int ida, GestorBibliotecaIntf biblioStub, String pIsbn, int pNoLibros)
    {

        try
        {
            return biblioStub.Comprar(ida, pIsbn, pNoLibros);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion comprar");
        }

        return -3;
    }

    private int funcBuscar(int pIda, GestorBibliotecaIntf biblioStub, String pIsbn)
    {

        try
        {
            return biblioStub.Buscar(pIda, pIsbn);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion buscar");
        }

        return -3;
    }

    private int funcRetirar(int pIda, GestorBibliotecaIntf biblioStub, String pIsbn, int pNoLibros)
    {

        try
        {
            return biblioStub.Retirar(pIda, pIsbn, pNoLibros);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion retirar");
        }

        return -3;
    }

    private boolean funcOrdenar(int pIda, GestorBibliotecaIntf biblioStub, int pCampo)
    {

        try
        {
            return biblioStub.Ordenar(pIda, pCampo);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion ordenar");
        }
        return false;
    }

    private int funcPrestar(int pPos, GestorBibliotecaIntf biblioStub)
    {

        try
        {
            return biblioStub.Prestar(pPos);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion prestar");
        }
        return -3;
    }

    private int funcDevolver(int pPos, GestorBibliotecaIntf biblioStub)
    {

        try
        {
            return biblioStub.Devolver(pPos);

        } catch (RemoteException ex)
        {
            System.out.println("Se ha producido un error en la funcion devolver");
        }
        return -3;
    }

    private boolean filtrarLibros(int ida, GestorBibliotecaIntf biblioStub, boolean admin)
    {
        Scanner teclado = new Scanner(System.in);
        int nlibros, contador = 0, reposOpcion, numRepos;
        TLibro libro;
        boolean mostrar = true, cargado = true, opcion_correcta = true, encontrado = false;
        String texto;
        char opcion;

        System.out.println("Introduce el texto a buscar: ");
        texto = teclado.nextLine();
        System.out.println("\nCódigo de Búsqueda\n");
        System.out.println("I.- Por Isbn\n");
        System.out.println("T.- Por Titulo\n");
        System.out.println("A.- Por Autor\n");
        System.out.println("P.- Por Pais\n");
        System.out.println("D.- Por Idioma\n");
        System.out.println("*.- Por todos los campos\n");
        System.out.print("\nIntroduce el Código: ");
        opcion = teclado.next().charAt(0);
        // Limpieza buffer
        teclado.nextLine();
        System.out.println("\n");

        if (admin)
        {
            numRepos = funcNRepositorios(ida, biblioStub);

            if (numRepos == 0)
            {
                System.out.println("Error, no hay ningun repositorio cargado");
                System.out.println("Presiona una tecla para continuar");
                teclado.nextLine();
                return false;
            }

            obtenerListaRepositorios(ida, biblioStub, true);

            do
            {
                System.out.print("Elige repositorio: ");
                reposOpcion = teclado.nextInt();
                // Limpieza buffer
                teclado.nextLine();
                System.out.println("");

                if (reposOpcion < 0 || reposOpcion > numRepos)
                {
                    System.out.println("\nError, debes elegir un repositorio valido");

                }

            } while (reposOpcion < 0 || reposOpcion > numRepos);

        } else
        {
            reposOpcion = 0;
            nlibros = funcNLibros(-1, biblioStub);

            if (nlibros == 0)
            {
                System.out.println("Error, no hay ningun repositorio cargado");
                System.out.println("Presiona una tecla para continuar");
                teclado.nextLine();
                return false;
            }
        }

        switch (opcion)
        {

            case 'i':
            case 'I':
                nlibros = funcNLibros(reposOpcion - 1, biblioStub);

                for (int i = 0; i < nlibros; i++)
                {
                    libro = funcDescargar(ida, biblioStub, reposOpcion - 1, i);

                    if (texto.equalsIgnoreCase(libro.getIsbn()))
                    {
                        Mostrar(i, mostrar, libro);
                        contador++;
                        mostrar = false;
                    }
                }

                break;

            case 't':
            case 'T':

                nlibros = funcNLibros(reposOpcion - 1, biblioStub);

                for (int i = 0; i < nlibros; i++)
                {
                    libro = funcDescargar(ida, biblioStub, reposOpcion - 1, i);

                    if (texto.equalsIgnoreCase(libro.getTitulo()))
                    {
                        Mostrar(i, mostrar, libro);
                        contador++;
                        mostrar = false;
                    }
                }

                break;

            case 'a':
            case 'A':

                nlibros = funcNLibros(reposOpcion - 1, biblioStub);

                for (int i = 0; i < nlibros; i++)
                {
                    libro = funcDescargar(ida, biblioStub, reposOpcion - 1, i);

                    if (texto.equalsIgnoreCase(libro.getAutor()))
                    {
                        Mostrar(i, mostrar, libro);
                        contador++;
                        mostrar = false;
                    }
                }

                break;

            case 'p':
            case 'P':

                nlibros = funcNLibros(reposOpcion - 1, biblioStub);

                for (int i = 0; i < nlibros; i++)
                {
                    libro = funcDescargar(ida, biblioStub, reposOpcion - 1, i);

                    if (texto.equalsIgnoreCase(libro.getPais()))
                    {
                        Mostrar(i, mostrar, libro);

                        contador++;
                        mostrar = false;
                    }

                }

                break;

            case 'd':
            case 'D':

                nlibros = funcNLibros(reposOpcion - 1, biblioStub);

                for (int i = 0; i < nlibros; i++)
                {
                    libro = funcDescargar(ida, biblioStub, reposOpcion - 1, i);

                    if (texto.equalsIgnoreCase(libro.getIdioma()))
                    {
                        Mostrar(i, mostrar, libro);
                        contador++;
                        mostrar = false;
                    }
                }

                break;

            case '*':

                nlibros = funcNLibros(reposOpcion - 1, biblioStub);

                for (int i = 0; i < nlibros; i++)
                {
                    libro = funcDescargar(ida, biblioStub, reposOpcion - 1, i);

                    if (texto.equalsIgnoreCase(libro.getIsbn()))
                    {
                        Mostrar(i, mostrar, libro);
                        contador++;
                        mostrar = false;

                    } else if (texto.equalsIgnoreCase(libro.getTitulo()))
                    {
                        Mostrar(i, mostrar, libro);
                        contador++;
                        mostrar = false;

                    } else if (texto.equalsIgnoreCase(libro.getAutor()))
                    {
                        Mostrar(i, mostrar, libro);
                        contador++;
                        mostrar = false;

                    } else if (texto.equalsIgnoreCase(libro.getPais()))
                    {
                        Mostrar(i, mostrar, libro);
                        contador++;
                        mostrar = false;

                    } else if (texto.equalsIgnoreCase(libro.getIdioma()))
                    {
                        Mostrar(i, mostrar, libro);
                        contador++;
                        mostrar = false;
                    }
                }

                break;

            default:
                System.out.println("\nError, se debe seleccionar una de las opciones indicadas\n");
                teclado.nextLine();
                opcion_correcta = false;
        }

        if (contador != 0)
        {
            System.out.println("\nPresiona una tecla para continuar...\n");
            teclado.nextLine();
            encontrado = true;

        } else if (opcion_correcta == true)
        {
            System.out.println("\nNo se han encontrado coincidencias...\n");
            teclado.nextLine();
        }

        return encontrado;

    }

    private void gestionAdministracion(int ida, GestorBibliotecaIntf biblioStub)
    {
        int opcionMenuAdministracion;
        boolean desconexion = false;
        boolean res = false;
        int resultado;
        Scanner teclado = new Scanner(System.in);
        String nombreFichero;
        int opcion;
        String isbn;
        String autor;
        String titulo;
        int anio;
        String pais;
        String idioma;
        int numLibros;
        TLibro libro;
        char siNo;

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
                    obtenerListaRepositorios(ida, biblioStub, true);
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
                    System.out.println("Introduce el Isbn: ");
                    isbn = teclado.nextLine();
                    System.out.println("Introduce el Autor: ");
                    autor = teclado.nextLine();
                    System.out.println("Introduce el Titulo: ");
                    titulo = teclado.nextLine();
                    System.out.println("Introduce el Anio: ");
                    anio = teclado.nextInt();
                    teclado.nextLine(); // Limpia el buffer
                    System.out.println("Introduce el Pais: ");
                    pais = teclado.nextLine();
                    System.out.println("Introduce el Idioma: ");
                    idioma = teclado.nextLine();
                    System.out.println("Introduce el numero inicial de libros: ");
                    numLibros = teclado.nextInt();
                    teclado.nextLine(); // Limpia el buffer
                    libro = new TLibro(titulo, autor, pais, idioma, isbn, anio, numLibros, 0, 0);
                    obtenerListaRepositorios(ida, biblioStub, false);
                    System.out.println("Elige repositorio: ");
                    opcion = teclado.nextInt();

                    resultado = funcNuevoLibro(ida, biblioStub, libro, opcion);
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
                        System.out.println("\n\nError, ya hay un libro con el mismo isbn en el repositorio indicado");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();
                    } else if (resultado == 1)
                    {
                        System.out.println("\n\n*** El libro ha sido añadido correctamente.**");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();
                    }
                    break;

                case 4:
                    System.out.println("Introduce ISBN a buscar: ");
                    isbn = teclado.nextLine();
                    resultado = funcBuscar(ida, biblioStub, isbn);

                    if (resultado == -2)
                    {
                        System.out.println("\n\nError, el id de administrador es incorrecto");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();

                    } else if (resultado == -1)
                    {
                        System.out.println("\n\nError, no se ha encontrado ningun libro con el isbn indicado");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();

                    } else if (resultado != -3)
                    {
                        libro = funcDescargar(ida, biblioStub, -1, resultado);
                        Mostrar(resultado, true, libro);

                        do
                        {
                            System.out.print("\n¿Es este el libro del que deseas comprar mas unidades (s/n)? ");
                            siNo = teclado.next().charAt(0);
                            siNo = Character.toLowerCase(siNo);
                            teclado.nextLine(); //Limpia el buffer

                            if (siNo != 's' && siNo != 'n')
                            {
                                System.out.println("Error, debes seleccionar s o n");
                            }
                        } while (siNo != 's' && siNo != 'n');

                        if (siNo == 's')
                        {
                            System.out.print("Introduce el numero de libros comprados: ");
                            numLibros = teclado.nextInt();
                            resultado = funcComprar(ida, biblioStub, isbn, numLibros);
                            teclado.nextLine(); //Limpia el buffer

                            if (resultado == -1)
                            {
                                System.out.println("\n\nError, el id de administrador es incorrecto");
                                System.out.println("\nPresiona una tecla para continuar...");
                                teclado.nextLine();

                            } else if (resultado == 0)
                            {
                                System.out.println("\n\nError, no se ha encontrado ningun libro con el isbn indicado");
                                System.out.println("\nPresiona una tecla para continuar...");
                                teclado.nextLine();

                            } else if (resultado == 1)
                            {
                                System.out.println("\n\n*** Se han añadido los nuevos libros.**");
                                System.out.println("\nPresiona una tecla para continuar...");
                                teclado.nextLine();
                            }
                        }
                    }

                    break;

                case 5:
                    System.out.println("Introduce ISBN a buscar: ");
                    isbn = teclado.nextLine();
                    resultado = funcBuscar(ida, biblioStub, isbn);

                    if (resultado == -2)
                    {
                        System.out.println("\n\nError, el id de administrador es incorrecto");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();

                    } else if (resultado == -1)
                    {
                        System.out.println("\n\nError, no se ha encontrado ningun libro con el isbn indicado");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();

                    } else if (resultado != -3)
                    {
                        libro = funcDescargar(ida, biblioStub, -1, resultado);
                        Mostrar(resultado, true, libro);

                        do
                        {
                            System.out.print("\n¿Es este el libro del que deseas retirar unidades (s/n)? ");
                            siNo = teclado.next().charAt(0);
                            siNo = Character.toLowerCase(siNo);
                            teclado.nextLine(); //Limpia el buffer

                            if (siNo != 's' && siNo != 'n')
                            {
                                System.out.println("Error, debes seleccionar s o n");
                            }
                        } while (siNo != 's' && siNo != 'n');

                        if (siNo == 's')
                        {
                            System.out.print("Introduce el numero de libros a retirar: ");
                            numLibros = teclado.nextInt();
                            resultado = funcRetirar(ida, biblioStub, isbn, numLibros);
                            teclado.nextLine(); //Limpia el buffer

                            if (resultado == -1)
                            {
                                System.out.println("\n\nError, el id de administrador es incorrecto");
                                System.out.println("\nPresiona una tecla para continuar...");
                                teclado.nextLine();

                            } else if (resultado == 0)
                            {
                                System.out.println("\n\nError, no se ha encontrado ningun libro con el isbn indicado");
                                System.out.println("\nPresiona una tecla para continuar...");
                                teclado.nextLine();

                            } else if (resultado == 2)
                            {
                                System.out.println("\n\nError, hay menos libros de los que se ha solicitado retirar");
                                System.out.println("\nPresiona una tecla para continuar...");
                                teclado.nextLine();

                            } else if (resultado == 1)
                            {
                                System.out.println("\n\n*** Se ha retirado el numero de libros indicados.**");
                                System.out.println("\nPresiona una tecla para continuar...");
                                teclado.nextLine();
                            }
                        }
                    }

                    break;

                case 6:
                    System.out.println("\nCódigo de Ordenación");
                    System.out.println("0.- Por ISBN");
                    System.out.println("1.- Por Título");
                    System.out.println("2.- Por Autor");
                    System.out.println("3.- Por Año");
                    System.out.println("4.- Por País");
                    System.out.println("5.- Por Idioma");
                    System.out.println("6.- Por nº de libros disponibles");
                    System.out.println("7.- Por nº de libros prestados");
                    System.out.println("8.- Por nº de libros en espera");

                    System.out.println("Introduce código:");
                    opcion = teclado.nextInt();
                    res = funcOrdenar(ida, biblioStub, opcion);
                    teclado.nextLine();

                    if (res)
                    {
                        System.out.println("*** La biblioteca ha sido ordenada correctamente.**");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();
                    } else
                    {
                        System.out.println("\n\nError, no se ha podido ordenar la biblioteca");
                        System.out.println("\nPresiona una tecla para continuar...");
                        teclado.nextLine();
                    }

                    break;

                case 7:
                    filtrarLibros(ida, biblioStub, true);
                    break;

                case 8:
                    numLibros = funcNLibros(-1, biblioStub);

                    for (int i = 0; i < numLibros; i++)
                    {
                        libro = funcDescargar(ida, biblioStub, -1, i);

                        if (i == 0)
                        {
                            Mostrar(i, true, libro);
                        } else
                        {
                            Mostrar(i, false, libro);
                        }
                    }

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
        int resultado, num;
        boolean res;
        char siNo;

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
                    filtrarLibros(-2, biblioStub, false);

                    break;

                case 3:
                    res = filtrarLibros(-2, biblioStub, false);

                    if (res)
                    {
                        do
                        {
                            System.out.println("¿Quiere sacar un libro de la biblioteca (s/n)? ");
                            siNo = teclado.next().charAt(0);
                            siNo = Character.toLowerCase(siNo);
                            // Limpiar el buffer
                            teclado.nextLine();

                            if (siNo != 's' && siNo != 'n')
                            {
                                System.out.println("Error, debes seleccionar s o n");
                            }
                        } while (siNo != 's' && siNo != 'n');

                        if (siNo == 's')
                        {

                            System.out.println("Introduce la posicion del libro a solicitar su prestamo: ");
                            num = teclado.nextInt();
                            // Limpiar el buffer
                            teclado.nextLine();
                            resultado = funcPrestar(num - 1, biblioStub);

                            if (resultado == -1)
                            {
                                System.out.println("Error, la posicion introducida no esta dentro de los limites del repositorio mezclado y ordenado\n");
                                System.out.println("\nPresiona una tecla para continuar...\n");
                                teclado.nextLine();

                            } else if (resultado == 1)
                            {
                                System.out.println("*** El préstamo se ha concedido, recoge el libro en el mostrador.**\n");
                                System.out.println("\nPresiona una tecla para continuar...\n");
                                teclado.nextLine();

                            } else if (resultado == 0)
                            {
                                System.out.println("** Se le ha puesto en lista de espera \n**");
                                System.out.println("\nPresiona una tecla para continuar...\n");
                                teclado.nextLine();
                            }
                        }
                    }

                    break;

                case 4:
                    res = filtrarLibros(-2, biblioStub, false);

                    if (res)
                    {
                        do
                        {
                            System.out.println("¿Quiere devolver un libro de la biblioteca (s/n)? ");
                            siNo = teclado.next().charAt(0);
                            siNo = Character.toLowerCase(siNo);
                            // Limpiar el buffer
                            teclado.nextLine();

                            if (siNo != 's' && siNo != 'n')
                            {
                                System.out.println("Error, debes seleccionar s o n");
                            }
                        } while (siNo != 's' && siNo != 'n');

                        if (siNo == 's')
                        {

                            System.out.println("Introduce la posicion del libro a devolver: ");
                            num = teclado.nextInt();
                            // Limpiar el buffer
                            teclado.nextLine();
                            resultado = funcDevolver(num - 1, biblioStub);

                            if (resultado == -1)
                            {
                                System.out.println("Error, la posicion introducida no esta dentro de los limites del repositorio mezclado y ordenado\n");
                                System.out.println("\nPresiona una tecla para continuar...\n");
                                teclado.nextLine();

                            } else if (resultado == 1)
                            {
                                System.out.println("*** Se ha devuelto el libro y se pondra en la estanteria**\n");
                                System.out.println("\nPresiona una tecla para continuar...\n");
                                teclado.nextLine();

                            } else if (resultado == 0)
                            {
                                System.out.println("** Se ha devuelto el libro y se ha reducido el numero de usuarios en espera **\n");
                                System.out.println("\nPresiona una tecla para continuar...\n");
                                teclado.nextLine();

                            } else if (resultado == 2)
                            {
                                System.out.println("Error, el libro no se puede devolver porque no hay usuarios en lista de espera ni libros prestados\n");
                                System.out.println("\nPresiona una tecla para continuar...\n");
                                teclado.nextLine();
                            }
                        }
                    }

                    break;

                case 0:
                    System.out.println("Saliendo del menu principal");
                    teclado.close();
                    break;
            }
        } while (opcionMenuPrincipal != 0);
    }

    public static void main(String[] args)
    {
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