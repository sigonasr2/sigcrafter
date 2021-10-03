package sig.skills;

import sig.Craft;
import sig.Skill;

public class MastersMend extends Skill {

	public MastersMend(String name, int CPCost, boolean guaranteed, int lvReq, int modifier, int key) {
		super(name, CPCost, guaranteed, lvReq, modifier, key);
	}

	public MastersMend(String name, int CPCost, boolean guaranteed, int lvReq, int key) {
		super(name, CPCost, guaranteed, lvReq, key);
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		c.craft_durability = Math.min(c.durability,c.craft_durability+30);
	}

}
