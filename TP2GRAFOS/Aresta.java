import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.LinkedList;
import java.io.*;
import java.util.*;

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