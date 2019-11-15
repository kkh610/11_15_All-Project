package halla.icsw.kkh_mainproject_1114;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

class dbHelper extends SQLiteOpenHelper {


    public dbHelper(Context context){
        super(context,"login.db",null,1);

    }

    public void onCreate(SQLiteDatabase db){
        String a = "CREATE TABLE 회원 (_id text PRIMARY KEY,"+
                "password text,name text,bri text,gender int,genre text);";
        db.execSQL(a);


    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }
}


public class SignUpActivity extends AppCompatActivity {
    dbHelper helper;
    SQLiteDatabase db;
    EditText id,password_2,password_1,bir,name;
    RadioGroup rg;
    RadioButton men,women;
    int gender;
    CheckBox c1,c2,c3,c4,c5,c6,c7,c8;
    CheckBox[] checkBoxes;
    String genre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        helper = new dbHelper(SignUpActivity.this);
        db = helper.getWritableDatabase();
        id = findViewById(R.id.ide);
        password_1 = findViewById(R.id.password_1);
        password_2 = findViewById(R.id.password_2);
        name = findViewById(R.id.name_1);
        bir = findViewById(R.id.bir_1);
        men = findViewById(R.id.men);
        women = findViewById(R.id.women);
        c1 = findViewById(R.id.checkBox);c2 = findViewById(R.id.checkBox2);c3 = findViewById(R.id.checkBox3);c4 = findViewById(R.id.checkBox4);
        c5 = findViewById(R.id.checkBox5);c6 = findViewById(R.id.checkBox6);c7 = findViewById(R.id.checkBox7);c8 = findViewById(R.id.checkBox8);

        checkBoxes = new CheckBox[]{c1,c2,c3,c4,c5,c6,c7,c8};
    }

    public void member(View view) {
        String id1 = id.getText().toString();
        String password1 = password_1.getText().toString();
        String password2 = password_2.getText().toString();
        String name1 = name.getText().toString();
        String bir1 = bir.getText().toString();
        if (men.isChecked())
            gender = 1;
        if (women.isChecked())
            gender = 2;
        for (int i = 0; i < 8; i++) {
            if (checkBoxes[i].isChecked()) {
                if (i == 7)
                    genre += checkBoxes[i].getText().toString();
                else
                    genre +=  checkBoxes[i].getText().toString() +",";
            }
        }
        if (!password1.equals(password2)) {
            Toast.makeText(this, "비밀번호를 다시 확인해주세요", Toast.LENGTH_LONG).show();
        } else {
            db.execSQL("INSERT INTO 회원 VALUES ('"+ id1 + "','" + password1 + "','" + name1 + "','" + bir1 + "','"  + gender + "','" +  genre + "')");
            Toast.makeText(this,"가입되었습니다.",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SignUpActivity.this,LogInActivity.class);
            startActivity(intent);
        }
    }
}
