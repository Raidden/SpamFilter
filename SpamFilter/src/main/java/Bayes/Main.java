package Bayes;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Bayes run = new Bayes();
        Model model = new Model();
        try {
            if (args[0].equals("train") && args.length == 4) {
                run.train(args[2], args[3]);
                model.saveModel(run.words, args[1]);
            }
            if (args[0].equals("classify") && args.length == 4) {
                run.setWords(model.readModel(new File(args[1])));
                run.filter(args[2], args[3]);
            }
        } catch (IOException e) {
            e.fillInStackTrace();
            System.out.println("AN ERROR HAS OCCURED");
        }
    }
}



