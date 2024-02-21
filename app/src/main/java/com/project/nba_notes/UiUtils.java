package com.project.nba_notes;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class UiUtils {
    // Método estático para ocultar el teclado virtual en una actividad.
    // Recibe un objeto 'Activity' como parámetro.
    public static void hideKeyboard(Activity activity) {
        // Obtiene la vista actualmente en foco en la actividad.
        View view = activity.getCurrentFocus();
        // Si hay una vista en foco...
        if (view != null) {
            // Crea un InputMethodManager para interactuar con el teclado virtual.
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            // Oculta el teclado virtual. El token de ventana se obtiene de la vista en foco.
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Método estático para aumentar el tamaño del texto en dos vistas TextView.
    // Recibe dos objetos TextView como parámetros.
    public static void increaseTextSize(TextView... textViews) {
        // Aumenta el tamaño del texto en 5 unidades (píxeles) para 'noteContent'.
        for(TextView textView : textViews){
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textView.getTextSize() +5);
        }
    }

    // Método estático para restablecer el tamaño del texto en dos vistas TextView.
    // Disminuye el tamaño del texto en 5 unidades (píxeles).
    public static void resetTextSize(TextView... textViews) {
        for(TextView textView : textViews){
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textView.getTextSize() -5);
        }
    }

    // Método estático para eliminar el foco de una o más vistas.
    // Recibe un número variable de objetos View.
    public static void resetFocus(View... views) {
        // Recorre cada vista proporcionada en el argumento.
        for (View view : views) {
            // Si la vista no es nula, elimina su foco.
            if (view != null) {
                view.clearFocus();
            }
        }
    }
}
