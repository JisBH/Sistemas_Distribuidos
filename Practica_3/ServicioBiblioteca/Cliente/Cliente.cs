using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Runtime.Remoting;
using System.Runtime.Remoting.Channels;
using System.Runtime.Remoting.Channels.Tcp;


namespace ServicioBiblioteca
{
    public class Cliente
    {
        private String Ajustar(String S, int Ancho)
        {
            byte[] v = Encoding.ASCII.GetBytes(S);
            int c = 0;
            int len = 0;
            uint uin;
            for (int i = 0; i < v.Length; i++)
            {
                uin = Convert.ToUInt32(v[i]);
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

        public void Mostrar(int Pos, bool Cabecera, TLibro libro)
        {
            if (Cabecera)
            {
                Console.WriteLine(String.Format("{0,-5}{1,-58}{2,-18}{3,-4}{4,-4}{5,-4}", "POS", "TITULO", "ISBN", "DIS", "PRE", "RES"));
                Console.WriteLine(String.Format("     {0,-30}{1,-28}{2,-12}", "AUTOR", "PAIS (IDIOMA)", "AÑO"));
                for (int i = 0; i < 93; i++)
                {
                    Console.Write("*");
                }
                Console.WriteLine("\n");
            }

            String T = Ajustar(String.Format("{0,-58}", libro.Titulo), 58);
            String A = Ajustar(String.Format("{0,-30}", libro.Autor), 30);
            String PI = Ajustar(String.Format("{0,-28}", libro.Pais + " (" + libro.Idioma + ")"), 28);

            Console.WriteLine(String.Format("{0,-5}{1,-18}{2,-4}{3,4}{4,4}{5,4}", Pos + 1, T, libro.Isbn, libro.Disponibles, libro.Prestados, libro.Reservados));
            Console.WriteLine(String.Format("     {0}{1}{2,-12}", A, PI, libro.Anio));
            Console.WriteLine("\n");
        }

        private void MuestraListaRepositorios(List<TDatosRepositorio> repositorios, bool flag)
        {
            Console.WriteLine(String.Format("{0,-5}{1,-38}{2,-28}{3,-10}", "POS", "NOMBRE", "DIRECCION", "Nº LIBROS"));

            for (int i = 0; i < 93; i++)
            {
                Console.Write("*");
            }

            Console.WriteLine("");

            for (int i = 0; i < repositorios.Count; i++)
            {
                TDatosRepositorio repositorio = repositorios.ElementAt(i);

                String nombre = Ajustar(String.Format("{0,-38}", repositorio.NombreRepositorio), 38);

                Console.WriteLine(String.Format("{0,-5}{1,-18}{2,-28}{3,-10}",
                    (i + 1), nombre, repositorio.DireccionRepositorio, repositorio.NumeroLibros));
            }

            if (flag == true)
            {
                Console.WriteLine(String.Format("{0,-5}{1,-18}", 0, "Todos los repositorios"));
            }
        }

        private void ObtenerListaRepositorios(int ida, GestorBiblioteca gestorBiblioteca, bool flag)
        {

            List<TDatosRepositorio> repositoriosCargados = new List<TDatosRepositorio>();

            int numRepositorios = FuncNRepositorios(ida, gestorBiblioteca);

            for (int i = 0; i < numRepositorios; i++)
            {
                TDatosRepositorio datosRepositorio = FuncDatosRepositorio(ida, gestorBiblioteca, i);

                if (datosRepositorio != null)
                {
                    repositoriosCargados.Add(datosRepositorio);

                }
            }

            MuestraListaRepositorios(repositoriosCargados, flag);

        }

        private void Pausa()
        {
            Console.WriteLine("\nPresiona una tecla para continuar...");
            Console.ReadLine();
        }

        private int MenuPrincipal()
        {
            int salida;
            do
            {
                Console.WriteLine("\n\n GESTOR BIBLIOTECARIO 3.0 (M. PRINCIPAL)");
                Console.WriteLine("*****************************************");
                Console.WriteLine("\t1.- M. Administración");
                Console.WriteLine("\t2.- Consulta de libros");
                Console.WriteLine("\t3.- Préstamo de libros");
                Console.WriteLine("\t4.- Devolución de libros");
                Console.WriteLine("\t0.- Salir\n");
                Console.WriteLine(" Elige opción: ");
                salida = Convert.ToInt32(Console.ReadLine());

                if (salida < 0 || salida > 4)
                {
                    Console.WriteLine("\n\n *** Error en la entrada de Datos.***\n");
                }
            } while (salida < 0 || salida > 4);
            return salida;
        }

        private int MenuAdministracion()
        {
            int salida;
            do
            {
                Console.WriteLine("\n\n GESTOR BIBLIOTECARIO 3.0 (M. ADMINISTRACION)");
                Console.WriteLine("**********************************************");
                Console.WriteLine("\t1.- Cargar datos Biblioteca");
                Console.WriteLine("\t2.- Guardar datos Biblioteca");
                Console.WriteLine("\t3.- Nuevo libro");
                Console.WriteLine("\t4.- Comprar libros");
                Console.WriteLine("\t5.- Retirar libros");
                Console.WriteLine("\t6.- Ordenar libros");
                Console.WriteLine("\t7.- Buscar libros");
                Console.WriteLine("\t8.- Listar libros");
                Console.WriteLine("\t0.- Salir\n");
                Console.WriteLine(" Elige opción: ");
                salida = Convert.ToInt32(Console.ReadLine());

                if (salida < 0 || salida > 8)
                {
                    Console.WriteLine("\n\n *** Error en la entrada de Datos.***\n");
                }
            } while (salida < 0 || salida > 8);
            return salida;
        }

        private int FuncConexion(String contraseña, GestorBiblioteca gestorBilioteca)
        {

            int resultado;

            try
            {
                resultado = gestorBilioteca.Conexion(contraseña);

            }
            catch (Exception)
            {
                Console.WriteLine("Se ha producido un error en la funcion conexion");
                resultado = 0;
            }

            return resultado;
        }

        private bool FuncDesconexion(int ida, GestorBiblioteca gestorBilioteca)
        {

            bool resultado;

            try
            {
                resultado = gestorBilioteca.Desconexion(ida);

            }
            catch (Exception)
            {
                Console.WriteLine("Se ha producido un error en la funcion desconexion");
                resultado = false;
            }

            return resultado;
        }

        private int FuncAbrirRepositorio(int ida, GestorBiblioteca gestorBilioteca, String nombreRepositorio)
        {

            int resultado;

            try
            {
                resultado = gestorBilioteca.AbrirRepositorio(ida, nombreRepositorio);

            }
            catch (Exception)
            {
                Console.WriteLine("Se ha producido un error en la funcion abrir repositorio");
                resultado = -3;
            }

            return resultado;
        }

        private int FuncNLibros(int pRepo, GestorBiblioteca gestorBilioteca)
        {

            try
            {
                return gestorBilioteca.NLibros(pRepo);

            }
            catch (Exception)
            {
                Console.WriteLine("Se ha producido un error en la funcion NLibros");
            }

            return -3;
        }

        private TLibro FuncDescargar(int ida, GestorBiblioteca gestorBilioteca, int pRepo, int pPos)
        {

            try
            {
                return gestorBilioteca.Descargar(ida, pRepo, pPos);

            }
            catch (Exception)
            {
                Console.WriteLine("Se ha producido un error en la funcion descargar");
            }

            return null;
        }

        private bool FuncOrdenar(int pIda, GestorBiblioteca gestorBilioteca, int pCampo)
        {

            try
            {
                return gestorBilioteca.Ordenar(pIda, pCampo);

            }
            catch (Exception)
            {
                Console.WriteLine("Se ha producido un error en la funcion ordenar");
            }
            return false;
        }

        private int FuncGuardarRepositorio(int ida, GestorBiblioteca gestorBilioteca, int pRepos)
        {

            try
            {
                return gestorBilioteca.GuardarRepositorio(ida, pRepos - 1);

            }
            catch (Exception)
            {
                Console.WriteLine("Se ha producido un error en la funcion guardar repositorio");
            }

            return -3;
        }

        private int FuncNRepositorios(int ida, GestorBiblioteca gestorBilioteca)
        {
            try
            {
                return gestorBilioteca.NRepositorios(ida);

            }
            catch (Exception)
            {
                Console.WriteLine("Se ha producido un error en la funcion nRepositorios");
            }

            return -3;
        }

        private TDatosRepositorio FuncDatosRepositorio(int ida, GestorBiblioteca gestorBilioteca, int pRepos)
        {

            try
            {
                return gestorBilioteca.DatosRepositorio(ida, pRepos);

            }
            catch (Exception)
            {
                Console.WriteLine("Se ha producido un error en la funcion DatosRepositorio");
            }

            return null;
        }

        private void GestionAdministracion(int ida, GestorBiblioteca gestorBiblioteca)
        {
            int opcionMenuAdministracion;
            bool desconexion = false;
            bool res = false;
            int resultado;
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
                        Console.WriteLine("\n\nIntroduzca el nombre del repositorio a abrir: ");
                        nombreFichero = Console.ReadLine();
                        resultado = FuncAbrirRepositorio(ida, gestorBiblioteca, nombreFichero);

                        if (resultado == -1)
                        {
                            Console.WriteLine("\n\nError, el id de administrador es incorrecto");
                            Pausa();
                        }
                        else if (resultado == -2)
                        {
                            Console.WriteLine("\n\nError, ya existe un repositorio cargado con el mismo nombre");
                            Pausa();
                        }
                        else if (resultado == 0)
                        {
                            Console.WriteLine("\n\nNo se ha podido abrir el repositorio");
                            Pausa();
                        }
                        else if (resultado == 1)
                        {
                            Console.WriteLine("\n\n**El repositorio ha sido cargado correctamente**");
                            Pausa();
                        }
                        break;

                    case 2:
                        ObtenerListaRepositorios(ida, gestorBiblioteca, true);
                        Console.WriteLine("Elige repositorio: ");
                        opcion = Convert.ToInt32(Console.ReadLine());
                        resultado = FuncGuardarRepositorio(ida, gestorBiblioteca, opcion);

                        if (resultado == -1)
                        {
                            Console.WriteLine("\n\nError, el id de administrador es incorrecto");
                            Pausa();
                        }
                        else if (resultado == -2)
                        {
                            Console.WriteLine("\n\nError, la posicion introducida es incorrecta");
                            Pausa();
                        }
                        else if (resultado == 0)
                        {
                            Console.WriteLine("\n\nError, no se ha podido guardar el repositorio");
                            Pausa();
                        }
                        else if (resultado == 1)
                        {
                            Console.WriteLine("\n\n*** Se ha guardado el/los repositorios seleccionados de la biblioteca.**");
                            Pausa();
                        }

                        break;
                    /*
                        case 3:
                            Console.WriteLine("Introduce el Isbn: ");
                            isbn = teclado.nextLine();
                            Console.WriteLine("Introduce el Autor: ");
                            autor = teclado.nextLine();
                            Console.WriteLine("Introduce el Titulo: ");
                            titulo = teclado.nextLine();
                            Console.WriteLine("Introduce el Anio: ");
                            anio = teclado.nextInt();
                            teclado.nextLine(); // Limpia el buffer
                            Console.WriteLine("Introduce el Pais: ");
                            pais = teclado.nextLine();
                            Console.WriteLine("Introduce el Idioma: ");
                            idioma = teclado.nextLine();
                            Console.WriteLine("Introduce el numero inicial de libros: ");
                            numLibros = teclado.nextInt();
                            teclado.nextLine(); // Limpia el buffer
                            libro = new TLibro(titulo, autor, pais, idioma, isbn, anio, numLibros, 0, 0);
                            obtenerListaRepositorios(ida, gestorBiblioteca, false);
                            Console.WriteLine("Elige repositorio: ");
                            opcion = teclado.nextInt();

                            resultado = funcNuevoLibro(ida, gestorBiblioteca, libro, opcion);
                            teclado.nextLine(); //Limpia el buffer

                            if (resultado == -1)
                            {
                                Console.WriteLine("\n\nError, el id de administrador es incorrecto");
                                Console.WriteLine("\nPresiona una tecla para continuar...");
                                teclado.nextLine();
                            }
                            else if (resultado == -2)
                            {
                                Console.WriteLine("\n\nError, la posicion introducida es incorrecta");
                                Console.WriteLine("\nPresiona una tecla para continuar...");
                                teclado.nextLine();
                            }
                            else if (resultado == 0)
                            {
                                Console.WriteLine("\n\nError, ya hay un libro con el mismo isbn en el repositorio indicado");
                                Console.WriteLine("\nPresiona una tecla para continuar...");
                                teclado.nextLine();
                            }
                            else if (resultado == 1)
                            {
                                Console.WriteLine("\n\n*** El libro ha sido añadido correctamente.**");
                                Console.WriteLine("\nPresiona una tecla para continuar...");
                                teclado.nextLine();
                            }
                            break;

                        case 4:
                            Console.WriteLine("Introduce ISBN a buscar: ");
                            isbn = teclado.nextLine();
                            resultado = funcBuscar(ida, gestorBiblioteca, isbn);

                            if (resultado == -2)
                            {
                                Console.WriteLine("\n\nError, el id de administrador es incorrecto");
                                Console.WriteLine("\nPresiona una tecla para continuar...");
                                teclado.nextLine();

                            }
                            else if (resultado == -1)
                            {
                                Console.WriteLine("\n\nError, no se ha encontrado ningun libro con el isbn indicado");
                                Console.WriteLine("\nPresiona una tecla para continuar...");
                                teclado.nextLine();

                            }
                            else if (resultado != -3)
                            {
                                libro = funcDescargar(ida, gestorBiblioteca, -1, resultado);
                                Mostrar(resultado, true, libro);

                                do
                                {
                                    System.out.print("\n¿Es este el libro del que deseas comprar mas unidades (s/n)? ");
                                    siNo = teclado.next().charAt(0);
                                    siNo = Character.toLowerCase(siNo);
                                    teclado.nextLine(); //Limpia el buffer

                                    if (siNo != 's' && siNo != 'n')
                                    {
                                        Console.WriteLine("Error, debes seleccionar s o n");
                                    }
                                } while (siNo != 's' && siNo != 'n');

                                if (siNo == 's')
                                {
                                    System.out.print("Introduce el numero de libros comprados: ");
                                    numLibros = teclado.nextInt();
                                    resultado = funcComprar(ida, gestorBiblioteca, isbn, numLibros);
                                    teclado.nextLine(); //Limpia el buffer

                                    if (resultado == -1)
                                    {
                                        Console.WriteLine("\n\nError, el id de administrador es incorrecto");
                                        Console.WriteLine("\nPresiona una tecla para continuar...");
                                        teclado.nextLine();

                                    }
                                    else if (resultado == 0)
                                    {
                                        Console.WriteLine("\n\nError, no se ha encontrado ningun libro con el isbn indicado");
                                        Console.WriteLine("\nPresiona una tecla para continuar...");
                                        teclado.nextLine();

                                    }
                                    else if (resultado == 1)
                                    {
                                        Console.WriteLine("\n\n*** Se han añadido los nuevos libros.**");
                                        Console.WriteLine("\nPresiona una tecla para continuar...");
                                        teclado.nextLine();
                                    }
                                }
                            }

                            break;

                        case 5:
                            Console.WriteLine("Introduce ISBN a buscar: ");
                            isbn = teclado.nextLine();
                            resultado = funcBuscar(ida, gestorBiblioteca, isbn);

                            if (resultado == -2)
                            {
                                Console.WriteLine("\n\nError, el id de administrador es incorrecto");
                                Console.WriteLine("\nPresiona una tecla para continuar...");
                                teclado.nextLine();

                            }
                            else if (resultado == -1)
                            {
                                Console.WriteLine("\n\nError, no se ha encontrado ningun libro con el isbn indicado");
                                Console.WriteLine("\nPresiona una tecla para continuar...");
                                teclado.nextLine();

                            }
                            else if (resultado != -3)
                            {
                                libro = funcDescargar(ida, gestorBiblioteca, -1, resultado);
                                Mostrar(resultado, true, libro);

                                do
                                {
                                    System.out.print("\n¿Es este el libro del que deseas retirar unidades (s/n)? ");
                                    siNo = teclado.next().charAt(0);
                                    siNo = Character.toLowerCase(siNo);
                                    teclado.nextLine(); //Limpia el buffer

                                    if (siNo != 's' && siNo != 'n')
                                    {
                                        Console.WriteLine("Error, debes seleccionar s o n");
                                    }
                                } while (siNo != 's' && siNo != 'n');

                                if (siNo == 's')
                                {
                                    System.out.print("Introduce el numero de libros a retirar: ");
                                    numLibros = teclado.nextInt();
                                    resultado = funcRetirar(ida, gestorBiblioteca, isbn, numLibros);
                                    teclado.nextLine(); //Limpia el buffer

                                    if (resultado == -1)
                                    {
                                        Console.WriteLine("\n\nError, el id de administrador es incorrecto");
                                        Console.WriteLine("\nPresiona una tecla para continuar...");
                                        teclado.nextLine();

                                    }
                                    else if (resultado == 0)
                                    {
                                        Console.WriteLine("\n\nError, no se ha encontrado ningun libro con el isbn indicado");
                                        Console.WriteLine("\nPresiona una tecla para continuar...");
                                        teclado.nextLine();

                                    }
                                    else if (resultado == 2)
                                    {
                                        Console.WriteLine("\n\nError, hay menos libros de los que se ha solicitado retirar");
                                        Console.WriteLine("\nPresiona una tecla para continuar...");
                                        teclado.nextLine();

                                    }
                                    else if (resultado == 1)
                                    {
                                        Console.WriteLine("\n\n*** Se ha retirado el numero de libros indicados.**");
                                        Console.WriteLine("\nPresiona una tecla para continuar...");
                                        teclado.nextLine();
                                    }
                                }
                            }

                            break;
                    */
                    case 6:
                        Console.WriteLine("\nCódigo de Ordenación");
                        Console.WriteLine("0.- Por ISBN");
                        Console.WriteLine("1.- Por Título");
                        Console.WriteLine("2.- Por Autor");
                        Console.WriteLine("3.- Por Año");
                        Console.WriteLine("4.- Por País");
                        Console.WriteLine("5.- Por Idioma");
                        Console.WriteLine("6.- Por nº de libros disponibles");
                        Console.WriteLine("7.- Por nº de libros prestados");
                        Console.WriteLine("8.- Por nº de libros en espera");

                        Console.WriteLine("\nIntroduce código:");
                        opcion = Convert.ToInt32(Console.ReadLine());
                        res = FuncOrdenar(ida, gestorBiblioteca, opcion);

                        if (res)
                        {
                            Console.WriteLine("\n*** La biblioteca ha sido ordenada correctamente.**");
                            Pausa();
                        }
                        else
                        {
                            Console.WriteLine("\n\nError, no se ha podido ordenar la biblioteca");
                            Pausa();
                        }

                        break;
                    /*
                        case 7:
                            filtrarLibros(ida, gestorBiblioteca, true);
                            break;
                    */
                    case 8:
                        numLibros = FuncNLibros(-1, gestorBiblioteca);

                        for (int i = 0; i < numLibros; i++)
                        {
                            libro = FuncDescargar(ida, gestorBiblioteca, -1, i);

                            if (i == 0)
                            {
                                Mostrar(i, true, libro);
                            }
                            else
                            {
                                Mostrar(i, false, libro);
                            }
                        }
                        Pausa();

                        break;


                    case 0:

                        desconexion = FuncDesconexion(ida, gestorBiblioteca);
                        if (desconexion == true)
                        {
                            Console.WriteLine("\nSaliendo del menu de administracion\n");

                        }
                        else
                        {
                            Console.WriteLine("\nError en la desconexion\n");
                        }

                        break;
                }
            } while (opcionMenuAdministracion != 0 && desconexion != true);
        }

