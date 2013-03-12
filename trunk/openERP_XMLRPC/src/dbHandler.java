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
 * Classe qui s'occupe des communiquations vers la base de donn�es de openERP
 * @author Kid Dynamite
 * @see http://doc.openerp.com/v6.0/developer/6_22_XML-RPC_web_services/index.html#id1
 * @see http://doc.openerp.com/v6.0/developer/6_21_web_services/index.html
 * @see http://doc.openerp.com/v6.0/developer/2_5_Objects_Fields_Methods/methods.html
 * @see http://sourceforge.net/p/openerpjavaapi/wiki/Home/ (Librairie utilis�e pour la communication xml-rpc)
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
	private ObjectAdapter emplAd = null ;


	/**
	 * Constructeur de la classe avec h�te et port en param�tres, d�fini les params puis
	 * se connecte � l'hote sp�cifi�
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

			//this.openERPSession = new Session(host,port,bdPass,username,erpBdName);
			openERPSession.startSession();
			//ObjectAdapter partnerAd = openERPSession.getObjectAdapter("res.partner");
			System.out.println("Connected to : "+this.host);
			System.out.println("Browsing object hr.employee");
			emplAd = openERPSession.getObjectAdapter("hr.employee");

		} catch (Exception e) {
			System.out.println("Error while reading data from server:\n\n" + e.getMessage());
		}
	}


	public void getEmployees () throws XmlRpcException, OpeneERPApiException{	

		try {

			FilterCollection filters = new FilterCollection();
			filters.add("active","=",true);
			RowCollection partners = emplAd.searchAndReadObject(filters, new String[]{"name","work_email"});

			for (Row row : partners ){

				System.out.println("Row ID: " + row.getID());
				System.out.println("Name:" + row.get("name"));
				System.out.println("Email:" + row.get("work_email"));
				System.out.println("-------------------------------------------------------");
			}
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OpeneERPApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void getEmployee (String name) throws XmlRpcException, OpeneERPApiException{	

		try {

			FilterCollection filters = new FilterCollection();
			filters.add("name","=",name);
			RowCollection partners = emplAd.searchAndReadObject(filters, new String[]{"name","work_email"});

			for (Row row : partners ){

				System.out.println("Row ID: " + row.getID());
				System.out.println("Name:" + row.get("name"));
				System.out.println("Email:" + row.get("work_email"));
				System.out.println("-------------------------------------------------------");
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
	 * @param name
	 * @param newMail
	 * @throws Exception 
	 */
	public void updateField(String FieldName ,String name, String newValue) throws Exception{	

		FilterCollection filters = new FilterCollection();
		filters.add("name","=",name);		
		RowCollection employees = emplAd.searchAndReadObject(filters, new String[]{"name",FieldName});

		// You could do some validation here to see if the customer was found
		Row emplRow = employees.get(0);
		System.out.println("Old value : "+emplRow.get(FieldName));

		emplRow.put(FieldName, newValue);


		// Tell writeObject to only write changes ie the name isn't updated because it wasn't changed.
		boolean success = emplAd.writeObject(emplRow, true);

		if (success)
			System.out.println("Update was successful to employee : "+name+", on field : "+FieldName+" with new value : "+newValue);

	}

	public void createEmployee(String name, String mail) throws XmlRpcException, OpeneERPApiException{
		Row newPartner = emplAd.getNewRow(new String[]{"name", "work_email"});
		newPartner.put("name", name);
		newPartner.put("work_email", mail);
		emplAd.createObject(newPartner);

		System.out.println("New Employee ID: " + newPartner.getID());
	}

	/**
	 * M�thode qui affiche � la console la list des base de donn�es trouv�e sur l'h�te 
	 * et le port pass�s en param�tres
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
