package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class App
{
    /**
     * A class for retrieving premade SQL statements you can also enter a string to add a limit to some of the sql Statements
     *
     * @param number An integer that should correspond to the statement you want
     * @return Returns a string with your desired sql statement
     */
    String getStatements(int number) {return getStatements(number, "10000");}

    /**
     * A class for retrieving premade SQL statements you can also enter a string to add a limit(Default 10000) to some of the sql Statements
     *
     * @param number An integer that should correspond to the statement you want
     * @param input it's a string input containing an integer because I couldn't be bothered to convert it
     * @return Returns a string with your desired sql statement + your limit if the statement accepts limits
     * @author Fergus Reid 40534638@live.napier.ac.uk
     */
    String getStatements(int number, String input) {
        String[] Statements = {
                //If you want to compare these to our readme file just remember to minus 1 from the number in the readme file
                //Statements 0-5
                "SELECT Code, Name, Continent, Region, Population, Capital FROM country ORDER BY Population DESC",
                "SELECT Code, Name, Continent, Region, Population, Capital FROM country WHERE Continent = 'Europe' ORDER BY Population DESC",
                "SELECT Code, Name, Continent, Region, Population, Capital FROM country WHERE Region = 'Western Europe' ORDER BY Population DESC",
                "SELECT Code, Name, Continent, Region, Population, Capital FROM country ORDER BY Population DESC LIMIT " + input, //Adds a limit to the sql statement (Default 10000)
                "SELECT Code, Name, Continent, Region, Population, Capital FROM country WHERE Continent = 'Europe' ORDER BY Population DESC LIMIT " + input,
                "SELECT Code, Name, Continent, Region, Population, Capital FROM country WHERE Region = 'Western Europe' ORDER BY Population DESC LIMIT " + input,
                //Statements 6-10
                "SELECT Name, CountryCode, District, Population FROM city ORDER BY Population DESC",
                "SELECT city.Name, city.CountryCode, city.District, city.Population, country.Continent FROM city INNER JOIN country ON city.CountryCode = country.Code WHERE country.Continent = 'Europe' ORDER BY city.Population DESC",
                "SELECT city.Name, city.CountryCode, city.District, city.Population, country.Region FROM city INNER JOIN country ON city.CountryCode = country.Code WHERE country.Region = 'Western Europe' ORDER BY city.Population DESC",
                "SELECT Name, CountryCode, District, Population FROM city WHERE CountryCode = 'GBR' ORDER BY Population DESC",
                "SELECT Name, CountryCode, District, Population FROM city WHERE District = 'England' ORDER BY Population DESC",
                //Statements 11-15
                "SELECT Name, CountryCode, District, Population FROM city ORDER BY Population DESC LIMIT " + input,
                "SELECT city.Name, city.CountryCode, city.District, city.Population, country.Continent FROM city INNER JOIN country ON city.CountryCode = country.Code WHERE country.Continent = 'Europe' ORDER BY city.Population DESC LIMIT " + input,
                "SELECT city.Name, city.CountryCode, city.District, city.Population, country.Region FROM city INNER JOIN country ON city.CountryCode = country.Code WHERE country.Region = 'Western Europe' ORDER BY city.Population DESC LIMIT " + input,
                "SELECT Name, CountryCode, District, Population FROM city WHERE CountryCode = 'GBR' ORDER BY Population DESC LIMIT " + input,
                "SELECT Name, CountryCode, District, Population FROM city WHERE District = 'England' ORDER BY Population DESC LIMIT " + input,
                //Statements 16-21
                "SELECT city.Name, country.Name, city.Population FROM city INNER JOIN country ON city.ID = country.Capital ORDER BY city.Population DESC",
                "SELECT city.Name, country.Name, city.Population, country.Continent FROM city INNER JOIN country ON city.ID = country.Capital WHERE country.Continent = 'Europe' ORDER BY city.Population DESC",
                "SELECT city.Name, country.Name, city.Population, country.Region FROM city INNER JOIN country ON city.ID = country.Capital WHERE country.Region = 'Western Europe' ORDER BY city.Population DESC",
                "SELECT city.Name, country.Name, city.Population FROM city INNER JOIN country ON city.ID = country.Capital ORDER BY city.Population DESC LIMIT " + input,
                "SELECT city.Name, country.Name, city.Population, country.Continent FROM city INNER JOIN country ON city.ID = country.Capital WHERE country.Continent = 'Europe' ORDER BY city.Population DESC LIMIT " + input,
                "SELECT city.Name, country.Name, city.Population, country.Region FROM city INNER JOIN country ON city.ID = country.Capital WHERE country.Region = 'Western Europe' ORDER BY city.Population DESC LIMIT " + input,
                //Statements 22-24
                "SELECT Continent, SUM(Population) AS 'Total Continent Population', SUM(CityPopulation) AS 'Total City Population', SUM(Population)-SUM(CityPopulation) AS 'Total Rural Population', CONCAT(ROUND(SUM(CityPopulation)/SUM(Population)*100, 2), '%') AS 'City %', CONCAT(ROUND(SUM(Population-CityPopulation)/SUM(Population)*100, 2), '%') AS 'Rural %' FROM (SELECT country.Continent, country.Population, SUM(city.Population) AS CityPopulation FROM city INNER JOIN country ON city.CountryCode = country.Code GROUP BY country.Code) AS CountryPopulations GROUP BY Continent ORDER BY SUM(Population)",
                "SELECT Region, SUM(Population) AS 'Total Region Population', SUM(CityPopulation) AS 'Total City Population', SUM(Population)-SUM(CityPopulation) AS 'Total Rural Population', CONCAT(ROUND(SUM(CityPopulation)/SUM(Population)*100, 2), '%') AS 'City %', CONCAT(ROUND(SUM(Population-CityPopulation)/SUM(Population)*100, 2), '%') AS 'Rural %' FROM (SELECT country.Region, country.Population, SUM(city.Population) AS CityPopulation FROM city INNER JOIN country ON city.CountryCode = country.Code GROUP BY country.Code) AS CountryPopulations GROUP BY Region ORDER BY SUM(Population)",
                "SELECT country.Name, country.Population, SUM(city.Population) AS 'Total City Population', country.Population - SUM(city.Population) AS 'Total Rural Population', CONCAT(ROUND(SUM(city.Population)/country.Population*100, 2), '%') AS 'City %', CONCAT(ROUND((country.Population - SUM(city.Population))/country.Population*100, 2), '%') AS 'Rural %' FROM city INNER JOIN country ON city.CountryCode = country.Code GROUP BY country.Code",
                //Statements 25-25
                "SELECT SUM(Population) AS 'World Population', SUM(`Total City Population`) AS 'Total City Population', SUM(Population-`Total City Population`) AS 'Total Rural Population', CONCAT(ROUND(SUM(`Total City Population`)/SUM(Population)*100, 2), '%') AS 'City %', CONCAT(ROUND(SUM(Population-`Total City Population`)/SUM(Population)*100, 2), '%') AS 'Rural %' FROM(SELECT country.Name, country.Population, SUM(city.Population) AS 'Total City Population', country.Population - SUM(city.Population) AS 'Total Rural Population' FROM city INNER JOIN country ON city.CountryCode = country.Code GROUP BY country.Code) AS countryPop"
        };

        //Returns the desired sql statement as a string
        return Statements[number];
    }

    /**
     * This is the main method, it
     *
     * @param args does nothing
     * @author Fergus Reid 40534638@live.napier.ac.uk
     */
    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        //Prints out the desired sql results to the console
        for (int i = 22; i <= 24; i++) {
            //getSql runs the SQL statements and prints it to the console
            //getStatements retrieves the desired statement and optionally adds a limit to some of the results
            a.getSql(a.getStatements(i, "3"));
        }

        // Disconnect from database
        a.disconnect();
    }

    /**
     * Prints the results from the inputted sql statement
     * It's a modified version of Kevins Employee code
     *
     * @param sqlStatement an SQL statement represented as a string,
     *                     made to be used with getStatements() but can accept any valid sql statement
     *
     * @author Fergus Reid 40534638@live.napier.ac.uk
     * @author Kevin Sim K.Sim@nampier.ac.uk
     */
    public void getSql(String sqlStatement)
    {
        try
        {
            Statement stmt = con.createStatement();
            ResultSet rset = stmt.executeQuery(sqlStatement);

            //Gets the metadata from the sql results so that i can easily print it to the console
            ResultSetMetaData rsetmd = rset.getMetaData();

            //Gets the number of columns so i can get the column names
            int columnNumber = rsetmd.getColumnCount();

            //Loops through all the column names and prints them to the console
            for (int i = 1; i <= columnNumber; i++)
            {
                System.out.print(rsetmd.getColumnName(i) + " | ");

            }
            System.out.println(); //Adds a new line so that the column names are above the first row

            //Loops whilst there are still rows to print
            while (rset.next())
            {
                String output = "";

                //Loops through each column for this row
                for (int i = 1; i <= columnNumber; i++)
                {
                    String columnName = rsetmd.getColumnName(i); //Finds the column name fo this column
                    output = output + rset.getString(columnName) + "\t"; //Adds the results from the column in this row to a string with a tab between each result
                }
                //Outputs the row to the console
                System.out.println(output);
            }
            //Prints a few dashes to the console, meant to make it easier to find when the results of a stements has ended
            //very useful when printing multiple statements at once
            System.out.println("-----");
        }

        //If there is an error running the sql code it will print the sql error message and then prints my error message
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country/city details details");
        }
    }

    /*
    After this point its just Kevins code, I may have made a few changes but they are inconsequential and i cant remember what they were

     */



    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect()
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for db to start
                Thread.sleep(5000);//Changed to 5 seconds because 30 was too long
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }
}
