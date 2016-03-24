import Jama.Matrix;

import java.io.Serializable;

/**
 * Created by amanb on 3/21/2016.
 */


public class NeuronLayer implements Serializable {
    private static final long serialVersionUID = 4526472641452109247L;
    public int numberOfNeurons;
    public int numberOfNeuronsInNextLayer;

    public Matrix activationVector;     //frontProp
    public Matrix errorVector;          //backProp
    public Matrix weightMatrix;         //set during initialization and update during backProp
    public Matrix deltaWeightMatrix;    //backProp

    public NeuronLayer(int numberOfNeurons, int numberOfNeuronsInNextLayer){
        this.numberOfNeuronsInNextLayer = numberOfNeuronsInNextLayer;
        this.numberOfNeurons = numberOfNeurons+1;
        this.activationVector = new Matrix(this.numberOfNeurons,1);
        this.errorVector = new Matrix(this.numberOfNeurons,1);
        this.weightMatrix = new Matrix(this.numberOfNeuronsInNextLayer,this.numberOfNeurons);
        this.deltaWeightMatrix = new Matrix(this.numberOfNeuronsInNextLayer,this.numberOfNeurons);
        setRandomWeight(this.weightMatrix);
    }

    public void setRandomWeight(Matrix weightMatrix){
        for(int i=0; i<this.numberOfNeuronsInNextLayer; i++){
            for(int j=0; j<this.numberOfNeurons; j++){
                double value = (Math.random()*0.1)-0.05;
                weightMatrix.set(i,j,value);
            }
        }
    }
    //set output of each layer used in frontProp
    public void setActivationVector(Matrix activationVector){
        this.activationVector.set(0,0,1);
        for(int i=1; i<this.numberOfNeurons; i++){
            double value = calculateSigmoid(activationVector.get(i-1,0));
            this.activationVector.set(i,0,value);
        }
    }
    //set output of input layer used in backProp
    public void setInputVector(Matrix activationVector){
        this.activationVector.set(0,0,1);
        for(int i=1; i<this.numberOfNeurons; i++){
            this.activationVector.set(i,0,activationVector.get(i-1,0));
        }
    }

    public double calculateSigmoid(double input){
        return (1/(1+Math.exp(-input)));
    }

}
