package de.lucaspradel.comunioassistent.dailytransfermarket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import de.lucaspradel.comunioassistent.R;
import de.lucaspradel.comunioassistent.common.view.SlidingTabLayout;
import de.lucaspradel.comunioassistent.dailytransfermarket.helper.UserInfo;
import de.lucaspradel.comunioassistent.dailytransfermarket.manager.DailyTransferMarketManager;
import de.lucaspradel.comunioassistent.dailytransfermarket.view.TransferMarket;


public class DailyTransferMarketActivity extends ActionBarActivity implements TransferMarket.OnFragmentInteractionListener {

    public static final String COMUNIO_IDS = "comunioIds";
    public static final String SEPARATOR = ",";
    public static final String COMUNIO_USER_INFOS_FILENAME = "comunioUserInfos";

    private List<UserInfo> userInfos;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private DailyTransferMarketManager dailyTransferMarketManager;
    private SlidingTabLayout mSlidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_transfer_market);

        dailyTransferMarketManager = new DailyTransferMarketManager();
        userInfos = new ArrayList<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(openFileInput(COMUNIO_USER_INFOS_FILENAME));
            userInfos = (List<UserInfo>) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {

        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), userInfos);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_daily_transfer_market, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.mi_delete_comunio) {
            final List<Integer> mSelectedItems = new ArrayList();  // Where we track the selected items
            String[] comunioNames = new String[userInfos.size()];
            for (int i = 0; i<comunioNames.length; i++) {
                comunioNames[i] = userInfos.get(i).getComunioName();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Set the dialog title
            builder.setTitle("Test")
                    // Specify the list array, the items to be selected by default (null for none),
                    // and the listener through which to receive callbacks when items are selected
                    .setMultiChoiceItems(comunioNames, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        mSelectedItems.add(which);
                                    } else if (mSelectedItems.contains(which)) {
                                        // Else, if the item is already in the array, remove it
                                        mSelectedItems.remove(Integer.valueOf(which));
                                    }
                                }
                            })
                            // Set the action buttons
                    .setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, so save the mSelectedItems results somewhere
                            // or return them to the component that opened the dialog
                            mSectionsPagerAdapter.deleteTransferMarket(mSelectedItems);

                        }
                    })
                    .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            builder.create().show();
        } else if(id == R.id.mi_refresh) {
            mSectionsPagerAdapter.refreshTransferMarket(mViewPager.getCurrentItem());
        } else if(id == R.id.mi_add_comunio) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialog = inflater.inflate(R.layout.dialog_add_comunio, null);
            final EditText editText = (EditText) dialog.findViewById(R.id.et_comunioId);
            builder.setView(dialog);
            builder.setPositiveButton(R.string.dialog_add_comunio_ok_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                }
            });
            builder.setNegativeButton(R.string.dialog_add_comunio_cancel_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            final AlertDialog dia = builder.create();
            dia.show();
            dia.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String userName = editText.getText().toString();
                    dailyTransferMarketManager.setGetComunioInfoFinishedListener(new DailyTransferMarketManager.GetComunioInfoFinishedListener() {
                        @Override
                        public void onGetComunioInfoFinished(int id, String name) {
                            mSectionsPagerAdapter.addTransferMarket(new UserInfo(userName, name, id));
                            mSlidingTabLayout.setViewPager(mViewPager);
                            dia.dismiss();
                        }

                        @Override
                        public void onGetComunioInfoFailed() {
                            dia.dismiss();
                            Toast.makeText(DailyTransferMarketActivity.this,R.string.dialog_add_comunio_user_not_found, Toast.LENGTH_LONG).show();
                        }
                    }).getComunioInfo(userName);
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    private void saveUserInfos(List<UserInfo> userInfos) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(openFileOutput(COMUNIO_USER_INFOS_FILENAME, Context.MODE_PRIVATE));
            oos.writeObject(userInfos);
            oos.close();
        } catch (IOException e) {
            Toast.makeText(this, "Saving of comunio user was not succesful. Please contact the vendor.", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<TransferMarket> markets;
        private List<UserInfo> userInfos;

        private class ViewHolder {
            private final TransferMarket transferMarket;

            protected ViewHolder(TransferMarket transferMarket) {
                this.transferMarket = transferMarket;
            }

            public TransferMarket getTransferMarket() {
                return transferMarket;
            }
        }

        public SectionsPagerAdapter(FragmentManager fm, List<UserInfo> userInfos) {
            super(fm);
            markets = new ArrayList<>();
            this.userInfos = userInfos;
            for (UserInfo ui : userInfos) {
                //TODO Anzahl Tage und OnlyShowComputer konfigurierbar machen
                markets.add(TransferMarket.newInstance(ui.getId(),90,true));
            }
        }

        public void refreshTransferMarket(int position) {
            markets.get(position).updateTransferMarket();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);

            return markets.get(position);
        }

        public void addTransferMarket(UserInfo userInfo) {
            //TODO Anzahl Tage und OnlyShowComputer konfigurierbar machen
            markets.add(TransferMarket.newInstance(userInfo.getId(), 90, true));
            userInfos.add(userInfo);
            notifyDataSetChanged();
            DailyTransferMarketActivity.this.saveUserInfos(userInfos);

        }

        public void deleteTransferMarket(List<Integer> positions) {
            Collections.sort(positions);
            Collections.reverse(positions);
            for (Integer position : positions) {
                int pos = position;
                markets.remove(pos);
                userInfos.remove(pos);
                destroyItem();
            }
            notifyDataSetChanged();
            DailyTransferMarketActivity.this.saveUserInfos(userInfos);
        }

        @Override
        public int getCount() {
            return markets.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return userInfos.get(position).getComunioName();
        }
    }

}
