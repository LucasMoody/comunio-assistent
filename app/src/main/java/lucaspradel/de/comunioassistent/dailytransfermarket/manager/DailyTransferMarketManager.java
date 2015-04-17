package lucaspradel.de.comunioassistent.dailytransfermarket.manager;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lucaspradel.de.comunioassistent.dailytransfermarket.helper.PlayerInfo;

/**
 * Created by lucas on 03.04.15.
 */
public class DailyTransferMarketManager {

    private static final String DAILY_TRANSFERMARKET_URL = "http://www.lucaspradel.de/comunio/dailytransfermarket";
    private static final String COMUNIO_SERVICE_URL = "http://www.lucaspradel.de/comunio/";
    private static final String GET_COMUNIO_PATH = "getcomunioname";
    private static final String GET_COMUNIO_PARAM_USERNAME = "username";
    private static final String URL_PARAMETER_COMUNIO_ID = "comunioid";
    private static final String URL_PARAMETER_DAYS = "days";
    private static final String URL_PARAMETER_SHOW_ONLY_COMPUTER = "com";
    public static final String GET_COMUNIO_INFO_RESP_ID = "id";
    public static final String GET_COMUNIO_INFO_RESP_NAME = "name";

    private DailyTransferMarketFinishedListener dailyTransferMarketListener;
    private GetComunioInfoFinishedListener getComunioInfoFinishedListener;

    public DailyTransferMarketManager() {
    }

    public DailyTransferMarketManager getDailyTransferMarket(String comunioId, int days, boolean onlyFromComputer) {
        new GetTransferMarketTask().execute(new Parameter(comunioId, days, onlyFromComputer));
        return this;
    }

    public DailyTransferMarketManager setDailyTransferMarketFinishedListener(DailyTransferMarketFinishedListener listener) {
        dailyTransferMarketListener = listener;
        return this;
    }

    public DailyTransferMarketManager setGetComunioInfoFinishedListener(GetComunioInfoFinishedListener listener) {
        getComunioInfoFinishedListener = listener;
        return this;
    }

    public DailyTransferMarketManager getComunioInfo(String userName) {
        new GetComunioInfoTask().execute(userName);
        return this;
    }

   private class GetTransferMarketTask extends AsyncTask<Parameter, Void, Object> {
       @Override
       protected Object doInBackground(Parameter... params) {
           Parameter param = params[0];
           try {

               StringBuffer query = new StringBuffer("?");
               if (param.getComunioId() != null) {
                   query.append(URL_PARAMETER_COMUNIO_ID).append("=").append(param.getComunioId());
               }
               if (param.getDays() != -1) {
                   if(!(query.lastIndexOf("?")==query.length())) {
                       query.append("&");
                   }
                   query.append(URL_PARAMETER_DAYS).append("=").append(param.getDays());
               }
               if(!(query.lastIndexOf("?")==query.length())) {
                   query.append("&");
               }
               query.append(URL_PARAMETER_SHOW_ONLY_COMPUTER).append("=").append(param.isOnlyFromComputer());
               URL url = new URL(DAILY_TRANSFERMARKET_URL + query.toString());
               HttpURLConnection con = (HttpURLConnection) url.openConnection();
               int responseCode = con.getResponseCode();
               if (responseCode != 200) {
                   return new MalformedURLException("Response Code: " + responseCode + ". The query parameters where wrong. Query :" + query.toString());
               }
               BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
               String line;
               StringBuffer response = new StringBuffer();
               while ((line = in.readLine()) != null) {
                   response.append(line);
               }
               in.close();
               return response.toString();
           } catch (MalformedURLException e) {
               return e;
           } catch (IOException e) {
               return e;
           }
       }

