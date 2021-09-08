package org.mymodel.simulation;

import org.junit.Test;
import org.mymodel.simulation.agent.CAngleTest;
import org.mysimulationmodel.simulation.common.CForce;
import org.mysimulationmodel.simulation.common.CVector;

import javax.vecmath.Vector2d;
import java.util.ArrayList;
import java.util.List;

public class CPixelPerMeterTest
{
    @Test
    public void directionAndAngleTest()
    {
        Vector2d m_position1 = new Vector2d(12,22);
        List<Vector2d> static_points1 = new ArrayList<>();
        static_points1.add(new Vector2d(20.0,20.0));
        static_points1.add(new Vector2d(22.0,22.0));
        static_points1.add(new Vector2d(24.0,24.0));
        Vector2d direction_point1 = new Vector2d(60,60);
        System.out.println("direction_point" + direction_point1);
        Vector2d direction_vector1 = CVector.direction(direction_point1, m_position1);

        static_points1.forEach(
                i->
                {
                    if( CVector.distance( m_position1, i)< 25)
                    {
                        System.out.println("static_points " + i);
                        System.out.println("angle " + CForce.getViewAngle(direction_vector1.x, direction_vector1.y,
                            (i.x - m_position1.x),(i.y - m_position1.y)));
                        System.out.println("-------------------------- ");
                    }
                }
        );

        Vector2d m_position10 = new Vector2d(36,66);
        List<Vector2d> static_points10 = new ArrayList<>();
        static_points10.add(new Vector2d(60.0,60.0));
        static_points10.add(new Vector2d(66.0,66.0));
        static_points10.add(new Vector2d(72.0,72.0));
        Vector2d direction_point10 = new Vector2d(180,180);
        System.out.println("direction_point10" + direction_point10);
        Vector2d direction_vector10 = CVector.direction(direction_point10, m_position10);

        static_points10.forEach(
                i->
                {
                    if( CVector.distance( m_position10, i)< 75)
                    {
                        System.out.println("static_points10 " + i);
                        System.out.println("angle10 " + CForce.getViewAngle(direction_vector10.x, direction_vector10.y,
                            (i.x - m_position10.x), (i.y - m_position10.y)));
                        System.out.println("-------------------------- ");
                    }
                }
        );


    }

    @Test
    public void signTest()
    {
        System.out.println(Integer.signum(12));
        System.out.println(Math.signum(-112.0023));
    }


    public static void main( final String[] p_args )
    {
        // new CAngleTest().testgroup();
        //new CPixelPerMeterTest().directionAndAngleTest();
    }

}
