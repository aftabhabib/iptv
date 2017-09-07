package fr.mipih.pastel.iptv.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mipih.pastel.iptv.IPTV;

/**
 * Classe pour dezipper un fichier contenu dans un zip
 * 
 * source :
 * http://www.speakingcs.com/2015/07/how-to-unzipextract-files-from-zip.html
 * 
 * 
 * @author capdecomme
 *
 */

public class UnZipFile {
	
	private static final Logger log = LoggerFactory.getLogger(UnZipFile.class);

	public static boolean execute(String source, String destination) throws ZipException, IOException, Exception {
		UnZipFile uzf = new UnZipFile();
		
		//return uzf.unZipFile(new File(source), new File(destination));
		return unzipFileIntoDirectory(new File(source), new File(destination)); 
	}
	
	
	private static boolean unzipFileIntoDirectory(File archive, File destinationDir) 
		    throws Exception {
		    final int BUFFER_SIZE = 1024;
		    BufferedOutputStream dest = null;
		    FileInputStream fis = new FileInputStream(archive);
		    ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
		    ZipEntry entry;
//		    File destFile;
		    while ((entry = zis.getNextEntry()) != null) {
		            int count;
		            byte data[] = new byte[BUFFER_SIZE];
		            FileOutputStream fos = new FileOutputStream(destinationDir);
		            dest = new BufferedOutputStream(fos, BUFFER_SIZE);
		            while ((count = zis.read(data, 0, BUFFER_SIZE)) != -1) {
		                dest.write(data, 0, count);
		            }
		            dest.flush();
		            dest.close();
		            fos.close();
		    }
		    zis.close();
		    fis.close();
		    return true;
		}
	

//	private boolean unZipFile(File src, File dst) throws ZipException, IOException {
//		if(!src.exists() || src.isDirectory()) { 
//		    log.error("Le fichier est introuvable");
//			return false;
//		}
//		ZipFile zFile = new ZipFile(src);
//		Enumeration<? extends ZipEntry> entries = zFile.entries();
//		ZipEntry anEntry = (ZipEntry) entries.nextElement();
//		if (!anEntry.isDirectory()) {
//			return saveEntry(zFile, anEntry, dst);
//		}
//		if (zFile != null) {
//			zFile.close();
//		}
//		return false;
//	}
//
//	private boolean saveEntry(ZipFile zFile, ZipEntry anEntry, File newPath) throws IOException {
//		InputStream in = null;
//		BufferedOutputStream fos = null;
//		try {
//			in = zFile.getInputStream(anEntry);
//			fos = new BufferedOutputStream(new FileOutputStream(newPath));
//			byte[] buffer = new byte[1024];
//			int length;
//			while ((length = in.read(buffer)) > 0) {
//				fos.write(buffer, 0, length);
//			}
//		} finally {
//			if (zFile != null) {
//				zFile.close();
//			}
//			if (in != null) {
//				in.close();
//			}
//			if (fos != null) {
//				fos.close();
//			}
//		}
//		return true;
//	}
}