import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) throws IOException {

        if(args.length < 1 || args.length > 2) throw new IllegalArgumentException("Parametros: <IP> <Puerto>");

        InetAddress address =  InetAddress.getByName(args[0]);
        int port =  Integer.parseInt(args[1]);

        try {
            Socket echoSocket = new Socket(address,port);

            System.out.println("Conexión establecida con el server: " + address.getHostAddress() + " en el puerto " + port);

            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            System.out.println(in.readLine());
            Scanner scan = new Scanner(System.in);

            boolean salir = false;

            while(!salir){

                System.out.println("Introduzca la letra: ");
                String letra = scan.nextLine();

                if(letra.equals("f")){
                    out.println(letra +"\r\n");
                    System.out.println(in.readLine());
                   salir = true;
                } else {
                    System.out.println("Introduzca el texto: ");
                    String texto = scan.nextLine();

                    out.print(letra +"\r\n");
                    out.println(texto);

                    System.out.println("Conectado a : " + address.getHostAddress() + ":" + port + ", Esperando la respuesta... \n");
                    Thread.sleep(2000);

                    String res = in.readLine();
                    System.out.println("El texto modificado es: " + res);
                }

            }

            out.close();
            in.close();
            echoSocket.close();
        } catch (Exception e) {
            System.err.println("Error en el cliente " + e.getMessage());
            System.exit(1);
        }
    }
}
