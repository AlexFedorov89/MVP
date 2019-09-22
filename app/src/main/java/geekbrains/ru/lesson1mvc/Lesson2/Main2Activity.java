package geekbrains.ru.lesson1mvc.Lesson2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import geekbrains.ru.lesson1mvc.R;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class Main2Activity extends AppCompatActivity {
    static String TAG = "MainActivity2";

    EditText editText;
    TextView textView;
    PublishSubject<Long> eventBus;


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initViews();

        btnStartEventBus_AddListener();
        SendDataToEventBus_AddListener();
    }

    private void initViews() {
        editText = findViewById(R.id.editTextLsn2);
        textView = findViewById(R.id.textViewLsn2);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                textView.setText(s);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void SendDataToEventBus_AddListener() {
        findViewById(R.id.btnSendDataToEventBus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToEventBus();
            }
        });
    }

    private void btnStartEventBus_AddListener() {
        findViewById(R.id.btnStartEventBus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEventBus();
            }
        });
    }

    private void sendDataToEventBus() {
        // Send user's data to eventBus.
        Main2Activity.this.eventBus.onNext(100L);
    }

    private void startEventBus() {
        Toast.makeText(Main2Activity.this, "EventBus started!", Toast.LENGTH_SHORT).show();
        Observable<Long> observable = Observable
                .interval(1, TimeUnit.SECONDS)
                .take(5);

        Observer<Long> observer1 = new Observer<Long>() {
            @Override
            public void onComplete() {
                Log.d(TAG, "observer1 onComplete");
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "observer2 onSubscribe");
            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "observer1 onNext value = " + aLong);
            }
        };

        Observer<Long> observer2 = new Observer<Long>() {
            @Override
            public void onComplete() {
                Log.d(TAG, "observer2 onComplete");
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "observer2 onSubscribe");
            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "observer2 onNext value = " + aLong);
            }
        };

        eventBus = PublishSubject.create();

        observable.subscribe(eventBus);

        eventBus.subscribe(observer1);
        eventBus.subscribe(observer2);
    }
}