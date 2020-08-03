package com.example.finalproject.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.finalproject.Models.LandMark;
import com.example.finalproject.Models.TouristPlace;
import com.example.finalproject.Views.MapsActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//This class recover all data from database
public class TouristPlacesLandmarksController {

    static ArrayList<TouristPlace> touristPlaces = new ArrayList<TouristPlace>();
    static ArrayList<LandMark> landMarks = new ArrayList<LandMark>();
    DatabaseReference mDatabase;

    public TouristPlacesLandmarksController(boolean charge){
        if (charge){
            fillMarkers();
            fillLandMarks();
        }
    }

    private void fillMarkers(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Places");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                touristPlaces.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode: dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Log.e("Key", keyNode.getKey());
                    TouristPlace touristPlace = keyNode.getValue(TouristPlace.class);
                    touristPlaces.add(touristPlace);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error","The read failed: " + databaseError.getCode());
            }
        });


    }

    public void fillLandMarks(){
        mDatabase = FirebaseDatabase.getInstance().getReference("LandMarks");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                landMarks.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode: dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Log.e("Key", keyNode.getKey());
                    LandMark landMark = keyNode.getValue(LandMark.class);
                    landMarks.add(landMark);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error","The read failed: " + databaseError.getCode());
            }
        });

        /*landMarks.add(new LandMark("Santa Catalina", "Sor Ana de los Ángeles",
                "https://diariocorreo.pe/resizer/4X1kil7myYrOsmsiX2pSyFaJlK0=/980x528/smart/arc-anglerfish-arc2-prod-elcomercio.s3.amazonaws.com/public/K7653YVDVZASNNHMILMIKF3L6U.jpg",
                "Según algunas deducciones y testimonios se presume que nació el 26 de julio de 1604, fecha que no se puede asumir como exacta, ya que su Fe de Bautismo se perdió en un incendio ocurrido en la sacristía de la Iglesia Mayor de Arequipa, antecesora de la primera catedral de la ciudad, en 1620. Ana fue la cuarta de ocho hijos que conformaron la familia Sebastián de Monteagudo y Francisca de León: Francisco, Mariana, Catalina, (Ana), Juana, Inés, Andrea y Sebastián."));
        landMarks.add(new LandMark("Santa Catalina", "Sor Ana de los Ángeles 2",
                "https://diariocorreo.pe/resizer/4X1kil7myYrOsmsiX2pSyFaJlK0=/980x528/smart/arc-anglerfish-arc2-prod-elcomercio.s3.amazonaws.com/public/K7653YVDVZASNNHMILMIKF3L6U.jpg",
                "Según algunas deducciones y testimonios se presume que nació el 26 de julio de 1604, fecha que no se puede asumir como exacta, ya que su Fe de Bautismo se perdió en un incendio ocurrido en la sacristía de la Iglesia Mayor de Arequipa, antecesora de la primera catedral de la ciudad, en 1620. Ana fue la cuarta de ocho hijos que conformaron la familia Sebastián de Monteagudo y Francisca de León: Francisco, Mariana, Catalina, (Ana), Juana, Inés, Andrea y Sebastián."));
        landMarks.add(new LandMark("Santa Catalina", "Sor Ana de los Ángeles 3",
                "https://diariocorreo.pe/resizer/4X1kil7myYrOsmsiX2pSyFaJlK0=/980x528/smart/arc-anglerfish-arc2-prod-elcomercio.s3.amazonaws.com/public/K7653YVDVZASNNHMILMIKF3L6U.jpg",
                "Según algunas deducciones y testimonios se presume que nació el 26 de julio de 1604, fecha que no se puede asumir como exacta, ya que su Fe de Bautismo se perdió en un incendio ocurrido en la sacristía de la Iglesia Mayor de Arequipa, antecesora de la primera catedral de la ciudad, en 1620. Ana fue la cuarta de ocho hijos que conformaron la familia Sebastián de Monteagudo y Francisca de León: Francisco, Mariana, Catalina, (Ana), Juana, Inés, Andrea y Sebastián."));
        landMarks.add(new LandMark("Santa Catalina", "Sor Ana de los Ángeles 4",
                "https://diariocorreo.pe/resizer/4X1kil7myYrOsmsiX2pSyFaJlK0=/980x528/smart/arc-anglerfish-arc2-prod-elcomercio.s3.amazonaws.com/public/K7653YVDVZASNNHMILMIKF3L6U.jpg",
                "Según algunas deducciones y testimonios se presume que nació el 26 de julio de 1604, fecha que no se puede asumir como exacta, ya que su Fe de Bautismo se perdió en un incendio ocurrido en la sacristía de la Iglesia Mayor de Arequipa, antecesora de la primera catedral de la ciudad, en 1620. Ana fue la cuarta de ocho hijos que conformaron la familia Sebastián de Monteagudo y Francisca de León: Francisco, Mariana, Catalina, (Ana), Juana, Inés, Andrea y Sebastián."));
        */
    }

    public ArrayList<TouristPlace> getTouristPlaces(){
        return touristPlaces;
    }

    public ArrayList<LandMark> getLandMarks() {
        return landMarks;
    }
}
