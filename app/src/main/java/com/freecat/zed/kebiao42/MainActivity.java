package com.freecat.zed.kebiao42;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private List<View> viewList;

    private InfoAll infoAll;
    private List<InfoAll> infoAllList;


    private String jsonA, jsonB;
    private List<String> json;

    private final String A = "jsonA";
    private final String B = "jsonB";
    private final String C = "jsonC";

    private final String actionA = "course";
    private final String actionB = "grade";


    private int[] rlId = {R.id.weekPanel_1, R.id.weekPanel_2, R.id.weekPanel_3, R.id.weekPanel_4, R.id.weekPanel_5, R.id.weekPanel_6, R.id.weekPanel_7};
    private RelativeLayout[] rl = new RelativeLayout[7];


    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        json = new ArrayList<>();
        viewList = new ArrayList<>();
        infoAllList = new ArrayList<>();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkJsonIsEmpty4Login();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {

            }

            //show student info
            public void onDrawerOpened(View drawerView) {
                TextView tv1 = (TextView) findViewById(R.id.nhm_a);
                TextView tv2 = (TextView) findViewById(R.id.nhm_b);
                Student student = infoAll.getStudent();
                tv1.setText(student.getS_n() + "@" + student.getS_cN());
                tv2.setText(student.getS_s() + student.getS_c());
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        setupContainer();

    }

    /**
     * set up the viewpager
     */
    public void setupContainer() {

        if (checkJsonIsEmpty4Layout()) {

            PagerAdapter pagerAdapter = new PagerAdapter() {

                @Override
                public boolean isViewFromObject(View arg0, Object arg1) {
                    // TODO Auto-generated method stub
                    return arg0 == arg1;
                }

                @Override
                public int getCount() {
                    // TODO Auto-generated method stub
                    return viewList.size();
                }

                @Override
                public void destroyItem(ViewGroup container, int position,
                                        Object object) {
                    // TODO Auto-generated method stub

                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    // TODO Auto-generated method stub
                    container.addView(viewList.get(position));
                    return viewList.get(position);
                }
            };
            // Set up the ViewPager with the sections adapter.


            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(pagerAdapter);
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    infoAll = infoAllList.get(position);
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("filename", data1);
        intent.putExtra("action", data2);
        context.startActivity(intent);
    }


    /**
     * check if there exists saved file 4 another login
     */
    public void checkJsonIsEmpty4Login() {
        jsonA = loadJson(A);
        jsonB = loadJson(B);

        if (TextUtils.isEmpty(jsonA)) {
            actionStart(MainActivity.this, A, actionA);
            /*Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("filename", A);
            startActivity(intent);*/
            finish();
        } else if (TextUtils.isEmpty(jsonB)) {
            actionStart(MainActivity.this, B, actionA);
            /*Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("filename", B);
            startActivity(intent);
            finish();*/
        }
    }


    /**
     * check if there exists saved file 4 layout
     */
    public boolean checkJsonIsEmpty4Layout() {

        boolean flag = false;
        jsonA = loadJson(A);
        jsonB = loadJson(B);
        infoAllList.clear();
        viewList.clear();
        json.clear();

        if (TextUtils.isEmpty(jsonA) && TextUtils.isEmpty(jsonB)) {
            actionStart(MainActivity.this, A, actionA);
            /*Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("filename", A);
            startActivity(intent);*/
            finish();
            flag = false;
        }
        if (!TextUtils.isEmpty(jsonA)) {
            useJson2getView(jsonA, A);
            flag = true;
        }
        if (!TextUtils.isEmpty(jsonB)) {
            useJson2getView(jsonB, B);
            flag = true;
        }
        return flag;
    }

    /**
     * inflate a view by content_main_course.xml
     *
     * @param jsonData
     * @param finalString
     */
    public void useJson2getView(String jsonData, String finalString) {
        LayoutInflater inflater = getLayoutInflater();
        View content_main_course = inflater.inflate(R.layout.content_main_course, null);
        Gson gson = new Gson();
        InfoAll infoAll = gson.fromJson(jsonData, InfoAll.class);
        setupCourseView(infoAll, content_main_course);
        viewList.add(content_main_course);
        infoAllList.add(infoAll);
        json.add(finalString);
    }


    /**
     * set up a view by a given infoAll
     *
     * @param infoAll
     * @param view
     */
    public void setupCourseView(InfoAll infoAll, View view) {
        final int itemHeight = 54;
        final int marTop = 2;
        final int marLeft = 2;

        List<Course> courseList = infoAll.getCourse_all();
        for (Course c : courseList) {
            int i = c.getC_w();
            rl[i] = (RelativeLayout) view.findViewById(rlId[i - 1]);
            TextView tv = new TextView(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, Dp2Px(this, (itemHeight * c.getC_p() + marTop * (c.getC_p() - 1))));
            lp.setMargins(marLeft, Dp2Px(this, (c.getC_p_s() - 1) * (itemHeight + marTop) + marTop), 0, 0);
            tv.setLayoutParams(lp);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(12);
            tv.setTextColor(getResources().getColor(R.color.courseTextColor));
            tv.setText(c.getC_n() + "\n" + c.getC_l() + "\n" + c.getC_t());
            tv.setBackground(getResources().getDrawable(R.drawable.tvshape));
            rl[i].addView(tv);

        }
    }

    /**
     * load json data by a given filename
     *
     * @param filename
     * @return
     */
    public String loadJson(String filename) {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();

        try {

            in = openFileInput(filename);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return content.toString();
    }

    /**
     * delete json data by a given filename
     *
     * @param filename
     * @return if deleted returns true，else returns false
     */
    public boolean deleteJson(String filename) {
        if (Mapplication.getContext().deleteFile(filename)) {
            return true;
        }
        return false;
    }

    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int i = 0;
        Student s1 = new Student();
        Student s2 = new Student();
        Student[] s = {s1, s2};
        for (InfoAll ia : infoAllList) {
            s[i] = ia.getStudent();
            menu.add(0, i + 1, i + 1, "注销" + s[i].getS_n() + "的课表");
            i++;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == 1) {
            String s = json.get(0);
            if (deleteJson(s)) {
                Toast.makeText(Mapplication.getContext(), getString(R.string.delete_successful), Toast.LENGTH_SHORT).show();
                toolbar.getMenu().removeItem(1);
                setupContainer();
            } else {
                Toast.makeText(Mapplication.getContext(), getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
            }


            return true;
        }
        if (id == 2) {
            String s = json.get(1);
            if (deleteJson(s)) {
                Toast.makeText(Mapplication.getContext(), getString(R.string.delete_successful), Toast.LENGTH_SHORT).show();
                toolbar.getMenu().removeItem(2);
                setupContainer();
            } else {
                Toast.makeText(Mapplication.getContext(), getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_grade) {
            actionStart(MainActivity.this, C, actionB);
            /*Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);*/

        } else if (id == R.id.nav_test) {

        } else if (id == R.id.nav_course) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
