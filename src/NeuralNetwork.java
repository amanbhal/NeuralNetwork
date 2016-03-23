/**
 * Created by amanb on 3/21/2016.
 */
import Jama.Matrix;

import java.util.*;
public class NeuralNetwork {
    int numberOfHiddenLayers;
    int numOfNeuronsInInputLayer;
    int numOfNeuronsInOutputLayer;
    List<NeuronLayer> networkLayers;

    public NeuralNetwork(int numberOfHiddenLayers, int numOfNeuronsInInputLayer, int numOfNeuronsInOutputLayer){
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numOfNeuronsInInputLayer = numOfNeuronsInInputLayer;
        this.numOfNeuronsInOutputLayer = numOfNeuronsInOutputLayer;
        this.networkLayers = new ArrayList<NeuronLayer>();
        buildNeuralNetwork();
    }

    public void buildNeuralNetwork(){
        NeuronLayer inputLayer;
        if(this.numberOfHiddenLayers==0){
            inputLayer = new NeuronLayer(this.numOfNeuronsInInputLayer,this.numOfNeuronsInOutputLayer);
        }
        else{
            inputLayer = new NeuronLayer(this.numOfNeuronsInInputLayer,this.numOfNeuronsInInputLayer);
        }
        this.networkLayers.add(inputLayer);
        for(int i=0; i<this.numberOfHiddenLayers-1; i++){
            NeuronLayer hiddenLayer = new NeuronLayer(this.numOfNeuronsInInputLayer,this.numOfNeuronsInInputLayer);
            this.networkLayers.add(hiddenLayer);
        }
        if(this.numberOfHiddenLayers!=0){
            NeuronLayer hiddenLayer = new NeuronLayer(this.numOfNeuronsInInputLayer,this.numOfNeuronsInOutputLayer);
            this.networkLayers.add(hiddenLayer);
        }
        NeuronLayer outputLayer = new NeuronLayer(this.numOfNeuronsInOutputLayer,1);
        this.networkLayers.add(outputLayer);
    }
}
