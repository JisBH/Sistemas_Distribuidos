package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import servicio.comun.ArrayListRepositorioLibro;
import servicio.comun.GestorBibliotecaIntf;
import servicio.comun.RepositorioLibro;
import servicio.comun.TDatosRepositorio;
import servicio.comun.TLibro;
import servicio.comun.ComparadorLibro;

/**
 *
 * @author Usuario
 */
public class GestorBiblioteca implements GestorBibliotecaIntf
{

    private int numAdministradores = 0; // Contador con el numero de administradores actualmente en el sistema
    private int idAdmin = -1;	 // Copia del Identificador de Administración enviado al usuario.
    private List<TDatosRepositorio> repositoriosCargados = new ArrayList<>(); // Lista con los repositorios cargados en memoria actualmente
    private List<TLibro> librosTodosRepositorios = new ArrayList<>(); // Lista con los libros de todos los repositorios cargados en memoria actualmente
    private int campoOrdenacion = 0;

    public GestorBiblioteca() throws RemoteException
    {
        super();
    }

    @Override
    public int Conexion(String pPasswd) throws RemoteException
    {
        int result;

        // Si ya tengo un administrador, devuelvo -1
        if (this.numAdministradores == 1)
        {
            result = -1;
        } // Si aún no tengo ningún administrador y la contraseña es correcta, devuelvo un número aleatorio
        else if (pPasswd.equals("1234"))
        {
            this.numAdministradores++;
            Random random = new Random();
            result = random.nextInt(1000000) + 1;
            this.idAdmin = result;
        } // Si aún no tengo ningún administrador y la contraseña es incorrecta, devuelvo -2
        else
        {
            result = -2;
        }

        return result;
    }

    @Override
    public boolean Desconexion(int pIda) throws RemoteException
    {
        boolean result;

        if (this.idAdmin == pIda)
        {
            this.idAdmin = -1;
            this.numAdministradores--;
            result = true;
        } else
        {
            result = false;
        }
        return result;
    }

    @Override
    public int NRepositorios(int pIda) throws RemoteException
    {
        if (pIda != this.idAdmin || this.numAdministradores > 1)
        {
            return -1;
        }

        return this.repositoriosCargados.size();
    }

    @Override
    public TDatosRepositorio DatosRepositorio(int pIda, int pPosRepo) throws RemoteException
    {
        if (pIda != this.idAdmin || this.numAdministradores > 1)
        {
            return null;
        }
        if (pPosRepo < 0 || pPosRepo >= this.repositoriosCargados.size())
        {
            return null;
        }

        return this.repositoriosCargados.get(pPosRepo);

    }

    @Override
    public int AbrirRepositorio(int pIda, String pNomFichero) throws RemoteException
    {

        int result;

        if (pIda != this.idAdmin || this.numAdministradores > 1)
        {
            result = -1;

        } else
        {
            try
            {
                DataInputStream inputStream = new DataInputStream(new FileInputStream(Paths.get(pNomFichero).toAbsolutePath().toString()));

                int numeroLibros = inputStream.readInt();
                String nombreRepositorio = inputStream.readUTF();
                String direccionRepositorio = inputStream.readUTF();

                RepositorioLibro repositorioLibro = new ArrayListRepositorioLibro();
                TDatosRepositorio datosRepositorio = new TDatosRepositorio(numeroLibros, nombreRepositorio, direccionRepositorio, repositorioLibro,pNomFichero);

                if (this.repositoriosCargados.contains(datosRepositorio))
                {
                    result = -2;
                } else
                {
                    for (int i = 0; i < numeroLibros; i++)
                    {
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
                        this.librosTodosRepositorios.add(libro);
                    }

                    this.repositoriosCargados.add(datosRepositorio);
                    Ordenar(pIda, this.campoOrdenacion);

                    result = 1;
                }

            } catch (FileNotFoundException ex)
            {
                System.out.println("Error: " + ex);
                result = 0;
            } catch (IOException ex)
            {
                System.out.println("Error: " + ex);
                result = 0;
            }

        }
        return result;
    }

