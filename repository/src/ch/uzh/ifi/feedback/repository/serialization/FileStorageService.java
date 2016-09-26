package ch.uzh.ifi.feedback.repository.serialization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.Part;

import ch.uzh.ifi.feedback.repository.model.FileFeedback;

public class FileStorageService {
	
	public <T extends FileFeedback> T ParseFilePart(Part filePart, T feedback, String storeagePath) {

			InputStream inputStream = null;
			OutputStream outputStream = null;

			try {
				inputStream = filePart.getInputStream();
				int fileSize = (int) filePart.getSize();
				String fileName = getFileName(filePart);
				
				String fileExtension = "";
				
				if(filePart.getContentType().equals("image/png")) {
					fileExtension = ".png";
				} else if (filePart.getContentType().equals("image/jpeg")) {
					fileExtension = ".jpg";
				} else if (filePart.getContentType().equals("image/tiff")) {
					fileExtension = ".tiff";
				} else if (filePart.getContentType().equals("image/gif")) {
					fileExtension = ".gif";
				}
				
				// more or less unique filename
				String fileNameOfStoredFile = Integer.toString(fileSize) + "_" + String.valueOf(new Date().getTime()) + fileExtension;				
				
				File outputFile = new File(storeagePath, fileNameOfStoredFile);
				outputStream = new FileOutputStream(outputFile);
				
				int read = 0;
				byte[] bytes = new byte[fileSize];
				
				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
				
				feedback.setFileExtension(fileExtension);
				feedback.setPath(outputFile.toPath().toString());
				feedback.setSize(fileSize);
				feedback.setName(fileName);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}

		return feedback;
	}
	
	public String CreateDirectory(String directoryName)
	{
		String rootPath = System.getProperty("catalina.home");
		String relativePath = "webapps" + File.separator + directoryName;
		String uploadsStoragePath = rootPath + File.separator + relativePath;
		File uploadDirectory = new File(uploadsStoragePath);
		if (!uploadDirectory.exists()) {
			uploadDirectory.mkdirs();
		}
		
		return uploadsStoragePath;
	}
	
	private static String getFileName(Part filePart) {
		String header = filePart.getHeader("content-disposition");
		if (header == null)
			return null;
		for (String headerPart : header.split(";")) {
			if (headerPart.trim().startsWith("filename")) {
				return headerPart.substring(headerPart.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
}