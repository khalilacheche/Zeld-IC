package ch.epfl.cs107.play.game.arpg.actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Darklord Class
 *
 */
public class DarkLord extends Monster {
	/**
	 * DarkLordState types
	 *
	 */
	private  enum DarkLordState{
		IDLE, 
		ATTACKING, 
		INVOKING, 
		INVOKINGTELEPORT, 
		TELEPORTING;
	}
	private static final float MAX_HEALTH = 3;
	private static final float PROBABILITY_OF_ATTACK = 0.4f; 
	private static final float PROBABILITY_OF_INACTIVE = 0.05f;
	private static final int ANIMATION_DURATION = 8;
	private static final int RADIUS=2;
	private static final int MIN_SPELL_WAIT_DURATION=50;
	private static final int MAX_SPELL_WAIT_DURATION=100;
	private static final int TELEPORTATION_RADIUS = 10 ;
	private static final int MAX_INACTION_TIME = 15;
	private static final List<AttackType> vulnerabilities = new ArrayList<AttackType>(Arrays.asList(
			AttackType.MAGIC));
	private final  int CYCLE; 
	private Animation[] idleAnimations; 
	private Animation[] attackAnimations;
	private Animation currentAnimation; 
	private DarkLordHandler handler ; 
	private int cycleCounter = 0;
	private float inactionTime;
	private DarkLordState state; 
	


	public DarkLord(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position, vulnerabilities,MAX_HEALTH);
		
		
		Sprite[][] sprites = RPGSprite.extractSprites("zelda/darkLord", 3, 2, 2, this, 32, 32, new Vector(-0.5f, 0.5f),new Orientation[] {Orientation.UP ,
				Orientation.LEFT , Orientation.DOWN, Orientation.RIGHT});
		idleAnimations = RPGSprite.createAnimations(ANIMATION_DURATION, sprites);
		
		
		
		sprites = RPGSprite.extractSprites("zelda/darkLord.spell", 3, 2,2 , this, 32, 32, new Vector(-0.5f, 0.5f),new Orientation[] {Orientation.UP ,
				Orientation.LEFT , Orientation.DOWN, Orientation.RIGHT});
		attackAnimations = RPGSprite.createAnimations(ANIMATION_DURATION/2, sprites,false);
		
		
		
		handler = new DarkLordHandler(); 
		CYCLE = RandomGenerator.getInstance().nextInt(MAX_SPELL_WAIT_DURATION-MIN_SPELL_WAIT_DURATION)+MIN_SPELL_WAIT_DURATION ;
		currentAnimation =setAnimation(); 
		inactionTime=0;
		state=DarkLordState.IDLE;
		
