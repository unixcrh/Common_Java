package com.orange.common.utils;

import java.io.IOException;

import javax.servlet.ServletInputStream;

public class ServletUtil {

	
	/**
	 * Read the next line of input.
	 * 
	 * @return a String containing the next line of input from the stream, or
	 *         null to indicate the end of the stream.
	 * @exception IOException
	 *                if an input or output exception has occurred.
	 */
	public static String readLine(ServletInputStream in, int length) throws IOException {
		if (in == null || length <= 0){
			return null;
		}
		
		byte[] buf = new byte[length+1];
		StringBuffer sbuf = new StringBuffer();
		int result;
		// String line;

		do {
			result = in.readLine(buf, 0, buf.length); // does +=
			if (result != -1) {
				sbuf.append(new String(buf, 0, result, "UTF-8"));
			}
			else{
				break;
			}
		} while (result == buf.length); // loop only if the buffer was filled

		if (sbuf.length() == 0) {
			return null; // nothing read, must be at the end of stream
		}
		
		return sbuf.toString();
	}	
}
