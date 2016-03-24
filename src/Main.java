import Jama.Matrix;

import java.lang.reflect.Array;
import java.util.*;

public class Main {
    public static Properties properties;
    static String controlFile ;
    static String dataFile;
    static int numOfNeuronsInInputLayer;
    static int numOfNeuronsInOutputLayer;
    static int numOfHiddenLayers;
    static double learningRate = 0.25;
    public static void main(String[] args) throws Exception{
        // TODO int noOfHiddenLayer = args[0];
        Parser parse = new Parser();
        controlFile = "C:/Users/amanb/OneDrive/Documents/TAMU/Spring 2016/Machine Learning/Project 2 (Neural Network)/Data/irisControl.txt";
        dataFile = "C:/Users/amanb/OneDrive/Documents/TAMU/Spring 2016/Machine Learning/Project 2 (Neural Network)/Data/iris.txt";
        properties = Parser.loadControlFile(controlFile);
        List<List<List<Double>>> dataList = Parser.loadExampleData(dataFile, properties);

        List<List<Double>> inputList = dataList.get(0);     //input list of training data
        List<List<Double>> outputList = dataList.get(1);    //output list of training data
        List<List<List<Double>>> totalDataList = new ArrayList<List<List<Double>>>();
        totalDataList = fillTotalDataList(inputList,outputList);
        numOfNeuronsInInputLayer = inputList.get(0).size();
        numOfNeuronsInOutputLayer = outputList.get(0).size();
        numOfHiddenLayers = 2;

        int hops = totalDataList.size()/10;
        System.out.println("10 CROSS VALIDATION");
        randomize(totalDataList);
        for(int i=0; i<totalDataList.size();i+=hops){
            List<Matrix> TestInputMatrix = new ArrayList<Matrix>();
            List<Matrix> TestOutputMatrix = new ArrayList<Matrix>();
            List<List<List<Double>>> TestDataList = new ArrayList<List<List<Double>>>();
            List<List<List<Double>>> NonTestDataList = new ArrayList<List<List<Double>>>();
            List<List<List<Double>>> ValidationDataList = new ArrayList<List<List<Double>>>();
            List<List<List<Double>>> TrainingDataList = new ArrayList<List<List<Double>>>();
            List<Matrix> ValidationInputMatrix = new ArrayList<Matrix>();
            List<Matrix> ValidationOutputMatrix = new ArrayList<Matrix>();
            List<Matrix> TrainingInputMatrix = new ArrayList<Matrix>();
            List<Matrix> TrainingOutputMatrix = new ArrayList<Matrix>();
            for(int j=0; j<totalDataList.size(); j++){
                if(j>=i && j<(i+hops))
                    TestDataList.add(totalDataList.get(j));
                else
                    NonTestDataList.add(totalDataList.get(j));
            }
            int twoThird = (NonTestDataList.size()*2)/3;
            int k = 0;
            for(;k<twoThird;k++){
                TrainingDataList.add(NonTestDataList.get(k));
            }
            for(;k<NonTestDataList.size();k++){
                ValidationDataList.add(NonTestDataList.get(k));
            }
            fillInputOutput(TestDataList,TestInputMatrix,TestOutputMatrix);
            fillInputOutput(ValidationDataList,ValidationInputMatrix,ValidationOutputMatrix);
            fillInputOutput(TrainingDataList,TrainingInputMatrix,TrainingOutputMatrix);

            List<List<Double>> mseInput = new ArrayList<List<Double>>();
            List<List<Double>> mseOutput = new ArrayList<List<Double>>();

            NeuralNetwork neuralNetwork = new NeuralNetwork(numOfHiddenLayers,numOfNeuronsInInputLayer,numOfNeuronsInOutputLayer);
            NeuralNetworkTrainer trainer = new NeuralNetworkTrainer(neuralNetwork,TrainingInputMatrix,TrainingOutputMatrix,learningRate);

            int x=1;
            for(;x<50;x++)
                trainer.train();

            double mse = calculateMSE(neuralNetwork,mseInput,mseOutput);
            double lowestMSE = Double.MAX_VALUE;
            NeuralNetwork bestNetwork = null;
            int limit = 100;
            int lowestMSEfoundAt = 0;
            do{
                if(mse<=lowestMSE){
                    lowestMSE = mse;
                    bestNetwork = neuralNetwork.copy();
                    lowestMSEfoundAt = x;
                    limit = x*2;
                }
                trainer.train();
                mse = calculateMSE(neuralNetwork,mseInput,mseOutput);
                x++;
            }while(x<limit && x<=20000);

            System.out.println("Lowest MSE found after "+ lowestMSEfoundAt + " iterations, checked further till "+limit +" iterations.");

            Test tester = new Test(TestInputMatrix,TestOutputMatrix,neuralNetwork);
            double accuracy = tester.run();
            System.out.println("Accuracy is "+accuracy);



            System.out.println("Iteration ends !!\n\n");
        }
    }

    private static double calculateMSE(NeuralNetwork neuralNetwork, List<List<Double>> mseInput, List<List<Double>> mseOutput) {
        double mse = 0;
        for(int i=0; i<mseInput.size(); i++){
            List<Double> input = mseInput.get(i);
            List<Double> expected = mseOutput.get(i);

            Matrix inputVector = new Matrix(numOfNeuronsInInputLayer+1,1);
            inputVector.set(0,0,1);
            for(int j=0; j<input.size(); j++){
                inputVector.set(j+1,0,input.get(j));
            }
            neuralNetwork.networkLayers.get(0).activationVector = inputVector;
            int totalLayers = neuralNetwork.networkLayers.size();
            for(int j=0; j<totalLayers-1; j++){
                Matrix wt = neuralNetwork.networkLayers.get(j).weightMatrix;
                Matrix myValue = neuralNetwork.networkLayers.get(j).activationVector;
                Matrix myOutput = wt.times(myValue);
                neuralNetwork.networkLayers.get(j+1).setActivationVector(myOutput);
            }
            Matrix output = neuralNetwork.networkLayers.get(totalLayers-1).activationVector;
            output = output.getMatrix(1,output.getRowDimension()-1,0,output.getColumnDimension()-1);

            double mseTemp = 0;
            for(int j=0; j<output.getRowDimension(); j++){
                mseTemp += Math.pow((output.get(j,0)-expected.get(j)),2);
            }
            mse += (mseTemp/output.getRowDimension());
        }
        return (mse/mseInput.size());
    }

    private static void fillInputOutput(List<List<List<Double>>> dataList, List<Matrix> inputMatrix, List<Matrix> outputMatrix) {
        List<List<Double>> inputList = new ArrayList<List<Double>>();
        List<List<Double>> outputList = new ArrayList<List<Double>>();
        for(int i=0; i<dataList.size(); i++){
            inputList.add(dataList.get(i).get(0));
            outputList.add(dataList.get(i).get(1));
        }
        convertListToMatrix(inputList,inputMatrix);
        convertListToMatrix(outputList,outputMatrix);
    }

    private static List<List<List<Double>>> fillTotalDataList(List<List<Double>> inputList, List<List<Double>> outputList) {
        List<List<List<Double>>> totalDataList = new ArrayList<List<List<Double>>>();
        for(int i=0; i<inputList.size(); i++){
            List<List<Double>> temp = new ArrayList<List<Double>>();
            temp.add(inputList.get(i));
            temp.add(outputList.get(i));
            totalDataList.add(temp);
        }
        return totalDataList;
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
