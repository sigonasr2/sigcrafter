package sig.skills;

import sig.Buff;
import sig.Craft;
import sig.Skill;

public class Veneration extends Skill {
	public Veneration(String name, int CPCost, boolean guaranteed, int lvReq) {
		super(name, CPCost, guaranteed, lvReq);
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		c.BuffList.put("Veneration",new Buff("Veneration",4));
	}
}
