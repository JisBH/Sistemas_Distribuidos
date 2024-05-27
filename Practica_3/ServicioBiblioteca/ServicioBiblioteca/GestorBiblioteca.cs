using System;
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
            FileStream fs = null;
            BinaryReader binaryReader = null;

            try
            {

                fs = File.OpenRead(pNomFichero);
                binaryReader = new BinaryReader(fs);

                int numeroLibros = binaryReader.ReadInt32();
                String nombreRepositorio = binaryReader.ReadString();
                String direccionRepositorio = binaryReader.ReadString();

                RepositorioLibroIntf repositorioLibro = new ListRepositorioLibro();
                TDatosRepositorio datosRepositorio = new TDatosRepositorio(numeroLibros, nombreRepositorio, direccionRepositorio, repositorioLibro);

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
                // Ordenar(pIda, this.campoOrdenacion);

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
            throw new NotImplementedException();
        }

        public int Comprar(int pIda, string pIsbn, int pNoLibros)
        {
            throw new NotImplementedException();
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
            throw new NotImplementedException();
        }

        public TLibro Descargar(int pIda, int pRepo, int pPos)
        {
            throw new NotImplementedException();
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
            throw new NotImplementedException();
        }

        public int NLibros(int pRepo)
        {
            throw new NotImplementedException();
        }

        public int NRepositorios(int pIda)
        {
            throw new NotImplementedException();
        }

        public int NuevoLibro(int pIda, TLibro L, int pRepo)
        {
            throw new NotImplementedException();
        }

        public bool Ordenar(int pIda, int pCampo)
        {
            throw new NotImplementedException();
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
