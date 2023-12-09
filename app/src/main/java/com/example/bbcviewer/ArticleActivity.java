package com.example.bbcviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ArticleActivity extends MainActivity {
    ArrayList<String> titles;
    ArrayList<String> descs;
    ArrayList<String> urls;
    ListView lv;
    ProgressBar pbar = findViewById(R.id.pbar1);
    Snackbar snackbar = Snackbar.make(ArticleActivity.this.getCurrentFocus(), "Articles finished populating", Snackbar.LENGTH_LONG);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_list);
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


        new BackgroundProcess().execute();


        lv.setOnItemLongClickListener((parent, view, pos, id) -> {//loads data loads title, description and url into sharedprefs to be used in next activity
            String title = titles.get(pos);
            String desc = descs.get(pos);
            String uri = urls.get(pos);
            SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("title", title );
            edit.putString("desc", desc );
            edit.putString("uri", uri );
            edit.commit();
            Toast.makeText(getApplicationContext(), title+" "+desc+" "+uri, Toast.LENGTH_LONG).show();//example toast shows
            Intent intent = new Intent(ArticleActivity.this, DetailsActivity.class);
            startActivity(intent);

            return false;
        });


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

    public InputStream getInputStream(URL url) {//method opens connection to newsfeed
        try {
            return url.openConnection().getInputStream();

        } catch (IOException e) {
            return null;
        }
    }

    public class BackgroundProcess extends AsyncTask<Integer, Integer, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbar.setMax(100);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pbar.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(Integer... integers) {//fills arraylists with data from newsfeed
            for(int i=0; i<100; i++){
                publishProgress(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                URL url = new URL("https://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(getInputStream(url), "UTF_8");
                boolean insideItem = false;
                int eType = xpp.getEventType();
                while (eType != XmlPullParser.END_DOCUMENT) {
                    if (eType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                titles.add(xpp.nextText());
                            }

                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            if (insideItem) {
                                descs.add(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem) {
                                urls.add(xpp.nextText());
                            }
                        }
                    } else if (eType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                    }
                    eType = xpp.next();

                }


                return null;
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (XmlPullParserException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


            @Override
            protected void onPostExecute (String s){//loads listview with data
                super.onPostExecute(s);
                ArrayAdapter<String> aDap = new ArrayAdapter<>(ArticleActivity.this, android.R.layout.simple_list_item_1, titles);
                lv.setAdapter(aDap);
                snackbar.show();//Snackbar occurs

            }
        }
    }


