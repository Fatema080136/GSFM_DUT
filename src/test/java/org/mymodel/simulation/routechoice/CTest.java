package org.mymodel.simulation.routechoice;

import org.junit.Before;
import org.junit.Test;
import org.mysimulationmodel.simulation.common.CVector;
import org.mysimulationmodel.simulation.routechoice.CNode;

import javax.vecmath.Vector2d;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//import static cern.clhep.PhysicalConstants.pi;


/**
 * Created by fatema on 11.11.2017.
 */
public class CTest
{
    private Vector2d point1 = new Vector2d( 0.0, 190.0 );
    private Vector2d point2 = new Vector2d( 170.0, 190.0 );
    private Vector2d point3 = new Vector2d( 170.0, 340.0 );
    private Vector2d point4 = new Vector2d( 0.0, 340.0 );
    private final Map<Vector2d, CNode> m_visibilitynodes = new ConcurrentHashMap<>();

    /**
     * test of a correct working route with some obstacles
     */
    @Test
    public void test()
    {
        double dx = point3.x - point2.x, dy = point3.y - point2.y;
        double dx1 = point3.x - point4.x, dy1 = point3.y - point4.y;
        //m_visibilitynodes.put( new Vector2d( 5, 5),  new CNode( new ArrayList<>(), "bla" ) );
        System.out.println( m_visibilitynodes.containsKey( new Vector2d( 5, 6) ) );

        System.out.println( new Vector2d(dx, dy).angle(new Vector2d(dx1,dy1))*(180/3.14159265359));
        System.out.println( new Vector2d(point2.x - point1.x, point2.y - point1.y).angle(new Vector2d(point2.x - point3.x,point2.y - point3.y)));
       // System.out.println( CVector.angle(point3, point2) );
        //System.out.println( CVector.angle(point1, point2) );
        //System.out.println( CVector.angle(point1, point2) );

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
      new CTest().test();
    }
}
