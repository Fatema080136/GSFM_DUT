package org.mysimulationmodel.simulation.agent;

import org.lightjason.agentspeak.action.binding.IAgentAction;
import org.lightjason.agentspeak.action.binding.IAgentActionFilter;
import org.lightjason.agentspeak.action.binding.IAgentActionName;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.mysimulationmodel.simulation.common.CForce;
import org.mysimulationmodel.simulation.common.CGame;
import org.mysimulationmodel.simulation.common.CVector;
import org.mysimulationmodel.simulation.environment.CEnvironment;

import javax.annotation.Nonnull;
import javax.vecmath.Vector2d;
import java.util.*;
import java.util.stream.IntStream;



/**
 * Created by fatema on 14.03.2018.
 * Class for game host (an central entity for all game)
 * randomly chosen leader
 * @todo run genetic algotithm both with and without helpingfunctionofacceleration
 * @todo NoSC need to initialize
 * @todo add rules to decide leader
 * @todo in case of multiple conflicts not multi-user conflict pedestrian need to select one of all strategies he/she got by playing with multiple users
 */
@IAgentAction
public final class CHostAgent extends IBaseAgent<CHostAgent>
{
    private static final long serialVersionUID = -2111543876806742109L;
    private CEnvironment m_env;
    private Map<IBaseRoadUser, List<IBaseRoadUser>> l_viewedroadusers = new HashMap<>();
    private Map<Integer, String> l_strategy = new HashMap<>();
    private List<IBaseRoadUser> l_closedlist = new ArrayList<>();
    public double m_distance;
    public double m_collisionchekingfactor;
    int m_pixelpermeter = CEnvironment.getpixelpermeter();

    //CGame parameter start
    private int m_randomization;
    private String u_id;
    //CGame parameter end

    /**
     * ctor
     *
     * @param p_configuration agent configuration
     */
    CHostAgent( @Nonnull IAgentConfiguration<CHostAgent> p_configuration, final CEnvironment p_env )
    {
        super( p_configuration );
        m_env = p_env;
        l_strategy.put( 0, "force/accelerate" );
        l_strategy.put( 1, "force/decelerate" );
        l_strategy.put( 2, "force/deviate" );
    }

    // overload agent-cycle
    @Override
    public final CHostAgent call() throws Exception
    {
        // run default cycle
        return super.call();
    }

