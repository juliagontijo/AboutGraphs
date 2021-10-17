import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.LinkedList;
import java.io.*;
import java.util.*;

public class tp2 {
    public static Scanner in = new Scanner(System.in);
    private static int numNodes; //number of nodes
    private static int numVertex; //number of vertex
    private static LinkedList<Integer> no; //list of nodes in the graph
    private static LinkedList<Aresta> vertex; //list of vertexes in the graph

    //Permutation helpers
    static LinkedList<LinkedList<Integer>> ans;

    public static void main(String[] args){
        String entry = in.nextLine();
        //manipulate entry and define nodes and vertices from it
        readEntry(entry);

        //Matriz de Adjacência
        sortVertexA();
        System.out.println("\n-- Matriz de Adjacência --\n");
        MatrixAdjacency matrixA = new MatrixAdjacency(numNodes);
        matrixA.fillMatrixA(vertex, numNodes);
        matrixA.printMatrixA(numNodes);


        //COUNT CYCLES FROM PERMUTATION
        System.out.println("\nQuantidade de ciclos por permutação: "+countCyclesPermutation(matrixA));


        //COUNT CYCLES FROM DFS
        System.out.println("Quantidade de ciclos por caminhamento: "+findCyclesDFS());

    }

    public static void readEntry(String entry){
        entry = replace(entry);
        defineNodes(entry);
        defineVertex(entry);
        sortVertexA();
        printVertex();
        printNodes();
    }

    //retrive anything unecessary from string entrance
    public static String replace(String entry){
        entry = entry.replace("{", "");
        entry = entry.replace("(", "");
        entry = entry.replace(")", "");
        entry = entry.replace("}", "");
        entry = entry.replace(" ", "");
        return entry;
    }

    public static int getnumVertex(String entry){
        //store each vertex (separated in the string with ;) in a vector
        String seperatedVertex[] = entry.split(";");
        return seperatedVertex.length;
    }

    //store all nodes in "no" list and define its amount
    public static void defineNodes(String entry){
        no = new LinkedList<Integer>();
        entry = entry.replaceAll(";", ",");
        String[] nos = entry.split(",");
        for(int i=0; i<nos.length; i++){
                if(!no.contains(Integer.parseInt(nos[i])))
                    no.add(Integer.parseInt(nos[i]));
        }
        Collections.sort(no);
        numNodes = no.size();
    }

    //store all vertex in "vertex" list and define its amount
    public static void defineVertex(String entry){
        vertex = new LinkedList<Aresta>();
        numVertex = getnumVertex(entry);
        entry = entry.replaceAll(";", ",");
        String[] nos = entry.split(",");
        for(int i=0; i<nos.length; i+=2){
                vertex.add(new Aresta(Integer.parseInt(nos[i]), Integer.parseInt(nos[i+1])));
        }
    }

    public static void printVertex(){
        System.out.println("\nPRINT ARESTAS\n");
        for(int i=0; i<vertex.size(); i++){
             System.out.println("("+vertex.get(i).getA()+","+vertex.get(i).getB()+")");
        }
    }

    public static void printNodes(){
        System.out.println("\nPRINT NOS\n");
        for(int i=0; i<no.size(); i++){
            System.out.println("NO [" + i + "] = ("+no.get(i)+")");
        }
    }

    //Sort vertex from it's first node (atribute A in Arestas class)
    public static void sortVertexA(){
        for (int i = 0; i < (vertex.size() - 1); i++) {
            int min = i;
            for (int j = (i + 1); j < vertex.size(); j++){
                int comp = ((vertex.get(min).getA()) > (vertex.get(j).getA())) ? 1:0;
               if (comp>0){
                  min = j;
               }
            }
            swap(min, i);
         }
    }

    //Sort vertex from it's second node (atribute B in Arestas class)
    public static void sortVertexB(){
        for (int i = 0; i < (vertex.size() - 1); i++) {
            int min = i;
            for (int j = (i + 1); j < vertex.size(); j++){
                int comp = ((vertex.get(min).getB()) > (vertex.get(j).getB())) ? 1:0;
               if (comp>0){
                  min = j;
               }
            }
            swap(min, i);
         }
    }
  
    public static void swap(int i, int j) {
        Aresta temp = new Aresta(vertex.get(i).getA(),vertex.get(i).getB());
        vertex.get(i).setA(vertex.get(j).getA());
        vertex.get(i).setB(vertex.get(j).getB());
        vertex.get(j).setA(temp.getA());
        vertex.get(j).setB(temp.getB());
    }

