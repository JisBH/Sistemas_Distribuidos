using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServicioWebBiblioteca
{

    public class ComparadorLibro : IComparer<TLibro>
    {
        private int campoOrdenacion;

        public ComparadorLibro(int campoOrdenacion)
        {
            this.campoOrdenacion = campoOrdenacion;
        }

        public int Compare(TLibro x, TLibro y)
        {
            int C = 0;
            switch (this.campoOrdenacion)
            {
                case 0:
                    C = x.Isbn.CompareTo(y.Isbn);
                    break;
                case 1:
                    C = x.Titulo.CompareTo(y.Titulo);
                    break;
                case 2:
                    C = x.Autor.CompareTo(y.Autor);
                    break;
                case 3:
                    C = x.Anio.CompareTo(y.Anio);
                    break;
                case 4:
                    C = x.Pais.CompareTo(y.Pais);
                    break;
                case 5:
                    C = x.Idioma.CompareTo(y.Idioma);
                    break;
                case 6:
                    C = x.Disponibles.CompareTo(y.Disponibles);
                    break;
                case 7:
                    C = x.Prestados.CompareTo(y.Prestados);
                    break;
                case 8:
                    C = x.Reservados.CompareTo(y.Reservados);
                    break;
            }
            return C;
        }
    }
}
