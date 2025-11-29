package com.optimization;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        // Таб для Генетического алгоритма
        Tab gaTab = new Tab("Генетический алгоритм");
        VBox gaBox = new VBox();
        TextField gaPopSize = new TextField("100");
        TextField gaMutRate = new TextField("0.01");
        TextField gaGens = new TextField("100");
        ChoiceBox<String> funcChoice = new ChoiceBox<>();
        funcChoice.getItems().addAll("Sphere", "Rosenbrock", "Rastrigin", "Ackley");
        funcChoice.setValue("Sphere");
        TextField dims = new TextField("2");
        Button runGa = new Button("Запустить ГА");
        TextArea gaResult = new TextArea();
        runGa.setOnAction(e -> {
            int pop = Integer.parseInt(gaPopSize.getText());
            double mut = Double.parseDouble(gaMutRate.getText());
            int gens = Integer.parseInt(gaGens.getText());
            int dim = Integer.parseInt(dims.getText());
            ObjectiveFunction func = getFunction(funcChoice.getValue(), dim);
            GeneticAlgorithm ga = new GeneticAlgorithm(pop, mut, gens, func);
            double[] best = ga.optimize();
            gaResult.setText("Лучшая позиция: " + arrayToString(best) + "\nЗначение функции: " + func.evaluate(best));
        });
        gaBox.getChildren().addAll(new Label("Размер популяции:"), gaPopSize, new Label("Скорость мутации:"), gaMutRate, new Label("Поколения:"), gaGens,
                new Label("Функция:"), funcChoice, new Label("Размерность:"), dims, runGa, gaResult);
        gaTab.setContent(gaBox);

        // Таб для Метода роя частиц
        Tab psoTab = new Tab("Метод роя частиц");
        VBox psoBox = new VBox();
        TextField psoSwarmSize = new TextField("50");
        TextField psoC1 = new TextField("2.0");
        TextField psoC2 = new TextField("2.0");
        TextField psoIters = new TextField("100");
        CheckBox useLBest = new CheckBox("Использовать LBEST");
        ChoiceBox<String> psoFuncChoice = new ChoiceBox<>();
        psoFuncChoice.getItems().addAll("Sphere", "Rosenbrock", "Rastrigin", "Ackley");
        psoFuncChoice.setValue("Sphere");
        TextField psoDims = new TextField("2");
        Button runPso = new Button("Запустить МРЧ");
        TextArea psoResult = new TextArea();
        runPso.setOnAction(e -> {
            int swarm = Integer.parseInt(psoSwarmSize.getText());
            double c1 = Double.parseDouble(psoC1.getText());
            double c2 = Double.parseDouble(psoC2.getText());
            int iters = Integer.parseInt(psoIters.getText());
            int dim = Integer.parseInt(psoDims.getText());
            ObjectiveFunction func = getFunction(psoFuncChoice.getValue(), dim);
            ParticleSwarmOptimization pso = new ParticleSwarmOptimization(swarm, c1, c2, iters, func, useLBest.isSelected());
            double[] best = pso.optimize();
            psoResult.setText("Лучшая позиция: " + arrayToString(best) + "\nЗначение функции: " + func.evaluate(best));
        });
        psoBox.getChildren().addAll(new Label("Размер роя:"), psoSwarmSize, new Label("C1:"), psoC1, new Label("C2:"), psoC2,
                new Label("Итерации:"), psoIters, useLBest, new Label("Функция:"), psoFuncChoice, new Label("Размерность:"), psoDims, runPso, psoResult);
        psoTab.setContent(psoBox);

        // Таб для Экспериментов
        Tab expTab = new Tab("Эксперименты");
        VBox expBox = new VBox();
        TextField expDims = new TextField("2");
        TextField expRuns = new TextField("10");
        Button runExp = new Button("Запустить эксперименты");
        TextArea expResult = new TextArea();
        runExp.setOnAction(e -> {
            int dim = Integer.parseInt(expDims.getText());
            int runs = Integer.parseInt(expRuns.getText());
            ExperimentRunner.runExperiments(dim, runs);
            expResult.setText("Эксперименты завершены. Результаты сохранены в experiment_results.json");
        });
        expBox.getChildren().addAll(new Label("Размерность:"), expDims, new Label("Запусков на алгоритм/функцию:"), expRuns, runExp, expResult);
        expTab.setContent(expBox);

        tabPane.getTabs().addAll(gaTab, psoTab, expTab);

        Scene scene = new Scene(tabPane, 600, 400);
        scene.getRoot().setStyle("-fx-font-family: 'Arial';");  // Добавьте эту строку
        primaryStage.setTitle("Алгоритмы оптимизации");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ObjectiveFunction getFunction(String name, int dim) {
        switch (name) {
            case "Sphere": return new SphereFunction(dim);
            case "Rosenbrock": return new RosenbrockFunction(dim);
            case "Rastrigin": return new RastriginFunction(dim);
            case "Ackley": return new AckleyFunction(dim);
            default: return new SphereFunction(dim);
        }
    }

    private String arrayToString(double[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (double d : arr) sb.append(d).append(", ");
        return sb.substring(0, sb.length() - 2) + "]";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
