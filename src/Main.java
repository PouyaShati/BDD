import java.util.ArrayList;
import java.util.Random;

public class Main
{
    public static int n;
    public static int f;
    public static int k;
    public static double[][] data;
    public static int[] labels;
    public static double[][] testData;
    public static int[] testLabels;
    public static double testRatio;
    public static int test5Fold;
    public static boolean anyCategorical = false;
    public static boolean[] categorical;
    public static int[][] sorted;
    public static int[][] sortedInverse;

    public static ArrayList<int[]> hardClauses;
    public static ArrayList<int[]> softClauses;
    public static ArrayList<Integer> softClausesWeights;
    public static int var_cnt;
    public static boolean timedout;
    public static int solutionCost;
    public static boolean unknownSolution;
    public static String output;
    public static boolean postBBDPhase = false;

    public static long startTime;
    public static long seed = 1111;

    public static boolean normalize;
    public static int depth;
    public static int leaves_cnt;
    public static int leaves_cnt_log;
    public static String postfix;
    public static String name;
    public static String loandraPath = "~/SAT_Project/loandra-master/loandra_static";
    public static int timeout;
    public static boolean printResult = true;
    public static boolean modelDirection;
    public static boolean modelPresence;
    public static boolean orderedSelection;
    public static boolean symmetryBreakingFeatures;
    public static boolean featureToDirection;
    public static boolean pruneLabels;
    public static boolean diagram;
    public static boolean limitDiagram;
    public static boolean multDimFeatures;
    public static int multSize;
    public static int[] featureDims;
    public static boolean maxRedundantNode;
    public static int classifyWeight;
    public static int redundantWeight;
    public static int emptyWeight;
    public static int bddWeight;
    public static boolean normalizeWeights;
    public static double weightUnit;
    public static boolean prioritizeClassification;
    public static boolean postMinBDD;
    public static boolean postMinBDDLimited;
    public static boolean postParentLabel;
    public static boolean postMergeLabel;
    public static boolean minBDD;
    public static boolean maxEmpties;
    public static boolean forceSplit;
    public static boolean readOnce;
    public static boolean minDepthObj;
    public static boolean minisat;
    public static boolean beadCard;
    public static int beadUB;
    public static boolean fixLabels;
    public static boolean[][] fixedLabels;

