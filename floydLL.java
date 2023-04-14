import java.io.BufferedReader;
import java.io.FileReader;


public class floydLL {
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
   
    public static void insertDown(NodeStart temp, int vertex){
        NodeStart new_node_start = new NodeStart(vertex);
        //base case empty head
        if(temp == null){
            temp = new_node_start;
        }
        //base case insert at head
        if(temp.vertex > vertex){
            new_node_start.down = temp;
            temp = new_node_start;
        }

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

    public static void insertDownUnordered(NodeStart temp, int vertex){
        NodeStart new_node_start = new NodeStart(vertex);
        //base case empty head
        if(temp == null){
            temp = new_node_start;
        }
        
        while(temp != null){
            if(temp.down == null){
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

    public static void compute(NodeStart head, int destination, int data){
        Node new_node = new Node(destination, data);

        //base case no nodes
        if(head.next == null){
            head.next = new_node;
            return;
        }

        Node temp = head.next;

        //base case: need to insert in front
        if(temp.destination > destination){
            head.next = new_node;
            new_node.next = temp;
            return;
        }

        while(temp != null){
            //if we've reached the end of the list, insert it
            if(temp.next == null){
                temp.next = new_node;
                return;
            }
            //if there's no node at destinaion, insert one
            else if(temp.next.destination > destination){
                new_node.next = temp.next;
                temp.next = new_node;
                return;
            }
            //if there is a node at destination, update it
            else if(temp.next.destination == destination){
                temp.next.data = data;
                return;
            }

            temp = temp.next;
        }

    }

    public static NodeStart getNodeStart(NodeStart temp, int vertex){
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
            return 9999;
        }
        if(temp.destination == destination)
            return temp.data;
        return getWeight(temp.next, destination);
    }

    public static int getMaxVertex(NodeStart temp){
        return getMaxVertex(temp.down, 0);
    }

    public static int getMaxVertex(NodeStart temp, int counter){
        if(temp == null || temp.down == null)
            return counter;
        counter++;
        return getMaxVertex(temp.down, counter);
    }

    public static NodeStart graphMaker(int fileIndex){
        NodeStart head = new NodeStart(0);

        /*
        I made alternate csv files for the test cases
            a file index of -1 is test case 1
            a file index of -2 is test case 2
            a file index of -3 is test case 3
        */
        String filename;
        boolean test = false;
        if(fileIndex < 0){
            filename = "Test Case ";
            fileIndex *= -1;
            test = true;
            System.out.println(filename + fileIndex);
        }
        else{
            filename = "Project2_Input_File";
        }

        try{
            BufferedReader br = new BufferedReader(new FileReader(filename + fileIndex + ".csv"));
            String line = br.readLine();
            line = br.readLine();
            String[] values = new String[3];
            while(line != null){
                values = line.split(",");

                if(!hasNodeStart(head, Integer.parseInt(values[0])))
                    insertDown(head, Integer.parseInt(values[0]));

                NodeStart temp = getNodeStart(head, Integer.parseInt(values[0]));
                insertRight(temp, Integer.parseInt(values[1]), Integer.parseInt(values[2]));
                line = br.readLine();
            }

            //only in test cases is there a destination that doesn't have its own source row
            //this part adds the destination as a node
            if(test){
                insertDown(head, Integer.parseInt(values[1]));
                insertRight(getNodeStart(head, Integer.parseInt(values[1])), Integer.parseInt(values[0]), Integer.parseInt(values[2]));
                head = head.down;
            }
            br.close();
        }catch(Exception e){
            System.out.println("oh no");
        }
        return head;
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

    public static int[] keyMaker(NodeStart temp, int vertices){
        int[] key = new int[vertices];
        for(int x = 0; x < vertices; x++){
            key[x] = temp.vertex;
            temp = temp.down;
        }
        return key;
    }

    public static void floyd(NodeStart head, boolean test){
        int i, j, k;
        int vertices = getMaxVertex(head) + 2;

        int[][] path = new int[vertices][vertices];
        for(int x = 0; x < vertices; x++){
            for(int y = 0; y < vertices; y++){
                path[x][y] = -1;
            }
        }

        int[] key;
        if(test){
            key = keyMaker(head, vertices);
        }
        else{
            key = new int[vertices];
        }

        int percent = 0;

        NodeStart kStart = head;
        for(k = 0; k < vertices; k++){
            NodeStart iStart = head;
            for(i = 0; i < vertices; i++){
                NodeStart jStart = head;
                for(j = 0; j < vertices; j++){
                    if(k * 100 / vertices > percent && !test){
                        percent++;
                        System.out.print(percent + "%, ");
                    }
                    int i_k, k_j, i_j;
                        if(test){
                            i_k = getWeight(iStart.next, key[k]);
                            k_j = getWeight(kStart.next, key[j]);
                            i_j = getWeight(iStart.next, key[j]);
                            //System.out.println(i_k + " " + k_j + " " + i_j);
                        }
                        else{
                            i_k = getWeight(iStart.next, k);
                            k_j = getWeight(kStart.next, j);
                            i_j = getWeight(iStart.next, j);
                        }
                    if(i_k + k_j < i_j){
                        //System.out.println("Flag raised");
                        int data = Math.min(i_j, (i_k + k_j));
                        path[i][j] = k;
                        //System.out.println("Coordinaties: (" + i + ", " + j + ")");
                        if(test)
                            compute(iStart, key[j], data);
                        else
                            compute(iStart, j, data);
                    }
                   jStart = jStart.down;
                }
                iStart = iStart.down;
            }
            kStart = kStart.down;
        }
        //if you'd like to test if my code works on the input files, use this
        //printPairs(head, vertices, path);
        
        if(test)
            printTest(head, vertices, path);
    }


    public static void printPairs(NodeStart temp, int vertices, int[][] path){
        System.out.print("Vertex\t Distance\tPath");
        for(int x = 0; x < vertices; x++){
            for(int y = 0; y < vertices; y++){
                if(x != y){
                    int distance = getWeight(getNodeStart(temp, x).next, y);
                    System.out.print("\n" + x + "->" + y + "\t\t" + distance + "\t\t");
                    System.out.print(x + " ");
                    printPath(path, x, y);
                    System.out.print(y);
                }
            }
        }
        System.out.println(vertices);
    }

    public static void printTest(NodeStart head, int vertices, int[][] path){
        int startNode, endNode;
        //setting start and end nodes based on the test case
        if(hasNodeStart(head, 192)){
           startNode = 192;
           endNode = 163;
        }
        else if(hasNodeStart(head, 138)){
            startNode = 138;
            endNode = 66;
        }
        else{
            startNode = 465;
            endNode = 22;
        }

        System.out.println("Start Node: " + startNode);
        System.out.println("End Node: " + endNode);
        //printGraph(head);
        System.out.println("Distance: " + getWeight(getNodeStart(head, startNode).next, endNode));

        System.out.print("Path: ");

        //converting the nodes to the index via key
        int[] key = keyMaker(head, vertices);

        int q = 0;
        int r = 0;
        for(int i = 0; i < key.length; i++){
            //System.out.print(key[i] + " ");
            if(key[i] == startNode)
                q = i;
            if(key[i] == endNode)
                r = i;
        }
        //System.out.println("\nq = " + q + ", r = " + r);
        System.out.print(startNode + "->");
        printPath(path, q, r, key);
        System.out.println(endNode + "\n");

    }

    public static void printPath(int[][] path, int q, int r){
        if(path[q][r] != -1){
            printPath(path, q, path[q][r]);
            System.out.print(path[q][r] + " ");
            printPath(path, path[q][r], r);
        }
    }

    public static void printPath(int[][] path, int q, int r, int[] key){
        if(path[q][r] != -1){
            printPath(path, q, path[q][r], key);
            System.out.print(key[path[q][r]] + "->");
            printPath(path, path[q][r], r, key);
        }
    }

    public static void runAlgorithm(){
        long memory_before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long[] times = new long[16];
        int counter = 0;
        for(int i = 1; i <= 15; i++){
            System.out.println("Running Algorithm on Input File " + i + " at " + java.time.LocalTime.now());
            long startTime = System.nanoTime();
            floyd(graphMaker(i), false);
            long stopTime = System.nanoTime();
            times[counter] = stopTime - startTime;
            System.out.println("100%");
            System.out.println("Input File " + i + " Completed");
            System.out.println("Runtime: " + (stopTime - startTime) + "\n");
        }
        long memory_after = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Memory: " + (memory_after - memory_before));

        System.out.print("Runtimes: ");
        for(int i = 0; i < 15; i++){
            System.out.print(times[i] + ", ");
        }
    }

    public static void testCases(){
        for(int i = -1; i >= -3; i--){
            floyd(graphMaker(i), true);
        }
    }


    public static void main(String[] args){  
        System.out.println("Floyd's Algorithm Linked List\n");
        
        testCases();
        
        //runAlgorithm();
    }
}

