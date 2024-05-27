using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServicioBiblioteca
{
    [Serializable]
    class TLibro
    {
        public String titulo;
        public String autor;
        public String pais;
        public String idioma;
        public String isbn;
        public int anio;
        public int disponibles;
        public int prestados;
        public int reservados;

        public TLibro()
        {
        }

        public TLibro(String titulo, String autor, String pais, String idioma, String isbn, int anio, int disponibles, int prestados, int reservados)
        {
            this.titulo = titulo;
            this.autor = autor;
            this.pais = pais;
            this.idioma = idioma;
            this.isbn = isbn;
            this.anio = anio;
            this.disponibles = disponibles;
            this.prestados = prestados;
            this.reservados = reservados;
        }

        public string Titulo
        {
            get { return this.titulo; }
            set { this.titulo = value; }
        }

        public string Autor
        {
            get { return this.autor; }
            set { this.autor = value; }
        }

        public string Pais
        {
            get { return this.pais; }
            set { this.pais = value; }
        }

        public string Idioma
        {
            get { return this.idioma; }
            set { this.idioma = value; }
        }

        public string Isbn
        {
            get { return this.isbn; }
            set { this.isbn = value; }
        }

        public int Anio
        {
            get { return this.anio; }
            set { this.anio = value; }
        }

        public int Disponibles
        {
            get { return this.disponibles; }
            set { this.disponibles = value; }
        }

        public int Prestados
        {
            get { return this.prestados; }
            set { this.prestados = value; }
        }

        public int Reservados
        {
            get { return this.reservados; }
            set { this.reservados = value; }
        }

        public override int GetHashCode()
        {
            int hash = 3;
            hash = 97 * hash + (titulo?.GetHashCode() ?? 0);
            hash = 97 * hash + (autor?.GetHashCode() ?? 0);
            hash = 97 * hash + (pais?.GetHashCode() ?? 0);
            hash = 97 * hash + (idioma?.GetHashCode() ?? 0);
            hash = 97 * hash + (isbn?.GetHashCode() ?? 0);
            hash = 97 * hash + anio.GetHashCode();
            hash = 97 * hash + disponibles.GetHashCode();
            hash = 97 * hash + prestados.GetHashCode();
            hash = 97 * hash + reservados.GetHashCode();
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

            TLibro other = (TLibro)obj;

            if (this.anio != other.anio)
            {
                return false;
            }
            if (this.disponibles != other.disponibles)
            {
                return false;
            }
            if (this.prestados != other.prestados)
            {
                return false;
            }
            if (this.reservados != other.reservados)
            {
                return false;
            }
            if (!Equals(this.titulo, other.titulo))
            {
                return false;
            }
            if (!Equals(this.autor, other.autor))
            {
                return false;
            }
            if (!Equals(this.pais, other.pais))
            {
                return false;
            }
            if (!Equals(this.idioma, other.idioma))
            {
                return false;
            }
            return Equals(this.isbn, other.isbn);
        }

        public override string ToString()
        {
            string s = "{" + this.titulo + ", " + this.autor + ", " + this.anio + ", "
                + this.pais + ", " + this.idioma + ", " + this.isbn + ", " + this.disponibles
                + ", " + this.prestados + ", " + this.reservados + "}";

            return s;
        }
    }
}
