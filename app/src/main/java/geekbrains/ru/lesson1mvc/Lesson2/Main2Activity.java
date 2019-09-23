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
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class Main2Activity extends AppCompatActivity {
    static String TAG = "MainActivity2";

    EditText editText;
    TextView textView;
    EventBus eventBus;

    Observable<String> stringObservable;
    Disposable disposable;

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
        initTextReact();
        createEventBus();

        btnStartEventBus_AddListener();
        SendDataToEventBus_AddListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        disposable = stringObservable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) {
                textView.setText(s);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        disposable.dispose();
    }

    private void initTextReact() {
        Observer<String> onTextChange = new Observer<String>() {
            @Override
            public void onComplete() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(String s) {
                textView.setText(s);
            }
        };

        stringObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) {
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                        emitter.onNext(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
            }
        });

        stringObservable.subscribe(onTextChange);
    }

    private void createEventBus() {
        eventBus = new EventBus();
    }

    private void initViews() {
        editText = findViewById(R.id.editTextLsn2);
        textView = findViewById(R.id.textViewLsn2);
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
        if (eventBus != null) {
            eventBus.sendData(100L);
        }
    }

    private void startEventBus() {
        Observable<Long> observable1 = Observable
                .interval(1, TimeUnit.SECONDS)
                .take(5);

        Observable<Long> observable2 = Observable
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

        eventBus.subscribeObservable(observable1);
        eventBus.subscribeObservable(observable2);

        Toast.makeText(Main2Activity.this, "EventBus started!", Toast.LENGTH_SHORT).show();

        eventBus.subscribeObserver(observer1);
        eventBus.subscribeObserver(observer2);
    }
}