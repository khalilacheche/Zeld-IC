package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.arpg.ARPGBehavior.ARPGCell;
import ch.epfl.cs107.play.game.arpg.actor.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.CaveDoor;
import ch.epfl.cs107.play.game.arpg.actor.Chest;
import ch.epfl.cs107.play.game.arpg.actor.DialogTrigger;
import ch.epfl.cs107.play.game.arpg.actor.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.Grass;
import ch.epfl.cs107.play.game.arpg.actor.King;
import ch.epfl.cs107.play.game.arpg.actor.Monster;
import ch.epfl.cs107.play.game.arpg.actor.Orb;
import ch.epfl.cs107.play.game.arpg.actor.Rock;
import ch.epfl.cs107.play.game.arpg.actor.ShopMan;
import ch.epfl.cs107.play.game.arpg.actor.Villager;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;

public interface ARPGInteractionVisitor extends RPGInteractionVisitor {
    
	
	/**
     * Simulate and interaction between ARPG Interactor and an ARPGCell
     * @param cell (ARPGCell), not null
     */
	default void interactWith(ARPGCell cell) {
		//by default the interaction is empty
	}
	
	
	/**
     * Simulate and interaction between ARPG Interactor and an ARPGPlayer
     * @param player (ARPGPlayer), not null
     */
	default void interactWith(ARPGPlayer player) {
		//by default the interaction is empty
	}
	
	
	/**
     * Simulate and interaction between ARPG Interactor and a Grass
     * @param grass (Grass), not null
     */
	default void interactWith(Grass grass) {
		
	}
	
	
	/**
     * Simulate and interaction between ARPG Interactor and a CollectableAreaEntity
     * @param item (CollectableAreaEntity), not null
     */
	default void interactWith(ARPGCollectableAreaEntity item) {
		
	}
	
	
	/**
     * Simulate and interaction between ARPG Interactor and a CastleDoor
     * @param door (CastleDoor), not null
     */
	default void interactWith(CastleDoor door) {
		
	}
	
	
	/**
     * Simulate and interaction between ARPG Interactor and a Bomb
     * @param bomb (Bomb), not null
     */
	default void interactWith(Bomb bomb) {
		
	}
	
	
	/**
     * Simulate and interaction between ARPG Interactor and an Monster
     * @param monster (Monster), not null
     */
	default void interactWith(Monster monster) {
		
	}
	
	
	/**
     * Simulate and interaction between ARPG Interactor and a FireSpell
     * @param spell (FireSpell), not null
     */
	default void interactWith(FireSpell spell) {
		
	}
	
	/**
     * Simulate and interaction between ARPG Interactor and a Rock
     * @param rock (Rock), not null
     */
	default void interactWith(Rock rock) {
		
	}
	/**
     * Simulate and interaction between ARPG Interactor and a CaveDoor
     * @param door (CaveDoor), not null
     */
	default void interactWith(CaveDoor door) {
		
	}	

	/**
     * Simulate and interaction between ARPG Interactor and a Chest
     * @param chest (Chest), not null
     */
	default void interactWith(Chest chest) {
	}	
	/**
     * Simulate and interaction between ARPG Interactor and an orb
     * @param orb (orb), not null
     */
	default void interactWith(Orb orb) {
	}
	
	/**
     * Simulate and interaction between ARPG Interactor and a villager
     * @param villager (Villager), not null
     */
	default void interactWith(Villager villager) {
	
	}
	/**
     * Simulate and interaction between ARPG Interactor and a dialogTrigger
     * @param trigger (DialogTrigger), not null
     */
	default void interactWith(DialogTrigger trigger) {
		
	}

	/**
     * Simulate and interaction between ARPG Interactor and a shopMan
     * @param shop (ShopMan), not null
     */
	default void interactWith(ShopMan shop) {
		
	}

	/**
     * Simulate and interaction between ARPG Interactor and a king
     * @param king (King), not null
     */
	default void interactWith(King king) {
		
	}
}
