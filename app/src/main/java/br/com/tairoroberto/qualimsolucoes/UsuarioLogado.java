package br.com.tairoroberto.qualimsolucoes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import br.com.tairoroberto.util.Base64;

/**
 * Created by tairo on 16/01/15.
 */
public class UsuarioLogado implements Parcelable {

    private long id;
    private String name;
    private String email;
    private String type;
    private Bitmap image;
    private Bitmap signature;

    public UsuarioLogado() {

    }

    public UsuarioLogado(long id, String name, String email, String type, Bitmap image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.type = type;
        this.image = image;
    }

    public UsuarioLogado(Parcel parcel) {
        this.id = parcel.readLong();
        this.name = parcel.readString();
        this.email = parcel.readString();
        this.type = parcel.readString();
        this.image = (Bitmap) parcel.readValue(Bitmap.class.getClassLoader());
        this.signature = (Bitmap) parcel.readValue(Bitmap.class.getClassLoader());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getSignature() {
        return signature;
    }

    public void setSignature(Bitmap signature) {
        this.signature = signature;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Escreve os valores que serão transportados
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(type);
        dest.writeValue(image);
        dest.writeValue(signature);
    }

    //Obs: Obrigatorio usar o CREATOR para recuperar os dados do parcelable
    public static final Creator<UsuarioLogado> CREATOR = new Creator<UsuarioLogado>() {

        //Pega o nosso parcel e instacia para podermos utilizar o nosso objeto "No caso o Studant"
        @Override
        public UsuarioLogado createFromParcel(Parcel source) {
            return new UsuarioLogado(source);
        }

        @Override
        public UsuarioLogado[] newArray(int size) {
            return new UsuarioLogado[size];
        }
    };


    /*****************************************************************************************/
    /**                      Methods to convert image                                       */
    /***************************************************************************************/
    private String mime;


    public String getMime() {
        return mime;
    }
    public void setMime(String mime) {
        this.mime = mime;
    }
    public void setMimeFromImgPath(String imgPath) {
        String[] aux = imgPath.split("\\.");
        this.mime = aux[aux.length - 1];
    }

    /*****************************************************************************************/
    /**                      Methods to convert image PHOTO                                 */
    /***************************************************************************************/

    public Bitmap getBitmapPhoto() {
        return image;
    }
    public void setBitmapPhoto(Bitmap bitmap) {
        this.image = bitmap;
    }
    public String getBitmapBase64Photo(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        if(mime.equalsIgnoreCase("png"))
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        else
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return(Base64.encodeBytes(byteArray));
    }


    public void setResizedBitmapPhoto(File file, int width, int height) {
        try {
            image = BitmapFactory.decodeFile(file.getPath());

            int w = image.getWidth();
            int h = image.getHeight();

            float scaleX = (float) width / image.getWidth();
            float scaleY = (float) height / image.getHeight();

            Matrix matrix = new Matrix();
            matrix.setScale(scaleX, scaleY);
            Bitmap auxBitmap = Bitmap.createBitmap(image, 0, 0, w, h, matrix, true);

            auxBitmap = fixMatrix(file, auxBitmap);

            // Retira da memória o arquivo de imagem com tamanho original
            image.recycle();
            image = auxBitmap;
        }
        catch (OutOfMemoryError e) { e.printStackTrace(); }
        catch (RuntimeException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
    }


    /*****************************************************************************************/
    /**                      Methods to convert image SIGNATURE                             */
    /***************************************************************************************/
    public Bitmap getBitmapSignature() {
        return signature;
    }
    public void setBitmapSignature(Bitmap bitmap) {
        this.signature = bitmap;
    }
    public String getBitmapBase64Signature(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        if(mime.equalsIgnoreCase("png"))
            signature.compress(Bitmap.CompressFormat.PNG, 100, stream);
        else
            signature.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return(Base64.encodeBytes(byteArray));
    }


    public void setResizedBitmapSignature(File file, int width, int height) {
        try {
            image = BitmapFactory.decodeFile(file.getPath());

            int w = signature.getWidth();
            int h = signature.getHeight();

            float scaleX = (float) width / signature.getWidth();
            float scaleY = (float) height / signature.getHeight();

            Matrix matrix = new Matrix();
            matrix.setScale(scaleX, scaleY);
            Bitmap auxBitmap = Bitmap.createBitmap(signature, 0, 0, w, h, matrix, true);

            auxBitmap = fixMatrix(file, auxBitmap);

            // Retira da memória o arquivo de imagem com tamanho original
            signature.recycle();
            signature = auxBitmap;
        }
        catch (OutOfMemoryError e) { e.printStackTrace(); }
        catch (RuntimeException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
    }


    private static Bitmap fixMatrix(File file, Bitmap bitmap) throws IOException {
        Matrix matrix = new Matrix();
        boolean fixed = false;
        ExifInterface exif = new ExifInterface(file.getPath()); // Classe para ler tags escritas no JPEG
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL); // Orientação que foi salva a foto

        // Rotate bitmap
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                fixed = true;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                fixed = true;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                fixed = true;
                break;
            default:
                fixed = false;
                break;
        }

        if(!fixed) {
            return bitmap;
        }

        // Correção da orientação da foto (passa a matrix)
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,true);

        bitmap.recycle();
        bitmap = null;

        return newBitmap;
    }

}
