package org.mysimulationmodel.simulation.environment;

import org.mysimulationmodel.simulation.agent.IBaseRoadUser;
import org.mysimulationmodel.simulation.common.CStatic;
import org.mysimulationmodel.simulation.common.CVector;
import org.mysimulationmodel.simulation.common.CWall;
import org.mysimulationmodel.simulation.routechoice.CAstarAlgorithm;
import org.mysimulationmodel.simulation.routechoice.CNode;

//import java.awt.*;

//import javax.swing.JPanel;
import javax.swing.*;
import javax.vecmath.Vector2d;
//import java.awt.geom.Ellipse2D;
//import java.awt.geom.Line2D;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


/**
 * environment class
 * * Created by Fatema on 10/20/2016.
 * extends JPanel
 */

public class CEnvironment extends JPanel implements IEnvironment
{
    /*for genetic algo
    //static Map< String, ArrayList<String>> m_realdata = CCSVFileReaderForGA.readDataFromCSVForSimulation();
     */
    public int m_playedgame = 0;
    private Graphics2D graphics2d;
    private static final int m_pixelpermeter = 1;
    private static final double m_timestep = 0.5;
    private ArrayList<IBaseRoadUser> m_pedestrian = new ArrayList<>();
    private Map<Integer,List<IBaseRoadUser>> m_pedestrianneedtoinitialize = Collections.synchronizedMap(new HashMap<>());
    private Map<Integer,List<IBaseRoadUser>> m_carneedtoinitialize = Collections.synchronizedMap(new HashMap<>());
    private CopyOnWriteArrayList<IBaseRoadUser> m_agent = new CopyOnWriteArrayList<>();
    private ArrayList<IBaseRoadUser> m_car = new ArrayList<>();
    private Map<Double,List<IBaseRoadUser>> m_pedestriangroup = new HashMap<>();
    private ArrayList<CStatic> m_wall = new ArrayList<>( 4 );
    private ArrayList<CWall> m_walledge = new ArrayList<>( );
    private ArrayList<CWall> m_roadborder = new ArrayList<>( 2 );
    private ArrayList<CWall> m_pedestrianborder = new ArrayList<>( 2 );
    private Map<String,Double> m_strategy = new HashMap<>();
    private int m_width;
    private int m_height;
    private float m_cycle;
    private int m_scenario;


    public CEnvironment()
    {
       setFocusable( true );
       setBackground( Color.WHITE );
       setDoubleBuffered( true );

        m_roadborder.add( new CWall ( new Vector2d(0, 165), new Vector2d( 800, 165 ), null ) );
        m_roadborder.add( new CWall ( new Vector2d(0, 200), new Vector2d( 800, 200 ), null ) );

        m_width = 80*m_pixelpermeter;//600;//x axis
        m_height =70*m_pixelpermeter; //400;//y axis
    }

    public Map<String,Double> getStrategy() { return m_strategy; }
    public int getScenario()
    {
        return m_scenario;
    }

    public void setStrategy( String p_id, double p_strategy ) { m_strategy.putIfAbsent(p_id,p_strategy); }

    /**
     * add pedestrian to pedestrian's set
     *
     * @param p_agent agent
     *
     */
    public void initialset(IBaseRoadUser p_agent ){ m_agent.add( p_agent ); }
    //change with multimap
    public Map<Integer, List<IBaseRoadUser>> addPedtoInitializeLater()
    {
        return m_pedestrianneedtoinitialize;
    }
    public Map<Integer,List<IBaseRoadUser>> addCartoInitializeLater()
    {
        return m_carneedtoinitialize;
    }
    public void initialCar(IBaseRoadUser p_car ){ m_car.add( p_car ); }
    public void initialPedestrian(IBaseRoadUser p_pedestrian ){ m_pedestrian.add( p_pedestrian ); }

    /*for genetic algo
    public Map< String, ArrayList<String>> getRealData()
    {
        return m_realdata;
    }*/
    public void setScenario( int p_senarioid){ m_scenario = p_senarioid;}
    public static int getpixelpermeter()
    {
        return m_pixelpermeter;
    }
    // get methods
    public int getWidth() { return m_width; }