    @Override
    public int GuardarRepositorio(int pIda, int pRepo) throws RemoteException
    {
        if (this.repositoriosCargados.size() == 0)
        {
            return -2;
        }

        if (pIda != this.idAdmin || this.numAdministradores > 1)
        {
            return -1;
        }

        if (pRepo < -1 || pRepo >= this.repositoriosCargados.size())
        {
            return -2;
        }

        try
        {
            if (pRepo != -1)
            {

                TDatosRepositorio datosRepositorio = this.repositoriosCargados.get(pRepo);
                DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(Paths.get(datosRepositorio.getRutaRepositorio()).toAbsolutePath().toString()));

                outputStream.writeInt(datosRepositorio.getNumeroLibros());
                outputStream.writeUTF(datosRepositorio.getNombreRepositorio());
                outputStream.writeUTF(datosRepositorio.getDireccionRepositorio());

                for (int i = 0; i < datosRepositorio.getNumeroLibros(); i++)
                {
                    TLibro libro = datosRepositorio.getRepositorioLibro().getLibro(i);

                    outputStream.writeUTF(libro.isbn);
                    outputStream.writeUTF(libro.titulo);
                    outputStream.writeUTF(libro.autor);
                    outputStream.writeInt(libro.anio);
                    outputStream.writeUTF(libro.pais);
                    outputStream.writeUTF(libro.idioma);
                    outputStream.writeInt(libro.disponibles);
                    outputStream.writeInt(libro.prestados);
                    outputStream.writeInt(libro.reservados);
                }

                return 1;
            }

            for (int i = 0; i < this.repositoriosCargados.size(); i++)
            {
                TDatosRepositorio datosRepositorio = this.repositoriosCargados.get(i);
                DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(Paths.get(datosRepositorio.getRutaRepositorio()).toAbsolutePath().toString()));

                outputStream.writeInt(datosRepositorio.getNumeroLibros());
                outputStream.writeUTF(datosRepositorio.getNombreRepositorio());
                outputStream.writeUTF(datosRepositorio.getDireccionRepositorio());

                for (int j = 0; j < datosRepositorio.getNumeroLibros(); j++)
                {
                    TLibro libro = datosRepositorio.getRepositorioLibro().getLibro(j);

                    outputStream.writeUTF(libro.isbn);
                    outputStream.writeUTF(libro.titulo);
                    outputStream.writeUTF(libro.autor);
                    outputStream.writeInt(libro.anio);
                    outputStream.writeUTF(libro.pais);
                    outputStream.writeUTF(libro.idioma);
                    outputStream.writeInt(libro.disponibles);
                    outputStream.writeInt(libro.prestados);
                    outputStream.writeInt(libro.reservados);
                }
            }

