/*
  Carlos Pineda Guerrero 2021
*/
//import com.google.gson.*;

class Usuario
{
  int id_usuario;
  String email;
  String nombre;
  String apellido_paterno;
  String apellido_materno;
  String fecha_nacimiento;
  String telefono;
  String genero;
  byte[] foto;

  public Usuario() {
    this.foto = null;
}

    int getid() { return this.id_usuario;}
    String getEmail() { return this.email; }
    String getNombre() { return this.nombre; }
    String getApellidoPaterno() { return this.apellido_paterno; }
    String getApellidoMaterno() { return this.apellido_materno; }
    String getFechaNacimiento() { return this.fecha_nacimiento; }
    String getTelefono() { return this.telefono; }
    String getGenero() { return this.genero; }
    byte[] getFoto() { return this.foto; }

    void setid(int id_usuario){ this.id_usuario=id_usuario; }
    void setEmail(String email) { this.email = email; }
    void setNombre(String nombre) { this.nombre = nombre; }
    void setApellidoPaterno(String apellidoPaterno) { this.apellido_paterno = apellidoPaterno; }
    void setApellidoMaterno(String apellidoMaterno) { this.apellido_materno = apellidoMaterno; }
    void setFechaNacimiento(String fechaNacimiento) { this.fecha_nacimiento = fechaNacimiento; }
    void setTelefono(String telefono) { this.telefono = telefono; }
    void setGenero(String genero) { this.genero = genero; }
    void setFoto(byte[] foto) { this.foto = foto; }

  // @FormParam necesita un metodo que convierta una String al objeto de tipo Usuario
  public static Usuario valueOf(String s) throws Exception
  {
    Gson j = new GsonBuilder().registerTypeAdapter(byte[].class,new AdaptadorGsonBase64()).create();
    return (Usuario)j.fromJson(s,Usuario.class);
  }
}
