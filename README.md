# OSRS Bot Tracker README

This is a README file for the `OsrsBotTracker` Java class. The purpose of this document is to provide a brief overview of the public functions available in the class, their uses, and how they can be utilized.

Checkout the website at https://dev.mybottracker.com

## Table of Contents

1. [Overview](#overview)
2. [Public Functions](#public-functions)
    - [initialize](#initialize)
    - [isInstanceInitialize](#isinstanceinitialize)
    - [getInstance](#getinstance)
    - [track](#track)
    - [pushPlayerData](#pushplayerdata)
    - [pushPlayerCoinData](#pushplayercoindata)
    - [pushMembershipData](#pushmembershipdata)
    - [pushActivityData](#pushactivitydata)

## Overview

The `OsrsBotTracker` class is designed to track and manage player data for an Old School RuneScape (OSRS) bot. The class collects various types of player data, such as player statistics, coins, membership, and activities. It then sends this information to specified endpoints using the provided API farm key.

## Usage Examples

To use the `OsrsBotTracker` class in your OSRS bot script, follow these steps:

1. Initialize the `OsrsBotTracker` singleton instance in the `onStart` method:

```
public void onStart() {
    // Initialize OsrsBotTracker with the MethodProvider and API farm key
    OsrsBotTracker.initialize(this, "apiFarmKey");
}

public int onLoop() {
    // Call the track method to push player stats and coin data at specified intervals
    OsrsBotTracker.getInstance().track();
    return 100;
}
```



## Public Functions

### initialize

`public static void initialize(MethodProvider mp, String apiFarmKey)`

This function initializes the `OsrsBotTracker` instance with the provided `MethodProvider` and API farm key. It should be called before any other function is used.

### isInstanceInitialize

`public static boolean isInstanceInitialize()`

This function checks if the `OsrsBotTracker` instance has been initialized. It returns true if the instance is initialized, and false otherwise.

### getInstance

`public static OsrsBotTracker getInstance()`

This function returns the singleton instance of the `OsrsBotTracker` class. It throws an `IllegalStateException` if the instance has not been initialized.

### track

`public void track()`

This function is responsible for tracking the player's data at specific intervals. If the API farm key is valid and the required time has passed since the last data push, it pushes player statistics and coin data.

### pushPlayerData

`public void pushPlayerData()`

This function sends player skill data to the playerStatsUrl endpoint. It creates a JSON payload containing the player's skill levels and experience.

### pushPlayerCoinData

`public void pushPlayerCoinData()`

This function sends player coin data to the playerMoneyUrl endpoint. It calculates the total number of coins the player has in their bank and inventory and sends the information as a JSON payload.

### pushMembershipData

`public void pushMembershipData(Long days)`

This function sends player membership data to the playerMembershipUrl endpoint. It takes the number of days of membership as input and sends the information as a JSON payload.

### pushActivityData

`public void pushActivityData(String activity)`

This function sends player activity data to the playerActivityUrl endpoint. It takes the activity description as input and sends the information as a JSON payload.