            return 1;

        } catch (FileNotFoundException ex)
        {
            System.out.println("Error: " + ex);
            return 0;

        } catch (IOException ex)
        {
            System.out.println("Error: " + ex);
            return 0;
        }
    }

    @Override
    public int NuevoLibro(int pIda, TLibro L, int pRepo) throws RemoteException
    {

        if (pIda != this.idAdmin || this.numAdministradores > 1)
        {
            return -1;
        }

        if (pRepo < 0 || pRepo >= this.repositoriosCargados.size())
        {
            return -2;
        }

        TDatosRepositorio datosRepositorio;

        for (int i = 0; i < this.repositoriosCargados.size(); i++)
        {
            datosRepositorio = this.repositoriosCargados.get(i);
            if (datosRepositorio.getRepositorioLibro().getLibroPorIsbn(L.getIsbn()) != null)
            {
                return 0;
            }
        }

        datosRepositorio = this.repositoriosCargados.get(pRepo);

        datosRepositorio.getRepositorioLibro().aniadirLibro(L);
        datosRepositorio.setNumeroLibros(datosRepositorio.getNumeroLibros() + 1);
        this.librosTodosRepositorios.add(L);
        Comparator comp = new ComparadorLibro(this.campoOrdenacion);
        datosRepositorio.getRepositorioLibro().getTodosLibros().sort(comp);

        return 1;

    }

    @Override
    public int Comprar(int pIda, String pIsbn, int pNoLibros) throws RemoteException
    {
        if (pIda != this.idAdmin)
        {
            return -1;
        }

        int posLibro = Buscar(pIda, pIsbn);

        if (posLibro == -1)
        {
            return 0;
        }

        TLibro libro = this.librosTodosRepositorios.get(posLibro);
        libro.setDisponibles(libro.getDisponibles() + pNoLibros);

        if (libro.getDisponibles() >= libro.getReservados())
        {
            libro.setDisponibles(libro.getDisponibles() - libro.getReservados());
            libro.setPrestados(libro.getPrestados() + libro.getReservados());
            libro.setReservados(0);
        } else
        {
            libro.setReservados(libro.getReservados() - libro.getDisponibles());
            libro.setPrestados(libro.getPrestados() + libro.getDisponibles());
            libro.setDisponibles(0);
        }
        
        //Codigo para ordenar el repositorio que contiene el libro
        int i = 0;
        boolean encontrado = false;

        while (i < this.repositoriosCargados.size() && !encontrado)
        {
            TDatosRepositorio datosRepositorio = this.repositoriosCargados.get(i);

            if (datosRepositorio.getRepositorioLibro().getLibroPorIsbn(pIsbn) != null)
            {
                encontrado = true;

            } else
            {
                i++;
            }
        }
        // i tiene el repositorio en el que esta el libro
        // Ordenamos el repositorio
        Comparator comp = new ComparadorLibro(this.campoOrdenacion);
        this.repositoriosCargados.get(i).getRepositorioLibro().getTodosLibros().sort(comp);

        return 1;
    }

    @Override
    public int Retirar(int pIda, String pIsbn, int pNoLibros) throws RemoteException
    {
        if (pIda != this.idAdmin)
        {
            return -1;
        }

        int posLibro = Buscar(pIda, pIsbn);

        if (posLibro == -1)
        {
            return 0;
        }

        TLibro libro = this.librosTodosRepositorios.get(posLibro);

        if (libro.getDisponibles() >= pNoLibros)
        {
            libro.setDisponibles(libro.getDisponibles() - pNoLibros);

        } else
        {
            return 2;
        }
        
        //Codigo para ordenar el repositorio que contiene el libro
        int i = 0;
        boolean encontrado = false;

        while (i < this.repositoriosCargados.size() && !encontrado)
        {
            TDatosRepositorio datosRepositorio = this.repositoriosCargados.get(i);

            if (datosRepositorio.getRepositorioLibro().getLibroPorIsbn(pIsbn) != null)
            {
                encontrado = true;

            } else
            {
                i++;
            }
        }
        // i tiene el repositorio en el que esta el libro
        // Ordenamos el repositorio
        Comparator comp = new ComparadorLibro(this.campoOrdenacion);
        this.repositoriosCargados.get(i).getRepositorioLibro().getTodosLibros().sort(comp);

        return 1;
    }

    @Override
    public boolean Ordenar(int pIda, int pCampo) throws RemoteException
    {
        if (pIda != this.idAdmin)
        {
            return false;
        }

        if (pCampo < 0 || pCampo > 8)
        {
            return false;
        }

        Comparator comp = new ComparadorLibro(pCampo);

        this.librosTodosRepositorios.sort(comp);

        for (TDatosRepositorio repositorio : this.repositoriosCargados)
        {
            repositorio.getRepositorioLibro().getTodosLibros().sort(comp);
        }

        this.campoOrdenacion = pCampo;

        return true;
    }

    @Override
    public int NLibros(int pRepo) throws RemoteException
    {
        if (pRepo == -1)
        {
            return this.librosTodosRepositorios.size();
        }

        if (pRepo >= this.repositoriosCargados.size() || pRepo < 0)
        {
            return -1;
        }
        return this.repositoriosCargados.get(pRepo).getRepositorioLibro().numLibros();
    }

    @Override
    public int Buscar(int pIda, String pIsbn) throws RemoteException
    {
        if (pIda != this.idAdmin)
        {
            return -2;
        }

        for (int i = 0; i < this.librosTodosRepositorios.size(); i++)
        {
            TLibro libro = this.librosTodosRepositorios.get(i);

            if (pIsbn.equals(libro.getIsbn()))
            {
                return i;
            }
        }

        return -1;
    }

    @Override
    public TLibro Descargar(int pIda, int pRepo, int pPos) throws RemoteException
    {
        if (pRepo == -1)
        {
            if (pPos >= this.librosTodosRepositorios.size() || pPos < 0)
            {
                return null;
            }

            TLibro libro = null;

            libro = (TLibro) this.librosTodosRepositorios.get(pPos);
            TLibro copiaLibro = new TLibro(libro.titulo, libro.autor, libro.pais, libro.idioma, libro.isbn, libro.anio, libro.disponibles, libro.prestados, libro.reservados);

            if (pIda != this.idAdmin)
            {
                copiaLibro.setReservados(0);
                copiaLibro.setPrestados(0);
            }

            return copiaLibro;
        }

        if (pRepo >= this.repositoriosCargados.size() || pRepo < 0)

        {
            return null;
        }

        TDatosRepositorio datosRepositorio = this.repositoriosCargados.get(pRepo);

        List<TLibro> librosRepositorios = datosRepositorio.getRepositorioLibro().getTodosLibros();

        TLibro libro = null;

        if (pPos < 0 || pPos >= librosRepositorios.size())
        {
            return null;
        }

        libro = (TLibro) librosRepositorios.get(pPos);
        TLibro copiaLibro = new TLibro(libro.titulo, libro.autor, libro.pais, libro.idioma, libro.isbn, libro.anio, libro.disponibles, libro.prestados, libro.reservados);
        if (pIda != this.idAdmin)
        {
            copiaLibro.setReservados(0);
            copiaLibro.setPrestados(0);
        }

        return copiaLibro;
    }

    @Override
    public int Prestar(int pPos) throws RemoteException
    {
        if (pPos < 0 || pPos >= this.librosTodosRepositorios.size())
        {
            return -1;
        }
        
        TLibro libro = this.librosTodosRepositorios.get(pPos);
        
        //Codigo para ordenar el repositorio que contiene el libro
        int i = 0;
        boolean encontrado = false;

        while (i < this.repositoriosCargados.size() && !encontrado)
        {
            TDatosRepositorio datosRepositorio = this.repositoriosCargados.get(i);

            if (datosRepositorio.getRepositorioLibro().getLibroPorIsbn(libro.getIsbn()) != null)
            {
                encontrado = true;

            } else
            {
                i++;
            }
        }
        // i tiene el repositorio en el que esta el libro
        // Ordenamos el repositorio
        Comparator comp = new ComparadorLibro(this.campoOrdenacion);
        
        if (libro.getDisponibles() > 0)
        {
            libro.setDisponibles(libro.getDisponibles() - 1);
            libro.setPrestados(libro.getPrestados() + 1);
            this.repositoriosCargados.get(i).getRepositorioLibro().getTodosLibros().sort(comp);
            return 1;
        }

        libro.setReservados(libro.getReservados() + 1);
        this.repositoriosCargados.get(i).getRepositorioLibro().getTodosLibros().sort(comp);
        return 0;

    }

    @Override
    public int Devolver(int pPos) throws RemoteException
    {
        if (pPos < 0 || pPos >= this.librosTodosRepositorios.size())
        {
            return -1;
        }
        
        TLibro libro = this.librosTodosRepositorios.get(pPos);
        
        //Codigo para ordenar el repositorio que contiene el libro
        int i = 0;
        boolean encontrado = false;

        while (i < this.repositoriosCargados.size() && !encontrado)
        {
            TDatosRepositorio datosRepositorio = this.repositoriosCargados.get(i);

            if (datosRepositorio.getRepositorioLibro().getLibroPorIsbn(libro.getIsbn()) != null)
            {
                encontrado = true;

            } else
            {
                i++;
            }
        }
        // i tiene el repositorio en el que esta el libro
        // Ordenamos el repositorio
        Comparator comp = new ComparadorLibro(this.campoOrdenacion);
        
        if (libro.getReservados() == 0 && libro.getPrestados() > 0)
        {
            libro.setPrestados(libro.getPrestados() - 1);
            libro.setDisponibles(libro.getDisponibles() + 1);          
            this.repositoriosCargados.get(i).getRepositorioLibro().getTodosLibros().sort(comp);
            return 1;
        }
        
        if(libro.getReservados() > 0 && libro.getPrestados() > 0){
            libro.setReservados(libro.getReservados() - 1);
            this.repositoriosCargados.get(i).getRepositorioLibro().getTodosLibros().sort(comp);
            return 0;
        }

        return 2;
    }

}
