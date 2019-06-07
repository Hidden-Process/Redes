import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Cliente {

    private static final int MAX_TRIES = 3;

    public static void main(String[] args) throws IOException {

        if(args.length < 1 || args.length > 2) throw new IllegalArgumentException("Parametros: <IP> <Puerto>");

        InetAddress address =  InetAddress.getByName(args[0]);
        int port =  Integer.parseInt(args[1]);

        try {
            Socket clientSocket = new Socket(address,port);

            System.out.println("Conexión establecida con el server: " + address.getHostAddress() + " en el puerto " + port);

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println(in.readLine());
            Scanner scan = new Scanner(System.in);

            boolean salir = false;
            int cont = 0;

            while(!salir){

                System.out.print("Introduzca la letra: ");
                String letra = scan.nextLine();

                if(letra.equals("f")){
                    out.println(letra +"\r\n");
                    System.out.print(in.readLine());
                   salir = true;
                } else {
                    System.out.print("Introduzca el texto: ");
                    String texto = scan.nextLine();

                    out.print(letra +"\r\n");
                    out.println(texto);

                    System.out.println("Conectado a : " + address.getHostAddress() + ":" + port + ", Esperando la respuesta... ");
                    Thread.sleep(1000);

                    String res = in.readLine();
                    if(res == null){
                        cont++;
                        if(cont == MAX_TRIES){
                            System.out.println("Maximo numero de intentos de conexión con el servidor superado.");
                            salir = true;
                        }
                    } else {
                            System.out.println("El texto modificado es: " + res);
                    } 
                }

            }

            System.out.println("\n");
            System.out.println("Conexión con el servidor cerrada \n");
            out.close();
            in.close();
            clientSocket.close();

        } catch (ConnectException c){
            System.out.println("No se puedo establecer conexión con el servidor");
        } catch (Exception e) {
            System.err.println("Error en el cliente " + e.getMessage());
            System.exit(1);
        }
    }
}
