package sig.skills;

import sig.Craft;
import sig.Skill;

public class Observe extends Skill {
	public Observe(String name, int CPCost, boolean guaranteed, int lvReq, int modifier, int key) {
		super(name, CPCost, guaranteed, lvReq, modifier, key);
	}

	public Observe(String name, int CPCost, boolean guaranteed, int lvReq, int key) {
		super(name, CPCost, guaranteed, lvReq, key);
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		super.updateBuffs(c);
	}
}
