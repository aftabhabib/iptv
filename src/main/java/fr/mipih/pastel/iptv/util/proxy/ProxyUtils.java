package fr.mipih.pastel.iptv.util.proxy;

import java.net.Authenticator;
import java.net.ProxySelector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mipih.pastel.rpps.util.proxy.model.ProxyParameters;

public class ProxyUtils {

	private static final Logger log = LoggerFactory.getLogger(ProxyUtils.class);

	public boolean setProxyInternet(ProxyParameters p) {
		log.debug("Gestion du proxy établissement");
		String urlProxy = p.getProxyServeur();
		String portProxy = p.getProxyPort();
		String userProxy = p.getProxyUtilisateur();
		String pswdProxy = p.getProxyPass();

		boolean proxy = true;
		int noPortProxy;
		// controle syntaxique si proxy renseigné, et initialisation du proxy si
		// ok
		if (urlProxy != null && urlProxy.length() > 0) {
			if (portProxy == null) {
				log.error("Echec accès à CdriWS : Port du proxy '{}' non renseigné." + urlProxy);
				return false;
			}
			try {
				noPortProxy = Integer.valueOf(portProxy);
			} catch (NumberFormatException e) {
				log.error(
						"Echec accès à CdriWS : n° Port '{}' du proxy '{}' non conforme." + portProxy + " " + urlProxy);
				e.printStackTrace();
				return false;
			}
			ProxySelector.setDefault(new RPPSProxySelector(urlProxy, noPortProxy));
			Authenticator.setDefault(new ProxyAuthenticator(userProxy, pswdProxy));
		}
		return proxy;
	}
}
