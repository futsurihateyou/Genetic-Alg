package com.optimization;

public class SphereFunction implements ObjectiveFunction {
    private int dimensions;
    private double lowerBound = -5.12;
    private double upperBound = 5.12;

    public SphereFunction(int dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public double evaluate(double[] x) {
        double sum = 0;
        for (double xi : x) {
            sum += xi * xi;
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
