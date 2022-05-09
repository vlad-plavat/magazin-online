package org.nl.model;

import org.dizitart.no2.objects.Id;

import java.util.Date;
import java.util.Objects;

public class Feedback {
    private String username;
    private String text;
    private Date date;

    public Feedback(String username, String text, Date date) {
        this.username = username;
        this.text = text;
        this.date = date;
    }

    public Feedback(){

    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;

        return Objects.equals(username, feedback.username) && Objects.equals(text, feedback.text) && Objects.equals(date, feedback.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, text, date);
    }
}
