# Earthbound
A simple turn-based, DnD-inspired RPG, built with JSwing for the ISC4UR course culminating. 

## Project Overview

Earthbound is a fantasy turn-based RPG (role-playing-game), inspired by common turn-based systems like those found in the Pokemon games or Baldur’s Gate 3. 
The player can create a save, choose a class, and control a character from that class through a series of challenges/fight encounters with enemies.
Based on the player's attributes (hp, mana), they can choose from a selection of actions during an encounter. Successful encounters reward exp, to improve the player character's profile. 

## Why Earthbound?

I chose Earthbound as my final culminating for ISC4UR because it allows me to both expand upon concepts I've explored within the course, and experiment with unknown concepts. 
Considering that this is the last project I'll make for any compsci course at this school, I wanted to demonstrate improvement from previous projects without simply repeating something I've already done. 

For example, the Street-Fighter inspired game I made with Processing in the ICS3UR course last year allowed for playable characters, but the gameplay didn't employ much strategy, there was only one enemy & one player character choice, and animations were minimal.
With Earthbound, I'm able to add persistent save mechanics and player progression, better animations, and a larger scope in terms of development. This also serves as  a form of callback towards my introduction to games, beginning with playing Pokemon on a 2DS. 
Furthermore, the mechanics of a game like Earthbound are possible within JSwing, and aligns with what we learned in the course without being too simple/overly complex/unrelated from the course content. 

## Key Features

### Login/Registration Page:
  Options to load an existing save or create a new save (current save slot limit of 2);
  Character/class selection screen, with each class having different abilities/movesets.

### Gameplay:
  Idle animations between actions, animations for actions + effects;
  Turn-based actions selected on mouse click, dependent on mana level and hp level;
  Semi-random enemy algorithm;
  EXP System + player leveling;
  Multiple stages with custom enemies/enemy abilities.
