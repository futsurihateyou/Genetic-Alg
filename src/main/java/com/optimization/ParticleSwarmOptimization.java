package com.optimization;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSwarmOptimization {
    private int swarmSize;
    private double c1, c2; // Cognitive and social coefficients
    private double wMax = 0.9, wMin = 0.4; // Inertia bounds
    private int maxIterations;
    private ObjectiveFunction function;
    private Random random = new Random();
    private double lowerBound, upperBound;
    private int dimensions;
    private List<Particle> swarm;
    private double[] globalBestPosition;
    private double globalBestFitness;
    private boolean useLBest = false; // Toggle LBEST

    public ParticleSwarmOptimization(int swarmSize, double c1, double c2, int maxIterations, ObjectiveFunction function, boolean useLBest) {
        this.swarmSize = swarmSize;
        this.c1 = c1;
        this.c2 = c2;
        this.maxIterations = maxIterations;
        this.function = function;
        this.useLBest = useLBest;
        this.dimensions = function.getDimensions();
        double[] bounds = function.getBounds();
        this.lowerBound = bounds[0];
        this.upperBound = bounds[1];
        initializeSwarm();
    }

    private void initializeSwarm() {
        swarm = new ArrayList<>();
        globalBestFitness = Double.MAX_VALUE;
        for (int i = 0; i < swarmSize; i++) {
            double[] position = new double[dimensions];
            double[] velocity = new double[dimensions];
            for (int d = 0; d < dimensions; d++) {
                position[d] = lowerBound + random.nextDouble() * (upperBound - lowerBound);
                velocity[d] = - (upperBound - lowerBound) + random.nextDouble() * 2 * (upperBound - lowerBound);
            }
            Particle p = new Particle(position, velocity);
            swarm.add(p);
            double fitness = function.evaluate(position);
            if (fitness < globalBestFitness) {
                globalBestFitness = fitness;
                globalBestPosition = position.clone();
            }
        }
    }

    public double[] optimize() {
        for (int iter = 0; iter < maxIterations; iter++) {
            double w = wMax - (wMax - wMin) * iter / maxIterations; // Linear decrease

            for (int i = 0; i < swarmSize; i++) {
                Particle p = swarm.get(i);
                double[] localBest = useLBest ? getNeighborhoodBest(i) : globalBestPosition;

                for (int d = 0; d < dimensions; d++) {
                    double r1 = random.nextDouble();
                    double r2 = random.nextDouble();
                    p.velocity[d] = w * p.velocity[d] +
                            c1 * r1 * (p.bestPosition[d] - p.position[d]) +
                            c2 * r2 * (localBest[d] - p.position[d]);
                    p.position[d] += p.velocity[d];
                    // Clamp bounds
                    if (p.position[d] < lowerBound) p.position[d] = lowerBound;
                    if (p.position[d] > upperBound) p.position[d] = upperBound;
                }

                double fitness = function.evaluate(p.position);
                if (fitness < p.bestFitness) {
                    p.bestFitness = fitness;
                    p.bestPosition = p.position.clone();
                }
                if (fitness < globalBestFitness) {
                    globalBestFitness = fitness;
                    globalBestPosition = p.position.clone();
                }
            }
        }
        return globalBestPosition;
    }

    private double[] getNeighborhoodBest(int index) {
        // LBEST: Ring topology, 2 neighbors
        int prev = (index - 1 + swarmSize) % swarmSize;
        int next = (index + 1) % swarmSize;
        Particle best = swarm.get(index);
        if (swarm.get(prev).bestFitness < best.bestFitness) best = swarm.get(prev);
        if (swarm.get(next).bestFitness < best.bestFitness) best = swarm.get(next);
        return best.bestPosition;
    }

    private class Particle {
        double[] position;
        double[] velocity;
        double[] bestPosition;
        double bestFitness;

        public Particle(double[] position, double[] velocity) {
            this.position = position;
            this.velocity = velocity;
            this.bestPosition = position.clone();
            this.bestFitness = function.evaluate(position);
        }
    }
}
