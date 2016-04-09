/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.geogockel.simmanager.balschmiter.weatherapi;

import de.geogockel.simmanager.balschmiter.settings.SystemSettings;
import de.geogockel.simmanager.balschmiter.settings.DrupalSettings;
import de.geogockel.simmanager.balschmiter.drupalrestapi.RestAPI;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


/**
 *
 * @author Tim
 */
public class WeatherAPI 
{
    //DWD Setting Parameter
    String DWDtempURL;
    String DWDperciURL;
    
    public WeatherAPI(SystemSettings settings, DrupalSettings drupal, RestAPI restapi) throws ProtocolException, IOException, MalformedURLException, ParseException
    {
        //build Settings for DWD
        JSONObject jsonSettings = restapi.restGET(drupal.entityNodeGET, drupal.DWDNode);
        JSONArray ar = (JSONArray) jsonSettings.get("field_key_url_pair");
        for (Object ar1 : ar) {
            JSONObject as = (JSONObject) ar1;
            String[] url = as.get("value").toString().split("=>");
            if(url[0].equals("temp"))
            {
                this.DWDtempURL = url[1];
            }
            if(url[0].equals("preci"))
            {
                this.DWDperciURL = url[1];
            }
        }
    }
}
