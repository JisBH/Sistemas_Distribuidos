using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServicioBiblioteca
{
    [Serializable]
    public class ListRepositorioLibro : RepositorioLibroIntf
    {
        private List<TLibro> libros = new List<TLibro>();


        public void AniadirLibro(TLibro libro)
        {

            this.libros.Add(libro);
        }


        public void EliminarLibro(TLibro libro)
        {
            this.libros.Remove(libro);
        }


        public TLibro GetLibroPorIsbn(String isbn)
        {

            foreach (TLibro libro in libros)
            {
                if (libro.isbn.Equals(isbn))
                {
                    return libro;
                }
            }
            return null;
        }


        public TLibro GetLibro(int pos)
        {
            return libros.ElementAt(pos);
        }


        public int NumLibros()
        {
            return libros.Capacity;
        }


        public List<TLibro> GetTodosLibros()
        {
            return libros;
        }
    }
}
