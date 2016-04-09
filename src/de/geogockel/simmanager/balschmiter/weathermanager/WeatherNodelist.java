/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.geogockel.simmanager.balschmiter.weathermanager;

import de.geogockel.simmanager.balschmiter.drupalrestapi.RestAPI;
import de.geogockel.simmanager.balschmiter.settings.SystemSettings;
import de.geogockel.simmanager.balschmiter.settings.DrupalSettings;
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
class WeatherNodelist 
{
    SystemSettings settings;
    DrupalSettings drupal;
    RestAPI restapi;
    
    public WeatherNodelist(SystemSettings settings, DrupalSettings drupal, RestAPI restapi)
    {
        this.settings = settings;
        this.drupal = drupal;
        this.restapi = restapi;
    }
    
    public void getDWDStations() throws ProtocolException, IOException, MalformedURLException, ParseException
    {   
        JSONObject weatherstations = restapi.restGET(drupal.DWDWeatherstation, null);
        System.out.println(weatherstations.get("json"));
        
        JSONArray ar = (JSONArray) weatherstations.get("json");
        for (Object ar1 : ar) {
            JSONObject as = (JSONObject) ar1;
            System.out.println(as);
        }
        
    }
}
