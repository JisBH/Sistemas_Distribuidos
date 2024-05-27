using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Runtime.Remoting;
using System.Runtime.Remoting.Channels;
using System.Runtime.Remoting.Channels.Tcp;


namespace ServicioBiblioteca
{
    public class Servidor
    {
        static void Main(string[] args)
        {
            int puerto;
            bool parsingResult = false;

            do
            {
                Console.WriteLine("Introduce el nº de puerto para comunicarse: ");
                parsingResult = Int32.TryParse(Console.ReadLine(), out puerto);

                if (!parsingResult)
                {
                    Console.WriteLine("\nPor favor, indique un puerto válido.");
                }
            } while (!parsingResult);

            try
            {

                ChannelServices.RegisterChannel(new TcpChannel(puerto), false);
                Console.WriteLine("\nRegistrando el servicio del Gestor Bibliotecario en modo Singlenton...");
                RemotingConfiguration.RegisterWellKnownServiceType(typeof(GestorBiblioteca), "GestorBiblioteca",
                WellKnownObjectMode.Singleton);
                Console.WriteLine("\nEsperando llamadas Remotas...");
                Console.WriteLine("\nPulsa Enter para Salir..");
                Console.ReadLine();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error: " + ex);
                Console.WriteLine("\nPresiona una tecla para continuar");
                Console.ReadLine();
            }
        }
    }
}
