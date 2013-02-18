package ru.fanter.merge.event;

import ru.fanter.mergel.entities.Entity;

public class EntityEvent {
	private Entity entity;
	private EventType eventType;
	
	public EntityEvent(Entity entity, EventType eventType) {
		this.entity = entity;
		this.eventType = eventType;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public enum EventType {
		DELETE, SPLIT;
	}
}
