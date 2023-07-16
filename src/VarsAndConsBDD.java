import java.util.ArrayList;

public class VarsAndConsBDD
{
    public static int cntC;
    public static int cntCD;
    public static int cntB;
    public static int cntR;

    public static void initiate()
    {
        cntC = Main.leaves_cnt * Main.k;
        cntCD = Main.depth * (Main.leaves_cnt/2);
        cntB = Main.leaves_cnt - 1;
        cntR = 0;
        for(int h=1; h<Main.depth; h++)
        {
            int temp = (int) Math.pow(2, h);
            cntR += (temp-1)*(temp/2);
        }
    }

    public static void updateCnt(int result)
    {
        if(result > Main.var_cnt)
            Main.var_cnt = result;
    }

    public static int varC(int t, int l)
    {
        int result = t * Main.k + l + 1;
        updateCnt(result);
        return result;
    }

    public static int varCD(int h, int t)
    {
        int result = h * (Main.leaves_cnt/2) + t + 1;
        result += cntC;
        updateCnt(result);
        return result;
    }

    public static int varB(int t)
    {
        int result = t + 1;
        result += cntC;
        result += cntCD;
        updateCnt(result);
        return result;
    }

    public static int varR(int t)
    {
        int result = t + 1;
        result += cntC;
        result += cntCD;
        result += cntB;
        updateCnt(result);
        return result;
    }

    public static void addConC(int t)
    {
        for(int l1 = 0; l1 < Main.k; l1++)
            for(int l2 = l1+1; l2 < Main.k; l2++)
                Main.hardClauses.add(new int[]{-varC(t,l1), -varC(t,l2)});
        int[] temp = new int[Main.k];
        for(int l1 = 0; l1 < Main.k; l1++)
            temp[l1] = varC(t,l1);
        Main.hardClauses.add(temp);

        if (t+1 < Main.leaves_cnt)
            addConC(t+1);
    }

    public static void addConFix()
    {
        for(int t=0; t<Main.leaves_cnt; t++)
            if(Solution.leaf_known[t])
                Main.hardClauses.add(new int[]{varC(t,Solution.leaf_labels[t]-1)});
    }

    public static void addConPruneLabels()
    {
        int[] indexList = new int[Main.leaves_cnt/2];
        for(int t=0; t<indexList.length; t++)
            indexList[t] = t;

        int jump = Main.leaves_cnt/2;

        int b_counter = 0;
        int r_counter = 0;

        for(int h=0; h<Main.depth; h++)
        {
            for(int t=0; t<indexList.length; t++)
                for(int l=0; l<Main.k; l++)
                {
                    Main.hardClauses.add(new int[]{-varCD(h, t), -varC(indexList[t],l), -varC(indexList[t]+jump,l)});
                    Main.hardClauses.add(new int[]{varCD(h, t), varC(indexList[t],l), -varC(indexList[t]+jump,l)});
                    Main.hardClauses.add(new int[]{varCD(h, t), -varC(indexList[t],l), varC(indexList[t]+jump,l)});
                }

            //if(Main.minBeads)
            //{
            for(int w=0; w<(int)Math.pow(2,h); w++)
            {
                ArrayList<Integer> temp = new ArrayList<>();

                for(int q=0; q<(int)Math.pow(2,Main.depth-h-1); q++)
                    Main.hardClauses.add(new int[]{-varCD(h,w*((int)Math.pow(2,Main.depth-h-1)) + q), varB(b_counter)});
                temp.add(-varB(b_counter));
                b_counter ++;

                for(int w2=0; w2<w; w2++)
                {
                    for(int q=0; q<(int)Math.pow(2,Main.depth-h); q++)
                        for(int l=0; l<Main.k; l++)
                        {
                            Main.hardClauses.add(new int[]{-varC(w*((int)Math.pow(2,Main.depth-h)) + q,l), varC(w2*((int)Math.pow(2,Main.depth-h)) + q,l), -varR(r_counter)});
                            Main.hardClauses.add(new int[]{varC(w*((int)Math.pow(2,Main.depth-h)) + q,l), -varC(w2*((int)Math.pow(2,Main.depth-h)) + q,l), -varR(r_counter)});
                        }
                    temp.add(varR(r_counter));
                    r_counter ++;
                }

                int[] temp2 = new int[temp.size()];
                for(int p=0; p<temp2.length; p++)
                    temp2[p] = temp.get(p);
                Main.softClauses.add(temp2);
                Main.softClausesWeights.add(1);
            }
            //}



            int[] temp = new int[indexList.length]; // this part (and varCD as a result) can be unnecessary if finding beads are being done as a post-process
            for(int t=0; t<indexList.length; t++)
                temp[t] = varCD(h, t);
            Main.hardClauses.add(temp);

            jump = jump/2;
            if(jump > 0)
                for(int t=0; t<indexList.length; t++)
                    if((t / jump) % 2 == 1)
                        indexList[t] += jump;
        }
    }
}
