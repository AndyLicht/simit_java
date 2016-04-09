/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.geogockel.simmanager.balschmiter.settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Tim
 */
public class DrupalSettings {
    
    public String basePath;
    public String weatherAPIPath;
    public String weatherNodesPath;
    public String entityNodePOST;
    public String entityNodePATCH;
    public String entityNodeDELETE;
    public String entityNodeGET;
    
    public String DWDgetWeatherBuild;
    public String voronoi;

    public DrupalSettings(SystemSettings settings) throws SQLException
    {
        Connection postgres = connectToDatabaseOrDie(settings.databaseHost,settings.databasePort,settings.databaseName,settings.databaseUser,settings.databasePassword);
        Statement st = postgres.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM config WHERE name = '"+settings.drupalConfigName+"'");
        
        while (rs.next())
        {
            String input = rs.getString(3);
            input = input.substring(input.indexOf("{")+1, input.lastIndexOf("}"));
            String [] part = input.split(";");
            
            for(int i = 0;i<part.length;i = i+2)
            {
                String [] parts = part[i].split(":");
                if("\"basePath\"".equals(parts[2]))
                {
                    this.basePath = part[i+1].split(":")[2].replace("\"","")+":"+part[i+1].split(":")[3].replace("\"","");
                }
                if("\"weatherapiPath\"".equals(parts[2]))
                {
                    this.weatherAPIPath = part[i+1].split(":")[2].replace("\"","");
                }
                if("\"weathernodePath\"".equals(parts[2]))
                {
                    this.weatherNodesPath = part[i+1].split(":")[2].replace("\"","");
                }
                if("\"entityNodePOST\"".equals(parts[2]))
                {
                    this.entityNodePOST = part[i+1].split(":")[2].replace("\"","");
                }
                if("\"entityNodePATCH\"".equals(parts[2]))
                {
                    this.entityNodePATCH = part[i+1].split(":")[2].replace("\"","");
                }
                if("\"entityNodeDELETE\"".equals(parts[2]))
                {
                    this.entityNodeDELETE = part[i+1].split(":")[2].replace("\"","");
                }
                if("\"entityNodeGET\"".equals(parts[2]))
                {
                    this.entityNodeGET = part[i+1].split(":")[2].replace("\"","");
                }
                if("\"voronoibuild\"".equals(parts[2]))
                {
                    this.voronoi = part[i+1].split(":")[1];
                }
                if("\"DWDgetweatherbuild\"".equals(parts[2]))
                {
                    this.DWDgetWeatherBuild = part[i+1].split(":")[1];
                }
            }
        }
        rs.close();
        st.close();
    }
    
    public void drupalSaveSettings(SystemSettings settings) throws SQLException
    {
        String output = "a:"+getColumnCount()+":{";
        output = output + "s:8:\"basePath\";";
        output = output + "s:"+this.basePath.length()+":\""+this.basePath+"\";";
        output = output + "s:14:\"weatherapiPath\";";
        output = output + "s:"+this.weatherAPIPath.length()+":\""+this.weatherAPIPath+"\";";
        output = output + "s:15:\"weathernodePath\";";
        output = output + "s:"+this.weatherNodesPath.length()+":\""+this.weatherNodesPath+"\";";
        output = output + "s:14:\"entityNodePOST\";";
        output = output + "s:"+this.entityNodePOST.length()+":\""+this.entityNodePOST+"\";";
        output = output + "s:15:\"entityNodePATCH\";";
        output = output + "s:"+this.entityNodePATCH.length()+":\""+this.entityNodePATCH+"\";";
        output = output + "s:16:\"entityNodeDELETE\";";
        output = output + "s:"+this.entityNodeDELETE.length()+":\""+this.entityNodeDELETE+"\";";
        output = output + "s:13:\"entityNodeGET\";";
        output = output + "s:"+this.entityNodeGET.length()+":\""+this.entityNodeGET+"\";";
        output = output + "s:12:\"voronoibuild\";";
        output = output + "b:"+this.voronoi+";";
        output = output + "s:18:\"DWDgetweatherbuild\";";
        output = output + "b:"+this.DWDgetWeatherBuild+";";
        output = output + "}";
        Connection postgres = connectToDatabaseOrDie(settings.databaseHost,settings.databasePort,settings.databaseName,settings.databaseUser,settings.databasePassword);
        Statement st = postgres.createStatement();
        st.executeUpdate("UPDATE config SET data = '"+output+"' WHERE name = '"+settings.drupalConfigName+"'");
        st.executeUpdate("DELETE from cache_config WHERE cid = '"+settings.drupalConfigName+"'");
        st.close();
        
    }
    public int getColumnCount() 
    {
        return getClass().getDeclaredFields().length;
    }
    private Connection connectToDatabaseOrDie(String host, String port, String databasename, String username, String password)
    {
        Connection conn = null;
        try
        {
            Class.forName("org.postgresql.Driver");
            
            String url = "jdbc:postgresql://"+host+":"+port+"/"+databasename;
            conn = DriverManager.getConnection(url,username, password);
        }
        catch (ClassNotFoundException e)
        {
            System.exit(1);
        }
        catch (SQLException e)
        {
            System.exit(2);
        }
        return conn;
  }
}
