package numericalmethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.lang.NullPointerException;

public class LinguisticReconstructor
{
    public static final String[] ALPHABET_ORDER = 
        {"U", "O", "I", "E", "A", "M", "F", "PH", "V", "P", "B", "TH", "N", "L", "S", "Z", "T", "D", "R","SH", "CH", "J",
                "Y", "NG", "W", "K", "C", "Q", "G", "H"};
    public static final String[] LANGUAGES =
        {"English", "German", "Latin", "Old Norse"};
    public static final int MAX_WORD_LENGTH = 10;
    
    public static final int MAX_ITERATIONS = 1000;
    public static final double DERIVATIVE_STEP = 1e-7;
    public static final double ERROR_TOLERANCE = 100;
    public static final double LEARNING_INCREASE = 1.5;
    public static final double LEARNING_DECREASE = 0.5;
    
    public double learningRate;
    
    public HashMap<String, Integer> alphabetPairings;
    
    double[][] weights;
    double[][] biases;
    
    double[][] weights2;
    double[][] biases2;
    
    public ArrayList<TrainingWord> data;
    
    public LinguisticReconstructor()
    {
        alphabetPairings = new HashMap<String, Integer>();
        data = new ArrayList<TrainingWord>();
        
        weights = new double[MAX_WORD_LENGTH][LANGUAGES.length];
        weights2 = new double[MAX_WORD_LENGTH][LANGUAGES.length];
        biases = new double[LANGUAGES.length][MAX_WORD_LENGTH];
        biases2 = new double[LANGUAGES.length][MAX_WORD_LENGTH];
        
        for(int i = 0; i < weights.length; i++)
        {
            for(int j = 0; j < weights[0].length; j++)
            {
                weights[i][j] = Math.random();
                biases[j][i] = Math.random();
                weights[i][j] = Math.random();
                biases[j][i] = Math.random();
            }
        }

        alphabetPairings.put(null, new Integer(0));
        for(int i = 0; i < ALPHABET_ORDER.length; i++)
        {
            alphabetPairings.put(ALPHABET_ORDER[i], new Integer(i+1));
        }
        
        learningRate = 0.1;
    }
    
    public void addWord(TrainingWord word)
    {
        data.add(word);
    }
    
    public void clearWords()
    {
        data.clear();
    }
    
    public void addWord(String pie, ArrayList<String> languages) throws Exception
    {
        TrainingWord word = new TrainingWord(wordToVector(pie), MAX_WORD_LENGTH);
        
        for(int i = 0; i < LANGUAGES.length; i++)
        {
            word.addDescendant(LANGUAGES[i], wordToVector(languages.get(i)));
        }
        
        addWord(word);
    }
     
