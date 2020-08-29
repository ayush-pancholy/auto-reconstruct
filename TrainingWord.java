package numericalmethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class TrainingWord
{
    public HashMap<String, int[]> derivedWords;
    public int[] reconstruction;
    public ArrayList<String> languages;
    public int wordSize;
    
    public TrainingWord(int[] input, int wordSizeInput)
    {
        derivedWords = new HashMap<String, int[]>();
        reconstruction = input;
        languages = new ArrayList<String>();
        wordSize = wordSizeInput;
    }
    
    public void addDescendant(String language, int[] word) throws Exception
    {
        if(word.length != wordSize)
        {
            throw new Exception("Error: Word does not match specified size");
        }
        derivedWords.put(language, word);
        languages.add(language);
        Collections.sort(languages);
    }
    
    public int[] getOriginal()
    {
        return reconstruction;
    }
    
    public int[] getDescendant(String language)
    {
        return derivedWords.get(language);
    }
    
    public int[][] getLanguageMatrixInt()
    {
        int[][] output = new int[languages.size()][wordSize];
        int[] current;
        
        for(int i = 0; i < languages.size(); i++)
        {
            current = getDescendant(languages.get(i));
            for(int j = 0; j < wordSize; j++)
            {
                output[i][j] = current[j];
            }
        }
        
        return output;
    }
    
    public double[][] getLanguageMatrixDouble()
    {
        double[][] output = new double[languages.size()][wordSize];
        int[] current;
        
        for(int i = 0; i < languages.size(); i++)
        {
            current = getDescendant(languages.get(i));
            for(int j = 0; j < wordSize; j++)
            {
                output[i][j] = (double)current[j];
            }
        }
        
        return output;
    }
    
    public static void main(String[] args) throws Exception
    {
        int[] temp = new int[10];
        temp[0] = 1;
        
        int[] newtemp = new int[10];
        newtemp[5] = 3;
        
        TrainingWord word = new TrainingWord(temp, 10);
        word.addDescendant("Latin", temp);
        word.addDescendant("English", newtemp);
        int[][] mat = word.getLanguageMatrixInt();
        
        for(int i = 0; i < mat.length; i++)
        {
            for(int j = 0; j < mat[0].length; j++)
            {
                System.out.print(mat[i][j] + " ");
            }
            System.out.print("\n");
        }
    }
}
