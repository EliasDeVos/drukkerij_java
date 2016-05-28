package drukkerij.model;

import javax.persistence.*;

@Entity
@Table(name = "DRUKITEMS")
public class DrukItem {
    @Id
    @GeneratedValue
    @Column(name = "DRUKITEMID")
    private Integer drukItemId;
    @Column(name = "KLANT")
    private String klant;
    @Column(name = "OPDRACHT")
    private String opdracht;
    @Column(name = "XPERVEL")
    private String xPerVel;
    @Column(name = "AANTALNODIG")
    private String aantalNodig;
    @Column(name = "INSCHIET")
    private String inschiet;
    @Column(name = "NMKNB")
    private String nmkNB;
    @Column(name = "Q")
    private String q;
    @Column(name = "ZW")
    private String zW;
    @Column(name = "ZWAAR4Z2")
    private String zwaar4Z2;
    @Column(name = "GLANZEND")
    private String glanzend;
    @Column(name = "HELDERHEID")
    private String helderheid;
    @Column(name = "SOORTPAPIER")
    private String soortPapier;
    @Column(name = "GEPLAATSTDOOR")
    private String geplaatstDoor;
    @Column(name = "PRINTER")
    private String printer;
    @Column(name = "DATE")
    private String date;
    @Column(name = "OPDRACHTVOOR")
    private String opdrachtVoor;
    @Column(name = "PRIORITEIT")
    private String prioriteit;
    @Column(name = "type")
    private String type;
    @Column(name = "COMMETAAR")
    private String commentaar;


    public DrukItem() {
        super();
    }

    //region Getters and setters
    public Integer getDrukItemId() {
        return drukItemId;
    }

    public String getKlant() {
        return klant;
    }

    public void setKlant(String klant) {
        this.klant = klant;
    }


    public String getOpdracht() {
        return opdracht;
    }

    public void setOpdracht(String opdracht) {
        this.opdracht = opdracht;
    }

    public void setDrukItemId(Integer drukItemId) {
        this.drukItemId = drukItemId;
    }

    public String getxPerVel() {
        return xPerVel;
    }

    public void setxPerVel(String xPerVel) {
        this.xPerVel = xPerVel;
    }

    public String getAantalNodig() {
        return aantalNodig;
    }

    public void setAantalNodig(String aantalNodig) {
        this.aantalNodig = aantalNodig;
    }

    public String getInschiet() {
        return inschiet;
    }

    public void setInschiet(String inschiet) {
        this.inschiet = inschiet;
    }

    public String getNmkNB() {
        return nmkNB;
    }

    public void setNmkNB(String nmkNB) {
        this.nmkNB = nmkNB;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getzW() {
        return zW;
    }

    public void setzW(String zW) {
        this.zW = zW;
    }

    public String getZwaar4Z2() {
        return zwaar4Z2;
    }

    public void setZwaar4Z2(String zwaar4Z2) {
        this.zwaar4Z2 = zwaar4Z2;
    }

    public String getGlanzend() {
        return glanzend;
    }

    public void setGlanzend(String glanzend) {
        this.glanzend = glanzend;
    }

    public String getHelderheid() {
        return helderheid;
    }

    public void setHelderheid(String helderheid) {
        this.helderheid = helderheid;
    }

    public String getSoortPapier() {
        return soortPapier;
    }

    public void setSoortPapier(String soortPapier) {
        this.soortPapier = soortPapier;
    }

    public String getGeplaatstDoor() {
        return geplaatstDoor;
    }

    public void setGeplaatstDoor(String geplaatstDoor) {
        this.geplaatstDoor = geplaatstDoor;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOpdrachtVoor() {
        return opdrachtVoor;
    }

    public void setOpdrachtVoor(String opdrachtVoor) {
        this.opdrachtVoor = opdrachtVoor;
    }

    public String getPrioriteit() {
        return prioriteit;
    }

    public void setPrioriteit(String prioriteit) {
        this.prioriteit = prioriteit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommentaar() {
        return commentaar;
    }

    public void setCommentaar(String commentaar) {
        this.commentaar = commentaar;
    }

    //endregion

}