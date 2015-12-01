package obligatorio.ort.obligatorio;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import obligatorio.ort.obligatorio.Servicios.EstacionamientosServices;


/**
 * A login screen that offers login via email/password.
 */
public class IngresarPuntoIntermedioActivity extends AppCompatActivity{

    private EditText mTituloView;
    private EditText mDescripcionView;
    private View mProgressView;
    private View mGuardarPuntoIntermedioFormView;
    private ImageButton mImageButton;
    private String mCurrentPhotoPath;
    public static final int SACAR_FOTO = 1;
    public int mImageButtonWidth;
    public int mImageButtonHeight;
    public Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_punto_intermedio);

        mTituloView = (EditText) findViewById(R.id.titulo);
        mDescripcionView = (EditText) findViewById(R.id.descripcion);

        Button mGuardarPuntoIntermedioBtn = (Button) findViewById(R.id.guardar_punto_intermedio);
        mGuardarPuntoIntermedioBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarPuntoIntermedio();
            }
        });
        Intent intent = getIntent();
        mLocation = intent.getParcelableExtra(getString(R.string.location));
        mGuardarPuntoIntermedioFormView = findViewById(R.id.guardar_punto_intermedio_form);
        mProgressView = findViewById(R.id.iniciar_recorrido_progress);

        mImageButton = (ImageButton) findViewById(R.id.imageButton);
        mImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sacarFoto();
            }
        });
    }

    private void sacarFoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                photoFile = null;
                mCurrentPhotoPath = null;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                savePreferences();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, SACAR_FOTO);
            }
        }
    }

    private void guardarPuntoIntermedio() {

        mTituloView.setError(null);
        mDescripcionView.setError(null);
        String titulo = mTituloView.getText().toString();
        String descripcion = mTituloView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(titulo)) {
            mDescripcionView.setError(getString(R.string.error_descripcion_requerido));
            focusView = mDescripcionView;
            cancel = true;
        }
        if (TextUtils.isEmpty(titulo)) {
            mTituloView.setError(getString(R.string.error_titulo_requerido));
            focusView = mTituloView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            Intent resultIntent = new Intent();
            resultIntent.putExtra(getString(R.string.foto_result), mCurrentPhotoPath);
            resultIntent.putExtra(getString(R.string.titulo), titulo);
            resultIntent.putExtra(getString(R.string.descripcion), descripcion);
            resultIntent.putExtra(getString(R.string.location), mLocation);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mGuardarPuntoIntermedioFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mGuardarPuntoIntermedioFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mGuardarPuntoIntermedioFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mGuardarPuntoIntermedioFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        switch (requestCode) {
            case SACAR_FOTO:
                if (resultCode == RESULT_OK) {
                    restorePreferences();
                    if (mCurrentPhotoPath != null) {
                        setPic();
                        mCurrentPhotoPath = null;
                    }
                }
                break;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPic() {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((mImageButtonWidth > 0) || (mImageButtonHeight > 0)) {
            scaleFactor = Math.min(photoW/mImageButtonWidth, photoH/mImageButtonHeight);
        }

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageButton.setImageBitmap(bitmap);
    }

    private void savePreferences(){
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("mCurrentPhotoPath", mCurrentPhotoPath);
        editor.putInt("targetW", mImageButton.getWidth());
        editor.putInt("targetH", mImageButton.getHeight());
        editor.commit();
    }

    private void restorePreferences() {
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        mCurrentPhotoPath = settings.getString("mCurrentPhotoPath", "");
        mImageButtonWidth = settings.getInt("targetW", 1);
        mImageButtonHeight = settings.getInt("targetH", 1);
    }

}

