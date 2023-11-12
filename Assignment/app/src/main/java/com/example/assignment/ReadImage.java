package com.example.assignment;
import android.graphics.Bitmap;
import android.util.*;
import java.net.*;
import android.graphics.*;
import java.io.InputStream;

public class ReadImage {
    public static Bitmap readImage(String address){
        Bitmap bitmapImage = null;
        try {
            InputStream inputStream = new URL(address).openStream();
            bitmapImage = BitmapFactory.decodeStream(inputStream);
        }catch (Exception e){
            e.printStackTrace();
            Log.d("Message", "Error fetching image: " + e.getMessage());
        }
        if (bitmapImage == null)
            Log.d("Message", "The image was not fetched");
        return bitmapImage;
    }

}
