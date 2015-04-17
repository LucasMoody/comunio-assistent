package lucaspradel.de.comunioassistent.dailytransfermarket.helper;

import android.support.v4.util.Pair;

import java.util.Date;
import java.util.List;

/**
 * Created by lucas on 01.04.15.
 */
public class PlayerInfo {

    private String id;
    private String name;
    private int points;
    private int clubId;
    private int marketValue;
    private int recommendedPrice;
    private String status;
    private String statusInfo;
    private String position;
    private List<Pair<Date, Integer>> marketValues;

    public PlayerInfo(String id, String name, int points, int clubId, int marketValue, int recommendedPrice, String status, String statusInfo, String position, List<Pair<Date, Integer>> marketValues) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.clubId = clubId;
        this.marketValue = marketValue;
        this.recommendedPrice = recommendedPrice;
        this.status = status;
        this.statusInfo = statusInfo;
        this.position = position;
        this.marketValues = marketValues;
    }

    public PlayerInfo() {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getClubId() {
        return clubId;
    }

    public int getMarketValue() {
        return marketValue;
    }

    public int getRecommendedPrice() {
        return recommendedPrice;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public String getPosition() {
        return position;
    }

    public List<Pair<Date, Integer>> getMarketValues() {
        return marketValues;
    }
}
