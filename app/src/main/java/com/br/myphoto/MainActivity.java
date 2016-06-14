package com.br.myphoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

/*This class is an attempt to take a picture or select one from gallery. It will take the selected photo and when
    user confirm his choice the intent answer with an int (SELECT_PHOTO or TAKE_PICTURE), so the case can know where to go
    whatever is the choice, the case will get the bmp and set it into a imageview. 
    
    Now I need to work on how to save and edit this picture. 
    
    This class is part of a bigger project, and I'll try to turn it into a method so I can use it everywere.
*/

//Here I set the variables the most important is SELECT_PHOTO and TAKE_PICTURE....
public class MainActivity extends AppCompatActivity
{
    Button foto;
    ImageView imagem;
    private static final int SELECT_PHOTO = 100;
    private static final int TAKE_PICTURE = 200;
    
    //This is the action for the imageview, when u click on it, it opens the gallery.apk, you can select a picture...
    //The result will form a package and it will be sent to onActivityResult..
    public void imgClick(View v)
    {
        Intent photoPick = new Intent(Intent.ACTION_PICK);
        photoPick.setType("image/*");
        startActivityForResult(photoPick, SELECT_PHOTO);
    }
    
    /* This is for sure not the best practice or way to do it, but still works...
        Here I set the action for camera icon (button), when you click, it opens the camera.apk, after take a picture and confirm
        it will send the package to onActivityResult and set the imageview with the photo you took.*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foto = (Button) findViewById(R.id.btnCamera);
        imagem = (ImageView) findViewById(R.id.imageView);

        foto.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PICTURE);
            }
        });
    }
    
    /* The "MAIN" method of this class, here I receive the requestCode (TAKE_PICTURE or SELECT_PHOTO), the resultCode(OK OR CANCEL)
        and the package (photo from intent)...
        After this, the switch case will catch the requestCode...*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Foto tirada com sucesso!", Toast.LENGTH_LONG); // Don't know why but it doesn't works..
                    Bitmap bp = (Bitmap) data.getExtras().get("data"); // Here the Bitmap variable bp get the picture from intent.
                    foto.setEnabled(false); 
                    imagem.setImageBitmap(bp); //Set the imageview with the bitmap variable bp.
                    imagem.setScaleType(ImageView.ScaleType.FIT_XY); //To make the imageview fit the screen of phone.
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Captura cancelada pelo usuário.", Toast.LENGTH_LONG); //Doesn't work too :(
                } else {
                    Toast.makeText(this, "Erro ao inciar a camera ou capturar a foto.", Toast.LENGTH_LONG); //Same..
                }
            break;
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK)
                {
                    try {
                        Uri selectedImage = data.getData(); //Here the Uri variable selectedImage get the picture path from intent...
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage); //Here I get the picture from the path
                        Bitmap bp = BitmapFactory.decodeStream(imageStream); // Set the picture into a bitmap variable called bp.
                        foto.setEnabled(false);
                        imagem.setImageBitmap(bp); //Set the imageview with the variable bp.
                        imagem.setScaleType(ImageView.ScaleType.FIT_XY); //To make the imageview fit the screen of phone.
                    }catch(FileNotFoundException e){}
                }
            break;
        }
    }
    
    /*Unecessary Android does it automatic, but I'll increment that later... */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
