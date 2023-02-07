package newsanalizer.usermanagement;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login {

    public void doLogin() {

        List<User> listUsers = loadDB();
        boolean succes = false;
        int contor = 0;
        do {
            System.out.println("username:");
            String kbUsername = new Scanner(System.in).nextLine();
            System.out.println("password:");
            String kbPwd = new Scanner(System.in).nextLine();
            User userKb = new User();
            String passdecripted = passCript(kbPwd);
            userKb.setUsername(kbUsername);
            userKb.setPassword(passdecripted);
            contor = contor + 1;

            for (User u : listUsers) {

                if (u.equalsUsers(userKb)) {
                    succes = true;
                    contor = 0;
                    if (!u.isAdmin()) System.out.println("ok, you are logged in now as a guest");
                    addUserbyAdmin(u);
                }
            }
            if (contor == 5) {
                Calendar date = Calendar.getInstance();
                long timeInSecs = date.getTimeInMillis();
                Date afterAdding1Mins = new Date(timeInSecs + (60 * 1000));
                System.out.println("Ai depasit numarul de incercari. Timeout pana la " + afterAdding1Mins);
                while (!afterAdding1Mins.equals(new Date())) {
                    succes = true;
                }
                succes = false;
                contor = 0;
            }
        }
        while (!succes);

    }

    public static String passCript(String s) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            char a = s.charAt(i);
            int b = s.charAt(i);
            for (int j = 0; j < alphabet.length() - 4; j++) {
                if (alphabet.charAt(j) == a) {
                    a = alphabet.charAt(j + 4);
                    break;
                }
            }
            str += a;
        }
        return str;
    }

    private boolean checkUserValid(String usernameAddedByAdmin) {
        boolean checkUserValid;
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(usernameAddedByAdmin);
        if (!m.matches()) {
            System.out.println("Username-ul nu a fost adaugat deoarece nu este in formatul dorit, incearca din nou.");
            checkUserValid = false;
        } else checkUserValid = true;
        return checkUserValid;
    }

    private boolean checPassValid(String usernameAddedByAdmin) {
        boolean checkPassValid;
        String regex = "^(?=.*[A-Za-z])[A-Za-z\\d]{8,}$"; //Minimum eight characters, at least one letter
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(usernameAddedByAdmin);
        if (!m.matches()) {
            System.out.println("Parola nu este in formatul dorit, incearca din nou(Minim 8 caractere, o litera mare).");
            checkPassValid = false;
        } else checkPassValid = true;
        return checkPassValid;
    }

    private void addUserbyAdmin(User user) {
        User us = new User();
        boolean status = false;
        if (user.isAdmin()) {
            System.out.println("Salut " + user.getUsername() + ", esti logat ca si ADMIN");
            while (!status) {
                showMenu();
                int option = new Scanner(System.in).nextInt();
                switch (option) {
                    case 1:
                        System.out.println("adauga pe fisier noul username(format email address):");
                        String usernameAddedByAdmin = new Scanner(System.in).nextLine();
                        if (checkUserValid(usernameAddedByAdmin)) {
                            System.out.println("adauga parola pentru user-ul creeat(Minim 8 caractere si o litera mare):");
                            String passAddedByAdmin = new Scanner(System.in).nextLine();
                            if (checPassValid(passAddedByAdmin)) {
                                System.out.println("adauga rolul pentru user-ul creeat(Utilizator simplu(false),Admin(true):");
                                String roleAddedByAdmin = new Scanner(System.in).nextLine();
                                System.out.println("Poate user-ul sa adauge stiri?(True/False):");
                                String userisAnalyst = new Scanner(System.in).nextLine();
                                us.setUsername(usernameAddedByAdmin);
                                us.setPassword(passCript(passAddedByAdmin));
                                us.setAdmin(Boolean.parseBoolean(roleAddedByAdmin));
                                us.setAnalist(Boolean.parseBoolean(userisAnalyst));
                                writeOnDisk(us);
                            }
                        }
                        break;

                    case 2:
                        status = true;
                        //break;
                    default:
                        break;
                }
            }
        }
    }

    private void showMenu() {
        System.out.println("""
                \n Alege din meniul de mai jos operatia dorita:
                    -Pentru adaugare useri noi apasa 1
                    -Pentru iesire din program apasa 2""");
    }

    private void writeOnDisk(User user) {
        try (FileWriter fw = new FileWriter("users.txt", true)) {
            if (checkUserValid(user.getUsername())&& checPassValid(user.getPassword())) {
                fw.write( user.addToFile());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private List<User> loadDB() {

        Path path = Paths.get("users.txt");
        List<User> listOfUsers = new ArrayList<>();
        try {
            List<String> listOfUsersAsStrings = Files.readAllLines(path);
            //System.out.println(listOfUsersAsStrings);

            for (int i = 0; i < listOfUsersAsStrings.size(); i++) {
                User uObj = new User();
                String currentLineOfText = listOfUsersAsStrings.get(i);
                StringTokenizer st = new StringTokenizer(currentLineOfText, ",");
                while (st.hasMoreTokens()) {
                    String user = st.nextToken();
                    String pass = st.nextToken();
                    String admin = st.nextToken();
                    String news = st.nextToken();
                    uObj.setUsername(user.trim());
                    uObj.setPassword(pass.trim());
                    uObj.setAdmin(admin.trim().equalsIgnoreCase("true"));
                    uObj.setAnalist(news.trim().equalsIgnoreCase("true"));
                }
                listOfUsers.add(uObj);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
        }

        return listOfUsers;
    }

}
