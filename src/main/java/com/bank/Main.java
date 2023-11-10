package com.bank;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.*;

public class Main {
    String name;
    String dob;
    String email;
    String password;

    Main() {}
    Main(String name, String dob, String email, String password) {
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.password = password;
    }
    public void insertData() {
        try {
            // #1 Database Details
            String connectionString = "jdbc:mysql://localhost/bank";
            String user = "root";
            String dbpassword = "rootpassword";

            // #2 Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // #3 Open Connection
            Connection conn = DriverManager.getConnection(connectionString, user, dbpassword);
            // #4 Create Statement
            Statement stat = conn.createStatement();

            String query = "insert into Customers(name, dob, email, password) values ('" +
                    this.name + "', '" + this.dob + "', '" + this.email + "', '" + this.password + "')";

            int i = stat.executeUpdate(query);
            if (i > 0) {
                System.out.println("Successfull");
            } else {
                System.out.println("Failed");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        User user = new User();
        int option;
        do {
            System.out.println("\n\n\t\t\t\t\t\t====== Online Banking ======");
            System.out.print("\n\t\t\t\t1. Register\n\t\t\t\t2. Login\n\t\t\t\t3. Exit\n\t\t\t\tSelect Option: ");
            option = sc.nextInt();
            switch (option) {
                case 1:
                    System.out.print("\t\t\t\tEnter Your Name: ");
                    sc.nextLine();
                    String name = sc.nextLine();
                    System.out.print("\t\t\t\tEnter Your DOB: ");
                    String dob = sc.next();
                    System.out.print("\t\t\t\tEnter Your Email: ");
                    String email = sc.next();
                    while (user.emailExist(email)) {
                        System.out.print("\t\t\t\tEmail Already Exist Enter Another: (To exit 0): ");
                        email = sc.next();
                        if (email.equals("0")) {
                            break;
                        }
                    }
                    if (email.equals("0")) {
                        break;
                    }
                    System.out.print("\t\t\t\tEnter Your Password: ");
                    String password = sc.next();
                    user.setName(name);
                    user.setDob(dob);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.insertData();
                    break;
                case 2:
                    System.out.print("\t\t\t\tEnter Your Email: ");
                    email = sc.next();
                    while (!user.emailExist(email)) {
                        System.out.print("\t\t\t\tEmail Does not Exist Enter Another: (To exit 0): ");
                        email = sc.next();
                        if (email.equals("0")) {
                            break;
                        }
                    }
                    if (email.equals("0")) {
                        break;
                    }
                    System.out.print("\t\t\t\tEnter Your Password: ");
                    password = sc.next();

                    while (!user.login(email, password)) {
                        System.out.print("\t\t\t\tPassword Does not match: (To exit 0): ");
                        password = sc.next();
                        if (password.equals("0")) {
                            break;
                        }
                    }
                    if (password.equals("0")) {
                        break;
                    }

                    System.out.println("\n\t\t\t\t ==== Hi, " + user.getName() + "  A/c: " + user.getId() + " ====");
                    do {
                        System.out.print("\n\t\t\t\t1. Deposite Money\n\t\t\t\t2. Withdraw Money\n\t\t\t\t3. Show Money\n\t\t\t\t4. Transfer Money\n\t\t\t\t5. Show Last 10 Transactions\n\t\t\t\t6. Logout\n\t\t\t\tSelect Option: ");
                        option = sc.nextInt();
                        double amt;
                        switch (option) {
                            case 1:
                                System.out.print("\n\t\t\t\tEnter Amount to Deposit: ");
                                amt = sc.nextDouble();
                                user.deposit(user.getId(), amt);
                                break;
                            case 2:
                                System.out.print("\n\t\t\t\tEnter Amount to Withdraw: ");
                                amt = sc.nextDouble();
                                user.withdraw(user.getId(), amt);
                                break;
                            case 3:
                                System.out.printf("\n\t\t\t\tYour Account Balance is %.2f\n", user.getBalance());
                                break;
                            case 4:
                                System.out.print("\n\t\t\t\tEnter 6 digit A/c Number: ");
                                int ac = sc.nextInt();
                                while (!user.accountExist(ac)) {
                                    System.out.print("\t\t\t\tThis A/c No. does not exist Try Another(0 to exit): ");
                                    ac = sc.nextInt();
                                    if (ac == 0) {
                                        break;
                                    }
                                }
                                if (ac == 0) {
                                    break;
                                }
                                System.out.print("\n\t\t\t\tEnter Amount to Transfer: ");
                                amt = sc.nextDouble();
                                user.transfer(ac, amt);
                                break;
                            case 5:
                                user.last10Transaction();
                                break;
                        }
                    } while (option != 6);
                    break;
            }
        } while (option != 3);

//        System.out.print("Enter Name: ");
//        String name = scan.nextLine();
//        System.out.print("Enter DOB: ");
//        String dob = scan.next();
//        System.out.print("Enter Email: ");
//        String email = scan.next();
//        System.out.print("Enter Password: ");
//        String password = scan.next();

//        User user = new User(name, dob, email, password);
//        Main obj = new Main(name, dob, email, password);
//        User user = new User();
////        user.getRecord();
//        user.emailExist("owais1@gmail.com");
    }
}
