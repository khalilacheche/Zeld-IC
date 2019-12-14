package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;
public class Villager extends MovableAreaEntity{
	private Animation[] idleAnimations;
	private Animation currentAnimation;
	private final static int ANIMATION_DURATION=4;
	private static final int MAX_INACTION_TIME = 24;
	private static final double PROBABILITY_OF_INACTIVE = 0.1;
	private String key;
	private int inactionTime;
	private boolean canMove;
	
	
	/** Adds one InventoryItem to the inventory
	 * @param: item (InventoryItem): The item to add
	 * @return: success (boolean): Returns true if the element was successfully added to the inventory  
	 */
	public Villager(Area area, Orientation orientation, DiscreteCoordinates position,String key,boolean canMove) {
		super(area, orientation, position);
		idleAnimations = RPGSprite.createAnimations(ANIMATION_DURATION, 
				RPGSprite.extractSprites("zelda/character",
				4, 1, 2,
				this , 16, 32,new Orientation[] {Orientation.UP ,
				Orientation.RIGHT , Orientation.DOWN, Orientation.LEFT})
		);
		this.key=key;
		this.canMove=canMove;
	}
	public Villager(Area area, Orientation orientation, DiscreteCoordinates position,String key) {
		super(area, orientation, position);
		idleAnimations = RPGSprite.createAnimations(ANIMATION_DURATION, 
				RPGSprite.extractSprites("zelda/character",
				4, 1, 2,
				this , 16, 32,new Orientation[] {Orientation.UP ,
				Orientation.RIGHT , Orientation.DOWN, Orientation.LEFT})
		);
		this.key=key;
		this.canMove=true;
	}
	
	
	

	@Override
	public boolean takeCellSpace() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCellInteractable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		// TODO Auto-generated method stub
		return true;
	}

	
	//Villager does not accept interactions
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}
	@Override
	public void update(float deltaTime) {
		if(canMove) {
			
			if(!this.isDisplacementOccurs() && inactionTime <=0) {
				moveOrientate();
				if(RandomGenerator.getInstance().nextDouble()<PROBABILITY_OF_INACTIVE)
					inactionTime=RandomGenerator.getInstance().nextInt(MAX_INACTION_TIME);
			}
			else {
				if(this.isDisplacementOccurs()) {
					currentAnimation.update(deltaTime);
				}
				else  {
					inactionTime-=deltaTime;
					currentAnimation.reset();
				}
			}
		}
		currentAnimation = idleAnimations[getOrientation().ordinal()];	
		super.update(deltaTime);
		
		
	}
	
	protected String getKey() {
		return key;
	}
	private void moveOrientate() {
		if(RandomGenerator.getInstance().nextDouble()<=0.9f) {
			move(ANIMATION_DURATION*4);
		}
		else {
			this.orientate( Orientation.fromInt(RandomGenerator.getInstance().nextInt(4)));
		}		
	}

	@Override
	public void draw(Canvas canvas) {
		currentAnimation.draw(canvas);
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}


}