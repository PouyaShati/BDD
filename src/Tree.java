public class Tree
{
    public int[] features;
    public double[] thresholds;
    public int[] labels;

    public Tree(int h)
    {
        features = new int[h];
        thresholds = new double[h];
        labels = new int[(int)Math.pow(2, h)];
    }

    public int predict(double[] point)
    {
        int position = 0;
        for(int h=0; h<Main.depth; h++)
        {
            position = position * 2;
            if(point[features[h]] > thresholds[h]) // changed from <=
                position += 1;
        }
        //System.out.println(position);
        return labels[position];
    }

    public int misclassificationCost(double[][] data, int[] labels)
    {
        int cost = 0;
        for(int i=0; i<data.length; i++)
        {
            //System.out.print("" + i + ": ");
            int p = predict(data[i]);
            if(p != labels[i])
            {
                //System.out.println("--" + labels[i] + "--" + p);
                cost++;
            }
        }
        return cost;
    }

    public void print()
    {
        System.out.println("Tree features and thresholds:");
        for(int h=0; h<Main.depth; h++)
            System.out.print(" " + features[h] + ":" + thresholds[h]);
        System.out.println("\nTree labels:");
        for(int t=0; t<labels.length; t++)
            System.out.print(" " + labels[t]);
        System.out.println();
    }
}
