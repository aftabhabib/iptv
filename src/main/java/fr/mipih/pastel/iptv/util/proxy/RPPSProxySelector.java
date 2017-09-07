package fr.mipih.pastel.iptv.util.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPPSProxySelector extends ProxySelector{
  
  private static final Logger log = LoggerFactory.getLogger(RPPSProxySelector.class);

  private String urlProxy;

  private Integer portProxy;
  
  public RPPSProxySelector(String urlProxy, Integer portProxy) {
     super();
     this.urlProxy = urlProxy;
     this.portProxy = portProxy;
 }
  
  @Override
  public List<Proxy> select(URI uri)
  {
      Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(urlProxy, portProxy));
      List<Proxy> list = new ArrayList<Proxy>();
      list.add(proxy);
      return list;
  } 

  @Override
  public void connectFailed(URI uri, SocketAddress sa, IOException ioe)
  {
      log.error("Connection to " + uri + " failed.");
  } 
}