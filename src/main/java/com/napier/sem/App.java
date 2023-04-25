package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class App
{
    String getStatements(int number) {return getStatements(number, "");}
    String getStatements(int number, String input) {
        String[] Statements = {
                //0-5
                "SELECT Name, Population FROM country ORDER BY Population DESC",
                "SELECT Name, Continent, Population FROM country WHERE Continent = 'Europe' ORDER BY Population DESC",
                "SELECT Name, Region, Population FROM country WHERE Region = 'British Islands' ORDER BY Population DESC",
                "SELECT Name, Population FROM country ORDER BY Population DESC LIMIT " + input,
                "SELECT Name, Continent, Population FROM country WHERE Continent = 'Europe' ORDER BY Population DESC LIMIT " + input,
                "SELECT Name, Region, Population FROM country WHERE Region = 'British Islands' ORDER BY Population DESC LIMIT " + input,
                //6-10
                "SELECT Name, Population FROM city ORDER BY Population DESC",
                "SELECT city.Name, country.Continent, city.Population FROM city INNER JOIN country ON city.CountryCode = country.Code WHERE country.Continent = 'Europe' ORDER BY city.Population DESC",
                "SELECT city.Name, country.Region, city.Population FROM city INNER JOIN country ON city.CountryCode = country.Code WHERE country.Region = 'British Islands' ORDER BY city.Population DESC",
                "SELECT Name, Population FROM city WHERE CountryCode = 'GBR' ORDER BY Population DESC",
                "SELECT Name, Population FROM city WHERE District = 'Scotland' ORDER BY Population DESC",
                //11-15
                "SELECT Name, Population FROM city ORDER BY Population DESC LIMIT " + input,
                "SELECT city.Name, country.Continent, city.Population FROM city INNER JOIN country ON city.CountryCode = country.Code WHERE country.Continent = 'Europe' ORDER BY city.Population DESC LIMIT " + input,
                "SELECT city.Name, country.Region, city.Population FROM city INNER JOIN country ON city.CountryCode = country.Code WHERE country.Region = 'British Islands' ORDER BY city.Population DESC LIMIT " + input,
                "SELECT Name, Population FROM city WHERE CountryCode = 'GBR' ORDER BY Population DESC LIMIT " + input,
                "SELECT Name, Population FROM city WHERE District = 'Scotland' ORDER BY Population DESC LIMIT " + input,
                //16-21
                "SELECT city.Name, city.Population FROM city INNER JOIN country ON city.ID = country.Capital ORDER BY city.Population DESC",
                "SELECT city.Name, country.Continent, city.Population FROM city INNER JOIN country ON city.ID = country.Capital WHERE country.Continent = 'Europe' ORDER BY city.Population DESC",
                "SELECT city.Name, country.Region, city.Population FROM city INNER JOIN country ON city.ID = country.Capital WHERE country.Region = 'British Islands' ORDER BY city.Population DESC",
                "SELECT city.Name, city.Population FROM city INNER JOIN country ON city.ID = country.Capital ORDER BY city.Population DESC LIMIT " + input,
                "SELECT city.Name, country.Continent, city.Population FROM city INNER JOIN country ON city.ID = country.Capital WHERE country.Continent = 'Europe' ORDER BY city.Population DESC LIMIT " + input,
                "SELECT city.Name, country.Region, city.Population FROM city INNER JOIN country ON city.ID = country.Capital WHERE country.Region = 'British Islands' ORDER BY city.Population DESC LIMIT " + input,
                //22-??
                "SELECT Continent, SUM(Population) AS 'Total Continent Population', SUM(CityPopulation) AS 'Total City Population', SUM(Population)-SUM(CityPopulation) AS 'Total Rural Population', CONCAT(ROUND(SUM(CityPopulation)/SUM(Population)*100, 2), '%') AS 'City %' FROM (SELECT country.Continent, country.Population, SUM(city.Population) AS CityPopulation FROM city INNER JOIN country ON city.CountryCode = country.Code GROUP BY country.Code) AS CountryPopulations GROUP BY Continent ORDER BY SUM(Population)"
        };

        return Statements[number];
    }

    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        for (int i = 22; i <= 22; i++) {
            a.getSql(a.getStatements(i, "3"));
        }

        // Disconnect from database
        a.disconnect();
    }

    public void getSql(String sqlStatement)
    {
        try
        {
            Statement stmt = con.createStatement();
            ResultSet rset = stmt.executeQuery(sqlStatement);

            ResultSetMetaData rsetmd = rset.getMetaData();
            int columnNumber = rsetmd.getColumnCount();

            for (int i = 1; i <= columnNumber; i++)
            {
                System.out.print(rsetmd.getColumnName(i) + " | ");

            }
            System.out.println();

            while (rset.next())
            {
                String output = "";
                for (int i = 1; i <= columnNumber; i++)
                {
                    String columnName = rsetmd.getColumnName(i);
                    output = output + rset.getString(columnName) + "\t";
                }
                System.out.println(output);
            }
            System.out.println("-----");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
        }
    }

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
                Thread.sleep(5000);
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


    public City getCity(String cName)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT ID, Name, CountryCode, District, Population "
                            + "FROM city "
                            + "WHERE Name = '" + cName + "'";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next())
            {
                return parseCity(rset);
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public ArrayList<City> getCities()
    {
        return getCities(
                "SELECT ID, Name, CountryCode, District, Population ",
                "FROM city",
                "",
                "" );
    }

    public ArrayList<City> getCities(String selectStatement, String fromStatement, String whereStatement, String orderStatement)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = selectStatement + fromStatement + whereStatement + orderStatement;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            ArrayList<City> cities = new ArrayList<>();

            while (rset.next())
            {

                City city = parseCity(rset);
                cities.add(city);
            }

            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    private City parseCity(ResultSet rset) throws SQLException {
        City city = new City();

        city.city_id = rset.getInt("ID");
        city.city_name = rset.getString("Name");
        city.city_countryCode = rset.getString("CountryCode");
        city.city_district = rset.getString("District");
        city.city_population = rset.getInt("Population");
        return city;
    }

    private Country parseCountry(ResultSet rset) throws SQLException {
        Country country = new Country();

        country.Code = rset.getString("Code");
        country.Name = rset.getString("Name");
        country.Continent = rset.getString("Continent");
        country.Region = rset.getString("Region");
        country.SurfaceArea = rset.getFloat("SurfaceArea");
        country.IndepYear = rset.getInt("IndepYear");
        country.Population = rset.getInt("Population");
        country.LifeExpectancy = rset.getFloat("LifeExpectancy");
        country.GNP = rset.getFloat("GNP");
        country.GNPOLD = rset.getFloat("GNPOLD");
        country.LocalName = rset.getString("LocalName");
        country.GovernmentForm = rset.getString("GovernmentForm");
        country.HeadOfState = rset.getString("HeadOfState");
        country.Code2 = rset.getString("Code2");

        return country;
    }


    public void displayCity(City city)
    {
        if (city != null)
        {
            System.out.println(
                    city.city_id + "\n"
                            + city.city_name + "\n"
                            + city.city_countryCode + "\n"
                            + city.city_district + "\n"
                            + city.city_population + "\n");
        }
    }


    public void getCountries(String sqlStatement)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(sqlStatement);

            ResultSetMetaData rsetmd = rset.getMetaData();
            int columnNumber = rsetmd.getColumnCount();

            while (rset.next())
            {
                String output = "";
                for (int i = 1; i <= columnNumber; i++)
                {
                    String columnName = rsetmd.getColumnName(i);
                    output = output + rset.getString(columnName) + "\t";
                }
                System.out.println(output);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
        }
    }

    /**
     * Old function being kept because it might be needed in the future
     * Should be removed before release
     * @param country
     */
    public void displayCountry(Country country)
    {
        if (country != null)
        {
            System.out.println(
                    country.Code + "\n"
                            + country.Name + "\n"
                            + country.Continent + "\n"
                            + country.Region + "\n"
                            + country.SurfaceArea + "\n"
                            + country.IndepYear + "\n"
                            + country.Population + "\n"
                            + country.LifeExpectancy + "\n"
                            + country.GNP + "\n"
                            + country.GNPOLD + "\n"
                            + country.LocalName + "\n"
                            + country.GovernmentForm + "\n"
                            + country.HeadOfState + "\n"
                            + country.Code2 + "\n");
        }
    }

}
