package com.example.fitshare.model;

import android.widget.VideoView;

import java.util.List;

public class Trainer {

    public String email;
    public String password;
    public String name;
    public String img;
    public List<Proprties> Properties;
   // public List<Training_Devices> My_Training_Devices_list;
    public List<VideoView> My_Video_Training_list;
    public int Count_video;

    public Trainer(String email,String password,String name,  List<Proprties> Properties) {
        this.name = name;
        this.Properties = Properties;
        this.email=email;
        this.password=password;
       // My_Training_Devices_list= null;
        Count_video=0;
    }

    public List<Proprties> getProperties()
    {
        return Properties;
    }
//    public List<Training_Devices> getMy_Training_Devices_list()
//    {
//        return My_Training_Devices_list;
//    }
    public List<VideoView> getMy_Training_Video_list()
    {
        return My_Video_Training_list;
    }
}
