/**
 * Created by amanb on 3/21/2016.
 */
import Jama.Matrix;

import java.io.*;
import java.util.*;
public class NeuralNetwork implements Serializable {
    private static final long serialVersionUID = 4526472615622109247L;
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

    public NeuralNetwork copy() throws Exception{
        Object obj = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(this);
        out.flush();
        out.close();

        ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
        obj = in.readObject();

        return (NeuralNetwork)obj;

    }
}
