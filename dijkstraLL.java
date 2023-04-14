import java.io.BufferedReader;
import java.io.FileReader;

public class dijkstraLL {
    static class Node{
        int destination;
        int data;
        Node next;


        Node(int x, int y){
            destination = x;
            data = y;
            next = null;
        }
    }


    static class NodeStart{
        int vertex;
        Node next;
        NodeStart down;


        NodeStart(int x){
            vertex = x;
            next = null;
            down = null;
        }
    }


    //this inserts a node to the right of nodestart in order
    public static void insertRight(NodeStart x, int destination, int data){
        Node new_node = new Node(destination, data);

        //base case: nodestart with no nodes
        if(x.next == null){
            x.next = new_node;
            return;
        }

        //pointer to traverse nodes
        Node temp = x.next;

        //base case: need to insert new node at head
        if(temp.destination > destination){
            x.next = new_node;
            new_node.next = temp;
            return;
        }

        while(temp != null){
            if(temp.next == null){
                temp.next = new_node;
                return;
            }
            else if(temp.next.destination > destination){
                new_node.next = temp.next;
                temp.next = new_node;
                return;
            }
            temp = temp.next;
        }
    }
   
    public static void insertDown(NodeStart x, int vertex){
        NodeStart new_node_start = new NodeStart(vertex);


        if(x.down == null){
            if(x.next == null){
                x.vertex = vertex;
                return;
            }
            x.down = new_node_start;
            return;
        }


        NodeStart temp = x;
        while(temp != null){
            if(temp.down == null){
                temp.down = new_node_start;
                return;
            }
            else if(temp.down.vertex > vertex){
                new_node_start.down = temp.down;
                temp.down = new_node_start;
                return;
            }
            temp = temp.down;
        }
    }


    public static boolean hasNodeStart(NodeStart temp, int vertex){
        while(temp != null){
            if(temp.vertex == vertex)
                return true;
            temp = temp.down;
        }
        return false;
    }


    public static boolean hasNode(NodeStart head, int destination){
        if(head.next == null)
            return false;


        Node temp = head.next;
       
        while(temp != null){
            if(temp.destination == destination)
                return true;
            temp = temp.next;
        }
        return false;
    }


    public static NodeStart getNodeStart(NodeStart temp, int vertex){
        //System.out.println("Searching: " + temp.vertex);
        if(temp.vertex == vertex)
            return temp;
        return getNodeStart(temp.down, vertex);
    }


    public static Node getNode(Node temp, int destination){
        if(temp == null)
            return null;
        if(temp.destination == destination)
            return temp;
        return getNode(temp.next, destination);
    }


    public static int getWeight(Node temp, int destination){
        if(temp == null){
            return 0;
        }
        if(temp.destination == destination)
            return temp.data;
        return getWeight(temp.next, destination);
    }

    public static int getMaxVertex(NodeStart temp){
        if(temp == null)
            return 0;
        if(temp.down == null)
            return temp.vertex + 1;
        return getMaxVertex(temp.down);
    }

    public static NodeStart graphMaker(int fileIndex){
        NodeStart head = new NodeStart(0);


        try{
            BufferedReader br = new BufferedReader(new FileReader("Project2_Input_File" + fileIndex + ".csv"));
            String line = br.readLine();
            line = br.readLine();
            while(line != null){
                String[] values = line.split(",");


                if(!hasNodeStart(head, Integer.parseInt(values[0])))
                    insertDown(head, Integer.parseInt(values[0]));


                NodeStart temp = getNodeStart(head, Integer.parseInt(values[0]));
                insertRight(temp, Integer.parseInt(values[1]), Integer.parseInt(values[2]));


                line = br.readLine();
            }
            br.close();


        }catch(Exception e){
            System.out.println("oh no");
        }
        return head;
    }

    public static void printPairs(int start, int[] distances, int[] path){
        System.out.print("Vertex\t Distance\tPath");
        for(int x = 0; x < distances.length; x++){
                if(x != start){
                    System.out.print("\n" + start + "->" + x + "\t\t" + distances[x] + "\t\t");
                    printPath(x, path);
            }
        }
    }

    public static void printPath(int curr, int[] path){
        if(curr == -1)
            return;

        printPath(path[curr], path);
        System.out.print(curr + " ");
        
    }

    public static void dijkstra(NodeStart head){
        int vertices = getMaxVertex(head);
        int percent = 0;
        for(int i = 0; i < vertices; i++){
            if(i * 100 / vertices > percent){
                percent++;
                System.out.print(percent + "%, ");
            }
            dijkstra(head, i, vertices);
        }
    }
    
    public static void dijkstra(NodeStart head, int start, int vertices){
        int[] shortestDistances = new int[vertices];
        boolean[] traveled = new boolean[vertices];

        for(int i = 0 ; i < vertices; i++){
            shortestDistances[i] = Integer.MAX_VALUE;
            traveled[i] = false;
        }

        shortestDistances[start] = 0;
        int[] path = new int[vertices];

        path[start] = -1;

        for(int i = 1; i < vertices; i++){
            int nearestVertex = -1;
            int shortestDistance = Integer.MAX_VALUE;
            for(int vertexIndex = 0; vertexIndex < vertices; vertexIndex++){
                if(!traveled[vertexIndex] && shortestDistances[vertexIndex] <= shortestDistance){
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                }
            }

            //System.out.println("Broke out of the loop, vertexIndex is " + nearestVertex);
            traveled[nearestVertex] = true;

            for(int vertexIndex = 0; vertexIndex < vertices; vertexIndex++){
                //System.out.println("Coordinates: (" + nearestVertex + ", " + vertexIndex + ")");
                int edgeDistance = getWeight(getNodeStart(head, nearestVertex).next, vertexIndex);

                if(edgeDistance > 0 && ((shortestDistance + edgeDistance) < shortestDistances[vertexIndex])){
                    path[vertexIndex] = nearestVertex;
                    shortestDistances[vertexIndex] = shortestDistance + edgeDistance;
                }
            }
        }
        //printPairs(start, shortestDistances, path);
    }


    public static void printGraph(NodeStart temp){    
        while(temp != null){
            System.out.print(temp.vertex + ": ");
            Node temp2 = temp.next;
            while(temp2 != null){
                System.out.print("[" + temp2.destination + ", " + temp2.data + "] ");
                temp2 = temp2.next;
            }
            System.out.println();
            temp = temp.down;
        }
    }

    public static void runAlgorithm(){
        long memory_before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long[] times = new long[16];

        for(int i = 1; i <= 15; i++){
            System.out.println("Running Algorithm on Input File " + i + " at " + java.time.LocalTime.now());
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

    public static void main(String[] args){
        runAlgorithm();
        //dijkstra(graphMaker(1));
    }
}
