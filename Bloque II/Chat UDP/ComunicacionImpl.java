package es.uma.informatica.rsd.chat.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import es.uma.informatica.rsd.chat.ifaces.Comunicacion;
import es.uma.informatica.rsd.chat.ifaces.Controlador;
import es.uma.informatica.rsd.chat.impl.DialogoPuerto.PuertoAlias;


public class ComunicacionImpl implements Comunicacion {
	
	private Controlador c;
	private String alias;
	private int port;
	private DatagramSocket socket;
	

	@Override
	public void crearSocket(PuertoAlias pa) {
		port = pa.puerto;
		this.alias = pa.alias;
		
		try {
			socket = new DatagramSocket(port);
		} catch(IOException e) {
			System.out.println("Error en la creación del socket " + e.getMessage());
		}
	}

	@Override
	public void setControlador(Controlador c) {
		this.c = c;
	}

	@Override
	public void runReceptor() {
		while(true) {
			byte [] buff = new byte [1024];
			DatagramPacket packet = new DatagramPacket(buff, buff.length);
			
			try {
				socket.receive(packet);
			} catch(IOException e2) {
				System.out.println("Error al recibir el datagrama " + e2.getMessage());
			}
			
			String rcv = new String(packet.getData(),Charset.forName("UTF-8"));
			
			String ip = rcv.split(">")[0];
			String user = rcv.split(">")[1];
			String msg = rcv.split(">")[2];
			
			InetSocketAddress sa = new InetSocketAddress(ip,packet.getPort());
			
			c.mostrarMensaje(sa, user, msg);
		}
	}

	@Override
	public void envia(InetSocketAddress sa, String mensaje) {
		String send = (sa.getAddress().toString().replace("/", "") + ">" + alias + ">" + mensaje);
		
		InetAddress dst = sa.getAddress();
		int dstport = sa.getPort();
		
		byte [] buff = send.getBytes(Charset.forName("UTF-8"));
		
		DatagramPacket packet = new DatagramPacket(buff,buff.length,dst,dstport);
		
		try {
			socket.send(packet);
		} catch(IOException e1) {
			System.out.println("Error al enviar el datagrama " + e1.getMessage());
		}
		
		
	}

	@Override
	public void joinGroup(InetAddress multi) {
	}

	@Override
	public void leaveGroup(InetAddress multi) {
	}

}
