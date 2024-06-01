using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace ServicioWebBiblioteca
{
    [ServiceContract]
    public interface IGestorBiblioteca
    {
        [OperationContract]
        int Conexion(String pPasswd);

        [OperationContract]
        bool Desconexion(int pIda);

        //Servicios de Repositorio
        [OperationContract]
        int NRepositorios(int pIda);

        [OperationContract]
        TDatosRepositorio DatosRepositorio(int pIda, int pRepo);

        [OperationContract]
        int AbrirRepositorio(int pIda, String pNomFichero);

        [OperationContract]
        int GuardarRepositorio(int pIda, int pRepo);

        //Gestión de cada repositorio por el administrador
        [OperationContract]
        int NuevoLibro(int pIda, TLibro L, int pRepo);

        [OperationContract]
        int Comprar(int pIda, String pIsbn, int pNoLibros);

        [OperationContract]
        int Retirar(int pIda, String pIsbn, int pNoLibros);

        [OperationContract]
        bool Ordenar(int pIda, int pCampo);

        //Gestión de libros de todos los repositorios al mismo tiempo
        [OperationContract]
        int NLibros(int pRepo);

        [OperationContract]
        int Buscar(int pIda, String pIsbn);

        [OperationContract]
        TLibro Descargar(int pIda, int pRepo, int pPos);

        [OperationContract]
        int Prestar(int pPos);

        [OperationContract]
        int Devolver(int pPos);
    }

    // Utilice un contrato de datos, como se ilustra en el ejemplo siguiente, para agregar tipos compuestos a las operaciones de servicio.
    // Puede agregar archivos XSD al proyecto. Después de compilar el proyecto, puede usar directamente los tipos de datos definidos aquí, con el espacio de nombres "ServicioWebBilioteca.ContractType".
    [DataContract]
    public class CompositeType
    {
        bool boolValue = true;
        string stringValue = "Hello ";

        [DataMember]
        public bool BoolValue
        {
            get { return boolValue; }
            set { boolValue = value; }
        }

        [DataMember]
        public string StringValue
        {
            get { return stringValue; }
            set { stringValue = value; }
        }
    }
}
