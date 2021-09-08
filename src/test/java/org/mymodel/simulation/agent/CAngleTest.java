package org.mymodel.simulation.agent;

import org.junit.Test;
import org.mysimulationmodel.simulation.common.CForce;
import org.mysimulationmodel.simulation.common.CVector;
import javax.vecmath.Vector2d;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;


/**
 * Created by fatema on 06.03.2018.
 */
public class CAngleTest
{
    private Vector2d point1 = new Vector2d(  290, 135 );
    private Vector2d point2 = new Vector2d(  300, 105 );
    private Vector2d point3 = new Vector2d( -0.20, 1.0 );
    private Vector2d point4 = new Vector2d( -1.0, 0.5 );

    private Vector2d point5 = new Vector2d( 0.0, 1 );
    private Vector2d point6 = new Vector2d( 1.0, 0 );
    private Vector2d point7 = new Vector2d( 50, 50 );
    private Vector2d point8 = new Vector2d( 30, 30 );
    private Vector2d dir7 = new Vector2d( 0.25, 1 );
    private Vector2d dir8 = new Vector2d( -0.25, 1 );
    private List<Long> aList = LongStream.rangeClosed(1, 2_000_000_0).boxed()
            .collect(Collectors.toList());

    /**
     * test of a correct working route with some obstacles
     */
    @Test
    public void test()
    {

        System.out.println( "current road user "+CForce.getViewAngle( dir7.x, dir7.y, -20, -20 ));
        System.out.println( "competitive road user "+CForce.getViewAngle( dir8.x, dir8.y, -20, -20 ));
        System.out.println( dir7.dot( dir8));

        //System.out.println( "with subtract function "+CForce.getViewAngle( dir7.x, dir7.y, CVector.subtract( point8, point7).x, CVector.subtract( point8, point7).y));

    }

    @Test
    public void testgroupvission()
    {
        Stream.iterate(1, n  ->  n  + 2)
                .limit(500000)
                .parallel()
                .forEach(System.out::println);
        double xDiff = 63 - 68;
        double xSqr = Math.pow(xDiff, 2);

        double yDiff = 37 - 46;
        double ySqr = Math.pow(yDiff, 2);

        double distance = Math.sqrt(xSqr + ySqr);
        System.out.println( distance+ " " +1/distance);

    }

    @Test
    public void testgroup() throws ExecutionException, InterruptedException
    {
        ForkJoinPool customThreadPool = new ForkJoinPool(4);
        System.out.println( customThreadPool.submit(
                () -> aList.parallelStream().reduce(0L, Long::sum)).get() );
    }

    @Test
    public void onlyStream()
    {

        System.out.println( aList.stream().reduce(0L, Long::sum) );

    }

    @Test
    public void usingloop()
    {
        long actualTotal = 0;
        for(long i = 1; i<=20000000; i= i+1)
        {
            actualTotal = actualTotal + i;
        }
        System.out.println( actualTotal/20000000 );
    }

    @Test
    public void parallelStream()
    {
        //p_speed = -(start_speed + max_speed/2) * 5;
        //CVector.direction( new Vector2d(66.00545816,49.30636067) --> (endx, endy),
        // new Vector2d(80.60256493,46.98345734) --> (startx,starty)
        Vector2d l_test = CVector.scale(-3.619181059*2, CVector.direction(
                new Vector2d(27.84869604,23.06762033), new Vector2d(83.88006855,55.02288889)));

        //new Vector2d(80.60256493,46.98345734) --> (startx,starty)
        Vector2d start_position = CVector.add( l_test, new Vector2d(83.88006855,55.02288889));

        //p_speed = (start_speed + max_speed/2) * 5;
        //CVector.direction( new Vector2d(66.00545816,49.30636067) --> (endx, endy),
        // new Vector2d(80.60256493,46.98345734) --> (startx,starty)
        Vector2d l_test2 = CVector.scale(1.92152874*5, CVector.direction( new Vector2d(66.00545816,49.30636067),
                new Vector2d(80.60256493,46.98345734)));
        double distance = CVector.subtract( new Vector2d(23.16296226,12.45378588), new Vector2d(20.89495932,10.52035656)).length();
        double acceleration = 5.801319392 - 5.485218055;
        System.out.println( 2*distance + " acceleration "+2*acceleration);
        //new Vector2d(80.60256493,46.98345734) --> (endx,endy)
        Vector2d end_position = CVector.add( l_test2, new Vector2d(66.00545816,49.30636067));
        System.out.println( start_position + " " + end_position );
        System.out.println(1/3.2583348327333073E10f);
        System.out.println(CVector.subtract(new Vector2d(20.89495932,10.52035656),new Vector2d(23.16296226,12.45378588)).length()*2);

    }

    @Test
    public void testfor()
    {
        for(int i = 1; i<=500000; i= i+2)
        {
            System.out.println(i);
        }
    }

    @Test
    public void testforloop()
    {
        //new Genetic_Algorithm().avera
    }

    @Test
    public void testarea()
    {
        System.out.println(
        CVector.distance( new Vector2d(58.15428498,45.05673406), new Vector2d(70.26971483,38.79933891)));
        System.out.println(
                CVector.distance( new Vector2d(49.95141308,37.80895194), new Vector2d(52.88211011,31.37532976)));

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
       // new CAngleTest().testgroup();
        new CAngleTest().testgroupvission();
    }
}
