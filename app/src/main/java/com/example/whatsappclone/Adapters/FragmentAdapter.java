package com.example.whatsappclone.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.whatsappclone.Fragments.CallFragment;
import com.example.whatsappclone.Fragments.ChatFragment;
import com.example.whatsappclone.Fragments.StatusFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new ChatFragment();
            case 1: return new StatusFragment();
            case 2: return new CallFragment();
            default: return new ChatFragment();


        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

       String title=null;

       if (position==0)
       {
           title="Chats";
       }
        if (position==1)
        {
            title="Status";
        }
        if (position==2)
        {
            title="Calls";
        }

        return title;
    }
}
