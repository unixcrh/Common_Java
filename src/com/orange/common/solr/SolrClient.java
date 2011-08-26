package com.orange.common.solr;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;

public class SolrClient {
	static final String SOLR_SERVER_URL = "http://localhost:8099/solr";
	CommonsHttpSolrServer server;
	
	public static CommonsHttpSolrServer getSolrServer() {
		return getInstance().server;
	}
	
	public static boolean commit(){
		try {
			getInstance().server.commit();
			return true;
		} catch (SolrServerException e) {			
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static SolrClient getInstance(){     
        return SingletonContainer.instance;     
    }     
	
	private static class SingletonContainer{     
        private static SolrClient instance = new SolrClient();     
    }     
	
	private SolrClient(){
		try {
			String address = System.getProperty("solr.address");
			String portStr = System.getProperty("solr.port");
			int port = 8099;
			
			if (address == null){
				address = "localhost";
			}
			if (portStr != null){
				port = Integer.parseInt(portStr);
			}
			
			String solrURL = "http://".concat(address).concat(":").concat(String.valueOf(port)).concat("/solr");			
			server = new CommonsHttpSolrServer(solrURL);
		} catch (MalformedURLException e) {
			e.printStackTrace(); //TODO
		}
		server.setSoTimeout(10000); // socket read timeout
		server.setConnectionTimeout(1000);
		server.setDefaultMaxConnectionsPerHost(20);
		server.setMaxTotalConnections(100);
		server.setFollowRedirects(false); // defaults to false
		server.setAllowCompression(true);
		server.setMaxRetries(1); // defaults to 0. > 1 not recommended.	
	}
	
}
