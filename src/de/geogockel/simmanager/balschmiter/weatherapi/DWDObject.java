package de.geogockel.simmanager.balschmiter.weatherapi;

import com.sun.xml.internal.ws.util.StringUtils;
import java.util.Collections;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tim
 */
public class DWDObject {
    
    public String nid;
    public String lastentry;
    public String table;
    public String dwdid;

    
    public DWDObject(JSONObject jsonObject) throws JSONException
    {
        this.nid = jsonObject.getString("nid");
        this.lastentry = jsonObject.getString("field_simdwdlastitem");
        this.table = jsonObject.getString("field_simdbtablename");
        this.dwdid = jsonObject.getString("field_simdwdid");
        int i = this.dwdid.length();
        if(i<5)
        {
            String listString = "";
            List<String> list = Collections.nCopies(5-i, "0");
            for (String s : list)
            {
                listString += s;
            }
            this.dwdid = listString+this.dwdid;
        }
    }
    
}