        private void GestionPrincipal(GestorBiblioteca gestorBiblioteca)
        {
            int opcionMenuPrincipal;
            String contraseña;
            int resultado, num;
            bool res;
            char siNo;

            do
            {
                opcionMenuPrincipal = MenuPrincipal();
                switch (opcionMenuPrincipal)
                {
                    case 1:

                        Console.WriteLine("");
                        Console.WriteLine("Por favor inserte la contraseña de administración: ");
                        contraseña = Console.ReadLine();
                        resultado = FuncConexion(contraseña, gestorBiblioteca);

                        if (resultado == -1)
                        {
                            Console.WriteLine("\nError, ya hay un usuario identificado como administrador, solo se permite uno");
                            Pausa();

                        }
                        else if (resultado == -2)
                        {
                            Console.WriteLine("\nError, la contraseña es incorrecta\n");
                            Pausa();

                        }
                        else if (resultado != 0 && resultado != -1 && resultado != -2)
                        {
                            Console.WriteLine("\n*** Contraseña correcta, puede acceder al menú de administración ***\n");
                            Pausa();
                            GestionAdministracion(resultado, gestorBiblioteca);
                        }

                        break;

                    /*  case 2:
                          filtrarLibros(-2, gestorBiblioteca, false);

                          break;

                      case 3:
                          res = filtrarLibros(-2, gestorBiblioteca, false);

                          if (res)
                          {
                              do
                              {
                                  Console.WriteLine("¿Quiere sacar un libro de la biblioteca (s/n)? ");
                                  siNo = teclado.next().charAt(0);
                                  siNo = Character.toLowerCase(siNo);
                                  // Limpiar el buffer
                                  teclado.nextLine();

                                  if (siNo != 's' && siNo != 'n')
                                  {
                                      Console.WriteLine("Error, debes seleccionar s o n");
                                  }
                              } while (siNo != 's' && siNo != 'n');

                              if (siNo == 's')
                              {

                                  Console.WriteLine("Introduce la posicion del libro a solicitar su prestamo: ");
                                  num = teclado.nextInt();
                                  // Limpiar el buffer
                                  teclado.nextLine();
                                  resultado = funcPrestar(num - 1, gestorBiblioteca);

                                  if (resultado == -1)
                                  {
                                      Console.WriteLine("Error, la posicion introducida no esta dentro de los limites del repositorio mezclado y ordenado\n");
                                      Console.WriteLine("\nPresiona una tecla para continuar...\n");
                                      teclado.nextLine();

                                  }
                                  else if (resultado == 1)
                                  {
                                      Console.WriteLine("*** El préstamo se ha concedido, recoge el libro en el mostrador.**\n");
                                      Console.WriteLine("\nPresiona una tecla para continuar...\n");
                                      teclado.nextLine();

                                  }
                                  else if (resultado == 0)
                                  {
                                      Console.WriteLine("** Se le ha puesto en lista de espera \n**");
                                      Console.WriteLine("\nPresiona una tecla para continuar...\n");
                                      teclado.nextLine();
                                  }
                              }
                          }

                          break;

                      case 4:
                          res = filtrarLibros(-2, gestorBiblioteca, false);

                          if (res)
                          {
                              do
                              {
                                  Console.WriteLine("¿Quiere devolver un libro de la biblioteca (s/n)? ");
                                  siNo = teclado.next().charAt(0);
                                  siNo = Character.toLowerCase(siNo);
                                  // Limpiar el buffer
                                  teclado.nextLine();

                                  if (siNo != 's' && siNo != 'n')
                                  {
                                      Console.WriteLine("Error, debes seleccionar s o n");
                                  }
                              } while (siNo != 's' && siNo != 'n');

                              if (siNo == 's')
                              {

                                  Console.WriteLine("Introduce la posicion del libro a devolver: ");
                                  num = teclado.nextInt();
                                  // Limpiar el buffer
                                  teclado.nextLine();
                                  resultado = funcDevolver(num - 1, gestorBiblioteca);

                                  if (resultado == -1)
                                  {
                                      Console.WriteLine("Error, la posicion introducida no esta dentro de los limites del repositorio mezclado y ordenado\n");
                                      Console.WriteLine("\nPresiona una tecla para continuar...\n");
                                      teclado.nextLine();

                                  }
                                  else if (resultado == 1)
                                  {
                                      Console.WriteLine("*** Se ha devuelto el libro y se pondra en la estanteria**\n");
                                      Console.WriteLine("\nPresiona una tecla para continuar...\n");
                                      teclado.nextLine();

                                  }
                                  else if (resultado == 0)
                                  {
                                      Console.WriteLine("** Se ha devuelto el libro y se ha reducido el numero de usuarios en espera **\n");
                                      Console.WriteLine("\nPresiona una tecla para continuar...\n");
                                      teclado.nextLine();

                                  }
                                  else if (resultado == 2)
                                  {
                                      Console.WriteLine("Error, el libro no se puede devolver porque no hay usuarios en lista de espera ni libros prestados\n");
                                      Console.WriteLine("\nPresiona una tecla para continuar...\n");
                                      teclado.nextLine();
                                  }
                              }
                          }

                          break;
                    */
                    case 0:
                        Console.WriteLine("\nSaliendo del menu principal");
                        Pausa();

                        break;
                }
            } while (opcionMenuPrincipal != 0);
        }

        static void Main(string[] args)
        {
            string host;
            int puerto;
            bool parsingResult = false;
            Cliente c = new Cliente();

            Console.WriteLine("Indica el host al que quiere conectarse:");
            host = Console.ReadLine();

            do
            {
                Console.WriteLine("\nIndica el puerto del host:");
                parsingResult = Int32.TryParse(Console.ReadLine(), out puerto);

                if (!parsingResult)
                {
                    Console.WriteLine("\nPor favor, indique un puerto válido.");
                }
            } while (!parsingResult);

            try
            {
                ChannelServices.RegisterChannel(new TcpChannel(), false);
                GestorBiblioteca gestorBiblioteca = (GestorBiblioteca)Activator.GetObject(typeof(GestorBiblioteca),
                "tcp://" + host + ":" + puerto + "/GestorBiblioteca");

                c.GestionPrincipal(gestorBiblioteca);

            }
            catch (Exception ex)
            {
                Console.WriteLine("Error: " + ex);
                c.Pausa();
            }
        }
    }
}