    public static void main(String[] args) throws Exception
    {
        startTime = System.currentTimeMillis();
        timeout = 15;
        depth = 1;
        postfix = "_test";
        testRatio = -1;
        test5Fold = -1;
        modelDirection = true;
        orderedSelection = true;
        symmetryBreakingFeatures = false;
        featureToDirection = true;
        pruneLabels = false;
        normalize = true;
        diagram = false;
        limitDiagram = false;
        maxRedundantNode = false;
        classifyWeight = 1;
        redundantWeight = 1;
        emptyWeight = 1;
        bddWeight = 1;
        normalizeWeights = false;
        weightUnit = 0;
        prioritizeClassification = false;
        maxEmpties = false;
        forceSplit = false;
        postMinBDD = false;
        postMinBDDLimited = false;
        postParentLabel = false;
        postMergeLabel = false;
        multDimFeatures = false;
        multSize = 2;
        minDepthObj = false;
        minisat = false;
        beadCard = false;
        beadUB = -1;
        readOnce = false;
        modelPresence = false;
        fixLabels = false;

        int arg_p = 0;

        if(args[arg_p].equals("-auto"))
        {
            AutoPlay.run(true);
            return;
        }else if(args[arg_p].equals("-autoRead"))
        {
            AutoPlay.run(false);
            return;
        }

        while (arg_p < args.length - 1)
        {
            if (args[arg_p].equals("-post"))
            {
                postfix = args[arg_p + 1];
                arg_p += 2;
            }else if (args[arg_p].equals("-dummy"))
            {
                arg_p += 1;
            }
            else if (args[arg_p].equals("-timeout"))
            {
                timeout = Integer.parseInt(args[arg_p + 1]);
                arg_p += 2;
            }
            else if (args[arg_p].equals("-seed"))
            {
                seed = Integer.parseInt(args[arg_p + 1]);
                arg_p += 2;
            }
            else if (args[arg_p].equals("-d"))
            {
                depth = Integer.parseInt(args[arg_p + 1]);
                arg_p += 2;
            }else if (args[arg_p].equals("-ratio"))
            {
                testRatio = Double.parseDouble(args[arg_p + 1]);
                arg_p += 2;
            }else if (args[arg_p].equals("-5fold"))
            {
                test5Fold = Integer.parseInt(args[arg_p + 1]);
                arg_p += 2;
            }else if (args[arg_p].equals("-no_d"))
            {
                System.out.println("Obsolete feature!!!!!!!!!!!");
                System.out.println("This Mode only works for binary datasets!!!");
                System.out.println("This Mode only works for binary datasets!!!");
                modelDirection = false;
                arg_p += 1;
            }else if (args[arg_p].equals("-no_o"))
            {
                System.out.println("Obsolete feature!!!!!!!!!!!");
                orderedSelection = false;
                arg_p += 1;
            }else if (args[arg_p].equals("-sym"))
            {
                symmetryBreakingFeatures = true;
                arg_p += 1;
            }else if (args[arg_p].equals("-no_ftd"))
            {
                System.out.println("Obsolete feature!!!!!!!!!!!");
                System.out.println("This Mode only works for binary datasets!!!"); // doubt (?)
                featureToDirection = false;
                arg_p += 1;
            }else if (args[arg_p].equals("-pl"))
            {
                pruneLabels = true;
                arg_p += 1;
            }else if (args[arg_p].equals("-diag"))
            {
                diagram = true;
                arg_p += 1;
            }else if (args[arg_p].equals("-l_diag"))
            {
                diagram = true;
                limitDiagram = true;
                arg_p += 1;
            }else if (args[arg_p].startsWith("-mult"))
            {
                multDimFeatures = true;
                if(args[arg_p].equals("-mult_s"))
                {
                    multSize = Integer.parseInt(args[arg_p+1]);
                    arg_p += 1;
                }
                arg_p += 1;
            }
            else if (args[arg_p].equals("-mrn"))
            {
                maxRedundantNode = true;
                arg_p += 1;
            }else if (args[arg_p].equals("-cw"))
            {
                if(args[arg_p+1].equals("inf"))
                    prioritizeClassification = true;
                else
                    classifyWeight = Integer.parseInt(args[arg_p + 1]);
                arg_p += 2;

            }else if (args[arg_p].equals("-rw"))
            {
                redundantWeight = Integer.parseInt(args[arg_p + 1]);
                arg_p += 2;
            }else if (args[arg_p].equals("-ew"))
            {
                emptyWeight = Integer.parseInt(args[arg_p + 1]);
                arg_p += 2;
            }else if (args[arg_p].equals("-bw"))
            {
                bddWeight = Integer.parseInt(args[arg_p + 1]);
                arg_p += 2;
            }else if (args[arg_p].equals("-nw"))
            {
                normalizeWeights = true;
                weightUnit = Double.parseDouble(args[arg_p + 1]);
                arg_p += 2;
            }//else if (args[arg_p].equals("-pr_c"))
            //{
            //    prioritizeClassification = true;
            //    arg_p += 1;
            //}
            else if (args[arg_p].equals("-beadUB"))
            {
                beadCard = true;
                beadUB = Integer.parseInt(args[arg_p + 1]);
                arg_p += 2;
            }
            else if (args[arg_p].equals("-bdd"))
            {
                minBDD = true;
                arg_p += 1;
            }
            else if (args[arg_p].equals("-p_bdd"))
            {
                postMinBDD = true;
                arg_p += 1;
            }else if (args[arg_p].equals("-p_bdd_l"))
            {
                postMinBDD = true;
                postMinBDDLimited = true;
                arg_p += 1;
            }else if (args[arg_p].equals("-p_pl"))
            {
                postParentLabel = true;
                arg_p += 1;
            }else if (args[arg_p].equals("-p_ml"))
            {
                postMergeLabel = true;
                arg_p += 1;
            }
            else if (args[arg_p].equals("-me"))
            {
                maxEmpties = true;
                arg_p += 1;
            }else if (args[arg_p].equals("-fs"))
            {
                forceSplit = true;
                arg_p += 1;
            }else if (args[arg_p].equals("-readOnce"))
            {
                readOnce = true;
                arg_p += 1;
            }
            else if (args[arg_p].equals("-min-depth"))
            {
                minDepthObj = true;
                arg_p += 1;
            }else if (args[arg_p].equals("-minisat"))
            {
                minisat = true;
                arg_p += 1;
            }else if (args[arg_p].equals("-mp"))
            {
                modelPresence = true;
                arg_p += 1;
            }else if (args[arg_p].equals("-fl"))
            {
                fixLabels = true;
                arg_p += 1;
            }

            System.out.println(arg_p);
        }

        if(multDimFeatures)
        {
            if(multSize > 0)
            {
                if(depth % multSize == 0)
                    featureDims = new int[depth/multSize];
                else
                    featureDims = new int[depth/multSize+1];

                for(int h=0; h<featureDims.length; h++)
                        featureDims[h] = multSize;

                if(depth % multSize > 0)
                    featureDims[0] = depth % multSize;

            }else if(multSize == 0)
            {
                int temp=0;
                int temp_cnt=1;
                while(temp < depth)
                {
                    temp += temp_cnt;
                    temp_cnt++;
                }
                featureDims = new int[temp_cnt-1];
                for(int h=0; h<featureDims.length-1; h++)
                    featureDims[h] = h+1;
                featureDims[featureDims.length-1] = featureDims.length + depth - temp;
            }else if(multSize == -1)
            {
                int halfSize = depth/2;

                featureDims = new int[depth - halfSize + 1];
                featureDims[0] = halfSize;
                for(int h=1; h<featureDims.length; h++)
                    featureDims[h] = 1;
            }
            System.out.print("Multi features dimensions:");
            for(int h=0; h<featureDims.length; h++)
                System.out.print(" " + featureDims[h]);
            System.out.println();
        }

        if((diagram) && depth % 2 == 1)
        {
            System.out.println("Invalid depth for diagram or mult");
            return;
        }

        if(diagram)
            leaves_cnt_log = depth/2;
        else if(multDimFeatures)
            leaves_cnt_log = featureDims.length;
        else
            leaves_cnt_log = depth;

        leaves_cnt = (int) Math.pow(2, leaves_cnt_log);
        System.out.println("leaves count: " + leaves_cnt);



        name = args[args.length - 1];
        Util.readInstance(name); // reads the dataset
        if(testRatio > 0.0 || test5Fold >= 0)
            Util.divideDataset();
        Util.calculateSorts();

        if(fixLabels)
        {
            Random random = new Random(Main.seed);
            fixedLabels = new boolean[leaves_cnt][];
            for(int i=0; i<fixedLabels.length; i++)
            {
                fixedLabels[i] = new boolean[k];
                fixedLabels[i][random.nextInt(k)] = true;
            }
        }


        Solver.initiate();
        VarsAndCons.initiate();
        if(prioritizeClassification)
        {
            classifyWeight = 1;
            if(maxEmpties)
                classifyWeight += leaves_cnt * emptyWeight;
            if(maxRedundantNode)
                classifyWeight += VarsAndCons.cntB * redundantWeight;
            if(minBDD)
                classifyWeight += VarsAndCons.cntB * bddWeight; // I think the count is the same as maxRedundantNode
        }else if(normalizeWeights)
        {
//            System.out.println("weights");
//            System.out.println(classifyWeight);
//            System.out.println(Main.n);
//            System.out.println(weightUnit);
//            System.out.println(classifyWeight/Main.n/weightUnit);
//            System.out.println((int) (classifyWeight/Main.n/weightUnit));
            classifyWeight = (int) (1.0*classifyWeight/Main.n/weightUnit);
            emptyWeight = (int) (1.0*emptyWeight/leaves_cnt/weightUnit);
            redundantWeight = (int) (1.0*redundantWeight/VarsAndCons.cntB/weightUnit);
            bddWeight = (int) (1.0*bddWeight/VarsAndCons.cntB/weightUnit);
        }
        if(orderedSelection)
            VarsAndCons.addConA2(0);
        else
            VarsAndCons.addConA(0);
        if(readOnce)
            VarsAndCons.addConReadOnce();
        if(!fixLabels)
            VarsAndCons.addConC(0);
        VarsAndCons.addConLeaves();
        if(modelDirection)
            VarsAndCons.addConSplit();
        if(pruneLabels || maxRedundantNode || minBDD)
            VarsAndCons.addConLabels();
        if(diagram)
            VarsAndCons.addConStructure();
        if(multDimFeatures)
            VarsAndCons.addConDims();
        if(beadCard)
            VarsAndCons.addConBeadUB();
        VarsAndCons.addConObj();
        Solver.solve(); // solve the instance

        if(postMinBDD)
        {
            postBBDPhase = true;
            bddWeight = 1;
            minBDD = false;
            maxEmpties = false;
            pruneLabels = false;
            maxRedundantNode = false;
            fixLabels = false;
            if(diagram)
            {
                diagram = false;
                leaves_cnt = (int) Math.pow(2, depth);
                leaves_cnt_log = depth;
            }
            if(multDimFeatures)
            {
                multDimFeatures = false;
                leaves_cnt = (int) Math.pow(2, depth);
                leaves_cnt_log = depth;
            }
            Solver.initiate();
            VarsAndCons.initiate();
            VarsAndCons.addConC(0);
            VarsAndCons.addConFix();
            VarsAndCons.addConLabels();
            Solver.solve();
        }

        System.out.println("Time_Before_Print: " + (System.currentTimeMillis() - startTime));
        Solution.tree.print();
        int train_mis = Solution.tree.misclassificationCost(data, labels);
        System.out.println("Training_mis: " + train_mis + "\nout of " + n);
        System.out.println("Training_acc: " + (1.0*train_mis/n));
        if(testRatio > 0.0 || test5Fold >= 0)
        {
            int test_mis = Solution.tree.misclassificationCost(testData, testLabels);
            System.out.println("Testing_mis: " + test_mis + "\nout of " + testData.length);
            System.out.println("Testing_acc: " + (1.0*test_mis/testData.length));
        }

        //if(printResult)
        //    Solution.printSolution(); // prints the solution and the metrics

        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
    }



}