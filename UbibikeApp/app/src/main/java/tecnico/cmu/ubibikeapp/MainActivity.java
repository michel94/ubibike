package tecnico.cmu.ubibikeapp;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import tecnico.cmu.ubibikeapp.tabs.BikeActivityFragment;
import tecnico.cmu.ubibikeapp.tabs.FriendsFragment;
import tecnico.cmu.ubibikeapp.tabs.HomeFragment;
import tecnico.cmu.ubibikeapp.tabs.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private CollectionPagerAdapter mCollectionPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getSupportActionBar();
        mCollectionPagerAdapter =
                new CollectionPagerAdapter(
                        getSupportFragmentManager());

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(123, 233, 122)));
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPagerAdapter);
        mViewPager.setOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );


        setTabListener(actionBar);

        String username = Utils.getUsername(this);
        String password = Utils.getPassword(this);
        if(username == null){
            Log.d("Main", "username not defined!");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    public void setTabListener(ActionBar actionBar) {
        // Add 3 tabs, specifying the tab's text and TabListener

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        ArrayList<Drawable> icons = new ArrayList<>();
        icons.add(getResources().getDrawable(R.drawable.ic_home_white_36dp));
        icons.add(getResources().getDrawable(R.drawable.ic_directions_bike_white_36dp));
        icons.add(getResources().getDrawable(R.drawable.ic_people_white_36dp));
        icons.add(getResources().getDrawable(R.drawable.ic_person_white_36dp));


        for (Drawable icon: icons) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setTabListener(tabListener)
                            .setIcon(icon));
        }

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.

    }

    public static class CollectionPagerAdapter extends FragmentPagerAdapter {

        private static final int NR_PAGES = 4;

        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position){
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new BikeActivityFragment();
                    break;
                case 2:
                    fragment = new FriendsFragment();
                    break;
                case 3:
                    fragment = new ProfileFragment();
                    break;
                default:
                    return null;
            }
            Bundle args = new Bundle();
            // Our object is just an integer :-P
            //args.putInt(DemoObjectFragment.ARG_OBJECT, position + 1);
            //fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return NR_PAGES;
        }
    }
}
