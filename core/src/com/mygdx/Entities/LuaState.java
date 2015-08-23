package com.mygdx.Entities;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mygdx.Script.LuaScript;

public class LuaState implements State<BoidEntity> {
	private LuaScript script;
	public LuaState(LuaScript script){
		this.script = script;
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
