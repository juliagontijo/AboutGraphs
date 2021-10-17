import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.LinkedList;
import java.io.*;
import java.util.*;

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