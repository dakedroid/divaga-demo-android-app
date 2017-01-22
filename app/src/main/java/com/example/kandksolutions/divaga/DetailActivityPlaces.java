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
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class DetailActivityPlaces extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_places);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // Set title of Detail page
        // collapsingToolbar.setTitle(getString(R.string.item_title));

        int postion = getIntent().getIntExtra(EXTRA_POSITION, 0);
        Resources resources = getResources();
        String[] places = resources.getStringArray(R.array.lugares_titulos);
        collapsingToolbar.setTitle(places[postion % places.length]);

        String[] placeDetails = resources.getStringArray(R.array.lugares_detalles);
        TextView placeDetail = (TextView) findViewById(R.id.place_detail);
        placeDetail.setText(placeDetails[postion % placeDetails.length]);

        String[] placeLocations = resources.getStringArray(R.array.lugares_direcciones);
        TextView placeLocation =  (TextView) findViewById(R.id.place_location);
        placeLocation.setText(placeLocations[postion % placeLocations.length]);

        String[] placeHorarios = resources.getStringArray(R.array.lugares_horarios);
        TextView placeHorario =  (TextView) findViewById(R.id.place_horario);
        placeHorario.setText(placeHorarios[postion % placeHorarios.length]);

        String[] placeTelefonos = resources.getStringArray(R.array.lugares_telefonos);
        TextView placeTelefono =  (TextView) findViewById(R.id.place_telefono);
        placeTelefono.setText(placeTelefonos[postion % placeTelefonos.length]);


        TypedArray placePictures = resources.obtainTypedArray(R.array.lugares_imagenes);
        ImageView placePicutre = (ImageView) findViewById(R.id.image);
        if (placePicutre != null) {
            placePicutre.setAlpha((float) 0.9);
            placePicutre.setScaleType(ImageView.ScaleType.MATRIX);
        }
        placePicutre.setImageDrawable(placePictures.getDrawable(postion % placePictures.length()));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailActivityPlaces.this,LocationPlacesActivity.class);
                    startActivity(intent);
                }
            });
        }

        placePictures.recycle();
    }
}
