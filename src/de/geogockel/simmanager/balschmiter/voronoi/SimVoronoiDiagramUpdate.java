package de.geogockel.simmanager.balschmiter.voronoi;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.json.simple.JSONObject;
import de.geogockel.simmanager.balschmiter.settings.DrupalSettings;
import de.geogockel.simmanager.balschmiter.drupalrestapi.RestAPI;
import de.geogockel.simmanager.balschmiter.settings.SystemSettings;

/**
 *
 * @author Balschmiter
 */
public class SimVoronoiDiagramUpdate 
{

    public List <SimVoronoiDiagramFeature> featureList = new ArrayList<>();
    protected SystemSettings settings;
    protected DrupalSettings drupal;
    protected RestAPI restapi;
    
    public SimVoronoiDiagramUpdate(SimVoronoiDiagram diagram_old, SimVoronoiDiagram diagram_new,SystemSettings settings,DrupalSettings drupal) 
    {
        this.drupal = drupal;
        this.settings = settings;
        this.restapi = new RestAPI(this.settings,this.drupal);
        
        if(diagram_old == null || diagram_old.featureList.isEmpty())
        {
            for (SimVoronoiDiagramFeature featureList1 : diagram_new.featureList) 
            {
                this.featureList.add(featureList1);
            }
        }
        else
        {
            for (SimVoronoiDiagramFeature featureList1 : diagram_new.featureList) 
            {
                if (diagram_old.getFeature(featureList1.id) != null)
                {
                    if (!featureList1.polygon.equals(diagram_old.getFeature(featureList1.id).polygon)) 
                    {
                        this.featureList.add(featureList1);
                    }
                } 
                else 
                {
                    this.featureList.add(featureList1);
                }
            }
        }
    }
    
    public void saveinDrupal() throws ProtocolException, IOException
    {
        for(SimVoronoiDiagramFeature feature : this.featureList)
        {
            //Entity erzeugen
            JSONObject drupaljson = new JSONObject();
            LinkedHashMap type = new LinkedHashMap();
            LinkedHashMap area = new LinkedHashMap();
            LinkedList type_ = new LinkedList();
            LinkedList area_ = new LinkedList();
            type.put("target_id","sim_weatherstation");
            String polygon = feature.polygon.toString();
            area.put("value",polygon);
            area_.add(area);
            type_.add(type);
            drupaljson.put("type",type_);
            drupaljson.put("field_dwdarea",area_);
            restapi.restPATCH(drupaljson, this.drupal.entityNodePATCH,feature.id);
        }
    }
}