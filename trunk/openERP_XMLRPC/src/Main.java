import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.apache.*;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class Main {

	public static void main(String[] args) throws InterruptedException, XmlRpcException, MalformedURLException  {

		dbHandler myDB = new dbHandler ("10.194.32.165", 8069);

		myDB.getDatabaseList() ; 

		Thread.sleep(1000);

		myDB.Connect( "EQ05_BD_TP1", "admin", "Aquar1um!");
		myDB.switchToObject() ;
		myDB.readObjectFields("res.partner");



	}
}