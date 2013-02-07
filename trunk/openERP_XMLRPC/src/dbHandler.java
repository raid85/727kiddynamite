import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;

import org.apache.*;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * Classe qui s'occupe des communiquations vers la base de données de openERP
 * @author Kid Dynamite
 * @see http://doc.openerp.com/v6.0/developer/6_22_XML-RPC_web_services/index.html#id1
 * @see http://doc.openerp.com/v6.0/developer/6_21_web_services/index.html
 * @see http://doc.openerp.com/v6.0/developer/2_5_Objects_Fields_Methods/methods.html
 * @see http://doc.openerp.com/v6.0/developer/2_5_Objects_Fields_Methods/methods.html
 *
 */
public class dbHandler {

	private int id = -1;
	private XmlRpcClientConfigImpl xmlrpcConfigDb = new XmlRpcClientConfigImpl();
	private XmlRpcClient xmlrpcDb = new XmlRpcClient();
	private XmlRpcClient xmlrpcLogin = new XmlRpcClient();
	private String host = null ;
	private int port = -1 ;

	/**
	 * Constructeur de la classe avec hôte et port en paramètres
	 * @param host
	 * @param port
	 */
	public dbHandler (String host, int port){
		this.host = host ;
		this.port = port ;
	}
	/**
	 * Méthode qui affiche à la console la list des base de données trouvée sur l'hôte 
	 * et le port passés en paramètres
	 * @param host
	 * @param port
	 * @throws MalformedURLException 
	 * @throws XmlRpcException 
	 */
	public void getDatabaseList() throws MalformedURLException, XmlRpcException {		

		xmlrpcConfigDb.setEnabledForExtensions(true);
		xmlrpcConfigDb.setServerURL(new URL("http",this.host,this.port,"/xmlrpc/db"));
		xmlrpcDb.setConfig(xmlrpcConfigDb);

		//Retrieve databases
		Vector<Object> params = new Vector<Object>();
		Object result = xmlrpcDb.execute("list", params);
		Object[] a = (Object[]) result;
		System.out.println("Found following DB's at: "+host+":"+port);
		System.out.println("---------------------------------------------");
		Vector<String> res = new Vector<String>();
		for (int i = 0; i < a.length; i++) {
			if (a[i] instanceof String){res.addElement((String)a[i]);}
			System.out.println("--->  "+res.get(i));}

	}
	/**
	 * Méthode qui se connecte à la base de donnée spécifiée sur l'hôte spécifé avec login/pass
	 * et qui récupère le id de l'utilisateur pour utlisation ultérieure
	 * @param host
	 * @param port
	 * @param tinydb
	 * @param login
	 * @param password
	 */
	public void Connect(String tinydb, String login, String password){


		XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
		xmlrpcConfigLogin.setEnabledForExtensions(true);

		try {
			xmlrpcConfigLogin.setServerURL(new URL("http",host,port,"/xmlrpc/common"));

		} catch (MalformedURLException e1) {
			System.err.println("Something bad happened in setting the server url !! ");
			e1.printStackTrace();
		}

		xmlrpcLogin.setConfig(xmlrpcConfigLogin);

		try {
			//Connect
			Object[] params = new Object[] {tinydb,login,password};
			Object id = xmlrpcLogin.execute("login", params);
			if(Integer.valueOf(id.toString())==1){this.id = Integer.valueOf(id.toString()) ;
			System.out.println("");
			System.out.println("Succesfully logged in to web services at "+tinydb+"/xmlrpc/common");}

		}
		catch (XmlRpcException e) {
			e.printStackTrace();
		}


	}
	/**
	 * Méthode qui change le url pour /xmlrpc/object après le login pour permettre les 
	 * requete xmlrpc
	 * @throws MalformedURLException
	 */
	public void switchToObject () throws MalformedURLException{

		xmlrpcDb = new XmlRpcClient();
		xmlrpcConfigDb = new XmlRpcClientConfigImpl();
		xmlrpcConfigDb.setEnabledForExtensions(true);
		xmlrpcConfigDb.setServerURL(new URL("http", host, port, "/xmlrpc/object"));
		xmlrpcDb.setConfig(xmlrpcConfigDb);
		System.out.println("Switched client to /xmlrpc/object");
		System.out.println("----Now ready for xmlrpc object oriented requests----");
	}


	/**
	 * Méthode qui retourne (et affiche) les champs d'un objet donné dans la 
	 * base de données
	 * @param openERPbdObject
	 * @throws XmlRpcException
	 * @throws MalformedURLException
	 */
	public Object readObjectFields (String openERPbdObject) throws XmlRpcException{

		Vector<Object> arg = new Vector<Object>();

		arg.add("EQ05_BD_TP1");
		arg.add(this.id);
		arg.add("Aquar1um!");
		arg.add(openERPbdObject);
		arg.add("fields_view_get");
		//		arg.add(null);

		Object ret_id = null;

		ret_id = xmlrpcDb.execute("execute", arg);
		System.out.println("Those are the fields for the object : "+openERPbdObject);
		
		System.out.println(ret_id.toString());
		System.out.println(ret_id.toString().trim());
		return ret_id ;

	}


}
