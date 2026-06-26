

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import classes.Car;
import classes.DBinitialization;

@WebServlet("/RecommendServlet")
public class RecommendServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RecommendServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get user ID from request
            int userId = parseIntOrDefault(request.getParameter("userId"), -1);
            if (userId == -1) {
                request.setAttribute("error", "Invalid or missing user ID.");
                request.getRequestDispatcher("recommend.jsp").forward(request, response);
                return;
            }

            // Initialize DB and generate recommendations
            DBinitialization db = new DBinitialization();
            db.generateRecommendations(userId); // Generates the recommendations and stores them in the DB

            // Fetch recommended cars using the new method in DBinitialization
            List<Car> recommendedCars = db.generateRecommendation(userId); // Retrieves the recommendations

            // Set the recommended cars as a request attribute and forward to the JSP
            request.setAttribute("recommendedCars", recommendedCars);
            request.getRequestDispatcher("recommend.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("recommend.jsp").forward(request, response);
        }
    }

    // Helper method to safely parse integers
    private int parseIntOrDefault(String param, int defaultValue) {
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
