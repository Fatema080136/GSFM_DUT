package org.mysimulationmodel.simulation.common;

import org.mysimulationmodel.simulation.routechoice.MyHausdorfDistanceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CHausdorf
{

    public static Map<String, HashMap<Double, MyHausdorfDistanceImpl.Position>> simulatedDataFromCSV()
    {

        ArrayList<COutputFormat> m_realdata = new ArrayList<>();
        Map<String,HashMap<Double,MyHausdorfDistanceImpl.Position>> l_test = new HashMap<>();

        Path pathToFile = Paths.get( "C:\\Users\\fatema\\Desktop\\frame\\mysimulationmodel\\results.csv" );
        try ( BufferedReader l_br = Files.newBufferedReader( pathToFile, StandardCharsets.US_ASCII ) )
        {
            String l_line = l_br.readLine();
            while ( l_line != null )
            {
                String[] l_attributes = l_line.split(",");
                //System.out.println( l_attributes[0]);
                if(!l_attributes[0].equals("scenarioid"))
                    m_realdata.add(new COutputFormat( Integer.parseInt(l_attributes[0]),Double.parseDouble(l_attributes[1]), l_attributes[2],
                        Double.parseDouble(l_attributes[3]), Double.parseDouble(l_attributes[4]), Double.parseDouble(l_attributes[5]) ) );

                l_line = l_br.readLine();
            }
        }
        catch ( IOException ioe) { ioe.printStackTrace(); }
        for(COutputFormat out: m_realdata )
        {
            l_test.computeIfAbsent( out.m_id, k-> new HashMap<>()).put( out.m_timestep, new MyHausdorfDistanceImpl.Position( out.m_selfX, out.m_selfY));
        }
        return l_test;
    }

    public static Map<String, HashMap<Double, MyHausdorfDistanceImpl.Position>> realDataFromCSV()
    {

        ArrayList<COutputFormat> m_realdata = new ArrayList<>();
        Map<String,HashMap<Double, MyHausdorfDistanceImpl.Position>> l_test = new HashMap<>();

        Path pathToFile = Paths.get( "C:\\Users\\fatema\\Desktop\\pedestrians.csv" );
        try ( BufferedReader l_br = Files.newBufferedReader( pathToFile, StandardCharsets.US_ASCII ) )
        {
            String l_line = l_br.readLine();
            while ( l_line != null )
            {
                String[] l_attributes = l_line.split(",");
                //System.out.println( l_attributes[0]);
                if(!l_attributes[1].equals("frame_count"))
                    m_realdata.add(new COutputFormat( Double.parseDouble(l_attributes[3]), l_attributes[0].trim().replaceAll(" +", " ").toString(),
                            Double.parseDouble(l_attributes[4]), Double.parseDouble(l_attributes[5]) ) );

                l_line = l_br.readLine();
            }
        }
        catch ( IOException ioe) { ioe.printStackTrace(); }
        for(COutputFormat out: m_realdata )
        {
            l_test.computeIfAbsent( out.m_id, k-> new HashMap<Double, MyHausdorfDistanceImpl.Position>()).put( out.m_timestep, new MyHausdorfDistanceImpl.Position( out.m_selfX, out.m_selfY));
        }
        return l_test;
    }

}