    public String useModel(double[][] languageMatrix) throws Exception
    {
        double[][] sum1 = MatrixOperations.addMatrices(biases, languageMatrix);
        double[] product1 = MatrixOperations.matMulDiagonal(weights, sum1);
        
        double[][] sum2 = MatrixOperations.addMatrices(biases2, languageMatrix);
        double[] product2 = MatrixOperations.matMulDiagonal(weights2, sum2);
        
        double[] output = new double[product1.length];
        //output[0] = product1[0] + product2[1];
        //output[output.length - 1] = product1[output.length - 1] + product2[output.length - 2];
        output[0] = product1[0];
        output[output.length-1] = product1[output.length-1];
        
        for(int i = 1; i < output.length - 1; i++)
        {
            output[i] = product1[i] + product2[i - 1] + product2[i + 1];
        }
        
        int[] word = new int[output.length];
        for(int i = 0; i < output.length; i++)
        {
            word[i] = (int)(output[i] + 0.5);
        }
        
        return intArrayToWord(word);
    }
    
    
    public static void main(String[] args) throws Exception
    {
        LinguisticReconstructor reconstructor = new LinguisticReconstructor();
        
        //English, Old Norse, German, Latin
        reconstructor.addWord("manus", new ArrayList<String>(Arrays.asList("man", "mathr", "man", "mas")));
        reconstructor.addWord("phter", new ArrayList<String>(Arrays.asList("father", "fathir", "vater", "pater")));
        reconstructor.addWord("hnomn", new ArrayList<String>(Arrays.asList("name", "nafn", "name", "nomen")));
        reconstructor.addWord("wihros", new ArrayList<String>(Arrays.asList("wer", "verr", "wer", "vir")));
        reconstructor.addWord("mehter", new ArrayList<String>(Arrays.asList("mother", "mothir", "mutter", "mater")));
        reconstructor.addWord("brehter", new ArrayList<String>(Arrays.asList("brother", "brothir", "bruder", "frater")));
        reconstructor.addWord("swesor", new ArrayList<String>(Arrays.asList("sister", "systir", "schwester", "soror")));
        //reconstructor.addWord("genus", new ArrayList<String>(Arrays.asList("chin", "kinn", "kinn", "gena")));
        //reconstructor.addWord("ghostis", new ArrayList<String>(Arrays.asList("guest", "gestr", "gast", "hostis")));
        //reconstructor.addWord("kaput", new ArrayList<String>(Arrays.asList("head", "haufud", "houbit", "caput")));

        reconstructor.minimizeError();
        /**
        LinguisticReconstructor reconstructor2 = new LinguisticReconstructor();

        String pie = "nehs";
        String eng = "nose";
        String norse = "nos";
        String ger = "nasa";
        String lat = "nasus";
        
        reconstructor2.addWord(pie, new ArrayList<String>(Arrays.asList(eng, norse, ger, lat)));
        double initialError = reconstructor2.getError();
        
        //double originalError = reconstructor.getError();
        reconstructor.minimizeError();
        
        reconstructor.clearWords();
        
        reconstructor.addWord(pie, new ArrayList<String>(Arrays.asList(eng, norse, ger, lat)));
        
        double finalError = reconstructor.getError();
        
        System.out.println("Initial Error: " + initialError);
        System.out.println("Final Error: " + finalError);
        System.out.println("Change in Error: -" + (initialError - finalError) * 100/initialError + "%");
        /**
        System.out.println("Initial Error: " + originalError);
        System.out.println("Final Error: " + reconstructor.getError());
        System.out.println("Change in Error: -" + (originalError - reconstructor.getError())*100/originalError + "%");
        */
        System.out.print("\n");
        
        for(int i = 0; i < reconstructor.data.size(); i++)
        {
            System.out.println(reconstructor.useModel(reconstructor.data.get(i).getLanguageMatrixDouble()));
        }       
        
        //reconstructor.printWeights();
        //reconstructor.printBiases();
        //reconstructor.printSecondaryWeights();
        //reconstructor.printSecondaryBiases();
    }
    
    public int[] wordToVector(String input) throws Exception
    {
        if(input == null)
        {
            throw new NullPointerException("Error: Null Input String");
        }
        if(input.length() > MAX_WORD_LENGTH)
        {
            throw new Exception("Word Length Exception: Word Too Long");
        }
        input = input.toUpperCase();
        
        int currentLocation = 1;
        int soundsAdded = 0;
        String currentChar = "";
        String nextChar = "";
        boolean hasNextChar = true;
        boolean specialCombination = false;
        int[] vector = new int[MAX_WORD_LENGTH];
        
        while (currentLocation <= input.length())
        {
            currentChar = input.substring(currentLocation - 1, currentLocation);
            
            if(currentLocation == input.length())
            {
                hasNextChar = false;
            }
            else
            {
                hasNextChar = true;
                nextChar = input.substring(currentLocation, currentLocation + 1);
            }
            
            if(hasNextChar)
            {
                if(currentChar.equals("N"))
                {
                    if(nextChar.equals("G"))
                    {
                        vector[soundsAdded] = alphabetPairings.get("NG");
                        specialCombination = true;
                    }
                }
                else if(currentChar.equals("T"))
                {
                    if(nextChar.equals("H"))
                    {
                        vector[soundsAdded] = alphabetPairings.get("TH");
                        specialCombination = true;
                    }
                }
                else if(currentChar.equals("C"))
                {
                    if(nextChar.equals("H"))
                    {
                        vector[soundsAdded] = alphabetPairings.get("CH");
                        specialCombination = true;
                    }
                }
                else if (currentChar.equals("S"))
                {
                    if(nextChar.equals("H"))
                    {
                        vector[soundsAdded] = alphabetPairings.get("SH");
                        specialCombination = true;
                    }
                }
                else if (currentChar.equals("P"))
                {
                    if(nextChar.equals("H"))
                    {
                        vector[soundsAdded] = alphabetPairings.get("PH");
                    }
                }
            }
            
            if(specialCombination)
            {
                currentLocation++;
            }
            
            if(!specialCombination)
            {
                vector[soundsAdded] = alphabetPairings.get(currentChar);
            }

            specialCombination = false;
            currentLocation++;
            soundsAdded++;
        }
        
        return vector;
    }
    
