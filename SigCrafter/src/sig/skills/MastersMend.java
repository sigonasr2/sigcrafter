package sig.skills;

import sig.Craft;
import sig.Skill;

public class MastersMend extends Skill {

	public MastersMend(String name, int CPCost, boolean guaranteed, int lvReq) {
		super(name, CPCost, guaranteed, lvReq);
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		c.craft_durability = Math.min(c.durability,c.craft_durability+30);
	}

}
