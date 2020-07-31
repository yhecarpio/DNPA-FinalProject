package com.example.finalproject.Controllers;

import com.example.finalproject.Models.LandMark;
import com.example.finalproject.Models.TouristPlace;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

//This class recover all data from database
public class TouristPlacesLandmarksController {

    static ArrayList<TouristPlace> touristPlaces = new ArrayList<TouristPlace>();
    static ArrayList<LandMark> landMarks = new ArrayList<LandMark>();

    public TouristPlacesLandmarksController(){
        fillMarkers();
    }

    private void fillMarkers(){

        touristPlaces.add(new TouristPlace("Parque Umachiri", new LatLng(-16.39971377133493, -71.50651927719629), "Descripcion: dcskdfskhdsf"));
        touristPlaces.add(new TouristPlace("Parque Umachiri II", new LatLng(-16.400990017022863, -71.50685187111453),"Descripcion: dcskdfskhdsf"));
        touristPlaces.add(new TouristPlace("Parque Umachiri 3", new LatLng(-16.40070183326229, -71.50076862106157),"Descripcion: dcskdfskhdsf"));
        touristPlaces.add(new TouristPlace("Parque Umachiri 4", new LatLng(-16.397758217426443, -71.5070449901583),"Descripcion: dcskdfskhdsf"));

    }

    public void fillLandMarks(){
        landMarks.add(new LandMark("Santa Catalina", "Sor Ana de los Ángeles",
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

    }

    public ArrayList<TouristPlace> getTouristPlaces(){
        return touristPlaces;
    }

    public ArrayList<LandMark> getLandMarks() {
        return landMarks;
    }
}
