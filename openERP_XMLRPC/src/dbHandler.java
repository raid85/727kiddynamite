import java.net.MalformedURLException;
import java.net.URL;

import java.util.Vector;

import org.apache.*;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.debortoliwines.openerp.api.FilterCollection;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;
import com.debortoliwines.openerp.api.Session;

/**
 * Classe qui s'occupe des communiquations vers la base de données de openERP
 * @author Kid Dynamite
 * @see http://doc.openerp.com/v6.0/developer/6_22_XML-RPC_web_services/index.html#id1
 * @see http://doc.openerp.com/v6.0/developer/6_21_web_services/index.html
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
	private String erpBdName = null ;
	private String bdPass = null;
	private String username = null;
	private Session openERPSession = null;
	private ObjectAdapter partnerAd = null ;
	

	/**
	 * Constructeur de la classe avec hôte et port en paramètres, défini les params puis
	 * se connecte à l'hote spécifié
	 * @param host
	 * @param port
	 * @param erpBdName 
	 * @param username 
	 * @param bdPass 
	 */
	public dbHandler (String host, int port, String bdPass, String username, String erpBdName){
		this.host = host ;
		this.port = port ;
		this.bdPass = bdPass ;
		this.username = username ;
		this.erpBdName=erpBdName;
		this.openERPSession = new Session("10.194.32.165", 8069, "EQ05_BD_TP1", "admin", "Aquar1um!");
		try {
		    
		    openERPSession.startSession();
		    //ObjectAdapter partnerAd = openERPSession.getObjectAdapter("res.partner");
		    System.out.println("Connected to : "+this.host);
		  
		} catch (Exception e) {
		    System.out.println("Error while reading data from server:\n\n" + e.getMessage());
		}
	}
	public void getEmployee (String employeeName ){
		employeeName = "";
		
		try {
			partnerAd = openERPSession.getObjectAdapter("hr.employee");
			FilterCollection filters = new FilterCollection();
			filters.add("active","=",true);
			RowCollection partners = partnerAd.searchAndReadObject(filters, new String[]{"name","email"});
			for (Row row : partners){
			    System.out.println("Row ID: " + row.getID());
			    System.out.println("Name:" + row.get("name"));
			    System.out.println("Email:" + row.get("email"));
			}
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OpeneERPApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		System.err.println("Found following DB's at: "+host+":"+port);
		
		System.err.println("---------------------------------------------");
		Vector<String> res = new Vector<String>();
		for (int i = 0; i < a.length; i++) {
			if (a[i] instanceof String){res.addElement((String)a[i]);}
			System.out.println("--->  "+res.get(i));}

	}

	
	

}
