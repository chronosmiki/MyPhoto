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

public class MainActivity extends AppCompatActivity
{
    Button foto;
    ImageView imagem;
    private static final int SELECT_PHOTO = 100;
    private static final int TAKE_PICTURE = 200;

    public void imgClick(View v)
    {
        Intent photoPick = new Intent(Intent.ACTION_PICK);
        photoPick.setType("image/*");
        startActivityForResult(photoPick, SELECT_PHOTO);
    }

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Foto tirada com sucesso!", Toast.LENGTH_LONG);
                    Bitmap bp = (Bitmap) data.getExtras().get("data");
                    foto.setEnabled(false);
                    imagem.setImageBitmap(bp);
                    imagem.setScaleType(ImageView.ScaleType.FIT_XY);
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Captura cancelada pelo usuário.", Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(this, "Erro ao inciar a camera ou capturar a foto.", Toast.LENGTH_LONG);
                }
            break;
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK)
                {
                    try {
                        Uri selectedImage = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap bp = BitmapFactory.decodeStream(imageStream);
                        foto.setEnabled(false);
                        imagem.setImageBitmap(bp);
                        imagem.setScaleType(ImageView.ScaleType.FIT_XY);
                    }catch(FileNotFoundException e){}
                }
            break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
