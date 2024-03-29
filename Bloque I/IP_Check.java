package netUtils;
import java.io.IOException;
import java.net.*;
import java.util.regex.Pattern;

public class IP_Check {
	
	private static boolean ok = false;
	private static String IP_class;
	private static String Network_Mask;
	private static String Network_ID;
	private static String Broadcast_Addr;
	private static String Raw_Bits;
	private static final String IPv4_REGEX ="^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$";
	private static final Pattern IPv4_PATTERN = Pattern.compile(IPv4_REGEX);

	public static void main(String[] args) throws IOException  {
		
		if(args.length < 1 || args.length > 2) throw new IllegalArgumentException("Parametros: <IP Address> <Mask>");
    	
        InetAddress address =  InetAddress.getByName(args[0]);
        int mask = Integer.parseInt(args[1]);
		
        ok =  checkAddr(address.getHostAddress());
        getNetworkMask(mask);
        getID(address.getHostAddress());
        getBroadcastAddr(address.getHostAddress(),mask);
        
        if(ok) System.out.println("La IP es válidad y de clase " + IP_class);
        else System.out.println("La dirección IP no es válida");
        
        System.out.println("ID de la Red: " + Network_ID);
        
        System.out.println("Máscara de Red: " +  Network_Mask);
        
        System.out.println("Broadcast: " + Broadcast_Addr);
        
        System.out.println("Número de IP's para host: "  + getNumberofIPs(mask));
        
        System.out.print("Rango: ");
		getRange(Network_ID, Broadcast_Addr);
	}
	

	private static boolean checkAddr(String ip) {
		
		if(ip.equals(null)) return false;
		
		if (!IPv4_PATTERN.matcher(ip).matches())return false;
		
		String octect[] = ip.split("\\.");
		int c = Integer.parseInt(octect[0]);
		
		for(String group : octect) {
			if(Integer.parseInt(group) > 255 || Integer.parseInt(group) < 0 ) return false;
		}
		
		getClass(c);
			
			
		return true;	
	}
	
	private static void getClass(int c) {
		if((c >= 1 ) && (c <= 127)) 	IP_class = "A";
		else if((c >= 128 )&& (c <= 191)) IP_class = "B";
		else if((c >= 192 )&& (c <= 223)) IP_class = "C";
		else if ((c >= 224 )&& (c <= 239)) IP_class = "D";
		else IP_class ="E";
	}
		
	private static void getNetworkMask(int mask) {
		
		StringBuilder sb = new StringBuilder();
		
		sb = BinaryCalc(mask);
		
		Network_Mask = sb.toString();
		
		String octect_mask[] = Network_Mask.split("\\.");
		
		StringBuilder sb1 = new StringBuilder();
		
		for(String group : octect_mask) {
			int decimal = Integer.parseInt(group,2);
			sb1.append(decimal).append(".");
		}
				
		sb1.setLength(sb1.length()-1);
		
		Network_Mask = sb1.toString();
		
	}
	
	private static void getID(String ip) {
			
		String octect_ip[] = ip.split("\\.");
		String octect_mask[] = Network_Mask.split("\\.");
		
		StringBuilder sb = new StringBuilder();
		
		int addr;
		int mask;
		
		for(int i=0; i <= 3;i++) {
			addr = Integer.parseInt(octect_ip[i]);
			mask = Integer.parseInt(octect_mask[i]);
			sb.append(addr & mask).append(".");
		}
		
		sb.setLength(sb.length()-1);
		
		Network_ID = sb.toString();
		
	}
	