		this.healthBar.setAnchor(new Vector(-1.f,2.5f)); // fix health bar anchor

		
	}
	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		List<DiscreteCoordinates> coord = new ArrayList<>(); 
		for(int x=this.getCurrentMainCellCoordinates().x-RADIUS ; x<this.getCurrentMainCellCoordinates().x+RADIUS+1 ; ++x) {
			for(int y=this.getCurrentMainCellCoordinates().y-RADIUS ; y<this.getCurrentMainCellCoordinates().y+RADIUS+1 ; ++y) {
				if(this.getCurrentMainCellCoordinates().x==x && this.getCurrentMainCellCoordinates().y==y) 
					continue; //omit the maincellcoordinates it may cause a bug 
				coord.add(new DiscreteCoordinates(x,y));
			}
		}
		return coord ;
	}
	
	@Override
	public void update(float deltaTime) {
		++cycleCounter; 
		if(cycleCounter == CYCLE) {  // executed every cycle
			this.currentAnimation.reset();
			if(RandomGenerator.getInstance().nextDouble()>PROBABILITY_OF_ATTACK) {
				setState(DarkLordState.ATTACKING);
			}
			else {
			setState(DarkLordState.INVOKING);
			}
			orientateToOpenSpace();
			cycleCounter=0;
		}
		switch(state) {
		case IDLE :  
			if(!this.isDisplacementOccurs() && inactionTime <=0) {
				if(RandomGenerator.getInstance().nextDouble()<0.2) 
					moveOrientate(Orientation.fromInt(RandomGenerator.getInstance().nextInt(4)));
				else
		             moveOrientate(getOrientation());
				currentAnimation=idleAnimations[this.getOrientation().ordinal()];
				if(RandomGenerator.getInstance().nextDouble()<PROBABILITY_OF_INACTIVE)
					inactionTime=RandomGenerator.getInstance().nextInt(MAX_INACTION_TIME);		        
			}else {
				if(this.isDisplacementOccurs()) {
		        	currentAnimation.update(deltaTime);
		        }
		        else  {
		        	inactionTime-=deltaTime;
		        	currentAnimation.reset();
		        }
			}
			break; 
		case ATTACKING:
			if(!this.isDisplacementOccurs())
			{	FireSpell fire = new FireSpell(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates().jump(getOrientation().toVector()),4);
			    if(getOwnerArea().canEnterAreaCells(fire, Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()))))
			    	this.getOwnerArea().registerActor(fire);
				setState(DarkLordState.IDLE);
			}
			break; 
		case INVOKING:
			currentAnimation = setAnimation();
			if(currentAnimation.isCompleted()) {
	    	   currentAnimation.reset();
	    	   FlameSkull skull = new FlameSkull(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
			    if(getOwnerArea().canEnterAreaCells(skull, Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()))))
			    	this.getOwnerArea().registerActor(skull);
	    
	    	   setState(DarkLordState.IDLE);
	       }else {
	    	   currentAnimation.update(deltaTime);   
	       }
			break; 
		case INVOKINGTELEPORT :
			if(this.isDisplacementOccurs()) {		        	
		        this.currentAnimation.update(deltaTime);
			}else{   	
				this.currentAnimation.reset();
		        setState(DarkLordState.TELEPORTING);
		        currentAnimation = setAnimation();
		        	
	     	}
			break; 
		case TELEPORTING : 
			currentAnimation.update(deltaTime);
			if(currentAnimation.isCompleted()) {
				this.currentAnimation.reset();
				teleport();
				setState(DarkLordState.IDLE);
		 }
			break; 
		
		}
		
		
		
	   super.update(deltaTime);
			
	}
	
	
	@Override
	protected  void dropLoot() {
		
		getOwnerArea().registerActor(new CastleKey(getOwnerArea(),getCurrentMainCellCoordinates()));
	}
	
	/**teleport function of the darklord
	 * 
	 */
	private void teleport() {
		int k=0 ; 
		while(k<1000) {
			int x= RandomGenerator.getInstance().nextInt(TELEPORTATION_RADIUS)+2*RADIUS;   // x coordinate of the teleport destination (we add twice the radius to avoid being near the player(it doesnt work always ) but much more less
			int y= RandomGenerator.getInstance().nextInt(TELEPORTATION_RADIUS)+2*RADIUS;  // y coordinate of the teleport destination 
			if(this.getOwnerArea().leaveAreaCells(this,this.getCurrentCells()))		// leave current cells
			{
				if(this.getOwnerArea().enterAreaCells(this,Collections.singletonList(new DiscreteCoordinates(x,y)))) { // enter destination  cells
					this.setCurrentPosition(new Vector(x,y));
					
					break;
				}
				else {
					++k;
					continue;
				}
			}
			else {
				break;
				
			} 
			
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		if(isDead()) {
			super.draw(canvas);
		}
		else {
			this.currentAnimation.draw(canvas);
		    healthBar.draw(canvas);
		}

		
	}
	
	
	/**handles darklord movement
	 * @param orientation
	 */
	private void moveOrientate(Orientation orientation){
	    
	      
        if(getOrientation() == orientation) move(ANIMATION_DURATION);
        else orientate(orientation);
    
      }
	/**orientate to an orientation that lets the darklord summon thnigs
	 * 
	 */
	private void orientateToOpenSpace() {
		int j= 0 ; 
		while (j<8) {// getting a good orientation if the counter passes the conditon it stays the same		
			Orientation orientation = Orientation.fromInt(RandomGenerator.getInstance().nextInt(4));
			DiscreteCoordinates nextPlace =  getCurrentMainCellCoordinates().jump(orientation.toVector());
			if(this.getOwnerArea().canEnterAreaCells(new FireSpell(getOwnerArea(),orientation,nextPlace,5),Collections.singletonList(nextPlace))){
				this.orientate(orientation);
				break;
			}
			++j; 
					    
		}
		
	}

	private Animation setAnimation() {
		if(state == DarkLordState.IDLE) {
		for(Orientation direction : Orientation.values()) {
			int index = direction.ordinal();
			if(this.getOrientation().equals(direction) ) {
				return idleAnimations[index];
			}
				
		}
		return idleAnimations[0] ; 
		}
		else if(state == DarkLordState.TELEPORTING || state== DarkLordState.ATTACKING|| state == DarkLordState.INVOKING){
			for(Orientation direction : Orientation.values()) {
				int index = direction.ordinal();
				if(this.getOrientation().equals(direction) ) {
					return attackAnimations[index];
				}
					
			}
			return attackAnimations[0] ; 
		}
		return idleAnimations[0];
	}
	
	@Override
	public boolean takeCellSpace() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean wantsCellInteraction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean wantsViewInteraction() {
		// TODO Auto-generated method stub
		return !isDead();
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);		


	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor)v).interactWith(this);

	}
	
	/**Change the current state of the draklord
	 * @param state
	 */
	private void setState(DarkLordState state ) {
		this.state= state; 
	}
	
	/**
	 * interaction handler of the darklord
	 *
	 */
	private class DarkLordHandler implements ARPGInteractionVisitor {
		 @Override 
		 public void interactWith(ARPGPlayer player){
			 if(state == DarkLordState.TELEPORTING)
				 return; 
			 setState(DarkLordState.INVOKINGTELEPORT);
		 }	 

	}

}