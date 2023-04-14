import java.io.BufferedReader;
import java.io.FileReader;

public class floyd2D {
    public static int[] key;
    public static boolean test = false;

    public static void floyd(int[][] graph){
        int i, j, k;
        int[][] path = new int[graph.length][graph[0].length];
        
        //set unconnected edges to a really high number
        for(int x = 0; x < graph.length; x++){
            for(int y = 0; y < graph.length; y++){
                if(graph[x][y] == 0 && x != y)
                    graph[x][y] = 9999;
            }
        }

        int percent = 0;
        for(k = 0; k < graph[0].length; k++){
            for(i = 0; i < graph[0].length; i++){
                for(j = 0; j < graph[0].length; j++){
                    //System.out.println("Searching coordinates (" + i + ", " + k + ")");
                    //System.out.println("Comparing " + graph[i][j] + " to " + (graph[i][k] + graph[k][j]));
                    if(!test && k * 100 / graph[0].length > percent){
                        percent++;
                        System.out.print(percent + "%, ");
                    }
                    if(graph[i][k] + graph[k][j] < graph[i][j]){
                        //System.out.println("Flag raised, " + k + " added to path");
                        path[i][j] = k;
                        graph[i][j] = Math.min(graph[i][j], (graph[i][k] + graph[k][j]));
                    }
                    
                }
            }
        }

        //if you'd like to test if my code works on the input files, use this
        //printPairs(graph, path);

        if(test)
            printTest(graph, path);
    }

    public static void printPairs(int[][] graph, int[][] path){
        System.out.print("Vertex\t Distance\tPath");
        for(int x = 0; x < graph.length; x++){
            for(int y = 0; y < graph.length; y++){
                if(x != y){
                    System.out.print("\n" + x + "->" + y + "\t\t" + graph[x][y] + "\t\t");
                    System.out.print(x + " ");
                    printPath(path, x, y);
                    System.out.print(y);
                }
            }
        }
    }

    public static void printTest(int[][] graph, int[][] path){
        int startNode = 0;
        int endNode = 0;
        //setting start and end nodes based on the test case
        for(int i = 0; i < key.length; i++){
            //System.out.print(key[i] + " ");
            if(key[i] == 192){
                startNode = 192;
                endNode = 163;
            }
            else if(key[i] == 138){
                startNode = 138;
                endNode = 66;
            }
        }
        if(startNode == 0){
            startNode = 465;
            endNode = 22;
        }
        
        int q = 0;
        int r = 0;
        for(int i = 0; i < key.length; i++){
            //System.out.print(key[i] + " ");
            if(key[i] == startNode)
                q = i;
            if(key[i] == endNode)
                r = i;
        }
        
        System.out.println("Start Node: " + startNode);
        System.out.println("End Node: " + endNode);
        //printGraph(head);
        System.out.println("Distance: " + graph[q][r]);
        System.out.print(startNode + "->");
        printPath(path, q, r);
        System.out.println(endNode + "\n");
        key = null;
        test = false;
    }

    //path printer
    public static void printPath(int[][] path, int q, int r){
        if(path[q][r] != 0){
            printPath(path, q, path[q][r]);
            if(test)
                System.out.print(key[path[q][r]] + "->");
            else
                System.out.print(path[q][r] + " ");
            printPath(path, path[q][r], r);
        }
    }

    public static void printGraph(int[][] graph){
        System.out.println("Printing Graph:");
        for(int i = 0; i < graph.length; i++){
            System.out.print(i + ": ");
            for(int j = 0; j < graph[0].length; j++){
                System.out.print(graph[i][j] + " ");
            }
            System.out.println();
        }
    }

    //go through the file one time, finding the total size of the graph before actually creating it
    //I figure this would be much more efficient than remaking the entire array every time it needs to be bigger
    public static int[][] graphMaker(int fileIndex){
        int graphSize = 0;

        /*
        I made alternate csv files for the test cases
            a file index of -1 is test case 1
            a file index of -2 is test case 2
            a file index of -3 is test case 3
        */

        String fileName;
        if(fileIndex < 0){
            fileName = "Test Case ";
            fileIndex *= -1;
            test = true;
            System.out.println(fileName + fileIndex);
        }
        else{
            fileName = "Project2_Input_File";
        }
        int counter = 0;
        try{
            BufferedReader br = new BufferedReader(new FileReader(fileName + fileIndex + ".csv"));
            String line = br.readLine();
            line = br.readLine();
            while(line != null){
                String[] values = line.split(",");
                
                if(!test && graphSize < Math.max(Integer.parseInt(values[0]), Integer.parseInt(values[1])))
                    graphSize = Math.max(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                else
                    counter++;
                    
                line = br.readLine();
            }
            br.close();

        }catch(Exception e){
            System.out.println("oh no");
        }

        if(test)
            graphSize = counter + 1;
        else
            graphSize++;

        return graphPopulate(fileName, fileIndex, graphSize);
    }

    
    public static int[][] graphPopulate(String fileName, int fileIndex, int graphSize){
        int[][] graph = new int[graphSize][graphSize];
        if(test)
            key = new int[graphSize];
    
        try{
            BufferedReader br = new BufferedReader(new FileReader(fileName + fileIndex + ".csv"));
            String line = br.readLine();
            line = br.readLine();
            String[] values = new String[3];
            int counter = 0;
            while(line != null){
                values = line.split(",");
                if(test){
                    key[counter] = Integer.parseInt(values[0]);
                    graph[counter][counter + 1] = Integer.parseInt(values[2]);
                    graph[counter + 1][counter] = Integer.parseInt(values[2]);
                }
                else
                    graph[Integer.parseInt(values[0])][Integer.parseInt(values[1])] = Integer.parseInt(values[2]);
                counter++;
                line = br.readLine();
            }
            
            if(test)
                key[counter] = Integer.parseInt(values[1]);

            br.close();

        }catch(Exception e){
            System.out.println("oh no");
        }
        return graph;
    }

    public static void runAlgorithm(){
        long memory_before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long[] times = new long[16];

        for(int i = 1; i <= 15; i++){
            System.out.println("Running Algorithm on Input File " + i);
            long startTime = System.nanoTime();
            floyd(graphMaker(i));
            long stopTime = System.nanoTime();
            times[i - 1] = stopTime - startTime;
            System.out.println("Input File " + i + " Completed");
        }
        long memory_after = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Memory: " + (memory_after - memory_before));

        System.out.println("Runtimes: ");
        for(int i = 0; i < 15; i++){
            System.out.print(times[i]);
            if(i < 14)
                System.out.print(" ");
        }
    }

    public static void runTests(){
        for(int i = -1; i >= -3; i--){
            floyd(graphMaker(i));
        }
    }

    public static void main(String[] args){


        //runTests();
        floyd(graphMaker(1));
        //runAlgorithm();
        
    }
}
