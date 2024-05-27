using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServicioBiblioteca
{
    
    public class GestorBiblioteca : MarshalByRefObject, GestorBibliotecaIntf
    {
        public int AbrirRepositorio(int pIda, string pNomFichero)
        {
            throw new NotImplementedException();
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
            throw new NotImplementedException();
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
            throw new NotImplementedException();
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
