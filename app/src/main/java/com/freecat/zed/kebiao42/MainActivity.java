package com.freecat.zed.kebiao42;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
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
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private List<View> viewList;//view数组

    private InfoAll infoAll, infoAllA, infoAllB;
    private List<InfoAll> infoAllList;


    private String jsonA, jsonB;
    private List<String> json;

    private int itemHeight = 56;
    private int marTop = 2;
    private int marLeft = 2;

    private int[] rlId = {R.id.weekPanel_1, R.id.weekPanel_2, R.id.weekPanel_3, R.id.weekPanel_4, R.id.weekPanel_5, R.id.weekPanel_6, R.id.weekPanel_7};
    private RelativeLayout[] rl = new RelativeLayout[7];
    private View content_main_courseA;
    private View content_main_courseB;


    private Student s1 = new Student();
    private Student s2 = new Student();
    private Student[] s = {s1, s2};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        json = new ArrayList<>();
        viewList = new ArrayList<>();// 将要分页显示的View装入数组中
        infoAllList = new ArrayList<>();

        LayoutInflater inflater = getLayoutInflater();
        content_main_courseA = inflater.inflate(R.layout.content_main_course, null);
        content_main_courseB = inflater.inflate(R.layout.content_main_course, null);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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


        /**
         *
         */

        PagerAdapter pagerAdapter=refresh();

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

    public PagerAdapter refresh(){

        checkJsonIsEmpty4Layout();


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
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };
        return pagerAdapter;
    }

    public void checkJsonIsEmpty4Login() {
        jsonA = loadJson("jsonA");
        jsonB = loadJson("jsonB");
        if (TextUtils.isEmpty(jsonA)) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("filename", "jsonA");
            startActivity(intent);
            finish();
        }
        if (TextUtils.isEmpty(jsonB)) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("filename", "jsonB");
            startActivity(intent);
            finish();
        }
    }

    public void checkJsonIsEmpty4Layout() {
        jsonA = loadJson("jsonA");
        jsonB = loadJson("jsonB");

        if (TextUtils.isEmpty(jsonA) && TextUtils.isEmpty(jsonB)) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("filename", "jsonA");
            startActivity(intent);
            finish();
        }
        if (!TextUtils.isEmpty(jsonA)) {
            Gson gson = new Gson();
            infoAllA = gson.fromJson(jsonA, InfoAll.class);
            getCourseLayout(infoAllA, content_main_courseA);
            viewList.add(content_main_courseA);
            infoAllList.add(infoAllA);
            json.add("jsonA");
        }
        if (!TextUtils.isEmpty(jsonB)) {
            Gson gson = new Gson();
            infoAllB = gson.fromJson(jsonB, InfoAll.class);
            getCourseLayout(infoAllB, content_main_courseB);
            viewList.add(content_main_courseB);
            infoAllList.add(infoAllB);
            json.add("jsonB");
        }
    }


    public void getCourseLayout(InfoAll infoAll, View view) {
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
            //tv.setBackgroundColor(getResources().getColor(R.color.classIndex));
            tv.setBackground(getResources().getDrawable(R.drawable.tvshape));
            rl[i].addView(tv);

        }
    }

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
     * 删除单个文件
     *
     * @param filename 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
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

    public void setupMenu(Menu menu){
        int i=0;
        for (InfoAll ia : infoAllList) {
            s[i] = ia.getStudent();
            menu.add(0, i + 1, i + 1, "注销" + s[i].getS_n() + "的课表");
            i++;
        }
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
        setupMenu(menu);
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
                Toast.makeText(Mapplication.getContext(),  "成功删除！", Toast.LENGTH_SHORT).show();
                mViewPager.removeView(viewList.get(0));
                mViewPager.setAdapter(refresh());
            } else {
                Toast.makeText(Mapplication.getContext(),  "删除失败！", Toast.LENGTH_SHORT).show();
            }


            return true;
        }
        if (id == 2) {
            String s = json.get(1);
            if (deleteJson(s)) {
                Toast.makeText(Mapplication.getContext(),  "成功删除！", Toast.LENGTH_SHORT).show();
                mViewPager.removeView(viewList.get(1));
                refresh();
            } else {
                Toast.makeText(Mapplication.getContext(),"删除失败！", Toast.LENGTH_SHORT).show();
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

        } else if (id == R.id.nav_test) {

        } else if (id == R.id.nav_course) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
