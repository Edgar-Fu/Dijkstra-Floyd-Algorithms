import java.io.BufferedReader;
import java.io.FileReader;

public class dijkstra2D{

    public static void runAlgorithm(){
        long memory_before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long[] times = new long[16];

        for(int i = 1; i <= 15; i++){
            
            System.out.println("Running Algorithm on Input File " + i);
            long startTime = System.nanoTime();
            dijkstra(graphMaker(i));
            long stopTime = System.nanoTime();
            times[i - 1] = stopTime - startTime;
            System.out.println("Input File " + i + " Completed");
            System.out.println("Runtime: " + (stopTime - startTime) + "\n");
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
    
    
    public static void dijkstra(int[][] graph){
        for(int i = 0; i < graph.length; i++){
            dijkstra(graph, i);
        }
    }
    
    
    public static void dijkstra(int[][] graph, int start){
        int[] shortestDistances = new int[graph[0].length];

        Boolean[] traveled = new Boolean[graph[0].length];
        
        for(int i = 0; i < graph[0].length; i++){
            shortestDistances[i] = Integer.MAX_VALUE;
            traveled[i] = false;
        }

        shortestDistances[start] = 0;

        int[] path = new int[graph[0].length];
        path[start] = -1;

        for(int i = 1; i < graph[0].length; i++){
            int nearestVertex = -1;
            int shortestDistance = Integer.MAX_VALUE;
            for(int vertexIndex = 0; vertexIndex < graph[0].length; vertexIndex++){
                if(!traveled[vertexIndex]){
                    //System.out.println("currently at " + i + " of " + graph[0].length);
                    //System.out.println("traveled[vertexIndex] = " + traveled[vertexIndex] + ", shortestDistances = " + shortestDistances[vertexIndex] + ", shortestDistance = " + shortestDistance);
                    //System.out.println("Vertexindex is " + vertexIndex);
                }
                if(!traveled[vertexIndex] && shortestDistances[vertexIndex] <= shortestDistance){
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                    //System.out.println("Flag raised");
                }
            }

            //System.out.println("Broke out of the loop, vertexIndex is " + nearestVertex);
            traveled[nearestVertex] = true;

            for(int vertexIndex = 0; vertexIndex < graph[0].length; vertexIndex++){
                int edgeDistance = graph[nearestVertex][vertexIndex];

                if(edgeDistance > 0 && ((shortestDistance + edgeDistance) < shortestDistances[vertexIndex])){
                    path[vertexIndex] = nearestVertex;
                    shortestDistances[vertexIndex] = shortestDistance + edgeDistance;
                }
            }
        }
        //if you'd like to test if my code works on the input files, use this
        //printPairs(start, shortestDistances, path);
    }

    public static void printPairs(int start, int[] distances, int[]path){
        System.out.println("Vertex\t Distance\tPath");
        for(int i = 0; i < distances.length; i++){
            if(i != start){
                System.out.print("\n" + start + "->" + i + "\t\t" + distances[i] + "\t\t");
                printPath(i, path);
            }
        }
    }


    public static void printPath(int curr, int[] path){
        if(curr == -1)
            return;

        printPath(path[curr], path);
        System.out.print(curr + " ");
        
    }

    //go through the file one time, finding the total size of the graph before actually creating it
    //I figure this would be much more efficient than remaking the entire array every time it needs to be bigger
    public static int[][] graphMaker(int fileIndex){
        int graphSize = 0;
        try{
            BufferedReader br = new BufferedReader(new FileReader("Project2_Input_File" + fileIndex + ".csv"));
            String line = br.readLine();
            line = br.readLine();
            while(line != null){
                String[] values = line.split(",");
                if(graphSize < Math.max(Integer.parseInt(values[0]), Integer.parseInt(values[1])))
                    graphSize = Math.max(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                line = br.readLine();
            }
            br.close();

        }catch(Exception e){
            System.out.println("oh no");
        }

        return graphPopulate(fileIndex, graphSize);
    }

    
    public static int[][] graphPopulate(int fileIndex, int graphSize){
        int[][] graph = new int[graphSize + 1][graphSize + 1];

        try{
            BufferedReader br = new BufferedReader(new FileReader("Project2_Input_File" + fileIndex + ".csv"));
            String line = br.readLine();
            line = br.readLine();
            while(line != null){
                String[] values = line.split(",");
                graph[Integer.parseInt(values[0])][Integer.parseInt(values[1])] = Integer.parseInt(values[2]);
                //graph[Integer.parseInt(values[1])][Integer.parseInt(values[0])] = Integer.parseInt(values[2]);
                line = br.readLine();
            }
            br.close();

        }catch(Exception e){
            System.out.println("oh no");
        }
        
        return graph;
    }

    public static void printGraph(int[][] graph){
        System.out.println("Printing the Graph:");
        for(int i = 0; i < graph.length; i++){
            System.out.print(i + ": ");
            for(int j = 0; j < graph[0].length; j++){
                System.out.print(graph[i][j] + " ");
            }
            System.out.println();
        }
    }
    

    public static void main(String[] args){        
        
        runAlgorithm();

        //dijkstra(graphMaker(1));
    }
}
