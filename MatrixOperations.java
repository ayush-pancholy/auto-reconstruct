package numericalmethods;

public class MatrixOperations
{
    
    public static void main(String[] args)
    {
        double i = 0;
        double delta = 0.01;
        
        while(i <= 1)
        {
            System.out.println(i + "\t" + ((double)1)/Math.sqrt(((double)1)-i*i));
            i += delta;
        }
    }
    
    public static double[][] compareMatrices(double[][] a, double[][] b) throws Exception
    {
        if(a.length != b.length || a[0].length != b[0].length)
        {
            throw new Exception("Error: differently sized matrices");
        }
        
        double[][] output = new double[a.length][a[0].length];
        
        for(int i = 0; i < a.length; i++)
        {
            for(int j = 0; j < a[0].length; j++)
            {
                output[i][j] = (b[i][j]-a[i][j])/(a[i][j]);
            }
        }
        
        return output;
    }
    
    public static double[][] addMatrices(double[][] a, double[][] b) throws Exception
    {
        if(a.length != b.length || a[0].length != b[0].length)
        {
            throw new Exception("Error: matrices not same dimensions");
        }
        
        double[][] output = new double[a.length][a[0].length];
        
        //System.out.println(a.length + " " + a[0].length);
        
        for(int i = 0; i < a.length; i++)
        {
            for(int j = 0; j < a[0].length; j++)
            {
                output[i][j] = a[i][j] + b[i][j];
            }
        }
        
        return output;
    }
    
    public static double[][] matMul(double[][] a, double[][] b) throws Exception
    {
        if(a[0].length != b.length)
        {
            throw new Exception("Error: Cannot multiply these matrices");
        }
        
        double[][] product = new double[a.length][b[0].length];
        
        for(int i = 0; i < product.length; i++)
        {
            for(int j = 0; j < product[0].length; j++)
            {
                for(int k = 0; k < a[0].length; k++)
                {
                    product[i][j] += a[i][k]*b[k][j];
                }
            }
        }
        
        return product;
    }
    
    public static double[] matMulDiagonal(double[][] a, double[][] b) throws Exception
    {
        if(a[0].length != b.length)
        {
            throw new Exception("Error: Cannot multiply these matrices");
        }
        
        double[] output = new double[a.length];
        
        for(int i = 0; i < output.length; i++)
        {
            for(int j = 0; j < a[0].length; j++)
            {
                output[i] += a[i][j]*b[j][i];
            }
        }
        
        return output;
    }
    
    public static double[] matAddRows(double[][] a)
    {
        double[] output = new double[a[0].length];
        for(int i = 0; i < output.length; i++)
        {
            for(int j = 0; j < a.length; j++)
            {
                output[i] += a[j][i];
            }
        }
        
        return output;
    }
    
    public static double[] matAddCols(double[][] a)
    {
        double[] output = new double[a.length];
        for(int i = 0; i < output.length; i++)
        {
            for(int j = 0; j < a[0].length; j++)
            {
                output[i] += a[i][j];
            }
        }
        
        return output;
    }
    
    public static String matToString(double[][] a)
    {
        String output = "";
        
        for(int i = 0; i < a.length; i++)
        {
            for(int j = 0; j < a[0].length; j++)
            {
                output += (a[i][j] + " ");
            }
            output += "\n";
        }
        
        return output;
    }
    
    public static int[] doubleArrToIntArr(double[] array)
    {
        int[] output = new int[array.length];
        for(int i = 0; i < array.length; i++)
        {
            output[i] = (int)array[i];
        }
        return output;
    }
    
    public static String intArrayToString(int[] array)
    {
        String output = "";
        
        for(int i = 0; i < array.length; i++)
        {
            output += (Integer.toString(array[i]));
            if(i < array.length - 1)
            {
                output += ", ";
            }
        }
        
        return output;
    }
}
