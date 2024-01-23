package com.project.nba_notes;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class CalendarFragment extends Fragment {
    private Date lastModified;
    private String title;
    MCalendarView mcalendar;
    TextView date_view;



    public CalendarFragment(Date lastModified, String title){
        super(R.layout.fragment_calendar);
        this.lastModified = lastModified;
        this.title = title;
    }

    public Date NotesData(JSONObject dateinfo) {
        try {
            String dateString = dateinfo.getString("lastModified");

            // Quitar los microsegundos de la cadena de fecha y asumir que es UTC añadiendo 'Z'
            String modifiedDateString = dateString.substring(0, dateString.length() - 3) + "Z";

            // Configurar SimpleDateFormat para manejar la fecha en formato ISO 8601
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Asegúrate de que el objeto Date esté en UTC

            this.lastModified = isoFormat.parse(modifiedDateString);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return this.lastModified;
    }


    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //calendar = (CalendarView) getView().findViewById(R.id.calendar);
        date_view = (TextView) getView().findViewById(R.id.date_view);
        mcalendar = (MCalendarView) getView().findViewById(R.id.mcalendar);


     mcalendar.setOnDateClickListener(new OnDateClickListener() {
         @Override
         public void onDateClick(View view, DateData date) {
             String Date=date.getDayString()+"-"+date.getMonthString()+"-"+date.getYear();
             date_view.setText(Date);
         }
     });


/*
        calendar.setOnDateChangeListener(
                new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        String Date = dayOfMonth + "-" + (month + 1) + "-" + year;
                        date_view.setText(Date);
                    }
                }
        );
*/


    }

}
