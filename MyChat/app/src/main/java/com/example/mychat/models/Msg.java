package com.example.mychat.models;

public class Msg {
    private String author;
    private String text;
    private int isnew;
    public Msg(String _author,String _text){
        text=_text;
        author=_author;
    }
    public String getText(){
        return text;
    }
    public void setAuthor(String _author){
        author=_author;
    }
    public void setText(String _text){
        text=_text;
    }
    public String getAuthor(){
        return author;
    }

    public int getIsnew(){
        return isnew;
    }
    public void setIsnew(int _isnew){
        isnew=_isnew;
    }

}
