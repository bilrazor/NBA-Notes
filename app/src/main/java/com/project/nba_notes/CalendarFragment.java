package com.project.nba_notes;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class CalendarFragment extends Fragment {
    private JSONArray notesArray;
    MCalendarView mcalendar;
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;

    private RequestQueue requestQueue;

    public CalendarFragment() {
        super(R.layout.fragment_calendar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        requestQueue = Volley.newRequestQueue(requireContext());

        mcalendar = view.findViewById(R.id.mcalendar);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        notesAdapter = new NotesAdapter(new ArrayList<>());
        recyclerView.setAdapter(notesAdapter);

        getNotes();  // Call the method to get notes

        mcalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                if (notesArray != null) {
                    String selectedDate = date.getYear() + "-" + date.getMonthString() + "-" + date.getDayString();
                    List<String> titlesList = new ArrayList<>();

                    for (int i = 0; i < notesArray.length(); i++) {
                        try {
                            JSONObject note = notesArray.getJSONObject(i);
                            String lastModified = note.getString("lastModified");
                            String parsedDate = parseDate(lastModified);

                            if (selectedDate.equals(parsedDate)) {
                                String title = note.getString("title");
                                titlesList.add(title);
                                Log.d("CalendarFragment", "Title: " + title + ", Date: " + parsedDate);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (titlesList.isEmpty()) {
                        titlesList.add("No hay notas para este día");
                    }
                    notesAdapter.setNotesList(titlesList);
                }
            }
        });

        return view;
    }

    private void getNotes() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                Server.name + "/api/auth/notes",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        notesArray = response;
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject note = response.getJSONObject(i);
                                String lastModified = note.getString("lastModified");
                                int year = Integer.parseInt(lastModified.substring(0, 4));
                                int month = Integer.parseInt(lastModified.substring(5, 7));
                                int day = Integer.parseInt(lastModified.substring(8, 10));

                                mcalendar.setMarkedStyle(MarkStyle.BACKGROUND, Color.RED);
                                mcalendar.markDate(year, month, day);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        if (error.networkResponse == null) {
                            Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_LONG).show();
                        } else {
                            String serverCode = null;
                            try {
                                serverCode = new String(error.networkResponse.data, "utf-8");
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            Toast.makeText(getContext(), serverCode, Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        requestQueue.add(request);
    }

    private String parseDate(String dateString) {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        try {
            Date date = isoFormat.parse(dateString);
            SimpleDateFormat friendlyFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return friendlyFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
