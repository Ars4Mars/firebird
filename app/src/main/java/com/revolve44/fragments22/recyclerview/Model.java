package com.revolve44.fragments22.recyclerview;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class Model {


    public static final int TEXT_TYPE=0;
    public static final int GRAPH_TYPE=1;

    public int type;
    public int data;
    public String text;
    //public String text2;



    public Model(int type, String text, int data)
    {
        this.type=type;
        this.data=data;
        this.text=text;
        //this.text=text2;

    }

}
