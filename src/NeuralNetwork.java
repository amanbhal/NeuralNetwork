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
    }

    public void buildNeuralNetwork(){
        NeuronLayer inputLayer;
        if(this.numberOfHiddenLayers==0){
            inputLayer = new NeuronLayer(this.numOfNeuronsInInputLayer,this.numOfNeuronsInOutputLayer);
        }
        else{
            inputLayer = new NeuronLayer(this.numOfNeuronsInInputLayer,this.numOfNeuronsInInputLayer);
        }
        networkLayers.add(inputLayer);
        for(int i=0; i<this.numberOfHiddenLayers-1; i++){
            NeuronLayer hiddenLayer = new NeuronLayer(this.numOfNeuronsInInputLayer,this.numOfNeuronsInInputLayer);
            networkLayers.add(hiddenLayer);
        }
        NeuronLayer hiddenLayer = new NeuronLayer(this.numOfNeuronsInInputLayer,this.numOfNeuronsInOutputLayer);
        networkLayers.add(hiddenLayer);
        NeuronLayer outputLayer = new NeuronLayer(this.numOfNeuronsInInputLayer,-1);
        networkLayers.add(outputLayer);
    }
}
