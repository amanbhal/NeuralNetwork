import Jama.Matrix;

import java.util.*;

public class Main {
    public static Properties properties;
    public static void main(String[] args) {
        // TODO int noOfHiddenLayer = args[0];
        Parser parse = new Parser();
        String controlFile = "C:/Users/amanb/OneDrive/Documents/TAMU/Spring 2016/Machine Learning/Project 2 (Neural Network)/Data/playtennisControl.txt";
        String dataFile = "C:/Users/amanb/OneDrive/Documents/TAMU/Spring 2016/Machine Learning/Project 2 (Neural Network)/Data/playtennis.txt";
        properties = Parser.loadControlFile(controlFile);

        List<List<List<Double>>> dataList = Parser.loadExampleData(dataFile, properties);
        List<List<Double>> inputList = dataList.get(0);//makeInputList(dataList);
        List<List<Double>> outputList = dataList.get(1);//makeOutputList(dataList);

        int numOfNeuronsInInputLayer = inputList.get(0).size();
        int numOfNeuronsInOutputLayer = outputList.get(0).size();

        NeuralNetwork neuralNetwork = new NeuralNetwork(0,numOfNeuronsInInputLayer,numOfNeuronsInOutputLayer);

        List<Matrix> input = new ArrayList<Matrix>();
        for(int i=0; i<inputList.size(); i++){
            Matrix a = new Matrix(numOfNeuronsInInputLayer,1);
            for(int j=0; j<inputList.get(i).size(); j++)
                a.set(j,0,inputList.get(i).get(j));
            input.add(a);
        }
        List<Matrix> expectedOutput = new ArrayList<Matrix>();
        for(int i=0; i<outputList.size(); i++){
            Matrix a = new Matrix(numOfNeuronsInOutputLayer,1);
            for(int j=0; j<outputList.get(i).size(); j++)
                a.set(j,0,outputList.get(i).get(j));
            expectedOutput.add(a);
        }
        NeuralNetworkTrainer trainer = new NeuralNetworkTrainer(neuralNetwork,5000,input,expectedOutput);
    }
}
