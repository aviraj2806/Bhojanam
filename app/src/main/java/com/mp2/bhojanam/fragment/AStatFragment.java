package com.mp2.bhojanam.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.mp2.bhojanam.R;
import com.mp2.bhojanam.database.MealDatabase;
import com.mp2.bhojanam.database.MealEntity;
import com.mp2.bhojanam.database.StudentDatabse;
import com.mp2.bhojanam.database.StudentEntity;
import com.mp2.bhojanam.database.UserDatabase;
import com.mp2.bhojanam.database.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class AStatFragment extends Fragment {

    public AStatFragment() {
        // Required empty public constructor
    }

    BarChart chart,chartStu,chartSch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_stat, container, false);
        chart = view.findViewById(R.id.barchartMeals);
        chartStu = view.findViewById(R.id.barchartStudents);
        chartSch = view.findViewById(R.id.barchartSchool);

        chart.getXAxis().setLabelsToSkip(0);
        chart.getAxisRight().setEnabled(false);
        chart.setDescription("");

        chartStu.getXAxis().setLabelsToSkip(0);
        chartStu.getAxisRight().setEnabled(false);
        chartStu.setDescription("");

        chartSch.getXAxis().setLabelsToSkip(0);
        chartSch.getAxisRight().setEnabled(false);
        chartSch.setDescription("");

        loadMSGraph();
        loadSRGraph();
        loadSchGraph();

        return view;
    }

    private void loadSchGraph() {
        int orderJan, orderFeb, orderMar, orderApr, orderMay, orderJun, orderJul, orderAug, orderSept, orderOct, orderNov, orderDec, orderAll;
        orderJan = orderFeb = orderMar = orderApr = orderMay = orderJun = orderJul = orderAug = orderSept = orderOct = orderNov = orderDec = orderAll = 0;
        List<UserEntity> mealEntities = Room.databaseBuilder(getActivity(), UserDatabase.class, "user")
                .allowMainThreadQueries().build().userDao().getUsersByType("School In-charge");

        for (int i = 0; i < mealEntities.size(); i++) {
            String[] splitTimeStamp = mealEntities.get(i).getTimestamp().split(" ");
            String[] getYearMonth = splitTimeStamp[0].split("-");
            if(getYearMonth[0].equals("2020")){
                if(getYearMonth[1].equals("01")){
                    orderJan++;
                    orderAll++;
                }else if(getYearMonth[1].equals("02")){
                    orderFeb++;
                    orderAll++;
                }else if(getYearMonth[1].equals("03")){
                    orderMar++;
                    orderAll++;
                }else if(getYearMonth[1].equals("04")){
                    orderApr++;
                    orderAll++;
                }else if(getYearMonth[1].equals("05")){
                    orderMay++;
                    orderAll++;
                }else if(getYearMonth[1].equals("06")){
                    orderJun++;
                    orderAll++;
                }else if(getYearMonth[1].equals("07")){
                    orderJul++;
                    orderAll++;
                }else if(getYearMonth[1].equals("08")){
                    orderAug++;
                    orderAll++;
                }else if(getYearMonth[1].equals("09")){
                    orderSept++;
                    orderAll++;
                }else if(getYearMonth[1].equals("10")){
                    orderOct++;
                    orderAll++;
                }else if(getYearMonth[1].equals("11")){
                    orderNov++;
                    orderAll++;
                }else if(getYearMonth[1].equals("12")){
                    orderDec++;
                    orderAll++;
                }
            }
        }

        ArrayList noOfOrder = new ArrayList();

        noOfOrder.add(new BarEntry(orderJan, 0));
        noOfOrder.add(new BarEntry(orderFeb, 1));
        noOfOrder.add(new BarEntry(orderMar, 2));
        noOfOrder.add(new BarEntry(orderApr, 3));
        noOfOrder.add(new BarEntry(orderMay, 4));
        noOfOrder.add(new BarEntry(orderJun, 5));
        noOfOrder.add(new BarEntry(orderJul, 6));
        noOfOrder.add(new BarEntry(orderAug, 7));
        noOfOrder.add(new BarEntry(orderSept, 8));
        noOfOrder.add(new BarEntry(orderOct, 9));
        noOfOrder.add(new BarEntry(orderNov, 10));
        noOfOrder.add(new BarEntry(orderDec, 11));

        ArrayList year = new ArrayList();

        year.add("Jan");
        year.add("Feb");
        year.add("Mar");
        year.add("Apr");
        year.add("May");
        year.add("Jun");
        year.add("Jul");
        year.add("Aug");
        year.add("Sept");
        year.add("Oct");
        year.add("Nov");
        year.add("Dec");


        BarDataSet bardataset = new BarDataSet(noOfOrder, "Students Registered");
        chartSch.animateY(1000);
        BarData data = new BarData(year,bardataset);
        bardataset.setColor(getResources().getColor(R.color.colorAccent));
        chartSch.setData(data);
    }


    public void loadMSGraph() {
        int orderJan, orderFeb, orderMar, orderApr, orderMay, orderJun, orderJul, orderAug, orderSept, orderOct, orderNov, orderDec, orderAll;
        orderJan = orderFeb = orderMar = orderApr = orderMay = orderJun = orderJul = orderAug = orderSept = orderOct = orderNov = orderDec = orderAll = 0;
        List<MealEntity> mealEntities = Room.databaseBuilder(getActivity(), MealDatabase.class, "meal")
                .allowMainThreadQueries().build().mealDao().getAllMeals();

        for (int i = 0; i < mealEntities.size(); i++) {
            String[] splitTimeStamp = mealEntities.get(i).getTimestamp().split(" ");
            String[] getYearMonth = splitTimeStamp[0].split("-");
            if(getYearMonth[0].equals("2020")){
                if(getYearMonth[1].equals("01")){
                    orderJan++;
                    orderAll++;
                }else if(getYearMonth[1].equals("02")){
                    orderFeb++;
                    orderAll++;
                }else if(getYearMonth[1].equals("03")){
                    orderMar++;
                    orderAll++;
                }else if(getYearMonth[1].equals("04")){
                    orderApr++;
                    orderAll++;
                }else if(getYearMonth[1].equals("05")){
                    orderMay++;
                    orderAll++;
                }else if(getYearMonth[1].equals("06")){
                    orderJun++;
                    orderAll++;
                }else if(getYearMonth[1].equals("07")){
                    orderJul++;
                    orderAll++;
                }else if(getYearMonth[1].equals("08")){
                    orderAug++;
                    orderAll++;
                }else if(getYearMonth[1].equals("09")){
                    orderSept++;
                    orderAll++;
                }else if(getYearMonth[1].equals("10")){
                    orderOct++;
                    orderAll++;
                }else if(getYearMonth[1].equals("11")){
                    orderNov++;
                    orderAll++;
                }else if(getYearMonth[1].equals("12")){
                    orderDec++;
                    orderAll++;
                }
            }
        }

        ArrayList noOfOrder = new ArrayList();

        noOfOrder.add(new BarEntry(orderJan, 0));
        noOfOrder.add(new BarEntry(orderFeb, 1));
        noOfOrder.add(new BarEntry(orderMar, 2));
        noOfOrder.add(new BarEntry(orderApr, 3));
        noOfOrder.add(new BarEntry(orderMay, 4));
        noOfOrder.add(new BarEntry(orderJun, 5));
        noOfOrder.add(new BarEntry(orderJul, 6));
        noOfOrder.add(new BarEntry(orderAug, 7));
        noOfOrder.add(new BarEntry(orderSept, 8));
        noOfOrder.add(new BarEntry(orderOct, 9));
        noOfOrder.add(new BarEntry(orderNov, 10));
        noOfOrder.add(new BarEntry(orderDec, 11));

        ArrayList year = new ArrayList();

        year.add("Jan");
        year.add("Feb");
        year.add("Mar");
        year.add("Apr");
        year.add("May");
        year.add("Jun");
        year.add("Jul");
        year.add("Aug");
        year.add("Sept");
        year.add("Oct");
        year.add("Nov");
        year.add("Dec");


        BarDataSet bardataset = new BarDataSet(noOfOrder, "Meals Served");
        chart.animateY(1000);
        BarData data = new BarData(year,bardataset);
        bardataset.setColor(getResources().getColor(R.color.colorAccent));
        chart.setData(data);
    }

    public void loadSRGraph(){
        int orderJan, orderFeb, orderMar, orderApr, orderMay, orderJun, orderJul, orderAug, orderSept, orderOct, orderNov, orderDec, orderAll;
        orderJan = orderFeb = orderMar = orderApr = orderMay = orderJun = orderJul = orderAug = orderSept = orderOct = orderNov = orderDec = orderAll = 0;
        List<StudentEntity> mealEntities = Room.databaseBuilder(getActivity(), StudentDatabse.class, "student")
                .allowMainThreadQueries().build().studentDao().getAllStudents();

        for (int i = 0; i < mealEntities.size(); i++) {
            String[] splitTimeStamp = mealEntities.get(i).getTimestamp().split(" ");
            String[] getYearMonth = splitTimeStamp[0].split("-");
            if(getYearMonth[0].equals("2020")){
                if(getYearMonth[1].equals("01")){
                    orderJan++;
                    orderAll++;
                }else if(getYearMonth[1].equals("02")){
                    orderFeb++;
                    orderAll++;
                }else if(getYearMonth[1].equals("03")){
                    orderMar++;
                    orderAll++;
                }else if(getYearMonth[1].equals("04")){
                    orderApr++;
                    orderAll++;
                }else if(getYearMonth[1].equals("05")){
                    orderMay++;
                    orderAll++;
                }else if(getYearMonth[1].equals("06")){
                    orderJun++;
                    orderAll++;
                }else if(getYearMonth[1].equals("07")){
                    orderJul++;
                    orderAll++;
                }else if(getYearMonth[1].equals("08")){
                    orderAug++;
                    orderAll++;
                }else if(getYearMonth[1].equals("09")){
                    orderSept++;
                    orderAll++;
                }else if(getYearMonth[1].equals("10")){
                    orderOct++;
                    orderAll++;
                }else if(getYearMonth[1].equals("11")){
                    orderNov++;
                    orderAll++;
                }else if(getYearMonth[1].equals("12")){
                    orderDec++;
                    orderAll++;
                }
            }
        }

        ArrayList noOfOrder = new ArrayList();

        noOfOrder.add(new BarEntry(orderJan, 0));
        noOfOrder.add(new BarEntry(orderFeb, 1));
        noOfOrder.add(new BarEntry(orderMar, 2));
        noOfOrder.add(new BarEntry(orderApr, 3));
        noOfOrder.add(new BarEntry(orderMay, 4));
        noOfOrder.add(new BarEntry(orderJun, 5));
        noOfOrder.add(new BarEntry(orderJul, 6));
        noOfOrder.add(new BarEntry(orderAug, 7));
        noOfOrder.add(new BarEntry(orderSept, 8));
        noOfOrder.add(new BarEntry(orderOct, 9));
        noOfOrder.add(new BarEntry(orderNov, 10));
        noOfOrder.add(new BarEntry(orderDec, 11));

        ArrayList year = new ArrayList();

        year.add("Jan");
        year.add("Feb");
        year.add("Mar");
        year.add("Apr");
        year.add("May");
        year.add("Jun");
        year.add("Jul");
        year.add("Aug");
        year.add("Sept");
        year.add("Oct");
        year.add("Nov");
        year.add("Dec");


        BarDataSet bardataset = new BarDataSet(noOfOrder, "Students Registered");
        chartStu.animateY(1000);
        BarData data = new BarData(year,bardataset);
        bardataset.setColor(getResources().getColor(R.color.colorAccent));
        chartStu.setData(data);
    }
}
