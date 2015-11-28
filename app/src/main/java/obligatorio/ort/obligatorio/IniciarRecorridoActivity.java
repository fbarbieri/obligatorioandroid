package obligatorio.ort.obligatorio;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import obligatorio.ort.obligatorio.recorrido.PuntoIntermedio;
import obligatorio.ort.obligatorio.recorrido.Recorrido;


/**
 * A login screen that offers login via email/password.
 */
public class IniciarRecorridoActivity extends AppCompatActivity{

    private EditText mCodigoView;
    private View mProgressView;
    private View mIniciarRecorridoFormView;
    private ImageButton mImageButton;
    private String mCurrentPhotoPath;
    public static final int SACAR_FOTO = 1;
    public int mImageButtonWidth;
    public int mImageButtonHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_recorrido);
        mCodigoView = (EditText) findViewById(R.id.codigo_estacionamiento);

        Button mGuardarRecorridoBtn = (Button) findViewById(R.id.guardar_recorrido);
        mGuardarRecorridoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarRecorrido();
            }
        });

        mIniciarRecorridoFormView = findViewById(R.id.iniciar_recorrido_form);
        mProgressView = findViewById(R.id.iniciar_recorrido_progress);

        mImageButton = (ImageButton) findViewById(R.id.imageButton);
        mImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });
    }

    private void guardarRecorrido() {

        mCodigoView.setError(null);
        String codigoEstacionamiento = mCodigoView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(codigoEstacionamiento)) {
            mCodigoView.setError(getString(R.string.error_codigo_requerido));
            focusView = mCodigoView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);



            // guardar en base y mandar el codigo a backend
            Intent resultIntent = new Intent();
            resultIntent.putExtra(getString(R.string.foto_result), mCurrentPhotoPath);
            resultIntent.putExtra(getString(R.string.codigo_estacionamiento), codigoEstacionamiento);
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

            mIniciarRecorridoFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mIniciarRecorridoFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mIniciarRecorridoFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mIniciarRecorridoFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

