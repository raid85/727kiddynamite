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

		//Se connecter à la DB
		dbHandler myDB = new dbHandler ("10.194.32.165", 8069, "EQ05_BD_TP1", "admin", "Aquar1um!");
		//Print une liste de tout les employés
		
		myDB.getEmployees();
		//Change la valeur email de l'employé
		//myDB.updateField("work_email","Marc", "Marc@yahoo.com");	
		//Changer l'adresse d'un employé
//		myDB.updateField("mobile_phone","Marc", "514-123-4567");	
		//Créer un employé
//		myDB.createEmployee("Jonathan Vasco","fuckyou@vasco.com");
		

	}
}