import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.LinkedList;
import java.io.*;
import java.util.*;

public class tp1 {

    public static Scanner in = new Scanner(System.in);
    private static int quantNos;
    private static int quantArestas;
    private static boolean ponderada;
    private static LinkedList<Integer> no;
    private static LinkedList<Aresta> arestas;

    public static void main(String[] args) {
        String entry = in.nextLine();
        ponderada = IsPonderada(entry);
        readEntry(entry);
        chamaManipuladoes();
    }

    public static void chamaManipuladoes(){
        ordenaArestasA();
        printArestas();
        //Matriz de Incidência
        System.out.println("\n-- Matriz de Incidência --\n");
        MatrixIncidency matrixI = new MatrixIncidency(quantNos, quantArestas);
        matrixI.fillMatrixI(arestas);
        matrixI.printMatrixI(quantNos, quantArestas);

        //Matriz de Adjacência
        System.out.println("\n-- Matriz de Adjacência --\n");
        MatrixAdjacency matrixA = new MatrixAdjacency(quantNos);
        matrixA.fillMatrixA(arestas, quantNos);
        matrixA.printMatrixA(quantNos);

        //Lista de Adjacência Sucessores
        System.out.println("\n-- Lista de Adjacência Sucessores--\n");
        ordenaArestasA();
        ADJLSuccessors adjListS = new ADJLSuccessors(quantNos, no, quantArestas);
        adjListS.fillSuccessors(arestas);
        adjListS.printADJSUccessors();

        //Lista de Adjacência Antecessores
        System.out.println("\n-- Lista de Adjacência Antecessores--\n");
        ordenaArestasB();
        ADJLAntecessors adjListA = new ADJLAntecessors(quantNos, no, quantArestas);
        adjListA.fillAntecessors(arestas);
        adjListA.printADJAntecessors();

        /*Lista de Adjacência Sucessores - Vector
        System.out.println("\n-- Lista de Adjacência Sucessores - Vector --\n");
        ordenaArestasA();
        adjListS.fillVectorADJS2();
        adjListS.printVectorADJS();

        //Lista de Adjacência Sucessores - Vector
        System.out.println("\n-- Lista de Adjacência Antecessores - Vector --\n");
        ordenaArestasB();
        adjListA.fillVectorADJA();
        adjListA.printVectorADJA();*/
    }

    public static void defineNos(String entry){
        no = new LinkedList<Integer>();
        entry = entry.replaceAll(";", ",");
        String[] nos = entry.split(",");
        if(ponderada){
            for(int i=0; i<nos.length; i+=3){
                if(!no.contains(Integer.parseInt(nos[i])))
                    no.add(Integer.parseInt(nos[i]));
                if(!no.contains(Integer.parseInt(nos[i+1])))
                    no.add(Integer.parseInt(nos[i+1]));
            }
        }else{
            for(int i=0; i<nos.length; i++){
                if(!no.contains(Integer.parseInt(nos[i])))
                    no.add(Integer.parseInt(nos[i]));
            }
        }

        Collections.sort(no);
        quantNos = no.size();
    }

