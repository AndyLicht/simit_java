package de.geogockel.simmanager.balschmiter.voronoi;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

public class SimVoronoiDiagramFeature 
{
   
    protected int id;
    protected GeometryFactory geometryFactory = new GeometryFactory();
    protected WKTReader reader = new WKTReader( geometryFactory );
    protected Point point;
    public Geometry polygon;
    
    SimVoronoiDiagramFeature(JSONObject point) throws JSONException, ParseException
    {
        this.point =  (Point) reader.read((String) point.get("field_dwdpoint"));
        this.id = Integer.valueOf((String) point.get("nid"));
    }
}