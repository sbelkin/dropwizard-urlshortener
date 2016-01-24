package com.example.shortener.core;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "url")
@NamedQueries({
        @NamedQuery(
                name = "Url.findAll",
                query = "SELECT p FROM Url p"
        )
})
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @Column(name = "urlTarget", nullable = false)
    private String urlTarget;

    public Url() {
    }

    public Url(String urlTarget) {
        this.timestamp = new Timestamp(new Date().getTime());
        this.urlTarget = urlTarget;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrlTarget() {
        return urlTarget;
    }

    public void setUrlTarget(String urlTarget) {
        this.urlTarget = urlTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Url)) {
            return false;
        }

        final Url that = (Url) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.timestamp, that.timestamp) &&
                Objects.equals(this.urlTarget, that.urlTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, urlTarget);
    }
}
