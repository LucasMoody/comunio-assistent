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
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setContentTitle("My notification")
                        .setSmallIcon(R.drawable.ic_menu_add)
                        .setContentText(notificationText(transferMarkets, userInfos));
        mBuilder.setContentIntent(resultPendingIntent);
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
        int treshold = 1000000;
        contextText.append("Moin! Heute sind Kracher wie ");
        String delimiter = "";
        String comunioDelimiter = "";
        for (int i = 0; i < transferMarkets.size(); i++) {
            List<PlayerInfo> curTransferMarket = transferMarkets.get(i);
            UserInfo curUserInfo = userInfos.get(i);
            contextText.append(comunioDelimiter);
            for (int j = 0; j < curTransferMarket.size(); j++) {
                PlayerInfo curPlayer = curTransferMarket.get(j);
                if (curPlayer.getMarketValue() >= treshold) {
                    contextText.append(delimiter).append(curPlayer.getName());
                    delimiter = ", ";
                }
            }
            contextText.append(" bei ").append(curUserInfo.getComunioName());
            comunioDelimiter = " und ";
        }
        return contextText.toString();
    }
}
