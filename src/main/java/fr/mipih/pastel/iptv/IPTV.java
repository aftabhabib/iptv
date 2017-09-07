package fr.mipih.pastel.iptv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.security.cert.X509Certificate;
import java.util.zip.ZipException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mipih.pastel.iptv.util.ManageFileIPTV;
import fr.mipih.pastel.iptv.util.proxy.ProxyUtils;
import fr.mipih.pastel.rpps.util.proxy.model.ProxyParameters;

/**
 * Telechargement IPTV
 *
 */
public class IPTV {

	private static final Logger log = LoggerFactory.getLogger(IPTV.class);

	private String repertoireDestination = null;
	private String racineFichier = null;

	/**
	 * Exécute le batch
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		IPTV rpps = new IPTV();
		int returnCode = rpps.importRPPS(args);
		System.exit(returnCode);
	}

	/**
	 * Lance l'import du IPTV, cad téléchargement sur le site ASIPE, dezippage
	 * et import en Base
	 * @return 
	 */
	private int importRPPS(String[] args) {

		log.debug("Lancement import fichiers IPTV");
		setUncheckSSLValidation();
		forceProtocoleTlsv1();
			
		// Chemin où stocker le fichier
		if (!setInitialDirectory(args)) {
			log.error("L'emplacement du répertoire de stockage du fichier est absent");
			System.exit(1);
		}

		// Racine dese fichiers
		if (!setRacineFichiers(args)) {
			log.error("Il faut une racine pour les fichiers");
			System.exit(1);
		}

		// Gestion du proxy
		if (!setProxyEtablissement(args)) {
			log.error("Erreur lors de la prise en compte du proxy");
			System.exit(1);
		}

		// Gestion du fichier de l'ASIP
		ManageFileIPTV rf = new ManageFileIPTV();
		try {
			rf.downloadFile(repertoireDestination, racineFichier);
			//rf.unzipFile(repertoireDestination+"_temp");
			//rf.changeEncoding(repertoireDestination+"_temp", repertoireDestination);
			//rf.deleteZip();
		} catch (ZipException zipe) {
			log.error("Impossible de dezipper le fichier : " + zipe.getMessage(), zipe);
			return 1;
		} catch (UnsupportedEncodingException uee) {
			log.error("Erreur d'encodage du fichier : " + uee.getMessage(), uee);
			return 2;
		} catch (SocketTimeoutException ste) {
			log.error("Time out lors du téléchargement du fichier : " + ste.getMessage(), ste);
			return 3;
		} catch (FileNotFoundException fnfe) {
			log.error("Fichier introuvable : " + fnfe.getMessage(), fnfe);
			return 4;
		} catch (IOException ioe) {
			log.error("Erreur I/O : " + ioe.getMessage(), ioe);
			return 5;
		} catch (Exception e) {
			log.error("Impossible de manipuler le fichier : " + e.getMessage(), e);
			return 6;
		}
		log.error("Fin du programme");
		return 0;
	}

	private boolean setRacineFichiers(String[] args)
	{
		if (args.length < 2) {
			return false;
		}
		this.racineFichier = args[1];
		log.debug("La racine des fichers est : " + racineFichier);
		return true;
	}

	/**
	 * Positionne le proxy de l'établissement si nécessaire
	 * 
	 * @param args
	 *            : Arguments en paramétre du programme
	 */
	private boolean setInitialDirectory(String[] args) {
		if (args.length < 1) {
			return false;
		}
		this.repertoireDestination = args[0];
		log.debug("Le répertoire de destination est : " + repertoireDestination);
		return true;
	}

	/**
	 * Positionne le proxy de l'établissement si nécessaire
	 * 
	 * @param args
	 *            : Arguments en paramétre du programme
	 */
	private boolean setProxyEtablissement(String[] args) {
		if (args.length < 3) {
			log.debug("Pas de proxy établissement");
			return true;
		}
		// Lecture des informations du proxy en bd
		ProxyParameters p = new ProxyParameters();
		int cpt = 1;
		for (String s : args) {
			if (cpt == 3) {
				p.setProxyServeur(s);
				log.debug("Proxy - serveur  : " + s);
			}
			if (cpt == 4) {
				p.setProxyPort(s);
				log.debug("Proxy - port  : " + s);
			}
			if (cpt == 5) {
				p.setProxyUtilisateur(s);
				log.debug("Proxy - utilisateur  : " + s);
			}
			if (cpt == 6) {
				p.setProxyPass(s);
				log.debug("Proxy - pass  : *********");
			}
			cpt++;
		}
		// Positionnement du proxy si il y en a un
		if (p.getProxyServeur() != null) {
			ProxyUtils proxy = new ProxyUtils();
			return proxy.setProxyInternet(p);
		}
		return true;
	}
	


	/**
	 * Sur AIX, un bug revient souvent avec une JRE 6 : javax.net.ssl.SSLHandshakeException: Received fatal alert: handshake_failure
	 * Le problème vient de l'utilisation de l'ancien protocole SSL.
	 * On force donc la négociation en TLSv1 pour éviter ce désagrément
	 * A noter que d'après ce site :http://stackoverflow.com/questions/34963083/paypal-sandbox-api-javax-net-ssl-sslhandshakeexception-received-fatal-alert-h
	 * Une personne précise qu'il serait peut-être intéressant de préciser les versions. Par exemple :  -Dhttps.protocols=TLSv1.1,TLSv1.2  
	 */
	private void forceProtocoleTlsv1() {
		log.debug("Force le protocole TLSv1");
		System.setProperty("https.protocols", "TLSv1");
	}
	

	/**
	 * Bug sur la JRE 6 : unable to find valid certification path to requested target
	 * La solution trouvée est de désactiver la négociation SSL 
	 */
	private void setUncheckSSLValidation() {

		log.debug("Désactivation de la vérification SSL (bug jdk 1.6)");
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			log.error("Génération de la matrice de sécurité ", e);
		}

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

	}

}
