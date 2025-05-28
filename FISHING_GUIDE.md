# 🎣 Farming Game - Fishing System Guide

## 🎮 **How to Fish**

### Controls
- **Press 'C'** to cast your fishing line and attempt to catch fish

### Requirements
- **Energy**: Each fishing attempt costs **10 energy points**
- **Location**: You must be standing on or adjacent to fishable water (rivers, lakes, ocean)
- **Current Energy**: Check your energy level before fishing

### Energy System
- **Maximum Energy**: 100 points
- **Fishing Cost**: 10 energy per attempt
- **Energy Management**: Make sure to rest or consume food to restore energy

## 🐟 **Fish Rarity System**

The fishing system uses a weighted random selection with four rarity tiers:

### **Common Fish (60% chance)**
- **Carp** - Value: 30g, Energy: 15
- **Sardine** - Value: 40g, Energy: 20  
- **Smallmouth Bass** - Value: 50g, Energy: 25

### **Uncommon Fish (25% chance)**
- **Rainbow Trout** - Value: 65g, Energy: 35
- **Pike** - Value: 100g, Energy: 50
- **Red Snapper** - Value: 50g, Energy: 25
- **Flounder** - Value: 100g, Energy: 50
- **Salmon** - Value: 75g, Energy: 40
- **Largemouth Bass** - Value: 100g, Energy: 50

### **Rare Fish (10% chance)**
- **Catfish** - Value: 200g, Energy: 75
- **Tuna** - Value: 100g, Energy: 70

### **Legendary Fish (5% chance)**
- **Legend** - Value: 5000g, Energy: 200
- **Crimsonfish** - Value: 1500g, Energy: 150
- **Angler** - Value: 900g, Energy: 100

## 🌊 **Fishing Locations**

### **River Fishing**
- Find rivers on the forest map
- Good for: Carp, Catfish, Rainbow Trout, Pike, Angler

### **Ocean Fishing**  
- Access ocean areas from coastal maps
- Good for: Sardine, Tuna, Red Snapper, Flounder, Salmon, Crimsonfish

### **Lake Fishing**
- Find lakes in mountain areas
- Good for: Largemouth Bass, Smallmouth Bass, Legend

## ✨ **Visual Feedback**

The game provides real-time visual feedback for all fishing actions:

- **🎯 "Casting your line..."** - When you start fishing
- **✅ "You caught a [Fish Name]!"** - Successful catch (Green text)
- **❌ "No fish bit... Try again!"** - Unsuccessful attempt (Orange text)
- **⚡ "Not enough energy to fish!"** - Insufficient energy (Red text)
- **📍 "You need to be near water to fish!"** - Wrong location (Yellow text)

## 💡 **Tips for Success**

1. **Energy Management**: Keep track of your energy and plan fishing sessions accordingly
2. **Location Scouting**: Explore different water bodies for variety in fish types
3. **Persistence**: Legendary fish are rare - keep trying!
4. **Inventory Space**: Make sure you have inventory space for your catches
5. **Time Management**: Plan fishing around other farm activities

## 🔧 **Technical Implementation**

### **Key Classes:**
- `PlayerAction.java` - Main fishing logic and energy management
- `FishingUI.java` - Visual feedback and user interface
- `MapController.java` - Water detection and collision logic
- `FishData.java` - Fish database and properties
- `KeyHandler.java` - Input handling for fishing action

### **MVC Pattern:**
- **Model**: FishData, Fish items, Energy system
- **View**: FishingUI, visual feedback, console output  
- **Controller**: PlayerAction, MapController, KeyHandler

## 🎯 **Future Enhancements**

Potential improvements that could be added:
- Fishing rod upgrades for better catch rates
- Seasonal fish availability
- Weather-dependent fishing success
- Fishing competitions with NPCs
- Bait system for specific fish types
- Fishing skill progression system

---

**Happy Fishing! 🎣** 

*Remember: The best fishermen are patient and persistent!*
