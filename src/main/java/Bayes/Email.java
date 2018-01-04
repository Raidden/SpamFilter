package Bayes;

public class Email {

    private boolean isSpam;
    private String name;
    private String Subject;

    public Email(boolean isSpam, String name, String subject) {
        this.isSpam = isSpam;
        this.name = name;
        Subject = subject;
    }

    public boolean isSpam() {
        return isSpam;
    }

    public void setSpam(boolean spam) {
        isSpam = spam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }
}
