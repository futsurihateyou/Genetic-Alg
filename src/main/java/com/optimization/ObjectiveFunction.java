package com.optimization;

public interface ObjectiveFunction {
    double evaluate(double[] x); // Evaluate fitness at point x
    double[] getBounds(); // Return [lower, upper] bounds for each dimension
    int getDimensions(); // Number of dimensions
}