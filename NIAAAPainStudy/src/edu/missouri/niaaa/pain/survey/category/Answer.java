package edu.missouri.niaaa.pain.survey.category;

public interface Answer {

    public void setAnswerText(String answerText);

    public void setClear(boolean clear);
    public void setExtraInput(boolean extraInput);

    public void setSurveyTrigger(String name);

    public void setSelected(boolean selected);
    public void setSkip(String id);

    public void setOption(String opt);
    public void setSoftTrigger(String softTrigger);

    public String getId();
    public String getAnswerText();
    public String getAnswerInput();

    public boolean checkClear();
    public boolean getExtraInput();

    public String getTriggerFile();
    public boolean hasSurveyTrigger();

    public boolean isSelected();
    public String getSkip();

    public String getOption();
    public boolean hasOption();
    
    public String getSoftTrigger();
    public boolean hasSoftTrigger();


    public boolean equals(Answer ans);
}
