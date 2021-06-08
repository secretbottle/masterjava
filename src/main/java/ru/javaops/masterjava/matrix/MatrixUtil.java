package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        List<Callable<Integer>> callables = new ArrayList<>();

        for (int i = 0; i < matrixSize; i++) {
            int finalI = i;

            Callable<Integer> callable = () -> {
                int[] matrixBColumn = new int[matrixSize];

                for (int k = 0; k < matrixSize; k++) {
                    matrixBColumn[k] = matrixB[k][finalI];
                }

                for (int j = 0; j < matrixSize; j++) {
                    int[] matrixARow = matrixA[j];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += matrixBColumn[k] * matrixARow[k];
                    }
                    matrixC[finalI][j] = sum;
                }
                return null;
            };

            callables.add(callable);

        }

        executor.invokeAll(callables);

        return matrixC;
    }

    //TODO ForkJoin
    public static int[][] concurrentMultiplyForkJoin(int[][] matrixA, int[][] matrixB) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        List<ForkJoinTask<?>> tasks = new ArrayList<>();

        for (int i = 0; i < matrixSize; i++) {
            int finalI = i;

            ForkJoinTask<?> forkJoinTask = forkJoinPool.submit(() -> {
                int[] matrixBColumn = new int[matrixSize];

                for (int k = 0; k < matrixSize; k++) {
                    matrixBColumn[k] = matrixB[k][finalI];
                }

                for (int j = 0; j < matrixSize; j++) {
                    int[] matrixARow = matrixA[j];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += matrixBColumn[k] * matrixARow[k];
                    }
                    matrixC[finalI][j] = sum;
                }
            });

            tasks.add(forkJoinTask);
        }

        for (ForkJoinTask<?> task : tasks) {
            task.join();
        }

        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int[] matrixBColumn = new int[matrixSize];

        for (int i = 0; i < matrixSize; i++) {

            for (int k = 0; k < matrixSize; k++) {
                matrixBColumn[k] = matrixB[k][i];
            }

            for (int j = 0; j < matrixSize; j++) {
                int[] matrixARow = matrixA[j];
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixBColumn[k] * matrixARow[k];
                }
                matrixC[i][j] = sum;
            }
        }

        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
