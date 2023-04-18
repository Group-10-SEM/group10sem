package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class App
{
    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        ArrayList<Country> countries = a.getCountries(
                "SELECT Code, Name, Continent, Region, SurfaceArea,IndepYear, Population, LifeExpectancy, GNP, GNPOLD, LocalName, GovernmentForm, HeadOfState, Code2 ",
                "FROM country ",
                "WHERE Code = 'ZWE' ",
                "");
        countries.forEach(a::displayCountry);

        //ArrayList<City> cities = a.getCities();
        //cities.forEach(a::displayCity);

        // Disconnect from database
        a.disconnect();
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
                Thread.sleep(10000);
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

    public ArrayList getCountries()
    {
        return getCountries(
                "SELECT Code, Name, Continent, Region, SurfaceArea,IndepYear, Population, LifeExpectancy, GNP, GNPOLD, LocalName, GovernmentForm, HeadOfState, Code2 ",
                "FROM country ",
                "",
                "ORDER BY Population DESC");
    }

    public ArrayList<Country> getCountries(String selectStatement, String fromStatement, String whereStatement, String orderStatement)
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
            ArrayList<Country> countries = new ArrayList<>();

            while (rset.next())
            {
                Country country = parseCountry(rset);
                countries.add(country);
            }

            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

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