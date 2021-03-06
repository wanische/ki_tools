package com.mygdx.system;

import java.util.ArrayList;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.Entities.BoidEntity.Team;
import com.mygdx.Entities.PointOfInterestEntity;
import com.mygdx.components.BoidCenterComponent;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.RenderComponent;
import com.mygdx.components.RessourceComponent;
import com.mygdx.components.VelocityComponent;

public class RessourceSystem extends EntitySystem{
	
	private ImmutableArray<Entity> entities;
	private ImmutableArray<Entity> candidates;
	private ArrayList<PointOfInterestEntity> interestPoints = new ArrayList<PointOfInterestEntity>();
	private float elapsedTime = 0f;
	private float loseFuelTime = 1f;
	private int loseFuelAmount = 5;
	private int gainFuelAmount = 20;
	private int loseHealthAmount = 1;
	private boolean timeElapsed = false;
	private float collisionDistance = 25f;
	private ComponentMapper<RessourceComponent> rm = ComponentMapper.getFor(RessourceComponent.class);
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	
	@SuppressWarnings("unchecked")
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(RessourceComponent.class, BoidCenterComponent.class, PositionComponent.class, VelocityComponent.class).get());
		candidates = engine.getEntitiesFor(Family.all(RenderComponent.class, PositionComponent.class).get());
		//Finde Tankstellen oder HeilStationen
		for(Entity e : candidates){
			if(e.getClass() == PointOfInterestEntity.class){
				PointOfInterestEntity pie = (PointOfInterestEntity)e;
				if(pie.toString().contains("Tankstelle") || pie.toString().contains("Heilstation")) {
				    interestPoints.add(pie);
				}
				
			}
		}
		System.out.println("RessourceSystem added");
	}
	
	public void update(float deltaTime) {
		elapsedTime += deltaTime;
		timeElapsed = elapsedTime >= loseFuelTime;
		VelocityComponent velComp;
		
		for (int i = 0; i < entities.size(); ++i) {
			BoidEntity entity = (BoidEntity)entities.get(i);
			RessourceComponent rc = rm.get(entity);
			velComp = vm.get(entity);
			//Gewinne Sprit, falls an der Tanke, Leben falls an der Heilstation
			for(PointOfInterestEntity pie : interestPoints){
			    float distance = pm.get(pie).position.dst2(pm.get(entity).position);
			    
				if(pie.toString().contains("Tankstelle") && distance < 80){
					rc.fuel += gainFuelAmount;
					if(rc.fuel > 100)
						rc.fuel = 100;
				}
				
				if(pie.toString().contains("Heilstation") && distance < 80){
                    rc.health += 10;
                    if(rc.health > 100)
						rc.health = 100;
                }
			}
			
			//Verliere Sprit jede Zeiteinheit
			if(timeElapsed && rc.fuel > 0){				
				rc.fuel -= loseFuelAmount;
				
				//Cut the speed if no fuel
				if(rc.fuel > 0){
					velComp.maxSpeed = 3;
					
				}
				else{
					velComp.maxSpeed = 1f;
				}
			}
			
			//Verliere Leben, falls im grünen Team und Kontakt mit Gegner
			if(entity.team == Team.GREEN){
				Entity enemy = entity.searchTarget();
				
				if(enemy != null){
					PositionComponent pc = pm.get(entity);
					
					//Collision = Distanz < collisionDistance // dst2 für Performance
					if(pc.position.dst2(pm.get(enemy).position) < (collisionDistance * collisionDistance)){
						rc.health -= loseHealthAmount;
						entity.setHit(true);
						
						if(rc.health <= 0){
							entity.engine.removeEntity(entity);
						}
					}
				}
			}
		}
		
		if(timeElapsed){
			elapsedTime %= loseFuelTime;
			timeElapsed = false;
		}
	}
}
