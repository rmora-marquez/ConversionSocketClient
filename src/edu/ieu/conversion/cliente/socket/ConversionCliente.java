package edu.ieu.conversion.cliente.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ConversionCliente implements Callable<String>{
	private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
    private String mOrigen;
    private String mDestino;
    private Double cantidad;
    private Double resultado = 0d;   
    
	public ConversionCliente(String mOrigen, String mDestino, Double cantidad) {
		this.mOrigen = mOrigen;
		this.mDestino = mDestino;
		this.cantidad = cantidad;		
	}

	@Override
	public String call() throws Exception {		
	        clientSocket = new Socket("localhost", 6000);
	        out = new PrintWriter(clientSocket.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        //enviamos el origen y destino concatenado -
	        String msg = mOrigen + "-" + mDestino + ";"+cantidad;
	        out.println(msg);
	        System.out.println("Enviado: " + msg);
	        String resp = in.readLine();
	        System.out.println("Respuesta: " + resp);
	        
	        String[] arregloResp = resp.split(";");
	        	        
	        Double factorConversion = Double.parseDouble(arregloResp[0]);
	        System.out.println("Resultado de conversion del servidor :" + arregloResp[1]);
	        resultado = factorConversion * cantidad;
	        String returnValue = "Resultado de convertir " + cantidad + " " + mOrigen + 
	        		"(s) a " + mDestino + " = " + resultado + " " + mDestino + "(s)" +
	        		" , con un factor de conversion de " + factorConversion;	        
	        System.out.println(returnValue);
	        out.close();
	        in.close();
	        clientSocket.close();
	        return returnValue;    	
	}
	
	
}
