using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServicioBiblioteca
{
    [Serializable]
    public class TDatosRepositorio
    {
        private int numeroLibros;
        private String nombreRepositorio;
        private String direccionRepositorio;
        private String rutaRepositorio;
        private RepositorioLibroIntf repositorioLibro;

        public TDatosRepositorio()
        {
        }

        public TDatosRepositorio(int numeroLibros, String nombreRepositorio, String direccionRepositorio, RepositorioLibroIntf repositorioLibro, String rutaRepositorio)
        {
            this.numeroLibros = numeroLibros;
            this.nombreRepositorio = nombreRepositorio;
            this.direccionRepositorio = direccionRepositorio;
            this.repositorioLibro = repositorioLibro;
            this.rutaRepositorio = rutaRepositorio;
        }
        public string NombreRepositorio
        {
            get { return this.nombreRepositorio; }
            set { this.nombreRepositorio = value; }
        }

        public string DireccionRepositorio
        {
            get { return this.direccionRepositorio; }
            set { this.direccionRepositorio = value; }
        }

        public int NumeroLibros
        {
            get { return this.numeroLibros; }
            set { this.numeroLibros = value; }
        }

        public RepositorioLibroIntf RepositorioLibro
        {
            get { return this.repositorioLibro; }
            set { this.repositorioLibro = value; }
        }

        public String RutaRepositorio
        {
            get { return this.rutaRepositorio; }
            set { this.rutaRepositorio = value; }
        }


        public override int GetHashCode()
        {
            int hash = 7;
            hash = 89 * hash + this.numeroLibros.GetHashCode();
            hash = 89 * hash + (this.nombreRepositorio?.GetHashCode() ?? 0);
            hash = 89 * hash + (this.direccionRepositorio?.GetHashCode() ?? 0);
            return hash;
        }


        public override bool Equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (GetType() != obj.GetType())
            {
                return false;
            }

            TDatosRepositorio other = (TDatosRepositorio)obj;

            if (this.numeroLibros != other.numeroLibros)
            {
                return false;
            }
            if (!Equals(this.nombreRepositorio, other.nombreRepositorio))
            {
                return false;
            }
            return Equals(this.direccionRepositorio, other.direccionRepositorio);
        }
    }
}
