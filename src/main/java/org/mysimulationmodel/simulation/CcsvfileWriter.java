package org.mysimulationmodel.simulation;

import org.mysimulationmodel.simulation.common.COutputFormat;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fatema on 10.04.2018.
 */
public class CcsvfileWriter
{
    static ArrayList<COutputFormat> m_output;

    private static final String COMMA_DELIMITER = ",";

    private static final String NEW_LINE_SEPARATOR = "\n";

    private static final String FILE_HEADER = "scenarioid,time_step,ID,x_axis,y_axis,speed";

    public static void writeCsvFile(String fileName, ArrayList<COutputFormat> m_output)
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
