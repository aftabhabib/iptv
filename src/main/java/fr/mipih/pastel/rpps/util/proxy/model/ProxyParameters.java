package fr.mipih.pastel.rpps.util.proxy.model;

public class ProxyParameters 
{
	private String proxyPort;
	private String proxyServeur;	
	private String proxyUtilisateur;
	private String proxyPass;
	
	public ProxyParameters() {
		super();
		proxyPort="";
		proxyServeur="";
		proxyUtilisateur="";
		proxyPass="";
	}
	
	public String getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}
	public String getProxyServeur() {
		return proxyServeur;
	}
	public void setProxyServeur(String proxyServeur) {
		this.proxyServeur = proxyServeur;
	}
	public String getProxyUtilisateur() {
		return proxyUtilisateur;
	}
	public void setProxyUtilisateur(String proxyUtilisateur) {
		this.proxyUtilisateur = proxyUtilisateur;
	}
	public String getProxyPass() {
		return proxyPass;
	}
	public void setProxyPass(String proxyPass) {
		this.proxyPass = proxyPass;
	}

}