    public static void defineArestas(String entry){
        arestas = new LinkedList<Aresta>();
        entry = entry.replaceAll(";", ",");
        String[] nos = entry.split(",");
        if(ponderada){
            for(int i=0; i<nos.length; i+=3){
                arestas.add(new Aresta(Integer.parseInt(nos[i]), Integer.parseInt(nos[i+1]), Integer.parseInt(nos[i+2])));
            }
        }else{
            for(int i=0; i<nos.length; i+=2){
                arestas.add(new Aresta(Integer.parseInt(nos[i]), Integer.parseInt(nos[i+1])));
            }
        }
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

    public static boolean IsPonderada(String entry){
        int p = entry.indexOf(")");
        String temp = entry.substring(0, p);
        return (temp.chars().filter(ch -> ch == ',').count() == 2); //se tiver 2 vírgulas, é ponderada
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

    public static void printArestas(){
        System.out.println("\nPRINT ARESTAS\n");
        if(ponderada){
            for(int i=0; i<arestas.size(); i++){
                System.out.println("("+arestas.get(i).getA()+","+arestas.get(i).getB()+") Valor = "+arestas.get(i).getValor());
            }
        }else{
         for(int i=0; i<arestas.size(); i++){
             System.out.println("("+arestas.get(i).getA()+","+arestas.get(i).getB()+")");
         }
        }
    }

    public static void printNos(){
        System.out.println("\nPRINT NOS\n");
        for(int i=0; i<no.size(); i++){
            System.out.println("NO [" + i + "] = ("+no.get(i)+")");
        }
    }

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
        Aresta temp = new Aresta(arestas.get(i).getA(),arestas.get(i).getB(),arestas.get(i).getValor());
        arestas.get(i).setA(arestas.get(j).getA());
        arestas.get(i).setB(arestas.get(j).getB());
        arestas.get(i).setValor(arestas.get(j).getValor());
        arestas.get(j).setA(temp.getA());
        arestas.get(j).setB(temp.getB());
        arestas.get(j).setValor(temp.getValor());
     }
    
}

/*
class Aresta{
    private int a;
    private int b;
    private int valor;

    public Aresta(int a, int b){
        this.a = a;
        this.b = b;
        this.valor = 0;
    }

    public Aresta(int a, int b, int v){
        this.a = a;
        this.b = b;
        this.valor = v;
    }

    public void setA(int i){
        this.a = i;
    }

    public int getA(){
        return this.a;
    }

    public void setB(int i){
        this.b = i;
    }

    public int getB(){
        return this.b;
    }

    public void setValor(int i){
        this.valor = i;
    }

    public int getValor(){
        return this.valor;
    }

}

class MatrixIncidency{
    public int[][] matrix;

    public MatrixIncidency(int n, int m){
        this.matrix = new int[n][m];
        for(int i=0; i<n; i++){
            for(int x=0; x<m; x++){
                this.matrix[i][x] = 0;
            }
        } 
    }

    public void fillMatrixI(LinkedList<Aresta> arestas){
        for(int i=0; i<arestas.size(); i++){
            this.matrix[arestas.get(i).getA()-1][i] = 1;
            this.matrix[arestas.get(i).getB()-1][i] = -1;
        }
    }

    public void printMatrixI(int n, int m){
        for(int i=0; i<n; i++){
            for(int x=0; x<m; x++){
                System.out.print(this.matrix[i][x] + "   ");
            }
            System.out.println("");
        }
    }

}

class MatrixAdjacency{
    public int[][] matrix;

    public MatrixAdjacency(int n){
        this.matrix = new int[n][n];
        for(int i=0; i<n; i++){
            for(int x=0; x<n; x++){
                this.matrix[i][x] = 0;
            }
        } 
    }

    public void fillMatrixA(LinkedList<Aresta> arestas, int quantNos){
        for(int i=0; i<arestas.size(); i++){
            this.matrix[arestas.get(i).getA()-1][arestas.get(i).getB()-1] = 1;
        }
    }

    public void printMatrixA(int n){
        for(int i=0; i<n; i++){
            for(int x=0; x<n; x++){
                System.out.print(this.matrix[i][x] + "   ");
            }
            System.out.println("");
        }
    }

    public int get(int i, int j){
        return this.matrix[i][j];
    }
}

class AdjacencyList{
    protected NoLista[] vet;

    public AdjacencyList(int quantNos, LinkedList<Integer> no){
        this.vet = new NoLista[quantNos];
        for(int i=0; i<quantNos; i++){
            this.vet[i] = new NoLista(no.get(i), null);
        }
    }
}

class ADJLSuccessors extends AdjacencyList{

    int[] vectorOneADJList;
    int[] vectorTwoADJList;

    public ADJLSuccessors(int quantNos, LinkedList<Integer> no, int quantArestas) {
        super(quantNos, no);
        this.vectorOneADJList = new int[quantArestas];
        this.vectorTwoADJList = new int[quantArestas];
    }

    public void fillSuccessors(LinkedList<Aresta> arestas){
        NoLista temp;
        for(int i=0; i<this.vet.length; i++){
            temp = this.vet[i];
            for(int x=0; x<arestas.size(); x++){
                if(arestas.get(x).getA() == this.vet[i].getValue()){
                    temp.prox = new NoLista(arestas.get(x).getB());
                    temp = temp.prox;
                }
            }
        }
    }

    public void printADJSUccessors(){
        NoLista temp;
        for(int i=0; i<this.vet.length; i++){
            temp = this.vet[i];
            while(temp!=null){
                System.out.print(temp.getValue() + " ");
                temp = temp.prox;
            }
            System.out.println("");
        }
    }

    public void fillVectorADJS(){
        NoLista temp;
        int n=0;
        for(int i=0; i<this.vet.length; i++){
            temp = this.vet[i].prox;
            while(temp!=null){
                this.vectorOneADJList[n] = this.vet[i].getValue();
                this.vectorTwoADJList[n] = temp.getValue();
                temp = temp.prox;
                n++;
            }
        }
    }

    public void fillVectorADJS2(){
        NoLista temp;
        int n=0;
        int i=0;
        for(i=0; i<this.vet.length; i++){
            temp = this.vet[i].prox;
            this.vectorOneADJList[i] = n+1;
            while(temp!=null){
                this.vectorTwoADJList[n] = temp.getValue();
                temp = temp.prox;
                n++;
            }
        }
        this.vectorOneADJList[i]= n+1;
    }

    public void printVectorADJS(){
        for(int i=0; i<this.vectorOneADJList.length; i++){
            System.out.println(this.vectorOneADJList[i] + " -> " + this.vectorTwoADJList[i]);
        }
    }


}

class ADJLAntecessors extends AdjacencyList{

    int[] vectorOneADJList;
    int[] vectorTwoADJList;

    public ADJLAntecessors(int quantNos, LinkedList<Integer> no, int quantArestas) {
        super(quantNos, no);
        this.vectorOneADJList = new int[quantArestas];
        this.vectorTwoADJList = new int[quantArestas];
    }

    public void fillAntecessors(LinkedList<Aresta> arestas){
        NoLista temp;
        for(int i=0; i<this.vet.length; i++){
            temp = this.vet[i];
            for(int x=0; x<arestas.size(); x++){
                if(arestas.get(x).getB() == this.vet[i].getValue()){
                    temp.prox = new NoLista(arestas.get(x).getA());
                    temp = temp.prox;
                }
            }
        }
    }

    public void printADJAntecessors(){
        NoLista temp;
        for(int i=0; i<this.vet.length; i++){
            temp = this.vet[i];
            while(temp!=null){
                System.out.print(temp.getValue() + " ");
                temp = temp.prox;
            }
            System.out.println("");
        }
    }

    public void fillVectorADJA(){
        NoLista temp;
        int n=0;
        for(int i=0; i<this.vet.length; i++){
            temp = this.vet[i].prox;
            while(temp!=null){
                this.vectorOneADJList[n] = this.vet[i].getValue();
                this.vectorTwoADJList[n] = temp.getValue();
                temp = temp.prox;
                n++;
            }
        }
    }

    /*public void fillVectorADJA2(){
        NoLista temp;
        int n=0;
        int i=0;
        for(i=0; i<this.vet.length; i++){
            temp = this.vet[i].prox;
            this.vectorOneADJList[i] = n+1;
            while(temp!=null){
                this.vectorTwoADJList[n] = temp.getValue();
                temp = temp.prox;
                n++;
            }
        }
        this.vectorOneADJList[i]= n+1;
    }*//*

    public void printVectorADJA(){
        for(int i=0; i<this.vectorOneADJList.length; i++){
            System.out.println(this.vectorOneADJList[i] + " -> " + this.vectorTwoADJList[i]);
        }
    }


}

class NoLista{
    protected int value;
    protected NoLista prox;

    public NoLista(int v, NoLista p){
        this.value = v;
        this.prox = p;
    }

    public NoLista(int v){
        this.value = v;
        this.prox = null;
    }
    public void setValue(int v){
        this.value = v;
    }

    public int getValue(){
        return this.value;
    }

}

/*
class ListaADJ{
    protected int quantNos;
    protected LinkedList<Integer>[] adj;

    public ListaADJ(int quant, LinkedList<Integer> no){
        this.quantNos = quant;
        this.adj = new LinkedList<Integer>[quant];
        for(int i=0; i<this.quantNos; ++i){
            this.adj[i] = new LinkedList<Integer>();
            this.adj[i].add(no.get(i));
        }
    }

    public void addAresta(int v,int w){
        adj[v].add(w);
    }

    public int returnAresta(int v, int w){
        return adj[v].get(w);
    }
}

class ADJLSuccessors2 extends ListaADJ{

    int[] vectorOneADJList;
    int[] vectorTwoADJList;

    public ADJLSuccessors2(int quantNos, LinkedList<Integer> no, int quantArestas) {
        super(quantNos, no);
        this.vectorOneADJList = new int[quantArestas];
        this.vectorTwoADJList = new int[quantArestas];
    }

    public void fillSuccessors2(LinkedList<Aresta> arestas){
        for(int i=0; i<arestas.size(); i++){
            addAresta(arestas.get(i).getA(), arestas.get(i).getB());
        }
    }

    public void printADJSUccessors2(){
        for(int i=0; i<this.quantNos; i++){
            for(int j=0; j<adj[i].size() ; j++){
                System.out.println(returnAresta(i, j));
            }
            System.out.println("");
        }
    }

    /*public void fillVectorADJS(){
        NoLista temp;
        int n=0;
        for(int i=0; i<this.vet.length; i++){
            temp = this.vet[i].prox;
            while(temp!=null){
                this.vectorOneADJList[n] = this.vet[i].getValue();
                this.vectorTwoADJList[n] = temp.getValue();
                temp = temp.prox;
                n++;
            }
        }
    }

    public void fillVectorADJS2(){
        NoLista temp;
        int n=0;
        int i=0;
        for(i=0; i<this.vet.length; i++){
            temp = this.vet[i].prox;
            this.vectorOneADJList[i] = n+1;
            while(temp!=null){
                this.vectorTwoADJList[n] = temp.getValue();
                temp = temp.prox;
                n++;
            }
        }
        this.vectorOneADJList[i]= n+1;
    }

    public void printVectorADJS(){
        for(int i=0; i<this.vectorOneADJList.length; i++){
            System.out.println(this.vectorOneADJList[i] + " -> " + this.vectorTwoADJList[i]);
        }
    }*


}*/