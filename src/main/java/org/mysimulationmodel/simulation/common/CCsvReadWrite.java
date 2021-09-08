package org.mysimulationmodel.simulation.common;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CCsvReadWrite
{
    static ArrayList<COutputFormat> m_output;
    private static final String FILE_HEADER = "scenarioid,distance";

    private static final String COMMA_DELIMITER = ",";

    private static final String NEW_LINE_SEPARATOR = "\n";

    public static void writeCsvFile( String fileName, Map<String, Double> m_output, String FILE_HEADER )
    {
        FileWriter fileWriter = null;

        try {

            fileWriter = new FileWriter(fileName);

            try {
                fileWriter.append(FILE_HEADER.toString());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Map.Entry<String, Double> entry : m_output.entrySet())
            {
                System.out.println(entry.getKey() + "/" + entry.getValue());
                fileWriter.append(String.valueOf(entry.getKey()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(entry.getValue()));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
           
            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();

            } catch (IOException e) {

                System.out.println("Error while flushing/closing fileWriter !!!");

                e.printStackTrace();

            }
        }
    }

    public static void writeCsvFile(String fileName, Map<Integer, Double> m_output)
    {
        FileWriter fileWriter = null;

        try {

            fileWriter = new FileWriter(fileName);

            try {
                fileWriter.append(FILE_HEADER.toString());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            fileWriter.append(NEW_LINE_SEPARATOR);

            for ( int j= 1; j<=m_output.size(); j++)
            //m_output.forEach( (k,out) ->
            {
                fileWriter.append(String.valueOf(j));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(m_output.get(j)));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }//);
            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();

            } catch (IOException e) {

                System.out.println("Error while flushing/closing fileWriter !!!");

                e.printStackTrace();

            }
        }
    }

    public static void writeCsvFile(String fileName, ArrayList<COutputFormat> m_output, String FILE_HEADER)
    {
        FileWriter fileWriter = null;

        try {

            fileWriter = new FileWriter(fileName);

            try {
                fileWriter.append(FILE_HEADER.toString());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            fileWriter.append(NEW_LINE_SEPARATOR);

            for ( COutputFormat out : m_output )
            {
                fileWriter.append(String.valueOf(out.m_scenarioid));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(out.m_timestep));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(out.m_id));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(out.m_selfX));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(out.m_selfY));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(out.m_speed));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();

            } catch (IOException e) {

                System.out.println("Error while flushing/closing fileWriter !!!");

                e.printStackTrace();

            }
        }
    }

}
