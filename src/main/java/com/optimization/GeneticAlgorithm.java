package com.optimization;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    private int populationSize;
    private int chromosomeLength; // bits per variable * dimensions mb incorrect
    private double mutationRate;
    private int maxGenerations;
    private ObjectiveFunction function;
    private Random random = new Random();
    private double lowerBound, upperBound;
    private int dimensions;
    private List<Individual> population;

    public GeneticAlgorithm(int populationSize, double mutationRate, int maxGenerations, ObjectiveFunction function) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.maxGenerations = maxGenerations;
        this.function = function;
        this.dimensions = function.getDimensions();
        double[] bounds = function.getBounds();
        this.lowerBound = bounds[0];
        this.upperBound = bounds[1];
        this.chromosomeLength = 32 * dimensions; // 32 bits per var
        initializePopulation();
    }

    private void initializePopulation() {
        population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            boolean[] chromosome = new boolean[chromosomeLength];
            for (int j = 0; j < chromosomeLength; j++) {
                chromosome[j] = random.nextBoolean();
            }
            population.add(new Individual(chromosome)); //
        }
    }

    public double[] optimize() {
        for (int gen = 0; gen < maxGenerations; gen++) {

            evaluatePopulation();
            List<Individual> newPopulation = new ArrayList<>();

            // idk maybe will be edited
            Individual best = getBestIndividual();
            newPopulation.add(best);

            while (newPopulation.size() < populationSize) {
                Individual parent1 = tournamentSelection();
                Individual parent2 = tournamentSelection();
                Individual[] children = crossover(parent1, parent2);
                mutate(children[0]);
                mutate(children[1]);
                newPopulation.add(children[0]);
                newPopulation.add(children[1]);
            }
            population = newPopulation.subList(0, populationSize);


            if (gen > 0 && getBestFitness() == population.get(0).fitness) {
                mutationRate *= 1.1;
                if (mutationRate > 0.1) mutationRate = 0.1;
            } else {
                mutationRate *= 0.9;
                if (mutationRate < 0.001) mutationRate = 0.001;
            }
        }
        Individual best = getBestIndividual();
        return best.getPhenotype();
    }

    private void evaluatePopulation() {
        for (Individual ind : population) {
            double[] phenotype = ind.getPhenotype();
            ind.fitness = function.evaluate(phenotype);
        }
    }

    private Individual tournamentSelection() {
        Individual best = population.get(random.nextInt(populationSize));
        for (int i = 1; i < 3; i++) {
            Individual contender = population.get(random.nextInt(populationSize));
            if (contender.fitness < best.fitness) best = contender;
        }
        return new Individual(best.chromosome.clone());
    }

    private Individual[] crossover(Individual p1, Individual p2) {
        int point = random.nextInt(chromosomeLength - 1) + 1;
        boolean[] c1 = new boolean[chromosomeLength];
        boolean[] c2 = new boolean[chromosomeLength];
        for (int i = 0; i < point; i++) {
            c1[i] = p1.chromosome[i];
            c2[i] = p2.chromosome[i];
        }
        for (int i = point; i < chromosomeLength; i++) {
            c1[i] = p2.chromosome[i];
            c2[i] = p1.chromosome[i];
        }
        return new Individual[]{new Individual(c1), new Individual(c2)};
    }

    private void mutate(Individual ind) {
        for (int i = 0; i < chromosomeLength; i++) {
            if (random.nextDouble() < mutationRate) {
                ind.chromosome[i] = !ind.chromosome[i];
            }
        }
    }

    private Individual getBestIndividual() {
        evaluatePopulation();
        Individual best = population.get(0);
        for (Individual ind : population) {
            if (ind.fitness < best.fitness) best = ind;
        }
        return best;
    }

    private double getBestFitness() {
        return getBestIndividual().fitness;
    }

    private class Individual {
        boolean[] chromosome;
        double fitness;

        public Individual(boolean[] chromosome) {
            this.chromosome = chromosome;
            this.fitness = Double.MAX_VALUE;
        }

        public double[] getPhenotype() {
            double[] x = new double[dimensions];
            int bitsPerVar = chromosomeLength / dimensions;
            for (int d = 0; d < dimensions; d++) {
                long val = 0;
                for (int i = 0; i < bitsPerVar; i++) {
                    val = (val << 1) | (chromosome[d * bitsPerVar + i] ? 1 : 0);
                }
                x[d] = lowerBound + (upperBound - lowerBound) * val / (Math.pow(2, bitsPerVar) - 1);
            }
            return x;
        }
    }
}
