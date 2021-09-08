package org.mymodel.simulation;

import org.junit.Test;
import org.mysimulationmodel.simulation.common.CForce;
import org.mysimulationmodel.simulation.common.CVector;

import javax.vecmath.Vector2d;

public class CDataPreparation
{
    @Test
    public void readData()
    {

       System.out.println(CVector.angle(new Vector2d(0,1),new Vector2d(1,0.5) ));
       System.out.println(CVector.angle(new Vector2d(0,1),new Vector2d(0.5,-0.5) ));
       System.out.println(CVector.angle(new Vector2d(0,1),new Vector2d(1,0) ));
       System.out.println(CVector.angle(new Vector2d(0,1),new Vector2d(0.25,-1) ));
        System.out.println(CVector.angle(new Vector2d(0,-1),new Vector2d(0.25,-1) ));

        System.out.println(new Vector2d(-0.5593194199999942, 0.0).dot(new Vector2d(-46.43895109, -26.68378753)) );
        //25.974207189066902
    }
    @Test
    public void angleTest( double cp_x, double cp_y, double cg_x, double cg_y, double pp_x, double pp_y)
    {
       System.out.println("angle "+ CForce.getViewAngle( CVector.direction( new Vector2d(cg_x, cg_y), new Vector2d(cp_x, cp_y) ).x,
                CVector.direction( new Vector2d(cg_x, cg_y), new Vector2d(cp_x, cp_y) ).y,
               ( pp_x - cp_x), ( pp_y - cp_y ) ) );

    }

    @Test
    public void velocityTestCar(double v)
    {
        System.out.println("car "+ (v/2 - 1.38) );
    }

    @Test
    public void velocityTestped(double v)
    {
        System.out.println("Ped " + (v/2 - 0.65) );
    }

    //
    @Test
    public void distanceTest(double cp_x, double cp_y, double pp_x, double pp_y)
    {
        System.out.println( 3 - CVector.distance( new Vector2d( cp_x, cp_y), new Vector2d(pp_x,pp_y) ) );
    }

    public static void main( final String[] p_args )
    {
        new CDataPreparation().velocityTestCar(1.949554988);
        new CDataPreparation().velocityTestped(0.821137021);
        new CDataPreparation().angleTest( 70.54485905,
        47.8001138,
        28.32156594,
        23.12612345,
        49.91721891,
        41.06579529);

        new CDataPreparation().distanceTest(70.54485905,
                47.8001138,49.91721891,
                41.06579529);
        //new CDataPreparation().distanceTest(50.18122866,35.94068179,63.45416596,35.58300554);

        new CDataPreparation().readData();
    }


}
