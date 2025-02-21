package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;


public class Capteurs {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("capteurs")
    private Set<Capteur> capteurs;

    public Capteurs() {
        this.capteurs = new LinkedHashSet<>();
    }

    public Set<Capteur> getCapteurs() {
        return capteurs;
    }

    public void setCapteurs(Set<Capteur> capteurs) {
        if (capteurs != null) {
            this.capteurs= capteurs;
        } else {
            this.capteurs = new LinkedHashSet<>();
        };
    }

    public final Capteurs add (final Capteur capteur) {
        capteurs.add(capteur);
        return this;
    }

    @Override
    public String toString() {
        return "Capteurs{" +
                "capteurs=" + capteurs +
                '}';
    }
}

