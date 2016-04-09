/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.geogockel.simmanager.balschmiter.weathermanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Tim
 */
public class WeatherNodes 
{
    //Nodeliste
    
    public String name; //this is a field
    public static String gender = "Male"; //this is a static field

    public WeatherNodes(String basePath, String weatherNodePath) 
    {
        try 
        {
            String surl = "http://"+basePath+weatherNodePath;
            URL url = new URL(surl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                                    + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) 
            {
                System.out.println(output);
            }

            conn.disconnect();

	}
        catch (MalformedURLException e)
        {
            e.printStackTrace();
	}
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
    
