using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServicioBiblioteca
{
    interface RepositorioLibroIntf
    {
        void AniadirLibro(TLibro libro);

        void EliminarLibro(TLibro libro);

        TLibro GetLibroPorIsbn(String isbn);

        TLibro GetLibro(int pos);

        int NumLibros();

        List<TLibro> GetTodosLibros();
    }
}
