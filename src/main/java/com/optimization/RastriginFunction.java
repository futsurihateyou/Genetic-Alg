package com.optimization;

public class RastriginFunction implements ObjectiveFunction {
    private int dimensions;
    private double lowerBound = -5.12;
    private double upperBound = 5.12;

    public RastriginFunction(int dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public double evaluate(double[] x) {
        double sum = 10 * dimensions;
        for (double xi : x) {
            sum += xi * xi - 10 * Math.cos(2 * Math.PI * xi);
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
