# csc-131-vending-machine-software
## Main.java
The Main.java file is where the gui and interaction with the vending machines will be put for both restockers and customers, as well as initializing all the vending machines by creating a VendingMachines object.
For release 1 this file is only used to test the code and does not have a gui.
To interact with a specific vending machine the command to do so is: vendingMachineData.getVendingMachineById(id) with id being a number 1, 2, ..., n

## VendingMachines.java
This is where each vending machine is initialized and indexed into an Arraylist by using a file (called data.csv) which contains the id, location, inventory, and price of inventory of every Vending machine.
The class goes through every line, each of which is the contents of an individual vending machine, and does the initializing
the only method within the VendingMachines class is the getVendingMachineById(int id) method, which returns whichever vending machine by ID you want.

## VendingMachine.java
The initalization of the VendingMachine class is given by the data the VendingMachines class gives it, containing that particualar machines info, and is also given the file that the information is gotten from
The data is stored, the inventory and price is put into an array  which specifies the price, product, and if necessary the expiration date
### It has a couple of methods:
#### For Restockers: 
##### addItem(), which adds an item into a specific row and column if there is an available slot left.
##### removeSpecificItem(), which removes an item from a specific row,column, and slot.
##### refreshData(), which refreshes the data in the vending machine file after each change made to the inventory. (Not meant to be used in this release, but there if needed
##### checkExpirations(), which makes sure that there are no products that have expired, or are about to expire. If there are, the specific item is shown to the restocker.
#### For Customers:
##### removeFrontItem(), which only removes the front item of the vending machine, just like the hardware would give. This will be the only way that the customer will interact with the inventory.
#### For debugging:
##### printInventory(), which shows every detail about the inventory: which row, column, and slot each product is in, as well as what product, price, and expiration it has. This is for debugging the code only by the developers only.
