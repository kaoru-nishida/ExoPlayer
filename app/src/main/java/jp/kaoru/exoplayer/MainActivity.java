package jp.kaoru.exoplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private static  final String TAG = "MainActivity";
    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences data = getSharedPreferences("url", Context.MODE_PRIVATE);
        ((EditText)findViewById(R.id.editText)).setText(data.getString("url",""));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("video/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] members = { "通常", "HLS - ストリーミング" };
                ListView lv = new ListView(MainActivity.this);
                ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_expandable_list_item_1, members);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.i(TAG, String.valueOf(i));
                        String url = ((EditText)findViewById(R.id.editText)).getText().toString();
                        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                        SharedPreferences data = getSharedPreferences("url", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = data.edit();
                        switch(i){
                            case 0:
                                Log.i(TAG,url.toString());
                                intent.putExtra("type","OTHER");
                                intent.putExtra("url",url);
                                editor.putString("url", url);
                                editor.apply();
                                startActivity(intent);
                                break;
                            case 1:
                                Log.i(TAG,url);
                                intent.putExtra("type","HLS");
                                intent.putExtra("url",url);
                                editor = data.edit();
                                editor.putString("url", url);
                                editor.apply();
                                startActivity(intent);
                                break;
                        }
                    }
                });
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("種類を選択")
                        .setCancelable(true)
                        .setView(lv).create();
                dialog.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.toString());
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("url",uri.toString());
                intent.putExtra("type","OTHER");
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
