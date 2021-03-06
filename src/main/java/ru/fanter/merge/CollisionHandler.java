package ru.fanter.merge;

import java.util.ArrayList;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.contacts.Contact;

import ru.fanter.merge.entities.Consumable;
import ru.fanter.merge.entities.Entity;
import ru.fanter.merge.entities.EntityType;
import ru.fanter.merge.entities.PlayerSphere;

public class CollisionHandler implements ContactListener {
	ArrayList<Contact> contactList = new ArrayList<Contact>();
	ArrayList<Entity> consumed = new ArrayList<Entity>();
	
	@Override
	public void beginContact(Contact contact) {
		Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();
		
		if (entityA.getType() == EntityType.PLAYER_SPHERE && entityB.getType() == EntityType.PLAYER_SPHERE
			|| entityA.getType() == EntityType.PLAYER_SPHERE && entityB.getType() == EntityType.LIFE_SPHERE
			|| entityA.getType() == EntityType.LIFE_SPHERE && entityB.getType() == EntityType.PLAYER_SPHERE) {
			contactList.add(contact);
		}
	}
	
	public void handleCollisions() {
		for (Contact contact : contactList) {
			Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
			Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();
			CircleShape csA = (CircleShape) contact.getFixtureA().getShape();
			CircleShape csB = (CircleShape) contact.getFixtureB().getShape();
			
			//it works because radius of life sphere is always smaller
			//than player radius. Can broke easily.
			if (entityA.getType() == EntityType.PLAYER_SPHERE && entityB.getType() == EntityType.PLAYER_SPHERE) {
				if (((PlayerSphere)entityA).isTeammate((PlayerSphere) entityB)) {
					continue;
				}
			}
			
			if (csA.m_radius > csB.m_radius) {
				((PlayerSphere)entityA).consume((Consumable) entityB);
				entityB.remove();
			} else if (csA.m_radius < csB.m_radius) {
				((PlayerSphere)entityB).consume((Consumable) entityA);
				entityA.remove();
			} 
		}
		contactList.clear();
	}
	
	@Override
	public void endContact(Contact contact) {
		
	}
	
	@Override
	public void postSolve(Contact contact, ContactImpulse cImp) {}
	
	@Override 
	public void preSolve(Contact contact, Manifold manifold) {}
}
