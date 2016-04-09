/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.geogockel.simmanager.balschmiter.weatherapi;
import static de.geogockel.simmanager.balschmiter.weathermanager.WeatherManager.dwdsettings;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipInputStream;
/**
 *
 * @author Tim
 */
public class DWDWeatherParser {
    
    private final String tempurl;
    private final String pericipationurl;
    private String startsWith = null;
    
    


    //Array of Databasezeugs
    public DWDWeatherParser(DWDObject dwd) throws IOException
    {
       
        this.tempurl =  dwdsettings.tempurl.replace("{id}", dwd.dwdid);
        this.pericipationurl = dwdsettings.perciurl.replace("{id}", dwd.dwdid);
        this.startsWith = dwd.lastentry;
        getWeather();
    }
    private void getTemperature() throws MalformedURLException, IOException
    {
        //Temperatur
        URL url = new URL(this.tempurl);
        Boolean dateChecker = false;
        ZipInputStream inputStream = new ZipInputStream(url.openStream());
        if(this.startsWith == null)
        {
            readExternFile(inputStream);
        }
        else
        {
            while (inputStream.available() == 1)
            {
                ZipEntry ze;
                while ((ze = inputStream.getNextEntry()) != null)
                {
                    if(ze.getName().startsWith("produkt"))
                    {  
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String line;
                        int linenr = 0;
                        while ((line = bufferedReader.readLine()) != null)
                        {
                            String[] linearray = line.split(";");
                            if(linearray.length > 1  && linearray[1].startsWith(this.startsWith))
                            {
                                dateChecker = true;
                            }
                            if(dateChecker == true && linenr >0 )
                            {
                                System.out.println(line);
                            }
                            linenr++;
                        }
                    }
                }
            }
        }
    }
    
    
    
    private void getWeather() throws FileNotFoundException, IOException
    {
        getTemperature();
        getPericipation();
        
    }

    private void getPericipation() throws MalformedURLException, IOException {
        URL url = new URL(this.pericipationurl);
        Boolean dateChecker = false;
        ZipInputStream inputStream = new ZipInputStream(url.openStream());
        
        
        if(this.startsWith == null)
        {
            readExternFile(inputStream);
        }
        else
        {
            while (inputStream.available() == 1)
            {
                ZipEntry ze;
                while ((ze = inputStream.getNextEntry()) != null)
                {
                    if(ze.getName().startsWith("produkt"))
                    {  
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String line;
                        int linenr = 0;
                        while ((line = bufferedReader.readLine()) != null)
                        {
                            String[] linearray = line.split(";");
                            if(linearray.length > 1  && linearray[1].startsWith(this.startsWith))
                            {
                                dateChecker = true;
                            }
                            if(dateChecker == true && linenr > 0)
                            {
                                System.out.println(line);
                            }
                            linenr++;
                        }
                    }
                }
            }
        }
    }

    private void readExternFile(ZipInputStream inputStream) throws IOException 
    {
        while (inputStream.available() == 1)
        {
            ZipEntry ze;
            while ((ze = inputStream.getNextEntry()) != null)
            {
                if(ze.getName().startsWith("produkt"))
                {  
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        System.out.println(line);
                    }
                }
            }
        }  
    }
}
