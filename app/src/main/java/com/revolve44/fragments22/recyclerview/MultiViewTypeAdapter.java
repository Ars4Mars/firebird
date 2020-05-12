package com.revolve44.fragments22.recyclerview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.revolve44.fragments22.R;
//import com.revolve44.horizontalrecyclerview9.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import im.dacer.androidcharts.LineView;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

import static android.content.Context.MODE_PRIVATE;

//import org.eazegraph.lib.charts.ValueLineChart;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Model> dataSet;
    Context mContext;
    int total_types;
    MediaPlayer mPlayer;
    private boolean fabStateVolume = false;



    public static class TextTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        TextView pressureCard;
        TextView tempCard;
        TextView humidityCard;

        CardView cardView;
        Float temp;
        Float pressure;
        Float humidity;

        boolean tempFahrenheit;

        public TextTypeViewHolder(final View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.tempCard = (TextView) itemView.findViewById(R.id.temp_card);
            this.pressureCard = (TextView) itemView.findViewById(R.id.pressure_card);
            this.humidityCard = (TextView) itemView.findViewById(R.id.humidity_card);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            final SharedPreferences prefs = itemView.getContext().getSharedPreferences("MasterSave", MODE_PRIVATE);

            //////////////////////////////////////////////////////////////////////////
            //                   Paste data from server to chart                    //
            //////////////////////////////////////////////////////////////////////////
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 3s
                    Log.d("Lifecycle -> method "," Set temp ");
                    SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("MasterSave", MODE_PRIVATE);
                    temp = sharedPreferences.getFloat("temp",0f);
                    pressure = sharedPreferences.getFloat("pressure",0f);
                    humidity = sharedPreferences.getFloat("humidity",0f);
                    tempFahrenheit = sharedPreferences.getBoolean("tempFahrenheit",tempFahrenheit);
                    //int stringtemp = Math.round(temp);
                    Float s;

                    if (!tempFahrenheit){
                        tempCard.setText(Math.round(temp)+ "°C ");
                    }
                    if (tempFahrenheit){

                        s = temp*1.8f+32f;

                        tempCard.setText(Math.round(s)+ "°F ");
                    }
                    pressureCard.setText(pressure+ " hPa");
                    humidityCard.setText(humidity+ " %");


                }
            }, 5000);




        }
    }




    public static class GraphTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;

        public String city;
        //CardView cardView2;
        private Context mContext;
        CardView cardView;
        LineView lineView;
        LineChartView lineChart;
        String[] date2;
        Integer[] score2= {74,22,18,79};
        private List<PointValue> mPointValues = new ArrayList<PointValue>();
        private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
        int q=0;




        public GraphTypeViewHolder(final View itemView) {
            super(itemView);
            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.lineChart = (LineChartView)itemView.findViewById(R.id.line_chart);

            //this.mContext = context;

            //////////////////////////////////////////////////////////////////////////
            //                   Paste data from server to chart                    //
            //////////////////////////////////////////////////////////////////////////
            // i use delay for dont make error
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try{
                        Log.d("Lifecycle -> method "," build Graph ");
                        //////////////////////////////////////////////////////////////////////////
                        //                       GET FROM SHARED PREFERENCES                    //
                        //////////////////////////////////////////////////////////////////////////
                        LinkedList<String> Legend = new LinkedList<>();
                        LinkedList<Integer> Value = new LinkedList<>();
                        //SharedPreferences sharedPreferences = getSharedPreferences("HashMap", MODE_PRIVATE);
                        String defValue = new Gson().toJson(new LinkedHashMap<String,String>());
                        SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("MasterSave", MODE_PRIVATE);
//                        String json=sharedPreferences.getString("map",defValue);
//                        TypeToken<LinkedHashMap<String,String>> token = new TypeToken<LinkedHashMap<String,String>>() {};
//                        LinkedHashMap<String,String> linked=new Gson().fromJson(json,token.getType()); // its output from MainActivity

                        //>>>>
                        Gson gson = new Gson();
                        String json = sharedPreferences.getString("legend", null);
                        String json2 = sharedPreferences.getString("value", null);
                        Type type = new TypeToken<LinkedList<String>>() {}.getType();
                        Type type2 = new TypeToken<LinkedList<Integer>>() {}.getType();
                        Legend = gson.fromJson(json, type);
                        Value = gson.fromJson(json2, type2);


                        //////////////////////////////////////////////////////////
                        //                  initialization                      //
                        //////////////////////////////////////////////////////////
                        if (Legend.size()>1 & q<=1) {
                            date2 = Legend.toArray(new String[Legend.size()]);
                            score2 = Value.toArray(new Integer[Value.size()]);
                            //Value.toArray(score2);
                            //score2 = Value.toArray();

                            getAxisXLables();//获取x轴的标注
                            getAxisPoints();//获取坐标点
                            initLineChart();//初始化
                            q++;
                        }


                    }catch (Exception e){
                        Toast.makeText(itemView.getContext(), "No Internet. Check Internet connection", Toast.LENGTH_LONG).show();
                    }

                }
            }, 3400);
        }

        private void initLineChart(){
            Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色
            List<Line> lines = new ArrayList<Line>();
            line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
            line.setCubic(true);//曲线是否平滑
//	    line.setStrokeWidth(3);//线条的粗细，默认是3
            line.setFilled(true);//是否填充曲线的面积
            line.setHasLabels(true);//曲线的数据坐标是否加上备注
//		line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
            line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
            line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
            lines.add(line);
            LineChartData data = new LineChartData();
            data.setLines(lines);

            //坐标轴
            Axis axisX = new Axis(); //X轴
            axisX.setHasTiltedLabels(true);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//	    axisX.setTextColor(Color.WHITE);  //设置字体颜色
            axisX.setTextColor(Color.parseColor("#D6D6D9"));//灰色

//	    axisX.setName("未来几天的天气");  //表格名称
            axisX.setTextSize(11);//设置字体大小
            axisX.setMaxLabelChars(1); //1<- more fit  // less fit -> 7
            axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
            axisX.setHasTiltedLabels(false);
            data.setAxisXBottom(axisX); //x 轴在底部
//	    data.setAxisXTop(axisX);  //x 轴在顶部
            axisX.setHasLines(true); //x 轴分割线


            Axis axisY = new Axis();  //Y轴
            axisY.setName("power output [watts]");// Y axis name
            axisY.setTextSize(11);//设置字体大小
            data.setAxisYLeft(axisY);  //Y轴设置在左边
            //data.setAxisYRight(axisY);  //y轴设置在右边
            //设置行为属性，支持缩放、滑动以及平移
            lineChart.setInteractive(true);
            lineChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);  //缩放类型，水平
            lineChart.setMaxZoom((float) 1);//缩放比例
            lineChart.setLineChartData(data);
            lineChart.setVisibility(View.VISIBLE);
            /**注：下面的7，10只是代表一个数字去类比而已
             * 尼玛搞的老子好辛苦！！！见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
             * 下面几句可以设置X轴数据的显示个数（x轴0-7个数据），当数据点个数小于（29）的时候，缩小到极致hellochart默认的是所有显示。当数据点个数大于（29）的时候，
             * 若不设置axisX.setMaxLabelChars(int count)这句话,则会自动适配X轴所能显示的尽量合适的数据个数。
             * 若设置axisX.setMaxLabelChars(int count)这句话,
             * 33个数据点测试，若 axisX.setMaxLabelChars(10);里面的10大于v.right= 7; 里面的7，则
             刚开始X轴显示7条数据，然后缩放的时候X轴的个数会保证大于7小于10
             若小于v.right= 7;中的7,反正我感觉是这两句都好像失效了的样子 - -!
             * 并且Y轴是根据数据的大小自动设置Y轴上限
             * 若这儿不设置 v.right= 7; 这句话，则图表刚开始就会尽可能的显示所有数据，交互性太差
             */
            Viewport v = new Viewport(lineChart.getMaximumViewport());
            v.left = 0;
            v.right= 14;
            lineChart.setCurrentViewport(v);

        }

        /**
         * X 轴的显示
         */
        private void getAxisXLables(){
            for (int i = 0; i < date2.length; i++) {
                mAxisXValues.add(new AxisValue(i).setLabel(date2[i]));
            }

        }
        /**
         * Y
         */
        private void getAxisPoints(){
            for (int i = 0; i < score2.length; i++) {
                mPointValues.add(new PointValue(i, score2[i]));
            }

        }


    }

    public MultiViewTypeAdapter(ArrayList<Model> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case Model.TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_type, parent, false);
                return new TextTypeViewHolder(view);
            case Model.GRAPH_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.graph_type, parent, false);
                return new GraphTypeViewHolder(view);// here has been invisible error
        }
        return null;


    }

    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).type) {
            case 0:
                return Model.TEXT_TYPE;
            case 1:
                return Model.GRAPH_TYPE;
            default:
                return -1;
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        Model object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.type) {
                case Model.TEXT_TYPE:
                    ((TextTypeViewHolder) holder).txtType.setText(object.text);

                    break;
                case Model.GRAPH_TYPE:
                    ((GraphTypeViewHolder) holder).txtType.setText(object.text);
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}
