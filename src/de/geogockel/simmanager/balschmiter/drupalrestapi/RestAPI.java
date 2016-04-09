/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.geogockel.simmanager.balschmiter.drupalrestapi;

import de.geogockel.simmanager.balschmiter.settings.SystemSettings;
import de.geogockel.simmanager.balschmiter.settings.DrupalSettings;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.xml.bind.DatatypeConverter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Tim
 */
public class RestAPI {
    
    private final String authentification;
    private final String authentification_coded;    
    private final String serviceurl;
    
    
    public RestAPI(SystemSettings settings, DrupalSettings drupal)
    {
        //build authentification
        this.authentification = settings.drupalUser+':'+settings.drupalpassword;
        this.authentification_coded = DatatypeConverter.printBase64Binary(authentification.getBytes());
        //build base url
        this.serviceurl = drupal.basePath;   
    }
    
    /*
    *HttpURLConnection kennt kein Patch
    *mit conn.setRequestProperty("X-HTTP-Method-Override", "PATCH"); wird das verwendete POST überschrieben
    */
    
    public void restPATCH(JSONObject json,String entity,int entityID) throws MalformedURLException, ProtocolException, IOException
    {
        URL url = new URL(this.serviceurl+entity+entityID);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Basic " + this.authentification_coded);
        conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
        conn.setRequestMethod("POST"); 
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setUseCaches (false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        //Request Data
        DataOutputStream wr = new DataOutputStream (conn.getOutputStream());
        wr.writeBytes (json.toJSONString());
        wr.flush ();
        wr.close ();

        if (conn.getResponseCode() != 204) 
        {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        while ((output = br.readLine()) != null) 
        {
            System.out.println(output);
        }
        conn.disconnect();
    }
    /*
    * Funktioniert
    */
    public void restPOST(JSONObject json,String entity) throws MalformedURLException, ProtocolException, IOException
    {
        URL url = new URL(this.serviceurl+entity);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Basic " + this.authentification_coded);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setUseCaches (false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        //Request Data
        //Send request
        DataOutputStream wr = new DataOutputStream (conn.getOutputStream());
        wr.writeBytes (json.toJSONString());
        wr.flush ();
        wr.close ();

        if (conn.getResponseCode() != 201) 
        {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        while ((output = br.readLine()) != null) 
        {
            System.out.println(output);
        }
        conn.disconnect();
    }
    
    public void restDELETE(String entity, int entityID) throws MalformedURLException, ProtocolException, IOException
    {
        URL url = new URL(this.serviceurl+entity+entityID);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Basic " + this.authentification_coded);
        conn.setRequestMethod("DELETE");

        if (conn.getResponseCode() != 204) 
        {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        while ((output = br.readLine()) != null) 
        {
            System.out.println(output);
        }
        conn.disconnect();
    }
    
    public JSONObject restGET(String entity, String entityID) throws MalformedURLException, ProtocolException, IOException, ParseException
    {
        URL url;
        if(entityID == null)
        {
            url = new URL(this.serviceurl+entity);
        }
        else
        {
            url = new URL(this.serviceurl+entity+entityID+"?_format=json");
        }
        System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //conn.setRequestProperty("Authorization", "Basic " + this.authentification_coded);
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) 
        {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String builder = "";
        String line;
        while ((line = br.readLine()) != null) 
        {
            builder = builder + line + "\n";
        }
        
        if(new JSONParser().parse(builder) instanceof JSONObject)
        {
            return (JSONObject) new JSONParser().parse(builder);
        }
        else
        {
            JSONObject jo = new JSONObject();
            jo.put("json",new JSONParser().parse(builder));
            return jo;
        }
    }
}
