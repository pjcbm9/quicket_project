quicket_project
===============
- Unzip project
- ideally import to Android Studio (i havent tested in eclipse) - i do have concerns since the gradle is a feature of android studio exclusively
- start MainActivity (Quicket/app/src/main/java/pjcbm9.quicket/MAIN_ACTIVITY_PACKAGE/MainActivity)
- ideally start application on android phone 
  - to get developer option refer to http://www.samsung.com/us/support/faq/FAQ00056226/70300/SGH-I527ZKBATT
  -  from settings, click more then developer options, the for the section USB debugging ensure it is checked
  - then run quicket from android studio and choose the phone when the "choose device promp pops up" ** note phone must be plugged in to computer
  - after clicking ok the app should start on the phone automatically, if it doesn't try toggling the USB debugging
- If you use an emulator, Quicket's API is Android API 20 Platform, the jdk type = ANdroid SDK
Quicket Overview

MainActivity
-----------------
  - can click Create New Ticket (opens NewTicket Activity)
  - can click Load Draft Tickets (opens Ticket_List Activity)
  - can click View Completed Tickets (opens Ticket_List Activity)
  - can click View Active Tickets (opens Ticket_List Activity)
  - can click view Overdue Tickets (opens Ticket_List Activity)
  - **Note the last 4 options can be disabled if no ticket's exist yet of that status 
  - From ActionBar can select Maintenance Icon (opens Maintenance Activity)
  - From ActionBar can select About Icon (opens Info Activity)

NewTicket
-------------------
  - Can enter character into boxes labeled as Name and Customer
  - can select from spinners labled User,Type,Location
  - can click the button labled addDescription (opens a dialog - press clear to empty, submit to add description, exit to end dialog)
  - can click Submit to submit ticket (**Note will be disabled until all critical information is filled) (Opens TicketView)
  - can click Save Draft to save ticket as draft (revert to MainActivity)
  - can click the Home Icon from ActionBar (goes back to MainActivity)

ListActivity
------------------
  - The Vertical Button Bar
      - click Active to show active tickets in listview
      - click Overdue to show overdue tickets in listview
      - click Completed to show completed tickets in listview
      - click drafft to show draft tickets in listview
      - ** not buttons may be disabled if no tickets exist of the indicated status
  - the control bar directly above the listview
    - click magnifying glass icon to start search setup
        - enter a keyword into the textbox to the left of the magnifying glass button
        - click magnifying glass button to show matches that contain keyword
        - click cancel button next to magnifying glass button to cancel search mode
    - clikc the X icon to enter delete mode
        - click the checkbox to the left of the tickets in the listview to mark for deletion
        - click the button labled Delete to delete all checked tickets
        - click the cancel icon next to the delete button to cancel delete mode
    - click the green checkmark icon to enter complete mode
        - click the checkbox to the left of the tickets in the listview to mark for completion
        - click the button labled Complete to complete all checked tickets
        - click the cancel icon next to the complete button to cancel complete mode
  - the ActionBar
      - click the home icon to return to main activity
      - click the green plus icon to start the new ticket activity
  ** note icons for deleting and completing are only available for certain ticket statuses 
  - The ListView
      - clicking a draft ticket will start a new ticket acitvity with the fields prefilled with the saved contents of the draft ticket clicked
      - clicking any other ticket type will start the TicketView activity

  TicketView activity
  -------------------------
      - can click edit to start the new ticket activity with the fields prefilled with the contents of the ticket being displayed
      - can click delete to delete the displayed ticket
      - can click complete to compelte the displayed ticket
      ** note buttons may be disabled depending on ticket status
      - can click the home icon from the actionbar to return to main menu
 
  Maintenance activity
  ----------------------------
    - selecting an item from the spinner at the top changes the conents of the listview below
    - clicking an item in the listview allows you to edit its contents
    - clicking the delete icon to the right of the item name in the listview will delete that item
    - clicking the undo icon in the control bar above the listview will undo all changes since last saved
    - clicking the save icon in the control bar above the listview will save all changes made
    - clicking the clear icon in the control bar above the listview will clear all the items in the listview
    - clicking the add icon in the control bar above the listview will add a new item to the listview
    - if you select the countdown item in the spinner at the top the countdown editor will appear
    - click the arrows to manipulate the time
    - click the save icon to save the countdown duration
    - click undo icon to undo changes
    - click home icon in the actionbar to return to main menu
  
