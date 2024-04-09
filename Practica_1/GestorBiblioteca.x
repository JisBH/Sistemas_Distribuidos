typedef char Cadena[150];

struct TLibro
{
   Cadena Isbn;
   Cadena Titulo;
   Cadena Autor;
   int Anio;
   Cadena Pais;
   Cadena Idioma;
   int NoLibros;
   int NoPrestados;
   int NoListaEspera;
};

struct TNuevo
{
   int Ida;
   TLibro Libro;
};

struct TComRet
{
   int Ida;
   Cadena Isbn;
   int NoLibros;
};

struct TConsulta
{
   int Ida;
   Cadena Datos;
};

struct TPosicion
{
   int Ida;
   int Pos;
};

struct TOrdenacion
{
   int Ida;
   int Campo;

};
program GESTORBIBLIOTECA {
	version GESTORBIBLIOTECA_VER {
    	int Conexion(Cadena pPasswd)=1;
    	bool Desconexion(int Ida)=2;
    	int CargarDatos(TConsulta pDatos)=3;
    	bool GuardarDatos(int Ida)=4;
 		int NuevoLibro(TNuevo pDatos)=5;
 		int Comprar(TComRet pDatos)=6;
 		int Retirar(TComRet pDatos)=7;
 		bool Ordenar(TOrdenacion pDatos)=8;
 		 
 		int NLibros(int Ida)=9;
 		int Buscar(TConsulta pDatos)=11;
 		TLibro Descargar(TPosicion pDatos)=10;
 		int Prestar(TPosicion pDatos)=12;
 		int Devolver(TPosicion pDatos)=13; 
	} = 1;
} = 0x30000001;
