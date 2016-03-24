import java.io.*;
import java.util.*;

import Jama.Matrix;

public class Parser {

    public static Properties loadControlFile(String controlFile) {

        InputStream fileInput = null;
        Properties prop = new Properties();
        try {
            fileInput = new FileInputStream(controlFile);
            prop.load(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }

    public static List<List<String>> getAttributeList(Properties prop) {

        List<List<String>> attributeList = new ArrayList<List<String>>();
        List<String> isDiscrete = Arrays.asList(prop.getProperty("is_discrete").split(","));
        int noOfAttributes = Integer.parseInt(prop.getProperty("no_of_attributes"));
        for (int i = 0; i < noOfAttributes + 1; i++) {
            String[] attrVals = new String[0];
            if (isDiscrete.get(i).equals("true")) {
                attrVals = prop.getProperty("index" + i).split(",");
            }
            List<String> attributeValues = Arrays.asList(attrVals);
            attributeList.add(attributeValues);
        }
        return attributeList;
    }

    public static List<List<List<Double>>> loadExampleData(String dataFile, Properties prop) {

        List<List<List<Double>>> result = new ArrayList<List<List<Double>>>();
        List<List<Double>> exampleList = new ArrayList<List<Double>>();
        List<List<Double>> targetOutputList = new ArrayList<List<Double>>();
        List<List<String>> attributeValueList = getAttributeList(prop);
        List<Map<Integer, Double>> minMaxList = getContinuousMinMapList(dataFile, prop);
        Map<Integer, Double> minMap = minMaxList.get(0);
        Map<Integer, Double> maxMap = minMaxList.get(1);

        List<String> isDiscrete = Arrays.asList(prop.getProperty("is_discrete").split(","));
        int targetInputIndex = Integer.parseInt(prop.getProperty("target_index"));
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dataFile));
            String line = br.readLine();
            outer: while (line != null) {

                List<Double> targetOutput = new ArrayList<Double>();
                List<Double> example = new ArrayList<Double>();
                String[] eachExample = line.split(",");

                for (int i = 0; i < eachExample.length; i++) {
                    if (eachExample[i].equals("?")) {
                        break outer;
                    }
                    if (targetInputIndex == i) {
                        for (int j = 0; j < attributeValueList.get(i).size(); j++) {
                            if (attributeValueList.get(i).get(j).equals(eachExample[i])) {
                                targetOutput.add(1.0);
                            } else {
                                targetOutput.add(0.0);
                            }
                        }
                    } else {
                        if (isDiscrete.get(i).equals("true")) {
                            for (int j = 0; j < attributeValueList.get(i).size(); j++) {
                                if (attributeValueList.get(i).get(j).equals(eachExample[i])) {
                                    example.add(1.0);
                                } else {
                                    example.add(0.0);
                                }
                            }
                        } else {
                            example.add(
                                    getNormalizedVal(Double.parseDouble(eachExample[i]), minMap.get(i), maxMap.get(i)));
                        }
                    }

                }
                targetOutputList.add(targetOutput);
                exampleList.add(example);
                line = br.readLine();
            }
        } catch (Exception e) {
            System.out.println("Error while reading the file. Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("Error while reading the file. Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        result.add(exampleList);
        result.add(targetOutputList);
        return result;
    }

    private static Double getNormalizedVal(double val, double minVal, double maxVal) {
        return (double) (val - minVal) / (maxVal - minVal);
    }

    public static List<Map<Integer, Double>> getContinuousMinMapList(String dataFile, Properties prop) {

        List<Map<Integer, Double>> minMaxList = new ArrayList<Map<Integer, Double>>();
        List<String> isDiscreteList = Arrays.asList(prop.getProperty("is_discrete").split(","));
        HashMap<Integer, Double> minMap = new HashMap<Integer, Double>();
        HashMap<Integer, Double> maxMap = new HashMap<Integer, Double>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dataFile));
            String line = br.readLine();
            outer: while (line != null) {

                String[] eachExample = line.split(",");
                for (int i = 0; i < eachExample.length; i++) {
                    if (eachExample[i].equals("?")) {
                        break outer;
                    }
                    if (!isDiscreteList.get(i).equals("true")) {
                        double val = Double.parseDouble(eachExample[i]);
                        if (!minMap.containsKey(i)) {
                            minMap.put(i, val);
                        } else {
                            if (val < minMap.get(i)) {
                                minMap.put(i, val);
                            }
                        }
                        if (!maxMap.containsKey(i)) {
                            maxMap.put(i, val);
                        } else {
                            if (val > maxMap.get(i)) {
                                maxMap.put(i, val);
                            }
                        }
                    }
                }
                line = br.readLine();
            }

        } catch (Exception e) {
            System.out.println("Error while reading the file. Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("Error while reading the file. Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        minMaxList.add(minMap);
        minMaxList.add(maxMap);

        return minMaxList;
    }

    public static Matrix getMatrixRepresentation(List<Double> list) {
        Matrix mat = new Matrix(list.size() + 1, 1);
        mat.set(0, 0, 1);
        for (int i = 1; i < mat.getRowDimension(); i++) {
            mat.set(i, 0, list.get(i - 1));
        }
        return mat;
    }

    public static Matrix getUnitMatrix(int row, int col) {

        Matrix unitMatrix = new Matrix(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                unitMatrix.set(i, j, 1);
            }
        }
        return unitMatrix;
    }

    public static Matrix getEachIJProduct(Matrix a, Matrix b) {

        int rows = a.getRowDimension();
        int cols = a.getColumnDimension();

        Matrix resultMat = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                resultMat.set(i, j, a.get(i, j) * b.get(i, j));
            }
        }
        return resultMat;
    }

    public static void randomizeExampleSet(List<List<Double>> examples, List<List<Double>> outputs) {

        for (int i = 0; i < examples.size(); i++) {
            int randomIndex = (int) ((examples.size()) * Math.random());
            Collections.swap(examples.get(i), i, randomIndex);
            Collections.swap(outputs.get(i), i, randomIndex);
        }

    }

    public static List<List<List<Double>>> categorizeDataSet(int i, List<List<Double>> examples,
                                                             List<List<Double>> outputs) {

        int oneTenth = examples.size() / 10;
        List<List<List<Double>>> categorizedDataSet = new ArrayList<List<List<Double>>>();
        List<List<Double>> trainingExampleSet = new ArrayList<List<Double>>();
        List<List<Double>> trainingOutputSet = new ArrayList<List<Double>>();
        List<List<Double>> validationExampleSet = new ArrayList<List<Double>>();
        List<List<Double>> validationOutputSet = new ArrayList<List<Double>>();
        List<List<Double>> testingExampleSet = new ArrayList<List<Double>>();
        List<List<Double>> testingOutputSet = new ArrayList<List<Double>>();

        for (int j = 0; j < examples.size(); j++) {
            if (j >= i && j < (i + oneTenth)) {
                testingExampleSet.add(examples.get(j));
                testingOutputSet.add(outputs.get(j));
            } else {
                if (j % 3 == 0) {
                    validationExampleSet.add(examples.get(j));
                    validationOutputSet.add(outputs.get(j));
                } else {
                    trainingExampleSet.add(examples.get(j));
                    trainingOutputSet.add(outputs.get(j));
                }
            }
        }
        categorizedDataSet.add(trainingExampleSet);
        categorizedDataSet.add(trainingOutputSet);
        categorizedDataSet.add(validationExampleSet);
        categorizedDataSet.add(validationOutputSet);
        categorizedDataSet.add(testingExampleSet);
        categorizedDataSet.add(testingOutputSet);

        return categorizedDataSet;
    }

}