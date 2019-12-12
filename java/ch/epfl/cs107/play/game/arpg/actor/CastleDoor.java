package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Arrays;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer.ARPGPlayerHandler;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class CastleDoor extends Door {
	Sprite open;
	Sprite closed;

	public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area,
			Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates... otherCells) {
		super(destination, otherSideCoordinates, signal, area, orientation, position);
		this.currentCells.addAll(Arrays.asList(otherCells));
		setSignal(Logic.FALSE);
		open = new Sprite("zelda/castleDoor.open",2f,2f,this,new RegionOfInterest(0,0,32,32));
		closed = new Sprite("zelda/castleDoor.close",2f,2f,this,new RegionOfInterest(0,0,32,32));
	}
	
	
	
	
	@Override
	public boolean isViewInteractable() {
		return true;
	}
	
	
	@Override
	public boolean takeCellSpace() {
		return !isOpen();
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if(isOpen()) {
			open.draw(canvas);
		}else {
			closed.draw(canvas);
		}
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		if(v instanceof ARPGPlayerHandler)
			((ARPGPlayerHandler)v).interactWith(this);
	}
	

}
