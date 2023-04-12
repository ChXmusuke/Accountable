# Accountable
A small programming project for logging personal expenses.\
For now it aims to include the following features:
- [x] Logging of transactions
- [x] Multiple accounts
- [ ] Presentation of data about the logged transactions (charts)

# How to use

## Transactions
You can create a transaction either by clicking on the green "+" button in the bottom left \
or by pressing the spacebar while in the transactions list menu. \
A transaction can be of one of four types:

- **Revenue** - Green
- **Budget** - Yellow
- **Bill** - Red
- **Savings** - Blue

The value of the transaction can either be an absolute value, \
or defined by the values of previous transactions. \
The values can be computed as follows:

- **Absolute** - the value is the value of the transaction
- **Total** - the value is a percentage of the month's revenue
- **Remainder** - the value is a percentage of the month's remaining funds
- **All** - the value is all of the month's remaining funds \
(equivalent to setting a "Remainder" transaction's value to 100)

The month's remaining balance is displayed above the transaction list. \
It is green when everything's alright, orange if less than 10% of it is remaining, \
blue if it equals 0 and red if it goes below 0. \
To modify a transaction, click on the transaction's tile on the transaction list screen.

## Dates
If no transaction data is found, a new file corresponding to \
the current month and year is automatically created. \
To manually add a date, click on the "folder-plus" button in the top left part of the window. \
To select a date, use the two choice lists.

## Accounts
Before assigning a transaction to a savings account, you have to create the account. \
To do so, click on the "wallet" button in the top left corner of the window, \
and either click on the green "+" button or press the space bar. \
\
To delete an account, it must be empty. \
\
To select a savings account for a transaction, select the "Savings" transaction type and \
use the choice box that appears below to choose one of the accounts that you created. \
The account's balance will then be updated as you add transactions to that account. \
\
The account balances and names are stored in a cache file. If this file happens to be lost, \
it will be automatically rebuilt at startup time, but the account names will not be kept.
