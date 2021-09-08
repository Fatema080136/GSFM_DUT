package org.mysimulationmodel;


import org.apache.commons.math3.util.Precision;
import org.mysimulationmodel.simulation.common.CCSVFileReaderForGA;
import org.mysimulationmodel.simulation.common.CSampleOutput;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

public class Genetic_Algorithm_indiv
{
    static double crossRate = .9;
    static double mutRate = .01;
    static Random rand = new Random();
    static int populationSize = 40;
    static int chromolength=3;
    static int m_sid;
    static String m_uid;
    static int m_strategy;

    static Map< String, ArrayList<String>> m_realdata = CCSVFileReaderForGA.readDataFromCSV2();
    static Map<String,ArrayList<Integer>> input = CCSVFileReaderForGA.input();

    public static void main(String[] args)
    {
        long startTime = System.nanoTime();
        System.out.println("S "+System.nanoTime());
        /*input.forEach(
                (p,q) ->
                {
                    m_uid = p.split("@",2)[1];
                    m_sid = q.get(0);*/
                    //System.out.println(m_sid+" "+m_uid);
        ArrayList<chromosome> population;
        CopyOnWriteArrayList<chromosome> newPopulation=new CopyOnWriteArrayList();
        int gen=0;

        Genetic_Algorithm_indiv ga=new Genetic_Algorithm_indiv();

        population=ga.generate_Population();
        Collections.sort(population);
        // Loop until solution is found
        while(true) {
            // Clear the new pool
            newPopulation.clear();
            // Add to the generations
            gen++;
            chromosome n3 = new chromosome();//,Double.parseDouble(p.get(3))

            n3.duplicateChromosome(population.get(population.size()-1));

            chromosome n4 = new chromosome();
            n4.duplicateChromosome(population.get(population.size()-2));

            for(int x=population.size()-1;x>=2;x-=2)
            {
                // Select two members
                chromosome n1 = ga.selectMember(population);
                chromosome n2 = ga.selectMember(population);

                // Cross over and mutate
                n1.crossOver(n2);
                n1.mutate();
                n2.mutate();
                // Rescore the nodes
                try {
                    n1.score = n1.getFitnessScore();
                    n2.score = n2.getFitnessScore();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                // Add to the new pool
                newPopulation.add(n1);
                newPopulation.add(n2);
            }
            //keep the best 2 chromosome
            population.clear();
            // Add the newPool back to the old pool
            population.addAll(newPopulation);
            population.add(n3);
            population.add(n4);
            Collections.sort(population);

            //System.out.println(m_uid+ " s_id "+ m_sid + " i "+  population.get(population.size()-1).getScore()+" "+
            //population.get(population.size()-1).chromo);

            if (gen==50)
            {
                System.out.println(m_uid+ " s_id 1 fs"+ m_sid +" finish "+  population.get(population.size()-1).getScore()+" "+
                        population.get(population.size()-1).chromo);
                break;
            }

        }//});

        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println( totalTime );

    }

    public ArrayList generate_Population( )//, double p_max_speed
    {
        ArrayList<chromosome> population = new ArrayList<>();
        IntStream.range(0,populationSize)
                //.parallel()
                .forEach(i-> population.add(new chromosome()));
        return population;
    }

    //will also try another select method: Fitness proportionate method
    //Roulette Wheel Selection
    private chromosome selectMember(ArrayList l) {

        // Get the total fitness
        double tot=0.0;
        for (int x=l.size()-1;x>=0;x--)
        {
            double score = ((chromosome)l.get(x)).score;
            tot+=score;
        }
        double slice = tot*rand.nextDouble();

        // Loop to find the node
        double ttot=0.0;
        for (int x=l.size()-1;x>=0;x--) {
            chromosome node = (chromosome)l.get(x);
            ttot+=node.score;
            if (ttot>=slice) { l.remove(x); return node; }
        }

        return (chromosome)l.remove(l.size()-1);
    }

    //    responsible for creating each chromosome and
    private static class chromosome implements Comparable<chromosome>
    {

        ArrayList<Double> chromo = new ArrayList<>();
        private double score;


        public chromosome()//int p_sid, String p_uid, int p_strategy, double p_max_speed
        {
            for (int j = 0; j < chromolength; j++)
            { // give specific range for each chrome
                chromo.add(getRandomGene(j));
            }
            try {
                this.score=getFitnessScore();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        double getScore()
        {
            return this.score;
        }

        void duplicateChromosome(chromosome other)
        {   this.chromo.clear();
            for (int j = 0; j < chromolength; j++)
            {
                this.chromo.add(other.chromo.get(j));
            }
            this.score = other.score;

        }
        // give bounding as parameter // r.nextInt(high-low) + low
        private double getRandomGene(int gene)
        {
            Random ran= new Random();
            if(gene == 1)
                return ran.nextInt(chromo.get(0).intValue()-4)+ 4+Precision.round(ran.nextDouble(),2);
            if(gene == 2) {
                return ran.nextInt(chromo.get(1).intValue()-3) + 3 + Precision.round(ran.nextDouble(),2);}
            return ran.nextInt(8)+8+Precision.round(ran.nextDouble(),2);
        }

        // Crossover bits
        public final void crossOver(chromosome other)
        {
            // Should we cross over?
            if (rand.nextDouble() > crossRate) return;


            // Generate a random position
            int pos = rand.nextInt(chromolength);

            // Swap all chars after that position
            for (int x=pos;x<chromolength;x++) {
                // Get our character
                Double tmp = chromo.get(x);

                // Swap the chars
                chromo.set(x, other.chromo.get(x));
                other.chromo.set(x, tmp);
            }
        }

        // Mutation
        public final void mutate() {
            for (int x=0;x<chromolength;x++)
            {
                if (rand.nextDouble()<=mutRate)
                //chromo.set(x, rand.nextDouble()+rand.nextInt(11));
                {
                    chromo.set(x, Math.abs(Precision.round(chromo.get(x) +  rand.nextGaussian(),1)));// * chromo.get(x)/2
                    break;
                }

            }
        }

        public double average(List<Double> p_list )
        {
            double actualTotal = 0;
            for( int i = 0; i<= p_list.size()-1; i++)
            {
                actualTotal = actualTotal + p_list.get(i);
            }

            return actualTotal/(float)p_list.size();
        }

        //For fitness input chromosome and scenario, and output json object contains scenario->time->p/c->position
        public double getFitnessScore( ) throws SQLException
        {
            ArrayList<Double> l_totalfitness = new ArrayList();
            input.forEach(
                    (p,q) ->
                    {
                        m_uid = p.split("@",2)[1];
                        m_sid = q.get(0);
                        ArrayList<CSampleOutput> eachScenarioResult;
                        eachScenarioResult = CMain_GA.runSimulation(
                                this.chromo.get(0), this.chromo.get(1), this.chromo.get(2), m_sid, m_uid);

                        List<Double> l_scenariofitness = Collections.synchronizedList(new ArrayList<Double>());
                        eachScenarioResult.forEach(
                                j ->
                                {
                                    double simulatedX = j.m_selfX;
                                    double simulatedY = j.m_selfY;

                                    double realX = -1;
                                    double realY = -1;
                                    ArrayList<String> l_temp = m_realdata.get(new StringBuffer(new StringBuffer(String.valueOf(j.m_timestep))
                                            .append(j.m_id)).append(m_sid).toString());

                                    if (l_temp != null) {
                                        realX = Double.parseDouble(l_temp.get(0));
                                        realY = Double.parseDouble(l_temp.get(1));

                                    }
                                    if (realX >= 0 && realY >= 0) {
                                        double xDiff = realX - simulatedX;
                                        double xSqr = Math.pow(xDiff, 2);

                                        double yDiff = realY - simulatedY;
                                        double ySqr = Math.pow(yDiff, 2);

                                        double distance = Math.sqrt(xSqr + ySqr);
                                        if (distance != 0.0) l_scenariofitness.add((double) (1 / (float) distance));

                                    }

                                }

                        );
                        l_totalfitness.add(average(l_scenariofitness));
                    });
            return average(l_totalfitness);//l_totalfitness.get(0);//
        }

        @Override
        public int compareTo(chromosome o)
        {
            if(this.score>o.score)
                return 1;
            else
                return -1;
        }

    }

}