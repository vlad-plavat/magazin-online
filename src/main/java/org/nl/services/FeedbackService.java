package org.nl.services;

import org.dizitart.no2.Cursor;
import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.objects.ObjectRepository;
import org.nl.model.Feedback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class FeedbackService {
    private static ObjectRepository<Feedback> feedbackRepository;

    public static void initDatabase() {
        feedbackRepository = UserService.getDatabase().getRepository(Feedback.class);

    }

    public static Feedback addFeedback(String username, String text, Date date){
        feedbackRepository.insert(new Feedback(username, text, date));
        return new Feedback(username, text, date);
    }

    public static void printAllFeedback(){
        NitriteCollection nc = feedbackRepository.getDocumentCollection();
        Cursor lind = nc.find();
        Iterator<Document> it = lind.iterator();
        while (it.hasNext()) {
            Document d = it.next();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' hh:mm:ss");
            System.out.println( "" + d.get("username") + " " + d.get("text") + " " + ft.format(d.get("date")) );
        }
        System.out.println("");
    }

}
