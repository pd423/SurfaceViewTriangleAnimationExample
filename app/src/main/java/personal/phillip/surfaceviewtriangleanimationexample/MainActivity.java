package personal.phillip.surfaceviewtriangleanimationexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ExampleSurfaceView view = new ExampleSurfaceView(this);
        setContentView(view);
    }
}
