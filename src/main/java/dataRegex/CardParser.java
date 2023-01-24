package dataRegex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardParser {
    public static void main(String[] args) {
        CardParser cardParser = new CardParser();
        List<String> list = cardParser.fileParser("input_business_card.txt");
        List<BusCardContact> listContact = new ArrayList<>();
        for (String contact : list) {
            listContact.add(cardParser.parseContact(contact));
            System.out.println(contact);
        }
        for (BusCardContact contact : listContact) {
            System.out.println(contact);
        }
    }

    static final String begin = "BEGIN:VCARD";
    static final String end = "END:VCARD";

    public List<String> fileParser(String filePath) {
        List<String> stringArr = new ArrayList<>();
        try (BufferedReader bf = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean start = false;
            StringBuilder strBl = new StringBuilder();
            while ((line = bf.readLine()) != null) {
                if (line.contains(end)) {
                    start = false;
                    stringArr.add(strBl.toString());
                    strBl = new StringBuilder();
                }
                if (start) strBl.append(line).append("\n");
                if (line.contains(begin)) {
                    start = true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringArr;
    }

    private Type getType(String str) {
        return Type.valueOf(str.substring(0, str.length() - 1));
    }

    public BusCardContact parseContact(String contact) {
        BusCardContact cardContact = new BusCardContact();
        Phone phone = new Phone();
        Email email = new Email();
        Address address = new Address();
        Web web = new Web();

        String regex1 = "^(FN|TEL|EMAIL|ADDRESS|WEB)[;:](TYPE=.*:|)(.+)$";
        String regex = "^(FN|TEL|EMAIL|ADDRESS|WEB)[;:](TYPE=|)(.*,|)(.*:|)(.+)$";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(contact);

        while (matcher.find()) {
            String group1 = matcher.group(1);
            switch (group1) {
                case "FN" -> {
                    cardContact.setFulName(matcher.group(5));
                }
                case "TEL" -> {
                    phone.setType(getType(matcher.group(3)));
                    phone.setVoice(matcher.group(5));
                    cardContact.setPhone(phone);
                }
                case "EMAIL" -> {
                    email.setType(getType(matcher.group(3)));
                    email.setInternet(matcher.group(5));
                    cardContact.setEmail(email);
                }
                case "ADDRESS" -> {
                    if (address.getType() == null)
                        address.setType(getType(matcher.group(3)));
                    if (address.getType().equals(getType(matcher.group(3)))) {
                        if (matcher.group(4).endsWith("STREET:")) address.setStreet(matcher.group(5));
                        if (matcher.group(4).endsWith("CITY:")) address.setCity(matcher.group(5));
                        if (matcher.group(4).endsWith("COUNTRY:")) address.setCountry(matcher.group(5));
                    }
                    cardContact.setAddress(address);
                }
                case "WEB" -> {
                    web.setType(getType(matcher.group(3)));
                    web.setInternet(matcher.group(5));
                    cardContact.setWeb(web);
                }
            }
        }
        return cardContact;
    }
}
