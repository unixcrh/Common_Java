package com.orange.common.network.utils;



import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;

public class ChannelUtils {

	protected static final Logger logger = Logger.getLogger("ChannelUtils");
	
	public static String getChannelState(Channel channel){
		return " bound=" + channel.isBound() + " connected=" + channel.isConnected() + " open=" + channel.isOpen();		
	}

}
