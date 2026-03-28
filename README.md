# Energy-Management-System

This project is an implementation of an energy management system for a Mars colony.

## Description

The application simulates a micro-grid that manages energy production, consumption, and storage. The system runs in simulation steps (ticks), where a central controller balances energy across all components.

## Features

- Energy producers:
  - Solar panels
  - Wind turbines
  - Nuclear reactor
- Energy consumers with priority levels
- Battery system for energy storage
- Central controller for energy balancing
- Simulation system based on ticks
- Basic command-based interaction

## Functionality

- Calculate total energy production and consumption
- Store excess energy in batteries
- Use stored energy in case of deficit
- Disconnect low-priority consumers when needed
- Maintain critical systems active
- Detect and handle blackout situations

## Commands

- Add producers, consumers, and batteries
- Simulate next step (tick)
- Set component status (working/defective)
- View grid status
- View event history

## Technologies

- Java
- OOP principles (inheritance, polymorphism, abstraction)
- Collections

## How to run

Run the main class and use the console commands to interact with the system.

## Notes

The project focuses on modeling a system using object-oriented design and implementing the energy balancing logic correctly.
