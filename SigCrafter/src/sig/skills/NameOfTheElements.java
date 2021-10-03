package sig.skills;

import sig.Buff;
import sig.Craft;
import sig.Skill;

public class NameOfTheElements extends Skill {
	public NameOfTheElements(String name, int CPCost, boolean guaranteed, int lvReq, int modifier, int key) {
		super(name, CPCost, guaranteed, lvReq, modifier, key);
	}

	public NameOfTheElements(String name, int CPCost, boolean guaranteed, int lvReq, int key) {
		super(name, CPCost, guaranteed, lvReq, key);
	}

	@Override
	public boolean canBeUsed(Craft c) {
		return super.canBeUsed(c)&&c.BuffList.get("Name of the Elements Has Been Used").stackCount==0;
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		c.BuffList.put("Name of the Elements",new Buff("Name of the Elements",3));
		c.BuffList.put("Name of the Elements Has Been Used",new Buff("Name of the Elements Has Been Used",1));
		super.updateBuffs(c);
	}
}
