# OhmBin Vending Machine

OhmBin is a modern vending machine application with a graphical user interface (GUI) built using Java Swing. It allows users to browse products, add them to a cart, and proceed with checkout using different payment methods. The application also includes an admin panel for managing the product inventory.

## Features

- **Customer View**:

  - Browse products with images and details.
  - Add products to the cart.
  - Checkout with payment options (Cash or PromptPay).
  - View purchase history.

- **Admin View**:

  - Add, edit, or remove products.
  - Manage product inventory and priorities.

- **Interactive UI**:
  - Animations for buttons and cart updates.
  - Background music and sound effects for interactions.

## Prerequisites

- Java Development Kit (JDK) 8 or higher.
- A terminal or command prompt for running the application.

## Setup Instructions

1. **Clone the Repository**:

   ```bash
   git clone <repository-url>
   cd Final_ProStruct/OhmBin
   ```

2. **Compile the Code**:
   On macOS/Linux:

   ```bash
   ./run.sh
   ```

   On Windows:

   ```bat
   run.bat
   ```

3. **Run the Application**:
   The application will launch with the GUI.

## File Structure

- `src/`: Contains all the Java source files.
- `assets/`: Contains images, icons, and sound files used in the application.
- `bin/`: Directory where compiled `.class` files are stored.
- `run.sh`: Script to compile and run the application on macOS/Linux.
- `run.bat`: Script to compile and run the application on Windows.

## Admin Access

To access the admin panel, use the following secret code in **App.java** you can find it by yourself.

## Notes

- Ensure the `assets/` folder contains all required images and sound files.
- The application uses a priority queue to manage product inventory based on priority levels.

## License

This project is for educational purposes only.
