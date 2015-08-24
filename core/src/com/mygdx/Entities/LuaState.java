package com.mygdx.Entities;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mygdx.Script.LuaScript;

public class LuaState implements State<BoidEntity> {
	private LuaScript script;
	private String name = "";
	
	public LuaState(LuaScript script){
		this.script = script;
		script.executeFunction("init", name);
		System.out.println("Script " + name + "initialisiert");

	}

	@Override
	public void enter(BoidEntity entity) {		
		
	}

	@Override
	public void update(BoidEntity entity) {
		script.executeFunction("update", entity);		
	}

	@Override
	public void exit(BoidEntity entity) {		
		script.executeFunction("exit", entity);
	}

	@Override
	public boolean onMessage(BoidEntity entity, Telegram telegram) {
		// TODO Auto-generated method stub
		return false;
	}

}
