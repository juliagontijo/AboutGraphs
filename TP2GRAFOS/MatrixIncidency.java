import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.LinkedList;
import java.io.*;
import java.util.*;

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