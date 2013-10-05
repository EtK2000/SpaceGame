package com.etk2000.FileHandling;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileDownloading {
	/**
	 * Method downloads file from URL to a given directory.
	 * 
	 * @param fileURL
	 *            - file URL to download
	 * @param destinationDirectory
	 *            - directory to download file to
	 * @throws IOException
	 */
	public static void download(String fileURL, String destinationFileName) {
		try {
			// File name that is being downloaded
			String downloadedFileName = fileURL.substring(fileURL.lastIndexOf("/") + 1);

			// Open connection to the file
			URL url = new URL(fileURL);
			InputStream is = url.openStream();
			// Stream to the destionation file
			FileOutputStream fos = new FileOutputStream(destinationFileName);

			// Read bytes from URL to the local file
			byte[] buffer = new byte[4096];
			int bytesRead = 0;

			System.out.print("Downloading " + downloadedFileName);
			while ((bytesRead = is.read(buffer)) != -1) {
				System.out.print("."); // Progress bar :)
				fos.write(buffer, 0, bytesRead);
			}
			System.out.println("done!");

			// Close destination stream
			fos.close();
			// Close URL stream
			is.close();
		}
		catch (IOException e) {
			System.out.println(e.getMessage() + ", URL not found!");
		}
	}
}