package com.myspy.myspyandroid.functions;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;

/**
 * Created by Miroslav Murin on 30.11.2016.
 */

public class ClassSaver {

    private Gson gson = new Gson();

    /**
     * Save Class to JSON String
     * @param Class Class you want to save
     * @return String in JSON Format
     */
    public String SaveClass(Object Class)
    {
        try{
            return gson.toJson(Class);
        }catch (Exception ex)
        {
            Log.w("ClassSaver",ex);
            return null;
        }
    }


    /**
     * Load Class from String
     * @param Json String
     * @param type Type of class
     * @return Object
     */
    public Object LoadClass(String Json, Type type)
    {
        try{
            return gson.fromJson(Json,type);
        }catch (Exception ex)
        {
            Log.w("ClassSaver",ex);
            return null;
        }
    }


    /**
     * Save class to file
     * @param ClassToSave Class you want to save
     * @param Dirname Directory name
     * @param Filename File name
     * @param context context
     * @return boolean, true if successful
     */
    public boolean SaveClassToFile(Object ClassToSave, String Dirname, String Filename, Context context)
    {
        try{
            File dir = context.getDir(Dirname, Context.MODE_PRIVATE);
            File file = new File(dir, Filename);
            FileOutputStream outStream = new FileOutputStream(file);


            byte[] bytes = gson.toJson(ClassToSave).getBytes();
            outStream.write(bytes);

            outStream.flush();
            outStream.close();

            return true;
        }catch (Exception ex)
        {
            Log.w("ClassSaver",ex);
            return false;
        }
    }


    /**
     * Save class to file
     * @param ClassToSave Class you want to save
     * @param file File to save
     * @return boolean, true if successful
     */
    public boolean SaveClassToSpecificFile(Object ClassToSave, File file)
    {
        try{
            FileOutputStream outStream = new FileOutputStream(file);


            byte[] bytes = gson.toJson(ClassToSave).getBytes();
            outStream.write(bytes);

            outStream.flush();
            outStream.close();

            return true;
        }catch (Exception ex)
        {
            Log.w("ClassSaver",ex);
            return false;
        }
    }

    /**
     * File exists?
     * @param Dirname Directory name
     * @param Filename File name
     * @param context context
     * @return boolean, true if exists
     */
    public boolean FileExist(String Dirname, String Filename, Context context)
    {
        File dir = context.getDir(Dirname, Context.MODE_PRIVATE);
        Log.d("Path",dir.getPath());
        dir.mkdirs();
        File file = new File(dir, Filename);

        return file.exists();
    }


    /**
     * Return internal storage file
     * @param Dirname Directory name
     * @param Filename File name
     * @param context Context
     * @return File
     */
    public File GetFile(String Dirname, String Filename, Context context)
    {
        File dir = context.getDir(Dirname, Context.MODE_PRIVATE);
        File file = new File(dir, Filename);

        return file;
    }

    /**
     * Load class from file
     * @param type Class type
     * @param Dirname Directory name
     * @param Filename File name
     * @param context context
     * @return Object
     */
    public Object LoadClassFromFile(Type type, String Dirname, String Filename, Context context)
    {
        try{
            File dir = context.getDir(Dirname, Context.MODE_PRIVATE);
            File file = new File(dir, Filename);

            FileInputStream inStream = new FileInputStream(file);
            int n;

            StringBuffer text = new StringBuffer("");

            byte[] buffer = new byte[1024];

            while ((n = inStream.read(buffer)) != -1)
            {
                text.append(new String(buffer, 0, n));
            }



            return gson.fromJson(text.toString(),type);
        }catch (Exception ex)
        {
            Log.w("ClassSaver",ex);
            return null;
        }
    }


    /**
     * Load class from file
     * @param type Type of class
     * @param file File to load
     * @return Object
     */
    public Object LoadClassFromSpecificFile(Type type, File file)
    {
        try{

            FileInputStream inStream = new FileInputStream(file);
            int n;

            StringBuffer text = new StringBuffer("");

            byte[] buffer = new byte[1024];

            while ((n = inStream.read(buffer)) != -1)
            {
                text.append(new String(buffer, 0, n));
            }



            return gson.fromJson(text.toString(),type);
        }catch (Exception ex)
        {
            Log.w("ClassSaver",ex);
            return null;
        }
    }


}
