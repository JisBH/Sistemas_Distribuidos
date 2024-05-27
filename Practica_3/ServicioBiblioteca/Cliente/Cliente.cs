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
    class Cliente
    {
        static void Main(string[] args)
        {
            ChannelServices.RegisterChannel(new TcpChannel(), false);
            GestorBiblioteca Biblioteca = (GestorBiblioteca)Activator.GetObject(typeof(GestorBiblioteca),
            "tcp://localhost:12345/BibliotecaRemota");
        }
    }
}
