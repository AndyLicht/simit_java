package de.geogockel.simmanager.balschmiter.settings;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Tim
 */
public class SystemSettings 
{
   
    public String drupalUser;
    public String drupalpassword;
    String databaseHost;
    String databasePort;
    String databaseName;
    String databaseUser;
    String databasePassword;
    String drupalConfigName;
    
    public SystemSettings()
    {
        try
        {
            File settings_xml = new File("C:\\Users\\Tim\\Documents\\NetBeansProjects\\weatherManager\\conf\\settings.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(settings_xml);
            doc.getDocumentElement().normalize();

            this.drupalUser = doc.getElementsByTagName("drupalUser").item(0).getTextContent();
            this.drupalpassword = doc.getElementsByTagName("drupalPassword").item(0).getTextContent();
            this.drupalConfigName = doc.getElementsByTagName("drupalConfigName").item(0).getTextContent();
            this.databaseHost = doc.getElementsByTagName("databaseHost").item(0).getTextContent();
            this.databasePort = doc.getElementsByTagName("databasePort").item(0).getTextContent();
            this.databaseName = doc.getElementsByTagName("databaseName").item(0).getTextContent();
            this.databaseUser = doc.getElementsByTagName("databaseUser").item(0).getTextContent();
            this.databasePassword = doc.getElementsByTagName("databasePassword").item(0).getTextContent();
        }
        catch(ParserConfigurationException | SAXException | IOException | DOMException e){}
    }
}
