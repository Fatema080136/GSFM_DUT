package org.mysimulationmodel.simulation.common;

import org.mysimulationmodel.simulation.agent.IBaseRoadUser;
import org.mysimulationmodel.simulation.environment.CEnvironment;

import java.util.Random;

/**
 * own speed can also have impact on road user's behavior
 * Helping class for game playing
 * Created by fatema on 23.03.2018.
 */
public class CGame
{
    /**
     * measuring road user's speed
     * if speed of the competative user is higher than their normal speed, ego user prefer to decelerate (-1),
     * otherwise accelerate (1)
     **/

    private static final int m_pixelpermeter = CEnvironment.getpixelpermeter();

    //if this does not work then try with weight with each utility condition or factor
    private static final double m_carcontinue = 4;
    private static final double m_carstop  = 0;
    private static final double m_pedcontinue = 4;
    private static final double m_pedstop = 0;
    private static double m_peddeviate = 1;//1
    private static double m_carstoppeddeviate;//-2
    private static double m_randomization = 3;



    /**
     * Constant "p_NoSC > 2", need to be calibrated
     * measuring impact of number of simultaneous conflicts (NoSC) on road user's behavior, pagent = self
     **/
    static double NoSEcarStop( final IBaseRoadUser p_agent )
    {
        if ( p_agent.getNoSC() >= 2 ) return p_agent.getNoSC()-1;
        return 0;
    }


    /** for ca-tocar interaction
     * measuring impact of car following another car, on road user's behavior
     **/
    private static double carFollowingCarStop( final IBaseRoadUser p_agent )
    {
        if ( p_agent.getCarfollowingActive() == 1 ) return 1;
        return 0;
    }

    /**
     * measuring impact of car following another car, on road user's behavior
     **/
    private static double carFollowingCarContinue( final IBaseRoadUser p_agent )
    {
        if ( p_agent.getCarfollowingActive() == 1 ) return -1;
        return 0;
    }

    //new
    private static double angle( final double l_angle, final double h )
    {
        if ( l_angle >= h ) return l_angle;
        return 0;
    }

    private static double angleDev( final double l_angle, final int k)
    {
        if ( l_angle <= k ) return k - l_angle;
        return 0;
    }


    private static double speed( final double l_speed, final double i )
    {
        if ( l_speed > i ) return 1;
        return 0;
    }

    private static double distance( final double l_distance, final double l_speed )
    {
        if ( l_distance > 10 && l_speed > 0.9) return -(l_distance);
        return 0;
    }

    private static double peddistance( final double l_distance, final double l_equilien ,double p_factor)
    {
        if ( l_distance < 7 && (l_distance-l_equilien)<= p_factor) return -(l_distance);
        return 0;
    }


    /*
     * dimension 1 or row = player1; dimension 2 or column = player 2
     * 0 = continue, 1 = decelerate, 2 = deviate
     * third dimension: 0 = player1's(Car) utility, 1 = player2's(pedestrian) utility
     */
    public static double[][][] payoffMatrixCalculationCartoPed(double a, double b,double c,double e, double f,
                                                               double c_speed,
                                                               double l_angle, double p_speed, double noai, double stopped, double distance, IBaseRoadUser p_self, IBaseRoadUser p_other )
    {

        double[][][] l_payoffMatrix = new double[2][3][2];

        for( int i=0; i<2; i++ )
        {
            for( int j=0; j<3; j++ )
            {
                double[] l_utility = utilityCalculationCartoPed( a, b,c,e,f,c_speed, l_angle, p_speed, noai, stopped, distance, i, j,p_self,p_other );
                l_payoffMatrix[i][j][0] = l_utility[0];
                l_payoffMatrix[i][j][1] = l_utility[1];
            }
        }
        return l_payoffMatrix;

    }
    private static double distance( final double l_distance )
    {
        if ( l_distance <= 0 ) return -(l_distance);
        return 0;
    }

    /*
     * dimension 1 or row = player1; dimension 2 or column = player 2
     * 0 = continue, 1 = decelerate, 2 = deviate
     * third dimension: 0 = player1's utility, 1 = player2's utility
     */

    private static double[] utilityCalculationCartoPed( double a, double b, double c, double e,double f,
                                                        double c_speed,
                                                        double l_angle, double p_speed, double noai, double stopped, double mindistance,
                                                        final int p_rowstrategyindicator, final int p_columnstrategyindicator,IBaseRoadUser p_self, IBaseRoadUser p_other )
    {
        double[] l_return = new double[2];

        {
            if ( p_rowstrategyindicator == 0 )
            {
                if ( p_columnstrategyindicator == 0 ) { l_return[0] = -100; l_return[1] = -100; }

                else
                {
                    l_return[0] =  -a*speed(p_speed,0.65) + b*c_speed  + distance(mindistance) - f*angle(l_angle,8);

                    l_return[1] = ( p_columnstrategyindicator == 1 )? 3- 0*speed(p_speed,0.9) : 1  + 0*speed(p_speed,0.9)
                            + angleDev(l_angle,6);
                }
            }

            else
            {
                l_return[0] = ( p_columnstrategyindicator == 1 )? -50 :  0*stopped+0*noai+ f*angle(l_angle,8);
                l_return[1] = ( p_columnstrategyindicator == 1 )? -50
                        : ( p_columnstrategyindicator == 0 ) ? m_pedcontinue: angleDev(l_angle,6);

            }

        }
        return l_return;
    }



}

