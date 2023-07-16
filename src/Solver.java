import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Solver
{
    public static void initiate()
    {
        Main.hardClauses = new ArrayList<>();
        Main.softClauses = new ArrayList<>();
        Main.softClausesWeights = new ArrayList<>();
        Main.var_cnt = 0;
        Main.timedout = true;
        Main.solutionCost = -1;
        Main.unknownSolution = false;
        Main.output = "";
    }
    public static void solve() throws Exception
    {
        int timeout = Main.timeout;

        long init_time = System.currentTimeMillis();

        String clauseName = "clauses/clauses_" + Main.name + Main.postfix;
        if(Main.postBBDPhase)
            clauseName = clauseName + "_s2";
        String solutionName = "solutions/solution_" + Main.name + Main.postfix;
        if(Main.postBBDPhase)
            solutionName = solutionName + "_s2";
        String logName = "logs/log_" + Main.name + Main.postfix;
        if(Main.postBBDPhase)
            logName = logName + "_s2";
        String modelName = "models/model_" + Main.name + Main.postfix;
        if(Main.postBBDPhase)
            modelName = modelName + "_s2";

        writeClauses(clauseName);

        ProcessBuilder processBuilder = new ProcessBuilder();
        if(Main.minisat)
            processBuilder.command("bash", "-c", "timeout " + timeout + "m minisat " + clauseName + " " + modelName);
        //else if(Main.pysat)
        //    processBuilder.command("bash", "-c", "python3 glu.py " + clauseName + " " + timeout);
        else
            processBuilder.command("bash", "-c", "timeout " + timeout + "m " + Main.loandraPath + " -pmreslin-cglim=30 -weight-strategy=1 -print-model " + clauseName);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        FileWriter fileWriter = new FileWriter(logName);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        Main.timedout = true;
        Main.solutionCost = -1;
        Main.unknownSolution = false;
        Main.output = "";

        String line;
        String allLines = "";
        if(Main.minisat)
        {
            while ((line = reader.readLine()) != null)
            {
                allLines = allLines + line + "\n";
                printWriter.write(line + "\n");
            }
            File model = new File(modelName);
            Scanner scanner = new Scanner(model);
            scanner.nextLine();
            Main.output = Util.getAssignment(scanner.nextLine().split(" "));
        }
        else
            while ((line = reader.readLine()) != null)
            {
                allLines = allLines + line + "\n";
                printWriter.write(line + "\n");

                if(line.charAt(0) == 'v')
                    Main.output = Util.getAssignment(line.substring(2).split(" "));
                if(line.charAt(0) == 's')
                {
                    if(line.split(" ")[1].equals("UNKNOWN"))
                    {
                        System.out.println("Unknown Solution");
                        Main.unknownSolution = true;
                    }
                    if(line.split(" ")[1].equals("OPTIMUM") && line.split(" ")[2].equals("FOUND"))
                        Main.timedout = false;
                }
                if(line.charAt(0) == 'o')
                    Main.solutionCost = Integer.parseInt(line.split(" ")[1]);
                //if(line.charAt(0) == 'c' && line.split(" ").length > 4 && line.split(" ")[4].equals("LB:")) // does this only work for loandra?
                //    solutionLB = Integer.parseInt(line.split(" ")[5]);
            }
        printWriter.close();

        int exitVal = process.waitFor();
        if (exitVal == 0) {
            System.out.println("Solved!");
        }

        if(Main.solutionCost < 0)
            Main.timedout = false;

        if(Main.unknownSolution)
            System.out.println("Status: Unknown");
        else if(Main.timedout)
            System.out.println("Status: Timedout");
        else if(Main.solutionCost < 0)
            System.out.println("Status: Infeasible");
        else
            System.out.println("Status: Optimum");

        System.out.println("Solution Cost: " + Main.solutionCost);
        System.out.println("Timeout: " + timeout);
        //System.out.println("Solver_LB: " + solutionLB);
        System.out.println("Solver_Time: " + (System.currentTimeMillis() - init_time));

        if(Main.solutionCost < 0 && !Main.minisat)
        {
            System.out.println("Time_Before_Print: " + (System.currentTimeMillis() - Main.startTime));
            System.out.println(allLines);
            System.exit(1);
        }
//        else if(Main.postBBDPhase)
//        {
//            Solution.writeSolutionBDD(solutionName, Main.output, true);
//            Solution.findBeads();
//        }
        else
        {
            Solution.writeSolution(solutionName, Main.output, true);
            if(Main.diagram && !Main.postBBDPhase)
                Solution.printDiag();
            else if(Main.multDimFeatures && !Main.postBBDPhase)
                Solution.printMult();
            else
                Solution.printNonDiag();
        }
    }

    public static void writeClauses(String name) throws Exception
    {
        FileWriter fileWriter = new FileWriter(name);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        int weights_sum = 1;

        for(int i=0; i<Main.softClauses.size(); i++)
            weights_sum += Main.softClausesWeights.get(i);

        if(Main.minisat)
            printWriter.write("p cnf " + (Main.var_cnt) + " " + (Main.hardClauses.size() + Main.softClauses.size()) + "\n");
        else
            printWriter.write("p wcnf " + (Main.var_cnt) + " " + (Main.hardClauses.size() + Main.softClauses.size()) + " " + weights_sum + "\n");

        int lenSum = 0;

        for(int i=0; i<Main.softClauses.size(); i++)
        {
            String to_write = "";

            if(!Main.minisat)
                to_write = "" + Main.softClausesWeights.get(i) + " ";

            for(int j=0; j<Main.softClauses.get(i).length; j++)
                to_write = to_write + Main.softClauses.get(i)[j] + " ";
            to_write = to_write + "0\n";
            printWriter.write(to_write);
            lenSum += Main.softClauses.get(i).length;
        }

        for(int i=0; i<Main.hardClauses.size(); i++)
        {
            String to_write = "";

            if(!Main.minisat)
                to_write = "" + weights_sum + " ";

            for(int j=0; j<Main.hardClauses.get(i).length; j++)
                to_write = to_write + Main.hardClauses.get(i)[j] + " ";
            to_write = to_write + "0\n";
            printWriter.write(to_write);
            lenSum += Main.hardClauses.get(i).length;
        }


        System.out.println("Number_of_variables: " + (Main.var_cnt));
        System.out.println("Number_of_hard_clauses: " + (Main.hardClauses.size()));
        System.out.println("Number_of_soft_clauses: " + (Main.softClauses.size()));
        System.out.println("Clause_average_length: " + ((double) lenSum) / (Main.hardClauses.size() + Main.softClauses.size()));

        printWriter.close();
    }
}
