package com.optimization;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExperimentRunner {
    public static void runExperiments(int dimensions, int runs) {
        ObjectiveFunction[] functions = {
                new SphereFunction(dimensions),
                new RosenbrockFunction(dimensions),
                new RastriginFunction(dimensions),
                new AckleyFunction(dimensions)
        };
        String[] funcNames = {"Sphere", "Rosenbrock", "Rastrigin", "Ackley"};

        JSONArray results = new JSONArray();

        for (int f = 0; f < functions.length; f++) {
            ObjectiveFunction func = functions[f];
            JSONObject funcResult = new JSONObject();
            funcResult.put("function", funcNames[f]);

            JSONArray gaResults = new JSONArray();
            for (int r = 0; r < runs; r++) {
                long start = System.currentTimeMillis();
                GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.01, 100, func);
                double[] bestPos = ga.optimize();
                double bestFit = func.evaluate(bestPos);
                long time = System.currentTimeMillis() - start;
                JSONObject runRes = new JSONObject();
                runRes.put("run", r);
                runRes.put("best_fitness", bestFit);
                runRes.put("time_ms", time);

                JSONArray coordinates = new JSONArray();
                for (double coord : bestPos) {
                    coordinates.put(coord);
                }
                runRes.put("coordinates", coordinates);

                gaResults.put(runRes);
            }
            funcResult.put("GA", gaResults);

            JSONArray psoResults = new JSONArray();
            for (int r = 0; r < runs; r++) {
                long start = System.currentTimeMillis();
                ParticleSwarmOptimization pso = new ParticleSwarmOptimization(50, 2.0, 2.0, 100, func, true); // Use LBEST
                double[] bestPos = pso.optimize();
                double bestFit = func.evaluate(bestPos);
                long time = System.currentTimeMillis() - start;
                JSONObject runRes = new JSONObject();
                runRes.put("run", r);
                runRes.put("best_fitness", bestFit);
                runRes.put("time_ms", time);

                JSONArray coordinates = new JSONArray();
                for (double coord : bestPos) {
                    coordinates.put(coord);
                }
                runRes.put("coordinates", coordinates);

                psoResults.put(runRes);
            }
            funcResult.put("PSO", psoResults);

            results.put(funcResult);
        }

        try (FileWriter file = new FileWriter("experiment_results.json")) {
            file.write(results.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}