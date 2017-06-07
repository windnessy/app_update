package wind.myappupdate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(MainActivity.this,"检查版本",Toast.LENGTH_SHORT).show();
        CheckVersion checkVersion = new CheckVersion();
        checkVersion.check(MainActivity.this);
    }
}
