package com.project.nba_notes;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
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
import java.util.TimeZone;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class CalendarFragment extends Fragment {
    private Date lastModified;
    private JSONArray notesArray;
    MCalendarView mcalendar;
    TextView date_view;

    public CalendarFragment() {
        super(R.layout.fragment_calendar);
    }
/*
    public Date notesData(JSONObject dateinfo) {
        try {
            String dateString = dateinfo.getString("lastModified");

            // Quitar los microsegundos de la cadena de fecha y asumir que es UTC añadiendo 'Z'
            String modifiedDateString = dateString.substring(0, dateString.length() - 3) + "Z";

            // Configurar SimpleDateFormat para manejar la fecha en formato ISO 8601
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Asegúrate de que el objeto Date esté en UTC

            // Parsear la cadena de fecha y luego formatearla al formato deseado
            Date parsedDate = isoFormat.parse(modifiedDateString);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = outputFormat.format(parsedDate);

            // Asignar la fecha formateada al campo lastModified
            this.lastModified = outputFormat.parse(formattedDate);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return lastModified;
    }
*/
/*
    public Date notesData(JSONObject dateinfo) {
        try {
            String dateString = dateinfo.getString("lastModified");

            // Asegúrate de que el formato de fecha refleje la presencia de microsegundos
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date parsedDate = isoFormat.parse(dateString);
            this.lastModified = parsedDate;
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return lastModified;
    }
*/

    private void getNotes() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                Server.name + "/api/auth/notes",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Almacena las notas en la variable de instancia
                        notesArray = response;
                        try {
                            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
                            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject note = response.getJSONObject(i);
                                String title = note.getString("title");
                                String lastModified = note.getString("lastModified");

                                // Parsea la fecha directamente aquí
                                Date modifiedDate = isoFormat.parse(lastModified);
                                String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(modifiedDate);

                                // Aquí puedes procesar el título y la fecha de modificación de cada nota
                                Toast.makeText(getContext(), "Título: " + title + "\nFecha: " + formattedDate, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException | ParseException e) {
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
                }

        );

        // Agrega esta línea para enviar la solicitud
        Volley.newRequestQueue(getContext()).add(request);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        date_view = view.findViewById(R.id.date_view);
        mcalendar = view.findViewById(R.id.mcalendar);

        getNotes();  // Llama al método para obtener las notas

        mcalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                if (notesArray != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
                    String selectedDate = date.getYear() + "-" + date.getMonthString() + "-" + date.getDayString();

                    for (int i = 0; i < notesArray.length(); i++) {
                        try {
                            JSONObject note = notesArray.getJSONObject(i);
                            String lastModified = note.getString("lastModified");

                            Date modifiedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
                                    .parse(lastModified);
                            String formattedDate = dateFormat.format(modifiedDate);

                            if (selectedDate.equals(formattedDate)) {
                                mcalendar.markDate(date);
                                String title = note.getString("title");
                                Toast.makeText(getContext(), "Título: " + title, Toast.LENGTH_SHORT).show();
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
