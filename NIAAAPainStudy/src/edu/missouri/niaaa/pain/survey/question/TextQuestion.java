package edu.missouri.niaaa.pain.survey.question;

import java.util.ArrayList;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.missouri.niaaa.pain.survey.category.QuestionType;
import edu.missouri.niaaa.pain.survey.category.SurveyQuestion;

public class TextQuestion extends SurveyQuestion{

    String selectedText = "";

    public TextQuestion(String id){
        this.questionId = id;
        this.questionType = QuestionType.TEXT;
    }


    @Override
    public LinearLayout prepareLayout(Context c) {
        LinearLayout layout = new LinearLayout(c);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView questionText = new TextView(c);
        questionText.setText(this.getQuestion().replace("|", "\n"));
        //questionText.setTextAppearance(c, R.attr.textAppearanceLarge);
        questionText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        questionText.setLines(4);

        LinearLayout.LayoutParams layoutq = new LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutq.setMargins(10, 15, 0, 0);


        questionText.setLayoutParams(layoutq);


        EditText editText = new EditText(c);
        editText.addTextChangedListener(new TextWatcher(){


            @Override
            public void afterTextChanged(Editable arg0) {
                selectedText = arg0.toString();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

        });

        LinearLayout.LayoutParams layoutp = new LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutp.setMargins(0, 40, 0, 0);
        editText.setLayoutParams(layoutp);

        layout.addView(questionText);
        layout.addView(editText);

        return layout;
    }


    @Override
    public boolean validateSubmit() {
        if(selectedText.length() == 0) {
            return false;
        }
        return true;
    }


    @Override
    public String getSkip() {
        return null;
    }

    @Override
    public ArrayList<String> getSelectedAnswers(){
        ArrayList<String> temp = new ArrayList<String>();
        temp.add(selectedText);
        return temp;
    }

    @Override
    public boolean clearSelectedAnswers(){
//      answers = null;
//      answered = false;
        return true;
    }
}
