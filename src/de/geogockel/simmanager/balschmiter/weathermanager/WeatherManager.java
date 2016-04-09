/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.geogockel.simmanager.balschmiter.weathermanager;



import de.geogockel.simmanager.balschmiter.settings.SystemSettings;
import de.geogockel.simmanager.balschmiter.settings.DrupalSettings;
import de.geogockel.simmanager.balschmiter.settings.WeatherAPISettings;
import de.geogockel.simmanager.balschmiter.voronoi.SimVoronoiDiagram;
import de.geogockel.simmanager.balschmiter.voronoi.SimVoronoiDiagramUpdate;
import de.geogockel.simmanager.balschmiter.weatherapi.DWDObject;
import de.geogockel.simmanager.balschmiter.weatherapi.DWDWeatherParser;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.SQLException;
import org.apache.commons.io.IOUtils;
import org.json.*;
import org.json.simple.parser.ParseException;


/**
 *
 * @author Tim
 */
public class WeatherManager {

    /**
     * @throws java.io.IOException
     * @throws java.net.ProtocolException
     * @throws java.net.MalformedURLException
     * @throws org.json.simple.parser.ParseException
     * @throws java.sql.SQLException
     * @throws org.json.JSONException
     * @throws com.vividsolutions.jts.io.ParseException
     */
    
    public static SystemSettings systemsettings;
    public static DrupalSettings drupalsettings;
    public static WeatherAPISettings dwdsettings;
    /**
     *
     * @param args
     * @throws IOException
     * @throws ProtocolException
     * @throws MalformedURLException
     * @throws ParseException
     * @throws SQLException
     * @throws JSONException
     * @throws com.vividsolutions.jts.io.ParseException
     */
    public static void main(String[] args) throws IOException, ProtocolException, MalformedURLException, ParseException, SQLException, JSONException, com.vividsolutions.jts.io.ParseException
    {
        //Settings laden
        systemsettings = new SystemSettings();
        drupalsettings = new DrupalSettings(systemsettings);
        //set weather api settings
        String jsonurl = IOUtils.toString(new URL(drupalsettings.basePath+"/rest/simmanager/weatherapisettings"));
        JSONArray jsonarray = new JSONArray(jsonurl);
        if(jsonarray.length() > 0)
        {
            for(int i = 0; i<jsonarray.length();i++)
            {
                WeatherAPISettings apisettings = new WeatherAPISettings((JSONObject)jsonarray.get(i));
                if("dwd".equals(apisettings.shortname) )
                {
                    dwdsettings = apisettings;
                }
            }
        }
        
        if(args[0].equals("voronoi"))
        {
            weathervoronoi();
        }
        if(args[0].equals("weather"))
        {
            if(dwdsettings.shortname != null && dwdsettings.shortname.equals("dwd"))
            {
                jsonurl = IOUtils.toString(new URL(drupalsettings.basePath+dwdsettings.oldWeatherView));
                jsonarray = new JSONArray(jsonurl);
                if(jsonarray.length() > 0)
                {
                    for(int i = 0; i<jsonarray.length();i++)
                    {
                        DWDObject dwd = new DWDObject((JSONObject) jsonarray.get(i));
                        System.out.println(dwdsettings.tempurl.replace("{id}", dwd.dwdid));
                        System.out.println(dwdsettings.perciurl.replace("{id}", dwd.dwdid));
                        DWDWeatherParser parser = new DWDWeatherParser(dwd);
                    }
                }
            }
        }
    }
    
    
    public static void weathervoronoi() throws IOException, JSONException, com.vividsolutions.jts.io.ParseException, SQLException
    {
        //voronoilogik
        //Lade JSONliste an bestehenden Punkten mit Polygon
        String jsonurl = IOUtils.toString(new URL(drupalsettings.basePath+dwdsettings.existingVoronoiView));
        JSONArray jsonVoronoi = new JSONArray(jsonurl);
        jsonurl = IOUtils.toString(new URL(drupalsettings.basePath+dwdsettings.newVoronoiView));
        JSONArray jsonToVoronoi = new JSONArray(jsonurl);
        SimVoronoiDiagram diagram_old = null;
        SimVoronoiDiagram diagram_new = null;
        if(jsonVoronoi.length() > 0)
        {
            diagram_old = new SimVoronoiDiagram(jsonVoronoi, 0, 0, 0, 0, 0, 0);
        }
        if(jsonToVoronoi.length() > 0)
        {
            diagram_new = new SimVoronoiDiagram(jsonToVoronoi, 0, 0, 0, 0, 0, 0);
        }
        SimVoronoiDiagramUpdate update = new SimVoronoiDiagramUpdate(diagram_old,diagram_new,systemsettings,drupalsettings);
        update.saveinDrupal();
        drupalsettings.voronoi = "0";
        drupalsettings.drupalSaveSettings(systemsettings);
    }        
}
