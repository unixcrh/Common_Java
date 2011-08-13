package com.orange.common.solr;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;

public class SolrClient {
	static final String SOLR_SERVER_URL = "http://localhost:8080/solr";
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
			server = new CommonsHttpSolrServer(SOLR_SERVER_URL);
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
