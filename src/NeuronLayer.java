import Jama.Matrix;

/**
 * Created by amanb on 3/21/2016.
 */


public class NeuronLayer {
    public int numberOfNeurons;
    public int numberOfNeuronsInNextLayer = -1;

    public Matrix activationVector;
    public Matrix errorVector;
    public Matrix weightMatrix;
    public Matrix deltaWeightMatrix;

    public NeuronLayer(int numberOfNeurons, int numberOfNeuronsInNextLayer, Matrix activationVector){
        this.numberOfNeuronsInNextLayer = numberOfNeuronsInNextLayer;
        this.numberOfNeurons = numberOfNeurons+1;
        this.activationVector = new Matrix(this.numberOfNeurons);
        this.errorVector = new Matrix(this)
    }
}
