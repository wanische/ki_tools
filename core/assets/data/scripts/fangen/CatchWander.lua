
name = "Catch Wander"

function init()
end

function enter(entity)
	entity:addComponent(name)
end


-- UPDATE --
function update(entity) 
	
	if not entity:gotComponent(name) then
		entity:addComponent(name)
	end	
	
	local action = "Catch Evade"
	if entity.team:toString() == "RED" then
		action = "Catch Pursuit"
	end
	
	if entity:setTarget(entity:searchTarget(), action) then		
		entity:changeStateByName(action)
	end
	
	if entity:checkFuel() then
		entity:removeComponent("Seek")
	end
end


function exit(entity)
	entity:removeComponent(name)
end

function setName(inputName)
	return name
end
	
