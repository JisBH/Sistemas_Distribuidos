package servidor;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import servicio.comun.ArrayListRepositorioLibro;
import servicio.comun.GestorBibliotecaIntf;
import servicio.comun.RepositorioLibro;
import servicio.comun.TDatosRepositorio;
import servicio.comun.TLibro;

/**
 *
 * @author Usuario
 */
public class GestorBiblioteca implements GestorBibliotecaIntf {

    private int numAdministradores = 0; // Contador con el numero de administradores actualmente en el sistema
    private int idAdmin = -1;	 // Copia del Identificador de Administración enviado al usuario.
    private List<TDatosRepositorio> repositoriosCargados = new ArrayList<>(); // Lista con los repositorios cargados en memoria actualmente

    public GestorBiblioteca() throws RemoteException {
        super();
    }

    @Override
    public int Conexion(String pPasswd) throws RemoteException {
        int result;

        // Si ya tengo un administrador, devuelvo -1
        if (this.numAdministradores == 1) {
            result = -1;
        } // Si aún no tengo ningún administrador y la contraseña es correcta, devuelvo un número aleatorio
        else if (pPasswd.equals("1234")) {
            this.numAdministradores++;
            Random random = new Random();
            result = random.nextInt(1000000) + 1;
            this.idAdmin = result;
        } // Si aún no tengo ningún administrador y la contraseña es incorrecta, devuelvo -2
        else {
            result = -2;
        }

        return result;
    }

    @Override
    public boolean Desconexion(int pIda) throws RemoteException {
        boolean result;

        if (this.idAdmin == pIda) {
            this.idAdmin = -1;
            this.numAdministradores--;
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public int NRepositorios(int pIda) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public TDatosRepositorio DatosRepositorio(int pIda, int pPosRepo) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int AbrirRepositorio(int pIda, String pNomFichero) throws RemoteException {

        int result;
        
        if (pIda != idAdmin) {
            result = -1;

        } else {
            try {
                DataInputStream inputStream = new DataInputStream(new FileInputStream(Paths.get(pNomFichero).toAbsolutePath().toString()));

                int numeroLibros = inputStream.readInt();
                String nombreRepositorio = inputStream.readUTF();
                String direccionRepositorio = inputStream.readUTF();

                RepositorioLibro repositorioLibro = new ArrayListRepositorioLibro();
                TDatosRepositorio datosRepositorio = new TDatosRepositorio(numeroLibros, nombreRepositorio, direccionRepositorio, repositorioLibro);

                if (repositoriosCargados.contains(datosRepositorio)) {
                    result = -2;
                } else {
                    for (int i = 0; i < numeroLibros; i++) {
                        String isbn = inputStream.readUTF();
                        String titulo = inputStream.readUTF();
                        String autor = inputStream.readUTF();
                        int anio = inputStream.readInt();
                        String pais = inputStream.readUTF();
                        String idioma = inputStream.readUTF();
                        int disponible = inputStream.readInt();
                        int prestado = inputStream.readInt();
                        int reservado = inputStream.readInt();
                       
                        TLibro libro = new TLibro(titulo, autor, pais, idioma, isbn, anio, disponible, prestado, reservado);
                        datosRepositorio.getRepositorioLibro().aniadirLibro(libro);       
                    }
                   
                    repositoriosCargados.add(datosRepositorio);
                    result = 1;     
                }

            } catch (FileNotFoundException ex) {
                System.out.println("Error: " + ex);
                result = 0;
            } catch (IOException ex) {
                System.out.println("Error: " + ex);
                result = 0;
            }

        }
        return result;
    }

    @Override
    public int GuardarRepositorio(int pIda, int pRepo) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int NuevoLibro(int pIda, TLibro L, int pRepo) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int Comprar(int pIda, String pIsbn, int pNoLibros) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int Retirar(int pIda, String pIsbn, int pNoLibros) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean Ordenar(int pIda, int pCampo) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int NLibros(int pRepo) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int Buscar(int pIda, String pIsbn) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public TLibro Descargar(int pIda, int pRepo, int pPos) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int Prestar(int pPos) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int Devolver(int pPos) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /*código para Ordenar una estructura de datos que tenga definido el método sort.
********************************************************************************

Libros.sort(new Comparator<TLibro>() {
    @Override
    public int compare(TLibro o1, TLibro o2) {
        int C = 0;
        switch (CampoOrdenacion) {
            case 0:
                C = o1.getIsbn().compareTo(o2.getIsbn());
                break;
            case 1:
                C = o1.getTitulo().compareTo(o2.getTitulo());
                break;
            case 2:
                C = o1.getAutor().compareTo(o2.getAutor());
                break;
            case 3:
                C = Integer.compare(o1.getAnio(), o2.getAnio());
                break;
            case 4:
                C = o1.getPais().compareTo(o2.getPais());
                break;
            case 5:
                C = o1.getPais().compareTo(o2.getPais());
                break;
            case 6:
                C = Integer.compare(o1.getNoLibros(), o2.getNoLibros());
                break;
            case 7:
                C = Integer.compare(o1.getNoPrestados(), o2.getNoPrestados());
                break;
            case 8:
                C = Integer.compare(o1.getNoListaEspera(), o2.getNoListaEspera());
                break;
        }
        return C;
    }
});


Código para mostrar un libro y su cabecera en caso necesario.
*************************************************************

private String Ajustar(String S, int Ancho) {
    byte v[] = S.getBytes();
    int c = 0;
    int len = 0;
    int uin;
    for (int i = 0; i < v.length; i++) {
        uin = Byte.toUnsignedInt(v[i]);
        if (uin > 128) {
            c++;
        }
    }

    len = c / 2;

    for (int i = 0; i < len; i++) {
        S = S + " ";
    }

    return S;
}

public void Mostrar(int Pos, boolean Cabecera) {
    if (Cabecera) {
        System.out.println(String.format("%-5s%-58s%-18s%-4s%-4s%-4s", "POS", "TITULO", "ISBN", "DIS", "PRE", "RES"));
        System.out.println(String.format("     %-30s%-28s%-12s", "AUTOR", "PAIS (IDIOMA)", "AÑO"));
        for (int i = 0; i < 93; i++) {
            System.out.print("*");
        }
        System.out.print("\n");
    }

    String T = Ajustar(String.format("%-58s", Titulo), 58);
    String A = Ajustar(String.format("%-30s", Autor), 30);
    String PI = Ajustar(String.format("%-28s", Pais + " (" + Idioma + ")"), 28);

    System.out.println(String.format("%-5d%s%-18s%-4d%-4d%-4d", Pos + 1, T, Isbn, NoLibros, NoPrestados, NoListaEspera));
    System.out.println(String.format("     %s%s%-12d", A, PI, Anio));
}*/
}
