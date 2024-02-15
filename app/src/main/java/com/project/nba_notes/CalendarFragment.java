package com.project.nba_notes;

import static sun.bob.mcalendarview.utils.CalendarUtil.date;

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
import java.util.Date;
import java.util.Locale;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class CalendarFragment extends Fragment {
    private JSONArray notesArray;
    MCalendarView mcalendar;
    TextView date_view;

    private RequestQueue queue;

    public CalendarFragment() {
        super(R.layout.fragment_calendar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Inicializar la RequestQueue
        queue = Volley.newRequestQueue(requireContext());

        date_view = view.findViewById(R.id.date_view);
        mcalendar = view.findViewById(R.id.mcalendar);

        getNotes();  // Llama al método getNotes()

        mcalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                if (notesArray != null) {
                    String selectedDate = date.getYear() + "-" + date.getMonthString() + "-" + date.getDayString();

                    StringBuilder titlesStringBuilder = new StringBuilder();

                    for (int i = 0; i < notesArray.length(); i++) {
                        try {
                            JSONObject note = notesArray.getJSONObject(i);
                            String lastModified = note.getString("lastModified");
                            parseDate(lastModified);

                            if (selectedDate.equals(parseDate(lastModified))) {
                                String title = note.getString("title");
                                titlesStringBuilder.append(title).append("\n\n");
                                Log.d("CalendarFragment", "Title: " + title + ", Date: " + parseDate(lastModified));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                        if (titlesStringBuilder.toString().equals("")){
                            String message = "No hay notas que mostrar";
                            date_view.setText(message);
                        }else{
                            date_view.setText(titlesStringBuilder.toString().trim()); // Display all titles
                        }

                }
            }
        });

        return view;
    }

    private void getNotes() {
        JsonArrayRequestWithAuthHeader2 request = new JsonArrayRequestWithAuthHeader2(
                Request.Method.GET,
                Server.name + "/api/auth/notes",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Almacena las notas en la variable de instancia
                        notesArray = response;
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject note = response.getJSONObject(i);
                                String lastModified = note.getString("lastModified");
                                int year = Integer.parseInt(lastModified.substring(0, 4));
                                int month = Integer.parseInt(lastModified.substring(5,7));
                                int day = Integer.parseInt(lastModified.substring(8,10));

                                mcalendar.setMarkedStyle(MarkStyle.BACKGROUND, Color.RED);
                                mcalendar.markDate(year,month,day);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();  // Imprimir detalles del error en la consola

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
                },
                getContext()
        );

        // Añadir la petición a la RequestQueue (cola de peticiones)
        queue.add(request);
    }

    private String parseDate(String dateString) throws ParseException {
        // Convertir la cadena de fecha a un objeto Date
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = isoFormat.parse(dateString);

        // Formatear la fecha a un formato más "amigable"
        SimpleDateFormat friendlyFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return friendlyFormat.format(date);
    }
}