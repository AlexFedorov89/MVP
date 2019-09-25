package geekbrains.ru.lesson1mvc.Lesson3;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import geekbrains.ru.lesson1mvc.R;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Main3Activity extends AppCompatActivity {

    static final int REQUEST_PICK_UP_FILE = 777;
    static final String TAG = "MainActivity3";

    AlertDialog alert;
    Bitmap bmp;
    DisposableObserver<String> disposableObserver;
    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        disposableObserver = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
            }

            @Override
            public void onError(Throwable e) {
                if (alert.isShowing()) alert.cancel();
                Toast.makeText(Main3Activity.this, "Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                if (alert.isShowing()) alert.cancel();
                Toast.makeText(Main3Activity.this, "Got it!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initViews() {
        findViewById(R.id.btnStop).setOnClickListener(view -> {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        });

        if (readAndWriteExternalStorage(this)) {
            findViewById(R.id.btnConvert).setOnClickListener(view -> {
                Intent intent = new Intent();
                intent.setType("image/jpeg");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select JPG"), REQUEST_PICK_UP_FILE);
            });
        } else {
            Toast.makeText(this, "Can't convert without permissions!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PICK_UP_FILE) {
                Uri selectedPhotoUri = data.getData();

                Observable<String> observable = Observable.create((ObservableOnSubscribe<String>) emitter -> {
                    convertImg(selectedPhotoUri, emitter);

                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

                disposable = observable.subscribeWith(disposableObserver);
            }

            showAlertDlg();
        }
    }

    private void showAlertDlg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main3Activity.this);
        builder.setTitle("Conversation")
                .setMessage("converting in progress!")
                .setCancelable(false)
                .setNegativeButton("Cancel", (dialog, id) -> {
                    if (!disposable.isDisposed()) disposable.dispose();
                    dialog.cancel();
                });
        alert = builder.create();
        alert.show();
    }

    private void convertImg(Uri selectedPhotoUri, ObservableEmitter<String> emitter) throws InterruptedException {
        Thread.sleep(10_000);

        try {
            //  read.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                bmp = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), selectedPhotoUri));
            } else {
                bmp = (MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedPhotoUri));
            }
        } catch (IOException e) {

            Log.d(TAG, "Error. Get data from image.");

            emitter.onError(e);

            return;
        }

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        Integer counter = 0;
        File file = new File(path, "Result_" + new Date().getTime() + ".png");

        try (OutputStream fOut = new FileOutputStream(file)) {
            if (!file.exists()) {
                // Create blank file.
                file.createNewFile();
            }
            // Convert to PNG format.
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);

        } catch (Exception e) {
            emitter.onError(e);

            return;
        }

        emitter.onComplete();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static boolean readAndWriteExternalStorage(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            return false;
        } else {
            return true;
        }
    }
}
