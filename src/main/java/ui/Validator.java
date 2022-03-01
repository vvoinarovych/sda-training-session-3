package ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private boolean validate(String regex, String checking) {
        Pattern patter = Pattern.compile(regex);
        Matcher matcher = patter.matcher(checking);
        return !matcher.find();
    }


    public boolean validateLongitude(String checking) {
        //String pattern = "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$";
        String pattern = "[0-9]";
        return validate(pattern,checking);
    }

    public boolean validateLatitude(String checking) {
      //String pattern = "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$)";
        String pattern = "[0-9]";
        return validate(pattern, checking);
    }

    public void informationMessage(){
        System.out.println("NO parameter added");
    }

    public boolean countryNameValidation(String checking) {
        String pattern = "^[a-zA-Z]";
        return validate(pattern, checking);

    }

    public boolean cityNameValidation(String checking) {
        String pattern = "(^[a-zA-Z])";
        return validate(pattern, checking);
    }

    public boolean regionNameValidation(String checking) {
        String pattern = "(^[a-zA-Z])";
        return validate(pattern, checking);
    }

}


