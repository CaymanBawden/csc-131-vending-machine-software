from faker import Faker
from datetime import datetime
from random import shuffle, randint, uniform
from snack import snacks

fake = Faker()

lines = []
num_vending_machines = 200
rows = 8
cols = 5
slots = 15
alphabet = "ABCDE"


def build_inventory_and_price():
    inventory = ""
    shuffle(snacks)
    shuffled_snacks = snacks[:40]
    prices = ""

    for i, snack in enumerate(shuffled_snacks):
        row = i // cols + 1
        col = alphabet[i % cols]

        price = round(uniform(.75, 2) * 4) / 4
        price = "{0:.2f}".format(price)

        prices += f"{str(row) + col}*{price}~"

        for i in range(randint(0, slots)):
            slot = i + 1
            date = (fake.date_between_dates(
                date_start=(datetime(2022, 1, 1)),
                date_end=(datetime(2026, 1, 1))).strftime("%Y/%m/%d"))
            inventory += f"{row}{col}{slot}*{snack}*{date}~"

    prices = prices[:-1]
    inventory = inventory[:-1]
    return inventory, prices


for i in range(num_vending_machines):
    if i % 1000 == 0:
        print(i)
    location = f"{fake.street_address()}*{fake.city()}*{fake.state()}*{fake.zipcode()}"
    vending_machine_id = i + 1
    inventory, prices = build_inventory_and_price()

    lines.append(f"{vending_machine_id},{location},{inventory},{prices}\n")

with(open('../data/data.csv', 'w')) as f:
    i = 0
    f.write("id,location,inventory,prices,queuedItems,purchaseHistory\n")
    for line in lines:
        if i % 1000 == 0:
            print(f"Writing line: {i}")
        f.write(line)
        i += 1
