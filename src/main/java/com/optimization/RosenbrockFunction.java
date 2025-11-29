package com.optimization;

public class RosenbrockFunction implements ObjectiveFunction {
    private int dimensions;
    private double lowerBound = -5;
    private double upperBound = 10;

    public RosenbrockFunction(int dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public double evaluate(double[] x) {
        double sum = 0;
        for (int i = 0; i < dimensions - 1; i++) {
            sum += 100 * Math.pow(x[i + 1] - x[i] * x[i], 2) + Math.pow(1 - x[i], 2);
        }
        return sum;
    }

    @Override
    public double[] getBounds() {
        return new double[]{lowerBound, upperBound};
    }

    @Override
    public int getDimensions() {
        return dimensions;
    }
}
