import Jama.Matrix;

import java.util.*;

public class Main {
    public static Properties properties;
    public static void main(String[] args) {
        // TODO int noOfHiddenLayer = args[0];
        Parser parse = new Parser();
        String controlFile = "C:/Users/amanb/OneDrive/Documents/TAMU/Spring 2016/Machine Learning/Project 2 (Neural Network)/Data/irisControl.txt";
        String dataFile = "C:/Users/amanb/OneDrive/Documents/TAMU/Spring 2016/Machine Learning/Project 2 (Neural Network)/Data/iris.txt";
        properties = Parser.loadControlFile(controlFile);
        List<List<List<Double>>> dataList = Parser.loadExampleData(dataFile, properties);

        List<List<Double>> inputList = dataList.get(0);     //input list of training data
        List<List<Double>> outputList = dataList.get(1);    //output list of training data



        int numOfNeuronsInInputLayer = inputList.get(0).size();
        int numOfNeuronsInOutputLayer = outputList.get(0).size();
        int numOfHiddenLayers = 2;
        NeuralNetwork neuralNetwork = new NeuralNetwork(numOfHiddenLayers,numOfNeuronsInInputLayer,numOfNeuronsInOutputLayer);

        //convert inputList to Matrix input
        List<Matrix> input = new ArrayList<Matrix>();
        convertListToMatrix(inputList,input);

        //convert outputList to Matrix expectedOutput
        List<Matrix> expectedOutput = new ArrayList<Matrix>();
        convertListToMatrix(outputList,expectedOutput);
        NeuralNetworkTrainer trainer = new NeuralNetworkTrainer(neuralNetwork,5000,input,expectedOutput);
        trainer.train();

        Test tester = new Test(input,expectedOutput,neuralNetwork);
        double accuracy = tester.run();
        System.out.println("Accuracy is "+accuracy);

    }

    private static void convertListToMatrix(List<List<Double>> list, List<Matrix> matrix){
        int numOfNeurons = list.get(0).size();
        for(int i=0; i<list.size(); i++){
            Matrix a = new Matrix(numOfNeurons,1);
            for(int j=0; j<list.get(i).size(); j++)
                a.set(j,0,list.get(i).get(j));
            matrix.add(a);
        }
    }

    private static void getTrainingAndValidationList(List<List<List<Double>>> dataList, List<List<List<Double>>> trainingDataList, List<List<List<Double>>> testingDataList) {
        int length = dataList.size();
        int twoThird = (2*length)/3;
        for(int i=0; i<length; i++){
            if(i<twoThird){
                trainingDataList.add(dataList.get(i));
            }
            else{
                testingDataList.add(dataList.get(i));
            }
        }
    }

    private static void randomize(List<List<List<Double>>> dataList) {
        int length = dataList.size();
        for(int i=0; i<length; i++){
            int newPos = (int)(Math.random()*length);
            Collections.swap(dataList,i,newPos);
        }
    }
}
