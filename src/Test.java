import Jama.Matrix;

import java.util.List;

/**
 * Created by amanb on 3/23/2016.
 */
public class Test {
    List<Matrix> input;
    List<Matrix> output;
    NeuralNetwork neuralNetwork;

    public Test(List<Matrix> input, List<Matrix> output, NeuralNetwork neuralNetwork) {
        this.input = input;
        this.output = output;
        this.neuralNetwork = neuralNetwork;
    }

    public double run(){
        int numOfInputNeurons = this.neuralNetwork.numOfNeuronsInInputLayer;
        int correct = 0;
        for(int j=0; j<this.input.size(); j++){
            Matrix in = this.input.get(j);
            Matrix expected = this.output.get(j);
            this.neuralNetwork = frontProp(in);
            Matrix out = this.neuralNetwork.networkLayers.get(this.neuralNetwork.networkLayers.size()-1).activationVector;
            out = out.getMatrix(1,out.getRowDimension()-1,0,0);
            correct += matches(out,expected);
        }
        return ((double)correct/(double)input.size())*100;
    }

    public NeuralNetwork frontProp(Matrix inputVector){
        NeuronLayer currentLayer = this.neuralNetwork.networkLayers.get(0);
        currentLayer.setInputVector(inputVector);
        for(int i=1; i<this.neuralNetwork.networkLayers.size(); i++){
            Matrix weightMatrixOfPrevLayer = this.neuralNetwork.networkLayers.get(i-1).weightMatrix;
            Matrix activationMatrixOfPrevLayer = this.neuralNetwork.networkLayers.get(i-1).activationVector;
            Matrix inputMatrixOfCurrLayer = weightMatrixOfPrevLayer.times(activationMatrixOfPrevLayer);
            currentLayer = this.neuralNetwork.networkLayers.get(i);
            currentLayer.setActivationVector(inputMatrixOfCurrLayer);
        }
        return this.neuralNetwork;
    }

    public int matches(Matrix output, Matrix expected){
        //output = output.getMatrix(1,output.getRowDimension()-1,0,output.getColumnDimension()-1);
        double maxVal = Double.MIN_VALUE;
        double maxIndexA = -1;

        for(int i=0; i<output.getRowDimension(); i++){
            if(output.get(i,0)>maxVal){
                maxVal = output.get(i,0);
                maxIndexA = i;
            }
        }

        maxVal = Double.MIN_VALUE;
        double maxIndexB = -1;

        for(int i=0; i<expected.getRowDimension(); i++){
            if(expected.get(i,0) > maxVal){
                maxVal = expected.get(i,0);
                maxIndexB = i;
            }
        }

        return maxIndexA==maxIndexB?1:0;
    }
}
