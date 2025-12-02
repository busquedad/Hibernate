## Covered Edge Cases

- **Data Loading Error:** The frontend handles the case where the API call to fetch pizzas fails. It displays an error message to the user.
- **Empty Database:** The backend's `bootstrapData` method checks if the `TamanioPizza` table is empty before seeding it with initial data. This prevents duplicate data from being inserted on subsequent application startups.
- **No Pizzas Available:** The frontend's `PizzaDisplay` component handles the case where the API returns an empty array of pizzas. It displays a "No pizzas available." message.
- **CORS:** The backend handles CORS preflight requests (`OPTIONS`) and sets the `Access-Control-Allow-Origin` header in the `before` filter to allow requests from the frontend.
