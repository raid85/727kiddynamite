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

public class Main {

	public static void main(String[] args) throws Exception  {

		//Se connecter � la DB
		dbHandler myDB = new dbHandler ("10.194.32.165", 8069, "EQ05_BD_TP1", "admin", "Aquar1um!");
		//Print une liste de tout les employ�s
		
		myDB.getEmployees();
		//Change la valeur email de l'employ�
		//myDB.updateField("work_email","Marc", "Marc@yahoo.com");	
		//Changer l'adresse d'un employ�
//		myDB.updateField("mobile_phone","Marc", "514-123-4567");	
		//Cr�er un employ�
//		myDB.createEmployee("Jonathan Vasco","fuckyou@vasco.com");
		

	}
}