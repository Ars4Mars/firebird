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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import im.dacer.androidcharts.LineView;

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




        public GraphTypeViewHolder(final View itemView) {
            super(itemView);
            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            lineView= (LineView)itemView.findViewById(R.id.line_view);

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
                        //////////////////////////////////////////////////////////////////////////
                        //                       GET FROM SHARED PREFERENCES                    //
                        //////////////////////////////////////////////////////////////////////////

                        //SharedPreferences sharedPreferences = getSharedPreferences("HashMap", MODE_PRIVATE);
                        String defValue = new Gson().toJson(new LinkedHashMap<String,String>());
                        SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("MasterSave", MODE_PRIVATE);
                        String json=sharedPreferences.getString("map",defValue);
                        TypeToken<LinkedHashMap<String,String>> token = new TypeToken<LinkedHashMap<String,String>>() {};
                        LinkedHashMap<String,String> linked=new Gson().fromJson(json,token.getType()); // its output from MainActivity

                        //////////////////////////////////////////////////////////
                        //                  initialization                      //
                        //////////////////////////////////////////////////////////
                        Log.d("Recycler view ->Linked", "HashMap"+linked);
                        ArrayList<String> bottomStringList = new ArrayList<String>();
                        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();

                        ArrayList<Integer> ints = new ArrayList<Integer>();
                        //for check
                        //////////////////////////////////////////////////////////
//            LinkedHashMap<Long, Float> linked = new LinkedHashMap<Long, Float>();
//            linked.put((long) 1588707561,1300f);
//            linked.put((long) 1588807561,1400f);
//            linked.put((long) 1588907561,1500f);
//            linked.put((long) 1588917561,100f);
//            linked.put((long) 1588927561,5300f);
//            linked.put((long) 1588937561,1300f);
//            linked.put((long) 158894561,1200f);
//            linked.put((long) 1588957561,4300f);
//            linked.put((long) 1588967561,300f);
//            linked.put((long) 1588977561,1300f);

                        //////////////////////////////////////////////////////////
                        //                  Get Local GMT                       //
                        //////////////////////////////////////////////////////////
                        //get current unix timestamp
                        long unixUTC = System.currentTimeMillis() / 1000L;
                        long GMT= sharedPreferences.getLong("GMT",0);

//                        // Get current UNIX timestamp in current area
//                        Calendar cal = Calendar.getInstance();
//                        TimeZone timeZone =  cal.getTimeZone();
//                        Date cals =    Calendar.getInstance(TimeZone.getDefault()).getTime();
//                        long milliseconds =   cals.getTime();
//                        milliseconds = milliseconds + timeZone.getOffset(milliseconds);
//                        long UnixCurrentTime = milliseconds / 1000L;
//                        long GMT = UnixCurrentTime-unixUTC;

                        //////////////////////////////////////////////////////////
                        //     Distribution pair - (time, future power output)  //
                        //////////////////////////////////////////////////////////
                        for(Map.Entry<String, String> entry : linked.entrySet()) {
                            String key = (entry.getKey());
                            Float value = Float.parseFloat(entry.getValue());



//                            long time = (key+GMT); //???????????
//                            String ModernTime = DateFormat.format("HH:mm", time).toString();

                            ints.add(Math.round(value)); // add to Y axis
                            bottomStringList.add(key);// add to bottom Legend or X axis
                        }

                        dataLists.add(ints);
                        //////////////////////////////////////////////////////////
                        //              settings of graphic                     //
                        //////////////////////////////////////////////////////////
                        lineView.setDrawDotLine(true); //optional
                        lineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional
                        lineView.setBottomTextList(bottomStringList);
                        lineView.setColorArray(new int[]{Color.BLUE,Color.GRAY, Color.CYAN});
                        lineView.setDataList(dataLists); //or lineView.setFloatDataList(floatDataLists)

                    }catch (Exception e){
                        Toast.makeText(itemView.getContext(), "No Internet. Check Internet connection", Toast.LENGTH_LONG).show();
                    }







                }
            }, 5400);
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
