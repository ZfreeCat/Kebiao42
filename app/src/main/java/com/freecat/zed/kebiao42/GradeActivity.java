package com.freecat.zed.kebiao42;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class GradeActivity extends AppCompatActivity {

    private String grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_grade);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) (display.getHeight() * 0.9);
        params.width = (int) (display.getWidth() * 0.9);
        params.alpha = 1.0f;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.CENTER);


        RecyclerView listView = (RecyclerView) findViewById(R.id.listView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(mLayoutManager);

        Intent intent = getIntent();
        grade = intent.getStringExtra("grade");
        Gson gson = new Gson();
        List<Grade> gradeList = gson.fromJson(grade, new TypeToken<List<Grade>>() {
        }.getType());
        MyAdapter mAdapter = new MyAdapter(gradeList);
        listView.setAdapter(mAdapter);
        listView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

    }

    /**
     * This class is from the v7 samples of the Android SDK. It's not by me!
     * <p/>
     * See the license above for details.
     */
    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };

        public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

        public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

        private Drawable mDivider;

        private int mOrientation;

        public DividerItemDecoration(Context context, int orientation) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
            setOrientation(orientation);
        }

        public void setOrientation(int orientation) {
            if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
                throw new IllegalArgumentException("invalid orientation");
            }
            mOrientation = orientation;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent) {

            if (mOrientation == VERTICAL_LIST) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }

        }


        public void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                android.support.v7.widget.RecyclerView v = new android.support.v7.widget.RecyclerView(parent.getContext());
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        public void drawHorizontal(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getRight() + params.rightMargin;
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<Grade> mGradeList;

        public MyAdapter(List<Grade> list) {
            this.mGradeList = list;
        }

        @Override
        public int getItemCount() {
            // TODO Auto-generated method stub
            return mGradeList.size()-1;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            // TODO Auto-generated method stub
            Grade grade = mGradeList.get(position);

            viewHolder.g_yg_t.setText(grade.g_y + "@" + grade.g_t);
            viewHolder.g_cn.setText(grade.g_cn);
            viewHolder.g_p.setText(grade.g_p);
            viewHolder.g_ct.setText(grade.g_ct);
            viewHolder.g_cg.setText(grade.g_cg);
            viewHolder.g_fg.setText(grade.g_fg);
            viewHolder.g_ng.setText(grade.g_ng);

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
            // TODO Auto-generated method stub
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.vlist, null);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            view.setLayoutParams(p);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView g_yg_t, g_cn, g_p, g_ct, g_cg, g_fg, g_ng;

            public ViewHolder(View view) {
                super(view);
                // TODO Auto-generated constructor stub
                g_yg_t = (TextView) view.findViewById(R.id.g_yg_t);
                g_cn = (TextView) view.findViewById(R.id.g_cn);
                g_p = (TextView) view.findViewById(R.id.g_p);
                g_ct = (TextView) view.findViewById(R.id.g_ct);
                g_cg = (TextView) view.findViewById(R.id.g_cg);
                g_fg = (TextView) view.findViewById(R.id.g_fg);
                g_ng = (TextView) view.findViewById(R.id.g_ng);
            }

        }

    }
}
