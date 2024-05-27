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
    class Servidor
    {
        static void Main(string[] args)
        {
            ChannelServices.RegisterChannel(new TcpChannel(12345), false);
            Console.WriteLine("Registrando el servicio de la Biblioteca Remota en modo Singlenton...");
            RemotingConfiguration.RegisterWellKnownServiceType(typeof(GestorBiblioteca), "BibliotecaRemota",
            WellKnownObjectMode.Singleton);
            Console.WriteLine("Esperando llamadas Remotas...");
            Console.WriteLine("Pulsa Enter para Salir..");
            Console.ReadLine();
        }
    }
}
