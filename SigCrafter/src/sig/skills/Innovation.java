package sig.skills;

import sig.Buff;
import sig.Craft;
import sig.Skill;

public class Innovation extends Skill {
	public Innovation(String name, int CPCost, boolean guaranteed, int lvReq, int modifier, int key) {
		super(name, CPCost, guaranteed, lvReq, modifier, key);
	}

	public Innovation(String name, int CPCost, boolean guaranteed, int lvReq, int key) {
		super(name, CPCost, guaranteed, lvReq, key);
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		c.BuffList.put("Innovation",new Buff("Innovation",4));
	}
}
