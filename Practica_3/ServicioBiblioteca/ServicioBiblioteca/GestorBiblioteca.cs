using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServicioBiblioteca
{

    public class GestorBiblioteca : MarshalByRefObject, GestorBibliotecaIntf
    {
        private int numAdministradores = 0; // Contador con el numero de administradores actualmente en el sistema
        private int idAdmin = -1;    // Copia del Identificador de Administración enviado al usuario.
        private List<TDatosRepositorio> repositoriosCargados = new List<TDatosRepositorio>(); // Lista con los repositorios cargados en memoria actualmente
        private List<TLibro> librosTodosRepositorios = new List<TLibro>(); // Lista con los libros de todos los repositorios cargados en memoria actualmente
        private int campoOrdenacion = 0;

        public int AbrirRepositorio(int pIda, string pNomFichero)
        {


            if (pIda != this.idAdmin || this.numAdministradores > 1)
            {
                return -1;

            }

            try
            {

                using (FileStream fs = File.OpenRead(pNomFichero))
                using (BinaryReader binaryReader = new BinaryReader(fs))
                {

                    int numeroLibros = binaryReader.ReadInt32();
                    String nombreRepositorio = binaryReader.ReadString();
                    String direccionRepositorio = binaryReader.ReadString();

                    RepositorioLibroIntf repositorioLibro = new ListRepositorioLibro();
                    TDatosRepositorio datosRepositorio = new TDatosRepositorio(numeroLibros, nombreRepositorio, direccionRepositorio, repositorioLibro, pNomFichero);

                    if (this.repositoriosCargados.Contains(datosRepositorio))
                    {
                        return -2;
                    }


                    for (int i = 0; i < numeroLibros; i++)
                    {
                        String isbn = binaryReader.ReadString();
                        String titulo = binaryReader.ReadString();
                        String autor = binaryReader.ReadString();
                        int anio = binaryReader.ReadInt32();
                        String pais = binaryReader.ReadString();
                        String idioma = binaryReader.ReadString();
                        int disponible = binaryReader.ReadInt32();
                        int prestado = binaryReader.ReadInt32();
                        int reservado = binaryReader.ReadInt32();

                        TLibro libro = new TLibro(titulo, autor, pais, idioma, isbn, anio, disponible, prestado, reservado);
                        datosRepositorio.RepositorioLibro.AniadirLibro(libro);
                        this.librosTodosRepositorios.Add(libro);
                    }


                    this.repositoriosCargados.Add(datosRepositorio);
                    Ordenar(pIda, this.campoOrdenacion);
                }

            }
            catch (Exception ex)
            {
                Console.WriteLine("Error: " + ex);
                return 0;
            }


            return 1;
        }

        public int Buscar(int pIda, string pIsbn)
        {
            if (pIda != this.idAdmin)
            {
                return -2;
            }

            for (int i = 0; i < this.librosTodosRepositorios.Count; i++)
            {
                TLibro libro = this.librosTodosRepositorios.ElementAt(i);

                if (pIsbn.Equals(libro.Isbn))
                {
                    return i;
                }
            }

            return -1;
        }

        public int Comprar(int pIda, string pIsbn, int pNoLibros)
        {
            if (pIda != this.idAdmin)
            {
                return -1;
            }

            int posLibro = Buscar(pIda, pIsbn);

            if (posLibro == -1)
            {
                return 0;
            }

            TLibro libro = this.librosTodosRepositorios.ElementAt(posLibro);
            libro.Disponibles += pNoLibros;

            if (libro.Disponibles >= libro.Reservados)
            {
                libro.Disponibles -= libro.Reservados;
                libro.Prestados += libro.Reservados;
                libro.Reservados = 0;
            }
            else
            {
                libro.Reservados -= libro.Disponibles;
                libro.Prestados += libro.Disponibles;
                libro.Disponibles = 0;
            }

            //Codigo para ordenar el repositorio que contiene el libro
            int i = 0;
            bool encontrado = false;

            while (i < this.repositoriosCargados.Count && !encontrado)
            {
                TDatosRepositorio datosRepositorio = this.repositoriosCargados.ElementAt(i);

                if (datosRepositorio.RepositorioLibro.GetLibroPorIsbn(pIsbn) != null)
                {
                    encontrado = true;

                }
                else
                {
                    i++;
                }
            }
            // i tiene el repositorio en el que esta el libro
            // Ordenamos el repositorio
            IComparer<TLibro> comp = new ComparadorLibro(this.campoOrdenacion);
            this.repositoriosCargados.ElementAt(i).RepositorioLibro.GetTodosLibros().Sort(comp);

            return 1;
        }

        public int Conexion(string pPasswd)
        {
            int result;

            // Si ya tengo un administrador, devuelvo -1
            if (this.numAdministradores == 1)
            {
                return -1;
            }

            // Si aún no tengo ningún administrador y la contraseña es correcta, devuelvo un número aleatorio
            else if (pPasswd.Equals("1234"))
            {
                this.numAdministradores++;
                Random random = new Random();
                result = random.Next(1, 1000001);
                this.idAdmin = result;
            }

            // Si aún no tengo ningún administrador y la contraseña es incorrecta, devuelvo -2
            else
            {
                return -2;
            }

            return result;
        }

        public TDatosRepositorio DatosRepositorio(int pIda, int pRepo)
        {
            if (pIda != this.idAdmin || this.numAdministradores > 1)
            {
                return null;
            }
            if (pRepo < 0 || pRepo >= this.repositoriosCargados.Count)
            {
                return null;
            }

            return this.repositoriosCargados.ElementAt(pRepo);
        }

        public TLibro Descargar(int pIda, int pRepo, int pPos)
        {
            TLibro libro, copiaLibro;

            if (pRepo == -1)
            {
                if (pPos >= this.librosTodosRepositorios.Count || pPos < 0)
                {
                    return null;
                }

                libro = (TLibro)this.librosTodosRepositorios.ElementAt(pPos);
                copiaLibro = new TLibro(libro.titulo, libro.autor, libro.pais, libro.idioma, libro.isbn, libro.anio, libro.disponibles, libro.prestados, libro.reservados);

                if (pIda != this.idAdmin)
                {
                    copiaLibro.Reservados = 0;
                    copiaLibro.Prestados = 0;
                }

                return copiaLibro;
            }

            if (pRepo >= this.repositoriosCargados.Count || pRepo < 0)

            {
                return null;
            }

            TDatosRepositorio datosRepositorio = this.repositoriosCargados.ElementAt(pRepo);

            List<TLibro> librosRepositorios = datosRepositorio.RepositorioLibro.GetTodosLibros();

            if (pPos < 0 || pPos >= librosRepositorios.Count)
            {
                return null;
            }

            libro = (TLibro)librosRepositorios.ElementAt(pPos);
            copiaLibro = new TLibro(libro.titulo, libro.autor, libro.pais, libro.idioma, libro.isbn, libro.anio, libro.disponibles, libro.prestados, libro.reservados);

            if (pIda != this.idAdmin)
            {
                copiaLibro.Reservados = 0;
                copiaLibro.Prestados = 0;
            }

            return copiaLibro;
        }

        public bool Desconexion(int pIda)
        {
            if (this.idAdmin == pIda)
            {
                this.idAdmin = -1;
                this.numAdministradores--;
            }
            else
            {
                return false;
            }
            return true;
        }

        public int Devolver(int pPos)
        {
            throw new NotImplementedException();
        }

        public int GuardarRepositorio(int pIda, int pRepo)
        {
            if (this.repositoriosCargados.Count == 0)
            {
                return -2;
            }

            if (pIda != this.idAdmin || this.numAdministradores > 1)
            {
                return -1;
            }

            if (pRepo < -1 || pRepo >= this.repositoriosCargados.Count)
            {
                return -2;
            }

            try
            {
                if (pRepo != -1)
                {
                    TDatosRepositorio datosRepositorio = this.repositoriosCargados.ElementAt(pRepo);
                    using (FileStream fs = File.OpenWrite(datosRepositorio.RutaRepositorio))
                    using (BinaryWriter binaryWriter = new BinaryWriter(fs))
                    {




                        binaryWriter.Write(datosRepositorio.NumeroLibros);
                        binaryWriter.Write(datosRepositorio.NombreRepositorio);
                        binaryWriter.Write(datosRepositorio.DireccionRepositorio);

                        for (int i = 0; i < datosRepositorio.NumeroLibros; i++)
                        {
                            TLibro libro = datosRepositorio.RepositorioLibro.GetLibro(i);

                            binaryWriter.Write(libro.isbn);
                            binaryWriter.Write(libro.titulo);
                            binaryWriter.Write(libro.autor);
                            binaryWriter.Write(libro.anio);
                            binaryWriter.Write(libro.pais);
                            binaryWriter.Write(libro.idioma);
                            binaryWriter.Write(libro.disponibles);
                            binaryWriter.Write(libro.prestados);
                            binaryWriter.Write(libro.reservados);
                        }
                    }
                }

                else
                {
                    for (int i = 0; i < this.repositoriosCargados.Count; i++)
                    {
                        TDatosRepositorio datosRepositorio = this.repositoriosCargados.ElementAt(i);
                        using (FileStream fs = File.OpenWrite(datosRepositorio.RutaRepositorio))
                        using (BinaryWriter binaryWriter = new BinaryWriter(fs))
                        {

                            binaryWriter.Write(datosRepositorio.NumeroLibros);
                            binaryWriter.Write(datosRepositorio.NombreRepositorio);
                            binaryWriter.Write(datosRepositorio.DireccionRepositorio);

                            for (int j = 0; j < datosRepositorio.NumeroLibros; j++)
                            {
                                TLibro libro = datosRepositorio.RepositorioLibro.GetLibro(j);

                                binaryWriter.Write(libro.isbn);
                                binaryWriter.Write(libro.titulo);
                                binaryWriter.Write(libro.autor);
                                binaryWriter.Write(libro.anio);
                                binaryWriter.Write(libro.pais);
                                binaryWriter.Write(libro.idioma);
                                binaryWriter.Write(libro.disponibles);
                                binaryWriter.Write(libro.prestados);
                                binaryWriter.Write(libro.reservados);
                            }
                        }
                    }
                }

            }
            catch (Exception ex)
            {
                Console.WriteLine("Error: " + ex);
                return 0;

            }

            return 1;
        }

        public int NLibros(int pRepo)
        {
            if (pRepo == -1)
            {
                return this.librosTodosRepositorios.Count;
            }

            if (pRepo >= this.repositoriosCargados.Count || pRepo < 0)
            {
                return -1;
            }
            return this.repositoriosCargados.ElementAt(pRepo).RepositorioLibro.NumLibros();
        }

        public int NRepositorios(int pIda)
        {
            if (pIda != this.idAdmin || this.numAdministradores > 1)
            {
                return -1;
            }

            return this.repositoriosCargados.Count;
        }

        public int NuevoLibro(int pIda, TLibro L, int pRepo)
        {
            if (pIda != this.idAdmin || this.numAdministradores > 1)
            {
                return -1;
            }

            if (pRepo < 0 || pRepo >= this.repositoriosCargados.Count)
            {
                return -2;
            }

            TDatosRepositorio datosRepositorio;

            for (int i = 0; i < this.repositoriosCargados.Count; i++)
            {
                datosRepositorio = this.repositoriosCargados.ElementAt(i);
                if (datosRepositorio.RepositorioLibro.GetLibroPorIsbn(L.Isbn) != null)
                {
                    return 0;
                }
            }

            datosRepositorio = this.repositoriosCargados.ElementAt(pRepo);

            datosRepositorio.RepositorioLibro.AniadirLibro(L);
            datosRepositorio.NumeroLibros++;
            this.librosTodosRepositorios.Add(L);
            IComparer<TLibro> comp = new ComparadorLibro(this.campoOrdenacion);
            datosRepositorio.RepositorioLibro.GetTodosLibros().Sort(comp);

            return 1;
        }

        public bool Ordenar(int pIda, int pCampo)
        {
            if (pIda != this.idAdmin)
            {
                return false;
            }

            if (pCampo < 0 || pCampo > 8)
            {
                return false;
            }

            IComparer<TLibro> comp = new ComparadorLibro(pCampo);

            this.librosTodosRepositorios.Sort(comp);

            foreach (TDatosRepositorio repositorio in this.repositoriosCargados)
            {
                repositorio.RepositorioLibro.GetTodosLibros().Sort(comp);
            }

            this.campoOrdenacion = pCampo;

            return true;
        }

        public int Prestar(int pPos)
        {
            throw new NotImplementedException();
        }

        public int Retirar(int pIda, string pIsbn, int pNoLibros)
        {
            throw new NotImplementedException();
        }
    }
}
