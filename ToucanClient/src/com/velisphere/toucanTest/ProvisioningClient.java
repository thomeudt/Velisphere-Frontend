package com.velisphere.toucanTest;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.velisphere.toucanTest.DisableSSLCertificateCheckUtil.NullHostnameVerifier;
import com.velisphere.toucanTest.DisableSSLCertificateCheckUtil.NullX509TrustManager;
import com.velisphere.toucanTest.config.ConfigFileAccess;
import com.velisphere.toucanTest.config.SecretData;

public class ProvisioningClient {

	public static void main(String[] args) {
		InetAddress ip;
		
		// disable certificate matching check
		
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier()
		{

			@Override
			public boolean verify(String hostname, SSLSession session) {
				// TODO Auto-generated method stub
				return true;
			}
			
		});
		
		
		
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
		    public X509Certificate[] getAcceptedIssuers(){return null;}
		    public void checkClientTrusted(X509Certificate[] certs, String authType){}
		    public void checkServerTrusted(X509Certificate[] certs, String authType){}
		}};

		// Install the all-trusting trust manager
			 SSLContext sc;
			try {
				sc = SSLContext.getInstance("TLS");
			
		    sc.init(null, trustAllCerts, new SecureRandom());
		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		
  
		
	
		//Client client = ClientBuilder.newClient();
		
		Client client = ClientBuilder.newBuilder()	      
	      .sslContext(sc)
	      .build();
		
		StringBuilder sb = new StringBuilder();

		try {
			ip = InetAddress.getLocalHost();
			System.out.println("Current IP address : " + ip.getHostAddress());

			Enumeration<NetworkInterface> networks = NetworkInterface
					.getNetworkInterfaces();
			while (networks.hasMoreElements()) {
				NetworkInterface network = networks.nextElement();
				byte[] mac = network.getHardwareAddress();

				if (mac != null) {
					System.out.print("Current MAC address : ");
					for (int i = 0; i < mac.length; i++) {
						sb.append(String.format("%02X%s", mac[i],
								(i < mac.length - 1) ? "-" : ""));
					}
					System.out.println(sb.toString());
				}
			}

			
			
			
			WebTarget target = client
					.target("http://connectedthingslab.com:8080/BlenderServer/rest/config/get/general");
			Response response = target.path("TOUCAN").request().get();

			System.out.println(response);

			String toucanIP = response.readEntity(String.class);

			System.out.println(toucanIP);
			
			target = client.target("https://" + toucanIP
					+ "/rest/provisioning/put");
			
			String identifier = "sim";

			response = target.path("endpoint").path(identifier).request()
					.put(Entity.text("cbdf155d-98ab-490d-a911-e89e6e3e1428"));
			System.out.println("Search for identifier: " + identifier);

			System.out.println(response);
			String jsonInput = response.readEntity(String.class);
			System.out.println(jsonInput);

			ObjectMapper mapper = new ObjectMapper();

			SecretData secretData = new SecretData();
			try {
				secretData = mapper.readValue(jsonInput, SecretData.class);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ConfigFileAccess.saveParamChangesAsXML(secretData.getSecret(),
					secretData.getEpid());

		} catch (UnknownHostException e) {
			System.out.println("Unknown Host " + e);
			e.printStackTrace();
		} catch (SocketException e) {
			System.out.println("Socket Error " + e);
			e.printStackTrace();
		}
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (KeyManagementException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


	}
	

	private static TrustManager[ ] get_trust_mgr() {
	     TrustManager[ ] certs = new TrustManager[ ] {
	        new X509TrustManager() {
	           public X509Certificate[ ] getAcceptedIssuers() { return null; }
	           public void checkClientTrusted(X509Certificate[ ] certs, String t) { }
	           public void checkServerTrusted(X509Certificate[ ] certs, String t) { }
	         }
	      };
	      return certs;
	  }
	
}
