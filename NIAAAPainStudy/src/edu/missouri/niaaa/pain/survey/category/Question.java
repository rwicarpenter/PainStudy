package edu.missouri.niaaa.pain.survey.category;

import java.util.ArrayList;

import android.content.Context;
import android.widget.LinearLayout;

public interface Question {

    public String getQuestion();

    public void setQuestion(String questionText);

    public QuestionType getQuestionType();

    public void setQuestionType(QuestionType type);

    public void addAnswer(Answer answer);

    public void addAnswers(ArrayList<Answer> answers);

    public void addAnswers(Answer[] answers);

    public ArrayList<Answer> getAnswers();

    public LinearLayout prepareLayout(Context c);

    public boolean validateSubmit();

    public String getSkip();

    public String getId();

    public ArrayList<String> getSelectedAnswers();

    public boolean clearSelectedAnswers();
    
    public void setSoftSkip(String skip);
    public String getSoftSkip();
    public boolean hasSoftSkip();

}
