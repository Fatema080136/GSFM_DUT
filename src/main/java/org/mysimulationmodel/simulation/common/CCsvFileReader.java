package org.mysimulationmodel.simulation.common;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public class CCsvFileReader
{
    public static List<CInputFormat> readDataFromCSV(String p_fileName, int p_scenarioid )
    {
        List<CInputFormat> l_roadusers = new ArrayList<>();
        Path pathToFile = Paths.get( p_fileName );

        try ( BufferedReader l_br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII ) )
        {
            String l_line = l_br.readLine();
            while ( l_line != null )
            {
                String[] l_attributes = l_line.split(",");
                //System.out.println("is here problem");
                if (!l_attributes[1].equals("s_id") && Integer.parseInt(l_attributes[1]) == p_scenarioid )
                {
                    CInputFormat l_roaduser = createRoadUser(l_attributes);
                    l_roadusers.add(l_roaduser);
                }
                l_line = l_br.readLine();
            }
        }
        catch ( IOException ioe) { ioe.printStackTrace(); }
        return l_roadusers;
    }

    private static CInputFormat createRoadUser( String[] l_metadata )
    {
        return new CInputFormat( Integer.parseInt(l_metadata[1]), l_metadata[2], Double.parseDouble(l_metadata[3]), Double.parseDouble(l_metadata[4]),
                Double.parseDouble(l_metadata[5]), Double.parseDouble(l_metadata[6]), Double.parseDouble(l_metadata[7]), Double.parseDouble(l_metadata[8]),
                (int)Double.parseDouble(l_metadata[9]), (int)Double.parseDouble(l_metadata[10]), Integer.parseInt(l_metadata[11]),Integer.parseInt(l_metadata[12]) );
    }

}