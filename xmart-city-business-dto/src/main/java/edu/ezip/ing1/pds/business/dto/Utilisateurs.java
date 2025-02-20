package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.LinkedHashSet;
import java.util.Set;

@JsonRootName(value = "utilisateurs")
public class Utilisateurs {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("utilisateurs")
    private Set<Utilisateur> utilisateurs;

    public Utilisateurs() {
        this.utilisateurs = new LinkedHashSet<>();
    }

    public Set<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(Set<Utilisateur> utilisateurs) {
        if (utilisateurs != null) {
            this.utilisateurs= utilisateurs;
        } else {
            this.utilisateurs = new LinkedHashSet<>();
        }
    }


    public Utilisateurs add(Utilisateur utilisateur) {
        if (utilisateur != null) {
            utilisateurs.add(utilisateur);
        }
        return this;
    }

    @Override
    public String toString() {
        return "Utilisateurs{" +
                "utilisateurs=" + utilisateurs +
                '}';
    }
}
