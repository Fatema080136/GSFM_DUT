package org.mymodel.simulation.routechoice;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;
import org.mysimulationmodel.simulation.common.CWall;
import org.mysimulationmodel.simulation.environment.CEnvironment;
import org.mysimulationmodel.simulation.routechoice.CNode;

import javax.vecmath.Vector2d;

/**
 * Created by fatema on 04.11.2017.
 */
public class CAstarAlgorithmTest
{
    /**
     * initialize class with static data for empty grid
     */

    private ArrayList<CWall> m_walledge;
    private Map<Vector2d, CNode> p_visibilitygraph;


    @Before
    public void initializeemptygrid()
    {
        m_walledge = new ArrayList<>();
        m_walledge.add(0, new CWall( new Vector2d( 0, 190 ), new Vector2d( 170, 190 ), "poly1" ));
        m_walledge.add(1, new CWall( new Vector2d( 0, 190 ), new Vector2d( 0, 340 ), "poly1" ));
        m_walledge.add(2, new CWall( new Vector2d( 170, 190 ), new Vector2d( 170, 340 ), "poly1" ));
        m_walledge.add(3, new CWall( new Vector2d( 0, 340 ), new Vector2d( 170, 340 ), "poly1" ));


        ArrayList<Vector2d> node1 = new ArrayList<>();
        ArrayList<Vector2d> node2 = new ArrayList<>();
        ArrayList<Vector2d> node3 = new ArrayList<>();
        ArrayList<Vector2d> node4 = new ArrayList<>();

        p_visibilitygraph = new ConcurrentHashMap<>();
        node1.add(m_walledge.get(0).getPoint2());
        node1.add(m_walledge.get(1).getPoint2());

       // p_visibilitygraph.put( m_walledge.get(0).getPoint1(), new CNode( node1, "poly1" ) );

        //node2.add(m_walledge.get(0).getPoint1());
        node2.add(m_walledge.get(2).getPoint2());

        p_visibilitygraph.put( m_walledge.get(0).getPoint2(), new CNode( m_walledge.get(0).getPoint2(), node2, "poly1" ) );

        node3.add(m_walledge.get(1).getPoint1());
        node3.add(m_walledge.get(3).getPoint2());

        //p_visibilitygraph.put( m_walledge.get(1).getPoint2(), new CNode( node3, "poly1" ) );

        node4.add(m_walledge.get(2).getPoint1());
       // node4.add(m_walledge.get(3).getPoint1());

        p_visibilitygraph.put( m_walledge.get(2).getPoint2(), new CNode( m_walledge.get(2).getPoint2(), node4, "poly1" ) );

    }

    /**
     * test of a correct working route with some obstacles
     */
    @Test
    public void testrouting()
    {
        //final List<Vector2d> l_route = new CAstarAlgorithm().route( m_walledge, p_visibilitygraph, new Vector2d( 150, 100 ), new Vector2d( 60, 440 ) );

        //l_route.forEach(System.out::println);

    }

    /**
     * it is recommand, that each test-class uses also
     * a main-method, which calls the test-methods manually,
     * because the Maven-test calls does not allow any debugging
     * with the IDE, so this main-method allows to start the
     * test through the IDE and run the IDE debugger
     * @param p_args input arguments
     **/
    public static void main( final String[] p_args )
    {
        new CAstarAlgorithmTest().testrouting( );
    }
}
