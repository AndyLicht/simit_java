/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.geogockel.simmanager.balschmiter.voronoi;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;

import com.vividsolutions.jts.triangulate.VoronoiDiagramBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.json.*;

/**
 *
 * @author Balschmiter
 */

public class SimVoronoiDiagram 
{
    public float minX;
    public float maxX;
    public float minY;
    public float maxY;
    
    public int width;
    public int height;
    
    public List <SimVoronoiDiagramFeature> featureList = new ArrayList<>();
    
    protected GeometryFactory geometryFactory = new GeometryFactory();
    protected VoronoiDiagramBuilder voronoiBuilder = new VoronoiDiagramBuilder();
    public Collection<Geometry> geometries = new ArrayList<>();
    
    public SimVoronoiDiagram(JSONArray jsonarray,int width, int height, float minx, float maxx, float miny, float maxy) throws JSONException, ParseException
    {
        this.minX = minx;
        this.maxX = maxx;
        this.minY = miny;
        this.maxY = maxy;
        this.width = width;
        this.height = height;

        for (int i = 0; i<jsonarray.length();i++) 
        {
            JSONObject point = (JSONObject)jsonarray.get(i);
            SimVoronoiDiagramFeature feature = new SimVoronoiDiagramFeature(point);
            geometries.add(feature.point);
            featureList.add(feature);
        }
        GeometryCollection inputGeometries = new GeometryCollection(geometries.toArray(new Geometry[0]), geometryFactory);
        voronoiBuilder.setSites(inputGeometries);
        
        Geometry diagram = voronoiBuilder.getDiagram(geometryFactory);
        for (int i = 0; i < diagram.getNumGeometries();i++)
        {
            Geometry poly = diagram.getGeometryN(i);
            
            for (SimVoronoiDiagramFeature featureList1 : featureList)
            {
                if (featureList1.point.within(poly)) {
                    featureList1.polygon = poly;
                }
            }
        } 
    }
    
    public SimVoronoiDiagramFeature getFeature(int id)
    {
        for (SimVoronoiDiagramFeature featureList1 : this.featureList) 
        {
            if (featureList1.id == id) {
                return featureList1;
            }
        }
        return null;
    }
}