The goal of this assignment is to implement a multi-layer neural network (in any programming language of your choice), and test it on at least 5 datasets from the UCI Machine Learning Repository. Compare the performance of your neural net to your Decision Tree program.

Your program should implement the BackProp algorithm.

You will want to use flags to indicate the number of nodes and hidden layers. The program should be able to handle at least 0, 1, or 2 hidden layers.

You will also have to handle discrete attributes. If a discrete attribute has k values, you can try mapping them to k binary input nodes, or log2(k) nodes using a binary encoding, or 1 node with k different values (numeric assignment). Each approach has different advantages, so you might want to try both and see which works best.

If there are multiple class values, you should use multiple output nodes (i.e. 1 output for each class value). During testing, the output with the highest activation indicates the prediction class label. In the special case of binary classification (2-class problems), you may use 1 output node.

During training, you will have to monitor the error (MSE) on a validation set of examples. You will probably need to manually adjust the learning rate (eta) to get convergence that gradually achieves a minimum error over a reasonable amount of time (though it might take thousands of epochs).

Optionally, you might want to experiment with adding momentum.

Use cross-validation to report accuracies and confidence intervals. (that means testing on a different subset of examples than was used to update the weights or monitor MSE during training)

On a few datasets, experiment with different numbers of hidden layers and hidden units, and report your findings about how many nodes and hidden layers are optimal. Is there a dataset where 2 layers is statistically significantly better than 1? Does having 2 layers slow down training or impede convergence or cause overfit?

When comparing the performance of algorithms, use T-tests. Hints:

randomize the order of examples
normalize continuous attributes
monitor MSE on validation set
evaluate accuracy on independent test set
adjust learning rate to get good convergence in reasonable time
don't forget the bias input to each node
