package org.nl.services;

import javafx.scene.control.TextField;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.SortOrder;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.nl.controllers.FeedbackViewController;
import org.nl.model.Feedback;
import org.nl.model.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class FeedbackService {
    private static ObjectRepository<Feedback> feedbackRepository;

    public static void initDatabase() {
        feedbackRepository = UserService.getDatabase().getRepository(Feedback.class);

    }

    public static void addFeedback(String username, String text, Date date){
        feedbackRepository.insert(new Feedback(username, text, date));
    }

    public static void printAllFeedback(){
        NitriteCollection nc = feedbackRepository.getDocumentCollection();
        org.dizitart.no2.Cursor lind = nc.find();
        for (Document d : lind) {
            SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' hh:mm:ss");
            System.out.println("" + d.get("username") + " " + d.get("text") + " " + ft.format(d.get("date")));
        }
        System.out.println();
    }

    public static Cursor<Feedback> getAllFeedbacks() {
        return feedbackRepository.find(
                FindOptions.sort("date" , SortOrder.Descending)
        );
    }
    public static boolean checkText(Feedback p, TextField searchField) {
        String[] words = searchField.getText().split("\\s+");
        for(String s : words){
            if(!p.getText().toLowerCase().contains(s.toLowerCase()))
                return false;
        }
        return true;
    }

    public static void deleteFeedback(Feedback f, FeedbackViewController fvc) {
        feedbackRepository.remove(ObjectFilters.and(
                ObjectFilters.eq("date", f.getDate()), ObjectFilters.eq("username", f.getUsername())));
        fvc.reloadFeedback();
    }
}
