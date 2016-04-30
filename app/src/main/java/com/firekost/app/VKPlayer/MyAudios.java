package com.firekost.app.VKPlayer;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class MyAudios extends AppCompatActivity {

    private ImageView search;
    private EditText editText;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_audios);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        spinner = (Spinner) findViewById(R.id.spinner);
        //spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        String[] title = {"ВКонтакте", "Устройство"};
        ArrayAdapter<String> titleArray = new ArrayAdapter<>(this, R.layout.spinner, title);
        spinner.setAdapter(titleArray);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        search = (ImageView) findViewById(R.id.search);
        editText = (EditText) findViewById(R.id.editText);
        editText.setVisibility(View.GONE);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabMyAudios(), "Аудиозаписи");
        adapter.addFragment(new TabMyRecs(), "Рекомендации");
        adapter.addFragment(new TabPopular(), "Популярное");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void onSearchClick (View v){
        if (editText.getVisibility() == View.GONE){
            spinner.setVisibility(View.GONE);
            search.setImageResource(R.drawable.close);
            editText.setVisibility(View.VISIBLE);
        } else {
            search.setImageResource(R.drawable.search);
            editText.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            editText.setText("");
        }
    }
}