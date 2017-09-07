package fr.mipih.pastel.iptv.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManageFileIPTV {

	private static final Logger log = LoggerFactory.getLogger(ManageFileIPTV.class);


	private static final String HTTPS_URL_FICHIER_IPTV = "https://www.iptv4sat.com/telecharger-iptv-france";

	/**
	 * Recherche du lien vers l'extraction dans la page HTML
	 * 
	 * @throws IOException
	 */
	private String getLink() throws IOException, SocketTimeoutException {

		return null;
	}

	/**
	 * Téléchargement du fichier pointé par le lien
	 * 
	 * @param lien
	 *            : Lien vers le fichier à télécharger
	 * @throws IOException,
	 *             Exception
	 */
	public void downloadFile(String repertoire, String racine) throws IOException, Exception, SocketTimeoutException {
		log.debug("Recherche du lien dans la page " + HTTPS_URL_FICHIER_IPTV);
		Document doc = Jsoup.connect(HTTPS_URL_FICHIER_IPTV).userAgent("Mozilla").get();
		Elements classes = doc.getElementsByClass("attachment-link");
		int compteur = 0;
		log.debug("Nombre de fichiers trouvés : " + classes.size());
		for (Element classe : classes) {
				String linkHref = classe.attr("href");
				log.debug("Téléchargement du fichier " + linkHref);
				URL urlDestination = new URL(linkHref);
		      String destination = repertoire+FilenameUtils.getBaseName(urlDestination.getPath())+".zip";
				log.debug("Destination : " + destination);
				File dstFile = null;
				dstFile = new File(destination);
				URL url = new URL(linkHref);
				URLConnection conn = url.openConnection();
		      conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
		      conn.connect();
				FileUtils.copyInputStreamToFile(conn.getInputStream(), dstFile);
				// unzip
				if (classes.size()==1)
				{
					unzipFile(destination, repertoire+racine+ ".m3u");
				} 
				else
				{
					unzipFile(destination, repertoire+racine+compteur+ ".m3u");
				}				
				// fumage zip
				deleteZip(destination);
				compteur++;
		}
	}

	/**
	 * Unzip le fichier
	 * 
	 * @throws ZipException
	 *             , IOException
	 */
	public void unzipFile(String nomFichier, String nomFichierM3u) throws ZipException, IOException, Exception {
		log.debug("Unzip du fichier " + nomFichier + " vers "+nomFichierM3u );
		UnZipFile.execute(nomFichier, nomFichierM3u);
	}
	
	/**
	 * Change encodfind de UTF-8 en Iso_8859
	 * 
	 * @param nomFichier
	 * @param nomFichierDefinitif
	 * @throws ZipException
	 * @throws IOException
	 * @throws Exception
	 */
	public void changeEncoding(String nomFichier, String nomFichierDefinitif) throws ZipException, IOException, Exception {
		log.debug("change Encoding");
		File file_src = new File(nomFichier);
		File file_dst = new File(nomFichierDefinitif);
    	transform(file_src, "UTF-8", file_dst, "ISO8859_1");
		if (!file_src.delete()) {
			throw new IOException("Impossible de supprimer le fichier temporaire");
		}
	}
	
	
	/**
	 * Supprime le zip en fin de traitement 
	 * 
	 * @param nomFichier
	 * @throws ZipException
	 * @throws IOException
	 * @throws Exception
	 */
    public void deleteZip(String nomFichier) throws ZipException, IOException, Exception {
    	log.debug("Suppression du fichier zip");
		File file = new File(nomFichier);
		if (!file.delete()) {
			throw new IOException("Impossible de supprimer le fichier");
		}
    }

	private static void transform(File source, String srcEncoding, File target, String tgtEncoding) throws IOException {
		InputStreamReader br = null;
	    OutputStreamWriter bw = null;
	    // Création fichier si nécessaire
	    if(!target.exists()) {
	    	target.createNewFile();
	    } 
	    try{
	        br = new InputStreamReader(new FileInputStream(source),srcEncoding);
	        bw = new OutputStreamWriter(new FileOutputStream(target), tgtEncoding);
	        char[] buffer = new char[16384];
	        int read;
	        while ((read = br.read(buffer)) != -1)
	            bw.write(buffer, 0, read);
	    } finally {
	        try {
	            if (br != null)
	                br.close();
	        } finally {
	            if (bw != null)
	                bw.close();
	        }
	    }
	}

}
