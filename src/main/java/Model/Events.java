package Model;

import jakarta.persistence.*;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "events")
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column
    @NotNull
    public String type;

    @Column
    @NotNull
    public String title;

    @Column
    @NotNull
    public String date;

    @Column
    public String location;

    @Column
    @NotNull
    public Boolean online;

    @Column
    @NotNull
    public String link;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