    //returns all the cycles counted in the graph
    public static int countCyclesPermutation(MatrixAdjacency matrixA){
        int numCycles=0;

        //combination of all nodes 3 at a time (because 3 is the minimum amount of nodes so a cycle can exist)
        ans = combine(no.size(), 3);
        //OBS: 
        //do 3 seperated from the rest of nodes combinations because with only 3 we don't need to do permutation 
        //(if it is a cycle, all it's permutations correspond to the same cycle)

        //check if each combination 3 at a time is cycled
        for(int i=0; i<ans.size(); i++)
            if(isCycled(ans.get(i), matrixA))
                numCycles++;

        //combination of all nodes k at a time (from 4 to the amount of nodes)
        for(int k=4; k<=no.size(); k++){
            ans = combine(no.size(), k);
            for(int i=0; i<ans.size(); i++){
                //all the permutations from each combination
                LinkedList<LinkedList<Integer>> permutations = getPermutations(ans.get(i), 0, k-1, matrixA);
                //given all the permutations of that combination of nodes, check which ones are real cycles, and which cyles are actualy different from each other
                numCycles += getQuantCyclesThroughPermutationAnalysis(matrixA, permutations);
            }
        }
        return numCycles;
    }

    //Combination of all n nodes, k at a time
    public static LinkedList<LinkedList<Integer>> combine(int n, int k){
        LinkedList<LinkedList<Integer>> xx = new LinkedList();
        if(k==0){
            xx.add(new LinkedList());
            return xx;
        }
        backtrack(1, new LinkedList(), n, k, xx);
        return xx;
    }

    public static void backtrack(int start, LinkedList<Integer> current, int n, int k, LinkedList<LinkedList<Integer>> xx){
        if(current.size() == k){
            xx.add(new LinkedList(current));
        }

        for(int i=start; i<=n && current.size()<k; i++){
            current.add(i);
            backtrack(i+1, current, n, k, xx);
            current.removeLast();
        }
    }
    
    //Check if a sequence of nodes forms a cycle in the graph (analyzing the adjacency matrix)
    public static boolean isCycled(LinkedList<Integer> current, MatrixAdjacency matrixA){
        for(int i=0; i<current.size()-1; i++){
            if(matrixA.get((current.get(i)-1),(current.get(i+1)-1)) == 0 && matrixA.get((current.get(i+1)-1),(current.get(i)-1)) == 0)
                return false;
        }

        if(matrixA.get((current.get(current.size()-1)-1),(current.get(0)-1)) == 0 && matrixA.get((current.get(0)-1),(current.get(current.size()-1)-1)) == 0)
            return false;
        return true;
    }

    //Permutation -> returns a list of lists of all permutations given a combination of nodes
    public static LinkedList<LinkedList<Integer>> getPermutations(LinkedList<Integer> current, int i, int j, MatrixAdjacency matrixA){
        LinkedList<LinkedList<Integer>> permutations = new LinkedList();
        permute(current, i, j, matrixA, permutations);
        return permutations;
    }

    public static void permute(LinkedList<Integer> current, int i, int j, MatrixAdjacency matrixA, LinkedList<LinkedList<Integer>> permutations){
        if (i == j){
            permutations.add(new LinkedList(current));
        }
        else
        {
            for (int k = i; k <= j; k++)
            {
                current = swap(current,i,k);
                permute(current, i+1, j, matrixA, permutations);
                current = swap(current,i,k);
            }
        }
    }

    public static LinkedList<Integer> swap(LinkedList<Integer> current, int i, int j){
        LinkedList<Integer> temp = current;
        Collections.swap(temp, i, j);
        return temp;
    }

    //Returns the amount of valid and diferent cycles are in the generated permutations
    public static int getQuantCyclesThroughPermutationAnalysis(MatrixAdjacency matrixA, LinkedList<LinkedList<Integer>> permutations){
        //stores repeated permutations that may appear, that we need to discard
        //such as: 1234, 2341, 3412 and 4123, that are all cyclic but are all the same cycle
        LinkedList<LinkedList<Integer>> repeatedPermutationCycles = new LinkedList<>();
        //stores the valid diferent from each other cycles, once it's a cycle and it's not in the "repeatedPermutationCycles" list
        LinkedList<LinkedList<Integer>> validPermutationCycles = new LinkedList<>();
        //count the valid different cycles
        int cont=0;

        for(LinkedList<Integer> p : permutations){
            if(isCycled(p, matrixA) && (!repeatedPermutationCycles.contains(p))){
                validPermutationCycles.add(p);
                //add p's repeated cycles so we can avoid adding them to the "validPermutationCycles"
                repeatedPermutationCycles = addPsPepeatedCycles(p,repeatedPermutationCycles);
                cont++;
            }
        }
        return cont;
    }

