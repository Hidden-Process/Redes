import java.net.*;
import java.io.*;

public class Servidor {

    public static void main(String[] args) throws IOException {

        if(args.length != 1) throw new IllegalArgumentException("Introduzca el puerto como parametro");

        int port = Integer.parseInt(args[0]);

        try {
            ServerSocket server = new ServerSocket(port,1);

            System.out.println("Servidor Iniciado... Esperando Conexiones.");

            while (true) {
                Socket client = server.accept();

                System.out.println("Conexión Aceptada de " + client);

                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);

                out.println("Bienvenido al servicio de modificación de textos");

                boolean salir = false;

                while(!salir){
                    String opcion = in.readLine();
                    String texto = in.readLine();

                    if(opcion.equals("f")){
                        out.println("FINAL");
                       salir = true;
                    } else if(opcion.equals("m")){
                        out.println(texto.toLowerCase());
                    } else if(opcion.equals("M")){
                        out.println(texto.toUpperCase());
                    } else {
                        out.println("Opcion Incorrecta");
                    }
                }

                in.close();
                out.close();
                client.close();

                System.out.println("Conexión con el cliente finalizada, esperando nueva conexión...");
            }
        } catch (Exception e) {
            System.out.println("Error en el servidor " + e.getMessage());
            System.exit(-1);
        }
    }

}
