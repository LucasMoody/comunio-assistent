package lucaspradel.de.comunioassistent.dailytransfermarket.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import lucaspradel.de.comunioassistent.R;
import lucaspradel.de.comunioassistent.common.IconMapper;
import lucaspradel.de.comunioassistent.dailytransfermarket.helper.PlayerInfo;

/**
 * Created by lucas on 01.04.15.
 */
public class PlayerInfoListAdapter extends ArrayAdapter<PlayerInfo> {

    private final List<PlayerInfo> playerInfoList;

    private class ViewHolder {
        private final TextView name;
        private final TextView position;
        private final ImageView statusIcon;
        private final ImageView clubIcon;
        private final TextView statusInfo;
        private final TextView points;
        private final TextView marketValue;
        private final TextView recommendedPrice;
        private final LineChartView marketValueProcess;

        protected ViewHolder(TextView name, TextView position, ImageView clubIcon, ImageView statusIcon, TextView statusInfo, TextView points, TextView marketValue, TextView recommendedPrice, LineChartView marketValueProcess) {
            this.name = name;
            this.position = position;
            this.clubIcon = clubIcon;
            this.statusIcon = statusIcon;
            this.statusInfo = statusInfo;
            this.points = points;
            this.marketValue = marketValue;
            this.recommendedPrice = recommendedPrice;
            this.marketValueProcess = marketValueProcess;
        }

        public TextView getName() {
            return name;
        }

        public TextView getPosition() {
            return position;
        }

        public ImageView getStatusIcon() {
            return statusIcon;
        }

        public ImageView getClubIcon() {
            return clubIcon;
        }

        public TextView getStatusInfo() {
            return statusInfo;
        }

        public TextView getPoints() {
            return points;
        }

        public TextView getMarketValue() {
            return marketValue;
        }

        public TextView getRecommendedPrice() {
            return recommendedPrice;
        }

        public LineChartView getMarketValueProcess() {
            return marketValueProcess;
        }
    }

    public PlayerInfoListAdapter(Context context, List<PlayerInfo> playerInfos) {
        super(context, R.layout.transfermarket_player, playerInfos);
        playerInfoList = playerInfos;
    }

    public void refreshAdapter() {
        clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlayerInfo info = playerInfoList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transfermarket_player, parent, false);
            TextView name = (TextView) convertView.findViewById(R.id.tv_player_name);
            TextView playerPosition =(TextView) convertView.findViewById(R.id.tv_player_position);
            ImageView statusIcon = (ImageView) convertView.findViewById(R.id.iv_status_icon);
            ImageView clubIcon = (ImageView) convertView.findViewById(R.id.iv_club_icon);
            TextView statusInfo = (TextView) convertView.findViewById(R.id.tv_status_info);
            TextView points = (TextView) convertView.findViewById(R.id.tv_player_points);
            TextView marketValue = (TextView) convertView.findViewById(R.id.tv_player_market_value);
            TextView recommendedPrice = (TextView) convertView.findViewById(R.id.tv_player_recommended_price);
            LineChartView marketValueProcess = (LineChartView) convertView.findViewById(R.id.ch_market_value_process);
            marketValueProcess.setInteractive(false);

            viewHolder = new ViewHolder(name, playerPosition, clubIcon, statusIcon, statusInfo, points, marketValue, recommendedPrice, marketValueProcess);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.getName().setText(info.getName());
        viewHolder.getPosition().setText(info.getPosition());
        viewHolder.getClubIcon().setImageDrawable(IconMapper.getClubIconForClubId(getContext(), info.getClubId()));
        viewHolder.getStatusIcon().setImageDrawable(IconMapper.getStatusIconForStatusCode(getContext(), info.getStatus()));
        viewHolder.getStatusInfo().setText(info.getStatusInfo() == null ? "" : info.getStatusInfo());
        viewHolder.getPoints().setText(String.valueOf(info.getPoints()));
        viewHolder.getMarketValue().setText(String.format("%,d",info.getMarketValue()));
        viewHolder.getRecommendedPrice().setText(String.format("%,d",info.getRecommendedPrice()));
        List<PointValue> values = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<>();
        List<Pair<Date, Integer>> marketVals = info.getMarketValues();
        for (int i = 0; i<marketVals.size(); i++) {
            values.add(new PointValue(i, marketVals.get(i).second));
        }
        if(marketVals.size()>0) {
            axisValues.add(new AxisValue(0).setLabel(new SimpleDateFormat("dd.MM.yy").format(marketVals.get(0).first)));
            axisValues.add(new AxisValue(marketVals.size()-1).setLabel(new SimpleDateFormat("dd.MM.yy").format(marketVals.get(marketVals.size()-1).first)));
        }
        Line line = new Line(values).setColor(Color.parseColor("#2196F3")).setCubic(true).setHasPoints(false).setStrokeWidth(1);
        List<Line> lines = new ArrayList<>();
        lines.add(line);
        LineChartData data = new LineChartData(lines);
        data.setAxisXBottom(new Axis(axisValues).setHasLines(false));
        data.setAxisYLeft(new Axis().setHasLines(false).setInside(true).setFormatter(new ExtendedAxisValueFormatter().setAppendedText(new char[]{'k'})));
        viewHolder.getMarketValueProcess().setLineChartData(data);
        Viewport vp = new Viewport(viewHolder.getMarketValueProcess().getMaximumViewport());
        //vp.top = vp.top + 200000;
        vp.bottom = 0;
        //vp.right = vp.right + 2;
        //vp.left = vp.left - 2;
        viewHolder.getMarketValueProcess().setMaximumViewport(vp);
        viewHolder.getMarketValueProcess().setCurrentViewport(vp);
        return convertView;
    }
}