    public static LinkedList<LinkedList<Integer>> addPsPepeatedCycles(LinkedList<Integer> p, LinkedList<LinkedList<Integer>> repeatedPermut){
        LinkedList<Integer> reverse = new LinkedList<>();
        //get reversed p -> Example: if p = {1,2,3,4}; reverse = {4,3,2,1};
        for(int i=p.size()-1; i>=0; i--){
            reverse.add(p.get(i));
        }
        //get all cyclic permutations (that referes to the same cycle in a graph)
        //Example: 1234; 2341; 3412; 4123 from p
        for(int i=0; i<p.size(); i++){
            LinkedList<Integer> aux = new LinkedList<>();
            for(int first = i+1; first<p.size(); first++){
                aux.add(p.get(first));
            }
            for(int second = 0; second <=i; second ++){
                aux.add(p.get(second));
            }
            repeatedPermut.add(aux);
        }

        //now the same with reverse
        //Example: 4321; 1432; 2143; 3412 from reverse
        for(int i=0; i<p.size(); i++){
            LinkedList<Integer> aux = new LinkedList<>();
            for(int first = i+1; first<p.size(); first++){
                aux.add(reverse.get(first));
            }
            for(int second = 0; second <=i; second ++){
                aux.add(reverse.get(second));
            }
            repeatedPermut.add(aux);
        }
        return repeatedPermut;
    }
    
    //Count all cycles in a graph through DFS
    public static int findCyclesDFS(){
        findCycle p = new findCycle(vertex);
        return p.start();
    }
}

class findCycle{

    //  Graph modeled as list of edges
    public int[][] graph;

    public List<int[]> cycles = new ArrayList<int[]>();

    public findCycle(LinkedList<Aresta> arestas){
        this.graph = new int[arestas.size()][2];
        for(int i=0; i<arestas.size(); i++){
            this.graph[i][0] = arestas.get(i).getA();
            this.graph[i][1] = arestas.get(i).getB();
        }
    }

    public int start(){
        for (int i = 0; i < graph.length; i++)
            for (int j = 0; j < graph[i].length; j++){
                findNewCycles(new int[] {graph[i][j]});
            }

        return cycles.size();

        //printa todos os ciclos
        /*for (int[] cy : cycles){
            String s = "" + cy[0];
            for (int i = 1; i < cy.length; i++){
                s += "," + cy[i];
            }
            o(s);
        }*/
    }

    public void findNewCycles(int[] path){
            int n = path[0];
            int x;
            int[] sub = new int[path.length + 1];

            for (int i = 0; i < graph.length; i++)
                for (int y = 0; y <= 1; y++)
                    if (graph[i][y] == n)
                    //  edge refers to our current node
                    {
                        x = graph[i][(y + 1) % 2];
                        if (!visited(x, path))
                        //  neighbor node not on path yet
                        {
                            sub[0] = x;
                            System.arraycopy(path, 0, sub, 1, path.length);
                            //  explore extended path
                            findNewCycles(sub);
                        }
                        else if ((path.length > 2) && (x == path[path.length - 1]))
                        //  cycle found
                        {
                            int[] p = normalize(path);
                            int[] inv = invert(p);
                            if (isNew(p) && isNew(inv))
                            {
                                cycles.add(p);
                            }
                        }
                    }
    }

    //  check of both arrays have same lengths and contents
    public Boolean equals(int[] a, int[] b){
        Boolean ret = (a[0] == b[0]) && (a.length == b.length);

        for (int i = 1; ret && (i < a.length); i++){
            if (a[i] != b[i]){
                ret = false;
            }
        }

        return ret;
    }

    //  create a path array with reversed order
    public int[] invert(int[] path){
        int[] p = new int[path.length];

        for (int i = 0; i < path.length; i++){
            p[i] = path[path.length - 1 - i];
        }

        return normalize(p);
    }

    //  rotate cycle path such that it begins with the smallest node
    public int[] normalize(int[] path){
        int[] p = new int[path.length];
        int x = smallest(path);
        int n;

        System.arraycopy(path, 0, p, 0, path.length);

        while (p[0] != x){
            n = p[0];
            System.arraycopy(p, 1, p, 0, p.length - 1);
            p[p.length - 1] = n;
        }

        return p;
    }

    //  compare path against known cycles
    //  return true, iff path is not a known cycle
    public Boolean isNew(int[] path){
        Boolean ret = true;

        for(int[] p : cycles){
            if (equals(p, path))
            {
                ret = false;
                break;
            }
        }

        return ret;
    }

    public void o(String s){
        System.out.println(s);
    }

    //  return the int of the array which is the smallest
    public int smallest(int[] path){
        int min = path[0];

        for (int p : path){
            if (p < min)
            {
                min = p;
            }
        }

        return min;
    }

    //  check if vertex n is contained in path
    public Boolean visited(int n, int[] path){
        Boolean ret = false;

        for (int p : path){
            if (p == n){
                ret = true;
                break;
            }
        }

        return ret;
    }

}
