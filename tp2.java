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
    private static int quantNos;
    private static int quantArestas;
    private static LinkedList<Integer> no;
    private static LinkedList<Aresta> arestas;
    public static int quantCiclos;

    //Permutation helpers
    static LinkedList<LinkedList<Integer>> ans;
    //static LinkedList<LinkedList<Integer>> permutations;
    //static LinkedList<Integer> tmp; //only for the other way of pemrutation

    public static void main(String[] args){
        String entry = in.nextLine();
        readEntry(entry);

        //Matriz de Adjacência
        ordenaArestasA();
        System.out.println("\n-- Matriz de Adjacência --\n");
        MatrixAdjacency matrixA = new MatrixAdjacency(quantNos);
        matrixA.fillMatrixA(arestas, quantNos);
        matrixA.printMatrixA(quantNos);


        //CONTA CICLOS POR PERMUTAÇÃO
        quantCiclos = 0;
        //combinação dos nós de k em k (começando por 3 pois é o mínimo para acontecer um ciclo em um grafo)
        for(int k=3; k<=no.size(); k++){
            //pega as combinações existentes de k em k
            ans = combine(no.size(), k);
            for(int i=0; i<ans.size(); i++){
                System.out.println("\nCombinação: "+ ans.get(i));

                //verificar se cada combinação faz ciclo
                if(isCycled(ans.get(i), matrixA)){ //SE SIM: contabilizar
                    quantCiclos++;
                    System.out.println("Faz ciclo");
                }else{ //SE NAO: permutar a combinação e verificar se algum permutado faz ciclo
                    System.out.println("Não faz ciclo\n\nTESTANDO PERMUTAÇÕES");
                        //todas as permutações exisntentes com os números da combinação em questão
                        LinkedList<LinkedList<Integer>> permutations = getPermutations(ans.get(i), 0, k-1, matrixA);
                            //dentro das permutações, aquelas que formam ciclos válidos e diferentes um dos outros
                            //LinkedList<LinkedList<Integer>> validPermutationsCycles;
                            //quantCiclos += getValidPermutations(permutations, matrixA);
                    if(anyPermutationIsCycled(permutations, matrixA)){
                        quantCiclos++;
                        System.out.println("\n\nQuant ciclos: "+quantCiclos+"\n");
                    }else{
                        System.out.println("\n\nNenhuma faz ciclo\n\n");
                    }
                }
        
            }
            System.out.println("\nQuantidade de ciclos: "+quantCiclos+"\n");
        }

        System.out.println("Quantidade de ciclos: "+quantCiclos);


        //CONTA CICLOS POR CAMINHAMENTO
        System.out.println(findCyclesDFS());

    }

    public static void readEntry(String entry){
        entry = replace(entry);
        quantArestas = getQuantArestas(entry);
        defineNos(entry);
        defineArestas(entry);
        ordenaArestasA();
        printArestas();
        printNos();
    }

    public static String replace(String entry){
        entry = entry.replace("{", "");
        entry = entry.replace("(", "");
        entry = entry.replace(")", "");
        entry = entry.replace("}", "");
        entry = entry.replace(" ", "");
        return entry;
    }

    public static int getQuantArestas(String entry){
        String arrayArestasSeparadas[] = entry.split(";");
        return arrayArestasSeparadas.length;
    }

    public static void defineNos(String entry){
        no = new LinkedList<Integer>();
        entry = entry.replaceAll(";", ",");
        String[] nos = entry.split(",");
        for(int i=0; i<nos.length; i++){
                if(!no.contains(Integer.parseInt(nos[i])))
                    no.add(Integer.parseInt(nos[i]));
        }
        Collections.sort(no);
        quantNos = no.size();
    }

    public static void defineArestas(String entry){
        arestas = new LinkedList<Aresta>();
        entry = entry.replaceAll(";", ",");
        String[] nos = entry.split(",");
        for(int i=0; i<nos.length; i+=2){
                arestas.add(new Aresta(Integer.parseInt(nos[i]), Integer.parseInt(nos[i+1])));
        }
    }

    public static void printArestas(){
        System.out.println("\nPRINT ARESTAS\n");
        for(int i=0; i<arestas.size(); i++){
             System.out.println("("+arestas.get(i).getA()+","+arestas.get(i).getB()+")");
        }
    }

    public static void printNos(){
        System.out.println("\nPRINT NOS\n");
        for(int i=0; i<no.size(); i++){
            System.out.println("NO [" + i + "] = ("+no.get(i)+")");
        }
    }

    //Ordenação
    public static void ordenaArestasA(){
        for (int i = 0; i < (arestas.size() - 1); i++) {
            int menor = i;
            for (int j = (i + 1); j < arestas.size(); j++){
                int comp = ((arestas.get(menor).getA()) > (arestas.get(j).getA())) ? 1:0;
               if (comp>0){
                  menor = j;
               }
            }
            swap(menor, i);
         }
    }

    public static void ordenaArestasB(){
        for (int i = 0; i < (arestas.size() - 1); i++) {
            int menor = i;
            for (int j = (i + 1); j < arestas.size(); j++){
                int comp = ((arestas.get(menor).getB()) > (arestas.get(j).getB())) ? 1:0;
               if (comp>0){
                  menor = j;
               }
            }
            swap(menor, i);
         }
    }
  
    public static void swap(int i, int j) {
        Aresta temp = new Aresta(arestas.get(i).getA(),arestas.get(i).getB());
        arestas.get(i).setA(arestas.get(j).getA());
        arestas.get(i).setB(arestas.get(j).getB());
        arestas.get(j).setA(temp.getA());
        arestas.get(j).setB(temp.getB());
    }

    //Combinação
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
    
    //Confere se uma lista de nós forma um ciclo na ordem em que aparece
    public static boolean isCycled(LinkedList<Integer> current, MatrixAdjacency matrixA){
        for(int i=0; i<current.size()-1; i++){
            if(matrixA.get((current.get(i)-1),(current.get(i+1)-1)) == 0 && matrixA.get((current.get(i+1)-1),(current.get(i)-1)) == 0)
                return false;
        }

        if(matrixA.get((current.get(current.size()-1)-1),(current.get(0)-1)) == 0 && matrixA.get((current.get(0)-1),(current.get(current.size()-1)-1)) == 0)
            return false;
        return true;
    }

    //Permutação
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

    //Confere se existe alguma permutação na lista de permutações que forma um ciclo
    public static boolean anyPermutationIsCycled(LinkedList<LinkedList<Integer>> permutations, MatrixAdjacency matrixA){
        for(int i=0; i<permutations.size(); i++){
            System.out.println("Permutação: "+ permutations.get(i));
            if(isCycled(permutations.get(i), matrixA)){
                System.out.println("Faz ciclo");
                return true;
            }else{
                System.out.println("Não faz ciclo");
            }
        }
        return false;
    }

    //Pega, de uma lista com todas as permutações, aquelas que formam ciclos válidos e diferentes uns dos outros
    public static int getValidPermutations(LinkedList<LinkedList<Integer>> permutations, MatrixAdjacency matrixA){
        LinkedList<LinkedList<Integer>> validPermut = new LinkedList<>();
        int cont=0;
        for(int i=0; i<permutations.size(); i++){
            System.out.println("Permutação: "+ permutations.get(i));
            if(isCycled(permutations.get(i), matrixA)){
                validPermut.add(permutations.get(i));
                System.out.println("&&&&&&&&&&&&& FAZ CICLO &&&&&&&&&&");
            }else{
                System.out.println("Não faz ciclo");
            }
        }
        return cont;
    }
    
    public static int findCyclesDFS(){
        findCycle p = new findCycle(arestas);
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

//OUTROS MODOS DE FAZER COMBINAÇÃO

        /*Realiza combinação
        ans = new LinkedList<>();
        tmp = new LinkedList<>();
        for(int i=3; i<=no.size(); i++){
            makeCombi(no.size(), i);
        }
        System.out.println(ans);*/

        /*for(int i=1; i<=no.size(); i++){
            for(int j=1; j<=no.size(); j++){
                for(int k=1; k<=no.size(); k++){
                    if(i!=j && i!=k && j!=k){
                        //ordenar
                        System.out.println(i+" "+j+" "+k);
                    }
                }
            }
        }*/