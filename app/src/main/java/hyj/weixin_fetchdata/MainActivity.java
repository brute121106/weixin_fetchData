package hyj.weixin_fetchdata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button)findViewById(R.id.open_assist);
        btn.setOnClickListener(tbnListen);
    }
    private View.OnClickListener tbnListen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS));
            Toast.makeText(MainActivity.this, "找到对应用名称开启辅助权限", Toast.LENGTH_LONG).show();
        }
    };
}