       @Override
       protected void onPostExecute(Object o) {
           List<PlayerInfo> playerInfoList = new ArrayList<>();
           String id, name, position, status, statusInfo;
           int points, marketValue, recommendedPrice, clubId;
           if (o instanceof Exception) {
               return;
           }
           try {
               JSONArray result = new JSONArray((String) o);
               JSONObject curObj;
               for (int i = 0; i<result.length(); i++) {
                   List<Pair<Date, Integer>> marketValues = new ArrayList<>();
                   curObj = result.getJSONObject(i);
                   try{id=curObj.getString("id");}
                   catch(JSONException e) {id="NoId";}
                   try{name=curObj.getString("name");}
                   catch(JSONException e) {name="?";}
                   try{position=curObj.getString("position");}
                   catch(JSONException e) {position="?";}
                   try{status=curObj.getString("status");}
                   catch(JSONException e) {status="?";}
                   try{statusInfo=curObj.getString("status_info");}
                   catch(JSONException e) {statusInfo="";}
                   try{points=curObj.getInt("points");}
                   catch(JSONException e) {points=0;}
                   try{marketValue=curObj.getInt("quote");}
                   catch(JSONException e) {marketValue=0;}
                   try{recommendedPrice=curObj.getInt("recommendedprice");}
                   catch(JSONException e) {recommendedPrice=0;}
                   try{clubId=curObj.getInt("clubid");}
                   catch(JSONException e) {clubId=-1;}
                   try{
                       JSONArray arr = curObj.getJSONArray("quotes");
                       Date date;
                       int val;
                       JSONObject curQuote;
                       for (int j = 0; j < arr.length(); j++) {
                           curQuote = arr.getJSONObject(j);
                           try{date = new SimpleDateFormat("yyyy-MM-dd").parse(curQuote.getString("date"));}
                           catch(JSONException | ParseException e) {continue;}
                           try{val = curQuote.getInt("quote");}
                           catch (JSONException e) {continue;}
                           marketValues.add(new Pair<Date, Integer>(date, val));
                       }
                   }catch(JSONException e) {/*do nothing*/}
                   playerInfoList.add(new PlayerInfo(id, name, points, clubId, marketValue, recommendedPrice, status, statusInfo, position, marketValues));
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }
           dailyTransferMarketListener.onDailyTransferMarketFinished(playerInfoList);
       }
   }

    private class GetComunioInfoTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... params) {
            String userName = params[0];
            if(userName == null || userName.equals("")) {
                return new IllegalArgumentException("User name is null or empty");
            }
            try {
                URL url = new URL(COMUNIO_SERVICE_URL + GET_COMUNIO_PATH + "?" + GET_COMUNIO_PARAM_USERNAME + "=" + userName);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                int responseCode = con.getResponseCode();
                if (responseCode != 200) {
                    return new MalformedURLException("Response Code: " + responseCode + ". The query parameters where wrong. Query :" + url.toString());
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return response.toString();
            } catch (MalformedURLException e) {
                return e;
            } catch (IOException e) {
                return e;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            if (o instanceof Exception) {
                //listener muss irgendwie mitgeteilt werden, dass es nicht funktioniert hat,
                //sodass der Hinzufüge-Dialog wieder angezeigt werden kann
                getComunioInfoFinishedListener.onGetComunioInfoFailed();
                return;
            }
            try {
                JSONObject comunio = new JSONObject((String) o);
                getComunioInfoFinishedListener.onGetComunioInfoFinished(comunio.getInt(GET_COMUNIO_INFO_RESP_ID), comunio.getString(GET_COMUNIO_INFO_RESP_NAME));
            } catch (JSONException e) {
                getComunioInfoFinishedListener.onGetComunioInfoFailed();
                e.printStackTrace();
            }
        }
    }

    private class Parameter {
        private final String comunioId;
        private final int days;
        private final boolean onlyFromComputer;

        public Parameter(String comunioId, int days, boolean onlyFromComputer) {
            this.comunioId = comunioId;
            this.days = days;
            this.onlyFromComputer = onlyFromComputer;
        }

        public String getComunioId() {
            return comunioId;
        }

        public int getDays() {
            return days;
        }

        public boolean isOnlyFromComputer() {
            return onlyFromComputer;
        }
    }

    public interface DailyTransferMarketFinishedListener {
        public void onDailyTransferMarketFinished(List<PlayerInfo> playerInfoList);
    }
    public interface GetComunioInfoFinishedListener {
        public void onGetComunioInfoFinished(int id, String name);
        public void onGetComunioInfoFailed();
    }

}
