/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.geogockel.simmanager.balschmiter.settings;

import org.json.JSONException;
import org.json.JSONObject;



/**
 *
 * @author Tim
 */
public class WeatherAPISettings 
{
    public String nid;
    public String shortname;
    public String tempurl;
    public String perciurl;
    public String bothurl;
    public String oldWeatherView;
    public String newVoronoiView;
    public String existingVoronoiView;
    
    /**
     * @param jsonObject
     * @throws org.json.JSONException
     */
    public WeatherAPISettings(JSONObject jsonObject) throws JSONException 
    {
        //System.out.println(jsonObject);
        this.nid = jsonObject.getJSONArray("nid").getJSONObject(0).getString("value");
        this.shortname = jsonObject.getJSONArray("field_simshortname").getJSONObject(0).getString("value");
        this.existingVoronoiView = jsonObject.getJSONArray("field_voronoi").getJSONObject(0).getString("value");
        this.newVoronoiView = jsonObject.getJSONArray("field_tovoronoi").getJSONObject(0).getString("value");
        this.oldWeatherView = jsonObject.getJSONArray("field_oldweatherview").getJSONObject(0).getString("value");
        
        for(int i = 0; i<jsonObject.getJSONArray("field_simurls").length();i++)
        {
            String [] tmp = jsonObject.getJSONArray("field_simurls").getJSONObject(i).getString("value").split("=>");
            if(tmp[0].equals("temp")){this.tempurl = tmp[1];}
            if(tmp[0].equals("preci")){this.perciurl = tmp[1];}
            if(tmp[0].equals("both")){this.bothurl = tmp[1];}
        }
    } 
}
