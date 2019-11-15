package halla.icsw.kkh_mainproject_1114;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private CustomAdapter adapter;
    private LinearLayout audio;
    private LinearLayout book;
    private ListView audioListView;
    private ListView bookListView;
    private ListView bestRankListView;



    String strHtml = "";
    String[] data2= new String[21];
    String[] data= new String[21];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //파싱하기 위한 핸들러
        Handler h = new Handler() {
            public void handleMessage(Message msg) {
                HTMLParsing();
            } };
        new WorkerThread(h).start();


        //findViewByID부분
        Button bookButton=findViewById(R.id.bookButton);
        Button audioButton=findViewById(R.id.audioButton);
        Button bestRankButton=findViewById(R.id.bestRankButton);
        Button mapButton = findViewById(R.id.mapButton);
        ImageButton searchButton = findViewById(R.id.searchButton);
        Button signUpButton = findViewById(R.id.signUpButton);
        Button logInButton = findViewById(R.id.loginButton);
        final EditText searchText = findViewById(R.id.searchText);

        book=findViewById(R.id.book);
        audio=findViewById(R.id.audio);
        audioListView=findViewById(R.id.audioListView);
        bookListView=findViewById(R.id.bookListView);
        bestRankListView=findViewById(R.id.bestRankListView);


        searchText.clearFocus();


        //searchButton 클릭시
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(searchText.getText().toString().length()==0) {
                    Intent intent = new Intent(MainActivity.this, GenreSearchActivity.class);
                    startActivity(intent);
                }

            }
        });

        //회원가입 버튼 클릭시
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼 클릭시
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        //오디오button 클릭시 스크롤뷰가 보이게함, 안보이게 함
        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book.setVisibility(View.GONE);
                audio.setVisibility(View.VISIBLE);
                audioListView.setVisibility(View.VISIBLE);
                bookListView.setVisibility(View.GONE);
                bestRankListView.setVisibility(View.GONE);
            }
        });

        //북button 클릭시 스크롤뷰가 보이게함, 안보이게 함
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio.setVisibility(View.GONE);
                book.setVisibility(View.VISIBLE);
                audioListView.setVisibility(View.GONE);
                bookListView.setVisibility(View.VISIBLE);
                bestRankListView.setVisibility(View.GONE);
            }
        });

        //베스트셀러button 클릭시
        bestRankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioListView.setVisibility(View.GONE);
                bookListView.setVisibility(View.GONE);
                bestRankListView.setVisibility(View.VISIBLE);
            }
        });

        //mapButton 클릭시
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });


        //오디오북 list받아오고, tts기능까지 Intent 사용하여 넘겨줌.
        adapter = new CustomAdapter();
        audioListView = (ListView) findViewById(R.id.audioListView);
        setData();
        audioListView.setAdapter(adapter);

        audioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String titleRes = ((CustomDTO)adapter.getItem(position)).getTitle();

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("titleRes", titleRes);
                startActivity(intent);
            }
        });
    }

    //오디오북 데이타
    private void setData() {
        TypedArray arrResId = getResources().obtainTypedArray(R.array.resId);
        String[] titles = getResources().getStringArray(R.array.title);
        String[] contents = getResources().getStringArray(R.array.content);

        for (int i = 0; i < arrResId.length(); i++) {
            CustomDTO dto = new CustomDTO();
            dto.setResId(arrResId.getResourceId(i, 0));
            dto.setTitle(titles[i]);
            dto.setContent(contents[i]);

            adapter.addItem(dto);
        }
    }

    //베스트셀러 파싱을 위한 스레드
    class WorkerThread extends Thread {
        Handler h;
        String strLine;
        WorkerThread(Handler h) { this.h = h; }
        public void run() {
            try {
                URL aURL = new URL("https://www.aladin.co.kr/m/mbest.aspx?BranchType=1&start=momain");
                BufferedReader in = new BufferedReader(new
                        InputStreamReader( aURL.openStream() ));
                while ((strLine = in.readLine()) != null)
                    if(strLine.contains(" style=\"line-height:120%;"))
                        strHtml += strLine;
                in.close();
                h.sendMessage(new Message());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"네트워크 에러 : " + e.toString(),Toast.LENGTH_SHORT).show();

            }
        }
    }

    //베스트셀러 파싱
    void HTMLParsing() {
        try {
            int start = 0, end = 0;

//            String[] data2;
            for (int i = 1; i <= 20; i++) {
                start = strHtml.indexOf(" style=\"line-height:120%;", end);
                end = strHtml.indexOf("<", start);

                data[i] = i + "위 : " + strHtml.substring(start + 31, end);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"파싱 에러 : " + e.toString(),Toast.LENGTH_SHORT).show();
        }
        String data2[] = {data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12],
                data[13], data[14], data[15], data[16], data[17], data[18], data[19], data[20]};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data2);
        bestRankListView.setAdapter(adapter);
    }




}

