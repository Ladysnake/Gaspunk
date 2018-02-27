# GASPUNK 1.1

## The Update That Came After Modoff Update

#### Adds:

- Skin support: Now available, this support enables the ability to have special cosmetic skins on a Minecraft server. This is notably implemented for the Modoff Steampunk exclusive skin. Needs an internet connection.
- Configuration option: Grenade skin, taking a string. Current available options are "Classic" and "Steampunk" (solely for Modoff Gaspunk booth secret finders).
- πathos: A built-in library that introduces pathologies and diseases, with a severity factor.
  - Severity is identified by a floating between 0 and 1 included, where 0 is very minor and 1 is highly severe. Pathology effects are in function of this severity.
  - Syringe: An item added by the library that allows you to see what pathology you are affected by, along with their severity. An empty syringe is obtained with a bottle and an iron nugget on top, and right clicking and holding a syringe will sample your blood, giving a blood filled syringe with the list of active pathologies in it as tooltips. Using a blood filled syringe will result in you injecting the blood inside, and therefore affect yourself with the pathologies contained (along with emptying the syringe).
- Gas toxicity: A new factor that affects the evolution of a pathology severity when breathing long time effect gases. This factor is defined by a floating between 0 and 1 included, 0 being no effect, and 1 being extremely toxic.
- Formula for pathology severity evolution, at tic $n+1$ :
  - $severity $~n+1~$ = severity$~n~$ + (concentration * toxicity) / 20$
- Chemical agents: Obtained by brewing various elements, they are what the grenades vaporize. Every agent has its types displayed as a tooltip, both in the bottle and grenade item.
  - Smoke agent: Chemical agent that produces smoke once unleashed. View blocking but harmless when alone.
  - Pulmonary agent: Impedes breathing, and upon total air loss, suffocates any entity inside.
  - Lachrymator agent: Causes eye irritation pathology.
  - Heal agent: Triggers entity regeneration effect.
  - Nerve agent: Similar to pulmonary agent, except it causes respiratory muscle control loss pathology.

#### Gases:

Existing gases have their new agent, recipe and toxicity definitions below:

- [Smoke] Smoke: Obtained by brewing smoke powder into a water bottle. Whit colour
- [Smoke] Coloured smoke: Obtained by brewing any dye into a smoke bottle.
- [Smoke, Pulmonary] Choke smoke (aka Toxic Smoke): Obtained by brewing ash into a smoke bottle.
- [Smoke, Lachrymator] Tear gas: Obtained by brewing a fermented spider eye into a smoke bottle. Affects the player with eye irritation pathology with a toxicity of 0.2.
- [Heal] Healing vapour: Obtained by brewing a ghast tear into a water bottle. Affects the player with Regeneration II for 10 seconds (resetting each tick spent in the vapour).
- [Nerve] Sarin: Invisible, undetectable gas that bypasses the breath holding (unless the player sneaks). Causes lung control loss pathology with a toxicity of 0.8.

#### Pathologies:

List of new pathologies πathos includes in this update:

- Eye irritation: Blurs the player's vision(with a shader) in function of the severity. A severity of 1 makes the player totally blind. Not permanent, 0.001 severity lost by tick.
- Lung control loss: Deals suffocation damage when active at the rate of $-1hp * (severity * 10) / second$. Not permanent, 0.01 severity lost by tick.

#### Miscellaneous:

- Sneaking now blocks your respiration when in gas. Useful for avoiding Sarin for example.
- Gas concentration no longer affects the air left in your lungs.
- Removal of tubes. Vanilla bottles now act as tubes.
- Tear gas no longer slows the player.