import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class index{
public static void Imprimir_datos(Usuario user) {
    System.out.println( "Email: " + user.getEmail() + "\n" +
                        "Nombre: " + user.getNombre() + "\n" +
                        "Apellido Paterno: " + user.getApellidoPaterno() + "\n" +
                        "Apellido Materno: " + user.getApellidoMaterno() + "\n" + 
                        "Fecha de nacimiento: " + user.getFechaNacimiento() + "\n" + 
                        "Telefono: " + user.getTelefono() + "\n" + 
                        "Genero: " + user.getGenero() + "\n" +
                        "Foto: null");
}
/** */
private static Usuario crearUsuario(Usuario usuario) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Email:");
    usuario.setEmail(br.readLine());

    System.out.println("Nombre:");
    usuario.setNombre(br.readLine());

    System.out.println("Apellido Paterno:");
    usuario.setApellidoPaterno(br.readLine());

    System.out.println("Apellido Materno:");
    usuario.setApellidoMaterno(br.readLine());

    System.out.println("Fecha de nacimiento:");
    usuario.setFechaNacimiento(br.readLine());

    System.out.println("Telefono:");
    usuario.setTelefono(br.readLine());

    System.out.println("Genero (M/F):");
    usuario.setGenero(br.readLine());

    return usuario;
}

protected static String Consultar_usuario() throws IOException {
    String id;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Introduce el id del Usuario que se quiere consultar");
    id = br.readLine();

    return id;
}
static class Respuesta {
    public Respuesta(int responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    } 
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public int getResponseCode() { return this.responseCode; }
    public String getMessage() { return this.message; }

    private int responseCode;
    private String message;
}
static class Servicio {
    
    static Respuesta hacerConsulta(String cuerpo, String metodo, String endpoint, String parametro) {
        try {
            URL url = new URL(URL_MAQUINA + endpoint);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            
            conexion.setDoOutput(true);
            conexion.setRequestMethod(metodo);
            conexion.setRequestProperty(REQUEST_KEY, VALUE_KEY);

            if(cuerpo.length() > 0 && parametro.length() > 0) { 
                String parametros = parametro + "=" + URLEncoder.encode(cuerpo, "UTF-8");
                OutputStream os = conexion.getOutputStream();
                os.write(parametros.getBytes(), 0, parametros.getBytes().length);
                os.flush();
                os.close();
            }

            if(conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
                return new Respuesta(400, "{message: 'No encontrado'}");

            BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            String respuestaServidor;
            String respuesta = "";
            while((respuestaServidor = br.readLine()) != null) respuesta += respuestaServidor;
            conexion.disconnect();

            return new Respuesta(conexion.getResponseCode(), respuesta);
        } catch(Exception e) { e.printStackTrace(); }
        
        return new Respuesta(404, "No encontrado");
    }

    private final static String URL_MAQUINA = "http://70.37.67.201:8080/Servicio/rest/ws/";
    private final static String REQUEST_KEY = "Content-Type";
    private final static String VALUE_KEY = "application/x-www-form-urlencoded";
}
static class Error {
    String message;

    public Error(String message) {
        this.message = message;
    }
}
static class Menu {
    public Menu() {}    

    protected char mostrarMenu() {
        Scanner s = new Scanner(System.in);

        System.out.println("a. Alta usuario");
        System.out.println("b. Consulta usuario");
        System.out.println("c. Borra usuario");
        System.out.println("d. Salir");

        char seleccion = s.nextLine().charAt(0);

        return seleccion; 
    }

    protected void opcion(char op) {
        switch(op) {
            case 'a':
                altaUsuario();
                break;
            case 'b':
                consultaUsuario();
                break;
            case 'c':
                borrarUsuario();
                break;    
            case 'd':
            System.exit(0);
                break;
            default:
                System.out.println("No existe esa opcion");
                break;
        }
    }

    private void altaUsuario() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();

        Gson gson = builder.create();

        try {
            Usuario usuario = new Usuario();
            usuario = crearUsuario(usuario);
            String cuerpo = gson.toJson(usuario);
            Respuesta response = Servicio.hacerConsulta(cuerpo, POST_METHOD, "alta_usuario", "usuario");
            if(response.getResponseCode() != 400) {
                System.out.println(response.getMessage());
            } else {
                Error error = gson.fromJson(response.getMessage(), Error.class);
                System.out.println(error.message);
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    private void consultaUsuario() {
        Gson gson = new Gson();

        try {
            String cuerpo = Consultar_usuario();
            Respuesta response = Servicio.hacerConsulta(cuerpo, GET_METHOD, "consulta_usuario", "id_usuario");
            
            if(response.getResponseCode() != 400) {
                Usuario usuario = gson.fromJson(response.getMessage(), Usuario.class);
                Imprimir_datos(usuario);
            } else {
                Error error = gson.fromJson(response.getMessage(), Error.class);
                System.out.println(error.message);
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    private void borrarUsuario() {
        Gson gson = new Gson();

        try {
            String cuerpo = Consultar_usuario();
            Respuesta response = Servicio.hacerConsulta(cuerpo, POST_METHOD, "borra_usuario", "id_usuario");
            
            if(response.getResponseCode() != 400) {
                System.out.println(response.getMessage());
            } else {
                Error error = gson.fromJson(response.getMessage(), Error.class);
                System.out.println(error.message);
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    private final String POST_METHOD = "POST";
    private final String GET_METHOD = "GET";
}
public static void main(String[] args) {
    Menu Menu = new Menu();

    while(true) {
        char e = Menu.mostrarMenu();
        Menu.opcion(e);
    }
// fin main
}

}