    public double[] getReconstruction(int index) throws Exception
    {
        TrainingWord word = data.get(index);
        double[][] languageMatrix = word.getLanguageMatrixDouble();
        
        double[][] sum1 = MatrixOperations.addMatrices(biases, languageMatrix);
        double[] product1 = MatrixOperations.matMulDiagonal(weights, sum1);
        
        double[][] sum2 = MatrixOperations.addMatrices(biases2, languageMatrix);
        double[] product2 = MatrixOperations.matMulDiagonal(weights2, sum2);
        
        double[] output = new double[product1.length];
        //output[0] = product1[0] + product2[1];
        //output[output.length - 1] = product1[output.length - 1] + product2[output.length - 2];
        output[0] = product1[0];
        output[output.length-1] = product1[output.length-1];
        
        for(int i = 1; i < output.length - 1; i++)
        {
            output[i] = product1[i] + product2[i - 1] + product2[i + 1];
        }
        
        return output;
        //double[][] temp = MatrixOperations.addMatrices(word.getLanguageMatrixDouble(), biases);
        //return MatrixOperations.matMulDiagonal(weights, temp);
    }
    
    public double getError() throws Exception
    {
        double error = 0;
        
        double[] program;
        int[] original;
        
        for(int i = 0; i < data.size(); i++)
        {
            program = getReconstruction(i);
            original = data.get(i).getOriginal();
            
            for(int j = 0; j < program.length; j++)
            {
                error += (program[j]-(double)original[j])*(program[j]-(double)original[j]);
            }
        }
        
        return error;
    }
    
    public double getWeightErrorPartial(int row, int col) throws Exception
    {
        double initialValue = weights[row][col];
        double derivative = 0;
        
        weights[row][col] = initialValue + 2*DERIVATIVE_STEP;
        derivative -= getError();
        weights[row][col] = initialValue + DERIVATIVE_STEP;
        derivative += 8*getError();
        weights[row][col] = initialValue - DERIVATIVE_STEP;
        derivative -= 8*getError();
        weights[row][col] = initialValue - 2*DERIVATIVE_STEP;
        derivative += getError();
        
        weights[row][col] = initialValue;
        
        derivative /= (12*DERIVATIVE_STEP);
        
        return derivative;
    }
    
    public double getSecondaryWeightErrorPartial(int row, int col) throws Exception
    {
        double initialValue = weights2[row][col];
        double derivative = 0;
        
        weights2[row][col] = initialValue + 2*DERIVATIVE_STEP;
        derivative -= getError();
        weights2[row][col] = initialValue + DERIVATIVE_STEP;
        derivative += 8*getError();
        weights2[row][col] = initialValue - DERIVATIVE_STEP;
        derivative -= 8*getError();
        weights2[row][col] = initialValue - 2*DERIVATIVE_STEP;
        derivative += getError();
        
        weights2[row][col] = initialValue;
        
        derivative /= (12*DERIVATIVE_STEP);
        
        return derivative;
    }
    
    public double getBiasErrorPartial(int row, int col) throws Exception
    {
        double initialValue = biases[row][col];
        double derivative = 0;
        
        biases[row][col] = initialValue + 2*DERIVATIVE_STEP;
        derivative -= getError();
        biases[row][col] = initialValue + DERIVATIVE_STEP;
        derivative += 8*getError();
        biases[row][col] = initialValue - DERIVATIVE_STEP;
        derivative -= 8*getError();
        biases[row][col] = initialValue - 2*DERIVATIVE_STEP;
        derivative += getError();
        
        biases[row][col] = initialValue;
        
        derivative /= (12*DERIVATIVE_STEP);
        
        return derivative;
    }
    
    public double getSecondaryBiasErrorPartial(int row, int col) throws Exception
    {
        double initialValue = biases2[row][col];
        double derivative = 0;
        
        biases2[row][col] = initialValue + 2*DERIVATIVE_STEP;
        derivative -= getError();
        biases2[row][col] = initialValue + DERIVATIVE_STEP;
        derivative += 8*getError();
        biases2[row][col] = initialValue - DERIVATIVE_STEP;
        derivative -= 8*getError();
        biases2[row][col] = initialValue - 2*DERIVATIVE_STEP;
        derivative += getError();
        
        biases2[row][col] = initialValue;
        
        derivative /= (12*DERIVATIVE_STEP);
        
        return derivative;
    }
    
