/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.kandksolutions.divaga;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class DetallesEventosActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_eventos);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // Set title of Detail page
        // collapsingToolbar.setTitle(getString(R.string.item_title));

        int postion = getIntent().getIntExtra(EXTRA_POSITION, 0);
        Resources resources = getResources();
        String[] events = resources.getStringArray(R.array.eventos_titulos);
        collapsingToolbar.setTitle(events[postion % events.length]);

        String[] eventDetails = resources.getStringArray(R.array.eventos_descripciones);
        TextView eventDetail = (TextView) findViewById(R.id.event_detail);
        eventDetail.setText(eventDetails[postion % eventDetails.length]);

        String[] eventLocations = resources.getStringArray(R.array.eventos_direcciones);
        TextView eventLocation =  (TextView) findViewById(R.id.event_location);
        eventLocation.setText(eventLocations[postion % eventLocations.length]);

        String[] eventHorarios = resources.getStringArray(R.array.eventos_fechas);
        TextView eventHorario =  (TextView) findViewById(R.id.event_fecha);
        eventHorario.setText(eventHorarios[postion % eventHorarios.length]);


        TypedArray eventPictures = resources.obtainTypedArray(R.array.eventos_imagenes);
        ImageView eventPicture = (ImageView) findViewById(R.id.image);
        if (eventPicture != null) {
            eventPicture.setAlpha((float) 0.9);
            eventPicture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        eventPicture.setImageDrawable(eventPictures.getDrawable(postion % eventPictures.length()));

        eventPictures.recycle();
    }
}
