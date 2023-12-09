package com.example.bbcviewer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar tbar = findViewById(R.id.toolbar);
        Button b1 = findViewById(R.id.but_help);
        b1.setOnClickListener((click) -> {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);//Alert dialog displays help in the form of multiple actions user can take
            adb.setTitle("Help")
                    .setMessage("To access options: Press icon in top left."+"\n\n"+
                    "To access BBC articles to read: Press 'Article' in options, choose your desired article, and click the link if you wish to read further.\n\n"+
                    "To access your Favourite articles: Press 'Favourites' in options, and follow same directions for opening other articles.\n\n"+
                    "To close the app: Press 'Exit'").setNegativeButton("Understood", (click1, arg) ->{});
            adb.create().show();

        });
        setSupportActionBar(tbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawer = new ActionBarDrawerToggle(this, drawer, tbar, R.string.open, R.string.close);
        drawer.addDrawerListener(actionBarDrawer);
        actionBarDrawer.syncState();
        NavigationView nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);//Toolbar and navigation drawer instantiated





    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {//adds functionality to drawer items
        int id = item.getItemId();
        if (id == R.id.nav_articles){
            Intent intent = new Intent(this, ArticleActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_favs) {
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
}
