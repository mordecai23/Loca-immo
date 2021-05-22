package com.example.projetandroid;

public class annonce {
    private String idAnnonce;
    private String titre;
    private String TypeBien;
    private String description;
    private String dureedispo;
    private String Adresse;
    private String TypeContact;
    private String Ville;
    private String Loyer;
    private String surface;
    private String[] lienimage;

    public annonce(String idAnnonce, String titre, String TypeBien, String description) {
        this.idAnnonce = idAnnonce;
        this.titre = titre;
        this.TypeBien = TypeBien;
        this.description = description;
    }

    public String[] getLienimage() {
        return lienimage;
    }

    public void setLienimage(String[] lienimage) {
        this.lienimage = lienimage;
    }

    public String getDureedispo() {
        return dureedispo;
    }

    public void setDureedispo(String dureedispo) {
        this.dureedispo = dureedispo;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    public String getTypeContact() {
        return TypeContact;
    }

    public void setTypeContact(String typeContact) {
        TypeContact = typeContact;
    }

    public String getVille() {
        return Ville;
    }

    public void setVille(String ville) {
        Ville = ville;
    }

    public String getLoyer() {
        return Loyer;
    }

    public void setLoyer(String loyer) {
        Loyer = loyer;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTypeBien() {
        return TypeBien;
    }

    public void setTypeBien(String typeBien) {
        TypeBien = typeBien;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

