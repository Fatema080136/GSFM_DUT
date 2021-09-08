package org.mymodel.simulation.routechoice;

import org.junit.Test;
import org.mysimulationmodel.simulation.common.CCsvReadWrite;
import org.mysimulationmodel.simulation.common.CHausdorf;
import org.mysimulationmodel.simulation.routechoice.MyHausdorfDistanceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CTestHausdorfDistance
{
    MyHausdorfDistanceImpl l_bla = new MyHausdorfDistanceImpl();
    final String l_name = "C:\\Users\\fatema\\Desktop\\frame\\mysimulationmodel\\hausdorf.csv";
    private static final String FILE_HEADER = "id,distance";


    @Test
    public void test()
    {

        Map<String, HashMap<Double, MyHausdorfDistanceImpl.Position>> l_real = CHausdorf.realDataFromCSV();
        Map<String, HashMap<Double, MyHausdorfDistanceImpl.Position>> l_sim = CHausdorf.simulatedDataFromCSV();
        Map<String, Double> bla = new HashMap<>();
        l_sim.forEach( (i,j) ->
        {
            bla.put( i, l_bla.distanceHausdorff( j, l_real.get( i )));
        });
        System.out.println( " "+ bla.get("c17"));
        CCsvReadWrite.writeCsvFile( l_name, bla, FILE_HEADER);
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
        new CTestHausdorfDistance().test();
    }

}