    /*
     * perceive environment and get competitive road users for each users
     * Checked
     */
    @IAgentActionFilter
    @IAgentActionName( name = "host/perceive" )
    final void perceive()
    {

        /*
         * 14.03.2018
         * only for cars
         * get the list of cars which falls within the current car's FOV
         * currently I only consider interaction between two cars
         * @todo multiple cars interaction and multiple cars and multiple pedestrians interaction
         * if competitive user is also car then for road zone environment there will be no game playing(current environment)
         * for roundabout zone there will be game playing between cars
         */
        m_env.getCarinfo().stream()
                //.filter( i -> i.getSpeed() != 0 )
                .filter( i -> i.getname().equals(u_id))
                .forEach( i ->
                {
                    Vector2d l_desireddirection = CVector.direction( i.getGoalposition(), i.getPosition() );

                    // list of competitive user
                    List<IBaseRoadUser> l_neighbourlist = new ArrayList<>();

                    //distance when ego user start playing
                    final double[] l_distance = {i.m_distance};//38;
                    ArrayList<IBaseRoadUser> l_pedinfront = new ArrayList<>();

                    m_env.getPedestrianinfo().stream().filter( k -> !i.equals( k ) && !( i.getCarfollowingActive()== 1
                            && i.getCarFollowingCar().getCurrentlyPlayingWith().contains(k) ) &&
                            CVector.manhattanDistance( i.getPosition(), k.getPosition() ) <= i.m_distance ).forEach( j ->
                    {
                        double l_angle2 = CForce.getViewAngle( l_desireddirection.x, l_desireddirection.y,
                                ( j.getPosition().x - i.getPosition().x), ( j.getPosition().y - i.getPosition().y ) );

                        if ( ( l_angle2 <= 8 || l_angle2 >= 352 ) && (!i.getCurrentlyPlayingWith().contains(j)|| j.getCurrentBehavior() == 1
                                ||i.getCurrentBehavior() == 0))
                        {
                            l_pedinfront.add(j);
                        }
                        if( ( ( l_angle2 > 58 && l_angle2 <= 113 ) || ( l_angle2 >= 247 && l_angle2 < 302 ) )
                            && CForce.collisionCheckingfordeceleration( j,i,m_collisionchekingfactor ) )
                        {
                            i.getDeviateList().add(j);
                        }
                        if ( ( l_angle2 <= 113 || l_angle2 >= 247 ) && CForce.collisionCheckingfordeceleration( j,i,m_collisionchekingfactor ) )
                        {
                            l_neighbourlist.add( j );
                        }
                    });


                    if ( l_neighbourlist.isEmpty() )
                    {
                        i.beliefbase().remove( CLiteral.from("force", CRawTerm.from( 1.0 ) ) );
                        i.beliefbase().remove( CLiteral.from("force", CRawTerm.from( 0.0 ) ) );
                        i.beliefbase().add( CLiteral.from("force", CRawTerm.from( 3.0 ) ) );
                        i.setBehavior( 3 );

                        i.getCurrentlyPlayingWith().clear();
                    }

                    else
                    {
                        //if compitative users are not in ego user's FOV then delete them from getcurrentlyplayingwith() list,
                        // otherwise delete from l_neighborlist
                        for( int l= 0; l<i.getCurrentlyPlayingWith().size(); l++)
                        {
                            if( !l_neighbourlist.contains( i.getCurrentlyPlayingWith().get(l) ) )
                            {
                                i.getCurrentlyPlayingWith().remove(i.getCurrentlyPlayingWith().get(l));
                            }
                            else l_neighbourlist.remove(i.getCurrentlyPlayingWith().get(l));
                        }

                        final IBaseRoadUser[] l_test = {null};

                        // to get the nearest competitive road user
                        if(l_pedinfront.isEmpty())
                            for ( IBaseRoadUser l_neighbour : l_neighbourlist )
                            {
                                i.get_competitiveUserList().add( l_neighbour );
                                if ( CVector.distance( i.getPosition(), l_neighbour.getPosition() ) <= l_distance[0])
                                {
                                    l_test[0] = l_neighbour;
                                    l_distance[0] = CVector.distance(i.getPosition(), l_neighbour.getPosition());
                                }
                            }

                        //number of simultaneous conflict
                        i.setNoSC( l_neighbourlist.size() );

                       if (!l_pedinfront.isEmpty() )
                        {
                            m_env.m_playedgame = 1;
                            i.beliefbase().remove(CLiteral.from("force", CRawTerm.from(3.0)));
                            i.beliefbase().remove( CLiteral.from("force", CRawTerm.from( 0.0 )));
                            i.beliefbase().add(CLiteral.from("force", CRawTerm.from(1.0)));
                            i.trigger(CTrigger.from(
                                    ITrigger.EType.ADDGOAL,
                                    CLiteral.from("force/decelerate")));
                            m_env.setStrategy(i.getname(), 1);

                                // if one or more pedestrian is stopping as a result of game they played with i in before cycles, they will also start walking now.
                            if ( !i.getCurrentlyPlayingWith().isEmpty() )
                            {
                                i.getCurrentlyPlayingWith().stream().filter( s -> s.getCurrentBehavior() == 1 ).forEach( f ->
                                        {
                                            CForce.helpingfunctionofaccelaration( i,f);
                                            f.setBehavior(0);
                                            f.beliefbase().remove(CLiteral.from("force", CRawTerm.from(1.0)));
                                            f.beliefbase().add(CLiteral.from("force", CRawTerm.from(3.0)));
                                            l_closedlist.add( f );
                                            m_env.setStrategy(f.getname(), 0 );
                                        }
                                    );

                                }

                                //adding all pedestrians currently on car i's FOV to CurrentlyPlayingWith() list and vise versa
                                l_pedinfront
                                        .forEach( x ->
                                        {
                                            CForce.helpingfunctionofaccelaration( i,x);
                                            i.getCurrentlyPlayingWith().add( x );
                                            x.getCurrentlyPlayingWith().add( 0,i );
                                            x.beliefbase().remove(CLiteral.from("force", CRawTerm.from(1.0)));
                                            x.beliefbase().remove(CLiteral.from("force", CRawTerm.from(2.0)));
                                            x.beliefbase().remove( CLiteral.from("force", CRawTerm.from( 0.0 ) ) );
                                            x.beliefbase().add(CLiteral.from("force", CRawTerm.from(3.0)));
                                            m_env.setStrategy(x.getname(), 0 );
                                            l_closedlist.add( x );
                                        } );

                                /*i.getCurrentlyPlayingWith().stream().filter( s -> s.getCurrentBehavior() == 0 ).forEach( f ->
                                        {
                                            if ( CVector.distance( i.getPosition(), f.getPosition() ) <= l_distance[0])
                                            {
                                                l_test[0] = f;
                                                l_distance[0] = CVector.distance(i.getPosition(), f.getPosition());
                                            }
                                        }
                                );

                                i.getCurrentlyPlayingWith().remove(l_test[0]);
                                i.getCurrentlyPlayingWith().add(0, l_test[0]);*/
                                l_test[0] = null;
                            }

                        //l_viewedroadusers.put( i, l_test[0]);
                        List l_competetive = new ArrayList<>();
                        l_competetive.add(0,l_test[0]);
                        l_viewedroadusers.put( i, l_competetive);
                    }

                } );

        // multiple car to ped interaction for road zone
        l_viewedroadusers.forEach( (i,j) ->
        {
            l_viewedroadusers.forEach( (k,l) ->
            {
                if ( i !=k && !j.isEmpty() && !l.isEmpty() && j.get(0) == l.get(0) )
                {
                    j.add( 1, k);
                    l.clear();
                }
            });
        });

    }