    public int getHeight() { return m_height; }
    /**
     * paint all elements
     **/
    public void paint( Graphics g )
    {
        super.paint( g );
        graphics2d = ( Graphics2D ) g;
        //drawborder( Color.GRAY );
        try {
            drawPedestrian();
            drawCar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    private void drawborder( Color color )
    {
        graphics2d.setColor( color ) ;
        m_roadborder.forEach(
                i->
                    graphics2d.draw( new Line2D.Float((float)i.getPoint1().x, (float)i.getPoint1().y,
                            (float)i.getPoint2().x, (float)i.getPoint2().y) ) );
    }


    /**
     * draw each pedestrian
     */
    private void drawPedestrian()
    {
        graphics2d.setColor( Color.RED ) ;
        m_pedestrian
                .forEach( i -> {
                    Ellipse2D.Double shape = new Ellipse2D.Double( i.getPosition().getX(), i.getPosition().getY(), 0.75*m_pixelpermeter, 0.75*m_pixelpermeter );//2.5//8
                    graphics2d.fill( shape );

                });
    }
    public static double getTimestep()
    {
        return m_timestep;
    }

    /**
     * draw each car
     */
    private void drawCar()
    {
        graphics2d.setColor( Color.BLUE ) ;
        m_car.forEach( i -> {
                    AffineTransform rat = graphics2d.getTransform();
                    graphics2d.rotate( Math.toRadians(i.getM_angle()),i.getPosition().x, i.getPosition().y );
                    graphics2d.fillRect( (int)(i.getPosition().x -0.75*m_pixelpermeter ), (int)(i.getPosition().y -0.75*m_pixelpermeter), (int)1.5*m_pixelpermeter, (int)(1.5*m_pixelpermeter) );
                    graphics2d.setTransform(rat);
                    graphics2d.setColor( Color.GREEN ) ;
                });

    }

    /**
     * get the list of pedestrian with their information
     * @return a list of pedestrian information
     **/
    public ArrayList<IBaseRoadUser> getPedestrianinfo()
    {
        return m_pedestrian;
    }

    /**
     * get the list of car with their information
     * @return a list of car information
     **/
    public ArrayList<IBaseRoadUser> getCarinfo()
    {
        return m_car;
    }

    /**
     * get the list of pedestrian with their information
     * @return a list of agent
     **/
    public CopyOnWriteArrayList<IBaseRoadUser> getRoadUserinfo()
    {
        return m_agent;
    }


    /**
     * get the list of walls with their information
     * @return a list of wall information
     **/
    public ArrayList<CStatic> getWall()
    {
        return m_wall;
    }

    public float getCurrentCycle()
    {
        return m_cycle;
    }


    public void setCurrentCycle(float p_cycle)
    {
        m_cycle = p_cycle;
    }

    public Map<Double, List<IBaseRoadUser>> getGroupList()
    {
        return m_pedestriangroup;
    }

    public ArrayList<CWall> getPedestrianBorderInfo()
    {
        return m_pedestrianborder;
    }

    /**
     * get the list of walls with their information
     * @return a list of wall information
     **/
    public ArrayList<CWall> getWallinfo()
    {
        return m_walledge;
    }

    /**
     * check if the line segment between two points intersect with any wall edge and
     * the intersection point is not the start or end of point of the wall edge
     * @return true or false
     **/
    public Boolean check( Vector2d p_point1, CNode p_node1, Vector2d p_point2, CNode p_node2 )
    {
        int l_count = 0;
        for ( CWall l_walledge : m_walledge )
        {
            if ( CVector.segmentIntersect( p_point1, p_point2, l_walledge.getPoint1(),
                    l_walledge.getPoint2() ) )
            {
                l_count++;
            }
        }
        return l_count <= 2 || ( l_count == 4 && !Objects.equals( p_node1.getPolygonName(), p_node2.getPolygonName() ) );

    }

    /**
     * calculate route
     *
     * @param p_currentposition start position
     * @param p_targetposition target position
     * @return list of Vector2d points as path
     */
    public final List<Vector2d> route( final Vector2d p_currentposition, final Vector2d p_targetposition )
    {
        return new CAstarAlgorithm(this).route( m_walledge, p_currentposition, p_targetposition );
    }

    @Override
    public IEnvironment call() throws Exception
    {
        return this;
    }
}
