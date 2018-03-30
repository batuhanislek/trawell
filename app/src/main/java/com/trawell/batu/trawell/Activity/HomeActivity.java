package com.trawell.batu.trawell.Activity;


import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;


import com.trawell.batu.trawell.Fragments.ActionFragment;
import com.trawell.batu.trawell.Fragments.HomeFragment;
import com.trawell.batu.trawell.Fragments.CurrentFragment;
import com.trawell.batu.trawell.Fragments.DiscoverFragment;
import com.trawell.batu.trawell.Fragments.ProfileFragment;
import com.trawell.batu.trawell.R;

public class HomeActivity extends AppCompatActivity {

    public Button signoutButton;
    public TextView name;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.nav_home:
                    HomeFragment fragmentHome = new HomeFragment();
                    FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionHome.replace(R.id.frame_layout, fragmentHome, "FragmentName");
                    fragmentTransactionHome.commit();
                    return true;
                case R.id.nav_discover:
                    DiscoverFragment fragmentDiscover = new DiscoverFragment();
                    FragmentTransaction fragmentTransactionDiscover = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionDiscover.replace(R.id.frame_layout, fragmentDiscover, "FragmentName");
                    fragmentTransactionDiscover.commit();
                    return true;
                case R.id.add_action:
                    ActionFragment fragmentAction = new ActionFragment();
                    FragmentTransaction fragmentTransactionAction = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionAction.replace(R.id.frame_layout, fragmentAction, "FragmentName");
                    fragmentTransactionAction.commit();
                    return true;
                case R.id.nav_current:
                    CurrentFragment fragmentCurrent = new CurrentFragment();
                    FragmentTransaction fragmentTransactionCurrent = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionCurrent.replace(R.id.frame_layout, fragmentCurrent, "HomeFragment");
                    fragmentTransactionCurrent.commit();
                    return true;
                case R.id.nav_profile:
                    ProfileFragment fragmentProfile = new ProfileFragment();
                    FragmentTransaction fragmentTransactionProfile = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionProfile.replace(R.id.frame_layout, fragmentProfile, "HomeFragment");
                    fragmentTransactionProfile.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigationBar = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigationBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        HomeFragment fragmentHome = new HomeFragment();
        FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
        fragmentTransactionHome.replace(R.id.frame_layout, fragmentHome, "HomeFragment");
        fragmentTransactionHome.commit();

    }
}
