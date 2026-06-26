package classes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBinitialization {
    private Connection con;

    public DBinitialization() {
        try {
            con = DBconnection.getConnection();
        } catch (SQLException e) {
            System.err.println("Failed to get database connection.");
            e.printStackTrace();
        }
    }

    /**
     * Creates the necessary tables in the database
     * @throws SQLException if table creation fails
     */
    public void createTables() throws SQLException {
        Statement stmt = con.createStatement();
        
        try {
            // In Derby, we need to check if tables exist before creating them
            // since there's no IF NOT EXISTS clause
            DatabaseMetaData metaData = con.getMetaData();
            
            // Create Cars table if it doesn't exist
            ResultSet tables = metaData.getTables(null, null, "CARS", null);
            if (!tables.next()) {
                stmt.executeUpdate("CREATE TABLE Cars (id INT PRIMARY KEY, model VARCHAR(100), brand VARCHAR(100), price INT, fueltype VARCHAR(50), bodytype VARCHAR(50), transmission VARCHAR(50), num_seats INT)");
                System.out.println("Cars table created.");
            }
            
            // Create Users table if it doesn't exist
            tables = metaData.getTables(null, null, "USERS", null);
            if (!tables.next()) {
            	stmt.executeUpdate("CREATE TABLE Users (id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), name VARCHAR(100), age INT, gender VARCHAR(10), location VARCHAR(100), username VARCHAR(100) UNIQUE, password VARCHAR(255), role VARCHAR(50) DEFAULT 'user')");

                System.out.println("Users table created.");
            }
            
            // Create UserPreferences table if it doesn't exist
            tables = metaData.getTables(null, null, "USERPREFERENCES", null);
            if (!tables.next()) {
                stmt.executeUpdate("CREATE TABLE UserPreferences (user_id INT PRIMARY KEY, maxbudget INT, minbudget INT, preffered_color VARCHAR(50), preffered_fueltype VARCHAR(50), preffered_bodytype VARCHAR(50), preffered_transmission VARCHAR(50), preffered_num_seats INT, purpose VARCHAR(100), FOREIGN KEY (user_id) REFERENCES Users(id))");
                System.out.println("UserPreferences table created.");
            }
            
            // Create Recommendations table if it doesn't exist
            tables = metaData.getTables(null, null, "RECOMMENDATIONS", null);
            if (!tables.next()) {
                stmt.executeUpdate("CREATE TABLE Recommendations (recommendation_id INT GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY, user_id INT, car_recommended_id INT, score INT, FOREIGN KEY (user_id) REFERENCES Users(id), FOREIGN KEY (car_recommended_id) REFERENCES Cars(id))");
                System.out.println("Recommendations table created.");
            }
            
            System.out.println("Tables created successfully (if they didn't already exist).");
        } catch (SQLException e) {
            System.err.println("Error creating tables:");
            e.printStackTrace();
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    /**
     * Inserts a car into the database
     * @param car The car to insert
     * @throws SQLException if insertion fails
     */
    public void insertCar(Car car) throws SQLException {
        String sql = "INSERT INTO Cars (id, model, brand, price, fueltype, bodytype, transmission, num_seats) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, car.getId());
            pstmt.setString(2, car.getModel());
            pstmt.setString(3, car.getBrand());
            pstmt.setInt(4, car.getPrice());
            pstmt.setString(5, car.getFueltype());
            pstmt.setString(6, car.getBodytype());
            pstmt.setString(7, car.getTransmission());
            pstmt.setInt(8, car.getNum_seats());
            pstmt.executeUpdate();
            System.out.println("Car inserted successfully: " + car.getModel());
        } catch (SQLException e) {
            System.err.println("Error inserting car:");
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Inserts a user into the database
     * @param user The user to insert
     * @throws SQLException if insertion fails
     */
    public void insertUser(User user) throws SQLException {
        // Modified to include username and password
        String sql = "INSERT INTO Users (name, age, gender, location, username, password) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getName());
            pstmt.setInt(2, user.getAge());
            pstmt.setString(3, user.getGender());
            pstmt.setString(4, user.getLocation());
            pstmt.setString(5, user.getUsername());
            pstmt.setString(6, user.getPassword());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Get the generated ID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        System.out.println("User inserted successfully: " + user.getName() + " with ID: " + id);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting user:");
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Inserts user preferences into the database
     * @param prefs The user preferences to insert
     * @throws SQLException if insertion fails
     */
    public void insertUserPreferences(user_preferences prefs) throws SQLException {
        String sql = "INSERT INTO UserPreferences (user_id, maxbudget, minbudget, preffered_color, preffered_fueltype, preffered_bodytype, preffered_transmission, preffered_num_seats, purpose) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, prefs.getUser_id());
            pstmt.setInt(2, prefs.getMaxbudget());
            pstmt.setInt(3, prefs.getMinbudget());
            pstmt.setString(4, prefs.getPreffered_color());
            pstmt.setString(5, prefs.getPreffered_fueltype());
            pstmt.setString(6, prefs.getPreffered_bodytype());
            pstmt.setString(7, prefs.getPreffered_transmission());
            pstmt.setInt(8, prefs.getPreffered_num_seats());
            pstmt.setString(9, prefs.getPurpose());
            pstmt.executeUpdate();
            System.out.println("User preferences inserted successfully for user ID: " + prefs.getUser_id());
        } catch (SQLException e) {
            System.err.println("Error inserting user preferences:");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Inserts a recommendation into the database
     * @param rec The recommendation to insert
     * @throws SQLException if insertion fails
     */
    public void insertRecommendation(Recommendation rec) throws SQLException {
        // Modified to remove recommendation_id as it's auto-generated in Derby
        String sql = "INSERT INTO Recommendations (user_id, car_recommended_id, score) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, rec.getUser_id());
            pstmt.setInt(2, rec.getCar_recommended_id());
            pstmt.setInt(3, rec.getScore());
            pstmt.executeUpdate();
            System.out.println("Recommendation inserted successfully for user ID: " + rec.getUser_id());
        } catch (SQLException e) {
            System.err.println("Error inserting recommendation:");
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Generates recommendations for a user based on their preferences
     * @param userId The ID of the user
     * @throws SQLException if recommendation generation fails
     */
    public void generateRecommendations(int userId) throws SQLException {
        // Fetch user preferences
        String prefQuery = "SELECT * FROM UserPreferences WHERE user_id = ?";
        try (PreparedStatement prefStmt = con.prepareStatement(prefQuery)) {
            prefStmt.setInt(1, userId);
            ResultSet prefs = prefStmt.executeQuery();

            if (!prefs.next()) {
                System.out.println("No preferences found for user ID: " + userId);
                return;
            }

            Integer minBudget = prefs.getObject("minbudget", Integer.class);
            Integer maxBudget = prefs.getObject("maxbudget", Integer.class);
            String fuel = prefs.getString("preffered_fueltype");
            String body = prefs.getString("preffered_bodytype");
            String transmission = prefs.getString("preffered_transmission");
            Integer seats = prefs.getObject("preffered_num_seats", Integer.class);

            // Check if all preferences are null
            if (minBudget == null && maxBudget == null && fuel == null && body == null && transmission == null && seats == null) {
                System.out.println("No preferences specified for user ID: " + userId + ". Cannot generate recommendations.");
                return;
            }

            // Fetch all cars
            try (Statement stmt = con.createStatement();
                 ResultSet cars = stmt.executeQuery("SELECT * FROM Cars")) {

                // Process each car
                while (cars.next()) {
                    int score = 0;
                    int carId = cars.getInt("id");
                    int price = cars.getInt("price");
                    String carFuel = cars.getString("fueltype");
                    String carBody = cars.getString("bodytype");
                    String carTrans = cars.getString("transmission");
                    int carSeats = cars.getInt("num_seats");

                    // Scoring logic (skip comparison if user did not specify that attribute)
                    if (minBudget != null && maxBudget != null && price >= minBudget && price <= maxBudget) score += 2;
                    if (fuel != null && fuel.equalsIgnoreCase(carFuel)) score += 2;
                    if (body != null && body.equalsIgnoreCase(carBody)) score += 2;
                    if (transmission != null && transmission.equalsIgnoreCase(carTrans)) score += 2;
                    if (seats != null && seats == carSeats) score += 1;

                    if (score > 0) {
                        // Insert recommendation
                        String insertSQL = "INSERT INTO Recommendations (user_id, car_recommended_id, score) VALUES (?, ?, ?)";
                        try (PreparedStatement insertStmt = con.prepareStatement(insertSQL)) {
                            insertStmt.setInt(1, userId);
                            insertStmt.setInt(2, carId);
                            insertStmt.setInt(3, score);
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }

            System.out.println("Recommendations generated for user ID: " + userId);
        } catch (SQLException e) {
            System.err.println("Error generating recommendations:");
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Gets the top recommendation for a user
     * @param userId The ID of the user
     * @return The top recommended car, or null if none found
     * @throws SQLException if retrieval fails
     */
    public Car generateRecommendation(int userId) throws SQLException {
        // Derby doesn't support LIMIT, so we use FETCH FIRST ROW ONLY
        String query = "SELECT c.* FROM Cars c " +
                       "JOIN Recommendations r ON c.id = r.car_recommended_id " +
                       "WHERE r.user_id = ? ORDER BY r.score DESC FETCH FIRST ROW ONLY";
        
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            // Check if a recommendation was found
            if (rs.next()) {
                int id = rs.getInt("id");
                String model = rs.getString("model");
                String brand = rs.getString("brand");
                int price = rs.getInt("price");
                String fueltype = rs.getString("fueltype");
                String bodytype = rs.getString("bodytype");
                String transmission = rs.getString("transmission");
                int num_seats = rs.getInt("num_seats");

                // Create and return a Car object
                return new Car(id, model, brand, price, fueltype, bodytype, transmission, num_seats);
            } else {
                System.out.println("No recommendations found for user ID: " + userId);
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error getting top recommendation:");
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Gets all recommendations for a user
     * @param userId The ID of the user
     * @return A list of recommended cars
     * @throws SQLException if retrieval fails
     */
    public List<Car> getAllRecommendations(int userId) throws SQLException {
        List<Car> recommendedCars = new ArrayList<>();
        
        String query = "SELECT c.* FROM Cars c " +
                       "JOIN Recommendations r ON c.id = r.car_recommended_id " +
                       "WHERE r.user_id = ? ORDER BY r.score DESC";
        
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            // Iterate through the result set and create Car objects
            while (rs.next()) {
                int id = rs.getInt("id");
                String model = rs.getString("model");
                String brand = rs.getString("brand");
                int price = rs.getInt("price");
                String fueltype = rs.getString("fueltype");
                String bodytype = rs.getString("bodytype");
                String transmission = rs.getString("transmission");
                int num_seats = rs.getInt("num_seats");

                // Create a new Car object and add it to the list
                Car car = new Car(id, model, brand, price, fueltype, bodytype, transmission, num_seats);
                recommendedCars.add(car);
            }
            
            return recommendedCars;
        } catch (SQLException e) {
            System.err.println("Error getting all recommendations:");
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Main method to run the database initialization
     */
    public static void main(String[] args) {
        try {
            // Create database initialization object
            DBinitialization db = new DBinitialization();
            
            // Create tables if they don't exist
            db.createTables();
            
            // Sample data insertion for testing
            System.out.println("Would you like to insert sample data? (y/n)");
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            String response = scanner.nextLine().trim().toLowerCase();
            
            if (response.equals("y")) {
                // Insert sample cars
                db.insertCar(new Car(1, "Model S", "Tesla", 80000, "Electric", "Sedan", "Automatic", 5));
                db.insertCar(new Car(2, "F-150", "Ford", 45000, "Gasoline", "Truck", "Automatic", 5));
                db.insertCar(new Car(3, "Civic", "Honda", 25000, "Gasoline", "Sedan", "Automatic", 5));
                db.insertCar(new Car(4, "Wrangler", "Jeep", 35000, "Gasoline", "SUV", "Manual", 4));
                db.insertCar(new Car(5, "Prius", "Toyota", 30000, "Hybrid", "Hatchback", "Automatic", 5));
                
                // Insert a sample user
                User user = new User(0, "Adham Ashraf", 30, "Male", "New York", "aashraf", "123ash456","user");
                db.insertUser(user);
                
                // For Derby, we need to get the generated ID
                // This is a simplified approach - in a real app, you'd get the ID from the insertUser method
                int userId = 1;
                
                // Insert sample user preferences
                user_preferences prefs = new user_preferences(userId, 50000, 20000, "Black", "Gasoline", "Sedan", "Automatic", 5, "Daily commute");
                db.insertUserPreferences(prefs);
                
                // Generate recommendations for the user
                db.generateRecommendations(userId);
                
                // Retrieve and display the top recommendation
                Car topRecommendation = db.generateRecommendation(userId);
                if (topRecommendation != null) {
                    System.out.println("\nTop recommendation for user " + userId + ":");
                    System.out.println(topRecommendation.getBrand() + " " + topRecommendation.getModel() + " - $" + topRecommendation.getPrice());
                }
                
                // Optionally, retrieve and display all recommendations
                System.out.println("\nWould you like to see all recommendations? (y/n)");
                response = scanner.nextLine().trim().toLowerCase();
                if (response.equals("y")) {
                    List<Car> allRecommendations = db.getAllRecommendations(userId);
                    System.out.println("\nAll recommendations for user " + userId + ":");
                    for (Car car : allRecommendations) {
                        System.out.println(car.getBrand() + " " + car.getModel() + " - $" + car.getPrice());
                    }
                }
                scanner.close();
            }
            
            System.out.println("\nDatabase initialization completed successfully.");
            
        } catch (SQLException e) {
            System.err.println("Error during database operations:");
            e.printStackTrace();
        } finally {
            // Close the database connection
            try {
				DBconnection.closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}