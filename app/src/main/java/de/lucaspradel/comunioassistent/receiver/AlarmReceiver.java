package de.lucaspradel.comunioassistent.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

import de.lucaspradel.comunioassistent.R;
import de.lucaspradel.comunioassistent.dailytransfermarket.DailyTransferMarketActivity;
import de.lucaspradel.comunioassistent.dailytransfermarket.helper.PlayerInfo;
import de.lucaspradel.comunioassistent.dailytransfermarket.helper.UserInfo;
import de.lucaspradel.comunioassistent.dailytransfermarket.manager.DailyTransferMarketManager;
import de.lucaspradel.comunioassistent.dailytransfermarket.manager.UserInfoManger;

/**
 * Created by lucas on 09.07.15.
 */
public class AlarmReceiver extends BroadcastReceiver implements DailyTransferMarketManager.DailyTransferMarketFinishedListener {

    int counter = 0;
    List<List<PlayerInfo>> transferMarkets = new ArrayList<>();
    private Context context;
    List<UserInfo> userInfos;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        UserInfoManger userInfoManger = new UserInfoManger(context);
        userInfos = userInfoManger.loadUserInfos();
        DailyTransferMarketManager transferMarketManager = new DailyTransferMarketManager(context);
        transferMarketManager.setDailyTransferMarketFinishedListener(this);
        counter += userInfos.size();
        for (UserInfo userInfo : userInfos) {
            transferMarketManager.getDailyTransferMarket(String.valueOf(userInfo.getId()), 90, true);
        }
    }

    @Override
    public void onDailyTransferMarketFinished(List<PlayerInfo> playerInfoList) {
        transferMarkets.add(playerInfoList);
        counter--;
        if (counter <= 0) {
            counter = 0;
            showNotification(context);
        }
    }

    private void showNotification(Context context) {
        Intent resultIntent = new Intent(context, DailyTransferMarketActivity.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        String notificationText = notificationText(transferMarkets, userInfos);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setContentTitle("Moin")
                        .setSmallIcon(R.drawable.ic_menu_add)
                        .setContentText(notificationText);
        NotificationCompat.BigTextStyle inboxStyle =
                new NotificationCompat.BigTextStyle();
        inboxStyle.bigText(notificationText(transferMarkets, userInfos));
        mBuilder.setStyle(inboxStyle);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private String notificationText(List<List<PlayerInfo>> transferMarkets, List<UserInfo> userInfos) {
        StringBuilder contextText = new StringBuilder();
        int treshold = 4000000;
        List<List<PlayerInfo>> filteredTransferMarketsOverThreshold = new ArrayList<>();
        List<UserInfo> comuniosWithPlayersOverThreshold = new ArrayList<>();
        List<UserInfo> comuniosWithPlayersUnderThreshold = new ArrayList<>();
        int counter = 0;
        for (List<PlayerInfo> transfermarket : transferMarkets) {
            List<PlayerInfo> filteredMarket = new ArrayList<>();
            for (PlayerInfo player : transfermarket) {
                if (player.getMarketValue() >= treshold) {
                    filteredMarket.add(player);
                }
            }
            if (filteredMarket.size() > 0) {
                filteredTransferMarketsOverThreshold.add(filteredMarket);
                comuniosWithPlayersOverThreshold.add(userInfos.get(counter));
            } else {
                comuniosWithPlayersUnderThreshold.add(userInfos.get(counter));
            }
            counter++;
        }
        // No market with players over threshold
        if (comuniosWithPlayersOverThreshold.size() == 0) {
            contextText.append("Heute sind leider keine Kracher auf den Märkten.");
        }
        // only markets with players over threshold
        else if (comuniosWithPlayersUnderThreshold.size() == 0 && comuniosWithPlayersOverThreshold.size() > 0) {
            contextText.append("Heute sind Kracher wie ");
            String delimiter = "";
            String comunioDelimiter = "";
            for (int i = 0; i < comuniosWithPlayersOverThreshold.size(); i++) {
                contextText.append(comunioDelimiter);
                List<PlayerInfo> curTransferMarket = filteredTransferMarketsOverThreshold.get(i);
                for (int j = 0; j < curTransferMarket.size(); j++) {
                    contextText.append(delimiter).append(curTransferMarket.get(j).getName());
                    delimiter = ", ";
                }
                contextText.append(" bei ").append(comuniosWithPlayersOverThreshold.get(i).getComunioName());
                comunioDelimiter = " und ";
                delimiter = "";
            }
            contextText.append(" auf dem Transfermarkt.");
        }
        // markets with player under and over threshold
        else {
            contextText.append("Heute sind Kracher wie ");
            String delimiter = "";
            String comunioDelimiter = "";
            for (int i = 0; i < comuniosWithPlayersOverThreshold.size(); i++) {
                contextText.append(comunioDelimiter);
                List<PlayerInfo> curTransferMarket = filteredTransferMarketsOverThreshold.get(i);
                for (int j = 0; j < curTransferMarket.size(); j++) {
                    contextText.append(delimiter).append(curTransferMarket.get(j).getName());
                    delimiter = ", ";
                }
                contextText.append(" bei ").append(comuniosWithPlayersOverThreshold.get(i).getComunioName());
                comunioDelimiter = " und ";
                delimiter = "";
            }
            contextText.append(" auf dem Transfermarkt.");
            contextText.append("\nLeider sind keine Kracher bei ");
            comunioDelimiter = "";
            for (int i = 0; i < comuniosWithPlayersUnderThreshold.size(); i++) {
                contextText.append(comunioDelimiter).append(comuniosWithPlayersUnderThreshold.get(i).getComunioName());
                comunioDelimiter = " , ";
            }
            contextText.append(" zu finden.");

        }
        return contextText.toString();
    }
}
