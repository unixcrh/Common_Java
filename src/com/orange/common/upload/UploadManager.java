package com.orange.common.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;

import com.orange.common.utils.FileUtils;

public class UploadManager {

	static final Logger log = Logger.getLogger(UploadManager.class.getName());
	static String ENCODING_UTF8 = "UTF-8";
	
	public static UploadFileResult uploadFile(HttpServletRequest request, String localDir, String remoteDir) {
		String localPath = "";
		String httpPath = "";
		try {
			request.setCharacterEncoding(ENCODING_UTF8);
			ServletFileUpload upload = new ServletFileUpload();
//			upload.setProgressListener(progressListener);
			if (!ServletFileUpload.isMultipartContent(request)) {
				log.info("<uploadFile> the request doesn't contain a multipart/form-data "
								+ "or multipart/mixed stream, content type header is null .");
				return new UploadFileResult(UploadErrorCode.ERROR_NO_MIME_DATA, localPath, httpPath);
			}

			FileItemIterator iter = upload.getItemIterator(request);
			if (iter == null) {
				log.info("<uploadFile> the item iterator is null.");
				return new UploadFileResult(UploadErrorCode.ERROR_NO_FORM_ITEM, localPath, httpPath);
			}
			
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				String name = item.getFieldName();
				InputStream stream = item.openStream();
				if (item.isFormField()) {
					log.info("<uploadFile> Form field " + name + " with value "
							+ Streams.asString(stream) + " detected.");
				} else {
					log.info("<uploadFile> File field " + name
							+ " with file name " + item.getName()
							+ " detected.");
					String filename = "";
					if (item.getName() != null) {
						filename = item.getName();
					}
					// Process the input stream
					ArrayList<Integer> byteArray = new ArrayList<Integer>();
					int tempByte;
					do {
						tempByte = stream.read();
						byteArray.add(tempByte);
					} while (tempByte != -1);
					stream.close();

					int size = byteArray.size();
					log.info("<uploadFile> total " + size + " bytes read");

					byteArray.remove(size - 1);
					byte[] bytes = new byte[size];
					int i = 0;
					for (Integer tByte : byteArray) {
						bytes[i++] = tByte.byteValue();
					}
					
					// generate direcotry
					String timeDir = getTimeFilePath();
					String dir = localDir + timeDir;
					FileUtils.createDir(dir);
					
					// generate file name
					String generateFileName = getTimeFileName(filename);
					
					// construction path and write file
					localPath = dir + "/" + generateFileName;					
					
					// construct return http path										
					httpPath = remoteDir + timeDir + "/" + generateFileName;

					// write to file
					log.info("<uploadFile> write to file=" + localPath + 
							", http path = " + httpPath);
					FileOutputStream fw = new FileOutputStream(localPath);
					fw.write(bytes);
					fw.close();										
				}
			}
		} catch (Exception e) {			
			log.error("<uploadFile> file path=" + localPath
					+ ", but catch exception=" + e.toString(), e);
			return new UploadFileResult(UploadErrorCode.ERROR_UPLOAD_EXCEPTION, "", "");
		}
		
		if (localPath.length() > 0 && httpPath.length() > 0){
			return new UploadFileResult(UploadErrorCode.ERROR_SUCCESS, localPath, httpPath);
		}
		else{
			return new UploadFileResult(UploadErrorCode.ERROR_NO_DATA, localPath, httpPath);			
		}
	}

	private static String getTimeFileName(String filename) {
		return TimeUUIDUtils.getUniqueTimeUUIDinMillis().toString() + "_" + filename;
	}

	private static String getTimeFilePath() {
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String datePath = formatter.format(now);
		return datePath;
	}

	// Create a progress listener
	ProgressListener progressListener = new ProgressListener() {
		public void update(long pBytesRead, long pContentLength, int pItems) {
			System.out.println("We are currently reading item " + pItems);
			if (pContentLength == -1) {
				System.out.println("So far, " + pBytesRead
						+ " bytes have been read.");
			} else {
				System.out.println("So far, " + pBytesRead + " of "
						+ pContentLength + " bytes have been read.");
			}
		}
	};

}
