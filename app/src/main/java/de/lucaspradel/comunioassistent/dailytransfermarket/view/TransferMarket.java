package de.lucaspradel.comunioassistent.dailytransfermarket.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import de.lucaspradel.comunioassistent.R;

import de.lucaspradel.comunioassistent.dailytransfermarket.helper.PlayerInfo;
import de.lucaspradel.comunioassistent.dailytransfermarket.manager.DailyTransferMarketManager;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class TransferMarket extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COMUNIO_ID = "comunioId";
    private static final String ARG_DAYS = "days";
    private static final String ARG_ONLY_FROM_COMPUTER = "onlyFromComputer";

    // TODO: Rename and change types of parameters
    private int comunioId;
    private int days;
    private boolean onlyFromComputer;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;
    private DailyTransferMarketManager dailyTransferManager;
    private LinearLayout progress;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private PlayerInfoListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static TransferMarket newInstance(int comunioId, int days, boolean onlyFromComputer) {
        TransferMarket fragment = new TransferMarket();
        Bundle args = new Bundle();
        args.putInt(ARG_COMUNIO_ID, comunioId);
        args.putInt(ARG_DAYS, days);
        args.putBoolean(ARG_ONLY_FROM_COMPUTER, onlyFromComputer);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TransferMarket() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        comunioId = bundle.getInt(ARG_COMUNIO_ID);
        days = bundle.getInt(ARG_DAYS);
        onlyFromComputer = bundle.getBoolean(ARG_ONLY_FROM_COMPUTER);
        mAdapter = new PlayerInfoListAdapter(getActivity());
        dailyTransferManager = new DailyTransferMarketManager().
                setDailyTransferMarketFinishedListener(
                        new DailyTransferMarketManager.DailyTransferMarketFinishedListener() {
                            @Override
                            public void onDailyTransferMarketFinished(List<PlayerInfo> playerInfoList) {

                                //mAdapter = new PlayerInfoListAdapter(getActivity(), playerInfoList);
                                // Set the adapter
                                mAdapter.setPlayerInfoList(playerInfoList);
                                //mListView.setAdapter(mAdapter);
                                // Set OnItemClickListener so we can be notified on item clicks

                                progress.setVisibility(View.GONE);
                            }
                        });

        dailyTransferManager.getDailyTransferMarket(String.valueOf(comunioId),days, onlyFromComputer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_playerinfo, container, false);
        mListView = (AbsListView) view.findViewById(R.id.players);
        progress = (LinearLayout) view.findViewById(R.id.ll_progress);
        /*dailyTransferManager = new DailyTransferMarketManager().
                setDailyTransferMarketFinishedListener(
                        new DailyTransferMarketManager.DailyTransferMarketFinishedListener() {
            @Override
            public void onDailyTransferMarketFinished(List<PlayerInfo> playerInfoList) {

                //mAdapter = new PlayerInfoListAdapter(getActivity(), playerInfoList);
                // Set the adapter
                mAdapter.setPlayerInfoList(playerInfoList);
                ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
                // Set OnItemClickListener so we can be notified on item clicks
                mListView.setOnItemClickListener(TransferMarket.this);
                progress.setVisibility(View.GONE);
            }
        });
        */
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(TransferMarket.this);
        if(!mAdapter.isDataLoaded()) {
            progress.setVisibility(View.VISIBLE);
        }
        //dailyTransferManager.getDailyTransferMarket(String.valueOf(comunioId),days, onlyFromComputer);
        return view;
    }

    public void updateTransferMarket() {
        if(progress == null || mAdapter == null) {
            return;
        }
        progress.setVisibility(View.VISIBLE);
        mAdapter.refreshAdapter();
        dailyTransferManager.getDailyTransferMarket(String.valueOf(comunioId),days, onlyFromComputer);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
