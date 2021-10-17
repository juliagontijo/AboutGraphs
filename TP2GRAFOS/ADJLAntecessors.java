
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.LinkedList;
import java.io.*;
import java.util.*;

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
    }*/

    public void printVectorADJA(){
        for(int i=0; i<this.vectorOneADJList.length; i++){
            System.out.println(this.vectorOneADJList[i] + " -> " + this.vectorTwoADJList[i]);
        }
    }


}