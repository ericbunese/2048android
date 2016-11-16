package br.com.marblestudio.a2048android;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by eric on 08/11/16.
 */

public class FileServices
{
    private static String filepath = "android2048game";

    private boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            return true;
        }
        return false;
    }

    private boolean isExternalStorageReadable()
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
            return true;
        }
        return false;
    }

    public boolean fileAppendExternalStorage(Context context, String filename, String sBody)
    {
        File myExternalFile;

        if (!isExternalStorageWritable() && !isExternalStorageReadable())
        {
            return false;
        }
        else
        {
            myExternalFile = new File(context.getExternalFilesDir(filepath), filename);
            try
            {
                FileOutputStream fos = new FileOutputStream(myExternalFile, false);
                fos.write(sBody.getBytes());
                fos.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }

    public FileOutputStream getWriter(Context context, String filename) throws FileNotFoundException
    {
        File myExternalFile;
        FileOutputStream fos;

        if (!isExternalStorageWritable() && !isExternalStorageReadable())
        {
            return null;
        }
        else
        {
            myExternalFile = new File(context.getExternalFilesDir(filepath), filename);
            fos = new FileOutputStream(myExternalFile, false);
        }
        return fos;
    }

    public FileInputStream getReader(Context context, String filename) throws FileNotFoundException
    {
        File myExternalFile;
        FileInputStream fis;

        if (!isExternalStorageWritable() && !isExternalStorageReadable())
        {
            return null;
        }
        else
        {
            myExternalFile = new File(context.getExternalFilesDir(filepath), filename);
            fis = new FileInputStream(myExternalFile);
        }
        return fis;
    }
}
