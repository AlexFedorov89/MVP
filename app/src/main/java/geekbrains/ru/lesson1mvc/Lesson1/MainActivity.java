package geekbrains.ru.lesson1mvc.Lesson1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import geekbrains.ru.lesson1mvc.Lesson1.MainView;
import geekbrains.ru.lesson1mvc.Lesson1.Presenter;
import geekbrains.ru.lesson1mvc.R;


public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {
    private String TAG = "ERROR";

    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initPresenter();
    }

    private void initPresenter() {
        presenter = new Presenter(this);
    }

    private void initViews() {
        findViewById(R.id.btnCounter1).setOnClickListener(this);
        findViewById(R.id.btnCounter2).setOnClickListener(this);
        findViewById(R.id.btnCounter3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        presenter.onClick(v.getId());
    }

    @Override
    public void setButtonText(int id, int value) {
        Button button = findViewById(id);
        if (button == null) {
            String msgError = String.format("Error, button with id %d not found!", id);
            Log.d(TAG, msgError);
            Toast.makeText(this, msgError, Toast.LENGTH_SHORT).show();

            return;
        }
        button.setText("Количество = " + value);
    }
}

