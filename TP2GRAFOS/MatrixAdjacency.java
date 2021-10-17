import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.LinkedList;
import java.io.*;
import java.util.*;

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