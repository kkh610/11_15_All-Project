package halla.icsw.kkh_mainproject_1114;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {

    dbHelper helper;
    SQLiteDatabase db;
    EditText id,pw;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button login = findViewById(R.id.login);
        id = findViewById(R.id.l_id);
        pw = findViewById(R.id.l_pw);
        helper = new dbHelper(this);
        db = helper.getWritableDatabase();
        b1 = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
    public void login(View v){
        String a = id.getText().toString();
        String b = pw.getText().toString();
        Cursor cursor = db.rawQuery("SELECT _id,password FROM 회원 WHERE _id = '" + a + "' AND password = '" + b + "';",null);
        if(cursor.getCount() > 0){
            Toast.makeText(this,"로그인되었습니다.",Toast.LENGTH_LONG).show();
        }
    }
    public void member(View v){
        Intent intent = new Intent(LogInActivity.this,SignUpActivity.class);
        startActivity(intent);
    }

}
