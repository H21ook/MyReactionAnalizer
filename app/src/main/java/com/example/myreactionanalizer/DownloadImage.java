package com.example.myreactionanalizer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImage() {

    }
    public DownloadImage(ImageView bmImage){
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls){
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try{
            //InputStream in = new java.net.URL(urldisplay).openStream();

            //add Code start
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            InputStream input = new java.net.URL(urldisplay).openStream();
            CipherInputStream cis = new CipherInputStream(input, cipher);


            mIcon11 = BitmapFactory.decodeStream(cis);
            //add Code end


            //mIcon11 = BitmapFactory.decodeStream(in);
        }catch (Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result){
        bmImage.setImageBitmap(result);
    }

//    protected Bitmap getBitmapFromURL(String src) {
//        Bitmap myBitmap = null;
//        try {
//
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//
//            //Decryption
//            try {
//                Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
//                SecretKeySpec keySpec = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
//                IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
//                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
//
//                InputStream input = new java.net.URL(urldisplay).openStream();
//                CipherInputStream cis = new CipherInputStream(input, cipher);
//
//
//                myBitmap = BitmapFactory.decodeStream(cis);
//
//            }
//            catch(Exception e){
//                e.fillInStackTrace();
//                Log.v("ERROR","Errorchence : "+e);
//            }
//
//            return myBitmap;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
