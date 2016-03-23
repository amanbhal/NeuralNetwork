import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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
        int noOfAttributes = Integer.parseInt(prop.getProperty("no_of_attributes"));
        for (int i = 0; i < noOfAttributes + 1; i++) {
            String[] attrVals = prop.getProperty("index" + i).split(",");
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
        List<String> isDiscrete = Arrays.asList(prop.getProperty("is_discrete").split(","));
        // int noOfInputs =
        // Integer.parseInt(prop.getProperty("number_of_input_nodes"));
        int targetInputIndex = Integer.parseInt(prop.getProperty("target_index"));
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dataFile));
            String line = br.readLine();
            while (line != null) {

                List<Double> targetOutput = new ArrayList<Double>();
                List<Double> example = new ArrayList<Double>();
                String[] eachExample = line.split(",");

                for (int i = 0; i < eachExample.length; i++) {
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
                            example.add(getNormalizedVal(Integer.parseInt(eachExample[i])));
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

    private static Double getNormalizedVal(int val) {
        // TODO Auto-generated method stub
        return (double) val;
    }

    public static Matrix getMatrixRepresentation(List<Double> list) {
        // TODO Auto-generated method stub
        Matrix mat = new Matrix(list.size(), 1);
        for (int i = 0; i < list.size(); i++) {
            mat.set(i, 0, list.get(i));
        }
        return mat;
    }

}