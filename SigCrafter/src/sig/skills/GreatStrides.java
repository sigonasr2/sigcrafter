package sig.skills;

import sig.Buff;
import sig.Craft;
import sig.Skill;

public class GreatStrides extends Skill {
	public GreatStrides(String name, int CPCost, boolean guaranteed, int lvReq, int modifier, int key) {
		super(name, CPCost, guaranteed, lvReq, modifier, key);
	}

	public GreatStrides(String name, int CPCost, boolean guaranteed, int lvReq, int key) {
		super(name, CPCost, guaranteed, lvReq, key);
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		c.BuffList.put("Great Strides",new Buff("Great Strides",3));
		super.updateBuffs(c);
	}
}
