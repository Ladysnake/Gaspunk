#####Version 1.12.2-1.4.7 - BUILT
- Fixed a startup crash on dedicated servers

#####Version 1.12.2-1.4.6 - BUILT
- Disabled candyfloss ore recipe when candyworld is not installed, fixes loading crash with Immersive Engineering
- Gas loading errors are now more descriptive and don't crash. Check your logs if you make custom gases !

#####Version 1.12.2-1.4.5 - BUILT
Update the contained library

#####Version 1.12.2-1.4.4 - BUILT
Removed an unlocalized tooltip line on candyfloss grenades and tubes.

#####Version 1.12.2-1.4.3 - BUILT
Fix a crash when Need to Breathe is not installed

#####Version 1.12.2-1.4.2 - BUILT
Additions / Changes:

- Candyfloss clouds now are a lot more nutritious ! They aren't as easily breathable though.
- Made the gas mask work as a Need to Breathe protective helmet

Api changes:

- Made the gas bypass bypass agents checks as well

Also check out UpcraftLP's new add-on, Gaspunk Inhaler !

#####Version 1.12.2-1.4.1 - BUILT
Now requires forge 1.12.2-14.23.3.2669 or later


Added a few API features

- added a way for add-ons to bypass a gas' checks when running its effect
- exposed a few classes

Also now packages Ladylib as a contained dependency instead of shading the code like a caveman

#####Version 1.12.2-1.4 - BUILT
Additions / changes :

- Gas agents are now defined through json files
    - There is an appropriate config folder for modpack makers
    - This means you can make custom gases using any statusEffect now
- Items have thaumcraft aspects assigned
    - Gas tubes and grenades are done automagically, which means any custom gas should have aspects out of the box

Localization additions :

- Updated german translation thanks to UpcraftLP

Fixes :

- Fixed a crash when the jar's certificate was invalid
- Added the missing recipe for the syringe