    /**
     * trigger agents when found any collision
     */
    @IAgentActionFilter
    @IAgentActionName( name = "host/trigger/otheragents" )
    public void triggerAgents( )
    {
        l_viewedroadusers.forEach( ( IBaseRoadUser i, List<IBaseRoadUser> m ) ->
        {   m_env.m_playedgame = 1;
            //multiple car to ped interaction for road zone
            IBaseRoadUser j = null;
            if ( !m.isEmpty() )  j = m.get(0);

            // if i and j already played with each other then they will continue with the game result ( !i.getCurrentlyPlayingWith().contains( j ) && !j.getCurrentlyPlayingWith().contains( i ) )
            if  ( j != null && !i.getCurrentlyPlayingWith().contains( j )
                    && !j.getCurrentlyPlayingWith().contains( i ) && !l_closedlist.contains( i ) )
            {
                //set decelerationrate to 0
                i.setM_deceleration(0);
                ArrayList<Integer> l_strategyset;

                if((i.getCurrentBehavior() == 1 || j.getCurrentBehavior()== 0) && (m.size()>1 ))
                {
                    l_strategyset = new ArrayList<>();
                    l_strategyset.add(0,1); l_strategyset.add(1,0);
                }
                else l_strategyset = getstrategies( i, j, 2, 2, 3 );

                i.beliefbase().remove( CLiteral.from("force", CRawTerm.from( 3.0 ) ) );
                i.setBehavior(l_strategyset.get(0));
                m_env.setStrategy(i.getname(), l_strategyset.get(0) );

                // handling multiple conflict
                if ( m.size()>1 && m.get(1).getCurrentBehavior() != 1 )
                    {
                        m.get(1).beliefbase().remove( CLiteral.from("force", CRawTerm.from( 3.0 ) ) );
                        m.get(1).beliefbase().remove(CLiteral.from("force", CRawTerm.from(1.0)));
                        m.get(1).beliefbase().remove(CLiteral.from("force", CRawTerm.from(0.0)));
                        m.get(1).beliefbase().add(CLiteral.from("force", CRawTerm.from((double)l_strategyset.get(0))));
                        m.get(1).trigger(CTrigger.from(
                                ITrigger.EType.ADDGOAL, CLiteral.from(
                                        l_strategy.get(l_strategyset.get(0)))
                        ));
                        m.get(1).setBehavior(l_strategyset.get(0));
                        m.get(1).setM_deceleration(0);
                        m_env.setStrategy(m.get(1).getname(), l_strategyset.get(0) );
                        m.get(1).getCurrentlyPlayingWith().add(j);
                        j.getCurrentlyPlayingWith().add(m.get(1));
                    } // till here

                //to all pedestrians currently on car i's FOV
                if ( !i.get_competitiveUserList().isEmpty() && !l_strategyset.isEmpty() )
                {
                    if ( l_strategyset.get(1) == 2 && !i.getDeviateList().isEmpty() )
                    {
                        i.get_competitiveUserList().clear();
                        i.getDeviateList().forEach( x ->
                                {
                                    x.beliefbase().remove(CLiteral.from("force", CRawTerm.from(3.0)));
                                    x.beliefbase().remove(CLiteral.from("force", CRawTerm.from(1.0)));
                                    x.beliefbase().remove(CLiteral.from("force", CRawTerm.from(0.0)));
                                    x.beliefbase().remove(CLiteral.from("force", CRawTerm.from(2.0)));

                                    x.beliefbase().add(CLiteral.from("force", CRawTerm.from((double)l_strategyset.get(1))));
                                    x.setBehavior(l_strategyset.get(1));

                                    m_env.setStrategy(x.getname(), l_strategyset.get(1) );

                                    x.trigger(CTrigger.from(
                                            ITrigger.EType.ADDGOAL, CLiteral.from(
                                                    l_strategy.get(l_strategyset.get(1)))));

                                    i.getCurrentlyPlayingWith().add(x);
                                    x.getCurrentlyPlayingWith().add(0,i);
                                    l_closedlist.add(x);

                                }
                        );
                    }
                    if( !i.get_competitiveUserList().isEmpty() )
                    i.get_competitiveUserList().stream().filter(x->i.getDeviateList().isEmpty() || !i.getDeviateList().contains(x) )
                            .forEach(x ->
                    {
                        x.beliefbase().remove(CLiteral.from("force", CRawTerm.from(3.0)));
                        x.beliefbase().remove(CLiteral.from("force", CRawTerm.from(1.0)));
                        x.beliefbase().remove(CLiteral.from("force", CRawTerm.from(0.0)));
                        x.beliefbase().remove(CLiteral.from("force", CRawTerm.from(2.0)));

                        x.beliefbase().add(CLiteral.from("force", CRawTerm.from((double)l_strategyset.get(1))));
                        x.setBehavior(l_strategyset.get(1));
                        m_env.setStrategy(x.getname(), l_strategyset.get(1) );
                        if (l_strategyset.get(1) == 0)
                            CForce.helpingfunctionofaccelaration(i, x);

                        x.trigger(CTrigger.from(
                                ITrigger.EType.ADDGOAL, CLiteral.from(
                                        l_strategy.get(l_strategyset.get(1)))));

                    });

                    // if one or more pedestrian is stopping as a result of game they played with i in before cycles, they will also start walking now.
                    if ( l_strategyset.get(0) == 1 && !i.getCurrentlyPlayingWith().isEmpty() )
                    {
                        i.getCurrentlyPlayingWith().stream().filter( s -> s.getCurrentBehavior() == 1 ).forEach( f ->
                                {   f.setBehavior(0);
                                    CForce.helpingfunctionofaccelaration( i,f);
                                    f.beliefbase().remove(CLiteral.from("force", CRawTerm.from(1.0)));
                                    f.beliefbase().add(CLiteral.from("force", CRawTerm.from(0.0)));
                                    f.trigger(CTrigger.from(
                                            ITrigger.EType.ADDGOAL, CLiteral.from("force/accelerate")));
                                    m_env.setStrategy(f.getname(), 0 );
                                    l_closedlist.add( f );
                                }
                        );
                    }
                    i.beliefbase().remove(CLiteral.from("force", CRawTerm.from(1.0)));
                    i.beliefbase().remove(CLiteral.from("force", CRawTerm.from(0.0)));
                    i.beliefbase().add(CLiteral.from("force", CRawTerm.from((double)l_strategyset.get(0))));
                    i.trigger(CTrigger.from(
                            ITrigger.EType.ADDGOAL, CLiteral.from(
                                    l_strategy.get(l_strategyset.get(0)))
                    ));

                    //adding all pedestrians currently on car i's FOV to CurrentlyPlayingWith() list and vise versa
                    if(!i.get_competitiveUserList().isEmpty())
                    i.get_competitiveUserList().stream().filter(y -> y.getType() == 1)
                            .forEach(x ->
                            {
                                i.getCurrentlyPlayingWith().add(x);
                                x.getCurrentlyPlayingWith().add(0,i);
                                l_closedlist.add(x);
                            });

                    // in case of multiple conflict

                    if(m.size()>1)
                    i.getCurrentlyPlayingWith().stream().filter( s -> s.getType()== 1 && s.getCurrentBehavior() == 0
                            && s.getCurrentlyPlayingWith().contains(m.get(1)) )
                            .forEach(
                                    x ->
                                    {
                                        CForce.helpingfunctionofaccelaration( m.get(1), x);
                                    }

                            );


                    final double[] l_distance = {10000}; IBaseRoadUser[] l_test = {j};
                    i.getCurrentlyPlayingWith().stream().filter( s -> s.getCurrentBehavior() == 0 ).forEach( f ->
                            {
                                if ( CVector.distance( i.getPosition(), f.getPosition() ) <= l_distance[0])
                                {
                                    l_test[0] = f;
                                    l_distance[0] = CVector.distance(i.getPosition(), f.getPosition());
                                }
                            }
                    );
                    i.getCurrentlyPlayingWith().remove(l_test[0]);
                    i.getCurrentlyPlayingWith().add(0,l_test[0]);
                }

            }

        } );

        l_closedlist.clear();
        l_viewedroadusers.keySet().forEach( i -> i.get_competitiveUserList().clear() );
        l_viewedroadusers.clear();

    }

