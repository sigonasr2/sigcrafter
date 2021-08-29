package sig.skills;

import sig.Buff;
import sig.Craft;
import sig.Skill;

public class Innovation extends Skill {
	public Innovation(String name, int CPCost, boolean guaranteed, int lvReq) {
		super(name, CPCost, guaranteed, lvReq);
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		c.BuffList.put("Innovation",new Buff("Innovation",4));
	}
}
