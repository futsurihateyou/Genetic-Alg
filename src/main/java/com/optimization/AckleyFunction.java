package com.optimization;

public class AckleyFunction implements ObjectiveFunction {
    private int dimensions;
    private double lowerBound = -32.768;
    private double upperBound = 32.768;

    public AckleyFunction(int dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public double evaluate(double[] x) {
        double sum1 = 0, sum2 = 0;
        for (double xi : x) {
            sum1 += xi * xi;
            sum2 += Math.cos(2 * Math.PI * xi);
        }
        return -20 * Math.exp(-0.2 * Math.sqrt(sum1 / dimensions)) - Math.exp(sum2 / dimensions) + 20 + Math.E;
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
