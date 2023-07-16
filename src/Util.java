import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Util
{
    public static void readInstance(String name) throws Exception
    {

        File instance = new File("data/instance_" + name);
        Scanner scanner = new Scanner(instance);

        String flags = scanner.nextLine();

        Main.n = scanner.nextInt();
        Main.f = scanner.nextInt();

        Main.anyCategorical = false;

        if(flags.contains("c"))
        {
            Main.categorical = new boolean[Main.f];
            for(int j=0; j<Main.f; j++)
                if(scanner.nextInt() == 1)
                {
                    Main.categorical[j] = true;
                    Main.anyCategorical = true;
                }
                else
                    Main.categorical[j] = false;
        }else
        {
            Main.categorical = new boolean[Main.f];
        }

        double[] maxValues = new double[Main.f];
        double[] minValues = new double[Main.f];


        boolean labelsEnd = false;
        boolean labelsBeg = false;

        if(flags.contains(" le"))
            labelsEnd = true;
        else if(flags.contains(" lb"))
            labelsBeg = true;
        else if(flags.contains(" l"))
            labelsEnd = true;

        Main.k = 0;

        if(labelsBeg || labelsEnd)
        {
            Main.labels = new int[Main.n];
            Main.k = scanner.nextInt();
        }

        Main.data = new double[Main.n][];

        for(int i=0; i<Main.n; i++)
        {
            double[] new_entry = new double[Main.f];

            if(labelsBeg)
                Main.labels[i] = scanner.nextInt();

            for(int j=0; j<Main.f; j++)
            {
                new_entry[j] = scanner.nextDouble();
                if(i == 0)
                {
                    maxValues[j] = new_entry[j];
                    minValues[j] = new_entry[j];
                }else
                {
                    if(new_entry[j] > maxValues[j])
                        maxValues[j] = new_entry[j];
                    if(new_entry[j] < minValues[j])
                        minValues[j] = new_entry[j];
                }
            }

            if(labelsEnd)
                Main.labels[i] = scanner.nextInt();

            Main.data[i] = new_entry;
        }

        if(Main.normalize)
        {
            for(int i=0; i<Main.n; i++)
                for(int j=0; j<Main.f; j++)
                    if(maxValues[j] > minValues[j])
                        Main.data[i][j] = (Main.data[i][j] - minValues[j]) / (maxValues[j] - minValues[j]) * 100;
        }

    }

    public static void divideDataset() throws Exception
    {
        Random random = new Random(Main.seed);
        ArrayList<Integer> pool = new ArrayList<>();
        for(int i=0; i<Main.n; i++)
            pool.add(i);
        int[] chunkSizes;
        if(Main.testRatio > 0.0)
        {
            int temp = (int) (Main.testRatio * Main.n);
            chunkSizes = new int[]{temp, Main.n-temp};
        }
        else if(Main.test5Fold >= 0)
        {
            chunkSizes = new int[5];
            int temp = Main.n;
            for(int t=0; t<4; t++)
            {
                chunkSizes[t] = temp/(5-t);
                temp -= chunkSizes[t];
            }
            chunkSizes[4] = temp;

            System.out.println("Chunk sizes:");
            for(int t=0; t<5; t++)
                System.out.print("" + chunkSizes[t] + " ");
            System.out.println();
        }else
            chunkSizes = null;

        //System.out.println(new_n);

        FileWriter fileWriter = new FileWriter("data/" + chunkSizes.length + "fold_" + Main.name);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        ArrayList<Integer>[] chosen = new ArrayList[chunkSizes.length];
        for(int c=0; c<chosen.length; c++)
        {
            chosen[c] = new ArrayList<>();
            for(int i=0; i<chunkSizes[c]; i++)
            {
                int temp = random.nextInt(pool.size());
                chosen[c].add(pool.get(temp));
                printWriter.write("" + pool.get(temp) + "\n");
                pool.remove(temp);
            }
            printWriter.write("*");
            if(c < chosen.length-1)
                printWriter.write("\n");
        }
        printWriter.close();

        if(Main.testRatio > 0.0)
        {
            double[][] trainData = new double[chosen[1].size()][];
            double[][] testData = new double[chosen[0].size()][];
            int[] trainLabels = new int[chosen[1].size()];
            int[] testLabels = new int[chosen[0].size()];

            for(int i=0; i<chosen[1].size(); i++)
            {
                trainData[i] = Main.data[chosen[1].get(i)].clone();
                trainLabels[i] = Main.labels[chosen[1].get(i)];
            }
            for(int i=0; i<chosen[0].size(); i++)
            {
                testData[i] = Main.data[chosen[0].get(i)].clone();
                testLabels[i] = Main.labels[chosen[0].get(i)];
            }

            Main.data = trainData;
            Main.labels = trainLabels;
            Main.testData = testData;
            Main.testLabels = testLabels;
            Main.n = chosen[1].size();
        }else if(Main.test5Fold >= 0)
        {
            int sum_size=0;
            for(int c=0; c<chunkSizes.length; c++)
                sum_size += chunkSizes[c];
            sum_size -= chunkSizes[Main.test5Fold];
            double[][] trainData = new double[sum_size][];
            double[][] testData = new double[chunkSizes[Main.test5Fold]][];
            int[] trainLabels = new int[sum_size];
            int[] testLabels = new int[chunkSizes[Main.test5Fold]];

            int test_cnt=0;
            int train_cnt=0;

            for(int c=0; c<chunkSizes.length; c++)
            {
                for(int i=0; i<chosen[c].size(); i++)
                {
                    if(c == Main.test5Fold)
                    {
                        testData[test_cnt] = Main.data[chosen[c].get(i)].clone();
                        testLabels[test_cnt] = Main.labels[chosen[c].get(i)];
                        test_cnt++;
                    }else
                    {
                        trainData[train_cnt] = Main.data[chosen[c].get(i)].clone();
                        trainLabels[train_cnt] = Main.labels[chosen[c].get(i)];
                        train_cnt++;
                    }
                }
            }

            Main.data = trainData;
            Main.labels = trainLabels;
            Main.testData = testData;
            Main.testLabels = testLabels;
            Main.n = sum_size;
        }


        //System.out.println(Main.n);
        //System.out.println(trainData.length);
        //System.out.println(testData.length);
    }

    public static void calculateSorts()
    {
        Main.sorted = new int[Main.data.length][];
        for(int i=0; i<Main.n; i++)
        {
            Main.sorted[i] = new int[Main.data[i].length];
            for(int j=0; j<Main.sorted[i].length; j++)
                Main.sorted[i][j] = i;
        }

        for(int j=0; j<Main.f; j++)
            Util.sort(Main.data, j, Main.sorted, 0, Main.n-1);

        Main.sortedInverse = new int[Main.data.length][];
        for(int i=0; i<Main.n; i++)
            Main.sortedInverse[i] = new int[Main.data[i].length];

        for(int j=0; j<Main.f; j++)
            for(int i=0; i<Main.n; i++)
                Main.sortedInverse[Main.sorted[i][j]][j] = i;
    }

    public static void sort(double[][] data, int feature, int[][] sorted, int start, int end)
    {
        if(start >= end)
            return;

        sort(data, feature, sorted, start, (start+end)/2);
        sort(data, feature, sorted, (start+end)/2+1, end);

        int[] temp = new int[end-start+1];

        int i1 = start;
        int i2 = (start+end)/2+1;
        for(int i=0; i<end-start+1; i++)
            if(i1 <= (start+end)/2 && i2 <= end)
            {
                if(data[sorted[i1][feature]][feature] <  data[sorted[i2][feature]][feature])
                {
                    temp[i] = sorted[i1][feature];
                    i1++;
                }else
                {
                    temp[i] = sorted[i2][feature];
                    i2++;
                }
            }else if(i1 <= (start+end)/2)
            {
                temp[i] = sorted[i1][feature];
                i1++;
            }
            else if(i2 <= end)
            {
                temp[i] = sorted[i2][feature];
                i2++;
            }

        for(int i=0; i<end-start+1; i++)
            sorted[i+start][feature] = temp[i];
    }

    public static String getAssignment(String[] input)
    {
        String result = "";

        for(int i=0; i<input.length; i++)
            if(input[i].charAt(0) == '-')
                result = result + "0";
            else
                result = result + "1";

        return result;
    }


}
