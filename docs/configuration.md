---
layout: single 
title:  "Configure IFTTT for Spark Director"
date:   2020-04-15 21:29:59 -0500
categories: jekyll update
permalink: /configuration
---

### Configure IFTTT Services
1. Create an IFTTT account [here](https://ifttt.com)  
![setup1](/assets/images/IFTTT_signup.png){:.web_screenshot} 
2. Add the Webhooks Service by navigating to their [page](https://ifttt.com/maker_webhooks) and selecting "Connect"  
![setup2](/assets/images/IFTTT_Webhooks_connect.png){:.web_screenshot} 
3. After successfully setting up Webhooks, you should now see a "Documentation" Button. Clicking that reveals a page with you Webhook API Key. Save this page for future use.  
![setup3](/assets/images/IFTTT_Webhooks_connected.png){:.web_screenshot} 
4. Add the [Orro Service](https://ifttt.com/orro) (or other services), select "Connect", and sign in with your account.  
![setup4](/assets/images/IFTTT_Orro_connect.png){:.web_screenshot} 

### Create Applets
For each switch, you need to make 3 applets, 1 for on, 1 for off, and 1 to set a specific dimming level. The steps below can be repeated for each applet.  
1. Navigate to the IFTTT create page [here](https://ifttt.com/create)
2. Click "+ This"  
![setup5](/assets/images/IFTTT_create_this.png){:.web_screenshot} 
3. Search for and select "Webhooks"  
![setup6](/assets/images/IFTTT_create_webhook_search.png){:.web_screenshot} 
4. Choose "Receive a web request" as trigger  
![setup](/assets/images/IFTTT_create_choose_webhook_action.png){:.web_screenshot} 
5. Enter an event name signifying action and switch, and "Create Trigger". This will be needed later. For example, for an Attic switch: "turn_on_attic", "turn_off_attic", or set_attic"  
![setup](/assets/images/IFTTT_create_webhooks_trigger.png){:.web_screenshot} 
6. Click "+ That"  
![setup](/assets/images/IFTTT_create_that.png)  
7. Search for and select "Orro", "Hue", or other compatible lighting services  
![setup](/assets/images/IFTTT_create_orro_search.png){:.web_screenshot}  
8. Select the appropriate action (Turn ON, Turn OFF, or SET)  
![setup](/assets/images/IFTTT_create_orro_action.png){:.web_screenshot}  
9. Select which of your switches it should control 
    * When doing SET, click the "Add Ingredient" button and select "Value1" 
    ![setup](/assets/images/IFTTT_create_orro_fields.png){:.web_screenshot} 
10. Select "Create Action" 
11. Review your event and select "Finish" 
![setup](/assets/images/IFTTT_create_review.png){:.web_screenshot} 
12. Optional: You can test your new event from the Webhooks Documentation page and ensure it performs the desired action

## Add Rooms
Once you have created 3 applets (for turn on, turn off, and set level of a switch), you can use the event names and the webhook api key to add the control to the Spark Director App!

1. On the home page of the Spark Director app, tap the "+" icon on the bottom of the screen.  
![app1](/assets/images/SparkDirector_control_empty.png){: .app_screenshot} 
2. Enter a room name, e.g. "Office"
3. Enter the event name for the turn on action you previously created, e.g. "turn_on_office"
4. Enter the event name for the turn off action you previously created, e.g. "turn_off_office"
4. Enter the event name for the set action you previously created, e.g. "set_office"
5. Enter the Webhooks API Key from the webhooks documentation page. (Don't worry, you only have to enter this the first time. Will be saved and autopopulated for other rooms).  
![app4](/assets/images/SparkDirector_edit.png){: .app_screenshot} 
6. Select "Validate". This will ensure the Webhok API Key you entered is valid. If so, the "Add Room" button will be enabled
7. Select "Add Room". The room should now be added to the home page, and the On and Off buttons and the slider should control the light!  
![app3](/assets/images/SparkDirector_control.png){: .app_screenshot}