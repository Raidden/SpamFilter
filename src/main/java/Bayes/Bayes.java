package Bayes;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Bayes {
    HashMap<String, Word> words = new HashMap<String, Word>();
    private BufferedWriter out;
    private int countSpam = 0;
    private int countHam = 0;

    private static String getSubject(File testFile) throws IOException {
        String subject = "";
        BufferedReader in = new BufferedReader(new FileReader(testFile));
        String line = in.readLine();
        while (line != null) {
            if (line.startsWith("Subject:")) {
                int index = line.indexOf(":");
                subject = line.substring(index + 1, line.length()).trim();
                subject = subject.replaceAll("\\s{2,}", " ");
            }
            line = in.readLine();
        }
        in.close();
        return subject;
    }

    //uses a train-file to make a hashmap containing all words, and their probability of being spam
    public void train(String trainHam, String trainSpam) throws IOException {
        int totalSpamCount = 0;
        int totalHamCount = 0;
        File spam = new File(trainSpam);
        File ham = new File(trainHam);
        HashMap<String, List<File>> emailKorpus = new HashMap<String, List<File>>();
        emailKorpus.put("spam", Arrays.asList(spam.listFiles()));
        emailKorpus.put("ham", Arrays.asList(ham.listFiles()));
        for (final String trainFolder : emailKorpus.keySet()) {
            for (final File fileEntry : emailKorpus.get(trainFolder)) {
                BufferedReader in = new BufferedReader(new FileReader(fileEntry));
                String line = in.readLine();
                while (line != null) {
                    if (!line.equals("")) {
                        if (line.startsWith("Subject:")) {
                            int index = line.indexOf(":");
                            String type = trainFolder;
                            String subject = line.substring(index + 1, line.length()).trim();
                            subject = subject.replaceAll("\\s{2,}", " ");
                            for (String word : subject.split(" ")) {
                                word = word.replaceAll("\\W", "");
                                word = word.toLowerCase();
                                Word w;
                                if (words.containsKey(word)) {
                                    w = words.get(word);
                                } else {
                                    w = new Word(word);
                                    words.put(word, w);
                                }
                                if (type.equals("ham")) {
                                    w.countHam();
                                    totalHamCount++;
                                } else if (type.equals("spam")) {
                                    w.countSpam();
                                    totalSpamCount++;
                                }
                            }
                        }
                    }
                    line = in.readLine();
                }
                in.close();
            }
        }
        for (String key : words.keySet()) {
            words.get(key).calculateProbability(totalSpamCount, totalHamCount);
        }
        System.out.println("Training Done.");
    }

    //Takes the text to be analyzes as input, and produces predictions by form of 'spam' or 'ham'
    public void filter(String emailDir, String result_file) throws IOException {
        String subject;
        this.out = new BufferedWriter(new FileWriter(result_file));
        File dir = new File(emailDir);
        for (final File email : dir.listFiles()) {
            subject = getSubject(email);
            ArrayList<Word> emailWordList = makeWordList(subject);
            boolean isSpam = calculateBayes(emailWordList);
            if (isSpam) {
                this.out.write("spam || " + "Email: " + email.getName() + " = Subject: " + subject);
                this.countSpam++;
            } else if (!isSpam) {
                this.countHam++;
                this.out.write("ham || " + "Email: " + email.getName() + " = Subject: " + subject);
            }
            this.out.newLine();
        }
        this.out.newLine();
        this.out.write("Accuracy = " + accuracy());
        this.out.close();
    }

    //make an arraylist of all words in an Email, set probability of spam to 0.4 if word is not known
    private ArrayList<Word> makeWordList(String email) {
        ArrayList<Word> wordList = new ArrayList<Word>();
        for (String word : email.split(" ")) {
            word = word.replaceAll("\\W", "");
            word = word.toLowerCase();
            Word w;
            if (words.containsKey(word)) {
                w = words.get(word);
            } else {
                w = new Word(word);
                w.setProbOfSpam(0.40f);
            }
            wordList.add(w);
        }
        return wordList;
    }

    private boolean isSpam(float probOfSpam) {
        return probOfSpam > 0.9f;
    }

    //Applying Bayes rule and calculating probability of ham or spam. Return true if spam, false if ham
    private boolean calculateBayes(ArrayList<Word> email) {
        float probabilityOfPositiveProduct = 1.0f;
        float probabilityOfNegativeProduct = 1.0f;
        for (int i = 0; i < email.size(); i++) {
            Word word = email.get(i);
            probabilityOfPositiveProduct *= word.getProbOfSpam();
            probabilityOfNegativeProduct *= (1.0f - word.getProbOfSpam());
        }
        float probOfSpam = probabilityOfPositiveProduct / (probabilityOfPositiveProduct + probabilityOfNegativeProduct);
        return isSpam(probOfSpam);
    }

    public void setWords(HashMap<String, Word> words) {
        this.words = words;
    }

    public float accuracy() {
        float ham = this.countHam;
        float spam = this.countSpam;
        return spam / (ham + spam);
    }

}

