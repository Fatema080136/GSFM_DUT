package org.mysimulationmodel;
import org.mysimulationmodel.simulation.agent.*;
import org.mysimulationmodel.simulation.common.*;
import org.mysimulationmodel.simulation.environment.CEnvironment;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.logging.LogManager;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Multi-agent Simulation
 */
final class CMain_GA
{

    private static final String FILE_HEADER = "scenarioid,time,id,x_axis,y_axis,velocity";
    static Map< String, ArrayList<String>> m_realdata = CCSVFileReaderForGA.readDataFromCSV2();


    //private String m_name;
    static
    {
        LogManager.getLogManager().reset();
    }

    private CMain_GA()
    {
    }

    protected static ArrayList<CSampleOutput> runSimulation(double p_distance, double p_cartopeddis, double p_cartopeddis_2,
                                                            int p_senarioid, String p_uid )//,double p_maxspeed
    {
        //m_realdata.forEach((i,j)->System.out.println(i+" "+j));
        int m_pixelpermeter = CEnvironment.getpixelpermeter();
        CHostAgent l_hostagent;

        File l_ped = new File(System.getProperty("user.dir").concat("/agent.asl"));
        File l_car = new File(System.getProperty("user.dir").concat("/car.asl"));
        File l_host = new File(System.getProperty("user.dir").concat("/host.asl"));

        CEnvironment l_env = new CEnvironment();


        List<CInputFormat> l_roadusers = CCsvFileReader.readDataFromCSV(System.getProperty("user.dir").concat("//start_end_dut_extended.csv"), p_senarioid);
        List<CInputFormat> l_pedestrians = l_roadusers.stream().filter( n -> n.m_roaduser_id.startsWith("p") ).collect(Collectors.toList());
        List<CInputFormat> l_cars = l_roadusers.stream().filter( n -> n.m_roaduser_id.startsWith("v") ).collect(Collectors.toList());
        int count = 0;
        int maxTries = 3;
        ArrayList<CSampleOutput> m_output = new ArrayList<>();
        while(true) {
            try
                    (    final FileInputStream l_pedestrianstream = new FileInputStream( l_ped );
                         final FileInputStream l_carstream = new FileInputStream( l_car );
                         final FileInputStream l_hoststream = new FileInputStream( l_host )
                    )
            {
                Stream.concat(
                        new CPedestrianGenerator( l_pedestrianstream, l_env, l_pedestrians )
                                .generatemultiple(  l_pedestrians.size()),

                        new CCarGenerator( l_carstream, l_env, l_cars )
                                .generatemultiple( l_cars.size() )

                ).collect( Collectors.toSet() );

                l_hostagent = new CHostGenarator( l_hoststream, l_env )
                        .generatesingle( 1 );
                break;

            }

            catch ( final Exception l_exception )
            {
                /*if (++count > maxTries)
                    l_exception.printStackTrace();
                else throw new RuntimeException();*/
            }
        }

        int l_start = l_roadusers.stream().map( i -> i.m_start_cycle ).sorted().collect(Collectors.toList()).get(0);
        int l_end = l_roadusers.stream().map( i -> i.m_numberofcycle ).sorted(Comparator.reverseOrder()).collect(Collectors.toList()).get(0);
        final float[] l_timestep = {l_start/2f};//0.5  l_start/2f
        CHostAgent finalL_hostagent = l_hostagent;
        l_env.setScenario(p_senarioid);

        IntStream.range(l_start, l_end)
                .forEach( j ->
                {
                    if ( l_env.addPedtoInitializeLater().get(j) != null )
                    {
                        l_env.addPedtoInitializeLater().get(j).forEach( n ->
                        {
                            l_env.initialset( n );
                            l_env.initialPedestrian( n );
                        });
                    }

                    if ( l_env.addCartoInitializeLater().get(j) != null )
                    {
                        l_env.addCartoInitializeLater().get(j).forEach( n ->
                        {
                            l_env.initialset( n );
                            l_env.initialCar( n );
                        });
                    }

                    //long startTime = System.nanoTime();
                    try {
                        finalL_hostagent.updateParameter( p_distance*m_pixelpermeter, 9, 2, p_uid );
                        finalL_hostagent.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    l_env.repaint();
                    l_env.getRoadUserinfo()
                            .forEach( i ->
                            {
                                try
                                {   if(i.getType()==1) {
                                    if (i.m_cluster == 0)
                                        i.updateParameter(m_pixelpermeter, 3 * m_pixelpermeter,
                                                3 * m_pixelpermeter, 0.43000000000000005,
                                                0.01 * m_pixelpermeter, 0.17 * m_pixelpermeter, 0.11 * m_pixelpermeter,
                                                6.109999999999999 * m_pixelpermeter, 8 * m_pixelpermeter,
                                                6.0 * m_pixelpermeter, 6.0 * m_pixelpermeter);

                                    else if (i.m_cluster == 1)
                                        i.updateParameter(m_pixelpermeter, 3 * m_pixelpermeter,
                                                3 * m_pixelpermeter, 0.16,
                                                0.1 * m_pixelpermeter, 0.18 * m_pixelpermeter, 0.14 * m_pixelpermeter,
                                                13.7 * m_pixelpermeter, 8 * m_pixelpermeter,
                                                7.0 * m_pixelpermeter, 7.0 * m_pixelpermeter);

                                    else
                                        i.updateParameter(m_pixelpermeter, 3 * m_pixelpermeter,
                                                3 * m_pixelpermeter, 0.41000000000000003,
                                                0.1* m_pixelpermeter, 0.23 * m_pixelpermeter,
                                                0.27 * m_pixelpermeter, 18.2 * m_pixelpermeter, 8 * m_pixelpermeter,
                                                9.01 * m_pixelpermeter, 9.01 * m_pixelpermeter);
                                    }
                                    if(i.getType()==2)
                                    i.updateParameter( p_cartopeddis*m_pixelpermeter, p_cartopeddis_2*m_pixelpermeter, p_uid, 8);//, 2
                                    i.call();
                                    //System.out.println(i.getname());
                                    ArrayList<String> l_temp = m_realdata.get( new StringBuffer(new StringBuffer(String.valueOf(l_timestep[0])).append(i.getname())).append(p_senarioid).toString());
                                    if(i.getname().equals(p_uid)&&l_temp!=null){
                                        double l_speed =  i.getVelocity().length() == 0.2 ? 0.0 : i.getVelocity().length();
                                        m_output.add( new CSampleOutput( l_timestep[0], i.getname(),i.getPosition().x, i.getPosition().y, l_speed ) );
                                    }

                                    Thread.sleep(5);
                                }

                                catch ( final Exception l_exception )
                                {
                                    //l_exception.printStackTrace();
                                    //throw new RuntimeException();
                                }

                            } );
                    l_timestep[0] = (float) (l_timestep[0] + 0.5);
                    l_env.setCurrentCycle(l_timestep[0]);
                } );
        return m_output;
    }

}