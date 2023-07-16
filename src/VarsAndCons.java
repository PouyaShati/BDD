import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class VarsAndCons
{
    public static int cntA;
    public static int cntA2;
    public static int cntC;
    public static int cntD;
    public static int cntCD;
    public static int cntS;
    public static int cntO;
    public static int cntB;
    public static int cntCE;
    public static int cntR;
    public static int cntLD;
    public static int cntLL;
    public static int cntZ;
    public static int cntUB;
    public static void initiate()
    {
        if(Main.postBBDPhase)
        {
            cntA = 0;
            cntA2 = 0;
            cntD = 0;
            cntS = 0;
            cntO = 0;
            cntCE = 0;

            cntC = Main.leaves_cnt * Main.k;
            cntCD = Main.leaves_cnt_log * (Main.leaves_cnt/2);
            cntB = Main.leaves_cnt - 1;
            cntR = 0;
            for(int h=1; h<Main.leaves_cnt_log; h++)
            {
                int temp = (int) Math.pow(2, h);
                cntR += (temp-1)*(temp/2);
            }
            cntLD = 0;
            cntLL = 0;
            cntZ = 0;
            cntUB = 0;
        }else
        {
            if (Main.orderedSelection && Main.featureToDirection)
                cntA = 0;
            else
                cntA = Main.depth * Main.f;

            if (Main.orderedSelection)
                cntA2 = Main.depth * Main.f;
            else
                cntA2 = 0;

            if(Main.fixLabels)
                cntC = 0;
            else
                cntC = Main.leaves_cnt * Main.k;

            if (Main.modelDirection)
                cntD = Main.depth * Main.n;
            else
                cntD = 0;

            if (Main.diagram)
                cntD += (Main.depth * (Main.depth + 2) / 8) * Main.n;

            if (Main.pruneLabels || Main.maxRedundantNode || Main.minBDD)
            {
                cntCD = Main.leaves_cnt_log * (Main.leaves_cnt / 2);
            }
            else
                cntCD = 0;

            if(Main.minDepthObj)
                cntO = 0;
            else
                cntO = Main.n;

            if (Main.diagram)
            {
                cntS = 0;
                for (int d = 0; d < Main.depth; d += 2)
                    cntS += (d / 2 + 1) * ((int) Math.pow(2, d / 2 + 2));
            } else
                cntS = 0;

            if (Main.maxRedundantNode || Main.minBDD)
                cntB = Main.leaves_cnt - 1;
            else
                cntB = 0;

            if (Main.maxEmpties)
            {
                if(Main.pruneLabels || Main.maxRedundantNode || Main.minBDD)
                    cntCE = Main.leaves_cnt * Main.k;
                else
                    cntCE = Main.leaves_cnt;
            }
            else
                cntCE = 0;

            cntR = 0;
            if(Main.minBDD)
            {
                for(int h=1; h<Main.leaves_cnt_log; h++)
                {
                    int temp = (int) Math.pow(2, h);
                    cntR += (temp-1)*(temp/2);
                }
            }

            if(Main.multDimFeatures)
                cntLD = Main.featureDims.length * Main.n;
            else
                cntLD = 0;

            cntLL = 0;
            if(Main.multDimFeatures)
            {
                for(int h=0; h<Main.featureDims.length; h++)
                    cntLL += (int) Math.pow(2, Main.featureDims[h]);
            }

            if(Main.modelPresence)
                cntZ = Main.leaves_cnt * Main.n;
            else
                cntZ = 0;

            if(Main.beadCard)
                cntUB = Main.leaves_cnt - 1;
            else
                cntUB = 0;

        }

        System.out.println("Var counts:");
        System.out.print("A:" + cntA + " ");
        System.out.print("A2:" + cntA2 + " ");
        System.out.print("C:" + cntC + " ");
        System.out.print("D:" + cntD + " ");
        System.out.print("CD:" + cntCD + " ");
        System.out.print("S:" + cntS + " ");
        System.out.print("O:" + cntO + " ");
        System.out.print("B:" + cntB + " ");
        System.out.print("CE:" + cntCE + " ");
        System.out.print("R:" + cntR + " ");
        System.out.print("LD:" + cntLD + " ");
        System.out.print("LL:" + cntLL + " ");
        System.out.print("Z:" + cntZ + " ");
        System.out.print("UB:" + cntUB + " ");
        System.out.println();
    }

    public static void updateCnt(int result)
    {
        if(result > Main.var_cnt)
            Main.var_cnt = result;
    }
    public static int varA(int h, int j)
    {
        int result = h*Main.f + j + 1;
        updateCnt(result);
        return result;
    }

    public static int varA2(int h, int j)
    {
        int result = h*Main.f + j + 1;
        result += cntA;
        updateCnt(result);
        return result;
    }

    public static int varC(int t, int l)
    {
        int result = t * Main.k + l + 1;
        result += cntA;
        result += cntA2;
        updateCnt(result);
        return result;
    }

    public static int varD(int h, int i)
    {
        int result;
        result = h * Main.n + i + 1; // this should be the same for with and without diagram (?)
        result += cntA;
        result += cntA2;
        result += cntC;
        updateCnt(result);
        return result;
    }

    public static int varCD(int h, int t)
    {
        int result = h * (Main.leaves_cnt/2) + t + 1;
        result += cntA;
        result += cntA2;
        result += cntC;
        result += cntD;
        updateCnt(result);
        return result;
    }

    public static int varO(int i)
    {
        int result = i + 1;
        result += cntA;
        result += cntA2;
        result += cntC;
        result += cntD;
        result += cntCD;
        updateCnt(result);
        return result;
    }

    public static int varS(int t)
    {
        int result = t + 1;
        result += cntA;
        result += cntA2;
        result += cntC;
        result += cntD;
        result += cntCD;
        result += cntO;
        updateCnt(result);
        return result;
    }

    public static int varB(int t)
    {
        int result = t + 1;
        result += cntA;
        result += cntA2;
        result += cntC;
        result += cntD;
        result += cntCD;
        result += cntO;
        result += cntS;
        updateCnt(result);
        return result;
    }

    public static int varCE(int t, int l)
    {
        int result;
        if(Main.maxRedundantNode || Main.pruneLabels || Main.minBDD)
            result = t * Main.k + l + 1;
        else
            result = t + 1;
        result += cntA;
        result += cntA2;
        result += cntC;
        result += cntD;
        result += cntCD;
        result += cntO;
        result += cntS;
        result += cntB;
        updateCnt(result);
        return result;
    }

    public static int varR(int t)
    {
        int result = t + 1;
        result += cntA;
        result += cntA2;
        result += cntC;
        result += cntD;
        result += cntCD;
        result += cntO;
        result += cntS;
        result += cntB;
        result += cntCE;
        updateCnt(result);
        return result;
    }

    public static int varLD(int h, int i)
    {
        int result = h * Main.n + i + 1;
        result += cntA;
        result += cntA2;
        result += cntC;
        result += cntD;
        result += cntCD;
        result += cntO;
        result += cntS;
        result += cntB;
        result += cntCE;
        result += cntR;
        updateCnt(result);
        return result;
    }

    public static int varLL(int h, int t)
    {
        int result = t + 1;
        for(int h2=0; h2<h; h2++)
            result += (int) Math.pow(2, Main.featureDims[h2]);
        result += cntA;
        result += cntA2;
        result += cntC;
        result += cntD;
        result += cntCD;
        result += cntO;
        result += cntS;
        result += cntB;
        result += cntCE;
        result += cntR;
        result += cntLD;
        updateCnt(result);
        return result;
    }

    public static int varZ(int t, int i)
    {
        int result = t*Main.n + i + 1;
        result += cntA;
        result += cntA2;
        result += cntC;
        result += cntD;
        result += cntCD;
        result += cntO;
        result += cntS;
        result += cntB;
        result += cntCE;
        result += cntR;
        result += cntLD;
        result += cntLL;
        updateCnt(result);
        return result;
    }

    public static int varUB(int t)
    {
        int result = t + 1;
        result += cntA;
        result += cntA2;
        result += cntC;
        result += cntD;
        result += cntCD;
        result += cntO;
        result += cntS;
        result += cntB;
        result += cntCE;
        result += cntR;
        result += cntLD;
        result += cntLL;
        result += cntZ;
        updateCnt(result);
        return result;
    } // be careful of the cardinality constraint generation, when adding new variables

    public static void addConDims()
    {
        ArrayList<Integer> literals = new ArrayList<>();

        for(int i=0; i<Main.n; i++)
        {
            for(int h=0; h<Main.featureDims.length; h++)
                for(int t=0; t<(int) Math.pow(2, Main.featureDims[h]); t++)
                {
                    literals.clear();
                    int pt = t;
                    int org_h=0;
                    for(int h2=0; h2<=h; h2++)
                        org_h += Main.featureDims[h2];
                    for(int hd=Main.featureDims[h]-1; hd>=0; hd--)
                    {
                        org_h--;
                        int dir = pt % 2;
                        if(dir == 1)
                            literals.add(varD(org_h, i)); // changed the negation of this
                        else
                            literals.add(-varD(org_h, i)); // and this
                        pt = pt / 2;
                    }
                    int[] temp1 = new int[literals.size()+2];
                    int[] temp2 = new int[literals.size()+2];
                    for(int l=0; l<literals.size(); l++)
                    {
                        temp1[l+2] = literals.get(l);
                        temp2[l+2] = literals.get(l);
                    }
                    temp1[0] = varLD(h, i);
                    temp1[1] = -varLL(h, t);

                    temp2[0] = -varLD(h, i);
                    temp2[1] = varLL(h, t);

                    Main.hardClauses.add(temp1);
                    Main.hardClauses.add(temp2);
                }
        }

        // if(Main.forceSplit) this part has nothing to do with forcing split (?)
            for(int h=0; h<Main.featureDims.length; h++)
                Main.hardClauses.add(new int[]{varLL(h, 0)});
        if(Main.forceSplit)
            for(int h=0; h<Main.featureDims.length; h++)
            {
                int[] temp = new int[(int) Math.pow(2, Main.featureDims[h])];
                for(int t=0; t<temp.length; t++)
                    temp[t] = - varLL(h, t);
                Main.hardClauses.add(temp);
            }
    }

    public static void addConA(int h)
    {
        for(int j1 = 0; j1 < Main.f; j1++)
            for(int j2 = j1+1; j2 < Main.f; j2++)
                Main.hardClauses.add(new int[]{-varA(h,j1), -varA(h,j2)});
        int[] temp = new int[Main.f];
        for(int j1 = 0; j1 < Main.f; j1++)
            temp[j1] = varA(h,j1);
        Main.hardClauses.add(temp);

        if (h+1 < Main.depth)
            addConA(h+1);
    }

    public static void addConReadOnce()
    {
        // revise if you plan to add Main.featureToDirection=false back
        if(Main.orderedSelection)
        {
            for(int j = 0; j < Main.f - 1; j++)
                for(int h1 = 0; h1 < Main.depth; h1++)
                    for(int h2 = h1+1; h2 < Main.depth; h2++)
                        Main.hardClauses.add(new int[]{-varA2(h1,j), -varA2(h2,j), varA2(h1,j+1), varA2(h2,j+1)});

            for(int h1 = 0; h1 < Main.depth; h1++)
                for(int h2 = h1+1; h2 < Main.depth; h2++)
                    Main.hardClauses.add(new int[]{-varA2(h1,Main.f - 1), -varA2(h2,Main.f - 1)});
        }else
        {
            for(int j = 0; j < Main.f; j++)
                for(int h1 = 0; h1 < Main.depth; h1++)
                    for(int h2 = h1+1; h2 < Main.depth; h2++)
                        Main.hardClauses.add(new int[]{-varA(h1,j), -varA(h2,j)});
        }

    }

    public static void addConA2(int h)
    {
        for(int j = 0; j < Main.f-1; j++)
        {
            Main.hardClauses.add(new int[]{varA2(h,j), -varA2(h,j+1)});
            if(!Main.featureToDirection)
            {
                Main.hardClauses.add(new int[]{-varA2(h,j), varA2(h,j+1), varA(h,j)});
                Main.hardClauses.add(new int[]{varA2(h,j), -varA(h,j)});
                Main.hardClauses.add(new int[]{-varA2(h,j+1), -varA(h,j)});
            }
        }

        if(!Main.featureToDirection)
        {
            Main.hardClauses.add(new int[]{-varA2(h,Main.f-1), varA(h,Main.f-1)});
            Main.hardClauses.add(new int[]{varA2(h,Main.f-1), -varA(h,Main.f-1)});
        }

        Main.hardClauses.add(new int[]{varA2(h,0)});

        if(h > 0 && Main.symmetryBreakingFeatures)
        {
            for(int j = 0; j < Main.f; j++)
                Main.hardClauses.add(new int[]{varA2(h,j), -varA2(h-1,j)});
        }

        if (h+1 < Main.depth)
            addConA2(h+1);
    }

    public static void addConC(int t)
    {
        for(int l1 = 0; l1 < Main.k; l1++)
            for(int l2 = l1+1; l2 < Main.k; l2++)
                Main.hardClauses.add(new int[]{-varC(t,l1), -varC(t,l2)});

        if(Main.maxEmpties)
        {
            if(Main.pruneLabels || Main.maxRedundantNode || Main.minBDD || Main.postBBDPhase)
            {
                for(int l1 = 0; l1 < Main.k; l1++)
                    for(int l2 = l1+1; l2 < Main.k; l2++)
                        Main.hardClauses.add(new int[]{-varCE(t,l1), -varCE(t,l2)});

                for(int l1 = 0; l1 < Main.k; l1++)
                    for(int l2 = 0; l2 < Main.k; l2++)
                        Main.hardClauses.add(new int[]{-varC(t,l1), -varCE(t,l2)});
            }else
            {
                for(int l1 = 0; l1 < Main.k; l1++)
                    Main.hardClauses.add(new int[]{-varC(t,l1), -varCE(t,0)});
            }
        }

        if(Main.maxEmpties)
        {
            if(Main.pruneLabels || Main.maxRedundantNode || Main.minBDD || Main.postBBDPhase)
            {
                int[] temp = new int[Main.k*2];
                for(int l1 = 0; l1 < Main.k; l1++)
                    temp[l1] = varC(t,l1);
                for(int l1 = 0; l1 < Main.k; l1++)
                    temp[l1+Main.k] = varCE(t,l1);
                Main.hardClauses.add(temp);
            }else
            {
                int[] temp = new int[Main.k+1];
                for(int l1 = 0; l1 < Main.k; l1++)
                    temp[l1] = varC(t,l1);
                temp[Main.k] = varCE(t,0);
                Main.hardClauses.add(temp);
            }

        }else
        {
            int[] temp = new int[Main.k];
            for(int l1 = 0; l1 < Main.k; l1++)
                temp[l1] = varC(t,l1);
            Main.hardClauses.add(temp);
        }

        if (t+1 < Main.leaves_cnt)
            addConC(t+1);
    }

    public static void addConLeaves()
    {
        //ArrayList<Integer> zeros = new ArrayList<>();
        //ArrayList<Integer> ones = new ArrayList<>();
        ArrayList<Integer> literals = new ArrayList<>();

        for(int t=0; t<Main.leaves_cnt; t++)
        {
            for(int i=0; i<Main.n; i++)
            {
                literals.clear();

                if(Main.modelPresence)
                {
                    literals.add(varZ(t,i));
                    if(Main.fixLabels)
                    {
                        if(!Main.fixedLabels[t][Main.labels[i]-1])
                            Main.hardClauses.add(new int[]{-varZ(t,i), -varO(i)});
                    }
                    else
                        Main.hardClauses.add(new int[]{-varZ(t,i), -varO(i), varC(t, Main.labels[i]-1)});
                }else
                {
                    if(!Main.fixLabels)
                        literals.add(varC(t, Main.labels[i]-1));

                    if(!Main.minDepthObj)
                        literals.add(-varO(i));
                }

                if(Main.modelDirection)
                {
                    int pt = t;
                    if(Main.diagram)
                        for(int h=Main.depth*(Main.depth+2)/8+Main.depth-1; h>=Main.depth*(Main.depth+2)/8+Main.depth/2; h--)
                        {
                            int dir = pt % 2;
                            if(dir == 1)
                                literals.add(varD(h, i)); // changed the negation of this
                            else
                                literals.add(-varD(h, i)); // and this

                            pt = pt / 2;
                        }
                    else if(Main.multDimFeatures)
                        for(int h=Main.featureDims.length-1; h>=0; h--)
                        {
                            int dir = pt % 2;
                            if(dir == 1)
                                literals.add(varLD(h, i));
                            else
                                literals.add(-varLD(h, i));

                            pt = pt / 2;
                        }
                    else
                        for(int h=Main.depth-1; h>=0; h--)
                        {
                            int dir = pt % 2;
                            if(dir == 1)
                                literals.add(varD(h, i)); // changed the negation of this
                            else
                                literals.add(-varD(h, i)); // and this

                            pt = pt / 2;
                        }
                }else
                {
                    System.out.println("Obsolete feature!!!!!!!!!!!!!!");
//                    zeros.clear();
//                    ones.clear();
//                    for(int j=0; j<Main.f; j++)
//                        if(Main.data[i][j] >= 0.5)
//                            ones.add(j);
//                        else
//                            zeros.add(j);
//
//                    int pt = t;
//                    for(int h=Main.depth-1; h>=0; h--)
//                    {
//                        int dir = pt % 2;
//                        if(dir == 1)
//                        {
//                            for(int j: zeros)
//                                literals.add(varA(h, j));
//                        }else
//                        {
//                            for(int j: ones)
//                                literals.add(varA(h, j));
//                        }
//                        pt = pt / 2;
//                    }
                }

                int[] temp = new int[literals.size()];
                for(int l=0; l<temp.length; l++)
                    temp[l] = literals.get(l);

                if(Main.fixLabels)
                {
                    if(!Main.fixedLabels[t][Main.labels[i]-1])
                        Main.hardClauses.add(temp);
                }else
                    Main.hardClauses.add(temp);


                if(Main.modelPresence)
                {
                    int temp_s = 0;
                    if(!Main.fixLabels)
                        temp_s ++;
                    for(int l=1; l<temp.length; l++)
                        Main.hardClauses.add(new int[]{-varZ(t,i), -temp[l]});
                }
            }
        }
    }

    public static void addConObj()
    {
        if(!Main.minDepthObj)
            for(int i=0; i<Main.n; i++)
            {
                Main.softClauses.add(new int[]{varO(i)});
                Main.softClausesWeights.add(Main.classifyWeight);
            }
        if(Main.maxRedundantNode)
        {
            for(int t=0; t<cntB; t++)
            {
                Main.softClauses.add(new int[]{-varB(t)});
                Main.softClausesWeights.add(Main.redundantWeight);
            }
        }
        if(Main.maxEmpties)
        {
            if(Main.pruneLabels || Main.maxRedundantNode || Main.minBDD)
            {
                for(int t=0; t<Main.leaves_cnt; t++)
                    for(int l=0; l<Main.k; l++)
                    {
                        Main.softClauses.add(new int[]{varCE(t, l)});
                        Main.softClausesWeights.add(Main.emptyWeight);
                    }
            }else
            {
                for(int t=0; t<Main.leaves_cnt; t++)
                {
                    Main.softClauses.add(new int[]{varCE(t, 0)});
                    Main.softClausesWeights.add(Main.emptyWeight);
                }
            }

        }
    }

    public static void addConSplit()
    {
        ArrayList<Integer> zeros = new ArrayList<>();
        ArrayList<Integer> ones = new ArrayList<>();

        for(int org_h=Main.depth-1; org_h>=0; org_h--)
        {
            int h;
            if(Main.diagram)
            {
                h = org_h;
                for(int temp=0; temp<=org_h; temp+=2)
                    h += temp/2;
            }
            else
                h = org_h;
            if(Main.featureToDirection)
            {
                if(Main.orderedSelection)
                {
                    for(int j=0; j<Main.f-1; j++)
                    {
                        Main.hardClauses.add(new int[]{-varA2(org_h, j), varA2(org_h, j+1), varD(h, Main.sorted[0][j])});
                        if(Main.forceSplit)
                        {
                            if(!Main.categorical[j])
                                Main.hardClauses.add(new int[]{-varA2(org_h, j), varA2(org_h, j+1), -varD(h, Main.sorted[Main.n-1][j])});
                        }


                        for(int i=0; i<Main.n-1; i++)
                        {
                            if(Main.data[Main.sorted[i][j]][j] == Main.data[Main.sorted[i+1][j]][j])
                            {
                                Main.hardClauses.add(new int[]{-varA2(org_h, j), varA2(org_h, j+1), -varD(h, Main.sorted[i][j]), varD(h, Main.sorted[i+1][j])});
                                Main.hardClauses.add(new int[]{-varA2(org_h, j), varA2(org_h, j+1), varD(h, Main.sorted[i][j]), -varD(h, Main.sorted[i+1][j])});
                            }else
                            {
                                if(!Main.categorical[j])
                                    Main.hardClauses.add(new int[]{-varA2(org_h, j), varA2(org_h, j+1), varD(h, Main.sorted[i][j]), -varD(h, Main.sorted[i+1][j])});
                            }
                        }
                    }

                    int last_j = Main.f-1;

                    Main.hardClauses.add(new int[]{-varA2(org_h, last_j), varD(h, Main.sorted[0][last_j])});
                    if(!Main.categorical[last_j])
                        Main.hardClauses.add(new int[]{-varA2(org_h, last_j), -varD(h, Main.sorted[Main.n-1][last_j])});

                    for(int i=0; i<Main.n-1; i++)
                    {
                        if(Main.data[Main.sorted[i][last_j]][last_j] == Main.data[Main.sorted[i+1][last_j]][last_j])
                        {
                            Main.hardClauses.add(new int[]{-varA2(org_h, last_j), -varD(h, Main.sorted[i][last_j]), varD(h, Main.sorted[i+1][last_j])});
                            Main.hardClauses.add(new int[]{-varA2(org_h, last_j), varD(h, Main.sorted[i][last_j]), -varD(h, Main.sorted[i+1][last_j])});
                        }else
                        {
                            if(!Main.categorical[last_j])
                                Main.hardClauses.add(new int[]{-varA2(org_h, last_j), varD(h, Main.sorted[i][last_j]), -varD(h, Main.sorted[i+1][last_j])});
                        }
                    }

                }else
                {
                    System.out.println("Obsolete Feature!!!!!!!!!!!!! Is it though?");

                    for(int j=0; j<Main.f; j++)
                    {
                        Main.hardClauses.add(new int[]{-varA(org_h, j), varD(h, Main.sorted[0][j])});
                        if(Main.forceSplit)
                        {
                            if(!Main.categorical[j])
                                Main.hardClauses.add(new int[]{-varA(org_h, j), -varD(h, Main.sorted[Main.n-1][j])});
                        }

                        for(int i=0; i<Main.n-1; i++)
                        {
                            if(Main.data[Main.sorted[i][j]][j] == Main.data[Main.sorted[i+1][j]][j])
                            {
                                Main.hardClauses.add(new int[]{-varA(org_h, j), -varD(h, Main.sorted[i][j]), varD(h, Main.sorted[i+1][j])});
                                Main.hardClauses.add(new int[]{-varA(org_h, j), varD(h, Main.sorted[i][j]), -varD(h, Main.sorted[i+1][j])});
                            }else
                            {
                                if(!Main.categorical[j])
                                    Main.hardClauses.add(new int[]{-varA(org_h, j), varD(h, Main.sorted[i][j]), -varD(h, Main.sorted[i+1][j])});
                            }
                        }
                    }

                }
            }else
            {
                System.out.println("Obsolete feature!!!!!!!!!!!!!!!!!!!!!!");
//                for(int i=0; i<Main.n; i++)
//                {
//                    zeros.clear();
//                    ones.clear();
//                    for(int j=0; j<Main.f; j++)
//                        if(Main.data[i][j] >= 0.5)
//                            ones.add(j);
//                        else
//                            zeros.add(j);
//
//                    int[] temp = new int[ones.size() + 1];
//                    for(int o = 0; o<ones.size(); o++)
//                        temp[o] = varA(org_h, ones.get(o));
//                    temp[ones.size()] = -varD(h, i);
//                    Main.hardClauses.add(temp);
//                    int[] temp2 = new int[zeros.size() + 1];
//                    for(int z = 0; z<zeros.size(); z++)
//                        temp2[z] = varA(org_h, zeros.get(z));
//                    temp2[zeros.size()] = varD(h, i);
//                    Main.hardClauses.add(temp2);
//                }
            }
        }

    }

    public static void addConLabels()
    {
        int[] indexList = new int[Main.leaves_cnt/2];
        for(int t=0; t<indexList.length; t++)
            indexList[t] = t;

        int jump = Main.leaves_cnt/2;

        int b_counter = 0;
        int r_counter = 0;


        for(int h=0; h<Main.leaves_cnt_log; h++)
        {
//            for(int t=0; t<indexList.length; t++)
//                System.out.print(" " + indexList[t]);
//            System.out.println();

            for(int t=0; t<indexList.length; t++)
                for(int l=0; l<Main.k; l++)
                {
                    Main.hardClauses.add(new int[]{-varCD(h, t), -varC(indexList[t],l), -varC(indexList[t]+jump,l)});
                    if(Main.maxEmpties)
                    {
                        Main.hardClauses.add(new int[]{-varCD(h, t), -varC(indexList[t],l), -varCE(indexList[t]+jump,l)});
                        Main.hardClauses.add(new int[]{-varCD(h, t), -varCE(indexList[t],l), -varC(indexList[t]+jump,l)});
                        Main.hardClauses.add(new int[]{-varCD(h, t), -varCE(indexList[t],l), -varCE(indexList[t]+jump,l)});
                    }
                    // Main.hardClauses.add(new int[]{-varCD(h, t), varC(indexList[t],l), varC(indexList[t]+jump,l)}); I think this is invalid
                    if(Main.maxEmpties)
                    {
                        Main.hardClauses.add(new int[]{varCD(h, t), varC(indexList[t],l), varCE(indexList[t],l), -varC(indexList[t]+jump,l)});
                        Main.hardClauses.add(new int[]{varCD(h, t), varC(indexList[t],l), varCE(indexList[t],l), -varCE(indexList[t]+jump,l)});
                        Main.hardClauses.add(new int[]{varCD(h, t), -varC(indexList[t],l), varC(indexList[t]+jump,l), varCE(indexList[t]+jump,l)});
                        Main.hardClauses.add(new int[]{varCD(h, t), -varCE(indexList[t],l), varC(indexList[t]+jump,l), varCE(indexList[t]+jump,l)});
                    }else
                    {
                        Main.hardClauses.add(new int[]{varCD(h, t), varC(indexList[t],l), -varC(indexList[t]+jump,l)});
                        Main.hardClauses.add(new int[]{varCD(h, t), -varC(indexList[t],l), varC(indexList[t]+jump,l)});
                    }

                }

            if(Main.maxRedundantNode || Main.minBDD || Main.postBBDPhase)
            {
                for(int w=0; w<(int)Math.pow(2,h); w++)
                {
                    ArrayList<Integer> temp = new ArrayList<>();

                    for(int q=0; q<(int)Math.pow(2,Main.leaves_cnt_log-h-1); q++)
                        Main.hardClauses.add(new int[]{-varCD(h,w*((int)Math.pow(2,Main.leaves_cnt_log-h-1)) + q), varB(b_counter)});
                    temp.add(-varB(b_counter));
                    b_counter ++;

                    if(Main.minBDD || Main.postBBDPhase)
                    {
                        for(int w2=0; w2<w; w2++)
                        {
                            for(int q=0; q<(int)Math.pow(2,Main.leaves_cnt_log-h); q++)
                                for(int l=0; l<Main.k; l++)
                                {
                                    Main.hardClauses.add(new int[]{-varC(w*((int)Math.pow(2,Main.leaves_cnt_log-h)) + q,l), varC(w2*((int)Math.pow(2,Main.leaves_cnt_log-h)) + q,l), -varR(r_counter)});
                                    Main.hardClauses.add(new int[]{varC(w*((int)Math.pow(2,Main.leaves_cnt_log-h)) + q,l), -varC(w2*((int)Math.pow(2,Main.leaves_cnt_log-h)) + q,l), -varR(r_counter)});
                                }
                            temp.add(varR(r_counter));
                            r_counter ++;
                        }

                        if(Main.beadCard)
                        {
                            temp.add(varUB(b_counter));

                            int[] temp2 = new int[temp.size()];
                            for(int p=0; p<temp2.length; p++)
                                temp2[p] = temp.get(p);
                            Main.hardClauses.add(temp2);

                            //Main.softClauses.add(new int[] {-varUB(b_counter)});
                            //Main.softClausesWeights.add(Main.bddWeight);

                        }else
                        {
                            int[] temp2 = new int[temp.size()];
                            for(int p=0; p<temp2.length; p++)
                                temp2[p] = temp.get(p);
                            Main.softClauses.add(temp2);
                            Main.softClausesWeights.add(Main.bddWeight);
                        }
                    }
                }
            }

            if(Main.pruneLabels)
            {
                int[] temp = new int[indexList.length];
                for(int t=0; t<indexList.length; t++)
                    temp[t] = varCD(h, t);
                Main.hardClauses.add(temp);
            }

            jump = jump/2;
            if(jump > 0)
                for(int t=0; t<indexList.length; t++)
                    if((t / jump) % 2 == 1)
                        indexList[t] += jump;
        }
    }

    public static void addConStructure()
    {
        int source_s = 0;
        for(int d=0; d<Main.depth; d+= 2)
        {
            int source_cnt = (int) Math.pow(2, d/2+2);
            int source_len = d/2 + 1;

            if(Main.limitDiagram)
            {
                //if(Main.forceSplit) this part has nothing to do with forcing split (?)
                    Main.hardClauses.add(new int[]{varS(source_s + d/2)});
                if(Main.forceSplit)
                {
                    int[] temp = new int[source_cnt];
                    for (int t = 0; t < source_cnt; t++)
                        temp[t] = -varS(source_s + t*source_len + d/2);
                    Main.hardClauses.add(temp);
                }
                boolean[] toFix = new boolean[d/2];

                for(int f=0; f<toFix.length; f++)
                    toFix[f] = true;
                for(int p=0; p<source_cnt/4; p++)
                {
                    for(int f=0; f<toFix.length; f++)
                        for(int e=0; e<4; e++)
                            if(toFix[f])
                                Main.hardClauses.add(new int[]{varS(source_s + p*4*source_len + e*source_len + f)});
                            else
                                Main.hardClauses.add(new int[]{-varS(source_s + p*4*source_len + e*source_len + f)});

                    for(int f=toFix.length-1; f>=0; f--)
                        if(toFix[f])
                        {
                            toFix[f] = false;
                            break;
                        }else
                            toFix[f] = true;
                }

                for(int p=0; p<source_cnt/4-1; p++)
                {
                    int p2 = p+1;
                    for(int e=0; e<4; e++)
                    for(int s=d/2; s<source_len; s++)
                    {
                        Main.hardClauses.add(new int[]{varS(source_s + p*4*source_len + e*source_len + s), -varS(source_s + p2*4*source_len + e*source_len + s)});
                        Main.hardClauses.add(new int[]{-varS(source_s + p*4*source_len + e*source_len + s), varS(source_s + p2*4*source_len + e*source_len + s)});
                    }
                }
            }else // if(Main.forceSplit) this part has nothing to do with forcing split (?)
            {
                for(int s=0; s<source_len; s++)
                    Main.hardClauses.add(new int[]{varS(source_s + s)});
                if(Main.forceSplit)
                {
                    int[] temp = new int[source_cnt * source_len];
                    for (int t = 0; t < source_cnt; t++)
                        for(int l=0; l<source_len; l++)
                            temp[t*source_len+l] = -varS(source_s + t*source_len + l);
                    Main.hardClauses.add(temp);
                }
            }

            int org_dir_s = 0;
            int dup_dir_s = 0;
            for(int temp=0; temp < d/2; temp++)
            {
                org_dir_s += temp+2;
                dup_dir_s += temp+2;
            }
            dup_dir_s += d/2 + 2;

            for(int source=0; source<source_cnt; source++)
            {
                int[] directions = new int[dup_dir_s - org_dir_s];
                int cs = source;
                for(int temp=directions.length-1; temp >= 0; temp--)
                {
                    if (cs % 2 == 1)
                        directions[temp] = +1; // changed the negation of this
                    else
                        directions[temp] = -1; // and this
                    cs = cs/2;
                }
                for(int i=0; i<Main.n; i++)
                {
                    for(int len=0; len<source_len; len++)
                    {
                        int[] temp = new int[dup_dir_s - org_dir_s + 2];
                        int[] temp2 = new int[dup_dir_s - org_dir_s + 2];

                        for(int dir=org_dir_s; dir<dup_dir_s; dir++)
                        {
                            temp[dir - org_dir_s] = varD(dir, i) * directions[dir - org_dir_s];
                            temp2[dir - org_dir_s] = varD(dir, i) * directions[dir - org_dir_s];
                        }
                        temp[dup_dir_s - org_dir_s] = varS(source_s + source*source_len + len);
                        temp[dup_dir_s - org_dir_s + 1] = -varD(dup_dir_s + len, i);
                        temp2[dup_dir_s - org_dir_s] = -varS(source_s + source*source_len + len);
                        temp2[dup_dir_s - org_dir_s + 1] = varD(dup_dir_s + len, i);
                        Main.hardClauses.add(temp);
                        Main.hardClauses.add(temp2);
                    }
                }
            }
            source_s += source_cnt * source_len;
        }
    }

    public static void addConFix()
    {
        for(int t=0; t<Main.leaves_cnt; t++)
            if(Solution.leaf_known[t])
                Main.hardClauses.add(new int[]{varC(t,Solution.leaf_labels[t]-1)});
        if(Solution.leaf_map != null && Main.postMinBDDLimited)
        {
            for(int t1=0; t1<Main.leaves_cnt; t1++)
                //if(!Solution.leaf_known[t1])
                    for(int t2=t1+1; t2<Main.leaves_cnt; t2++)
                  //      if(!Solution.leaf_known[t2])
                            if(Solution.leaf_map[t1] == Solution.leaf_map[t2])
                                for(int l=0; l<Main.k; l++)
                                {
                                    Main.hardClauses.add(new int[]{-varC(t1,l), varC(t2,l)});
                                    Main.hardClauses.add(new int[]{varC(t1,l), -varC(t2,l)});
                                }

        }
    }

    public static void addConBeadUB() throws Exception
    {
        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command("bash", "-c", "python3 card.py " + varUB(0) + " " + varUB(cntUB-1) + " " + Main.beadUB + " " + varUB(cntUB-1));
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while (!(line = reader.readLine()).startsWith("==="))
        {
            String[] lits = line.split(" ");
            int[] temp = new int[lits.length];
            for(int i=0; i<temp.length; i++)
                temp[i] = Integer.parseInt(lits[i]);

            Main.hardClauses.add(temp);
        }

        int temp = Integer.parseInt(reader.readLine());
        updateCnt(temp);
    }
}
