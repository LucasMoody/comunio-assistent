package de.lucaspradel.comunioassistent.dailytransfermarket.view;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.lucaspradel.comunioassistent.dailytransfermarket.helper.UserInfo;

/**
 * Created by lucas on 10.06.15.
 */
public class MarketPagerAdapter extends PagerAdapter {

    private static final String COMUNIO_USER_INFOS_FILENAME = "comunioUserInfos";
    List<UserInfo> userInfos;

    private static final String TAG = "MarketPagerAdapter";
    private static final boolean DEBUG = false;

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;
    Context context;

    protected ArrayList<Fragment> fragments = new ArrayList<>();

    public MarketPagerAdapter(Context context, FragmentManager fm) {
        this.context = context;
        mFragmentManager = fm;

        loadUserInfos();
        for (UserInfo ui : userInfos) {
            addFragment(TransferMarket.newInstance(ui.getId(), 90, true));
        }
    }

    private void loadUserInfos() {
        userInfos = new ArrayList<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(context.openFileInput(COMUNIO_USER_INFOS_FILENAME));
            userInfos = (List<UserInfo>) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {

        }
    }

    public void saveUserInfos() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(COMUNIO_USER_INFOS_FILENAME, Context.MODE_PRIVATE));
            oos.writeObject(userInfos);
            oos.close();
        } catch (IOException e) {
            Toast.makeText(context, "Saving of comunio user was not succesful. Please contact the vendor.", Toast.LENGTH_LONG).show();
        }
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    /**
     * Return the Fragment associated with a specified position.
     */
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    ;

    @Override
    public int getItemPosition(Object object) {
        int index = fragments.indexOf(object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void startUpdate(ViewGroup container) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        final long itemId = getItemId(position);

        // Do we already have this fragment?
        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            if (DEBUG) Log.v(TAG, "Attaching item #" + itemId + ": f=" + fragment);
            mCurTransaction.attach(fragment);
        } else {
            fragment = getItem(position);
            if (DEBUG) Log.v(TAG, "Adding item #" + itemId + ": f=" + fragment);
            mCurTransaction.add(container.getId(), fragment,
                    makeFragmentName(container.getId(), itemId));
        }
        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG) Log.v(TAG, "Detaching item #" + getItemId(position) + ": f=" + object
                + " v=" + ((Fragment) object).getView());
        mCurTransaction.detach((Fragment) object);
    }

    public void removeFragment(int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG)
            Log.v(TAG, "Removing item #" + getItemId(position) + ": f=" + fragments.get(position)
                    + " v=" + fragments.get(position).getView());
        mCurTransaction.remove(fragments.get(position));
        fragments.remove(position);
        notifyDataSetChanged();
        userInfos.remove(position);
    }

    public void addTransferMarket(String userName, String name, int id) {
        UserInfo userInfo = new UserInfo(userName, name, id);
        addFragment(TransferMarket.newInstance(userInfo.getId(), 90, true));
        userInfos.add(userInfo);
        saveUserInfos();
    }

    public void addFragment(Fragment f) {
        addFragment(f, fragments.size());
    }

    public void addFragment(Fragment f, int position) {
        fragments.add(position, f);
        notifyDataSetChanged();
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    /**
     * Return a unique identifier for the item at the given position.
     * <p/>
     * <p>The default implementation returns the given position.
     * Subclasses should override this method if the positions of items can change.</p>
     *
     * @param position Position within this adapter
     * @return Unique identifier for the item at position
     */
    public long getItemId(int position) {
        return position;
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return userInfos.get(position).getComunioName();
    }
}
