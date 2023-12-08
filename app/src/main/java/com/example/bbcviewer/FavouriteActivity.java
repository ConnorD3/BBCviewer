package com.example.bbcviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class FavouriteActivity extends MainActivity {
    ArrayList<String> titles;
    ArrayList<String> descs;
    ArrayList<String> urls;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_list);
        Toolbar tbar = findViewById(R.id.toolbar);
        Button b1 = findViewById(R.id.but_help);
        b1.setOnClickListener((click) -> {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Help")
                    .setMessage("To access options: Press icon in top left." + "\n\n" +
                            "To access BBC articles to read: Press 'Article' in options, choose your desired article, and click the link if you wish to read further.\n\n" +
                            "To access your Favourite articles: Press 'Favourites' in options, and follow same directions for opening other articles.\n\n" +
                            "To close the app: Press 'Exit'").setNegativeButton("Understood", (click1, arg) -> {
                    });
            adb.create().show();

        });
        setSupportActionBar(tbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawer = new ActionBarDrawerToggle(this, drawer, tbar, R.string.open, R.string.close);
        drawer.addDrawerListener(actionBarDrawer);
        actionBarDrawer.syncState();
        NavigationView nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        lv = findViewById(R.id.artList);
        titles = new ArrayList<>();
        descs = new ArrayList<>();
        urls = new ArrayList<>();
        dataGrab();
        lv.setOnItemLongClickListener((parent, view, pos, id) -> {
            String title = titles.get(pos);
            String desc = descs.get(pos);
            String uri = urls.get(pos);
            SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("title", title );
            edit.putString("desc", desc );
            edit.putString("uri", uri );
            edit.commit();
            Toast.makeText(getApplicationContext(), title+" "+desc+" "+uri, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(FavouriteActivity.this, DetailsActivity.class);
            startActivity(intent);

            return false;
        });


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_articles) {
            Intent intent = new Intent(this, ArticleActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_favs) {
            Intent intent = new Intent(this, FavouriteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            this.finishAffinity();

        }
        return false;
    }
    private void dataGrab(){
        DBHelper db = new DBHelper(this);
        Cursor cursor = db.popList();
        if (cursor.getCount() == 0){
            Log.d("tag", "nothing to populate ");
        }else{
            while(cursor.moveToNext()){
                titles.add(cursor.getString(1));
                descs.add(cursor.getString(2));
                urls.add(cursor.getString(3));

            }
            ArrayAdapter<String> aDap = new ArrayAdapter<>(FavouriteActivity.this, android.R.layout.simple_list_item_1, titles);
            lv.setAdapter(aDap);
        }
    }
    }