    public void minimizeError() throws Exception
    {
        System.out.println("Training on input data to minimize error...");
        int iterator = 0;
        
        double[][] previousWeights = new double[weights.length][weights[0].length];
        double[][] weightSteps = new double[weights.length][weights[0].length];
        
        double[][] previousBiases = new double[biases.length][biases[0].length];
        double[][] biasSteps = new double[biases.length][biases[0].length];
        
        double[][] previousSecondaryWeights = new double[weights2.length][weights2[0].length];
        double[][] secondaryWeightSteps = new double[weights2.length][weights2[0].length];
        
        double[][] previousSecondaryBiases = new double[biases2.length][biases2[0].length];
        double[][] secondaryBiasSteps = new double[biases2.length][biases2[0].length];
        
        double previousError;
        
        while(iterator < MAX_ITERATIONS && getError() > ERROR_TOLERANCE)
        {
            previousError = getError();
            
            for(int i = 0; i < weights.length; i++)
            {
                for(int j = 0; j < weights[0].length; j++)
                {
                    previousWeights[i][j] = weights[i][j];
                    weightSteps[i][j] = getWeightErrorPartial(i,j)*learningRate;
                    
                    previousSecondaryWeights[i][j] = weights2[i][j];
                    secondaryWeightSteps[i][j] = getSecondaryWeightErrorPartial(i,j)*learningRate;
                }
            }
            
            for(int p = 0; p < biases.length; p++)
            {
                for(int q = 0; q < biases[0].length; q++)
                {
                    previousBiases[p][q] = biases[p][q];
                    biasSteps[p][q] = getBiasErrorPartial(p,q)*learningRate;
                    
                    previousSecondaryBiases[p][q] = biases2[p][q];
                    secondaryBiasSteps[p][q] = getSecondaryBiasErrorPartial(p,q)*learningRate;
                }
            }
            
            for(int k = 0; k < weights.length; k++)
            {
                for(int l = 0; l < weights[0].length; l++)
                {
                    weights[k][l] -= weightSteps[k][l];
                    
                    weights2[k][l] -= secondaryWeightSteps[k][l];
                }
            }
            
            for(int r = 0; r < biases.length; r++)
            {
                for(int s = 0; s < biases[0].length; s++)
                {
                    biases[r][s] -= biasSteps[r][s];
                    
                    biases2[r][s] -= secondaryBiasSteps[r][s];
                }
            }
            
            if(getError() > previousError)
            {
                for(int m = 0; m < weights.length; m++)
                {
                    for(int n = 0; n < weights[0].length; n++)
                    {
                        weights[m][n] = previousWeights[m][n];
                        
                        weights2[m][n] = previousSecondaryWeights[m][n];
                    }
                }
                
                for(int t = 0; t < biases.length; t++)
                {
                    for(int u = 0; u < biases[0].length; u++)
                    {
                        biases[t][u] = previousBiases[t][u];
                        
                        biases2[t][u] = previousSecondaryBiases[t][u];
                    }
                }
                
                learningRate *= LEARNING_DECREASE;
            }
            else
            {
                learningRate *= LEARNING_INCREASE;
            }
            
            if(iterator % (MAX_ITERATIONS/20) == 0)
            {
                System.out.print("|");
            }
            iterator++;
        }
        
        System.out.println("\n");
    }
    
    public String intArrayToWord(int[] array)
    {
        String sound;
        String output = "";
        for(int i = 0; i < array.length; i++)
        {
            if(array[i] == 0)
            {
                break;
            }
            
            if(array[i] - 1 >= ALPHABET_ORDER.length)
            {
                sound = ALPHABET_ORDER[ALPHABET_ORDER.length - 1];
            }
            else if(array[i] - 1 < 0)
            {
                sound = ALPHABET_ORDER[0];
            }
            else
            {
                sound = ALPHABET_ORDER[array[i] - 1];
            }
            
            output += sound;
        }
        
        return output;
    }
    
    public void printWeights()
    {
        System.out.println(MatrixOperations.matToString(weights));
    }
    
    public void printSecondaryWeights()
    {
        System.out.println(MatrixOperations.matToString(weights2));
    }
    
    public void printBiases()
    {
        System.out.println(MatrixOperations.matToString(biases));
    }
    
    public void printSecondaryBiases()
    {
        System.out.println(MatrixOperations.matToString(biases2));
    }
}