# BeeForagingSimulation

This is a Java application for simulating bee foraging behavior in a given environment.

## Prerequisites

Before running the application, make sure you have the following installed:

1. **OpenJDK 21.0.3**: You can download and install it from [here](https://learn.microsoft.com/en-us/java/openjdk/download#openjdk-21).

2. **Cmder**: Download and install Cmder from [here](https://cmder.app/).

3. **Visual Studio Code Extensions**: Install the required extensions for Visual Studio Code to run and execute Java code.

4. **Git**: Download and install Git from [here](https://git-scm.com/download/win).

## Setup

1. **Add Environment Variables**:
   - Add the paths of OpenJDK, Git, and Cmder to the system's environment variables.

2. **Clone Repository**:
   - Open your command line interface (e.g., Cmder) and navigate to the directory where you want to clone the repository.
   - Run the following command to clone the repository:
     ```
     git clone [<repository_link>](https://github.com/mohamedabi17/Bee-Foraging-Simulation/)
     ```

3. **Compile Source Code**:
   - Navigate to the `src` folder of the cloned repository.
   - Run the following command to compile the Java source code:
     ```
     javac -cp . entities/*.java gui/*.java simulation/*.java
     ```

## Running the Application

1. **Navigate to Project Directory**:
   - Using Cmder, navigate to the directory where the project files are located.

2. **Execute Command**:
   - Run the following command to start the application:
     ```
     java gui.BeeForagingSimulationGUI
     ```

## Usage

- Upon running the application, you will see the GUI for the Bee Foraging Simulation.
- Follow the instructions on the GUI to configure the simulation parameters and start the simulation.

## Contributors

- [Your Name](https://github.com/your_username)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