    /*
     * function to get the strategy couple
     * Game type 1 = car-to-car
     * Game type 2 = car-to-ped
     */
    private ArrayList<Integer> getstrategies( final IBaseRoadUser p_self, final IBaseRoadUser p_other, final int p_gametype, final int p_row, final int p_column )
    {
        final double[] valueCheck = {-1000};
        int[] columnCheck = {0};
        int l_stopped = p_self.getCurrentBehavior() == 1 ? 1 : 0;
        Vector2d l_desireddirection = CVector.direction( p_self.getGoalposition(), p_self.getPosition() );
        double l_distance =  CVector.manhattanDistance( p_self.getPosition(), p_other.getPosition()) -5*m_pixelpermeter;//
        double l_angle = CForce.getViewAngle( l_desireddirection.x, l_desireddirection.y,
                ( p_other.getPosition().x - p_self.getPosition().x), ( p_other.getPosition().y - p_self.getPosition().y ) );

        if ((l_angle < 16 && l_angle >= 0 ) || l_angle > 344)
        l_angle = 8;
        else if ( (l_angle <= 42 && l_angle >= 16 ) || (l_angle <= 344 && l_angle >= 318 ))
        l_angle = 7;
        else if ((l_angle <= 65 && l_angle > 42 ) || (l_angle < 318 && l_angle >= 295 ))
        l_angle = 6;
        else if ((l_angle <= 90 && l_angle > 65 ) || (l_angle < 295 && l_angle >= 270 ))
        l_angle = 5;
        else if ((l_angle < 112 && l_angle > 90 ) ||  (l_angle < 270 && l_angle > 248 ))
        l_angle = 4;
        else
        l_angle = 1;

        final ArrayList<Integer> finalIndex = new ArrayList<>();
        double[][][] l_matrix = CGame.payoffMatrixCalculationCartoPed( 0, 4.0, 0, 0, 6.6,(p_self.getSpeed())/m_pixelpermeter, l_angle, p_other.getSpeed()/m_pixelpermeter,
            p_self.get_competitiveUserList().size(), l_stopped, l_distance/m_pixelpermeter,p_self,p_other);

        for( int m= 0; m< 2 ; m++ )//run only once
        {
            final int[] l_column = {0};
            for ( int n = 1; n <= 3-1; n++ )
            {
                l_column[0] = ( l_matrix[m][l_column[0]][1] > l_matrix[m][n][1] )? l_column[0] :
                        ( l_matrix[m][l_column[0]][1] < l_matrix[m][n][1] ) ? n :
                                ( l_matrix[m][l_column[0]][0] >= l_matrix[m][n][0] )? l_column[0] : n;
            }

            if ( valueCheck[0] <= l_matrix[m][l_column[0]][0] )
            {
                if ( m == 1 && valueCheck[0] == l_matrix[m][l_column[0]][0]
                        && l_matrix[m][l_column[0]][1] < l_matrix[0][columnCheck[0]][1] )
                {
                    finalIndex.clear();
                    finalIndex.add( 0, 0 );
                    finalIndex.add( 1, columnCheck[0] );
                }
                else
                {
                    columnCheck[0] = l_column[0];
                    valueCheck[0] = l_matrix[m][l_column[0]][0];
                    finalIndex.clear();
                    finalIndex.add( 0, m );
                    finalIndex.add( 1, l_column[0] );
                }
            }
        }
        //only to print the game matrix
        /*IntStream.range( 0, 2 )
                .forEach( m ->
                {
                    IntStream.range( 0, 3 )
                            .forEach( n ->
                            {
                                System.out.print( l_matrix[m][n][0] + "  "+ l_matrix[m][n][1] +"            ");
                            });
                    System.out.println();
                } );*/
        return finalIndex;
    }


    /**
     * trigger agents when found any collision
     */
    @IAgentActionFilter
    @IAgentActionName( name = "host/again/trigger" )
    public void triggeragain()
    {
        m_env.getRoadUserinfo().forEach( i ->
        {
            i.trigger(
                    CTrigger.from(
                            ITrigger.EType.ADDGOAL,
                            CLiteral.from(
                                    "freely/moving"
                            )
                    ));
        });

    }

    public void updateParameter(double p_distance, double p_collisionchekingfactor, int p_randomization )
    {
        m_distance = p_distance;
        m_collisionchekingfactor = p_collisionchekingfactor;
        m_randomization = p_randomization;
    }

    public void updateParameter(double p_distance, double p_collisionchekingfactor, int p_randomization, String p_id )
    {
        m_distance = p_distance;
        m_collisionchekingfactor = p_collisionchekingfactor;
        m_randomization = p_randomization;
        u_id = p_id;
    }

}
