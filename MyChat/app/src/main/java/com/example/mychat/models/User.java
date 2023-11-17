package com.example.mychat.models;

public class User {
    private String Nickname;
    private int isOnline;
    public User(String nickname,int _isonline){
        isOnline = _isonline;
        Nickname = nickname;
    }
    public String getNickname(){
        return Nickname;
    }
    public void setNickname(String nickname){
        Nickname = nickname;
    }
    public int getIsonline(){
        return isOnline;
    }
    public void setIsOnline(int _isOnline){
        isOnline = _isOnline;
    }
}
