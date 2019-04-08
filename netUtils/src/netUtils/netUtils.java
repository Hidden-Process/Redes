package netUtils;
import java.net.*;
import java.util.Enumeration;

public class netUtils {

	public static void main(String[] args) throws SocketException{
		
		byte [] HWaddr;
		boolean [] bit;
		StringBuilder sb = new StringBuilder();
		Enumeration<?> interfaces = NetworkInterface.getNetworkInterfaces();
		
		System.out.println("Interfaces de Red Disponibles: \n");
		
		System.out.println("---------------------------------------------");
		
		while(interfaces.hasMoreElements()) {
			
			NetworkInterface network = (NetworkInterface)interfaces.nextElement();
			
			if (network.isLoopback()) {
				continue;
			}
			
			HWaddr = network.getHardwareAddress();
			bit = BitArray(HWaddr);
			
			for (int i = 0; i < HWaddr.length; i++) {
				sb.append(String.format("%02X%s", HWaddr[i], (i < HWaddr.length - 1) ? ":" : ""));		
			}
			
			System.out.println("| " + network.getName() + ": " + sb + " - " + MacAdministration(bit)  + " - "+ connectionStatus(network.isUp()) + " |");
			
			System.out.println("---------------------------------------------");
			
			sb.delete(0, sb.length());
			}
		
	}
	
	 private static boolean[] BitArray(byte[] b) {
		    boolean[] bits = new boolean[b.length * 8];
		    for (int i = 0; i < b.length * 8; i++) {
		      if ((b[i / 8] & (1 << (7 - (i % 8)))) > 0)
		        bits[i] = true;
		    }
		    return bits;
	 }
	
	private static String connectionStatus(boolean net) {
		return net ? "(up)" : "(down)";
	}
	
	private static String MacAdministration(boolean [] bits) {
		return bits[6] ? "local" : "global";
	}

}
