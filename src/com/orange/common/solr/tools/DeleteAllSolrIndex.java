package com.orange.common.solr.tools;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;

import com.orange.common.solr.SolrClient;



public class DeleteAllSolrIndex {
	public static void main(String[] args) throws SolrServerException, IOException{
		SolrServer server = SolrClient.getSolrServer();
		server.deleteByQuery( "*:*" );
		server.commit();
	}
}
