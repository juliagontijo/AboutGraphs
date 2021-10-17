import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.LinkedList;
import java.io.*;
import java.util.*;

class AdjacencyList{
    protected NoLista[] vet;

    public AdjacencyList(int quantNos, LinkedList<Integer> no){
        this.vet = new NoLista[quantNos];
        for(int i=0; i<quantNos; i++){
            this.vet[i] = new NoLista(no.get(i), null);
        }
    }
}