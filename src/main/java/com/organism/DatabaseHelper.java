package com.organism;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class DatabaseHelper {
    private String dbPath;

    public DatabaseHelper(String dbPath) {
        this.dbPath = dbPath;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "SQLite JDBC driver not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    public boolean testConnection() {
        try (Connection conn = connect()) {
            return conn != null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to database:\n" + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    //sql command for adding organisms into the database from the GUI
    public boolean addOrganism(String id, String clade, String genus, int lifespan, String lifespanUnit,
                               String features, float avgLength, String lengthUnit) {
        String sql = "INSERT INTO Organisms (organism_id, clade, species, lifespan, lifespan_unit, features, average_length, length_unit) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"; //placeholder for the values about to be added

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            //here the values to be added are collected
            ps.setString(1, id);
            ps.setString(2, clade);
            ps.setString(3, genus);
            ps.setInt(4, lifespan);
            ps.setString(5, lifespanUnit);
            ps.setString(6, features);
            ps.setFloat(7, avgLength);
            ps.setString(8, lengthUnit);
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    //sql statement for deleting an organism
    public boolean deleteOrganism(String id) {
        String sql = "DELETE FROM Organisms WHERE organism_id = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting organism:\n" + e.getMessage());
            return false;
        }
    }

    //update organism method connection to database
    public boolean updateOrganism(String id, String column, String newValue) {
        String dbColumn;
        // switch case for attribute names to  database column names
        switch (column.toLowerCase()) {
            case "clade":
                dbColumn = "clade";
                break;
            case "genus":
                dbColumn = "species";
                break;
            case "lifespan":
                dbColumn = "lifespan";
                break;
            case "lifespan unit":
                dbColumn = "lifespan_unit";
                break;
            case "features":
                dbColumn = "features";
                break;
            case "average length":
                dbColumn = "average_length";
                break;
            case "lengthunit":
            case "length unit":
                dbColumn = "length_unit";
                break;
            default:
                JOptionPane.showMessageDialog(null, "Unknown column: " + column);
                return false;
        }

        // Make sure numeric values are positive and numbers
        if (dbColumn.equals("lifespan")) {
            try {
                int val = Integer.parseInt(newValue);
                if (val <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Lifespan must be a positive integer.");
                return false;
            }
        }

        //ensure average length is also positive and a float
        if (dbColumn.equals("average_length")) {
            try {
                float val = Float.parseFloat(newValue);
                if (val <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Average length must be a positive number.");
                return false;
            }
        }
        // validate length unit to either m, mm, or cm
        if (dbColumn.equals("length_unit") && !(newValue.equals("mm") || newValue.equals("cm") || newValue.equals("m"))) {
            JOptionPane.showMessageDialog(null, "Length unit must be mm, cm, or m.");
            return false;
        }

        //sql statement to update the organism
        String sql = "UPDATE Organisms SET " + dbColumn + " = ? WHERE organism_id = ?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // make sure the numbers are set to int and float
            if (dbColumn.equals("lifespan")) {
                ps.setInt(1, Integer.parseInt(newValue));
            } else if (dbColumn.equals("average_length")) {
                ps.setFloat(1, Float.parseFloat(newValue));
            } else {
                ps.setString(1, newValue);
            }
            ps.setString(2, id); //sets second placeholder in sql statement to id

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating organism:\n" + e.getMessage());
            return false;
        }
    }

    public String getAverageLengthByCladeString() {
        StringBuilder output = new StringBuilder();
        //sql statement for finding the averages and returning them
        String sql = "SELECT clade, AVG(average_length) AS avg_length FROM Organisms GROUP BY clade";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String clade = rs.getString("clade");
                float avg = rs.getFloat("avg_length");
                output.append(String.format("Clade: %s | Average Length: %.2f cm%n", clade, avg));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error calculating averages:\n" + e.getMessage());
        }

        return output.toString();
    }

    public DefaultTableModel getAllOrganisms() {
        String[] columns = {"ID", "Clade", "Genus & Species", "Lifespan", "Lifespan Unit", "Features", "Average Length", "Length unit"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        String sql = "SELECT * FROM Organisms";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rset = stmt.executeQuery(sql)) {

            while (rset.next()) {
               //getting the columns for displaying the table
                Object[] row = {
                        rset.getString("organism_id"),
                        rset.getString("clade"),
                        rset.getString("species"),
                        rset.getInt("lifespan"),
                        rset.getString("lifespan_unit"),
                        rset.getString("features"),
                        rset.getFloat("average_length"),
                        rset.getString("length_unit")
                };
                model.addRow(row); //adding rows until all data is shown
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error returning organisms:\n");
        }
        return model;
    }

}
