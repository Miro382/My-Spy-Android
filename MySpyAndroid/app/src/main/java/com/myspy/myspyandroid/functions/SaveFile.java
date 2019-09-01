package com.myspy.myspyandroid.functions;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Miroslav Murin on 28.11.2016.
 */

public class SaveFile {

    private File dir, file;
    private Map<String,String> values = new HashMap<String, String>();


    /**
     *
     * @param DirName Directory name
     * @param context context
     */
    public SaveFile(String DirName, Context context)
    {
        values.clear();
        dir = context.getDir(DirName, Context.MODE_PRIVATE);
    }

    /**
     *
     * @param DirName Directory name
     * @param Filename File name
     * @param context context
     */
    public SaveFile(String DirName, String Filename, Context context)
    {
        values.clear();
        dir = context.getDir(DirName, Context.MODE_PRIVATE);
        OpenFile(Filename);
    }

    /**
     * Open file
     * @param FileName filename
     * @return boolean, true if successful
     */
    public boolean OpenFile(String FileName)
    {
        try {
            values.clear();
            file = new File(dir, FileName);
            return true;
        }catch (Exception ex)
        {
            Log.w("SaveFile",ex);
            return false;
        }
    }


    /**
     * Delete file if exist
     * @return boolean, true if successful
     */
    public boolean DeleteFile()
    {
        try {
            if (file.exists())
            {
                file.delete();
                return true;
            }else{
                return false;
            }
        }catch (Exception ex)
        {
            Log.w("SaveFile",ex);
            return false;
        }
    }


    /**
     * Add item to file
     * @param Tag Tag
     * @param Value Value
     */
    public void AddItem(String Tag, Object Value)
    {
        values.put(Tag,""+Value);
    }


    /**
     * Get item you want to receive
     * @param Tag Tag
     * @return Object
     */
    public Object GetItem(String Tag)
    {
        try {
            return values.get(Tag);
        }catch (Exception exc)
        {
            Log.w("SaveFile",exc);
            return null;
        }
    }


    /**
     * Item exist?
     * @param Tag Tag
     * @return boolean
     */
    public boolean ItemExist(String Tag)
    {
        return values.containsKey(Tag);
    }

    public void RemoveItem(String Tag)
    {
        values.remove(Tag);
    }

    public void Clear()
    {
        values.clear();
    }



    private String ToSafeString(String string)
    {
        string = string.replace("=","<EQL>");
        string = string.replace("[","<LFT>");
        string = string.replace("]","<RGT>");
        return  string;
    }



    private String FromSafeString(String string)
    {
        string = string.replace("<EQL>","=");
        string = string.replace("<LFT>","[");
        string = string.replace("<RGT>","]");
        return  string;
    }


    /**
     * Save to file
     * @return boolean , true if successful
     */
    public boolean Save()
    {
        try {
            FileOutputStream outStream = new FileOutputStream(file);

            StringBuilder stringBuilder = new StringBuilder();

            for(Map.Entry<String, String> entry : values.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                value = ToSafeString(value);

                stringBuilder.append("["+key+"]="+value);
            }

            byte[] bytes = stringBuilder.toString().getBytes();
            outStream.write(bytes);

            outStream.flush();
            outStream.close();
            return true;
        }catch (Exception ex)
        {
            return false;
        }
    }


    /**
     * Load file
     * @return boolean , true if successful
     */
    public boolean Load()
    {
        try {
            values.clear();
            FileInputStream inStream = new FileInputStream(file);
            int n;

            StringBuffer text = new StringBuffer("");

            byte[] buffer = new byte[1024];

            while ((n = inStream.read(buffer)) != -1)
            {
                text.append(new String(buffer, 0, n));
            }

            String[] strings = (text.toString()).split("[=\\[\\]]");


            int i=0;
            boolean tag = true;
            String Tag = "";
            for(String str : strings)
            {
                if(!str.isEmpty()) {
                    if(tag)
                    {
                        Tag=str;
                        tag=false;
                    }else{
                        tag=true;
                        values.put(Tag,FromSafeString(str));
                    }
                    i++;
                }
            }


            for(Map.Entry<String, String> entry : values.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                Log.d("LOADED", key+" : "+value);
            }

            return true;
        }catch (Exception ex)
        {
            return false;
        }
    }


}
