package ch.epfl.cs107.play.game.arpg.actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.FlyableEntity;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * FlameSkull class implements FlyabaleEntity
 *
 */
public class FlameSkull extends Monster implements FlyableEntity {
	
	
	private static List<AttackType> vulnerabilities = new ArrayList<AttackType>(Arrays.asList(
			AttackType.MAGIC,AttackType.PHYSICAL));
	private final int ANIMATION_DURATION=8;
	private final static float MAX_HEALTH=1;
	private final static float DAMAGE=0.1f;
	private FlameSkullHandler handler;
	private Animation currentAnimation;
	private Animation[] idleAnimations;
	private AttackType attack =AttackType.FIRE;
	private boolean isImmortal;
	
	private final float MIN_LIFE_TIME=1f;
	private final static float MAX_LIFE_TIME=4f;
	
	private float lifeTime;
	
	
	private class FlameSkullHandler implements ARPGInteractionVisitor{
		
		@Override
		public void interactWith(Monster monster) {
			monster.receiveAttack(attack,DAMAGE);
		}
		@Override
		public void interactWith(ARPGPlayer player) {
			player.addHealth(-DAMAGE);
		}
		@Override
		public void interactWith(Bomb bomb) {
			bomb.explode();
		}
		
		@Override
		public void interactWith(Grass grass) {
			grass.cut();
		}
		
		
	}
	
	
	
    /**
     * Default FlameSkull constructor
     * @param area (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param isImmortal (boolean): if true, then skull only dies by hits
     */
	public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates position,boolean isImmortal) {
		super(area, orientation, position, vulnerabilities,MAX_HEALTH);
		Sprite [][] sprites = RPGSprite.extractSprites("zelda/flameSkull",
				3, 2, 2,
				this , 32, 32,new Vector(-0.5f, -0.5f), new Orientation[] {Orientation.UP ,
				Orientation.LEFT , Orientation.DOWN, Orientation.RIGHT});
		idleAnimations=RPGSprite.createAnimations(ANIMATION_DURATION, sprites);
		handler= new FlameSkullHandler();
		
		
		lifeTime =MIN_LIFE_TIME+RandomGenerator.getInstance().nextFloat()*(MAX_LIFE_TIME-MIN_LIFE_TIME);
		this.isImmortal=isImmortal;
		vulnerabilities = new ArrayList<AttackType>(Arrays.asList(
				AttackType.MAGIC,AttackType.PHYSICAL));
		
	 	this.healthBar.setAnchor(new Vector(-1.f*this.getHealth()/MAX_HEALTH,1.5f));

	}
    /**
     * Default FlameSkull constructor
     * @param area (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
	 *Immortal is false by default
     */
	public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
		this(area, orientation,position,false);
	}





	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}
	
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		
		if(!isImmortal)
			lifeTime-=deltaTime;
			
		currentAnimation=idleAnimations[this.getOrientation().ordinal()];
		moveOrientate();
		if(isDisplacementOccurs()) {
			currentAnimation.update(deltaTime);
			
		}else {
			currentAnimation.reset();
		}
		if(lifeTime<=0) {
			receiveAttack(AttackType.PHYSICAL,1111);   // to fade out 
		}
		
		
	}
	@Override
	protected  void dropLoot() {
		Heart heart = new Heart(getOwnerArea(),getCurrentMainCellCoordinates()); 
		if(getOwnerArea().canEnterAreaCells(heart,getCurrentCells()))
			getOwnerArea().registerActor(heart);
	}
	/**random movement handler
	 * 
	 */
	private void moveOrientate() {
		if(RandomGenerator.getInstance().nextDouble()<=0.9f) {
			move(ANIMATION_DURATION);
		}
		else {
			this.orientate( Orientation.fromInt(RandomGenerator.getInstance().nextInt(4)));
		}		
	}
	
	
	@Override
	public boolean takeCellSpace() {
		return false;
	}


	@Override
	public boolean isCellInteractable() {
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
		
	}

	@Override
	public void draw(Canvas canvas) {
		if(isDead())
			super.draw(canvas);
		else {
			this.currentAnimation.draw(canvas);
		    healthBar.draw(canvas);
		}

	}





	@Override
	public boolean wantsCellInteraction() {
		return !isDead();
	}
	@Override
	public boolean wantsViewInteraction() {
		return false;
	}

}
