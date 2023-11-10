package com.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class User {
    private DBConnection dbConnection = new DBConnection();
    private Connection conn = dbConnection.connection();

    private int id;
    private String name;
    private String dob;
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String password;

    User() {}
    User(String name, String dob, String email, String password) {
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void insertData() {
        try {
            Statement stat = this.conn.createStatement();

            String query = "insert into Customers(id, name, dob, email, password) values ('" + getRandom() + "','" +
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

    public boolean emailExist(String email) {
        try {
            Statement stat = this.conn.createStatement();
            String query = "select * from Customers where email = " + "'"+email+"'";
            ResultSet resultSet = stat.executeQuery(query);

            return (resultSet.isBeforeFirst() && resultSet.getRow() == 0);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean login(String email, String password) {
        try {
            Statement stat = this.conn.createStatement();

            String query = "select * from Customers where email = " + "'" + email + "'";

            ResultSet resultSet = stat.executeQuery(query);

            if (resultSet.next()) {
                if (resultSet.getString("email").equals(email) && resultSet.getString("password").equals(password)) {
                    setName(resultSet.getString("name"));
                    setEmail(resultSet.getString("email"));
                    setId(resultSet.getInt("id"));
                    return true;
                }

                return false;
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return false;
    }

    public boolean deposit(int ac, double amt) {
        try {
            Statement stat = this.conn.createStatement();
            String query = "select * from Accounts where cid = " + "'"+ ac +"'";
            ResultSet resultSet = stat.executeQuery(query);

            if (!resultSet.isBeforeFirst() && resultSet.getRow() == 0) {

                String sql = "insert into Accounts(amt, cid) values ('" + amt + "','" + ac + "')";
                String txn = "insert into Transactions(amt, cid, cr_dr) values('" + amt + "','" + ac + "', 'Cr')";
                stat.executeUpdate(txn);
                int i = stat.executeUpdate(sql);
                if (i > 0) {
                    System.out.println("\t\t\t\tSuccessfull");
                    return true;
                } else {
                    System.out.println("\t\t\t\tFailed");
                    return false;
                }
            } else {
                double total_amt = 0;
                if (resultSet.next()) {
                    total_amt = resultSet.getDouble("amt") + amt;
                }
                String sql = "update Accounts set amt = '" + total_amt + "' where cid = '" + ac + "'";
                String txn = "insert into Transactions(amt, cid, cr_dr) values('" + amt + "','" + ac + "', 'Cr')";
                stat.executeUpdate(txn);
                int i = stat.executeUpdate(sql);
                if (i > 0) {
                    System.out.println("\t\t\t\tSuccessfull");
                    return true;
                } else {
                    System.out.println("\t\t\t\tFailed");
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public boolean withdraw(int ac, double amt) {
        try {
            Statement stat = this.conn.createStatement();
            String query = "select * from Accounts where cid = " + "'"+ ac +"'";
            ResultSet resultSet = stat.executeQuery(query);

            if (!resultSet.isBeforeFirst() && resultSet.getRow() == 0) {
                System.out.println("\n\t\t\t\tInsufficient Balance\n");
                return false;
            } else {
                double total_amt = 0;
                if (resultSet.next()) {
                    if (resultSet.getDouble("amt") < amt) {
                        System.out.println("\n\t\t\t\tInsufficient Balance\n");
                        return false;
                    }
                    total_amt = resultSet.getDouble("amt") - amt;
                }
                String sql = "update Accounts set amt = '" + total_amt + "' where cid = '" + ac + "'";
                String txn = "insert into Transactions(amt, cid, cr_dr) values('" + amt + "','" + ac + "', 'Dr')";
                stat.executeUpdate(txn);
                int i = stat.executeUpdate(sql);
                if (i > 0) {
                    System.out.println("\t\t\t\tSuccessfull");
                    return true;
                } else {
                    System.out.println("\t\t\t\tFailed");
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public double getBalance() {
        try {
            Statement stat = this.conn.createStatement();
            String query = "select * from Accounts where cid = " + "'"+ this.id +"'";
            ResultSet resultSet = stat.executeQuery(query);

            if (!resultSet.isBeforeFirst() && resultSet.getRow() == 0) {
                return 0;
            } else {
                if (resultSet.next()) {
                    return resultSet.getDouble("amt");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public void last10Transaction() {
        try {
            Statement stat = this.conn.createStatement();
            String query = "select * from Transactions where cid = " + "'" + this.id + "' order by time_stamp desc";
            ResultSet resultSet = stat.executeQuery(query);
            System.out.println("\n\t\t\t\t\tLast 10 Transactions");
            System.out.println("\t\t\t\tID\t\tBalance\t\tCr/Dr\t\tTxn Time");
            int i = 0;
            while (resultSet.next()) {
                i++;
                System.out.println("\t\t\t\t" + i + "\t\t" + resultSet.getDouble("amt") + "\t\t" +
                        resultSet.getString("cr_dr") + "\t\t\t" + resultSet.getDate("time_stamp") + " " +
                        resultSet.getTime("time_stamp"));

                if (i == 10) {
                    break;
                }
            }
            System.out.println();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean accountExist(int ac) {
        try {
            Statement stat = this.conn.createStatement();
            String query = "select * from Customers where id = " + "'"+ac+"'";
            ResultSet resultSet = stat.executeQuery(query);

            if (!resultSet.isBeforeFirst() && resultSet.getRow() == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public void transfer(int ac, double amt) {
        boolean is_true = withdraw(this.id, amt);
        if (is_true) {
            deposit(ac, amt);
        }
    }

    public int getRandom() {
        Random rnd = new Random();
        return 100000 + rnd.nextInt(900000);
    }
}