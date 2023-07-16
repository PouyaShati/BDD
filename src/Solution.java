import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Solution
{
    public static Tree tree;
    public static int[] leaf_labels;
    public static int[] diag_leaf_labels;
    public static int[] mult_leaf_labels;
    public static int[][] structure;
    public static int[] leaf_map = null;
    public static boolean[] leaf_known;
    //public static boolean[] diag_leaf_known;
    public static int[] positions;
    public static int unknownCnt;
    public static int classifyCost;
    public static int redundancyCost;
    public static int minEmptyCost;
    public static void writeSolution(String name, String assignment, boolean writeFile) throws Exception
    {
        if(!Main.postBBDPhase)
            tree = new Tree(Main.depth);
        leaf_labels = new int[(int) Math.pow(2, Main.depth)];
        leaf_known = new boolean[(int) Math.pow(2, Main.depth)];
        positions = new int[Main.n];
        if(Main.diagram)
        {
            diag_leaf_labels = new int[Main.leaves_cnt];
            //diag_leaf_known = new boolean[Main.leaves_cnt];
            structure = new int[Main.depth/2][];
            for(int d=0; d<Main.depth; d+=2)
                structure[d/2] = new int[((int) Math.pow(2, d / 2 + 2))];
        }else if(Main.multDimFeatures)
        {
            mult_leaf_labels = new int[Main.leaves_cnt];
            structure = new int[Main.featureDims.length][];
            for(int s=0; s<structure.length; s++)
                structure[s] = new int[(int) Math.pow(2, Main.featureDims[s])];
            leaf_map = new int[(int) Math.pow(2, Main.depth)];
        }
        classifyCost = 0;
        redundancyCost = 0;
        minEmptyCost = 0;

        FileWriter fileWriter;
        PrintWriter printWriter = null;

        if(writeFile)
        {
            fileWriter = new FileWriter(name);
            printWriter = new PrintWriter(fileWriter);
        }
        int pointer = 0;

        if(VarsAndCons.cntA > 0)
        {
            if(writeFile)
                printWriter.write("a:");
            for(int h=0; h<Main.depth; h++)
            {
                printWriter.write("\n");
                for(int j=0; j<Main.f; j++)
                {
                    if(writeFile)
                        printWriter.write(" " + (pointer+1) + ": " + assignment.charAt(pointer));

                    if(assignment.charAt(pointer) == '1')
                        tree.features[h] = j;

                    pointer++;
                }
            }
            if(writeFile)
                printWriter.write("\n");
        }


        if(VarsAndCons.cntA2 > 0)
        {
            if(writeFile)
                printWriter.write("a2:");
            for(int h=0; h<Main.depth; h++)
            {
                printWriter.write("\n");
                for(int j=0; j<Main.f; j++)
                {
                    if(writeFile)
                        printWriter.write(" " + (pointer+1) + ": " + assignment.charAt(pointer));

                    if(assignment.charAt(pointer) == '1')
                        tree.features[h] = j;

                    pointer++;
                }
            }
            if(writeFile)
                printWriter.write("\n");
        }

        if(writeFile)
            printWriter.write("c:");
        for(int t=0; t<Main.leaves_cnt; t++)
        {
            printWriter.write("\n");
            for(int l=0; l<Main.k; l++)
            {
                if(writeFile)
                {
                    if(Main.fixLabels)
                        printWriter.write(" " + Main.fixedLabels[t][l]);
                    else
                        printWriter.write(" " + (pointer+1) + ": " + assignment.charAt(pointer));
                }

                if((Main.fixLabels && Main.fixedLabels[t][l]) || (!Main.fixLabels && assignment.charAt(pointer) == '1'))
                {
                    if(Main.diagram)
                        diag_leaf_labels[t] = l+1;
                    else if(Main.multDimFeatures)
                        mult_leaf_labels[t] = l+1;
                    else
                        leaf_labels[t] = l+1;
                    if(Main.maxEmpties)
                        minEmptyCost ++;
                }

                if(!Main.fixLabels)
                    pointer++;
            }
        }
        if(writeFile)
            printWriter.write("\n");

        if(VarsAndCons.cntD > 0)
        {
            if(writeFile)
                printWriter.write("d:");
            if(Main.diagram)
            {
                System.out.println("Real depths:");
                int real_h = 0;
                for(int h=0; h<Main.depth + (Main.depth * (Main.depth + 2) / 8); h++)
                {
                    int split = 0;
                    boolean isRealD = false;
                    int s_h = 0;
                    int s_c = 1;
                    while(s_h <= h)
                    {
                        if(isRealD)
                        {
                            s_h += s_c;
                            s_c++;
                            isRealD = false;
                        }else
                        {
                            s_h += 2;
                            isRealD = true;
                        }
                    }
                    System.out.print(" " + isRealD);

                    if(isRealD)
                    {
                        for(int i=0; i<Main.n; i++)
                            positions[i] = positions[i] * 2;
                    }
                    printWriter.write("\n");
                    for(int i=0; i<Main.n; i++)
                    {
                        if(writeFile)
                            printWriter.write(" " + (pointer+1) + ": " + assignment.charAt(pointer));
                        //if((Main.depth + (Main.depth * (Main.depth + 2) / 8) - h) <= Main.depth/2       &&       assignment.charAt(pointer) == '1')
                        if(isRealD && assignment.charAt(pointer) == '0')
                            positions[i] += 1;
                        if(isRealD && assignment.charAt(pointer) == '1')
                            split++;

                        pointer++;
                    }
                    if(isRealD)
                    {
                        if(split > 0)
                            tree.thresholds[real_h] = Main.data[Main.sorted[split-1][tree.features[real_h]]][tree.features[real_h]];
                        else
                            tree.thresholds[real_h] = Main.data[Main.sorted[0][tree.features[real_h]]][tree.features[real_h]] - 1.0;
                        // check if the above line is okayyyyyyyyyyyyyyyyyyyyyyyyyyy
                        real_h++;
                    }
                }
                System.out.println();
            }else
            {
                for(int h=0; h<Main.depth; h++)
                {
                    int split = 0;
                    //if(!Main.multDimFeatures)
                        for(int i=0; i<Main.n; i++)
                            positions[i] = positions[i] * 2;
                    printWriter.write("\n");
                    for(int i=0; i<Main.n; i++)
                    {
                        if(writeFile)
                            printWriter.write(" " + (pointer+1) + ": " + assignment.charAt(pointer));

                        //if(assignment.charAt(pointer) == '0' && !Main.multDimFeatures)
                        if(assignment.charAt(pointer) == '0')
                            positions[i] += 1;
                        if(assignment.charAt(pointer) == '1')
                            split++;

                        pointer++;
                    }
                    if(split > 0)
                    {
                        //System.out.println("Split");
                        //System.out.println(split);
                        //System.out.println(tree.features[h]);
                        //System.out.println(Main.sorted[split-1][tree.features[h]]);
                        tree.thresholds[h] = Main.data[Main.sorted[split-1][tree.features[h]]][tree.features[h]];
                    }
                    else
                        tree.thresholds[h] = Main.data[Main.sorted[0][tree.features[h]]][tree.features[h]] - 1.0;
                }
            }
            if(writeFile)
                printWriter.write("\n");
        }



        if(VarsAndCons.cntCD > 0)
        {
            if(writeFile)
                printWriter.write("cd:");
            for(int h=0; h<Main.leaves_cnt_log; h++)
            {
                printWriter.write("\n");
                for(int i=0; i<Main.leaves_cnt/2; i++)
                {
                    if(writeFile)
                        printWriter.write(" " + (pointer+1) + ": " + assignment.charAt(pointer));
                    pointer++;
                }
            }
            if(writeFile)
                printWriter.write("\n");
        }

        if(VarsAndCons.cntO > 0)
        {
            if(writeFile)
                printWriter.write("o:\n");
            for(int h=0; h<VarsAndCons.cntO; h++)
            {
                if(writeFile)
                    printWriter.write(" " + (pointer+1) + ": " + assignment.charAt(pointer));

                if(assignment.charAt(pointer) == '0')
                    classifyCost += 1;

                pointer++;
            }
            if(writeFile)
                printWriter.write("\n");
        }

        if(VarsAndCons.cntS > 0)
        {
            if(writeFile)
                printWriter.write("s:\n");
            for(int d=0; d < Main.depth; d += 2)
            for(int t=0; t < ((int) Math.pow(2, d / 2 + 2)); t++)
            {
                int link = 0;
                for(int b=0; b< (d / 2 + 1); b++)
                {
                    link = link * 2;
                    if(writeFile)
                        printWriter.write(" " + (pointer+1) + ": " + assignment.charAt(pointer));

                    if(assignment.charAt(pointer) == '0') // changed from 1 to 0
                        link ++;

                    pointer++;
                }
                structure[d/2][t] = link;
            }
            if(writeFile)
                printWriter.write("\n");
        }

        if(VarsAndCons.cntB > 0)
        {
            if(writeFile)
                printWriter.write("b:\n");
            for(int h=0; h<VarsAndCons.cntB; h++)
            {
                if(writeFile)
                    printWriter.write(" " + (pointer+1) + ": " + assignment.charAt(pointer));

                if(assignment.charAt(pointer) == '1')
                    redundancyCost += 1;

                pointer++;
            }
            if(writeFile)
                printWriter.write("\n");
        }


        if(VarsAndCons.cntCE > 0)
        {
            if(writeFile)
                printWriter.write("ce:");
            for(int t=0; t<Main.leaves_cnt; t++)
            {
                printWriter.write("\n");
                int temp;
                if(Main.pruneLabels || Main.maxRedundantNode)
                    temp = Main.k;
                else
                    temp = 1;
                for(int l=0; l<temp; l++)
                {
                    if(writeFile)
                        printWriter.write(" " + (pointer+1) + ": " + assignment.charAt(pointer));

                    if(Main.pruneLabels || Main.maxRedundantNode)
                        if(assignment.charAt(pointer) == '1')
                            leaf_labels[t] = l+1;

                    pointer++;
                }
            }
            if(writeFile)
                printWriter.write("\n");
        }


        if(VarsAndCons.cntR > 0)
        {
            if(writeFile)
                printWriter.write("r:\n");
            for(int h=0; h<VarsAndCons.cntR; h++)
            {
                if(writeFile)
                    printWriter.write(" " + (pointer+1) + ": " + assignment.charAt(pointer));

                //if(assignment.charAt(pointer) == '1')
                //    redundancyCost += 1;

                pointer++;
            }
            if(writeFile)
                printWriter.write("\n");
        }


        if(VarsAndCons.cntLD > 0)
        {
            if(writeFile)
                printWriter.write("LD:");

            for(int h=0; h<Main.featureDims.length; h++)
            {
                //for(int i=0; i<Main.n; i++)
                //    positions[i] = positions[i] * 2;
                printWriter.write("\n");
                for(int i=0; i<Main.n; i++)
                {
                    if(writeFile)
                        printWriter.write(" " + (pointer+1) + ": " + assignment.charAt(pointer));
                    //if(assignment.charAt(pointer) == '0')
                    //    positions[i] += 1;

                    pointer++;
                }
            }

            if(writeFile)
                printWriter.write("\n");
        }


        if(VarsAndCons.cntLL > 0)
        {
            if(writeFile)
                printWriter.write("LL:\n");
            for(int h=0; h<Main.featureDims.length; h++)
                for(int t=0; t<(int)Math.pow(2, Main.featureDims[h]); t++)
            {
                if(writeFile)
                    printWriter.write(" " + (pointer+1) + ": " + assignment.charAt(pointer));

                if(assignment.charAt(pointer) == '1')
                    structure[h][t] = 0;
                else
                    structure[h][t] = 1;

                pointer++;
            }
            if(writeFile)
                printWriter.write("\n");
        }


        for(int i=0; i<Main.n; i++)
            leaf_known[positions[i]] = true;

        if(writeFile)
            printWriter.close();
    }


    public static void printDiag()
    {
        System.out.println("Original Labels:");
        printLabels(diag_leaf_labels, null, false, false);

        System.out.println("Structure:");
        for(int i=0; i<structure.length; i++)
        {
            for(int j=0; j<structure[i].length; j++)
                System.out.print(" " + structure[i][j]);
            System.out.println();
        }

        int[] currLabels = diag_leaf_labels;
        //boolean[] currKnown = diag_leaf_known;

        for(int d= Main.depth - 2; d>= 0; d-=2)
        {
            int[] nextLabels = new int[currLabels.length*2];
            //boolean[] nextKnowns = new boolean[currKnown.length*2];
            int len = (int) Math.pow(2, Main.depth - 2 - d);
            for(int t=0; t<((int) Math.pow(2, d / 2 + 2)); t++)
                for(int l=0; l<len; l++)
                {
                    nextLabels[t*len + l] = currLabels[structure[d/2][t]*len+l];
                    //nextKnowns[t*len + l] = currKnown[structure[d/2][t]*len+l];
                }
            currLabels = nextLabels;
            //currKnown = nextKnowns;
        }

        System.out.println("Expanded Labels:");
        tree.labels = printLabels(currLabels, leaf_known, true, false);

        leaf_labels = currLabels; // is it okay to keep working with leaf_labels instead of a different variable dedicated to the diagram case?

        System.out.println("Cost:");
        printCosts();
    }


    public static void printMult()
    {
        System.out.println("Original Labels:");
        printLabels(mult_leaf_labels, null, false, false);

        System.out.println("Structure:");
        for(int i=0; i<structure.length; i++)
        {
            for(int j=0; j<structure[i].length; j++)
                System.out.print(" " + structure[i][j]);
            System.out.println();
        }

        int[] currLabels = mult_leaf_labels;

        int[] dirs = new int[Main.depth];
        for(int d=0; d<dirs.length; d++)
            dirs[d] = 0;

        int[] nextLabels = new int[(int) Math.pow(2, Main.depth)];

        for(int n=0; n<nextLabels.length; n++)
        {
            int pos = 0;
            int h = 0;
            for(int f=0; f<Main.featureDims.length; f++)
            {
                pos = pos * 2;
                int pos2 = 0;
                for(int f2=0; f2<Main.featureDims[f]; f2++)
                {
                    pos2 = pos2 * 2;
                    if(dirs[h] == 1)
                        pos2++;
                    h++;
                }
                if(structure[f][pos2] == 1)
                    pos += 1;
            }

            nextLabels[n] = currLabels[pos];
            leaf_map[n] = pos;

            for(int d=dirs.length-1; d>=0; d--)
                if(dirs[d] == 0)
                {
                    dirs[d] = 1;
                    break;
                }else
                    dirs[d] = 0;
        }

        System.out.println("Expanded Labels:");
        tree.labels = printLabels(nextLabels, leaf_known, true, false);
        leaf_labels = nextLabels;

        System.out.println("Cost:");
        printCosts();
    }

    public static void printNonDiag()
    {
        System.out.println("Labels: ");
        if(Main.postBBDPhase)
            tree.labels = printLabels(leaf_labels, leaf_known, true, true);
        else
            tree.labels = printLabels(leaf_labels, leaf_known, true, false);

        System.out.println("Cost:");
        printCosts();
    }

    public static void printCosts()
    {
        if(!Main.postBBDPhase)
        {
            System.out.println("Classify_cost: " + classifyCost);
            if(Main.maxRedundantNode)
                System.out.println("Redundancy_cost: " + redundancyCost);
            if(Main.maxEmpties)
                System.out.println("Min_empties_cost: " + minEmptyCost);
        }else
        {
            System.out.println("Bead_cnt should be equal to solution cost");
        }
    }


    public static int[] printLabels(int[] labels, boolean[] known, boolean toFindBeads, boolean post)
    {
        String labelsStr = "";
        for(int t=0; t<labels.length; t++)
        {
            System.out.print(labels[t]);
            labelsStr = labelsStr + labels[t];
        }
        System.out.println();

        if(toFindBeads)
        {
            if(post)
                System.out.println("Post_bead_count: " + findBeads(labelsStr).size());
            else
                System.out.println("Bead_count: " + findBeads(labelsStr).size());
        }
            //System.out.println("Bead_count: " + findBeads(labelsStr).size()); why is this here?


        if(!Main.postBBDPhase && known != null)
        {
            unknownCnt = 0;
            System.out.print("Unknowns: ");
            for(int t=0; t<known.length; t++)
                if(!known[t])
                {
                    System.out.print("u");
                    unknownCnt += 1;
                }else
                {
                    System.out.print(labels[t]);
                }
            System.out.println();
            System.out.println("Unknown count: " + unknownCnt);
        }

        if(Main.postParentLabel)
        {
            int len = labels.length;
            int[] newLabels = new int[labels.length];
            for(int i=0; i<newLabels.length; i++)
                if(known[i])
                    newLabels[i] = labels[i];
                else
                    newLabels[i] = -1;
            while(len > 1)
            {
                for(int p=0; p<newLabels.length/len; p++)
                {
                    int[] labelCnt = new int[Main.k];
                    for(int l=0; l<len; l++)
                        if(newLabels[p*len+l] > 0)
                            labelCnt[newLabels[p*len+l]-1]++;

                    int majorityLabel = 1;
                    for(int k=0; k<labelCnt.length; k++)
                        if(labelCnt[k] > labelCnt[majorityLabel-1])
                            majorityLabel = k+1;

                    boolean firstAllU = true;
                    boolean secondAllU = true;

                    for(int l=0; l<len/2; l++)
                        if(newLabels[p*len+l] > 0)
                            firstAllU = false;

                    for(int l=len/2; l<len; l++)
                        if(newLabels[p*len+l] > 0)
                            secondAllU = false;

                    if(firstAllU)
                        for(int l=0; l<len/2; l++)
                            newLabels[p*len+l] = majorityLabel;
                    if(secondAllU)
                        for(int l=len/2; l<len; l++)
                            newLabels[p*len+l] = majorityLabel;
                }
                len = len/2;
            }

            System.out.println("Labels after filling unknowns in parent mode:");
            labelsStr = "";
            for(int t=0; t<newLabels.length; t++)
            {
                System.out.print(newLabels[t]);
                labelsStr = labelsStr + newLabels[t];
            }
            System.out.println();

            if(toFindBeads)
                System.out.println("Post_bead_count: " + findBeads(labelsStr).size());

            return newLabels;
        }



        if(Main.postMergeLabel)
        {
            int len = labels.length;
            int[] newLabels = new int[labels.length];
            for(int i=0; i<newLabels.length; i++)
                if(known[i])
                    newLabels[i] = labels[i];
                else
                    newLabels[i] = -1;
            while(len > 1)
            {
                len = len/2;
                for(int p1=0; p1<newLabels.length/len-1; p1++)
                    OUTER: for(int p2=p1+1; p2<newLabels.length/len; p2++)
                    {
                        for(int l=0; l<len; l++)
                            if(newLabels[p1*len+l] > 0 && newLabels[p2*len+l] > 0)
                                if(newLabels[p1*len+l] != newLabels[p2*len+l])
                                    continue OUTER;

                        for(int l=0; l<len; l++)
                            if(newLabels[p1*len+l] < 0)
                                newLabels[p1*len+l] = newLabels[p2*len+l];
                            else if(newLabels[p2*len+l] < 0)
                                newLabels[p2*len+l] = newLabels[p1*len+l];
                    }
            }
            System.out.println("Labels after filling unknowns in merge mode:");
            labelsStr = "";
            for(int t=0; t<newLabels.length; t++)
            {
                System.out.print(newLabels[t]);
                labelsStr = labelsStr + newLabels[t];
            }
            System.out.println();

            if(toFindBeads)
                System.out.println("Post_bead_count: " + findBeads(labelsStr).size());

            return newLabels;
        }

        return labels;
    }
    public static ArrayList<String> findBeads(String labelsStr)
    {
        ArrayList<String> beads = new ArrayList<>();
        ArrayList<String> beadCandidates = new ArrayList<>();
        beadCandidates.add(labelsStr);
        while(beadCandidates.size() > 0)
        {
            String temp = beadCandidates.remove(0);
            if(temp.length() <= 1)
                continue;
            String first = temp.substring(temp.length()/2);
            String second = temp.substring(0,temp.length()/2);
            if(first.equals(second))
            {
                if(!beadCandidates.contains(first) && !beads.contains(first))
                    beadCandidates.add(first);
            }else
            {
                beads.add(temp);
                if(!beadCandidates.contains(first) && !beads.contains(first))
                    beadCandidates.add(first);
                if(!beadCandidates.contains(second) && !beads.contains(second))
                    beadCandidates.add(second);
            }
        }

        return beads;
    }



}