	private static void getBroadcastAddr(String ip,int m) {
		
		String bits = "";
		int cont = 0;
		int n = 0;
		for(int i=0;i<Raw_Bits.length();i++) {
			if(Raw_Bits.charAt(i) == '0') {
				bits += '1';
				cont++;
			} else if(Raw_Bits.charAt(i) == '1'){
				bits += '0';
				cont++;
			}
			
			if(cont == 8 && n <3) {
				bits += '.';
				cont = 0;
				n++;
			}
		}	
		
		String octect_mask[] = bits.split("\\.");
		
		StringBuilder sb1 = new StringBuilder();
		
		for(String group : octect_mask) {
			int decimal = Integer.parseInt(group,2);
			sb1.append(decimal).append(".");
		}
				
		sb1.setLength(sb1.length()-1);
		
		Broadcast_Addr = sb1.toString();
			
		String octect_ip[] = ip.split("\\.");
		String octect_broad[] = Broadcast_Addr.split("\\.");
		
		StringBuilder sb = new StringBuilder();
		
		int addr;
		int mask;
		
		for(int i=0; i <= 3;i++) {
			addr = Integer.parseInt(octect_ip[i]);
			mask = Integer.parseInt(octect_broad[i]);
			sb.append(addr | mask).append(".");
		}
		
		sb.setLength(sb.length()-1);
		
		Broadcast_Addr = sb.toString();
	}
	
	private static StringBuilder BinaryCalc(int mask) {
		StringBuilder sb = new StringBuilder();   
		StringBuilder sb1 = new StringBuilder();
		
		if(mask <= 8) {
			
		int aux1 = 8 - mask;
		
		for(int i=0;i<mask;i++) {
			sb.append("1");
		}
		
		for(int i=0;i<aux1;i++) {
			sb.append("0");
		}	
			sb.append(".00000000.00000000.00000000");
		}
		
		else if(mask> 8 && mask <= 16) {
			
			int aux1 = mask - 8;
			int aux2 = 8 - aux1;
			
			sb.append("11111111.");
			
			for(int i=0;i<aux1;i++) {
				sb.append("1");
			}
			
			for(int i=0;i<aux2;i++) {
				sb.append("0");
			}
			
			sb.append(".00000000.00000000");
		}
		else if(mask > 16 && mask <= 24) {
			
			int aux1 = mask - 16;
			int aux2 = 8 - aux1;
			
			sb.append("11111111.11111111.");
			
			for(int i=0;i<aux1;i++) {
				sb.append("1");
			}
			for(int i=0;i<aux2;i++) {
				sb.append("0");
			}
			sb.append(".00000000");
		} else {
			
			int aux1 = mask - 24;
			int aux2 = 8 - aux1;
			
			sb.append("11111111.11111111.11111111.");
			for(int i=0;i<aux1;i++) {
				sb.append("1");
			}
			for(int i=0;i<aux2;i++) {
				sb.append("0");
			}
		}
		
		sb1 = sb;
		Raw_Bits = sb1.toString().replace('.', ' ');
		Raw_Bits = Raw_Bits.replaceAll("\\s","");
		return sb;
	}
	
	private static int getNumberofIPs(int mask) {
		int result = 32 - mask;
		result = (int) (Math.pow(2, result) - 2);
		return result;
	}
	
	private static void getRange(String id, String broadcast) {
		
		String octect_id[] = id.split("\\.");
		String octect_broad[] = broadcast.split("\\.");
		
		StringBuilder primero = new StringBuilder();
		StringBuilder ultimo = new StringBuilder();
		
		for(int i=0;i<=3;i++) {
			if((octect_id[i]).equals(octect_broad[i])) {
				primero.append(octect_id[i]).append(".");
				ultimo.append(octect_broad[i]).append(".");
			} else {
				int menor = Integer.parseInt(octect_id[i]);
				int mayor = Integer.parseInt(octect_broad[i]);
				if(i == 3) {
					menor++;
					mayor--;
				}
				primero.append(menor).append(".");
				ultimo.append(mayor).append(".");
			}
		}
		
		primero.setLength(primero.length() -1);
		ultimo.setLength(ultimo.length() -1);
		
		System.out.println(primero.toString() + " - " + ultimo.toString());
	}
	
}
