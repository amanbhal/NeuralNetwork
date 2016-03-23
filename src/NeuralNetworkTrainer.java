/**
 * Created by amanb on 3/22/2016.
 */
import Jama.Matrix;

import java.util.*;
public class NeuralNetworkTrainer {
    NeuralNetwork neuralNetwork;
    int maxNumOfIterations;
    List<Matrix> input;
    List<Matrix> expectedOutput;
    int numOfInstances;
    int numOfLayers;
    double learningRate = 0.2;    //TODO initialize
    double threshold;       //TODO initialize

    public NeuralNetworkTrainer(NeuralNetwork neuralNetwork, int maxNumOfIterations, List<Matrix> input, List<Matrix> expectedOutput) {
        this.neuralNetwork = neuralNetwork;
        this.maxNumOfIterations = maxNumOfIterations;
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.numOfLayers = this.neuralNetwork.networkLayers.size();
        this.numOfInstances = this.input.size();
    }

    public NeuralNetwork trainer(){
        double errorValue = Double.MAX_VALUE;
        while(errorValue>threshold){
            Matrix error = new Matrix(this.neuralNetwork.numOfNeuronsInOutputLayer,1);
            for(int i=0; i<this.numOfInstances; i++){
                Matrix inputVector = this.input.get(i);
                Matrix expectedOutputVector = this.expectedOutput.get(i);
                this.neuralNetwork = frontProp(inputVector);
                if(i==this.numOfInstances-1){
                    NeuronLayer outputLayer = this.neuralNetwork.networkLayers.get(this.numOfLayers-1);
                    error = outputLayer.activationVector.minus(expectedOutputVector);
                }
                this.neuralNetwork = backProp(expectedOutputVector);
            }
            int numOfNeuronsInOutputLayer = this.neuralNetwork.numOfNeuronsInOutputLayer;
            //TODO calculation of error
            for(int i=0; i<numOfNeuronsInOutputLayer; i++){
                errorValue += error.get(i,0);
            }
            if(errorValue<this.threshold){
                break;
            }
        }
        return this.neuralNetwork;
    }

    public NeuralNetwork backProp(Matrix expectedOutputVector){
        return this.neuralNetwork;
    }

    public NeuralNetwork frontProp(Matrix inputVector){
        NeuronLayer currentLayer = this.neuralNetwork.networkLayers.get(0);
        currentLayer.setInputVector(inputVector);
        for(int i=1; i<this.numOfLayers; i++){
            Matrix weightMatrixOfPrevLayer = this.neuralNetwork.networkLayers.get(i-1).weightMatrix;
            Matrix activationMatrixOfPrevLayer = this.neuralNetwork.networkLayers.get(i-1).activationVector;
            Matrix inputMatrixOfCurrLayer = weightMatrixOfPrevLayer.times(activationMatrixOfPrevLayer);
            currentLayer = this.neuralNetwork.networkLayers.get(i);
            currentLayer.setActivationVector(inputMatrixOfCurrLayer);
        }
        return this.neuralNetwork;
    }
}
