# Vending Machine Software

## Installation
1. A JRE (min version 1.9) is required (install [here](https://www.oracle.com/java/technologies/downloads/))
2. Expand the app.zip file in the latest [release](https://github.com/CaymanBawden/csc-131-vending-machine-software/releases) in GitHub
3. change data/id.txt to the desired vending machine.

You are now ready to run the .exe files.

## Dev Guide
1. Install IntelliJ [here](https://www.jetbrains.com/idea/download/)
2. Build project in IntelliJ to make .jars in out/artifacts/*.
3. Install Launch4J [here](https://sourceforge.net/projects/launch4j/files/launch4j-3/3.50/)
4. Open Launch4J and open app/*-config.xml.
5. Change the .jar file location and output location to generate .exe.

## Future Features
- Add better Error Messages
- Add SQL database to synchronize 
- Give exact change (quarters, dimes, pennies, etc)
- Add quantity field to the manager interface to let managers add multiple items
- Add ability for restockers to search for items
- Make UI prettier and able to be on a smaller screen.
- If customer inserts less money than required, the machine will prompt for more change and wait x seconds for money to be inserted.
- Add functionality for cancelling a purchase
- Make vending machine time out after certain time and return to home screen.

## Current Bugs
- When a customer purchases an item with a string (eg 1A) the system parses that as a valid number
- When a restocker resolves an item that was queued to be removed by management, all the items after that item in the inventory are also removed when the file is saved. Restockers have to remove all items to resolve bug.

