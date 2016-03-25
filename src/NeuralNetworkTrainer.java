/**
 * Created by amanb on 3/22/2016.
 */
import Jama.Matrix;

import java.util.*;
public class NeuralNetworkTrainer {
    NeuralNetwork neuralNetwork;
    List<Matrix> input;
    List<Matrix> expectedOutput;
    int numOfInstances;
    int numOfLayers;
    double learningRate;

    public NeuralNetworkTrainer(NeuralNetwork neuralNetwork, List<Matrix> input, List<Matrix> expectedOutput, double learningRate) {
        this.neuralNetwork = neuralNetwork;
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.learningRate = learningRate;
        this.numOfInstances = this.input.size();
        this.numOfLayers = this.neuralNetwork.networkLayers.size();
    }

    public NeuralNetwork train(){
        int numOfInputNeurons = this.neuralNetwork.numOfNeuronsInInputLayer;
        for(int j=0; j<this.numOfInstances; j++){
            Matrix in = this.input.get(j);
            Matrix out = this.expectedOutput.get(j);
            this.neuralNetwork = frontProp(in);
            this.neuralNetwork = backProp(out);
        }
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

    public NeuralNetwork backProp(Matrix expectedOutput){
        Matrix output = new Matrix(expectedOutput.getRowDimension()+1,1);
        output.set(0,0,1);
        for(int i=0; i<expectedOutput.getRowDimension(); i++){
            output.set(i+1,0,expectedOutput.get(i,0));
        }
        Matrix Ok = this.neuralNetwork.networkLayers.get(this.neuralNetwork.networkLayers.size()-1).activationVector;
        Matrix I = getIdentityMatrix(Ok.getRowDimension());

        NeuronLayer outputLayer = this.neuralNetwork.networkLayers.get(this.neuralNetwork.networkLayers.size()-1);
        outputLayer.errorVector = hadamardProduct(output.minus(Ok),hadamardProduct(Ok,(I.minus(Ok))));

        //back prop the error
        for(int i=this.neuralNetwork.networkLayers.size()-2; i>=0; i--){
            NeuronLayer currLayer = this.neuralNetwork.networkLayers.get(i);
            NeuronLayer nextLayer = this.neuralNetwork.networkLayers.get(i+1);
            Matrix Oj = currLayer.activationVector;
            Matrix Wjk = currLayer.weightMatrix.transpose();
            Matrix dk = removeFirstRow(nextLayer.errorVector);
            I = getIdentityMatrix(Oj.getRowDimension());

            Matrix a = Wjk.times(dk);
            Matrix c = I.minus(Oj);

            currLayer.errorVector = hadamardProduct(a,hadamardProduct(Oj,c));
        }

        //updating weight matrix for each layer
        for(int i=0; i<this.neuralNetwork.networkLayers.size()-1; i++){
            NeuronLayer currLayer = this.neuralNetwork.networkLayers.get(i);
            NeuronLayer nextLayer = this.neuralNetwork.networkLayers.get(i+1);
            currLayer.deltaWeightMatrix = nextLayer.errorVector.times(currLayer.activationVector.transpose()).times(this.learningRate);
            currLayer.deltaWeightMatrix = currLayer.deltaWeightMatrix.getMatrix(1,currLayer.deltaWeightMatrix.getRowDimension()-1,0,
                    currLayer.deltaWeightMatrix.getColumnDimension()-1);
            currLayer.weightMatrix = currLayer.weightMatrix.plus(currLayer.deltaWeightMatrix);
        }

        return this.neuralNetwork;
    }

    private Matrix removeFirstRow(Matrix a) {
        Matrix b = new Matrix(a.getRowDimension()-1,1);
        for(int i=1; i<a.getRowDimension(); i++){
            b.set(i-1,0,a.get(i,0));
        }
        return b;
    }

    private Matrix getIdentityMatrix(int rowDimension) {
        Matrix I = new Matrix(rowDimension,1);
        for(int i=0; i<rowDimension; i++){
            I.set(i,0,1);
        }
        return I;
    }

    private Matrix hadamardProduct(Matrix a, Matrix b){
        int rows =  a.getRowDimension();
        int cols = a.getColumnDimension();

        Matrix c = new Matrix(rows,cols);

        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                c.set(i,j,a.get(i,j)*b.get(i,j));
            }
        }

        return c;
    }

}
