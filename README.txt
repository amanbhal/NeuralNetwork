HOW TO RUN THE PROJECT

I have implemented the project in Windows10.
Steps to run:
1.	Unzip the folder and go inside the folder containing the code.
2.	Type the following command to run the project on your windows machine: javac -cp *; *.java
To compile on Linux machine: javac -cp ".:./Jama-1.0.3.jar" *.java
3.	To run the project type the following command: 

java -cp *; Main "<ControlFileName>.txt " "<DataSetFileName>.txt" <NumOFHiddenLayers> <LearningRate>
eg:
java -cp *; Main "balanceControl.txt " "balance.txt" 2 0.25

java -cp *; Main "carControl.txt " "car.txt" 2 0.25

java -cp *; Main "creditscreeningControl.txt " "creditscreening.txt" 2 0.25

java -cp *; Main "irisControl.txt " "iris.txt" 2 0.25

java -cp *; Main "tictactoeControl.txt " "tictactoe.txt" 2 0.25

