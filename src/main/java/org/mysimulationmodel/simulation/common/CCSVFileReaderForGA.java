package org.mysimulationmodel.simulation.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CCSVFileReaderForGA
{
    public static Map<String, String> readDataFromCSV()
    {

        Map<String,String> m_realdata = new HashMap<>();
        //Map<String,ArrayList<String>> m_realdata = new HashMap<>();
        //Path pathToFile = Paths.get( "C:\\Users\\fatema\\Desktop\\roadusers.csv" );
        Path pathToFile = Paths.get( "C:\\Users\\fatema\\Desktop\\frame\\framemodel\\beforeMCConflict\\realdataHBC.csv" );
        try ( BufferedReader l_br = Files.newBufferedReader( pathToFile, StandardCharsets.US_ASCII ) )
        {
            String l_line = l_br.readLine();
            while ( l_line != null )
            {
                String[] l_attributes = l_line.split(",");
                ArrayList<String> l_temp = new ArrayList<>();
                l_temp.add(l_attributes[5]);
                l_temp.add(l_attributes[6]);

                //m_realdata.put( new StringBuffer(l_attributes[3]).append(l_attributes[0]).toString()
                //.replaceAll("\\s+",""), l_temp );
                //System.out.println( " bla "+l_attributes.length );
                if(l_attributes.length == 9){
                    m_realdata.put( new StringBuffer(l_attributes[4]).append(l_attributes[1]).toString()
                            .replaceAll("\\s+",""), l_attributes[7] );}

                l_line = l_br.readLine();
            }
        }
        catch ( IOException ioe) { ioe.printStackTrace(); }
        return m_realdata;
    }

    // for game parameter estimation using genetic algorithm
    public static Map<Integer, Map<String,Double>> readDataFromCSV3()
    {

        Map<Integer, Map<String,Double>> m_realdata = new HashMap<>();
        Path pathToFile = Paths.get( System.getProperty("user.dir").concat("/strategy.csv"));
        try ( BufferedReader l_br = Files.newBufferedReader( pathToFile, StandardCharsets.US_ASCII ) )
        {
            int scenario_id = 1;
            String l_line = l_br.readLine();
            Map<String,Double> l_temp = new HashMap<>();
            while ( l_line != null )
            {
                String[] l_attributes = l_line.split(",");

                if(!l_attributes[0].equals("scenario_id") )
                {

                    if( scenario_id == (int)Double.parseDouble(l_attributes[0]) )
                    {
                        l_temp.put(l_attributes[1],Double.parseDouble(l_attributes[3]));
                    }
                    else
                    {
                        m_realdata.put( scenario_id, l_temp);
                        l_temp = new HashMap<>();
                        scenario_id = (int)Double.parseDouble(l_attributes[0]);
                        l_temp.put(l_attributes[1],Double.parseDouble(l_attributes[3]));
                    }
                }

                l_line = l_br.readLine();
            }
        }
        catch ( IOException ioe) { ioe.printStackTrace(); }
        return m_realdata;
    }


    public static Map<String,ArrayList<String>> readDataFromCSV2()
    {

        Map<String,ArrayList<String>> m_realdata = new HashMap<>();
        Path pathToFile = Paths.get( System.getProperty("user.dir").concat("/real_data_dut.csv"));
        //Path pathToFile = Paths.get( "C:\\Users\\fatema\\Desktop\\frame\\framemodel\\beforeMCConflict\\realdataHBC.csv" );

        try ( BufferedReader l_br = Files.newBufferedReader( pathToFile, StandardCharsets.US_ASCII ) )
        {
            String l_line = l_br.readLine();
            while ( l_line != null )
            {
                String[] l_attributes = l_line.split(",");
                //System.out.println( "gggg "+l_attributes[6]);

                ArrayList<String> l_temp = new ArrayList<>();
                l_temp.add(l_attributes[4]);
                l_temp.add(l_attributes[5]);

                //System.out.println( l_attributes[5] + " wh "+l_attributes[6]);
                m_realdata.put( new StringBuffer(new StringBuffer(l_attributes[1]).append(l_attributes[3])).append(l_attributes[12]).toString()
                        , l_temp );

                l_line = l_br.readLine();
            }
        }
        catch ( IOException ioe) { ioe.printStackTrace(); }
        return m_realdata;
    }

    //for genetic algo simulation
    public static Map<String, String> readDataFromCSVForSimulation()
    {

        Map<String,String> m_realdata = new HashMap<>();
        Path pathToFile = Paths.get( "C:\\Users\\fatema\\Desktop\\frame\\framemodel\\beforeMCConflict\\realdataHBC.csv" );
        try ( BufferedReader l_br = Files.newBufferedReader( pathToFile, StandardCharsets.US_ASCII ) )
        {
            String l_line = l_br.readLine();
            while ( l_line != null )
            {
                String[] l_attributes = l_line.split(",");
//                ArrayList<String> l_temp = new ArrayList<>();
//                l_temp.add(l_attributes[4]);
//                l_temp.add(l_attributes[5]);
//                l_temp.add(l_attributes[6]);

                if(l_attributes.length == 9){
                    m_realdata.put( new StringBuffer(l_attributes[4]).append(l_attributes[1]).toString()
                            .replaceAll("\\s+",""), l_attributes[7] );}

                l_line = l_br.readLine();
            }
        }
        catch ( IOException ioe) { ioe.printStackTrace(); }
        return m_realdata;
    }

    public static Map<String,ArrayList<Integer>> input()
    {
        Map<String,ArrayList<Integer>> input = new HashMap<>();

        Path pathToFile = Paths.get(System.getProperty("user.dir").concat("/GAInput_DUT_3.csv") );

        try ( BufferedReader l_br = Files.newBufferedReader( pathToFile, StandardCharsets.US_ASCII ) )
        {
            String l_line = l_br.readLine();
            while ( l_line != null )
            {
                String[] l_attributes = l_line.split(",");
                if(!l_attributes[1].equals("s_id") )
                {
                    ArrayList<Integer> l_temp = new ArrayList<>();
                    l_temp.add(Integer.valueOf(l_attributes[1]));
                    l_temp.add(Integer.valueOf(l_attributes[3]));
                    input.put(new StringBuffer(l_attributes[1]).append("@").append(l_attributes[2]).toString(),l_temp);
                }
                l_line = l_br.readLine();
            }
        }
        catch ( IOException ioe) { ioe.printStackTrace(); }
        return input;
    }


}
